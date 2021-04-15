package com.example.ht;

import java.util.Date;

public class ClimateDietEntry extends Entry {
    private double dairy_emission;
    private double meat_emission;
    private double plant_emission;
    private double restaurant_emission;
    private double total_emission;

    public ClimateDietEntry(double d, double m, double p, double r, double t) {
        this.dairy_emission = d;
        this.meat_emission = m;
        this.plant_emission = p;
        this.restaurant_emission = r;
        this.total_emission = t;
        date = new Date();
    }

    public double[] getEmissions() {
        double[] arr = {
                dairy_emission,
                meat_emission,
                plant_emission,
                restaurant_emission,
                total_emission
        };
        return arr;
    }

}
