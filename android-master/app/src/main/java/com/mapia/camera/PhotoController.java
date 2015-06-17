package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.view.SurfaceHolder;
import android.view.View;


public interface PhotoController extends ShutterButton.OnShutterButtonListener
{
    public static final int FOCUSING = 2;
    public static final int IDLE = 1;
    public static final int PREVIEW_STOPPED = 0;
    public static final int SNAPSHOT_IN_PROGRESS = 3;
    public static final int SWITCHING_CAMERA = 4;

    void cancelAutoFocus();

    int getCameraState();

    boolean isCameraIdle();

    boolean isImageCaptureIntent();

    void onCaptureCancelled();

    void onCaptureDone();

    void onCaptureRetake();

    void onCountDownFinished();

    void onScreenSizeChanged(int p0, int p1, int p2, int p3);

    void onSingleTapUp(View p0, int p1, int p2);

    void onSurfaceCreated(SurfaceHolder p0);

    int onZoomChanged(int p0);

    void stopPreview();
}