package de.craften.util;

import com.google.gson.JsonObject;
import de.craften.craftenlauncher.logic.json.JSONReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

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
