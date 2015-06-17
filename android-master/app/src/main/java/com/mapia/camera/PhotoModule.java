package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.CameraProfile;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.mapia.R;
import com.mapia.camera.ui.CountDownView;
import com.mapia.camera.util.UsageStatistics;
import com.mapia.util.BitmapUtils;
import com.mapia.util.CameraUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;


public class PhotoModule implements CameraModule, PhotoController, FocusOverlayManager.Listener, CameraPreference.OnPreferenceChangedListener, ShutterButton.OnShutterButtonListener, MediaSaveService.Listener, CountDownView.OnCountDownFinishedListener, SensorEventListener
{
    private static final int CAMERA_DISABLED = 12;
    private static final int CAMERA_OPEN_DONE = 9;
    private static final int CHECK_DISPLAY_ROTATION = 5;
    private static final int CLEAR_SCREEN_DELAY = 3;
    private static final String EXTRA_QUICK_CAPTURE = "android.intent.extra.quickCapture";
    private static final int FIRST_TIME_INIT = 2;
    private static final int KEEP_CAMERA_TIMEOUT = 1000;
    private static final int OPEN_CAMERA_FAIL = 11;
    private static final int SCREEN_DELAY = 120000;
    private static final int SETUP_PREVIEW = 1;
    private static final int SET_CAMERA_PARAMETERS_WHEN_IDLE = 4;
    private static final int SHOW_TAP_TO_FOCUS_TOAST = 6;
    private static final int START_PREVIEW_DONE = 10;
    private static final int SWITCH_CAMERA = 7;
    private static final int SWITCH_CAMERA_START_ANIMATION = 8;
    private static final String TAG = "CAM_PhotoModule";
    private static final int UPDATE_PARAM_ALL = -1;
    private static final int UPDATE_PARAM_INITIALIZE = 1;
    private static final int UPDATE_PARAM_PREFERENCE = 4;
    private static final int UPDATE_PARAM_ZOOM = 2;
    private static final String sTempCropFilename = "crop-temp";
    private boolean isDone;
    private CameraActivity mActivity;
    private boolean mAeLockSupported;
    private final AutoFocusCallback mAutoFocusCallback;
    private final Object mAutoFocusMoveCallback;
    public long mAutoFocusTime;
    private boolean mAwbLockSupported;
    private final StringBuilder mBuilder;
    private CameraManager.CameraProxy mCameraDevice;
    private boolean mCameraDisabled;
    private int mCameraDisplayOrientation;
    private int mCameraId;
    CameraStartUpThread mCameraStartUpThread;
    private int mCameraState;
    public long mCaptureStartTime;
    private ContentResolver mContentResolver;
    private boolean mContinousFocusSupported;
    private String mCropValue;
    private int mDisplayOrientation;
    private int mDisplayRotation;
    private Runnable mDoSnapRunnable;
    private final CameraErrorCallback mErrorCallback;
    private boolean mFaceDetectionStarted;
    private boolean mFirstTimeInitialized;
    private Runnable mFlashRunnable;
    private boolean mFocusAreaSupported;
    private FocusOverlayManager mFocusManager;
    private long mFocusStartTime;
    private final Formatter mFormatter;
    private final Object[] mFormatterArgs;
    private float[] mGData;
    private final Handler mHandler;
    private int mHeading;
    private Camera.Parameters mInitialParams;
    private boolean mIsImageCaptureIntent;
    public long mJpegCallbackFinishTime;
    private byte[] mJpegImageData;
    private long mJpegPictureCallbackTime;
    private int mJpegRotation;
    private LocationManager mLocationManager;
    private float[] mMData;
    private ContentProviderClient mMediaProviderClient;
    private boolean mMeteringAreaSupported;
    private NamedImages mNamedImages;
    private MediaSaveService.OnMediaSavedListener mOnMediaSavedListener;
    private long mOnResumeTime;
    private boolean mOpenCameraFail;
    private int mOrientation;
    private Camera.Parameters mParameters;
    private boolean mPaused;
    protected int mPendingSwitchCameraId;
    public long mPictureDisplayedToJpegCallbackTime;
    private final PostViewPictureCallback mPostViewPictureCallback;
    private long mPostViewPictureCallbackTime;
    private PreferenceGroup mPreferenceGroup;
    private ComboPreferences mPreferences;
    private float[] mR;
    private final RawPictureCallback mRawPictureCallback;
    private long mRawPictureCallbackTime;
    private Uri mSaveUri;
    private String mSceneMode;
    private SensorManager mSensorManager;
    private long mShutterCallbackTime;
    public long mShutterLag;
    public long mShutterToPictureDisplayedTime;
    private boolean mSnapshotOnIdle;
    ConditionVariable mStartPreviewPrerequisiteReady;
    private PhotoUI mUI;
    private int mUpdateSet;
    private int mZoomValue;

    public PhotoModule() {
        super();
        this.mPendingSwitchCameraId = -1;
        this.mOrientation = -1;
        this.mFaceDetectionStarted = false;
        this.mDoSnapRunnable = new Runnable() {
            @Override
            public void run() {
                PhotoModule.this.onShutterButtonClick();
            }
        };
        this.mFlashRunnable = new Runnable() {
            @Override
            public void run() {
                PhotoModule.this.animateFlash();
            }
        };
        this.mBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mBuilder);
        this.mFormatterArgs = new Object[1];
        this.mCameraState = 0;
        this.mSnapshotOnIdle = false;
        this.mPostViewPictureCallback = new PostViewPictureCallback();
        this.mRawPictureCallback = new RawPictureCallback();
        this.mAutoFocusCallback = new AutoFocusCallback();
        AutoFocusMoveCallback mAutoFocusMoveCallback;
        if (true){//ApiHelper.HAS_AUTO_FOCUS_MOVE_CALLBACK) {
            mAutoFocusMoveCallback = new AutoFocusMoveCallback();
        }
        else {
            mAutoFocusMoveCallback = null;
        }
        this.mAutoFocusMoveCallback = mAutoFocusMoveCallback;
        this.mErrorCallback = new CameraErrorCallback();
        this.mHandler = new MainHandler();
        this.mStartPreviewPrerequisiteReady = new ConditionVariable();
        this.mGData = new float[3];
        this.mMData = new float[3];
        this.mR = new float[16];
        this.mHeading = -1;
        this.mOnMediaSavedListener = new MediaSaveService.OnMediaSavedListener() {
            @Override
            public void onMediaSaved(final Uri uri) {
                if (uri != null) {
                    return;
                }
            }
        };
    }

    private void addIdleHandler() {
        Looper.myQueue().addIdleHandler((MessageQueue.IdleHandler)new MessageQueue.IdleHandler() {
            public boolean queueIdle() {
                Storage.ensureOSXCompatible();
                return false;
            }
        });
    }

    private void animateFlash() {
        if ( !this.mIsImageCaptureIntent && this.mActivity.mShowCameraAppView) { //ApiHelper.HAS_SURFACE_TEXTURE &&
            ((CameraScreenNail)this.mActivity.mCameraScreenNail).animateFlash(this.mDisplayRotation);
        }
    }

    private boolean canTakePicture() {
        return this.isCameraIdle() && this.mActivity.getStorageSpace() > 50000000L;
    }

    @TargetApi(14)
    private void closeCamera() {
        if (this.mCameraDevice != null) {
            this.mCameraDevice.setZoomChangeListener(null);
            if (false) {//ApiHelper.HAS_FACE_DETECTION
                this.mCameraDevice.setFaceDetectionListener(null);
            }
            this.mCameraDevice.setErrorCallback(null);
            CameraHolder.instance().release();
            this.mFaceDetectionStarted = false;
            this.mCameraDevice = null;
            this.setCameraState(0);
            this.mFocusManager.onCameraReleased();
        }
    }

    private int getPreferredCameraId(final ComboPreferences comboPreferences) {
        final int cameraFacingIntentExtras = Util.getCameraFacingIntentExtras(this.mActivity);
        if (cameraFacingIntentExtras != -1) {
            return cameraFacingIntentExtras;
        }
        return CameraSettings.readPreferredCameraId((SharedPreferences)comboPreferences);
    }

    private void initializeCapabilities() {
        this.mInitialParams = this.mCameraDevice.getParameters();
        this.mFocusAreaSupported = Util.isFocusAreaSupported(this.mInitialParams);
        this.mMeteringAreaSupported = Util.isMeteringAreaSupported(this.mInitialParams);
        this.mAeLockSupported = Util.isAutoExposureLockSupported(this.mInitialParams);
        this.mAwbLockSupported = Util.isAutoWhiteBalanceLockSupported(this.mInitialParams);
        this.mContinousFocusSupported = this.mInitialParams.getSupportedFocusModes().contains("continuous-picture");
    }

    private void initializeFirstTime() {
        if (this.mFirstTimeInitialized) {
            return;
        }
        this.mLocationManager.recordLocation(RecordLocationPreference.get((SharedPreferences)this.mPreferences, this.mContentResolver));
        this.keepMediaProviderInstance();
        this.mUI.initializeFirstTime();
        final MediaSaveService mediaSaveService = this.mActivity.getMediaSaveService();
        if (mediaSaveService != null) {
            mediaSaveService.setListener((MediaSaveService.Listener)this);
        }
        this.mNamedImages = new NamedImages();
        this.mFirstTimeInitialized = true;
        this.addIdleHandler();
        this.mActivity.updateStorageSpaceAndHint();
    }

    private void initializeFocusManager() {
        boolean b = true;
        if (this.mFocusManager != null) {
            this.mFocusManager.removeMessages();
            return;
        }
        if (CameraHolder.instance().getCameraInfo()[this.mCameraId].facing != 1) {
            b = false;
        }
        this.mFocusManager = new FocusOverlayManager(this.mPreferences, this.mActivity.getResources().getStringArray(R.array.pref_camera_focusmode_default_array), this.mInitialParams, (FocusOverlayManager.Listener)this, b, this.mActivity.getMainLooper(), (FocusOverlayManager.FocusUI)this.mUI);
    }

    private void initializeSecondTime() {
        this.mLocationManager.recordLocation(RecordLocationPreference.get((SharedPreferences)this.mPreferences, this.mContentResolver));
        final MediaSaveService mediaSaveService = this.mActivity.getMediaSaveService();
        if (mediaSaveService != null) {
            mediaSaveService.setListener((MediaSaveService.Listener)this);
        }
        this.mNamedImages = new NamedImages();
        this.mUI.initializeSecondTime(this.mParameters);
        this.keepMediaProviderInstance();
    }

    private void keepMediaProviderInstance() {
        if (this.mMediaProviderClient == null) {
            this.mMediaProviderClient = this.mContentResolver.acquireContentProviderClient("media");
        }
    }

    private void keepScreenOnAwhile() {
        this.mHandler.removeMessages(3);
        this.mActivity.getWindow().addFlags(128);
        this.mHandler.sendEmptyMessageDelayed(3, 120000L);
    }

    private void loadCameraPreferences() {
        this.mPreferenceGroup = new CameraSettings(this.mActivity, this.mInitialParams, this.mCameraId, CameraHolder.instance().getCameraInfo()).getPreferenceGroup(2131034112);
    }

    private void locationFirstRun() {
        if (!RecordLocationPreference.isSet((SharedPreferences)this.mPreferences) && CameraHolder.instance().getBackCameraId() != -1) {
            this.setLocationPreference("off");
        }
    }

    private void onCameraOpened() {
        final View rootView = this.mUI.getRootView();
        final int width = rootView.getWidth();
        final int height = rootView.getHeight();
        this.mFocusManager.setPreviewSize(width, height);
        if (Util.getDisplayRotation(this.mActivity) % 180 == 0) {
            ((CameraScreenNail)this.mActivity.mCameraScreenNail).setPreviewFrameLayoutSize(width, height);
        }
        else {
            ((CameraScreenNail)this.mActivity.mCameraScreenNail).setPreviewFrameLayoutSize(height, width);
        }
        this.mActivity.setSingleTapUpListener(rootView);
        this.openCameraCommon();
        this.onFullScreenChanged(this.mActivity.isInCameraApp());
    }

    private void onPreviewStarted() {
        this.mCameraStartUpThread = null;
        this.setCameraState(1);
        if (!true) {//ApiHelper.HAS_SURFACE_TEXTURE
            this.mCameraDevice.setPreviewDisplayAsync(this.mUI.getSurfaceHolder());
        }
        this.startFaceDetection();
        this.locationFirstRun();
    }

    private void openCameraCommon() {
        this.loadCameraPreferences();
        this.mUI.onCameraOpened(this.mPreferenceGroup, this.mPreferences, this.mParameters, this);
        this.updateSceneMode();
        this.showTapToFocusToastIfNeeded();
    }

    private void overrideCameraSettings(final String s, final String s2, final String s3) {
        this.mUI.overrideSettings("pref_camera_flashmode_key", s, "pref_camera_whitebalance_key", s2, "pref_camera_focusmode_key", s3);
    }

    private void resetExposureCompensation() {
        if (!"0".equals(this.mPreferences.getString("pref_camera_exposure_key", "0"))) {
            final SharedPreferences.Editor edit = this.mPreferences.edit();
            edit.putString("pref_camera_exposure_key", "0");
            edit.apply();
        }
    }

    private void resetScreenOn() {
        this.mHandler.removeMessages(3);
        this.mActivity.getWindow().clearFlags(128);
    }

    @TargetApi(16)
    private void setAutoExposureLockIfSupported() {
        if (this.mAeLockSupported) {
            this.mParameters.setAutoExposureLock(this.mFocusManager.getAeAwbLock());
        }
    }

    @TargetApi(16)
    private void setAutoWhiteBalanceLockIfSupported() {
        if (this.mAwbLockSupported) {
            this.mParameters.setAutoWhiteBalanceLock(this.mFocusManager.getAeAwbLock());
        }
    }

    private void setCameraParameters(final int n) {
        if ((n & 0x1) != 0x0) {
            this.updateCameraParametersInitialize();
        }
        if ((n & 0x2) != 0x0) {
            this.updateCameraParametersZoom();
        }
        if ((n & 0x4) != 0x0) {
            this.updateCameraParametersPreference();
        }
        this.mCameraDevice.setParameters(this.mParameters);
    }

    private void setCameraParametersWhenIdle(final int n) {
        this.mUpdateSet |= n;
        if (this.mCameraDevice == null) {
            this.mUpdateSet = 0;
        }
        else {
            if (this.isCameraIdle()) {
                this.setCameraParameters(this.mUpdateSet);
                this.updateSceneMode();
                this.mUpdateSet = 0;
                return;
            }
            if (!this.mHandler.hasMessages(4)) {
                this.mHandler.sendEmptyMessageDelayed(4, 1000L);
            }
        }
    }

    private void setCameraState(final int mCameraState) {
        switch (this.mCameraState = mCameraState) {
            case 0:
            case 2:
            case 3:
            case 4: {
                this.mUI.enableGestures(false);
            }
            case 1: {
                if (this.mActivity.isInCameraApp()) {
                    this.mUI.enableGestures(true);
                    return;
                }
                break;
            }
        }
    }

    private void setDisplayOrientation() {
        this.mDisplayRotation = Util.getDisplayRotation(this.mActivity);
        this.mDisplayOrientation = Util.getDisplayOrientation(this.mDisplayRotation, this.mCameraId);
        this.mCameraDisplayOrientation = Util.getDisplayOrientation(this.mDisplayRotation, this.mCameraId);
        this.mUI.setDisplayOrientation(this.mDisplayOrientation);
        if (this.mFocusManager != null) {
            this.mFocusManager.setDisplayOrientation(this.mDisplayOrientation);
        }
        this.mActivity.getGLRoot().requestLayoutContentPane();
    }

    @TargetApi(14)
    private void setFocusAreasIfSupported() {
        if (this.mFocusAreaSupported) {
            final String model = Build.MODEL;
            if (!model.startsWith("SHW-M480") && !model.equals("SHW-M500W")) {
                this.mParameters.setFocusAreas(this.mFocusManager.getFocusAreas());
            }
        }
    }

    private void setLocationPreference(final String s) {
        this.mPreferences.edit().putString("pref_camera_recordlocation_key", s).apply();
        this.onSharedPreferenceChanged();
    }

    @TargetApi(14)
    private void setMeteringAreasIfSupported() {
        if (this.mMeteringAreaSupported) {
            this.mParameters.setMeteringAreas(this.mFocusManager.getMeteringAreas());
        }
    }

    private void setupCaptureParams() {
        final Bundle extras = this.mActivity.getIntent().getExtras();
        if (extras != null) {
            this.mSaveUri = (Uri)extras.getParcelable("output");
            this.mCropValue = extras.getString("crop");
        }
    }

    private void setupPreview() {
        this.mFocusManager.resetTouchFocus();
        this.startPreview();
        this.setCameraState(1);
        this.startFaceDetection();
    }

    private void showTapToFocusToastIfNeeded() {
        if (this.mFocusAreaSupported && this.mPreferences.getBoolean("pref_camera_first_use_hint_shown_key", true)) {
            this.mHandler.sendEmptyMessageDelayed(6, 1000L);
        }
    }

    private void startPreview() {
        this.mCameraDevice.setErrorCallback((Camera.ErrorCallback) this.mErrorCallback);
        if (this.mCameraState != 0) {
            this.stopPreview();
        }
        this.setDisplayOrientation();
        if (!this.mSnapshotOnIdle) {
            if ("continuous-picture".equals(this.mFocusManager.getFocusMode())) {
                this.mCameraDevice.cancelAutoFocus();
            }
            this.mFocusManager.setAeAwbLock(false);
        }
        this.setCameraParameters(-1);
        while(true) {
            if (!true) {//ApiHelper.HAS_SURFACE_TEXTURE
                this.mCameraDevice.setDisplayOrientation(this.mDisplayOrientation);
                this.mCameraDevice.setPreviewDisplayAsync(this.mUI.getSurfaceHolder());
                break;
            }
            final CameraScreenNail cameraScreenNail = (CameraScreenNail)this.mActivity.mCameraScreenNail;
            if (this.mUI.getSurfaceTexture() == null) {
                final Camera.Size previewSize = this.mParameters.getPreviewSize();
                if (this.mCameraDisplayOrientation % 180 == 0) {
                    cameraScreenNail.setSize(previewSize.width, previewSize.height);
                }
                else {
                    cameraScreenNail.setSize(previewSize.height, previewSize.width);
                }
                cameraScreenNail.enableAspectRatioClamping();
                cameraScreenNail.acquireSurfaceTexture();
                final CameraStartUpThread mCameraStartUpThread = this.mCameraStartUpThread;
                if (mCameraStartUpThread != null && mCameraStartUpThread.isCanceled()) {
                    return;
                }
                this.mUI.setSurfaceTexture(cameraScreenNail.getSurfaceTexture());
            }
            else {
                this.updatePreviewSize(cameraScreenNail);
            }
            this.mCameraDevice.setDisplayOrientation(this.mCameraDisplayOrientation);
            final Object surfaceTexture = this.mUI.getSurfaceTexture();
            if (surfaceTexture != null) {
                this.mCameraDevice.setPreviewTextureAsync((SurfaceTexture)surfaceTexture);
            }
            break;
        }
        Log.v("CAM_PhotoModule", "startPreview");
        this.mCameraDevice.startPreviewAsync();
        this.mFocusManager.onPreviewStarted();
        if (this.mSnapshotOnIdle) {
            this.mHandler.post(this.mDoSnapRunnable);
        }
    }

    private void switchCamera() {
        boolean mirror = true;
        if (!this.mPaused) {
            Log.v("CAM_PhotoModule", "Start to switch camera. id=" + this.mPendingSwitchCameraId);
            this.mCameraId = this.mPendingSwitchCameraId;
            this.mPendingSwitchCameraId = -1;
            this.setCameraId(this.mCameraId);
            this.closeCamera();
            this.mUI.clearFaces();
            if (this.mFocusManager != null) {
                this.mFocusManager.removeMessages();
            }
            while (true) {
                this.mPreferences.setLocalId((Context)this.mActivity, this.mCameraId);
                CameraSettings.upgradeLocalPreferences(this.mPreferences.getLocal());
                while (true) {
                    try {
                        this.mCameraDevice = Util.openCamera(this.mActivity, this.mCameraId);
                        this.mParameters = this.mCameraDevice.getParameters();
                        this.initializeCapabilities();
                        if (CameraHolder.instance().getCameraInfo()[this.mCameraId].facing == 1) {
                            this.mFocusManager.setMirror(mirror);
                            this.mFocusManager.setParameters(this.mInitialParams);
                            this.setupPreview();
                            this.openCameraCommon();
                            if (false) {//ApiHelper.HAS_SURFACE_TEXTURE
                                this.mHandler.sendEmptyMessage(8);
                                return;
                            }
                            break;
                        }
                    }
                    catch (CameraHardwareException ex) {
                        Util.showErrorAndFinish(this.mActivity, 2131558443);
                        return;
                    }
                    catch (CameraDisabledException ex2) {
                        Util.showErrorAndFinish(this.mActivity, 2131558430);
                        return;
                    }
                    mirror = false;
                    continue;
                }
            }
        }
    }

    @TargetApi(16)
    private void updateAutoFocusMoveCallback() {
        if (this.mParameters.getFocusMode().equals("continuous-picture")) {
            this.mCameraDevice.setAutoFocusMoveCallback((Camera.AutoFocusMoveCallback)this.mAutoFocusMoveCallback);
            return;
        }
        this.mCameraDevice.setAutoFocusMoveCallback(null);
    }

    private void updateCameraParametersInitialize() {
        final List supportedPreviewFrameRates = this.mParameters.getSupportedPreviewFrameRates();
        if (supportedPreviewFrameRates != null) {
            this.mParameters.setPreviewFrameRate((int)Collections.max((Collection<Integer>)supportedPreviewFrameRates));
        }
        this.mParameters.set("recording-hint", "false");
        if ("true".equals(this.mParameters.get("video-stabilization-supported"))) {
            this.mParameters.set("video-stabilization", "false");
        }
    }

    private void updateCameraParametersPreference() {
        this.setAutoExposureLockIfSupported();
        this.setAutoWhiteBalanceLockIfSupported();
        this.setFocusAreasIfSupported();
        this.setMeteringAreasIfSupported();
        final String string = this.mPreferences.getString("pref_camera_picturesize_key", null);
        if (string == null) {
            CameraSettings.initialCameraPictureSize((Context)this.mActivity, this.mParameters);
        }
        else {
            CameraSettings.setCameraPictureSize(string, this.mParameters.getSupportedPictureSizes(), this.mParameters);
        }
        final Camera.Size pictureSize = this.mParameters.getPictureSize();
        final Camera.Size optimalPreviewSize = Util.getOptimalPreviewSize(this.mActivity, this.mParameters.getSupportedPreviewSizes(), pictureSize.width / pictureSize.height);
        if (!this.mParameters.getPreviewSize().equals((Object)optimalPreviewSize)) {
            this.mParameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
            if (this.mHandler.getLooper() == Looper.myLooper()) {
                this.setupPreview();
            }
            else {
                this.mCameraDevice.setParameters(this.mParameters);
            }
            this.mParameters = this.mCameraDevice.getParameters();
        }
        Log.v("CAM_PhotoModule", "Preview size is " + optimalPreviewSize.width + "x" + optimalPreviewSize.height);
        if (this.mActivity.getString(R.string.setting_on_value).equals(this.mPreferences.getString("pref_camera_hdr_key", this.mActivity.getString(R.string.pref_camera_hdr_default)))) {
            this.mSceneMode = "hdr";
        }
        else {
            this.mSceneMode = this.mPreferences.getString("pref_camera_scenemode_key", this.mActivity.getString(R.string.pref_camera_scenemode_default));
        }
        if (Util.isSupported(this.mSceneMode, this.mParameters.getSupportedSceneModes())) {
            if (!this.mParameters.getSceneMode().equals(this.mSceneMode)) {
                this.mParameters.setSceneMode(this.mSceneMode);
                this.mCameraDevice.setParameters(this.mParameters);
                this.mParameters = this.mCameraDevice.getParameters();
            }
        }
        else {
            this.mSceneMode = this.mParameters.getSceneMode();
            if (this.mSceneMode == null) {
                this.mSceneMode = "auto";
            }
        }
        this.mParameters.setJpegQuality(CameraProfile.getJpegEncodingQualityParameter(this.mCameraId, 2));
        final int exposure = CameraSettings.readExposure(this.mPreferences);
        final int maxExposureCompensation = this.mParameters.getMaxExposureCompensation();
        if (exposure >= this.mParameters.getMinExposureCompensation() && exposure <= maxExposureCompensation) {
            this.mParameters.setExposureCompensation(exposure);
        }
        else {
            Log.w("CAM_PhotoModule", "invalid exposure range: " + exposure);
        }
        if ("auto".equals(this.mSceneMode)) {
            final String string2 = this.mPreferences.getString("pref_camera_flashmode_key", this.mActivity.getString(R.string.pref_camera_flashmode_default));
            if (Util.isSupported(string2, this.mParameters.getSupportedFlashModes())) {
                this.mParameters.setFlashMode(string2);
            }
            else if (this.mParameters.getFlashMode() == null) {
                this.mActivity.getString(R.string.pref_camera_flashmode_no_flash);
            }
            final String string3 = this.mPreferences.getString("pref_camera_whitebalance_key", this.mActivity.getString(R.string.pref_camera_whitebalance_default));
            if (Util.isSupported(string3, this.mParameters.getSupportedWhiteBalance())) {
                this.mParameters.setWhiteBalance(string3);
            }
            else if (this.mParameters.getWhiteBalance() == null) {}
            this.mFocusManager.overrideFocusMode(null);
            this.mParameters.setFocusMode(this.mFocusManager.getFocusMode());
        }
        else {
            this.mFocusManager.overrideFocusMode(this.mParameters.getFocusMode());
        }
        if (this.mContinousFocusSupported && true) {//ApiHelper.HAS_AUTO_FOCUS_MOVE_CALLBACK
            this.updateAutoFocusMoveCallback();
        }
    }

    private void updateCameraParametersZoom() {
        if (this.mParameters.isZoomSupported()) {
            this.mParameters.setZoom(this.mZoomValue);
        }
    }

    private void updatePreviewSize(final CameraScreenNail cameraScreenNail) {
        final Camera.Size previewSize = this.mParameters.getPreviewSize();
        int n = previewSize.width;
        int n2 = previewSize.height;
        if (this.mCameraDisplayOrientation % 180 != 0) {
            n = previewSize.height;
            n2 = previewSize.width;
        }
        if (cameraScreenNail.getWidth() != n || cameraScreenNail.getHeight() != n2) {
            cameraScreenNail.setSize(n, n2);
        }
        cameraScreenNail.enableAspectRatioClamping();
    }

    private void updateSceneMode() {
        if (!"auto".equals(this.mSceneMode)) {
            this.overrideCameraSettings(this.mParameters.getFlashMode(), this.mParameters.getWhiteBalance(), this.mParameters.getFocusMode());
            return;
        }
        this.overrideCameraSettings(null, null, null);
    }

    @Override
    public void autoFocus() {
        this.mFocusStartTime = System.currentTimeMillis();
        this.mCameraDevice.autoFocus((Camera.AutoFocusCallback)this.mAutoFocusCallback);
        this.setCameraState(2);
    }

    @Override
    public void cancelAutoFocus() {
        this.mCameraDevice.cancelAutoFocus();
        this.setCameraState(1);
        this.setCameraParameters(4);
    }

    @Override
    public boolean capture() {
        if (this.mCameraDevice == null || this.mCameraState == 3 || this.mCameraState == 4 || this.mActivity.getMediaSaveService().isQueueFull()) {
            return false;
        }
        this.mCaptureStartTime = System.currentTimeMillis();
        this.mPostViewPictureCallbackTime = 0L;
        this.mJpegImageData = null;
        boolean b;
        if (this.mSceneMode == "hdr") {
            b = true;
        }
        else {
            b = false;
        }
        while (true) {
            if (b) {
                this.mActivity.disableOrientationListener();
                this.mJpegRotation = Util.getJpegRotation(this.mCameraId, this.mOrientation);
                this.mParameters.setRotation(this.mJpegRotation);
                final Location currentLocation = this.mLocationManager.getCurrentLocation();
                Util.setGpsParameters(this.mParameters, currentLocation);
                this.mCameraDevice.setParameters(this.mParameters);
                this.mCameraDevice.takePicture2((Camera.ShutterCallback)new ShutterCallback(!b), (Camera.PictureCallback)this.mRawPictureCallback, (Camera.PictureCallback)this.mPostViewPictureCallback, (Camera.PictureCallback)new JpegPictureCallback(currentLocation), this.mCameraState, this.mFocusManager.getFocusState());
                this.mNamedImages.nameNewImage(this.mContentResolver, this.mCaptureStartTime);
                this.mFaceDetectionStarted = false;
                this.setCameraState(3);
                UsageStatistics.onEvent("Camera", "CaptureDone", "Photo");
                return true;
            }
            continue;
        }
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        return this.mCameraState == 4 || this.mUI.dispatchTouchEvent(motionEvent);
    }

    @Override
    public void easterEgg() {
    }

    @Override
    public int getCameraState() {
        return this.mCameraState;
    }

    @Override
    public ComboPreferences getComboPreferences() {
        return this.mPreferences;
    }

    @Override
    public void init(CameraActivity mActivity, final View view) {
        this.mActivity = mActivity;
        this.mUI = new PhotoUI(mActivity, this, view);
        this.mPreferences = new ComboPreferences((Context)this.mActivity);
        CameraSettings.upgradeGlobalPreferences(this.mPreferences.getGlobal());
        this.mCameraId = this.getPreferredCameraId(this.mPreferences);
        this.mContentResolver = this.mActivity.getContentResolver();
        (this.mCameraStartUpThread = new CameraStartUpThread()).start();
        this.mIsImageCaptureIntent = this.isImageCaptureIntent();
        mActivity = this.mActivity;
        mActivity.reuseCameraScreenNail(!this.mIsImageCaptureIntent);
        this.mPreferences.setLocalId((Context)this.mActivity, this.mCameraId);
        CameraSettings.upgradeLocalPreferences(this.mPreferences.getLocal());
        this.resetExposureCompensation();
        this.mStartPreviewPrerequisiteReady.open();
        this.mLocationManager = new LocationManager((Context)this.mActivity, (LocationManager.Listener)this.mUI);
        this.mSensorManager = (SensorManager)this.mActivity.getSystemService("sensor");
        this.isDone = false;
    }

    @Override
    public void installIntentFilter() {
    }

    @Override
    public boolean isCameraIdle() {
        return this.mCameraState == 1 || this.mCameraState == 0 || (this.mFocusManager != null && this.mFocusManager.isFocusCompleted() && this.mCameraState != 4);
    }

    public boolean isCountDown() {
        return this.mUI.isCountingDown();
    }

    @Override
    public boolean isDone() {
        return this.isDone;
    }

    @Override
    public boolean isImageCaptureIntent() {
        final String action = this.mActivity.getIntent().getAction();
        return "android.media.action.IMAGE_CAPTURE".equals(action) || "android.media.action.IMAGE_CAPTURE_SECURE".equals(action);
    }

    @Override
    public boolean needsPieMenu() {
        return true;
    }

    @Override
    public boolean needsSwitcher() {
        return !this.mIsImageCaptureIntent;
    }

    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }

    @Override
    public void onActivityResult(final int n, final int n2, final Intent intent) {
    }

    @Override
    public boolean onBackPressed() {
        if (this.mUI.isCountingDown()) {
            this.mUI.cancelCountDown();
            this.mActivity.setVisibilityTopButton(0);
            this.mFocusManager.onShutterUp();
            this.isDone = false;
            return true;
        }
        return this.mUI.onBackPressed();
    }

    @Override
    public void onCameraPickerClicked(final int mPendingSwitchCameraId) {
        if (this.mPaused || this.mPendingSwitchCameraId != -1) {
            return;
        }
        this.mPendingSwitchCameraId = mPendingSwitchCameraId;
        if (false) {//ApiHelper.HAS_SURFACE_TEXTURE
            Log.v("CAM_PhotoModule", "Start to copy texture. cameraId=" + mPendingSwitchCameraId);
            ((CameraScreenNail)this.mActivity.mCameraScreenNail).copyTexture();
            this.setCameraState(4);
            return;
        }
        this.switchCamera();
    }

    @Override
    public void onCaptureCancelled() {
        this.mActivity.setResultEx(0, new Intent());
        this.mActivity.finish();
    }

    @Override
    public void onCaptureDone() {
    }

    @Override
    public void onCaptureRetake() {
        if (this.mPaused) {
            return;
        }
        this.mUI.hidePostCaptureAlert();
        this.setupPreview();
    }

    @Override
    public void onCaptureTextureCopied() {
    }

    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        Log.v("CAM_PhotoModule", "onConfigurationChanged");
        this.setDisplayOrientation();
    }

    @Override
    public void onCountDownFinished() {
        this.isDone = true;
        this.mSnapshotOnIdle = false;
        this.mUI.cancelCountDown();
        this.mActivity.setVisibilityTopButton(0);
        this.mFocusManager.doSnap();
        this.mFocusManager.onShutterUp();
    }

    @Override
    public void onFinish() {
        this.mActivity.finish();
    }

    @Override
    public void onFullScreenChanged(final boolean fullScreen) {
        this.mUI.onFullScreenChanged(fullScreen);
        if (true && this.mActivity.mCameraScreenNail != null) {//ApiHelper.HAS_SURFACE_TEXTURE
            ((CameraScreenNail)this.mActivity.mCameraScreenNail).setFullScreen(fullScreen);
        }
    }

    @Override
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        final boolean b = true;
        boolean b2 = false;
        switch (n) {
            default: {
                b2 = false;
                break;
            }
            case 24:
            case 25:
            case 80: {
                if (!this.mActivity.isInCameraApp() || !this.mFirstTimeInitialized) {
                    return false;
                }
                b2 = b;
                if (keyEvent.getRepeatCount() == 0) {
                    this.onShutterButtonFocus(true);
                    return true;
                }
                break;
            }
            case 27: {
                b2 = b;
                if (!this.mFirstTimeInitialized) {
                    break;
                }
                b2 = b;
                if (keyEvent.getRepeatCount() == 0) {
                    this.onShutterButtonClick();
                    return true;
                }
                break;
            }
            case 23: {
                b2 = b;
                if (!this.mFirstTimeInitialized) {
                    break;
                }
                b2 = b;
                if (keyEvent.getRepeatCount() == 0) {
                    this.onShutterButtonFocus(true);
                    this.mUI.pressShutterButton();
                    return true;
                }
                break;
            }
        }
        return b2;
    }

    @Override
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        boolean b = true;
        switch (n) {
            default: {
                b = false;
                break;
            }
            case 24:
            case 25: {
                if (this.mActivity.isInCameraApp() && this.mFirstTimeInitialized) {
                    this.onShutterButtonClick();
                    return true;
                }
                return false;
            }
            case 80: {
                if (this.mFirstTimeInitialized) {
                    this.onShutterButtonFocus(false);
                    return true;
                }
                break;
            }
        }
        return b;
    }

    @Override
    public void onMediaSaveServiceConnected(final MediaSaveService mediaSaveService) {
        if (this.mFirstTimeInitialized) {
            mediaSaveService.setListener((MediaSaveService.Listener)this);
        }
    }

    @Override
    public void onOrientationChanged(final int mOrientation) {
        if (mOrientation != -1) {
            this.mOrientation = mOrientation;
            if (this.mHandler.hasMessages(6)) {
                this.mHandler.removeMessages(6);
            }
        }
    }

    @Override
    public void onOverriddenPreferencesClicked() {
        if (this.mPaused) {
            return;
        }
        this.mUI.showPreferencesToast();
    }

    @Override
    public void onPauseAfterSuper() {
        this.waitCameraStartUpThread();
        if (this.mCameraDevice != null && this.mCameraState != 0) {
            this.mCameraDevice.cancelAutoFocus();
        }
        this.stopPreview();
        ((CameraScreenNail)this.mActivity.mCameraScreenNail).releaseSurfaceTexture();
        this.mNamedImages = null;
        if (this.mLocationManager != null) {
            this.mLocationManager.recordLocation(false);
        }
        this.mJpegImageData = null;
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(5);
        this.mHandler.removeMessages(7);
        this.mHandler.removeMessages(8);
        this.mHandler.removeMessages(9);
        this.mHandler.removeMessages(10);
        this.mHandler.removeMessages(11);
        this.mHandler.removeMessages(12);
        this.closeCamera();
        this.resetScreenOn();
        this.mUI.onPause();
        this.mPendingSwitchCameraId = -1;
        if (this.mFocusManager != null) {
            this.mFocusManager.removeMessages();
        }
        final MediaSaveService mediaSaveService = this.mActivity.getMediaSaveService();
        if (mediaSaveService != null) {
            mediaSaveService.setListener(null);
        }
    }

    @Override
    public void onPauseBeforeSuper() {
        this.mPaused = true;
        final Sensor defaultSensor = this.mSensorManager.getDefaultSensor(1);
        if (defaultSensor != null) {
            this.mSensorManager.unregisterListener((SensorEventListener)this, defaultSensor);
        }
        final Sensor defaultSensor2 = this.mSensorManager.getDefaultSensor(2);
        if (defaultSensor2 != null) {
            this.mSensorManager.unregisterListener((SensorEventListener)this, defaultSensor2);
        }
    }

    @Override
    public void onPreviewTextureCopied() {
        this.mHandler.sendEmptyMessage(7);
    }

    @Override
    public void onQueueStatus(final boolean b) {
        this.mUI.enableShutter(!b);
    }

    @Override
    public void onRestorePreferencesClicked() {
    }

    @Override
    public void onResumeAfterSuper() {
        if (!this.mOpenCameraFail && !this.mCameraDisabled) {
            this.mJpegPictureCallbackTime = 0L;
            this.mZoomValue = 0;
            if (this.mCameraState == 0 && this.mCameraStartUpThread == null) {
                this.resetExposureCompensation();
                (this.mCameraStartUpThread = new CameraStartUpThread()).start();
            }
            if (!this.mFirstTimeInitialized) {
                this.mHandler.sendEmptyMessage(2);
            }
            else {
                this.initializeSecondTime();
            }
            this.keepScreenOnAwhile();
            UsageStatistics.onContentViewChanged("Camera", "PhotoModule");
            final Sensor defaultSensor = this.mSensorManager.getDefaultSensor(1);
            if (defaultSensor != null) {
                this.mSensorManager.registerListener((SensorEventListener)this, defaultSensor, 3);
            }
            final Sensor defaultSensor2 = this.mSensorManager.getDefaultSensor(2);
            if (defaultSensor2 != null) {
                this.mSensorManager.registerListener((SensorEventListener)this, defaultSensor2, 3);
            }
        }
    }

    @Override
    public void onResumeBeforeSuper() {
        this.mPaused = false;
        this.isDone = false;
    }

    @Override
    public void onScreenSizeChanged(final int n, final int n2, final int n3, final int n4) {
        if (this.mFocusManager != null) {
            this.mFocusManager.setPreviewSize(n, n2);
        }
        ((CameraScreenNail)this.mActivity.mCameraScreenNail).setPreviewFrameLayoutSize(n3, n4);
    }

    public void onSensorChanged(final SensorEvent sensorEvent) {
        final int type = sensorEvent.sensor.getType();
        float[] array;
        if (type == 1) {
            array = this.mGData;
        }
        else {
            if (type != 2) {
                return;
            }
            array = this.mMData;
        }
        for (int i = 0; i < 3; ++i) {
            array[i] = sensorEvent.values[i];
        }
        final float[] array2 = new float[3];
        SensorManager.getRotationMatrix(this.mR, (float[])null, this.mGData, this.mMData);
        SensorManager.getOrientation(this.mR, array2);
        this.mHeading = (int)(array2[0] * 180.0f / 3.141592653589793) % 360;
        if (this.mHeading < 0) {
            this.mHeading += 360;
        }
    }

    @Override
    public void onSharedPreferenceChanged() {
        if (this.mPaused) {
            return;
        }
        this.mLocationManager.recordLocation(RecordLocationPreference.get((SharedPreferences)this.mPreferences, this.mContentResolver));
        this.setCameraParametersWhenIdle(4);
        this.mUI.updateOnScreenIndicators(this.mParameters, this.mPreferenceGroup, this.mPreferences);
    }

    @Override
    public void onShowSwitcherPopup() {
    }

    @Override
    public void onShutterButtonClick() {
        if (this.mPaused || this.mCameraState == 4 || this.mCameraState == 0) {
            return;
        }
        if (this.mActivity.getStorageSpace() <= 50000000L) {
            Log.i("CAM_PhotoModule", "Not enough space or storage not ready. remaining=" + this.mActivity.getStorageSpace());
            return;
        }
        Log.v("CAM_PhotoModule", "onShutterButtonClick: mCameraState=" + this.mCameraState);
        if ((this.mFocusManager.isFocusingSnapOnFinish() || this.mCameraState == 3) && !this.mIsImageCaptureIntent) {
            this.mSnapshotOnIdle = true;
            return;
        }
        final String string = this.mPreferences.getString("pref_camera_timer_key", this.mActivity.getString(R.string.pref_camera_timer_default));
        final boolean equals = this.mPreferences.getString("pref_camera_timer_sound_key", this.mActivity.getString(R.string.pref_camera_timer_sound_default)).equals(this.mActivity.getString(R.string.setting_on_value));
        final int int1 = Integer.parseInt(string);
        if (this.mUI.isCountingDown()) {
            this.mUI.cancelCountDown();
            this.mActivity.setVisibilityTopButton(0);
            this.mFocusManager.onShutterUp();
            return;
        }
        if (int1 > 0 && !this.mActivity.isOnlyPhoto()) {
            this.mActivity.setVisibilityTopButton(8);
            this.mUI.startCountDown(int1, equals);
            return;
        }
        this.isDone = true;
        this.mSnapshotOnIdle = false;
        this.mFocusManager.doSnap();
    }

    @Override
    public void onShutterButtonFocus(final boolean b) {
        if (!this.mPaused && this.mCameraState != 3 && this.mCameraState != 0 && (!b || this.canTakePicture())) {
            if (b) {
                this.mFocusManager.onShutterDown();
                return;
            }
            if (!this.mUI.isCountingDown()) {
                this.mFocusManager.onShutterUp();
            }
        }
    }

    @Override
    public void onSingleTapUp(final View view, final int n, final int n2) {
        if (!this.mPaused && this.mCameraDevice != null && this.mFirstTimeInitialized && this.mCameraState != 3 && this.mCameraState != 4 && this.mCameraState != 0 && (this.mFocusAreaSupported || this.mMeteringAreaSupported)) {
            this.mFocusManager.onSingleTapUp(n, n2);
        }
    }

    @Override
    public void onStop() {
        if (this.mMediaProviderClient != null) {
            this.mMediaProviderClient.release();
            this.mMediaProviderClient = null;
        }
    }

    @Override
    public void onSurfaceCreated(final SurfaceHolder previewDisplayAsync) {
        if (this.mCameraDevice != null && this.mCameraStartUpThread == null) {
            this.mCameraDevice.setPreviewDisplayAsync(previewDisplayAsync);
            if (this.mCameraState == 0) {
                this.setupPreview();
            }
        }
    }

    @Override
    public void onUserInteraction() {
        if (!this.mActivity.isFinishing()) {
            this.keepScreenOnAwhile();
        }
    }

    @Override
    public int onZoomChanged(final int mZoomValue) {
        if (!this.mPaused) {
            this.mZoomValue = mZoomValue;
            if (this.mParameters != null && this.mCameraDevice != null) {
                this.mParameters.setZoom(this.mZoomValue);
                this.mCameraDevice.setParameters(this.mParameters);
                final Camera.Parameters parameters = this.mCameraDevice.getParameters();
                if (parameters != null) {
                    return parameters.getZoom();
                }
            }
        }
        return mZoomValue;
    }

    protected void setCameraId(final int n) {
        this.mPreferenceGroup.findPreference("pref_camera_id_key").setValue("" + n);
    }

    @Override
    public void setFocusParameters() {
        this.setCameraParameters(4);
    }

    @TargetApi(14)
    @Override
    public void startFaceDetection() {
        boolean b = true;
        if (true && !this.mFaceDetectionStarted && this.mParameters.getMaxNumDetectedFaces() > 0) {//ApiHelper.HAS_FACE_DETECTION
            this.mFaceDetectionStarted = true;
            final Camera.CameraInfo cameraCameraInfo = CameraHolder.instance().getCameraInfo()[this.mCameraId];
            final PhotoUI mui = this.mUI;
            final int mDisplayOrientation = this.mDisplayOrientation;
            if (cameraCameraInfo.facing != 1) {
                b = false;
            }
            mui.onStartFaceDetection(mDisplayOrientation, b);
            this.mCameraDevice.setFaceDetectionListener((Camera.FaceDetectionListener)this.mUI);
            this.mCameraDevice.startFaceDetection();
        }
    }

    @TargetApi(14)
    @Override
    public void stopFaceDetection() {
        if (true && this.mFaceDetectionStarted && this.mParameters.getMaxNumDetectedFaces() > 0) {//ApiHelper.HAS_FACE_DETECTION
            this.mFaceDetectionStarted = false;
            this.mCameraDevice.setFaceDetectionListener(null);
            this.mCameraDevice.stopFaceDetection();
            this.mUI.clearFaces();
        }
    }

    @Override
    public void stopPreview() {
        if (this.mCameraDevice != null && this.mCameraState != 0) {
            Log.v("CAM_PhotoModule", "stopPreview");
            this.mCameraDevice.stopPreview();
            this.mFaceDetectionStarted = false;
        }
        this.setCameraState(0);
        if (this.mFocusManager != null) {
            this.mFocusManager.onPreviewStopped();
        }
    }

    @Override
    public void updateCameraAppView() {
    }

    @Override
    public boolean updateStorageHintOnResume() {
        return this.mFirstTimeInitialized;
    }

    void waitCameraStartUpThread() {
        try {
            if (this.mCameraStartUpThread != null) {
                this.mCameraStartUpThread.cancel();
                this.mCameraStartUpThread.join();
                this.mCameraStartUpThread = null;
                this.setCameraState(1);
            }
        }
        catch (InterruptedException ex) {}
    }

    private final class AutoFocusCallback implements Camera.AutoFocusCallback
    {
        public void onAutoFocus(final boolean b, final Camera camera) {
            if (PhotoModule.this.mPaused) {
                return;
            }
            PhotoModule.this.mAutoFocusTime = System.currentTimeMillis() - PhotoModule.this.mFocusStartTime;
            Log.v("CAM_PhotoModule", "mAutoFocusTime = " + PhotoModule.this.mAutoFocusTime + "ms");
            PhotoModule.this.setCameraState(1);
            PhotoModule.this.mFocusManager.onAutoFocus(b, PhotoModule.this.mUI.isShutterPressed());
        }
    }

    @TargetApi(16)
    private final class AutoFocusMoveCallback implements Camera.AutoFocusMoveCallback
    {
        public void onAutoFocusMoving(final boolean b, final Camera camera) {
            PhotoModule.this.mFocusManager.onAutoFocusMoving(b);
        }
    }

    private class CameraStartUpThread extends Thread
    {
        private volatile boolean mCancelled;

        public void cancel() {
            this.mCancelled = true;
            this.interrupt();
        }

        public boolean isCanceled() {
            return this.mCancelled;
        }

        @Override
        public void run() {
            try {
                if (this.mCancelled) {
                    return;
                }
                PhotoModule.this.mCameraDevice = Util.openCamera(PhotoModule.this.mActivity, PhotoModule.this.mCameraId);
                PhotoModule.this.mParameters = PhotoModule.this.mCameraDevice.getParameters();
                PhotoModule.this.mStartPreviewPrerequisiteReady.block();
                PhotoModule.this.initializeCapabilities();
                if (PhotoModule.this.mFocusManager == null) {
                    PhotoModule.this.initializeFocusManager();
                }
                if (!this.mCancelled) {
                    PhotoModule.this.setCameraParameters(-1);
                    PhotoModule.this.mHandler.sendEmptyMessage(9);
                    if (!this.mCancelled) {
                        PhotoModule.this.startPreview();
                        PhotoModule.this.mHandler.sendEmptyMessage(10);
                        PhotoModule.this.mOnResumeTime = SystemClock.uptimeMillis();
                        PhotoModule.this.mHandler.sendEmptyMessage(5);
                    }
                }
            }
            catch (CameraHardwareException ex) {
                PhotoModule.this.mHandler.sendEmptyMessage(11);
            }
            catch (CameraDisabledException ex2) {
                PhotoModule.this.mHandler.sendEmptyMessage(12);
            }
        }
    }

    private final class JpegPictureCallback implements Camera.PictureCallback
    {
        Location mLocation;

        public JpegPictureCallback(final Location mLocation) {
            super();
            this.mLocation = mLocation;
        }

        public void onPictureTaken(final byte[] array, final Camera camera) {
            if (PhotoModule.this.mPaused) {
                return;
            }
            PhotoModule.this.mJpegPictureCallbackTime = System.currentTimeMillis();
            if (PhotoModule.this.mPostViewPictureCallbackTime != 0L) {
                PhotoModule.this.mShutterToPictureDisplayedTime = PhotoModule.this.mPostViewPictureCallbackTime - PhotoModule.this.mShutterCallbackTime;
                PhotoModule.this.mPictureDisplayedToJpegCallbackTime = PhotoModule.this.mJpegPictureCallbackTime - PhotoModule.this.mPostViewPictureCallbackTime;
            }
            else {
                PhotoModule.this.mShutterToPictureDisplayedTime = PhotoModule.this.mRawPictureCallbackTime - PhotoModule.this.mShutterCallbackTime;
                PhotoModule.this.mPictureDisplayedToJpegCallbackTime = PhotoModule.this.mJpegPictureCallbackTime - PhotoModule.this.mRawPictureCallbackTime;
            }
            Log.v("CAM_PhotoModule", "mPictureDisplayedToJpegCallbackTime = " + PhotoModule.this.mPictureDisplayedToJpegCallbackTime + "ms");
            if (true && !PhotoModule.this.mIsImageCaptureIntent && PhotoModule.this.mActivity.mShowCameraAppView) {//ApiHelper.HAS_SURFACE_TEXTURE
                ((CameraScreenNail)PhotoModule.this.mActivity.mCameraScreenNail).animateSlide();
            }
            PhotoModule.this.mFocusManager.updateFocusUI();
            if (!PhotoModule.this.mIsImageCaptureIntent) {
                if (true){//ApiHelper.CAN_START_PREVIEW_IN_JPEG_CALLBACK
                    PhotoModule.this.setupPreview();
                }
                else {
                    PhotoModule.this.mHandler.sendEmptyMessageDelayed(1, 300L);
                }
            }
            final Camera.Size pictureSize = PhotoModule.this.mParameters.getPictureSize();
//            final ExifInterface exif = Exif.getExif(array);
//            final int orientation = Exif.getOrientation(exif);
            int n;
            int n2;
//            if ((PhotoModule.this.mJpegRotation + orientation) % 180 == 0) {
//                n = pictureSize.width;
//                n2 = pictureSize.height;
//            }

                n = pictureSize.height;
                n2 = pictureSize.width;

            final String title = PhotoModule.this.mNamedImages.getTitle();
            final long date = PhotoModule.this.mNamedImages.getDate();
            if (title == null) {
                Log.e("CAM_PhotoModule", "Unbalanced name/data pair");
            }
            else if ("Y".equals(CameraUtils.getSaveOriginalPhotoYn())) {
                long mCaptureStartTime = date;
                if (date == -1L) {
                    mCaptureStartTime = PhotoModule.this.mCaptureStartTime;
                }
                if (PhotoModule.this.mHeading >= 0) {
//                    final ExifTag buildTag = exif.buildTag(ExifInterface.TAG_GPS_IMG_DIRECTION_REF, "M");
//                    final ExifTag buildTag2 = exif.buildTag(ExifInterface.TAG_GPS_IMG_DIRECTION, new Rational(PhotoModule.this.mHeading, 1L));
//                    exif.setTag(buildTag);
//                    exif.setTag(buildTag2);
                }
//                PhotoModule.this.mActivity.getMediaSaveService().addImage(array, title, mCaptureStartTime, this.mLocation, n, n2, orientation, exif, PhotoModule.this.mOnMediaSavedListener, PhotoModule.this.mContentResolver);
            }
            PhotoModule.this.mActivity.updateStorageSpaceAndHint();
            PhotoModule.this.mJpegCallbackFinishTime = System.currentTimeMillis() - PhotoModule.this.mJpegPictureCallbackTime;
            Log.v("CAM_PhotoModule", "mJpegCallbackFinishTime = " + PhotoModule.this.mJpegCallbackFinishTime + "ms");
            PhotoModule.this.mJpegPictureCallbackTime = 0L;
//            CameraActivity.photoData = new PhotoData(BitmapUtils.convertByteToBitmap(array), orientation, PhotoModule.this.mActivity.isSquare());
            if (CameraHolder.instance().getCameraInfo()[PhotoModule.this.mCameraId].facing == 1) {
                CameraActivity.photoData.setPhoto(BitmapUtils.getLeftRightInversionBitmap(CameraActivity.photoData.getPhoto()));
            }
            PhotoModule.this.mActivity.startFilterActivity();
        }
    }

    private class MainHandler extends Handler
    {
        public void handleMessage(final Message message) {
            switch (message.what) {
                case 1: {
                    PhotoModule.this.setupPreview();
                }
                case 3: {
                    PhotoModule.this.mActivity.getWindow().clearFlags(128);
                }
                case 2: {
                    PhotoModule.this.initializeFirstTime();
                }
                case 4: {
                    PhotoModule.this.setCameraParametersWhenIdle(0);
                }
                case 5: {
                    if (Util.getDisplayRotation(PhotoModule.this.mActivity) != PhotoModule.this.mDisplayRotation) {
                        PhotoModule.this.setDisplayOrientation();
                    }
                    if (SystemClock.uptimeMillis() - PhotoModule.this.mOnResumeTime < 5000L) {
                        PhotoModule.this.mHandler.sendEmptyMessageDelayed(5, 100L);
                        return;
                    }
                    break;
                }
                case 7: {
                    PhotoModule.this.switchCamera();
                }
                case 8: {
                    ((CameraScreenNail)PhotoModule.this.mActivity.mCameraScreenNail).animateSwitchCamera();
                }
                case 9: {
                    PhotoModule.this.onCameraOpened();
                }
                case 10: {
                    PhotoModule.this.onPreviewStarted();
                }
                case 11: {
                    PhotoModule.this.mCameraStartUpThread = null;
                    PhotoModule.this.mOpenCameraFail = true;
                    Util.showErrorAndFinish(PhotoModule.this.mActivity, 2131558443);
                }
                case 12: {
                    PhotoModule.this.mCameraStartUpThread = null;
                    PhotoModule.this.mCameraDisabled = true;
                    Util.showErrorAndFinish(PhotoModule.this.mActivity, 2131558430);
                }
            }
        }
    }

    private static class NamedImages
    {
        private NamedEntity mNamedEntity;
        private ArrayList<NamedEntity> mQueue;
        private boolean mStop;

        public NamedImages() {
            super();
            this.mQueue = new ArrayList<NamedEntity>();
        }

        public long getDate() {
            if (this.mNamedEntity == null) {
                return -1L;
            }
            return this.mNamedEntity.date;
        }

        public String getTitle() {
            if (this.mQueue.isEmpty()) {
                this.mNamedEntity = null;
                return null;
            }
            this.mNamedEntity = this.mQueue.get(0);
            this.mQueue.remove(0);
            return this.mNamedEntity.title;
        }

        public void nameNewImage(final ContentResolver contentResolver, final long date) {
            final NamedEntity namedEntity = new NamedEntity();
            namedEntity.title = Util.createJpegName(date);
            namedEntity.date = date;
            this.mQueue.add(namedEntity);
        }

        private static class NamedEntity
        {
            long date;
            String title;
        }
    }

    private final class PostViewPictureCallback implements Camera.PictureCallback
    {
        public void onPictureTaken(final byte[] array, final Camera camera) {
            PhotoModule.this.mPostViewPictureCallbackTime = System.currentTimeMillis();
            Log.v("CAM_PhotoModule", "mShutterToPostViewCallbackTime = " + (PhotoModule.this.mPostViewPictureCallbackTime - PhotoModule.this.mShutterCallbackTime) + "ms");
        }
    }

    private final class RawPictureCallback implements Camera.PictureCallback
    {
        public void onPictureTaken(final byte[] array, final Camera camera) {
            PhotoModule.this.mRawPictureCallbackTime = System.currentTimeMillis();
            Log.v("CAM_PhotoModule", "mShutterToRawCallbackTime = " + (PhotoModule.this.mRawPictureCallbackTime - PhotoModule.this.mShutterCallbackTime) + "ms");
        }
    }

    private final class ShutterCallback implements Camera.ShutterCallback
    {
        private boolean mAnimateFlash;

        public ShutterCallback(final boolean mAnimateFlash) {
            super();
            this.mAnimateFlash = mAnimateFlash;
        }

        public void onShutter() {
            PhotoModule.this.mShutterCallbackTime = System.currentTimeMillis();
            PhotoModule.this.mShutterLag = PhotoModule.this.mShutterCallbackTime - PhotoModule.this.mCaptureStartTime;
            Log.v("CAM_PhotoModule", "mShutterLag = " + PhotoModule.this.mShutterLag + "ms");
            if (this.mAnimateFlash) {
                PhotoModule.this.mActivity.runOnUiThread(PhotoModule.this.mFlashRunnable);
            }
        }
    }
}