package com.weibo.skynet.model;

/**
 * Created by wxjqgt on 2016/1/2.
 */
public class ImageInfo {

    private String id;
    private String title;
    private String display_name;
    private long size;
    private String data;
    private String content_type;
    private String height;
    private String width;
    private String description;
    private String count;

    public ImageInfo() {
    }

    public ImageInfo(String id, String title, String display_name,
                     long size, String data, String content_type,
                     String height, String width, String description,
                     String count) {
        this.id = id;
        this.title = title;
        this.display_name = display_name;
        this.size = size;
        this.data = data;
        this.content_type = content_type;
        this.height = height;
        this.width = width;
        this.description = description;
        this.count = count;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id='" + id + '\'' +
                ", display_name='" + display_name + '\'' +
                ", size=" + size +
                ", data='" + data + '\'' +
                /*
                ", title='" + title + '\'' +
                ", content_type='" + content_type + '\'' +
                ", height='" + height + '\'' +
                ", width='" + width + '\'' +
                ", description='" + description + '\'' +
                ", count='" + count + '\'' +*/
                '}';
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
