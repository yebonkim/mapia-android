package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.io.Serializable;

public class SimplePic implements Serializable
{
    private String fileType;
    private String picImageColor;
    private int picImageHeight;
    private String picImageUrl;
    private int picImageWidth;
    private long picNo;
    private Picker picker;

    public String getFileType() {
        return this.fileType;
    }

    public String getPicImageColor() {
        return this.picImageColor;
    }

    public int getPicImageHeight() {
        return this.picImageHeight;
    }

    public String getPicImageUrl() {
        return this.picImageUrl;
    }

    public int getPicImageWidth() {
        return this.picImageWidth;
    }

    public long getPicNo() {
        return this.picNo;
    }

    public Picker getPicker() {
        return this.picker;
    }

    public void setFileType(final String fileType) {
        this.fileType = fileType;
    }

    public void setPicImageColor(final String picImageColor) {
        this.picImageColor = picImageColor;
    }

    public void setPicImageHeight(final int picImageHeight) {
        this.picImageHeight = picImageHeight;
    }

    public void setPicImageUrl(final String picImageUrl) {
        this.picImageUrl = picImageUrl;
    }

    public void setPicImageWidth(final int picImageWidth) {
        this.picImageWidth = picImageWidth;
    }

    public void setPicNo(final long picNo) {
        this.picNo = picNo;
    }

    public void setPicker(final Picker picker) {
        this.picker = picker;
    }
}