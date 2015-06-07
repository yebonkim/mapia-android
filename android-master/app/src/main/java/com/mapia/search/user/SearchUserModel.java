package com.mapia.search.user;

/**
 * Created by daehyun on 15. 6. 6..
 */

public class SearchUserModel
{
    private int count;
    private String iconUrl;
    private long memberNo;
    private String name;
    private String tag;

    public SearchUserModel(final long memberNo, final String iconUrl, final String name, final String tag, final int count) {
        super();
        this.memberNo = memberNo;
        this.iconUrl = iconUrl;
        this.name = name;
        this.tag = tag;
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public long getMemberNo() {
        return this.memberNo;
    }

    public String getName() {
        return this.name;
    }

    public String getTag() {
        return this.tag;
    }

    public void setCount(final int n) {
        this.count = this.count;
    }

    public void setIconUrl(final String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setMemberNo(final long memberNo) {
        this.memberNo = memberNo;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }
}