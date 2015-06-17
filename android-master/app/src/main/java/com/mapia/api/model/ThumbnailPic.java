package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.io.Serializable;

public class ThumbnailPic implements Serializable
{
    private String fileType;
    private String picImageUrl;
    private Long picNo;

    public String getFileType() {
        return this.fileType;
    }

    public String getPicImageUrl() {
        return this.picImageUrl;
    }

    public Long getPicNo() {
        return this.picNo;
    }

    public void setFileType(final String fileType) {
        this.fileType = fileType;
    }

    public void setPicImageUrl(final String picImageUrl) {
        this.picImageUrl = picImageUrl;
    }

    public void setPicNo(final Long picNo) {
        this.picNo = picNo;
    }
}