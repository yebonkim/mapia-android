package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;

import com.mapia.camera.common.ApiHelper;
import com.mapia.camera.glrenderer.ExtTexture;
import com.mapia.camera.glrenderer.GLCanvas;


@TargetApi(11)
public abstract class SurfaceTextureScreenNail implements ScreenNail, SurfaceTexture.OnFrameAvailableListener
{
    private static final int GL_TEXTURE_EXTERNAL_OES = 36197;
    private static final String TAG = "SurfaceTextureScreenNail";
    protected ExtTexture mExtTexture;
    private boolean mHasTexture;
    private int mHeight;
    private SurfaceTexture mSurfaceTexture;
    private float[] mTransform;
    private int mWidth;

    public SurfaceTextureScreenNail() {
        super();
        this.mTransform = new float[16];
        this.mHasTexture = false;
    }

    @TargetApi(14)
    private static void releaseSurfaceTexture(final SurfaceTexture surfaceTexture) {
        surfaceTexture.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)null);
        if (ApiHelper.HAS_RELEASE_SURFACE_TEXTURE) {
            surfaceTexture.release();
        }
    }

    @TargetApi(15)
    private static void setDefaultBufferSize(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        if (ApiHelper.HAS_SET_DEFALT_BUFFER_SIZE) {
            surfaceTexture.setDefaultBufferSize(n, n2);
        }
    }

    public void acquireSurfaceTexture(final GLCanvas glCanvas) {
        (this.mExtTexture = new ExtTexture(glCanvas, 36197)).setSize(this.mWidth, this.mHeight);
        setDefaultBufferSize(this.mSurfaceTexture = new SurfaceTexture(this.mExtTexture.getId()), this.mWidth, this.mHeight);
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        synchronized (this) {
            this.mHasTexture = true;
        }
    }

    @Override
    public void draw(final GLCanvas glCanvas, final int n, final int n2, final int n3, final int n4) {
        synchronized (this) {
            if (!this.mHasTexture) {
                return;
            }
            this.mSurfaceTexture.updateTexImage();
            this.mSurfaceTexture.getTransformMatrix(this.mTransform);
            glCanvas.save(2);
            final int n5 = n + n3 / 2;
            final int n6 = n2 + n4 / 2;
            glCanvas.translate(n5, n6);
            glCanvas.scale(1.0f, -1.0f, 1.0f);
            glCanvas.translate(-n5, -n6);
            this.updateTransformMatrix(this.mTransform);
            glCanvas.drawTexture(this.mExtTexture, this.mTransform, n, n2, n3, n4);
            glCanvas.restore();
        }
    }

    @Override
    public void draw(final GLCanvas glCanvas, final RectF rectF, final RectF rectF2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getHeight() {
        return this.mHeight;
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mSurfaceTexture;
    }

    @Override
    public int getWidth() {
        return this.mWidth;
    }

    @Override
    public abstract void noDraw();

    public abstract void onFrameAvailable(final SurfaceTexture p0);

    @Override
    public abstract void recycle();

    public void releaseSurfaceTexture() {
        synchronized (this) {
            this.mHasTexture = false;
            // monitorexit(this)
            this.mExtTexture.recycle();
            this.mExtTexture = null;
            releaseSurfaceTexture(this.mSurfaceTexture);
            this.mSurfaceTexture = null;
        }
    }

    public void resizeTexture() {
        if (this.mExtTexture != null) {
            this.mExtTexture.setSize(this.mWidth, this.mHeight);
            setDefaultBufferSize(this.mSurfaceTexture, this.mWidth, this.mHeight);
        }
    }

    public void setSize(final int mWidth, final int mHeight) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
    }

    protected void updateTransformMatrix(final float[] array) {
    }
}