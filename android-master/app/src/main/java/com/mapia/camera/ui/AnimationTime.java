package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.os.SystemClock;


public class AnimationTime
{
    private static volatile long sTime;

    public static long get() {
        return AnimationTime.sTime;
    }

    public static long startTime() {
        return AnimationTime.sTime = SystemClock.uptimeMillis();
    }

    public static void update() {
        AnimationTime.sTime = SystemClock.uptimeMillis();
    }
}