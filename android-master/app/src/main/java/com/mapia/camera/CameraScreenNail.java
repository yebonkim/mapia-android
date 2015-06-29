package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.Matrix;
import android.util.Log;

import com.mapia.camera.glrenderer.GLCanvas;
import com.mapia.camera.glrenderer.RawTexture;
import com.mapia.camera.ui.SurfaceTextureScreenNail;


@TargetApi(11)
public class CameraScreenNail extends SurfaceTextureScreenNail {
    private static final int ANIM_CAPTURE_RUNNING = 2;
    private static final int ANIM_CAPTURE_START = 1;
    private static final int ANIM_NONE = 0;
    private static final int ANIM_SWITCH_COPY_TEXTURE = 3;
    private static final int ANIM_SWITCH_DARK_PREVIEW = 4;
    private static final int ANIM_SWITCH_RUNNING = 7;
    private static final int ANIM_SWITCH_START = 6;
    private static final int ANIM_SWITCH_WAITING_FIRST_FRAME = 5;
    private static final String TAG = "CAM_ScreenNail";
    private boolean mAcquireTexture;
    private float mAlpha;
    private int mAnimState;
    private RawTexture mAnimTexture;
    private CaptureAnimManager mCaptureAnimManager;
    private final DrawClient mDefaultDraw;
    private DrawClient mDraw;
    private boolean mEnableAspectRatioClamping;
    private boolean mFirstFrameArrived;
    private boolean mFullScreen;
    private Listener mListener;
    private Object mLock;
    private Runnable mOnFrameDrawnListener;
    private OnFrameDrawnListener mOneTimeFrameDrawnListener;
    private int mRenderHeight;
    private int mRenderWidth;
    private float mScaleX;
    private float mScaleY;
    private SwitchAnimManager mSwitchAnimManager;
    private final float[] mTextureTransformMatrix;
    private int mUncroppedRenderHeight;
    private int mUncroppedRenderWidth;
    private boolean mVisible;

    public CameraScreenNail(final Listener mListener, final Context context) {
        super();
        this.mTextureTransformMatrix = new float[16];
        this.mSwitchAnimManager = new SwitchAnimManager();
        this.mAnimState = 0;
        this.mLock = new Object();
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mEnableAspectRatioClamping = false;
        this.mAcquireTexture = false;
        this.mDefaultDraw = (DrawClient) new DrawClient() {
            @Override
            public RawTexture copyToTexture(final GLCanvas glCanvas, final RawTexture rawTexture, final int n, final int n2) {
                return null;
            }

            @Override
            public void onDraw(final GLCanvas glCanvas, final int x, final int y, final int width, final int height) {
                CameraScreenNail.super.draw(glCanvas, x, y, width, height);
            }

            @Override
            public boolean requiresSurfaceTexture() {
                return true;
            }
        };
        this.mDraw = this.mDefaultDraw;
        this.mAlpha = 1.0f;
        this.mListener = mListener;
        this.mCaptureAnimManager = new CaptureAnimManager(context);
    }

    private void allocateTextureIfRequested(final GLCanvas glCanvas) {
        synchronized (this.mLock) {
            if (this.mAcquireTexture) {
                super.acquireSurfaceTexture(glCanvas);
                this.mAcquireTexture = false;
                this.mLock.notifyAll();
            }
        }
    }

    private void callbackIfNeeded() {
        if (this.mOneTimeFrameDrawnListener != null) {
            this.mOneTimeFrameDrawnListener.onFrameDrawn(this);
            this.mOneTimeFrameDrawnListener = null;
        }
    }

    private void copyPreviewTexture(final GLCanvas glCanvas) {
        if (!this.mDraw.requiresSurfaceTexture()) {
            this.mAnimTexture = this.mDraw.copyToTexture(glCanvas, this.mAnimTexture, this.getTextureWidth(), this.getTextureHeight());
            return;
        }
        final int width = this.mAnimTexture.getWidth();
        final int height = this.mAnimTexture.getHeight();
        glCanvas.beginRenderTarget(this.mAnimTexture);
        glCanvas.translate(0.0f, height);
        glCanvas.scale(1.0f, -1.0f, 1.0f);
        this.getSurfaceTexture().getTransformMatrix(this.mTextureTransformMatrix);
        this.updateTransformMatrix(this.mTextureTransformMatrix);
        glCanvas.drawTexture(this.mExtTexture, this.mTextureTransformMatrix, 0, 0, width, height);
        glCanvas.endRenderTarget();
    }

    private int getTextureHeight() {
        return super.getHeight();
    }

    private int getTextureWidth() {
        return super.getWidth();
    }

    private void setPreviewLayoutSize(final int mRenderWidth, final int mRenderHeight) {
        Log.i("CAM_ScreenNail", "preview layout size: " + mRenderWidth + "/" + mRenderHeight);
        this.mRenderWidth = mRenderWidth;
        this.mRenderHeight = mRenderHeight;
        this.updateRenderSize();
    }

    private void updateRenderSize() {
        if (!this.mEnableAspectRatioClamping) {
            this.mScaleY = 1.0f;
            this.mScaleX = 1.0f;
            this.mUncroppedRenderWidth = this.getTextureWidth();
            this.mUncroppedRenderHeight = this.getTextureHeight();
            Log.i("CAM_ScreenNail", "aspect ratio clamping disabled");
            return;
        }
        float n;
        if (this.getTextureWidth() > this.getTextureHeight()) {
            n = this.getTextureWidth() / this.getTextureHeight();
        } else {
            n = this.getTextureHeight() / this.getTextureWidth();
        }
        float n2;
        float n3;
        if (this.mRenderWidth > this.mRenderHeight) {
            n2 = Math.max(this.mRenderWidth, (int) (this.mRenderHeight * n));
            n3 = Math.max(this.mRenderHeight, (int) (this.mRenderWidth / n));
        } else {
            n2 = Math.max(this.mRenderWidth, (int) (this.mRenderHeight / n));
            n3 = Math.max(this.mRenderHeight, (int) (this.mRenderWidth * n));
        }
        this.mScaleX = this.mRenderWidth / n2;
        this.mScaleY = this.mRenderHeight / n3;
        this.mUncroppedRenderWidth = Math.round(n2);
        this.mUncroppedRenderHeight = Math.round(n3);
        Log.i("CAM_ScreenNail", "aspect ratio clamping enabled, surfaceTexture scale: " + this.mScaleX + ", " + this.mScaleY);
    }

    public void acquireSurfaceTexture() {
        synchronized (this.mLock) {
            this.mFirstFrameArrived = false;
            this.mAnimTexture = new RawTexture(this.getTextureWidth(), this.getTextureHeight(), true);
            this.mAcquireTexture = true;
            // monitorexit(this.mLock)
            this.mListener.requestRender();
        }
    }

    public void animateCapture(final int orientation) {
        synchronized (this.mLock) {
            this.mCaptureAnimManager.setOrientation(orientation);
            this.mCaptureAnimManager.animateFlashAndSlide();
            this.mListener.requestRender();
            this.mAnimState = 1;
        }
    }

    public void animateFlash(final int orientation) {
        synchronized (this.mLock) {
            this.mCaptureAnimManager.setOrientation(orientation);
            this.mCaptureAnimManager.animateFlash();
            this.mListener.requestRender();
            this.mAnimState = 1;
        }
    }

    public void animateSlide() {
        synchronized (this.mLock) {
            this.mListener.requestRender();
        }
    }

    public void animateSwitchCamera() {
        Log.v("CAM_ScreenNail", "animateSwitchCamera");
        synchronized (this.mLock) {
            if (this.mAnimState == 4) {
                this.mAnimState = 5;
            }
        }
    }

    public void copyTexture() {
        synchronized (this.mLock) {
            this.mListener.requestRender();
            this.mAnimState = 3;
        }
    }

    public void directDraw(GLCanvas canvas, int x, int y, int width, int height) {
        DrawClient draw;
        synchronized (mLock) {
            draw = mDraw;
        }
        draw.onDraw(canvas, x, y, width, height);
    }



    @Override
    public void draw(GLCanvas canvas, int x, int y, int width, int height) {
        synchronized (mLock) {
            allocateTextureIfRequested(canvas);
            if (!mVisible) mVisible = true;
            SurfaceTexture surfaceTexture = getSurfaceTexture();
            if (mDraw.requiresSurfaceTexture() && (surfaceTexture == null || !mFirstFrameArrived)) {
                return;
            }

            switch (mAnimState) {
                case ANIM_NONE:
                    directDraw(canvas, x, y, width, height);
                    break;
                case ANIM_SWITCH_COPY_TEXTURE:
                    copyPreviewTexture(canvas);
                    mSwitchAnimManager.setReviewDrawingSize(width, height);
                    mListener.onPreviewTextureCopied();
                    mAnimState = ANIM_SWITCH_DARK_PREVIEW;
                    break;
                    // The texture is ready. Fall through to draw darkened
                    // preview.
                case ANIM_SWITCH_DARK_PREVIEW:
                    break;
                case ANIM_SWITCH_WAITING_FIRST_FRAME:
                    // Consume the frame. If the buffers are full,
                    // onFrameAvailable will not be called. Animation state
                    // relies on onFrameAvailable.
                    surfaceTexture.updateTexImage();
                    mSwitchAnimManager.drawDarkPreview(canvas, x, y, width,
                            height, mAnimTexture);
                    break;
                case ANIM_SWITCH_START:
                    mSwitchAnimManager.startAnimation();
                    mAnimState = ANIM_SWITCH_RUNNING;
                    break;
                case ANIM_CAPTURE_START:
                    copyPreviewTexture(canvas);
                    mListener.onCaptureTextureCopied();
                    mCaptureAnimManager.startAnimation(x, y, width, height);
                    mAnimState = ANIM_CAPTURE_RUNNING;
                    break;
            }

            if (mAnimState == ANIM_CAPTURE_RUNNING || mAnimState == ANIM_SWITCH_RUNNING) {
                boolean drawn;
                if (mAnimState == ANIM_CAPTURE_RUNNING) {
                    if (!mFullScreen) {
                        // Skip the animation if no longer in full screen mode
                        drawn = false;
                    } else {
                        drawn = mCaptureAnimManager.drawAnimation(canvas, this, mAnimTexture);
                    }
                } else {
                    drawn = mSwitchAnimManager.drawAnimation(canvas, x, y,
                            width, height, this, mAnimTexture);
                }
                if (drawn) {
                    mListener.requestRender();
                } else {
                    // Continue to the normal draw procedure if the animation is
                    // not drawn.
                    mAnimState = ANIM_NONE;
                    directDraw(canvas, x, y, width, height);
                }
            }
            callbackIfNeeded();
        } // mLock
    }


    public void enableAspectRatioClamping() {
        this.mEnableAspectRatioClamping = true;
        this.updateRenderSize();
    }

    public float getAlpha() {
        synchronized (this.mLock) {
            return this.mAlpha;
        }
    }

    public RawTexture getAnimationTexture() {
        return this.mAnimTexture;
    }

    @Override
    public int getHeight() {
        if (this.mEnableAspectRatioClamping) {
            return this.mRenderHeight;
        }
        return this.getTextureHeight();
    }

    @Override
    public SurfaceTexture getSurfaceTexture() {
        synchronized (this.mLock) {
            SurfaceTexture surfaceTexture2;
            final SurfaceTexture surfaceTexture = surfaceTexture2 = super.getSurfaceTexture();
            if (surfaceTexture != null) {
                return surfaceTexture2;
            }
            final boolean mAcquireTexture = this.mAcquireTexture;
            surfaceTexture2 = surfaceTexture;
            if (!mAcquireTexture) {
                return surfaceTexture2;
            }
            try {
                this.mLock.wait();
                surfaceTexture2 = super.getSurfaceTexture();
                return surfaceTexture2;
            } catch (InterruptedException ex) {
                Log.w("CAM_ScreenNail", "unexpected interruption");
                surfaceTexture2 = surfaceTexture;
            }
            return surfaceTexture;
        }
    }

    public int getUncroppedRenderHeight() {
        return this.mUncroppedRenderHeight;
    }

    public int getUncroppedRenderWidth() {
        return this.mUncroppedRenderWidth;
    }

    @Override
    public int getWidth() {
        if (this.mEnableAspectRatioClamping) {
            return this.mRenderWidth;
        }
        return this.getTextureWidth();
    }

    @Override
    public void noDraw() {
        synchronized (this.mLock) {
            this.mVisible = false;
        }
    }

    @Override
    public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
        synchronized (this.mLock) {
            if (this.getSurfaceTexture() != surfaceTexture) {
                return;
            }
            this.mFirstFrameArrived = true;
            if (this.mVisible) {
                if (this.mAnimState == 5) {
                    this.mAnimState = 6;
                }
                this.mListener.requestRender();
            }
        }
    }

    @Override
    public void recycle() {
        synchronized (this.mLock) {
            this.mVisible = false;
        }
    }

    @Override
    public void releaseSurfaceTexture() {
        synchronized (this.mLock) {
            if (this.mAcquireTexture) {
                this.mAcquireTexture = false;
                this.mLock.notifyAll();
            } else {
                if (super.getSurfaceTexture() != null) {
                    super.releaseSurfaceTexture();
                }
                this.mAnimState = 0;
            }
        }
    }

    public void setAlpha(final float mAlpha) {
        synchronized (this.mLock) {
            this.mAlpha = mAlpha;
            this.mListener.requestRender();
        }
    }

    public void setDraw(final DrawClient mDraw) {
        final Object mLock = this.mLock;
        // monitorenter(mLock)
        // monitorenter(mLock)

        if (mDraw != null) {
            return;
        }
        try {
            this.mDraw = this.mDefaultDraw;

            this.mListener.requestRender();
            this.mDraw = mDraw;
            return;

        }
        // monitorexit(mLock)
        finally {
        }
        // monitorexit(mLock)
    }


    public void setFullScreen(final boolean mFullScreen) {
        synchronized (this.mLock) {
            this.mFullScreen = mFullScreen;
        }
    }

    public void setOnFrameDrawnOneShot(final Runnable mOnFrameDrawnListener) {
        synchronized (this.mLock) {
            this.mOnFrameDrawnListener = mOnFrameDrawnListener;
        }
    }

    public void setOneTimeOnFrameDrawnListener(final OnFrameDrawnListener mOneTimeFrameDrawnListener) {
        synchronized (this.mLock) {
            this.mFirstFrameArrived = false;
            this.mOneTimeFrameDrawnListener = mOneTimeFrameDrawnListener;
        }
    }

    public void setPreviewFrameLayoutSize(final int n, final int n2) {
        synchronized (this.mLock) {
            this.mSwitchAnimManager.setPreviewFrameLayoutSize(n, n2);
            this.setPreviewLayoutSize(n, n2);
        }
    }

    @Override
    public void setSize(final int mRenderWidth, final int mRenderHeight) {
        super.setSize(mRenderWidth, mRenderHeight);
        this.mEnableAspectRatioClamping = false;
        if (this.mRenderWidth == 0) {
            this.mRenderWidth = mRenderWidth;
            this.mRenderHeight = mRenderHeight;
        }
        this.updateRenderSize();
    }

    @Override
    protected void updateTransformMatrix(final float[] array) {
        super.updateTransformMatrix(array);
        Matrix.translateM(array, 0, 0.5f, 0.5f, 0.0f);
        Matrix.scaleM(array, 0, this.mScaleX, this.mScaleY, 1.0f);
        Matrix.translateM(array, 0, -0.5f, -0.5f, 0.0f);
    }

    public interface DrawClient {
        RawTexture copyToTexture(GLCanvas p0, RawTexture p1, int p2, int p3);

        void onDraw(GLCanvas p0, int p1, int p2, int p3, int p4);

        boolean requiresSurfaceTexture();
    }

    public interface Listener {
        void onCaptureTextureCopied();

        void onPreviewTextureCopied();

        void requestRender();
    }

    public interface OnFrameDrawnListener {
        void onFrameDrawn(CameraScreenNail p0);
    }
}