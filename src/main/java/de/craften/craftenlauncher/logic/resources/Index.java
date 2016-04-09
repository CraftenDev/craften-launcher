package de.craften.craftenlauncher.logic.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Index {
    private static final Logger LOGGER = LogManager.getLogger(Index.class);
    private String mPath;
    private ArrayList<ResEntry> mList;
    private boolean mVirtual;

    public Index(String path) {
        this.mPath = path;
        mList = new ArrayList<>();

        buildIndex();
    }

    private void buildIndex() {
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(mPath)) {
            Object obj = parser.parse(reader);

            if (obj != null && !(obj instanceof JsonNull)) {
                JsonObject jObject = (JsonObject) obj;

                if (jObject.has("virtual")) {
                    if (jObject.get("virtual").getAsString().equals("true"))
                        setVirtual(true);
                    else if (jObject.get("virtual").getAsString().equals("false"))
                        setVirtual(false);

                }

                if (jObject.has("objects")) {
                    for (Map.Entry<String, JsonElement> entry : jObject.getAsJsonObject("objects").entrySet()) {


                        JsonObject res = (JsonObject) entry.getValue();

                        ResEntry resEntry = new ResEntry();
                        resEntry.setName(entry.getKey());
                        if (res.has("hash"))
                            resEntry.setHash(res.get("hash").getAsString());
                        if (res.has("size"))
                            resEntry.setSize(res.get("size").getAsInt());

                        resEntry.setVirtual(isVirtual());

                        mList.add(resEntry);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Could not read file", e);
        }
    }

    public void setVirtual(boolean virtual) {
        this.mVirtual = virtual;
    }

    public boolean isVirtual() {
        return mVirtual;
    }

    public List<ResEntry> getRes() {
        return mList;
    }
}
