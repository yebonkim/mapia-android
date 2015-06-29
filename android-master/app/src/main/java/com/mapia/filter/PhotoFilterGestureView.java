package com.mapia.filter;

/**
 * Created by daehyun on 15. 6. 21..
 */

import android.view.ViewConfiguration;


        import android.view.MotionEvent;
        import android.view.ViewConfiguration;
        import android.util.AttributeSet;
        import android.content.Context;
        import android.view.GestureDetector.OnGestureListener;
        import android.os.Handler;
        import android.view.GestureDetector;
        import android.view.View;

public class PhotoFilterGestureView extends View
{
    private GestureDetector gestureScanner;
    private Runnable goneRunnable;
    private Handler handler;
    private float mTouchDownX;
    private float mTouchDownY;
    private GestureDetector.OnGestureListener onGestureListener;
    private int touchSlop;
    private View view;

    public PhotoFilterGestureView(final Context context) {
        super(context);
        this.handler = new Handler();
        this.goneRunnable = new Runnable() {
            @Override
            public void run() {
                if (PhotoFilterGestureView.this.view != null) {
                    PhotoFilterGestureView.this.view.setVisibility(8);
                }
            }
        };
        this.init();
    }

    public PhotoFilterGestureView(final Context context, final AttributeSet set) {
        super(context, set);
        this.handler = new Handler();
        this.goneRunnable = new Runnable() {
            @Override
            public void run() {
                if (PhotoFilterGestureView.this.view != null) {
                    PhotoFilterGestureView.this.view.setVisibility(8);
                }
            }
        };
        this.init();
    }

    public PhotoFilterGestureView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.handler = new Handler();
        this.goneRunnable = new Runnable() {
            @Override
            public void run() {
                if (PhotoFilterGestureView.this.view != null) {
                    PhotoFilterGestureView.this.view.setVisibility(8);
                }
            }
        };
        this.init();
    }

    private void init() {
        this.touchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.gestureScanner.onTouchEvent(motionEvent);
        switch (motionEvent.getAction()) {
            case 0: {
                this.handler.postDelayed(this.goneRunnable, 150L);
                this.mTouchDownX = motionEvent.getX();
                this.mTouchDownY = motionEvent.getY();
                break;
            }
            case 2: {
                final float abs = Math.abs(motionEvent.getX() - this.mTouchDownX);
                final float abs2 = Math.abs(motionEvent.getY() - this.mTouchDownY);
                if (abs > this.touchSlop || abs2 > this.touchSlop) {
                    this.handler.removeCallbacks(this.goneRunnable);
                    break;
                }
                break;
            }
            case 1: {
                this.handler.removeCallbacks(this.goneRunnable);
                if (this.view != null) {
                    this.view.setVisibility(0);
                    break;
                }
                break;
            }
        }
        return true;
    }

    public void setOnGestureListener(final GestureDetector.OnGestureListener onGestureListener) {
        this.onGestureListener = onGestureListener;
        this.gestureScanner = new GestureDetector(this.getContext(), onGestureListener);
    }

    public void setView(final View view) {
        this.view = view;
    }
}