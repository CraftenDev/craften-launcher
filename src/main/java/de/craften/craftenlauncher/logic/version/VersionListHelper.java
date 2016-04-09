package de.craften.craftenlauncher.logic.version;

import de.craften.craftenlauncher.exception.CraftenVersionNotKnownException;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.download.DownloadUrls;
import de.craften.craftenlauncher.logic.download.VersionLoader;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class VersionListHelper {
    private static final Logger LOGGER = LogManager.getLogger(VersionListHelper.class);
    private List<String> mVersions = new ArrayList<String>();
    private MinecraftPath mMinecraftPath;

    public VersionListHelper(MinecraftPath mcPath) {
        this.mMinecraftPath = mcPath;
        fillVersionsList();
    }

    private void fillVersionsList() {
        mVersions = VersionLoader.getVersionStringList();
        checkVersionsPath();
    }

    private void checkVersionsPath() {
        String path = mMinecraftPath.getMinecraftVersionsDir();
        File versionsDir = new File(path);

        File[] files = versionsDir.listFiles();

        if (files != null)
            for (File dir : files) {
                if (dir.isDirectory()) {
                    File[] f = dir.listFiles(new FileFilter() {

                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getAbsolutePath().contains(".json");
                        }
                    });

                    if (f.length == 1) {
                        mVersions.add(dir.getName());
                    }
                }
            }
    }

    public List<String> getVersionsList() {
        return mVersions;
    }

    public void checkVesion(String version) throws CraftenVersionNotKnownException {
        if (!mVersions.contains(version)) {
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

            if (!mVersions.contains(versionName)) {
                mVersions.add(versionName);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Could not check if version " + versionName + " is available", e);
            return false;
        }
    }
}
