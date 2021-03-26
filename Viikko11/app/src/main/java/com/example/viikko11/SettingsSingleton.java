package com.example.viikko11;

public class SettingsSingleton {

    private boolean editing_enabled;

    private static SettingsSingleton settings = new SettingsSingleton();

    private SettingsSingleton() {
        editing_enabled = true;
    }

    public static SettingsSingleton getInstance() {
        return settings;
    }

    public boolean getEditingEnabled() {return editing_enabled;}
    public void setEditingEnabled(boolean bool) {editing_enabled = bool;}
}
