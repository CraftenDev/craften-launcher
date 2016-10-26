package de.craften.craftenlauncher.logic;

import de.craften.craftenlauncher.Config;
import de.craften.craftenlauncher.exception.*;
import de.craften.craftenlauncher.logic.auth.AuthenticationService;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import de.craften.craftenlauncher.logic.auth.Profiles;
import de.craften.craftenlauncher.logic.download.DownloadService;
import de.craften.craftenlauncher.logic.download.DownloadTasks;
import de.craften.craftenlauncher.logic.minecraft.MinecraftInfo;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPathImpl;
import de.craften.craftenlauncher.logic.minecraft.MinecraftProcess;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.craftenlauncher.logic.version.VersionListHelper;
import de.craften.craftenlauncher.logic.vm.DownloadVM;
import de.craften.craftenlauncher.logic.vm.SkinVM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Observer;

public class LogicController {
    private static final Logger LOGGER = LogManager.getLogger(LogicController.class);
    private AuthenticationService mAuthService;
    private DownloadService mDownService;
    private Config config;
    private VersionListHelper mVersionList;
    private MinecraftPathImpl mMinecraftPath;
    private MinecraftVersion mCurrentVersion;
    private String mAssetsVersion;
    private HashMap<String, String> mMincraftArgs;
    private Profiles mProfiles;

    private DownloadVM mDownloadVM;
    private SkinVM mSkinVM;

    public LogicController() {
        mAuthService = new AuthenticationService();
        mProfiles = new Profiles();
        mDownloadVM = new DownloadVM();
        mMincraftArgs = new HashMap<>();
    }

    /**
     * Inits the logic layer.
     */
    public void init(Config config) {
        this.config = config;

        if (config.hasMinecraftPath()) {
            mMinecraftPath = new MinecraftPathImpl(config.getMcPath());
        } else {
            mMinecraftPath = new MinecraftPathImpl();
        }

        mProfiles.setPath(mMinecraftPath.getMinecraftDir());

        if (config.hasServerAddress()) {
            mMincraftArgs.put("server", config.getServer());
        }

        if (config.hasXMXParameter()) {
            mMincraftArgs.put("xmx", config.getXmx());
        }

        mVersionList = new VersionListHelper(mMinecraftPath);

        if (config.getVersion() != null) {
            String version = config.getVersion();

            try {
                if (mVersionList.isVersionAvailableOnline(version)) {
                    this.mCurrentVersion = new MinecraftVersion(version);
                } else {
                    mVersionList.checkVersion(version);
                    this.mCurrentVersion = new MinecraftVersion(version);
                }

                mMincraftArgs.put("version", version);
                mMinecraftPath.setVersionName(version);
            } catch (CraftenVersionNotKnownException e) {
                LOGGER.error("Version not available: " + version, e);
            }
        }

        if (config.getAssetsVersion() != null) {
            mAssetsVersion = config.getAssetsVersion();
        }

        mAuthService.setMcPath(mMinecraftPath);
        Profiles login = mAuthService.readProfiles();

        if (login != null) {
            LOGGER.info("craftenlauncher_profiles.json found! Username is: " + login.getSelectedUser().getUsername());

            if (config.hasProfileID()) {
                login.changeSelectedUser(config.getProfileID());
            }

            //TODO checken was genau die Response ist und was man damit so anfaengt!
            //user.setAuthentication(showProfile.getUsername(), null, showProfile.getAccessToken(), showProfile.getClientToken(), showProfile.getProfileId(),null);

            mProfiles = login;
        } else {
            LOGGER.info("No craftenlauncher_profiles.json found in " + mMinecraftPath.getMinecraftDir());
        }
    }

    public void setUser(String username, char[] password) throws CraftenLogicValueIsNullException {
        //TODO weiter char[] durchziehen wegen Sicherheit.
        String pass = String.valueOf(password);

        if (username == null || username.equals(" ") || username.equals("")) {
            LOGGER.error("Username is missing");
            throw new CraftenLogicValueIsNullException("Username is missing");
        }
        if (pass.equals(" ") || pass.equals("")) {
            LOGGER.error("Password is missing");
            throw new CraftenLogicValueIsNullException("Password is missing");
        }
        mProfiles.setSelectedUser(new MinecraftUser(username, pass));
    }

    public MinecraftUser getUser() throws CraftenLogicException {
        if (mProfiles.getSelectedUser() != null) {
            return mProfiles.getSelectedUser();
        } else {
            LOGGER.error("No user selected");
            throw new CraftenUserException("No user selected");
        }
    }

    public void authenticateUser() throws CraftenException {
        LOGGER.debug("Authenticate!");
        String session;

        if (mProfiles.getSelectedUser().hasAccessToken()) {                                                           // existing user
            session = mAuthService.getSessionID(mProfiles.getSelectedUser());
            if (session != null) {
                mProfiles.getSelectedUser().loggingInSuccess();

                if (mProfiles.getAvailableUser(mProfiles.getSelectedUser().getProfileId()) != null)
                    mProfiles.removeAvailableUser(mProfiles.getSelectedUser().getProfileId());
                mProfiles.addAvailableUser(mProfiles.getSelectedUser());

                mProfiles.save();
            }
        } else {      //new User
            session = mAuthService.getSessionID(mProfiles.getSelectedUser());

            if (session != null) {
                mProfiles.setSelectedUser(mAuthService.getUser());
                mProfiles.getSelectedUser().loggingInSuccess();

                if (mProfiles.getAvailableUser(mProfiles.getSelectedUser().getProfileId()) != null)
                    mProfiles.removeAvailableUser(mProfiles.getSelectedUser().getProfileId());
                mProfiles.addAvailableUser(mProfiles.getSelectedUser());

                mProfiles.save();
            }
        }

        startDownloadService();
        if (mDownService != null) {
            mDownService.downloadSkin(mSkinVM, mProfiles.getSelectedUser().getUsername());
        }
    }

    private void startDownloadService() throws CraftenLogicException {
        if (mDownService != null) {
            return;
        }

        if (mProfiles.getSelectedUser() != null && !mProfiles.getSelectedUser().isLoggedIn()) {
            LOGGER.error("Trying to start DownloadService although user is not logged in!");
        }

        mDownService = new DownloadService(mMinecraftPath, mDownloadVM);

        if (mCurrentVersion != null) {
            if (mAssetsVersion != null) {
                LOGGER.info("Using assets from " + mAssetsVersion + " for " + mCurrentVersion.getVersion());
                try {
                    mDownService.setMinecraftVersion(mCurrentVersion, mAssetsVersion);
                } catch (Exception e) {
                    //TODO Workaround vllt. klappt es beim zweiten Mal.
                    LOGGER.info("Trying again to download json!", e);
                    mDownService.setMinecraftVersion(mCurrentVersion, mAssetsVersion);
                }
            } else {
                try {
                    mDownService.setMinecraftVersion(mCurrentVersion);
                } catch (Exception e) {
                    //TODO Workaround vllt. klappt es beim zweiten Mal.
                    LOGGER.info("Trying again to download json!", e);
                    mDownService.setMinecraftVersion(mCurrentVersion);
                }
            }
            mDownService.addTask(DownloadTasks.RESSOURCES);
            mDownService.addTask(DownloadTasks.JAR);
            mDownService.addTask(DownloadTasks.LIBRARIES);
        }

        new Thread(mDownService).start();
    }

    public void logout() {
        LOGGER.info("Trying to logout user: " + mProfiles.getSelectedUser().getUsername());

        String name = mProfiles.getSelectedUser().getUsername();

        mAuthService.invalidate(mProfiles.getSelectedUser());
        if (mProfiles.getAvailableUser(mProfiles.getSelectedUser().getProfileId()) != null)
            mProfiles.removeAvailableUser(mProfiles.getSelectedUser().getProfileId());

        mProfiles.clearSelectedUser();
        mProfiles.save();
        // TODO: Muss das unbedingt null werden?
        // mCurrentVersion = null;
        mDownloadVM.setProgressBarToNull();
        //mAuthService.deleteProfiles();
        mAuthService = new AuthenticationService();

        if (mDownService != null) {
            LOGGER.debug("Shutting down DownloadService because of logout");
            mDownService.setRunning(false);

            mDownService = null;
        }
        LOGGER.info("User " + name + " has logged out");
    }

    //TODO vorher besser alten Service ordentlich stoppen oder neu Init?
    public void setMinecraftVersion(String version) throws CraftenLogicException {
        this.mVersionList.checkVersion(version);
        this.mCurrentVersion = new MinecraftVersion(version);
        this.mAssetsVersion = null;

        mMinecraftPath.setVersionName(version);
        if (mDownService != null) {
            mDownService.setRunning(false);

            mDownService = null;
        }

        startDownloadService();
    }

    public MinecraftVersion getMinecraftVersion() throws CraftenLogicException {
        if (mCurrentVersion == null) {
            throw new CraftenLogicException("Current version is null");
        }
        return mCurrentVersion;
    }

    public List<String> getMinecraftVersions() {
        return mVersionList.getVersionsList();
    }

    public boolean isMinecraftDownloaded() {
        return mDownService.isFinished();
    }

    public boolean isQuickPlay() {
        return config.isQuickPlay();
    }

    public boolean isForceLogin() {
        return config.isForcelogin();
    }

    public void startMinecraft() throws CraftenLogicException {
        if (!isMinecraftDownloaded()) {
            LOGGER.error("Minecraft download not yet complete");
            throw new CraftenLogicException("Minecraft download not yet complete");
        }

        if (!mProfiles.getSelectedUser().isLoggedIn()) {
            LOGGER.error("Trying to start Minecraft although User is not logged in!");
            throw new CraftenLogicException("Trying to start Minecraft although User is not logged in!");
        }

        MinecraftInfo info = new MinecraftInfo(mCurrentVersion.getVersion());
        info.setUser(mProfiles.getSelectedUser());
        info.setMSV(mCurrentVersion);
        info.setMinecraftPath(mMinecraftPath);
        info.setXMX(mMincraftArgs.get("xmx"));
        //TODO Server und Port Anfrage ueberpruefen und falls noetig korrigieren.
        String server = mMincraftArgs.get("server");
        info.setServerAdress(server);
        info.setFullscreen(config.isFullscreen());

        MinecraftProcess process = new MinecraftProcess(info, info.getMSV().getVersionJson());

        process.startMinecraft();

        if (!process.getSuccess()) {
            LOGGER.error("Minecraft process could not be started!");
            throw new CraftenLogicException("Minecraft Process could not be started!");
        } else {
            System.exit(0);
        }
    }

    public void startMinecraftWithoutLogin(String username) throws CraftenLogicException {
        if (!isMinecraftDownloaded()) {
            LOGGER.error("Minecraft download not yet complete");
            throw new CraftenLogicException("Minecraft download not yet complete");
        }

        MinecraftUser user = new MinecraftUser();
        user.setUsername(username);
        user.setProfileId("0123456789abcdef0123456789abcdef");
        user.setAccessToken("1123456789abcdef0123456789abcdef");

        MinecraftInfo info = new MinecraftInfo(mCurrentVersion.getVersion());
        info.setUser(user);
        info.setMSV(mCurrentVersion);
        info.setMinecraftPath(mMinecraftPath);
        info.setXMX(mMincraftArgs.get("xmx"));
        //TODO Server und Port Anfrage ueberpruefen und falls noetig korrigieren.
        String server = mMincraftArgs.get("server");
        info.setServerAdress(server);
        info.setFullscreen(config.isFullscreen());

        MinecraftProcess process = new MinecraftProcess(info, info.getMSV().getVersionJson());

        process.startMinecraft();
        LOGGER.info("Started without login");

        if (!process.getSuccess()) {
            LOGGER.error("Minecraft process could not be started!");
            throw new CraftenLogicException("Minecraft Process could not be started!");
        } else {
            System.exit(0);
        }
    }

    public void setDownloadObserver(Observer server) {
        mDownloadVM.addObserver(server);
    }

    public HashMap<String, String> getMinecraftArguments() {
        return new HashMap<String, String>(mMincraftArgs);
    }

    public String getMinecraftArgument(String key) throws CraftenLogicException {
        String argument = mMincraftArgs.get(key);

        if (argument != null) {
            return mMincraftArgs.get(key);
        } else {
            LOGGER.error("No argument for key: " + key);
            throw new CraftenLogicException("No Argument for key: " + key);
        }
    }

    //TODO Sicherheits-Checks mit einbauen.
    public void setMinecraftArguments(String key, String value) {
        mMincraftArgs.put(key, value);
    }

    public void setSkinObserver(Observer server) {
        mSkinVM = new SkinVM();
        mSkinVM.addObserver(server);
    }

    /**
     * Returns a list of users from the current craftenlauncher_profiles.
     *
     * @return
     */
    public List<MinecraftUser> getUsers() {
        return mProfiles.getAvailableUsers();
    }
}
