package com.example.monkeytreasure.web;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.monkeytreasure.R;

import androidx.appcompat.app.AppCompatActivity;

public class myWebView extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usd_value);
        TextView usdGetValue = findViewById(R.id.json_usd);
        String dollarValue = getIntent().getStringExtra("value");
        usdGetValue.setText(dollarValue);

    }


    public void onClick(View view) {
        setContentView(R.layout.my_browser);
        webView = findViewById(R.id.dollarWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.banki.ru/products/currency/cash/moskva/");
        webView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
