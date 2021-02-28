package com.example.viikko7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

        // Tekstin päivittäminen Enterillä
        textInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String new_text = textInput.getText().toString();
                    textLabel.setText(new_text);
                    return true;
                }
                return false;
            }
        });

        // Tekstin päivittäminen reaaliaikaisesti
        /*textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String new_text = textInput.getText().toString();
                textLabel.setText(new_text);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    public void updateText(View v) {
        System.out.println("Hello World!");
        String new_text = textInput.getText().toString();
        textLabel.setText(new_text);
    }
}