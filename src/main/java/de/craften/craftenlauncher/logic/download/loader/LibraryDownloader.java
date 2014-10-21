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
import java.io.IOException;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.download.DownloadURLHelper;
import de.craften.craftenlauncher.logic.json.JSONReader;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;
import de.craften.craftenlauncher.logic.resources.LibEntry;
import de.craften.craftenlauncher.logic.resources.Version;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.craftenlauncher.logic.vm.DownloadVM;
import de.craften.util.OSHelper;

public class LibraryDownloader implements Downloader {
	
	private MinecraftPath mMinecraftPath;
	private MinecraftVersion mCurrentVersion;
	private DownloadVM mDownloadVM;
	
	public LibraryDownloader(MinecraftVersion version, MinecraftPath path, DownloadVM downloadVM) {
		this.mCurrentVersion = version;
		this.mMinecraftPath = path;
		this.mDownloadVM = downloadVM;
	}

	@Override
	public void download() throws CraftenDownloadException {
		Version vers = JSONReader.readJsonFileFromSelectedVersion(mMinecraftPath.getMinecraftJarPath() + mCurrentVersion.getVersion() + ".json");
		
		String libDir = mMinecraftPath.getLibraryDir();

		for (LibEntry entry : vers.getLibraries().get()) {
			
			if(entry.isExternal()) {
				Logger.getInstance().logInfo("Not downloading library because external: " + entry.getName());
				continue;
			}
			
			if (entry.isNeeded()) {
				
                if(entry.getFileName().contains("$")) {
                    if(OSHelper.getOSArch().equals("32"))
                        entry.setFilename(entry.getFileName().replace("${arch}", "32"));
                    else if(OSHelper.getOSArch().equals("64"))
                        entry.setFilename(entry.getFileName().replace("${arch}", "64"));
                    else
                        Logger.getInstance().logError("LibraryDownloader-> No OS arch detected!");
                }
                
				String replaced = entry.getPath().replace(File.separator, "/");
				String adress = DownloadURLHelper.URL_LIBRARIES + replaced + "/" + entry.getFileName();
				
				
				Logger.getInstance().logInfo("File download started: " + entry.getFileName());
				
				mDownloadVM.updateDownloadFile(entry.getFileName());
				mDownloadVM.updateProgress(1);
				
				try {
					DownloadHelper.downloadFileToDiskWithCheck(adress, libDir + entry.getPath(), entry.getFileName());
				} catch (CraftenDownloadException e) {
					Logger.getInstance().logError("Could not download: " + entry.getFileName() + " msg: " + e.getMessage());
					throw new CraftenDownloadException("Download failed: " + entry.getFileName());
				}

				if (entry.isExtractable()) {
					
					String file = libDir + entry.getPath() + File.separator + entry.getFileName();
					String natives = mMinecraftPath.getMinecraftJarPath() + this.mCurrentVersion.getVersion() + "-natives-131231";
					
					new File(natives).mkdirs();
					
					try {
						DownloadHelper.unpackJarFile(file, natives);
					} catch (IOException e) {
						Logger.getInstance().logError("Unpacking jar ( " + file + " ) failed: " + e.getMessage());
						throw new CraftenDownloadException("Unpacking jar failed: " + file);
					}
				}
			}
		}
	}

}
