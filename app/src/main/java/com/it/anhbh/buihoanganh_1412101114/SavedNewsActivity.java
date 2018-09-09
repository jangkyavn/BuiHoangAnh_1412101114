package com.it.anhbh.buihoanganh_1412101114;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.it.anhbh.buihoanganh_1412101114.adapters.CustomArrayAdapter;
import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.storages.InternalStorage;

import java.util.ArrayList;
import java.util.Collections;

public class SavedNewsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvSavedNews;
    CustomArrayAdapter adapter;

    ArrayList<News> arrSavedNews;

    InternalStorage internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_news);

        toolbar = findViewById(R.id.toolbar);
        lvSavedNews = findViewById(R.id.lv_saved_news);

        internalStorage = new InternalStorage(this);

        toolbar.setTitle(R.string.toolbar_saved_news_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        registerEvents();
    }

    private void loadData() {
        arrSavedNews = internalStorage.readFile(Constants.FILE_SAVED_NEWS);
        if (arrSavedNews != null) {
            Collections.reverse(arrSavedNews);
            adapter = new CustomArrayAdapter(this, R.layout.custom_list_item, arrSavedNews);
            lvSavedNews.setAdapter(adapter);
        }
    }

    private void registerEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lvSavedNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = arrSavedNews.get(position);

                Intent intent = new Intent(SavedNewsActivity.this, DetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
}
