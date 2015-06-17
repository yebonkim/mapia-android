package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 13..
 */

import com.android.volley.DefaultRetryPolicy;


public class RequestUtils
{
    private static DefaultRetryPolicy defaultRetryPolicy;

    public static DefaultRetryPolicy getDefaultRetryPolicy() {
        if (RequestUtils.defaultRetryPolicy == null) {
            RequestUtils.defaultRetryPolicy = new DefaultRetryPolicy(10000, 1, 1.0f);
        }
        return RequestUtils.defaultRetryPolicy;
    }

    public static DefaultRetryPolicy getNoRetryPolicy() {
        return new DefaultRetryPolicy(10000, 0, 1.0f);
    }
}