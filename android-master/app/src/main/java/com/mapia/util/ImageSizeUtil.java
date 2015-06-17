package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 16..
 */
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

import android.graphics.Rect;


public class ImageSizeUtil
{

    public ImageSizeUtil()
    {
    }

    public static Rect centerCrop(Rect rect, Rect rect1)
    {
        if (rect.width() * rect1.height() > rect1.width() * rect.height())
        {
            int i = (rect.width() * rect1.height()) / rect.height();
            return new Rect(((rect1.left + rect1.right) - i) / 2, rect1.top, (rect1.left + rect1.right + i) / 2, rect1.bottom);
        } else
        {
            int j = (rect.height() * rect1.width()) / rect.width();
            return new Rect(rect1.left, ((rect1.top + rect1.bottom) - j) / 2, rect1.right, (rect1.top + rect1.bottom + j) / 2);
        }
    }

    public static Rect fitCenter(Rect rect, Rect rect1)
    {
        if (rect.width() * rect1.height() > rect1.width() * rect.height())
        {
            int i = (rect.height() * rect1.width() + rect.width() / 2) / rect.width();
            return new Rect(rect1.left, ((rect1.top + rect1.bottom) - i) / 2, rect1.right, (rect1.top + rect1.bottom + i) / 2);
        } else
        {
            int j = (rect.width() * rect1.height() + rect.height() / 2) / rect.height();
            return new Rect(((rect1.left + rect1.right) - j) / 2, rect1.top, (rect1.left + rect1.right + j) / 2, rect1.bottom);
        }
    }

    public static int[] getAspectRatio(int i, int j, int k, int l)
    {
        if ((double)i / (double)j == (double)k / (double)l)
        {
            return (new int[] {
                    k, l
            });
        }
        int j1 = (int)Math.ceil(((double)l * (double)i) / (double)j);
        int i1 = l;
        l = j1;
        if (j1 > k)
        {
            l = k;
            i1 = (int)Math.ceil(((double)k * (double)j) / (double)i);
        }
        return (new int[] {
                l, i1
        });
    }
}
