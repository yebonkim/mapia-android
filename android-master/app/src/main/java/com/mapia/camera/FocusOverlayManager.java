package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class FocusOverlayManager
{
    private static final int RESET_TOUCH_FOCUS = 0;
    private static final int RESET_TOUCH_FOCUS_DELAY = 3000;
    private static final int STATE_FAIL = 4;
    private static final int STATE_FOCUSING = 1;
    private static final int STATE_FOCUSING_SNAP_ON_FINISH = 2;
    private static final int STATE_IDLE = 0;
    private static final int STATE_SUCCESS = 3;
    private static final String TAG = "CAM_FocusManager";
    private boolean mAeAwbLock;
    private String[] mDefaultFocusModes;
    private int mDisplayOrientation;
    private List<Object> mFocusArea;
    private boolean mFocusAreaSupported;
    private boolean mFocusDefault;
    private String mFocusMode;
    private Handler mHandler;
    private boolean mInitialized;
    Listener mListener;
    private boolean mLockAeAwbNeeded;
    private android.graphics.Matrix mMatrix;
    private List<Object> mMeteringArea;
    private boolean mMeteringAreaSupported;
    private boolean mMirror;
    private String mOverrideFocusMode;
    private Camera.Parameters mParameters;
    private ComboPreferences mPreferences;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private boolean mPreviousMoving;
    private int mState;
    private FocusUI mUI;

    public FocusOverlayManager(final ComboPreferences mPreferences, final String[] mDefaultFocusModes, final Camera.Parameters parameters, final Listener mListener, final boolean mirror, final Looper looper, final FocusUI mui) {
        super();
        this.mState = 0;
        this.mHandler = new MainHandler(looper);
        this.mMatrix = new Matrix();
        this.mPreferences = mPreferences;
        this.mDefaultFocusModes = mDefaultFocusModes;
        this.setParameters(parameters);
        this.mListener = mListener;
        this.setMirror(mirror);
        this.mFocusDefault = true;
        this.mUI = mui;
    }

    private void autoFocus() {
        this.autoFocus(false);
    }

    private void autoFocus(final boolean b) {
        Log.v("CAM_FocusManager", "Start autofocus.");
        this.mListener.autoFocus();
        this.mState = 1;
        this.mUI.pauseFaceDetection();
        this.updateFocusUI(b);
        this.mHandler.removeMessages(0);
    }

    private void calculateTapArea(int clamp, int clamp2, final float n, final Rect rect) {
        final int n2 = (int)(Math.min(this.mPreviewWidth, this.mPreviewHeight) * n / 20.0f);
        clamp = Util.clamp(clamp - n2, 0, this.mPreviewWidth - n2 * 2);
        clamp2 = Util.clamp(clamp2 - n2, 0, this.mPreviewHeight - n2 * 2);
        final RectF rectF = new RectF((float)clamp, (float)clamp2, (float)(n2 * 2 + clamp), (float)(n2 * 2 + clamp2));
        this.mMatrix.mapRect(rectF);
        Util.rectFToRect(rectF, rect);
    }

    private void cancelAutoFocus() {
        Log.v("CAM_FocusManager", "Cancel autofocus.");
        this.resetTouchFocus();
        this.mListener.cancelAutoFocus();
        this.mUI.resumeFaceDetection();
        this.mState = 0;
        this.updateFocusUI();
        this.mHandler.removeMessages(0);
    }

    private void capture() {
        if (this.mListener.capture()) {
            this.mState = 0;
            this.mHandler.removeMessages(0);
        }
    }

    @TargetApi(14)
    private void initializeFocusAreas(final int n, final int n2) {
        if (this.mFocusArea == null) {
            (this.mFocusArea = new ArrayList<Object>()).add(new Camera.Area(new Rect(), 1));
        }
        this.calculateTapArea(n, n2, 1.0f, ((Camera.Area)this.mFocusArea.get(0)).rect);
    }

    @TargetApi(14)
    private void initializeMeteringAreas(final int n, final int n2) {
        if (this.mMeteringArea == null) {
            (this.mMeteringArea = new ArrayList<Object>()).add(new Camera.Area(new Rect(), 1));
        }
        this.calculateTapArea(n, n2, 1.5f, ((Camera.Area)this.mMeteringArea.get(0)).rect);
    }

    private void lockAeAwbIfNeeded() {
        if (this.mLockAeAwbNeeded && !this.mAeAwbLock) {
            this.mAeAwbLock = true;
            this.mListener.setFocusParameters();
        }
    }

    private boolean needAutoFocusCall() {
        final String focusMode = this.getFocusMode();
        return !focusMode.equals("infinity") && !focusMode.equals("fixed") && !focusMode.equals("edof");
    }

    private void setMatrix() {
        if (this.mPreviewWidth != 0 && this.mPreviewHeight != 0) {
            final Matrix matrix = new Matrix();
            Util.prepareMatrix(matrix, this.mMirror, this.mDisplayOrientation, this.mPreviewWidth, this.mPreviewHeight);
            matrix.invert(this.mMatrix);
            this.mInitialized = true;
        }
    }

    private void unlockAeAwbIfNeeded() {
        if (this.mLockAeAwbNeeded && this.mAeAwbLock && this.mState != 2) {
            this.mAeAwbLock = false;
            this.mListener.setFocusParameters();
        }
    }

    public void doSnap() {
        if (this.mInitialized) {
            if (!this.needAutoFocusCall() || this.mState == 3 || this.mState == 4) {
                this.capture();
                return;
            }
            if (this.mState == 1) {
                this.mState = 2;
                return;
            }
            if (this.mState == 0) {
                this.capture();
            }
        }
    }

    public boolean getAeAwbLock() {
        return this.mAeAwbLock;
    }

    public List getFocusAreas() {
        return this.mFocusArea;
    }

    public String getFocusMode() {
        if (this.mOverrideFocusMode != null) {
            return this.mOverrideFocusMode;
        }
        if (this.mParameters == null) {
            return "auto";
        }
        final List supportedFocusModes = this.mParameters.getSupportedFocusModes();
        if (this.mFocusAreaSupported && !this.mFocusDefault) {
            this.mFocusMode = "auto";
        }
        else {
            this.mFocusMode = this.mPreferences.getString("pref_camera_focusmode_key", null);
            if (this.mFocusMode == null) {
                for (int i = 0; i < this.mDefaultFocusModes.length; ++i) {
                    final String mFocusMode = this.mDefaultFocusModes[i];
                    if (Util.isSupported(mFocusMode, supportedFocusModes)) {
                        this.mFocusMode = mFocusMode;
                        break;
                    }
                }
            }
        }
        if (!Util.isSupported(this.mFocusMode, supportedFocusModes)) {
            if (Util.isSupported("auto", this.mParameters.getSupportedFocusModes())) {
                this.mFocusMode = "auto";
            }
            else {
                this.mFocusMode = this.mParameters.getFocusMode();
            }
        }
        return this.mFocusMode;
    }

    int getFocusState() {
        return this.mState;
    }

    public List getMeteringAreas() {
        return this.mMeteringArea;
    }

    public boolean isFocusCompleted() {
        return this.mState == 3 || this.mState == 4;
    }

    public boolean isFocusingSnapOnFinish() {
        return this.mState == 2;
    }

    public void onAutoFocus(final boolean b, final boolean b2) {
        if (this.mState == 2) {
            if (b) {
                this.mState = 3;
            }
            else {
                this.mState = 4;
            }
            this.updateFocusUI();
            this.capture();
        }
        else if (this.mState == 1) {
            if (b) {
                this.mState = 3;
            }
            else {
                this.mState = 4;
            }
            this.updateFocusUI();
            if (!this.mFocusDefault) {
                this.mHandler.sendEmptyMessageDelayed(0, 3000L);
            }
            if (b2) {
                this.lockAeAwbIfNeeded();
            }
        }
        else if (this.mState == 0) {
            return;
        }
    }

    public void onAutoFocusMoving(final boolean mPreviousMoving) {
        if (this.mInitialized) {
            if (this.mUI.hasFaces()) {
                this.mUI.clearFocus();
                return;
            }
            if (this.mState == 0) {
                if (mPreviousMoving && !this.mPreviousMoving) {
                    this.mUI.onFocusStarted();
                }
                else if (!mPreviousMoving) {
                    this.mUI.onFocusSucceeded(true);
                }
                this.mPreviousMoving = mPreviousMoving;
            }
        }
    }

    public void onCameraReleased() {
        this.onPreviewStopped();
    }

    public void onPreviewStarted() {
        this.mState = 0;
    }

    public void onPreviewStopped() {
        this.mState = 0;
        this.resetTouchFocus();
        this.updateFocusUI();
    }

    public void onShutterDown() {
        if (this.mInitialized) {
            boolean b2;
            final boolean b = b2 = false;
            if (this.needAutoFocusCall()) {
                b2 = b;
                if (this.mState != 3) {
                    b2 = b;
                    if (this.mState != 4) {
                        this.autoFocus();
                        b2 = true;
                    }
                }
            }
            if (!b2) {
                this.lockAeAwbIfNeeded();
            }
        }
    }

    public void onShutterUp() {
        if (!this.mInitialized) {
            return;
        }
        if (this.needAutoFocusCall() && (this.mState == 1 || this.mState == 3 || this.mState == 4)) {
            this.cancelAutoFocus();
        }
        this.unlockAeAwbIfNeeded();
    }

    public void onSingleTapUp(final int n, final int n2) {
        if (this.mInitialized && this.mState != 2) {
            if (!this.mFocusDefault && (this.mState == 1 || this.mState == 3 || this.mState == 4)) {
                this.cancelAutoFocus();
            }
            if (this.mPreviewWidth != 0 && this.mPreviewHeight != 0) {
                this.mFocusDefault = false;
                if (this.mFocusAreaSupported) {
                    this.initializeFocusAreas(n, n2);
                }
                if (this.mMeteringAreaSupported) {
                    this.initializeMeteringAreas(n, n2);
                }
                this.mUI.setFocusPosition(n, n2);
                this.mListener.stopFaceDetection();
                this.mListener.setFocusParameters();
                if (this.mFocusAreaSupported) {
                    this.autoFocus(true);
                    return;
                }
                this.updateFocusUI();
                this.mHandler.removeMessages(0);
                this.mHandler.sendEmptyMessageDelayed(0, 3000L);
            }
        }
    }

    public void overrideFocusMode(final String mOverrideFocusMode) {
        this.mOverrideFocusMode = mOverrideFocusMode;
    }

    public void removeMessages() {
        this.mHandler.removeMessages(0);
    }

    public void resetTouchFocus() {
        if (!this.mInitialized) {
            return;
        }
        this.mUI.clearFocus();
        if (this.mFocusAreaSupported) {
            this.initializeFocusAreas(this.mPreviewWidth / 2, this.mPreviewHeight / 2);
        }
        if (this.mMeteringAreaSupported) {
            this.initializeMeteringAreas(this.mPreviewWidth / 2, this.mPreviewHeight / 2);
        }
        this.mFocusDefault = true;
    }

    public void setAeAwbLock(final boolean mAeAwbLock) {
        this.mAeAwbLock = mAeAwbLock;
    }

    public void setDisplayOrientation(final int mDisplayOrientation) {
        this.mDisplayOrientation = mDisplayOrientation;
        this.setMatrix();
    }

    public void setMirror(final boolean mMirror) {
        this.mMirror = mMirror;
        this.setMatrix();
    }

    public void setParameters(final Camera.Parameters mParameters) {
        if (mParameters == null) {
            return;
        }
        this.mParameters = mParameters;
        this.mFocusAreaSupported = Util.isFocusAreaSupported(mParameters);
        this.mMeteringAreaSupported = Util.isMeteringAreaSupported(mParameters);
        this.mLockAeAwbNeeded = (Util.isAutoExposureLockSupported(this.mParameters) || Util.isAutoWhiteBalanceLockSupported(this.mParameters));
    }

    public void setPreviewSize(final int mPreviewWidth, final int mPreviewHeight) {
        if (this.mPreviewWidth != mPreviewWidth || this.mPreviewHeight != mPreviewHeight) {
            this.mPreviewWidth = mPreviewWidth;
            this.mPreviewHeight = mPreviewHeight;
            this.setMatrix();
        }
    }

    public void updateFocusUI() {
        this.updateFocusUI(false);
    }

    public void updateFocusUI(final boolean b) {
        if (this.mInitialized) {
            if (this.mState == 0) {
                if (this.mFocusDefault) {
                    this.mUI.clearFocus();
                    return;
                }
                this.mUI.onFocusStarted();
            }
            else {
                if (this.mState == 1 || this.mState == 2) {
                    this.mUI.onFocusStarted(b);
                    return;
                }
                if ("continuous-picture".equals(this.mFocusMode)) {
                    this.mUI.onFocusSucceeded(false);
                    return;
                }
                if (this.mState == 3) {
                    this.mUI.onFocusSucceeded(false);
                    return;
                }
                if (this.mState == 4) {
                    this.mUI.onFocusFailed(false);
                }
            }
        }
    }

    public interface FocusUI
    {
        void clearFocus();

        boolean hasFaces();

        void onFocusFailed(boolean p0);

        void onFocusStarted();

        void onFocusStarted(boolean p0);

        void onFocusSucceeded(boolean p0);

        void pauseFaceDetection();

        void resumeFaceDetection();

        void setFocusPosition(int p0, int p1);
    }

    public interface Listener
    {
        void autoFocus();

        void cancelAutoFocus();

        boolean capture();

        void setFocusParameters();

        void startFaceDetection();

        void stopFaceDetection();
    }

    private class MainHandler extends Handler
    {
        public MainHandler(final Looper looper) {
            super(looper);
        }

        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {}
                case 0: {
                    FocusOverlayManager.this.cancelAutoFocus();
                    FocusOverlayManager.this.mListener.startFaceDetection();
                }
            }
        }
    }
}