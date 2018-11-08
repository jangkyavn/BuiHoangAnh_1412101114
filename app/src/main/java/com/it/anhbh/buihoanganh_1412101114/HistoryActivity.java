package com.it.anhbh.buihoanganh_1412101114;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.it.anhbh.buihoanganh_1412101114.adapters.CustomArrayAdapter;
import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.storages.InternalStorage;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvHistory;
    TextView tvNoData;

    CustomArrayAdapter adapter;
    ArrayList<News> arrHistory;

    InternalStorage internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        lvHistory = findViewById(R.id.lv_read_recently);
        tvNoData = findViewById(R.id.tv_no_data);

        internalStorage = new InternalStorage(this);

        toolbar.setTitle(R.string.toolbar_read_recently_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        registerEvents();
    }

    private void loadData() {
        arrHistory = internalStorage.readFile(Constants.FILE_HISTORY);
        if (arrHistory.size() > 0) {
            lvHistory.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);

            Collections.reverse(arrHistory);
            adapter = new CustomArrayAdapter(this, R.layout.custom_list_item, arrHistory);
            lvHistory.setAdapter(adapter);
        } else {
            lvHistory.setVisibility(View.GONE);
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

        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = arrHistory.get(position);

                Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                intent.putExtra(Constants.KEY_NEWS, news);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_history_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_all) {
            new AlertDialog.Builder(HistoryActivity.this, R.style.MyDialogTheme)
                    .setTitle("Xóa lịch sử")
                    .setMessage("Bạn có chắc chắn muốn xóa TOÀN BỘ lịch sử đọc tin?")
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.btn_text_agree), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            internalStorage.clearData(Constants.FILE_HISTORY);

                            loadData();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.btn_text_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
}
