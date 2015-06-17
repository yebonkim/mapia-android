package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

import com.mapia.R;
import com.mapia.camera.orientation.OrientationEvent;
import com.mapia.camera.ui.RenderOverlay;
import com.mapia.camera.ui.ZoomRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PreviewGestures implements ScaleGestureDetector.OnScaleGestureListener
{
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;
    public static final int DIR_UP = 0;
    private static final int MODE_ALL = 4;
    private static final int MODE_MODULE = 3;
    private static final int MODE_NONE = 0;
    private static final int MODE_SWIPE = 5;
    private static final int MODE_ZOOM = 2;
    private static final String TAG = "CAM_gestures";
    private static final long TIMEOUT_PIE = 200L;
    private CameraActivity mActivity;
    private MotionEvent mCurrent;
    private MotionEvent mDown;
    private boolean mEnabled;
    private int[] mLocation;
    private int mMode;
    private int mOrientation;
    private RenderOverlay mOverlay;
    private List<View> mReceivers;
    private ScaleGestureDetector mScale;
    private int mSlop;
    private SwipeListener mSwipeListener;
    private SingleTapListener mTapListener;
    private int mTapTimeout;
    private List<View> mUnclickableAreas;
    private ZoomRenderer mZoom;
    private boolean mZoomOnly;

    public PreviewGestures(final CameraActivity mActivity, final SingleTapListener mTapListener, final ZoomRenderer mZoom, final SwipeListener mSwipeListener) {
        super();
        this.mActivity = mActivity;
        this.mTapListener = mTapListener;
        this.mZoom = mZoom;
        this.mMode = 4;
        this.mScale = new ScaleGestureDetector((Context)mActivity, (ScaleGestureDetector.OnScaleGestureListener)this);
        this.mSlop = (int)mActivity.getResources().getDimension(R.dimen.pie_touch_slop);
        this.mTapTimeout = ViewConfiguration.getTapTimeout();
        this.mEnabled = true;
        this.mLocation = new int[2];
        this.mSwipeListener = mSwipeListener;
    }

    private boolean checkClickable(final MotionEvent motionEvent) {
        if (this.mUnclickableAreas != null) {
            final Iterator<View> iterator = this.mUnclickableAreas.iterator();
            while (iterator.hasNext()) {
                if (this.isInside(motionEvent, iterator.next())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkReceivers(final MotionEvent motionEvent) {
        if (this.mReceivers != null) {
            final Iterator<View> iterator = this.mReceivers.iterator();
            while (iterator.hasNext()) {
                if (this.isInside(motionEvent, iterator.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getSwipeDirection(final MotionEvent motionEvent) {
        float n = 0.0f;
        float n2 = 0.0f;
        switch (this.mOrientation) {
            case 0: {
                n = motionEvent.getX() - this.mDown.getX();
                n2 = motionEvent.getY() - this.mDown.getY();
                break;
            }
            case 90: {
                n = -(motionEvent.getY() - this.mDown.getY());
                n2 = motionEvent.getX() - this.mDown.getX();
                break;
            }
            case 180: {
                n = -(motionEvent.getX() - this.mDown.getX());
                n2 = motionEvent.getY() - this.mDown.getY();
                break;
            }
            case 270: {
                n = motionEvent.getY() - this.mDown.getY();
                n2 = motionEvent.getX() - this.mDown.getX();
                break;
            }
        }
        if (n < 0.0f && Math.abs(n2) / -n < 2.0f) {
            return 2;
        }
        if (n > 0.0f && Math.abs(n2) / n < 2.0f) {
            return 3;
        }
        if (n2 > 0.0f) {
            return 1;
        }
        return 0;
    }

    private boolean isInside(final MotionEvent motionEvent, final View view) {
        view.getLocationInWindow(this.mLocation);
        if ((int)view.getRotationY() == 180) {
            final int[] mLocation = this.mLocation;
            mLocation[0] -= view.getWidth();
        }
        if ((int)view.getRotationX() == 180) {
            final int[] mLocation2 = this.mLocation;
            mLocation2[1] -= view.getHeight();
        }
        return view.getVisibility() == View.VISIBLE && motionEvent.getX() >= this.mLocation[0] && motionEvent.getX() < this.mLocation[0] + view.getWidth() && motionEvent.getY() >= this.mLocation[1] && motionEvent.getY() < this.mLocation[1] + view.getHeight();
    }

    private MotionEvent makeCancelEvent(MotionEvent obtain) {
        obtain = MotionEvent.obtain(obtain);
        obtain.setAction(3);
        return obtain;
    }

    public void addTouchReceiver(final View view) {
        if (this.mReceivers == null) {
            this.mReceivers = new ArrayList<View>();
        }
        this.mReceivers.add(view);
    }

    public void cancelActivityTouchHandling(final MotionEvent motionEvent) {
        this.mActivity.superDispatchTouchEvent(this.makeCancelEvent(motionEvent));
    }

    public void clearTouchReceivers() {
        if (this.mReceivers != null) {
            this.mReceivers.clear();
        }
    }

    public void clearUnclickableAreas() {
        if (this.mUnclickableAreas != null) {
            this.mUnclickableAreas.clear();
        }
    }

    public boolean dispatchTouch(final MotionEvent mCurrent) {
        if (!this.mEnabled) {
            return this.mActivity.superDispatchTouchEvent(mCurrent);
        }
        this.mCurrent = mCurrent;
        if (mCurrent.getActionMasked() == 0) {
            this.mOrientation = OrientationEvent.getCurrentOrientation();
            if (this.checkReceivers(mCurrent)) {
                this.mMode = 3;
                return this.mActivity.superDispatchTouchEvent(mCurrent);
            }
            this.mMode = 4;
            this.mDown = MotionEvent.obtain(mCurrent);
            if (this.mZoom != null) {
                this.mScale.onTouchEvent(mCurrent);
            }
            return this.mActivity.superDispatchTouchEvent(mCurrent);
        }
        else {
            if (this.mMode == 0) {
                return false;
            }
            if (this.mMode == 5) {
                if (1 == mCurrent.getActionMasked()) {
                    this.mSwipeListener.onSwipe(this.getSwipeDirection(mCurrent));
                }
                return true;
            }
            if (this.mMode == 2) {
                this.mScale.onTouchEvent(mCurrent);
                if (!this.mScale.isInProgress() && 6 == mCurrent.getActionMasked()) {
                    this.mMode = 0;
                    this.onScaleEnd(this.mScale);
                }
                return true;
            }
            if (this.mMode == 3) {
                return this.mActivity.superDispatchTouchEvent(mCurrent);
            }
            if (this.mDown == null) {
                return true;
            }
            if (5 == mCurrent.getActionMasked()) {
                if (this.mZoom != null) {
                    this.mScale.onTouchEvent(mCurrent);
                    this.onScaleBegin(this.mScale);
                }
            }
            else if (this.mMode == 2 && !this.mScale.isInProgress() && 6 == mCurrent.getActionMasked()) {
                this.mScale.onTouchEvent(mCurrent);
                this.onScaleEnd(this.mScale);
            }
            if (this.mZoom != null) {
                final boolean onTouchEvent = this.mScale.onTouchEvent(mCurrent);
                if (this.mScale.isInProgress()) {
                    this.cancelActivityTouchHandling(mCurrent);
                    return onTouchEvent;
                }
            }
            if (1 != mCurrent.getActionMasked()) {
                if (2 == mCurrent.getActionMasked() && (Math.abs(mCurrent.getX() - this.mDown.getX()) > this.mSlop || Math.abs(mCurrent.getY() - this.mDown.getY()) > this.mSlop)) {
                    this.getSwipeDirection(mCurrent);
                    this.mMode = 5;
                }
                return false;
            }
            if (mCurrent.getEventTime() - this.mDown.getEventTime() < this.mTapTimeout && this.checkClickable(mCurrent)) {
                this.cancelActivityTouchHandling(mCurrent);
                this.mTapListener.onSingleTapUp(null, (int)this.mDown.getX() - this.mOverlay.getWindowPositionX(), (int)this.mDown.getY() - this.mOverlay.getWindowPositionY());
                return true;
            }
            return this.mActivity.superDispatchTouchEvent(mCurrent);
        }
    }

    public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
        return this.mZoom.onScale(scaleGestureDetector);
    }

    public boolean onScaleBegin(final ScaleGestureDetector scaleGestureDetector) {
        if (this.mMode != 2) {
            this.mMode = 2;
            this.cancelActivityTouchHandling(this.mCurrent);
        }
        return this.mCurrent.getActionMasked() == 2 || this.mZoom.onScaleBegin(scaleGestureDetector);
    }

    public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
        if (this.mCurrent.getActionMasked() != 2) {
            this.mZoom.onScaleEnd(scaleGestureDetector);
        }
    }

    public void reset() {
        this.clearTouchReceivers();
        this.clearUnclickableAreas();
    }

    public void setEnabled(final boolean mEnabled) {
        this.mEnabled = mEnabled;
    }

    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
    }

    public void setRenderOverlay(final RenderOverlay mOverlay) {
        this.mOverlay = mOverlay;
    }

    public void setZoomOnly(final boolean mZoomOnly) {
        this.mZoomOnly = mZoomOnly;
    }

    public interface SingleTapListener
    {
        void onSingleTapUp(View p0, int p1, int p2);
    }

    interface SwipeListener
    {
        void onSwipe(int p0);
    }
}