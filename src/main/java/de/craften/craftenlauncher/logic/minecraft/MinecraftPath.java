package de.craften.craftenlauncher.logic.minecraft;

import java.io.File;

import de.craften.util.OSHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MinecraftPath {
    private static final Logger LOGGER = LogManager.getLogger(MinecraftPath.class);
    private String mMinecraftDir;

    /**
     * set MincraftDir to the os-specific value
     */
    public MinecraftPath() {
        this.mMinecraftDir = OSHelper.getMinecraftPath();
    }

    /**
     * set MinecraftDir to the parameter
     *
     * @param minecraftDir
     */
    public MinecraftPath(String minecraftDir) {
        this();

        if (minecraftDir == null) {
            this.mMinecraftDir = OSHelper.getMinecraftPath();
        } else if (!minecraftDir.equals("")) {
            if (!minecraftDir.endsWith("\\")) {
                minecraftDir += File.separator;
            }

            this.mMinecraftDir = minecraftDir;
        }

        LOGGER.info("MinecraftPath: " + this.mMinecraftDir);
    }

    public String getMinecraftDir() {
        return mMinecraftDir;
    }

    public abstract String getNativeDir();

    public abstract String getLibraryDir();

    public abstract String getMinecraftJarPath();

    public abstract String getMinecraftVersionsDir();

    public abstract String getResourcePath();
}
