package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import org.json.JSONArray;

import java.io.Serializable;

public class Repic implements Serializable
{
    public String body;
    public String copyrightYn;
    public String fileType;
    public int likeCount;
    public String likeYn;
    public JSONArray mentionedUsers;
    public long modifyDate;
    public String picImageColor;
    public int picImageHeight;
    public String picImageUrl;
    public int picImageWidth;
    public Long picNo;
    public String picVid;
    public Profile picker;
    public long regDate;
    public boolean withAnimationLikeButton;

    public Repic() {
        super();
        this.withAnimationLikeButton = false;
    }
}