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

import de.craften.craftenlauncher.logic.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple UIParser parsing command line arguments.
 *
 * @author redbeard
 * @author saschb2b
 */
public class UIParser {
    public Map<String, String> mArguments = new HashMap<String, String>();

    /**
     * Needs the command line arguments and
     * parses them.
     * @param args hst to be nut null.
     */
    public UIParser(String[] args) {
        for (String arg : args) {
            parseArg(arg);
        }
    }

    /**
     * Parses a given arg and checks if this arg is parseble
     * @param arg
     */
    private void parseArg(String arg) {
        if (arg.contains("--")) {
            String cleanedArg = arg.substring(2);

            parseCleanedArg(cleanedArg);
        } else {
            Logger.getInstance().logInfo("Not known arg: " + arg);
        }
    }

    /**
     * Parses the cleaned arg and saves it.
     * @param cleanedArg
     */
    private void parseCleanedArg(String cleanedArg) {
        if (cleanedArg.contains("=")) {
            String[] splitted = cleanedArg.split("=");

            if (splitted.length == 2) {
                mArguments.put(splitted[0], splitted[1]);
            } else {
                Logger.getInstance().logInfo("Unknown argument: " + cleanedArg);
            }
        }else{
            mArguments.put(cleanedArg, null);
        }
    }

    /**
     * Returns the argument for the given key.
     *
     * @param key
     * @return the argument or null.
     */
    public String getArg(String key) {
        if (hasArg(key)) {
            return mArguments.get(key);
        }

        return null;
    }

    /**
     * Checks if there is a specified key.
     *
     * @param key
     * @return true if it contains the key, false if it does not.
     */
    public boolean hasKey(String key){
        return mArguments.containsKey(key);
    }

    /**
     * Checks if there is an Value for the specified key.
     *
     * @param key
     * @return true if it contains an argument, false if it does not.
     */
    public boolean hasArg(String key) {
        return mArguments.get(key) != null;
    }
}
