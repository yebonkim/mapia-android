package com.mapia.post;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;

import com.mapia.cache.realmcache.RealmManager;
import com.mapia.cache.realmcache.TagHistoryCacheModel;

import java.util.ArrayList;


public class TagHistory
{
    private final int MAX_TAG;
    private final int MAX_TAG_LEN;
    private final String TAG_HISTORY_DB_ID;
    private Context mContext;
    private RealmManager mRealmManager;
    private ArrayList<String> mTagList;

    public TagHistory(final Context mContext) {
        super();
        this.MAX_TAG = 10;
        this.MAX_TAG_LEN = 20;
        this.TAG_HISTORY_DB_ID = "0";
        this.mContext = mContext;
    }

    private String convertTagListToString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.mTagList.size(); ++i) {
            sb.append(this.mTagList.get(i));
        }
        return sb.toString();
    }

    private int findTag(final String s) {
        for (int i = 0; i < this.mTagList.size(); ++i) {
            if (this.mTagList.get(i).equalsIgnoreCase(s)) {
                return i;
            }
        }
        return -1;
    }

    private void loadTagHistory() {
        final String tagHistory = this.readTagHistory();
        if (tagHistory != null) {
            this.mTagList.clear();
            final String[] split = tagHistory.split("#");
            for (int i = 0; i < split.length; ++i) {
                if (split[i].length() != 0) {
                    this.mTagList.add("#" + split[i]);
                }
            }
        }
    }

    private String readTagHistory() {
        final TagHistoryCacheModel selectTagHistory = this.mRealmManager.selectTagHistory("0");
        if (selectTagHistory != null) {
            return selectTagHistory.getTagHistory();
        }
        return null;
    }

    public void addTag(final String s) {
        if (s.charAt(0) == '#' && s.length() > 21) {
            return;
        }
        final int tag = this.findTag(s);
        if (this.mTagList.size() >= 10) {
            if (tag == -1) {
                this.mTagList.remove(9);
                this.mTagList.add(0, s);
                return;
            }
            this.mTagList.remove(tag);
            this.mTagList.add(0, s);
        }
        else {
            if (tag == -1) {
                this.mTagList.add(0, s);
                return;
            }
            this.mTagList.remove(tag);
            this.mTagList.add(0, s);
        }
    }

    public void cleanTagHistory() {
        this.mRealmManager.cleanTagHistory();
    }

    public int getTagCount() {
        return this.mTagList.size();
    }

    public ArrayList<String> getTagList() {
        return this.mTagList;
    }

    public void init() {
        this.mTagList = new ArrayList<String>();
        this.mRealmManager = new RealmManager(this.mContext);
        this.loadTagHistory();
    }

    public void writeTagHistory() {
        final String convertTagListToString = this.convertTagListToString();
        if (this.mRealmManager.containsTagHistory("0")) {
            this.mRealmManager.updateTagHistory("0", convertTagListToString);
            return;
        }
        this.mRealmManager.insertTagHistory("0", convertTagListToString);
    }
}