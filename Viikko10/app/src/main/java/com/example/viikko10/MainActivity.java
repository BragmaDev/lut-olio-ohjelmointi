package com.example.viikko10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String index_address = "file:///android_asset/index.html";
    String current_address = "";

    EditText edit_address;
    Button bttn_refresh;
    Button bttn_prev;
    Button bttn_next;
    Button bttn_shoutout;
    Button bttn_initialize;
    WebView web;

    WebBackForwardList history;

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

        web.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                history = web.copyBackForwardList();
                edit_address.setText(web.getOriginalUrl());
                current_address = web.getUrl();
                updateButtons();
            }
        });
        web.getSettings().setJavaScriptEnabled(true);

        edit_address.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String address_text = edit_address.getText().toString();

                if (!address_text.startsWith("http://") || !address_text.startsWith("https://")) {
                    // Checking if the user wrote "index.html" as the address
                    address_text = (address_text.equals("index.html") ? index_address : "http://" + address_text);
                }

                web.loadUrl(address_text);
                updateButtons();
                return true;
            }
            return false;
        });
    }

    public void prevSite(View v) {
        web.goBack();
    }

    public void nextSite(View v) {
        web.goForward();
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
        bttn_shoutout.setEnabled(current_address.equals(index_address));
        bttn_initialize.setEnabled(current_address.equals(index_address));
        bttn_prev.setEnabled(web.canGoBack());
        bttn_next.setEnabled(web.canGoForward());
        bttn_refresh.setEnabled(!current_address.equals(""));
    }
}