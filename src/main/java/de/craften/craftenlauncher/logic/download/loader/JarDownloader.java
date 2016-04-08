package de.craften.craftenlauncher.logic.download.loader;

import java.io.File;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.download.DownloadUrls;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPathImpl;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;

public class JarDownloader implements Downloader {

    private MinecraftVersion mVersion;
    private MinecraftPathImpl mMinecraftPath;

    /**
     * @param version
     * @param mcPath
     */
    public JarDownloader(MinecraftVersion version, MinecraftPathImpl mcPath) {
        this.mVersion = version;
        this.mMinecraftPath = mcPath;
    }

    @Override
    public void download() throws CraftenDownloadException {
        String version = this.mVersion.getVersion();
        String path = mMinecraftPath.getMinecraftJarPath();

        new File(path).mkdirs();

        DownloadHelper.downloadFileToDiskWithoutCheck(DownloadUrls.URL_VERSION + version + "/" + version
                + ".jar", path, version + ".jar");
    }

}
