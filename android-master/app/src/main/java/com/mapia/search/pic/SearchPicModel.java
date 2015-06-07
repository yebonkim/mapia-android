package com.mapia.search.pic;

/**
 * Created by daehyun on 15. 6. 7..
 */

public class SearchPicModel
{
    private String tag;
    private int tagCount;

    public SearchPicModel(final String tag) {
        super();
        this.tag = tag;
    }

    public SearchPicModel(final String tag, final int tagCount) {
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