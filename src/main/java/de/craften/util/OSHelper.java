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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.craften.craftenlauncher.logic.Logger;

/**
 * Simple OS Helper determines the current OS and cpu architecture.
 *
 * @author evidence
 * @author redbeard
 */
public final class OSHelper {
	private static String operatingSystem;
	private static OSHelper instance;
	private static String pS = File.separator;
    private static String[] mOsArch32 = {"x86", "i386", "i686"}, //32-bit
            mOsArch64 = {"x64", "ia64", "amd64"};                //64-bit

    /**
     * Returns a new OSHelper instance.
     * @return
     */
    public synchronized static OSHelper getInstance() {
        if (instance == null) {
            instance = new OSHelper();
        }
        return instance;
    }

    /**
     * Returns a new OSHelper instance for testing purposes.
     * @return
     */
    public static OSHelper TEST_CreateInstance() {
        return new OSHelper();
    }

    private OSHelper() {
        operatingSystem = System.getProperty("os.name");

        if (operatingSystem.contains("Win")) {
            operatingSystem = "windows";
        } else if (operatingSystem.contains("Linux")) {
            operatingSystem = "linux";
        } else if (operatingSystem.contains("Mac")) {
            operatingSystem = "osx";
        }
    }

    public boolean isJava32bit(){
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

    public boolean isJava64bit(){
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
     * Returns the OS / JVM processor architecture.
     * @return 32 or 64
     */
    public String getOSArch(){
        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

		String realArch = null;

        if(arch != null) {  	
			if(arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64")) {
				realArch = "64";
			} else {
				realArch = "32";			
			}
		} else {
			if(wow64Arch != null && wow64Arch.endsWith("64")) {
				realArch = "64";			
			} else {
                realArch = "32";
            }
		}
        return realArch;
    }

	public String getMinecraftPath() {
		String path = "";
		if (operatingSystem.equals("windows")) {
			path = System.getenv("APPDATA") + pS + ".minecraft" + pS;
			if (new File(path).exists()) {
				return path;
			}
		} else if (operatingSystem.equals("linux")) {
			path = System.getProperty("user.home") + pS + ".minecraft"
					+ pS;
			if (new File(path).exists()) {
				return path;
			}
		} else if (operatingSystem.equals("mac")) {
			path = System.getProperty("user.home") + pS + "Library" + pS
					+ "Application Support" + pS + "minecraft" + pS;
			if (new File(path).exists()) {
				return path;

			}
		}
		
		new File(path).mkdirs();
		return path;
	}
	
	public String getOS() {
		return operatingSystem;
	}

	public String getOSasString(){
        return operatingSystem.toString().toLowerCase();
    }

    public String getJavaPath() {
        String fs = File.separator;

        String path = System.getProperty("java.home") + fs + "bin" + fs;

        if (operatingSystem.equals(OS.WINDOWS) &&
                (new File(path + "javaw.exe").isFile())) {
            return path + "javaw.exe";
        }

        return path + "java";
    }
}
