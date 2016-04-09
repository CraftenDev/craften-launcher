package de.craften.craftenlauncher.logic.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MCOptions {
    private static final Logger LOGGER = LogManager.getLogger(MCOptions.class);
    private static MCOptions instance;
    private List<MCOptionEntry> mContent;

    private MCOptions() {
    }

    public synchronized static MCOptions getInstance() {
        if (instance == null) {
            instance = new MCOptions();
        }
        return instance;
    }

    public void read(String path) {
        mContent = new ArrayList<>();

        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null && line.contains(":")) {
                String[] splitted = line.split(Pattern.quote(":"));
                MCOptionEntry entry = new MCOptionEntry(splitted[0], splitted[1]);
                mContent.add(entry);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void setValue(String name, String value) {
        for (MCOptionEntry aMContent : mContent) {
            if (aMContent.getName().equals(name)) {
                aMContent.setValue(value);
            }
        }
    }

    public String getValue(String name) {
        for (MCOptionEntry aMContent : mContent) {
            if (aMContent.getName().equals(name)) {
                return aMContent.getValue();
            }
        }
        return null;
    }
}
