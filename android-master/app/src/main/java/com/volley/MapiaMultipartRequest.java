package com.volley;

/**
 * Created by daehyun on 15. 6. 13..
 */

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.mapia.login.LoginInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;



public class MapiaMultipartRequest extends Request<String>
{
    private String mFileParamName;
    private HttpEntity mHttpEntity;
    private Response.Listener mListener;

    public MapiaMultipartRequest(final String s, final File file, final Map<String, String> map, final Response.Listener<String> mListener, final Response.ErrorListener errorListener) {
        super(1, s, errorListener);
        this.mListener = mListener;
//        this.mHttpEntity = this.buildMultipartEntity(file, map);
        this.mFileParamName = null;
    }

    public MapiaMultipartRequest(final String s, final String mFileParamName, final File file, final Map<String, String> map, final Response.Listener<String> mListener, final Response.ErrorListener errorListener) {
        super(1, s, errorListener);
        this.mListener = mListener;
        this.mFileParamName = mFileParamName;
        this.mHttpEntity = this.buildMultipartEntity(file, map);
    }

    private HttpEntity buildMultipartEntity(final File file, final Map<String, String> map) {
        final MultipartEntityBuilder create = MultipartEntityBuilder.create();
        create.setCharset(Charset.forName("UTF-8"));
        create.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (file != null && this.mFileParamName != null) {
            create.addPart(this.mFileParamName, new FileBody(file));
        }
        if (map != null) {
            for (final String s : map.keySet()) {
                if (StringUtils.isNotBlank(map.get(s))) {
                    Charset utf8 = Charset.forName("UTF-8");
                    create.addTextBody(s, map.get(s), org.apache.http.entity.ContentType.APPLICATION_JSON);
                }
            }
        }
        return create.build();
    }

    @Override
    protected void deliverResponse(final String s) {
        this.mListener.onResponse(s);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            this.mHttpEntity.writeTo((OutputStream)byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException ex) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream", new Object[0]);
            return byteArrayOutputStream.toByteArray();
        }
    }

    @Override
    public String getBodyContentType() {
        return this.mHttpEntity.getContentType().getValue();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final LoginInfo instance = LoginInfo.getInstance();
        hashMap.put("NEOID_ID", instance.getNeoidId());
        hashMap.put("Authorization", "Bearer " + instance.getToken());
        hashMap.put("consumerKey", "Bjy3yFaDvdZWwd4eeCsC");
        return hashMap;
    }

    @Override
    protected Response<String> parseNetworkResponse(final NetworkResponse networkResponse) {
        return Response.success(new String(networkResponse.data), this.getCacheEntry());
    }
}