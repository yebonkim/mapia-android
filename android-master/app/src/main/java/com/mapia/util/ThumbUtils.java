package com.mapia.util;

import com.mapia.common.Thumb;

/**
 * Created by daehyun on 15. 6. 8..
 */

public class ThumbUtils
{
    private static int resolution;

    static {
        ThumbUtils.resolution = 0;
    }

    public static String getSuffix(final Thumb thumb) {
        if (ThumbUtils.resolution == 0) {
            initThumbTypeUtil();
        }
        switch (ThumbUtils.resolution) {
            default: {
                return "?type=";
            }
            case 4: {
                return "?type=" + thumb.for480();
            }
            case 7: {
                return "?type=" + thumb.for720();
            }
        }
    }

    public static void initThumbTypeUtil() {
        if (ThumbUtils.resolution > 0) {
            return;
        }
        if (DeviceUtils.getDeviceWidth() < 720) {
            ThumbUtils.resolution = 4;
            return;
        }
        ThumbUtils.resolution = 7;
    }
}