package com.mapia.network;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import java.net.CookieHandler;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

public class RestRequestHelper {

    private static RestRequestHelper instance;

    private RestCookieManager cookieManager;
    private RestAdapter restAdapter;
    private RestRequest restRequest;

    public static RestRequestHelper newInstance() {
        if (instance == null) {
            instance = new RestRequestHelper();
        }
        return instance;
    }

    public RestRequestHelper() {
        cookieManager = new com.mapia.network.RestCookieManager();
        CookieHandler.setDefault(cookieManager);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://54.65.32.198:8081")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        // 쿠키 설정
                        request.addHeader("Cookie", cookieManager.getCurrentCookie());
                    }
                })
                .build();

        restRequest = restAdapter.create(RestRequest.class);

    }

    public interface RestRequest {

        @POST("/auth/signup")
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        void signUp(@Body UserJsonRequest userJO,
                    Callback<JsonObject> callback);

//        @FormUrlEncoded
        @POST("/auth/login")
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        void login(@Body UserJsonRequest userJO,
                   Callback<JsonObject> callback);

        @POST("/post")
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        void postPost(@Body PostJsonRequest postJO,
                   Callback<JsonObject> callback);

        @GET("/auth/login")
        void login(Callback<JsonObject> callback);

        @GET("/auth/profile")
        void profile(Callback<JsonObject> callback);

        @GET("/post")
        void getPosts(@Query("map-type") String mapType,
                   @Query("center-latitude")double lat, @Query("center-longitude")double lng,
                   @Query("map-level")float level, Callback<JsonObject> callback);

        @GET("/post?type={map-type}&(group={group-id})&lat={center-latitude}&lng={center-longitude}&level={map-level}")
        void getPosts(@Query("map-type") String mapType, @Query("group-id") int groupID,
                   @Query("center-latitude")double lat, @Query("center-longitude")double lng,
                   @Query("map-level")float level, Callback<JsonObject> callback);

    }

    public void signUp(String id, String password, Callback<JsonObject> callback) {
        UserJsonRequest userJO = null;
        //Since GSON make string as json format while wrapping key 'nameValuePairs' ,
        //그래서 그냥 JSONObject 안쓰고 JsonObject 사용함
        try {
            userJO = new UserJsonRequest(id, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        restRequest.signUp(userJO, callback);
    }

    public void login(Callback<JsonObject> callback) {
        restRequest.login(callback);
    }

    public void login(String id, String password, Callback<JsonObject> callback) {
        UserJsonRequest userJO = null;
        //Since GSON make string as json format while wrapping key 'nameValuePairs' ,
        //그래서 그냥 JSONObject 안쓰고 JsonObject 사용함
        try {
            userJO = new UserJsonRequest(id, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        restRequest.login(userJO, callback);
    }

    public void posts(String mapType, String content, LatLng latlng, Callback<JsonObject> callback){
        PostJsonRequest postJO = null;
        try{
            postJO = new PostJsonRequest(mapType, content, latlng);
        } catch(Exception e){
            e.printStackTrace();
        }
        restRequest.postPost(postJO, callback);
    }

    public void posts(String mapType, String content, LatLng latlng, ArrayList<String> filelist, Callback<JsonObject> callback){
        PostJsonRequest postJO = null;
        try{
            postJO = new PostJsonRequest(mapType, content, latlng, filelist);
        } catch(Exception e){
            e.printStackTrace();
        }
        restRequest.postPost(postJO, callback);
    }

    public void profile(Callback<JsonObject> callback) {
        restRequest.profile(callback);
    }

    public void getPosts(String mapType, double lat, double lng, float level, Callback<JsonObject> callback){
        restRequest.getPosts(mapType, lat, lng, level, callback);
    }
    public void getPosts(String mapType, int groupID, double lat, double lng, float level, Callback<JsonObject> callback){
        restRequest.getPosts(mapType, groupID, lat, lng, level, callback);
    }

}

