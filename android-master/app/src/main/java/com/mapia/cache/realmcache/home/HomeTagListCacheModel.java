package com.mapia.cache.realmcache.home;

/**
 * Created by daehyun on 15. 6. 8..
 */

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class HomeTagListCacheModel extends RealmObject
{
    private String id;
    private String tagList;

    public String getId() {
        return this.id;
    }

    public String getTagList() {
        return this.tagList;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setTagList(final String tagList) {
        this.tagList = tagList;
    }
}