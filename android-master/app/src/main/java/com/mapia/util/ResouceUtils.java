package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;

import com.mapia.MainApplication;


public class ResouceUtils
{
    private static Context context;

    public static String getString(final int n) {
        if (ResouceUtils.context == null) {
            ResouceUtils.context = MainApplication.getContext();
        }
        return ResouceUtils.context.getString(n);
    }
}