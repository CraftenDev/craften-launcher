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
 * LastLogin class:
 * 
 * @author saschb2b
 */
package de.craften.craftenlauncher.logic.auth;


public class LastLogin {
    private String mUsername;
    private String mEMail;
    private String mAccessToken;
    private String mProfileID;
    private String mClientToken;
    private String mPath;

    public LastLogin(String path) {
        this.mPath = path;
    }

    public LastLogin(String email, String username, String accessToken, String profileID, String clientToken) {
        setEmail(email);
        setUsername(username);
        setAccessToken(accessToken);
        setProfileID(profileID);
        setClientToken(clientToken);
    }

    public void setEmail(String email) {
        this.mEMail = email;
    }

    public String getEmail() {
        return mEMail;
    }

    public boolean hasEmail() {
        return getEmail() != null;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getPath() {
        return mPath;
    }

    public void setClientToken(String clientToken) {
        this.mClientToken = clientToken;
    }

    public String getClientToken() {
        return mClientToken;
    }

    public void setProfileID(String profileID) {
        this.mProfileID = profileID;
    }

    public String getProfileID() {
        return mProfileID;
    }

    public void setAccessToken(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public boolean isAccessTokenValid(){
        return AuthenticationService.isValid(getAccessToken());
    }
}
