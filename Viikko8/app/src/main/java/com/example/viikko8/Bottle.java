package com.example.viikko8;

public class Bottle {
    private String name = "Pepsi Max";
    private String manufacturer = "Pepsi";
    private double total_energy = 0.3;
    private double size = 0.5;
    private double price = 1.80;

    public Bottle() {}
    public Bottle(String _name, String _manuf, double _totE, double _size, double _price) {
        name = _name;
        manufacturer = _manuf;
        total_energy = _totE;
        size = _size;
        price = _price;
    }

    public String getName() {
        return name;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public double getEnergy() {
        return total_energy;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }
}
