package com.it.anhbh.buihoanganh_1412101114.storages;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class InternalStorage {
    Activity activity;

    public InternalStorage(Activity activity) {
        this.activity = activity;
    }

    public void addObject(News news, String fileName) {
        ArrayList<News> arrNews = readFile(fileName);

        if (arrNews != null) {
            if (!isContain(arrNews, news)) {
                arrNews.add(news);
            }
        } else {
            arrNews = new ArrayList<>();
            arrNews.add(news);
        }

        try {
            String jsonArray = new Gson().toJson(arrNews);

            FileOutputStream outputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(jsonArray.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeObject(News news, String fileName) {
        ArrayList<News> arrNews = readFile(fileName);

        int length = arrNews.size();
        for (int i = 0; i < length; i++) {
            if (isEqual(arrNews.get(i), news)) {
                arrNews.remove(i);
                break;
            }
        }

        try {
            String jsonList = new Gson().toJson(arrNews);

            FileOutputStream outputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(jsonList.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read data from file
    public ArrayList<News> readFile(String fileName) {
        try {
            FileInputStream inputStream = activity.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }

            // Create type
            Type listType = new TypeToken<ArrayList<News>>() {
            }.getType();
            // Cast json array follow type
            ArrayList<News> arrNews = new Gson().fromJson(buffer.toString(), listType);

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

            return arrNews;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void clearData(String fileName) {
        try {
            String data = new Gson().toJson(new ArrayList<News>());

            FileOutputStream outputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSaved(News news) {
        ArrayList<News> arrNews = readFile(Constants.FILE_SAVED);

        return arrNews != null && isContain(arrNews, news);
    }

    private boolean isContain(ArrayList<News> list, News object) {
        for (News item : list) {
            if (item.getTitle().toUpperCase().equals(object.getTitle().toUpperCase())) {
                return true;
            }
        }

        return false;
    }

    private boolean isEqual(News object1, News object2) {
        if (object1.getTitle().toUpperCase().equals(object2.getTitle().toUpperCase())) {
            return true;
        }

        return false;
    }
}
