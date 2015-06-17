package com.mapia.npush;

/**
 * Created by daehyun on 15. 6. 13..
 */

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.mapia.util.PreferenceUtils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class NpushUtils
{
    private static int currentActivityCode;
    private static String deviceUniqueId;
    private static boolean isJustNowLogin;
    private static String npushAppKey;
    private static String registrationId;

    static {
        NpushUtils.isJustNowLogin = false;
        NpushUtils.deviceUniqueId = null;
        NpushUtils.registrationId = null;
        NpushUtils.npushAppKey = null;
        NpushUtils.currentActivityCode = 0;
    }

    public static int getCurrentActivityCode() {
        return NpushUtils.currentActivityCode;
    }

    public static String getDeviceUniqueId() {
        return NpushUtils.deviceUniqueId;
    }

    public static boolean getIsJustNowLogin() {
        return NpushUtils.isJustNowLogin;
    }

    public static String getNpushAppKey() {
        if (StringUtils.isNotBlank(NpushUtils.npushAppKey)) {
            return NpushUtils.npushAppKey;
        }
        NpushUtils.npushAppKey = PreferenceUtils.getPreference("pushAppKey");
        if (StringUtils.isNotBlank(NpushUtils.npushAppKey)) {
            return NpushUtils.npushAppKey;
        }
        return null;
    }

    public static String getRegistrationId() {
        if (StringUtils.isNotBlank(NpushUtils.registrationId)) {
            return NpushUtils.registrationId;
        }
        NpushUtils.registrationId = PreferenceUtils.getPreference("pushRegistrationId");
        if (StringUtils.isNotBlank(NpushUtils.registrationId)) {
            return NpushUtils.registrationId;
        }
        return null;
    }

    public static void setCurrentActivity(final int currentActivityCode) {
        NpushUtils.currentActivityCode = currentActivityCode;
    }

    public static void setDeviceUniqueId(final Context context) {
        String deviceId = null;
        Label_0051: {
            if ((deviceId = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()) == null) {
                final String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
                if (!"000000000000000".equals(string)) {
                    deviceId = string;
                    if (!"9774d56d682e549c".equals(string)) {
                        break Label_0051;
                    }
                }
                deviceId = null;
            }
        }
        while (true) {
            String string2;
            if ((string2 = deviceId) == null) {
//                break Label_0094;
            }
            try {
                final MessageDigest instance = MessageDigest.getInstance("MD5");
                instance.update(deviceId.getBytes(), 0, deviceId.length());
                string2 = new BigInteger(1, instance.digest()).toString(16);
                NpushUtils.deviceUniqueId = string2;
            }
            catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                string2 = deviceId;
                continue;
            }
            break;
        }
    }

    public static void setIsJustNowLogin(final boolean isJustNowLogin) {
        NpushUtils.isJustNowLogin = isJustNowLogin;
    }

    public static void setNpushAppKey(final String npushAppKey) {
        PreferenceUtils.putPreference("pushAppKey", NpushUtils.npushAppKey = npushAppKey);
    }

    public static void setRegistrationId(final String registrationId) {
        PreferenceUtils.putPreference("pushRegistrationId", NpushUtils.registrationId = registrationId);
    }
}