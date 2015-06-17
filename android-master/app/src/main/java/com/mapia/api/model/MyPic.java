package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.io.Serializable;
import java.util.List;

public class MyPic implements Serializable
{
    private String body;
    private String copyrightYn;
    private Long feedId;
    private String fileType;
    private int likeCount;
    private String likeYn;
    private List<MentionedUser> mentionedUsers;
    private long modifyDate;
    private int page;
    private Penalty penalty;
    private String picImageColor;
    private int picImageHeight;
    private String picImageUrl;
    private int picImageWidth;
    private Long picNo;
    private String picVid;
    private long regDate;
    private int repicCount;
    private int replyCount;
    private int size;
    private List<String> tags;
    private boolean withLikeButtonAnimation;

    public MyPic() {
        super();
        this.withLikeButtonAnimation = false;
    }

    public String getBody() {
        return this.body;
    }

    public String getCopyrightYn() {
        return this.copyrightYn;
    }

    public Long getFeedId() {
        return this.feedId;
    }

    public String getFileType() {
        return this.fileType;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public String getLikeYn() {
        return this.likeYn;
    }

    public List<MentionedUser> getMentionedUser() {
        return this.mentionedUsers;
    }

    public long getModifyDate() {
        return this.modifyDate;
    }

    public int getPage() {
        return this.page;
    }

    public Penalty getPenalty() {
        return this.penalty;
    }

    public String getPicImageColor() {
        return this.picImageColor;
    }

    public int getPicImageHeight() {
        return this.picImageHeight;
    }

    public String getPicImageUrl() {
        return this.picImageUrl;
    }

    public int getPicImageWidth() {
        return this.picImageWidth;
    }

    public Long getPicNo() {
        return this.picNo;
    }

    public String getPicVid() {
        return this.picVid;
    }

    public long getRegDate() {
        return this.regDate;
    }

    public int getRepicCount() {
        return this.repicCount;
    }

    public int getReplyCount() {
        return this.replyCount;
    }

    public int getSize() {
        return this.size;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public boolean getWithLikeButtonAnimation() {
        return this.withLikeButtonAnimation;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public void setCopyrightYn(final String copyrightYn) {
        this.copyrightYn = copyrightYn;
    }

    public void setFeedId(final Long feedId) {
        this.feedId = feedId;
    }

    public void setFileType(final String fileType) {
        this.fileType = fileType;
    }

    public void setLikeCount(final int likeCount) {
        this.likeCount = likeCount;
    }

    public void setLikeYn(final String likeYn) {
        this.likeYn = likeYn;
    }

    public void setMentionedUser(final List<MentionedUser> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }

    public void setModifyDate(final long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    public void setPenalty(final Penalty penalty) {
        this.penalty = penalty;
    }

    public void setPicImageColor(final String picImageColor) {
        this.picImageColor = picImageColor;
    }

    public void setPicImageHeight(final int picImageHeight) {
        this.picImageHeight = picImageHeight;
    }

    public void setPicImageUrl(final String picImageUrl) {
        this.picImageUrl = picImageUrl;
    }

    public void setPicImageWidth(final int picImageWidth) {
        this.picImageWidth = picImageWidth;
    }

    public void setPicNo(final Long picNo) {
        this.picNo = picNo;
    }

    public void setPicVid(final String picVid) {
        this.picVid = picVid;
    }

    public void setRegDate(final long regDate) {
        this.regDate = regDate;
    }

    public void setRepicCount(final int repicCount) {
        this.repicCount = repicCount;
    }

    public void setReplyCount(final int replyCount) {
        this.replyCount = replyCount;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public void setWithLikeButtonAnimation(final boolean withLikeButtonAnimation) {
        this.withLikeButtonAnimation = withLikeButtonAnimation;
    }
}
