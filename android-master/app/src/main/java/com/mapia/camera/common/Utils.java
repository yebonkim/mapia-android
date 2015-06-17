package com.mapia.camera.common;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;


public class Utils
{
    private static final String DEBUG_TAG = "GalleryDebug";
    private static final long INITIALCRC = -1L;
    private static final boolean IS_DEBUG_BUILD;
    private static final String MASK_STRING = "********************************";
    private static final long POLY64REV = -7661587058870466123L;
    private static final String TAG = "Utils";
    private static long[] sCrcTable;

    static {
        Utils.sCrcTable = new long[256];
        IS_DEBUG_BUILD = (Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug"));
        for (int i = 0; i < 256; ++i) {
            long n = i;
            for (int j = 0; j < 8; ++j) {
                long n2;
                if (((int)n & 0x1) != 0x0) {
                    n2 = -7661587058870466123L;
                }
                else {
                    n2 = 0L;
                }
                n = (n >> 1 ^ n2);
            }
            Utils.sCrcTable[i] = n;
        }
    }

    public static void assertTrue(final boolean b) {
        if (!b) {
            throw new AssertionError();
        }
    }

    public static int ceilLog2(final float n) {
        int n2;
        for (n2 = 0; n2 < 31 && 1 << n2 < n; ++n2) {}
        return n2;
    }

    public static <T> T checkNotNull(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    public static float clamp(final float n, final float n2, final float n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }

    public static int clamp(final int n, final int n2, final int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }

    public static long clamp(final long n, final long n2, final long n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }

    public static void closeSilently(final Cursor cursor) {
        if (cursor == null) {
            return;
        }
        try {
            cursor.close();
        }
        catch (Throwable t) {
            Log.w("Utils", "fail to close", t);
        }
    }

    public static void closeSilently(final ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor == null) {
            return;
        }
        try {
            parcelFileDescriptor.close();
        }
        catch (Throwable t) {
            Log.w("Utils", "fail to close", t);
        }
    }

    public static void closeSilently(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {
            Log.w("Utils", "close fail ", (Throwable)ex);
        }
    }

    public static int compare(final long n, final long n2) {
        if (n < n2) {
            return -1;
        }
        if (n == n2) {
            return 0;
        }
        return 1;
    }

    public static String[] copyOf(final String[] array, final int n) {
        final String[] array2 = new String[n];
        System.arraycopy(array, 0, array2, 0, Math.min(array.length, n));
        return array2;
    }

    public static final long crc64Long(final String s) {
        if (s == null || s.length() == 0) {
            return 0L;
        }
        return crc64Long(getBytes(s));
    }

    public static final long crc64Long(final byte[] array) {
        long n = -1L;
        for (int i = 0; i < array.length; ++i) {
            n = (Utils.sCrcTable[((int)n ^ array[i]) & 0xFF] ^ n >> 8);
        }
        return n;
    }

    public static void debug(final String s, final Object... array) {
        Log.v("GalleryDebug", String.format(s, array));
    }

    public static String ensureNotNull(final String s) {
        String s2 = s;
        if (s == null) {
            s2 = "";
        }
        return s2;
    }

    public static boolean equals(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }

    public static String escapeXml(final String s) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            switch (char1) {
                default: {
                    sb.append(char1);
                    break;
                }
                case 60: {
                    sb.append("<");
                    break;
                }
                case 62: {
                    sb.append(">");
                    break;
                }
                case 34: {
                    sb.append("\"");
                    break;
                }
                case 39: {
                    sb.append("'");
                    break;
                }
                case 38: {
                    sb.append("&");
                    break;
                }
            }
        }
        return sb.toString();
    }

    public static void fail(String format, final Object... array) {
        if (array.length != 0) {
            format = String.format(format, array);
        }
        throw new AssertionError((Object)format);
    }

    public static int floorLog2(final float n) {
        int n2;
        for (n2 = 0; n2 < 31 && 1 << n2 <= n; ++n2) {}
        return n2 - 1;
    }

    public static byte[] getBytes(final String s) {
        final byte[] array = new byte[s.length() * 2];
        final char[] charArray = s.toCharArray();
        final int length = charArray.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            final char c = charArray[i];
            final int n2 = n + 1;
            array[n] = (byte)(c & '\u00ff');
            n = n2 + 1;
            array[n2] = (byte)(c >> 8);
            ++i;
        }
        return array;
    }

    public static String getUserAgent(final Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return String.format("%s/%s; %s/%s/%s/%s; %s/%s/%s", packageInfo.packageName, packageInfo.versionName, Build.BRAND, Build.DEVICE, Build.MODEL, Build.ID, Build.VERSION.SDK_INT, Build.VERSION.RELEASE, Build.VERSION.INCREMENTAL);
        }
        catch (PackageManager.NameNotFoundException ex) {
            throw new IllegalStateException("getPackageInfo failed");
        }
    }

    public static boolean handleInterrruptedException(final Throwable t) {
        if (t instanceof InterruptedIOException || t instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            return true;
        }
        return false;
    }

    public static float interpolateAngle(float n, float n2, final float n3) {
        final float n4 = n2 -= n;
        if (n4 < 0.0f) {
            n2 = n4 + 360.0f;
        }
        float n5 = n2;
        if (n2 > 180.0f) {
            n5 = n2 - 360.0f;
        }
        n2 = (n += n5 * n3);
        if (n2 < 0.0f) {
            n = n2 + 360.0f;
        }
        return n;
    }

    public static float interpolateScale(final float n, final float n2, final float n3) {
        return (n2 - n) * n3 + n;
    }

    public static boolean isNullOrEmpty(final String s) {
        return TextUtils.isEmpty((CharSequence) s);
    }

    public static boolean isOpaque(final int n) {
        return n >>> 24 == 255;
    }

    public static String maskDebugInfo(final Object o) {
        String string;
        if (o == null) {
            string = null;
        }
        else {
            string = o.toString();
            final int min = Math.min(string.length(), "********************************".length());
            if (!Utils.IS_DEBUG_BUILD) {
                return "********************************".substring(0, min);
            }
        }
        return string;
    }

    public static int nextPowerOf2(int n) {
        if (n <= 0 || n > 1073741824) {
            throw new IllegalArgumentException("n is invalid: " + n);
        }
        --n;
        n |= n >> 16;
        n |= n >> 8;
        n |= n >> 4;
        n |= n >> 2;
        return (n | n >> 1) + 1;
    }

    public static float parseFloatSafely(final String s, final float n) {
        if (s == null) {
            return n;
        }
        try {
            return Float.parseFloat(s);
        }
        catch (NumberFormatException ex) {
            return n;
        }
    }

    public static int parseIntSafely(final String s, final int n) {
        if (s == null) {
            return n;
        }
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ex) {
            return n;
        }
    }

    public static int prevPowerOf2(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        return Integer.highestOneBit(n);
    }

    public static void swap(final int[] array, final int n, final int n2) {
        final int n3 = array[n];
        array[n] = array[n2];
        array[n2] = n3;
    }

    public static void waitWithoutInterrupt(final Object o) {
        try {
            o.wait();
        }
        catch (InterruptedException ex) {
            Log.w("Utils", "unexpected interrupt: " + o);
        }
    }
}