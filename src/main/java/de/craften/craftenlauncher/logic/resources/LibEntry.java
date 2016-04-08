package de.craften.craftenlauncher.logic.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.craften.util.OS;
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
        if (path.contains("\\"))
            splitter = path.split("\\\\");
        else
            splitter = path.split(File.separator);
        String result = splitter[splitter.length - 2] + "-" + splitter[splitter.length - 1];
        if (this.isNativ()) {
            if (OSHelper.getOSasEnum() == OS.WINDOWS)
                result += "-" + this.mNatives.getWindows();
            else if (OSHelper.getOSasEnum() == OS.LINUX)
                result += "-" + this.mNatives.getLinux();
            else if (OSHelper.getOSasEnum() == OS.OSX)
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
            for (Rules mRule : mRules) {
                if (mRule.getAction().equals("allow")) {
                    if (mRule.getOs() == null || OSHelper.getOSasString().equals(mRule.getOs().getName())) {
                        needed = true;
                    }
                } else if (mRule.getAction().equals("disallow")) {
                    if (mRule.getOs() != null && OSHelper.getOSasString().equals(mRule.getOs().getName())) {
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
