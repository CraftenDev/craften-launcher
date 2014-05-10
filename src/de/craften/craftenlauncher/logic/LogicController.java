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
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observer;

import de.craften.craftenlauncher.exception.CraftenAuthenticationFailure;
import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.exception.CraftenLogicValueIsNullException;
import de.craften.craftenlauncher.exception.CraftenUserException;
import de.craften.craftenlauncher.logic.auth.AuthenticationService;
import de.craften.craftenlauncher.logic.auth.Profiles;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import de.craften.craftenlauncher.logic.download.DownloadService;
import de.craften.craftenlauncher.logic.download.DownloadTasks;
import de.craften.craftenlauncher.logic.minecraft.MinecraftInfo;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPathImpl;
import de.craften.craftenlauncher.logic.minecraft.MinecraftProcess;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.craftenlauncher.logic.version.VersionListHelper;
import de.craften.craftenlauncher.logic.vm.DownloadVM;
import de.craften.craftenlauncher.logic.vm.SkinVM;
import de.craften.util.UIParser;

public class LogicController {
	private MinecraftUser mUser;
	private AuthenticationService mAuthService;
	private DownloadService mDownService;
	private UIParser mParser;
	private VersionListHelper mVersionList;
	private MinecraftPathImpl mMinecraftPath;
	private MinecraftVersion mCurrentVersion;
	private HashMap<String,String> mMincraftArgs;
	private Profiles mProfiles;
    private boolean mQuickPlay;
	
	private DownloadVM mDownloadVM;
	private SkinVM mSkinVM;
	
	public LogicController() {
		logLauncherVersion();
		mAuthService = new AuthenticationService();
		mDownloadVM = new DownloadVM();
		mMincraftArgs = new HashMap<String, String>();
	}
	
	private void logLauncherVersion() {
	    Package objPackage = this.getClass().getPackage();
	    
	    // examines the package object 
	    String version = objPackage.getSpecificationVersion();
	    
	    if(version != null) {
	    	Logger.getInstance().logInfo("Launcher version: " + version);
	    } else {
	    	Logger.getInstance().logInfo("Launcher version: 0.11.0");
	    }
	}

    /**
     * Inits the logic layer.
     */
	public void init() {
		if(mParser.hasArg("mcpath")) {
			mMinecraftPath = new MinecraftPathImpl(mParser.getArg("mcpath"));
		}
		else {
			mMinecraftPath = new MinecraftPathImpl();
		}
		
		if(mParser.hasArg("server")) {
			mMincraftArgs.put("server", mParser.getArg("server"));
		}
		
		if(mParser.hasArg("xmx")) {
			mMincraftArgs.put("xmx", mParser.getArg("xmx"));
		}
		
		mVersionList = new VersionListHelper(mMinecraftPath);
		
		if(mParser.hasArg("version")) {
			String version = mParser.getArg("version");
			
			try {
				if(mVersionList.isVersionAvailableOnline(version)) {
					this.mCurrentVersion = new MinecraftVersion(version);	
				} else {
					mVersionList.checkVesion(version);
					this.mCurrentVersion = new MinecraftVersion(version);
				}

                mMincraftArgs.put("version", "true");
				mMinecraftPath.setVersionName(version);
			} catch (Exception e) {
				Logger.getInstance().logInfo("Version not available: " + mParser.getArg("version"));
			}
		}

        if(mParser.hasKey("quickplay")) {
            mQuickPlay = true;
        }

		mAuthService.setMcPath(mMinecraftPath);
		Profiles login = mAuthService.readProfiles();
		
		if(login != null) {
			Logger.getInstance().logInfo("craftenlauncher_profiles found! Username is: " + login.getSelectedUser().getUsername());

            if(mParser.hasArg("profileid")) {
                login.changeSelectedUser(mParser.getArg("profileid"));
            }

			mUser = new MinecraftUser(login.getSelectedUser().getUsername(),"pw");
			
			//TODO checken was genau die Response ist und was man damit so anfaengt!
			//user.setAuthentication(showProfile.getUsername(), null, showProfile.getAccessToken(), showProfile.getClientToken(), showProfile.getProfileId(),null);
			
			mProfiles = login;
		}
		else {
			Logger.getInstance().logInfo("No craftenlauncher_profiles found at Position: " + mMinecraftPath.getMinecraftDir() + "craftenlauncher_profiles.json");
		}
	}
	
	public void setUser(String username, char[] password) throws CraftenLogicValueIsNullException {
        //TODO weiter char[] durchziehen wegen Sicherheit.
        String pass = String.valueOf(password);

        if(username == null || username.equals(" ") || username.equals("")) {
        	Logger.getInstance().logError("Username is null!");
			throw new CraftenLogicValueIsNullException("Username is missing!");
		}
		if(pass.equals(" ") || pass.equals("")) {
			Logger.getInstance().logError("Password is null!");
			throw new CraftenLogicValueIsNullException("Password is missing!");
		}
		mUser = new MinecraftUser(username,pass);
		mProfiles = null;
	}
	
	public MinecraftUser getUser() throws CraftenLogicException{
		if(mUser != null) {
			return mUser;
		}
		else {
			Logger.getInstance().logError("No User known!");
			throw new CraftenUserException("Username / Password not correct!");
		}
	}

	public void authenticateUser() throws CraftenLogicException {
		String session;
		
		System.out.println("Authenticate!");
		
		if(mProfiles == null) {
			session = mAuthService.getSessionID(mUser.getEmail(), mUser.getPassword());
		}
		else {
			session = mAuthService.getSessionID(mProfiles);
			
		}
		
		if(session == null) {
			System.out.println("Error!");
			throw new CraftenAuthenticationFailure("Error while authenticating!");
		}
		mUser.setUsername(mAuthService.getName());
        mUser.setAccessToken(mAuthService.getAccessToken());
        mUser.setClientToken(mAuthService.getClientToken());
        mUser.setProfileId(mAuthService.getProfileId());
        mUser.setResponse(mAuthService.getResponse());
        mUser.setSession(session);
		mUser.loggingInSuccess();

		startDownloadService();
	}
	
	private void startDownloadService() throws CraftenLogicException {
		if(mDownService != null) {
			return;
		}
		
		if(!mUser.isLoggedIn()) {
			Logger.getInstance().logError("Trying to start DownloadService although user is not logged in!");
		}
		
		mDownService = new DownloadService(mMinecraftPath,mDownloadVM);
		
		if(mCurrentVersion != null) {
			try {
				mDownService.setMinecraftVersion(mCurrentVersion);
			} catch (Exception e) {
				//TODO Workaround vllt. klappt es beim zweiten Mal.
				Logger.getInstance().logInfo("Trying again to download json!");
				mDownService.setMinecraftVersion(mCurrentVersion);
			}
			mDownService.addTask(DownloadTasks.ressources);
			mDownService.addTask(DownloadTasks.jar);
			mDownService.addTask(DownloadTasks.libraries);
		}
		
        new Thread(mDownService).start();
	}
	
	public void logout() {
		Logger.getInstance().logInfo("Trying to logout user: " + mUser.getUsername());
		
		String name = mUser.getUsername();
		
		mUser = null;
		// TODO: Muss das unbedingt null werden?
		// mCurrentVersion = null;
		mProfiles = null;
		mDownloadVM.setProgressBarToNull();
		mAuthService.deleteProfiles();
		mAuthService = new AuthenticationService();
		
		if(mDownService != null) {
			Logger.getInstance().logInfo("Shutting down DownloadService because of logout");
			mDownService.setRunning(false);
				
			mDownService = null;
		}

		Logger.getInstance().logInfo("User " + name + " was locked out!");
	}
	
	//TODO vorher besser alten Service ordentlich stoppen oder neu Init?
	public void setMinecraftVersion(String version) throws CraftenLogicException {
		this.mVersionList.checkVesion(version);
		this.mCurrentVersion = new MinecraftVersion(version);
		
		mMinecraftPath.setVersionName(version);
		if(mDownService != null) {
			mDownService.setRunning(false);
			
			mDownService = null;
		}
		
		startDownloadService();
	}
	
	public MinecraftVersion getMinecraftVersion() throws CraftenLogicException {
		if(mCurrentVersion == null) {
			throw new CraftenLogicException();
		}
		return mCurrentVersion;
	}
	
	public ArrayList<String> getMinecraftVersions() {
		return mVersionList.getVersionsList();
	}
	
	public boolean isMinecraftDownloaded() {
		return mDownService.isFinished();
	}

    public boolean isQuickPlay(){
        return mQuickPlay;
    }
	
	public void startMinecraft() throws CraftenLogicException {
		if(!isMinecraftDownloaded()) {
			Logger.getInstance().logError("Minecraft has not been downloaded fully!");
			throw new CraftenLogicException("Minecraft has not been downloaded fully yet!");
		}
		
		if(!mUser.isLoggedIn()) {
			Logger.getInstance().logError("Trying to start Minecraft although User is not logged in!");
			throw new CraftenLogicException("Trying to start Minecraft although User is not logged in!");
		}
		
		MinecraftInfo info = new MinecraftInfo(mCurrentVersion.getVersion());
		info.setUser(mUser);
		info.setMSV(mCurrentVersion);
		info.setMinecraftPath(mMinecraftPath);
		info.setXMX(mMincraftArgs.get("xmx"));
		//TODO Server und Port Anfrage ueberpruefen und falls noetig korrigieren.
		String server = mMincraftArgs.get("server");
		info.setServerAdress(server);
		
		MinecraftProcess process = new MinecraftProcess(info, info.getMSV().getVersionJson());
		
		process.startMinecraft();
		
		if(!process.getSuccess()) {
			Logger.getInstance().logError("Minecraft Process could not be started!");
			throw new CraftenLogicException("Minecraft Process could not be started!");
		}
		else {
			System.exit(0);
		}
	}
	
	public void setParser(UIParser parser) throws CraftenLogicValueIsNullException {
		if(parser == null) {
			Logger.getInstance().logError("UI Parser was null!");
			throw new CraftenLogicValueIsNullException("Parser must not be null!");
		}
		this.mParser = parser;
	}

	public void setDownloadObserver(Observer server) {
		mDownloadVM.addObserver(server);
	}
	
	public HashMap<String,String> getMinecraftArguments() {
		return new HashMap<String, String>(mMincraftArgs);
	}
	
	public String getMinecraftArgument(String key) throws CraftenLogicException {
		String argument = mMincraftArgs.get(key);
		
		if(argument != null) {
			return mMincraftArgs.get(key);
		} else {
			Logger.getInstance().logError("Trying to get Argument for key: " + key);
			throw new CraftenLogicException("No Argument for key: " + key);
		}
	}
	
	//TODO Sicherheits-Checks mit einbauen.
	public void setMinecraftArguments(String key, String value) {
		mMincraftArgs.put(key, value);
	}
	
	public void setSkinObserver(Observer server) {
		mSkinVM = new SkinVM();
		mSkinVM.addObserver(server);
		
		if( mDownService != null ) {
			mDownService.downloadSkin(mSkinVM, mUser.getUsername());
		}
	}

    public List<MinecraftUser> getUsers() {
        return mAuthService.getUsers();
    }
}
