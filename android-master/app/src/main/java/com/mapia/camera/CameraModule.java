package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Intent;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;


public interface CameraModule
{
    boolean dispatchTouchEvent(MotionEvent p0);

    void easterEgg();

    ComboPreferences getComboPreferences();

    void init(CameraActivity p0, View p1);

    void installIntentFilter();

    boolean isDone();

    boolean needsPieMenu();

    boolean needsSwitcher();

    void onActivityResult(int p0, int p1, Intent p2);

    boolean onBackPressed();

    void onCaptureTextureCopied();

    void onConfigurationChanged(Configuration p0);

    void onFinish();

    void onFullScreenChanged(boolean p0);

    boolean onKeyDown(int p0, KeyEvent p1);

    boolean onKeyUp(int p0, KeyEvent p1);

    void onMediaSaveServiceConnected(MediaSaveService p0);

    void onOrientationChanged(int p0);

    void onPauseAfterSuper();

    void onPauseBeforeSuper();

    void onPreviewTextureCopied();

    void onResumeAfterSuper();

    void onResumeBeforeSuper();

    void onShowSwitcherPopup();

    void onSingleTapUp(View p0, int p1, int p2);

    void onStop();

    void onUserInteraction();

    void updateCameraAppView();

    boolean updateStorageHintOnResume();
}