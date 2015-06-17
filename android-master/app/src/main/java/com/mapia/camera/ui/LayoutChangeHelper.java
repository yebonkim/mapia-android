package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.view.View;


public class LayoutChangeHelper implements LayoutChangeNotifier
{
    private boolean mFirstTimeLayout;
    private LayoutChangeNotifier.Listener mListener;
    private View mView;

    public LayoutChangeHelper(final View mView) {
        super();
        this.mView = mView;
        this.mFirstTimeLayout = true;
    }

    public void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.mListener != null && (this.mFirstTimeLayout || b)) {
            this.mFirstTimeLayout = false;
            this.mListener.onLayoutChange(this.mView, n, n2, n3, n4);
        }
    }

    @Override
    public void setOnLayoutChangeListener(final LayoutChangeNotifier.Listener mListener) {
        this.mListener = mListener;
    }
}