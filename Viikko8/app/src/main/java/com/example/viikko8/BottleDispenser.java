package com.example.viikko8;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class BottleDispenser {
    private static BottleDispenser dispenser = new BottleDispenser();
    private int bottles;
    private ArrayList<Bottle> bottle_list;
    private double money;

    private BottleDispenser() {
        bottles = 5;
        money = 0;

        bottle_list = new ArrayList();
        for (int i = 0; i < bottles; i++) {
            switch (i) {
                case (0):
                    bottle_list.add(new Bottle());
                    continue;

                case (1):
                    bottle_list.add(new Bottle("Pepsi Max", "Pepsi", 0.9, 1.5, 2.2));
                    continue;

                case (2):
                    bottle_list.add(new Bottle("Coca-Cola Zero", "The Coca-Cola Company", 0.3, 0.5, 2.0));
                    continue;

                case (3):
                    bottle_list.add(new Bottle("Coca-Cola Zero", "The Coca-Cola Company", 0.9, 1.5, 2.5));
                    continue;

                case (4):
                    bottle_list.add(new Bottle("Fanta Zero", "The Coca-Cola Company", 0.3, 0.5, 1.95));
                    continue;

                default:
                    bottle_list.add(new Bottle());
                    continue;
            }

        }
    }

    public static BottleDispenser getInstance() {
        return dispenser;
    }

    public void addMoney() {
        money += 1;
        System.out.println("Klink! Added more money!");
    }

    public void buyBottle(int bottle_number) {
        if (bottle_list.size() < bottle_number) {
            System.out.println("Bottle does not exist!");

        } else if (money <= bottle_list.get(bottle_number - 1).getPrice()) {
            System.out.println("Add money first!");

        }  else {
            money -= bottle_list.get(bottle_number - 1).getPrice();
            System.out.println("KACHUNK! " + bottle_list.get(bottle_number - 1).getName() + " came out of the dispenser!");
            removeBottle(bottle_number - 1);
        }
    }

    public void returnMoney() {
        Locale fi = new Locale("fi", "FI");
        String money_formatted = String.format(fi, "%.2f", money);
        System.out.printf("Klink klink. Money came out! You got %s€ back\n", money_formatted);
        money = 0;
    }

    public void listBottles(TextView textBottles) {
        Locale fi = new Locale("fi", "FI");
        textBottles.setText("*** BOTTLE DISPENSER ***\n");
        if (bottle_list.size() > 0) {
            for (Bottle bottle : bottle_list) {
                String price_formatted = String.format(fi, "%.2f", bottle.getPrice());
                textBottles.setText(textBottles.getText() + "\n" + bottle.getName() + ";\t\t" + bottle.getSize() + "l;\t\t" + price_formatted + "€");
                //System.out.println((bottle_list.indexOf(bottle) + 1) + ". Name: " + bottle.getName());
                //System.out.println("\tSize: " + bottle.getSize() + "\tPrice: " + bottle.getPrice());
            }
        } else {
            textBottles.setText("\nOut of bottles!");
        }
    }

    public void removeBottle(int index) {
        if (bottle_list.size() >= index + 1) {
            bottle_list.remove(index);
        } else {
            System.out.println("Bottle does not exist!");
        }
    }
}
