package com.mapia.custom;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by daehyun on 15. 6. 5..
 */
public class HomeSwipeRefreshLayout extends SwipeRefreshLayout {
    private float prevX;
    private int touchSlop;

    public HomeSwipeRefreshLayout(Context paramContext, AttributeSet paramAttributeSet){
        super(paramContext, paramAttributeSet);
        this.touchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent){
        switch(paramMotionEvent.getAction()) {
        }
        do {
            for (; ; ) {
                this.prevX = MotionEvent.obtain(paramMotionEvent).getX();
                return super.onInterceptTouchEvent(paramMotionEvent);
            }
        }while(Math.abs(paramMotionEvent.getX() - this.prevX) <= this.touchSlop);

            //weird line. - asked stack overflow
//    return false;
    }
}
