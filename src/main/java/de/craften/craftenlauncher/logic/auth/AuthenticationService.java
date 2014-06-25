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

import java.io.File;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.json.JSONConnector;
import de.craften.craftenlauncher.logic.json.JSONReader;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;

public class AuthenticationService {
    private MinecraftUser user;
    private MinecraftPath mcPath;

    /**
     * Creates a new Authentication Service with the given MinecraftPath.
     * Path is uses to read the craftenlauncher_profiles.
     * @param path
     */
    public AuthenticationService(MinecraftPath path) {
        user = new MinecraftUser();
        this.mcPath = path;
    }

    /**
     * Just for compatable purpose ;D.
     */
    public AuthenticationService() {
        user = new MinecraftUser();
    }

    public String getSessionID(MinecraftUser user) {
        if(user.hasAccessToken()){
            if (refresh(user)) {
                Logger.getInstance().logInfo("Login with craftenlauncher_profiles successful");
                return user.getSession();
            } else {
                Logger.getInstance().logError("Login failed");
            }
            return null;
        }
        else {
            String response = getSSID(user.getEmail(), user.getPassword());
            String sessionID = null;
            if (response != null && !response.equals("")) {
                this.user = new MinecraftUser(user.getEmail(), getProfileIDFromResponse(response),getName(response),getAccessTokenFromResponse(response),getClientTokenFromResponse(response));
                this.user.setResponse(response);

                sessionID = "token:" + user.getAccessToken() + ":" + user.getProfileId();
                Logger.getInstance().logInfo("SessionID created");
            } else {
                Logger.getInstance().logError("Login failed");
            }
            return sessionID;
        }
    }

    public String getAccessTokenFromResponse(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
        JsonObject jsonObject = (JsonObject) obj;

        return jsonObject.get("accessToken").getAsString();
    }

    public String getClientTokenFromResponse(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
        JsonObject jsonObject = (JsonObject) obj;

        return jsonObject.get("clientToken").getAsString();
    }

    private String getProfileIDFromResponse(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
        JsonObject jsonObject = (JsonObject) obj;

        JsonObject selectedProfile = jsonObject.get("selectedProfile").getAsJsonObject();
        return selectedProfile.get("id").getAsString();
    }

    public String getName(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
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

        return JSONConnector.executePost("https://authserver.mojang.com/authenticate", gson.toJson(jsonResult));
    }

    /*
    Checks if an accessToken is a valid session token with a currently-active session.
    Note: this method will not respond successfully to all currently-logged-in sessions, just the most recently-logged-in for each user.
    It is intended to be used by servers to validate that a user should be connecting
    (and reject users who have logged in elsewhere since starting Minecraft), NOT to auth that a particular session token is valid for authentication purposes.
    To authenticate a user by session token, use the refresh verb and catch resulting errors.
     */
    @Deprecated
    public static boolean isValid(String accessToken) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonObject jsonAccessToken = new JsonObject();
        jsonAccessToken.addProperty("accessToken", accessToken);

        String dummy = JSONConnector.executePost("https://authserver.mojang.com/validate", gson.toJson(jsonAccessToken));
        return dummy != null;
    }

    private boolean refresh(MinecraftUser user) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonObject jsonPayload = new JsonObject();

        jsonPayload.addProperty("accessToken", user.getAccessToken());
        jsonPayload.addProperty("clientToken", user.getClientToken());

        String response = JSONConnector.executePost("https://authserver.mojang.com/refresh", gson.toJson(jsonPayload));

        if(response == null || response.equals(""))
            return false;

        user.setAccessToken(getAccessTokenFromResponse(response));

        return true;
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

    public MinecraftUser getUser() {
        return user;
    }
}
