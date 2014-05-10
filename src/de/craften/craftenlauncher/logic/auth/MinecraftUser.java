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
package de.craften.craftenlauncher.logic.auth;

public class MinecraftUser {
    private String mEmail, mUsername, mPassword;
    private String mSession, mAccessToken, mClientToken, mProfileId;
    private String mResponse;

    //TODO Refacoteren. Vllt. auslagern in den AuthService?
    private boolean mLoggedIn;

    public MinecraftUser() {
    }

    public MinecraftUser(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public MinecraftUser(String email, String profileId, String username, String accessToken, String clientToken) {
        setEmail(email);
        setProfileId(profileId);
        setUsername(username);
        setAccessToken(accessToken);
        setClientToken(clientToken);
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public void setClientToken(String clientToken) {
        mClientToken = clientToken;
    }

    public void setProfileId(String profileId) {
        mProfileId = profileId;
    }

    public void setSession(String session) {
        mSession = session;
    }

    public void setResponse(String response) {
        mResponse = response;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getSession() {
        if (mSession == null) {
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
     *
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

    @Override
    public boolean equals(Object obj) {
        MinecraftUser a = (MinecraftUser) obj;
        if (this.getEmail().equals(a.getEmail()) &&
                this.getProfileId().equals(a.getProfileId()) &&
                this.getUsername().equals(a.getUsername()) &&
                this.getAccessToken().equals(a.getAccessToken()) &&
                this.getClientToken().equals(a.getClientToken()))

        {
            return true;
        }
        return false;
    }
}
