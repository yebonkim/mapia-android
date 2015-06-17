package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;


public abstract class CameraPreference
{
    private final Context mContext;
    private SharedPreferences mSharedPreferences;
//    private final String mTitle;

    public CameraPreference(final Context mContext, final AttributeSet set) {
        super();
        this.mContext = mContext;
//        final TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(set, R.styleable.CameraPreference, 0, 0);
//        this.mTitle = obtainStyledAttributes.getString(0);
//        obtainStyledAttributes.recycle();
    }

    public SharedPreferences getSharedPreferences() {
        if (this.mSharedPreferences == null) {
            this.mSharedPreferences = (SharedPreferences)ComboPreferences.get(this.mContext);
        }
        return this.mSharedPreferences;
    }

    public String getTitle() {
        return "";
//        return this.mTitle;
    }

    public abstract void reloadValue();

    public interface OnPreferenceChangedListener
    {
        void onCameraPickerClicked(int p0);

        void onOverriddenPreferencesClicked();

        void onRestorePreferencesClicked();

        void onSharedPreferenceChanged();
    }
}