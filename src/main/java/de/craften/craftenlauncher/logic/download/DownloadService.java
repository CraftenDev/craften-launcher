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
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.download;

import java.io.File;
import java.util.Stack;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.download.loader.JarDownloader;
import de.craften.craftenlauncher.logic.download.loader.LibraryDownloader;
import de.craften.craftenlauncher.logic.download.loader.RessourceDownloader;
import de.craften.craftenlauncher.logic.download.loader.SkinDownloader;
import de.craften.craftenlauncher.logic.json.JSONReader;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPathImpl;
import de.craften.craftenlauncher.logic.resources.Version;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.craftenlauncher.logic.vm.DownloadVM;
import de.craften.craftenlauncher.logic.vm.SkinVM;

public class DownloadService implements Runnable{
	private boolean mRunning;
	private boolean mFinRessources,mFinJar,mFinLibraries;
	private Stack<DownloadTasks> mCurrentTasks;
	private DownloadVM mDownloadVM;
	private MinecraftPathImpl mMinecraftPath;
	private MinecraftVersion mCurrentVersion;
	
	private RessourceDownloader mRessDownloader;
	
	/**
	 * Erstellt einen neuen Download Service. Fuer die Downloads werden alle aktuellen
	 * Minecraft Pfade benoetigt ( NewPath ). Die DownloadVM stellt den Zuganngspunkt fuer
	 * die GUI dar.
	 * @param mcPath
	 * @param downVM
	 */
	public DownloadService(MinecraftPathImpl mcPath, DownloadVM downVM) {
		this.mDownloadVM = downVM;
		DownloadHelper.setDownloadHelper(downVM);
		this.mDownloadVM.setProgressBarToNull();
		this.mMinecraftPath = mcPath;
		this.mCurrentTasks = new Stack<DownloadTasks>();
		this.mRunning = true;
	}
	
	public synchronized void addTask(DownloadTasks task) {
		mCurrentTasks.push(task);
	}

	@Override
	public void run() {
		Logger.logDebug("DownloadService started!");
		
		while ( mRunning ) {
			if(mCurrentVersion == null) {
				Thread.yield();
				continue;
			}
			
			DownloadTasks task = getTask();
			
			if(task != null) {
				try {
					doTask(task);
				} catch(Exception e) {
					//TODO: Test-Workaround damit unter keinen Umstaenden der Service stirbt.
					Logger.logError("Unkown exception in DownloadService: " + e.getMessage());
					
					if(DownloadTasks.ressources != task) {
						Logger.logInfo("Adding Task in run() again");
						addTask(task);
					}
				}
			}
		}
	}
	
	private synchronized DownloadTasks getTask() {
		if(!mCurrentTasks.isEmpty()) {
			return mCurrentTasks.pop();
		}
		
		return null;
	}
	
	private void doTask(DownloadTasks task) {
		switch(task) {
			case ressources: 
				downloadRessources();
				break;
				
			case jar:
				downloadJar();
				break;
				
			case libraries:
				downloadLibraries();
				break;
				
			default:
				break;
		}
		
		mDownloadVM.setMinecraftDownload(isFinished());
	}

	private void downloadRessources() {
		Logger.logInfo("Starting download ressources");
		
		mRessDownloader = new RessourceDownloader(mCurrentVersion, mMinecraftPath.getResourcePath(), mDownloadVM);
		
		try {
			mRessDownloader.download();
			mFinRessources = true;
		} catch (CraftenDownloadException e) {
			//Sonderfall kein finRes=false da Minecraft auch mit nicht vollstaendigen Ressourcen gestartet werden kann / soll.
			Logger.logError("Error in downloadRessources: " + e.getMessage() + " -> Adding Task again!");
			addTask(DownloadTasks.ressources);
		}
		Logger.logInfo("Resources-Download finished.");
	}
	
	private void downloadJar() {
		Logger.logInfo("Starting download jar");
		
		JarDownloader jarDown = new JarDownloader(mCurrentVersion, mMinecraftPath);
		
		try {
			jarDown.download();
			mFinJar = true;
		} catch (CraftenDownloadException e) {
			Logger.logError("Jar could not be downloaded: " + e.getMessage() + " -> Adding Task again!");
			mFinJar = false;
			addTask(DownloadTasks.jar);
		}
		
		Logger.logInfo("Jar-Download finished.");
		mDownloadVM.updateProgress(10);
	}
	
	private void downloadLibraries() {
		Logger.logInfo("Starting download libraries");
		
		LibraryDownloader libDown = new LibraryDownloader(mCurrentVersion, mMinecraftPath, mDownloadVM);
		
		try {
			libDown.download();
			mFinLibraries = true;
		} catch (CraftenDownloadException e) {
			Logger.logError("Possible Version Json could not be downloaded: " + e.getMessage() + " -> Adding Task again!");
			mFinLibraries = false;
			addTask(DownloadTasks.libraries);
		}
		Logger.logInfo("Libaries-Download finished.");
	}
	
	public synchronized void setRunning(boolean running) {
		this.mRunning = running;
		
		if(!running && mRessDownloader != null) {
			mRessDownloader.stopDownload();
		}
	}
	
	public boolean isFinished() {
		return (mFinRessources && mFinLibraries && mFinJar);
	}

	public void setMinecraftVersion(MinecraftVersion version) throws CraftenLogicException {	
		try {
			DownloadHelper.downloadFileToDiskWithoutCheck(DownloadURLHelper.URL_VERSION + version.getVersion() + "/" + version.getVersion()
					+ ".json", mMinecraftPath.getMinecraftVersionsDir() + version.getVersion()+ File.separator, version.getVersion() + ".json");
			
			Version vers = JSONReader.readJsonFileFromSelectedVersion(mMinecraftPath.getMinecraftJarPath() + version.getVersion() + ".json");
			
			version.setVersionJson(vers);
		} catch (CraftenDownloadException e) {
			Logger.logError("Could not download Json File: " + version.getVersion() + ".json -> Error: " + e.getMessage());
			
			throw new CraftenDownloadException("Could not download Json File: " + version.getVersion() + ".json");
		}
		
		this.mCurrentVersion = version;
	}
	
	/**
	 * Loads skin for given username.
	 * @param skin
	 * @param username
	 */
	public void downloadSkin(SkinVM skin,String username) {
		final SkinDownloader downloader = new SkinDownloader(username,skin);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					downloader.download();
				} catch (CraftenDownloadException e) {
					//TODO Use the standard skin
				}
			}
		});
		
		t.start();
	}
}
