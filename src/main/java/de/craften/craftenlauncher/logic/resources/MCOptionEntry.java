package de.craften.craftenlauncher.logic.resources;

import java.util.regex.Pattern;

public class MCOptionEntry {
    private String mName;
    private String mValue;

    public MCOptionEntry(String name, String value) {
        this.mName = name;
        this.mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public String getClassFromValue() {
        mValue = mValue.toLowerCase();
        if (mValue.equals("false") || mValue.equals("true"))
            return "BOOLEAN";
        else if (mValue.contains(".")) {
            String[] dummy = mValue.split(Pattern.quote("."));
            try {
                Integer.parseInt(dummy[0]);
                Integer.parseInt(dummy[1]);
                return "DOUBLE";
            } catch (NumberFormatException e) {
            }
        } else {
            try {
                Integer.parseInt(mValue);
                return "INTEGER";
            } catch (NumberFormatException e) {
            }
        }
        return "STRING";
    }
}
