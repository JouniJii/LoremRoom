package com.example.loremroom;

import com.google.gson.annotations.SerializedName;

public class LoremObject {
    @SerializedName("file")
    private String url;

    @SerializedName("license")
    private String license;

    @SerializedName("owner")
    private String owner;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("filter")
    private String filter;

    @SerializedName("tags")
    private String tags;

    @SerializedName("tagMode")
    private String tagMode;

    public LoremObject(String url, String license, String owner, int width, int height, String filter, String tags, String tagMode) {
        this.url = url;
        this.license = license;
        this.owner = owner;
        this.width = width;
        this.height = height;
        this.filter = filter;
        this.tags = tags;
        this.tagMode = tagMode;
    }

    public String getTagMode() {
        return tagMode;
    }
    public void setTagMode(String tagMode) {
        this.tagMode = tagMode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
