package com.volley;

/**
 * Created by daehyun on 15. 6. 13..
 */

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.mapia.login.LoginInfo;

import java.util.HashMap;
import java.util.Map;


public class MapiaStringRequest extends StringRequest
{
    public MapiaStringRequest(final int n, final String s, final Response.Listener<String> listener, final Response.ErrorListener errorListener) {
        super(n, s, listener, errorListener);
    }

    public MapiaStringRequest(final String s, final Response.Listener<String> listener, final Response.ErrorListener errorListener) {
        super(s, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + LoginInfo.getInstance().getToken());
        hashMap.put("consumerKey", "Bjy3yFaDvdZWwd4eeCsC");
        return hashMap;
    }
}