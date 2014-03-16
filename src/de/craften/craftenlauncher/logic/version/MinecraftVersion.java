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
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.version;

import de.craften.craftenlauncher.logic.resources.Version;

public class MinecraftVersion {
	private String mVersion;
	private Version mVersionJson;
	
	public MinecraftVersion(String version) {
		this.mVersion = version;
	}
	
	public String getVersion() {
		return mVersion;
	}

	public String getAssets() {
		return mVersionJson.getAssets();
	}
	
	public boolean hasAssets() {
		return mVersionJson.hasAssets();
	}

	public void setVersionJson(Version versionJson) {
		this.mVersionJson = versionJson;
	}
	
	public Version getVersionJson() {
		return mVersionJson;
	}
}
