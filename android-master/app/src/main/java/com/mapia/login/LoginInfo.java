package com.mapia.login;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mapia.api.model.Profile;


public class LoginInfo
{
    public static final String ID_NO = "id_no";
    public static final String MEMBER_NO = "memberNo";
    public static final String MODE = "mode";
    public static final String SNS_CD = "sns_cd";
    public static final String TOKEN = "token";
    private static LoginInfo instance;
    private String mIdNo;
    private long mMemberNo;
    private String mMode;
    private String mNickName;
    private Profile mProfile;
    private String mSnsCd;
    private String mToken;

    static {
        LoginInfo.instance = null;
    }

    public static LoginInfo getInstance() {
        synchronized (LoginInfo.class) {
            if (LoginInfo.instance == null) {
                LoginInfo.instance = new LoginInfo();
            }
            return LoginInfo.instance;
        }
    }

    public void clear() {
        LoginInfo.instance.mToken = "";
        LoginInfo.instance.mSnsCd = "";
        LoginInfo.instance.mMode = "";
        LoginInfo.instance.mIdNo = "";
    }

    public long getMemberNo() {
        return LoginInfo.instance.mMemberNo;
    }

    public String getMode() {
        return LoginInfo.instance.mMode;
    }

    public String getNeoidId() {
        return LoginInfo.instance.mIdNo;
    }

    public String getNickName() {
        return LoginInfo.instance.mNickName;
    }

    public Profile getProfile() {
        return LoginInfo.instance.mProfile;
    }

    public String getSnsCd() {
        return LoginInfo.instance.mSnsCd;
    }

    public String getToken() {
        return LoginInfo.instance.mToken;
    }

    public boolean isLogined() {
        return LoginInfo.instance.mToken != null && LoginInfo.instance.mIdNo != null && LoginInfo.instance.mMode != null && LoginInfo.instance.mSnsCd != null && !LoginInfo.instance.mToken.isEmpty() && !LoginInfo.instance.mIdNo.isEmpty() && !LoginInfo.instance.mMode.isEmpty() && !LoginInfo.instance.mSnsCd.isEmpty();
    }

    public void loadLoginInfo(final Context context) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        LoginInfo.instance.mToken = defaultSharedPreferences.getString("token", (String)null);
        LoginInfo.instance.mIdNo = defaultSharedPreferences.getString("id_no", (String)null);
        LoginInfo.instance.mMode = defaultSharedPreferences.getString("mode", (String)null);
        LoginInfo.instance.mSnsCd = defaultSharedPreferences.getString("sns_cd", (String)null);
        LoginInfo.instance.mMemberNo = defaultSharedPreferences.getLong("memberNo", -1L);
    }

    public void saveNeoLoginInfo(final Context context) {
        final SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString("token", LoginInfo.instance.mToken);
        edit.putString("id_no", LoginInfo.instance.mIdNo);
        edit.putString("mode", LoginInfo.instance.mMode);
        edit.putString("sns_cd", LoginInfo.instance.mSnsCd);
        edit.putLong("memberNo", LoginInfo.instance.mMemberNo);
        edit.commit();
    }

    public void setMemberNo(final long mMemberNo) {
        synchronized (this) {
            LoginInfo.instance.mMemberNo = mMemberNo;
        }
    }

    public void setMode(final String mMode) {
        synchronized (this) {
            LoginInfo.instance.mMode = mMode;
        }
    }

    public void setNeoidAccessToken(final String mToken) {
        synchronized (this) {
            LoginInfo.instance.mToken = mToken;
        }
    }

    public void setNeoidId(final String mIdNo) {
        synchronized (this) {
            LoginInfo.instance.mIdNo = mIdNo;
        }
    }

    public void setNickName(final String mNickName) {
        synchronized (this) {
            LoginInfo.instance.mNickName = mNickName;
        }
    }

    public void setProfile(final Profile mProfile) {
        synchronized (this) {
            LoginInfo.instance.mProfile = mProfile;
        }
    }

    public void setSnsCd(final String mSnsCd) {
        synchronized (this) {
            LoginInfo.instance.mSnsCd = mSnsCd;
        }
    }

    public void setToken(final String mToken) {
        synchronized (this) {
            LoginInfo.instance.mToken = mToken;
        }
    }
}