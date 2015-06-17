package com.mapia.api.model;

/**
 * Created by daehyun on 15. 6. 8..
 */
public class Penalty
{
    public static final String CS_BLOCK_CONTENT = "CS_BLOCK_CONTENT";
    public static final String FORCE_BLIND_CONTENT = "FORCE_BLIND_CONTENT";
    public static final String FORCE_BLIND_MEMBER = "FORCE_BLIND_MEMBER";
    public static final String WARNING_MEMBER = "WARNING_MEMBER";
    public String blockReasonDetail;
    public long createDate;
    public boolean isCommentPenalty;
    public long memberNo;
    public String message;
    public String messageDetail;
    public String penaltyCode;
    public long penaltyNo;
    public String penaltyReason;
    public long picNo;
}