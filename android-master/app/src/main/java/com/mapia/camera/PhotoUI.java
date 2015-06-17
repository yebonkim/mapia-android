package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.mapia.R;
import com.mapia.camera.ui.CountDownView;
import com.mapia.camera.ui.FaceView;
import com.mapia.camera.ui.FocusIndicator;
import com.mapia.camera.ui.PieRenderer;
import com.mapia.camera.ui.RenderOverlay;
import com.mapia.camera.ui.VerticalSeekBar;
import com.mapia.camera.ui.ZoomRenderer;

import java.util.Iterator;
import java.util.List;


public class PhotoUI implements SurfaceHolder.Callback, PreviewGestures.SingleTapListener, FocusOverlayManager.FocusUI, com.mapia.camera.LocationManager.Listener, Camera.FaceDetectionListener, PreviewGestures.SwipeListener
{
    private static final String TAG = "CAM_UI";
    private final int DURATION_LONG;
    private boolean exposureFadeOutStarted;
    private View exposureLayout;
    private VerticalSeekBar exposureSeekBar;
    private Runnable fadeOutExposureRunnable;
    private Handler handler;
    private CameraActivity mActivity;
    private PhotoController mController;
    private CountDownView mCountDownView;
    private FaceView mFaceView;
    private PreviewGestures mGestures;
    private View.OnLayoutChangeListener mLayoutListener;
    private PhotoMenu mMenu;
    private Toast mNotSelectableToast;
    private PieRenderer mPieRenderer;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private RenderOverlay mRenderOverlay;
    private View mRootView;
    private ShutterButton mShutterButton;
    private volatile SurfaceHolder mSurfaceHolder;
    private Object mSurfaceTexture;
    private int mZoomMax;
    private List<Integer> mZoomRatios;
    private ZoomRenderer mZoomRenderer;

    public PhotoUI(final CameraActivity mActivity, final PhotoController mController, final View mRootView) {
        super();
        this.mPreviewWidth = 0;
        this.mPreviewHeight = 0;
        this.mLayoutListener = (View.OnLayoutChangeListener)new View.OnLayoutChangeListener() {
            public void onLayoutChange(final View view, int n, int n2, int n3, int n4, int n5, final int n6, final int n7, final int n8) {
                n = n3 - n;
                n2 = n4 - n2;
                n3 = n;
                n4 = n2;
                n5 = n3;
                if (Util.getDisplayRotation(PhotoUI.this.mActivity) % 180 != 0) {
                    n5 = n2;
                    n3 = (n4 = n);
                }
                if (PhotoUI.this.mPreviewWidth != n || PhotoUI.this.mPreviewHeight != n2) {
                    PhotoUI.this.mPreviewWidth = n;
                    PhotoUI.this.mPreviewHeight = n2;
                    PhotoUI.this.mController.onScreenSizeChanged(n, n2, n5, n4);
                }
            }
        };
        this.fadeOutExposureRunnable = new Runnable() {
            @Override
            public void run() {
                PhotoUI.this.fadeOutExposureControl();
            }
        };
        this.handler = new Handler();
        this.DURATION_LONG = 2000;
        this.mActivity = mActivity;
        this.mController = mController;
        this.mRootView = mRootView;
        this.mActivity.getLayoutInflater().inflate(R.layout.photo_module, (ViewGroup)this.mRootView, true);
        this.mRenderOverlay = (RenderOverlay)this.mRootView.findViewById(R.id.render_overlay);
        this.initIndicators();
        (this.mCountDownView = (CountDownView)this.mRootView.findViewById(R.id.count_down_to_capture)).setCountDownFinishedListener((CountDownView.OnCountDownFinishedListener)this.mController);
        if (true){//ApiHelper.HAS_FACE_DETECTION) {
            final ViewStub viewStub = (ViewStub)this.mRootView.findViewById(R.id.face_view_stub);
            if (viewStub != null) {
                viewStub.inflate();
                this.mFaceView = (FaceView)this.mRootView.findViewById(R.id.face_view);
            }
        }
        this.exposureLayout = this.mActivity.getExposureLayout();
        this.exposureFadeOutStarted = false;
    }

    private void fadeOutExposureControl() {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        ((Animation)alphaAnimation).setStartOffset(1000L);
        ((Animation)alphaAnimation).setDuration(500L);
        this.exposureLayout.startAnimation((Animation)alphaAnimation);
        ((Animation)alphaAnimation).setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                if (PhotoUI.this.exposureFadeOutStarted) {
                    PhotoUI.this.exposureFadeOutStarted = false;
                    PhotoUI.this.handler.removeCallbacks(PhotoUI.this.fadeOutExposureRunnable);
                    PhotoUI.this.exposureLayout.clearAnimation();
                    PhotoUI.this.exposureLayout.setVisibility(View.INVISIBLE);
                }
            }

            public void onAnimationRepeat(final Animation animation) {
            }

            public void onAnimationStart(final Animation animation) {
                PhotoUI.this.exposureFadeOutStarted = true;
            }
        });
    }

    private FocusIndicator getFocusIndicator() {
        if (this.mFaceView != null && this.mFaceView.faceExists()) {
            return this.mFaceView;
        }
        return this.mPieRenderer;
    }

    private void initIndicators() {
    }

    public void cancelCountDown() {
        this.mShutterButton.setImageResource(R.drawable.camera_selector_photo_shutter_button);
        this.mCountDownView.cancelCountDown();
        this.mPieRenderer.setIsCountDown(false);
    }

    public void clearFaces() {
        if (this.mFaceView != null) {
            this.mFaceView.clear();
        }
    }

    public void clearFocus() {
        final FocusIndicator focusIndicator = this.getFocusIndicator();
        if (focusIndicator != null) {
            focusIndicator.clear();
        }
    }

    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        return this.mGestures != null && this.mRenderOverlay != null && this.mGestures.dispatchTouch(motionEvent);
    }

    public void enableGestures(final boolean enabled) {
        if (this.mGestures != null) {
            this.mGestures.setEnabled(enabled);
        }
    }

    public void enableShutter(final boolean enabled) {
        if (this.mShutterButton != null) {
            this.mShutterButton.setEnabled(enabled);
        }
    }

    public void focusSetIsCountDown(final boolean isCountDown) {
        this.mPieRenderer.setIsCountDown(isCountDown);
    }

    public View getRootView() {
        return this.mRootView;
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceHolder;
    }

    public Object getSurfaceTexture() {
        return this.mSurfaceTexture;
    }

    public boolean hasFaces() {
        return this.mFaceView != null && this.mFaceView.faceExists();
    }

    public void hideGpsOnScreenIndicator() {
    }

    protected void hidePostCaptureAlert() {
        this.mShutterButton.setVisibility(View.VISIBLE);
        this.resumeFaceDetection();
    }

    public void initializeFirstTime() {
        (this.mShutterButton = this.mActivity.getShutterButton()).setImageResource(R.drawable.camera_selector_photo_shutter_button);
        this.mShutterButton.setOnShutterButtonListener((ShutterButton.OnShutterButtonListener)this.mController);
        this.mShutterButton.setVisibility(View.VISIBLE);
        this.mRootView.addOnLayoutChangeListener(this.mLayoutListener);
    }

    public void initializeSecondTime(final Camera.Parameters cameraParameters) {
        this.initializeZoom(cameraParameters);
        if (this.mController.isImageCaptureIntent()) {
            this.hidePostCaptureAlert();
        }
        if (this.mMenu != null) {
            this.mMenu.reloadPreferences();
        }
        this.mRootView.addOnLayoutChangeListener(this.mLayoutListener);
    }

    public void initializeZoom(final Camera.Parameters cameraParameters) {
        if (cameraParameters != null && cameraParameters.isZoomSupported() && this.mZoomRenderer != null) {
            this.mZoomMax = cameraParameters.getMaxZoom();
            this.mZoomRatios = (List<Integer>)cameraParameters.getZoomRatios();
            if (this.mZoomRenderer != null) {
                this.mZoomRenderer.setZoomMax(this.mZoomMax);
                this.mZoomRenderer.setZoom(cameraParameters.getZoom());
                this.mZoomRenderer.setZoomValue(this.mZoomRatios.get(cameraParameters.getZoom()));
                this.mZoomRenderer.setOnZoomChangeListener((ZoomRenderer.OnZoomChangedListener)new ZoomChangeListener());
            }
        }
    }

    public boolean isCountingDown() {
        return this.mCountDownView.isCountingDown();
    }

    public boolean isShutterPressed() {
        return this.mShutterButton.isPressed();
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onCameraOpened(final PreferenceGroup preferenceGroup, final ComboPreferences comboPreferences, final Camera.Parameters cameraParameters, final CameraPreference.OnPreferenceChangedListener listener) {
        if (this.mPieRenderer == null) {
            this.mPieRenderer = new PieRenderer((Context)this.mActivity);
            this.mRenderOverlay.addRenderer(this.mPieRenderer);
        }
        if (this.mMenu == null) {
            (this.mMenu = new PhotoMenu(this.mActivity, this, this.mPieRenderer)).setListener(listener);
        }
        this.mMenu.initialize(preferenceGroup);
        this.exposureSeekBar = this.mMenu.getExposureSeekBar();
        if (this.mZoomRenderer == null) {
            this.mZoomRenderer = new ZoomRenderer((Context)this.mActivity);
            this.mRenderOverlay.addRenderer(this.mZoomRenderer);
        }
        if (this.mGestures == null) {
            this.mGestures = new PreviewGestures(this.mActivity, (PreviewGestures.SingleTapListener)this, this.mZoomRenderer, (PreviewGestures.SwipeListener)this);
        }
        this.mGestures.reset();
        this.mGestures.setRenderOverlay(this.mRenderOverlay);
        final Iterator<View> iterator = this.mActivity.getTouchReceivers().iterator();
        while (iterator.hasNext()) {
            this.mGestures.addTouchReceiver(iterator.next());
        }
        this.mRenderOverlay.requestLayout();
        this.initializeZoom(cameraParameters);
        this.updateOnScreenIndicators(cameraParameters, preferenceGroup, comboPreferences);
    }

    public void onFaceDetection(final Camera.Face[] faces, final Camera camera) {
        this.mFaceView.setFaces(faces);
    }

    public void onFocusFailed(final boolean b) {
        this.getFocusIndicator().showFail(b);
    }

    public void onFocusStarted() {
        this.onFocusStarted(false);
    }

    public void onFocusStarted(final boolean b) {
        this.getFocusIndicator().showStart(b);
    }

    public void onFocusSucceeded(final boolean b) {
        this.getFocusIndicator().showSuccess(b);
    }

    public void onFullScreenChanged(final boolean enabled) {
        final boolean b = true;
        if (this.mFaceView != null) {
            this.mFaceView.setBlockDraw(!enabled);
        }
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
        if (this.mPieRenderer != null) {
            this.mPieRenderer.setBlockFocus(!enabled && b);
        }
        if (!enabled && this.mCountDownView != null) {
            this.mCountDownView.cancelCountDown();
        }
    }

    public void onPause() {
        this.mCountDownView.cancelCountDown();
        this.mSurfaceTexture = null;
        if (this.mFaceView != null) {
            this.mFaceView.clear();
        }
        if (this.mZoomRenderer != null) {
            this.mZoomRenderer.setVisible(false);
        }
        this.mRootView.removeOnLayoutChangeListener(this.mLayoutListener);
        this.mPreviewWidth = 0;
        this.mPreviewHeight = 0;
    }

    public void onSingleTapUp(final View view, final int n, final int n2) {
        this.mController.onSingleTapUp(view, n, n2);
    }

    public void onStartFaceDetection(final int displayOrientation, final boolean mirror) {
        this.mFaceView.clear();
        this.mFaceView.setVisibility(View.VISIBLE);
        this.mFaceView.setDisplayOrientation(displayOrientation);
        this.mFaceView.setMirror(mirror);
        this.mFaceView.resume();
    }

    public void onSwipe(final int n) {
        if (n != 0 && n != 1) {
            if (n == 2) {
                if (this.isCountingDown()) {
                    return;
                }
                if (!this.mActivity.isOnlyPhoto()) {
                    this.mActivity.changeVideoMode();
                    this.mActivity.onCameraSelected(1);
                }
            }
            else if (n == 3) {}
        }
        if (this.exposureSeekBar != null) {}
    }

    public void overrideSettings(final String... array) {
        this.mMenu.overrideSettings(array);
    }

    public void pauseFaceDetection() {
        if (this.mFaceView != null) {
            this.mFaceView.pause();
        }
    }

    public void pressShutterButton() {
        if (this.mShutterButton.isInTouchMode()) {
            this.mShutterButton.requestFocusFromTouch();
        }
        else {
            this.mShutterButton.requestFocus();
        }
        this.mShutterButton.setPressed(true);
    }

    public void resumeFaceDetection() {
        if (this.mFaceView != null) {
            this.mFaceView.resume();
        }
    }

    public void setCameraState(final int n) {
    }

    public void setDisplayOrientation(final int displayOrientation) {
        if (this.mFaceView != null) {
            this.mFaceView.setDisplayOrientation(displayOrientation);
        }
    }

    public void setFocusPosition(final int n, final int n2) {
        this.mPieRenderer.setFocus(n, n2);
    }

    public void setSurfaceTexture(final Object mSurfaceTexture) {
        this.mSurfaceTexture = mSurfaceTexture;
    }

    public void showGpsOnScreenIndicator(final boolean b) {
    }

    public void showPreferencesToast() {
        if (this.mNotSelectableToast == null) {
            this.mNotSelectableToast = Toast.makeText((Context) this.mActivity, (CharSequence) this.mActivity.getResources().getString(R.string.not_selectable_in_scene_mode), Toast.LENGTH_SHORT);
        }
        this.mNotSelectableToast.show();
    }

    public void startCountDown(final int n, final boolean b) {
        this.mShutterButton.setImageResource(R.drawable.cam_timer_stop);
        this.mCountDownView.startCountDown(n, b);
        this.mPieRenderer.setIsCountDown(true);
    }

    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
        Log.v("CAM_UI", "surfaceChanged:" + surfaceHolder + " width=" + n2 + ". height=" + n3);
    }

    public void surfaceCreated(final SurfaceHolder mSurfaceHolder) {
        Log.v("CAM_UI", "surfaceCreated: " + mSurfaceHolder);
        this.mSurfaceHolder = mSurfaceHolder;
        this.mController.onSurfaceCreated(mSurfaceHolder);
    }

    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        Log.v("CAM_UI", "surfaceDestroyed: " + surfaceHolder);
        this.mSurfaceHolder = null;
        this.mController.stopPreview();
    }

    public void updateOnScreenIndicators(final Camera.Parameters cameraParameters, final PreferenceGroup preferenceGroup, final ComboPreferences comboPreferences) {
        if (cameraParameters == null) {
            return;
        }
        final ListPreference preference = preferenceGroup.findPreference("pref_camera_whitebalance_key");
        if (preference != null) {
            preference.getCurrentIndex();
        }
        RecordLocationPreference.get((SharedPreferences)comboPreferences, this.mActivity.getContentResolver());
    }

    private class ZoomChangeListener implements ZoomRenderer.OnZoomChangedListener
    {
        @Override
        public void onZoomEnd() {
            if (PhotoUI.this.mPieRenderer != null) {
                PhotoUI.this.mPieRenderer.setBlockFocus(false);
            }
        }

        @Override
        public void onZoomStart() {
            if (PhotoUI.this.mPieRenderer != null) {
                PhotoUI.this.mPieRenderer.setBlockFocus(true);
            }
        }

        @Override
        public void onZoomValueChanged(int onZoomChanged) {
            onZoomChanged = PhotoUI.this.mController.onZoomChanged(onZoomChanged);
            if (PhotoUI.this.mZoomRenderer != null) {
                PhotoUI.this.mZoomRenderer.setZoomValue(PhotoUI.this.mZoomRatios.get(onZoomChanged));
            }
        }
    }
}