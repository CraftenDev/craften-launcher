package de.craften.craftenlauncher.logic.auth;

public class MinecraftUser {

    private String mEmail;
    private String mUsername;
    private String mPassword;

    private String mAccessToken;
    private String mClientToken;
    private String mProfileId;
    private String mSession;

    private String mResponse;

    //TODO Refacoteren. Vllt. auslagern in den AuthService?
    private boolean mLoggedIn;

    /**
     * Creates an empty MinecraftUser
     */
    public MinecraftUser() {
    }

    /**
     * Creates a new MinecraftUser
     *
     * @param email    The users' email
     * @param password The users' password
     */
    public MinecraftUser(String email, String password) {
        setEmail(email);
        setPassword(password);
    }

    /**
     * Creates a new MinecraftUser where a login with password is not needed
     *
     * @param email       The users' email
     * @param profileId   The users' profileId
     * @param username    The users' username
     * @param accessToken The users' accessToken
     * @param clientToken The users' clientToken
     */
    public MinecraftUser(String email, String profileId, String username, String accessToken, String clientToken) {
        setEmail(email);
        setProfileId(profileId);
        setUsername(username);
        setAccessToken(accessToken);
        setClientToken(clientToken);
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        mEmail = email;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        mUsername = username;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        mPassword = password;
    }

    /**
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    /**
     * @param clientToken
     */
    public void setClientToken(String clientToken) {
        mClientToken = clientToken;
    }

    /**
     * @param profileId
     */
    public void setProfileId(String profileId) {
        mProfileId = profileId;
    }

    /**
     * @param session
     */
    public void setSession(String session) {
        mSession = session;
    }

    /**
     * @param response
     */
    public void setResponse(String response) {
        mResponse = response;
    }

    /**
     * @return current email of the user
     */
    public String getEmail() {
        if (mEmail == null)
            return "";
        return mEmail;
    }

    /**
     * @return current username of the user
     */
    public String getUsername() {
        if (mUsername == null)
            return "";
        return mUsername;
    }

    /**
     * @return current password of the user
     */
    public String getPassword() {
        if (mPassword == null)
            return "";
        return mPassword;
    }


    /**
     * @return The accesstoken of the user
     */
    public String getAccessToken() {
        if (mAccessToken == null)
            return "";
        return mAccessToken;
    }

    /**
     * @return current clienttoken of the user
     */
    public String getClientToken() {
        if (mClientToken == null)
            return "";
        return mClientToken;
    }

    /**
     * @return current profileId of the user
     */
    public String getProfileId() {
        if (mProfileId == null)
            return "";
        return mProfileId;
    }

    /**
     * @return current sessionId of the user
     */
    public String getSession() {
        if (mSession == null) {
            mSession = "token:" + getAccessToken() + ":" + getProfileId();
        }
        return mSession;
    }

    /**
     * @return current response from Mojangs' servers after authenticating the user
     */
    public String getResponse() {
        if (mResponse == null)
            return "";
        return mResponse;
    }

    /**
     * Checks if user has an Accesstoken
     *
     * @return true if it has false if not
     */
    public boolean hasAccessToken() {
        return mAccessToken != null && !mAccessToken.equals("");
    }

    /**
     * Checks if the user was logged in successfully.
     *
     * @return
     */
    public boolean isLoggedIn() {
        return mLoggedIn;
    }

    /**
     * Set the isLoggedIn flag to true
     */
    public void loggingInSuccess() {
        mLoggedIn = true;
    }

    /**
     * Set the isLoggedIn flag to false
     */
    public void loggingInFailed() {
        mLoggedIn = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MinecraftUser))
            return false;

        MinecraftUser a = (MinecraftUser) obj;
        return this.getEmail().equals(a.getEmail()) &&
                this.getProfileId().equals(a.getProfileId()) &&
                this.getUsername().equals(a.getUsername()) &&
                this.getAccessToken().equals(a.getAccessToken()) &&
                this.getClientToken().equals(a.getClientToken());
    }

    @Override
    public int hashCode() {
        return getProfileId().hashCode();
    }
}
