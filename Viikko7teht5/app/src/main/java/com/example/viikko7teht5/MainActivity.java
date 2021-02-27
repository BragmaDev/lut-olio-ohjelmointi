package com.example.viikko7teht5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    Context context = null;
    EditText textField;
    EditText filenameInput;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        textField = (EditText) findViewById(R.id.textField);
        filenameInput = (EditText) findViewById(R.id.filenameInput);
        errorText = (TextView) findViewById(R.id.errorText);
    }

    public void loadFile(View v) {
        try {
            String filename = filenameInput.getText().toString();
            InputStream in = context.openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            errorText.setText("");

            String s = "";
            while ((s = br.readLine()) != null) {
                textField.setText(s);
            }
            in.close();
        } catch (IOException e) {
            Log.e("IOException", "Error while reading");
            errorText.setText("Error: File not found");
        }
    }

    public void saveFile(View v) {
        try {
            String filename = filenameInput.getText().toString();
            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            errorText.setText("");

            String s = textField.getText().toString();
            out.write(s);
            out.close();
        } catch (IOException e) {
            Log.e("IOException", "Error while writing");
        }
    }
}