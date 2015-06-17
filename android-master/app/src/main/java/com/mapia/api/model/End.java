package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.mapia.post.LocationData;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

public class End implements Serializable, Parcelable
{
    public String body;
    public String copyrightYn;
    public String fileType;
    public int likeCount;
    public ArrayList<Profile> likeMembers;
    public String likeYn;
    public LocationData location;
    public JSONArray mentionedUsers;
    public long modifyDate;
    public SimplePic parentPic;
    public Penalty penalty;
    public String picImageColor;
    public int picImageHeight;
    public String picImageUrl;
    public int picImageWidth;
    public long picNo;
    public String picVid;
    public Picker picker;
    public long regDate;
    public int repicCount;
    public int replyCount;
    public String vidoePlayUrl;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(final Parcel parcel, final int n) {
    }
}