package com.mapia.cache.realmcache.search;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;


@RealmClass
public class SearchTagHistoryCacheModel extends RealmObject
{
    private String tag;
    private Date updateYmdt;

    public String getTag() {
        return this.tag;
    }

    public Date getUpdateYmdt() {
        return this.updateYmdt;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }

    public void setUpdateYmdt(final Date updateYmdt) {
        this.updateYmdt = updateYmdt;
    }
}