package com.weibo.skynet.model;

/**
 * Created by Administrator on 2016/5/8.
 */
public class ImageSelect {

    private int position;
    private String url;

    public ImageSelect(int position) {
        this.position = position;
    }

    public ImageSelect(int position, String url) {
        this.position = position;
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
