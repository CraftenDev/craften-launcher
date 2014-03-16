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
 * Class that represents a "OS"
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.resources;

public class Os {
    private String mName, mVersion;

    public Os() {
    }

    public Os(String name) {
        setName(name);
    }

    public Os(String name, String version) {
        setName(name);
        setVersion(version);
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getVersion() {
        return mVersion;
    }

    public String getName() {
        return mName;
    }

    public boolean hasVersion() {
        return getVersion() != null;
    }
}
