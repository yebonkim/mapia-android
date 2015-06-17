package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.io.Serializable;

public class ListingPic implements Serializable
{
    private String copyrightYn;
    private String fileType;
    private int likeCount;
    private long modifyDate;
    private String picImageColor;
    private int picImageHeight;
    private String picImageUrl;
    private int picImageWidth;
    private Long picNo;
    private Picker picker;
    private long regDate;
    private int repicCount;

    public String getCopyrightYn() {
        return this.copyrightYn;
    }

    public String getFileType() {
        return this.fileType;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public long getModifyDate() {
        return this.modifyDate;
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

    public Long getPicNo() {
        return this.picNo;
    }

    public Picker getPicker() {
        return this.picker;
    }

    public long getRegDate() {
        return this.regDate;
    }

    public int getRepicCount() {
        return this.repicCount;
    }

    public void setCopyrightYn(final String copyrightYn) {
        this.copyrightYn = copyrightYn;
    }

    public void setFileType(final String fileType) {
        this.fileType = fileType;
    }

    public void setLikeCount(final int likeCount) {
        this.likeCount = likeCount;
    }

    public void setModifyDate(final long modifyDate) {
        this.modifyDate = modifyDate;
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

    public void setPicNo(final Long picNo) {
        this.picNo = picNo;
    }

    public void setPicker(final Picker picker) {
        this.picker = picker;
    }

    public void setRegDate(final long regDate) {
        this.regDate = regDate;
    }

    public void setRepicCount(final int repicCount) {
        this.repicCount = repicCount;
    }
}