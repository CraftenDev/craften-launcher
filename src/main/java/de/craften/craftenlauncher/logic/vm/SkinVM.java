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
