package com.it.anhbh.buihoanganh_1412101114.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

        Date pubDate = null;
        try {
            pubDate = dateFormat.parse(strPubDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //in milliseconds
        long diff = now.getTime() - pubDate.getTime();

        //long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffWeeks = diffDays / 7;

        if (diffWeeks > 0) {
            return diffWeeks + " tuần trước";
        }

        if (diffDays > 0) {
            return diffDays + " ngày trước";
        }

        if (diffHours > 0 && diffHours < 24) {
            return diffHours + " giờ trước";
        }

        if (diffMinutes > 0 && diffMinutes < 60) {
            return diffMinutes + " phút trước";
        }

        return "Vài giây trước";
    }

    public static boolean isConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getDeviceInformation() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String version = Build.VERSION.RELEASE;

        return manufacturer.toUpperCase() + " " + model + ", Android: " + version;
    }
}
