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

	public DownloadVM() {
	}
	
	public void updateDownloadFile(final String filename) {
		updateStatusInfo("File: " + filename);
	}
	
	public void updateStatusInfo(final String info) {
		this.mInfo += " " + info;

        setChanged();
        notifyObservers();
	}
	
	public String getInfo() {
		String i = this.mInfo;
		
		this.mInfo = "";
		
		return i;
	}
	
	public void updateStatus(final String status) {
		this.mStatus += " " + status;
		
		setChanged();
		notifyObservers();
	}
	
	public String getStatus() {
		String s = this.mStatus;
		
		this.mStatus = "";
		
		return s;
	}
	
	/**
	 * Adds the progess value to the current progress.
	 * @param progress
	 */
	public synchronized void updateProgress(int progress) {
		this.mProgress += progress;
		
		setChanged();
        notifyObservers();
	}
	
	public int getProgress() {
		return mProgress;
	}
	
	/**
	 * Um den Fortschritt zurueckzusetzen.
	 */
	public synchronized void setProgressBarToNull() {
		mProgress = 0;
	}
	
	public void setMinecraftDownload(boolean downloaded) {
		this.mDownloaded = downloaded;
		
		setChanged();
		notifyObservers();
	}
	
	public boolean isMinecraftDownloaded() {
		return mDownloaded;
	}
	
	public void addDownloadedKByte(int kb) {
		mDownloadedKByte += kb;
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Liefert die Anzahl heruntergeladener KByte-Bloecke.
	 * @return
	 */
	public int getDownloadedKByte() {
		return mDownloadedKByte;
	}
}
