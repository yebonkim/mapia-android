package com.mapia.cache.realmcache.search;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;

import com.mapia.cache.realmcache.RealmManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class SearchHistoryCacheWrapper
{
    private Context context;

    public SearchHistoryCacheWrapper(final Context context) {
        super();
        this.context = context;
    }

    public void deletePic(final String s) {
        new RealmManager(this.context).deleteSearchPic(s);
    }

    public void deletePicAll() {
        new RealmManager(this.context).deleteSearchPicAll();
    }

    public void deleteTag(final String s) {
        new RealmManager(this.context).deleteSearchTag(s);
    }

    public void deleteTagAll() {
        new RealmManager(this.context).deleteSearchTagAll();
    }

    public void deleteUser(final long n) {
        new RealmManager(this.context).deleteSearchUser(n);
    }

    public void deleteUserAll() {
        new RealmManager(this.context).deleteSearchUserAll();
    }

    public void insertPic(String replace) {
        if (StringUtils.isBlank(replace)) {
            return;
        }
        replace = replace.replace("", "");
        final RealmManager realmManager = new RealmManager(this.context);
        if (realmManager.containsSearchPic(replace)) {
            realmManager.updateSearchPic(replace);
            return;
        }
        if (realmManager.selectSearchPicCount() == 10) {
            realmManager.deleteSearchOldestPic();
        }
        realmManager.insertSearchPic(replace);
    }

    public void insertTag(String replace) {
        if (StringUtils.isBlank(replace)) {
            return;
        }
        replace = replace.replace("", "");
        final RealmManager realmManager = new RealmManager(this.context);
        if (realmManager.containsSearchTag(replace)) {
            realmManager.updateSearchTag(replace);
            return;
        }
        if (realmManager.selectSearchTagCount() == 10) {
            realmManager.deleteSearchOldestTag();
        }
        realmManager.insertSearchTag(replace);
    }

    public void insertUser(final long n, final String s, final String s2, final String s3) {
        if (n == 0L) {
            return;
        }
        final RealmManager realmManager = new RealmManager(this.context);
        if (realmManager.containsSearchUser(n)) {
            realmManager.updateSearchUser(n);
            return;
        }
        if (realmManager.selectSearchUserCount() == 10) {
            realmManager.deleteSearchOldestUser();
        }
        realmManager.insertSearchUser(n, s, s2, s3);
    }

    public void printPicHistory() {
        new RealmManager(this.context).printSearchPic();
    }

    public void printTagHistory() {
        new RealmManager(this.context).printSearchTag();
    }

    public void printUserHistory() {
        new RealmManager(this.context).printSearchUser();
    }

    public ArrayList<String> selectPicHistory() {
        final RealmManager realmManager = new RealmManager(this.context);
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<SearchPicHistoryCacheModel> iterator = realmManager.selectSearchPicList().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getTag());
        }
        return list;
    }

    public ArrayList<String> selectTagHistory() {
        final RealmManager realmManager = new RealmManager(this.context);
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<SearchTagHistoryCacheModel> iterator = realmManager.selectSearchTagList().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getTag());
        }
        return list;
    }

    public ArrayList<SearchUserHistoryCacheModel> selectUserHistory() {
        final RealmManager realmManager = new RealmManager(this.context);
        final ArrayList<SearchUserHistoryCacheModel> list = new ArrayList<SearchUserHistoryCacheModel>();
        final Iterator<SearchUserHistoryCacheModel> iterator = realmManager.selectSearchUserList().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}