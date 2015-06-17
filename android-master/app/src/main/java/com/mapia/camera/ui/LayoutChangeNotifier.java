package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.view.View;


public interface LayoutChangeNotifier
{
    void setOnLayoutChangeListener(Listener p0);

    public interface Listener
    {
        void onLayoutChange(View p0, int p1, int p2, int p3, int p4);
    }
}