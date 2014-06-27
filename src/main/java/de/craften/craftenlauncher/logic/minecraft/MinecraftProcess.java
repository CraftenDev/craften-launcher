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
 * MinecraftProcess Class:
 * 
 * Launches Minecraft in a new Process. Uses Process Builder and logs the creation of the
 * command. Debug functionality not yet implemented
 * 
 * 
 * @author redbeard
 */
package de.craften.craftenlauncher.logic.minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.resources.Version;
import de.craften.craftenlauncher.logic.resources.version.VersionHelper;
import de.craften.util.JVMArgmuments;
import de.craften.util.OSHelper;

public class MinecraftProcess {
	private MinecraftInfo mInfo;
	private Version mVersion;
	private boolean mSuccess;
	private ArrayList<String> mParams = new ArrayList<String>();

	public MinecraftProcess(MinecraftInfo info, Version version) {
		this.mInfo = info;
		this.mVersion = version;
	}
	
	/**
	 * @return True if launching was succesful, if it is not yet finished or failed it returns false.
	 */
	public boolean getSuccess() {
		return mSuccess;
	}
	
	/**
	 * Tries to launch Minecraft in a new Process.
	 * Sets an internal variable if the launch was succesful.
	 */
	public void startMinecraft() {
		Process minecraft = null;
		try {
			minecraft = startProcess();
			Logger.getInstance().logInfo("Command: " + mParams.toString());
			mSuccess = true;
			Logger.getInstance().logInfo("Minecraft started!");
		} catch (Exception e) {
			mSuccess = false;
			Logger.getInstance().logError("Could not start Minecraft!");
			Logger.getInstance().logError(e.getMessage());
		}
		
		if(minecraft == null) {
			Logger.getInstance().logError("Minecraft Process Null");
		}else {
			// TODO: Problems with Standard-Output are not yet fixed.
		}
	}
	
	private Process startProcess() throws IOException {
		String java = OSHelper.getInstance().getJavaPath();

		mParams.add(java);
		addJavaCommand();
		addMinecraftPaths();
		addMinecraftArguments();
		
		ProcessBuilder pb = new ProcessBuilder(mParams);
        pb.directory(new File(mInfo.getMinecraftPath().getMinecraftDir()));

        return pb.start();
	}
	
	private void addJavaCommand() {
		if(mInfo.hasXMX()) {
			mParams.add("-Xmx" + mInfo.getXMX());
		}
		
		ArrayList<String> args = JVMArgmuments.get();
 		if(args != null) {
			mParams.addAll(args);
		}
	}
	
	private void addMinecraftPaths() {
		String natives = mInfo.getMinecraftPath().getMinecraftJarPath() + mInfo.getMinecraftVersion() + "-natives-131231";
		String libraries = VersionHelper.getLibFilessAsArgmument(mInfo.getMinecraftPath(), mVersion.getLibraries()) + "";
		String jar = mInfo.getMinecraftPath().getMinecraftJarPath() + mInfo.getMinecraftVersion() + ".jar";

		mParams.add("-Djava.library.path=" + natives);
		mParams.add("-cp");
		mParams.add(libraries + ";" + jar);
		mParams.add(mVersion.getMainClass());	
	}
	
	private void addMinecraftArguments() {
		
		String fs = File.separator;
		ArrayList<String> mcArgs = new ArrayList<String>(Arrays.asList(mVersion.getMinecraftArguments().split(" ")));

        if(mcArgs.indexOf("${auth_player_name}") >= 0)
		    mcArgs.set(mcArgs.indexOf("${auth_player_name}"), mInfo.getUsername());
        if(mcArgs.indexOf("${auth_session}") >= 0)
            mcArgs.set(mcArgs.indexOf("${auth_session}"), mInfo.getSessionID());
        if(mcArgs.indexOf("${auth_uuid}") >= 0)
            mcArgs.set(mcArgs.indexOf("${auth_uuid}"), mInfo.getProfileID());
        if(mcArgs.indexOf("${auth_access_token}") >= 0)
            mcArgs.set(mcArgs.indexOf("${auth_access_token}"), mInfo.getAccessToken());
        if(mcArgs.indexOf("${version_name}") >= 0)
		    mcArgs.set(mcArgs.indexOf("${version_name}"), mInfo.getMinecraftVersion());
        if(mcArgs.indexOf("${game_directory}") >= 0)
		    mcArgs.set(mcArgs.indexOf("${game_directory}"), mInfo.getMinecraftPath().getMinecraftDir());
        if(mcArgs.indexOf("${game_assets}") >= 0)
		    mcArgs.set(mcArgs.indexOf("${game_assets}"), mInfo.getMinecraftPath().getResourcePath() + "virtual" + fs + "legacy");
        if(mcArgs.indexOf("${user_properties}") >= 0)
            mcArgs.set(mcArgs.indexOf("${user_properties}"), "{}");
        if(mcArgs.indexOf("${assets_index_name}") >= 0) {
        	if(mInfo.getMSV().hasAssets()) {
        		mcArgs.set(mcArgs.indexOf("${assets_index_name}"), mInfo.getMSV().getAssets() );
        	}
        	else {
        		mcArgs.set(mcArgs.indexOf("${assets_index_name}"), "legacy" );
        	}
        }
        if(mcArgs.indexOf("${assets_root}") >= 0) {
        	mcArgs.set(mcArgs.indexOf("${assets_root}"), mInfo.getMinecraftPath().getResourcePath() );
        }

		if(mInfo.hasServerAdress()) {
			mcArgs.add("--server");
			mcArgs.add(mInfo.getServerAddress());
            if(mInfo.hasServerPort()){
                mcArgs.add("--port");
                mcArgs.add(mInfo.getServerPort());
            }
		}

        if(mInfo.isFullscreen()) {
            mcArgs.add("--fullscreen");
        }
        //mcArgs.add("--demo");
        System.out.println(mcArgs.toString());
        mParams.addAll(mcArgs);
	}
}