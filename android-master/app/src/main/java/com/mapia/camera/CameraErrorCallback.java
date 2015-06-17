package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.hardware.Camera;
import android.util.Log;


public class CameraErrorCallback implements Camera.ErrorCallback
{
    private static final String TAG = "CameraErrorCallback";

    public void onError(final int n, final Camera camera) {
        Log.e("CameraErrorCallback", "Got camera error callback. error=" + n);
        if (n == 100) {
            throw new RuntimeException("Media server died.");
        }
    }
}
