package com.mapia.cache.realmcache;

/**
 * Created by daehyun on 15. 6. 8..
 */
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

import android.content.Context;

import com.mapia.cache.realmcache.home.HomeTagListCacheModel;
import com.mapia.cache.realmcache.search.SearchPicHistoryCacheModel;
import com.mapia.cache.realmcache.search.SearchTagHistoryCacheModel;
import com.mapia.cache.realmcache.search.SearchUserHistoryCacheModel;

import java.util.Date;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;


public class RealmManager
{

    private Realm realm;

    public RealmManager(Context context)
    {
        try
        {
            realm = Realm.getInstance(context);
            return;
        }
        catch (RealmMigrationNeededException realmmigrationneededexception)
        {
            Realm.deleteRealmFile(context);
        }
    }

    public void cleanTagHistory()
    {
        RealmResults realmresults = realm.where(TagHistoryCacheModel.class).findAll();
        realm.beginTransaction();
        realmresults.clear();
        realm.commitTransaction();
    }

    public boolean containsHomeTagList(String s)
    {
        return realm.where(HomeTagListCacheModel.class).equalTo("id", s).findAll().size() != 0;
    }

    public boolean containsSearchPic(String s)
    {
        return realm.where(SearchPicHistoryCacheModel.class).equalTo("tag", s).findAll().size() != 0;
    }

    public boolean containsSearchTag(String s)
    {
        return realm.where(SearchTagHistoryCacheModel.class).equalTo("tag", s).findAll().size() != 0;
    }

    public boolean containsSearchUser(long l)
    {
        return realm.where(SearchUserHistoryCacheModel.class).equalTo("memberNo", l).findAll().size() != 0;
    }

    public boolean containsTagHistory(String s)
    {
        return realm.where(TagHistoryCacheModel.class).equalTo("id", s).findAll().size() != 0;
    }

    public void deleteSearchOldestPic()
    {
        RealmResults realmresults = realm.where(SearchPicHistoryCacheModel.class).findAll();
        realmresults.sort("updateYmdt", true);
        realm.beginTransaction();
        ((SearchPicHistoryCacheModel)realmresults.get(0)).removeFromRealm();
        realm.commitTransaction();
    }

    public void deleteSearchOldestTag()
    {
        RealmResults realmresults = realm.where(SearchTagHistoryCacheModel.class).findAll();
        realmresults.sort("updateYmdt", true);
        realm.beginTransaction();
        ((SearchTagHistoryCacheModel)realmresults.get(0)).removeFromRealm();
        realm.commitTransaction();
    }

    public void deleteSearchOldestUser()
    {
        RealmResults realmresults = realm.where(SearchUserHistoryCacheModel.class).findAll();
        realmresults.sort("updateYmdt", true);
        realm.beginTransaction();
        ((SearchUserHistoryCacheModel)realmresults.get(0)).removeFromRealm();
        realm.commitTransaction();
    }

    public void deleteSearchPic(String s)
    {
        RealmResults realmresults = realm.where(SearchPicHistoryCacheModel.class).equalTo("tag", s).findAll();
        realm.beginTransaction();
        realmresults.clear();
        realm.commitTransaction();
    }

    public void deleteSearchPicAll()
    {
        RealmResults realmresults = realm.where(SearchPicHistoryCacheModel.class).findAll();
        realm.beginTransaction();
        realmresults.clear();
        realm.commitTransaction();
    }

    public void deleteSearchTag(String s)
    {
        RealmResults realmresults = realm.where(SearchTagHistoryCacheModel.class).equalTo("tag", s).findAll();
        realm.beginTransaction();
        realmresults.clear();
        realm.commitTransaction();
    }

    public void deleteSearchTagAll()
    {
        RealmResults realmresults = realm.where(SearchTagHistoryCacheModel.class).findAll();
        realm.beginTransaction();
        realmresults.clear();
        realm.commitTransaction();
    }

    public void deleteSearchUser(long l)
    {
        RealmResults realmresults = realm.where(SearchUserHistoryCacheModel.class).equalTo("memberNo", l).findAll();
        realm.beginTransaction();
        realmresults.clear();
        realm.commitTransaction();
    }

    public void deleteSearchUserAll()
    {
        RealmResults realmresults = realm.where(SearchUserHistoryCacheModel.class).findAll();
        realm.beginTransaction();
        realmresults.clear();
        realm.commitTransaction();
    }

    public void insertHomeTagList(String s, String s1)
    {
        realm.beginTransaction();
        HomeTagListCacheModel hometaglistcachemodel = (HomeTagListCacheModel)realm.createObject(HomeTagListCacheModel.class);
        hometaglistcachemodel.setId(s);
        hometaglistcachemodel.setTagList(s1);
        realm.commitTransaction();
    }

    public void insertSearchPic(String s)
    {
        realm.beginTransaction();
        SearchPicHistoryCacheModel searchpichistorycachemodel = (SearchPicHistoryCacheModel)realm.createObject(SearchPicHistoryCacheModel.class);
        searchpichistorycachemodel.setTag(s);
        searchpichistorycachemodel.setUpdateYmdt(new Date());
        realm.commitTransaction();
    }

    public void insertSearchTag(String s)
    {
        realm.beginTransaction();
        SearchTagHistoryCacheModel searchtaghistorycachemodel = (SearchTagHistoryCacheModel)realm.createObject(SearchTagHistoryCacheModel.class);
        searchtaghistorycachemodel.setTag(s);
        searchtaghistorycachemodel.setUpdateYmdt(new Date());
        realm.commitTransaction();
    }

    public void insertSearchUser(long l, String s, String s1, String s2)
    {
        realm.beginTransaction();
        SearchUserHistoryCacheModel searchuserhistorycachemodel = (SearchUserHistoryCacheModel)realm.createObject(SearchUserHistoryCacheModel.class);
        searchuserhistorycachemodel.setMemberNo(l);
        searchuserhistorycachemodel.setIconUrl(s);
        searchuserhistorycachemodel.setName(s1);
        searchuserhistorycachemodel.setTag(s2);
        searchuserhistorycachemodel.setUpdateYmdt(new Date());
        realm.commitTransaction();
    }

    public void insertTagHistory(String s, String s1)
    {
        realm.beginTransaction();
        TagHistoryCacheModel taghistorycachemodel = (TagHistoryCacheModel)realm.createObject(TagHistoryCacheModel.class);
        taghistorycachemodel.setId(s);
        taghistorycachemodel.setTagHistory(s1);
        realm.commitTransaction();
    }

    public void printSearchPic()
    {
        SearchPicHistoryCacheModel searchpichistorycachemodel;
        for (Iterator iterator = realm.where(SearchPicHistoryCacheModel.class).findAll().iterator(); iterator.hasNext();)
        {
            searchpichistorycachemodel = (SearchPicHistoryCacheModel)iterator.next();
        }

    }

    public void printSearchTag()
    {
        SearchTagHistoryCacheModel searchtaghistorycachemodel;
        for (Iterator iterator = realm.where(SearchTagHistoryCacheModel.class).findAll().iterator(); iterator.hasNext();)
        {
            searchtaghistorycachemodel = (SearchTagHistoryCacheModel)iterator.next();
        }

    }

    public void printSearchUser()
    {
        SearchUserHistoryCacheModel searchuserhistorycachemodel;
        for (Iterator iterator = realm.where(SearchUserHistoryCacheModel.class).findAll().iterator(); iterator.hasNext();)
        {
            searchuserhistorycachemodel = (SearchUserHistoryCacheModel)iterator.next();
        }

    }

    public HomeTagListCacheModel selectHomeTagList(String s)
    {
        RealmQuery realmquery = realm.where(HomeTagListCacheModel.class);
        realmquery.equalTo("id", s);
        return (HomeTagListCacheModel)realmquery.findFirst();
    }

    public int selectSearchPicCount()
    {
        return realm.where(SearchPicHistoryCacheModel.class).findAll().size();
    }

    public RealmResults selectSearchPicList()
    {
        RealmResults realmresults = realm.where(SearchPicHistoryCacheModel.class).findAll();
        realmresults.sort("updateYmdt", false);
        return realmresults;
    }

    public int selectSearchTagCount()
    {
        return realm.where(SearchTagHistoryCacheModel.class).findAll().size();
    }

    public RealmResults selectSearchTagList()
    {
        RealmResults realmresults = realm.where(SearchTagHistoryCacheModel.class).findAll();
        realmresults.sort("updateYmdt", false);
        return realmresults;
    }

    public int selectSearchUserCount()
    {
        return realm.where(SearchUserHistoryCacheModel.class).findAll().size();
    }

    public RealmResults selectSearchUserList()
    {
        RealmResults realmresults = realm.where(SearchUserHistoryCacheModel.class).findAll();
        realmresults.sort("updateYmdt", false);
        return realmresults;
    }

    public TagHistoryCacheModel selectTagHistory(String s)
    {
        RealmQuery realmquery = realm.where(TagHistoryCacheModel.class);
        realmquery.equalTo("id", s);
        return (TagHistoryCacheModel)realmquery.findFirst();
    }

    public void showHomeTagList()
    {
        if (realm != null)
        {
            HomeTagListCacheModel hometaglistcachemodel;
            for (Iterator iterator = realm.where(HomeTagListCacheModel.class).findAll().iterator(); iterator.hasNext();)
            {
                hometaglistcachemodel = (HomeTagListCacheModel)iterator.next();
            }

        }
    }

    public void updateHomeTagList(String s, String s1)
    {
        HomeTagListCacheModel realmresults = (HomeTagListCacheModel)realm.where(HomeTagListCacheModel.class).equalTo("id", s).findFirst();
        realm.beginTransaction();
        realmresults.setTagList(s1);
        realm.commitTransaction();
    }

    public void updateSearchPic(String s)
    {
        SearchPicHistoryCacheModel realmresults = (SearchPicHistoryCacheModel)realm.where(SearchPicHistoryCacheModel.class).equalTo("tag", s).findFirst();
        realm.beginTransaction();
        realmresults.setUpdateYmdt(new Date());
        realm.commitTransaction();
    }

    public void updateSearchTag(String s)
    {
        SearchTagHistoryCacheModel realmresults = (SearchTagHistoryCacheModel)realm.where(SearchTagHistoryCacheModel.class).equalTo("tag", s).findFirst();
        realm.beginTransaction();
        realmresults.setUpdateYmdt(new Date());
        realm.commitTransaction();
    }

    public void updateSearchUser(long l)
    {
        SearchUserHistoryCacheModel searchuserhistorycachemodel = (SearchUserHistoryCacheModel)realm.where(SearchUserHistoryCacheModel.class).equalTo("memberNo", l).findFirst();
        realm.beginTransaction();
        searchuserhistorycachemodel.setUpdateYmdt(new Date());
        realm.commitTransaction();
    }

    public void updateTagHistory(String s, String s1)
    {
        TagHistoryCacheModel tagHistoryCacheModel = (TagHistoryCacheModel)realm.where(TagHistoryCacheModel.class).equalTo("id", s).findFirst();
        realm.beginTransaction();
        tagHistoryCacheModel.setTagHistory(s1);
        realm.commitTransaction();
    }
}
