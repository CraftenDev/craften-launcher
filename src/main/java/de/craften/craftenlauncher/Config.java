package de.craften.craftenlauncher;

import com.tngtech.configbuilder.annotation.valueextractor.CommandLineValue;

/**
 * Craften Launcher config class.
 */
public class Config {

    @CommandLineValue(shortOpt = "mcpath", longOpt = "mcpath", hasArg = true)
    private String mcPath;

    @CommandLineValue(shortOpt = "server", longOpt = "server", hasArg = true)
    private String server;

    @CommandLineValue(shortOpt = "xmx", longOpt = "xmx", hasArg = true)
    private String xmx;

    @CommandLineValue(shortOpt = "version", longOpt = "version", hasArg = true)
    private String version;

    @CommandLineValue(shortOpt = "assetsversion", longOpt = "assetsversion", hasArg = true)
    private String assetsVersion;

    @CommandLineValue(shortOpt = "profileid", longOpt = "profileid", hasArg = true)
    private String profileID;

    @CommandLineValue(shortOpt = "quickplay", longOpt = "quickplay")
    private boolean quickPlay;

    @CommandLineValue(shortOpt = "forcelogin", longOpt = "forcelogin")
    private boolean forcelogin;

    @CommandLineValue(shortOpt = "fullscreen", longOpt = "fullscreen")
    private boolean fullscreen;

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

    public String getAssetsVersion() {
        return assetsVersion;
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

    public boolean hasMinecraftPath() {
        return mcPath != null;
    }

    public boolean hasServerAddress() {
        return server != null;
    }

    public boolean hasXMXParameter() {
        return xmx != null;
    }

    public boolean hasProfileID() {
        return profileID != null;
    }
}
