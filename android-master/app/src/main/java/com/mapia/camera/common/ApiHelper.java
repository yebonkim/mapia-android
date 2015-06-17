package com.mapia.camera.common;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;


public class ApiHelper
{
    public static interface VERSION_CODES
    {

        public static final int GINGERBREAD_MR1 = 10;
        public static final int HONEYCOMB = 11;
        public static final int HONEYCOMB_MR1 = 12;
        public static final int HONEYCOMB_MR2 = 13;
        public static final int ICE_CREAM_SANDWICH = 14;
        public static final int ICE_CREAM_SANDWICH_MR1 = 15;
        public static final int JELLY_BEAN = 16;
        public static final int JELLY_BEAN_MR1 = 17;
        public static final int JELLY_BEAN_MR2 = 18;
    }

//
//    public static final boolean AT_LEAST_16;
//    public static final boolean CAN_START_PREVIEW_IN_JPEG_CALLBACK;
//    public static final boolean ENABLE_PHOTO_EDITOR;
//    public static final boolean HAS_ACTION_BAR;
//    public static final boolean HAS_ANNOUNCE_FOR_ACCESSIBILITY;
//    public static final boolean HAS_AUTO_FOCUS_MOVE_CALLBACK;
    public static final boolean HAS_CAMERA_FOCUS_AREA = (Build.VERSION.SDK_INT >= 14);
//    public static final boolean HAS_CAMERA_HDR;
    public static final boolean HAS_CAMERA_METERING_AREA = (Build.VERSION.SDK_INT >= 14);
//    public static final boolean HAS_CANCELLATION_SIGNAL;
//    public static final boolean HAS_DISPLAY_LISTENER;
//    public static final boolean HAS_EFFECTS_RECORDING = false;
//    public static final boolean HAS_EFFECTS_RECORDING_CONTEXT_INPUT;
//    public static final boolean HAS_FACE_DETECTION;
//    public static final boolean HAS_FINE_RESOLUTION_QUALITY_LEVELS;
    public static final boolean HAS_GET_CAMERA_DISABLED = hasMethod(DevicePolicyManager.class, "getCameraDisabled", ComponentName.class);;
//    public static final boolean HAS_GET_SUPPORTED_VIDEO_SIZE;
    public static final boolean HAS_GLES20_REQUIRED = true;
//    public static final boolean HAS_INTENT_EXTRA_LOCAL_ONLY;
//    public static final boolean HAS_MEDIA_ACTION_SOUND;
    public static final boolean HAS_MEDIA_COLUMNS_WIDTH_AND_HEIGHT = hasField(MediaStore.MediaColumns.class, "WIDTH");
//    public static final boolean HAS_MEDIA_MUXER;
//    public static final boolean HAS_MEDIA_PROVIDER_FILES_TABLE;
//    public static final boolean HAS_MOTION_EVENT_TRANSFORM;
//    public static final boolean HAS_MTP;
//    public static final boolean HAS_OBJECT_ANIMATION;
//    public static final boolean HAS_OLD_PANORAMA;
//    public static final boolean HAS_OPTIONS_IN_MUTABLE;
//    public static final boolean HAS_ORIENTATION_LOCK;
    public static final boolean HAS_POST_ON_ANIMATION = (Build.VERSION.SDK_INT >= 16);
    public static final boolean HAS_RELEASE_SURFACE_TEXTURE = hasMethod("android.graphics.SurfaceTexture", "release", (new Class[0]));
//    public static final boolean HAS_REMOTE_VIEWS_SERVICE;
//    public static final boolean HAS_REUSING_BITMAP_IN_BITMAP_FACTORY;
//    public static final boolean HAS_REUSING_BITMAP_IN_BITMAP_REGION_DECODER;
//    public static final boolean HAS_ROTATION_ANIMATION;
//    public static final boolean HAS_SET_BEAM_PUSH_URIS;
    public static final boolean HAS_SET_DEFALT_BUFFER_SIZE = hasMethod("android.graphics.SurfaceTexture", "setDefaultBufferSize", Integer.TYPE, Integer.TYPE);
    public static final boolean HAS_SET_ICON_ATTRIBUTE = (Build.VERSION.SDK_INT >= 11);;
    public static final boolean HAS_SET_SYSTEM_UI_VISIBILITY = hasMethod(View.class, "setSystemUiVisibility", new Class[] { Integer.TYPE});
//    public static final boolean HAS_SURFACE_TEXTURE;
//    public static final boolean HAS_SURFACE_TEXTURE_RECORDING;
//    public static final boolean HAS_TIME_LAPSE_RECORDING;
//    public static final boolean HAS_VIEW_PROPERTY_ANIMATOR;
//    public static final boolean HAS_VIEW_SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    public static final boolean HAS_VIEW_SYSTEM_UI_FLAG_LAYOUT_STABLE = hasField(View.class, "SYSTEM_UI_FLAG_LAYOUT_STABLE");
//    public static final boolean HAS_VIEW_TRANSFORM_PROPERTIES;
//    public static final boolean HAS_ZOOM_WHEN_RECORDING;
//    public static final boolean USE_888_PIXEL_FORMAT;

    public ApiHelper()
    {
    }

    public static int getIntFieldIfExists(final Class clazz, final String s, final Class clazz2, final int n) {
        try {
            return clazz.getDeclaredField(s).getInt(clazz2);
        }
        catch (Exception ex) {
            return n;
        }
    }

    private static boolean hasField(final Class clazz, final String s) {
        try {
            clazz.getDeclaredField(s);
            return true;
        }
        catch (NoSuchFieldException ex) {
            return false;
        }
    }

    private static boolean hasMethod(final Class clazz, final String s, final Class... array) {
        try {
            clazz.getDeclaredMethod(s, (Class[])array);
            return true;
        }
        catch (NoSuchMethodException ex) {
            return false;
        }
    }

    private static boolean hasMethod(final String s, final String s2, final Class... array) {
        try {
            Class.forName(s).getDeclaredMethod(s2, array);
            return true;
        }
        catch (Throwable t) {
            return false;
        }
    }
}