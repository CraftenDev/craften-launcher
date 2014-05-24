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
 * MCInfo Class:
 * 
 * Information class which holds all needed information for a mc-version
 * to start.
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.minecraft;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.util.OSHelper;

public class MinecraftInfo {
	private String mVersion;
	private String mUsername;
	private String mSessionID, mClientToken, mAccessToken, mProfileID;
    private String mResponse;
	private MinecraftPath mMinecraftPath;
	private String mServer, mPort;
	private String mXmx, mDemo;
    private MinecraftVersion mMinecraftVersion;
    private boolean mFullscreen;

    public MinecraftInfo(String version) {
		this.mVersion = version;
	}
	
	public void setUserCredentials(String username,String session) {
		this.mSessionID = session;
		this.mUsername = username;
	}

    public String getProfileID() {
        return mProfileID;
    }

    public void setProfileID(String profileID) {
        this.mProfileID = profileID;
    }

    public void setAccessToken(String value){
        this.mAccessToken = value;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getClientToken() {
        return mClientToken;
    }

    public String getResponse() {
        return mResponse;
    }

    public void setResponse(String response) {
        this.mResponse = response;
    }

    public void setClientToken(String clientToken) {
        this.mClientToken = clientToken;
    }

    public void setServerAdress(String server) {
		if(server == null || server.equals("")) {
			server = null;
		}
		else {
            if(server.contains(":")){
                String dummy[] = server.split(":");
                this.mServer = dummy[0];
                setServerPort(dummy[1]);
            }
            else
			    this.mServer = server;
		}
	}

    public void setServerPort(String port) {
        if(port == null || port.equals("")) {
            port = null;
        }
        else {
            this.mPort = port;
        }
    }
	
	public void setMinecraftPath(MinecraftPath path) {
		this.mMinecraftPath = path;
	}

    public void setFullscreen(boolean fullscreen){
        mFullscreen = fullscreen;
    }

	public void setXMX(String xmx) {
		if(xmx == null || xmx.contains(" ")) {
			this.mXmx = null;
		}
		else {
			this.mXmx = checkXMX(xmx);
		}
	}
	
	private String checkXMX(String xmx) {
		xmx = xmx.toLowerCase();
		if(xmx.contains("g") || xmx.contains("m")) {
			String suffix = xmx.substring(xmx.length()-1);
			String value = xmx.substring(0, xmx.length()-1);
			
			int amount = Integer.decode(value);
			if(suffix.contains("g")) {
				amount *= 1024;
			}
			
			if(OSHelper.getInstance().is32bit() && OSHelper.getInstance().getOperatingSystem().equals("windows") && amount > 2048) {
				Logger.getInstance().logInfo("Trying to set Xmx="+xmx+" changing to 2048m.");
				return "2048m";
			}
		}
		
		return xmx;
	}
	
	public MinecraftPath getMinecraftPath() {
		return mMinecraftPath;
	}
	
	public boolean hasUserCredentials() {
		return (mUsername!=null && mSessionID != null);
	}
	
	public String getUsername() {
		return mUsername;
	}
	
	public String getSessionID() {
		return mSessionID;
	}
	
	public boolean hasServerAdress() {
		return (mServer!=null);
	}

    public boolean hasServerPort() {
        return (mPort!=null);
    }
	
	public String getServerAddress() {
		return mServer;
	}

    public String getServerPort() {
        return mPort;
    }
	
	public String getMinecraftVersion() {
		return mVersion;
	}
	
	public boolean hasXMX() {
		return (mXmx != null);
	}
	
	public String getXMX() {
		return mXmx;
	}

    public boolean isFullscreen() {
        return mFullscreen;
    }
    
    public void setUser(MinecraftUser user) {
    	this.mUsername = user.getUsername();
    	this.mAccessToken = user.getAccessToken();
    	this.mClientToken = user.getClientToken();
    	this.mProfileID = user.getProfileId();
    	this.mSessionID = user.getSession();
    	this.mResponse = user.getResponse();
    }
    
    public MinecraftVersion getMSV() {
    	return mMinecraftVersion;
    }
    
    public void setMSV(MinecraftVersion vs) {
    	this.mMinecraftVersion = vs;
    }
}
