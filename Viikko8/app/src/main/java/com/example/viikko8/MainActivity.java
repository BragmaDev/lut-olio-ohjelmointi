package com.example.viikko8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
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
    SeekBar seekAmount;
    Button bttnAdd;
    String product;
    String size;
    double balance;
    double amount;
    Locale fi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dispenser = BottleDispenser.getInstance();
        textBottles = (TextView) findViewById(R.id.textBottles);
        textOutput = (TextView) findViewById(R.id.textOutput);
        textBalance = (TextView) findViewById(R.id.textBalance);
        spinProducts = (Spinner) findViewById(R.id.spinProducts);
        spinSizes = (Spinner) findViewById(R.id.spinSizes);
        seekAmount = (SeekBar) findViewById(R.id.seekAmount);
        bttnAdd = (Button) findViewById(R.id.bttnAdd);
        fi = new Locale("fi", "FI");

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.products, R.layout.spinner_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.sizes, R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinProducts.setAdapter(adapter1);
        spinSizes.setAdapter(adapter2);
        seekAmount.setProgress(0);

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

        seekAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amount = progress / 10; // Range of 0.00-10.00
                String s = ("+" + String.format(fi, "%.2f", amount) + "€");
                bttnAdd.setText(s);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        dispenser.listBottles(textBottles);
        updateBalance(0);
    }

    public void purchase(View v) {
        int index = dispenser.findBottle(product, size);
        dispenser.buyBottle(index, textOutput);
        updateBalance(dispenser.getMoney());
        dispenser.listBottles(textBottles);
    }

    public void addMoney(View v) {
        dispenser.addMoney(textOutput, amount);
        updateBalance(dispenser.getMoney());
        seekAmount.setProgress(0);
    }

    public void returnMoney(View v) {
        dispenser.returnMoney(textOutput);
        updateBalance(dispenser.getMoney());
    }

    private void updateBalance(double new_balance) {
        balance = new_balance;
        String s = ("Balance: " + String.format(fi, "%.2f", balance) + "€");
        textBalance.setText(s);
    }
}