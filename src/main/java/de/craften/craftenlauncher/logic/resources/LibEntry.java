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
 * Declares one library entry and its attributes
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.resources;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.craften.util.OSHelper;

import java.io.File;

public class LibEntry {
    private String mName, mPath, mFilename, mUrl;
    private Rules[] mRules;
    private Natives mNatives;
    private boolean mExtractable;

    public LibEntry() {
    }

    public void setPath(String path) {
        this.mPath = path;
        String[] splitter;
                if(path.contains("\\"))
                    splitter = path.split("\\\\");
                else
                    splitter = path.split(File.separator);
        String result = splitter[splitter.length - 2] + "-" + splitter[splitter.length - 1];
        if (this.isNativ()) {
            if (OSHelper.getInstance().getOperatingSystem().equals("windows"))
                result += "-" + this.mNatives.getWindows();
            else if (OSHelper.getInstance().getOperatingSystem().equals("linux"))
                result += "-" + this.mNatives.getLinux();
            else if (OSHelper.getInstance().getOperatingSystem().equals("osx"))
                result += "-" + this.mNatives.getOsx();
        }
        setFilename(result + ".jar");
    }

    public void setFilename(String filename) {
        this.mFilename = filename;
    }

    public String getPath() {
        return mPath;
    }

    public String getFileName() {
        return mFilename;
    }

    public String getName() {
        return mName;
    }

    public Rules[] getRules() {
        return mRules;
    }

    public Natives getNatives() {
        return mNatives;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setName(String name) {
        this.mName = name;

        String[] dummy = name.split(":");
        String path = dummy[0].replace(".", File.separator);
        for (int i = 1; i < dummy.length; i++) {
            path += File.separator + dummy[i];
        }
        setPath(path);
    }

    public void setRules(JsonArray rules) {
        JsonObject[] rulesJsonObjects = new JsonObject[rules.size()];
        this.mRules = new Rules[rules.size()];

        for (int i = 0; i < rules.size(); i++) {
            rulesJsonObjects[i] = (JsonObject) rules.get(i);
            this.mRules[i] = new Rules(rulesJsonObjects[i].get("action").getAsString());
            if (rulesJsonObjects[i].has("os")) {

                JsonObject osJsonObject = (JsonObject) rulesJsonObjects[i].get("os");
                Os os = new Os();
                if (osJsonObject.has("version"))
                    os = new Os(osJsonObject.get("name").getAsString(), osJsonObject.get("version").getAsString());
                else
                    os = new Os(osJsonObject.get("name").getAsString());

                this.mRules[i].setOs(os);
            }
        }
    }

    public void setNatives(JsonObject natives) {
        this.mNatives = new Natives();
        if (natives.has("linux"))
            this.mNatives.setLinux(natives.get("linux").getAsString());
        if (natives.has("windows"))
            this.mNatives.setWindows(natives.get("windows").getAsString());
        if (natives.has("osx"))
            this.mNatives.setOsx(natives.get("osx").getAsString());
    }

    public void setExtractable(boolean value) {
        this.mExtractable = value;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public boolean isExtractable() {
        return mExtractable;
    }

    public boolean isNeeded() {
        Boolean needed = false;
        if (mRules == null)
            needed = true;
        else {
            OSHelper oshelper = OSHelper.getInstance();
            for (Rules mRule : mRules) {
                if (mRule.getAction().equals("allow")) {
                    if (mRule.getOs() == null || oshelper.getOperatingSystem().equals(mRule.getOs().getName())) {
                        needed = true;
                    }
                } else if (mRule.getAction().equals("disallow")) {
                    if (mRule.getOs() != null && oshelper.getOperatingSystem().equals(mRule.getOs().getName())) {
                        needed = false;
                    }
                }
            }
        }
        return needed;
    }

    public boolean isNativ() {
        return this.mNatives != null;
    }

    public boolean isExternal() {
        return mUrl != null && !mUrl.isEmpty();
    }
}
