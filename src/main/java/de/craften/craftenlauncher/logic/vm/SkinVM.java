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
package de.craften.craftenlauncher.logic.vm;

import java.awt.image.BufferedImage;
import java.util.Observable;

public class SkinVM extends Observable{
	
	private BufferedImage mSkin;
	
	public SkinVM() {
		
	}
	
	public boolean wasSkinDownloaded() {
		return ( mSkin != null );
	}
	
	public void setSkinDownloaded(BufferedImage skin) {
		this.mSkin = skin;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public BufferedImage getSkin() {
		return mSkin;
	}
}
