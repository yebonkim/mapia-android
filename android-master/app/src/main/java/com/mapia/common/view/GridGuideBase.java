package com.mapia.common.view;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.mapia.R;

public class GridGuideBase
{
    private int mColumnCount;
    private Context mContext;
    protected RectF mDrawRect;
    private Paint mInnerLinePaint;
    private boolean mIsOuterLine;
    private Paint mOuterLinePaint;
    private Paint mOuterPaint;
    private float[] mPts;
    private int mRowCount;

    public GridGuideBase(final Context mContext) {
        super();
        this.mContext = mContext;
        (this.mInnerLinePaint = new Paint()).setStrokeWidth(mContext.getResources().getDimension(R.dimen.camera_grid_guide_line_width));
        this.mInnerLinePaint.setColor(-1);
        (this.mOuterLinePaint = new Paint()).setStrokeWidth(mContext.getResources().getDimension(R.dimen.camera_grid_guide_line_width));
        this.mOuterLinePaint.setColor(-1);
        this.mOuterPaint = new Paint();
        this.mOuterLinePaint.setStyle(Paint.Style.STROKE);
        this.mRowCount = 3;
        this.mColumnCount = 3;
        this.initPts();
        this.mDrawRect = new RectF();
        this.mIsOuterLine = false;
        this.mOuterPaint.setColor(this.mContext.getResources().getColor(R.color.crop_image_above_layer));
    }

    private void initPts() {
        final int n = (this.mRowCount - 1) * 4;
        final int n2 = (this.mColumnCount - 1) * 4;
        int n3 = n;
        if (n < 0) {
            n3 = 0;
        }
        int n4;
        if ((n4 = n2) < 0) {
            n4 = 0;
        }
        this.mPts = new float[n3 + n4];
    }

    public void drawGrid(final Canvas canvas) {
        int n = 0;
        for (int i = 0; i < this.mRowCount - 1; ++i) {
            final float[] mPts = this.mPts;
            final int n2 = n + 1;
            mPts[n] = this.mDrawRect.left;
            final float[] mPts2 = this.mPts;
            final int n3 = n2 + 1;
            mPts2[n2] = this.mDrawRect.height() * ((i + 1.0f) / this.mRowCount) + this.mDrawRect.top;
            final float[] mPts3 = this.mPts;
            final int n4 = n3 + 1;
            mPts3[n3] = this.mDrawRect.right;
            final float[] mPts4 = this.mPts;
            n = n4 + 1;
            mPts4[n4] = this.mDrawRect.height() * ((i + 1.0f) / this.mRowCount) + this.mDrawRect.top;
        }
        final int n5 = 0;
        int n6 = n;
        for (int j = n5; j < this.mColumnCount - 1; ++j) {
            final float[] mPts5 = this.mPts;
            final int n7 = n6 + 1;
            mPts5[n6] = this.mDrawRect.width() * ((j + 1.0f) / this.mColumnCount) + this.mDrawRect.left;
            final float[] mPts6 = this.mPts;
            final int n8 = n7 + 1;
            mPts6[n7] = this.mDrawRect.top;
            final float[] mPts7 = this.mPts;
            final int n9 = n8 + 1;
            mPts7[n8] = this.mDrawRect.width() * ((j + 1.0f) / this.mColumnCount) + this.mDrawRect.left;
            final float[] mPts8 = this.mPts;
            n6 = n9 + 1;
            mPts8[n9] = this.mDrawRect.bottom;
        }
        canvas.drawLines(this.mPts, this.mInnerLinePaint);
        if (this.mIsOuterLine) {
            final float n10 = this.mOuterLinePaint.getStrokeWidth() * 0.5f;
            canvas.drawRect(this.mDrawRect.left + n10, this.mDrawRect.top + n10, this.mDrawRect.right - n10, this.mDrawRect.bottom - n10, this.mOuterLinePaint);
            canvas.drawRect(this.mDrawRect.left, 0.0f, this.mDrawRect.right, this.mDrawRect.top, this.mOuterPaint);
            canvas.drawRect(this.mDrawRect.left, this.mDrawRect.bottom, this.mDrawRect.right, (float)canvas.getHeight(), this.mOuterPaint);
        }
    }

    public void setColumnCount(final int mColumnCount) {
        this.mColumnCount = mColumnCount;
        this.initPts();
    }

    public void setInnerLineAlpha(final int alpha) {
        this.mInnerLinePaint.setAlpha(alpha);
    }

    public void setInnerLineColor(final int color) {
        this.mInnerLinePaint.setColor(color);
    }

    public void setInnerLineStrokeWidth(final float strokeWidth) {
        this.mInnerLinePaint.setStrokeWidth(strokeWidth);
    }

    public void setIsOuterLine(final boolean mIsOuterLine) {
        this.mIsOuterLine = mIsOuterLine;
    }

    public void setOuterLineAlpha(final int alpha) {
        this.mOuterLinePaint.setAlpha(alpha);
    }

    public void setOuterLineColor(final int color) {
        this.mOuterLinePaint.setColor(color);
    }

    public void setOuterLineStrokeWidth(final float strokeWidth) {
        this.mOuterLinePaint.setStrokeWidth(strokeWidth);
    }

    public void setRect(final Rect rect) {
        this.setRect(new RectF(rect));
    }

    public void setRect(final RectF rectF) {
        this.mDrawRect = new RectF(rectF);
    }

    public void setRowCount(final int mRowCount) {
        this.mRowCount = mRowCount;
        this.initPts();
    }
}