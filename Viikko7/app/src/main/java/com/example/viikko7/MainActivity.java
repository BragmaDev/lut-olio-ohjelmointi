package com.example.viikko7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextView textLabel;
    EditText textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textLabel = (TextView) findViewById(R.id.textLabel);
        textInput = (EditText) findViewById(R.id.textInput);
    }

    public void updateText(View v) {
        System.out.println("Hello World!");
        String new_text = textInput.getText().toString();
        textLabel.setText(new_text);
    }
}