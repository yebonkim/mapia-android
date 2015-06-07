// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.mapia.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class HeightableRelativeLayout extends RelativeLayout
{

    private int height;

    public HeightableRelativeLayout(Context context)
    {
        super(context);
    }

    public HeightableRelativeLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public HeightableRelativeLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    protected void onMeasure(int i, int j)
    {
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(height, 0x40000000));
    }

    public void setHeight(int i)
    {
        height = i;
    }
}
