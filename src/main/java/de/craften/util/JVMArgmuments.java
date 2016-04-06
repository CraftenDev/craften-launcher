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
 * JVMArgmuments Class:
 * <p>
 * Reads the extra JVMArguments if there are any
 *
 * @author saschb2b
 */
package de.craften.util;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;

import de.craften.craftenlauncher.logic.json.JSONReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple Helper for JVM specific Arguments
 *
 * @author saschb2b
 */
public class JVMArgmuments {
    private static final Logger LOGGER = LogManager.getLogger(JVMArgmuments.class);
    private final static String PATH = "jvm.json";

    public static ArrayList<String> get() {
        ArrayList<String> arg = new ArrayList<String>();
        JsonObject jsonObject = null;

        if (new File(PATH).canRead()) {
            jsonObject = JSONReader.readJson(PATH);
        }

        if (jsonObject == null) {
            try (InputStreamReader reader = new InputStreamReader(JVMArgmuments.class.getClassLoader().getResourceAsStream("jvm.json"))) {
                jsonObject = JSONReader.readJson(reader);
            } catch (Exception e) {
                LOGGER.error("Could not read included JVM config", e);
            }
        }

        if (jsonObject != null && jsonObject.has("JVMArguments")) {
            String[] dummy = jsonObject.get("JVMArguments").getAsString().split(Pattern.quote(";"));
            Collections.addAll(arg, dummy);
        }

        return arg;
    }
}
