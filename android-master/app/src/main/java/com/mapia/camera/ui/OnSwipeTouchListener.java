package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 17..
 */

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.mapia.MainApplication;
import com.mapia.camera.orientation.OrientationEvent;
import com.mapia.util.MetricUtils;

public abstract class OnSwipeTouchListener implements View.OnTouchListener
{
    private static final int SWIPE_THRESHOLD_LARGE = 30000;
    private static final int SWIPE_THRESHOLD_SMALL = 400;
    private boolean isConsumed;
    private int orientation;
    private int swipeThreshold;
    private Point touchBegin;

    public OnSwipeTouchListener(final boolean b) {
        super();
        this.swipeThreshold = 0;
        this.touchBegin = new Point();
        this.isConsumed = false;
        if (b) {
            this.swipeThreshold = MetricUtils.dpToPx(MainApplication.getContext(), 400);
        }
        else {
            this.swipeThreshold = MetricUtils.dpToPx(MainApplication.getContext(), 30000);
        }
        this.orientation = 0;
    }

    public abstract boolean onSingleTapUp(final View p0, final MotionEvent p1);

    public abstract void onSwipeBottom(final View p0);

    public abstract void onSwipeLeft(final View p0);

    public abstract void onSwipeRight(final View p0);

    public abstract void onSwipeTop(final View p0);

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        final int x = (int)motionEvent.getX();
        final int y = (int)motionEvent.getY();
        if (action == 0) {
            this.isConsumed = false;
            this.touchBegin.x = x;
            this.touchBegin.y = y;
            this.orientation = OrientationEvent.getCurrentOrientation();
        }
        else if (2 == action) {
            if (!this.isConsumed) {
                final int n = this.touchBegin.x - x;
                final int n2 = this.touchBegin.y - y;
                if (this.swipeThreshold < n * n + n2 * n2) {
                    this.isConsumed = true;
                    int n3;
                    if (n > 0) {
                        n3 = n;
                    }
                    else {
                        n3 = -n;
                    }
                    int n4;
                    if (n2 > 0) {
                        n4 = n2;
                    }
                    else {
                        n4 = -n2;
                    }
                    if (n3 < n4) {
                        if (n2 > 0) {
                            if (this.orientation == 0) {
                                this.onSwipeTop(view);
                                return true;
                            }
                            if (this.orientation == 180) {
                                this.onSwipeBottom(view);
                                return true;
                            }
                            if (this.orientation == 90) {
                                this.onSwipeRight(view);
                                return true;
                            }
                            this.onSwipeLeft(view);
                            return true;
                        }
                        else {
                            if (this.orientation == 0) {
                                this.onSwipeBottom(view);
                                return true;
                            }
                            if (this.orientation == 180) {
                                this.onSwipeTop(view);
                                return true;
                            }
                            if (this.orientation == 90) {
                                this.onSwipeLeft(view);
                                return true;
                            }
                            this.onSwipeRight(view);
                            return true;
                        }
                    }
                    else if (n > 0) {
                        if (this.orientation == 0) {
                            this.onSwipeLeft(view);
                            return true;
                        }
                        if (this.orientation == 180) {
                            this.onSwipeRight(view);
                            return true;
                        }
                        if (this.orientation == 90) {
                            this.onSwipeTop(view);
                            return true;
                        }
                        this.onSwipeBottom(view);
                        return true;
                    }
                    else {
                        if (this.orientation == 0) {
                            this.onSwipeRight(view);
                            return true;
                        }
                        if (this.orientation == 180) {
                            this.onSwipeLeft(view);
                            return true;
                        }
                        if (this.orientation == 90) {
                            this.onSwipeBottom(view);
                            return true;
                        }
                        this.onSwipeTop(view);
                        return true;
                    }
                }
            }
        }
        else if (action == 1 && ! this.isConsumed) {
            final int n5 = this.touchBegin.x - x;
            final int n6 = this.touchBegin.y - y;
            if (this.swipeThreshold > n5 * n5 + n6 * n6) {
                this.onSingleTapUp(view, motionEvent);
                return true;
            }
        }
        return true;
    }
}