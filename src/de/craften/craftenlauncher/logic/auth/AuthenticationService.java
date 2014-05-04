/**
 * CraftenLauncher is an alternative Launcher for Minecraft developed by Mojang.
 * Copyright (C) 2013  Johannes "redbeard" Busch, Sascha "saschb2b" Becker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Standard Authentication Service against Mojang.
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.auth;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.json.JSONReader;
import de.craften.craftenlauncher.logic.json.JSONWriter;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;

public class AuthenticationService {
    private String mResponse = "";
    private String mAccessToken = "";
    private String mClientToken = "";
    private String mProfileID = "";
    private boolean mValid = false;
    private LastLogin mLastLogin;
    private MinecraftPath mcPath;

    /**
     * Creates a new Authentication Service with the given MinecraftPath.
     * Path is uses to read the lastLogin.
     * @param path
     */
    public AuthenticationService(MinecraftPath path) {
        this.mcPath = path;
        mLastLogin = JSONReader.readLastLogin(mcPath.getMinecraftDir());
    }

    /**
     * Just for compatable purpose ;D.
     */
    public AuthenticationService() {

    }

    public String getResponse() {
        return mResponse;
    }

    public String getSessionID(String username, String password) {
        getSSID(username, password);
        String sessionID = null;
        if (this.mResponse != null && this.mResponse != "") {
            setClientTokenFromResponse(this.mResponse);
            setProfileIDFromRequest(this.mResponse);
            sessionID = "token:" + getAccessToken() + ":" + getProfileId();
            Logger.getInstance().logInfo("SessionID created");

            MinecraftUser user = new MinecraftUser(username, getProfileId(), getName(), getAccessToken(), getClientToken());

            LastLogin login = new LastLogin();
            login.setPath(mcPath.getMinecraftDir());
            login.setSelectedUser(user);
            if(login.getAvailableUsers() == null || login.getAvailableUsers().size() <= 0 || login.getAvailableUser(getProfileId()) != null)
                login.addAvailableUser(user);
            login.save();

            Logger.getInstance().logInfo("Saved showProfile to lastlogin.json");
        } else {
            Logger.getInstance().logError("Login failed");
        }
        return sessionID;
    }

    public String getSessionID(LastLogin login) {
        this.mValid = isValid(login.getSelectedUser().getAccessToken());
        String sessionID = null;
        if (this.mValid) {
            this.mLastLogin = login;
            setClientToken(this.mLastLogin.getSelectedUser().getClientToken());
            setAccessToken(this.mLastLogin.getSelectedUser().getAccessToken());
            setProfileID(this.mLastLogin.getSelectedUser().getProfileId());
            sessionID = "token:" + login.getSelectedUser().getAccessToken() + ":" + login.getSelectedUser().getProfileId();

            login.setPath(mcPath.getMinecraftDir());
            login.save();
            Logger.getInstance().logInfo("Login with LastLogin successful");
            return sessionID;
        } else {
            Logger.getInstance().logError("Login failed");
        }
        return sessionID;
    }

    public String getAccessToken() {
        if (this.mValid)
            return mLastLogin.getSelectedUser().getAccessToken();

        JsonParser parser = new JsonParser();
        Object obj = parser.parse(this.mResponse);
        JsonObject jsonObject = (JsonObject) obj;

        this.mAccessToken = jsonObject.get("accessToken").getAsString();
        return this.mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public String getClientToken() {
        if (this.mValid)
            return mLastLogin.getSelectedUser().getClientToken();

        return mClientToken;
    }

    public void setClientToken(String value) {
        this.mClientToken = value;
    }

    public void setClientTokenFromResponse(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
        JsonObject jsonObject = (JsonObject) obj;

        this.mClientToken = jsonObject.get("clientToken").getAsString();
    }

    private void setProfileIDFromRequest(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(this.mResponse);
        JsonObject jsonObject = (JsonObject) obj;

        JsonObject selectedProfile = jsonObject.get("selectedProfile").getAsJsonObject();
        this.mProfileID = selectedProfile.get("id").getAsString();
    }

    public void setProfileID(String profileID) {
        this.mProfileID = profileID;
    }

    public String getProfileId() {
        return mProfileID;
    }

    public String getName() {
        if (this.mValid)
            return mLastLogin.getSelectedUser().getUsername();

        JsonParser parser = new JsonParser();
        Object obj = parser.parse(this.mResponse);
        JsonObject jsonObject = (JsonObject) obj;

        JsonObject selectedProfile = jsonObject.get("selectedProfile").getAsJsonObject();
        String name = selectedProfile.get("name").getAsString();
        return name;
    }

    private String getSSID(String username, String password) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        JsonObject jsonResult = new JsonObject(), jsonNameVersion = new JsonObject();

        jsonNameVersion.addProperty("name", "Minecraft");
        jsonNameVersion.addProperty("version", 1);
        jsonResult.add("agent", jsonNameVersion);
        jsonResult.addProperty("username", username);
        jsonResult.addProperty("password", password);

        this.mResponse = executePost("https://authserver.mojang.com/authenticate", gson.toJson(jsonResult));

        return this.mResponse;
    }

    public static boolean isValid(String accessToken) {
        JsonObject jsonAccessToken = new JsonObject();
        jsonAccessToken.addProperty("accessToken", accessToken);

        String dummy = executePost("https://authserver.mojang.com/validate", jsonAccessToken.toString());
        return dummy != null;
    }

    public static String executePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            byte[] bytes = urlParameters.getBytes("UTF-8");
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(bytes.length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            System.out.println(connection.getURL());
            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.write(bytes);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

        	Logger.getInstance().logError("AuthSer->executePost error: " + e.getMessage());
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String genUUID() {
        return UUID.randomUUID().toString();
    }

    public void setMcPath(MinecraftPath mcPath) {
        this.mcPath = mcPath;
    }

    public LastLogin readLastLogin() {
        return JSONReader.readLastLogin(mcPath.getMinecraftDir());
    }

    public void deleteLastLogin() {

        String path = mcPath.getMinecraftDir() + "lastLogin.json";

        Logger.getInstance().logInfo("Trying to delete lastLogin! (At: " + path + " )");

        File lastLogin = new File(path);

        if (lastLogin.exists()) {
            try {
                if(lastLogin.delete()) {
                    Logger.getInstance().logInfo("LastLogin at: " + path + " deleted!");
                } else {
                    Logger.getInstance().logError("Could not delete LastLogin at: " + path);
                }
            } catch (Exception e) {
                Logger.getInstance().logError("Could not delete LastLogin at: " + path);
            }
        }
    }

    /**
     * Returns a list of users from the current lastLogin.
     * @return
     */
    public List<MinecraftUser> getUsers() {
        return mLastLogin.getAvailableUsers();
    }
}
