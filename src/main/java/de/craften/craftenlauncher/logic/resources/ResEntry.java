package de.craften.craftenlauncher.logic.resources;

import java.io.File;

public class ResEntry {
    private String mName, mHash;
    private int mSize;
    private boolean mVirtual;

    public ResEntry() {
    }

    public String getName() {
        return mName;
    }

    public String getHash() {
        return mHash;
    }

    public int getSize() {
        return mSize;
    }

    public boolean isVirtual() {
        return mVirtual;
    }

    public void setName(String name) {
        this.mName = name.replace("/", File.separator);
    }

    public void setHash(String hash) {
        this.mHash = hash;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public void setVirtual(boolean virtual) {
        this.mVirtual = virtual;
    }

    public String getPath() {
        String fs = File.separator;

        if (mVirtual) {
            return "virtual" + fs + "legacy" + fs + getName();
        } else {
            return "objects" + fs + getHash().substring(0, 2) + fs + getHash();
        }
    }

    public String getDownloadPath() {
        return getHash().substring(0, 2) + "/" + getHash();
    }
}
