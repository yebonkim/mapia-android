package com.mapia.util;

import android.content.SharedPreferences;

import com.mapia.MainApplication;

/**
 * Created by daehyun on 15. 6. 5..
 */
public class PreferenceUtils {
    private static SharedPreferences mapiaLocalCache;
    public static boolean getBooleanPreference(String paramString, boolean paramBoolean){
        mapiaLocalCache = MainApplication.getContext().getSharedPreferences("mapia_shared_preferences", 0);
        return mapiaLocalCache.getBoolean(paramString, paramBoolean);
    }

    public static String getPreference(String paramString){
        mapiaLocalCache = MainApplication.getContext().getSharedPreferences("mapia_shared_preferences",0);
        return mapiaLocalCache.getString(paramString, "");
    }
    public static void putPreference(String paramString1, String paramString2){
        mapiaLocalCache = MainApplication.getContext().getSharedPreferences("mapia_shared_preferences",0);
        SharedPreferences.Editor localEditor = mapiaLocalCache.edit();
        localEditor.putString(paramString1, paramString2);
        localEditor.commit();
    }

}
