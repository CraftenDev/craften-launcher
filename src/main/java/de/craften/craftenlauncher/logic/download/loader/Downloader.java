package de.craften.craftenlauncher.logic.download.loader;

import de.craften.craftenlauncher.exception.CraftenDownloadException;

public interface Downloader {
    /**
     * Downloads the specified file.
     *
     * @throws CraftenDownloadException if downloading the file fails
     */
    void download() throws CraftenDownloadException;
}
