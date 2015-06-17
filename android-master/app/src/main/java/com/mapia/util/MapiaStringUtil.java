package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 17..
 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class MapiaStringUtil
{
    public static String decodeUrl(final String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return s;
        }
    }
}