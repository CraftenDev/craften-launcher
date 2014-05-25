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
 * Main-function of the Craften Launcher.
 * Starts Logic and GUI Layer.
 * 
 * Commandline input format:
 * --[parameter]=[value]
 * 
 * Used parameters:
 * --version    = [14w11b, 1.7.5, ...]defines the Minecraft-Version
 * --user       = Minecraft-Username (not implemented)
 * --pass       = Minecraft-Password (not implemented)
 * --server     = [192.168.2.1, 192.168.2.1:PORT, subdomain.name.de, subdomain.name.de:PORT] IP-Adress of a server to join + Port if needed
 * --mcpath     = Minecraft-Path
 * --xmx        = [1024m,4g, ....] ram-amount with witch mc will be started
 * --quickplay  = [] skip profile page and start minecrat directly
 * --profileid  = [54bnb5br_profileid_berb4b3] chooses a saved user
 * --forcelogin = erzwingt das Einloggen eines Users
 * --fullscreen = starts minecraft in fullscreen mode
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher;

import static javax.swing.SwingUtilities.invokeLater;

import de.craften.craftenlauncher.GUI.Manager;
import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.util.OSHelper;
import de.craften.util.UIParser;

import java.text.SimpleDateFormat;
import java.util.Date;

class Application {

	public static void main(String[] args) throws CraftenLogicException {
        Logger.getInstance().logInfo("Launcher started!");
        Logger.getInstance().logDebug("OS : " + System.getProperty("os.name"));
        Logger.getInstance().logDebug("OS Arch: " + OSHelper.getInstance().getOSArch());
        Logger.getInstance().logDebug("OS Java Arch : " + System.getProperty("os.arch"));
        Logger.getInstance().logDebug("OS Version : " + System.getProperty("os.version"));
        Logger.getInstance().logDebug("Username : " + System.getProperty("user.name"));
        Logger.getInstance().logDebug("Java Vendor : " + System.getProperty("java.vendor"));
        Logger.getInstance().logDebug("Java Version : " + System.getProperty("java.version"));
        Logger.getInstance().logDebug("Java Home : " + System.getProperty("java.home"));
        Logger.getInstance().logDebug("Java Classpath : " + System.getProperty("java.class.path"));
        Logger.getInstance().logDebug("Available processors (cores): " +
                Runtime.getRuntime().availableProcessors());
        Logger.getInstance().logDebug("Total memory (bytes): " +
                Runtime.getRuntime().totalMemory());
        Logger.getInstance().logInfo("Date: " + new SimpleDateFormat("dd.MM.yy").format(new Date()));

		Facade.getInstance().init(new UIParser(args));
		startGUI();
	}

	private static void startGUI() {
		Logger.getInstance().logInfo("loading gui!");

		invokeLater(new Runnable() {

            @Override
            public void run() {
                Manager.getInstance().init();
            }
        });
	}

}
