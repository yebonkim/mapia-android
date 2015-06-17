package com.mapia.camera.util;

import android.content.Context;

/**
 * Created by daehyun on 15. 6. 16..
 */


public class UsageStatistics
{
    public static final String ACTION_CAPTURE_DONE = "CaptureDone";
    public static final String ACTION_CAPTURE_FAIL = "CaptureFail";
    public static final String ACTION_CAPTURE_START = "CaptureStart";
    public static final String ACTION_SHARE = "Share";
    public static final String COMPONENT_CAMERA = "Camera";
    public static final String COMPONENT_EDITOR = "Editor";
    public static final String COMPONENT_GALLERY = "Gallery";
    public static final String COMPONENT_IMPORTER = "Importer";
    public static final String TRANSITION_BACK_BUTTON = "BackButton";
    public static final String TRANSITION_BUTTON_TAP = "ButtonTap";
    public static final String TRANSITION_INTENT = "Intent";
    public static final String TRANSITION_ITEM_TAP = "ItemTap";
    public static final String TRANSITION_MENU_TAP = "MenuTap";
    public static final String TRANSITION_PINCH_IN = "PinchIn";
    public static final String TRANSITION_PINCH_OUT = "PinchOut";
    public static final String TRANSITION_SWIPE = "Swipe";
    public static final String TRANSITION_UP_BUTTON = "UpButton";

    public static void initialize(final Context context) {
    }

    public static void onContentViewChanged(final String s, final String s2) {
    }

    public static void onEvent(final String s, final String s2, final String s3) {
    }

    public static void onEvent(final String s, final String s2, final String s3, final long n) {
    }

    public static void setPendingTransitionCause(final String s) {
    }
}