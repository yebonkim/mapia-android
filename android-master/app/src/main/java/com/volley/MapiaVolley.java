package com.volley;

/**
 * Created by daehyun on 15. 6. 13..
 */

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;


public class MapiaVolley
{
    private static RequestQueue requestQueue;

    public static RequestQueue getRequestQueue() {
        if (MapiaVolley.requestQueue == null) {
            throw new IllegalStateException("RequestQueue is not initialized.");
        }
        return MapiaVolley.requestQueue;
    }

    public static void init(final Context context) {
        if (context == null) {
            throw new NullPointerException("context must not be null.");
        }
        (MapiaVolley.requestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()))).start();
    }
}