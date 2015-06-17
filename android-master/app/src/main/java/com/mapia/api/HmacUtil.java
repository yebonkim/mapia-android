package com.mapia.api;

/**
 * Created by daehyun on 15. 6. 13..
 */


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacUtil
{
    private static final String ALGORITHM = "HmacSHA1";
    private static final String AMPERCENT = "&";
    private static final int MAX_MESSAGESIZE = 255;
    private static final String MD = "&md=";
    private static final String MSGPAD = "msgpad=";
    private static final String QUESTION = "?";
    private static final String UTF8 = "utf-8";

    public static Mac getMac(final String s) throws NoSuchAlgorithmException, InvalidKeyException {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(s.getBytes(), "HmacSHA1");
        final Mac instance = Mac.getInstance("HmacSHA1");
        instance.init(secretKeySpec);
        return instance;
    }

    public static String getMessage(String substring, final String s) {
        substring = substring.substring(0, Math.min(255, substring.length()));
        final StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(substring).append(s);
        return sb.toString();
    }

    public static String getMessageDigest(final String s, final String s2) throws Exception {
        return getMessageDigest(getMac(s), s2);
    }

    public static String getMessageDigest(final Mac mac, final String s) {
        synchronized (mac) {
            return Base64.encodeBase64(mac.doFinal(s.getBytes()));
        }
    }

    public static String makeEncryptUrl(final Mac mac, final String s) throws Exception {
        return makeEncryptUrlCore(mac, s, String.valueOf(Calendar.getInstance().getTimeInMillis()));
    }

    public static String makeEncryptUrl(final Mac mac, final String s, final long n) throws Exception {
        return makeEncryptUrlCore(mac, s, String.valueOf(Calendar.getInstance().getTimeInMillis() + n));
    }

    private static String makeEncryptUrlCore(final Mac mac, final String s, final String s2) throws UnsupportedEncodingException {
        final String encode = URLEncoder.encode(getMessageDigest(mac, getMessage(s, s2)), "utf-8");
        final StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(s);
        if (s.contains("?")) {
            sb.append("&");
        }
        else {
            sb.append("?");
        }
        sb.append("msgpad=").append(s2).append("&md=").append(encode);
        return sb.toString();
    }

    public static String makeEncryptUrlWithMsgpad(final Mac mac, final String s, final long n) throws Exception {
        return makeEncryptUrlCore(mac, s, String.valueOf(n));
    }
}