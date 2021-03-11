package com.example.viikko8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    BottleDispenser dispenser;
    TextView textBottles;
    TextView textOutput;
    TextView textBalance;
    Spinner spinProducts;
    Spinner spinSizes;
    String product;
    String size;
    double balance;
    Locale fi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottleDispenser dispenser = BottleDispenser.getInstance();
        textBottles = (TextView) findViewById(R.id.textBottles);
        textOutput = (TextView) findViewById(R.id.textOutput);
        textBalance = (TextView) findViewById(R.id.textBalance);
        spinProducts = (Spinner) findViewById(R.id.spinProducts);
        spinSizes = (Spinner) findViewById(R.id.spinSizes);
        fi = new Locale("fi", "FI");

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.products, R.layout.spinner_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.sizes, R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinProducts.setAdapter(adapter1);
        spinSizes.setAdapter(adapter2);

        spinProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                product = "Pepsi Max";
            }
        });;

        spinSizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                size = "0,5l";
            }
        });;

        dispenser.listBottles(textBottles);
        updateBalance(0);
        checkForNullInstance();
    }

    public void purchase(View v) {
        int index = dispenser.findBottle(product, size);
        dispenser.buyBottle(index, textOutput);
        updateBalance(dispenser.getMoney());
        dispenser.listBottles(textBottles);
    }

    public void addMoney(View v) {
        dispenser.addMoney();
        updateBalance(dispenser.getMoney());
    }

    private void updateBalance(double new_balance) {
        balance = new_balance;
        String s = ("Balance: " + String.format(fi, "%.2f", balance) + "€");
        textBalance.setText(s);
    }

    // Prevents crash when calling the dispenser instance
    private void checkForNullInstance() {
        if (dispenser == null) {
            dispenser = BottleDispenser.getInstance();
        }
    }

}