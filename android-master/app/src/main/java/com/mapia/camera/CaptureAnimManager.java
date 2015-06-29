package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.mapia.R;
import com.mapia.camera.glrenderer.GLCanvas;
import com.mapia.camera.glrenderer.RawTexture;

public class CaptureAnimManager
{
    private static final int ANIM_BOTH = 0;
    private static final int ANIM_FLASH = 1;
    private static final int ANIM_HOLD2 = 3;
    private static final int ANIM_SLIDE = 2;
    private static final int ANIM_SLIDE2 = 4;
    private static final String TAG = "CAM_Capture";
    private static final int TIME_FLASH = 200;
    private static final int TIME_HOLD = 400;
    private static final int TIME_HOLD2 = 3300;
    private static final int TIME_SLIDE = 800;
    private static final int TIME_SLIDE2 = 4100;
    private volatile int mAnimOrientation;
    private long mAnimStartTime;
    private int mAnimType;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mHoldH;
    private int mHoldW;
    private int mHoldX;
    private int mHoldY;
    private int mMarginRight;
    private int mMarginTop;
    private int mOffset;
    private Resources mResources;
    private int mShadowSize;
    private int mSize;
    private final Interpolator mSlideInterpolator;
    private float mX;
    private float mY;
    private float mDelta;


    public CaptureAnimManager(final Context context) {
        super();
        this.mSlideInterpolator = (Interpolator)new DecelerateInterpolator();
        this.mResources = context.getResources();
    }

    public static int getAnimationDuration() {
        return 4100;
    }

    private static float interpolate(final float n, final float n2, final float n3) {
        return (n2 - n) * n3 + n;
    }

    private void setAnimationGeometry(final int n, final int n2, final int mDrawWidth, final int mDrawHeight) {
        this.mMarginRight = this.mResources.getDimensionPixelSize(R.dimen.capture_margin_right);
        this.mMarginTop = this.mResources.getDimensionPixelSize(R.dimen.capture_margin_top);
        this.mSize = this.mResources.getDimensionPixelSize(R.dimen.capture_size);
        this.mShadowSize = this.mResources.getDimensionPixelSize(R.dimen.capture_border);
        this.mOffset = this.mMarginRight + this.mSize;
        this.mDrawWidth = mDrawWidth;
        this.mDrawHeight = mDrawHeight;
        this.mX = n;
        this.mY = n2;
        this.mHoldW = this.mSize;
        this.mHoldH = this.mSize;
        this.mHoldX = n + mDrawWidth - this.mMarginRight - this.mSize;
        this.mHoldY = this.mMarginTop + n2;
    }

    public void animateFlash() {
        this.mAnimType = 1;
    }

    public void animateFlashAndSlide() {
        this.mAnimType = 0;
    }

    public void animateSlide() {
        if (this.mAnimType != 1) {
            return;
        }
        this.mAnimType = 2;
        this.mAnimStartTime = SystemClock.uptimeMillis();
    }

    public boolean drawAnimation(final GLCanvas glCanvas, final CameraScreenNail cameraScreenNail, final RawTexture rawTexture, int n, final int n2, final int n3, final int n4) {
        this.setAnimationGeometry(n, n2, n3, n4);
        final long n5 = SystemClock.uptimeMillis() - this.mAnimStartTime;
        if (this.mAnimType == 2 && n5 > 3700L) {
            return false;
        }
        if (this.mAnimType == 0 && n5 > 4100L) {
            return false;
        }
        n = this.mAnimType;
        long n6 = n5;
        if (this.mAnimType == 2) {
            n6 = n5 + 400L;
        }
        long n7 = 0L;
            if (this.mAnimType != 2) {
                n7 = n6;
                if (this.mAnimType == 0) {
                    if (n6 < 400L) {
                        n = 1;
                        n7 = n6;
                    }
                    else if (n6 < 800L) {
                        n = 2;
                        n7 = n6 - 400L;
                    }
                    else if (n6 < 3300L) {
                        n = 3;
                        n7 = n6 - 800L;
                    }
                    else {
                        n = 4;
                        n7 = n6 - 3300L;
                    }
                }
            }


        if (n == 1) {
            rawTexture.draw(glCanvas, (int)this.mX, (int)this.mY, this.mDrawWidth, this.mDrawHeight);
            if (n7 < 200L) {
                n = Color.argb((int) (255.0f * (0.3f - 0.3f * n7 / 200.0f)), 255, 255, 255);
                glCanvas.fillRect(this.mX, this.mY, this.mDrawWidth, this.mDrawHeight, n);
            }
        }

//        else if (n == ANIM_FLASH) {
//            rawTexture.draw(glCanvas, (int) mX, (int) mY, mDrawWidth, mDrawHeight);
//            if (n5 < TIME_FLASH) {
//                float f = 0.3f - 0.3f * n5 / TIME_FLASH;
//                int color = Color.argb((int) (255 * f), 255, 255, 255);
//                glCanvas.fillRect(mX, mY, mDrawWidth, mDrawHeight, color);
//            }
//        }
//    else if (n == ANIM_SLIDE) {
//            float fraction = (float) (n5) / TIME_SLIDE;
//            float x = mX;
//            float y = mY;
//            if (mAnimOrientation == 0 || mAnimOrientation == 180) {
//                x = x + mDelta * mSlideInterpolator.getInterpolation(fraction);
//            } else {
//                y = y + mDelta * mSlideInterpolator.getInterpolation(fraction);
//            }
//             float alpha = glCanvas.getAlpha();
//             glCanvas.setAlpha(fraction);
//            cameraScreenNail.directDraw(glCanvas, (int) mX, (int) mY,
//                    mDrawWidth, mDrawHeight);
//             glCanvas.setAlpha(alpha);
//
//            rawTexture.draw(glCanvas, (int) x, (int) y, mDrawWidth, mDrawHeight);
//        }
        else if (n == 2) {
            final float interpolation = this.mSlideInterpolator.getInterpolation(n7 / 400.0f);
            final float mx = this.mX;
            final float my = this.mY;
            final float interpolate = interpolate(this.mX, this.mHoldX, interpolation);
            final float interpolate2 = interpolate(this.mY, this.mHoldY, interpolation);
            final float interpolate3 = interpolate(this.mDrawWidth, this.mHoldW, interpolation);
            final float interpolate4 = interpolate(this.mDrawHeight, this.mHoldH, interpolation);
            cameraScreenNail.directDraw(glCanvas, (int)this.mX, (int)this.mY, this.mDrawWidth, this.mDrawHeight);
            rawTexture.draw(glCanvas, (int)interpolate, (int)interpolate2, (int)interpolate3, (int)interpolate4);
        }
        else if (n == 3) {
            cameraScreenNail.directDraw(glCanvas, (int)this.mX, (int)this.mY, this.mDrawWidth, this.mDrawHeight);
            rawTexture.draw(glCanvas, this.mHoldX, this.mHoldY, this.mHoldW, this.mHoldH);
        }
        else if (n == 4) {
            final float n8 = n7 / 800.0f;
            float n9 = this.mHoldX;
            float n10 = this.mHoldY;
            final float n11 = this.mOffset * n8;
            switch (this.mAnimOrientation) {
                case 0: {
                    n9 = this.mHoldX + n11;
                    break;
                }
                case 180: {
                    n9 = this.mHoldX - n11;
                    break;
                }
                case 90: {
                    n10 = this.mHoldY - n11;
                    break;
                }
                case 270: {
                    n10 = this.mHoldY + n11;
                    break;
                }
            }
            cameraScreenNail.directDraw(glCanvas, (int)this.mX, (int)this.mY, this.mDrawWidth, this.mDrawHeight);
            rawTexture.draw(glCanvas, (int)n9, (int)n10, this.mHoldW, this.mHoldH);
        }
        return true;
    }


    public boolean drawAnimation(GLCanvas canvas, CameraScreenNail preview,
                                 RawTexture review) {
        long timeDiff = SystemClock.uptimeMillis() - mAnimStartTime;
        // Check if the animation is over
        if (mAnimType == ANIM_SLIDE && timeDiff > TIME_SLIDE) return false;
        if (mAnimType == ANIM_BOTH && timeDiff > TIME_HOLD + TIME_SLIDE) return false;

        int animStep = mAnimType;
        if (mAnimType == ANIM_BOTH) {
            animStep = (timeDiff < TIME_HOLD) ? ANIM_FLASH : ANIM_SLIDE;
            if (animStep == ANIM_SLIDE) {
                timeDiff -= TIME_HOLD;
            }
        }

        if (animStep == ANIM_FLASH) {
            review.draw(canvas, (int) mX, (int) mY, mDrawWidth, mDrawHeight);
            if (timeDiff < TIME_FLASH) {
                float f = 0.3f - 0.3f * timeDiff / TIME_FLASH;
                int color = Color.argb((int) (255 * f), 255, 255, 255);
                canvas.fillRect(mX, mY, mDrawWidth, mDrawHeight, color);
            }
        } else if (animStep == ANIM_SLIDE) {
            float fraction = (float) (timeDiff) / TIME_SLIDE;
            float x = mX;
            float y = mY;
            if (mAnimOrientation == 0 || mAnimOrientation == 180) {
                x = x + mDelta * mSlideInterpolator.getInterpolation(fraction);
            } else {
                y = y + mDelta * mSlideInterpolator.getInterpolation(fraction);
            }
            // float alpha = canvas.getAlpha();
            // canvas.setAlpha(fraction);
            preview.directDraw(canvas, (int) mX, (int) mY,
                    mDrawWidth, mDrawHeight);
            // canvas.setAlpha(alpha);

            review.draw(canvas, (int) x, (int) y, mDrawWidth, mDrawHeight);
        } else {
            return false;
        }
        return true;
    }


    public void setOrientation(final int n) {
        this.mAnimOrientation = (360 - n) % 360;
    }

    public void startAnimation() {
        this.mAnimStartTime = SystemClock.uptimeMillis();
    }

    public void startAnimation(int x, int y, int w, int h) {
        mAnimStartTime = SystemClock.uptimeMillis();
        // Set the views to the initial positions.
        mDrawWidth = w;
        mDrawHeight = h;
        mX = x;
        mY = y;
        switch (mAnimOrientation) {
            case 0:  // Preview is on the left.
                mDelta = w;
                break;
            case 90:  // Preview is below.
                mDelta = -h;
                break;
            case 180:  // Preview on the right.
                mDelta = -w;
                break;
            case 270:  // Preview is above.
                mDelta = h;
                break;
        }
    }
}