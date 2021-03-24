package com.example.viikko10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    String index_address = "file:///android_asset/index.html";
    String current_address = "";
    String prev_address = "";
    String next_address = "";

    EditText edit_address;
    Button bttn_refresh;
    Button bttn_prev;
    Button bttn_next;
    Button bttn_shoutout;
    Button bttn_initialize;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_address = (EditText) findViewById(R.id.editAddress);
        bttn_refresh = (Button) findViewById(R.id.bttnRefresh);
        bttn_prev = (Button) findViewById(R.id.bttnPrev);
        bttn_next = (Button) findViewById(R.id.bttnNext);
        bttn_shoutout = (Button) findViewById(R.id.bttnShoutOut);
        bttn_initialize = (Button) findViewById(R.id.bttnInitialize);
        web = (WebView) findViewById(R.id.webView);

        updateButtons();

        web.setWebViewClient(new WebViewClient());
        web.getSettings().setJavaScriptEnabled(true);

        edit_address.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String address_text = edit_address.getText().toString();
                // Checking if the user wrote "index.html" as the address
                String new_address = (address_text.equals("index.html") ? index_address : "http://" + address_text);
                if (!current_address.equals("")) {
                    prev_address = current_address;
                    next_address = "";
                }
                loadWebsite(new_address);
                updateButtons();
                return true;
            }
            return false;
        });
    }

    private void loadWebsite(String url) {
        current_address = url;
        web.loadUrl(current_address);
    }

    public void prevSite(View v) {
        next_address = current_address;
        loadWebsite(prev_address);
        edit_address.setText(current_address);
        prev_address = "";
        updateButtons();
    }

    public void nextSite(View v) {
        prev_address = current_address;
        loadWebsite(prev_address);
        edit_address.setText(current_address);
        next_address = "";
        updateButtons();
    }

    public void refreshSite(View v) {
        web.loadUrl(web.getUrl());
    }

    public void executeShoutOut(View v) {
        web.evaluateJavascript("javascript:shoutOut()", null);
    }

    public void executeInitialize(View v) {
        web.evaluateJavascript("javascript:initialize()", null);
    }

    private void updateButtons() {
        if (current_address.equals(index_address)) {
            bttn_shoutout.setEnabled(true);
            bttn_initialize.setEnabled(true);
        } else {
            bttn_shoutout.setEnabled(false);
            bttn_initialize.setEnabled(false);
        }

        if (!prev_address.equals("")) {
            bttn_prev.setEnabled(true);
        } else {
            bttn_prev.setEnabled(false);
        }

        if (!next_address.equals("")) {
            bttn_next.setEnabled(true);
        } else {
            bttn_next.setEnabled(false);
        }

        if (!current_address.equals("")) {
            bttn_refresh.setEnabled(true);
        } else {
            bttn_refresh.setEnabled(false);
        }
    }
}