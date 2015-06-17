package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import com.mapia.noti.NotiType;

import java.io.Serializable;

public class Activity implements Serializable
{
    private long activityId;
    private String activityType;
    private Profile activityUser;
    private long followingMemberNo;
    private String followingNickname;
    private boolean isPenalty;
    private NotiType notiType;
    private Penalty penalty;
    private SimplePic pic;
    private String readYn;
    private long regDate;
    private String tag;

    public Activity() {
        super();
        this.isPenalty = false;
    }

    public long getActivityId() {
        return this.activityId;
    }

    public String getActivityType() {
        return this.activityType;
    }

    public Profile getActivityUser() {
        return this.activityUser;
    }

    public long getFollowingMemberNo() {
        return this.followingMemberNo;
    }

    public String getFollowingNickname() {
        return this.followingNickname;
    }

    public boolean getIsPenalty() {
        return this.isPenalty;
    }

    public NotiType getNotiType() {
        return this.notiType;
    }

    public Penalty getPenalty() {
        return this.penalty;
    }

    public SimplePic getPic() {
        return this.pic;
    }

    public String getReadYn() {
        return this.readYn;
    }

    public long getRegDate() {
        return this.regDate;
    }

    public String getTag() {
        return this.tag;
    }

    public void setActivityId(final long activityId) {
        this.activityId = activityId;
    }

    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }

    public void setActivityUser(final Profile activityUser) {
        this.activityUser = activityUser;
    }

    public void setFollowingMemberNo(final long followingMemberNo) {
        this.followingMemberNo = followingMemberNo;
    }

    public void setFollowingNickname(final String followingNickname) {
        this.followingNickname = followingNickname;
    }

    public void setIsPenalty(final boolean isPenalty) {
        this.isPenalty = isPenalty;
    }

    public void setNotiType(final NotiType notiType) {
        this.notiType = notiType;
    }

    public void setPenalty(final Penalty penalty) {
        this.penalty = penalty;
    }

    public void setPic(final SimplePic pic) {
        this.pic = pic;
    }

    public void setReadYn(final String readYn) {
        this.readYn = readYn;
    }

    public void setRegDate(final long regDate) {
        this.regDate = regDate;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }
}
