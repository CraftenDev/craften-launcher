package de.craften.craftenlauncher.logic.resources;

public class Os {
    private String mName, mVersion;

    public Os() {
    }

    public Os(String name) {
        setName(name);
    }

    public Os(String name, String version) {
        setName(name);
        setVersion(version);
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getVersion() {
        return mVersion;
    }

    public String getName() {
        return mName;
    }

    public boolean hasVersion() {
        return getVersion() != null;
    }
}
