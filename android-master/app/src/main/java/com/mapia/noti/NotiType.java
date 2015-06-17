package com.mapia.noti;

import com.mapia.util.ResouceUtils;

/**
 * Created by daehyun on 15. 6. 8..
 */

public enum NotiType
{
    NOTI_FOLLOW_ME(2130903144, (String)null, ResouceUtils.getString(2131558696)),
    NOTI_FOLLOW_TAG(2130903143, ResouceUtils.getString(2131558697), ResouceUtils.getString(2131558698)),
    NOTI_FOLLOW_USER(2130903143, ResouceUtils.getString(2131558699), ResouceUtils.getString(2131558700)),
    NOTI_MENTION_BODY(2130903148, (String)null, ResouceUtils.getString(2131558701)),
    NOTI_MENTION_COMMENT(2130903148, (String)null, ResouceUtils.getString(2131558702)),
    NOTI_MENTION_PIC_COMMENT(2130903148, (String)null, ResouceUtils.getString(2131558703)),
    NOTI_MENTION_PIC_LIKE(2130903148, (String)null, ResouceUtils.getString(2131558704)),
    NOTI_MENTION_PIC_REPIC(2130903148, (String)null, ResouceUtils.getString(2131558705)),
    NOTI_PENALTY_FIRST(2130903145, (String)null, (String)null),
    NOTI_PENALTY_SECOND(2130903146, (String)null, (String)null),
    NOTI_PENALTY_UMON(2130903147, (String)null, (String)null),
    NOTI_PIC_COMMENT(2130903148, (String)null, ResouceUtils.getString(2131558706)),
    NOTI_PIC_DEFAULT(2130903148, (String)null, (String)null),
    NOTI_PIC_LIKE(2130903148, (String)null, ResouceUtils.getString(2131558707)),
    NOTI_PIC_REPIC(2130903148, (String)null, ResouceUtils.getString(2131558708));

    private int layoutResouceId;
    private String postposition;
    private String sentence;

    private NotiType(final int layoutResouceId, final String postposition, final String sentence) {
        this.layoutResouceId = layoutResouceId;
        this.postposition = postposition;
        this.sentence = sentence;
    }

    public int layoutResouceId() {
        return this.layoutResouceId;
    }

    public String postposition() {
        return this.postposition;
    }

    public String sentence() {
        return this.sentence;
    }
}