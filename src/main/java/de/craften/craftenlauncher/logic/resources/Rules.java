package de.craften.craftenlauncher.logic.resources;

public class Rules {
    private String mAction;
    private Os mOs;

    public Rules() {
    }

    public Rules(String action) {
        setAction(action);
    }

    public Rules(String action, Os os) {
        setAction(action);
        setOs(os);
    }

    public String getAction() {
        return mAction;
    }

    public Os getOs() {
        return mOs;
    }

    public void setAction(String action) {
        this.mAction = action;
    }

    public void setOs(Os os) {
        this.mOs = os;
    }
}
