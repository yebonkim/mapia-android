package com.volley;

/**
 * Created by daehyun on 15. 6. 13..
 */

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.mapia.login.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapiaPostRequest extends Request<JSONObject>
{
    private Response.Listener<JSONObject> mListener;
    private Map<String, String> mParam;

    public MapiaPostRequest(final String s, final Map<String, String> mParam, final Response.Listener<JSONObject> mListener, final Response.ErrorListener errorListener) {
        super(1, s, errorListener);
        this.mListener = mListener;
        this.mParam = mParam;
    }

    @Override
    protected void deliverResponse(final JSONObject jsonObject) {
        this.mListener.onResponse(jsonObject);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + LoginInfo.getInstance().getToken());
        hashMap.put("consumerKey", "Bjy3yFaDvdZWwd4eeCsC");
        return hashMap;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (this.mParam == null) {
            return null;
        }
        final Iterator<String> iterator = this.mParam.keySet().iterator();
        while (iterator.hasNext()) {
            if (this.mParam.get(iterator.next()) == null) {
                continue;
            }
        }
        return this.mParam;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(final NetworkResponse networkResponse) {
        try {
            return Response.success(new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers))), HttpHeaderParser.parseCacheHeaders(networkResponse));
        }
        catch (UnsupportedEncodingException ex) {
            return Response.error(new ParseError(ex));
        }
        catch (JSONException ex2) {
            return Response.error(new ParseError((Throwable)ex2));
        }
    }
}