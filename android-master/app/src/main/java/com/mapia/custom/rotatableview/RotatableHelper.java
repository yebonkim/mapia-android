package com.mapia.custom.rotatableview;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.view.View;
import android.view.animation.AnimationUtils;

public class RotatableHelper implements Rotatable
{
    private static final int ANIMATION_SPEED = 270;
    private long animationEndTime;
    private long animationStartTime;
    private boolean clockwise;
    private int currentDegree;
    private boolean enableAnimation;
    private OnRotateEndListener onRotateEndListener;
    private int startDegree;
    private int targetDegree;
    private View view;

    public RotatableHelper(final View view) {
        super();
        this.onRotateEndListener = new BaseOnRotateEndListener();
        this.currentDegree = 0;
        this.startDegree = 0;
        this.targetDegree = 0;
        this.clockwise = false;
        this.enableAnimation = true;
        this.animationStartTime = 0L;
        this.animationEndTime = 0L;
        this.view = view;
    }

    @Override
    public void setOrientation(int targetDegree, final boolean enableAnimation) {
        this.enableAnimation = enableAnimation;
        if (targetDegree >= 0) {
            targetDegree %= 360;
        }
        else {
            targetDegree = targetDegree % 360 + 360;
        }
        if (targetDegree == this.targetDegree) {
            return;
        }
        this.targetDegree = targetDegree;
        if (this.enableAnimation) {
            this.startDegree = this.currentDegree;
            this.animationStartTime = AnimationUtils.currentAnimationTimeMillis();
            targetDegree = this.targetDegree - this.currentDegree;
            if (targetDegree < 0) {
                targetDegree += 360;
            }
            int n;
            if ((n = targetDegree) > 180) {
                n = targetDegree - 360;
            }
            this.clockwise = (n >= 0);
            this.animationEndTime = this.animationStartTime + Math.abs(n) * 1000 / 270;
        }
        else {
            this.currentDegree = this.targetDegree;
        }
        this.view.invalidate();
    }

    public void setRotateEndListener(final OnRotateEndListener onRotateEndListener) {
        if (onRotateEndListener == null) {
            this.onRotateEndListener = new BaseOnRotateEndListener();
            return;
        }
        this.onRotateEndListener = onRotateEndListener;
    }

    public int updateCurrentDegreeAndView() {
        if (this.currentDegree != this.targetDegree) {
            final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            if (currentAnimationTimeMillis < this.animationEndTime) {
                int n = (int)(currentAnimationTimeMillis - this.animationStartTime);
                final int startDegree = this.startDegree;
                if (!this.clockwise) {
                    n = -n;
                }
                final int n2 = startDegree + n * 270 / 1000;
                int currentDegree;
                if (n2 >= 0) {
                    currentDegree = n2 % 360;
                }
                else {
                    currentDegree = n2 % 360 + 360;
                }
                this.currentDegree = currentDegree;
                this.view.invalidate();
            }
            else {
                this.currentDegree = this.targetDegree;
                this.onRotateEndListener.onRotateEnd();
            }
        }
        return this.currentDegree;
    }

    private class BaseOnRotateEndListener implements OnRotateEndListener
    {
        @Override
        public void onRotateEnd() {
        }
    }
}