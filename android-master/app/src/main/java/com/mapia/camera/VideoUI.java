package com.mapia.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapia.R;
import com.mapia.camera.ui.PieRenderer;
import com.mapia.camera.ui.PreviewSurfaceView;
import com.mapia.camera.ui.RecordingProgressBar;
import com.mapia.camera.ui.RenderOverlay;
import com.mapia.camera.ui.ZoomRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by daehyun on 15. 6. 16..
 */


public class VideoUI implements SurfaceHolder.Callback, PreviewGestures.SingleTapListener, PreviewGestures.SwipeListener, FocusOverlayManager.FocusUI
{
    public static final int NEXT_DISABLE = 3;
    public static final int NEXT_ENABLE = 4;
    private static final String TAG = "CAM_VideoUI";
    public static final int TRASH_DISABLE = 0;
    public static final int TRASH_ENABLE = 1;
    public static final int TRASH_READY = 2;
    private View cameraModeLayout;
    private View cameraSwitchButton;
    private int currentNextStatus;
    private int currentSec;
    private int currentTrashStatus;
    private View galleryThumbView;
    private Handler handler;
    private boolean ifFirstRecording;
    private boolean isRecording;
    private CameraActivity mActivity;
    private VideoController mController;
    private PreviewGestures mGestures;
    private PieRenderer mPieRenderer;
    private PreviewFrameLayout mPreviewFrameLayout;
    private PreviewSurfaceView mPreviewSurfaceView;
    private View mProgress;
    private TextView mProgressText;
    private ImageView mRecordingTimeImage;
    private View mRecordingTimeLayout;
    private TextView mRecordingTimeView;
    private RenderOverlay mRenderOverlay;
    private View mRootView;
    private ShutterButton mShutterButton;
    private boolean mSurfaceViewReady;
    private View mTimeLapseLabel;
    private VideoMenu mVideoMenu;
    private int mZoomMax;
    private List<Integer> mZoomRatios;
    private ZoomRenderer mZoomRenderer;
    private RecordingProgressBar progressBar;
    private View ratioButton;
    private int totalTime;
    private ImageView videoNextImageView;
    private ImageView videoTrashView;

    public VideoUI(final CameraActivity mActivity, final VideoController mController, final View mRootView) {
        super();
        this.ifFirstRecording = true;
        this.isRecording = false;
        this.handler = new Handler((Handler.Callback)new Handler.Callback() {
            public boolean handleMessage(final Message message) {
                return true;
            }
        });
        this.totalTime = 0;
        this.currentSec = 0;
        this.mActivity = mActivity;
        this.mController = mController;
        this.mRootView = mRootView;
        this.mActivity.getLayoutInflater().inflate(R.layout.video_module, (ViewGroup)this.mRootView, true);
        this.mPreviewSurfaceView = (PreviewSurfaceView)this.mRootView.findViewById(R.id.preview_surface_view);
        this.videoNextImageView = this.mActivity.getVideoNextImageView();
        this.videoTrashView = (ImageView)this.mActivity.findViewById(R.id.camera_video_delete_view);
        this.ratioButton = this.mActivity.getRatioButton();
        this.cameraModeLayout = this.mActivity.getCameraModeLayout();
        this.initializeMiscControls();
        this.initializeOverlay();
        this.currentTrashStatus = -1;
        this.currentNextStatus = -1;
        this.setTrashButtonStatus(0);
        this.setNextButtonStatus(3);
    }

    private void initializeMiscControls() {
        (this.mPreviewFrameLayout = (PreviewFrameLayout)this.mRootView.findViewById(R.id.frame)).setOnLayoutChangeListener(this.mActivity);
        (this.mShutterButton = this.mActivity.getShutterButton()).setImageResource(R.drawable.camera_selector_video_shutter_button);
        this.mShutterButton.setOnShutterButtonListener((ShutterButton.OnShutterButtonListener)this.mController);
        this.mShutterButton.setVisibility(View.VISIBLE);
        this.mShutterButton.requestFocus();
        this.mShutterButton.enableTouch(true);
        this.mRecordingTimeLayout = this.mActivity.getRecordingTimeLayout();
        this.mRecordingTimeImage = (ImageView)this.mActivity.findViewById(R.id.recording_time_image);
        this.mRecordingTimeView = (TextView)this.mActivity.findViewById(R.id.recording_time);
        this.mTimeLapseLabel = this.mRootView.findViewById(R.id.time_lapse_label);
        this.progressBar = this.mActivity.getRecordingProgressBar();
        this.mProgress = this.mRootView.findViewById(R.id.progress_holder);
        this.mProgressText = (TextView)this.mProgress.findViewById(R.id.progress_text);
        this.cameraSwitchButton = this.mActivity.getCameraSwitchButton();
        this.galleryThumbView = this.mActivity.getGalleryThumbView();
    }

    private void initializeOverlay() {
        this.mRenderOverlay = (RenderOverlay)this.mRootView.findViewById(R.id.render_overlay);
        if (this.mPieRenderer == null) {
            this.mPieRenderer = new PieRenderer((Context)this.mActivity);
            this.mVideoMenu = new VideoMenu(this.mActivity, this, this.mPieRenderer);
        }
        this.mRenderOverlay.addRenderer(this.mPieRenderer);
        if (this.mZoomRenderer == null) {
            this.mZoomRenderer = new ZoomRenderer((Context)this.mActivity);
        }
        this.mRenderOverlay.addRenderer(this.mZoomRenderer);
        if (this.mGestures == null) {
            this.mGestures = new PreviewGestures(this.mActivity, (PreviewGestures.SingleTapListener)this, this.mZoomRenderer, (PreviewGestures.SwipeListener)this);
        }
        this.mGestures.setRenderOverlay(this.mRenderOverlay);
        this.mGestures.reset();
        final Iterator<View> iterator = this.mActivity.getTouchReceivers().iterator();
        while (iterator.hasNext()) {
            this.mGestures.addTouchReceiver(iterator.next());
        }
        this.mGestures.addTouchReceiver((View)this.videoNextImageView);
    }

    public void addProgressBarMediaListener(final RecordingProgressBar.OnAddMediaListener onAddMediaListener) {
        this.progressBar.setOnAddMediaListener(onAddMediaListener);
    }

    public void clearFocus() {
        this.mPieRenderer.clear();
    }

    public void clickShutter() {
        this.mShutterButton.performClick();
    }

    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        if (this.currentTrashStatus != 2) {
            return this.mGestures != null && this.mRenderOverlay != null && this.mGestures.dispatchTouch(motionEvent);
        }
        final int[] array = new int[2];
        this.videoTrashView.getLocationInWindow(array);
        if ((int)this.videoTrashView.getRotationY() == 180) {
            array[0] -= this.videoTrashView.getWidth();
        }
        if ((int)this.videoTrashView.getRotationX() == 180) {
            array[1] -= this.videoTrashView.getHeight();
        }
        int n;
        if (this.videoTrashView.getVisibility() == View.VISIBLE && motionEvent.getX() >= array[0] && motionEvent.getX() < array[0] + this.videoTrashView.getWidth() && motionEvent.getY() >= array[1] && motionEvent.getY() < array[1] + this.videoTrashView.getHeight()) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            return this.mActivity.superDispatchTouchEvent(motionEvent);
        }
        this.setTrashButtonStatus(1);
        this.setProgressBarStatus(RecordingProgressBar.Status.RECORDED, true);
        return true;
    }

    public void enableCameraControls(final boolean b) {
        if (this.mGestures != null) {
            this.mGestures.setZoomOnly(!b);
        }
    }

    public void enableShutter(final boolean enabled) {
        if (this.mShutterButton != null) {
            this.mShutterButton.setEnabled(enabled);
        }
    }

    public List<RecordingProgressBar.Clip> getClips() {
        List<RecordingProgressBar.Clip> clips;
        if ((clips = this.progressBar.getClips()) == null) {
            clips = new ArrayList<RecordingProgressBar.Clip>();
        }
        return clips;
    }

    public int getNextButtonStatus() {
        return this.currentNextStatus;
    }

    public View getPreview() {
        return (View)this.mPreviewFrameLayout;
    }

    public View getRootView() {
        return this.mRootView;
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.mPreviewSurfaceView.getHolder();
    }

    public int getTrashButtonStatus() {
        return this.currentTrashStatus;
    }

    public boolean hasFaces() {
        return false;
    }

    public boolean hidePieRenderer() {
        return false;
    }

    public void hideSurfaceView() {
        this.mPreviewSurfaceView.setVisibility(View.INVISIBLE);
    }

    public void initializePopup(final com.mapia.camera.PreferenceGroup preferenceGroup) {
        this.mVideoMenu.initialize(preferenceGroup);
    }

    public void initializeZoom(final Camera.Parameters cameraParameters) {
        if (cameraParameters == null || !cameraParameters.isZoomSupported()) {
            return;
        }
        this.mZoomMax = cameraParameters.getMaxZoom();
        this.mZoomRatios = (List<Integer>)cameraParameters.getZoomRatios();
        this.mZoomRenderer.setZoomMax(this.mZoomMax);
        this.mZoomRenderer.setZoom(cameraParameters.getZoom());
        this.mZoomRenderer.setZoomValue(this.mZoomRatios.get(cameraParameters.getZoom()));
        this.mZoomRenderer.setOnZoomChangeListener((ZoomRenderer.OnZoomChangedListener)new ZoomChangeListener());
    }

    public boolean isSurfaceViewReady() {
        return this.mSurfaceViewReady;
    }

    public void onFocusFailed(final boolean b) {
        this.mPieRenderer.showFail(b);
    }

    public void onFocusStarted() {
        this.onFocusStarted(false);
    }

    public void onFocusStarted(final boolean b) {
        this.mPieRenderer.showStart(b);
    }

    public void onFocusSucceeded(final boolean b) {
        this.mPieRenderer.showSuccess(b);
    }

    public void onFullScreenChanged(final boolean enabled) {
        if (this.mGestures != null) {
            this.mGestures.setEnabled(enabled);
        }
        if (this.mRenderOverlay != null) {
            final RenderOverlay mRenderOverlay = this.mRenderOverlay;
            int visibility;
            if (enabled) {
                visibility = View.VISIBLE;
            }
            else {
                visibility = View.INVISIBLE;
            }
            mRenderOverlay.setVisibility(visibility);
        }
    }

    public void onShowSwitcherPopup() {
        this.hidePieRenderer();
    }

    public void onSingleTapUp(final View view, final int n, final int n2) {
        this.mController.onSingleTapUp(view, n, n2);
    }

    public void onSwipe(final int n) {
        if (n == 3 && this.ifFirstRecording && !this.isRecording) {
            this.mActivity.changePhotoMode();
            this.mActivity.onCameraSelected(0);
        }
    }

    public void pauseFaceDetection() {
    }

    public void pressShutter(final boolean pressed) {
        this.mShutterButton.setPressed(pressed);
    }

    public void resetUi() {
        this.ifFirstRecording = true;
        this.videoTrashView.setVisibility(View.INVISIBLE);
        this.ratioButton.setVisibility(View.VISIBLE);
        this.cameraModeLayout.setVisibility(View.VISIBLE);
        this.cameraSwitchButton.setVisibility(View.VISIBLE);
        this.mRecordingTimeView.setText((CharSequence)"15");
        this.mRecordingTimeImage.setImageResource(0);
        this.mRecordingTimeLayout.setAlpha(0.5f);
        this.galleryThumbView.setVisibility(View.VISIBLE);
        this.mActivity.enableOrientationListener();
    }

    public void resumeFaceDetection() {
    }

    public void setAspectRatio(final double aspectRatio) {
        this.mPreviewFrameLayout.setAspectRatio(aspectRatio);
    }

    public void setFocusPosition(final int n, final int n2) {
        this.mPieRenderer.setFocus(n, n2);
    }

    public void setNextButtonStatus(final int n) {
        switch (n) {
            case 3: {
                if (this.currentNextStatus != 3) {
                    this.videoNextImageView.setImageResource(R.drawable.cam_arrow_dis);
                    this.currentNextStatus = 3;
                    return;
                }
                break;
            }
            case 4: {
                if (this.currentNextStatus != 4) {
                    this.videoNextImageView.setImageResource(R.drawable.cam_next);
                    this.currentNextStatus = 4;
                    return;
                }
                break;
            }
        }
    }

    public void setOnClickNextButtonListener(final View.OnClickListener onClickListener) {
        this.videoNextImageView.setOnClickListener(onClickListener);
    }

    public void setOnClickTrashButtonListener(final View.OnClickListener onClickListener) {
        this.videoTrashView.setOnClickListener(onClickListener);
    }

    public void setOrientationIndicator(final int orientation, final boolean b) {
        if (this.mGestures != null) {
            this.mGestures.setOrientation(orientation);
        }
    }

    public void setPrefChangedListener(final CameraPreference.OnPreferenceChangedListener listener) {
        this.mVideoMenu.setListener(listener);
    }

    public void setProgressBarListener(final RecordingProgressBar.RecordingProgressBarListener listener) {
        this.progressBar.setListener(listener);
    }

    public void setProgressBarStatus(final RecordingProgressBar.Status status, final boolean b) {
        this.progressBar.setStatus(status, b);
    }

    public void setRecordingTime(final long n) {
        this.currentSec = (int)n;
        final StringBuilder sb = new StringBuilder();
        if (this.totalTime + this.currentSec < 10) {
            sb.append("0");
        }
        sb.append(this.totalTime + this.currentSec);
        sb.append(" sec");
        this.mRecordingTimeView.setText((CharSequence)sb.toString());
    }

    public void setRecordingTime(final String text) {
        this.mRecordingTimeView.setText((CharSequence)text);
    }

    public void setShutterPressed(final boolean b) {
        if (this.mGestures == null) {
            return;
        }
        this.mGestures.setEnabled(!b);
    }

    public void setTrashButtonStatus(final int n) {
        switch (n) {
            case 0: {
                if (this.currentTrashStatus != 0) {
                    this.videoTrashView.setImageResource(R.drawable.cam_video_cancel_dis);
                    this.currentTrashStatus = 0;
                    return;
                }
                break;
            }
            case 1: {
                if (this.currentTrashStatus != 1) {
                    this.videoTrashView.setImageResource(R.drawable.cam_video_cancel);
                    this.currentTrashStatus = 1;
                    return;
                }
                break;
            }
            case 2: {
                if (this.currentTrashStatus != 2) {
                    this.videoTrashView.setImageResource(R.drawable.cam_video_del);
                    this.currentTrashStatus = 2;
                    return;
                }
                break;
            }
        }
    }

    public void showDialog(final VideoDeleteDialog.OnClickConfirmListener onClickConfirmListener) {
        new VideoDeleteDialog((Context)this.mActivity, onClickConfirmListener).show();
    }

    public void showProgress(final boolean b) {
        if (b) {
            this.mProgressText.setText((CharSequence)"0 %");
            this.mProgress.setVisibility(View.VISIBLE);
            return;
        }
        this.mProgress.setVisibility(View.INVISIBLE);
    }

    public void showRecordCompleteUI() {
        this.mShutterButton.setImageResource(R.drawable.cam_video_end);
        this.mRecordingTimeImage.setVisibility(View.INVISIBLE);
    }

    public void showRecordIncompleteUI() {
        this.mShutterButton.setImageResource(R.drawable.camera_selector_video_shutter_button);
        this.mRecordingTimeImage.setVisibility(View.INVISIBLE);
        this.mRecordingTimeImage.setImageResource(R.drawable.cam_dot_pause);
    }

    public void showRecordingUI(final boolean isRecording, final boolean b) {
        this.isRecording = isRecording;
        if (isRecording) {
            if (this.ifFirstRecording) {
                this.mRecordingTimeLayout.setAlpha(1.0f);
                this.videoTrashView.setVisibility(View.VISIBLE);
                this.ratioButton.setVisibility(View.INVISIBLE);
                this.cameraModeLayout.setVisibility(View.INVISIBLE);
                this.cameraSwitchButton.setVisibility(View.INVISIBLE);
                this.galleryThumbView.setVisibility(View.INVISIBLE);
                this.mActivity.disableOrientationListener();
            }
            this.setTrashButtonStatus(0);
            this.mShutterButton.setImageResource(R.drawable.cam_video_pause);
            this.mRecordingTimeImage.setImageResource(R.drawable.cam_dot_rec);
            this.mActivity.setVisibilityTopButton(8);
            this.mRecordingTimeView.setText((CharSequence)"");
        }
        else {
            this.mShutterButton.setImageResource(R.drawable.camera_selector_video_shutter_button);
            this.mActivity.setVisibilityTopButton(0);
            this.setTrashButtonStatus(1);
            this.mRecordingTimeImage.setImageResource(R.drawable.cam_dot_pause);
            this.totalTime += this.currentSec;
            if (this.ifFirstRecording) {
                this.ifFirstRecording = false;
                if (b) {
                    this.progressBar.getClips().clear();
                    this.resetUi();
                }
            }
        }
    }

    public void showSurfaceView() {
        this.mPreviewSurfaceView.setVisibility(View.VISIBLE);
    }

    public void showTimeLapseUI(final boolean b) {
        if (this.mTimeLapseLabel != null) {
            final View mTimeLapseLabel = this.mTimeLapseLabel;
            int visibility;
            if (b) {
                visibility = View.VISIBLE;
            }
            else {
                visibility = View.INVISIBLE;
            }
            mTimeLapseLabel.setVisibility(visibility);
        }
    }

    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
        Log.v("CAM_VideoUI", "Surface changed. width=" + n2 + ". height=" + n3);
    }

    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        Log.v("CAM_VideoUI", "Surface created");
        this.mSurfaceViewReady = true;
    }

    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        Log.v("CAM_VideoUI", "Surface destroyed");
        this.mSurfaceViewReady = false;
    }

    public void updateOnScreenIndicators(final Camera.Parameters cameraParameters, final ComboPreferences comboPreferences) {
        RecordLocationPreference.get((SharedPreferences)comboPreferences, this.mActivity.getContentResolver());
    }

    public void updateProgress(final int n) {
        this.mProgressText.setText((CharSequence)(n + " %"));
    }

    public void updateProgressBar(final long n) {
        this.progressBar.update(n);
    }

    private class ZoomChangeListener implements ZoomRenderer.OnZoomChangedListener
    {
        @Override
        public void onZoomEnd() {
        }

        @Override
        public void onZoomStart() {
        }

        @Override
        public void onZoomValueChanged(int onZoomChanged) {
            onZoomChanged = VideoUI.this.mController.onZoomChanged(onZoomChanged);
            if (VideoUI.this.mZoomRenderer != null) {
                VideoUI.this.mZoomRenderer.setZoomValue(VideoUI.this.mZoomRatios.get(onZoomChanged));
            }
        }
    }
}