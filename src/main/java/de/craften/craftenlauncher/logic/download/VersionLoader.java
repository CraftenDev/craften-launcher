package de.craften.craftenlauncher.logic.download;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class VersionLoader {
    private static final Logger LOGGER = LogManager.getLogger(VersionLoader.class);

    public static List<String> getVersionStringList() {
        String jsonString = DownloadHelper.downloadFileToString("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        JsonObject versionsJson = new Gson().fromJson(jsonString, JsonObject.class);

        if (versionsJson != null) {
            List<String> versions = new ArrayList<>();
            for (JsonElement version : versionsJson.getAsJsonArray("versions")) {
                versions.add(version.getAsJsonObject().get("id").getAsString());
            }
            return versions;
        } else {
            LOGGER.warn("Downloading versions list failed, using local versions only");
            return new ArrayList<>(); //TODO cache the versions or throw an exception
        }
    }
}
