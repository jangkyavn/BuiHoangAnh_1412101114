package com.it.anhbh.buihoanganh_1412101114.storages;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.it.anhbh.buihoanganh_1412101114.models.News;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "News";
    public static final String TABLE_SAVED = "Saved";
    public static final String TABLE_HISTORY = "History";
    private static final String TITLE = "Title";
    private static final String THUMBNAIL = "Thumbnail";
    private static final String LINK = "Link";
    private static final String PUB_DATE = "PubDate";

    private Activity activity;

    public DBManager(Activity activity) {
        super(activity, DATABASE_NAME, null, 1);
        this.activity = activity;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbHistory = "CREATE TABLE " + TABLE_HISTORY + " (" +
                TITLE + " TEXT PRIMARY KEY, " +
                THUMBNAIL + " TEXT, " +
                LINK + " TEXT," +
                PUB_DATE + " TEXT)";

        String tbSaved = "CREATE TABLE " + TABLE_SAVED + " (" +
                TITLE + " TEXT PRIMARY KEY, " +
                THUMBNAIL + " TEXT, " +
                LINK + " TEXT," +
                PUB_DATE + " TEXT)";

        db.execSQL(tbHistory);
        db.execSQL(tbSaved);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED);
        onCreate(db);
    }

    public ArrayList<News> getAllNews(String tableName) {
        ArrayList<News> listNews = new ArrayList<News>();
        String selectQuery = "SELECT  * FROM " + tableName;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                News news = new News();
                news.setTitle(cursor.getString(0));
                news.setThumbnail(cursor.getString(1));
                news.setLink(cursor.getString(2));
                news.setPubDate(cursor.getString(3));

                listNews.add(news);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listNews;
    }

    public boolean isContain(News news, String tableName) {
        ArrayList<News> listNews = getAllNews(tableName);

        for (News item : listNews) {
            if (item.getTitle().equals(news.getTitle())) {
                return true;
            }
        }

        return false;
    }

    public void addNews(News news, String tableName) {
        if (!isContain(news, tableName)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TITLE, news.getTitle());
            values.put(THUMBNAIL, news.getThumbnail());
            values.put(LINK, news.getLink());
            values.put(PUB_DATE, news.getPubDate());

            db.insert(tableName, null, values);
            db.close();
        }
    }

    public void deleteNews(News news, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, TITLE + " = ?", new String[]{String.valueOf(news.getTitle())});
        db.close();
    }

    public void deleteAllNews(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }

    public boolean isSaved(News news) {
        String query = "SELECT  * FROM " + TABLE_SAVED + " WHERE " + TITLE + " = '" + news.getTitle() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int row = cursor.getCount();
        cursor.close();

        return row > 0;
    }
}
