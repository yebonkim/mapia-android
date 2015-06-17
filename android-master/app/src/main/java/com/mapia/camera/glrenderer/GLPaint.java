package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import junit.framework.Assert;


public class GLPaint
{
    private int mColor;
    private float mLineWidth;

    public GLPaint() {
        super();
        this.mLineWidth = 1.0f;
        this.mColor = 0;
    }

    public int getColor() {
        return this.mColor;
    }

    public float getLineWidth() {
        return this.mLineWidth;
    }

    public void setColor(final int mColor) {
        this.mColor = mColor;
    }

    public void setLineWidth(final float mLineWidth) {
        Assert.assertTrue(mLineWidth >= 0.0f);
        this.mLineWidth = mLineWidth;
    }
}