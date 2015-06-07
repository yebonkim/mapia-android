package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 7..
 */
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

import java.io.Serializable;

// Referenced classes of package com.naver.android.mapia.api.model:
//            Picker

public class SearchPic
        implements Serializable
{

    private String copyrightYn;
    private String fileType;
    private long modifyDate;
    private String picImageColor;
    private int picImageHeight;
    private String picImageUrl;
    private int picImageWidth;
    private Long picNo;
    private String picVid;
    private Picker picker;
    private long regDate;
    private int repicCount;

    public SearchPic()
    {
    }

    public String getCopyrightYn()
    {
        return copyrightYn;
    }

    public String getFileType()
    {
        return fileType;
    }

    public long getModifyDate()
    {
        return modifyDate;
    }

    public String getPicImageColor()
    {
        return picImageColor;
    }

    public int getPicImageHeight()
    {
        return picImageHeight;
    }

    public String getPicImageUrl()
    {
        return picImageUrl;
    }

    public int getPicImageWidth()
    {
        return picImageWidth;
    }

    public Long getPicNo()
    {
        return picNo;
    }

    public String getPicVid()
    {
        return picVid;
    }

    public Picker getPicker()
    {
        return picker;
    }

    public long getRegDate()
    {
        return regDate;
    }

    public int getRepicCount()
    {
        return repicCount;
    }

    public void setCopyrightYn(String s)
    {
        copyrightYn = s;
    }

    public void setFileType(String s)
    {
        fileType = s;
    }

    public void setModifyDate(long l)
    {
        modifyDate = l;
    }

    public void setPicImageColor(String s)
    {
        picImageColor = s;
    }

    public void setPicImageHeight(int i)
    {
        picImageHeight = i;
    }

    public void setPicImageUrl(String s)
    {
        picImageUrl = s;
    }

    public void setPicImageWidth(int i)
    {
        picImageWidth = i;
    }

    public void setPicNo(Long long1)
    {
        picNo = long1;
    }

    public void setPicVid(String s)
    {
        picVid = s;
    }

    public void setPicker(Picker picker1)
    {
        picker = picker1;
    }

    public void setRegDate(long l)
    {
        regDate = l;
    }

    public void setRepicCount(int i)
    {
        repicCount = i;
    }
}
