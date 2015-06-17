package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.ViewGroup;


public class PreviewSurfaceView extends SurfaceView
{
    public PreviewSurfaceView(final Context context, final AttributeSet set) {
        super(context, set);
        this.setZOrderMediaOverlay(true);
        this.getHolder().setType(3);
    }

    private void setLayoutSize(final int n) {
        final ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams.width != n || layoutParams.height != n) {
            layoutParams.width = n;
            layoutParams.height = n;
            this.setLayoutParams(layoutParams);
        }
    }

    public void expand() {
        this.setLayoutSize(-1);
    }

    public void shrink() {
        this.setLayoutSize(1);
    }
}