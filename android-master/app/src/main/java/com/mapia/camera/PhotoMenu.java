package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.mapia.R;
import com.mapia.camera.ui.PieRenderer;
import com.mapia.camera.ui.VerticalSeekBar;

import java.util.Locale;


public class PhotoMenu extends PieController
{
    private static String TAG;
    private static final int[] TIMER_DRAWS_IDS;
    private VerticalSeekBar exposureSeekBar;
    private CameraActivity mActivity;
    private final String mSettingOff;
    private ImageView mTimerView;
    private PhotoUI mUI;

    static {
        PhotoMenu.TAG = "CAM_photomenu";
        TIMER_DRAWS_IDS = new int[] { 2130837589, 2130837582, 2130837583 };
    }

    public PhotoMenu(final CameraActivity mActivity, final PhotoUI mui, final PieRenderer pieRenderer) {
        super(mActivity, pieRenderer);
        this.mUI = mui;
        this.mSettingOff = mActivity.getString(R.string.setting_off_value);
        this.mActivity = mActivity;
        if (this.exposureSeekBar != null) {
            this.exposureSeekBar.setVisibility(View.INVISIBLE);
            this.exposureSeekBar = null;
        }
        this.mTimerView = this.mActivity.getTimerImageView();
    }

    private static boolean notSame(final ListPreference listPreference, final String s, final String s2) {
        return s.equals(listPreference.getKey()) && !s2.equals(listPreference.getValue());
    }

    private void setPreference(final String s, final String value) {
        final ListPreference preference = this.mPreferenceGroup.findPreference(s);
        if (preference != null && !value.equals(preference.getValue())) {
            preference.setValue(value);
            this.reloadPreferences();
        }
    }

    public VerticalSeekBar getExposureSeekBar() {
        return this.exposureSeekBar;
    }

    @Override
    public void initialize(final PreferenceGroup preferenceGroup) {
        super.initialize(preferenceGroup);
        final Locale locale = this.mActivity.getResources().getConfiguration().locale;
        if (preferenceGroup.findPreference("pref_camera_hdr_key") == null) {}
        if (preferenceGroup.findPreference("pref_camera_exposure_key") != null) {
            final IconListPreference iconListPreference = (IconListPreference)this.mPreferenceGroup.findPreference("pref_camera_exposure_key");
            this.exposureSeekBar = this.mActivity.getExposureSeekBar();
            final CharSequence[] entries = iconListPreference.getEntries();
            this.exposureSeekBar.setMax(entries.length - 1);
            for (int i = 0; i < entries.length; ++i) {
                if (entries[i].equals(iconListPreference.getValue())) {
                    this.exposureSeekBar.setProgress(i);
                }
            }
            this.exposureSeekBar.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener)new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(final SeekBar seekBar, final int valueIndex, final boolean b) {
                    iconListPreference.setValueIndex(valueIndex);
                    PhotoMenu.this.reloadPreference(iconListPreference);
                    PhotoMenu.this.onSettingChanged(iconListPreference);
                }

                public void onStartTrackingTouch(final SeekBar seekBar) {
                }

                public void onStopTrackingTouch(final SeekBar seekBar) {
                }
            });
        }
        if (preferenceGroup.findPreference("pref_camera_flashmode_key") != null) {
            final IconListPreference iconListPreference2 = (IconListPreference)this.mPreferenceGroup.findPreference("pref_camera_flashmode_key");
            (this.flashView = this.mActivity.getFlashView()).setImageResource(iconListPreference2.getLargeIconIds()[iconListPreference2.findIndexOfValue(iconListPreference2.getValue())]);
            this.flashView.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
                    PhotoMenu.this.setFlashPref(iconListPreference2);
                }
            });
        }
        if (preferenceGroup.findPreference("pref_camera_id_key") != null) {
            (this.switchCameraButton = this.mActivity.getCameraSwitchButton()).setVisibility(View.VISIBLE);
            this.switchCameraButton.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
//                    AceUtils.nClick(NClicks.CAMERA_ROTATION);
                    final ListPreference preference = PhotoMenu.this.mPreferenceGroup.findPreference("pref_camera_id_key");
                    if (preference != null) {
                        final int valueIndex = (preference.findIndexOfValue(preference.getValue()) + 1) % preference.getEntryValues().length;
                        preference.setValueIndex(valueIndex);
                        PhotoMenu.this.mListener.onCameraPickerClicked(valueIndex);
                    }
                }
            });
        }
        if (preferenceGroup.findPreference("pref_camera_recordlocation_key") == null) {}
        final ListPreference preference = preferenceGroup.findPreference("pref_camera_timer_key");
        preferenceGroup.findPreference("pref_camera_timer_sound_key").setValueIndex(0);
        this.mTimerView.setImageResource(PhotoMenu.TIMER_DRAWS_IDS[preference.getCurrentIndex()]);
        this.mTimerView.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.CAMERA_TIMER);
                final int valueIndex = (preference.getCurrentIndex() + 1) % preference.getEntryValues().length;
                preference.setValueIndex(valueIndex);
                PhotoMenu.this.mTimerView.setImageResource(PhotoMenu.TIMER_DRAWS_IDS[valueIndex]);
                PhotoMenu.this.onSettingChanged(preference);
            }
        });
        if (preferenceGroup.findPreference("pref_camera_whitebalance_key") == null) {}
        if (preferenceGroup.findPreference("pref_camera_scenemode_key") != null) {
            return;
        }
    }

    @Override
    public void onSettingChanged(final ListPreference listPreference) {
        if (notSame(listPreference, "pref_camera_hdr_key", this.mSettingOff)) {
            this.setPreference("pref_camera_scenemode_key", "auto");
        }
        else if (notSame(listPreference, "pref_camera_scenemode_key", "auto")) {
            this.setPreference("pref_camera_hdr_key", this.mSettingOff);
        }
        super.onSettingChanged(listPreference);
    }

    public void reloadExposurePreference(final int valueIndex) {
        final IconListPreference iconListPreference = (IconListPreference)this.mPreferenceGroup.findPreference("pref_camera_exposure_key");
        iconListPreference.setValueIndex(valueIndex);
        this.reloadPreference(iconListPreference);
        this.onSettingChanged(iconListPreference);
    }
}