package com.it.anhbh.buihoanganh_1412101114;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.storages.InternalStorage;

public class DetailActivity extends AppCompatActivity {
    WebView webView;
    Button btnBack, btnSave, btnShare;

    InternalStorage internalStorage;
    News news;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        webView = findViewById(R.id.web_view);
        btnBack = findViewById(R.id.btn_back);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);

        internalStorage = new InternalStorage(this);

        Intent intent = getIntent();
        news = (News) intent.getSerializableExtra("news");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(news.getLink());
        webView.setWebViewClient(new WebViewClient());

        registerEvents();
    }

    private void registerEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internalStorage.isSaved(news)) {
                    internalStorage.removeObject(news, Constants.FILE_SAVED);
                    Toast.makeText(DetailActivity.this, "Xóa tin đã lưu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    internalStorage.addObject(news, Constants.FILE_SAVED);
                    Toast.makeText(DetailActivity.this, "Lưu tin thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, news.getLink());

                startActivity(Intent.createChooser(share, "Chia sẻ với"));
            }
        });
    }
}
