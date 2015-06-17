package com.mapia.camera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenVideo;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.imagechooser.api.VideoChooserListener;
import com.kbeanie.imagechooser.api.VideoChooserManager;
import com.mapia.MainApplication;
import com.mapia.R;
import com.mapia.camera.orientation.OrientationEvent;
import com.mapia.camera.ui.GridGuideView;
import com.mapia.camera.ui.OnSwipeTouchListener;
import com.mapia.camera.ui.RecordingProgressBar;
import com.mapia.camera.ui.VerticalSeekBar;
import com.mapia.camera.viewmodel.RatioManager;
import com.mapia.common.data.PhotoData;
import com.mapia.custom.rotatableview.OnRotateEndListener;
import com.mapia.custom.rotatableview.RotatableImageButton;
import com.mapia.ratio.PhotoRatioActivity;
import com.mapia.util.BitmapUtils;
import com.mapia.util.CameraUtils;
import com.mapia.util.MapiaStringUtil;
import com.mapia.util.MapiaToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class CameraActivity extends ActivityBase implements ImageChooserListener, VideoChooserListener
{
    public static final int PHOTO_MODULE_INDEX = 0;
    private static final String TAG = "CAM_activity";
    public static final int VIDEO_MODULE_INDEX = 1;
    public static PhotoData photoData;
    private final int ACTIVITY_REQUESTCODE_CROP;
    private final int ACTIVITY_REQUESTCODE_FILTER;
    private View cameraModeLayout;
    private View cameraPreviewLayout;
    private RotatableImageButton cameraSwitchButton;
    private String chosenFilePath;
    private int clickCount;
    private View closeButton;
    private View exposureLayout;
    private VerticalSeekBar exposureSeekBar;
    private RotatableImageButton flashButton;
    private int fromActivity;
    private RotatableImageButton galleryThumbnailView;
    private RotatableImageButton gridButton;
    private GridGuideView gridGuideView;
    private Handler handler;
    private ImageChooserManager imageChooserManager;
    private boolean isGridOn;
    private boolean isOnlyPhoto;
    private ObjectAnimator mCameraSwitchAnimator;
    private ServiceConnection mConnection;
    CameraModule mCurrentModule;
    private int mCurrentModuleIndex;
    private MotionEvent mDown;
    private Runnable mFadeInCameraScreenNail;
    private FrameLayout mFrame;
    private boolean mIsSquare;
    private int mLastRawOrientation;
    private MediaSaveService mMediaSaveService;
    private Runnable mOnFrameDrawn;
    private MyOrientationEventListener mOrientationListener;
    private View mRecordingTimeLayout;
    private ShutterButton mShutter;
    private ImageView photoModeImageView;
    private RecordingProgressBar progressBar;
    private ProgressDialog progressDialog;
    private RotatableImageButton ratioButton;
    private RatioManager ratioManager;
    private int secretCount;
    private View squareBlockView1;
    private View squareBlockView2;
    private RotatableImageButton timerImageView;
    private final OnRotateEndListener to34ImageChange;
    private final OnRotateEndListener to43ImageChange;
    private View topLayout;
    private List<View> touchReceivers;
    private VideoChooserManager videoChooserManager;
    private ImageView videoModeImageView;
    private ImageView videoNextImageView;

    public CameraActivity() {
        super();
        this.ACTIVITY_REQUESTCODE_CROP = 10000;
        this.ACTIVITY_REQUESTCODE_FILTER = 10001;
        this.handler = new Handler();
        this.to34ImageChange = new OnRotateEndListener() {
            @Override
            public void onRotateEnd() {
                CameraActivity.this.ratioButton.setImageResource(R.drawable.cam_rate_34);
            }
        };
        this.to43ImageChange = new OnRotateEndListener() {
            @Override
            public void onRotateEnd() {
                CameraActivity.this.ratioButton.setImageResource(R.drawable.cam_rate_43);
            }
        };
        this.mLastRawOrientation = -1;
        this.mConnection = (ServiceConnection)new ServiceConnection() {
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                CameraActivity.this.mMediaSaveService = ((MediaSaveService.LocalBinder)binder).getService();
                CameraActivity.this.mCurrentModule.onMediaSaveServiceConnected(CameraActivity.this.mMediaSaveService);
            }

            public void onServiceDisconnected(final ComponentName componentName) {
                CameraActivity.this.mMediaSaveService = null;
            }
        };
        this.mOnFrameDrawn = new Runnable() {
            @Override
            public void run() {
                CameraActivity.this.runOnUiThread(CameraActivity.this.mFadeInCameraScreenNail);
            }
        };
        this.mFadeInCameraScreenNail = new Runnable() {
            @Override
            public void run() {
                CameraActivity.this.mCameraSwitchAnimator = ObjectAnimator.ofFloat((Object)CameraActivity.this.getCameraScreenNail(), "alpha", new float[] { 0.0f, 1.0f });
                CameraActivity.this.mCameraSwitchAnimator.setStartDelay(50L);
                CameraActivity.this.mCameraSwitchAnimator.start();
            }
        };
    }

    private void bindMediaSaveService() {
        this.bindService(new Intent((Context)this, (Class)MediaSaveService.class), this.mConnection, Context.BIND_ABOVE_CLIENT);
    }

    private void closeModule(final CameraModule cameraModule) {
        cameraModule.onPauseBeforeSuper();
        cameraModule.onPauseAfterSuper();
        this.mFrame.removeAllViews();
    }

    private void doChangeCamera(final int mCurrentModuleIndex) {
        CameraHolder.instance().keep();
        this.closeModule(this.mCurrentModule);
        switch (this.mCurrentModuleIndex = mCurrentModuleIndex) {
            case 1: {
                this.mCurrentModule = new VideoModule();
                break;
            }
            case 0: {
                this.mCurrentModule = new PhotoModule();
                break;
            }
        }
        this.openModule(this.mCurrentModule);
        this.mCurrentModule.onOrientationChanged(this.mLastRawOrientation);
        if (this.mMediaSaveService != null) {
            this.mCurrentModule.onMediaSaveServiceConnected(this.mMediaSaveService);
        }
        this.getCameraScreenNail().setAlpha(0.0f);
        this.getCameraScreenNail().setOnFrameDrawnOneShot(this.mOnFrameDrawn);
    }

    private void initGridGuideView() {
        this.isGridOn = this.mCurrentModule.getComboPreferences().getLocal().getBoolean("pref_grid_guide_key", false);
        this.gridGuideView.getGridGuideBase().setInnerLineAlpha(127);
        this.setGridGuideView();
    }

    private void initIsOnlyPhoto() {
        this.ratioButton.setVisibility(View.INVISIBLE);
        this.videoModeImageView.setVisibility(View.INVISIBLE);
        this.galleryThumbnailView.setVisibility(View.INVISIBLE);
        this.timerImageView.setVisibility(View.INVISIBLE);
    }

    private void initListener() {
        this.closeButton.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.CAMERA_CLOSE);
                CameraActivity.this.finishCameraActivity();
            }
        });
        this.ratioButton.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.CAMERA_RATIO);
                if (CameraActivity.this.ratioManager.is34Ratio() || CameraActivity.this.ratioManager.is43Ratio()) {
                    CameraActivity.this.set11RatioView();
                }
                else {
                    CameraActivity.this.set34RatioView();
                }
                final SharedPreferences.Editor edit = CameraActivity.this.mCurrentModule.getComboPreferences().getLocal().edit();
                edit.putBoolean("pref_square_view_key", CameraActivity.this.mIsSquare);
                edit.apply();
            }
        });
        this.gridButton.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.CAMERA_GUIDE);
                CameraActivity.this.isGridOn = !CameraActivity.this.isGridOn;
                CameraActivity.this.setGridGuideView();
            }
        });
        if (!this.isOnlyPhoto) {
            final OnSwipeTouchListener cameraActivity = new OnSwipeTouchListener(true) {
                @Override
                public boolean onSingleTapUp(final View view, final MotionEvent motionEvent) {
                    if (!CameraActivity.this.mPaused && !CameraActivity.this.isRecording()) {
                        if (CameraActivity.this.mCurrentModuleIndex == 0) {
                            if (view.equals(CameraActivity.this.photoModeImageView)) {
                                if (CameraActivity.this.clickCount > CameraActivity.this.secretCount) {
                                    CameraActivity.this.secretCount = 999;
                                    CameraActivity.this.mCurrentModule.easterEgg();
                                    return false;
                                }
                                CameraActivity.this.clickCount++;
                                return false;
                            }
                            else {
                                if (((PhotoModule)CameraActivity.this.mCurrentModule).isCountDown()) {
                                    return false;
                                }
                                CameraActivity.this.changeVideoMode();
                            }
                        }
                        else if (view.equals(CameraActivity.this.videoModeImageView)) {
                            if (CameraActivity.this.clickCount > CameraActivity.this.secretCount) {
                                CameraActivity.this.secretCount = 999;
                                CameraActivity.this.mCurrentModule.easterEgg();
                                return false;
                            }
                            CameraActivity.this.clickCount++;
                            return false;
                        }
                        else {
                            CameraActivity.this.changePhotoMode();
                        }
                        CameraActivity.this.onCameraSelected(CameraActivity.this.mCurrentModuleIndex);
                        return false;
                    }
                    return false;
                }

                @Override
                public void onSwipeBottom(final View view) {
                }

                @Override
                public void onSwipeLeft(final View view) {
                    if (!CameraActivity.this.mPaused && !CameraActivity.this.isRecording() && (CameraActivity.this.mCurrentModuleIndex != 0 || !((PhotoModule)CameraActivity.this.mCurrentModule).isCountDown()) && CameraActivity.this.mCurrentModuleIndex != 1) {
                        CameraActivity.this.changeVideoMode();
                        CameraActivity.this.onCameraSelected(CameraActivity.this.mCurrentModuleIndex);
                    }
                }

                @Override
                public void onSwipeRight(final View view) {
                    if (!CameraActivity.this.mPaused && !CameraActivity.this.isRecording() && CameraActivity.this.mCurrentModuleIndex != 0) {
                        CameraActivity.this.changePhotoMode();
                        CameraActivity.this.onCameraSelected(CameraActivity.this.mCurrentModuleIndex);
                    }
                }

                @Override
                public void onSwipeTop(final View view) {
                }
            };
            this.photoModeImageView.setOnTouchListener((View.OnTouchListener)cameraActivity);
            this.videoModeImageView.setOnTouchListener((View.OnTouchListener)cameraActivity);
            this.galleryThumbnailView.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
                    if (CameraActivity.this.mCurrentModule instanceof PhotoModule && ((PhotoModule)CameraActivity.this.mCurrentModule).isCountDown()) {
                        return;
                    }
//                    AceUtils.nClick(NClicks.CAMERA_GALLERY);
                    CameraActivity.this.progressDialog = ProgressDialog.show((Context)CameraActivity.this, (CharSequence)"", (CharSequence)"Loading..", true);
                    try {
                        if (CameraActivity.this.mCurrentModuleIndex == 0) {
                            CameraActivity.this.imageChooserManager = new ImageChooserManager(CameraActivity.this, 291, "mapia_tmp", false);
                            CameraActivity.this.imageChooserManager.setImageChooserListener(CameraActivity.this);
                            CameraActivity.this.imageChooserManager.choose();
                            return;
                        }

                    }
                    catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                        CameraActivity.this.progressDialog.dismiss();
                        CameraActivity.this.progressDialog = null;
                    }
                    catch (Exception ex2) {
                        ex2.printStackTrace();
                        CameraActivity.this.progressDialog.dismiss();
                        CameraActivity.this.progressDialog = null;
                    }
                }
            });
        }
    }

    private void initModeCount() {
        this.clickCount = 0;
        this.secretCount = 30;
    }

    private void openModule(final CameraModule cameraModule) {
        cameraModule.init(this, (View)this.mFrame);
        cameraModule.onResumeBeforeSuper();
        this.handler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                cameraModule.onResumeAfterSuper();
                CameraActivity.this.mPaused = false;
            }
        }, 500L);
    }

    private void set11RatioView() {
        final RelativeLayout.LayoutParams relativeLayoutLayoutParams = (RelativeLayout.LayoutParams)this.ratioButton.getLayoutParams();
        this.ratioButton.setImageResource(R.drawable.camera_selector_rate11_button);
        relativeLayoutLayoutParams.width = (int)this.getResources().getDimension(R.dimen.camera_rate_11_button_size);
        relativeLayoutLayoutParams.height = (int)this.getResources().getDimension(R.dimen.camera_rate_11_button_size);
        relativeLayoutLayoutParams.leftMargin = (int)this.getResources().getDimension(R.dimen.camera_rate_11_button_margin_left);
        this.ratioManager.setCurrentRect(0);
        this.mIsSquare = true;
        this.squareBlockView1.setVisibility(View.VISIBLE);
        this.squareBlockView2.setVisibility(View.VISIBLE);
        final Animation loadAnimation = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.abc_slide_in_top);
        loadAnimation.setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                CameraActivity.this.gridGuideView.getGridGuideBase().setRect(CameraActivity.this.ratioManager.getCurrentRect());
                CameraActivity.this.gridGuideView.invalidate();
            }

            public void onAnimationRepeat(final Animation animation) {
            }

            public void onAnimationStart(final Animation animation) {
            }
        });
        this.squareBlockView1.startAnimation(loadAnimation);
        this.squareBlockView2.startAnimation(AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.abc_slide_in_bottom));
    }

    private void set34RatioView() {
        final RelativeLayout.LayoutParams relativeLayoutLayoutParams = (RelativeLayout.LayoutParams)this.ratioButton.getLayoutParams();
        final int currentOrientation = OrientationEvent.getCurrentOrientation();
        if (currentOrientation == 90 || currentOrientation == 270) {
            this.ratioButton.setImageResource(R.drawable.camera_selector_rate43_button);
            this.ratioManager.setCurrentRect(2);
        }
        else {
            this.ratioButton.setImageResource(R.drawable.camera_selector_rate34_button);
            this.ratioManager.setCurrentRect(1);
        }
        relativeLayoutLayoutParams.width = (int)this.getResources().getDimension(R.dimen.camera_rate_34_button_width);
        relativeLayoutLayoutParams.height = (int)this.getResources().getDimension(R.dimen.camera_rate_34_button_height);
        relativeLayoutLayoutParams.leftMargin = (int)this.getResources().getDimension(R.dimen.camera_rate_34_button_margin_left);
        this.mIsSquare = false;
        this.squareBlockView1.setVisibility(View.INVISIBLE);
        this.squareBlockView2.setVisibility(View.INVISIBLE);
        final Animation loadAnimation = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.abc_slide_out_top);
        loadAnimation.setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                if (CameraActivity.this.ratioManager.is43Ratio()) {
                    CameraActivity.this.gridGuideView.getGridGuideBase().setRect(CameraActivity.this.ratioManager.getRect_3_4());
                }
                else {
                    CameraActivity.this.gridGuideView.getGridGuideBase().setRect(CameraActivity.this.ratioManager.getCurrentRect());
                }
                CameraActivity.this.gridGuideView.invalidate();
            }

            public void onAnimationRepeat(final Animation animation) {
            }

            public void onAnimationStart(final Animation animation) {
            }
        });
        this.squareBlockView1.startAnimation(loadAnimation);
        this.squareBlockView2.startAnimation(AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.abc_slide_out_bottom));
    }

    private void setGridGuideView() {
        if (!this.isGridOn) {
            this.gridButton.setImageResource(R.drawable.cam_grid_off);
            this.gridGuideView.setVisibility(View.INVISIBLE);
        }
        else {
            this.gridButton.setImageResource(R.drawable.cam_grid);
            this.gridGuideView.setVisibility(View.VISIBLE);
        }
        final SharedPreferences.Editor edit = this.mCurrentModule.getComboPreferences().getLocal().edit();
        edit.putBoolean("pref_grid_guide_key", this.isGridOn);
        edit.apply();
    }

    private void unbindMediaSaveService() {
        if (this.mMediaSaveService != null) {
            this.mMediaSaveService.setListener(null);
        }
        if (this.mConnection != null) {
            this.unbindService(this.mConnection);
        }
    }

    public void changePhotoMode() {
        if (this.mCurrentModuleIndex == 0) {
            return;
        }
//        AceUtils.nClick(NClicks.CAMERA_CAMERA);
        final RelativeLayout.LayoutParams relativeLayoutLayoutParams = (RelativeLayout.LayoutParams)this.photoModeImageView.getLayoutParams();
        relativeLayoutLayoutParams.rightMargin = 0;
        relativeLayoutLayoutParams.addRule(14);
        relativeLayoutLayoutParams.addRule(0, 0);
        final RelativeLayout.LayoutParams relativeLayoutLayoutParams2 = (RelativeLayout.LayoutParams)this.videoModeImageView.getLayoutParams();
        relativeLayoutLayoutParams2.leftMargin = (int)this.getResources().getDimension(R.dimen.camera_mode_view_margin);
        relativeLayoutLayoutParams2.addRule(14);
        relativeLayoutLayoutParams2.addRule(1, this.photoModeImageView.getId());
        this.photoModeImageView.setImageResource(R.drawable.cam_mode_camera);
        this.videoModeImageView.setImageResource(R.drawable.cam_mode_video_dis);
        this.mCurrentModuleIndex = 0;
        this.cameraModeLayout.requestLayout();
        this.videoNextImageView.setVisibility(View.INVISIBLE);
        this.timerImageView.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.mRecordingTimeLayout.setVisibility(View.INVISIBLE);
        new GalleryThumbnailTask().execute(new Void[0]);
    }

    public void changeVideoMode() {
        if (this.mCurrentModuleIndex == 1) {
            return;
        }
//        AceUtils.nClick(NClicks.CAMERA_VIDEO);
        final RelativeLayout.LayoutParams relativeLayoutLayoutParams = (RelativeLayout.LayoutParams)this.photoModeImageView.getLayoutParams();
        relativeLayoutLayoutParams.rightMargin = (int)this.getResources().getDimension(R.dimen.camera_mode_view_margin);
        relativeLayoutLayoutParams.addRule(14, 0);
        relativeLayoutLayoutParams.addRule(0, this.videoModeImageView.getId());
        final RelativeLayout.LayoutParams relativeLayoutLayoutParams2 = (RelativeLayout.LayoutParams)this.videoModeImageView.getLayoutParams();
        relativeLayoutLayoutParams2.leftMargin = 0;
        relativeLayoutLayoutParams2.addRule(14);
        relativeLayoutLayoutParams2.addRule(1, 0);
        this.photoModeImageView.setImageResource(R.drawable.cam_mode_video_dis);
        this.videoModeImageView.setImageResource(R.drawable.cam_mode_video);
        this.mCurrentModuleIndex = 1;
        this.cameraModeLayout.requestLayout();
        this.videoNextImageView.setVisibility(View.VISIBLE);
        this.timerImageView.setVisibility(View.INVISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.mRecordingTimeLayout.setVisibility(View.VISIBLE);
        new GalleryThumbnailTask().execute(new Void[0]);
    }

    public void disableOrientationListener() {
        this.mOrientationListener.disable();
    }

    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        if (this.mCurrentModule.isDone() || this.mPaused) {
            return true;
        }
        if (!this.isTrashReadyMode() && this.cameraPreviewLayout.getHeight() < motionEvent.getY()) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return this.mCurrentModule.dispatchTouchEvent(motionEvent);
    }

    public void enableOrientationListener() {
        this.mOrientationListener.enable();
    }

    public void finishCameraActivity() {
        this.mCurrentModule.onFinish();
    }

    public long getAutoFocusTime() {
        if (this.mCurrentModule instanceof PhotoModule) {
            return ((PhotoModule)this.mCurrentModule).mAutoFocusTime;
        }
        return -1L;
    }

    public View getCameraModeLayout() {
        return this.cameraModeLayout;
    }

    public CameraScreenNail getCameraScreenNail() {
        return (CameraScreenNail)this.mCameraScreenNail;
    }

    public View getCameraSwitchButton() {
        return (View)this.cameraSwitchButton;
    }

    public long getCaptureStartTime() {
        if (this.mCurrentModule instanceof PhotoModule) {
            return ((PhotoModule)this.mCurrentModule).mCaptureStartTime;
        }
        return -1L;
    }

    public View getExposureLayout() {
        return this.exposureLayout;
    }

    public VerticalSeekBar getExposureSeekBar() {
        return this.exposureSeekBar;
    }

    public ImageView getFlashView() {
        return (ImageView)this.flashButton;
    }

    public View getGalleryThumbView() {
        return (View)this.galleryThumbnailView;
    }

    public long getJpegCallbackFinishTime() {
        if (this.mCurrentModule instanceof PhotoModule) {
            return ((PhotoModule)this.mCurrentModule).mJpegCallbackFinishTime;
        }
        return -1L;
    }

    public MediaSaveService getMediaSaveService() {
        return this.mMediaSaveService;
    }

    public long getPictureDisplayedToJpegCallbackTime() {
        if (this.mCurrentModule instanceof PhotoModule) {
            return ((PhotoModule)this.mCurrentModule).mPictureDisplayedToJpegCallbackTime;
        }
        return -1L;
    }

    public View getRatioButton() {
        return (View)this.ratioButton;
    }

    public RecordingProgressBar getRecordingProgressBar() {
        return this.progressBar;
    }

    public View getRecordingTimeLayout() {
        return this.mRecordingTimeLayout;
    }

    public ShutterButton getShutterButton() {
        return this.mShutter;
    }

    public long getShutterLag() {
        if (this.mCurrentModule instanceof PhotoModule) {
            return ((PhotoModule)this.mCurrentModule).mShutterLag;
        }
        return -1L;
    }

    public long getShutterToPictureDisplayedTime() {
        if (this.mCurrentModule instanceof PhotoModule) {
            return ((PhotoModule)this.mCurrentModule).mShutterToPictureDisplayedTime;
        }
        return -1L;
    }

    public ImageView getTimerImageView() {
        return (ImageView)this.timerImageView;
    }

    public List<View> getTouchReceivers() {
        return this.touchReceivers;
    }

    public ImageView getVideoNextImageView() {
        return this.videoNextImageView;
    }

    public void init() {
        this.mShutter = (ShutterButton)this.findViewById(R.id.shutter_button);
        this.closeButton = this.findViewById(R.id.camera_close_button);
        this.cameraPreviewLayout = this.findViewById(R.id.camera_preview_layout);
        this.cameraModeLayout = this.findViewById(R.id.camera_top_layout);
        this.exposureLayout = this.findViewById(R.id.camera_exposure_layout);
        this.squareBlockView1 = this.findViewById(R.id.camera_square_block_1);
        this.squareBlockView2 = this.findViewById(R.id.camera_square_block_2);
        this.mRecordingTimeLayout = this.findViewById(R.id.recording_time_layout);
        this.topLayout = this.findViewById(R.id.camera_top_layout);
        this.photoModeImageView = (ImageView)this.findViewById(R.id.camera_photo_mode_view);
        this.videoModeImageView = (ImageView)this.findViewById(R.id.camera_video_mode_view);
        this.videoNextImageView = (ImageView)this.findViewById(R.id.camera_video_next_button);
        this.timerImageView = (RotatableImageButton)this.findViewById(R.id.camera_timer_button);
        this.ratioButton = (RotatableImageButton)this.findViewById(R.id.camera_ratio_button);
        this.cameraSwitchButton = (RotatableImageButton)this.findViewById(R.id.camera_switch_button);
        this.gridButton = (RotatableImageButton)this.findViewById(R.id.camera_grid_button);
        this.flashButton = (RotatableImageButton)this.findViewById(R.id.camera_flash_button);
        this.gridGuideView = (GridGuideView)this.findViewById(R.id.camera_gird_guide_view);
        this.galleryThumbnailView = (RotatableImageButton)this.findViewById(R.id.camera_gallery_thumbnail);
        this.exposureSeekBar = (VerticalSeekBar)this.findViewById(R.id.camera_exposure_seek_bar);
        this.progressBar = (RecordingProgressBar)this.findViewById(R.id.record_progress_bar);
        this.mCurrentModuleIndex = 0;
        (this.touchReceivers = new ArrayList<View>()).add((View)this.flashButton);
        this.touchReceivers.add((View)this.gridButton);
        this.touchReceivers.add((View)this.cameraSwitchButton);
        this.touchReceivers.add(this.closeButton);
        this.touchReceivers.add((View)this.timerImageView);
        this.touchReceivers.add((View)this.photoModeImageView);
        this.touchReceivers.add((View)this.videoModeImageView);
    }

    @Override
    protected void installIntentFilter() {
        super.installIntentFilter();
        this.mCurrentModule.installIntentFilter();
    }

    public boolean isInCameraApp() {
        return this.mShowCameraAppView;
    }

    public boolean isOnlyPhoto() {
        return this.isOnlyPhoto;
    }

    public boolean isRecording() {
        return this.mCurrentModule instanceof VideoModule && ((VideoModule)this.mCurrentModule).isRecording();
    }

    public boolean isSquare() {
        return this.mIsSquare;
    }

    public boolean isTrashReadyMode() {
        return this.mCurrentModule instanceof VideoModule && ((VideoModule)this.mCurrentModule).isTrashReadyMode();
    }

    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n2 == -1) {
            switch (n) {
                case 291: {
                    if (this.imageChooserManager == null) {
                        (this.imageChooserManager = new ImageChooserManager(this, 291, "mapia_tmp", false)).setImageChooserListener(this);
                    }
                    this.imageChooserManager.submit(n, intent);
                }
                case 295: {
                    if (this.videoChooserManager == null) {
                        (this.videoChooserManager = new VideoChooserManager(this, 295, "mapia_tmp", false)).setVideoChooserListener(this);
                    }
                    this.videoChooserManager.submit(n, intent);
                }
                case 10000: {
                    BitmapUtils.deleteImageChooserTemp();
                    MainApplication.getInstance().getPostingInfo().copyrightYn = "N";
                }
            }
        }
        else if (n2 == 0) {
            MainApplication.getInstance().getPostingInfo().body = null;
            MainApplication.getInstance().getPostingInfo().locationData = null;
            MainApplication.getInstance().getPostingInfo().snsParam = null;
            switch (n) {
                case 10001: {
                    break;
                }
                default: {}
                case 291:
                case 295: {
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                        this.progressDialog = null;
                        return;
                    }
                    break;
                }
                case 10000: {
                    if (n == 10000) {
                        BitmapUtils.deleteImageChooserTemp();
                    }
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                        this.progressDialog = null;
                        return;
                    }
                    break;
                }
            }
        }
        else if (n2 == 2) {
            this.setResult(2, intent);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (!this.mCurrentModule.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void onCameraSelected(final int n) {
        if (this.mPaused) {
            return;
        }
        this.mPaused = true;
        final CameraScreenNail cameraScreenNail = this.getCameraScreenNail();
        if (cameraScreenNail != null) {
            if (this.mCameraSwitchAnimator != null && this.mCameraSwitchAnimator.isRunning()) {
                this.mCameraSwitchAnimator.cancel();
            }
            (this.mCameraSwitchAnimator = ObjectAnimator.ofFloat((Object)cameraScreenNail, "alpha", new float[] { cameraScreenNail.getAlpha(), 0.0f })).addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    CameraActivity.this.doChangeCamera(n);
                }
            });
            this.mCameraSwitchAnimator.start();
        }
        else {
            this.doChangeCamera(n);
        }
        this.initModeCount();
    }

    @Override
    public void onCaptureTextureCopied() {
        this.mCurrentModule.onCaptureTextureCopied();
    }

    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mCurrentModule.onConfigurationChanged(configuration);
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.camera_main);
        this.fromActivity = this.getIntent().getIntExtra("cameraFrom", 0);
        if (this.fromActivity == 101) {
            this.isOnlyPhoto = true;
        }
        else {
            this.isOnlyPhoto = false;
        }
        this.mFrame = (FrameLayout)this.findViewById(R.id.camera_app_root);
        this.init();
        this.mCurrentModule = new PhotoModule();
        this.mCurrentModuleIndex = 0;
        this.mCurrentModule.init(this, (View)this.mFrame);
        this.initGridGuideView();
        this.initListener();
        this.mOrientationListener = new MyOrientationEventListener((Context)this);
        this.bindMediaSaveService();
        this.cameraPreviewLayout.post((Runnable)new Runnable() {
            @Override
            public void run() {
                final int width = CameraActivity.this.cameraPreviewLayout.getWidth();
                final int height = CameraActivity.this.cameraPreviewLayout.getHeight();
                CameraActivity.this.ratioManager = new RatioManager(width, height);
                final int n = (height - width) / 2;
                CameraActivity.this.squareBlockView1.getLayoutParams().height = n;
                CameraActivity.this.squareBlockView2.getLayoutParams().height = n;
                if (!CameraActivity.this.isOnlyPhoto) {
                    CameraActivity.this.mIsSquare = CameraActivity.this.mCurrentModule.getComboPreferences().getLocal().getBoolean("pref_square_view_key", true);
                    if (CameraActivity.this.mIsSquare) {
                        CameraActivity.this.ratioManager.setCurrentRect(0);
                    }
                    else {
                        CameraActivity.this.set34RatioView();
                    }
                }
                else {
                    CameraActivity.this.ratioManager.setCurrentRect(0);
                }
                CameraActivity.this.gridGuideView.getGridGuideBase().setRect(CameraActivity.this.ratioManager.getCurrentRect());
            }
        });
        this.mIsSquare = true;
        if (this.isOnlyPhoto) {
            this.initIsOnlyPhoto();
        }
        this.initModeCount();
    }

    public void onDestroy() {
        if (this.mCurrentModule != null && this.mCurrentModule instanceof VideoModule) {
            ((VideoModule)this.mCurrentModule).deleteAllTempFiles();
        }
        this.unbindMediaSaveService();
        super.onDestroy();
    }

    @Override
    public void onError(final String s) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (CameraActivity.this.progressDialog != null) {
                    CameraActivity.this.progressDialog.dismiss();
                    CameraActivity.this.progressDialog = null;
                    if (CameraActivity.this.mCurrentModuleIndex != 0) {
                        MapiaToast.show(CameraActivity.this, CameraActivity.this.getString(R.string.camera_error_1), 1);
                        return;
                    }
                    MapiaToast.show(CameraActivity.this, CameraActivity.this.getString(R.string.image_load_fail), 1);
                }
            }
        });
    }

    @Override
    public void onImageChosen(final ChosenImage chosenImage) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                MainApplication.getInstance().getPostingInfo().copyrightYn = "N";
                CameraActivity.this.chosenFilePath = chosenImage.getFilePathOriginal();
                CameraActivity.this.chosenFilePath = MapiaStringUtil.decodeUrl(CameraActivity.this.chosenFilePath);
                final Intent intent = new Intent((Context)CameraActivity.this, (Class)PhotoRatioActivity.class);
                intent.putExtra("chosenFile", CameraActivity.this.chosenFilePath);
                CameraActivity.this.startActivityForResult(intent, 10000);
                if (CameraActivity.this.progressDialog != null) {
                    CameraActivity.this.progressDialog.dismiss();
                    CameraActivity.this.progressDialog = null;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return this.mCurrentModule.onKeyDown(n, keyEvent) || super.onKeyDown(n, keyEvent);
    }

    @Override
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return this.mCurrentModule.onKeyUp(n, keyEvent) || super.onKeyUp(n, keyEvent);
    }

    public void onPause() {
        this.mPaused = true;
        this.mOrientationListener.disable();
        this.mCurrentModule.onPauseBeforeSuper();
        super.onPause();
        this.mCurrentModule.onPauseAfterSuper();
    }

    @Override
    public void onPreviewTextureCopied() {
        this.mCurrentModule.onPreviewTextureCopied();
    }

    public void onResume() {
        this.mPaused = false;
        this.mOrientationListener.enable();
        this.mCurrentModule.onResumeBeforeSuper();
        super.onResume();
        this.mCurrentModule.onResumeAfterSuper();
        if (!this.isOnlyPhoto) {
            new GalleryThumbnailTask().execute(new Void[0]);
        }
        if (this.getClass() != null) {
//            AceUtils.site(this.getClass().getSimpleName());
        }
    }

    @Override
    protected void onSingleTapUp(final View view, final int n, final int n2) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mCurrentModule.onStop();
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        this.mCurrentModule.onUserInteraction();
    }

    @Override
    public void onVideoChosen(final ChosenVideo chosenVideo) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                CameraActivity.this.chosenFilePath = chosenVideo.getVideoFilePath();
                CameraActivity.this.chosenFilePath = MapiaStringUtil.decodeUrl(CameraActivity.this.chosenFilePath);
                if (!CameraUtils.isSupportVideoFile(new File(CameraActivity.this.chosenFilePath).getName())) {
                    MapiaToast.show(CameraActivity.this, CameraActivity.this.getString(R.string.camera_error_1), 1);
                }
                else {
                    MainApplication.getInstance().getPostingInfo().copyrightYn = "N";
//                    final Intent intent = new Intent((Context)CameraActivity.this, (Class)VideoRatioActivity.class);
//                    intent.putExtra("chosenFile", CameraActivity.this.chosenFilePath);
//                    CameraActivity.this.startActivityForResult(intent, 10000);
                }
                if (CameraActivity.this.progressDialog != null) {
                    CameraActivity.this.progressDialog.dismiss();
                    CameraActivity.this.progressDialog = null;
                }
            }
        });
    }

    public void setVisibilityTopButton(final int visibility) {
        this.topLayout.setVisibility(visibility);
    }

    public void startFilterActivity() {
        if (this.isOnlyPhoto) {
            this.setResult(-1, new Intent());
            this.finish();
            return;
        }
//        final Intent intent = new Intent((Context)this, (Class)ApplyFilterActivity.class);
//        intent.putExtra("cameraFrom", 0);
//        intent.putExtra("videoRatio", this.ratioManager.getCurrentRatio());
//        this.startActivityForResult(intent, 10001);
    }

    public void startFilterActivity(final String s) {
//        final Intent intent = new Intent((Context)this, (Class)VideoFilterPreviewActivity.class);
//        intent.putExtra("filterTargetPath", s);
//        intent.putExtra("cameraFrom", 0);
//        intent.putExtra("videoRatio", this.ratioManager.getCurrentRatio());
//        this.startActivityForResult(intent, 10001);
    }

    public boolean superDispatchTouchEvent(final MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    public void updateCameraAppView() {
        super.updateCameraAppView();
        this.mCurrentModule.updateCameraAppView();
    }

    @Override
    protected boolean updateStorageHintOnResume() {
        return this.mCurrentModule.updateStorageHintOnResume();
    }

    private class GalleryThumbnailTask extends AsyncTask<Void, Void, Bitmap>
    {
        protected Bitmap doInBackground(final Void... array) {
            Cursor cursor = null;
            if (CameraActivity.this.mCurrentModuleIndex == 0) {
                cursor = CameraActivity.this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { "_id", "_data", "bucket_display_name", "datetaken", "mime_type" }, (String)null, (String[])null, "datetaken DESC");
            }
            else if (CameraActivity.this.mCurrentModuleIndex == 1) {
                cursor = CameraActivity.this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { "_id", "_data", "bucket_display_name", "datetaken", "mime_type" }, (String)null, (String[])null, "datetaken DESC");
            }
            final int n = (int)CameraActivity.this.getApplicationContext().getResources().getDimension(R.dimen.camera_galley_thumbnail_size);
            if (cursor != null && cursor.moveToFirst()) {
                final String string = cursor.getString(1);
                cursor.close();
                if (string != null) {
                    Bitmap bitmap = null;
                    if (CameraActivity.this.mCurrentModuleIndex == 0) {
                        bitmap = BitmapUtils.thumbnailBitmap(string, n);
                    }
                    else if (CameraActivity.this.mCurrentModuleIndex == 1) {
                        bitmap = ThumbnailUtils.createVideoThumbnail(string, 1);
                    }
                    if (bitmap != null) {
                        return bitmap;
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(final Bitmap imageBitmap) {
            if (imageBitmap != null) {
                CameraActivity.this.galleryThumbnailView.setImageBitmap(imageBitmap);
                return;
            }
            CameraActivity.this.galleryThumbnailView.setImageResource(R.drawable.cam_thumbnail);
        }
    }

    private class MyOrientationEventListener extends OrientationEventListener
    {
        public MyOrientationEventListener(final Context context) {
            super(context);
        }

        public void onOrientationChanged(final int n) {
            if (n == -1) {
                return;
            }
            CameraActivity.this.mLastRawOrientation = Util.roundOrientation(n, CameraActivity.this.mLastRawOrientation);
            CameraActivity.this.mCurrentModule.onOrientationChanged(CameraActivity.this.mLastRawOrientation);
            OrientationEvent.setCurrentOrientation(CameraActivity.this.mLastRawOrientation);
            CameraActivity.this.gridButton.setOrientation(OrientationEvent.getCurrentOrientation(), true);
            CameraActivity.this.ratioButton.setOrientation(OrientationEvent.getCurrentOrientation(), true);
            if (CameraActivity.this.ratioManager != null) {
                if (CameraActivity.this.ratioManager.is34Ratio() || CameraActivity.this.ratioManager.is43Ratio()) {
                    if (OrientationEvent.getCurrentOrientation() == 270 || OrientationEvent.getCurrentOrientation() == 90) {
                        CameraActivity.this.ratioButton.setRotateEndListener(CameraActivity.this.to43ImageChange);
                        CameraActivity.this.ratioManager.setCurrentRect(2);
                    }
                    else {
                        CameraActivity.this.ratioButton.setRotateEndListener(CameraActivity.this.to34ImageChange);
                        CameraActivity.this.ratioManager.setCurrentRect(1);
                    }
                }
                else {
                    CameraActivity.this.ratioButton.setRotateEndListener(null);
                }
            }
            CameraActivity.this.cameraSwitchButton.setOrientation(OrientationEvent.getCurrentOrientation(), true);
            CameraActivity.this.flashButton.setOrientation(OrientationEvent.getCurrentOrientation(), true);
            CameraActivity.this.galleryThumbnailView.setOrientation(OrientationEvent.getCurrentOrientation(), true);
            CameraActivity.this.timerImageView.setOrientation(OrientationEvent.getCurrentOrientation(), true);
        }
    }
}