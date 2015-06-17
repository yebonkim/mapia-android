package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.util.Log;

import java.util.Iterator;
import java.util.WeakHashMap;

public abstract class BasicTexture implements Texture
{
    private static final int MAX_TEXTURE_SIZE = 4096;
    protected static final int STATE_ERROR = -1;
    protected static final int STATE_LOADED = 1;
    protected static final int STATE_UNLOADED = 0;
    private static final String TAG = "BasicTexture";
    protected static final int UNSPECIFIED = -1;
    private static WeakHashMap<BasicTexture, Object> sAllTextures;
    private static ThreadLocal sInFinalizer;
    protected GLCanvas mCanvasRef;
    private boolean mHasBorder;
    protected int mHeight;
    protected int mId;
    protected int mState;
    protected int mTextureHeight;
    protected int mTextureWidth;
    protected int mWidth;

    static {
        BasicTexture.sAllTextures = new WeakHashMap<BasicTexture, Object>();
        BasicTexture.sInFinalizer = new ThreadLocal();
    }

    protected BasicTexture() {
        this(null, 0, 0);
    }

    protected BasicTexture(final GLCanvas associatedCanvas, final int mId, final int mState) {
        super();
        this.mId = -1;
        this.mWidth = -1;
        this.mHeight = -1;
        this.mCanvasRef = null;
        this.setAssociatedCanvas(associatedCanvas);
        this.mId = mId;
        this.mState = mState;
        synchronized (BasicTexture.sAllTextures) {
            BasicTexture.sAllTextures.put(this, null);
        }
    }

    private void freeResource() {
        final GLCanvas mCanvasRef = this.mCanvasRef;
        if (mCanvasRef != null && this.mId != -1) {
            mCanvasRef.unloadTexture(this);
            this.mId = -1;
        }
        this.mState = 0;
        this.setAssociatedCanvas(null);
    }

    public static boolean inFinalizer() {
        return BasicTexture.sInFinalizer.get() != null;
    }

    public static void invalidateAllTextures() {
        synchronized (BasicTexture.sAllTextures) {
            for (final BasicTexture basicTexture : BasicTexture.sAllTextures.keySet()) {
                basicTexture.mState = 0;
                basicTexture.setAssociatedCanvas(null);
            }
        }
        // monitorexit(weakHashMap)
    }
    // monitorexit(weakHashMap)

    public static void yieldAllTextures() {
        synchronized (BasicTexture.sAllTextures) {
            final Iterator<BasicTexture> iterator = BasicTexture.sAllTextures.keySet().iterator();
            while (iterator.hasNext()) {
                iterator.next().yield();
            }
        }
        // monitorexit(weakHashMap)
    }
    // monitorexit(weakHashMap)

    @Override
    public void draw(final GLCanvas glCanvas, final int n, final int n2) {
        glCanvas.drawTexture(this, n, n2, this.getWidth(), this.getHeight());
    }

    @Override
    public void draw(final GLCanvas glCanvas, final int n, final int n2, final int n3, final int n4) {
        glCanvas.drawTexture(this, n, n2, n3, n4);
    }

    @Override
    protected void finalize() {
        BasicTexture.sInFinalizer.set(BasicTexture.class);
        this.recycle();
        BasicTexture.sInFinalizer.set(null);
    }

    @Override
    public int getHeight() {
        return this.mHeight;
    }

    public int getId() {
        return this.mId;
    }

    protected abstract int getTarget();

    public int getTextureHeight() {
        return this.mTextureHeight;
    }

    public int getTextureWidth() {
        return this.mTextureWidth;
    }

    @Override
    public int getWidth() {
        return this.mWidth;
    }

    public boolean hasBorder() {
        return this.mHasBorder;
    }

    public boolean isFlippedVertically() {
        return false;
    }

    public boolean isLoaded() {
        return this.mState == 1;
    }

    protected abstract boolean onBind(final GLCanvas p0);

    public void recycle() {
        this.freeResource();
    }

    protected void setAssociatedCanvas(final GLCanvas mCanvasRef) {
        this.mCanvasRef = mCanvasRef;
    }

    protected void setBorder(final boolean mHasBorder) {
        this.mHasBorder = mHasBorder;
    }

    public void setSize(int mTextureHeight, final int mHeight) {
        this.mWidth = mTextureHeight;
        this.mHeight = mHeight;
        if (mTextureHeight > 0) {
            mTextureHeight = com.mapia.camera.common.Utils.nextPowerOf2(mTextureHeight);
        }
        else {
            mTextureHeight = 0;
        }
        this.mTextureWidth = mTextureHeight;
        if (mHeight > 0) {
            mTextureHeight = com.mapia.camera.common.Utils.nextPowerOf2(mHeight);
        }
        else {
            mTextureHeight = 0;
        }
        this.mTextureHeight = mTextureHeight;
        if (this.mTextureWidth > 4096 || this.mTextureHeight > 4096) {
            Log.w("BasicTexture", String.format("texture is too large: %d x %d", this.mTextureWidth, this.mTextureHeight), (Throwable) new Exception());
        }
    }

    public void yield() {
        this.freeResource();
    }
}