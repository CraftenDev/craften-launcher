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
 * Loader-class for the "new" mc-version.
 * Loads all needed ressources from mojang.
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.download.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.download.DownloadURLHelper;
import de.craften.craftenlauncher.logic.resources.Index;
import de.craften.craftenlauncher.logic.resources.ResEntry;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.craftenlauncher.logic.vm.DownloadVM;

public class RessourceDownloader implements Downloader{
	private String mResDir, mResURL, mIndexesURL;
	private DownloadVM mAccess;
	private MinecraftVersion mVersion;
	private Index mIndex;
    private boolean mDownloadable, mStop;
    
    private ExecutorService executor;
	
	public RessourceDownloader(MinecraftVersion version,String resDir, DownloadVM access) {
		this.mResURL = DownloadURLHelper.URL_RESOURCES;
		this.mIndexesURL = DownloadURLHelper.URL_INDEXES;
		this.mResDir = resDir;
		this.mAccess = access;
		this.mVersion = version;
		
		int poolSize = Runtime.getRuntime().availableProcessors();
		
		if(poolSize <= 4) {
			poolSize *= 2;
		}
		
		Logger.getInstance().logDebug("RessourceDownloader: PoolSize " + poolSize );
		
		this.executor = Executors.newFixedThreadPool(poolSize);
		
		downloadIndexFile();
	}
	
    private void downloadIndexFile() {
    	String fs = File.separator;
    	boolean force = DownloadHelper.getForce();
    	
    	DownloadHelper.setForce(true);
    	String assets = "";
    	
    	if(mVersion.getVersionJson().hasAssets()) {
    		assets = mVersion.getVersionJson().getAssets();
    	}
    	else {
    		assets = "legacy";
    	}
    	
        try {
			DownloadHelper.downloadFileToDiskWithCheck(mIndexesURL+ assets + ".json", mResDir + fs + "indexes" + fs, assets + ".json");
			mIndex = new Index(mResDir + fs + "indexes" + fs + assets + ".json");
			mDownloadable = true;
		} catch (CraftenDownloadException e) {
			Logger.getInstance().logInfo("Could not download " + assets + ".json.");
		}
        
        DownloadHelper.setForce(force);
    }
    
    @Override
	public void download() throws CraftenDownloadException {
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<Future<String>> futures = new ArrayList<Future<String>>();
		
		
		if(!mDownloadable) {
			throw new CraftenDownloadException("Ressource not downloadable!");
		}
		
		for(final ResEntry res : mIndex.getRes()) {
			if(mStop) {
				break;
			}
			files.add(mResDir + res.getPath());
			
			Future<String> f = executor.submit(getCallable(res));
			futures.add(f);
		}
		
		//TODO Logging this on a lower warn level!
		for(Future<String> s : futures) {
			try {
				String se = s.get	();
				
				files.remove(se);
			} catch (Exception e) {
				Logger.getInstance().logError("Error while Handling Future: " + e.getMessage());
			}
		}
		
		
		checkDownloadedFilesNumber(files);
		
		executor.shutdown();
	}
	
	private Callable<String> getCallable(final ResEntry res) {
		final String file = mResDir + res.getPath();
		
		Callable<String> c = new Callable<String>() {

			@Override
			public String call() throws Exception {
				Logger.getInstance().logInfo("File Download started: " + file);
				mAccess.updateDownloadFile(res.getName());
				
				try {
					DownloadHelper.downloadFileToDiskWithoutCheck(mResURL + res.getDownloadPath(), file);
				} catch (CraftenDownloadException e) {
					Logger.getInstance().logError("Could not download " + res.getName());
				}
				mAccess.updateProgress(1);
				
				
				return file;
			}
		};
		
		return c;
	}

	private void checkDownloadedFilesNumber(ArrayList<String> files) {
		if(files.size() == 0) {
			Logger.getInstance().logInfo("Finished with all Ressource-Downloads");
		} else {
			Logger.getInstance().logError("Not all files where removed from files-list");
		}
	}
	
	public void stopDownload() {
		mStop = true;
		executor.shutdown();
	}
}
