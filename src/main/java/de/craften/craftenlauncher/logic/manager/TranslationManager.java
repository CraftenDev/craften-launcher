package de.craften.craftenlauncher.logic.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class TranslationManager {
    private static final Logger LOGGER = LogManager.getLogger(TranslationManager.class);

    public static String getString(String phrase) {
        String baseName = "stringsBundle";
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName);
            return bundle.getString(phrase);
        } catch (MissingResourceException e) {
            LOGGER.warn("Missing translation " + baseName, e);
            return baseName;
        }
    }

    public static String getString(String phrase, Object... args) {
        return String.format(getString(phrase), args);
    }
}
