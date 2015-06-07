package com.mapia.search.tag;

/**
 * Created by daehyun on 15. 6. 7..
 */

public class SearchTagModel
{
    private String tag;
    private int tagCount;

    public SearchTagModel(final String tag) {
        super();
        this.tag = tag;
    }

    public SearchTagModel(final String tag, final int tagCount) {
        super();
        this.tag = tag;
        this.tagCount = tagCount;
    }

    public String getTag() {
        return this.tag;
    }

    public int getTagCount() {
        return this.tagCount;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }

    public void setTagCount(final int tagCount) {
        this.tagCount = tagCount;
    }
}