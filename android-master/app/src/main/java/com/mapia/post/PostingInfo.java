package com.mapia.post;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.io.File;
import java.io.Serializable;

public class PostingInfo implements Serializable
{
    public static final int POSTING_MODE_MODIFY = 1;
    public static final int POSTING_MODE_POST = 0;
    public static final int POSTING_MODE_REPIC = 2;
    public static final int POSTING_MODE_TAGGALLERY = 3;
    public String body;
    public String copyrightYn;
    public File file;
    public boolean filterApplied;
//    public FilterType filterType;
    public LocationData locationData;
    public String mediaType;
    public long memberNo;
    public int mode;
    public String originalMediaPath;
    public long parentMemberNo;
    public long parentPicNo;
    public int picImageHeight;
    public String picImageUrl;
    public int picImageWidth;
    public long picNo;
    public String postingMediaPath;
    public String snsParam;
    public String tag;
    public String vid;
    public int vidHeight;
    public int vidWidth;
    public String videoThumbUrl;
}
