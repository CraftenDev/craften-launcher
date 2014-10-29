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
 * Logger Class:
 * 
 * Simple log-class. Logs info, debug, warning and error messages with a timestamp and 
 * a label ( e.g. [INFO] ). Message are written in a simple text file.
 * The log level is determined by an integer. 
 * The higher the integer, the lower the log level.
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher.logic;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger Class which supports several log level.
 *
 * @author redbeard
 */
public class Logger {
    private static final int LOG_LEVEL = 10;
	private static Logger instance = new Logger();
	private PrintStream mOutput;
	
	private Logger() {
		try {
			mOutput = new PrintStream(new FileOutputStream("LogFile.log"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not open file!");
			e.printStackTrace(System.err);
		}
	}

    /**
     * Appends the given message with the current timestamp to the log file.
     *
     * @param message Mesasge to be logged.
     */
    private synchronized void appendToLog(String message) {
        mOutput.println(getCurrentTime() + message);
    }

    /**
     * Returns the current time formatted.
     *
     * @return curent time formated in HH:mm:ss
     */
	private String getCurrentTime() {
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(new Date()) + ": ";
	}

    /**
     * Logs an info Message with a timestamp and a label [INFO]
     * @param message Mesasge to be logged.
     */
    public static void logInfo(String message) {
        if (LOG_LEVEL >= 3) {
            instance.appendToLog("[INFO] " + message);
        }

    }
	
	/**
	 * Logs an debug message with a timestamp. Shown in log through the label [DEBUG].
	 * @param message
	 */
	public static void logDebug(String message) {
		if (LOG_LEVEL >= 2) {
			instance.appendToLog("[DEBUG] " + message);
		}
	}
	
	/**
	 * Logs an warning message with a timestamp. Shown in log through the label [WARNING].
	 * @param message
	 */
	public static void logWarning(String message) {
		if (LOG_LEVEL >= 1) {
			instance.appendToLog("[WARNING] " + message);
		}
	}
	
	/**
	 * Logs an error message with a timestamp. Shown in log through the label [ERROR].
	 * @param message
	 */
	public static void logError(String message) {
		if (LOG_LEVEL > 0) {
            instance.appendToLog("[ERROR] " + message);
		}
	}
}
