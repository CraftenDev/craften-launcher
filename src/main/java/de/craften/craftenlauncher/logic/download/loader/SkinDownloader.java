/**
 * CraftenLauncher is an alternative Launcher for Minecraft developed by Mojang.
 * Copyright (C) 2013  Johannes "redbeard" Busch, Sascha "saschb2b" Becker
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.download.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.logic.download.DownloadURLHelper;
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

        String url = DownloadURLHelper.URL_SKINS;

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
