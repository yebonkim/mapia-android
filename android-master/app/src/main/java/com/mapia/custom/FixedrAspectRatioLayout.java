package com.mapia.custom;

/**
 * Created by daehyun on 15. 6. 16..
 */
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mapia.util.ImageSizeUtil;


public class FixedrAspectRatioLayout extends FrameLayout
{

    Rect newRect;
    Rect parentRect;
    Rect rect_3_4;

    public FixedrAspectRatioLayout(Context context)
    {
        super(context);
        init();
    }

    public FixedrAspectRatioLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        init();
    }

    public FixedrAspectRatioLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        init();
    }

    private void init()
    {
        parentRect = new Rect(0, 0, 0, 0);
        rect_3_4 = new Rect(0, 0, 3, 4);
        newRect = new Rect(0, 0, 0, 0);
    }

    protected void onMeasure(int i, int j)
    {
        i = android.view.View.MeasureSpec.getSize(i);
        j = android.view.View.MeasureSpec.getSize(j);
        Object obj = (android.widget.RelativeLayout.LayoutParams)getLayoutParams();
        parentRect.right = ((android.widget.RelativeLayout.LayoutParams) (obj)).leftMargin + ((android.widget.RelativeLayout.LayoutParams) (obj)).rightMargin + i;
        parentRect.bottom = ((android.widget.RelativeLayout.LayoutParams) (obj)).topMargin + ((android.widget.RelativeLayout.LayoutParams) (obj)).bottomMargin + j;
        obj = ImageSizeUtil.fitCenter(rect_3_4, parentRect);
        super.onMeasure(android.view.View.MeasureSpec.makeMeasureSpec(((Rect) (obj)).width(), 0x40000000), android.view.View.MeasureSpec.makeMeasureSpec(((Rect) (obj)).height(), 0x40000000));
    }
}
