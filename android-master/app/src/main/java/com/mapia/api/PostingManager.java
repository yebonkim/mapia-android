package com.mapia.api;

import android.content.Context;
import android.media.MediaScannerConnection;

import com.mapia.post.PostingInfo;
import com.mapia.util.CameraUtils;
import com.mapia.util.FileUtils;
import com.volley.MapiaPostRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daehyun on 15. 6. 13..
 */

public class PostingManager
{
    ArrayList<MapiaPostRequest> mListRequest;

    public PostingManager() {
        super();
        this.mListRequest = new ArrayList<MapiaPostRequest>();
    }

    private Map makeVideoParam(final PostingInfo postingInfo) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("fileType", "CLIP");
        hashMap.put("copyrightYn", postingInfo.copyrightYn);
        hashMap.put("picThumbnailUrl", postingInfo.videoThumbUrl);
        hashMap.put("picThumbnailWidth", String.valueOf(postingInfo.vidWidth));
        hashMap.put("picThumbnailHeight", String.valueOf(postingInfo.vidHeight));
        hashMap.put("picVid", postingInfo.vid);
        hashMap.put("body", postingInfo.body);
        if (postingInfo.locationData != null) {
            hashMap.put("latitude", String.valueOf(postingInfo.locationData.latitude));
            hashMap.put("longitude", String.valueOf(postingInfo.locationData.longitude));
            hashMap.put("code", String.valueOf(postingInfo.locationData.placeId));
            hashMap.put("name", postingInfo.locationData.main);
            hashMap.put("address", postingInfo.locationData.address);
        }
        return hashMap;
    }

//    public static void propagatePicAdded(final EventBus eventBus) {
//        final HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("event", "enp");
//        eventBus.post(hashMap);
//    }

    public static void removeTempVideoFile(final Context context, final PostingInfo postingInfo) {
        if ("Y".equals(CameraUtils.getSaveMapiaVideoYn())) {
            MediaScannerConnection.scanFile(context, new String[]{postingInfo.file.getAbsolutePath()}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
        }
        else {
            FileUtils.deleteFile(postingInfo.file);
            MediaScannerConnection.scanFile(context, new String[] { postingInfo.file.getAbsolutePath() }, (String[])null, (MediaScannerConnection.OnScanCompletedListener)null);
        }
        if ("Y".equalsIgnoreCase(postingInfo.copyrightYn)) {
            FileUtils.deleteFile(new File(postingInfo.originalMediaPath));
        }
    }

    public Map makeVideoParamForRepic(final PostingInfo postingInfo) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("fileType", "CLIP");
        hashMap.put("copyrightYn", postingInfo.copyrightYn);
        hashMap.put("picThumbnailUrl", postingInfo.videoThumbUrl);
        hashMap.put("picThumbnailWidth", String.valueOf(postingInfo.vidWidth));
        hashMap.put("picThumbnailHeight", String.valueOf(postingInfo.vidHeight));
        hashMap.put("picVid", postingInfo.vid);
        hashMap.put("body", postingInfo.body);
        hashMap.put("parentPicNo", String.valueOf(postingInfo.parentPicNo));
        hashMap.put("parentMemberNo", String.valueOf(postingInfo.parentMemberNo));
        if (postingInfo.locationData != null) {
            hashMap.put("latitude", String.valueOf(postingInfo.locationData.latitude));
            hashMap.put("longitude", String.valueOf(postingInfo.locationData.longitude));
            hashMap.put("code", String.valueOf(postingInfo.locationData.placeId));
            hashMap.put("name", postingInfo.locationData.main);
            hashMap.put("address", postingInfo.locationData.address);
        }
        return hashMap;
    }
//
//    public void requestVideoPost(final PostingInfo postingInfo, final EventBus eventBus, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
//        final MapiaPostRequest mapiaPostRequest = new MapiaPostRequest(QueryManager.makePostEpicUrl(postingInfo.snsParam), this.makeVideoParam(postingInfo), new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                PostingManager.this.mListRequest.remove(this);
//                PostingManager.propagatePicAdded(eventBus);
//                if (listener != null) {
//                    listener.onResponse(jsonObject);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(final VolleyError volleyError) {
//                if (errorListener != null) {
//                    errorListener.onErrorResponse(volleyError);
//                }
//                PostingManager.this.mListRequest.remove(this);
//            }
//        });
//        this.mListRequest.add(mapiaPostRequest);
//        mapiaPostRequest.setRetryPolicy(new DefaultRetryPolicy(100000, 0, 1.0f));
//        MapiaVolley.getRequestQueue().add((Request<Object>)mapiaPostRequest);
//    }

//    public void requestVideoRepic(final PostingInfo postingInfo, final EventBus eventBus, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
//        final MapiaPostRequest mapiaPostRequest = new MapiaPostRequest(QueryManager.makePostEpicUrl(postingInfo.snsParam), this.makeVideoParamForRepic(postingInfo), new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                PostingManager.this.mListRequest.remove(this);
//                PostingManager.propagatePicAdded(eventBus);
//                if (listener != null) {
//                    listener.onResponse(jsonObject);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(final VolleyError volleyError) {
//                if (errorListener != null) {
//                    errorListener.onErrorResponse(volleyError);
//                }
//                PostingManager.this.mListRequest.remove(this);
//            }
//        });
//        this.mListRequest.add(mapiaPostRequest);
//        mapiaPostRequest.setRetryPolicy(new DefaultRetryPolicy(100000, 0, 1.0f));
//        MapiaVolley.getRequestQueue().add((Request<Object>)mapiaPostRequest);
//    }
}