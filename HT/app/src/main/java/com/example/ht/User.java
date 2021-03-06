package com.example.ht;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String name;
    private String password;
    private double weight;
    private String diet;
    private boolean low_carbon;
    private ArrayList<Entry> climate_diet_entries;
    private ArrayList<Entry> weight_entries;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        climate_diet_entries = new ArrayList<>();
        weight_entries = new ArrayList<>();
        weight = 60.0f;
        diet = "Omnivore";
        low_carbon = true;
    }

    public String getName() { return name; }
    public String getPassword() { return password; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getDiet() { return diet; }
    public void setDiet(String diet) { this.diet = diet; }
    public boolean getLowCarbon() { return low_carbon; }
    public void setLowCarbon(boolean low_carbon) { this.low_carbon = low_carbon; }

    /* This method takes in the arraylist id parameter and returns either the climate diet entries
    or the weight entries arraylist based on that id (0 = climate diet, anything else = weight) */
    public ArrayList<Entry> getEntries(int arraylist_id) {
        if (arraylist_id == 0) {
            return climate_diet_entries;
        } else {
            return weight_entries;
        }
    }

    /* This method adds the passed Entry object to either the climate diet entries or the weight entries
    arraylist */
    public void addEntry(Entry entry) {
        if (entry instanceof ClimateDietEntry) {
            // Checking if the same date has an entry
            for (Entry e : climate_diet_entries) {
                if (e.getDate().equals(entry.getDate())) {
                    climate_diet_entries.remove(e);
                    break;
                }
            }
            climate_diet_entries.add(entry);
        } else {
            // Checking if the same date has an entry
            for (Entry e : weight_entries) {
                if (e.getDate().equals(entry.getDate())) {
                    weight_entries.remove(e);
                    break;
                }
            }
            weight_entries.add(entry);
        }
    }

}
