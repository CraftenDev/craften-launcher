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
 * Facade Class:
 * 
 * Interface between GUI and Logic.
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observer;

import de.craften.craftenlauncher.exception.CraftenException;
import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.util.UIParser;

/**
 * Facade class separates logic and gui components / classes.
 *
 * @author redbeard
 * @author saschb2b
 */
public class Facade {
    // Implements the singleton pattern.
	private static Facade mInstance = new Facade();

	private LogicController mController;
	
	private Facade() {
		mController = new LogicController();
	}
	
	/**
	 * Returns an instance of facade.
	 * @return
	 */
	public static Facade getInstance() {
		return mInstance;
	}
	
	/**
     * Initializes the needed logic classes.
     * Need an {@see #UIParser} for init.
     * Has to be called first!
     *
     * @param parser holds the command line arguments.
	 * @throws CraftenLogicException when something went wrong.
	 */
	public void init(UIParser parser) throws CraftenLogicException {
		mController.setParser(parser);
		mController.init();
	}
	
	/**
	 * If setForce is set to true, all Minecraft files will be downloaded fresh
     * and replaces the local files.
     *
     * Default is false.
     *
	 * @param force
	 */
	public void setForce(boolean force) {
		DownloadHelper.setForce(force);
	}

    /**
     * Returns true of force login shall be used.
     * @return
     */
    public boolean isForceLogin(){
        return mController.isForceLogin();
    }
	
	/**
	 * Sets the username and corresponding password.
     *
	 * @param username
	 * @param password
	 * @throws CraftenLogicException when username or password are wrong or network failure
	 */
	public void setUser(String username, char[] password) throws CraftenLogicException {
		mController.setUser(username,password);
	}
	
	/**
     * Returns the current user. This user can be either a saved one, given by command line
     * or set by setUser().
     *
	 * @return
	 */
	public MinecraftUser getUser() throws CraftenLogicException {
		return mController.getUser();
	}

    /**
     * Returns a list of current saved users.
     * @return
     */
    // TODO Move AuthenticationSevice into facade class.
    public List<MinecraftUser> getUsers() {
        return mController.getUsers();
    }
	
	/**
	 * Authenticate the current user.
	 * @throws CraftenLogicException if authentication failed
	 */
	public void authenticateUser() throws CraftenException {
		mController.authenticateUser();
	}

	/**
     * Sets the Minecraft version name and starts the donwload process.
	 * @param version
	 * @throws CraftenLogicException will be thrown if this version is unknown or not startable.
	 */
	public void setMinecraftVersion(String version) throws CraftenLogicException {
		mController.setMinecraftVersion(version);
	}
	
	/**
     * Returns the current choosen Minecraft version.
	 * @throws CraftenLogicException if no version was choosen.
	 */
	public MinecraftVersion getMinecraftVersion() throws CraftenLogicException {
		return mController.getMinecraftVersion();
	}
	
	/**
	 * Returns a list of startable Versions.
     * This list contains all local versions, all minecraft.net versions and the version set through
     * command line argument.
	 * @return list of possible versions
	 * @throws CraftenLogicException if building the list was not possible.
	 */
	public ArrayList<String> getMinecraftVersions() throws CraftenLogicException {
		return mController.getMinecraftVersions();
	}
	
	/**
     * Starts minecraft in a new process.
	 * @throws CraftenLogicException if start was not possible
	 */
	public void startMinecraft() throws CraftenLogicException {
		mController.startMinecraft();
	}
	
	/**
	 * Returns a Minecraft argument for the specified key.
     * e.g server = returns the server address which shall be joined on startup.
	 * @param key
	 * @throws CraftenLogicException is thrown if the key does not exist.
	 */
	public String getMinecraftArgument(String key) throws CraftenLogicException{
		return mController.getMinecraftArgument(key);
	}
	
	/**
	 * Sets a new kv pair.
	 * @param key
	 * @param value
	 * @throws CraftenLogicException if it was not possible.
	 */
	public void setMinecraftArgument(String key, String value) throws CraftenLogicException {
		mController.setMinecraftArguments(key, value);
	}
	
	/**
	 * Returns all current arguments.
	 * @return
	 * @throws CraftenLogicException if something went wrong.
	 */
	public HashMap<String,String> getMinecraftArguments() throws CraftenLogicException {
		return mController.getMinecraftArguments();
		
	}
	
	/**
     * Sets a new observer to get information about the download status.
	 * @param server
	 */
	public void setMinecraftDownloadObserver(Observer server) {
		mController.setDownloadObserver(server);
	}
	
	/**
     * Sets a new observer to get information about the skin download.
     * Also starts the skin download. This method can be invoked if the user
     * is authenticated.
	 * @param server
	 */
	public void setSkinObserver(Observer server) {
		mController.setSkinObserver(server);
	}
	
	/**
     * Returns true if Minecraft was completely downloaded.
	 * @return
	 */
	public boolean isMinecraftDownloaded() {
		return mController.isMinecraftDownloaded();
	}
	
	/**
     * Logs out the current user.
	 */
	public void logout() {
		mController.logout();
	}

    /**
     * Returns true if minecraft should be started directly.
     * @return boolean
     */
    public boolean isQuickPlay(){
        return mController.isQuickPlay();
    }


}
