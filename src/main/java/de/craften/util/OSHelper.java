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
 * Simple os-helper.
 * 
 * @author evidence
 * @author redbeard
 */
package de.craften.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.craften.craftenlauncher.logic.Logger;

public final class OSHelper {
	private static String operatingSystem;
	private static OSHelper instance;
	private static String pS = File.separator;
    private static String[] mOsArch32 = {"x86", "i386", "i686"}, //32-bit
            mOsArch64 = {"x64", "ia64", "amd64"};                //64-bit

	private OSHelper() {
		operatingSystem = System.getProperty("os.name");
		
		if (operatingSystem.contains("Win")) {
			operatingSystem = "windows";
		} else if (operatingSystem.contains("Linux")) {
			operatingSystem = "linux";
		} else if (operatingSystem.contains("Mac")) {
			operatingSystem = "osx";
		}
		
		
		//TODO: Alle Logger Aufrufen verschieben. Vllt. in die Main?
		
		Logger.getInstance().logDebug("OS : " + operatingSystem);
		Logger.getInstance().logDebug("OS Arch : " + System.getProperty("os.arch"));
		Logger.getInstance().logDebug("OS Version : " + System.getProperty("os.version"));
		Logger.getInstance().logDebug("Username : " + System.getProperty("user.name"));
		Logger.getInstance().logDebug("Java Vendor : " + System.getProperty("java.vendor"));
		Logger.getInstance().logDebug("Java Version : " + System.getProperty("java.version"));
		Logger.getInstance().logDebug("Java Home : " + System.getProperty("java.home"));
		Logger.getInstance().logDebug("Java Classpath : " + System.getProperty("java.class.path"));
		
		Logger.getInstance().logDebug("Available processors (cores): " + 
		        Runtime.getRuntime().availableProcessors());
		Logger.getInstance().logDebug("Total memory (bytes): " + 
		        Runtime.getRuntime().totalMemory());
		
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		
		Logger.getInstance().logInfo("Date: " + formatter.format(new Date()));
		
	}

	public synchronized static OSHelper getInstance() {
		if (instance == null) {
			instance = new OSHelper();
		}
		return instance;
	}

    public boolean is32bit(){
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

    public boolean is64bit(){
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
	
	public String getOperatingSystem() {
		return operatingSystem;
	}
}
