package de.craften.craftenlauncher.logic.download;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.download.loader.JarDownloader;
import de.craften.craftenlauncher.logic.download.loader.LibraryDownloader;
import de.craften.craftenlauncher.logic.download.loader.RessourceDownloader;
import de.craften.craftenlauncher.logic.download.loader.SkinDownloader;
import de.craften.craftenlauncher.logic.json.JSONReader;
import de.craften.craftenlauncher.logic.minecraft.MinecraftPathImpl;
import de.craften.craftenlauncher.logic.resources.Version;
import de.craften.craftenlauncher.logic.version.MinecraftVersion;
import de.craften.craftenlauncher.logic.vm.DownloadVM;
import de.craften.craftenlauncher.logic.vm.SkinVM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Stack;

public class DownloadService implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(DownloadService.class);
    private boolean mRunning;
    private boolean mFinRessources, mFinJar, mFinLibraries;
    private Stack<DownloadTasks> mCurrentTasks;
    private DownloadVM mDownloadVM;
    private MinecraftPathImpl mMinecraftPath;
    private MinecraftVersion mCurrentVersion;

    private RessourceDownloader mRessDownloader;

    /**
     * Erstellt einen neuen Download Service. Fuer die Downloads werden alle aktuellen
     * Minecraft Pfade benoetigt ( NewPath ). Die DownloadVM stellt den Zuganngspunkt fuer
     * die GUI dar.
     *
     * @param mcPath
     * @param downVM
     */
    public DownloadService(MinecraftPathImpl mcPath, DownloadVM downVM) {
        this.mDownloadVM = downVM;
        DownloadHelper.setDownloadHelper(downVM);
        this.mDownloadVM.setProgressBarToNull();
        this.mMinecraftPath = mcPath;
        this.mCurrentTasks = new Stack<DownloadTasks>();
        this.mRunning = true;
    }

    public synchronized void addTask(DownloadTasks task) {
        mCurrentTasks.push(task);
    }

    @Override
    public void run() {
        LOGGER.debug("DownloadService started!");

        while (mRunning) {
            if (mCurrentVersion == null) {
                Thread.yield();
                continue;
            }

            DownloadTasks task = getTask();

            if (task != null) {
                try {
                    doTask(task);
                } catch (Exception e) {
                    //TODO: Test-Workaround damit unter keinen Umstaenden der Service stirbt.
                    LOGGER.error("Unkown exception in DownloadService", e);

                    if (DownloadTasks.RESSOURCES != task) {
                        LOGGER.debug("Adding task in run() again");
                        addTask(task);
                    }
                }
            }
        }
    }

    private synchronized DownloadTasks getTask() {
        if (!mCurrentTasks.isEmpty()) {
            return mCurrentTasks.pop();
        }

        return null;
    }

    private void doTask(DownloadTasks task) {
        switch (task) {
            case RESSOURCES:
                downloadRessources();
                break;

            case JAR:
                downloadJar();
                break;

            case LIBRARIES:
                downloadLibraries();
                break;

            default:
                break;
        }

        mDownloadVM.setMinecraftDownload(isFinished());
    }

    private void downloadRessources() {
        LOGGER.info("Starting download ressources");
        mRessDownloader = new RessourceDownloader(mCurrentVersion, mMinecraftPath.getResourcePath(), mDownloadVM);

        try {
            mRessDownloader.download();
            mFinRessources = true;
        } catch (CraftenDownloadException e) {
            //Sonderfall kein finRes=false da Minecraft auch mit nicht vollstaendigen Ressourcen gestartet werden kann / soll.
            LOGGER.warn("Error in downloadRessources -> Adding Task again!", e);
            addTask(DownloadTasks.RESSOURCES);
        }

        LOGGER.info("Resources-Download finished.");
    }

    private void downloadJar() {
        LOGGER.info("Starting download jar");

        JarDownloader jarDown = new JarDownloader(mCurrentVersion, mMinecraftPath);

        try {
            jarDown.download();
            mFinJar = true;
        } catch (CraftenDownloadException e) {
            LOGGER.warn("Jar could not be downloaded -> Adding Task again!", e);
            mFinJar = false;
            addTask(DownloadTasks.JAR);
        }

        LOGGER.info("Jar-Download finished.");
        mDownloadVM.updateProgress(10);
    }

    private void downloadLibraries() {
        LOGGER.info("Starting download libraries");

        LibraryDownloader libDown = new LibraryDownloader(mCurrentVersion, mMinecraftPath, mDownloadVM);

        try {
            libDown.download();
            mFinLibraries = true;
        } catch (CraftenDownloadException e) {
            LOGGER.warn("Possible Version Json could not be downloaded -> Adding Task again!", e);
            mFinLibraries = false;
            addTask(DownloadTasks.LIBRARIES);
        }
        LOGGER.info("Libaries-Download finished.");
    }

    public synchronized void setRunning(boolean running) {
        this.mRunning = running;

        if (!running && mRessDownloader != null) {
            mRessDownloader.stopDownload();
        }
    }

    public boolean isFinished() {
        return (mFinRessources && mFinLibraries && mFinJar);
    }

    public void setMinecraftVersion(MinecraftVersion version, String assetsVersion) throws CraftenLogicException {
        try {
            DownloadHelper.downloadFileToDiskWithoutCheck(DownloadUrls.URL_VERSION + assetsVersion + "/" + assetsVersion
                    + ".json", mMinecraftPath.getMinecraftVersionsDir() + version.getVersion() + File.separator, version.getVersion() + ".json");

            Version vers = JSONReader.readJsonFileFromSelectedVersion(mMinecraftPath.getMinecraftJarPath() + version.getVersion() + ".json");

            version.setVersionJson(vers);
        } catch (CraftenDownloadException e) {
            LOGGER.error("Could not download Json File: " + version.getVersion() + ".json", e);
            throw new CraftenDownloadException("Could not download Json File: " + version.getVersion() + ".json");
        }

        this.mCurrentVersion = version;
    }

    public void setMinecraftVersion(MinecraftVersion version) throws CraftenLogicException {
        setMinecraftVersion(version, version.getVersion());
    }

    /**
     * Loads skin for given username.
     *
     * @param skin
     * @param username
     */
    public void downloadSkin(SkinVM skin, String username) {
        final SkinDownloader downloader = new SkinDownloader(username, skin);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    downloader.download();
                } catch (CraftenDownloadException e) {
                    LOGGER.error(e);
                    //TODO Use the standard skin
                }
            }
        });

        t.start();
    }
}
