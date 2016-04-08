package de.craften.craftenlauncher.logic.resources;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.*;

public class Index {
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
        JsonObject jObject = new JsonObject();
        Object obj = null;

        try {
            FileReader reader = new FileReader(mPath);
            obj = parser.parse(reader);
        } catch (FileNotFoundException ex) {

        }

        if (obj != null && !(obj instanceof JsonNull)) {
            jObject = (JsonObject) obj;

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
    }

    public void setVirtual(boolean virtual) {
        this.mVirtual = virtual;
    }

    public boolean isVirtual() {
        return mVirtual;
    }

    public ArrayList<ResEntry> getRes() {
        return mList;
    }
}
