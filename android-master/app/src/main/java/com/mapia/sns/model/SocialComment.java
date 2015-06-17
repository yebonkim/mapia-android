package com.mapia.sns.model;

/**
 * Created by daehyun on 15. 6. 12..
 */
public class SocialComment {
    public String id;
    public String createdTime;
    public String text;
    public SocialPerson from;
    public SocialComment(String id, String createdTime, String text, SocialPerson from){
        this.id = id;
        this.createdTime = createdTime;
        this.text = text;
        this.from = from;
    }
}
