package com.it.anhbh.buihoanganh_1412101114;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
import com.it.anhbh.buihoanganh_1412101114.utilities.Utility;

import java.util.ArrayList;
import java.util.Collections;

public class SavedActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvSaved;
    TextView tvNoData, tvNotFound;
    SearchView searchView;

    CustomArrayAdapter adapter;
    ArrayList<News> arrSaved;

    InternalStorage internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        toolbar = findViewById(R.id.toolbar);
        lvSaved = findViewById(R.id.lv_saved_news);
        tvNoData = findViewById(R.id.tv_no_data);
        tvNotFound = findViewById(R.id.tv_not_found);

        internalStorage = new InternalStorage(this);

        toolbar.setTitle(R.string.toolbar_saved_news_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        registerEvents();
    }

    private void loadData() {
        arrSaved = internalStorage.readFile(Constants.FILE_SAVED);
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
                                internalStorage.removeObject(news, Constants.FILE_SAVED);

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

    private void loadFilterData(String keyword) {
        ArrayList<News> filterList = new ArrayList<>();

        if (searchView != null) {
            for (News item : arrSaved) {
                String description = item.getDescription().length() > 95 ? item.getDescription().substring(0, 95) + "..." : item.getDescription();

                if (Utility.removeAccents(item.getTitle().toLowerCase()).contains(Utility.removeAccents(keyword.toLowerCase())) ||
                        Utility.removeAccents(description.toLowerCase()).contains(Utility.removeAccents(keyword.toLowerCase()))) {
                    filterList.add(item);
                }
            }
        } else {
            filterList = arrSaved;
        }

        adapter = new CustomArrayAdapter(SavedActivity.this, R.layout.custom_list_item, filterList);
        lvSaved.setAdapter(adapter);

        if (filterList.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
        } else {
            tvNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_saved_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                loadFilterData(s);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
}
