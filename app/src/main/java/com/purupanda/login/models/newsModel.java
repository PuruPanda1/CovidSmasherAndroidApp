package com.purupanda.login.models;

public class newsModel {
    private String newsTitle,newsSource,newsImage,newsUrl,desctiption;

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public newsModel(String newsTitle, String newsSource, String newsImage, String newsUrl, String desctiption) {
        this.newsTitle = newsTitle;
        this.newsSource = newsSource;
        this.newsImage = newsImage;
        this.newsUrl = newsUrl;
        this.desctiption=desctiption;
    }
}
