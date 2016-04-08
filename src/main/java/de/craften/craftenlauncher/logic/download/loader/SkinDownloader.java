package de.craften.craftenlauncher.logic.download.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.logic.download.DownloadUrls;
import de.craften.craftenlauncher.logic.vm.SkinVM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkinDownloader implements Downloader {
    private static final Logger LOGGER = LogManager.getLogger(SkinDownloader.class);
    private String mUsername;
    private SkinVM mSkinVm;

    public SkinDownloader(String username, SkinVM vm) {
        this.mUsername = username;
        this.mSkinVm = vm;
    }

    @Override
    public void download() throws CraftenDownloadException {
        LOGGER.info("Starting skin-download!");

        String url = DownloadUrls.URL_SKINS;

        URL skinURL;
        try {
            skinURL = new URL(url + mUsername + ".png");
        } catch (MalformedURLException e) {
            LOGGER.error("Skin URL error", e);
            throw new CraftenDownloadException("SkinURL-Error!", e);
        }


        try {
            BufferedImage img = ImageIO.read(skinURL);
            mSkinVm.setSkinDownloaded(img);
            LOGGER.debug("Skin downloaded");
        } catch (IOException e) {
            LOGGER.error("Could not download skin", e);
            throw new CraftenDownloadException("Skin Download-Error!");
        }
    }

}
