/**
 * CraftenLauncher is an alternative Launcher for Minecraft developed by Mojang.
 * Copyright (C) 2013  Johannes "redbeard" Busch, Sascha "saschb2b" Becker
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Represents a JSONWriter class
 * Currently only used for saving the showProfile data
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.json;

import com.google.gson.stream.JsonWriter;
import de.craften.craftenlauncher.logic.auth.Profiles;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import de.craften.util.OSHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONWriter {
    private static final Logger LOGGER = LogManager.getLogger(JSONWriter.class);

    public static void saveProfiles(Profiles login) {
        String filename = "craftenlauncher_profiles.json";
        JsonWriter writer;

        try {
            if (login.getPath() == null || login.getPath().equals("")) {
                LOGGER.info("Writing craftenlauncher_profiles to " + OSHelper.getMinecraftPath());
                writer = new JsonWriter(new FileWriter(OSHelper.getMinecraftPath() + filename));
                login.setPath(OSHelper.getMinecraftPath());
            } else {
                LOGGER.info("Writing craftenlauncher_profiles to " + login.getPath());

                if (login.getPath().endsWith(File.separator)) {
                    writer = new JsonWriter(new FileWriter(login.getPath() + filename));
                } else {
                    writer = new JsonWriter(new FileWriter(login.getPath() + File.separator + filename));
                    login.setPath(login.getPath() + File.separator);
                }
            }
            writer.setIndent(" ");
            writer.beginObject();

            writer.name("selectedUser");
            writer.beginObject();
            writeMinecraftUser(writer, login.getSelectedUser());
            writer.endObject();

            writer.name("availableUsers");
            writer.beginArray();
            for (int i = 0; i < login.getAvailableUsers().size(); i++) {
                MinecraftUser user = login.getAvailableUser(i);
                writer.beginObject();
                writeMinecraftUser(writer, user);
                writer.endObject();
            }
            writer.endArray();

            writer.endObject();
            writer.close();

        } catch (Exception e) {
            LOGGER.error("JSONWriter Error: " + e.getMessage(), e);
        }
    }

    private static void writeMinecraftUser(JsonWriter writer, MinecraftUser user) throws IOException {
        writer.name("email").value(user.getEmail());
        writer.name("profileid").value(user.getProfileId());
        writer.name("username").value(user.getUsername());
        writer.name("accesstoken").value(user.getAccessToken());
        writer.name("clienttoken").value(user.getClientToken());
    }
}
