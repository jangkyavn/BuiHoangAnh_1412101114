package com.it.anhbh.buihoanganh_1412101114.utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utility {
    public static String getPeriod(String strPubDate) {
        Date now = new Date();

        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

        Date pubDate = null;
        try {
            pubDate = dateFormat.parse(strPubDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String result = "";

        //in milliseconds
        long diff = now.getTime() - pubDate.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays > 0) {
            result = diffDays + " ngày trước";
        } else if (diffDays == 0 && diffHours > 0 && diffHours < 24) {
            result = diffHours + " giờ trước";
        } else if (diffDays == 0 && diffHours == 0 && diffMinutes > 0 && diffMinutes < 60) {
            result = diffMinutes + " phút trước";
        } else if (diffDays == 0 && diffHours == 0 && diffMinutes == 0 && diffSeconds > 0 && diffSeconds < 60) {
            result = "Vài giây trước";
        }

        return result;
    }

    public static String getContentFromUrl(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
