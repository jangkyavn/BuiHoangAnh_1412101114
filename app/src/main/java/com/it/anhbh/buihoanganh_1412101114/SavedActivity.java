package com.it.anhbh.buihoanganh_1412101114;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.it.anhbh.buihoanganh_1412101114.adapters.CustomArrayAdapter;
import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.storages.DBManager;

import java.util.ArrayList;
import java.util.Collections;

public class SavedActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvSaved;
    TextView tvNoData;

    CustomArrayAdapter adapter;
    ArrayList<News> arrSaved;

    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        toolbar = findViewById(R.id.toolbar);
        lvSaved = findViewById(R.id.lv_saved_news);
        tvNoData = findViewById(R.id.tv_no_data);

        dbManager = new DBManager(this);

        toolbar.setTitle(R.string.toolbar_saved_news_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        registerEvents();
    }

    private void loadData() {
        arrSaved = dbManager.getAllNews(DBManager.TABLE_SAVED);
        if (arrSaved.size() > 0) {
            lvSaved.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);

            Collections.reverse(arrSaved);
            adapter = new CustomArrayAdapter(this, R.layout.custom_list_item, arrSaved);
            lvSaved.setAdapter(adapter);
        } else {
            lvSaved.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
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
                News news = arrSaved.get(position);

                Intent intent = new Intent(SavedActivity.this, DetailActivity.class);
                intent.putExtra(Constants.KEY_NEWS, news);
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
                        .setPositiveButton(getResources().getString(R.string.btn_text_agree), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                News news = arrSaved.get(position);
                                dbManager.deleteNews(news, DBManager.TABLE_SAVED);

                                loadData();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.btn_text_cancel), new DialogInterface.OnClickListener() {
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
