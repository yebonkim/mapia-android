package com.mapia.camera.util;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.util.FloatMath;
import android.view.MotionEvent;


public final class MotionEventHelper
{
    private static MotionEvent.PointerCoords[] getPointerCoords(final MotionEvent motionEvent) {
        final int pointerCount = motionEvent.getPointerCount();
        final MotionEvent.PointerCoords[] array = new MotionEvent.PointerCoords[pointerCount];
        for (int i = 0; i < pointerCount; ++i) {
            motionEvent.getPointerCoords(i, array[i] = new MotionEvent.PointerCoords());
        }
        return array;
    }

    private static int[] getPointerIds(final MotionEvent motionEvent) {
        final int pointerCount = motionEvent.getPointerCount();
        final int[] array = new int[pointerCount];
        for (int i = 0; i < pointerCount; ++i) {
            array[i] = motionEvent.getPointerId(i);
        }
        return array;
    }

    private static float transformAngle(final Matrix matrix, float n) {
        final float[] array = { FloatMath.sin(n), -FloatMath.cos(n) };
        matrix.mapVectors(array);
        final float n2 = (float)Math.atan2(array[0], -array[1]);
        if (n2 < -1.5707963267948966) {
            n = (float)(n2 + 3.141592653589793);
        }
        else {
            n = n2;
            if (n2 > 1.5707963267948966) {
                return (float)(n2 - 3.141592653589793);
            }
        }
        return n;
    }

    public static MotionEvent transformEvent(final MotionEvent motionEvent, final Matrix matrix) {
        if (true) {
            return transformEventNew(motionEvent, matrix);
        }
        return transformEventOld(motionEvent, matrix);
    }

    @TargetApi(11)
    private static MotionEvent transformEventNew(MotionEvent obtain, final Matrix matrix) {
        obtain = MotionEvent.obtain(obtain);
        obtain.transform(matrix);
        return obtain;
    }

    private static MotionEvent transformEventOld(final MotionEvent motionEvent, final Matrix matrix) {
        final long downTime = motionEvent.getDownTime();
        final long eventTime = motionEvent.getEventTime();
        final int action = motionEvent.getAction();
        final int pointerCount = motionEvent.getPointerCount();
        final int[] pointerIds = getPointerIds(motionEvent);
        final MotionEvent.PointerCoords[] pointerCoords = getPointerCoords(motionEvent);
        final int metaState = motionEvent.getMetaState();
        final float xPrecision = motionEvent.getXPrecision();
        final float yPrecision = motionEvent.getYPrecision();
        final int deviceId = motionEvent.getDeviceId();
        final int edgeFlags = motionEvent.getEdgeFlags();
        final int source = motionEvent.getSource();
        final int flags = motionEvent.getFlags();
        final float[] array = new float[pointerCoords.length * 2];
        for (int i = 0; i < pointerCount; ++i) {
            array[i * 2] = pointerCoords[i].x;
            array[i * 2 + 1] = pointerCoords[i].y;
        }
        matrix.mapPoints(array);
        for (int j = 0; j < pointerCount; ++j) {
            pointerCoords[j].x = array[j * 2];
            pointerCoords[j].y = array[j * 2 + 1];
            pointerCoords[j].orientation = transformAngle(matrix, pointerCoords[j].orientation);
        }
        return MotionEvent.obtain(downTime, eventTime, action, pointerCount, pointerIds, pointerCoords, metaState, xPrecision, yPrecision, deviceId, edgeFlags, source, flags);
    }
}