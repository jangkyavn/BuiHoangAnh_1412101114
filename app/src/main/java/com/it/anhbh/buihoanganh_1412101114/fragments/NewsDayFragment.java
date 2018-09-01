package com.it.anhbh.buihoanganh_1412101114.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.it.anhbh.buihoanganh_1412101114.DetailActivity;
import com.it.anhbh.buihoanganh_1412101114.R;
import com.it.anhbh.buihoanganh_1412101114.adapters.CustomArrayAdapter;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.tasks.AsyncResponse;
import com.it.anhbh.buihoanganh_1412101114.tasks.MyAsyncTask;

import java.util.ArrayList;

public class NewsDayFragment extends Fragment {
    ListView lvNewsDay;
    CustomArrayAdapter adapter;
    ArrayList<News> arrNewsDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_day, container, false);

        lvNewsDay = view.findViewById(R.id.lv_news_day);

        loadData();
        registerEvents();

        return view;
    }

    private void registerEvents() {
        lvNewsDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("link", arrNewsDay.get(position).getLink());
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        new MyAsyncTask(new AsyncResponse() {
            @Override
            public void onCompleted(ArrayList<News> result) {
                arrNewsDay = result;
                adapter = new CustomArrayAdapter(getActivity(), R.layout.custom_list_item, arrNewsDay);
                lvNewsDay.setAdapter(adapter);
            }
        }).execute("https://www.24h.com.vn/upload/rss/tintuctrongngay.rss");
    }
}