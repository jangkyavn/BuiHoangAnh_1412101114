package com.it.anhbh.buihoanganh_1412101114.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.it.anhbh.buihoanganh_1412101114.DetailActivity;
import com.it.anhbh.buihoanganh_1412101114.R;
import com.it.anhbh.buihoanganh_1412101114.adapters.CustomArrayAdapter;
import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.storages.InternalStorage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class NewsDayFragment extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ListView lvNewsDay;
    ProgressBar progressBar;

    CustomArrayAdapter adapter;
    ArrayList<News> arrNewsDay;
    InternalStorage internalStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_day, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        lvNewsDay = view.findViewById(R.id.lv_news_day);
        progressBar = view.findViewById(R.id.progress_bar);

        internalStorage = new InternalStorage(getActivity());

        loadData();
        registerEvents();

        return view;
    }

    private void loadData() {
        NewsDayTask task = new NewsDayTask();
        task.execute("https://www.24h.com.vn/upload/rss/tintuctrongngay.rss");
    }

    private void registerEvents() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        lvNewsDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = arrNewsDay.get(position);
                internalStorage.addObject(news, Constants.FILE_READ_RECENTLY);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });
    }

    class NewsDayTask extends AsyncTask<String, Void, Document> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            lvNewsDay.setVisibility(View.GONE);
        }

        @Override
        protected Document doInBackground(String... strings) {
            Document document = null;

            try {
                document = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            Elements elements = document.select("item");

            News news = null;
            arrNewsDay = new ArrayList<>();

            for (Element element : elements) {
                news = new News();
                news.setTitle(element.select("title").text());
                news.setThumbnail(Jsoup.parse(element.select("description").text()).select("img").attr("src"));
                news.setLink(element.select("link").text());
                news.setPubDate(element.select("pubDate").text());

                arrNewsDay.add(news);
            }

            adapter = new CustomArrayAdapter(getActivity(), R.layout.custom_list_item, arrNewsDay);
            lvNewsDay.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            lvNewsDay.setVisibility(View.VISIBLE);
        }
    }
}