package com.example.ht;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String name;
    private float weight;
    private String diet;
    private boolean low_carbon;
    private ArrayList<Entry> climate_diet_entries;
    private ArrayList<Entry> weight_entries;

    public User(String name) {
        this.name = name;
        climate_diet_entries = new ArrayList<>();
        weight_entries = new ArrayList<>();
    }

    public String getName() { return name; }
    public float getWeight() { return weight; }
    public String getDiet() { return diet; }
    public boolean getLowCarbon() { return low_carbon; }
    public ArrayList<Entry> getEntries(int arraylist_id) {
        if (arraylist_id == 0) {
            return climate_diet_entries;
        } else {
            return weight_entries;
        }
    }
    public void setEntries(int arraylist_id, ArrayList<Entry> entries) {
        if (arraylist_id == 0) {
            climate_diet_entries = entries;
        } else {
            weight_entries = entries;
        }
    }

    public void addEntry(int arraylist_id, Entry entry) {
        if (arraylist_id == 0) {
            climate_diet_entries.add(entry);
        } else {
            weight_entries.add(entry);
        }
    }

}
