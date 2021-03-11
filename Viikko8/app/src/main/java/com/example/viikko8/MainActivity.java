package com.example.viikko8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textBottles;
    TextView textOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottleDispenser dispenser = BottleDispenser.getInstance();

        textBottles = (TextView) findViewById(R.id.textBottles);
        textOutput = (TextView) findViewById(R.id.textOutput);

        dispenser.listBottles(textBottles);
    }
}