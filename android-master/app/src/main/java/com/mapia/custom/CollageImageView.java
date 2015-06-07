// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.mapia.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CollageImageView extends ImageView
{

    private int height;
    private int width;

    public CollageImageView(Context context)
    {
        super(context);
    }

    public CollageImageView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public CollageImageView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    protected void onMeasure(int i, int j)
    {
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, 0x40000000), MeasureSpec.makeMeasureSpec(height, 0x40000000));
    }

    public void setImageUrl(String s, int i, int j)
    {
//        if (StringUtils.isBlank(s) || !s.contains("http://"))
//        {
//            return;
//        } else
//        {
//            width = i;
//            height = j;
//            Glide.with(getContext()).load(s).into(this);
//            return;
//        }
    }
}
