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
 * Represents a minecraft version from a json file
 *
 * @author saschb2b
 */
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
