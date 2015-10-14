package com.mapia.api;

/**
 * Created by daehyun on 15. 6. 13..
 */

import android.net.Uri;

import com.mapia.common.CommonConstants;
import com.mapia.endpage.Comment;
import com.mapia.login.LoginInfo;
import com.mapia.npush.NpushUtils;
import com.mapia.post.LocationData;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class QueryManager
{
    public static final String ACE_HOST = "ace.naver.com";
    public static final String API_COMMON_PATH = "mapiaApp";
    public static final String API_COMMON_PATH_MAPIA = "mapia";
    public static final String API_KEY_BROWSER = "AIzaSyADf-WZGHFaWz83_R9ByjL1yj6BZx02Ego";
    public static final String API_PATH_LIKE = "likeit";
    public static final String API_URL_ALLOW_SEARCH = "modifyConfig.json";
    public static final String API_URL_CHECK_ALLOW_SEARCH = "getConfig.json";
    public static final String API_URL_CHECK_MEMBER = "tokenCheck.json";
    public static final String API_URL_DELETE_POST = "removePic.json";
    public static final String API_URL_END = "viewPic.json";
    public static final String API_URL_END_MOCK = "viewPicMock.json";
    public static final String API_URL_END_REPORT = "reportPic.json";
    public static final String API_URL_HOME = "home.json";
    public static final String API_URL_HOME_BANNER = "homeBanner.json";
    public static final String API_URL_HOME_HOT_PIC = "hotPics.json";
    public static final String API_URL_HOME_HOT_USER = "hotUser.json";
    public static final String API_URL_HOME_RANDOM_TAG = "getRandomTag.json";
    public static final String API_URL_HOME_SUDDEN_TAG = "getSuddenTag.json";
    public static final String API_URL_LEAVE = "leave.json";
    public static final String API_URL_LIKE = "common_likeItContentAdd.json";
    public static final String API_URL_LIKE_LIST = "profileListOfPicLikes.json";
    public static final String API_URL_LOCATION = "locationInfo.json";
    public static final String API_URL_LOCATION_GALLERY = "locationPics.json";
    public static final String API_URL_MODIFY_EPIC = "modifyPic.json";
    public static final String API_URL_MODIFY_PROFILE = "modifyProfile.json";
    public static final String API_URL_MYFEED_PIC_LIST = "getMyFeeds.json";
    public static final String API_URL_MYFEED_TAG_LIST = "getMySubTagList.json";
    public static final String API_URL_NICKNAME_TO_MEMBERNO = "getMemberNoByNickname.json";
    public static final String API_URL_NOTI = "getActivities.json";
    public static final String API_URL_NOTI_HAS_NEW = "hasNewActivity.json";
    public static final String API_URL_NOTI_READ = "readActivity.json";
    public static final String API_URL_POST_EPIC = "postPic.json";
    public static final String API_URL_POST_EPIC_MOCK = "postPicMock.json";
    public static final String API_URL_POST_RECOMMENDED_TAG = "getRecommendedTags.json";
    public static final String API_URL_PROFILE = "getUserInfo.json";
    public static final String API_URL_PROFILE_FOLLOWER = "getFollowers.json";
    public static final String API_URL_PROFILE_FOLLOWING = "getFollowings.json";
    public static final String API_URL_PROFILE_ICON = "uploadProfileImage.json";
    public static final String API_URL_PROFILE_INTRODUCE = "modifyIntroduceDesc.json";
    public static final String API_URL_PROFILE_MY_LIKE = "myLikePics.json";
    public static final String API_URL_PROFILE_MY_PIC = "getMyPics.json";
    public static final String API_URL_PROFILE_MY_TAG = "getMyTags.json";
    public static final String API_URL_PUSH_ACTIVATE = "activateDeviceToken.json";
    public static final String API_URL_PUSH_DESTROY = "destroyDeviceToken.json";
    public static final String API_URL_PUSH_FIRST_INIT = "firstInitialize.json";
    public static final String API_URL_PUSH_GET_CONFIG = "getAllConfigs.json";
    public static final String API_URL_PUSH_GET_RECEIVE_CONFIG = "getConfig.json";
    public static final String API_URL_PUSH_INACTIVATE = "inactivateDeviceToken.json";
    public static final String API_URL_PUSH_INIT = "initialize.json";
    public static final String API_URL_PUSH_RECEIVE_COMMENT = "modifyConfig.json";
    public static final String API_URL_PUSH_RECEIVE_FOLLOWER = "modifyConfig.json";
    public static final String API_URL_PUSH_RECEIVE_FOLLOWING = "modifyConfig.json";
    public static final String API_URL_PUSH_RECEIVE_LIKE = "modifyConfig.json";
    public static final String API_URL_PUSH_RECEIVE_MENTION = "modifyConfig.json";
    public static final String API_URL_PUSH_RECEIVE_REPIC = "modifyConfig.json";
    public static final String API_URL_PUSH_SET_ETIQUETTE_TIME = "saveEtiquetteTimeConfig.json";
    public static final String API_URL_RELATED_CONTENT = "getLinkedTags.json";
    public static final String API_URL_RELATED_CONTENT_MOCK = "getLinkedTagsMock.json";
    public static final String API_URL_REPIC = "getRepics.json";
    public static final String API_URL_SEARCH_PIC = "searchPics.json";
    public static final String API_URL_SEARCH_TAG = "searchTags.json";
    public static final String API_URL_SEARCH_USER = "searchMember.json";
    public static final String API_URL_SNS_MAPPING = "snsLoginBeginWithMapping.json";
    public static final String API_URL_SNS_MAPPING_INFO = "getSnsRelationInfo.json";
    public static final String API_URL_SNS_UNMAPPING = "deleteSnsToken.json";
    public static final String API_URL_TAG = "tagInfo.json";
    public static final String API_URL_TAG_BY_PIC = "getTagsByPic.json";
    public static final String API_URL_TAG_FOLLOW = "subscribeTag.json";
    public static final String API_URL_TAG_GALLERY = "tagPics.json";
    public static final String API_URL_UNLIKE = "common_unLikeItContent.json";
    public static final String API_URL_UNREPIC = "unrepic.json";
    public static final String API_URL_USER_FOLLOW = "followUser.json";
    public static final String APPINFO_LINK_EULA = "http://www.mapia.co/support/terms.html ";
    public static final String APPINFO_LINK_LOCATION = "http://www.mapia.co/support/termsLocation.html";
    public static final String APPINFO_LINK_POLICY = "http://www.mapia.co/support/policy.html";
    public static final String APPINFO_LINK_PRIVACY = "http://www.mapia.co/support/privacy.html";
    private static final String COMMENT_TEMPLATE_ID = "default";
    public static final String COMMENT_TICKET = "mapia";
    public static final String PENALTY_BLOCK_MEMBER = "block.json";
    public static final String PENALTY_BLOCK_USER_COUNT = "getBlockMemberTotalCount.json";
    public static final String PENALTY_BLOCK_USER_LIST = "getBlockMemberList.json";
    public static final String PENALTY_HIDE_POSTING = "block.json";
    public static final String PENALTY_UNBLOCK_MEMBER = "unBlock.json";
    public static final String REPORT_ACCOUNT = "reportMember.json";
    public static final String REPORT_DOMAIN = "http://srp.mapia.co";
    public static final String REPORT_PATH = "neoMain.nhn";
    public static final String REPORT_READ_ALERT = "readPenalty.json";
    public static final String REPORT_REMOVE_ALERT = "removePenalty.json";
    public static final String REPORT_RETURN_URL = "mapia://closeWebview?version=1";
    public static final int SEARVER_REQUEST_TIMEOUT = 10000;
    public static final int SEARVER_REQUEST_TIMEOUT_POST = 100000;
    public static final int SEARVER_REQUEST_TIMEOUT_VIDEO = 1000000;
    public static final String URI_AUTHORITY = "apis.mapsns.com";
    public static final String URI_MAPIA_WEB_END = "http://www.mapia.co/pic/";
    public static final String URI_MAPIA_WEB_LOCATION = "http://www.mapia.co/location/";
    public static final String URI_MAPIA_WEB_PROFILE = "http://www.mapia.co/my/";
    public static final String URI_MAPIA_WEB_REPIC = "http://www.mapia.co/repics/";
    public static final String URI_MAPIA_WEB_TAG = "http://www.mapia.co/tag/";
    public static final String URI_SCHEME = "https";
    public static final String URI_SCHEME_HTTP = "http";
    private static final String URL_COMMENT_CREATE = "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_create_json.json";
    private static final String URL_COMMENT_DELETE = "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_delete_json.json";
    private static final String URL_COMMENT_LIST = "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_list_json.json";
    private static final String URL_COMMENT_REPORT = "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_report_json.json";
    private static final String URL_COMMENT_UPDATE = "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_update_json.json";
    private static final String URL_GET_UPLOAD_KEY = "http://uploader.nmv.naver.com/upload/getUploadKey.nhn";
    private static final String URL_GET_VIDEO_INFO = "http://uploader.nmv.naver.com/upload/getVideoInfo.nhn";
    private static final String URL_GET_VIDEO_PLAY_KEY = "http://serviceapi.nmv.naver.com/flash/createInKey.nhn";
    private static final String URL_GET_VIDEO_URL = "picVideoPlayPath.json";
    private static final String URL_UPLOAD = "http://uploader.nmv.naver.com/upload/chunk.nhn";
    private static final String URL_UPLOAD_COMPLETE = "http://uploader.nmv.naver.com/upload/completeUploadFile.nhn";
    private static final int VIDEO_SERVICE_ID = 31;

    public static String getAceHost() {
        return "ace.naver.com";
    }

    public static int getNaverNoticeManagerServerType() {
        return 0;
    }

    public static String makeAddComment() {
        try {
            return MACManager.getEncryptUrl("https://apis.mapsns.com/mapiaApp/cbox/v2_neo_create_json.json");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_create_json.json";
        }
    }

    public static String makeAllowSearchingApiUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyConfig.json");
        uriBuilder.appendQueryParameter("allowedSearchYn", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeBlockMemberUrl(final long n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("block.json");
        uriBuilder.appendQueryParameter("type", "MEMBER");
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeBlockUserCountApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getBlockMemberTotalCount.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeBlockUserListApiUrl(final int n, final int n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getBlockMemberList.json");
        uriBuilder.appendQueryParameter("page", "" + n);
        uriBuilder.appendQueryParameter("size", "" + n2);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeLoginTokenApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getLoginToken.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeLoginUrl(final String id, final String pw) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("login.json");
        uriBuilder.appendQueryParameter("id", id);
        uriBuilder.appendQueryParameter("pw", pw);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }



    public static String makeCheckAllowSearchingApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getConfig.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeCheckMember(String encryptUrl, final String s, final String s2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("tokenCheck.json");
        uriBuilder.appendQueryParameter("snsCd", encryptUrl);
        uriBuilder.appendQueryParameter("snsToken", s);
        uriBuilder.appendQueryParameter("snsTokenSecret", s2);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeCheckUploadCompleteUrl(final String s, final String s2, final JSONArray jsonArray) {
        final StringBuilder sb = new StringBuilder("http://uploader.nmv.naver.com/upload/completeUploadFile.nhn");
        sb.append("?key=" + s);
        try {
            sb.append("&fn=" + URLEncoder.encode(s2, "UTF-8"));
            sb.append("&chunkList=" + jsonArray.toString());
            return sb.toString();
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeCommentList(final long n, final long n2, String string, final int n3) {
        string = "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_list_json.json?lang=" + string + "&clientType=app-android" + "&objectId=" + n + "_" + n2 + "&snsCode=" + LoginInfo.getInstance().getSnsCd() + "&ticket=" + "mapia" + "&pageSize=20" + "&page=" + n3;
        try {
            return MACManager.getEncryptUrl(string);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return string;
        }
    }

    public static String makeDeleteComment(final long n, final long n2, final long n3, Comment o, String encryptUrl) {
        final String s = (String)("https://apis.mapsns.com/mapiaApp/cbox/v2_neo_delete_json.json?lang=ko&clientType=app-android&objectId=" + n + "_" + n2 + "&snsCode=" + LoginInfo.getInstance().getSnsCd() + "&ticket=" + "mapia" + "&commentType=txt" + "&objectUrl=XX" + "&commentNo=" + ((Comment)o).commentNo + "&resultType=comment" + "&groupId=" + n3);
//        if (encryptUrl != null) {
//            o = s + "&userType=" + encryptUrl;
//        }
//        try {
//            encryptUrl = MACManager.getEncryptUrl((String)o);
//            return encryptUrl;
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//            return (String)o;
//        }
        return s;
    }

    public static String makeEndUrl(final long n, final long n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("viewPic.json");
        uriBuilder.appendQueryParameter("picNo", String.valueOf(n));
        uriBuilder.appendQueryParameter("memberNo", String.valueOf(n2));
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeEndWebUrl(final long n, final long n2) {
        try {
            final StringBuilder sb = new StringBuilder("http://www.mapia.co/pic/");
            sb.append(n);
            sb.append("/");
            sb.append(n2);
            return sb.toString();
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeEntireFollowingListUrl(final long n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getFollowings.json");
        uriBuilder.appendQueryParameter("memberNo", String.valueOf(n));
        uriBuilder.appendQueryParameter("fetchType", "ALL");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeExplanationUrl() {
        return "https://inoti.naver.com/inoti/service.nhn?m=honorRepostOnline";
    }

    public static String makeFollowTagApiUrl(String encryptUrl, final String s) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("subscribeTag.json");
        uriBuilder.appendQueryParameter("targetTag", encryptUrl);
        uriBuilder.appendQueryParameter("followingStatusType", s);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeFollowUserApiUrl(final long n, String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("followUser.json");
        uriBuilder.appendQueryParameter("targetMemberNo", "" + n);
        uriBuilder.appendQueryParameter("followingStatusType", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeGetUploadKeyUrl(final String s, final long n, final long n2) {
        final StringBuilder sb = new StringBuilder("http://uploader.nmv.naver.com/upload/getUploadKey.nhn");
        sb.append("?sid=" + String.valueOf(31));
        while (true) {
            try {
                sb.append("&fn=" + URLEncoder.encode(s, "UTF-8"));
                sb.append("&fs=" + n);
                sb.append("&cs=" + n2);
                return sb.toString();
            }
            catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                continue;
            }
        }
    }

    public static String makeGetVideoInfoUrl(final String s) {
        final StringBuilder sb = new StringBuilder("http://uploader.nmv.naver.com/upload/getVideoInfo.nhn");
        sb.append("?key=" + s);
        return sb.toString();
    }

    public static String makeGetVideoPlayKey(final String s) {
        final StringBuilder sb = new StringBuilder("http://serviceapi.nmv.naver.com/flash/createInKey.nhn");
        sb.append("?vid=" + s);
        sb.append("&sid=20031");
        return sb.toString();
    }

    public static String makeGetVideoUrl(final Long n, final Long n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("picVideoPlayPath.json");
        uriBuilder.appendQueryParameter("picNo", String.valueOf(n));
        uriBuilder.appendQueryParameter("memberNo", String.valueOf(n2));
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeGoogleDetailSearch(final String s) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + s + "&sensor=false" + "&components=country:kr" + "&language=ko" + "&key=" + "AIzaSyADf-WZGHFaWz83_R9ByjL1yj6BZx02Ego";
    }

    public static String makeGoogleNearBySearch(final double n, final double n2) {
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + String.format("%s,%s", String.valueOf(n), String.valueOf(n2)) + "&sensor=false" + "&radius=500" + "&language=ko" + "&key=" + "AIzaSyADf-WZGHFaWz83_R9ByjL1yj6BZx02Ego";
    }

    public static String makeGooglePOIUrl(String s, final String s2, String string, final String s3) {
        String s4 = null;
        while (true) {
            try {
                s = URLEncoder.encode(s, "UTF-8");
                s4 = (s = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + s + "&sensor=false" + "&key=" + "AIzaSyADf-WZGHFaWz83_R9ByjL1yj6BZx02Ego");
                if (string != null) {
                    s = s4;
                    if (s3 != null) {
                        s = s4 + "&location=" + string + "," + s3;
                    }
                }
                string = s;
                if (s2 != null) {
                    string = s + "&radius=" + s2;
                }
                return string;
            }
            catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                s = s4;
                continue;
            }
        }
    }

    public static String makeHidePostingUrl(final long n, final long n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("block.json");
        uriBuilder.appendQueryParameter("type", "PIC");
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        uriBuilder.appendQueryParameter("picNo", "" + n2);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeHomeApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("home.json");
        uriBuilder.appendQueryParameter("now", "" + System.currentTimeMillis());
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeHomeBannerApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("homeBanner.json");
        uriBuilder.appendQueryParameter("now", "" + System.currentTimeMillis());
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeHotPicApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("hotPics.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeHotUserApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("hotUser.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeInfoEulaApiUrl() {
        return "http://www.mapia.co/support/terms.html ";
    }

    public static String makeInfoHelpApiUrl() {
        return "https://m.help.naver.com/support/service/main.nhn?serviceNo=11064";
    }

    public static String makeInfoLocationApiUrl() {
        return "http://www.mapia.co/support/termsLocation.html";
    }

    public static String makeInfoPolicyApiUrl() {
        return "http://www.mapia.co/support/policy.html";
    }

    public static String makeInfoPrivacyApiUrl() {
        return "http://www.mapia.co/support/privacy.html";
    }

    public static String makeLeaveUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("leave.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeLikeListUrl(String encryptUrl, final String s, final int n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("profileListOfPicLikes.json");
        if (encryptUrl == null || s == null) {
            return null;
        }
        uriBuilder.appendQueryParameter("picNo", encryptUrl);
        uriBuilder.appendQueryParameter("memberNo", s);
        uriBuilder.appendQueryParameter("page", String.valueOf(n));
        uriBuilder.appendQueryParameter("size", String.valueOf(20));
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeLikeUrl(final long n, final long n2) {
        if (n == 0L || n2 == 0L) {
            return null;
        }
        final String string = "https://apis.mapsns.com/mapiaApp/likeit/mapiaApp_neo_like_json.json?contentsId=" + (n + "_" + n2) + "&serviceId=MAPIA" + "&countCallbackYn=Y";
        try {
            return MACManager.getEncryptUrl(string);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeLocationApiUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("locationInfo.json");
        uriBuilder.appendQueryParameter("locationCode", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeLocationGalleryApiUrl(final int n, final int n2, String encryptUrl, final String s) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("locationPics.json");
        uriBuilder.appendQueryParameter("isExtFormat", "true");
        uriBuilder.appendQueryParameter("page", "" + n);
        uriBuilder.appendQueryParameter("size", "" + n2);
        uriBuilder.appendQueryParameter("orderType", encryptUrl);
        uriBuilder.appendQueryParameter("locationCode", s);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeLocationGalleryWebUrl(String string, final String s) {
        try {
            final StringBuilder sb = new StringBuilder("http://www.mapia.co/location/");
            sb.append("?code=" + string);
            sb.append("&name=" + URLEncoder.encode(s, "UTF-8"));
            string = sb.toString();
            return string;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeLocationImageApiUrl(final double n, final double n2, final int n3, final int n4) {
//        return "http://maps.googleapis.com" + UrlSigner.signStaticMapApiUrl("http://maps.googleapis.com/maps/api/staticmap?center=" + n + "," + n2 + "&markers=color:purple%7C" + n + "," + n2 + "&zoom=15&size=" + n3 + "x" + n4 + "&language=ko" + "®ion=KR" + "&client=gme-nhncorp");
        return "http://maps.googleapis.com";
    }

    public static String makeModifyComment() {
        try {
            return MACManager.getEncryptUrl("https://apis.mapsns.com/mapiaApp/cbox/v2_neo_update_json.json");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_update_json.json";
        }
    }

    public static String makeModifyEpicUrl(final LocationData locationData, final String s) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        if (locationData != null) {
            uriBuilder.appendQueryParameter("latitude", String.valueOf(locationData.latitude));
            uriBuilder.appendQueryParameter("longitude", String.valueOf(locationData.longitude));
            uriBuilder.appendQueryParameter("code", String.valueOf(locationData.placeId));
            uriBuilder.appendQueryParameter("name", locationData.main);
            uriBuilder.appendQueryParameter("address", locationData.address);
        }
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyPic.json");


            try {
                if (s == null) {
                    return MACManager.getEncryptUrl(uriBuilder.toString());
                }
                return MACManager.getEncryptUrl(uriBuilder.toString() + s);

            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }


    }

    public static String makeModifyProfileUrl(String encryptUrl, final String s) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyProfile.json");
        uriBuilder.appendQueryParameter("nickname", encryptUrl);
        uriBuilder.appendQueryParameter("introduceDesc", s);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeMyaccountWebUrl(final long n) {
        try {
            final StringBuilder sb = new StringBuilder("http://www.mapia.co/my/");
            sb.append(n);
            sb.append("/profile");
            sb.append("/?t=" + Calendar.getInstance().getTime().getTime());
            return sb.toString();
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeMyfeedInfoApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getMyFeeds.json");
        uriBuilder.appendQueryParameter("size", "1");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeMyfeedListApiUrl(final long n, final int n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getMyFeeds.json");
        uriBuilder.appendQueryParameter("size", "" + n2);
        if (n > 0L) {
            uriBuilder.appendQueryParameter("lastFeedId", "" + n);
        }
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeMyfeedTagListApiUrl(final long n, final int n2, final boolean b) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getMySubTagList.json");
        uriBuilder.appendQueryParameter("size", "" + n2);
        if (n > 0L) {
            uriBuilder.appendQueryParameter("lastFollowingRegDate", "" + n);
        }
        if (b) {
            uriBuilder.appendQueryParameter("type", "SIMPLE");
        }
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNicknameToMemberno(final ArrayList<String> list) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getMemberNoByNickname.json");
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            uriBuilder.appendQueryParameter("nickname", (String)iterator.next());
        }
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeNotiApiUrl(String encryptUrl, final long n, final int n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getActivities.json");
        uriBuilder.appendQueryParameter("type", encryptUrl);
        uriBuilder.appendQueryParameter("size", "" + n2);
        if (n > 0L) {
            uriBuilder.appendQueryParameter("lastActivityId", "" + n);
        }
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNotiHasNewApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("hasNewActivity.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNotiReadAllApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("readActivity.json");
        uriBuilder.appendQueryParameter("type", "ALL");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNotiReadApiUrl(final long n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("readActivity.json");
        uriBuilder.appendQueryParameter("type", "ONE");
        uriBuilder.appendQueryParameter("activityId", "" + n);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushActivateApiUrl() {
        if (StringUtils.isBlank(NpushUtils.getNpushAppKey())) {
            return null;
        }
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("activateDeviceToken.json");
        uriBuilder.appendQueryParameter("deviceId", NpushUtils.getRegistrationId());
        uriBuilder.appendQueryParameter("appKey", NpushUtils.getNpushAppKey());
        uriBuilder.appendQueryParameter("duId", NpushUtils.getDeviceUniqueId());
        uriBuilder.appendQueryParameter("appId", "APG00213");
        uriBuilder.appendQueryParameter("deviceType", "nni");
        uriBuilder.appendQueryParameter("manufacturer", CommonConstants.PUSH_MANUFACTURER);
        uriBuilder.appendQueryParameter("model", CommonConstants.PUSH_MODEL);
        uriBuilder.appendQueryParameter("os", CommonConstants.PUSH_OS);
        uriBuilder.appendQueryParameter("vendor", CommonConstants.PUSH_VENDOR);
        uriBuilder.appendQueryParameter("appVersion", "1.0.01");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushDestroyApiUrl() {
        if (StringUtils.isBlank(NpushUtils.getNpushAppKey())) {
            return null;
        }
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("destroyDeviceToken.json");
        uriBuilder.appendQueryParameter("deviceId", NpushUtils.getRegistrationId());
        uriBuilder.appendQueryParameter("appKey", NpushUtils.getNpushAppKey());
        uriBuilder.appendQueryParameter("duId", NpushUtils.getDeviceUniqueId());
        uriBuilder.appendQueryParameter("appId", "APG00213");
        uriBuilder.appendQueryParameter("deviceType", "nni");
        uriBuilder.appendQueryParameter("manufacturer", CommonConstants.PUSH_MANUFACTURER);
        uriBuilder.appendQueryParameter("model", CommonConstants.PUSH_MODEL);
        uriBuilder.appendQueryParameter("os", CommonConstants.PUSH_OS);
        uriBuilder.appendQueryParameter("vendor", CommonConstants.PUSH_VENDOR);
        uriBuilder.appendQueryParameter("appVersion", "1.0.01");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushFirstInitApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("firstInitialize.json");
        uriBuilder.appendQueryParameter("deviceId", NpushUtils.getRegistrationId());
        uriBuilder.appendQueryParameter("duId", NpushUtils.getDeviceUniqueId());
        uriBuilder.appendQueryParameter("appId", "APG00213");
        uriBuilder.appendQueryParameter("deviceType", "nni");
        uriBuilder.appendQueryParameter("manufacturer", CommonConstants.PUSH_MANUFACTURER);
        uriBuilder.appendQueryParameter("model", CommonConstants.PUSH_MODEL);
        uriBuilder.appendQueryParameter("os", CommonConstants.PUSH_OS);
        uriBuilder.appendQueryParameter("vendor", CommonConstants.PUSH_VENDOR);
        uriBuilder.appendQueryParameter("appVersion", "1.0.01");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushGetConfigApiUrl() {
        if (StringUtils.isBlank(NpushUtils.getNpushAppKey())) {
            return null;
        }
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getAllConfigs.json");
        uriBuilder.appendQueryParameter("deviceId", NpushUtils.getRegistrationId());
        uriBuilder.appendQueryParameter("appKey", NpushUtils.getNpushAppKey());
        uriBuilder.appendQueryParameter("duId", NpushUtils.getDeviceUniqueId());
        uriBuilder.appendQueryParameter("appId", "APG00213");
        uriBuilder.appendQueryParameter("deviceType", "nni");
        uriBuilder.appendQueryParameter("manufacturer", CommonConstants.PUSH_MANUFACTURER);
        uriBuilder.appendQueryParameter("model", CommonConstants.PUSH_MODEL);
        uriBuilder.appendQueryParameter("os", CommonConstants.PUSH_OS);
        uriBuilder.appendQueryParameter("vendor", CommonConstants.PUSH_VENDOR);
        uriBuilder.appendQueryParameter("appVersion", "1.0.01");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushGetReceiveConfigUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getConfig.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushInactivateApiUrl() {
        if (StringUtils.isBlank(NpushUtils.getNpushAppKey())) {
            return null;
        }
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("inactivateDeviceToken.json");
        uriBuilder.appendQueryParameter("deviceId", NpushUtils.getRegistrationId());
        uriBuilder.appendQueryParameter("appKey", NpushUtils.getNpushAppKey());
        uriBuilder.appendQueryParameter("duId", NpushUtils.getDeviceUniqueId());
        uriBuilder.appendQueryParameter("appId", "APG00213");
        uriBuilder.appendQueryParameter("deviceType", "nni");
        uriBuilder.appendQueryParameter("manufacturer", CommonConstants.PUSH_MANUFACTURER);
        uriBuilder.appendQueryParameter("model", CommonConstants.PUSH_MODEL);
        uriBuilder.appendQueryParameter("os", CommonConstants.PUSH_OS);
        uriBuilder.appendQueryParameter("vendor", CommonConstants.PUSH_VENDOR);
        uriBuilder.appendQueryParameter("appVersion", "1.0.01");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushInitApiUrl() {
        if (StringUtils.isBlank(NpushUtils.getNpushAppKey())) {
            return null;
        }
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("initialize.json");
        uriBuilder.appendQueryParameter("deviceId", NpushUtils.getRegistrationId());
        uriBuilder.appendQueryParameter("appKey", NpushUtils.getNpushAppKey());
        uriBuilder.appendQueryParameter("duId", NpushUtils.getDeviceUniqueId());
        uriBuilder.appendQueryParameter("appId", "APG00213");
        uriBuilder.appendQueryParameter("deviceType", "nni");
        uriBuilder.appendQueryParameter("manufacturer", CommonConstants.PUSH_MANUFACTURER);
        uriBuilder.appendQueryParameter("model", CommonConstants.PUSH_MODEL);
        uriBuilder.appendQueryParameter("os", CommonConstants.PUSH_OS);
        uriBuilder.appendQueryParameter("vendor", CommonConstants.PUSH_VENDOR);
        uriBuilder.appendQueryParameter("appVersion", "1.0.01");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushReceiveCommentUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyConfig.json");
        uriBuilder.appendQueryParameter("commentAlarm", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushReceiveFollowerUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyConfig.json");
        uriBuilder.appendQueryParameter("newFollowerAlarm", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushReceiveFollowingUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyConfig.json");
        uriBuilder.appendQueryParameter("followingAlarm", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushReceiveLikeUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyConfig.json");
        uriBuilder.appendQueryParameter("likeAlarm", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushReceiveMentionUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyConfig.json");
        uriBuilder.appendQueryParameter("mentionAlarm", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushReceiveRepicUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyConfig.json");
        uriBuilder.appendQueryParameter("repicAlarm", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeNpushSetEtiquetteTimeApiUrl(String encryptUrl, final int n, final int n2) {
        if (StringUtils.isBlank(NpushUtils.getNpushAppKey())) {
            return null;
        }
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("saveEtiquetteTimeConfig.json");
        uriBuilder.appendQueryParameter("deviceId", NpushUtils.getRegistrationId());
        uriBuilder.appendQueryParameter("appKey", NpushUtils.getNpushAppKey());
        uriBuilder.appendQueryParameter("duId", NpushUtils.getDeviceUniqueId());
        uriBuilder.appendQueryParameter("appId", "APG00213");
        uriBuilder.appendQueryParameter("deviceType", "nni");
        uriBuilder.appendQueryParameter("manufacturer", CommonConstants.PUSH_MANUFACTURER);
        uriBuilder.appendQueryParameter("model", CommonConstants.PUSH_MODEL);
        uriBuilder.appendQueryParameter("os", CommonConstants.PUSH_OS);
        uriBuilder.appendQueryParameter("vendor", CommonConstants.PUSH_VENDOR);
        uriBuilder.appendQueryParameter("appVersion", "1.0.01");
        uriBuilder.appendQueryParameter("useYn", encryptUrl);
        uriBuilder.appendQueryParameter("startTime", "" + n);
        uriBuilder.appendQueryParameter("endTime", "" + n2);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makePostEpicUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http");
        uriBuilder.authority("54.65.32.198:8081");
        uriBuilder.appendPath("post");

// MAC 암호화는 차후에.
//            try {
//                if (encryptUrl == null) {
//                    encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
//                    return encryptUrl;
//                }
//                return MACManager.getEncryptUrl(uriBuilder.toString() + encryptUrl);
//
//            }
//            catch (Exception ex) {
//                ex.printStackTrace();
//                return null;
//            }
        //임시로 평문 uri 반환
        return uriBuilder.toString();
        }


    public static String makeProfileAlbumApiUrl(final int n, final int n2, String encryptUrl, final String s, final long n3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("tagPics.json");
        uriBuilder.appendQueryParameter("isExtFormat", "true");
        uriBuilder.appendQueryParameter("page", "" + n);
        uriBuilder.appendQueryParameter("size", "" + n2);
        uriBuilder.appendQueryParameter("memberNo", "" + n3);
        uriBuilder.appendQueryParameter("orderType", encryptUrl);
        uriBuilder.appendQueryParameter("tag", s);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfileApiUrl(final long n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getUserInfo.json");
        uriBuilder.appendQueryParameter("targetMemberNo", "" + n);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfileFollowerApiUrl(final long n, final long n2, final int n3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getFollowers.json");
        uriBuilder.appendQueryParameter("size", "" + n3);
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        if (n2 > 0L) {
            uriBuilder.appendQueryParameter("lastFollowerRegDate", "" + n2);
        }
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfileFollowingApiUrl(final long n, final long n2, final int n3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getFollowings.json");
        uriBuilder.appendQueryParameter("size", "" + n3);
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        if (n2 > 0L) {
            uriBuilder.appendQueryParameter("lastFollowingRegDate", "" + n2);
        }
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfileIconApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("uploadProfileImage.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfileIconApiUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("modifyIntroduceDesc.json");
        uriBuilder.appendQueryParameter("introduce", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfileLikeApiUrl(final int n, final int n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("myLikePics.json");
        uriBuilder.appendQueryParameter("isExtFormat", "true");
        uriBuilder.appendQueryParameter("page", "" + n);
        uriBuilder.appendQueryParameter("size", "" + n2);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfilePicGridApiUrl(final long n, final int n2, final int n3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getMyPics.json");
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        uriBuilder.appendQueryParameter("page", "" + n2);
        uriBuilder.appendQueryParameter("size", "" + n3);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfilePicListApiUrl(final long n, final int n2, final int n3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getMyPics.json");
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        uriBuilder.appendQueryParameter("page", "" + n2);
        uriBuilder.appendQueryParameter("size", "" + n3);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeProfileTagApiUrl(final long n, final int n2, final int n3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getMyTags.json");
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        uriBuilder.appendQueryParameter("page", "" + n2);
        uriBuilder.appendQueryParameter("size", "" + n3);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeRandomTagApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getRandomTag.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeReadPenaltyAlert(final long n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("readPenalty.json");
        uriBuilder.appendQueryParameter("penaltyNo", String.valueOf(n));
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeRecommendedTagUrl(String encryptUrl, final String s, final String s2, final String s3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getRecommendedTags.json");
        if (encryptUrl != null) {
            uriBuilder.appendQueryParameter("recommendationType", encryptUrl);
        }
        if (s != null) {
            uriBuilder.appendQueryParameter("tag", s);
        }
        if (s2 != null) {
            uriBuilder.appendQueryParameter("parentPicNo", s2);
        }
        if (s3 != null) {
            uriBuilder.appendQueryParameter("parentMemberNo", s3);
        }
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeRelatedContentUrl(final long n, String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getLinkedTags.json");
        uriBuilder.appendQueryParameter("memberNo", String.valueOf(n));
        uriBuilder.appendQueryParameter("tags", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeRelatedContentUrlMock(final long n, final long n2, final int n3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getLinkedTags.json");
        uriBuilder.appendQueryParameter("picNo", String.valueOf(n));
        uriBuilder.appendQueryParameter("memberNo", String.valueOf(n2));
        uriBuilder.appendQueryParameter("size", String.valueOf(n3));
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeRemovePenaltyAlert(final long n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("removePenalty.json");
        uriBuilder.appendQueryParameter("penaltyNo", String.valueOf(n));
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeRemovePostUrl(final long n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("removePic.json");
        uriBuilder.appendQueryParameter("picNo", String.valueOf(n));
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeRepicGalleryWebUrl(final long n, final long n2) {
        try {
            final StringBuilder sb = new StringBuilder("http://www.mapia.co/repics/");
            sb.append(n);
            sb.append("/");
            sb.append(n2);
            return sb.toString();
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeRepicListUrl(final long n, final long n2, final int n3, final int n4) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getRepics.json");
        uriBuilder.appendQueryParameter("picNo", String.valueOf(n));
        uriBuilder.appendQueryParameter("memberNo", String.valueOf(n2));
        uriBuilder.appendQueryParameter("page", String.valueOf(n3));
        uriBuilder.appendQueryParameter("size", String.valueOf(n4));
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeReportAccountUrl(final long n, String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("reportMember.json");
        uriBuilder.appendQueryParameter("memberNo", String.valueOf(n));
        uriBuilder.appendQueryParameter("reportDesc", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeReportCommentUrl(final long n, final long n2, final long n3) {
        final String string = "https://apis.mapsns.com/mapiaApp/cbox/v2_neo_report_json.json?lang=ko&objectId=" + n + "_" + n2 + "&ticket=" + "mapia" + "&commentNo=" + n3;
        try {
            return MACManager.getEncryptUrl(string);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return string;
        }
    }

    public static String makeReportUrl(final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final String s8) {
        final Uri.Builder buildUpon = Uri.parse("http://srp.mapia.co").buildUpon();
        buildUpon.appendPath("neoMain.nhn");
        buildUpon.appendQueryParameter("itemSvcCd", "PHL");
        buildUpon.appendQueryParameter("itemVirtualSvcCd", "PHL");
        buildUpon.appendQueryParameter("itemType", s);
        buildUpon.appendQueryParameter("itemId", s2);
        buildUpon.appendQueryParameter("itemTitle", s3);
        buildUpon.appendQueryParameter("itemCateId", s4);
        buildUpon.appendQueryParameter("itemCateName", s5);
        buildUpon.appendQueryParameter("itemCateLevel", String.valueOf(0));
        buildUpon.appendQueryParameter("itemWriterId", s6);
        buildUpon.appendQueryParameter("itemWriterNick", s7);
        buildUpon.appendQueryParameter("itemDt", s8);
        buildUpon.appendQueryParameter("retUrl", "mapia://closeWebview?version=1");
        buildUpon.appendQueryParameter("m", "rprtMobileFrmApp");
        return buildUpon.toString();
    }

    public static String makeSearchPicApiUrl(String encryptUrl, final int n, final int n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("searchPics.json");
        uriBuilder.appendQueryParameter("page", "" + n);
        uriBuilder.appendQueryParameter("size", "" + n2);
        uriBuilder.appendQueryParameter("query", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeSearchPicAutoCompleteApiUrl(String encode) {
        final String s = "";
        try {
            encode = URLEncoder.encode(encode, "UTF-8");
            return "http://acpic.mapia.co/ac?st=1&r_lt=1&r_format=json&t_koreng=1&q_enc=UTF-8&r_enc=UTF-8&r_unicode=0&r_escape=1&frm=mapia&q=" + encode;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            encode = s;
            return "http://acpic.mapia.co/ac?st=1&r_lt=1&r_format=json&t_koreng=1&q_enc=UTF-8&r_enc=UTF-8&r_unicode=0&r_escape=1&frm=mapia&q=" + encode;
        }
    }

    public static String makeSearchTagApiUrl(String encryptUrl, final int n, final int n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("searchTags.json");
        uriBuilder.appendQueryParameter("page", "" + n);
        uriBuilder.appendQueryParameter("size", "" + n2);
        uriBuilder.appendQueryParameter("query", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeSearchTagAutoCompleteApiUrl(String encode) {
        final String s = "";
        try {
            encode = URLEncoder.encode(encode, "UTF-8");
            return "http://actag.mapia.co/ac?st=1&r_lt=1&r_format=json&t_koreng=1&q_enc=UTF-8&r_enc=UTF-8&r_unicode=0&r_escape=1&frm=mapia&q=" + encode;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            encode = s;
            return "http://actag.mapia.co/ac?st=1&r_lt=1&r_format=json&t_koreng=1&q_enc=UTF-8&r_enc=UTF-8&r_unicode=0&r_escape=1&frm=mapia&q=" + encode;
        }
    }

    public static String makeSearchUserApiUrl(String encryptUrl, final int n, final int n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("searchMember.json");
        uriBuilder.appendQueryParameter("page", "" + n);
        uriBuilder.appendQueryParameter("size", "" + n2);
        uriBuilder.appendQueryParameter("nickname", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeSearchUserAutoCompleteApiUrl(String encode) {
        final String s = "";
        try {
            encode = URLEncoder.encode(encode, "UTF-8");
            return "http://acusr.mapia.co/ac?st=1&r_lt=1&r_format=json&t_koreng=1&q_enc=UTF-8&r_enc=UTF-8&r_unicode=0&r_escape=1&frm=mapia&q=" + encode;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            encode = s;
            return "http://acusr.mapia.co/ac?st=1&r_lt=1&r_format=json&t_koreng=1&q_enc=UTF-8&r_enc=UTF-8&r_unicode=0&r_escape=1&frm=mapia&q=" + encode;
        }
    }

    public static String makeSettingOpinionUrl() {
        return "https://m.help.naver.com/support/inquiry/input.nhn?categoryNo=6113&serviceNo=11064";
    }

    public static String makeSnsMappingInfoUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getSnsRelationInfo.json");
        uriBuilder.appendQueryParameter("token", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeSnsMappingUrl(String encryptUrl, final String s, final String s2, final String s3) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("snsLoginBeginWithMapping.json");
        uriBuilder.appendQueryParameter("snsCd", encryptUrl);
        uriBuilder.appendQueryParameter("snsToken", s);
        uriBuilder.appendQueryParameter("snsTokenSecret", s2);
        uriBuilder.appendQueryParameter("token", s3);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeSnsUnMappingInfoUrl(String encryptUrl, final String s) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("deleteSnsToken.json");
        uriBuilder.appendQueryParameter("snsCd", encryptUrl);
        uriBuilder.appendQueryParameter("token", s);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeSuddenTagApiUrl() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getSuddenTag.json");
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeTagApiUrl(final long n, String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("tagInfo.json");
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        uriBuilder.appendQueryParameter("tag", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeTagApiUrl(String encryptUrl) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("tagInfo.json");
        uriBuilder.appendQueryParameter("tag", encryptUrl);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeTagByPicApiUrl(final long n, final long n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("getTagsByPic.json");
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        uriBuilder.appendQueryParameter("picNo", "" + n2);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeTagGalleryApiUrl(final int n, final int n2, String encryptUrl, final String s) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("tagPics.json");
        uriBuilder.appendQueryParameter("isExtFormat", "true");
        uriBuilder.appendQueryParameter("page", "" + n);
        uriBuilder.appendQueryParameter("size", "" + n2);
        uriBuilder.appendQueryParameter("orderType", encryptUrl);
        uriBuilder.appendQueryParameter("tag", s);
        try {
            encryptUrl = MACManager.getEncryptUrl(uriBuilder.toString());
            return encryptUrl;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeTaggalleryWebUrl(final long n, String string) {
        try {
            final StringBuilder sb = new StringBuilder("http://www.mapia.co/tag/");
            sb.append(String.valueOf(n));
            sb.append("/?tag=");
            sb.append(URLEncoder.encode(string, "UTF-8"));
            string = sb.toString();
            return string;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeTaggalleryWebUrl(String string) {
        try {
            final StringBuilder sb = new StringBuilder("http://www.mapia.co/tag/");
            sb.append("?tag=");
            sb.append(URLEncoder.encode(string, "UTF-8"));
            string = sb.toString();
            return string;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeUnLikeUrl(final long n, final long n2) {
        final String string = "https://apis.mapsns.com/mapiaApp/likeit/mapiaApp_neo_unlike_json.json?contentsId=" + (n + "_" + n2) + "&serviceId=MAPIA" + "&countCallbackYn=Y";
        try {
            return MACManager.getEncryptUrl(string);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeUnblockMemberUrl(final long n) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("unBlock.json");
        uriBuilder.appendQueryParameter("type", "MEMBER");
        uriBuilder.appendQueryParameter("memberNo", "" + n);
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String makeUnrepicUrl(final long n, final long n2) {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("apis.mapsns.com");
        uriBuilder.appendPath("mapiaApp");
        uriBuilder.appendPath("mapia");
        uriBuilder.appendPath("unrepic.json");
        uriBuilder.appendQueryParameter("picNo", String.valueOf(n));
        uriBuilder.appendQueryParameter("memberNo", String.valueOf(n2));
        try {
            return MACManager.getEncryptUrl(uriBuilder.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String makeUploadUrl(final String s, final int n, final long n2, final long n3, final String s2) {
        final StringBuilder sb = new StringBuilder("http://uploader.nmv.naver.com/upload/chunk.nhn");
        sb.append("?key=" + s);
        sb.append("&cn=" + String.valueOf(n));
        sb.append("&cs=" + String.valueOf(n2));
        sb.append("&fs=" + String.valueOf(n3));
        try {
            sb.append("&fn=" + URLEncoder.encode(s2, "UTF-8"));
            return sb.toString();
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}