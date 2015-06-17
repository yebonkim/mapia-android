package com.mapia.camera.orientation;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.view.WindowManager;

import com.mapia.MainApplication;

public class OrientationEvent
{
    public static final int LANDSCAPE_270 = 270;
    public static final int LANDSCAPE_90 = 90;
    public static final int PORTRAIT_0 = 0;
    public static final int PORTRAIT_180 = 180;
    private static final int RANGE_HALF_ANGLE = 30;
    private static final int WINDOW_DEFAULT_ROTATION;
    private static int currentOrientation;

    static {
        WINDOW_DEFAULT_ROTATION = getAngleFromRotation(((WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation());
        OrientationEvent.currentOrientation = 0;
    }

    private static int getAngleFromRotation(final int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 1: {
                return 90;
            }
            case 2: {
                return 180;
            }
            case 3: {
                return 270;
            }
        }
    }

    public static int getCurrentOrientation() {
        return OrientationEvent.currentOrientation;
    }

    public static void setCurrentOrientation(int n) {
        n = (OrientationEvent.WINDOW_DEFAULT_ROTATION + n) % 360;
        if (330 <= n || 30 >= n) {
            OrientationEvent.currentOrientation = 0;
        }
        else {
            if (60 <= n && 120 >= n) {
                OrientationEvent.currentOrientation = 90;
                return;
            }
            if (150 <= n && 210 >= n) {
                OrientationEvent.currentOrientation = 180;
                return;
            }
            if (240 <= n && 300 >= n) {
                OrientationEvent.currentOrientation = 270;
            }
        }
    }
}