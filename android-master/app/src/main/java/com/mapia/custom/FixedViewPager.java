package com.mapia.custom;

/**
 * Created by daehyun on 15. 6. 9..
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class FixedViewPager extends ViewPager
{
    public FixedViewPager(final Context context) {
        super(context);
    }

    public FixedViewPager(final Context context, final AttributeSet set) {
        super(context, set);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return false;
    }
}