package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.view.View;


public interface VideoController extends ShutterButton.OnShutterButtonListener
{
    boolean isInReviewMode();

    boolean isVideoCaptureIntent();

    void onSingleTapUp(View p0, int p1, int p2);

    int onZoomChanged(int p0);

    void stopPreview();
}