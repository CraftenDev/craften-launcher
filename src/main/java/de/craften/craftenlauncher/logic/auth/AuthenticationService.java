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
    /**
     * Stores the current user
     */
    private MinecraftUser user;
    /**
     * Stores the current Minecraft path, used for saving the craftenlauncher_profiles
     */
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

    /**
     * Returns a SessionID from a given user.
     * Either it contains a refreshable AccessToken or it will create a new set of keys.
     * AccessToken + ProfileID is named SessionID
     * @param user the selected user from craftenlauncher_profiles or fresh created with password and email
     * @return a valid SessionID or NULL if something went wrong
     */
    public String getSessionID(MinecraftUser user) {
        if(user.hasAccessToken()){
            if (refresh(user)) {
                Logger.logInfo("Login with craftenlauncher_profiles successful");
                return user.getSession();
            } else {
                Logger.logError("Login failed");
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
                Logger.logInfo("SessionID created");
            } else {
                Logger.logError("Login failed");
            }
            return sessionID;
        }
    }

    /**
     * Extracts the Accesstoken from the returned login response
     * @param response Mojangs response to our login
     * @return a fresh Accesstoken
     */
    public String getAccessTokenFromResponse(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
        JsonObject jsonObject = (JsonObject) obj;

        return jsonObject.get("accessToken").getAsString();
    }

    /**
     * Extracts the ClientToken from the returned login response
     * @param response Mojangs response to our login
     * @return a fresh ClientToken
     */
    public String getClientTokenFromResponse(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
        JsonObject jsonObject = (JsonObject) obj;

        return jsonObject.get("clientToken").getAsString();
    }

    /**
     * Extracts the ProfileID from the returned login response
     * @param response Mojangs response to our login
     * @return a fresh ProfileID
     */
    private String getProfileIDFromResponse(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
        JsonObject jsonObject = (JsonObject) obj;

        JsonObject selectedProfile = jsonObject.get("selectedProfile").getAsJsonObject();
        return selectedProfile.get("id").getAsString();
    }

    /**
     * Extracts the Name from the returned login response
     * @param response Mojangs response to our login
     * @return a fresh Name
     */
    public String getName(String response) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(response);
        JsonObject jsonObject = (JsonObject) obj;

        JsonObject selectedProfile = jsonObject.get("selectedProfile").getAsJsonObject();
        return selectedProfile.get("name").getAsString();
    }

    /**
     * Connects to Mojangs server with given username and password
     * @param username Users username
     * @param password Users password
     * @return a response string with needed data if successful.
     */
    private String getSSID(String username, String password) {
        if(username != null &&username != "" && password != null && password != "") {

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();

            JsonObject jsonResult = new JsonObject(), jsonNameVersion = new JsonObject();

            jsonNameVersion.addProperty("name", "Minecraft");
            jsonNameVersion.addProperty("version", 1);
            jsonResult.add("agent", jsonNameVersion);
            jsonResult.addProperty("username", username);
            jsonResult.addProperty("password", password);

            return JSONConnector.executePost("https://authserver.mojang.com/authenticate", gson.toJson(jsonResult));
        }else{
            return null;
        }
    }

    /**
     * Checks if an accessToken is a valid session token with a currently-active session.
     * Note: this method will not respond successfully to all currently-logged-in sessions, just the most recently-logged-in for each user.
     * It is intended to be used by servers to validate that a user should be connecting
     * (and reject users who have logged in elsewhere since starting Minecraft), NOT to auth that a particular session token is valid for authentication purposes.
     * To authenticate a user by session token, use the refresh verb and catch resulting errors.
     * @param accessToken
     * @return
     */
    @Deprecated
    public static boolean isValid(String accessToken) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonObject jsonAccessToken = new JsonObject();
        jsonAccessToken.addProperty("accessToken", accessToken);

        String dummy = JSONConnector.executePost("https://authserver.mojang.com/validate", gson.toJson(jsonAccessToken));
        return dummy != null;
    }

    /**
     * Refreshes a user with Mojangs server.
     * A refresh is needed to check its validity
     * Sending Accesstoken and Clienttoken
     * @param user the user we want to refresh
     * @return true if new achieved Accesstoken is valid or false if something went wrong
     */
    private boolean refresh(MinecraftUser user) {
        if(user.getAccessToken() != null && user.getAccessToken() != "" && user.getClientToken() != null && user.getClientToken() != "") {

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            JsonObject jsonPayload = new JsonObject();

            jsonPayload.addProperty("accessToken", user.getAccessToken());
            jsonPayload.addProperty("clientToken", user.getClientToken());

            String response = JSONConnector.executePost("https://authserver.mojang.com/refresh", gson.toJson(jsonPayload));

            if (response == null || response.equals(""))
                return false;

            user.setAccessToken(getAccessTokenFromResponse(response));

            return true;
        }else{
            return false;
        }
    }

    /**
     * Invalidates a users' data
     * Needed to logout a user properly
     * @param user the user we want to logout
     */
    public void invalidate(MinecraftUser user) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonObject jsonPayload = new JsonObject();

        jsonPayload.addProperty("accessToken", user.getAccessToken());
        jsonPayload.addProperty("clientToken", user.getClientToken());

        String response = JSONConnector.executePost("https://authserver.mojang.com/invalidate", gson.toJson(jsonPayload));
    }

    /**
     * Set the minecraft path
     * @param mcPath the location where minecraft is located
     */
    public void setMcPath(MinecraftPath mcPath) {
        this.mcPath = mcPath;
    }

    /**
     * Get the user
     * @return
     */
    public MinecraftUser getUser() {
        return user;
    }

    /**
     * Reads the craftenlauncher_profiles
     * @return a Profiles object with read user/s
     */
    public Profiles readProfiles() {
        return JSONReader.readProfiles(mcPath.getMinecraftDir());
    }

    /**
     * Deletes the craftenlauncher_profiles file
     */
    public void deleteProfiles() {

        String path = mcPath.getMinecraftDir() + "craftenlauncher_profiles.json";

        Logger.logInfo("Trying to delete craftenlauncher_profiles! (At: " + path + " )");

        File craftenlauncher_profiles = new File(path);

        if (craftenlauncher_profiles.exists()) {
            try {
                if(craftenlauncher_profiles.delete()) {
                    Logger.logInfo("craftenlauncher_profiles at: " + path + " deleted!");
                } else {
                    Logger.logError("Could not delete craftenlauncher_profiles at: " + path);
                }
            } catch (Exception e) {
                Logger.logError("Could not delete craftenlauncher_profiles at: " + path);
            }
        }
    }
}
