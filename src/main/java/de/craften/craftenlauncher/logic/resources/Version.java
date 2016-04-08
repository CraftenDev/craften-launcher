package de.craften.craftenlauncher.logic.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Version {
    private String mId, mTime, mReleaseTime, mType, mAssets, mMinecraftArguments, mMainClass;
    private int mMinimumLauncherVersion;
    private Libraries mLibraries;

    public void setId(String id) {
        this.mId = id;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public void setReleaseTime(String releaseTime) {
        this.mReleaseTime = releaseTime;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setAssets(String assets) {
        this.mAssets = assets;
    }

    public void setMinecraftArguments(String minecraftArguments) {
        this.mMinecraftArguments = minecraftArguments;
    }

    public void setMainClass(String mainClass) {
        this.mMainClass = mainClass;
    }

    public void setMinimumLauncherVersion(int minimumLauncherVersion) {
        this.mMinimumLauncherVersion = minimumLauncherVersion;
    }

    public void setLibs(JsonArray libraries) {
        this.mLibraries = new Libraries();
        for (int i = 0; i < libraries.size(); i++) {
            LibEntry entry = new LibEntry();
            JsonObject jsonObject = (JsonObject) libraries.get(i);


            if (jsonObject.has("natives"))
                entry.setNatives(jsonObject.get("natives").getAsJsonObject());
            if (jsonObject.has("name"))
                entry.setName(jsonObject.get("name").getAsString());
            if (jsonObject.has("rules"))
                entry.setRules(jsonObject.get("rules").getAsJsonArray());
            if (jsonObject.has("extract"))
                entry.setExtractable(true);
            if (jsonObject.has("url"))
                entry.setUrl(jsonObject.get("url").getAsString());
            this.mLibraries.add(entry);
        }
    }

    public String getId() {
        return mId;
    }

    public String getTime() {
        return mTime;
    }

    public String getReleaseTime() {
        return mReleaseTime;
    }

    public String getType() {
        return mType;
    }

    public String getAssets() {
        return mAssets;
    }

    public String getMinecraftArguments() {
        return mMinecraftArguments;
    }

    public String getMainClass() {
        return mMainClass;
    }

    public Libraries getLibraries() {
        return mLibraries;
    }

    public int getMinimumLauncherVersion() {
        return mMinimumLauncherVersion;
    }

    public boolean hasAssets() {
        return mAssets != null;
    }
}
