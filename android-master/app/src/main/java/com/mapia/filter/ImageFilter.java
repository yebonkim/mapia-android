package com.mapia.filter;

/**
 * Created by daehyun on 15. 6. 21..
 */

import android.graphics.Bitmap;

public class ImageFilter
{
    public static final int BLENDING_COLORBURN = 4;
    public static final int BLENDING_EXCLUSION = 7;
    public static final int BLENDING_LINEARCOLORBURN = 3;
    public static final int BLENDING_LINEARDODGE = 2;
    public static final int BLENDING_MULTIPLE = 5;
    public static final int BLENDING_NORMAL = 0;
    public static final int BLENDING_OVERLAY = 1;
    public static final int BLENDING_PINLIGHT = 9;
    public static final int BLENDING_SATURATION = 10;
    public static final int BLENDING_SCREEN = 8;
    public static final int BLENDING_SOFTLIGHT = 6;
    public static final int FILTER_ERR_BITMAP_FORMAT = 4;
    public static final int FILTER_ERR_BITMAP_INFO = 3;
    public static final int FILTER_ERR_GET_BITMAP_PIXEL = 5;
    public static final int FILTER_ERR_INVALID_PARAM = 1;
    public static final int FILTER_ERR_NOT_SUPPORT = 2;
    public static final int FILTER_ERR_OK = 0;

    static {
        try {
            System.loadLibrary("ImageFilter");
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            System.out.println("JNI Can't load libImageFilter.so");
        }
    }

    private native int applyFilter(final int p0, final Bitmap p1, final Bitmap p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final int p11, final int p12, final int p13, final int p14, final int p15, final int p16, final int p17, final int p18, final int p19, final int p20, final int p21, final int p22, final int p23, final boolean p24, final boolean p25);

    public int applyFilter(final int n, final Bitmap bitmap, final Bitmap bitmap2) {
        return this.applyFilter(n, bitmap, bitmap2, 256, 0, 0, 0);
    }

    public int applyFilter(final int n, final Bitmap bitmap, final Bitmap bitmap2, final int n2, final int n3, final int n4, final int n5) {
        return this.applyFilter(n, bitmap, bitmap2, n2, n3, n4, n5, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, -1, 0, false, false);
    }

    public native boolean setAppName(final byte[] p0);
    //since there is weird error "java.lang.UnsatisfiedLinkError: Native method not found:" so, change it as static method.
}