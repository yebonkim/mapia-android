package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.mapia.R;
import com.mapia.camera.app.AbstractGalleryActivity;
import com.mapia.camera.ui.GLRoot;
import com.mapia.camera.ui.GLRootView;
import com.mapia.camera.ui.GLView;
import com.mapia.camera.ui.LayoutChangeNotifier;
import com.mapia.camera.ui.ScreenNail;
import com.mapia.util.GalleryUtils;


public abstract class ActivityBase extends AbstractGalleryActivity implements LayoutChangeNotifier.Listener, CameraScreenNail.Listener
{
    public static final String ACTION_IMAGE_CAPTURE_SECURE = "android.media.action.IMAGE_CAPTURE_SECURE";
    private static final int CAMERA_APP_VIEW_TOGGLE_TIME = 100;
    private static final String TAG = "ActivityBase";
    private static final int UPDATE_STORAGE_HINT = 0;
    private static boolean sFirstStartAfterScreenOn;
    protected static int sSecureAlbumId;
    private CameraView cameraView;
    protected float[] mBackgroundColor;
    protected View mCameraAppView;
    private Animation mCameraAppViewFadeIn;
    private Animation mCameraAppViewFadeOut;
    protected CameraManager.CameraProxy mCameraDevice;
    protected boolean mCameraDisabled;
    protected int mCameraId;
    protected ScreenNail mCameraScreenNail;
    private GLView mContentPane;
    private GLRootView mGLRootView;
    private final Handler mHandler;
    protected int mNumberOfCameras;
    protected boolean mOpenCameraFail;
    protected Camera.Parameters mParameters;
    protected boolean mPaused;
    protected int mPendingSwitchCameraId;
    private final BroadcastReceiver mReceiver;
    private int mResultCodeForTesting;
    private Intent mResultDataForTesting;
    private final GLView mRootPane;
    protected boolean mShowCameraAppView;
    private View mSingleTapArea;
    private OnScreenHint mStorageHint;
    private long mStorageSpace;

    static {
        ActivityBase.sFirstStartAfterScreenOn = true;
    }

    public ActivityBase() {
        super();
        this.mPendingSwitchCameraId = -1;
        this.mShowCameraAppView = true;
        this.mStorageSpace = 50000000L;
        this.mHandler = new Handler() {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {}
                    case 0: {
                        ActivityBase.this.updateStorageHint();
                    }
                }
            }
        };
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if (action.equals("android.intent.action.MEDIA_MOUNTED") || action.equals("android.intent.action.MEDIA_UNMOUNTED") || action.equals("android.intent.action.MEDIA_CHECKING") || action.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
                    ActivityBase.this.updateStorageSpaceAndHint();
                }
            }
        };
        this.mRootPane = new GLView() {
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                ActivityBase.this.cameraView.layout(0, 0, n3 - n, n4 - n2);
            }
        };
    }

    private boolean onSingleTapUp(int n, int n2) {
        if (this.mSingleTapArea == null || !this.mShowCameraAppView) {
            return false;
        }
        final int[] relativeLocation = Util.getRelativeLocation((View)this.getGLRoot(), this.mSingleTapArea);
        n -= relativeLocation[0];
        n2 -= relativeLocation[1];
        if (n >= 0 && n < this.mSingleTapArea.getWidth() && n2 >= 0 && n2 < this.mSingleTapArea.getHeight()) {
            this.onSingleTapUp(this.mSingleTapArea, n, n2);
            return true;
        }
        return false;
    }

    public ScreenNail attachScreenNail() {
        if (this.mCameraScreenNail == null) {
            this.mCameraScreenNail = new CameraScreenNail((CameraScreenNail.Listener)this, (Context)this);
        }
        return this.mCameraScreenNail;
    }

    public ScreenNail createCameraScreenNail(final boolean b) {
        this.mCameraAppView = this.findViewById(R.id.camera_app_root);
        if (this.mCameraScreenNail != null) {
            this.mCameraScreenNail.recycle();
        }
        return this.attachScreenNail();
    }

    protected float[] getBackgroundColor() {
        return this.mBackgroundColor;
    }

    protected int getBackgroundColorId() {
        return R.color.default_background;
    }

    public GLRoot getGLRoot() {
        return this.mGLRootView;
    }

    public int getResultCode() {
        return this.mResultCodeForTesting;
    }

    public Intent getResultData() {
        return this.mResultDataForTesting;
    }

    protected long getStorageSpace() {
        return this.mStorageSpace;
    }

    protected void installIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        intentFilter.addAction("android.intent.action.MEDIA_CHECKING");
        intentFilter.addDataScheme("file");
        this.registerReceiver(this.mReceiver, intentFilter);
    }

    public boolean isPanoramaActivity() {
        return false;
    }

    public void onBackPressed() {
        final GLRoot glRoot = this.getGLRoot();
        glRoot.lockRenderThread();
        this.finish();
        glRoot.unlockRenderThread();
    }

    @Override
    public void onCaptureTextureCopied() {
    }

    public void onCreate(final Bundle bundle) {
        this.mBackgroundColor = GalleryUtils.intColorToFloatARGBArray(this.getResources().getColor(this.getBackgroundColorId()));
        this.getWindow().addFlags(1024);
        if (false) {
            this.requestWindowFeature(9);
        }
        else {
            this.requestWindowFeature(1);
        }
        this.cameraView = new CameraView();
        this.mRootPane.addComponent(this.cameraView);
        this.attachScreenNail();
        super.onCreate(bundle);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onFullScreenChanged(final boolean mShowCameraAppView) {
        if (this.mShowCameraAppView != mShowCameraAppView) {
            this.mShowCameraAppView = mShowCameraAppView;
            if (!this.mPaused && !this.isFinishing()) {
                this.updateCameraAppView();
            }
        }
    }

    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return ((n == 84 || n == 82) && keyEvent.isLongPress()) || (n == 82 && this.mShowCameraAppView) || super.onKeyDown(n, keyEvent);
    }

    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return (n == 82 && this.mShowCameraAppView) || super.onKeyUp(n, keyEvent);
    }

    @Override
    public void onLayoutChange(final View view, int n, int n2, final int n3, final int n4) {
        n = n3 - n;
        n2 = n4 - n2;
        if (false) {
            final CameraScreenNail cameraScreenNail = (CameraScreenNail)this.mCameraScreenNail;
            if (Util.getDisplayRotation(this) % 180 != 0) {
                cameraScreenNail.setPreviewFrameLayoutSize(n2, n);
                return;
            }
            cameraScreenNail.setPreviewFrameLayoutSize(n, n2);
        }
    }

    protected void onPause() {
        super.onPause();
        this.mGLRootView.onPause();
        this.mGLRootView.lockRenderThread();
        this.cameraView.setScreenNail(null);
        this.mGLRootView.unlockRenderThread();
        this.cameraView.setScreenNail(null);
        if (this.mStorageHint != null) {
            this.mStorageHint.cancel();
            this.mStorageHint = null;
        }
        this.unregisterReceiver(this.mReceiver);
    }

    @Override
    public void onPreviewTextureCopied() {
    }

    protected void onResume() {
        super.onResume();
        this.mGLRootView.lockRenderThread();
        this.setContentPane(this.mRootPane);
        this.cameraView.setScreenNail(this.mCameraScreenNail);
        this.mGLRootView.unlockRenderThread();
        this.mGLRootView.onResume();
        this.installIntentFilter();
        if (this.updateStorageHintOnResume()) {
            this.updateStorageSpace();
            this.mHandler.sendEmptyMessageDelayed(0, 200L);
        }
    }

    public boolean onSearchRequested() {
        return false;
    }

    protected void onSingleTapUp(final View view, final int n, final int n2) {
    }

    @Override
    public void requestRender() {
        this.getGLRoot().requestRenderForced();
    }

    protected ScreenNail reuseCameraScreenNail(final boolean b) {
        this.mCameraAppView = this.findViewById(2131361995);
        this.cameraView.setScreenNail(this.mCameraScreenNail);
        return this.mCameraScreenNail;
    }

    protected void setContentPane(final GLView mContentPane) {
        (this.mContentPane = mContentPane).setBackgroundColor(this.getBackgroundColor());
        this.getGLRoot().setContentPane(this.mContentPane);
    }

    public void setContentView(final int contentView) {
        super.setContentView(contentView);
        this.mGLRootView = (GLRootView)this.findViewById(R.id.gl_root_view);
    }

    public void setContentView(final View contentView) {
        super.setContentView(contentView);
        this.mGLRootView = (GLRootView)this.findViewById(R.id.gl_root_view);
    }

    protected void setResultEx(final int mResultCodeForTesting) {
        this.setResult(this.mResultCodeForTesting = mResultCodeForTesting);
    }

    protected void setResultEx(final int mResultCodeForTesting, final Intent mResultDataForTesting) {
        this.setResult(this.mResultCodeForTesting = mResultCodeForTesting, this.mResultDataForTesting = mResultDataForTesting);
    }

    protected void setSingleTapUpListener(final View mSingleTapArea) {
        this.mSingleTapArea = mSingleTapArea;
    }

    protected void updateCameraAppView() {
        if (this.mCameraAppViewFadeIn == null) {
            (this.mCameraAppViewFadeIn = (Animation)new AlphaAnimation(0.0f, 1.0f)).setDuration(100L);
            this.mCameraAppViewFadeIn.setInterpolator((Interpolator)new DecelerateInterpolator());
            (this.mCameraAppViewFadeOut = (Animation)new AlphaAnimation(1.0f, 0.0f)).setDuration(100L);
            this.mCameraAppViewFadeOut.setInterpolator((Interpolator)new DecelerateInterpolator());
            this.mCameraAppViewFadeOut.setAnimationListener((Animation.AnimationListener)new HideCameraAppView());
        }
        if (this.mShowCameraAppView) {
            this.mCameraAppView.setVisibility(View.VISIBLE);
            this.mCameraAppView.requestLayout();
            this.mCameraAppView.startAnimation(this.mCameraAppViewFadeIn);
            return;
        }
        this.mCameraAppView.startAnimation(this.mCameraAppViewFadeOut);
    }

    protected void updateStorageHint() {
        this.updateStorageHint(this.mStorageSpace);
    }

    protected void updateStorageHint(final long n) {
        CharSequence text = null;
        if (n == -1L) {
            text = this.getString(R.string.no_storage);
        }
        else if (n == -2L) {
            text = this.getString(R.string.preparing_sd);
        }
        else if (n == -3L) {
            text = this.getString(R.string.access_sd_fail);
        }
        else if (n <= 50000000L) {
            text = this.getString(R.string.spaceIsLow_content);
        }
        if (text != null) {
            if (this.mStorageHint == null) {
                this.mStorageHint = OnScreenHint.makeText((Context)this, text);
            }
            else {
                this.mStorageHint.setText(text);
            }
            this.mStorageHint.show();
        }
        else if (this.mStorageHint != null) {
            this.mStorageHint.cancel();
            this.mStorageHint = null;
        }
    }

    protected boolean updateStorageHintOnResume() {
        return true;
    }

    protected void updateStorageSpace() {
        this.mStorageSpace = Storage.getAvailableSpace();
    }

    protected void updateStorageSpaceAndHint() {
        this.updateStorageSpace();
        this.updateStorageHint(this.mStorageSpace);
    }

    private class HideCameraAppView implements Animation.AnimationListener
    {
        public void onAnimationEnd(final Animation animation) {
            ActivityBase.this.mCameraAppView.setVisibility(4);
        }

        public void onAnimationRepeat(final Animation animation) {
        }

        public void onAnimationStart(final Animation animation) {
        }
    }
}