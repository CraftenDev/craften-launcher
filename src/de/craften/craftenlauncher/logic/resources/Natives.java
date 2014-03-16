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
 * Natives class for specific actions using the os
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.resources;

public class Natives {
    private String mLinux, mWindows, mOsx;

    public Natives(){

    }

    public Natives(String linux, String windows, String osx) {
        this.mLinux = linux;
        this.mWindows = windows;
        this.mOsx = osx;
    }

    public void setLinux(String linux) {
        this.mLinux = linux;
    }

    public void setWindows(String windows) {
        this.mWindows = windows;
    }

    public void setOsx(String osx) {
        this.mOsx = osx;
    }

    public String getLinux() {
        return mLinux;
    }

    public String getWindows() {
        return mWindows;
    }

    public String getOsx() {
        return mOsx;
    }
}
