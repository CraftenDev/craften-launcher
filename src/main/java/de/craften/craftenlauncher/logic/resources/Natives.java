package de.craften.craftenlauncher.logic.resources;

public class Natives {
    private String mLinux, mWindows, mOsx;

    public Natives() {
    }

    public Natives(String linux, String windows, String osx) {
        this.mLinux = linux;
        this.mWindows = windows;
        this.mOsx = osx;
    }

    public void setLinux(String linux) {
        this.mLinux = linux;
    }

    public void setWindows(String windows) {
        this.mWindows = windows;
    }

    public void setOsx(String osx) {
        this.mOsx = osx;
    }

    public String getLinux() {
        return mLinux;
    }

    public String getWindows() {
        return mWindows;
    }

    public String getOsx() {
        return mOsx;
    }
}
