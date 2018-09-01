package com.it.anhbh.buihoanganh_1412101114.models;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String image;
    private String link;
    private String pubDate;

    public News() {
    }

    public News(String title, String image, String link, String pubDate) {
        this.title = title;
        this.image = image;
        this.link = link;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
