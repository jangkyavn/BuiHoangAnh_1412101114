package com.it.anhbh.buihoanganh_1412101114.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.anhbh.buihoanganh_1412101114.R;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<News> {
    Activity activity;
    int resource;
    List<News> objects;

    public CustomArrayAdapter(@NonNull Activity activity, int resource, @NonNull List<News> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = this.activity.getLayoutInflater();
            convertView = inflater.inflate(this.resource, null);

            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.tvDescription = convertView.findViewById(R.id.tv_description);
            holder.tvPubDate = convertView.findViewById(R.id.tv_pubdate);
            holder.ivThumbnail = convertView.findViewById(R.id.iv_thumbnail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News news = this.objects.get(position);

        holder.tvTitle.setText(Html.fromHtml(news.getTitle()));
        String description = news.getDescription().length() > 95 ? news.getDescription().substring(0, 95) + "..." : news.getDescription();
        holder.tvDescription.setText(description.replace("&amp;#34;", "''"));
        holder.tvPubDate.setText(Utility.getPeriod(news.getPubDate()));
        Picasso.get().load(news.getThumbnail()).into(holder.ivThumbnail);

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle, tvDescription, tvPubDate;
        ImageView ivThumbnail;
    }
}
