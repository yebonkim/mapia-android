package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ShutterButton extends ImageView
{
    private OnShutterButtonListener mListener;
    private boolean mOldPressed;
    private boolean mTouchEnabled;

    public ShutterButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.mTouchEnabled = true;
    }

    private void callShutterButtonFocus(final boolean b) {
        if (this.mListener != null) {
            this.mListener.onShutterButtonFocus(b);
        }
    }

    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        return this.mTouchEnabled && super.dispatchTouchEvent(motionEvent);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final boolean pressed = this.isPressed();
        if (pressed != this.mOldPressed) {
            if (!pressed) {
                this.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ShutterButton.this.callShutterButtonFocus(pressed);
                    }
                });
            }
            else {
                this.callShutterButtonFocus(pressed);
            }
            this.mOldPressed = pressed;
        }
    }

    public void enableTouch(final boolean mTouchEnabled) {
        this.mTouchEnabled = mTouchEnabled;
    }

    public boolean performClick() {
        final boolean performClick = super.performClick();
        if (this.mListener != null && this.getVisibility() == View.VISIBLE) {
//            AceUtils.nClick(NClicks.CAMERA_SHUTTER);
            this.mListener.onShutterButtonClick();
        }
        return performClick;
    }

    public void setOnShutterButtonListener(final OnShutterButtonListener mListener) {
        this.mListener = mListener;
    }

    public interface OnShutterButtonListener
    {
        void onShutterButtonClick();

        void onShutterButtonFocus(boolean p0);
    }
}