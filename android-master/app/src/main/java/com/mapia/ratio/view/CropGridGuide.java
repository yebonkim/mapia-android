package com.mapia.ratio.view;

/**
 * Created by daehyun on 15. 6. 17..
 */

import android.content.Context;
import android.graphics.RectF;

import com.mapia.common.view.GridGuideBase;


public class CropGridGuide extends GridGuideBase
{
    private RectF mImageViewRect;

    public CropGridGuide(final Context context) {
        super(context);
    }

    public RectF getDrawRect() {
        return this.mDrawRect;
    }

    public boolean gridContains(final float n, final float n2) {
        return this.mDrawRect.contains((float)(int)n, (float)(int)n2);
    }

    public void moveBy(final float n, final float n2) {
        this.mDrawRect.offset(n, n2);
        this.mDrawRect.offset(Math.max(0.0f, this.mImageViewRect.left - this.mDrawRect.left), Math.max(0.0f, this.mImageViewRect.top - this.mDrawRect.top));
        this.mDrawRect.offset(Math.min(0.0f, this.mImageViewRect.right - this.mDrawRect.right), Math.min(0.0f, this.mImageViewRect.bottom - this.mDrawRect.bottom));
    }

    public void setImageViewRect(final RectF rectF) {
        this.mImageViewRect = new RectF(rectF);
    }
}