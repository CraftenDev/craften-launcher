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

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MCOptions {
    private static MCOptions instance;
    private ArrayList<MCOptionEntry> mContent;

    private MCOptions() {
    }

    public synchronized static MCOptions getInstance() {
        if (instance == null) {
            instance = new MCOptions();
        }
        return instance;
    }

    public void read(String path) {
        mContent = new ArrayList<MCOptionEntry>();

        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null && line.contains(":")) {
                String[] splitted = line.split(Pattern.quote(":"));
                MCOptionEntry entry = new MCOptionEntry(splitted[0], splitted[1]);
                mContent.add(entry);
            }
            
            br.close();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }

    public void setValue(String name, String value) {
        for (int i = 0; i < mContent.size(); i++) {
            if (mContent.get(i).getName().equals(name))
                mContent.get(i).setValue(value);
        }
    }

    public String getValue(String name) {
        for (int i = 0; i < mContent.size(); i++) {
            if (mContent.get(i).getName().equals(name))
                return mContent.get(i).getValue();
        }
        return null;
    }
}
