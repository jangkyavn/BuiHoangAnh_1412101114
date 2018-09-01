package com.it.anhbh.buihoanganh_1412101114;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class DetailActivity extends AppCompatActivity {
    WebView webView;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        webView = findViewById(R.id.web_view);
        btnClose = findViewById(R.id.btn_close);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());;

        registerEvents();
    }

    private void registerEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
