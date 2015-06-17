package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 17..
 */

import android.content.Context;


public class MetricUtils
{
    public static float dpToPx(final Context context, final float n) {
        return context.getResources().getDisplayMetrics().densityDpi * n / 160.0f;
    }

    public static int dpToPx(final Context context, final int n) {
        return Math.round(context.getResources().getDisplayMetrics().densityDpi * n / 160);
    }
}