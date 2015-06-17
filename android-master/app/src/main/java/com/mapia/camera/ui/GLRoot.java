package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.graphics.Matrix;

import com.mapia.camera.anim.CanvasAnimation;
import com.mapia.camera.glrenderer.GLCanvas;

public interface GLRoot
{
    void addOnGLIdleListener(OnGLIdleListener p0);

    void freeze();

    int getCompensation();

    Matrix getCompensationMatrix();

    Context getContext();

    int getDisplayRotation();

    void lockRenderThread();

    void registerLaunchedAnimation(CanvasAnimation p0);

    void requestLayoutContentPane();

    void requestRender();

    void requestRenderForced();

    void setContentPane(GLView p0);

    void setLightsOutMode(boolean p0);

    void setOrientationSource(OrientationSource p0);

    void unfreeze();

    void unlockRenderThread();

    public interface OnGLIdleListener
    {
        boolean onGLIdle(GLCanvas p0, boolean p1);
    }
}