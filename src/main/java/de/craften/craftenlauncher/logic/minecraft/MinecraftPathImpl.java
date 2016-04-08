package de.craften.craftenlauncher.logic.minecraft;

import java.io.File;


public class MinecraftPathImpl extends MinecraftPath {
    private String mVersion;

    public MinecraftPathImpl() {
        super();
    }

    public MinecraftPathImpl(String minecraftPath, String version) {
        super(minecraftPath);
        this.mVersion = version;
    }

    public MinecraftPathImpl(String path) {
        super(path);
    }

    @Override
    public String getNativeDir() {
        return super.getMinecraftDir() + "natives" + File.separator;
    }

    @Override
    public String getLibraryDir() {
        return super.getMinecraftDir() + "libraries" + File.separator;
    }

    @Override
    public String getMinecraftJarPath() {
        return getMinecraftVersionsDir() + mVersion + File.separator;
    }

    @Override
    public String getResourcePath() {
        return super.getMinecraftDir() + "assets" + File.separator;
    }

    @Override
    public String getMinecraftVersionsDir() {
        return super.getMinecraftDir() + "versions" + File.separator;
    }

    public void setVersionName(String version) {
        this.mVersion = version;
    }

}
