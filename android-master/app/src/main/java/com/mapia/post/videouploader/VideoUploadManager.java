package com.mapia.post.videouploader;

/**
 * Created by daehyun on 15. 6. 13..
 */

import android.content.Context;
import android.os.Environment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mapia.api.QueryManager;
import com.volley.MapiaRequest;
import com.volley.MapiaVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class VideoUploadManager implements FileSplitEvent
{
    private final long CHUNK_FILE_SIZE;
    private final int MAX_PARALLEL_UPLOAD;
    private final String NELO_ERROR_CODE_VIDEO;
    private final int SERVICE_ID_REAL;
    private String mCacheDir;
    private ArrayList<FileChunk> mFileChunks;
    private File mInputFile;
    private long mTotalChunkCount;
    private String mUploadKey;
    private int mUploadedChunk;
    private JSONArray mUploadedChunkResults;
    private String mUploadedVid;
    private int mUploadingCount;
    private VideoUploaderEvent mVideoUploadListener;
    private boolean onError;

    public VideoUploadManager(final Context context, final File mInputFile, final long chunk_FILE_SIZE) {
        super();
        this.NELO_ERROR_CODE_VIDEO = "video";
        this.SERVICE_ID_REAL = 31;
        this.MAX_PARALLEL_UPLOAD = 2;
        this.mInputFile = mInputFile;
        this.mCacheDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
        this.CHUNK_FILE_SIZE = chunk_FILE_SIZE;
        this.mTotalChunkCount = this.mInputFile.length() / this.CHUNK_FILE_SIZE;
        if (this.mInputFile.length() % this.CHUNK_FILE_SIZE != 0L) {
            ++this.mTotalChunkCount;
        }
        this.mFileChunks = new ArrayList<FileChunk>();
        this.mUploadedChunkResults = new JSONArray();
        this.mUploadedChunk = 0;
        this.mUploadingCount = 0;
        this.onError = false;
    }

    private void decreaseUploadingCount() {
        synchronized (this) {
            --this.mUploadingCount;
        }
    }

    private int getUploadingCount() {
        synchronized (this) {
            return this.mUploadingCount;
        }
    }

    private void increaseUploadingCount() {
        synchronized (this) {
            ++this.mUploadingCount;
        }
    }

    private void onUploadCompleted() {
        this.requestCheckUploadCompleted();
    }

    private void onUploadKeyAcquired() {
//        final FileSplitter fileSplitter = new FileSplitter(this.mCacheDir);
//        fileSplitter.setFileSplitEventListener((FileSplitter.FileSplitEvent)this);
//        fileSplitter.splitFile(this.mInputFile, this.CHUNK_FILE_SIZE);
    }

    private void onUploadProcessCompleted() {
        this.requestGetVideoInfo();
    }

    private void parseCheckUploadCompleteResponse(final JSONObject jsonObject) {
        try {
            if (jsonObject.getBoolean("resultCode")) {
                this.onUploadProcessCompleted();
                return;
            }
            this.propagateError();
        }
        catch (JSONException ex) {
//            NeloLog.debug((Throwable)ex, "video", "checkUploadedChunkInfo : " + this.mInputFile.getName());
            this.propagateError();
            ex.printStackTrace();
        }
    }

    private void parseGetVideoInfoResponse(JSONObject jsonObject) {
        try {
            if (!jsonObject.getBoolean("resultCode")) {
                this.propagateError();
                return;
            }
            jsonObject = jsonObject.getJSONObject("data");
            this.mUploadedVid = jsonObject.getString("videoId");
            final String string = jsonObject.getString("logoImage");
            if (this.mVideoUploadListener != null) {
                this.mVideoUploadListener.onUploadFinished(string, this.mUploadedVid);
            }
        }
        catch (JSONException ex) {
//            NeloLog.debug((Throwable)ex, "video", "getVideoInfo : " + this.mInputFile.getName());
            this.propagateError();
            ex.printStackTrace();
        }
    }

    private void parseUploadKeyResponse(final JSONObject jsonObject) {
        this.mUploadKey = null;
        try {
            if (jsonObject.getBoolean("resultCode")) {
                this.mUploadKey = jsonObject.getString("key");
                this.onUploadKeyAcquired();
            }
        }
        catch (JSONException ex) {
//            NeloLog.debug((Throwable)ex, "video", "UploadKey : " + this.mInputFile.getName());
            ex.printStackTrace();
            this.mUploadKey = null;
            if (this.mVideoUploadListener != null) {
                this.mVideoUploadListener.onError();
            }
        }
    }

    private void parseUploadResponse(final String s) {
        try {
            final JSONObject jsonObject = new JSONObject(s);
            if (!jsonObject.getBoolean("resultCode")) {
                this.propagateError();
                return;
            }
            this.mUploadedChunkResults.put((Object)jsonObject.getJSONObject("data"));
            if (this.mUploadedChunkResults.length() == this.mTotalChunkCount) {
                this.onUploadCompleted();
                return;
            }
        }
        catch (JSONException ex) {
//            NeloLog.debug((Throwable)ex, "video", "uploadChunk : " + this.mInputFile.getName());
            ex.printStackTrace();
            this.propagateError();
            return;
        }
        this.tryUpload();
    }

    private void propagateError() {
        if (this.mVideoUploadListener != null) {
            this.mVideoUploadListener.onError();
        }
    }

    private void requestCheckUploadCompleted() {
        final MapiaRequest mapiaRequest = new MapiaRequest(1, QueryManager.makeCheckUploadCompleteUrl(this.mUploadKey, this.mInputFile.getName(), this.mUploadedChunkResults), (JSONObject)null, new Response.Listener<JSONObject>() {
            public void onResponse(final JSONObject jsonObject) {
                VideoUploadManager.this.parseCheckUploadCompleteResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
//                NeloLog.debug(volleyError, "video", "checkUploadedChunkInfo : " + VideoUploadManager.this.mInputFile.getName());
                VideoUploadManager.this.propagateError();
                volleyError.printStackTrace();
            }
        });
        mapiaRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1.0f));
        MapiaVolley.getRequestQueue().add(mapiaRequest);
    }

    private void requestGetVideoInfo() {
        final MapiaRequest mapiaRequest = new MapiaRequest(QueryManager.makeGetVideoInfoUrl(this.mUploadKey), (JSONObject)null, new Response.Listener<JSONObject>() {
            public void onResponse(final JSONObject jsonObject) {
                VideoUploadManager.this.parseGetVideoInfoResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
//                NeloLog.debugbug(volleyError, "video", "getVideoInfo : " + VideoUploadManager.this.mInputFile.getName());
                VideoUploadManager.this.propagateError();
                volleyError.printStackTrace();
            }
        });
        mapiaRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1.0f));
        MapiaVolley.getRequestQueue().add(mapiaRequest);
    }

//    private void requestUpload(final String s, final String s2, final FileChunk fileChunk) {
//        final MapiaVideoUploadRequest mapiaVideoUploadRequest = new MapiaVideoUploadRequest(QueryManager.makeUploadUrl(s, fileChunk.sequence + 1, this.CHUNK_FILE_SIZE, this.mInputFile.length(), s2), fileChunk.file, null, new Response.Listener<String>() {
//            public void onResponse(final String s) {
//                VideoUploadManager.this.decreaseUploadingCount();
//                VideoUploadManager.this.parseUploadResponse(s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(final VolleyError volleyError) {
////                NeloLog.debug(volleyError, "video", "uploadChunk : " + VideoUploadManager.this.mInputFile.getName());
//                VideoUploadManager.this.decreaseUploadingCount();
//                VideoUploadManager.this.propagateError();
//            }
//        });
//        this.increaseUploadingCount();
//        mapiaVideoUploadRequest.setRetryPolicy(new DefaultRetryPolicy(1000000, 0, 1.0f));
//        MapiaVolley.getRequestQueue().add((Request<Object>)mapiaVideoUploadRequest);
//    }

    private void tryUpload() {
//        if (this.mUploadedChunkResults.length() < this.mTotalChunkCount && this.getUploadingCount() < 2 && this.mFileChunks.size() > 0) {
//            this.requestUpload(this.mUploadKey, this.mInputFile.getName(), this.mFileChunks.remove(0));
//        }
    }

    @Override
    public void onFileSplit(final FileChunk fileChunk) {
        this.mFileChunks.add(fileChunk);
        this.tryUpload();
    }

    public void requestUploadKey() {
//        final MapiaRequest mapiaRequest = new MapiaRequest(QueryManager.makeGetUploadKeyUrl(this.mInputFile.getName(), this.mInputFile.length(), this.CHUNK_FILE_SIZE), (JSONObject)null, new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                VideoUploadManager.this.parseUploadKeyResponse(jsonObject);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(final VolleyError volleyError) {
//                NeloLog.debug(volleyError, "video", "UploadKey : " + VideoUploadManager.this.mInputFile.getName());
//                if (VideoUploadManager.this.mVideoUploadListener != null) {
//                    VideoUploadManager.this.mVideoUploadListener.onError();
//                }
//            }
//        });
//        mapiaRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1.0f));
//        MapiaVolley.getRequestQueue().add((Request<Object>)mapiaRequest);
    }

    public void setVideoUploadEventListener(final VideoUploaderEvent mVideoUploadListener) {
        this.mVideoUploadListener = mVideoUploadListener;
    }

    public interface VideoUploaderEvent
    {
        void onError();

        void onUploadFinished(String p0, String p1);
    }
}