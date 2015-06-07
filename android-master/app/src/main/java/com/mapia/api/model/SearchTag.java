package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 7..
 */

import java.io.Serializable;

public class SearchTag implements Serializable
{
    private int documentCount;
    private String tag;

    public int getDocumentCount() {
        return this.documentCount;
    }

    public String getTag() {
        return this.tag;
    }

    public void setDocumentCount(final int documentCount) {
        this.documentCount = documentCount;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }
}