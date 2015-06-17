package com.mapia.camera.anim;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.view.animation.Interpolator;

import com.mapia.camera.common.Utils;

public abstract class Animation
{
    private static final long ANIMATION_START = -1L;
    private static final long NO_ANIMATION = -2L;
    private int mDuration;
    private Interpolator mInterpolator;
    private long mStartTime;

    public Animation() {
        super();
        this.mStartTime = -2L;
    }

    public boolean calculate(final long mStartTime) {
        if (this.mStartTime != -2L) {
            if (this.mStartTime == -1L) {
                this.mStartTime = mStartTime;
            }
            final int n = (int)(mStartTime - this.mStartTime);
            final float clamp = Utils.clamp(n / this.mDuration, 0.0f, 1.0f);
            final Interpolator mInterpolator = this.mInterpolator;
            float interpolation = clamp;
            if (mInterpolator != null) {
                interpolation = mInterpolator.getInterpolation(clamp);
            }
            this.onCalculate(interpolation);
            if (n >= this.mDuration) {
                this.mStartTime = -2L;
            }
            if (this.mStartTime != -2L) {
                return true;
            }
        }
        return false;
    }

    public void forceStop() {
        this.mStartTime = -2L;
    }

    public boolean isActive() {
        return this.mStartTime != -2L;
    }

    protected abstract void onCalculate(final float p0);

    public void setDuration(final int mDuration) {
        this.mDuration = mDuration;
    }

    public void setInterpolator(final Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    public void setStartTime(final long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void start() {
        this.mStartTime = -1L;
    }
}