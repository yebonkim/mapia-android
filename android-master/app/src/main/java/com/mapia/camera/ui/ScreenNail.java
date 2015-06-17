package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.RectF;

import com.mapia.camera.glrenderer.GLCanvas;


public interface ScreenNail
{
    void draw(GLCanvas p0, int p1, int p2, int p3, int p4);

    void draw(GLCanvas p0, RectF p1, RectF p2);

    int getHeight();

    int getWidth();

    void noDraw();

    void recycle();
}