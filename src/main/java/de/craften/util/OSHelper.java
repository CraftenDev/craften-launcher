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
 */
package de.craften.util;

import java.io.File;

/**
 * Simple OS Helper determines the current OS and cpu architecture.
 *
 * @author saschb2b
 * @author redbeard
 */
public final class OSHelper {
    private static final String operatingSystem;
    private static final OS os;
    private static final String[] mOsArch32 = {"x86", "i386", "i686"}, //32-bit
            mOsArch64 = {"x64", "ia64", "amd64"};                //64-bit

    static {
        String osName = System.getProperty("os.name");

        if (osName.contains("Win")) {
            operatingSystem = "windows";
            os = OS.WINDOWS;
        } else if (osName.contains("Linux")) {
            operatingSystem = "linux";
            os = OS.LINUX;
        } else if (osName.contains("Mac")) {
            operatingSystem = "osx";
            os = OS.OSX;
        } else {
            operatingSystem = "undefined";
            os = OS.UNDEFINED;
        }
    }

    /**
     * Returns true if Java is running in x86 (32 bit) version.
     *
     * @return
     */
    public static boolean isJava32bit() {
        String archInfo = System.getProperty("os.arch");

        if (archInfo != null && !archInfo.equals("")) {
            for (String aMOsArch32 : mOsArch32) {
                if (archInfo.equals(aMOsArch32)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if Java is running in x64 (64 bit) version.
     *
     * @return
     */
    public static boolean isJava64bit() {
        String archInfo = System.getProperty("os.arch");

        if (archInfo != null && !archInfo.equals("")) {
            for (String aMOsArch64 : mOsArch64) {
                if (archInfo.equals(aMOsArch64)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the OS processor architecture.
     *
     * @return 32 or 64
     */
    public static String getOSArch() {
        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        String realArch;

        if (arch != null) {
            if (arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64")) {
                realArch = "64";
            } else {
                realArch = "32";
            }
        } else {
            if (wow64Arch != null && wow64Arch.endsWith("64")) {
                realArch = "64";
            } else {
                realArch = "32";
            }
        }
        return realArch;
    }

    /**
     * Returns the minecraft path for the current os system and creates the
     * path if it does not exist.
     *
     * @return
     */
    public static String getMinecraftPath() {
        final String fs = File.separator;

        String path = "";

        if (os == OS.WINDOWS) {
            path = System.getenv("APPDATA") + fs + ".minecraft" + fs;
            if (new File(path).exists()) {
                return path;
            }
        } else if (os == OS.LINUX) {
            path = System.getProperty("user.home") + fs + ".minecraft"
                    + fs;
            if (new File(path).exists()) {
                return path;
            }
        } else if (os == OS.OSX) {
            path = System.getProperty("user.home") + fs + "Library" + fs
                    + "Application Support" + fs + "minecraft" + fs;
            if (new File(path).exists()) {
                return path;
            }
        }

        new File(path).mkdirs();
        return path;
    }

    /**
     * Returns the current {@see #OS} as an enum.
     * Can return undefined if the os does not match (Windows, Linux, Mac OSX)
     *
     * @return
     */
    public static OS getOSasEnum() {
        return os;
    }

    /**
     * Returns the operatins system as a String in lower case letters.
     *
     * @return
     */
    public static String getOSasString() {
        return operatingSystem.toLowerCase();
    }

    /**
     * Returns the current java path. The java programm for the current os
     * is added.
     * e.g. (path)\javaw.exe in windows.
     *
     * @return
     */
    public static String getJavaPath() {
        final String fs = File.separator;

        String path = System.getProperty("java.home") + fs + "bin" + fs;

        if (getOSasEnum() == OS.WINDOWS && new File(path + "javaw.exe").isFile()) {
            return path + "javaw.exe";
        }

        return path + "java";
    }
}
