/**
 * CraftenLauncher is an alternative Launcher for Minecraft developed by Mojang.
 * Copyright (C) 2013  Johannes "redbeard" Busch, Sascha "saschb2b" Becker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package de.craften.craftenlauncher.logic.vm;

import java.util.Observable;

/**
 * DownloadVM Class:
 *
 * "ViewModel" class which serves as a connection between
 * the downloader and the gui.
 * Serves several information regarding the current download process.
 *
 * @author redbeard
 */
public class DownloadVM extends Observable{
	private int mProgress;
	private String mInfo;
	private String mStatus;
	private volatile boolean mDownloaded;
	private int mDownloadedKByte;

    /**
     * Changes the name of the download file to
     * current downloaded file.
     * @param filename
     */
	public void updateDownloadFile(final String filename) {
		updateStatusInfo("File: " + filename);
	}

    /**
     * Updates the current status to the new one.
     * @param info
     */
	public void updateStatusInfo(final String info) {
		this.mInfo += " " + info;

        setChanged();
        notifyObservers();
	}

    /**
     * Returns the current info and cleans the internal info state.
     * @return
     */
	public String getInfo() {
		String i = this.mInfo;
		
		this.mInfo = "";
		
		return i;
	}

    /**
     * Sets the new currnt status.
     * @param status
     */
	public void updateStatus(final String status) {
		this.mStatus += " " + status;
		
		setChanged();
		notifyObservers();
	}

    /**
     * Gets the current status and cleans internal status state.
     * @return
     */
	public String getStatus() {
		String s = this.mStatus;
		
		this.mStatus = "";
		
		return s;
	}
	
	/**
	 * Adds the given progess value to the current progress.
	 * @param progress Is the new download progress.
	 */
	public synchronized void updateProgress(int progress) {
		this.mProgress += progress;
		
		setChanged();
        notifyObservers();
	}

    /**
     * Returns the current progress of the download.
     * @return
     */
	public int getProgress() {
		return mProgress;
	}
	
	/**
	 * Sets the progress to 0.
	 */
	public synchronized void setProgressBarToNull() {
		mProgress = 0;
	}

    /**
     * This method can used if the download is finished.
     *
     * @param downloaded
     */
	public void setMinecraftDownload(boolean downloaded) {
		this.mDownloaded = downloaded;
		
		setChanged();
		notifyObservers();
	}

    /**
     * Returns true if the download process is finished.
     * @return
     */
	public boolean isMinecraftDownloaded() {
		return mDownloaded;
	}

    /**
     * Method adds the given kb to the current kb.
     * @param kb
     */
	public void addDownloadedKByte(int kb) {
		mDownloadedKByte += kb;
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns the current downloaded KByte.
	 * @return
	 */
	public int getDownloadedKByte() {
		return mDownloadedKByte;
	}
}
