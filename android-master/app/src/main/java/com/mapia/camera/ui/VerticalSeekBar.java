package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBarCompat
{
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;

    public VerticalSeekBar(final Context context) {
        super(context);
        this.onSeekBarChangeListener = (SeekBar.OnSeekBarChangeListener)new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
            }

            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        };
    }

    public VerticalSeekBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.onSeekBarChangeListener = (SeekBar.OnSeekBarChangeListener)new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
            }

            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        };
    }

    public VerticalSeekBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.onSeekBarChangeListener = (SeekBar.OnSeekBarChangeListener)new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
            }

            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        };
    }

    protected void onDraw(final Canvas canvas) {
        canvas.rotate(-90.0f);
        canvas.translate((float)(-this.getHeight()), 0.0f);
        super.onDraw(canvas);
    }

    protected void onMeasure(final int n, final int n2) {
        synchronized (this) {
            super.onMeasure(n2, n);
            this.setMeasuredDimension(this.getMeasuredHeight(), this.getMeasuredWidth());
        }
    }

    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n2, n, n4, n3);
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (!this.isEnabled()) {
            return false;
        }
        switch (motionEvent.getAction()) {
            default: {
                return true;
            }
            case 0: {
                this.onSeekBarChangeListener.onStartTrackingTouch((SeekBar)this);
                return true;
            }
            case 2: {
                final int progress = this.getMax() - (int)(this.getMax() * motionEvent.getY() / this.getHeight());
                this.setProgress(progress);
                this.onSeekBarChangeListener.onProgressChanged((SeekBar)this, progress, true);
                return true;
            }
            case 1: {
                this.onSeekBarChangeListener.onStopTrackingTouch((SeekBar)this);
                return true;
            }
            case 3: {
                this.onSeekBarChangeListener.onStopTrackingTouch((SeekBar)this);
                return true;
            }
        }
    }

    public void setOnSeekBarChangeListener(final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    public void setProgress(final int progress) {
        super.setProgress(progress);
        this.onSizeChanged(this.getWidth(), this.getHeight(), 0, 0);
    }
}