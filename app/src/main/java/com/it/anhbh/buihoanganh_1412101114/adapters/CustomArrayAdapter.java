package com.it.anhbh.buihoanganh_1412101114.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.anhbh.buihoanganh_1412101114.R;
import com.it.anhbh.buihoanganh_1412101114.models.News;
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
        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = this.activity.getLayoutInflater();
            view = inflater.inflate(this.resource, null);

            holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tv_title);
            holder.tvPubDate = view.findViewById(R.id.tv_pubdate);
            holder.ivImage = view.findViewById(R.id.iv_image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        News news = this.objects.get(position);

        holder.tvTitle.setText(Html.fromHtml(news.getTitle()));
        holder.tvPubDate.setText(news.getPubDate());
        Picasso.get().load(news.getImage()).into(holder.ivImage);

        return view;
    }

    class ViewHolder {
        TextView tvTitle, tvPubDate;
        ImageView ivImage;
    }
}
