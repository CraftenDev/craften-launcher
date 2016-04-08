package de.craften.craftenlauncher.logic.json;

import java.io.*;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import de.craften.craftenlauncher.logic.auth.Profiles;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.resources.Version;
import de.craften.util.OSHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JSONReader {
    private static final Logger LOGGER = LogManager.getLogger(JSONReader.class);

    public static Version readJsonFileFromSelectedVersion(String path) {
        Version version = new Version();
        JsonObject jsonObject = readJson(path);

        if (jsonObject != null) {
            version.setId(jsonObject.get("id").getAsString());
            LOGGER.debug("Version ID: " + version.getId());

            version.setTime(jsonObject.get("time").getAsString());
            LOGGER.debug("Version Time: " + version.getTime());

            version.setReleaseTime(jsonObject.get("releaseTime").getAsString());
            LOGGER.debug("Version Release Time: " + version.getReleaseTime());

            version.setType(jsonObject.get("type").getAsString());
            LOGGER.debug("Version type: " + version.getType());

            if (jsonObject.has("assets")) {
                version.setAssets(jsonObject.get("assets").getAsString());
                LOGGER.debug("Version Assets: " + version.getAssets());
            }

            version.setMinecraftArguments(jsonObject.get("minecraftArguments").getAsString());
            LOGGER.debug("Version Arguments" + version.getMinecraftArguments());

            version.setLibs(jsonObject.get("libraries").getAsJsonArray());

            version.setMainClass(jsonObject.get("mainClass").getAsString());
            LOGGER.debug("Version Main-Class: " + version.getMainClass());

            version.setMinimumLauncherVersion(jsonObject.get("minimumLauncherVersion").getAsInt());
            LOGGER.debug("Minimum Launcher Version: " + version.getMinimumLauncherVersion());
        }
        return version;
    }

    public static ArrayList<String> readVersions(String url) {
        ArrayList<String> versions = new ArrayList<String>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = new JsonObject();
        if (!url.isEmpty()) {
            String versionsJSON = DownloadHelper.downloadFileToString(url);
            Object obj = parser.parse(versionsJSON);

            jsonObject = (JsonObject) obj;
        }
        if (jsonObject != null) {
            if (jsonObject.has("versions")) {
                JsonArray jsonVersions = jsonObject.get("versions").getAsJsonArray();
                for (int i = 0; i < jsonVersions.size(); i++) {
                    versions.add(jsonVersions.get(i).getAsJsonObject().get("id").getAsString());
                }
            }
        }
        return versions;
    }

    //TODO Fehlerbehandlung auf Exceptions umbauen
    public static JsonObject readJson(String path) {
        LOGGER.info("Reading JSON-File: " + path);
        try (FileReader reader = new FileReader(path)) {
            return readJson(reader);
        } catch (JsonParseException e) {
            LOGGER.error("JReader->JsonParseException: " + path, e);
            return null;
        } catch (Exception e) {
            LOGGER.error("JReader->Exception: " + path, e);
            return null;
        }
    }

    public static JsonObject readJson(Reader inputStream) throws JsonParseException {
        JsonParser parser = new JsonParser();
        return parser.parse(inputStream).getAsJsonObject();
    }

    public static Profiles readProfiles(String minecraftDir) {
        String filename = "craftenlauncher_profiles.json";
        Profiles profiles = null;
        String path;

        JsonObject jsonObject;
        if (minecraftDir == null) {
            LOGGER.info("Reading craftenlauncher_profiles from: " + OSHelper.getMinecraftPath());
            path = OSHelper.getMinecraftPath();
            jsonObject = readJson(path + filename);
        } else {
            LOGGER.info("Reading craftenlauncher_profiles from: " + minecraftDir);

            if (minecraftDir.endsWith(File.separator)) {
                path = minecraftDir;
                jsonObject = readJson(path + filename);
            } else {
                path = minecraftDir + File.separator;
                jsonObject = readJson(path + filename);
            }
        }

        if (jsonObject != null) {
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
                for (int i = 0; i < jsonArray_availableUsers.size(); i++) {
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
