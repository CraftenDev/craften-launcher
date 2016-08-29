package de.craften.craftenlauncher.logic.version;

import de.craften.craftenlauncher.exception.CraftenVersionNotKnownException;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.download.DownloadUrls;
import de.craften.craftenlauncher.logic.download.VersionLoader;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VersionListHelper {
    private static final Logger LOGGER = LogManager.getLogger(VersionListHelper.class);
    private final List<String> versions;
    private final MinecraftPath minecraftPath;

    public VersionListHelper(MinecraftPath mcPath) {
        minecraftPath = mcPath;
        versions = VersionLoader.getVersionStringList();

        //hash set to check for duplicates without evil n² time complexity
        HashSet<String> foundVersions = new HashSet<>();
        foundVersions.addAll(versions);

        //prepend local (i.e. custom modded) versions
        List<String> localVersions = getLocalVersions(new File(minecraftPath.getMinecraftVersionsDir()));
        for (String version : localVersions) {
            if (!foundVersions.contains(version)) {
                versions.add(0, version);
            }
        }
    }

    private static List<String> getLocalVersions(File versionsDir) {
        List<String> versions = new ArrayList<>();
        File[] files = versionsDir.listFiles();

        if (files != null) {
            for (File dir : files) {
                if (dir.isDirectory()) {
                    versions.add(dir.getName());
                }
            }
        }
        return versions;
    }

    public List<String> getVersionsList() {
        return versions;
    }

    public void checkVersion(String version) throws CraftenVersionNotKnownException {
        if (!versions.contains(version)) {
            throw new CraftenVersionNotKnownException("Version: " + version);
        }
    }

    /**
     * Ueberprueft die Version online und fuegt Sie dann der Liste
     * hinzu (falls Pruefung erfolgreich).
     *
     * @param versionName version name
     */
    public boolean isVersionAvailableOnline(String versionName) {
        try {
            String test = DownloadHelper.downloadFileToString(DownloadUrls.URL_VERSION + versionName + "/" + versionName + ".json");

            if (test == null || test.equals("")) {
                return false;
            }

            if (!versions.contains(versionName)) {
                versions.add(versionName);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Could not check if version " + versionName + " is available", e);
            return false;
        }
    }
}
