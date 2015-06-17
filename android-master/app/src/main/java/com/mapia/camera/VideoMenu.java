package com.mapia.camera;

import android.view.View;

import com.mapia.camera.ui.PieRenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class VideoMenu extends PieController
{
    private static String TAG;
    private CameraActivity mActivity;
    private VideoUI mUI;

    static {
        VideoMenu.TAG = "CAM_VideoMenu";
    }

    public VideoMenu(final CameraActivity mActivity, final VideoUI mui, final PieRenderer pieRenderer) {
        super(mActivity, pieRenderer);
        this.mUI = mui;
        this.mActivity = mActivity;
    }

    @Override
    public void initialize(final PreferenceGroup preferenceGroup) {
        super.initialize(preferenceGroup);
        if (preferenceGroup.findPreference("pref_camera_id_key") != null) {
            (this.switchCameraButton = this.mActivity.getCameraSwitchButton()).setVisibility(View.VISIBLE);
            this.switchCameraButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
//                    AceUtils.nClick(NClicks.CAMERA_ROTATION);
                    final ListPreference preference = VideoMenu.this.mPreferenceGroup.findPreference("pref_camera_id_key");
                    if (preference != null) {
                        final int indexOfValue = preference.findIndexOfValue(preference.getValue());
                        final CharSequence[] entryValues = preference.getEntryValues();
                        VideoMenu.this.mListener.onCameraPickerClicked(Integer.parseInt((String)entryValues[(indexOfValue + 1) % entryValues.length]));
                    }
                }
            });
        }
        if (preferenceGroup.findPreference("pref_camera_video_flashmode_key") != null) {
            final IconListPreference iconListPreference = (IconListPreference)this.mPreferenceGroup.findPreference("pref_camera_video_flashmode_key");
            final int imageResource = iconListPreference.getLargeIconIds()[iconListPreference.findIndexOfValue(iconListPreference.getValue())];
            (this.flashView = this.mActivity.getFlashView()).setVisibility(View.VISIBLE);
            this.flashView.setImageResource(imageResource);
            this.flashView.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    VideoMenu.this.setFlashPref(iconListPreference);
                }
            });
        }
    }
}