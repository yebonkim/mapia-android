package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mapia.camera.ui.PieItem;
import com.mapia.camera.ui.PieRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PieController
{
    protected static float CENTER = 0.0f;
    protected static final int MODE_PHOTO = 0;
    protected static final int MODE_VIDEO = 1;
    protected static final float SWEEP = 0.06f;
    private static String TAG;
    protected ImageView flashView;
    protected Activity mActivity;
    protected CameraPreference.OnPreferenceChangedListener mListener;
    private Map<IconListPreference, String> mOverrides;
    protected PreferenceGroup mPreferenceGroup;
    private Map<IconListPreference, PieItem> mPreferenceMap;
    private List<IconListPreference> mPreferences;
    protected PieRenderer mRenderer;
    protected View switchCameraButton;

    static {
        PieController.TAG = "CAM_piecontrol";
        PieController.CENTER = 1.5707964f;
    }

    public PieController(final Activity mActivity, final PieRenderer mRenderer) {
        super();
        this.mActivity = mActivity;
        this.mRenderer = mRenderer;
        this.mPreferences = new ArrayList<IconListPreference>();
        this.mPreferenceMap = new HashMap<IconListPreference, PieItem>();
        this.mOverrides = new HashMap<IconListPreference, String>();
    }

    private void override(final IconListPreference iconListPreference, final String... array) {
        this.mOverrides.remove(iconListPreference);
        for (int i = 0; i < array.length; i += 2) {
            final String s = array[i];
            final String s2 = array[i + 1];
            if (s.equals(iconListPreference.getKey())) {
                this.mOverrides.put(iconListPreference, s2);
                this.mPreferenceMap.get(iconListPreference).setEnabled(s2 == null);
                break;
            }
        }
        this.reloadPreference(iconListPreference);
    }

    public void initialize(final PreferenceGroup preferenceGroup) {
        this.mPreferenceMap.clear();
        this.setPreferenceGroup(preferenceGroup);
        if (this.flashView != null) {
            this.flashView.setVisibility(View.INVISIBLE);
            this.flashView = null;
        }
        if (this.switchCameraButton != null) {
            this.switchCameraButton.setVisibility(View.INVISIBLE);
            this.switchCameraButton = null;
        }
    }

    public void onSettingChanged(final ListPreference listPreference) {
        if (this.mListener != null) {
            this.mListener.onSharedPreferenceChanged();
        }
    }

    public void overrideSettings(final String... array) {
        if (array.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        final Iterator<IconListPreference> iterator = this.mPreferenceMap.keySet().iterator();
        while (iterator.hasNext()) {
            this.override(iterator.next(), array);
        }
        if (this.flashView != null) {
            this.flashView.setVisibility(View.VISIBLE);
        }
    }

    protected void reloadPreference(final IconListPreference iconListPreference) {
        if (iconListPreference.getUseSingleIcon()) {
            return;
        }
        final PieItem pieItem = this.mPreferenceMap.get(iconListPreference);
        final String s = this.mOverrides.get(iconListPreference);
        final int[] largeIconIds = iconListPreference.getLargeIconIds();
        if (largeIconIds != null) {
            int n;
            if (s == null) {
                n = iconListPreference.findIndexOfValue(iconListPreference.getValue());
            }
            else if ((n = iconListPreference.findIndexOfValue(s)) == -1) {
                Log.e(PieController.TAG, "Fail to find override value=" + s);
                iconListPreference.print();
                return;
            }
            pieItem.setImageResource((Context)this.mActivity, largeIconIds[n]);
            return;
        }
        pieItem.setImageResource((Context)this.mActivity, iconListPreference.getSingleIcon());
    }

    public void reloadPreferences() {
        this.mPreferenceGroup.reloadValue();
        final Iterator<IconListPreference> iterator = this.mPreferenceMap.keySet().iterator();
        while (iterator.hasNext()) {
            this.reloadPreference(iterator.next());
        }
    }

    protected void setCameraId(final int n) {
        this.mPreferenceGroup.findPreference("pref_camera_id_key").setValue("" + n);
    }

    protected void setFlashPref(final IconListPreference iconListPreference) {
//        AceUtils.nClick(NClicks.CAMERA_FLASH);
        final int valueIndex = (iconListPreference.getCurrentIndex() + 1) % iconListPreference.getLargeIconIds().length;
        iconListPreference.setValueIndex(valueIndex);
        this.flashView.setImageResource(iconListPreference.getLargeIconIds()[valueIndex]);
        this.onSettingChanged(iconListPreference);
    }

    public void setListener(final CameraPreference.OnPreferenceChangedListener mListener) {
        this.mListener = mListener;
    }

    public void setPreferenceGroup(final PreferenceGroup mPreferenceGroup) {
        this.mPreferenceGroup = mPreferenceGroup;
    }
}