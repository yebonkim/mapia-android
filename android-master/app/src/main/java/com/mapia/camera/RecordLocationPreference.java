package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;

public class RecordLocationPreference extends IconListPreference
{
    public static final String VALUE_NONE = "none";
    public static final String VALUE_OFF = "off";
    public static final String VALUE_ON = "on";
    private final ContentResolver mResolver;

    public RecordLocationPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.mResolver = context.getContentResolver();
    }

    public static boolean get(final SharedPreferences sharedPreferences, final ContentResolver contentResolver) {
        return "on".equals(sharedPreferences.getString("pref_camera_recordlocation_key", "none"));
    }

    public static boolean isSet(final SharedPreferences sharedPreferences) {
        return !"none".equals(sharedPreferences.getString("pref_camera_recordlocation_key", "none"));
    }

    @Override
    public String getValue() {
        if (get(this.getSharedPreferences(), this.mResolver)) {
            return "on";
        }
        return "off";
    }
}