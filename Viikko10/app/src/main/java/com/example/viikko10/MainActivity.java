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

    String current_address = "";
    String index_address = "file:///android_asset/index.html";

    EditText edit_address;
    Button bttn_refresh;
    Button bttn_shoutout;
    Button bttn_initialize;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_address = (EditText) findViewById(R.id.editAddress);
        bttn_refresh = (Button) findViewById(R.id.bttnRefresh);
        bttn_shoutout = (Button) findViewById(R.id.bttnShoutOut);
        bttn_initialize = (Button) findViewById(R.id.bttnInitialize);
        web = (WebView) findViewById(R.id.webView);

        bttn_shoutout.setEnabled(false);
        bttn_initialize.setEnabled(false);

        web.setWebViewClient(new WebViewClient());
        web.getSettings().setJavaScriptEnabled(true);

        edit_address.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String address_text = edit_address.getText().toString();
                // Checking if the user wrote "index.html" as the address
                String new_address = (address_text.equals("index.html") ? index_address : "http://" + address_text);
                loadWebsite(new_address);
                return true;
            }
            return false;
        });
    }

    private void loadWebsite(String url) {
        current_address = url;
        web.loadUrl(current_address);
        if (current_address.equals(index_address)) {
            bttn_shoutout.setEnabled(true);
            bttn_initialize.setEnabled(true);
        } else {
            bttn_shoutout.setEnabled(false);
            bttn_initialize.setEnabled(false);
        }
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
}