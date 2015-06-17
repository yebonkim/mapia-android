package com.mapia.ratio.view;

/**
 * Created by daehyun on 15. 6. 17..
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class CropImageView extends ImageViewTouch
{
    private CropGridGuide cropGridGuide;
    private Rect currentRect;
    private boolean isChangingScale;
    private OnSizeChanged onSizeChanged;

    public CropImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.onSizeChanged = (OnSizeChanged)new BaseOnSizeChanged();
        this.init();
    }

    public CropImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.onSizeChanged = (OnSizeChanged)new BaseOnSizeChanged();
        this.init();
    }

    private boolean ensureVisible(final RectF rectF) {
        boolean b = false;
        int max = Math.max(0, this.getLeft() - (int)rectF.left);
        final int min = Math.min(0, this.getRight() - (int)rectF.right);
        int max2 = Math.max(0, this.getTop() - (int)rectF.top);
        final int min2 = Math.min(0, this.getBottom() - (int)rectF.bottom);
        if (max == 0) {
            max = min;
        }
        if (max2 == 0) {
            max2 = min2;
        }
        if (max != 0 || max2 != 0) {
            this.panBy(max, max2);
            if (rectF.left < 0.0f || rectF.right > this.getWidth()) {
                rectF.left = 0.0f;
                rectF.right = this.currentRect.width();
            }
            if (rectF.top < 0.0f) {
                rectF.top = 0.0f;
                rectF.bottom = this.currentRect.height();
            }
            else if (rectF.bottom > this.getHeight()) {
                rectF.top = this.getHeight() - this.currentRect.height();
                rectF.bottom = this.getHeight();
            }
            b = true;
        }
        return b;
    }

    private void init() {
        this.cropGridGuide = new CropGridGuide(this.getContext());
    }

    public CropGridGuide getCropGridGuide() {
        return this.cropGridGuide;
    }

    @Override
    protected GestureDetector.OnGestureListener getGestureListener() {
        return (GestureDetector.OnGestureListener)new CropGestureListener();
    }

    @Override
    protected ScaleGestureDetector.OnScaleGestureListener getScaleListener() {
        return (ScaleGestureDetector.OnScaleGestureListener)new CropScaleListener();
    }

    public boolean isChangingScale() {
        return this.isChangingScale;
    }

    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.cropGridGuide.drawGrid(canvas);
    }

    @Override
    protected void onDrawableChanged(final Drawable drawable) {
        super.onDrawableChanged(drawable);
        this.cropGridGuide.setImageViewRect(this.getBitmapRect());
    }

    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        this.onSizeChanged.onSizeChanged();
    }

    @Override
    protected void postScale(final float n, final float n2, final float n3) {
        super.postScale(n, n2, n3);
        final RectF bitmapRect = this.getBitmapRect();
        final RectF drawRect = this.cropGridGuide.getDrawRect();
        this.cropGridGuide.setImageViewRect(bitmapRect);
        if (bitmapRect.top > drawRect.top || bitmapRect.bottom < drawRect.bottom) {
            drawRect.set(this.currentRect);
        }
    }

    public void setCurrentRect(final Rect rect) {
        this.currentRect = new Rect(rect);
        this.cropGridGuide.setRect(rect);
    }

    public void setOnSizeChanged(final OnSizeChanged onSizeChanged) {
        this.onSizeChanged = onSizeChanged;
    }

    private class BaseOnSizeChanged implements OnSizeChanged
    {
        @Override
        public void onSizeChanged() {
        }
    }

    public class CropGestureListener extends ImageViewTouch.GestureListener
    {
        private boolean isGridTouch;

        public CropGestureListener() {
            super();
            this.isGridTouch = false;
        }

        @Override
        public boolean onDown(final MotionEvent motionEvent) {
            if (CropImageView.this.cropGridGuide.gridContains(motionEvent.getX(), motionEvent.getY())) {
                this.isGridTouch = true;
            }
            else {
                this.isGridTouch = false;
            }
            return super.onDown(motionEvent);
        }

        @Override
        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            final float x = motionEvent2.getX();
            final float x2 = motionEvent.getX();
            final float y = motionEvent2.getY();
            final float y2 = motionEvent.getY();
            if (!this.isGridTouch && (Math.abs(n) > 800.0f || Math.abs(n2) > 800.0f)) {
                CropImageView.this.mUserScaled = true;
                CropImageView.this.scrollBy((x - x2) / 2.0f, (y - y2) / 2.0f, 300L);
                CropImageView.this.invalidate();
                return true;
            }
            return false;
        }

        @Override
        public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            if (CropImageView.this.mScrollEnabled && motionEvent != null && motionEvent2 != null && motionEvent.getPointerCount() <= 1 && motionEvent2.getPointerCount() <= 1 && !CropImageView.this.mScaleDetector.isInProgress()) {
                if (this.isGridTouch) {
                    CropImageView.this.cropGridGuide.moveBy(-n, -n2);
                    CropImageView.this.ensureVisible(CropImageView.this.cropGridGuide.getDrawRect());
                }
                else {
                    if (CropImageView.this.getScale() == 1.0f) {
                        return false;
                    }
                    CropImageView.this.mUserScaled = true;
                    CropImageView.this.scrollBy(-n, -n2);
                }
                CropImageView.this.invalidate();
                return true;
            }
            return false;
        }
    }

    public class CropScaleListener extends ImageViewTouch.ScaleListener
    {
        @Override
        public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
            return super.onScale(scaleGestureDetector);
        }

        public boolean onScaleBegin(final ScaleGestureDetector scaleGestureDetector) {
            CropImageView.this.isChangingScale = true;
            return super.onScaleBegin(scaleGestureDetector);
        }

        public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
            CropImageView.this.isChangingScale = false;
            super.onScaleEnd(scaleGestureDetector);
        }
    }

    public interface OnSizeChanged
    {
        void onSizeChanged();
    }
}
