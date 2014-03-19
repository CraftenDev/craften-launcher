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
 * Represents a JSONWriter class
 * Currently only used for saving the showProfile data
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.json;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.auth.LastLogin;
import de.craften.util.OSHelper;

import java.io.File;
import java.io.FileWriter;

public class JSONWriter {
    public static void saveLastLogin(LastLogin login){
        String jsonEncoded = "{" +
                "\"email\":\""
                + login.getEmail() + "\"," +
                "\"username\":\""
                + login.getUsername() + "\"," +
                "\"accesstoken\":\""
                + login.getAccessToken() + "\"," +
                "\"profileid\":\""
                + login.getProfileID() + "\"," +
                "\"clienttoken\":\""
                + login.getClientToken() + "\"" +
                "}";
        try{
            FileWriter writer;
            if(login.getPath() == null){
                Logger.getInstance().logInfo("Writing LastLogin to " + OSHelper.getInstance().getMinecraftPath());
                writer = new FileWriter(OSHelper.getInstance().getMinecraftPath()+"lastLogin.json");
            }
            else{
                Logger.getInstance().logInfo("Writing LastLogin to " + login.getPath());

                if(login.getPath().endsWith(File.separator)) {
                    writer = new FileWriter(login.getPath()+"lastLogin.json");
                }
                else {
                    writer = new FileWriter(login.getPath()+ File.separator + "lastLogin.json");
                }
            }
            writer.write(jsonEncoded);
            writer.flush();
            writer.close();
        }catch (Exception e){
        	Logger.getInstance().logError("JSONWriter Error: " + e.getMessage());
        }
    }
}
