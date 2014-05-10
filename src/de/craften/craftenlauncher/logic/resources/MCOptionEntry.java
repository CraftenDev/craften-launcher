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
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.resources;

import java.util.regex.Pattern;

public class MCOptionEntry {
    private String mName;
    private String mValue;

    public MCOptionEntry(String name, String value) {
        this.mName = name;
        this.mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public String getClassFromValue() {
        mValue = mValue.toLowerCase();
        if (mValue.equals("false") || mValue.equals("true"))
            return "BOOLEAN";
        else if (mValue.contains(".")) {
            String[] dummy = mValue.split(Pattern.quote("."));
            try {
                Integer.parseInt(dummy[0]);
                Integer.parseInt(dummy[1]);
                return "DOUBLE";
            } catch (NumberFormatException e) {
            }
        } else {
            try {
                Integer.parseInt(mValue);
                return "INTEGER";
            } catch (NumberFormatException e) {
            }
        }
        return "STRING";
    }
}
