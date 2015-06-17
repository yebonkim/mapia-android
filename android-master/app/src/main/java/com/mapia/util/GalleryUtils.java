package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.graphics.Color;

import java.util.HashMap;

public class GalleryUtils
{
    private static int oneColumnThumbHeight34;
    private static int oneColumnThumbHeight43;
    private static int oneColumnThumbWidth;
    private static int threeColumnThumbHeight34;
    private static int threeColumnThumbHeight43;
    private static int threeColumnThumbWidth;

    static {
        GalleryUtils.oneColumnThumbWidth = 0;
        GalleryUtils.oneColumnThumbHeight43 = 0;
        GalleryUtils.oneColumnThumbHeight34 = 0;
        GalleryUtils.threeColumnThumbWidth = 0;
        GalleryUtils.threeColumnThumbHeight43 = 0;
        GalleryUtils.threeColumnThumbHeight34 = 0;
    }

    public static int getOneColumnThumbHeight(final int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 0: {
                return GalleryUtils.oneColumnThumbWidth;
            }
            case 1: {
                if (GalleryUtils.oneColumnThumbHeight43 > 0) {
                    return GalleryUtils.oneColumnThumbHeight43;
                }
                return GalleryUtils.oneColumnThumbWidth * 3 / 4;
            }
            case 2: {
                if (GalleryUtils.oneColumnThumbHeight34 > 0) {
                    return GalleryUtils.oneColumnThumbHeight34;
                }
                return GalleryUtils.oneColumnThumbWidth * 4 / 3;
            }
        }
    }

    public static int getOneColumnThumbWidth() {
        if (GalleryUtils.oneColumnThumbWidth > 0) {
            return GalleryUtils.oneColumnThumbWidth;
        }
        return GalleryUtils.oneColumnThumbWidth = DeviceUtils.getDeviceWidth();
    }

    public static HashMap<String, Integer> getOneColumnThumbWidthHeight(final int n, final int n2) {
        final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        if (n == 0 || n2 == 0) {
            hashMap.put("width", getOneColumnThumbWidth());
            hashMap.put("height", getOneColumnThumbWidth());
            return hashMap;
        }
        switch (getRatioType(n, n2)) {
            default: {
                return hashMap;
            }
            case 0: {
                hashMap.put("width", getOneColumnThumbWidth());
                hashMap.put("height", getOneColumnThumbWidth());
                return hashMap;
            }
            case 1: {
                hashMap.put("width", getOneColumnThumbWidth());
                hashMap.put("height", getOneColumnThumbHeight(1));
                return hashMap;
            }
            case 2: {
                hashMap.put("width", getOneColumnThumbWidth());
                hashMap.put("height", getOneColumnThumbHeight(2));
                return hashMap;
            }
        }
    }

    public static int getRatioType(final int n, final int n2) {
        final float n3 = n2 / n;
        if (n3 > 1.1) {
            return 2;
        }
        if (n3 < 0.9) {
            return 1;
        }
        return 0;
    }

    public static int getThreeColumnThumbHeight(final int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 0: {
                return GalleryUtils.threeColumnThumbWidth;
            }
            case 1: {
                if (GalleryUtils.threeColumnThumbHeight43 > 0) {
                    return GalleryUtils.threeColumnThumbHeight43;
                }
                return GalleryUtils.threeColumnThumbWidth * 3 / 4;
            }
            case 2: {
                if (GalleryUtils.threeColumnThumbHeight34 > 0) {
                    return GalleryUtils.threeColumnThumbHeight34;
                }
                return GalleryUtils.threeColumnThumbWidth * 4 / 3;
            }
        }
    }

    public static int getThreeColumnThumbWidth() {
        if (GalleryUtils.threeColumnThumbWidth > 0) {
            return GalleryUtils.threeColumnThumbWidth;
        }
        final int n = DeviceUtils.getDeviceWidth() - BitmapUtils.convertDipToPixelInt(3.0f);
        switch (n % 3) {
            case 0: {
                GalleryUtils.threeColumnThumbWidth = n / 3;
                break;
            }
            case 1: {
                GalleryUtils.threeColumnThumbWidth = n / 3 + 1;
                break;
            }
            case 2: {
                GalleryUtils.threeColumnThumbWidth = n / 3 + 1;
                break;
            }
        }
        return GalleryUtils.threeColumnThumbWidth;
    }

    public static HashMap<String, Integer> getThreeColumnThumbWidthHeight(final int n, final int n2) {
        final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        if (n == 0 || n2 == 0) {
            hashMap.put("width", getThreeColumnThumbWidth());
            hashMap.put("height", getThreeColumnThumbWidth());
            return hashMap;
        }
        switch (getRatioType(n, n2)) {
            default: {
                return hashMap;
            }
            case 0: {
                hashMap.put("width", getThreeColumnThumbWidth());
                hashMap.put("height", getThreeColumnThumbWidth());
                return hashMap;
            }
            case 1: {
                hashMap.put("width", getThreeColumnThumbWidth());
                hashMap.put("height", getThreeColumnThumbHeight(1));
                return hashMap;
            }
            case 2: {
                hashMap.put("width", getThreeColumnThumbWidth());
                hashMap.put("height", getThreeColumnThumbHeight(2));
                return hashMap;
            }
        }
    }

    public static float[] intColorToFloatARGBArray(final int n) {
        return new float[] { Color.alpha(n) / 255.0f, Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f };
    }
}