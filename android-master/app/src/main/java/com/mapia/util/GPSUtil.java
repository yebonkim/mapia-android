package com.mapia.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by daehyun on 15. 6. 17..
 */

public class GPSUtil
{
    private static Double convertToDegree(final String s) {
        final String[] split = s.split(",", 3);
        final String[] split2 = split[0].split("/", 2);
        final double n = Double.valueOf(split2[0]) / Double.valueOf(split2[1]);
        final String[] split3 = split[1].split("/", 2);
        final double n2 = Double.valueOf(split3[0]) / Double.valueOf(split3[1]);
        final String[] split4 = split[2].split("/", 2);
        return Double.valueOf(n) + Double.valueOf(n2) / 60.0 + Double.valueOf(Double.valueOf(split4[0]) / Double.valueOf(split4[1])) / 3600.0;
    }

    public static Double getLatitudeDegree(final String s, final String s2) {
        if (StringUtils.isEmpty(s)) {
            return 200.0;
        }
        if (StringUtils.isEmpty(s2)) {
            return 200.0;
        }
        if ("N".equalsIgnoreCase(s2)) {
            return convertToDegree(s);
        }
        return 0.0 - convertToDegree(s);
    }

    public static Double getLongitudeDegree(final String s, final String s2) {
        if (StringUtils.isEmpty(s)) {
            return 200.0;
        }
        if (StringUtils.isEmpty(s2)) {
            return 200.0;
        }
        if ("E".equalsIgnoreCase(s2)) {
            return convertToDegree(s);
        }
        return 0.0 - convertToDegree(s);
    }
}