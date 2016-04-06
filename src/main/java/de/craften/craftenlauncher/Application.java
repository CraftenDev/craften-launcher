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
 * <p>
 * <p>
 * Commandline input format:
 * --[parameter]=[value]
 * <p>
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
 */
package de.craften.craftenlauncher;

import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.util.OSHelper;
import de.craften.util.UIParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * Craften Launcher main class.
 * Logs several infos about the system and starts / initializes the logic and gui.
 *
 * @author redbeard
 * @author saschb2b
 */
class Application {
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    /**
     * Main Method!
     *
     * @param args
     * @throws CraftenLogicException
     */
    public static void main(String[] args) throws CraftenLogicException {
        logSystemInfo();
        Facade.getInstance().init(new UIParser(args));
        startGUI();
    }

    /**
     * Logs several system information.
     */
    private static void logSystemInfo() {
        LOGGER.info("Launcher started!");
        LOGGER.debug("OS : " + System.getProperty("os.name"));
        LOGGER.debug("OS Arch: " + OSHelper.getOSArch());
        LOGGER.debug("OS Java Arch : " + System.getProperty("os.arch"));
        LOGGER.debug("OS Version : " + System.getProperty("os.version"));
        LOGGER.debug("Username : " + System.getProperty("user.name"));
        LOGGER.debug("Java Vendor : " + System.getProperty("java.vendor"));
        LOGGER.debug("Java Version : " + System.getProperty("java.version"));
        LOGGER.debug("Java Home : " + System.getProperty("java.home"));
        LOGGER.debug("Java Classpath : " + System.getProperty("java.class.path"));
        LOGGER.debug("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
        LOGGER.debug("Total memory (bytes): " + Runtime.getRuntime().totalMemory());
        LOGGER.info("Date: " + new SimpleDateFormat("dd.MM.yy").format(new Date()));
    }

    /**
     * Starts the gui by invoking the gui manager.
     */
    private static void startGUI() {
        LOGGER.debug("Starting GUI");

        invokeLater(new Runnable() {
            @Override
            public void run() {
                MainController.getInstance().openMainWindow();
            }
        });
    }
}
