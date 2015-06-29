package com.mapia.camera.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.mapia.R;
import com.mapia.camera.anim.CanvasAnimation;
import com.mapia.camera.common.ApiHelper;
import com.mapia.camera.common.Utils;
import com.mapia.camera.glrenderer.BasicTexture;
import com.mapia.camera.glrenderer.GLCanvas;
import com.mapia.camera.glrenderer.GLES11Canvas;
import com.mapia.camera.glrenderer.GLES20Canvas;
import com.mapia.camera.glrenderer.UploadedTexture;
import com.mapia.camera.util.MotionEventHelper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class GLRootView extends GLSurfaceView implements GLSurfaceView.Renderer, GLRoot
{
    private static final boolean DEBUG_DRAWING_STAT = false;
    private static final boolean DEBUG_FPS = false;
    private static final boolean DEBUG_INVALIDATE = false;
    private static final boolean DEBUG_PROFILE = false;
    private static final boolean DEBUG_PROFILE_SLOW_ONLY = false;
    private static final int FLAG_INITIALIZED = 1;
    private static final int FLAG_NEED_LAYOUT = 2;
    private static final String TAG = "GLRootView";
    private final ArrayList<CanvasAnimation> mAnimations;
    private GLCanvas mCanvas;
    private int mCompensation;
    private android.graphics.Matrix mCompensationMatrix;
    private GLView mContentView;
    private int mDisplayRotation;
    private boolean mFirstDraw;
    private int mFlags;
    private int mFrameCount;
    private long mFrameCountingStart;
    private boolean mFreeze;
    private final Condition mFreezeCondition;
    private GL11 mGL;
    private final ArrayDeque<GLRoot.OnGLIdleListener> mIdleListeners;
    private final IdleRunner mIdleRunner;
    private boolean mInDownState;
    private int mInvalidateColor;
    private long mLastDrawFinishTime;
    private OrientationSource mOrientationSource;
    private final ReentrantLock mRenderLock;
    private volatile boolean mRenderRequested;
    private Runnable mRequestRenderOnAnimationFrame;

    public GLRootView(final Context context) {
        this(context, null);
    }

    public GLRootView(final Context context, final AttributeSet set) {
        super(context, set);
        int eglContextClientVersion = 2;
        this.mFrameCount = 0;
        this.mFrameCountingStart = 0L;
        this.mInvalidateColor = 0;
        this.mCompensationMatrix = new android.graphics.Matrix();
        this.mFlags = 2;
        this.mRenderRequested = false;
        this.mAnimations = new ArrayList<CanvasAnimation>();
        this.mIdleListeners = new ArrayDeque<OnGLIdleListener>();
        this.mIdleRunner = new IdleRunner();
        this.mRenderLock = new ReentrantLock();
        this.mFreezeCondition = this.mRenderLock.newCondition();
        this.mInDownState = false;
        this.mFirstDraw = true;
        this.mRequestRenderOnAnimationFrame = new Runnable() {
            @Override
            public void run() {
                GLRootView.this.superRequestRender();
            }
        };
        this.mFlags |= 0x1;
        this.setBackgroundDrawable((Drawable)null);
        if (!ApiHelper.HAS_GLES20_REQUIRED) {
            eglContextClientVersion = 1;
        }
        this.setEGLContextClientVersion(eglContextClientVersion);
        if (true){//ApiHelper.USE_888_PIXEL_FORMAT) {
            this.setEGLConfigChooser(8, 8, 8, 0, 0, 0);
        }
        else {
            this.setEGLConfigChooser(5, 6, 5, 0, 0, 0);
        }
        this.setRenderer((GLSurfaceView.Renderer)this);
        if (true){//ApiHelper.USE_888_PIXEL_FORMAT) {
            this.getHolder().setFormat(3);
            return;
        }
        this.getHolder().setFormat(4);
    }

    private void checkAndUpdateTextureSize() {
        GLES20.glGetIntegerv(3379, new int[1], 0);
    }

    private void layoutContentPane() {
        this.mFlags &= 0xFFFFFFFD;
        final int width = this.getWidth();
        final int height = this.getHeight();
        int displayRotation;
        int compensation;
        if (this.mOrientationSource != null) {
            displayRotation = this.mOrientationSource.getDisplayRotation();
            compensation = this.mOrientationSource.getCompensation();
        }
        else {
            displayRotation = 0;
            compensation = 0;
        }
        if (this.mCompensation != compensation) {
            this.mCompensation = compensation;
            if (this.mCompensation % 180 != 0) {
//                this.mCompensationMatrix.setRotate((float)this.mCompensation);
//                this.mCompensationMatrix.preTranslate((float)(-width / 2), (float)(-height / 2));
//                this.mCompensationMatrix.postTranslate((float)(height / 2), (float)(width / 2));
            }
            else {
//                this.mCompensationMatrix.setRotate((float)this.mCompensation, (float)(width / 2), (float)(height / 2));
            }
        }
        this.mDisplayRotation = displayRotation;
        int n = height;
        int n2 = width;
        if (this.mCompensation % 180 != 0) {
            n2 = height;
            n = width;
        }
        Log.i("GLRootView", "layout content pane " + n2 + "x" + n + " (compensation " + this.mCompensation + ")");
        if (this.mContentView != null && n2 != 0 && n != 0) {
            this.mContentView.layout(0, 0, n2, n);
        }
    }

    private void onDrawFrameLocked(final GL10 gl10) {
        this.mCanvas.deleteRecycledResources();
        UploadedTexture.resetUploadLimit();
        this.mRenderRequested = false;
        if ((this.mOrientationSource != null && this.mDisplayRotation != this.mOrientationSource.getDisplayRotation()) || (this.mFlags & 0x2) != 0x0) {
            this.layoutContentPane();
        }
        this.mCanvas.save(-1);
        this.rotateCanvas(-this.mCompensation);
        if (this.mContentView != null) {
            this.mContentView.render(this.mCanvas);
        }
        this.mCanvas.restore();
        if (!this.mAnimations.isEmpty()) {
            final long value = AnimationTime.get();
            for (int i = 0; i < this.mAnimations.size(); ++i) {
                this.mAnimations.get(i).setStartTime(value);
            }
            this.mAnimations.clear();
        }
        if (UploadedTexture.uploadLimitReached()) {
            this.requestRender();
        }
        synchronized (this.mIdleListeners) {
            if (!this.mIdleListeners.isEmpty()) {
                this.mIdleRunner.enable();
            }
        }
    }

    private void outputFps() {
        final long nanoTime = System.nanoTime();
        if (this.mFrameCountingStart == 0L) {
            this.mFrameCountingStart = nanoTime;
        }
        else if (nanoTime - this.mFrameCountingStart > 1000000000L) {
            this.mFrameCountingStart = nanoTime;
            this.mFrameCount = 0;
        }
        ++this.mFrameCount;
    }

    private void rotateCanvas(final int n) {
        if (n == 0) {
            return;
        }
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int n2 = width / 2;
        final int n3 = height / 2;
        this.mCanvas.translate(n2, n3);
        this.mCanvas.rotate(n, 0.0f, 0.0f, 1.0f);
        if (n % 180 != 0) {
            this.mCanvas.translate(-n3, -n2);
            return;
        }
        this.mCanvas.translate(-n2, -n3);
    }

    private void superRequestRender() {
        super.requestRender();
    }

    public void addOnGLIdleListener(final GLRoot.OnGLIdleListener onGLIdleListener) {
        synchronized (this.mIdleListeners) {
            this.mIdleListeners.addLast(onGLIdleListener);
            this.mIdleRunner.enable();
        }
    }

    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        final boolean b = false;
        if (!this.isEnabled()) {
            return false;
        }
        final int action = motionEvent.getAction();
        Label_0108: {
            if (action != 3 && action != 1) {
                break Label_0108;
            }
            this.mInDownState = false;
            MotionEvent transformEvent = null;

            transformEvent = motionEvent;

            if (this.mCompensation != 0) {
                transformEvent = MotionEventHelper.transformEvent(motionEvent, this.mCompensationMatrix);
            }
            this.mRenderLock.lock();
            boolean b2 = b;
            try {
                if (this.mContentView != null) {
                    b2 = b;
                    if (this.mContentView.dispatchTouchEvent(transformEvent)) {
                        b2 = true;
                    }
                }
                if (action == 0 && b2) {
                    this.mInDownState = true;
                }
                return b2;
                // iftrue(Label_0032:, this.mInDownState || action == 0)

            }
            finally {
                this.mRenderLock.unlock();
            }
        }
        return false;
    }

    protected void finalize() throws Throwable {
        try {
            this.unfreeze();
        }
        finally {
            super.finalize();
        }
    }

    public void freeze() {
        this.mRenderLock.lock();
        this.mFreeze = true;
        this.mRenderLock.unlock();
    }

    public int getCompensation() {
        return this.mCompensation;
    }

    public android.graphics.Matrix getCompensationMatrix() {
        return this.mCompensationMatrix;
    }

    public int getDisplayRotation() {
        return this.mDisplayRotation;
    }

    public void lockRenderThread() {
        this.mRenderLock.lock();
    }

    protected void onDetachedFromWindow() {
        this.unfreeze();
        super.onDetachedFromWindow();
    }

    public void onDrawFrame(final GL10 gl10) {
        AnimationTime.update();
        this.mRenderLock.lock();
        while (this.mFreeze) {
            this.mFreezeCondition.awaitUninterruptibly();
        }
        try {
            this.onDrawFrameLocked(gl10);
            this.mRenderLock.unlock();
            if (this.mFirstDraw) {
                this.mFirstDraw = false;
                this.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        GLRootView.this.getRootView().findViewById(R.id.gl_root_cover).setVisibility(View.INVISIBLE);
                    }
                });
            }
            GLES20.glFlush();
        }
        finally {
//            this.mRenderLock.unlock();
        }
    }

    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (b) {
            this.requestLayoutContentPane();
        }
    }

    public void onPause() {
        this.unfreeze();
        super.onPause();
    }

    public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
        Log.i("GLRootView", "onSurfaceChanged: " + n + "x" + n2 + ", gl10: " + gl10.toString());
//        Process.setThreadPriority(-4);
        Utils.assertTrue(this.mGL == (GL11) gl10);
        this.mCanvas.setSize(n, n2);
    }


    public void onSurfaceCreated(GL10 gl10, javax.microedition.khronos.egl.EGLConfig config){//onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        final GL11 mgl = (GL11)gl10;
        if (this.mGL != null) {
            Log.i("GLRootView", "GLObject has changed from " + this.mGL + " to " + mgl);
        }
        this.mRenderLock.lock();
        try {
            this.mGL = mgl;
            GLCanvas mCanvas;
            if (ApiHelper.HAS_GLES20_REQUIRED) {
                mCanvas = new GLES20Canvas();
            }
            else {
                mCanvas = new GLES11Canvas(mgl);
            }
            this.mCanvas = mCanvas;
            BasicTexture.invalidateAllTextures();
            this.mRenderLock.unlock();
            this.setRenderMode(0);
            this.checkAndUpdateTextureSize();
        }
        finally {
//            this.mRenderLock.unlock();
        }
    }

    public void registerLaunchedAnimation(final CanvasAnimation canvasAnimation) {
        this.mAnimations.add(canvasAnimation);
    }

    public void requestLayoutContentPane() {
        this.mRenderLock.lock();
        try {
            if (this.mContentView == null || (this.mFlags & 0x2) != 0x0) {
                return;
            }
            if ((this.mFlags & 0x1) == 0x0) {
                return;
            }
            this.mFlags |= 0x2;
            this.requestRender();
        }
        finally {
            this.mRenderLock.unlock();
        }
    }

    public void requestRender() {
        if (this.mRenderRequested) {
            return;
        }
        this.mRenderRequested = true;
        if (ApiHelper.HAS_POST_ON_ANIMATION) {
            this.postOnAnimation(this.mRequestRenderOnAnimationFrame);
            return;
        }
        super.requestRender();
    }

    public void requestRenderForced() {
        this.superRequestRender();
    }

    public void setContentPane(final GLView mContentView) {
        if (this.mContentView != mContentView) {
            if (this.mContentView != null) {
                if (this.mInDownState) {
                    final long uptimeMillis = SystemClock.uptimeMillis();
                    final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                    this.mContentView.dispatchTouchEvent(obtain);
                    obtain.recycle();
                    this.mInDownState = false;
                }
                this.mContentView.detachFromRoot();
                BasicTexture.yieldAllTextures();
            }
            if ((this.mContentView = mContentView) != null) {
                mContentView.attachToRoot(this);
                this.requestLayoutContentPane();
            }
        }
    }

    @TargetApi(16)
    public void setLightsOutMode(final boolean b) {
        if (!ApiHelper.HAS_SET_SYSTEM_UI_VISIBILITY) {
            return;
        }
        int systemUiVisibility = 0;
        if (b) {
            systemUiVisibility = 1;
            if (ApiHelper.HAS_VIEW_SYSTEM_UI_FLAG_LAYOUT_STABLE) {
                systemUiVisibility = (0x1 | 0x104);
            }
        }
        this.setSystemUiVisibility(systemUiVisibility);
    }

    public void setOrientationSource(final OrientationSource mOrientationSource) {
        this.mOrientationSource = mOrientationSource;
    }

    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
        this.unfreeze();
        super.surfaceChanged(surfaceHolder, n, n2, n3);
    }

    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        this.unfreeze();
        super.surfaceCreated(surfaceHolder);
    }

    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        this.unfreeze();
        super.surfaceDestroyed(surfaceHolder);
    }

    public void unfreeze() {
        this.mRenderLock.lock();
        this.mFreeze = false;
        this.mFreezeCondition.signalAll();
        this.mRenderLock.unlock();
    }

    public void unlockRenderThread() {
        this.mRenderLock.unlock();
    }

    private class IdleRunner implements Runnable
    {
        private boolean mActive;

        private IdleRunner() {
            super();
            this.mActive = false;
        }

        public void enable() {
            if (this.mActive) {
                return;
            }
            this.mActive = true;
            GLRootView.this.queueEvent((Runnable)this);
        }

        @Override
        public void run() {
            synchronized (mIdleListeners)
            {
                mActive = false;
                if (!mIdleListeners.isEmpty())
                {
                    return;
                }
            }
            GLRoot.OnGLIdleListener onglidlelistener = (GLRoot.OnGLIdleListener)mIdleListeners.removeFirst();

            mRenderLock.lock();
            boolean flag = onglidlelistener.onGLIdle(mCanvas, mRenderRequested);
            mRenderLock.unlock();
            if (!flag)
            {
                return;
            }
            mIdleListeners.addLast(onglidlelistener);
            if (!mRenderRequested && !mIdleListeners.isEmpty())
            {
                enable();
            }
        }


    }
}