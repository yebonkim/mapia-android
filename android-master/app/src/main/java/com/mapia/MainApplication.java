package com.mapia;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mapia.api.MACManager;
import com.mapia.api.PostingManager;
import com.mapia.api.Type;
import com.mapia.camera.Util;
import com.mapia.home.HomeActivity;
import com.mapia.login.LoginInfo;
import com.mapia.map.MapPrivateFragment;
import com.mapia.map.MapPublicFragment;
import com.mapia.myfeed.MyfeedActivity;
import com.mapia.post.PostActivity;
import com.mapia.post.PostFragment;
import com.mapia.post.PostingInfo;
import com.mapia.post.videouploader.VideoUploadManager;
import com.mapia.search.SearchActivity;
import com.mapia.videoplayer.VideoSDKListener;
import com.volley.MapiaVolley;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by daehyun on 15. 6. 2..
 */
public class MainApplication extends Application {//MultiDexApplication {

    private static Context context;
    private static boolean isMenubarClicked;
    private static LoginInfo loginInfo;
    private static MainApplication mainApplication;
//    private EventBus eventBus;
    private HomeActivity homeActivity;
    private MyfeedActivity myfeedActivity;
//    private NotiActivity notiActivity;
    private Intent npushServiceIntent;
    private Response.Listener<JSONObject> onVideoPostingComplete;
    private Response.ErrorListener onVideoPostingFailed;
    private PostActivity postActivity;
    private PostingInfo postingInfo;
//    private ProfileActivity profileActivity;
    private boolean requestUpload;
    private SearchActivity searchActivity;
    private MainActivity topActivity;
    private Bitmap uploadingVidThumb;
    private VideoSDKListener videoSDKListener;
    private VideoUploadManager videoUploader;

    private MapPublicFragment mapPublicFragment;
    private MapPrivateFragment mapPrivateFragment;
    private int videoUploadingStatus;

    public MainApplication(){
        super();
        this.videoUploadingStatus = -1;
        this.videoSDKListener = new VideoSDKListener() {
            @Override
            public void onError(final int n, final int n2) {
                MainApplication.this.requestUpload = false;
                MainApplication.getInstance().propagateBGUploadingStatus(4);
            }

            @Override
            public void onProgress(final int n, final VideoSDKListener.STATUS status, final int n2) {
                if (status != VideoSDKListener.STATUS.PROGRESSING && status == VideoSDKListener.STATUS.COMPLETE) {
                    MainApplication.getInstance().propagateBGUploadingStatus(5);
                    if (MainApplication.this.postingInfo.postingMediaPath != null) {
                        MainApplication.this.postingInfo.file = new File(MainApplication.this.postingInfo.postingMediaPath);
                    }
                    MainApplication.getInstance().uploadVideo();
                    final MainActivity topActivity = MainApplication.this.getTopActivity();
                    if (topActivity != null && topActivity instanceof PostActivity && topActivity.getCurrFragment() instanceof PostFragment) {
                        ((PostFragment)topActivity.getCurrFragment()).onCompleteVideoEncoding();
                    }
                }
            }
        };
        this.onVideoPostingComplete = new Response.Listener<JSONObject>() {
            public void onResponse(final JSONObject jsonObject) {
                if (MainApplication.this.postingInfo.mediaType.equals("CLIP")) {
                    PostingManager.removeTempVideoFile(MainApplication.getContext(), MainApplication.this.postingInfo);
                }
                MainApplication.this.requestUpload = false;
                MainApplication.this.propagateBGUploadingStatus(1);
                MainApplication.this.clearBGUpload();
            }
        };
        this.onVideoPostingFailed = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                MainApplication.this.propagateBGUploadingStatus(2);
            }
        };
    }
//
//    @Override
//    protected void attachBaseContext(Context base){
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    public static MainApplication getInstance(){
        synchronized (MainApplication.class) {

            return mainApplication;
        }
    }
    public void clearBGUpload() {
        this.videoUploadingStatus = -1;
        this.postingInfo = new PostingInfo();
        this.postingInfo.copyrightYn = "Y";
    }

    public HomeActivity getHomeActivity(){
        return this.homeActivity;
    }
    public void setHomeActivity(HomeActivity paramHomeActivity){
        this.homeActivity = paramHomeActivity;

    }

    public void onCreate(){
        super.onCreate();
        MainApplication.mainApplication = this;
        MainApplication.context = this.getApplicationContext();
        MapiaVolley.init(this);
//        NeoIdSdkManager.init(this.getApplicationContext());
//        NeoIdSdkManager.setContentParser(new MapiaNeoIdContentParser());
        (MainApplication.loginInfo = LoginInfo.getInstance()).loadLoginInfo(this.getApplicationContext());
//        this.initNelo();
//        AceUtils.init((Context)this);
//        this.eventBus = new EventBus();
        Util.initialize((Context) this);
//        this.startNpushService();
//        this.initNotice();
//        LineSdkContextManager.initialize((Context)this);
//        this.initNetworkStatus();

            try {
                MACManager.initialize(Type.KEY, "t94hsFVgJJF7RF4QIWbg0r1uzGrcPIUnMcHcgasZufK0QgRcZdv1bpaSYgQqr6Xz");
//                NeoIdSdkManager.setQueryGenerator(new MapiaQueryGenerator());
                this.postingInfo = new PostingInfo();
                this.postingInfo.copyrightYn = "Y";
//                AceUtils.nClick(NClicks.EXE);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }


    public void setMapPublicFragment(MapPublicFragment mapPublicFragment){
        this.mapPublicFragment = mapPublicFragment;
    }

    public MapPublicFragment getMapPublicFragment(){
        return this.mapPublicFragment;
    }
    public void setRequestUpload(final boolean requestUpload) {
        this.requestUpload = requestUpload;
    }

    public void setSearchActivity(SearchActivity searchActivity){
        this.searchActivity = searchActivity;
    }
    public SearchActivity getSearchActivity(){
        return this.searchActivity;
    }
    public static Context getContext(){
        return context;
    }


    public PostActivity getPostActivity()
    {
        return postActivity;
    }

    public void setPostActivity(PostActivity postactivity)
    {
        postActivity = postactivity;
    }


    public MyfeedActivity getMyfeedActivity() {
        return this.myfeedActivity;
    }
    public PostingInfo getPostingInfo() {
        return this.postingInfo;
    }

    public Bitmap getUploadingVidThumb() {
        return this.uploadingVidThumb;
    }


    public int getUploadingStatus() {
        return this.videoUploadingStatus;
    }

    public void setUploadingVidThumb(final Bitmap uploadingVidThumb) {
        this.uploadingVidThumb = uploadingVidThumb;
    }

    public void setMyfeedActivity(final MyfeedActivity myfeedActivity) {
        this.myfeedActivity = myfeedActivity;
    }

    public MainActivity getTopActivity() {
        return this.topActivity;
    }
    public void setIsMenubarClicked(final boolean isMenubarClicked) {
        MainApplication.isMenubarClicked = isMenubarClicked;
    }

    public void propagateBGUploadingStatus(final int videoUploadingStatus) {
        this.videoUploadingStatus = videoUploadingStatus;
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("event", "ebu");
        hashMap.put("status", "" + videoUploadingStatus);
//        this.eventBus.post(hashMap);
    }

    public void uploadVideo() {
        if (!this.requestUpload || (getInstance().getUploadingStatus() != 5 && getInstance().getUploadingStatus() != 2)) {
            return;
        }
        if (this.postingInfo.file == null) {
            getInstance().propagateBGUploadingStatus(2);
            return;
        }
        final VideoUploadManager videoUploadManager = new VideoUploadManager((Context)this, this.postingInfo.file, 5242880L);
        videoUploadManager.setVideoUploadEventListener((VideoUploadManager.VideoUploaderEvent)new VideoUploadManager.VideoUploaderEvent() {
            @Override
            public void onError() {
                MainApplication.this.propagateBGUploadingStatus(2);
            }

            @Override
            public void onUploadFinished(final String s, final String vid) {
                MainApplication.this.postingInfo.vid = vid;
                MainApplication.this.postingInfo.videoThumbUrl = s.replace("16x9", "origin");
                new Handler(MainApplication.this.getMainLooper()).postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        MainApplication.this.postVideo();
                    }
                }, 15000L);
            }
        });
        this.propagateBGUploadingStatus(0);
        videoUploadManager.requestUploadKey();
    }

    private void postVideo() {
        final PostingManager postingManager = new PostingManager();
        switch (this.postingInfo.mode) {
            default: {}
            case 0:
            case 3: {
//                postingManager.requestVideoPost(this.postingInfo, this.eventBus, this.onVideoPostingComplete, this.onVideoPostingFailed);
            }
            case 2: {
//                postingManager.requestVideoRepic(this.postingInfo, this.eventBus, this.onVideoPostingComplete, this.onVideoPostingFailed);
            }
        }
    }

//    public EventBus getEventBus() {
//        if (this.eventBus == null) {
//            this.eventBus = new EventBus();
//        }
//        return this.eventBus;
//    }


}
