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
 * NewPath Class:
 * 
 * Sub-class with the special mc-path for minecraft post 1.6.+
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.minecraft;

import java.io.File;


public class MinecraftPathImpl extends MinecraftPath {
	private String mVersion;

	public MinecraftPathImpl() {
		super();
	}
	
	public MinecraftPathImpl(String minecraftPath,String version) {
		super(minecraftPath);
		this.mVersion = version;
	}

	public MinecraftPathImpl(String path) {
		super(path);
	}

	@Override
	public String getNativeDir() {
		return super.getMinecraftDir()+"natives" + File.separator;
	}

	@Override
	public String getLibraryDir() {
		return super.getMinecraftDir() + "libraries" + File.separator;
	}

	@Override
	public String getMinecraftJarPath() {
		return getMinecraftVersionsDir() + mVersion + File.separator;
	}
	
	@Override
	public String getResourcePath() {
		return super.getMinecraftDir() + "assets" + File.separator;
	}

	@Override
	public String getMinecraftVersionsDir() {
		return super.getMinecraftDir() + "versions" + File.separator;
	}
	
	public void setVersionName(String version) {
		this.mVersion = version;
	}

}
