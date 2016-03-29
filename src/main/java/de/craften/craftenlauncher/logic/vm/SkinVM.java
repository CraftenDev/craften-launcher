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
 */
package de.craften.craftenlauncher.logic.vm;

import java.awt.image.BufferedImage;
import java.util.Observable;

/**
 * "ViewModel" class which offers information around
 * the minecraft skin.
 * SkinVM notifies all gui clases which handles skin presentation.
 * Offers Information about the download status and the downloaded skin
 * image file.
 *
 * @author redbeard
 */
public class SkinVM extends Observable {

    private BufferedImage mSkin;

    /**
     * Offers information about the download status
     *
     * @return True if the download is completed.
     */
    public boolean wasSkinDownloaded() {
        return (mSkin != null);
    }

    /**
     * Method to set the downloaded skin image.
     *
     * @param skin has to be not null.
     */
    public void setSkinDownloaded(BufferedImage skin) {
        this.mSkin = skin;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Returns the downloaded skin image.
     *
     * @return the downloaded skin image
     */
    public BufferedImage getSkin() {
        return mSkin;
    }
}
