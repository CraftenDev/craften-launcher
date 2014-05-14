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
package de.craften.craftenlauncher.logic.download.loader;

import java.io.File;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.download.DownloadURLHelper;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPathImpl;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;


public class JarDownloader implements Downloader {
	
	private MinecraftVersion mVersion;
	private MinecraftPathImpl mMinecraftPath;

    /**
     *
     * @param version
     * @param mcPath
     */
	public JarDownloader(MinecraftVersion version, MinecraftPathImpl mcPath) {
		this.mVersion = version;
		this.mMinecraftPath = mcPath;
	}
	
	@Override
	public void download() throws CraftenDownloadException {
		String version = this.mVersion.getVersion();
		String path = mMinecraftPath.getMinecraftJarPath();
		
		new File(path).mkdirs();
		
		DownloadHelper.downloadFileToDiskWithoutCheck(DownloadURLHelper.URL_VERSION + version + "/" + version
				+ ".jar", path, version + ".jar");
	}

}
