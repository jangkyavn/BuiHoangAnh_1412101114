package com.it.anhbh.buihoanganh_1412101114.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class TravelFragment extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ListView lvTravel;
    ProgressBar progressBar;
    LinearLayout noInternetLayout;
    Button btnRetry;

    CustomArrayAdapter adapter;
    ArrayList<News> arrTravel;
    InternalStorage internalStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        lvTravel = view.findViewById(R.id.lv_travel);
        progressBar = view.findViewById(R.id.progress_bar);
        noInternetLayout = view.findViewById(R.id.no_internet_layout);
        btnRetry = view.findViewById(R.id.btn_retry);

        internalStorage = new InternalStorage(getActivity());

        loadData();
        registerEvents();

        return view;
    }

    private void loadData() {
        TravelTask task = new TravelTask();
        task.execute("https://www.24h.com.vn/upload/rss/dulich24h.rss");
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

        lvTravel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = arrTravel.get(position);

                internalStorage.addObject(news, Constants.FILE_HISTORY);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noInternetLayout.setVisibility(View.GONE);
                loadData();
            }
        });
    }

    class TravelTask extends AsyncTask<String, Void, ArrayList<News>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            lvTravel.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<News> doInBackground(String... strings) {
            arrTravel = new ArrayList<>();

            try {
                Document document = Jsoup.connect(strings[0]).get();

                Elements elements = document.select("item");

                News news = null;
                arrTravel = new ArrayList<>();

                for (Element element : elements) {
                    news = new News();
                    news.setTitle(element.select("title").text());
                    news.setThumbnail(Jsoup.parse(element.select("description").text()).select("img").attr("src"));
                    news.setLink(element.select("link").text());
                    news.setPubDate(element.select("pubDate").text());

                    arrTravel.add(news);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return arrTravel;
        }

        @Override
        protected void onPostExecute(ArrayList<News> list) {
            super.onPostExecute(list);

            if (list.size() > 0) {
                adapter = new CustomArrayAdapter(getActivity(), R.layout.custom_list_item, list);
                lvTravel.setAdapter(adapter);

                progressBar.setVisibility(View.GONE);
                lvTravel.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                noInternetLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}