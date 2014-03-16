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
import de.craften.craftenlauncher.logic.auth.LastLogin;
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
    
    
    public static LastLogin readLastLogin(String minecraftDir){
        LastLogin lastLogin = null;

        JsonObject jsonObject;
        if(minecraftDir == null){
        	Logger.getInstance().logInfo("Reading lastLogin from: " + OSHelper.getInstance().getMinecraftPath());
            jsonObject = readJson(OSHelper.getInstance().getMinecraftPath()+"lastLogin.json");
        }
        else{
        	Logger.getInstance().logInfo("Reading lastLogin from: " + minecraftDir);
        	
            if(minecraftDir.endsWith(File.separator) ) {
                jsonObject = readJson(minecraftDir + "lastLogin.json");
            }
            else {
                jsonObject = readJson(minecraftDir + File.separator + "lastLogin.json");
            }
        }

        if(jsonObject != null){
            lastLogin = new LastLogin(minecraftDir);

            if(jsonObject.has("username")){
                lastLogin.setUsername(jsonObject.get("username").getAsString());
            }

            if(jsonObject.has("accesstoken")){
                lastLogin.setAccessToken(jsonObject.get("accesstoken").getAsString());
            }

            if(jsonObject.has("profileid")){
                lastLogin.setProfileID(jsonObject.get("profileid").getAsString());
            }

            if(jsonObject.has("clienttoken")){
                lastLogin.setClientToken(jsonObject.get("clienttoken").getAsString());
            }

           if(jsonObject.has("email")){
                lastLogin.setEmail(jsonObject.get("email").getAsString());
           }
        }
        return lastLogin;
    }
}
