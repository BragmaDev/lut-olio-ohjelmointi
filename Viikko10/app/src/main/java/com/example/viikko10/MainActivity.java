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

    EditText edit_address;
    Button bttn_refresh;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_address = (EditText) findViewById(R.id.editAddress);
        bttn_refresh = (Button) findViewById(R.id.bttnRefresh);
        web = (WebView) findViewById(R.id.webView);

        web.setWebViewClient(new WebViewClient());
        web.getSettings().setJavaScriptEnabled(true);

        edit_address.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String new_address = edit_address.getText().toString();
                loadWebsite(new_address);
                return true;
            }
            return false;
        });
    }

    private void loadWebsite(String url) {
        current_address = "http://" + url;
        web.loadUrl(current_address);
    }

    public void refreshSite(View v) {
        web.loadUrl(web.getUrl());
    }
}