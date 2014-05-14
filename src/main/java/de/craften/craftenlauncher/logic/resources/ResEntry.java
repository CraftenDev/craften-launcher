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
 * @author saschb2b
 */
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

    public String getPath(){
        String fs = File.separator;

        if(mVirtual) {
            return "virtual" + fs + "legacy" + fs + getName();
        }
        else {
            return "objects" + fs + getHash().substring(0,2) + fs + getHash();
        }
    }

    public String getDownloadPath(){
        return getHash().substring(0,2) + "/" + getHash();
    }
}
