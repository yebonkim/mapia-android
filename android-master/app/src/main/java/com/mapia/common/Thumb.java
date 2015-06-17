package com.mapia.common;

/**
 * Created by daehyun on 15. 6. 8..
 */

public enum Thumb
{
    END_BODY_11("f480_480", "f720_720"),
    END_BODY_34("f480_640", "f720_960"),
    END_BODY_43("f480_360", "f720_540"),
    END_BODY_ICON("fn59_59", "fn78_78"),
    END_REPIC("fn60_60", "fn80_80"),
    FOLLOW_ICON("fn59_59", "fn78_78"),
    HOME_BANNER("f480_160", "f720_240"),
    HOME_HOT_MONO("f480_160m", "f720_240m"),
    HOME_TAG_BIG("fnf320_480", "fnf480_720"),
    HOME_TAG_SMALL("fnf160_240", "fnf240_360"),
    HOT_PIC_GRID_11("f159_159", "f238_238"),
    HOT_PIC_GRID_34("f159_211", "f238_317"),
    HOT_PIC_GRID_43("f159_119", "f238_179"),
    HOT_USER_LIST_BIG("f160_160", "f240_240"),
    HOT_USER_LIST_ICON("fn120_120", "fn160_160"),
    HOT_USER_LIST_SMALL("f90_90", "f119_119"),
    LOCATION_GALLERY_GRID_11("f159_159", "f238_238"),
    LOCATION_GALLERY_GRID_34("f159_211", "f238_317"),
    LOCATION_GALLERY_GRID_43("f159_119", "f238_179"),
    LOCATION_GALLERY_HEAD("f480_249_b30", "f720_374_b30"),
    MYFEED_GRID_11("f159_159", "f238_238"),
    MYFEED_GRID_34("f159_211", "f238_317"),
    MYFEED_GRID_43("f159_119", "f238_179"),
    MYFEED_ICON("fn59_59", "fn78_78"),
    MYFEED_LIST_11("f480_480", "f720_720"),
    MYFEED_LIST_34("f480_640", "f720_960"),
    MYFEED_LIST_43("f480_360", "f720_540"),
    MYFEED_TAG("fn480_200", "fn720_300"),
    MYFEED_TAG_MONO("fn480_200m", "fn720_300m"),
    NOTI_ROW_ICON("fn59_59", "fn78_78"),
    NOTI_ROW_PIC("f89_89", "f118_118"),
    PROFILE_EDIT_ICON("fn465_465", "fn620_620"),
    PROFILE_GRID_11("f159_159", "f238_238"),
    PROFILE_GRID_34("f159_211", "f238_317"),
    PROFILE_GRID_43("f159_119", "f238_179"),
    PROFILE_HEAD("f480_249_b30", "f720_586_b30"),
    PROFILE_ICON("fn120_120", "fn160_160"),
    PROFILE_LIST_11("f480_480", "f720_720"),
    PROFILE_LIST_34("f480_640", "f720_960"),
    PROFILE_LIST_43("f480_360", "f720_540"),
    PROFILE_ROW_ICON("fn59_59", "fn78_78"),
    PROFILE_TAG("fn480_200", "fn720_300"),
    RELATED_CONT_THUMB_BIG("f160_160", "f240_240"),
    RELATED_CONT_THUMB_SMALL("f90_90", "f119_119"),
    SEARCH_ROW_ICON("fn59_59", "fn78_78"),
    SETTING_BLOCK_USER_ICON("fn59_59", "fn78_78"),
    SETTING_EDIT_ICON("fn225_225", "fn300_300"),
    SETTING_ROW_ICON("fn59_59", "fn78_78"),
    SNS_SHARING_PROFILE("f200_200_watermark", "f200_200_watermark"),
    TAG_GALLERY_GRID_11("f159_159", "f238_238"),
    TAG_GALLERY_GRID_34("f159_211", "f238_317"),
    TAG_GALLERY_GRID_43("f159_119", "f238_179"),
    TAG_GALLERY_HEAD("f480_323_b30", "f720_484_b30"),
    TAG_GALLERY_HEAD_ICON("fn59_59", "fn78_78"),
    VIEWER_11("f720_720", "f1080_1080"),
    VIEWER_34("f720_960", "f1080_1440"),
    VIEWER_43("f720_540", "f1080_810");

    private String for480;
    private String for720;

    private Thumb(final String for480, final String for2) {
        this.for480 = for480;
        this.for720 = for2;
    }

    public String for480() {
        return this.for480;
    }

    public String for720() {
        return this.for720;
    }
}