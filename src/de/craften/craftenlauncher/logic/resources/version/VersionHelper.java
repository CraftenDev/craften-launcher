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
 * VersionHelper class provides methods for getting needed information from a minecraft version
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.resources.version;

import java.io.File;
import java.util.ArrayList;

import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;
import de.craften.craftenlauncher.logic.resources.LibEntry;
import de.craften.craftenlauncher.logic.resources.Libraries;

public class VersionHelper {
    public static ArrayList<String> getLibFilePathsAsArray(MinecraftPath info, Libraries libraries) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<LibEntry> entries = libraries.get();
        for (int i = 0; i < entries.size(); i++) {
            File folder = new File(info.getLibraryDir() + File.separator + entries.get(i).getPath() + File.separator);
            File[] listAllFiles = folder.listFiles();
            if (listAllFiles != null)
                for (int j = 0; j < listAllFiles.length; j++) {
                    list.add(listAllFiles[j].getAbsolutePath());
                }
        }
        return list;
    }

    public static ArrayList<String> getLibPathsAsArray(MinecraftPath info, Libraries libraries) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<LibEntry> entries = libraries.get();
        for (int i = 0; i < entries.size(); i++) {
            File folder = new File(info.getLibraryDir() + File.separator + entries.get(i).getPath());
            list.add(folder.getAbsolutePath());
        }
        return list;
    }

    public static String getLibFilessAsArgmument(MinecraftPath info, Libraries libraries) {
        String libPath = info.getLibraryDir(),
                argmument = "";
        ArrayList<LibEntry> libEntries = libraries.get();

        for (int i = 0; i < libEntries.size(); i++) {
            if (libEntries.get(i).isNeeded() && !libEntries.get(i).isNativ()) {
                argmument += libPath + libEntries.get(i).getPath() + File.separator + libEntries.get(i).getFileName() + ";";
            }
        }

        return argmument;
    }
}
