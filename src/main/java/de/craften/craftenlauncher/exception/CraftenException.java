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
package de.craften.craftenlauncher.exception;

@SuppressWarnings("serial")
public class CraftenException extends Exception {
	public CraftenException() {
		// TODO Auto-generated constructor stub
	}

	public CraftenException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CraftenException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CraftenException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CraftenException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
}
