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
 * DownloadHelper Class:
 *
 * Simple Download Helper to download files specified by an url
 * into an String or an file on the hard-disk
 *
 * @author evidence
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.craften.craftenlauncher.exception.CraftenDownloadException;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.vm.DownloadVM;

public class DownloadHelper {
    final static int size = 1024;
    private static volatile boolean force = false;
    private static DownloadVM dwvm;
    
    /**
     * 
     * @param fAddress Addresse der Datei welche heruntergeladen werden soll.
     * @return String Repr�sentation der Datei.
     */
    public static String downloadFileToString(String fAddress) {
    	FileHelper helper = new FileHelper(fAddress, fAddress, fAddress);
    	StringDownloader downloader = new StringDownloader(helper);
    	
    	download(downloader);
    	
    	return downloader.getFile();
    }
    
    /**
     * Allgemeine Form der Download Funktion.
     * Benutzt ein Downloader Objekt um zu bestimmen wohin die Daten gespeichert werden soll.
     * @param downloader
     */
    private static void download(Downloader downloader) {
    	HttpURLConnection uCon = null;
        InputStream is = null;
        
    	try {
    		 URL Url = new URL(downloader.getHelper().getFileAddress());
    		 uCon = (HttpURLConnection) Url.openConnection();
    		 downloader.setContentLength(uCon.getContentLength());
    		 is = uCon.getInputStream();
    		 
    		 byte[] buf = new byte[size];
             int ByteRead;
             int dlKB = 0;
             
             while ((ByteRead = is.read(buf)) != -1) {
            	 downloader.appendBytes(buf, ByteRead);
            	 dlKB++;
            	 
            	 //TODO: Workaround dwvm fr�her setzen
                 if(dwvm != null) {
                	 if(dlKB == 10) {
                		 dwvm.addDownloadedKByte(10); 
                		 dlKB = 0;
                	 }
                 }
             }
             
             downloader.finished();
    	} catch ( Exception e ) {
    		Logger.logError("Could not download file to: " + downloader.getFilename());
            e.printStackTrace();
    	}
    	finally {
    		 try {
                 if (is != null) {
                     is.close();
                 }
                 if (uCon != null) {
                     uCon.disconnect();
                 }
             } catch (IOException e) {
                 Logger.logError("Could not close Download-Stream: " + e.getMessage());
             }
    	}
    }
    
    /**
     * Downloads a File to Disk with checking the size. The file is specified by the url and will be downloaded
     * into a file specified by file.
     * @param url
     * @param file
     * @return
     * @throws CraftenDownloadException
     */
    public static String downloadFileToDiskWithCheck(String url, String file) throws CraftenDownloadException {
        FileHelper helper = new FileHelper(url,file);
        
        return  downloadFileToDisk(helper, true);
    }
    
    /**
     * Downloads a File to Disk without checking the size. The file is specified by the url and will be downloaded
     * into a file specified by file.
     * @param url
     * @param file
     * @return
     * @throws CraftenDownloadException
     */
    public static String downloadFileToDiskWithoutCheck(String url, String file) throws CraftenDownloadException {
        FileHelper helper = new FileHelper(url,file);

        return downloadFileToDisk(helper, false);
    }

    /**
     * Downloads a File to Disk with checking the size. The file is specified by the url and will be downloaded
     * into a file specified by file and destinationDir as path.
     * @param fAddress
     * @param destinationDir
     * @param myFileName
     * @return Dateiname, unter dem der Download tatsaechlich gespeichert wurde
     * @throws CraftenDownloadException
     */
    public static String downloadFileToDiskWithCheck(String fAddress, String destinationDir,
                                            String myFileName) throws CraftenDownloadException {
    	FileHelper helper = new FileHelper(fAddress,destinationDir,myFileName);
    	
        return downloadFileToDisk(helper, true);
    }
    
    /**
     * Downloads a File to Disk without checking the size. The file is specified by the url and will be downloaded
     * into a file specified by file and destinationDir as path.
     * @param fAddress
     * @param destinationDir
     * @param myFileName
     * @return
     * @throws CraftenDownloadException
     */
    public static String downloadFileToDiskWithoutCheck(String fAddress, String destinationDir,
            String myFileName) throws CraftenDownloadException {
    	
    	FileHelper helper = new FileHelper(fAddress,destinationDir,myFileName);
    		
    	return downloadFileToDisk(helper, false);
    }

    /**
     * @param fileHelper
     * @param shouldCheck
     * @return Dateiname, unter dem der Download tatsaechlich gespeichert wurde
     * @throws CraftenDownloadException
     */
    private static String downloadFileToDisk(FileHelper fileHelper, boolean shouldCheck) throws CraftenDownloadException {
    	
    	String destinationDir = fileHelper.getDestinationDir();
    	String fAddress = fileHelper.getFileAddress();
    	String fileName = fileHelper.getLocalFileName();

        if (!new File(destinationDir).exists()) {
            new File(destinationDir).mkdirs();
        }
        
        int lastSlashIndex = fAddress.lastIndexOf('/');
        int lastPeriodIndex = fAddress.lastIndexOf('.');
        
        if (shouldFileBeDownloaded(fileHelper,shouldCheck)) {
        	
            if (lastPeriodIndex >= 1 && lastSlashIndex >= 0
                    && lastSlashIndex < fAddress.length() - 1) {

                try {
                	tryDownloadFile(fileHelper);
                } catch (Exception e) {
                	Logger.logError("Exception occured while downloading from: " + fAddress + " : " +  e.getMessage());
                    throw new CraftenDownloadException("Exception occured while downloading from: " + fAddress + " : " +  e.getMessage());
                }

                Logger.logInfo("File-Download finished: " + fileName);
            } else {
                Logger.logError("Error with path or file name. Path: " + destinationDir + " File: " + fileName);
            }
        } else {
        	//TODO: Bessere Fehlermeldung als should not be downloaded? Alte Fehlermeldung ( From: " + fAddress + " to: " + destinationDir + " )
        	// Vlt. besser den Grund f�r "shoudl not be" angeben?
        	Logger.logInfo(fileName + " should not be downloaded!");
        }

        return fileName;
    }
    
    /**
     * Checks if a file is okey by checking it's existence, length and if force-downloading is activated.
     * @param fileHelper
     * @param shouldCheck
     * @return
     */
    private static boolean shouldFileBeDownloaded(FileHelper fileHelper, boolean shouldCheck) {
    	String destinationDir = fileHelper.getDestinationDir();
    	String fAddress = fileHelper.getFileAddress();
    	String fileName = fileHelper.getLocalFileName();
    	
    	boolean exists = (new File(destinationDir
    			+ System.getProperty("file.separator") + fileName)).exists();
    	 
    	return force || !exists || (shouldCheck && exists && !isLengthEqual(fAddress, destinationDir
    			+ System.getProperty("file.separator") + fileName));
    }
    
    /**
     * Versucht die Datei herunterzuladen. Checkt dabei die vorhandene + uebertragene File-Groesse.
     * Falls diese nicht gleich sind, wird versuch die Datei nochmals herunterzuladen.
     * @param fileHelper
     * @throws Exception
     */
    private static void tryDownloadFile(FileHelper fileHelper) throws Exception{
    	boolean downloadSucceeded = false;
        int triesLeft = 3;
        
        while (!downloadSucceeded && triesLeft > 0) {
        	
        	FileDownloader downloader = new FileDownloader(fileHelper);
            
            download(downloader);
            
            if (downloader.getContentLength() != -1) {
            	long fileSize = getFileSize(fileHelper.getDestinationDir() + System.getProperty("file.separator") + fileHelper.getLocalFileName());
                if (fileSize == downloader.getContentLength()) {
                    downloadSucceeded = true;
                } else {
                	Logger.logWarning("File size should be: " + fileSize + " but was: " + downloader.getContentLength());
                    downloadSucceeded = false;
                    triesLeft--;
                }
            } else {
                downloadSucceeded = true;
            }
        }
        
        if (triesLeft == 0) {
        	Logger.logWarning("Needed all three tries and download " + (downloadSucceeded ? "succeeded" : "failed"));
        }
    }

    /**
     * Gets the size of a file, in bytes. If something goes wrong, -1 is returned.
     *
     * @param filename
     * @return Filesize or -1
     */
    private static long getFileSize(String filename) {
        try {
            File file = new File(filename);
            return file.length();
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Checkt ob eine Datei die selbe Laenge wie die uebertragene Datei hat.
     * @param url
     * @param filename
     * @return
     */
    private static boolean isLengthEqual(String url, String filename) {
        URL Url;
        HttpURLConnection uCon = null;
        
        try {
            Url = new URL(url);
            uCon = (HttpURLConnection) Url.openConnection();
            
            uCon.setRequestMethod("HEAD");
            int contentLength = uCon.getContentLength();
            uCon.disconnect();
            
            long fileSize = getFileSize(filename);
            if (fileSize >= 0 && contentLength == fileSize) {
                return true;
            }

        } catch (Exception e) {
            Logger.logError("Error whiel getting HTTP-Header: " + e.getMessage());
        } finally {
            if (uCon != null) {
                uCon.disconnect();
            }
        }
        return false;
    }
    
    /**
     * Setzt den Force-Modus damit alle Dateien zwingend neu heruntergeladen werden.
     * @param force
     */
    public static synchronized void setForce(boolean force) {
        DownloadHelper.force = force;
    }

    public static synchronized boolean getForce() {
        return force;
    }

    /**
     * Funktion um Jars zu entpacken.
     *
     * @param zipFile
     * @param destFolder
     * @throws IOException
     */
    public static void unpackJarFile(String zipFile, String destFolder)
            throws IOException {
        BufferedOutputStream dest;
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
                new FileInputStream(zipFile)));

        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            Logger.logInfo("Extracting: " + entry.getName());

            if (entry.getName().contains("META-INF")) {
                continue;
            }

            if (entry.isDirectory()) {
                new File(destFolder + File.separator + entry.getName()).mkdirs();
                continue;
            } else {
                int di = entry.getName().lastIndexOf('/');
                if (di != -1) {
                    new File(destFolder + File.separator
                            + entry.getName().substring(0, di)).mkdirs();
                }
            }
            
            int count;
            byte data[] = new byte[1024];
            
            dest = new BufferedOutputStream(new FileOutputStream(destFolder + File.separator
                    + entry.getName()));

            while ((count = zis.read(data)) != -1) {
                dest.write(data, 0, count);
            }

            dest.flush();
            dest.close();
        }

        zis.close();
    }
    
    /**
     * Setzt die Download ViewModel
     * @param vm
     */
    public static void setDownloadHelper(DownloadVM vm) {
    	dwvm = vm;
    }
}

/**
 * Abstrakte Ober-Klasse des Downloaders.
 * @author rebeard
 *
 */
abstract class Downloader {
	private int contentLength;
	private FileHelper helper;
	
	public Downloader(FileHelper helper) {
		contentLength = -1;
		this.helper = helper;
	}

	public FileHelper getHelper() {
		return helper;
	}
	
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	
	public int getContentLength() {
		return contentLength;
	}
	
	public String getFilename() {
		return helper.getLocalFileName();
	}
	
	public abstract void appendBytes(byte[] buffer, int byteRead) throws Exception;
	/**
	 * Um z.B. Streams zu schlie�en.
	 * @throws IOException 
	 */
	public abstract void finished() throws IOException;
}

/**
 * Sub-Klasse um eine Datei als String zu erhalten.
 * @author redbeard
 *
 */
class StringDownloader extends Downloader {
	private StringBuilder file;
	
	public StringDownloader(FileHelper helper) {
		super(helper);
		file = new StringBuilder();
	}

	@Override
	public void appendBytes(byte[] buffer, int byteRead) {
		String data = new String(buffer,0,byteRead);
		file.append(data);
	}
	
	public String getFile() {
		return file.toString();
	}
	
	/**
	 * Leer da nichts zu schlie�en ist.
	 */
	@Override
	public void finished() {
		
	}
}

/**
 * Sub-Klasse um die Datei auf die Platte zu schreiben.
 * @author redbeard
 *
 */
class FileDownloader extends Downloader {
	BufferedOutputStream output;
	
	public FileDownloader(FileHelper helper) throws FileNotFoundException {
		super(helper);
		 output = new BufferedOutputStream(new FileOutputStream(
                 helper.getDestinationDir() + File.separator + helper.getLocalFileName()));
	}

	@Override
	public void appendBytes(byte[] buffer, int byteRead) throws Exception {
		output.write(buffer, 0, byteRead);
	}

	@Override
	public void finished() throws IOException {
		output.close();
	}
}

/**
 * Einfacher File-Helper welcher die Attribute zusammen fasst.
 * @author redbeard
 *
 */
class FileHelper {
	private String fAddress;
	private String destinationDir;
	private String localFileName;
	
	public FileHelper(String fAddress, String destinationDir, String localFileName) {
		this.fAddress = fAddress;
		this.destinationDir = destinationDir;
		setLocalFileName(localFileName);
	}
	
	private void setLocalFileName(String fileName) {
		if (fileName == null || fileName.equals("") || fileName.equals(" ")) {
			int lastSlashIndex = fAddress.lastIndexOf('/');
			
			localFileName = fAddress.substring(lastSlashIndex + 1);
        } else {
        	localFileName = fileName;
        }
	}
	
	public FileHelper(String fAddress, String file) {
		this.fAddress = fAddress;
		
		int index = file.lastIndexOf(File.separator);
		
	    this.destinationDir = file.substring(0, index + 1);
	    this.localFileName = file.substring(index);
	}

	public String getFileAddress() {
		return fAddress;
	}

	public String getLocalFileName() {
		return localFileName;
	}

	public String getDestinationDir() {
		return destinationDir;
	}
}
