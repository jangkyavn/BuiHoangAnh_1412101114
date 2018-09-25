package com.it.anhbh.buihoanganh_1412101114;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.it.anhbh.buihoanganh_1412101114.adapters.CustomArrayAdapter;
import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.storages.InternalStorage;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvHistory;
    CustomArrayAdapter adapter;
    Button btnDeleteAll;

    ArrayList<News> arrReadRecently;

    InternalStorage internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        lvHistory = findViewById(R.id.lv_read_recently);
        btnDeleteAll = findViewById(R.id.btn_delete_all);

        internalStorage = new InternalStorage(this);

        toolbar.setTitle(R.string.toolbar_read_recently_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadData();
        registerEvents();
    }

    private void loadData() {
        arrReadRecently = internalStorage.readFile(Constants.FILE_HISTORY);
        if (arrReadRecently != null) {
            Collections.reverse(arrReadRecently);
            adapter = new CustomArrayAdapter(this, R.layout.custom_list_item, arrReadRecently);
            lvHistory.setAdapter(adapter);
        }
    }

    private void registerEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = arrReadRecently.get(position);

                Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HistoryActivity.this, R.style.MyDialogTheme)
                        .setTitle("Xóa lịch sử")
                        .setMessage("Bạn có chắc chắn muốn xóa TOÀN BỘ lịch sử đọc tin?")
                        .setCancelable(false)
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                internalStorage.clearData(Constants.FILE_HISTORY);

                                loadData();
                            }
                        })
                        .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }
}
