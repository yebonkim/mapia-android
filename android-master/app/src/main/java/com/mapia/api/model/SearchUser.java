package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 6..
 */

import android.nfc.Tag;

import java.io.Serializable;
import java.util.List;


public class SearchUser implements Serializable
{
    private String imageUrl;
    private long memberNo;
    private String nickname;
    private List<Tag> tags;

    public String getImageUrl() {
        return this.imageUrl;
    }

    public long getMemberNo() {
        return this.memberNo;
    }

    public String getNickname() {
        return this.nickname;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMemberNo(final long memberNo) {
        this.memberNo = memberNo;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void setTags(final List<Tag> tags) {
        this.tags = tags;
    }
}