package com.example.viikko8;

import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class BottleDispenser {
    private static BottleDispenser dispenser = null;
    private int bottles;
    private ArrayList<Bottle> bottle_list;
    private double money;
    private Locale fi = new Locale("fi", "FI");

    private BottleDispenser() {
        bottles = 5;
        money = 0;

        bottle_list = new ArrayList();
        for (int i = 0; i < bottles; i++) {
            switch (i) {
                case (1):
                    bottle_list.add(new Bottle("Pepsi Max", "Pepsi", 0.9, "1,5l", 2.2));
                    continue;

                case (2):
                    bottle_list.add(new Bottle("Coca-Cola Zero", "The Coca-Cola Company", 0.3, "0,5l", 2.0));
                    continue;

                case (3):
                    bottle_list.add(new Bottle("Coca-Cola Zero", "The Coca-Cola Company", 0.9, "1,5l", 2.5));
                    continue;

                case (4):
                    bottle_list.add(new Bottle("Fanta Zero", "The Coca-Cola Company", 0.3, "0,5l", 1.95));
                    continue;

                default:
                    bottle_list.add(new Bottle());
                    continue;
            }

        }
    }

    public static BottleDispenser getInstance() {
        if (dispenser == null) {
            dispenser = new BottleDispenser();
        }
        return dispenser;
    }

    public void addMoney() {
        money += 1;
        System.out.println("Klink! Added more money!");
    }

    public void buyBottle(int bottle_index, TextView textOutput) {
        if (bottle_index == -1) {
            textOutput.setText("Bottle does not exist!");

        } else if (money <= bottle_list.get(bottle_index).getPrice()) {
            textOutput.setText("Add money first!");

        }  else {
            money -= bottle_list.get(bottle_index).getPrice();
            textOutput.setText("KACHUNK! " + bottle_list.get(bottle_index).getName() + " came out of the dispenser!");
            removeBottle(bottle_index);
        }
    }

    public double getMoney() {
        return money;
    }

    public void returnMoney(TextView textOutput) {
        String money_formatted = String.format(fi, "%.2f", money);
        textOutput.setText("Klink klink. Money came out! You got " + money_formatted + "€ back\n");
        money = 0;
    }

    public void listBottles(TextView textBottles) {
        textBottles.setText("*** BOTTLE DISPENSER ***\n");
        if (bottle_list.size() > 0) {
            for (Bottle bottle : bottle_list) {
                String price_formatted = String.format(fi, "%.2f", bottle.getPrice());
                textBottles.setText(textBottles.getText() + "\n" + bottle.getName() + ";\t\t" + bottle.getSize() + ";\t\t" + price_formatted + "€");
            }
        } else {
            textBottles.setText(textBottles.getText() + "\nOut of bottles!");
        }
    }

    public void removeBottle(int index) {
        if (bottle_list.size() >= index + 1) {
            bottle_list.remove(index);
        } else {
            System.out.println("Bottle does not exist!");
        }
    }

    public int findBottle(String product, String size) {
        System.out.println("TEST1");
        for (Bottle bottle : bottle_list) {
            System.out.println("TEST2");
            if (bottle.getName().equals(product) && bottle.getSize().equals(size)) {
                System.out.println("TEST3");
                return bottle_list.indexOf(bottle);
            }
        }
        return -1;
    }
}
