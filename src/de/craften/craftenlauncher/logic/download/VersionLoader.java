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
 * VersionLoader Class:
 * 
 * Simple loader to load the version file
 * into an array of strings
 * 
 * @auhtor redbeard
 */
package de.craften.craftenlauncher.logic.download;

import java.util.ArrayList;
import java.util.Scanner;

public class VersionLoader {
	public static ArrayList<String> getVersionStringList() {
		ArrayList<String> versions = new ArrayList<String>();
		
		String versionsJSON = DownloadHelper.downloadFileToString("https://s3.amazonaws.com/Minecraft.Download/versions/versions.json");
		
		Scanner s = new Scanner(versionsJSON);
		
		//TODO: Should use json splitting instead of this.
		while (s.hasNextLine()) {
			String nextLine = s.nextLine();
			
			if(nextLine.contains("id")) {
				String[] split = nextLine.split(":");
				
				String version = split[1].substring(2, split[1].indexOf("\"", 3));
				
				versions.add(version);
			}
		}
		
		s.close();
		return versions;
	}
}
