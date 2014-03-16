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
package de.craften.craftenlauncher.logic;

public class MinecraftUser {
	private String mUsername, mPassword;
	private String mSession, mAccessToken, mClientToken, mProfileId;
	private String mResponse;
	
	//TODO Refacoteren. Vllt. auslagern in den AuthService?
	private boolean mLoggedIn;
	
	public MinecraftUser(String username,String password) {
		this.mUsername = username;
		this.mPassword = password;
	}
	
	public void setAuthentication(String username,String session, String accessToken, String clientToken, String profileId,String response) {
		this.mUsername = username;
		this.mSession = session;
		this.mAccessToken = accessToken;
		this.mClientToken = clientToken;
		this.mProfileId = profileId;
		this.mResponse = response;
	}
	
	public String getUsername() {
		return mUsername;
	}
	
	public String getPassword() {
		return mPassword;
	}

	public String getSession() {
		if(mSession == null) {
			 mSession = "token:" + getAccessToken() + ":" + getProfileId();
		}
		return mSession;
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public String getClientToken() {
		return mClientToken;
	}

	public String getProfileId() {
		return mProfileId;
	}
	
	public String getResponse() {
		return mResponse;
	}
	
	/**
	 * Returns true if the user was logged in successfully.
	 * @return
	 */
	public boolean isLoggedIn() {
		return mLoggedIn;
	}
	
	/**
	 * Bedeutet das dieser Nutzer erfolgreich eingeloggt wurde.
	 */
	public void loggingInSuccess() {
		mLoggedIn = true;
	}
}
