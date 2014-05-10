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
import de.craften.craftenlauncher.logic.json.JSONConnector;
import de.craften.craftenlauncher.logic.json.JSONReader;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;

public class AuthenticationService {
    private String mResponse = "";
    private String mAccessToken = "";
    private String mClientToken = "";
    private String mProfileID = "";
    private boolean mValid = false;
    private Profiles mProfiles;
    private MinecraftPath mcPath;

    /**
     * Creates a new Authentication Service with the given MinecraftPath.
     * Path is uses to read the craftenlauncher_profiles.
     * @param path
     */
    public AuthenticationService(MinecraftPath path) {
        this.mcPath = path;
        mProfiles = JSONReader.readProfiles(mcPath.getMinecraftDir());
    }

    /**
     * Just for compatable purpose ;D.
     */
    public AuthenticationService() {
        mProfiles = new Profiles();
    }

    public String getResponse() {
        return mResponse;
    }

    public String getSessionID(String email, String password) {
        getSSID(email, password);
        String sessionID = null;
        if (this.mResponse != null && !this.mResponse.equals("")) {
            setClientTokenFromResponse(this.mResponse);
            setProfileIDFromRequest();
            sessionID = "token:" + getAccessToken() + ":" + getProfileId();
            Logger.getInstance().logInfo("SessionID created");

            MinecraftUser user = new MinecraftUser(email, getProfileId(), getName(), getAccessToken(), getClientToken());

            mProfiles.setPath(mcPath.getMinecraftDir());
            mProfiles.setSelectedUser(user);
            if(!(mProfiles.getAvailableUsers() == null || mProfiles.getAvailableUsers().size() <= 0 || mProfiles.getAvailableUser(getProfileId()) == null))
                mProfiles.addAvailableUser(user);
            mProfiles.save();

            Logger.getInstance().logInfo("Saved showProfile to craftenlauncher_profiles.json");
        } else {
            Logger.getInstance().logError("Login failed");
        }
        return sessionID;
    }

    public String getSessionID(Profiles login) {
        this.mValid = isValid(login.getSelectedUser().getAccessToken());
        if (this.mValid) {
            String sessionID;
            this.mProfiles = login;
            setClientToken(this.mProfiles.getSelectedUser().getClientToken());
            setAccessToken(this.mProfiles.getSelectedUser().getAccessToken());
            setProfileID(this.mProfiles.getSelectedUser().getProfileId());
            sessionID = "token:" + login.getSelectedUser().getAccessToken() + ":" + login.getSelectedUser().getProfileId();

            login.setPath(mcPath.getMinecraftDir());
            login.save();
            Logger.getInstance().logInfo("Login with craftenlauncher_profiles successful");
            return sessionID;
        } else {
            Logger.getInstance().logError("Login failed");
        }
        return null;
    }

    public String getAccessToken() {
        if (this.mValid)
            return mProfiles.getSelectedUser().getAccessToken();

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
            return mProfiles.getSelectedUser().getClientToken();

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

    private void setProfileIDFromRequest() {
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
            return mProfiles.getSelectedUser().getUsername();

        JsonParser parser = new JsonParser();
        Object obj = parser.parse(this.mResponse);
        JsonObject jsonObject = (JsonObject) obj;

        JsonObject selectedProfile = jsonObject.get("selectedProfile").getAsJsonObject();
        return selectedProfile.get("name").getAsString();
    }

    private String getSSID(String username, String password) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        JsonObject jsonResult = new JsonObject(), jsonNameVersion = new JsonObject();

        jsonNameVersion.addProperty("name", "Minecraft");
        jsonNameVersion.addProperty("version", 1);
        jsonResult.add("agent", jsonNameVersion);
        jsonResult.addProperty("username", username);
        jsonResult.addProperty("password", password);

        this.mResponse = JSONConnector.executePost("https://authserver.mojang.com/authenticate", gson.toJson(jsonResult));

        return this.mResponse;
    }

    public static boolean isValid(String accessToken) {
        JsonObject jsonAccessToken = new JsonObject();
        jsonAccessToken.addProperty("accessToken", accessToken);

        String dummy = JSONConnector.executePost("https://authserver.mojang.com/validate", jsonAccessToken.toString());
        return dummy != null;
    }

    public String genUUID() {
        return UUID.randomUUID().toString();
    }

    public void setMcPath(MinecraftPath mcPath) {
        this.mcPath = mcPath;
    }

    public Profiles readProfiles() {
        return JSONReader.readProfiles(mcPath.getMinecraftDir());
    }

    public void deleteProfiles() {

        String path = mcPath.getMinecraftDir() + "craftenlauncher_profiles.json";

        Logger.getInstance().logInfo("Trying to delete craftenlauncher_profiles! (At: " + path + " )");

        File craftenlauncher_profiles = new File(path);

        if (craftenlauncher_profiles.exists()) {
            try {
                if(craftenlauncher_profiles.delete()) {
                    Logger.getInstance().logInfo("craftenlauncher_profiles at: " + path + " deleted!");
                } else {
                    Logger.getInstance().logError("Could not delete craftenlauncher_profiles at: " + path);
                }
            } catch (Exception e) {
                Logger.getInstance().logError("Could not delete craftenlauncher_profiles at: " + path);
            }
        }
    }

    /**
     * Returns a list of users from the current craftenlauncher_profiles.
     * @return
     */
    public List<MinecraftUser> getUsers() {
        return mProfiles.getAvailableUsers();
    }
}
