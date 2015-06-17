package com.volley;

/**
 * Created by daehyun on 15. 6. 13..
 */

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapia.login.LoginInfo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MapiaRequest extends JsonObjectRequest
{
    public MapiaRequest(final int n, final String s, final JSONObject jsonObject, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        super(n, s, jsonObject, listener, errorListener);
    }

    public MapiaRequest(final String s, final JSONObject jsonObject, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        super(s, jsonObject, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + LoginInfo.getInstance().getToken());
        hashMap.put("consumerKey", "Bjy3yFaDvdZWwd4eeCsC");
        return hashMap;
    }
}