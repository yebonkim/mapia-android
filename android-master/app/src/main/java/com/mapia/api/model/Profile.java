package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.nfc.Tag;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable
{
    private String blockYn;
    private String followingYn;
    private String imageUrl;
    private String introduceDesc;
    private long memberNo;
    private String neoIdNo;
    private String nickname;
    private List<Tag> tags;

    public String getBlockYn() {
        return this.blockYn;
    }

    public String getFollowingYn() {
        return this.followingYn;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getIntroduceDesc() {
        return this.introduceDesc;
    }

    public long getMemberNo() {
        return this.memberNo;
    }

    public String getNeoIdNo() {
        return this.neoIdNo;
    }

    public String getNickname() {
        return this.nickname;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setBlockYn(final String blockYn) {
        this.blockYn = blockYn;
    }

    public void setFollowingYn(final String followingYn) {
        this.followingYn = followingYn;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIntroduceDesc(final String introduceDesc) {
        this.introduceDesc = introduceDesc;
    }

    public void setMemberNo(final long memberNo) {
        this.memberNo = memberNo;
    }

    public void setNeoIdNo(final String neoIdNo) {
        this.neoIdNo = neoIdNo;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void setTags(final List<Tag> tags) {
        this.tags = tags;
    }
}