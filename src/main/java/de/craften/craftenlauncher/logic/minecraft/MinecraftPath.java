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
 * MinecraftPath Abstract-Class:
 * 
 * Abstract super class for different mc-paths
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.minecraft;

import java.io.File;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.util.OSHelper;

public abstract class MinecraftPath {
	private String mMinecraftDir;
	
	/**
	 * set MincraftDir to the os-specific value
	 */
	public MinecraftPath() {
		this.mMinecraftDir = OSHelper.getMinecraftPath();
	}
	
	/**
	 * set MinecraftDir to the parameter
	 * @param minecraftDir
	 */
	public MinecraftPath(String minecraftDir) {
		this();
		
		if(minecraftDir == null) {
			this.mMinecraftDir = OSHelper.getMinecraftPath();
		}
		else if(!minecraftDir.equals("")) {
			if(!minecraftDir.endsWith("\\")) {
				minecraftDir += File.separator;
			}
			
			this.mMinecraftDir = minecraftDir;
		}
		
		Logger.logInfo("MinecraftPath: " + this.mMinecraftDir);
	}
	
	public String getMinecraftDir() {
		return mMinecraftDir;
	}
	
	public abstract String getNativeDir();
	public abstract String getLibraryDir();
	public abstract String getMinecraftJarPath();
	public abstract String getMinecraftVersionsDir();
	public abstract String getResourcePath();
}
