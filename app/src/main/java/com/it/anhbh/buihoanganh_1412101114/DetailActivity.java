package com.it.anhbh.buihoanganh_1412101114;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.storages.InternalStorage;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    WebView webView;
    Button btnClose, btnSave, btnShare;

    InternalStorage internalStorage;
    News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        webView = findViewById(R.id.web_view);
        btnClose = findViewById(R.id.btn_close);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);

        internalStorage = new InternalStorage(this);

        Intent intent = getIntent();
        news = (News) intent.getSerializableExtra("news");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(news.getLink());

        registerEvents();
    }

    private void registerEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internalStorage.isSaved(news)) {
                    internalStorage.removeObject(news, Constants.FILE_SAVED_NEWS);
                    Toast.makeText(DetailActivity.this, "Xóa tin đã lưu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    internalStorage.addObject(news, Constants.FILE_SAVED_NEWS);
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
