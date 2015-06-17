package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.mapia.camera.ui.LayoutChangeHelper;
import com.mapia.camera.ui.LayoutChangeNotifier;


public class PreviewFrameLayout extends RelativeLayout implements LayoutChangeNotifier
{
    private static final String TAG = "CAM_preview";
    private double mAspectRatio;
    private LayoutChangeHelper mLayoutChangeHelper;
    private OnSizeChangedListener mListener;

    public PreviewFrameLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.setAspectRatio(1.3333333333333333);
        this.mLayoutChangeHelper = new LayoutChangeHelper((View)this);
    }

    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.mLayoutChangeHelper.onLayout(b, n, n2, n3, n4);
    }

    protected void onMeasure(int n, int size) {
        final int size2 = View.MeasureSpec.getSize(n);
        final int n2 = size = View.MeasureSpec.getSize(size);
        n = size2;
        if (!false){//ApiHelper.HAS_SURFACE_TEXTURE) {
            final int n3 = this.getPaddingLeft() + this.getPaddingRight();
            final int n4 = this.getPaddingTop() + this.getPaddingBottom();
            size = size2 - n3;
            final int n5 = n2 - n4;
            boolean b;
            if (size > n5) {
                b = true;
            }
            else {
                b = false;
            }
            if (b) {
                n = size;
            }
            else {
                n = n5;
            }
            if (b) {
                size = n5;
            }
            if (n > size * this.mAspectRatio) {
                n = (int)(size * this.mAspectRatio);
            }
            else {
                size = (int)(n / this.mAspectRatio);
            }
            int n6;
            if (b) {
                n6 = n;
                n = size;
            }
            else {
                n6 = size;
            }
            size = n6 + n3;
            final int n7 = n + n4;
            n = size;
            size = n7;
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(n, 1073741824), View.MeasureSpec.makeMeasureSpec(size, 1073741824));
    }

    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        if (this.mListener != null) {
            this.mListener.onSizeChanged(n, n2);
        }
    }

    public void setAspectRatio(final double mAspectRatio) {
        if (mAspectRatio <= 0.0) {
            throw new IllegalArgumentException();
        }
        if (this.mAspectRatio != mAspectRatio) {
            this.mAspectRatio = mAspectRatio;
            this.requestLayout();
        }
    }

    public void setOnLayoutChangeListener(final LayoutChangeNotifier.Listener onLayoutChangeListener) {
        this.mLayoutChangeHelper.setOnLayoutChangeListener(onLayoutChangeListener);
    }

    public void setOnSizeChangedListener(final OnSizeChangedListener mListener) {
        this.mListener = mListener;
    }

    public interface OnSizeChangedListener
    {
        void onSizeChanged(int p0, int p1);
    }
}