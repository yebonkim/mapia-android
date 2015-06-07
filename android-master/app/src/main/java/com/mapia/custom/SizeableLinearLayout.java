// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.mapia.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SizeableLinearLayout extends LinearLayout
{

    private int height;
    private int width;

    public SizeableLinearLayout(Context context)
    {
        super(context);
    }

    public SizeableLinearLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public SizeableLinearLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    protected void onMeasure(int i, int j)
    {
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, 0x40000000), MeasureSpec.makeMeasureSpec(height, 0x40000000));
    }

    public void setWidthHeight(int i, int j)
    {
        width = i;
        height = j;
    }
}
