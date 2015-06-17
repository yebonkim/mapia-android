package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.mapia.R;

public class OnScreenHint
{
    static final String TAG = "OnScreenHint";
    int mGravity;
    private final Handler mHandler;
    private final Runnable mHide;
    float mHorizontalMargin;
    View mNextView;
    private final WindowManager.LayoutParams mParams;
    private final Runnable mShow;
    float mVerticalMargin;
    View mView;
    private final WindowManager mWM;
    int mX;
    int mY;

    private OnScreenHint(final Context context) {
        super();
        this.mGravity = 81;
        this.mParams = new WindowManager.LayoutParams();
        this.mHandler = new Handler();
        this.mShow = new Runnable() {
            @Override
            public void run() {
                OnScreenHint.this.handleShow();
            }
        };
        this.mHide = new Runnable() {
            @Override
            public void run() {
                OnScreenHint.this.handleHide();
            }
        };
        this.mWM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        this.mY = context.getResources().getDimensionPixelSize(R.dimen.hint_y_offset);
        this.mParams.height = -2;
        this.mParams.width = -2;
        this.mParams.flags = 24;
        this.mParams.format = -3;
//        this.mParams.windowAnimations = context.getResources().getAnimation(R.id.Animation_OnScreenHint);
        this.mParams.type = 1000;
        this.mParams.setTitle((CharSequence)"OnScreenHint");
    }

    private void handleHide() {
        synchronized (this) {
            if (this.mView != null) {
                if (this.mView.getParent() != null) {
                    this.mWM.removeView(this.mView);
                }
                this.mView = null;
            }
        }
    }

    private void handleShow() {
        synchronized (this) {
            if (this.mView != this.mNextView) {
                this.handleHide();
                this.mView = this.mNextView;
                final int mGravity = this.mGravity;
                this.mParams.gravity = mGravity;
                if ((mGravity & 0x7) == 0x7) {
                    this.mParams.horizontalWeight = 1.0f;
                }
                if ((mGravity & 0x70) == 0x70) {
                    this.mParams.verticalWeight = 1.0f;
                }
                this.mParams.x = this.mX;
                this.mParams.y = this.mY;
                this.mParams.verticalMargin = this.mVerticalMargin;
                this.mParams.horizontalMargin = this.mHorizontalMargin;
                if (this.mView.getParent() != null) {
                    this.mWM.removeView(this.mView);
                }
                this.mWM.addView(this.mView, (ViewGroup.LayoutParams)this.mParams);
            }
        }
    }

    public static OnScreenHint makeText(final Context context, final CharSequence text) {
        final OnScreenHint onScreenHint = new OnScreenHint(context);
        final View inflate = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.on_screen_hint, (ViewGroup)null);
        ((TextView)inflate.findViewById(R.id.message)).setText(text);
        onScreenHint.mNextView = inflate;
        return onScreenHint;
    }

    public void cancel() {
        this.mHandler.post(this.mHide);
    }

    public void setText(final CharSequence text) {
        if (this.mNextView == null) {
            throw new RuntimeException("This OnScreenHint was not created with OnScreenHint.makeText()");
        }
        final TextView textView = (TextView)this.mNextView.findViewById(2131362959);
        if (textView == null) {
            throw new RuntimeException("This OnScreenHint was not created with OnScreenHint.makeText()");
        }
        textView.setText(text);
    }

    public void show() {
        if (this.mNextView == null) {
            throw new RuntimeException("View is not initialized");
        }
        this.mHandler.post(this.mShow);
    }
}