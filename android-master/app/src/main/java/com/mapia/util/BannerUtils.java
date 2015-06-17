package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
public class BannerUtils
{
    public static String getHost(final String s) {
        if (s.contains("://")) {
            final String[] split = s.split("://");
            if (split.length > 1) {
                return split[1].split("\\?")[0];
            }
        }
        return null;
    }

    public static String getProtocol(final String s) {
        if (!s.contains("://")) {
            return null;
        }
        return s.split("://")[0];
    }

    public static Map<String, String> getQueryParams(String decode) {
        Map<String, String> map;
        if (!decode.contains("?")) {
            map = null;
        }
        else {
            try {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                final String[] split = decode.split("\\?");
                map = hashMap;
                if (split.length > 1) {
                    final String[] split2 = split[1].split("&");
                    final int length = split2.length;
                    int n = 0;
                    while (true) {
                        map = hashMap;
                        if (n >= length) {
                            break;
                        }
                        final String[] split3 = split2[n].split("=");
                        final String decode2 = URLDecoder.decode(split3[0], "UTF-8");
                        decode = "";
                        if (split3.length > 1) {
                            decode = URLDecoder.decode(split3[1], "UTF-8");
                        }
                        hashMap.put(decode2, decode);
                        ++n;
                    }
                }
            }
            catch (UnsupportedEncodingException ex) {
                throw new AssertionError((Object)ex);
            }
        }
        return map;
    }
}