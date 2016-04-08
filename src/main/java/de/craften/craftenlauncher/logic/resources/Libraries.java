package de.craften.craftenlauncher.logic.resources;

import java.util.ArrayList;
import java.util.List;

public class Libraries {
    private List<LibEntry> mLiblist = new ArrayList<>();

    public Libraries() {
    }

    public void add(LibEntry entry) {
        mLiblist.add(entry);
    }

    public List<LibEntry> get() {
        return mLiblist;
    }
}
