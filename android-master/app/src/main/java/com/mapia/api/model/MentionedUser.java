package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.io.Serializable;


public class MentionedUser implements Serializable
{
    private long memberNo;
    private String nickname;

    public long getMemberNo() {
        return this.memberNo;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setMemberNo(final long memberNo) {
        this.memberNo = memberNo;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }
}