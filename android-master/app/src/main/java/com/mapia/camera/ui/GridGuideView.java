package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.mapia.common.view.GridGuideBase;


public class GridGuideView extends View
{
    private GridGuideBase gridGuideBase;

    public GridGuideView(final Context context) {
        super(context);
        this.init();
    }

    public GridGuideView(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }

    public GridGuideView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init();
    }

    private void init() {
        this.gridGuideBase = new GridGuideBase(this.getContext());
    }

    public GridGuideBase getGridGuideBase() {
        return this.gridGuideBase;
    }

    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.gridGuideBase.drawGrid(canvas);
    }
}