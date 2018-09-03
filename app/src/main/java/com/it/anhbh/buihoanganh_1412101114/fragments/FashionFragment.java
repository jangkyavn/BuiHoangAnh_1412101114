package com.it.anhbh.buihoanganh_1412101114.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.it.anhbh.buihoanganh_1412101114.DetailActivity;
import com.it.anhbh.buihoanganh_1412101114.R;
import com.it.anhbh.buihoanganh_1412101114.adapters.CustomArrayAdapter;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.utilities.Utility;
import com.it.anhbh.buihoanganh_1412101114.utilities.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FashionFragment extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ListView lvFashion;
    CustomArrayAdapter adapter;
    ArrayList<News> arrFashion;

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fashion, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        lvFashion = view.findViewById(R.id.lv_fashion);
        progressBar = view.findViewById(R.id.progress_bar);

        loadData();
        registerEvents();

        return view;
    }

    private void loadData() {
        FashionTask task = new FashionTask();
        task.execute("https://www.24h.com.vn/upload/rss/thoitrang.rss");
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
                }, 2000);
            }
        });

        lvFashion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("link", arrFashion.get(position).getLink());
                startActivity(intent);
            }
        });
    }

    class FashionTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            lvFashion.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return Utility.getContentFromUrl(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);

            NodeList nodeItems = document.getElementsByTagName("item");
            NodeList nodeDescriptions = document.getElementsByTagName("description");

            News news = null;
            String image = "";
            arrFashion = new ArrayList<>();

            int nodeLength = nodeItems.getLength();
            for (int i = 0; i < nodeLength; i++) {
                String cData = nodeDescriptions.item(i + 1).getTextContent();
                Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = pattern.matcher(cData);
                if (matcher.find()) {
                    image = matcher.group(1);
                }

                news = new News();

                Element element = (Element) nodeItems.item(i);

                news.setTitle(parser.getValue(element, "title"));
                news.setImage(image);
                news.setLink(parser.getValue(element, "link"));
                news.setPubDate(parser.getValue(element, "pubDate"));

                arrFashion.add(news);
            }

            adapter = new CustomArrayAdapter(getActivity(), R.layout.custom_list_item, arrFashion);
            lvFashion.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            lvFashion.setVisibility(View.VISIBLE);
        }
    }
}