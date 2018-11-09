package com.it.anhbh.buihoanganh_1412101114.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.it.anhbh.buihoanganh_1412101114.DetailActivity;
import com.it.anhbh.buihoanganh_1412101114.R;
import com.it.anhbh.buihoanganh_1412101114.adapters.CustomArrayAdapter;
import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.storages.InternalStorage;
import com.it.anhbh.buihoanganh_1412101114.utilities.Utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class FashionFragment extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ListView lvFashion;
    ProgressBar progressBar;
    LinearLayout noInternetLayout;
    Button btnRetry;
    SearchView searchView;
    TextView tvNotFound;

    CustomArrayAdapter adapter;
    ArrayList<News> arrFashion;

    InternalStorage internalStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fashion, container, false);
        setHasOptionsMenu(true);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        lvFashion = view.findViewById(R.id.lv_fashion);
        progressBar = view.findViewById(R.id.progress_bar);
        noInternetLayout = view.findViewById(R.id.no_internet_layout);
        btnRetry = view.findViewById(R.id.btn_retry);
        tvNotFound = view.findViewById(R.id.tv_not_found);

        internalStorage = new InternalStorage(getActivity());

        loadData();
        registerEvents();

        return view;
    }

    private void loadData() {
        FashionTask task = new FashionTask();
        task.execute(Constants.LINK_FASHION);
    }

    private void registerEvents() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (searchView != null) {
                    searchView.setIconified(true);
                }

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

        lvFashion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = arrFashion.get(position);
                internalStorage.addObject(news, Constants.FILE_HISTORY);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constants.KEY_NEWS, news);
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

    class FashionTask extends AsyncTask<String, Void, ArrayList<News>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            lvFashion.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<News> doInBackground(String... strings) {
            arrFashion = new ArrayList<>();

            try {
                Document document = Jsoup.connect(strings[0]).get();

                Elements elements = document.select(Constants.NodeName.ITEM);

                News news = null;
                arrFashion = new ArrayList<>();

                for (Element element : elements) {
                    news = new News();

                    String cData = element.select(Constants.NodeName.DESCRIPTION).text();
                    String desc = cData.substring(cData.lastIndexOf("/>") + 2, cData.length());
                    String src = Jsoup.parse(cData).select(Constants.NodeName.IMG).attr(Constants.Attribute.SRC);

                    news.setTitle(element.select(Constants.NodeName.TITLE).text());
                    news.setThumbnail(src);
                    news.setDescription(desc);
                    news.setLink(element.select(Constants.NodeName.LINK).text());
                    news.setPubDate(element.select(Constants.NodeName.PUB_DATE).text());

                    arrFashion.add(news);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return arrFashion;
        }

        @Override
        protected void onPostExecute(ArrayList<News> list) {
            super.onPostExecute(list);

            if (list.size() > 0) {
                adapter = new CustomArrayAdapter(getActivity(), R.layout.custom_list_item, list);
                lvFashion.setAdapter(adapter);

                progressBar.setVisibility(View.GONE);
                lvFashion.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                noInternetLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadFilterData(String keyword) {
        ArrayList<News> filterList = new ArrayList<>();

        if (searchView != null) {
            for (News item : arrFashion) {
                String description = item.getDescription().length() > 95 ? item.getDescription().substring(0, 95) + "..." : item.getDescription();

                if (Utility.removeAccents(item.getTitle().toLowerCase()).contains(Utility.removeAccents(keyword.toLowerCase())) ||
                        Utility.removeAccents(description.toLowerCase()).contains(Utility.removeAccents(keyword.toLowerCase()))) {
                    filterList.add(item);
                }
            }
        } else {
            filterList = arrFashion;
        }

        adapter = new CustomArrayAdapter(getActivity(), R.layout.custom_list_item, filterList);
        lvFashion.setAdapter(adapter);

        if (filterList.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
        } else {
            tvNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu, menu);

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
    }
}
