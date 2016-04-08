package de.craften.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple UIParser parsing command line arguments.
 *
 * @author redbeard
 * @author saschb2b
 */
public class UIParser {
    private static final Logger LOGGER = LogManager.getLogger(UIParser.class);
    private Map<String, String> mArguments = new HashMap<>();

    /**
     * Needs the command line arguments and
     * parses them.
     *
     * @param args hst to be nut null.
     */
    public UIParser(String[] args) {
        for (String arg : args) {
            parseArg(arg);
        }
    }

    /**
     * Parses a given arg and checks if this arg is parseble
     *
     * @param arg
     */
    private void parseArg(String arg) {
        if (arg.contains("--")) {
            String cleanedArg = arg.substring(2);
            parseCleanedArg(cleanedArg);
        } else {
            LOGGER.warn("Unknown argument: " + arg);
        }
    }

    /**
     * Parses the cleaned arg and saves it.
     *
     * @param cleanedArg
     */
    private void parseCleanedArg(String cleanedArg) {
        if (cleanedArg.contains("=")) {
            String[] splitted = cleanedArg.split("=");

            if (splitted.length == 2) {
                mArguments.put(splitted[0], splitted[1]);
            } else {
                LOGGER.warn("Unknown argument: " + cleanedArg);
            }
        } else {
            mArguments.put(cleanedArg, null);
        }
    }

    /**
     * Returns the argument for the given key.
     *
     * @param key
     * @return the argument or null.
     */
    public String getValue(String key) {
        if (hasValue(key)) {
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
    public boolean hasKey(String key) {
        return mArguments.containsKey(key);
    }

    /**
     * Checks if there is an Value for the specified key.
     *
     * @param key
     * @return true if it contains an argument, false if it does not.
     */
    public boolean hasValue(String key) {
        return mArguments.get(key) != null;
    }
}
