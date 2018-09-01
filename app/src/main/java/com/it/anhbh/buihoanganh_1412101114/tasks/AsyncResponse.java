package com.it.anhbh.buihoanganh_1412101114.tasks;

import com.it.anhbh.buihoanganh_1412101114.models.News;

import java.util.ArrayList;

public interface AsyncResponse {
    void onCompleted(ArrayList<News> result);
}
