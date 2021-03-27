package com.example.viikko11;

public class SettingsSingleton {

    private boolean editing_enabled;
    private int font_size;
    private int width;
    private int height;
    private boolean all_caps;

    private static SettingsSingleton settings = new SettingsSingleton();

    private SettingsSingleton() {
        editing_enabled = true;
        font_size = 14;
    }

    public static SettingsSingleton getInstance() {
        return settings;
    }

    public boolean getEditingEnabled() { return editing_enabled; }
    public void setEditingEnabled(boolean b) { editing_enabled = b; }

    public void setFontSize(int i) { font_size = i; }
    public int getFontSize() { return font_size; }

    public void setWidth(int i) { width = i; }
    public int getWidth() { return width; }

    public void setHeight(int i) { height = i; }
    public int getHeight() { return height; }

    public boolean getAllCaps() { return all_caps; }
    public void setAllCaps(boolean b) { all_caps = b; }
}
