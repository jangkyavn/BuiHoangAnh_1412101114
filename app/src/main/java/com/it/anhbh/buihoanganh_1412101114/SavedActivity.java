package com.it.anhbh.buihoanganh_1412101114;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class SavedActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvSaved;
    CustomArrayAdapter adapter;

    ArrayList<News> arrSavedNews;

    InternalStorage internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        toolbar = findViewById(R.id.toolbar);
        lvSaved = findViewById(R.id.lv_saved_news);

        internalStorage = new InternalStorage(this);

        toolbar.setTitle(R.string.toolbar_saved_news_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        registerEvents();
    }

    private void loadData() {
        arrSavedNews = internalStorage.readFile(Constants.FILE_SAVED);
        if (arrSavedNews != null) {
            Collections.reverse(arrSavedNews);
            adapter = new CustomArrayAdapter(this, R.layout.custom_list_item, arrSavedNews);
            lvSaved.setAdapter(adapter);
        }
    }

    private void registerEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lvSaved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = arrSavedNews.get(position);

                Intent intent = new Intent(SavedActivity.this, DetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });

        lvSaved.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(SavedActivity.this, R.style.MyDialogTheme)
                        .setTitle("Xóa tin đã lưu")
                        .setMessage("Bạn có chắc chắn muốn xóa tin đã lưu này không?")
                        .setCancelable(false)
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                News news = arrSavedNews.get(position);
                                internalStorage.removeObject(news, Constants.FILE_SAVED);

                                loadData();
                            }
                        })
                        .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
}
