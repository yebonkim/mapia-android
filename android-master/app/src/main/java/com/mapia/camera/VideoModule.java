package com.mapia.camera;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.location.Location;
import android.media.CamcorderProfile;
import android.media.CameraProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.mapia.R;
import com.mapia.camera.ui.RecordingProgressBar;
import com.mapia.camera.util.UsageStatistics;
import com.mapia.util.FileUtils;
import com.mapia.util.MapiaToast;
import com.mapia.videoplayer.VideoSDKListener;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class VideoModule implements CameraModule, VideoController, CameraPreference.OnPreferenceChangedListener, ShutterButton.OnShutterButtonListener, MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener, RecordingProgressBar.RecordingProgressBarListener, RecordingProgressBar.OnAddMediaListener, FocusOverlayManager.Listener {
    private static final int CAMERA_OPEN_DONE = 13;
    private static final int CHECK_DISPLAY_ROTATION = 3;
    private static final int CLEAR_SCREEN_DELAY = 4;
    private static final int ENABLE_SHUTTER_BUTTON = 6;
    private static final String EXTRA_QUICK_CAPTURE = "android.intent.extra.quickCapture";
    private static final int HIDE_SURFACE_VIEW = 10;
    private static final int MIN_THUMB_SIZE = 64;
    private static final int SCREEN_DELAY = 120000;
    private static final long SHUTTER_BUTTON_TIMEOUT = 500L;
    private static final int SWITCH_CAMERA = 8;
    private static final int SWITCH_CAMERA_START_ANIMATION = 9;
    private static final String TAG = "CAM_VideoModule";
    private static final int UPDATE_PROGRESS_BAR = 12;
    private static final int UPDATE_RECORD_TIME = 5;
    boolean fail;
    private boolean ifFirstRecording;
    private boolean isDone;
    private boolean isMerging;
    private CameraActivity mActivity;
    private final AutoFocusCallback mAutoFocusCallback;
    public long mAutoFocusTime;
    private int mCameraDisplayOrientation;
    private int mCameraId;
    private Boolean mCameraOpened;
    private boolean mCaptureTimeLapse;
    private ContentResolver mContentResolver;
    private String mCurrentVideoFilename;
    private Uri mCurrentVideoUri;
    private ContentValues mCurrentVideoValues;
    private int mDesiredPreviewHeight;
    private int mDesiredPreviewWidth;
    private int mDisplayRotation;
    private final CameraErrorCallback mErrorCallback;
    private boolean mFocusAreaSupported;
    private FocusOverlayManager mFocusManager;
    private long mFocusStartTime;
    private CameraScreenNail.OnFrameDrawnListener mFrameDrawnListener;
    private final Handler mHandler;
    private boolean mIsInReviewMode;
    private boolean mIsVideoCaptureIntent;
    private LocationManager mLocationManager;
    private int mMaxVideoDurationInMs;
    private MediaRecorder mMediaRecorder;
    private boolean mMediaRecorderEnd;
    private boolean mMediaRecorderRecording;
    private boolean mMeteringAreaSupported;
    private final MediaSaveService.OnMediaSavedListener mOnPhotoSavedListener;
    private long mOnResumeTime;
    private final MediaSaveService.OnMediaSavedListener mOnVideoSavedListener;
    private int mOrientation;
    private Camera.Parameters mParameters;
    private boolean mPaused;
    private int mPendingSwitchCameraId;
    private PreferenceGroup mPreferenceGroup;
    private ComboPreferences mPreferences;
    boolean mPreviewing;
    private CamcorderProfile mProfile;
    private boolean mQuickCapture;
    private BroadcastReceiver mReceiver;
    private long mRecordedTime;
    private long mRecordingStartTime;
    private boolean mRestoreFlash;
    private boolean mSwitchingCamera;
    private int mThrottleDuration;
    private int mTimeBetweenTimeLapseFrameCaptureMs;
    private VideoUI mUI;
    private ParcelFileDescriptor mVideoFileDescriptor;
    private String mVideoFilename;
    private VideoSDKListener mVideoSDKListener;
    private int mZoomValue;
    private int mediaDuration;
    private String mediaPath;
    private View.OnClickListener onClickNextButtonListener;
    private View.OnClickListener onClickTrashButtonListener;
    private String resultFilePath;
    private int videoFrameHeight;
    private int videoFrameWidth;

    public VideoModule() {
        super();
        this.mCameraOpened = false;
        this.mErrorCallback = new CameraErrorCallback();
        this.mIsInReviewMode = false;
        this.mMediaRecorderRecording = false;
        this.mMediaRecorderEnd = false;
        this.mRecordedTime = 0L;
        this.mCaptureTimeLapse = false;
        this.mTimeBetweenTimeLapseFrameCaptureMs = 0;
        this.mPreviewing = false;
        this.mHandler = new MainHandler();
        this.mOrientation = -1;
        this.ifFirstRecording = true;
        this.mAutoFocusCallback = new AutoFocusCallback();
        this.mOnVideoSavedListener = new MediaSaveService.OnMediaSavedListener() {
            @Override
            public void onMediaSaved(final Uri uri) {
                if (uri != null) {
                    VideoModule.this.mActivity.sendBroadcast(new Intent("android.hardware.action.NEW_VIDEO", uri));
                    Util.broadcastNewPicture((Context) VideoModule.this.mActivity, uri);
                }
            }
        };
        this.mOnPhotoSavedListener = new MediaSaveService.OnMediaSavedListener() {
            @Override
            public void onMediaSaved(final Uri uri) {
                if (uri != null) {
                    Util.broadcastNewPicture((Context) VideoModule.this.mActivity, uri);
                }
            }
        };
        this.mReceiver = null;
        this.fail = false;
        this.mediaPath = null;
        this.mediaDuration = 0;
        this.videoFrameWidth = 0;
        this.videoFrameHeight = 0;
        this.onClickTrashButtonListener = (View.OnClickListener) new View.OnClickListener() {
            public void onClick(final View view) {
                if (!VideoModule.this.isMerging) {
                    final int trashButtonStatus = VideoModule.this.mUI.getTrashButtonStatus();
                    if (trashButtonStatus == 1) {
                        VideoModule.this.mUI.setProgressBarStatus(RecordingProgressBar.Status.SELECTING, true);
                        VideoModule.this.mUI.setTrashButtonStatus(2);
                        return;
                    }
                    if (trashButtonStatus == 2) {
                        if (VideoModule.this.mMediaRecorderEnd) {
                            VideoModule.this.mMediaRecorderEnd = false;
                            VideoModule.this.mUI.showRecordIncompleteUI();
                        }
                        VideoModule.this.mUI.setTrashButtonStatus(1);
                        final List<RecordingProgressBar.Clip> clips = VideoModule.this.mUI.getClips();
                        if (clips != null && clips.size() > 0) {
                            VideoModule.this.deleteVideoFile(clips.get(clips.size() - 1).getPath());
                        }
                        VideoModule.this.mUI.setProgressBarStatus(RecordingProgressBar.Status.DELETE, true);
                    }
                }
            }
        };
        this.onClickNextButtonListener = (View.OnClickListener) new View.OnClickListener() {
            public void onClick(final View view) {
                if (!VideoModule.this.isMerging) {
//                    AceUtils.nClick(NClicks.CAMERA_VIDEO_NEXT);
                    if (VideoModule.this.mUI.getNextButtonStatus() == 4) {
                        VideoModule.this.isDone = true;
                        VideoModule.this.mergeVideoClips();
                    }
                }
            }
        };
        this.mVideoSDKListener = new VideoSDKListener() {
            @Override
            public void onError(final int n, final int n2) {
                VideoModule.this.mActivity.runOnUiThread((Runnable) new Runnable() {
                    @Override
                    public void run() {
                        MapiaToast.show(VideoModule.this.mActivity, VideoModule.this.mActivity.getString(R.string.camera_error_4), 1);
                    }
                });
            }

            @Override
            public void onProgress(final int n, final VideoSDKListener.STATUS status, final int n2) {
                VideoModule.this.mActivity.runOnUiThread((Runnable) new Runnable() {
                    @Override
                    public void run() {
                        if (status != VideoSDKListener.STATUS.COMPLETE) {
                            VideoModule.this.mUI.updateProgress(n2);
                            return;
                        }
//                        VideoSDK.setVideoSDKListener(null);
                        if (StringUtils.isNotEmpty(VideoModule.this.resultFilePath)) {
                            VideoModule.this.mActivity.startFilterActivity(VideoModule.this.resultFilePath);
                            return;
                        }
                        MapiaToast.show(VideoModule.this.mActivity, VideoModule.this.mActivity.getString(R.string.camera_error_4), 1);
                        VideoModule.this.mActivity.finish();
                    }
                });
            }
        };
    }

    private void cleanupEmptyFile() {
        if (this.mVideoFilename != null) {
            final File file = new File(this.mVideoFilename);
            if (file.length() == 0L && file.delete()) {
                Log.v("CAM_VideoModule", "Empty video file deleted: " + this.mVideoFilename);
                this.mVideoFilename = null;
            }
        }
    }

    private void closeCamera() {
        Log.v("CAM_VideoModule", "closeCamera");
        if (this.mActivity.mCameraDevice == null) {
            return;
        }
        this.mActivity.mCameraDevice.setZoomChangeListener(null);
        this.mActivity.mCameraDevice.setErrorCallback(null);
        synchronized (this.mCameraOpened) {
            if (this.mCameraOpened) {
                CameraHolder.instance().release();
            }
            // monitorexit(this.mCameraOpened = Boolean.valueOf(false))
            this.mActivity.mCameraDevice = null;
            this.mPreviewing = false;
        }
    }

    private void closeVideoFileDescriptor() {
        if (this.mVideoFileDescriptor == null) {
            return;
        }
        while (true) {
            try {
                this.mVideoFileDescriptor.close();
                this.mVideoFileDescriptor = null;
            } catch (IOException ex) {
                Log.e("CAM_VideoModule", "Fail to close fd", (Throwable) ex);
                continue;
            }
            break;
        }
    }

    private String convertOutputFormatToFileExt(final int n) {
        if (n == 2) {
            return ".mp4";
        }
        return ".3gp";
    }

    private String convertOutputFormatToMimeType(final int n) {
        if (n == 2) {
            return "video/mp4";
        }
        return "video/3gpp";
    }

    private void deleteVideoFile(final String s) {
        Log.v("CAM_VideoModule", "Deleting video " + s);
        if (!new File(s).delete()) {
            Log.v("CAM_VideoModule", "Could not delete " + s);
        }
    }

    private void doReturnToCaller(final boolean b) {
        final Intent intent = new Intent();
        int n;
        if (b) {
            n = -1;
            intent.setData(this.mCurrentVideoUri);
        } else {
            n = 0;
        }
        this.mActivity.setResultEx(n, intent);
        this.mActivity.finish();
    }

    private PreferenceGroup filterPreferenceScreenByIntent(final PreferenceGroup preferenceGroup) {
        final Intent intent = this.mActivity.getIntent();
        if (intent.hasExtra("android.intent.extra.videoQuality")) {
            CameraSettings.removePreferenceFromScreen(preferenceGroup, "pref_video_quality_key");
        }
        if (intent.hasExtra("android.intent.extra.durationLimit")) {
            CameraSettings.removePreferenceFromScreen(preferenceGroup, "pref_video_quality_key");
        }
        return preferenceGroup;
    }

    private void generateVideoFilename(final int n) {
        final long currentTimeMillis = System.currentTimeMillis();
        final String videoName = FileUtils.createVideoName((Context) this.mActivity, currentTimeMillis);
        final String string = videoName + this.convertOutputFormatToFileExt(n);
        final String convertOutputFormatToMimeType = this.convertOutputFormatToMimeType(n);
        final String string2 = Storage.DIRECTORY + '/' + string;
        final String string3 = string2 + ".tmp";
        (this.mCurrentVideoValues = new ContentValues(9)).put("title", videoName);
        this.mCurrentVideoValues.put("_display_name", string);
        this.mCurrentVideoValues.put("datetaken", currentTimeMillis);
        this.mCurrentVideoValues.put("date_modified", currentTimeMillis / 1000L);
        this.mCurrentVideoValues.put("mime_type", convertOutputFormatToMimeType);
        this.mCurrentVideoValues.put("_data", string2);
        this.mCurrentVideoValues.put("resolution", Integer.toString(this.mProfile.videoFrameWidth) + "x" + Integer.toString(this.mProfile.videoFrameHeight));
        final Location currentLocation = this.mLocationManager.getCurrentLocation();
        if (currentLocation != null) {
            this.mCurrentVideoValues.put("latitude", currentLocation.getLatitude());
            this.mCurrentVideoValues.put("longitude", currentLocation.getLongitude());
        }
        this.mVideoFilename = string3;
        Log.v("CAM_VideoModule", "New video filename: " + this.mVideoFilename);
    }

    @TargetApi(11)
    private void getDesiredPreviewSize() {
        this.mParameters = this.mActivity.mCameraDevice.getParameters();
        if (false) {//ApiHelper.HAS_GET_SUPPORTED_VIDEO_SIZE
            if (this.mParameters.getSupportedVideoSizes() == null) {
                this.mDesiredPreviewWidth = this.mProfile.videoFrameWidth;
                this.mDesiredPreviewHeight = this.mProfile.videoFrameHeight;
            } else {
                final List supportedPreviewSizes = this.mParameters.getSupportedPreviewSizes();
                final Camera.Size preferredPreviewSizeForVideo = this.mParameters.getPreferredPreviewSizeForVideo();
                final int width = preferredPreviewSizeForVideo.width;
                final int height = preferredPreviewSizeForVideo.height;
                final Iterator<Camera.Size> iterator = supportedPreviewSizes.iterator();
                while (iterator.hasNext()) {
                    final Camera.Size cameraSize = iterator.next();
                    if (cameraSize.width * cameraSize.height > width * height) {
                        iterator.remove();
                    }
                }
                final Camera.Size optimalPreviewSize = Util.getOptimalPreviewSize(this.mActivity, supportedPreviewSizes, this.mProfile.videoFrameWidth / this.mProfile.videoFrameHeight);
                this.mDesiredPreviewWidth = optimalPreviewSize.width;
                this.mDesiredPreviewHeight = optimalPreviewSize.height;
            }
        } else {
            this.mDesiredPreviewWidth = this.mProfile.videoFrameWidth;
            this.mDesiredPreviewHeight = this.mProfile.videoFrameHeight;
        }
        Log.v("CAM_VideoModule", "mDesiredPreviewWidth=" + this.mDesiredPreviewWidth + ". mDesiredPreviewHeight=" + this.mDesiredPreviewHeight);
    }

    @TargetApi(11)
    private static int getLowVideoQuality() {
        if (false) {//ApiHelper.HAS_FINE_RESOLUTION_QUALITY_LEVELS
            return 4;
        }
        return 0;
    }

    private int getPreferredCameraId(final ComboPreferences comboPreferences) {
        final int cameraFacingIntentExtras = Util.getCameraFacingIntentExtras(this.mActivity);
        if (cameraFacingIntentExtras != -1) {
            return cameraFacingIntentExtras;
        }
        return CameraSettings.readPreferredCameraId((SharedPreferences) comboPreferences);
    }

    private long getTimeLapseDuration(final long n) {
        return 1000L * n * this.mProfile.videoFrameRate / this.mTimeBetweenTimeLapseFrameCaptureMs;
    }

    private long getTimeLapseVideoLength(final long n) {
        return (long) (n / this.mTimeBetweenTimeLapseFrameCaptureMs / this.mProfile.videoFrameRate * 1000.0);
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
        this.mFocusManager = new FocusOverlayManager(this.mPreferences, this.mActivity.getResources().getStringArray(R.array.pref_video_focusmode_default_array), this.mParameters, (FocusOverlayManager.Listener) this, b, this.mActivity.getMainLooper(), (FocusOverlayManager.FocusUI) this.mUI);
    }

    private void initializeRecorder() {
        Log.v("CAM_VideoModule", "initializeRecorder");
        if (this.mActivity.mCameraDevice != null) {
            if (!false) {//ApiHelper.HAS_SURFACE_TEXTURE_RECORDING && ApiHelper.HAS_SURFACE_TEXTURE
                this.mUI.showSurfaceView();
                if (!this.mUI.isSurfaceViewReady()) {
                    return;
                }
            }
            this.closeVideoFileDescriptor();
            this.mMediaRecorder = new MediaRecorder();
            this.setupMediaRecorderPreviewDisplay();
            this.mActivity.mCameraDevice.stopPreview();
            this.mActivity.mCameraDevice.unlock();
            this.mActivity.mCameraDevice.waitDone();
            this.mMediaRecorder.setCamera(this.mActivity.mCameraDevice.getCamera());
            if (!this.mCaptureTimeLapse) {
                this.mMediaRecorder.setAudioSource(5);
            }
            this.mMediaRecorder.setVideoSource(1);
            this.mMediaRecorder.setProfile(this.mProfile);
            int maxDuration;
            if ((maxDuration = (int) (this.mMaxVideoDurationInMs - this.mRecordedTime)) < 500) {
                maxDuration = 500;
            }
            this.mMediaRecorder.setMaxDuration(maxDuration);
            if (this.mCaptureTimeLapse) {
                setCaptureRate(this.mMediaRecorder, 1000.0 / this.mTimeBetweenTimeLapseFrameCaptureMs);
            }
            this.setRecordLocation();

                if (this.mVideoFileDescriptor == null) {
                    return;
                }
                this.mMediaRecorder.setOutputFile(this.mVideoFileDescriptor.getFileDescriptor());
                long maxFileSize;
                final long n = maxFileSize = this.mActivity.getStorageSpace() - 50000000L;
                if (0L > 0L) {
                    maxFileSize = n;
                    if (0L < n) {
                        maxFileSize = 0L;
                    }
                }
                while (true) {
                    try {
                        this.mMediaRecorder.setMaxFileSize(maxFileSize);
                        int orientationHint = 0;

                            if (this.mOrientation != -1) {
                                final Camera.CameraInfo cameraCameraInfo = CameraHolder.instance().getCameraInfo()[this.mCameraId];
                                if (cameraCameraInfo.facing != 1) {
                                    break;
                                }
                                else{
                                orientationHint = (cameraCameraInfo.orientation - this.mOrientation + 360) % 360;


                                this.mMediaRecorder.setOrientationHint(orientationHint);
                                try {
                                    this.mMediaRecorder.prepare();
                                    this.mMediaRecorder.setOnErrorListener((MediaRecorder.OnErrorListener) this);
                                    this.mMediaRecorder.setOnInfoListener((MediaRecorder.OnInfoListener) this);

                                    orientationHint = (cameraCameraInfo.orientation + this.mOrientation) % 360;

                                    this.generateVideoFilename(this.mProfile.fileFormat);
                                    this.mMediaRecorder.setOutputFile(this.mVideoFilename);
                                } catch (IOException ex) {
                                    Log.e("CAM_VideoModule", "prepare failed for " + this.mVideoFilename, (Throwable) ex);
                                    this.releaseMediaRecorder();
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    } catch (RuntimeException ex2) {
                        continue;
                    }
                    break;
                }
            }

    }

    private void initializeSurfaceView() {
        if (!true) {//ApiHelper.HAS_SURFACE_TEXTURE_RECORDING
            this.mFrameDrawnListener = new CameraScreenNail.OnFrameDrawnListener() {
                @Override
                public void onFrameDrawn(final CameraScreenNail cameraScreenNail) {
                    VideoModule.this.mHandler.sendEmptyMessage(10);
                }
            };
            this.mUI.getSurfaceHolder().addCallback((SurfaceHolder.Callback) this.mUI);
        }
    }

    private void initializeVideoControl() {
        this.loadCameraPreferences();
        this.mUI.initializePopup(this.mPreferenceGroup);
    }

    private static boolean isSupported(final String s, final List<String> list) {
        return list != null && list.indexOf(s) >= 0;
    }

    private void keepScreenOn() {
        this.mHandler.removeMessages(4);
        this.mActivity.getWindow().addFlags(128);
    }

    private void keepScreenOnAwhile() {
        this.mHandler.removeMessages(4);
        this.mActivity.getWindow().addFlags(128);
        this.mHandler.sendEmptyMessageDelayed(4, 120000L);
    }

    private void loadCameraPreferences() {
        this.mPreferenceGroup = this.filterPreferenceScreenByIntent(new CameraSettings(this.mActivity, this.mParameters, this.mCameraId, CameraHolder.instance().getCameraInfo()).getPreferenceGroup(2131034113));
    }

    private void mergeVideoClips() {
        if (!this.isMerging) {
            this.isMerging = true;
            final List<RecordingProgressBar.Clip> clips = this.mUI.getClips();
            final String[] array = new String[clips.size()];
            int n = 0;
            final Iterator<RecordingProgressBar.Clip> iterator = clips.iterator();
            while (iterator.hasNext()) {
                array[n] = iterator.next().getPath();
                ++n;
            }
            this.mUI.showProgress(true);
//            VideoSDK.setVideoSDKListener(this.mVideoSDKListener);
            this.resultFilePath = Storage.DIRECTORY + '/' + (FileUtils.createVideoName((Context) this.mActivity, System.currentTimeMillis()) + ".mp4");
            if (array.length == 1) {
                FileUtils.copyFile(new File(array[0]), this.resultFilePath);
                this.mActivity.startFilterActivity(this.resultFilePath);
                return;
            }
//            if (VideoSDK.combine(array, this.resultFilePath) != 0) {
//                MapiaToast.show(this.mActivity, this.mActivity.getString(2131558434), 1);
//                this.deleteAllTempFiles();
//                this.mActivity.finish();
//            }
        }
    }

    private static String millisecondToTimeString(long n, final boolean b) {
        final long n2 = n / 1000L;
        final long n3 = n2 / 60L;
        final long n4 = n3 / 60L;
        final long n5 = n2 - 60L * n3;
        final StringBuilder sb = new StringBuilder();
        while (true) {
            if (n4 <= 0L || n4 < 10L) {
                if (n5 < 10L) {
                    sb.append('0');
                }
                sb.append(n5);
                if (b) {
                    sb.append('.');
                    n = (n - 1000L * n2) / 10L;
                    if (n < 10L) {
                        sb.append('0');
                    }
                    sb.append(n);
                }
                return sb.toString();
            }
            continue;
        }
    }

    private void onCameraOpened() {
        final View rootView = this.mUI.getRootView();
        this.mFocusManager.setPreviewSize(rootView.getWidth(), rootView.getHeight());
    }

    private void onPreviewStarted() {
        this.mHandler.sendEmptyMessage(6);
    }

    private void onStopVideoRecording() {
        this.stopVideoRecording();
    }

    private void openCamera() {
        try {
            synchronized (this.mCameraOpened) {
                if (!this.mCameraOpened) {
                    this.mActivity.mCameraDevice = Util.openCamera(this.mActivity, this.mCameraId);
                    this.mCameraOpened = true;
                }
                // monitorexit(this.mCameraOpened)
                this.mParameters = this.mActivity.mCameraDevice.getParameters();
                this.mFocusAreaSupported = Util.isFocusAreaSupported(this.mParameters);
                this.mMeteringAreaSupported = Util.isMeteringAreaSupported(this.mParameters);
                if (this.mFocusManager == null) {
                    this.initializeFocusManager();
                }
                this.mHandler.sendEmptyMessage(13);
            }
        } catch (CameraHardwareException ex) {
            this.mActivity.mOpenCameraFail = true;
        } catch (CameraDisabledException ex2) {
            this.mActivity.mCameraDisabled = true;
        }
    }

    private void pauseAudioPlayback() {
        final Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        this.mActivity.sendBroadcast(intent);
    }

    private void readVideoPreferences() {
        boolean mCaptureTimeLapse = false;
        int intValue = Integer.valueOf(this.mPreferences.getString("pref_video_quality_key", CameraSettings.getDefaultVideoQuality(this.mCameraId, this.mActivity.getResources().getString(R.string.pref_video_quality_default))));
        final Intent intent = this.mActivity.getIntent();
        if (intent.hasExtra("android.intent.extra.videoQuality")) {
            if (intent.getIntExtra("android.intent.extra.videoQuality", 0) > 0) {
                intValue = 1;
            } else {
                intValue = 0;
            }
        }
        if (intent.hasExtra("android.intent.extra.durationLimit")) {
            this.mMaxVideoDurationInMs = intent.getIntExtra("android.intent.extra.durationLimit", 0) * 1000;
        } else {
            this.mMaxVideoDurationInMs = CameraSettings.getMaxVideoDuration((Context) this.mActivity);
        }
        this.mThrottleDuration = CameraSettings.getTrottleVideoDuration((Context) this.mActivity);
        if (false) {//ApiHelper.HAS_TIME_LAPSE_RECORDING
            if (this.mTimeBetweenTimeLapseFrameCaptureMs != 0) {
                mCaptureTimeLapse = true;
            }
            this.mCaptureTimeLapse = mCaptureTimeLapse;
        }
        int n = intValue;
        if (this.mCaptureTimeLapse) {
            n = intValue + 1000;
        }
        this.mProfile = CamcorderProfile.get(this.mCameraId, n);
        this.getDesiredPreviewSize();
    }

    private void releaseMediaRecorder() {
        Log.v("CAM_VideoModule", "Releasing media recorder.");
        if (this.mMediaRecorder != null) {
            this.cleanupEmptyFile();
            this.mMediaRecorder.reset();
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
        }
        this.mVideoFilename = null;
    }

    private void releasePreviewResources() {
        if (false) {//ApiHelper.HAS_SURFACE_TEXTURE
            ((CameraScreenNail) this.mActivity.mCameraScreenNail).releaseSurfaceTexture();
            if (!false) {//ApiHelper.HAS_SURFACE_TEXTURE_RECORDING
                this.mHandler.removeMessages(10);
                this.mUI.hideSurfaceView();
            }
        }
    }

    private void resetScreenOn() {
        this.mHandler.removeMessages(4);
        this.mActivity.getWindow().clearFlags(128);
    }

    private void resizeForPreviewAspectRatio() {
        this.mUI.setAspectRatio(this.mProfile.videoFrameWidth / this.mProfile.videoFrameHeight);
    }

    private void saveVideo() {
        if (this.mVideoFileDescriptor == null) {
            final long n = SystemClock.uptimeMillis() - this.mRecordingStartTime;
            long timeLapseVideoLength;
            if (n > 0L) {
                timeLapseVideoLength = n;
                if (this.mCaptureTimeLapse) {
                    timeLapseVideoLength = this.getTimeLapseVideoLength(n);
                }
            } else {
                Log.w("CAM_VideoModule", "Video duration <= 0 : " + n);
                timeLapseVideoLength = n;
            }
            this.mActivity.getMediaSaveService().addVideo(this.mCurrentVideoFilename, timeLapseVideoLength, this.mCurrentVideoValues, this.mOnVideoSavedListener, this.mContentResolver);
        }
        this.mCurrentVideoValues = null;
    }

    private void setCameraParameters() {
        this.mParameters.setPreviewSize(this.mDesiredPreviewWidth, this.mDesiredPreviewHeight);
        this.mParameters.setPreviewFrameRate(this.mProfile.videoFrameRate);
        String string;
        if (this.mActivity.mShowCameraAppView) {
            string = this.mPreferences.getString("pref_camera_video_flashmode_key", this.mActivity.getString(R.string.pref_camera_video_flashmode_default));
        } else {
            string = "off";
        }
        if (isSupported(string, this.mParameters.getSupportedFlashModes())) {
            this.mParameters.setFlashMode(string);
        } else if (this.mParameters.getFlashMode() == null) {
            this.mActivity.getString(R.string.pref_camera_flashmode_title);
        }
        final String string2 = this.mPreferences.getString("pref_camera_whitebalance_key", this.mActivity.getString(R.string.pref_camera_whitebalance_default));
        if (isSupported(string2, this.mParameters.getSupportedWhiteBalance())) {
            this.mParameters.setWhiteBalance(string2);
        } else if (this.mParameters.getWhiteBalance() == null) {
        }
        if (this.mParameters.isZoomSupported()) {
            this.mParameters.setZoom(this.mZoomValue);
        }
        if (isSupported("continuous-video", this.mParameters.getSupportedFocusModes())) {
            this.mParameters.setFocusMode(this.mFocusManager.getFocusMode());
        }
        this.mParameters.set("recording-hint", "true");
        if ("true".equals(this.mParameters.get("video-stabilization-supported"))) {
            this.mParameters.set("video-stabilization", "true");
        }
        final Camera.Size optimalVideoSnapshotPictureSize = Util.getOptimalVideoSnapshotPictureSize(this.mParameters.getSupportedPictureSizes(), this.mDesiredPreviewWidth / this.mDesiredPreviewHeight);
        if (!this.mParameters.getPictureSize().equals((Object) optimalVideoSnapshotPictureSize)) {
            this.mParameters.setPictureSize(optimalVideoSnapshotPictureSize.width, optimalVideoSnapshotPictureSize.height);
        }
        Log.v("CAM_VideoModule", "Video snapshot size is " + optimalVideoSnapshotPictureSize.width + "x" + optimalVideoSnapshotPictureSize.height);
        this.mParameters.setJpegQuality(CameraProfile.getJpegEncodingQualityParameter(this.mCameraId, 2));
        this.mActivity.mCameraDevice.setParameters(this.mParameters);
        this.mParameters = this.mActivity.mCameraDevice.getParameters();
        this.updateCameraScreenNailSize(this.mDesiredPreviewWidth, this.mDesiredPreviewHeight);
    }

    @TargetApi(11)
    private static void setCaptureRate(final MediaRecorder mediaRecorder, final double captureRate) {
        mediaRecorder.setCaptureRate(captureRate);
    }

    private void setDisplayOrientation() {
        this.mDisplayRotation = Util.getDisplayRotation(this.mActivity);
        this.mCameraDisplayOrientation = Util.getDisplayOrientation(this.mDisplayRotation, this.mCameraId);
        this.mActivity.getGLRoot().requestLayoutContentPane();
    }

    @TargetApi(14)
    private void setFocusAreasIfSupported() {
        if (this.mFocusAreaSupported) {
            this.mParameters.setFocusAreas(this.mFocusManager.getFocusAreas());
        }
    }

    private void setMediaDuration(final int mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

    private void setMediaPath(final String mediaPath) {
        this.mediaPath = mediaPath;
    }

    @TargetApi(14)
    private void setMeteringAreasIfSupported() {
        if (this.mMeteringAreaSupported) {
            this.mParameters.setMeteringAreas(this.mFocusManager.getMeteringAreas());
        }
    }

    @TargetApi(14)
    private void setRecordLocation() {
        if (Build.VERSION.SDK_INT >= 14) {
            final Location currentLocation = this.mLocationManager.getCurrentLocation();
            if (currentLocation != null) {
                this.mMediaRecorder.setLocation((float) currentLocation.getLatitude(), (float) currentLocation.getLongitude());
            }
        }
    }

    private void setVideoFrameHeight(final int videoFrameHeight) {
        this.videoFrameHeight = videoFrameHeight;
    }

    private void setVideoFrameWidth(final int videoFrameWidth) {
        this.videoFrameWidth = videoFrameWidth;
    }

    private void setupMediaRecorderPreviewDisplay() {
        if (false) {//!ApiHelper.HAS_SURFACE_TEXTURE
            this.mMediaRecorder.setPreviewDisplay(this.mUI.getSurfaceHolder().getSurface());
        } else if (true) {//!ApiHelper.HAS_SURFACE_TEXTURE_RECORDING
            this.stopPreview();
            this.mActivity.mCameraDevice.setPreviewDisplayAsync(this.mUI.getSurfaceHolder());
            this.mActivity.mCameraDevice.setDisplayOrientation(Util.getDisplayOrientation(this.mDisplayRotation, this.mCameraId));
            this.mActivity.mCameraDevice.startPreviewAsync();
            this.mPreviewing = true;
            this.mMediaRecorder.setPreviewDisplay(this.mUI.getSurfaceHolder().getSurface());
        }
    }

    private void startPreview() {
        Log.v("CAM_VideoModule", "startPreview");
        this.mActivity.mCameraDevice.setErrorCallback((Camera.ErrorCallback) this.mErrorCallback);
        if (this.mPreviewing) {
            this.stopPreview();
        }
        this.setDisplayOrientation();
        if (this.mActivity.mCameraDevice == null) {
            return;
        }
        this.mActivity.mCameraDevice.setDisplayOrientation(this.mCameraDisplayOrientation);
        this.setCameraParameters();
        try {
            if (false) {//ApiHelper.HAS_SURFACE_TEXTURE
                final SurfaceTexture surfaceTexture = ((CameraScreenNail) this.mActivity.mCameraScreenNail).getSurfaceTexture();
                if (surfaceTexture == null) {
                    return;
                }
                this.mActivity.mCameraDevice.setPreviewTextureAsync(surfaceTexture);
            } else {
                this.mActivity.mCameraDevice.setPreviewDisplayAsync(this.mUI.getSurfaceHolder());
            }
            this.mActivity.mCameraDevice.startPreviewAsync();
            this.mPreviewing = true;
            this.onPreviewStarted();
        } catch (Throwable t) {
            this.closeCamera();
            throw new RuntimeException("startPreview failed", t);
        } finally {
            this.mActivity.runOnUiThread((Runnable) new Runnable() {
                @Override
                public void run() {
                    if (VideoModule.this.mActivity.mOpenCameraFail) {
                        Util.showErrorAndFinish(VideoModule.this.mActivity, 2131558443);
                    } else if (VideoModule.this.mActivity.mCameraDisabled) {
                        Util.showErrorAndFinish(VideoModule.this.mActivity, 2131558430);
                    }
                }
            });
        }
    }

    private void startVideoRecording() {
        boolean b = false;
        Log.v("CAM_VideoModule", "startVideoRecording");
        if (this.ifFirstRecording) {
            this.ifFirstRecording = false;
        }
        this.fail = false;
        this.mActivity.updateStorageSpaceAndHint();
        if (this.mActivity.getStorageSpace() <= 50000000L) {
            Log.v("CAM_VideoModule", "Storage issue, ignore the start request");
        } else if (this.mActivity.mCameraDevice.waitDone()) {
            this.mCurrentVideoUri = null;
            this.initializeRecorder();
            this.setMediaPath(this.mVideoFilename);
            this.setMediaDuration(0);
            this.setVideoFrameWidth(this.mProfile.videoFrameWidth);
            this.setVideoFrameHeight(this.mProfile.videoFrameHeight);
            if (this.mMediaRecorder == null) {
                Log.e("CAM_VideoModule", "Fail to initialize media recorder");
                return;
            }
            this.pauseAudioPlayback();
            try {
                this.mMediaRecorder.start();
//                AccessibilityUtils.makeAnnouncement((View) this.mActivity.getShutterButton(), this.mActivity.getString(2131559140));
                this.mActivity.mCameraDevice.refreshParameters();
//                if (ApiHelper.HAS_ZOOM_WHEN_RECORDING) {
//                    this.mParameters = this.mActivity.mCameraDevice.getParameters();
//                }
                this.mUI.enableCameraControls(false);
                this.mMediaRecorderRecording = true;
                this.mRecordingStartTime = SystemClock.uptimeMillis();
                final VideoUI mui = this.mUI;
                final RecordingProgressBar.Status recording = RecordingProgressBar.Status.RECORDING;
                if (this.mCameraId == 0) {
                    b = true;
                }
                mui.setProgressBarStatus(recording, b);
                this.mUI.showRecordingUI(true, this.fail);
                this.keepScreenOn();
                UsageStatistics.onEvent("Camera", "CaptureStart", "Video");
            } catch (RuntimeException ex) {
                Log.e("CAM_VideoModule", "Could not start media recorder. ", (Throwable) ex);
                this.releaseMediaRecorder();
                this.mActivity.mCameraDevice.lock();
            }
        }
    }

    private boolean stopVideoRecording() {
        Log.v("CAM_VideoModule", "stopVideoRecording");
        this.fail = false;

        if (!this.mMediaRecorderRecording) {
            return false;
        }
        try {
            this.mMediaRecorder.setOnErrorListener((MediaRecorder.OnErrorListener) null);
            this.mMediaRecorder.setOnInfoListener((MediaRecorder.OnInfoListener) null);
            this.mMediaRecorder.stop();
            long n = 0L;
            if (this.mediaPath != null) {
//                                final VideoSDKMediaRetriever videoSDKMediaRetriever = new VideoSDKMediaRetriever(this.mediaPath);
//                                n = videoSDKMediaRetriever.getDuration();
//                                videoSDKMediaRetriever.release();
            }
            this.setMediaDuration((int) n);
            long timeLapseDuration = n;
            if (this.mCaptureTimeLapse) {
                timeLapseDuration = this.getTimeLapseDuration(n);
            }
            this.mRecordedTime += timeLapseDuration;
            this.mCurrentVideoFilename = this.mVideoFilename;
            Log.v("CAM_VideoModule", "stopVideoRecording: Setting current video filename: " + this.mCurrentVideoFilename);
//                            AccessibilityUtils.makeAnnouncement((View)this.mActivity.getShutterButton(), this.mActivity.getString(2131559141));
            this.mMediaRecorderRecording = false;
            if (this.mPaused) {
                this.closeCamera();
            }
            this.mUI.showRecordingUI(false, this.fail);
            if (!this.mIsVideoCaptureIntent) {
                this.mUI.enableCameraControls(true);
            }
            this.mUI.setOrientationIndicator(0, true);
            this.keepScreenOnAwhile();
            this.releaseMediaRecorder();
            if (!this.mPaused) {
                this.mActivity.mCameraDevice.lock();
                this.mActivity.mCameraDevice.waitDone();
                if (false) {//ApiHelper.HAS_SURFACE_TEXTURE && !ApiHelper.HAS_SURFACE_TEXTURE_RECORDING
                    ((CameraScreenNail) this.mActivity.mCameraScreenNail).setOneTimeOnFrameDrawnListener(this.mFrameDrawnListener);
                }
            }
            if (!this.mPaused) {
                this.mParameters = this.mActivity.mCameraDevice.getParameters();
            }
            if (this.fail) {
                final String s = "CaptureFail";
                UsageStatistics.onEvent("Camera", s, "Video", SystemClock.uptimeMillis() - this.mRecordingStartTime);
                return this.fail;
            }
        } catch (RuntimeException ex) {
            Log.e("CAM_VideoModule", "stop fail", (Throwable) ex);
            if (this.mVideoFilename != null) {
                this.deleteVideoFile(this.mVideoFilename);
            }
            this.fail = true;
        }

        final String s = "CaptureDone";
        return true;

    }

    private void storeImage(final byte[] array, final Location location) {
        final long currentTimeMillis = System.currentTimeMillis();
        final String jpegName = Util.createJpegName(currentTimeMillis);
//        final ExifInterface exif = Exif.getExif(array);
//        final int orientation = Exif.getOrientation(exif);
        final Camera.Size pictureSize = this.mParameters.getPictureSize();
//        this.mActivity.getMediaSaveService().addImage(array, jpegName, currentTimeMillis, location, pictureSize.width, pictureSize.height, orientation, exif, this.mOnPhotoSavedListener, this.mContentResolver);
    }

    private void switchCamera() {
        if (this.mPaused) {
            return;
        }
        this.mCameraId = this.mPendingSwitchCameraId;
        this.mPendingSwitchCameraId = -1;
        this.setCameraId(this.mCameraId);
        this.closeCamera();
        this.mPreferences.setLocalId((Context) this.mActivity, this.mCameraId);
        CameraSettings.upgradeLocalPreferences(this.mPreferences.getLocal());
        this.openCamera();
        this.readVideoPreferences();
        this.startPreview();
        this.resizeForPreviewAspectRatio();
        this.initializeVideoControl();
        if (this.mFocusManager != null) {
            this.mFocusManager.removeMessages();
        }
        this.mUI.initializeZoom(this.mParameters);
        this.mUI.setOrientationIndicator(0, false);
        if (false) {//ApiHelper.HAS_SURFACE_TEXTURE
            this.mHandler.sendEmptyMessage(9);
        }
        this.mUI.updateOnScreenIndicators(this.mParameters, this.mPreferences);
    }

    private void updateCameraScreenNailSize(final int n, final int n2) {
        if (false) {//ApiHelper.HAS_SURFACE_TEXTURE
            int n3 = n;
            int n4 = n2;
            if (this.mCameraDisplayOrientation % 180 != 0) {
                n4 = n;
                n3 = n2;
            }
            final CameraScreenNail cameraScreenNail = (CameraScreenNail) this.mActivity.mCameraScreenNail;
            cameraScreenNail.getWidth();
            cameraScreenNail.getHeight();
            cameraScreenNail.setSize(n3, n4);
            cameraScreenNail.enableAspectRatioClamping();
            if (cameraScreenNail.getSurfaceTexture() == null) {
                cameraScreenNail.acquireSurfaceTexture();
            }
        }
    }

    private void updateProgressBar(final boolean b) {
        if (!this.mMediaRecorderRecording) {
            this.mUI.setProgressBarStatus(RecordingProgressBar.Status.RECORDED, true);
            if (!b) {
                return;
            }
        }
        long n2;
        final long n = n2 = SystemClock.uptimeMillis() - this.mRecordingStartTime + this.mRecordedTime;
        if (this.mCaptureTimeLapse) {
            n2 = this.getTimeLapseVideoLength(n);
        }
        if (b) {
            if (this.mCaptureTimeLapse) {
                n2 = this.getTimeLapseVideoLength(this.mRecordedTime);
            } else {
                n2 = this.mRecordedTime;
            }
        }
        this.mUI.updateProgressBar(n2);
        this.mHandler.sendEmptyMessageDelayed(12, 50L - n2 % 50L);
    }

    private void updateRecordingTime(final boolean b) {
        if (!this.mMediaRecorderRecording && !b) {
            return;
        }
        long mRecordedTime = SystemClock.uptimeMillis() - this.mRecordingStartTime + this.mRecordedTime;
        if (b) {
            mRecordedTime = this.mRecordedTime;
        }
        String recordingTime;
        if (this.mCaptureTimeLapse) {
            recordingTime = millisecondToTimeString(this.getTimeLapseVideoLength(mRecordedTime), true);
        } else {
            recordingTime = millisecondToTimeString(mRecordedTime, false);
        }
        this.mUI.setRecordingTime(recordingTime);
        this.mHandler.sendEmptyMessageDelayed(5, 1000L - mRecordedTime % 1000L);
    }

    public void autoFocus() {
        this.mFocusStartTime = System.currentTimeMillis();
        this.mActivity.mCameraDevice.autoFocus((Camera.AutoFocusCallback) this.mAutoFocusCallback);
    }

    public void cancelAutoFocus() {
        if (this.mActivity.mCameraDevice != null) {
            this.mActivity.mCameraDevice.cancelAutoFocus();
        }
    }

    public boolean capture() {
        return false;
    }

    public void deleteAllTempFiles() {
        final List<RecordingProgressBar.Clip> clips = this.mUI.getClips();
        if (!clips.isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Iterator<RecordingProgressBar.Clip> iterator = clips.iterator();
                    while (iterator.hasNext()) {
                        VideoModule.this.deleteVideoFile(iterator.next().getPath());
                    }
                }
            }).start();
        }
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        return this.mSwitchingCamera || this.mUI.dispatchTouchEvent(motionEvent);
    }

    @Override
    public void easterEgg() {
        this.mTimeBetweenTimeLapseFrameCaptureMs = 1000;
        this.onSharedPreferenceChanged();
    }

    @Override
    public ComboPreferences getComboPreferences() {
        return this.mPreferences;
    }

    public int getMediaDuration() {
        return this.mediaDuration;
    }

    public String getMediaPath() {
        return this.mediaPath;
    }

    public int getPreviewHeight() {
        return this.videoFrameHeight;
    }

    public int getPreviewWidth() {
        return this.videoFrameWidth;
    }

    public boolean ifFirstRecording() {
        return this.ifFirstRecording;
    }

    @Override
    public void init(final CameraActivity mActivity, final View view) {
//        if (!VideoSDK.isInitialized()) {
//            VideoSDK.init((Context) mActivity, false);
//        }
        this.mActivity = mActivity;
        this.mUI = new VideoUI(mActivity, this, view);
        this.mPreferences = new ComboPreferences((Context) this.mActivity);
        CameraSettings.upgradeGlobalPreferences(this.mPreferences.getGlobal());
        this.mCameraId = this.getPreferredCameraId(this.mPreferences);
        this.mPreferences.setLocalId((Context) this.mActivity, this.mCameraId);
        CameraSettings.upgradeLocalPreferences(this.mPreferences.getLocal());
        this.mActivity.mNumberOfCameras = CameraHolder.instance().getNumberOfCameras();
        final CameraOpenThread cameraOpenThread = new CameraOpenThread();
        cameraOpenThread.start();
        this.mContentResolver = this.mActivity.getContentResolver();
        this.mIsVideoCaptureIntent = this.isVideoCaptureIntent();
        this.mActivity.reuseCameraScreenNail(!this.mIsVideoCaptureIntent);
        this.initializeSurfaceView();
        try {
            cameraOpenThread.join();
            if (this.mActivity.mOpenCameraFail) {
                Util.showErrorAndFinish(this.mActivity, 2131558443);
                return;
            }
            if (this.mActivity.mCameraDisabled) {
                Util.showErrorAndFinish(this.mActivity, 2131558430);
                return;
            }
        } catch (InterruptedException ex) {
        }
        this.readVideoPreferences();
        this.mUI.setPrefChangedListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                VideoModule.this.startPreview();
            }
        }).start();
        this.mQuickCapture = this.mActivity.getIntent().getBooleanExtra("android.intent.extra.quickCapture", false);
        this.mLocationManager = new LocationManager((Context) this.mActivity, null);
        this.mUI.setOrientationIndicator(0, false);
        this.setDisplayOrientation();
        this.mUI.showTimeLapseUI(this.mCaptureTimeLapse);
        this.resizeForPreviewAspectRatio();
        this.initializeVideoControl();
        this.mPendingSwitchCameraId = -1;
        this.mUI.updateOnScreenIndicators(this.mParameters, this.mPreferences);
        this.mUI.setProgressBarListener(this);
        this.mUI.addProgressBarMediaListener(this);
        this.mUI.setOnClickTrashButtonListener(this.onClickTrashButtonListener);
        this.mUI.setOnClickNextButtonListener(this.onClickNextButtonListener);
        this.isMerging = false;
        this.isDone = false;
        if (false) {//!ApiHelper.HAS_SURFACE_TEXTURE_RECORDING && ApiHelper.HAS_SURFACE_TEXTURE
            this.mUI.showSurfaceView();
        }
    }

    @Override
    public void installIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter("android.intent.action.MEDIA_EJECT");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_STARTED");
        intentFilter.addDataScheme("file");
        this.mReceiver = new MyBroadcastReceiver();
        this.mActivity.registerReceiver(this.mReceiver, intentFilter);
    }

    @Override
    public boolean isDone() {
        return this.isDone;
    }

    @Override
    public boolean isInReviewMode() {
        return this.mIsInReviewMode;
    }

    public boolean isRecording() {
        return this.mMediaRecorderRecording;
    }

    public boolean isTrashReadyMode() {
        return this.mUI.getTrashButtonStatus() == 2;
    }

    @Override
    public boolean isVideoCaptureIntent() {
        return "android.media.action.VIDEO_CAPTURE".equals(this.mActivity.getIntent().getAction());
    }

    @Override
    public boolean needsPieMenu() {
        return true;
    }

    @Override
    public boolean needsSwitcher() {
        return !this.mIsVideoCaptureIntent;
    }

    @Override
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        this.deleteAllTempFiles();
    }

    @Override
    public boolean onBackPressed() {
        if (!this.mPaused) {
            if (this.mMediaRecorderRecording) {
                this.onStopVideoRecording();
                return true;
            }
            if (!this.mUI.hidePieRenderer()) {
                if (!this.mUI.getClips().isEmpty()) {
                    this.mUI.showDialog(new VideoDeleteDialog.OnClickConfirmListener() {
                        @Override
                        public void onClickConfirm() {
                            VideoModule.this.deleteAllTempFiles();
                            VideoModule.this.mActivity.finish();
                        }
                    });
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCameraPickerClicked(final int mPendingSwitchCameraId) {
        if (this.mPaused || this.mPendingSwitchCameraId != -1) {
            return;
        }
        this.mPendingSwitchCameraId = mPendingSwitchCameraId;
        if (true) {//ApiHelper.HAS_SURFACE_TEXTURE
            ((CameraScreenNail) this.mActivity.mCameraScreenNail).copyTexture();
            this.mSwitchingCamera = true;
            return;
        }
        this.switchCamera();
    }

    @Override
    public void onCaptureTextureCopied() {
    }

    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        Log.v("CAM_VideoModule", "onConfigurationChanged");
        this.setDisplayOrientation();
    }

    public void onDelete(final int n) {
        this.mRecordedTime -= n;
        if (this.mRecordedTime < this.mThrottleDuration) {
            this.mUI.setNextButtonStatus(3);
        }
        final List<RecordingProgressBar.Clip> clips = this.mUI.getClips();
        if (clips != null && clips.size() == 0) {
            this.ifFirstRecording = true;
            this.mRecordedTime = 0L;
        }
        this.updateRecordingTime(true);
        this.updateProgressBar(true);
        if (clips != null && clips.size() == 0) {
            this.mUI.resetUi();
        }
    }

    public void onError(final MediaRecorder mediaRecorder, final int n, final int n2) {
        Log.e("CAM_VideoModule", "MediaRecorder error. what=" + n + ". extra=" + n2);
        if (n == 1) {
            this.stopVideoRecording();
            this.mActivity.updateStorageSpaceAndHint();
        }
    }

    @Override
    public void onFinish() {
        if (!this.mUI.getClips().isEmpty()) {
            this.mUI.showDialog(new VideoDeleteDialog.OnClickConfirmListener() {
                @Override
                public void onClickConfirm() {
                    VideoModule.this.deleteAllTempFiles();
                    VideoModule.this.mActivity.finish();
                }
            });
            return;
        }
        this.mActivity.finish();
    }

    @Override
    public void onFullScreenChanged(final boolean fullScreen) {
        this.mUI.onFullScreenChanged(fullScreen);
        if (true && this.mActivity.mCameraScreenNail != null) { //ApiHelper.HAS_SURFACE_TEXTURE
            ((CameraScreenNail) this.mActivity.mCameraScreenNail).setFullScreen(fullScreen);
        }
    }

    public void onInfo(final MediaRecorder mediaRecorder, final int n, final int n2) {
        if (n == 800) {
            if (this.mMediaRecorderRecording) {
                this.onStopVideoRecording();
                this.mMediaRecorderEnd = true;
                this.mUI.showRecordCompleteUI();
            }
        } else if (n == 801) {
            if (this.mMediaRecorderRecording) {
                this.onStopVideoRecording();
                this.mMediaRecorderEnd = true;
                this.mUI.showRecordCompleteUI();
            }
            Toast.makeText((Context) this.mActivity, R.string.video_reach_size_limit, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (this.mPaused) {
            return true;
        }
        switch (n) {
            case 27: {
                if (keyEvent.getRepeatCount() == 0) {
                    this.mUI.clickShutter();
                    return true;
                }
                break;
            }
            case 23: {
                if (keyEvent.getRepeatCount() == 0) {
                    this.mUI.clickShutter();
                    return true;
                }
                break;
            }
            case 82: {
                if (this.mMediaRecorderRecording) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        switch (n) {
            default: {
                return false;
            }
            case 27: {
                this.mUI.pressShutter(false);
                return true;
            }
        }
    }

    @Override
    public void onMediaSaveServiceConnected(final MediaSaveService mediaSaveService) {
    }

    @Override
    public void onOrientationChanged(final int mOrientation) {
        if (this.ifFirstRecording && mOrientation != -1 && this.mOrientation != mOrientation) {
            this.mOrientation = mOrientation;
        }
    }

    @Override
    public void onOverriddenPreferencesClicked() {
    }

    @Override
    public void onPauseAfterSuper() {
    }

    @Override
    public void onPauseBeforeSuper() {
        this.mPaused = true;
        if (this.mMediaRecorderRecording) {
            this.onStopVideoRecording();
        } else {
            this.closeCamera();
            this.releaseMediaRecorder();
        }
        this.closeVideoFileDescriptor();
        this.releasePreviewResources();
        if (this.mReceiver != null) {
            this.mActivity.unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
        this.resetScreenOn();
        if (this.mLocationManager != null) {
            this.mLocationManager.recordLocation(false);
        }
        this.mHandler.removeMessages(3);
        this.mHandler.removeMessages(8);
        this.mHandler.removeMessages(9);
        this.mPendingSwitchCameraId = -1;
        this.mSwitchingCamera = false;
    }

    @Override
    public void onPreviewTextureCopied() {
        this.mHandler.sendEmptyMessage(8);
    }

    @Override
    public void onRestorePreferencesClicked() {
    }

    @Override
    public void onResumeAfterSuper() {
        if (this.mActivity.mOpenCameraFail || this.mActivity.mCameraDisabled) {
            return;
        }
        this.mUI.enableShutter(false);
        this.mZoomValue = 0;
        this.showVideoSnapshotUI(false);
        if (!this.mPreviewing) {
            this.openCamera();
            if (this.mActivity.mOpenCameraFail) {
                Util.showErrorAndFinish(this.mActivity, 2131558443);
                return;
            }
            if (this.mActivity.mCameraDisabled) {
                Util.showErrorAndFinish(this.mActivity, 2131558430);
                return;
            }
            this.readVideoPreferences();
            this.resizeForPreviewAspectRatio();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    VideoModule.this.startPreview();
                }
            }).start();
        } else {
            this.mUI.enableShutter(true);
        }
        this.mUI.initializeZoom(this.mParameters);
        this.keepScreenOnAwhile();
        this.mLocationManager.recordLocation(RecordLocationPreference.get((SharedPreferences) this.mPreferences, this.mContentResolver));
        if (this.mPreviewing) {
            this.mOnResumeTime = SystemClock.uptimeMillis();
            this.mHandler.sendEmptyMessageDelayed(3, 100L);
        }
        UsageStatistics.onContentViewChanged("Camera", "VideoModule");
    }

    @Override
    public void onResumeBeforeSuper() {
        this.mPaused = false;
        this.isDone = false;
        this.isMerging = false;
        this.mUI.showProgress(false);
    }

    @Override
    public void onSharedPreferenceChanged() {
        if (this.mPaused) {
            return;
        }
        synchronized (this.mPreferences) {
            if (this.mActivity.mCameraDevice == null) {
                return;
            }
        }
        this.mLocationManager.recordLocation(RecordLocationPreference.get((SharedPreferences) this.mPreferences, this.mContentResolver));
        this.readVideoPreferences();
        this.mUI.showTimeLapseUI(this.mCaptureTimeLapse);
        final Camera.Size previewSize = this.mParameters.getPreviewSize();
        if (previewSize.width != this.mDesiredPreviewWidth || previewSize.height != this.mDesiredPreviewHeight) {
            this.stopPreview();
            this.resizeForPreviewAspectRatio();
            this.startPreview();
        } else {
            this.setCameraParameters();
        }
        this.mUI.updateOnScreenIndicators(this.mParameters, this.mPreferences);
        // monitorexit(comboPreferences)
    }
    // monitorexit(comboPreferences)

    @Override
    public void onShowSwitcherPopup() {
        this.mUI.onShowSwitcherPopup();
    }

    @Override
    public void onShutterButtonClick() {
        if (!this.mSwitchingCamera) {
            final boolean mMediaRecorderRecording = this.mMediaRecorderRecording;
            if (this.mMediaRecorderEnd) {
                this.isDone = true;
                this.mergeVideoClips();
                return;
            }
            if (mMediaRecorderRecording) {
                this.onStopVideoRecording();
            } else {
                this.startVideoRecording();
            }
            this.mUI.enableShutter(false);
            this.updateRecordingTime(true);
            this.updateProgressBar(true);
            if (this.fail) {
                final List<RecordingProgressBar.Clip> clips = this.mUI.getClips();
                if (clips != null && clips.size() > 0) {
                    clips.remove(clips.size() - 1);
                }
            }
            if (!this.mIsVideoCaptureIntent || !mMediaRecorderRecording) {
                this.mHandler.sendEmptyMessageDelayed(6, 500L);
            }
        }
    }

    @Override
    public void onShutterButtonFocus(final boolean shutterPressed) {
        this.mUI.setShutterPressed(shutterPressed);
    }

    @Override
    public void onSingleTapUp(final View view, final int n, final int n2) {
        if (!this.mPaused && this.mActivity.mCameraDevice != null && (this.mFocusAreaSupported || this.mMeteringAreaSupported)) {
            this.mFocusManager.onSingleTapUp(n, n2);
        }
    }

    @Override
    public void onStop() {
    }

    public void onThrottle() {
        this.mUI.setNextButtonStatus(4);
    }

    @Override
    public void onUserInteraction() {
        if (!this.mMediaRecorderRecording && !this.mActivity.isFinishing()) {
            this.keepScreenOnAwhile();
        }
    }

    @Override
    public int onZoomChanged(final int mZoomValue) {
        if (!this.mPaused) {
            this.mZoomValue = mZoomValue;
            if (this.mParameters != null && this.mActivity.mCameraDevice != null) {
                this.mParameters.setZoom(this.mZoomValue);
                this.mActivity.mCameraDevice.setParameters(this.mParameters);
                final Camera.Parameters parameters = this.mActivity.mCameraDevice.getParameters();
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

    public void setFocusParameters() {
        this.setFocusAreasIfSupported();
        this.setMeteringAreasIfSupported();
        this.mActivity.mCameraDevice.setParameters(this.mParameters);
    }

    void showVideoSnapshotUI(final boolean b) {
        if (this.mParameters != null && Util.isVideoSnapshotSupported(this.mParameters) && !this.mIsVideoCaptureIntent) {
            if (true && b) {//ApiHelper.HAS_SURFACE_TEXTURE
                ((CameraScreenNail) this.mActivity.mCameraScreenNail).animateCapture(this.mDisplayRotation);
            }
            this.mUI.enableShutter(!b);
        }
    }

    public void startFaceDetection() {
    }

    public void stopFaceDetection() {
    }

    @Override
    public void stopPreview() {
        this.mActivity.mCameraDevice.stopPreview();
        if (this.mFocusManager != null) {
            this.mFocusManager.onPreviewStopped();
        }
        this.mPreviewing = false;
    }

    @Override
    public void updateCameraAppView() {
        if (this.mPreviewing && this.mParameters.getFlashMode() != null) {
            if (!this.mActivity.mShowCameraAppView) {
                if (this.mParameters.getFlashMode().equals("off")) {
                    this.mRestoreFlash = false;
                    return;
                }
                this.mRestoreFlash = true;
                this.setCameraParameters();
            } else if (this.mRestoreFlash) {
                this.mRestoreFlash = false;
                this.setCameraParameters();
            }
        }
    }

    @Override
    public boolean updateStorageHintOnResume() {
        return true;
    }

    private final class AutoFocusCallback implements Camera.AutoFocusCallback {
        public void onAutoFocus(final boolean b, final Camera camera) {
            if (VideoModule.this.mPaused) {
                return;
            }
            VideoModule.this.mAutoFocusTime = System.currentTimeMillis() - VideoModule.this.mFocusStartTime;
            Log.v("CAM_VideoModule", "mAutoFocusTime = " + VideoModule.this.mAutoFocusTime + "ms");
            VideoModule.this.mFocusManager.onAutoFocus(b, false);
        }
    }

    protected class CameraOpenThread extends Thread {
        @Override
        public void run() {
            VideoModule.this.openCamera();
        }
    }

    private final class JpegPictureCallback implements Camera.PictureCallback {
        Location mLocation;

        public JpegPictureCallback(final Location mLocation) {
            super();
            this.mLocation = mLocation;
        }

        public void onPictureTaken(final byte[] array, final Camera camera) {
            Log.v("CAM_VideoModule", "onPictureTaken");
            VideoModule.this.showVideoSnapshotUI(false);
            VideoModule.this.storeImage(array, this.mLocation);
        }
    }

    private class MainHandler extends Handler {
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    Log.v("CAM_VideoModule", "Unhandled message: " + message.what);
                    break;
                }
                case 6: {
                    VideoModule.this.mUI.enableShutter(true);
                }
                case 4: {
                    VideoModule.this.mActivity.getWindow().clearFlags(128);
                }
                case 5: {
                    VideoModule.this.updateRecordingTime(false);
                }
                case 3: {
                    if (Util.getDisplayRotation(VideoModule.this.mActivity) != VideoModule.this.mDisplayRotation && !VideoModule.this.mMediaRecorderRecording && !VideoModule.this.mSwitchingCamera) {
                        VideoModule.this.startPreview();
                    }
                    if (SystemClock.uptimeMillis() - VideoModule.this.mOnResumeTime < 5000L) {
                        VideoModule.this.mHandler.sendEmptyMessageDelayed(3, 100L);
                        return;
                    }
                    break;
                }
                case 8: {
                    VideoModule.this.switchCamera();
                }
                case 9: {
                    ((CameraScreenNail) VideoModule.this.mActivity.mCameraScreenNail).animateSwitchCamera();
                    VideoModule.this.mSwitchingCamera = false;
                }
                case 10: {
                    VideoModule.this.mUI.hideSurfaceView();
                }
                case 12: {
                    VideoModule.this.updateProgressBar(false);
                }
                case 13: {
                    VideoModule.this.onCameraOpened();
                }
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (action.equals("android.intent.action.MEDIA_EJECT")) {
                VideoModule.this.stopVideoRecording();
            } else if (action.equals("android.intent.action.MEDIA_SCANNER_STARTED")) {
                Toast.makeText((Context) VideoModule.this.mActivity, (CharSequence) VideoModule.this.mActivity.getResources().getString(R.string.wait), Toast.LENGTH_SHORT).show();
            }
        }
    }
}