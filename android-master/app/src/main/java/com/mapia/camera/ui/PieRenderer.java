package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.mapia.R;


public class PieRenderer extends OverlayRenderer implements FocusIndicator
{
    private static final int DISAPPEAR_TIMEOUT = 200;
    private static final int SCALING_DOWN_TIME = 100;
    private static final int SCALING_UP_TIME = 200;
    private static final int STATE_FINISHING = 2;
    private static final int STATE_FOCUSING = 1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PIE = 8;
    private static final String TAG = "CAM Pie";
    private boolean isCountDown;
    private ScaleAnimation mAnimation;
    private boolean mBlockFocus;
    private int mCenterX;
    private int mCenterY;
    private RectF mCircle;
    private int mCircleSize;
    private RectF mDial;
    private Runnable mDisappear;
    private Animation.AnimationListener mEndAction;
    private int mEndFocusRectRadius;
    private int mFailAlpha;
    private volatile boolean mFocusCancelled;
    private Paint mFocusPaint;
    private int mFocusRadius;
    private int mFocusX;
    private int mFocusY;
    private boolean mFocused;
    private int mInnerOffset;
    private int mOuterStroke;
    private int mStartFocusRectRadius;
    private volatile int mState;
    private int mSuccessAlpha;
    private Resources res;

    public PieRenderer(final Context context) {
        super();
        this.mDisappear = new Disappear();
        this.mEndAction = (Animation.AnimationListener)new EndAction();
        this.init(context);
    }

    private void cancelFocus() {
        this.mFocusCancelled = true;
        this.mOverlay.removeCallbacks(this.mDisappear);
        if (this.mAnimation != null && !this.mAnimation.hasEnded()) {
            this.mAnimation.cancel();
        }
        this.mFocusCancelled = false;
        this.mFocused = false;
        this.mState = 0;
    }

    private void init(final Context context) {
        this.setVisible(false);
        this.res = context.getResources();
        this.mCircleSize = this.res.getDimensionPixelSize(R.dimen.focus_inner_circle_radius);
        (this.mFocusPaint = new Paint()).setAntiAlias(true);
        this.mFocusPaint.setColor(-1);
        this.mFocusPaint.setStyle(Paint.Style.STROKE);
        this.mSuccessAlpha = this.res.getInteger(R.integer.camera_focus_success_alpha);
        this.mFailAlpha = this.res.getInteger(R.integer.camera_focus_fail_alpha);
        this.mCircle = new RectF();
        this.mDial = new RectF();
        this.mInnerOffset = this.res.getDimensionPixelSize(R.dimen.focus_inner_offset);
        this.mOuterStroke = this.res.getDimensionPixelSize(R.dimen.focus_outer_stroke);
        this.mFocusRadius = this.res.getDimensionPixelSize(R.dimen.focus_rect_radius);
        this.mStartFocusRectRadius = this.res.getDimensionPixelSize(R.dimen.focus_rect_radius);
        this.mEndFocusRectRadius = this.res.getDimensionPixelSize(R.dimen.focus_rect_end_radius);
        this.mState = 0;
        this.mBlockFocus = false;
        this.mAnimation = new ScaleAnimation();
    }

    private void setCircle(final int n, final int n2) {
        this.mCircle.set((float)(n - this.mCircleSize), (float)(n2 - this.mCircleSize), (float)(this.mCircleSize + n), (float)(this.mCircleSize + n2));
        this.mDial.set((float)(n - this.mCircleSize + this.mInnerOffset), (float)(n2 - this.mCircleSize + this.mInnerOffset), (float)(this.mCircleSize + n - this.mInnerOffset), (float)(this.mCircleSize + n2 - this.mInnerOffset));
    }

    private void startAnimation(final long duration, final boolean b, final float n, final float n2, final boolean b2) {
        this.setVisible(true);
        this.mAnimation.reset();
        this.mAnimation.setDuration(duration);
        this.mAnimation.setScale(n, n2);
        this.mAnimation.isSetAlpha(b2);
        final ScaleAnimation mAnimation = this.mAnimation;
        Animation.AnimationListener mEndAction;
        if (b) {
            mEndAction = this.mEndAction;
        }
        else {
            mEndAction = null;
        }
        mAnimation.setAnimationListener(mEndAction);
        this.mOverlay.startAnimation((Animation)this.mAnimation);
        this.update();
    }

    @Override
    public void clear() {
        if (this.mState == 8) {
            return;
        }
        this.cancelFocus();
        this.mOverlay.post(this.mDisappear);
    }

    public void drawFocus(final Canvas canvas) {
        if (!this.mBlockFocus && this.mState != 0) {
            this.mFocusPaint.setStrokeWidth((float)this.mOuterStroke);
            if (this.mState != 8) {
                final int color = this.mFocusPaint.getColor();
                if (this.mState == 2) {
                    final Paint mFocusPaint = this.mFocusPaint;
                    int alpha;
                    if (this.mFocused) {
                        alpha = this.mSuccessAlpha;
                    }
                    else {
                        alpha = this.mFailAlpha;
                    }
                    mFocusPaint.setAlpha(alpha);
                }
                if (this.mFocused) {
                    this.mFocusPaint.setColor(this.res.getColor(R.color.focus_color));
                }
                this.mFocusPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(new Rect(this.mFocusX - this.mFocusRadius, this.mFocusY - this.mFocusRadius, this.mFocusX + this.mFocusRadius, this.mFocusY + this.mFocusRadius), this.mFocusPaint);
                this.mFocusPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float)this.mFocusX, (float)this.mFocusY, (float)this.mCircleSize, this.mFocusPaint);
                this.mFocusPaint.setColor(color);
            }
        }
    }

    public int getSize() {
        return this.mCircleSize * 2;
    }

    @Override
    public boolean handlesTouch() {
        return true;
    }

    @Override
    public void layout(final int n, final int n2, final int n3, final int n4) {
        super.layout(n, n2, n3, n4);
        this.mCenterX = (n3 - n) / 2;
        this.mCenterY = (n4 - n2) / 2;
        this.mFocusX = this.mCenterX;
        this.mFocusY = this.mCenterY;
        this.setCircle(this.mFocusX, this.mFocusY);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        final int save = canvas.save();
        if (this.mState != 8 && !this.isCountDown) {
            this.drawFocus(canvas);
        }
        if (this.mState == 2) {
            canvas.restoreToCount(save);
            return;
        }
        canvas.restoreToCount(save);
    }

    public void setBlockFocus(final boolean mBlockFocus) {
        this.mBlockFocus = mBlockFocus;
        if (mBlockFocus) {
            this.clear();
        }
    }

    public void setFocus(final int mFocusX, final int mFocusY) {
        this.mFocusX = mFocusX;
        this.mFocusY = mFocusY;
        this.setCircle(this.mFocusX, this.mFocusY);
    }

    public void setIsCountDown(final boolean isCountDown) {
        this.isCountDown = isCountDown;
    }

    @Override
    public void showFail(final boolean b) {
        if (this.mState == 1) {
            this.startAnimation(100L, b, this.mEndFocusRectRadius, this.mEndFocusRectRadius, false);
            this.mState = 2;
            this.mFocused = false;
        }
    }

    @Override
    public void showStart() {
        this.showStart(false);
    }

    @Override
    public void showStart(final boolean b) {
        if (this.mState == 8) {
            return;
        }
        this.cancelFocus();
        if (b) {
            this.startAnimation(200L, false, this.mStartFocusRectRadius, this.mEndFocusRectRadius, true);
        }
        else {
            this.startAnimation(200L, false, this.mEndFocusRectRadius, this.mEndFocusRectRadius, false);
        }
        this.mState = 1;
    }

    @Override
    public void showSuccess(final boolean b) {
        if (this.mState == 1) {
            this.startAnimation(100L, b, this.mEndFocusRectRadius, this.mEndFocusRectRadius, false);
            this.mState = 2;
            this.mFocused = true;
        }
    }

    private class Disappear implements Runnable
    {
        @Override
        public void run() {
            if (PieRenderer.this.mState == 8) {
                return;
            }
            PieRenderer.this.setVisible(false);
            PieRenderer.this.mFocusX = PieRenderer.this.mCenterX;
            PieRenderer.this.mFocusY = PieRenderer.this.mCenterY;
            PieRenderer.this.mState = 0;
            PieRenderer.this.setCircle(PieRenderer.this.mFocusX, PieRenderer.this.mFocusY);
            PieRenderer.this.mFocused = false;
        }
    }

    private class EndAction implements Animation.AnimationListener
    {
        public void onAnimationEnd(final Animation animation) {
            if (!PieRenderer.this.mFocusCancelled) {
                PieRenderer.this.mOverlay.postDelayed(PieRenderer.this.mDisappear, 200L);
            }
        }

        public void onAnimationRepeat(final Animation animation) {
        }

        public void onAnimationStart(final Animation animation) {
        }
    }

    private class ScaleAnimation extends Animation
    {
        private boolean isSetAlpha;
        private float mFrom;
        private int mFromAlpha;
        private float mTo;
        private int mToAlpha;

        public ScaleAnimation() {
            super();
            this.mFrom = 1.0f;
            this.mTo = 1.0f;
            this.setFillAfter(true);
            this.mFromAlpha = 0;
            this.mToAlpha = PieRenderer.this.mSuccessAlpha;
        }

        protected void applyTransformation(final float n, final Transformation transformation) {
            PieRenderer.this.mFocusRadius = (int)(this.mFrom + (this.mTo - this.mFrom) * n);
            if (this.isSetAlpha) {
                PieRenderer.this.mFocusPaint.setAlpha((int)(this.mFromAlpha + (this.mToAlpha - this.mFromAlpha) * n));
            }
        }

        public void isSetAlpha(final boolean isSetAlpha) {
            this.isSetAlpha = isSetAlpha;
        }

        public void setScale(final float mFrom, final float mTo) {
            this.mFrom = mFrom;
            this.mTo = mTo;
        }
    }
}