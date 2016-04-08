package de.craften.craftenlauncher.logic.manager;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TranslationManager {
    private static final Logger LOGGER = LogManager.getLogger(TranslationManager.class);

    public static String getString(String phrase) {
        Locale.setDefault(new Locale("en", "GB"));
        String baseName = "de.craften.craftlauncher.conf.CraftenLauncher";
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName);
            return bundle.getString(phrase);
        } catch (MissingResourceException e) {
            LOGGER.warn("Missing translation", e);
            return "#########";
        }
    }
}
