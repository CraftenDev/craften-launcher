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
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.version;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import de.craften.craftenlauncher.exception.CraftenVersionNotKnownException;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.download.DownloadURLHelper;
import de.craften.craftenlauncher.logic.download.VersionLoader;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;

public class VersionListHelper {
	private ArrayList<String> mVersions = new ArrayList<String>();
	private MinecraftPath mMinecraftPath;
	
	public VersionListHelper(MinecraftPath mcPath) {
		this.mMinecraftPath = mcPath;
		fillVersionsList();
	}
	
	private void fillVersionsList() {
		mVersions = VersionLoader.getVersionStringList();
		checkVersionsPath();
	}

	private void checkVersionsPath() {
		String path = mMinecraftPath.getMinecraftVersionsDir();
		File versionsDir = new File(path);
		
		File[] files = versionsDir.listFiles();

        if(files != null)
            for(File dir : files) {
                if(dir.isDirectory()) {
                    File[] f = dir.listFiles(new FileFilter() {

                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getAbsolutePath().contains(".json");
                        }
                    });

                    if(f.length == 1) {
                        mVersions.add(dir.getName());
                    }
                }
            }
	}

	public ArrayList<String> getVersionsList() {
		return mVersions;
	}
	
	public void checkVesion(String version) throws CraftenVersionNotKnownException{
		if(!mVersions.contains(version)) {
			throw new CraftenVersionNotKnownException("Version: " + version);
		}
	}
	
	/**
	 * Ueberprueft die Version online und fuegt Sie dann der Liste
	 * hinzu ( falls Pruefung erfolgreich ).
	 * @param arg
	 */
	public boolean isVersionAvailableOnline(String versionName) {
		try {
			String test = DownloadHelper.downloadFileToString(DownloadURLHelper.URL_VERSION + versionName + "/" + versionName + ".json");
			
			if(test == null || test.equals("")) {
				return false;
			}
			
			if(!mVersions.contains(versionName)) {
				mVersions.add(versionName);
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
