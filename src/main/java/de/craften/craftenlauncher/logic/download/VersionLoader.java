package de.craften.craftenlauncher.logic.download;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class VersionLoader {
    public static List<String> getVersionStringList() {
        String jsonString = DownloadHelper.downloadFileToString("https://s3.amazonaws.com/Minecraft.Download/versions/versions.json");
        JsonObject versionsJson = new Gson().fromJson(jsonString, JsonObject.class);

        List<String> versions = new ArrayList<>();
        for (JsonElement version : versionsJson.getAsJsonArray("versions")) {
            versions.add(version.getAsJsonObject().get("id").getAsString());
        }
        return versions;
    }
}
