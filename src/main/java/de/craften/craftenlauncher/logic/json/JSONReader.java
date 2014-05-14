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
 * Represents a JSONReader class
 * Currently only used for a minecraft version json file
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.auth.Profiles;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.resources.Version;
import de.craften.util.OSHelper;

public class JSONReader {
    public static Version readJsonFileFromSelectedVersion(String path) {
        Version version = new Version();

        JsonObject jsonObject = readJson(path);

        if(jsonObject != null){
            version.setId(jsonObject.get("id").getAsString());
            Logger.getInstance().logDebug("Version ID: " + version.getId());

            version.setTime(jsonObject.get("time").getAsString());
            Logger.getInstance().logDebug("Version Time: " + version.getTime());

            version.setReleaseTime(jsonObject.get("releaseTime").getAsString());
            Logger.getInstance().logDebug("Version Release Time: " + version.getReleaseTime());

            version.setType(jsonObject.get("type").getAsString());
            Logger.getInstance().logDebug("Version type: " + version.getType());

            if(jsonObject.has("assets")){
                version.setAssets(jsonObject.get("assets").getAsString());
                Logger.getInstance().logDebug("Version Assets: " + version.getAssets());
            }

            version.setMinecraftArguments(jsonObject.get("minecraftArguments").getAsString());
            Logger.getInstance().logDebug("Version Arguments" + version.getMinecraftArguments());

            version.setLibs(jsonObject.get("libraries").getAsJsonArray());

            version.setMainClass(jsonObject.get("mainClass").getAsString());
            Logger.getInstance().logDebug("Version Main-Class: " + version.getMainClass());

            version.setMinimumLauncherVersion(jsonObject.get("minimumLauncherVersion").getAsInt());
            Logger.getInstance().logDebug("Minimum Launcher Version: " + version.getMinimumLauncherVersion());
        }
        return version;
    }

    public static ArrayList<String> readVersions(String url){
        ArrayList<String> versions = new ArrayList<String>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = new JsonObject();
        if(!url.isEmpty()){
            String versionsJSON = DownloadHelper.downloadFileToString(url);
            Object obj = parser.parse(versionsJSON);

            jsonObject = (JsonObject) obj;
        }
        if(jsonObject != null){
            if(jsonObject.has("versions")){
                JsonArray jsonVersions = jsonObject.get("versions").getAsJsonArray();
                for (int i = 0; i < jsonVersions.size(); i++){
                    versions.add(jsonVersions.get(i).getAsJsonObject().get("id").getAsString());
                }
            }
        }
        return versions;
    }
    
    //TODO Fehlerbehandlung auf Exceptions umbauen
    public static JsonObject readJson(String path){
        JsonParser parser = new JsonParser();
        JsonObject jsonObject;

        try {
            Logger.getInstance().logInfo("Reading JSON-File: " + path);
            FileReader reader = new FileReader(path);
            Object obj = parser.parse(reader);

            jsonObject = (JsonObject) obj;

            reader.close();
        } catch (FileNotFoundException e) {
            Logger.getInstance().logError("JReader->Not Found: " + path);
            return null;
        } catch (JsonParseException e) {
            Logger.getInstance().logError("JReader->JsonParseException: " + path);
            return null;
        }catch (Exception e){
            Logger.getInstance().logError("JReader->Exception: " + e.getMessage() + " while reading " + path);
            return null;
        }
        return jsonObject;
    }
    



    public static Profiles readProfiles(String minecraftDir){
        String filename = "craftenlauncher_profiles.json";
        Profiles profiles = null;
        String path;

        JsonObject jsonObject;
        if(minecraftDir == null){
        	Logger.getInstance().logInfo("Reading craftenlauncher_profiles from: " + OSHelper.getInstance().getMinecraftPath());
            path = OSHelper.getInstance().getMinecraftPath();
            jsonObject = readJson(path + filename);
        }
        else{
        	Logger.getInstance().logInfo("Reading craftenlauncher_profiles from: " + minecraftDir);
        	
            if(minecraftDir.endsWith(File.separator) ) {
                path = minecraftDir;
                jsonObject = readJson(path + filename);
            }
            else {
                path = minecraftDir + File.separator;
                jsonObject = readJson(path + filename);
            }
        }

        if(jsonObject != null){
            profiles = new Profiles();
            profiles.setPath(path);

            if (jsonObject.has("selectedUser")) {
                JsonObject json_selectedUser = jsonObject.get("selectedUser").getAsJsonObject();

                MinecraftUser user = new MinecraftUser();

                user.setEmail(json_selectedUser.get("email").getAsString());
                user.setProfileId(json_selectedUser.get("profileid").getAsString());
                user.setUsername(json_selectedUser.get("username").getAsString());
                user.setAccessToken(json_selectedUser.get("accesstoken").getAsString());
                user.setClientToken(json_selectedUser.get("clienttoken").getAsString());

                profiles.setSelectedUser(user);
            }

            if (jsonObject.has("availableUsers")) {
                profiles.clearAvailableUsers();

                JsonArray jsonArray_availableUsers = jsonObject.get("availableUsers").getAsJsonArray();
                for (int i = 0; i < jsonArray_availableUsers.size(); i++){
                    JsonObject json_availableUsers = jsonArray_availableUsers.get(i).getAsJsonObject();

                    MinecraftUser user = new MinecraftUser();

                    user.setEmail(json_availableUsers.getAsJsonObject().get("email").getAsString());
                    user.setProfileId(json_availableUsers.getAsJsonObject().get("profileid").getAsString());
                    user.setUsername(json_availableUsers.getAsJsonObject().get("username").getAsString());
                    user.setAccessToken(json_availableUsers.getAsJsonObject().get("accesstoken").getAsString());
                    user.setClientToken(json_availableUsers.getAsJsonObject().get("clienttoken").getAsString());

                    profiles.addAvailableUser(user);
                }
            }
        }
        return profiles;
    }
}
