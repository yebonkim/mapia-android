package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.ScaleGestureDetector;

import com.mapia.R;

public class ZoomRenderer extends OverlayRenderer implements ScaleGestureDetector.OnScaleGestureListener
{
    private static final String TAG = "CAM_Zoom";
    private boolean isStartOnScale;
    private int mCenterX;
    private int mCenterY;
    private int mCircleSize;
    private ScaleGestureDetector mDetector;
    private int mInnerStroke;
    private OnZoomChangedListener mListener;
    private float mMaxCircle;
    private int mMaxZoom;
    private float mMinCircle;
    private int mMinZoom;
    private int mOuterStroke;
    private Paint mPaint;
    private Rect mTextBounds;
    private Paint mTextPaint;
    private int mZoomFraction;
    private int mZoomSig;

    public ZoomRenderer(final Context context) {
        super();
        final Resources resources = context.getResources();
        (this.mPaint = new Paint()).setAntiAlias(true);
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Paint.Style.STROKE);
        (this.mTextPaint = new Paint(this.mPaint)).setStyle(Paint.Style.FILL);
        this.mTextPaint.setTextSize((float)resources.getDimensionPixelSize(R.dimen.zoom_font_size));
        this.mTextPaint.setTextAlign(Paint.Align.LEFT);
        this.mTextPaint.setAlpha(192);
        this.mInnerStroke = resources.getDimensionPixelSize(R.dimen.focus_inner_stroke);
        this.mOuterStroke = resources.getDimensionPixelSize(R.dimen.focus_outer_stroke);
        this.mDetector = new ScaleGestureDetector(context, (ScaleGestureDetector.OnScaleGestureListener)this);
        this.mMinCircle = resources.getDimensionPixelSize(R.dimen.zoom_ring_min);
        this.mTextBounds = new Rect();
        this.setVisible(false);
    }

    public boolean isScaling() {
        return this.mDetector.isInProgress();
    }

    @Override
    public void layout(final int n, final int n2, final int n3, final int n4) {
        super.layout(n, n2, n3, n4);
        this.mCenterX = (n3 - n) / 2;
        this.mCenterY = (n4 - n2) / 2;
        this.mMaxCircle = Math.min(this.getWidth(), this.getHeight());
        this.mMaxCircle = (this.mMaxCircle - this.mMinCircle) / 2.0f;
    }

    @Override
    public void onDraw(final Canvas canvas) {
        this.mPaint.setStrokeWidth((float)this.mInnerStroke);
        this.mPaint.setStrokeWidth((float)this.mOuterStroke);
        canvas.drawCircle((float)this.mCenterX, (float)this.mCenterY, (float)this.mCircleSize, this.mPaint);
        final String string = this.mZoomSig + "." + this.mZoomFraction + "x";
        this.mTextPaint.getTextBounds(string, 0, string.length(), this.mTextBounds);
        canvas.drawText(string, (float)(this.mCenterX - this.mTextBounds.centerX()), (float)(this.mCenterY - this.mTextBounds.centerY()), this.mTextPaint);
    }

    public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
        this.isStartOnScale = true;
        final float scaleFactor = scaleGestureDetector.getScaleFactor();
        final float min = Math.min(this.mMaxCircle, Math.max(this.mMinCircle, (int)(this.mCircleSize * scaleFactor * scaleFactor)));
        if (this.mListener != null && (int)min != this.mCircleSize) {
            this.mCircleSize = (int)min;
            this.mListener.onZoomValueChanged(this.mMinZoom + (int)((this.mCircleSize - this.mMinCircle) * (this.mMaxZoom - this.mMinZoom) / (this.mMaxCircle - this.mMinCircle)));
        }
        return true;
    }

    public boolean onScaleBegin(final ScaleGestureDetector scaleGestureDetector) {
        this.isStartOnScale = false;
        this.setVisible(true);
        if (this.mListener != null) {
//            AceUtils.nClick(NClicks.CAMERA_ZOOM);
            this.mListener.onZoomStart();
        }
        this.update();
        return true;
    }

    public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
        if (this.isStartOnScale) {
            this.setVisible(false);
            if (this.mListener != null) {
                this.mListener.onZoomEnd();
            }
        }
    }

    public void setOnZoomChangeListener(final OnZoomChangedListener mListener) {
        this.mListener = mListener;
    }

    public void setZoom(final int n) {
        this.mCircleSize = (int)(this.mMinCircle + n * (this.mMaxCircle - this.mMinCircle) / (this.mMaxZoom - this.mMinZoom));
    }

    public void setZoomMax(final int mMaxZoom) {
        this.mMaxZoom = mMaxZoom;
        this.mMinZoom = 0;
    }

    public void setZoomValue(int n) {
        n /= 10;
        this.mZoomSig = n / 10;
        this.mZoomFraction = n % 10;
    }

    public interface OnZoomChangedListener
    {
        void onZoomEnd();

        void onZoomStart();

        void onZoomValueChanged(int p0);
    }
}