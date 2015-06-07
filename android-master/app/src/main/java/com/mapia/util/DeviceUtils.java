package com.mapia.util;

import android.util.DisplayMetrics;

import com.mapia.MainApplication;

public class DeviceUtils
{
    private static final int HDPI = 3;
    private static final int LDPI = 1;
    private static final int MDPI = 2;
    private static final int XHDPI = 4;
    private static final int XXHDPI = 5;
    private static final int XXXHDPI = 6;
    public static int deviceWidthDivideByThree;
    private static int dpiType;
    private static DisplayMetrics metrics;

    static {
        DeviceUtils.dpiType = 0;
        DeviceUtils.deviceWidthDivideByThree = 0;
        DeviceUtils.metrics = MainApplication.getInstance().getResources().getDisplayMetrics();
    }

    public static int getDeviceDencityDpi() {
        return (int)(DeviceUtils.metrics.density * 160.0f);
    }

    public static int getDeviceHeight() {
        return DeviceUtils.metrics.heightPixels;
    }

    public static int getDeviceWidth() {
        return DeviceUtils.metrics.widthPixels;
    }

    public static int getDeviceWidthDivideByThree() {
        if (DeviceUtils.deviceWidthDivideByThree == 0) {
            DeviceUtils.deviceWidthDivideByThree = getDeviceWidth() / 3;
        }
        return DeviceUtils.deviceWidthDivideByThree;
    }

    public static int getDeviceWidthDp() {
        return (int)(DeviceUtils.metrics.widthPixels / DeviceUtils.metrics.density);
    }

    public static int getDiviceDpiType() {
        if (DeviceUtils.dpiType > 0) {
            return DeviceUtils.dpiType;
        }
        final int deviceDencityDpi = getDeviceDencityDpi();
        if (deviceDencityDpi > 480) {
            DeviceUtils.dpiType = 6;
        }
        else if (deviceDencityDpi > 320) {
            DeviceUtils.dpiType = 5;
        }
        else if (deviceDencityDpi > 240) {
            DeviceUtils.dpiType = 4;
        }
        else if (deviceDencityDpi > 160) {
            DeviceUtils.dpiType = 3;
        }
        else if (deviceDencityDpi > 120) {
            DeviceUtils.dpiType = 2;
        }
        else {
            DeviceUtils.dpiType = 1;
        }
        return DeviceUtils.dpiType;
    }
}