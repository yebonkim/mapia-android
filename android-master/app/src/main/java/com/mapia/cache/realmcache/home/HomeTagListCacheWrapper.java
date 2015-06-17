package com.mapia.cache.realmcache.home;

/**
 * Created by daehyun on 15. 6. 8..
 */

import com.mapia.cache.realmcache.RealmManager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;


public class HomeTagListCacheWrapper
{
    public String getHomeTagList(final RealmManager realmManager, final String s) {
        HomeTagListCacheModel selectHomeTagList = realmManager.selectHomeTagList(s);
        if (selectHomeTagList != null) {
            final String tagList = selectHomeTagList.getTagList();
            if (StringUtils.isNotBlank(tagList)) {
                return tagList;
            }
        }
        return null;
    }

    public void putHomeTagList(final RealmManager realmManager, final String s, final JSONObject jsonObject) {
        String string;
        if (jsonObject == null) {
            string = "";
        }
        else {
            string = jsonObject.toString();
        }
        if (realmManager.containsHomeTagList(s)) {
            realmManager.updateHomeTagList(s, string);
            return;
        }
        realmManager.insertHomeTagList(s, string);
    }
}