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
 * Rules class declares how things are handled
 * Action is either "allow" or "disallow"
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.resources;

public class Rules {
    private String mAction;
    private Os mOs;

    public Rules() {
    }

    public Rules(String action) {
        setAction(action);
    }

    public Rules(String action, Os os) {
        setAction(action);
        setOs(os);
    }

    public String getAction() {
        return mAction;
    }

    public Os getOs() {
        return mOs;
    }

    public void setAction(String action) {
        this.mAction = action;
    }

    public void setOs(Os os) {
        this.mOs = os;
    }
}
