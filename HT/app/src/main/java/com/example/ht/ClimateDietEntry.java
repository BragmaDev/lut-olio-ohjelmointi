package com.example.ht;

public class ClimateDietEntry extends Entry {
    private double dairy_emission;
    private double meat_emission;
    private double plant_emission;
    private double restaurant_emission;
    private double total_emission;
    private int[] inputs;

    public ClimateDietEntry(double d, double m, double p, double r, double t, int[] inputs) {
        this.dairy_emission = d;
        this.meat_emission = m;
        this.plant_emission = p;
        this.restaurant_emission = r;
        this.total_emission = t;
        this.inputs = inputs;
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

    public int[] getInputs() { return inputs; }

}
