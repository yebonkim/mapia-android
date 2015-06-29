// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.mapia.common;

import android.graphics.Color;
import android.os.Build;

import com.mapia.util.BitmapUtils;
import com.mapia.util.DeviceUtils;

public class CommonConstants
{

    public static final String ACTIVITY_FROM_KEY = "cameraFrom";
    public static final int ANIMATION_DURATION = 300;
    public static final String APP_VERSION = "1.0.01";
    public static final int AUTO_SCROLL_LISTVIEW_PERIOD_TIME = 2000;
    public static final int AUTO_SCROLL_LISTVIEW_SCROLL_TIME = 500;
    public static final int AUTO_SCROLL_LISTVIEW_TOTAL_TIME = 0x30d40;
    public static final int AUTO_SCROLL_LISTVIEW_WHERE_LOCATION_GALLERY = 2;
    public static final int AUTO_SCROLL_LISTVIEW_WHERE_PROFILE = 0;
    public static final int AUTO_SCROLL_LISTVIEW_WHERE_TAG_GALLERY = 1;
    public static final int BUTTON_REPEAT_CLICK_DEFENCE_PERIOD = 700;
    public static final String CAMERA_SAVE_ORIGINAL_PHOTO_YN_NAME = "saveOriginalPhotoYn";
    public static final String CAMERA_SAVE_mapia_PHOTO_YN_NAME = "savemapiaPhotoYn";
    public static final String CAMERA_SAVE_mapia_VIDEO_YN_NAME = "savemapiaVideoYn";
    public static final String CHOSEN_FILE_PATH_KEY = "chosenFile";
    public static final int POST_PIC = 11;
    public static final int POST_PIC_SUC = 111;
//    public static final int CROP_ICON_GUIDE_RADIUS = BitmapUtils.convertDipToPixelInt(170F);
    public static final int END = 0;
    public static final String EVENT_ACTIVITY_READ = "ear";
    public static final String EVENT_BGUPLOAD = "ebu";
    public static final String EVENT_BLOCK_MEMBER = "ebm";
    public static final String EVENT_CHANGE_COMMENT = "eccy";
    public static final String EVENT_CHANGE_FOLLOWING_TAG = "ecft";
    public static final String EVENT_CHANGE_FOLLOWING_USER = "ecfu";
    public static final String EVENT_CHANGE_FOLLOW_ME = "ecfm";
    public static final String EVENT_CHANGE_ICON = "eci";
    public static final String EVENT_CHANGE_LIKE = "ecl";
    public static final String EVENT_CHANGE_MESSAGE = "ecm";
    public static final String EVENT_CHANGE_NICKNAME = "ecn";
    public static final String EVENT_CHANGE_REPIC = "ecrc";
    public static final String EVENT_COMMENT_OP_ADDED = "ecpoa";
    public static final String EVENT_COMMENT_OP_REMOVED = "ecpor";
    public static final String EVENT_DELETE_POST = "edp";
    public static final String EVENT_HIDE_POST = "ehp";
    public static final String EVENT_MODIFY_POST = "emp";
    public static final int EVENT_NEW_POST = 100;
    public static final String EVENT_REMOVE_PENALTY = "erp";
    public static final String EVENT_REPIC_ADDED = "era";
    public static final String EVENT_REPIC_HIDE = "erh";
    public static final String EVENT_REPIC_REMOVED = "err";
    public static final String EVENT_SETTING_DATASAVE = "esd";
    public static final String EVENT_UNBLOCK_MEMBER = "eum";
    public static final String EXTRA_SNS_SHARING_URL = "extra_sns_sharing_url";
    public static final int FACEBOOK_LOGIN_REQUEST_CODE = 64206;
    public static final String FACEBOOK_PERMISSION_PUBLISH = "publish_actions";
    public static final String FILE_NAME_PREFIX = "mapia_";
    public static final String FILTER_TARGET_PATH_KEY = "filterTargetPath";
    public static final String FONT_NANUM_BOLD = "fonts/NanumBarunGothicOTFBold.otf";
    public static final String FONT_NANUM_REGULAR = "fonts/NanumBarunGothicOTF.otf";
    public static final int FROM_APPLY_FILTER_ACTIVITY = 2;
    public static final int FROM_CAMERA_ACTIVITY = 0;
    public static final int FROM_PHOTO_RATIO_ACTIVITY = 1;
    public static final int FROM_VIDEO_RATIO_ACTIVITY = 3;
    public static final String GALLERY_SHARE_EXTRA_STEAM_KEY = "galleryShareExtraSteam";
    public static final String GOOGLE_PLAY_DETAIL_LINK = "market://details?id=com.naver.android.mapia";
    public static final int HEADER_187_EXPANDED_HEIGHT = BitmapUtils.convertDipToPixelInt(187F);
    public static final int HEADER_187_FOLDED_HEIGHT = BitmapUtils.convertDipToPixelInt(53F);
    public static final int HEADER_242_EXPANDED_HEIGHT = BitmapUtils.convertDipToPixelInt(242F);
    public static final int HEADER_242_FOLDED_HEIGHT = BitmapUtils.convertDipToPixelInt(53F);
    public static final int HEADER_293_EXPANDED_HEIGHT = BitmapUtils.convertDipToPixelInt(293F);
    public static final int HEADER_293_FOLDED_HEIGHT = BitmapUtils.convertDipToPixelInt(53F);
    public static final int HOTPIC = 1;
    public static final int HOTUSER = 9;
    public static final String HOTUSER_TAGS_TEXTVIEW_WIDTH = "hotuserTagsTextviewWidth";
    public static final int HOT_PICS_SHOW_COUNT = 400;
    public static final String INTENT_TEXT_BODY_KEY = "intentTextBody";
    public static final double INVALIDE_LOCATION = 200D;
    public static final String LINK_BLIND_EXPLANATION = "https://inoti.naver.com/inoti/service.nhn?m=honorRepostOnline";
    public static final String LINK_SETTING_INFO_HELP = "https://m.help.naver.com/support/service/main.nhn?serviceNo=11064";
    public static final String LINK_SETTING_OPINION = "https://m.help.naver.com/support/inquiry/input.nhn?categoryNo=6113&serviceNo=11064";
    public static final int LOCATION_GALLERY = 6;
    public static final int MAX_LIST_ITEM_SIZE = 0xf423f;
    public static final int MENU_ROOT_CAMERA = 9;
    public static final int MENU_ROOT_HOME = 0;
    public static final int MENU_ROOT_MYFEED = 1;
    public static final int MENU_ROOT_NOTI = 2;
    public static final int MENU_ROOT_POST = 5;
    public static final int MENU_ROOT_PROFILE = 3;
    public static final int MENU_ROOT_SEARCH = 4;
    public static final int MORE_LOADING_COUNT_GRID_DATA_SAVE = 15;
    public static final int MORE_LOADING_COUNT_GRID_NO_DATA_SAVE = 24;
    public static final int MORE_LOADING_COUNT_LIST_DATA_SAVE = 20;
    public static final int MORE_LOADING_COUNT_LIST_NO_DATA_SAVE = 30;
    public static final int MORE_LOADING_COUNT_MYFEED_TAG = 100;
    public static final int MORE_LOADING_COUNT_PROFILE_TAG = 100;
    public static final int MYFEED = 2;
    public static final int MYFEED_LIST_REFRESH_LIST_DELAY = 700;
    public static final boolean NELO_DEBUG_MODE = false;
    public static final boolean NELO_LOGCAT_EVENT = false;
    public static final boolean NELO_LOGCAT_MAIN = false;
    public static final boolean NELO_LOGCAT_RADIO = false;
    public static final String NELO_PROJECT_ID = "mapia_app_android";
    public static final String NELO_PROJECT_VER = "1.0.01";
    public static final String NELO_SERVER_DOMAIN = "nelo2-col.nhncorp.com";
    public static final int NELO_SERVER_PORT = 10006;
    public static final boolean NELO_USE_FLAG = true;
    public static final int NOTI = 11;
    public static final int NOTI_ALL = 0;
    public static final int NOTI_FOLLOW = 2;
    public static final int NOTI_MY = 1;
    public static final int NPUSH_READABLE_VERSION = 2;
    public static final String OLD_CACHE_CLEAN_YN_NAME = "oldCacheCleanYn";
    public static final int ORDER_BY_LIKE = 1;
    public static final int ORDER_BY_TIME = 0;
    public static final String ORIGINAL_FILE_KEY = "originalFile";
    public static final int POST = 12;
    public static final String POSTING_MODE_MODIFY = "modify";
    public static final String POSTING_MODE_POST_IMAGE = "post_image";
    public static final String POSTING_MODE_POST_VIDEO = "post_video";
    public static final String POSTING_MODE_REPIC = "repic";
    public static final String PREF_ALLOW_SEARCH = "allow_search";
    public static final int PROFILE = 7;
    public static final int PROFILE_FOLLOWER = 0;
    public static final int PROFILE_FOLLOWING = 1;
    public static final int PROFILE_LIKE = 5;


    public static final int PLACE_PICKER_REQUEST = 10;





    public static final int PULL_TO_REFRESH_DELAY = 1000;
    public static final String PUSH_APP_KEY_PREFERENCE_NAME = "pushAppKey";
    public static final String PUSH_APP_VERSION = "1.0.01";
    public static final String PUSH_DEVICE_TYPE = "nni";
    public static final String PUSH_ETIQUETTE_END_TIME_NAME = "pushEtiquetteEndTime";
    public static final String PUSH_ETIQUETTE_START_TIME_NAME = "pushEtiquetteStartTime";
    public static final String PUSH_ETIQUETTE_YN_NAME = "pushEtiquetteYn";
    public static final String PUSH_MANUFACTURER;
    public static final String PUSH_MODEL;
    public static final String PUSH_NNI_SERVICE_NAME = "mapia";
    public static final String PUSH_NOTICE_COMMENT_YN_NAME = "pushNoticeCommentYn";
    public static final String PUSH_NOTICE_FOLLOWER_YN_NAME = "pushNoticeFollowerYn";
    public static final String PUSH_NOTICE_FOLLOWING_YN_NAME = "pushNoticeFollwingYn";
    public static final String PUSH_NOTICE_LIKE_YN_NAME = "pushNoticeLikeYn";
    public static final String PUSH_NOTICE_MENTION_YN_NAME = "pushNoticeMentionYn";
    public static final String PUSH_NOTICE_REPIC_YN_NAME = "pushNoticeRepicYn";
    public static final String PUSH_NPUSH_APP_ID = "APG00213";
    public static final String PUSH_NPUSH_REGISTRATION_ID_NAME = "pushRegistrationId";
    public static final String PUSH_OS;
    public static final String PUSH_RECEIVE_YN_NAME = "pushReceiveYn";
    public static final String PUSH_SOUND_YN_NAME = "pushSoundYn";
    public static final String PUSH_VENDOR;
    public static final String PUSH_VIBRATE_YN_NAME = "pushVibrateYn";
    public static final String RATIO_1_1 = "1:1";
    public static final String RATIO_3_4 = "3:4";
    public static final String RATIO_4_3 = "4:3";
    public static final String REGEX_INVALID_NICK = "[^a-zA-Z0-9\uAC00-\uD7A3\u3131-\u314E\u314F-\u3163\341\351\355\363\372\374\361\301\311\315\323\332\334\321\344\366\337\304\326\337\u3041-\u3093\u30FC\u30A1-\u30F4\u30FC\u4E00-\u9FA0_]";
    public static final String REGEX_MYFEED_BODY = "(#[a-zA-Z0-9\uAC00-\uD7A3\u3131-\u314E\u314F-\u3163\341\351\355\363\372\374\361\301\311\315\323\332\334\321\344\366\337\304\326\337\u3041-\u3093\u30FC\u30A1-\u30F4\u30FC\u4E00-\u9FA0_]{1,20})|(@[a-zA-Z0-9\uAC00-\uD7A3\u3131-\u314E\u314F-\u3163\341\351\355\363\372\374\361\301\311\315\323\332\334\321\344\366\337\304\326\337\u3041-\u3093\u30FC\u30A1-\u30F4\u30FC\u4E00-\u9FA0_]{1,15})|(... \uB354 \uBCF4\uAE30)";
    public static final String REGEX_NICKNAME_CHECK = "@[a-zA-Z0-9\uAC00-\uD7A3\u3131-\u314E\u314F-\u3163\341\351\355\363\372\374\361\301\311\315\323\332\334\321\344\366\337\304\326\337\u3041-\u3093\u30FC\u30A1-\u30F4\u30FC\u4E00-\u9FA0_]{0,15}";
    public static final String REGEX_NICKNAME_CHECK_NO_AT = "@[a-zA-Z0-9\uAC00-\uD7A3\u3131-\u314E\u314F-\u3163\341\351\355\363\372\374\361\301\311\315\323\332\334\321\344\366\337\304\326\337\u3041-\u3093\u30FC\u30A1-\u30F4\u30FC\u4E00-\u9FA0_]{1,15}";
    public static final String REGEX_TAG = "#[a-zA-Z0-9\uAC00-\uD7A3\u3131-\u314E\u314F-\u3163\341\351\355\363\372\374\361\301\311\315\323\332\334\321\344\366\337\304\326\337\u3041-\u3093\u30FC\u30A1-\u30F4\u30FC\u4E00-\u9FA0_]{0,20}";
    public static final String REGEX_TAG_NO_SHARP = "#[a-zA-Z0-9\uAC00-\uD7A3\u3131-\u314E\u314F-\u3163\341\351\355\363\372\374\361\301\311\315\323\332\334\321\344\366\337\304\326\337\u3041-\u3093\u30FC\u30A1-\u30F4\u30FC\u4E00-\u9FA0_]{1,20}";
    public static final String REGEX_URL = "((https?:\\/\\/)|(www\\.)|(m\\.))([-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\.,%#]+)|\\w+\\.blog\\.me\\/*[\\w\\/]*";
    public static final int REPIC_GALLERY = 8;
    public static final int RESOLUTION_480 = 4;
    public static final int RESOLUTION_720 = 7;
    public static final String ROOT_DIRECTORY_PATH = "mapia";
    public static final int SCHEME_READABLE_VERSION = 2;
    public static final int SEARCH_AUTO_COMPLETE_MAX_SHOW_COUNT = 100;
    public static final int SEARCH_PIC = 1;
    public static final int SEARCH_PICTURE = 10;
    public static final int SEARCH_TAG = 0;
    public static final int SEARCH_USER = 2;
    public static final int SEARVER_API_TIMEOUT = 5000;
    public static final String SETTING_DATA_SAVE_YN_NAME = "dataSaveYn";
    public static final String SETTING_ICON_EXTENSION = "jpg";
    public static final String SHARED_PREFERENCES_SCHEME = "mapia_shared_preferences";
    public static final String SNS_CHANNEL_FACEBOOK = "FACEBOOK";
    public static final String SNS_CHANNEL_LINE = "LINE";
    public static final String SNS_CHANNEL_TWITTER = "TWITTER";
    public static final int SWIPE_COLOR_SCHEME[] = {
        Color.parseColor("#474cfa"), Color.parseColor("#5853EB"), Color.parseColor("#7C77FF"), Color.parseColor("#A09BFF")
    };
    public static final int SWIPE_END_POINT_HOME = BitmapUtils.convertDipToPixelInt(10F);
    public static final int SWIPE_END_POINT_HOT_PIC = BitmapUtils.convertDipToPixelInt(10F);
    public static final int SWIPE_END_POINT_HOT_USER = BitmapUtils.convertDipToPixelInt(10F);
    public static final int SWIPE_END_POINT_LOCATIONGALLERY = BitmapUtils.convertDipToPixelInt(145F);
    public static final int SWIPE_END_POINT_MYFEED_ONE_COLUMN = BitmapUtils.convertDipToPixelInt(65F);
    public static final int SWIPE_END_POINT_MYFEED_THREE_COLUMN = BitmapUtils.convertDipToPixelInt(65F);
    public static final int SWIPE_END_POINT_NOTI = BitmapUtils.convertDipToPixelInt(10F);
    public static final int SWIPE_END_POINT_PROFILE = BitmapUtils.convertDipToPixelInt(255F);
    public static final int SWIPE_END_POINT_TAGGALLERY = BitmapUtils.convertDipToPixelInt(200F);
    public static final int SWIPE_START_POINT_HOME = BitmapUtils.convertDipToPixelInt(-50F);
    public static final int SWIPE_START_POINT_HOT_PIC = BitmapUtils.convertDipToPixelInt(-50F);
    public static final int SWIPE_START_POINT_HOT_USER = BitmapUtils.convertDipToPixelInt(-50F);
    public static final int SWIPE_START_POINT_LOCATIONGALLERY = BitmapUtils.convertDipToPixelInt(85F);
    public static final int SWIPE_START_POINT_MYFEED_ONE_COLUMN = BitmapUtils.convertDipToPixelInt(5F);
    public static final int SWIPE_START_POINT_MYFEED_THREE_COLUMN = BitmapUtils.convertDipToPixelInt(5F);
    public static final int SWIPE_START_POINT_NOTI = BitmapUtils.convertDipToPixelInt(-50F);
    public static final int SWIPE_START_POINT_PROFILE = BitmapUtils.convertDipToPixelInt(185F);
    public static final int SWIPE_START_POINT_TAGGALLERY = BitmapUtils.convertDipToPixelInt(140F);
    public static final int SWIPE_TRIGGER_DISTANCE = BitmapUtils.convertDipToPixelInt(60F);
    public static final int TAG_ALBUM = 3;
    public static final int TAG_GALLERY = 4;
    public static final int TAG_LISTVIEW_HEIGHT = BitmapUtils.convertDipToPixelInt(30F);
    public static final int THREE_COLUMN_THUMB_WIDTH = (DeviceUtils.getDeviceWidth() - BitmapUtils.convertDipToPixelInt(3F)) / 3;
    public static final int THUMB_RATIO_11 = 0;
    public static final int THUMB_RATIO_34 = 2;
    public static final int THUMB_RATIO_43 = 1;
    public static final String TWITTER_KEY = "aJXIfyanReibRdBY4af8UJ5iL";
    public static final int TWITTER_LOGIN_ERROR = 2;
    public static final int TWITTER_LOGIN_REQUEST_CODE = 100;
    public static final String TWITTER_SECRET = "XTUpRE3I9Tj1lsGtqxPFnpp7Za63HNmLlweGoXEkEVpbtxlZAB";
    public static final String URL_HOST_GO_CAMERA = "goCamera";
    public static final String URL_HOST_GO_HOME = "goHome";
    public static final String URL_HOST_GO_HOTPIC = "goHotPic";
    public static final String URL_HOST_GO_HOTUSER = "goHotUser";
    public static final String URL_HOST_GO_LOCATION_GALLERY = "goLocationGallery";
    public static final String URL_HOST_GO_NOTICE = "goNotice";
    public static final String URL_HOST_GO_PIC = "goPic";
    public static final String URL_HOST_GO_PROFILE = "goProfile";
    public static final String URL_HOST_GO_REPIC_GALLERY = "goRepicGallery";
    public static final String URL_HOST_GO_TAG_GALLERY = "goTagGallery";
    public static final String URL_HOST_GO_USER_TAG_ALBUM = "goUserTagAlbum";
    public static final String URL_PARAM_LATITUDE = "latitude";
    public static final String URL_PARAM_LOCATION_CODE = "locationCode";
    public static final String URL_PARAM_LOCATION_NAME = "locationName";
    public static final String URL_PARAM_LONGITUDE = "longitude";
    public static final String URL_PARAM_MEMBER_NO = "memberNo";
    public static final String URL_PARAM_PIC_NO = "picNo";
    public static final String URL_PARAM_TAG = "tag";
    public static final String URL_PARAM_VERSION = "version";
    public static final String URL_PROTOCOL_HTTP = "http";
    public static final String URL_PROTOCOL_mapia = "mapia";
    public static final String URL_REPORT = "https://inoti.naver.com/inoti/service.nhn?m=honorRequestOnline";
    public static final String USER_ICON_IMAGE_PREFERENCE_NAME = "userIconImage";
    public static final int VIDEO_AUTO_PLAY_ALWAYS = 0;
    public static final String VIDEO_AUTO_PLAY_KEY = "videoAutoPlayKey";
    public static final int VIDEO_AUTO_PLAY_OFF = 2;
    public static final int VIDEO_AUTO_PLAY_WIFI = 1;
    public static final String VIDEO_PATH_KEY = "videoPath";
    public static final String VIDEO_RATIO_KEY = "videoRatio";
    public static final String WEBVIEW_URL_REPORT = "https://m.help.naver.com/support/reportCenter.nhn";

    public CommonConstants()
    {
    }

    static 
    {
        PUSH_MANUFACTURER = Build.MANUFACTURER;
        PUSH_MODEL = Build.MODEL;
        PUSH_OS = (new StringBuilder()).append("").append(android.os.Build.VERSION.SDK_INT).toString();
        PUSH_VENDOR = Build.BRAND;
    }
}
