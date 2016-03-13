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

import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;
import de.craften.craftenlauncher.logic.resources.LibEntry;
import de.craften.craftenlauncher.logic.resources.Libraries;

import java.io.File;
import java.util.ArrayList;

public class VersionHelper {
    public static ArrayList<String> getLibFilePathsAsArray(MinecraftPath info, Libraries libraries) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<LibEntry> entries = libraries.get();
        for (LibEntry entry : entries) {
            File folder = new File(info.getLibraryDir() + File.separator + entry.getPath() + File.separator);
            File[] listAllFiles = folder.listFiles();
            if (listAllFiles != null) {
                for (File listAllFile : listAllFiles) {
                    list.add(listAllFile.getAbsolutePath());
                }
            }
        }
        return list;
    }

    public static ArrayList<String> getLibPathsAsArray(MinecraftPath info, Libraries libraries) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<LibEntry> entries = libraries.get();
        for (LibEntry entry : entries) {
            File folder = new File(info.getLibraryDir() + File.separator + entry.getPath());
            list.add(folder.getAbsolutePath());
        }
        return list;
    }

    public static String getLibFilessAsArgmument(MinecraftPath info, Libraries libraries) {
        String libPath = info.getLibraryDir(),
                argmument = "",
                libSep = File.pathSeparator;
        ArrayList<LibEntry> libEntries = libraries.get();

        for (LibEntry libEntry : libEntries) {
            if (libEntry.isNeeded() && !libEntry.isNativ()) {
                argmument += libPath + libEntry.getPath() + File.separator + libEntry.getFileName() + libSep;
            }
        }

        return argmument;
    }
}
