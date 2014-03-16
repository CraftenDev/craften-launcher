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
 * Simple class to handle command line arguments
 * 
 * @author redbeard
 */
package de.craften.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UIParser {
	public Map<String, String> mArguments = new HashMap<String, String>();
	
	public UIParser(String[] args) {
		for(String arg : args) {
			parseArg(arg);
		}
	}
	
	private void parseArg(String arg) {
		if(arg.contains("--")) {
			String cleanedArg = arg.substring(2);
			
			String[] splitted = cleanedArg.split("=");
			
			if(splitted.length == 2) {
				mArguments.put(splitted[0],splitted[1]);
			}
		}
	}
	
	public String getArg(String name) {
		if(hasArg(name)) {
			String arg = mArguments.get(name);
			return arg;
		}
		
		return null;
	}
	
	/**
	 * Checks if there is an Value for the specified key.
	 * @param key 
	 * @return true if it contains an argument, false if it does not.
	 */
	public boolean hasArg(String key) {
		String arg = mArguments.get(key);
		
		return (arg != null);
	}
	
	public void setArg(String key, String value) {
		if(key != null && !key.equals("") && value != null && !value.equals("")) {
			mArguments.put(key, value);
		}
	}
	
	public Set<String> getCommands() {
		return mArguments.keySet();
	}
}
