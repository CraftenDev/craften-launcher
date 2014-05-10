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
package de.craften.craftenlauncher.logic.download;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.resources.LibEntry;

public class FileNameHelper {
    private static String[] mOsArch32 = {"x86", "i386", "i686"}, //32-bit
            mOsArch64 = {"x64", "ia64", "amd64"};                //64-bit

    public static boolean hasVariables(LibEntry entry) {
        return entry.getFileName().contains("$");
    }

    public static void replaceVariables(LibEntry entry) {
        if (entry.getFileName().contains("${arch}")) {
            String archInfo = System.getProperty("os.arch");
            if (archInfo != null && !archInfo.equals("")) {
                for (String aMOsArch32 : mOsArch32) {
                    if (archInfo.equals(aMOsArch32)) {
                        entry.setFilename(entry.getFileName().replace("${arch}", "32"));
                        break;
                    }
                }
                for (String aMOsArch64 : mOsArch64) {
                    if (archInfo.equals(aMOsArch64)) {
                        entry.setFilename(entry.getFileName().replace("${arch}", "64"));
                        break;
                    }
                }
            }
            else{
            	Logger.getInstance().logError("FileNameHelper-> No OS arch detected!");
            }
        }
    }
}
