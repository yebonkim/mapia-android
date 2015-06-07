package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 7..
 */
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

import java.io.Serializable;
import java.util.List;

public class Picker
        implements Serializable
{

    private long followerRegDate;
    private long followingRegDate;
    private String followingYn;
    private String imageUrl;
    private long memberNo;
    private String neoIdNo;
    private String nickname;
    private List tags;

    public Picker()
    {
    }

    public long getFollowerRegDate()
    {
        return followerRegDate;
    }

    public long getFollowingRegDate()
    {
        return followingRegDate;
    }

    public String getFollowingYn()
    {
        return followingYn;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public long getMemberNo()
    {
        return memberNo;
    }

    public String getNeoIdNo()
    {
        return neoIdNo;
    }

    public String getNickname()
    {
        return nickname;
    }

    public List getTags()
    {
        return tags;
    }

    public void setFollowerRegDate(long l)
    {
        followerRegDate = l;
    }

    public void setFollowingRegDate(long l)
    {
        followingRegDate = l;
    }

    public void setFollowingYn(String s)
    {
        followingYn = s;
    }

    public void setImageUrl(String s)
    {
        imageUrl = s;
    }

    public void setMemberNo(long l)
    {
        memberNo = l;
    }

    public void setNeoIdNo(String s)
    {
        neoIdNo = s;
    }

    public void setNickname(String s)
    {
        nickname = s;
    }

    public void setTags(List list)
    {
        tags = list;
    }
}
