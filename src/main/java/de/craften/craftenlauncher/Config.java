package de.craften.craftenlauncher;

import com.tngtech.configbuilder.annotation.valueextractor.CommandLineValue;

/**
 * Craften Launcher config class.
 *
 */
public class Config {

    @CommandLineValue(shortOpt = "mcPath", longOpt = "minecraftPath", hasArg = true)
    private String mcPath;

    @CommandLineValue(shortOpt = "server", longOpt = "serverAddress", hasArg = true)
    private String server;

    @CommandLineValue(shortOpt = "xmx", longOpt = "minecaftMemory", hasArg = true)
    private String xmx;

    @CommandLineValue(shortOpt = "version", longOpt = "minecraftVersion", hasArg = true)
    private String version;

    @CommandLineValue(shortOpt = "quickplay", longOpt = "startQuickPlay")
    private boolean quickPlay;

    @CommandLineValue(shortOpt = "forcelogin", longOpt = "forceMinecraftLogin")
    private boolean forcelogin;

    @CommandLineValue(shortOpt = "fullscreen", longOpt = "startMinecraftFullscreen")
    private boolean fullscreen;

    @CommandLineValue(shortOpt = "profileid", longOpt = "usedProfileID", hasArg = true)
    private String profileID;

    public String getMcPath() {
        return mcPath;
    }

    public String getServer() {
        return server;
    }

    public String getXmx() {
        return xmx;
    }

    public String getVersion() {
        return version;
    }

    public boolean isQuickPlay() {
        return quickPlay;
    }

    public boolean isForcelogin() {
        return forcelogin;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public String getProfileID() {
        return profileID;
    }
}
