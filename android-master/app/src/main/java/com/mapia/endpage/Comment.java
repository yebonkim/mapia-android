package com.mapia.endpage;

/**
 * Created by daehyun on 15. 6. 13..
 */

import org.json.JSONObject;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment
{
    public long commentNo;
    public String contents;
    public long memberNo;
    public JSONObject mentions;
    public boolean mine;
    public String modTime;
    public String modTimeGmt;
    public String profileUserId;
    public String regTime;
    public String regTimeGmt;
    public String userName;
    public String userProfileImage;
}