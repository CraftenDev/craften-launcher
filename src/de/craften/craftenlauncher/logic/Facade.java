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
import java.util.Observer;

import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.download.DownloadHelper;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.util.UIParser;

public class Facade {
	private static Facade instance = new Facade();
	private LogicController mController;
	
	private Facade() {
		mController = new LogicController();
	}
	
	/**
	 * Liefert eine Instanz der Facade zur�ck.
	 * @return
	 */
	public static Facade getInstance() {
		return instance;
	}
	
	/**
	 * Initialisiert alle ben�tigten Klassen. Muss zum Start der Applikation aufgerufen werden!!!!
	 * @throws CraftenLogicException 
	 */
	public void init(UIParser parser) throws CraftenLogicException {
		mController.setParser(parser);
		mController.init();
	}
	
	/**
	 * F�hrt zu einem forced Download. Das hei�t alle Dateien werden neu heruntergeladen.
	 * @param force
	 */
	public void setForce(boolean force) {
		DownloadHelper.setForce(force);
	}
	
	/**
	 * Funktion zum setzen eines Benutzers beim Login
	 * @param username
	 * @param password
	 * @throws CraftenLogicException
	 */
	public void setUser(String username, char[] password) throws CraftenLogicException {
		mController.setUser(username,password);
	}
	
	/**
	 * Liefert den aktuellen Benutzer zur�ck. Liefert ebenfalls einen gespeicherten Benutzer, oder einen Benutzer aus den Startparametern.
	 * @return
	 */
	public MinecraftUser getUser() throws CraftenLogicException {
		return mController.getUser();
	}
	
	/**
	 * Authentifiziert den aktuellen Benutzer.
	 * @throws CraftenLogicException
	 */
	public void authenticateUser() throws CraftenLogicException {
		mController.authenticateUser();
	}

	/**
	 * Setzt den Namens-String der Minecraft-Version welche gestartet werden soll.
	 * Startet au�erdem den Download-Prozess f�r diese Version um die restlichen Datein zu laden ( Jar und Libraries )
	 * @param version
	 * @throws CraftenLogicException Exception wird geworfen falls es diese Version nicht gibt, bzw. diese Version nicht gestartet werden kann
	 */
	public void setMinecraftVersion(String version) throws CraftenLogicException {
		mController.setMinecraftVersion(version);
	}
	
	/**
	 * Gibt die aktuelle Minecraft Version zur�ck.
	 * @throws CraftenLogicException Exception wird geworfen falls noch keine Version gesetzt ist.
	 */
	public MinecraftVersion getMinecraftVersion() throws CraftenLogicException {
		return mController.getMinecraftVersion();
	}
	
	/**
	 * Gibt eine Liste startbarer Versionen zur�ck. �rsprungliche Liste von minecraft.net, erg�nzt um die bereits lokal vorhandenen Versionen
	 * sowie einer per Startparameter �bergebenen Version.
	 * @return Liste m�glicher Versionen
	 * @throws CraftenLogicException Exception wird geworfen falls es nicht m�glich war die Liste aufzubauen.
	 */
	public ArrayList<String> getMinecraftVersions() throws CraftenLogicException {
		return mController.getMinecraftVersions();
	}
	
	/**
	 * Start Minecraft in einem neuen Prozess
	 * @throws CraftenLogicException Exception wird geworfen falls das Starten nicht m�glich war.
	 */
	public void startMinecraft() throws CraftenLogicException {
		mController.startMinecraft();
	}
	
	/**
	 * Liefert f�r einen bestimmten Minecraft Argument den Wert zur�ck.
	 * z.B. server = Gibt die Server-Adresse welche zum starten verwendet werden soll.
	 * weitere folgen
	 * @param key
	 * @throws CraftenLogicException Wird geworfen, falls es den Key nicht gibt.
	 */
	public String getMinecraftArgument(String key) throws CraftenLogicException{
		return mController.getMinecraftArgument(key);
	}
	
	/**
	 * Erm�glicht das Setzen von Minecraft Argumenten.
	 * @param key
	 * @param value
	 * @throws CraftenLogicException Wird geworfen falls das Setzen nicht m�glich war.
	 */
	public void setMinecraftArgument(String key, String value) throws CraftenLogicException {
		mController.setMinecraftArguments(key, value);
	}
	
	/**
	 * Liefert alle vorhandenen Minecraft Argumente.
	 * @return
	 * @throws CraftenLogicException
	 */
	public HashMap<String,String> getMinecraftArguments() throws CraftenLogicException {
		return mController.getMinecraftArguments();
		
	}
	
	/**
	 * Gibt an ob der Launcher mit Auto-Login gestartet wurde.
	 * @return
	 */
	public boolean isAutoLogin() {
		return false;
	}
	
	/**
	 * Setzt einen Observer auf die GUIAccess-Klasse. Damit kann angezeigt werden was gerade heruntergeladen wird.
	 * @param server
	 */
	public void setMinecraftDownloadObserver(Observer server) {
		mController.setDownloadObserver(server);
	}
	
	/**
	 * Setzt einen Observer um den Skin download zu �berwachen. Startet au�erdem direkt den Skin-Download!
	 * Darf erst gesetzt werden nachdem der Nutzer authentifiziert wurde!
	 * @param server
	 */
	public void setSkinObserver(Observer server) {
		mController.setSkinObserver(server);
	}
	
	/**
	 * Gibt true wenn Minecraft komplett heruntergeladen wurde.
	 * Darf erst gesetzt werden nachdem der Nutzer authentifiziert wurde!
	 * @return
	 */
	public boolean isMinecraftDownloaded() {
		return mController.isMinecraftDownloaded();
	}
	
	/**
	 * Loggt den aktuellen Nutzer des Craften Launchers aus.
	 */
	public void logout() {
		mController.logout();
	}

    /**
     * Gibt true wenn Minecraft direkt gestartet werden soll
     * @return boolean
     */
    public boolean isQuickPlay(){
        return mController.isQuickPlay();
    }
}
