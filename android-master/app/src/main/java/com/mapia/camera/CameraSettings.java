package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.util.Log;

import com.mapia.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class CameraSettings
{
    public static final int CURRENT_LOCAL_VERSION = 2;
    public static final int CURRENT_VERSION = 5;
    public static final String EXPOSURE_DEFAULT_VALUE = "0";
    public static final String KEY_CAMERA_FIRST_USE_HINT_SHOWN = "pref_camera_first_use_hint_shown_key";
    public static final String KEY_CAMERA_HDR = "pref_camera_hdr_key";
    public static final String KEY_CAMERA_ID = "pref_camera_id_key";
    public static final String KEY_EXPOSURE = "pref_camera_exposure_key";
    public static final String KEY_FLASH_MODE = "pref_camera_flashmode_key";
    public static final String KEY_FOCUS_MODE = "pref_camera_focusmode_key";
    public static final String KEY_GRID_GUIDE = "pref_grid_guide_key";
    public static final String KEY_JPEG_QUALITY = "pref_camera_jpegquality_key";
    public static final String KEY_LOCAL_VERSION = "pref_local_version_key";
    public static final String KEY_PHOTOSPHERE_PICTURESIZE = "pref_photosphere_picturesize_key";
    public static final String KEY_PICTURE_SIZE = "pref_camera_picturesize_key";
    public static final String KEY_RECORD_LOCATION = "pref_camera_recordlocation_key";
    public static final String KEY_SCENE_MODE = "pref_camera_scenemode_key";
    public static final String KEY_SQUARE_VIEW = "pref_square_view_key";
    public static final String KEY_TIMER = "pref_camera_timer_key";
    public static final String KEY_TIMER_SOUND_EFFECTS = "pref_camera_timer_sound_key";
    public static final String KEY_VERSION = "pref_version_key";
    public static final String KEY_VIDEOCAMERA_FLASH_MODE = "pref_camera_video_flashmode_key";
    public static final String KEY_VIDEO_FIRST_USE_HINT_SHOWN = "pref_video_first_use_hint_shown_key";
    public static final String KEY_VIDEO_QUALITY = "pref_video_quality_key";
    public static final String KEY_VIDEO_TIME_LAPSE_FRAME_INTERVAL = "pref_video_time_lapse_frame_interval_key";
    public static final String KEY_WHITE_BALANCE = "pref_camera_whitebalance_key";
    private static final int NOT_FOUND = -1;
    private static final String TAG = "CameraSettings";
    private final int mCameraId;
    private final Camera.CameraInfo[] mCameraInfo;
    private final Context mContext;
    private final Camera.Parameters mParameters;

    public CameraSettings(final Activity mContext, final Camera.Parameters mParameters, final int mCameraId, final Camera.CameraInfo[] mCameraInfo) {
        super();
        this.mContext = (Context)mContext;
        this.mParameters = mParameters;
        this.mCameraId = mCameraId;
        this.mCameraInfo = mCameraInfo;
    }

    private void buildCameraId(final PreferenceGroup preferenceGroup, final IconListPreference iconListPreference) {
        final int length = this.mCameraInfo.length;
        if (length < 2) {
            removePreference(preferenceGroup, iconListPreference.getKey());
            return;
        }
        final CharSequence[] entryValues = new CharSequence[length];
        for (int i = 0; i < length; ++i) {
            entryValues[i] = "" + i;
        }
        iconListPreference.setEntryValues(entryValues);
    }

    private void buildExposureCompensation(final PreferenceGroup preferenceGroup, final IconListPreference iconListPreference) {
    }

    private void filterSimilarPictureSize(final PreferenceGroup preferenceGroup, final ListPreference listPreference) {
        listPreference.filterDuplicated();
        if (listPreference.getEntries().length <= 1) {
            removePreference(preferenceGroup, listPreference.getKey());
            return;
        }
        this.resetIfInvalid(listPreference);
    }

    private void filterUnsupportedOptions(final PreferenceGroup preferenceGroup, final ListPreference listPreference, final List<String> list) {
        if (list == null || list.size() <= 1) {
            removePreference(preferenceGroup, listPreference.getKey());
            return;
        }
        listPreference.filterUnsupported(list);
        if (listPreference.getEntries().length <= 1) {
            removePreference(preferenceGroup, listPreference.getKey());
            return;
        }
        this.resetIfInvalid(listPreference);
    }

    @TargetApi(11)
    public static String getDefaultVideoQuality(final int n, final String s) {
        if (true && CamcorderProfile.hasProfile(n, (int)Integer.valueOf(s))) { //ApiHelper.HAS_FINE_RESOLUTION_QUALITY_LEVELS
            return s;
        }
        return Integer.toString(1);
    }

    @TargetApi(11)
    private void getFineResolutionQuality(final ArrayList<String> list) {
        if (CamcorderProfile.hasProfile(this.mCameraId, 6)) {
            list.add(Integer.toString(6));
        }
        if (CamcorderProfile.hasProfile(this.mCameraId, 5)) {
            list.add(Integer.toString(5));
        }
        if (CamcorderProfile.hasProfile(this.mCameraId, 4)) {
            list.add(Integer.toString(4));
        }
    }

    public static int getMaxVideoDuration(final Context context) {
        try {
            return context.getResources().getInteger(R.integer.max_video_recording_length);
        }
        catch (Resources.NotFoundException ex) {
            return 0;
        }
    }

    private ArrayList<String> getSupportedVideoQuality() {
        final ArrayList<String> list = new ArrayList<String>();
        if (true) {//ApiHelper.HAS_FINE_RESOLUTION_QUALITY_LEVELS
            this.getFineResolutionQuality(list);
        }
        else {
            list.add(Integer.toString(1));
            final CamcorderProfile value = CamcorderProfile.get(this.mCameraId, 1);
            final CamcorderProfile value2 = CamcorderProfile.get(this.mCameraId, 0);
            if (value.videoFrameHeight * value.videoFrameWidth > value2.videoFrameHeight * value2.videoFrameWidth) {
                list.add(Integer.toString(0));
                return list;
            }
        }
        return list;
    }

    public static int getTrottleVideoDuration(final Context context) {
        try {
            return context.getResources().getInteger(R.integer.throttle_video_recording_length);
        }
        catch (Resources.NotFoundException ex) {
            return 0;
        }
    }

    private void initPreference(final PreferenceGroup preferenceGroup) {
        final ListPreference preference = preferenceGroup.findPreference("pref_video_quality_key");
        final ListPreference preference2 = preferenceGroup.findPreference("pref_video_time_lapse_frame_interval_key");
        final ListPreference preference3 = preferenceGroup.findPreference("pref_camera_picturesize_key");
        preferenceGroup.findPreference("pref_camera_whitebalance_key");
        preferenceGroup.findPreference("pref_camera_scenemode_key");
        final ListPreference preference4 = preferenceGroup.findPreference("pref_camera_flashmode_key");
        final ListPreference preference5 = preferenceGroup.findPreference("pref_camera_focusmode_key");
        final IconListPreference iconListPreference = (IconListPreference)preferenceGroup.findPreference("pref_camera_exposure_key");
        final CountDownTimerPreference countDownTimerPreference = (CountDownTimerPreference)preferenceGroup.findPreference("pref_camera_timer_key");
        preferenceGroup.findPreference("pref_camera_timer_sound_key");
        final IconListPreference iconListPreference2 = (IconListPreference)preferenceGroup.findPreference("pref_camera_id_key");
        final ListPreference preference6 = preferenceGroup.findPreference("pref_camera_video_flashmode_key");
        preferenceGroup.findPreference("pref_camera_hdr_key");
        if (preference != null) {
            this.filterUnsupportedOptions(preferenceGroup, preference, this.getSupportedVideoQuality());
        }
        if (preference3 != null) {
            this.filterUnsupportedOptions(preferenceGroup, preference3, sizeListToStringList(this.mParameters.getSupportedPictureSizes()));
            this.filterSimilarPictureSize(preferenceGroup, preference3);
        }
        if (preference4 != null) {
            this.filterUnsupportedOptions(preferenceGroup, preference4, this.mParameters.getSupportedFlashModes());
        }
        if (preference5 != null) {
            if (!Util.isFocusAreaSupported(this.mParameters)) {
                this.filterUnsupportedOptions(preferenceGroup, preference5, this.mParameters.getSupportedFocusModes());
            }
            else {
                removePreference(preferenceGroup, preference5.getKey());
            }
        }
        if (preference6 != null) {
            this.filterUnsupportedOptions(preferenceGroup, preference6, this.mParameters.getSupportedFlashModes());
        }
        if (iconListPreference2 != null) {
            this.buildCameraId(preferenceGroup, iconListPreference2);
        }
        if (preference2 != null) {
            if (!true) {//ApiHelper.HAS_TIME_LAPSE_RECORDING
                removePreference(preferenceGroup, preference2.getKey());
                return;
            }
            this.resetIfInvalid(preference2);
        }
    }

    public static void initialCameraPictureSize(final Context context, final Camera.Parameters cameraParameters) {
        final List supportedPictureSizes = cameraParameters.getSupportedPictureSizes();
        if (supportedPictureSizes == null) {
            return;
        }
        final String[] stringArray = context.getResources().getStringArray(R.array.pref_camera_picturesize_entryvalues);
        for (int length = stringArray.length, i = 0; i < length; ++i) {
            final String s = stringArray[i];
            if (setCameraPictureSize(s, supportedPictureSizes, cameraParameters)) {
                final SharedPreferences.Editor edit = ComboPreferences.get(context).edit();
                edit.putString("pref_camera_picturesize_key", s);
                edit.apply();
                return;
            }
        }
        Log.e("CameraSettings", "No supported picture size found");
    }

    public static int readExposure(ComboPreferences comboPreferences) {
        String s = comboPreferences.getString("pref_camera_exposure_key", "0");
        try {
            return Integer.parseInt((String)s);
        }
        catch (Exception ex) {
            Log.e("CameraSettings", "Invalid exposure: " + s);
            return 0;
        }
    }

    public static int readPreferredCameraId(final SharedPreferences sharedPreferences) {
        return Integer.parseInt(sharedPreferences.getString("pref_camera_id_key", "0"));
    }

    private static boolean removePreference(final PreferenceGroup preferenceGroup, final String s) {
        for (int i = 0; i < preferenceGroup.size(); ++i) {
            final CameraPreference value = preferenceGroup.get(i);
            if (value instanceof PreferenceGroup && removePreference((PreferenceGroup)value, s)) {
                return true;
            }
            if (value instanceof ListPreference && ((ListPreference)value).getKey().equals(s)) {
                preferenceGroup.removePreference(i);
                return true;
            }
        }
        return false;
    }

    public static void removePreferenceFromScreen(final PreferenceGroup preferenceGroup, final String s) {
        removePreference(preferenceGroup, s);
    }

    private void resetIfInvalid(final ListPreference listPreference) {
        if (listPreference.findIndexOfValue(listPreference.getValue()) == -1) {
            listPreference.setValueIndex(0);
        }
    }

    public static void restorePreferences(final Context context, final ComboPreferences comboPreferences, final Camera.Parameters cameraParameters) {
        final int preferredCameraId = readPreferredCameraId((SharedPreferences)comboPreferences);
        final int backCameraId = CameraHolder.instance().getBackCameraId();
        if (backCameraId != -1) {
            comboPreferences.setLocalId(context, backCameraId);
            final SharedPreferences.Editor edit = comboPreferences.edit();
            edit.clear();
            edit.apply();
        }
        final int frontCameraId = CameraHolder.instance().getFrontCameraId();
        if (frontCameraId != -1) {
            comboPreferences.setLocalId(context, frontCameraId);
            final SharedPreferences.Editor edit2 = comboPreferences.edit();
            edit2.clear();
            edit2.apply();
        }
        comboPreferences.setLocalId(context, preferredCameraId);
        upgradeGlobalPreferences(comboPreferences.getGlobal());
        upgradeLocalPreferences(comboPreferences.getLocal());
        initialCameraPictureSize(context, cameraParameters);
        writePreferredCameraId((SharedPreferences)comboPreferences, preferredCameraId);
    }

    public static boolean setCameraPictureSize(final String s, final List<Camera.Size> list, final Camera.Parameters cameraParameters) {
        if (s.indexOf(120) != -1) {
            final Camera.Size cameraSize = null;
            final List<Camera.Size> list2 = new ArrayList<Camera.Size>();
            for (final Camera.Size cameraSize2 : list) {
                if (cameraSize2.height * 4 == cameraSize2.width * 3) {
                    list2.add(cameraSize2);
                }
            }
            if (!list2.isEmpty()) {
                Collections.sort(list2, new SizeAscCompare());
                final Iterator<Camera.Size> iterator2 = list2.iterator();
                Camera.Size cameraSize3;
                do {
                    cameraSize3 = cameraSize;
                    if (!iterator2.hasNext()) {
                        break;
                    }
                    cameraSize3 = iterator2.next();
                } while (1440 > cameraSize3.width || 1080 > cameraSize3.height);
                if (cameraSize3 == null) {
                    final Camera.Size cameraSize4 = (Camera.Size)list2.get(list2.size() - 1);
                    cameraParameters.setPictureSize(cameraSize4.width, cameraSize4.height);
                }
                else {
                    cameraParameters.setPictureSize(cameraSize3.width, cameraSize3.height);
                }
                return true;
            }
        }
        return false;
    }

    private static List<String> sizeListToStringList(final List<Camera.Size> list) {
        final ArrayList<String> list2 = new ArrayList<String>();
        for (final Camera.Size cameraSize : list) {
            list2.add(String.format(Locale.ENGLISH, "%dx%d", cameraSize.width, cameraSize.height));
        }
        return list2;
    }

    private static void upgradeCameraId(final SharedPreferences sharedPreferences) {
        final int preferredCameraId = readPreferredCameraId(sharedPreferences);
        if (preferredCameraId != 0) {
            final int numberOfCameras = CameraHolder.instance().getNumberOfCameras();
            if (preferredCameraId < 0 || preferredCameraId >= numberOfCameras) {
                writePreferredCameraId(sharedPreferences, 0);
            }
        }
    }

    public static void upgradeGlobalPreferences(final SharedPreferences sharedPreferences) {
        upgradeOldVersion(sharedPreferences);
        upgradeCameraId(sharedPreferences);
    }

    public static void upgradeLocalPreferences(final SharedPreferences sharedPreferences) {
        int int1;
        while (true) {
            try {
                int1 = sharedPreferences.getInt("pref_local_version_key", 0);
                if (int1 == 2) {
                    return;
                }
            }
            catch (Exception ex) {
                int1 = 0;
                continue;
            }
            break;
        }
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        if (int1 == 1) {
            edit.remove("pref_video_quality_key");
        }
        edit.putInt("pref_local_version_key", 2);
        edit.apply();
    }

    private static void upgradeOldVersion(final SharedPreferences sharedPreferences) {
        int int1;
        while (true) {
            try {
                int1 = sharedPreferences.getInt("pref_version_key", 0);
                if (int1 == 5) {
                    return;
                }
            }
            catch (Exception ex) {
                int1 = 0;
                continue;
            }
            break;
        }
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        int n;
        if ((n = int1) == 0) {
            n = 1;
        }
        int n2;
        if ((n2 = n) == 1) {
            final String string = sharedPreferences.getString("pref_camera_jpegquality_key", "85");
            String s;
            if (string.equals("65")) {
                s = "normal";
            }
            else if (string.equals("75")) {
                s = "fine";
            }
            else {
                s = "superfine";
            }
            edit.putString("pref_camera_jpegquality_key", s);
            n2 = 2;
        }
        int n3;
        if ((n3 = n2) == 2) {
            String s2;
            if (sharedPreferences.getBoolean("pref_camera_recordlocation_key", false)) {
                s2 = "on";
            }
            else {
                s2 = "none";
            }
            edit.putString("pref_camera_recordlocation_key", s2);
            n3 = 3;
        }
        if (n3 == 3) {
            edit.remove("pref_camera_videoquality_key");
            edit.remove("pref_camera_video_duration_key");
        }
        edit.putInt("pref_version_key", 5);
        edit.apply();
    }

    public static void writePreferredCameraId(final SharedPreferences sharedPreferences, final int n) {
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("pref_camera_id_key", Integer.toString(n));
        edit.apply();
    }

    public PreferenceGroup getPreferenceGroup(final int n) {
        final PreferenceGroup preferenceGroup = (PreferenceGroup)new PreferenceInflater(this.mContext).inflate(n);
        if (this.mParameters != null) {
            this.initPreference(preferenceGroup);
        }
        return preferenceGroup;
    }

    private static class SizeAscCompare implements Comparator<Camera.Size>
    {
        @Override
        public int compare(final Camera.Size cameraSize, final Camera.Size cameraSize2) {
            if (cameraSize.height * cameraSize.width > cameraSize2.height * cameraSize2.width) {
                return 1;
            }
            if (cameraSize.height * cameraSize.width < cameraSize2.height * cameraSize2.width) {
                return -1;
            }
            return 0;
        }
    }
}