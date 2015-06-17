package com.mapia.cache.realmcache.search;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class SearchUserHistoryCacheModel extends RealmObject
{
    private String iconUrl;
    private long memberNo;
    private String name;
    private String tag;
    private Date updateYmdt;

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

    public Date getUpdateYmdt() {
        return this.updateYmdt;
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

    public void setUpdateYmdt(final Date updateYmdt) {
        this.updateYmdt = updateYmdt;
    }
}