package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 16..
 */

import org.apache.commons.lang3.StringUtils;

public class CameraUtils
{
    private static String saveOriginalPhotoYn;
    private static String saveMapiaPhotoYn;
    private static String saveMapiaVideoYn;

    static {
        CameraUtils.saveOriginalPhotoYn = null;
        CameraUtils.saveMapiaPhotoYn = null;
        CameraUtils.saveMapiaVideoYn = null;
    }

    public static String getSaveOriginalPhotoYn() {
        if (CameraUtils.saveOriginalPhotoYn != null && StringUtils.isNotBlank(CameraUtils.saveOriginalPhotoYn)) {
            return CameraUtils.saveOriginalPhotoYn;
        }
        CameraUtils.saveOriginalPhotoYn = PreferenceUtils.getPreference("saveOriginalPhotoYn");
        if (CameraUtils.saveOriginalPhotoYn != null && StringUtils.isNotBlank(CameraUtils.saveOriginalPhotoYn)) {
            return CameraUtils.saveOriginalPhotoYn;
        }
        return "Y";
    }

    public static String getSaveMapiaPhotoYn() {
        if (CameraUtils.saveMapiaPhotoYn != null && StringUtils.isNotBlank(CameraUtils.saveMapiaPhotoYn)) {
            return CameraUtils.saveMapiaPhotoYn;
        }
        CameraUtils.saveMapiaPhotoYn = PreferenceUtils.getPreference("saveMapiaPhotoYn");
        if (CameraUtils.saveMapiaPhotoYn != null && StringUtils.isNotBlank(CameraUtils.saveMapiaPhotoYn)) {
            return CameraUtils.saveMapiaPhotoYn;
        }
        return "Y";
    }

    public static String getSaveMapiaVideoYn() {
        if (CameraUtils.saveMapiaVideoYn != null && StringUtils.isNotBlank(CameraUtils.saveMapiaVideoYn)) {
            return CameraUtils.saveMapiaVideoYn;
        }
        CameraUtils.saveMapiaVideoYn = PreferenceUtils.getPreference("saveMapiaVideoYn");
        if (CameraUtils.saveMapiaVideoYn != null && StringUtils.isNotBlank(CameraUtils.saveMapiaVideoYn)) {
            return CameraUtils.saveMapiaVideoYn;
        }
        return "Y";
    }

    public static boolean isSupportVideoFile(final String s) {
        final String[] array = { "mp4", "3gp", "webm" };
        for (int length = array.length, i = 0; i < length; ++i) {
            if (StringUtils.endsWithIgnoreCase(s, array[i])) {
                return true;
            }
        }
        return false;
    }

    public static void setSaveOriginalPhotoYn(final String saveOriginalPhotoYn) {
        PreferenceUtils.putPreference("saveOriginalPhotoYn", CameraUtils.saveOriginalPhotoYn = saveOriginalPhotoYn);
    }

    public static void setSaveMapiaPhotoYn(final String saveMapiaPhotoYn) {
        PreferenceUtils.putPreference("saveMapiaPhotoYn", CameraUtils.saveMapiaPhotoYn = saveMapiaPhotoYn);
    }

    public static void setSaveMapiaVideoYn(final String saveMapiaVideoYn) {
        PreferenceUtils.putPreference("saveMapiaVideoYn", CameraUtils.saveMapiaVideoYn = saveMapiaVideoYn);
    }
}