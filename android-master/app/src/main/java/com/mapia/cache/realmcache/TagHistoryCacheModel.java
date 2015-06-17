package com.mapia.cache.realmcache;

/**
 * Created by daehyun on 15. 6. 8..
 */

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class TagHistoryCacheModel extends RealmObject
{
    private String id;
    private String tagHistory;

    public String getId() {
        return this.id;
    }

    public String getTagHistory() {
        return this.tagHistory;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setTagHistory(final String tagHistory) {
        this.tagHistory = tagHistory;
    }
}