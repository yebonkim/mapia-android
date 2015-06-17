package com.mapia.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by daehyun on 15. 6. 16..
 */


public abstract class OverlayRenderer implements RenderOverlay.Renderer
{
    private static final String TAG = "CAM OverlayRenderer";
    protected int mBottom;
    protected int mLeft;
    protected RenderOverlay mOverlay;
    protected int mRight;
    protected int mTop;
    protected boolean mVisible;

    @Override
    public void draw(final Canvas canvas) {
        if (this.mVisible) {
            this.onDraw(canvas);
        }
    }

    protected Context getContext() {
        if (this.mOverlay != null) {
            return this.mOverlay.getContext();
        }
        return null;
    }

    public int getHeight() {
        return this.mBottom - this.mTop;
    }

    public int getWidth() {
        return this.mRight - this.mLeft;
    }

    @Override
    public boolean handlesTouch() {
        return false;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    @Override
    public void layout(final int mLeft, final int mTop, final int mRight, final int mBottom) {
        this.mLeft = mLeft;
        this.mRight = mRight;
        this.mTop = mTop;
        this.mBottom = mBottom;
    }

    public abstract void onDraw(final Canvas p0);

    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void setOverlay(final RenderOverlay mOverlay) {
        this.mOverlay = mOverlay;
    }

    public void setVisible(final boolean mVisible) {
        this.mVisible = mVisible;
        this.update();
    }

    protected void update() {
        if (this.mOverlay != null) {
            this.mOverlay.update();
        }
    }
}