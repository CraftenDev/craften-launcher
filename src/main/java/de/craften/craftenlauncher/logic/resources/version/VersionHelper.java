package de.craften.craftenlauncher.logic.resources.version;

import de.craften.craftenlauncher.logic.minecraft.MinecraftPath;
import de.craften.craftenlauncher.logic.resources.LibEntry;
import de.craften.craftenlauncher.logic.resources.Libraries;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VersionHelper {
    public static List<String> getLibFilePathsAsArray(MinecraftPath info, Libraries libraries) {
        List<String> list = new ArrayList<String>();
        List<LibEntry> entries = libraries.get();
        for (LibEntry entry : entries) {
            File folder = new File(info.getLibraryDir() + File.separator + entry.getPath() + File.separator);
            File[] listAllFiles = folder.listFiles();
            if (listAllFiles != null) {
                for (File listAllFile : listAllFiles) {
                    list.add(listAllFile.getAbsolutePath());
                }
            }
        }
        return list;
    }

    public static List<String> getLibPathsAsArray(MinecraftPath info, Libraries libraries) {
        List<String> list = new ArrayList<>();
        List<LibEntry> entries = libraries.get();
        for (LibEntry entry : entries) {
            File folder = new File(info.getLibraryDir() + File.separator + entry.getPath());
            list.add(folder.getAbsolutePath());
        }
        return list;
    }

    public static String getLibFilessAsArgmument(MinecraftPath info, Libraries libraries) {
        String libPath = info.getLibraryDir(),
                argmument = "",
                libSep = File.pathSeparator;
        List<LibEntry> libEntries = libraries.get();

        for (LibEntry libEntry : libEntries) {
            if (libEntry.isNeeded() && !libEntry.isNativ()) {
                argmument += libPath + libEntry.getPath() + File.separator + libEntry.getFileName() + libSep;
            }
        }

        return argmument;
    }
}
