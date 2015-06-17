package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.util.Log;

public class RawTexture extends BasicTexture
{
    private static final String TAG = "RawTexture";
    private boolean mIsFlipped;
    private final boolean mOpaque;

    public RawTexture(final int n, final int n2, final boolean mOpaque) {
        super();
        this.mOpaque = mOpaque;
        this.setSize(n, n2);
    }

    @Override
    protected int getTarget() {
        return 3553;
    }

    @Override
    public boolean isFlippedVertically() {
        return this.mIsFlipped;
    }

    @Override
    public boolean isOpaque() {
        return this.mOpaque;
    }

    @Override
    protected boolean onBind(final GLCanvas glCanvas) {
        if (this.isLoaded()) {
            return true;
        }
        Log.w("RawTexture", "lost the content due to context change");
        return false;
    }

    protected void prepare(final GLCanvas associatedCanvas) {
        this.mId = associatedCanvas.getGLId().generateTexture();
        associatedCanvas.initializeTextureSize(this, 6408, 5121);
        associatedCanvas.setTextureParameters(this);
        this.mState = 1;
        this.setAssociatedCanvas(associatedCanvas);
    }

    public void setIsFlippedVertically(final boolean mIsFlipped) {
        this.mIsFlipped = mIsFlipped;
    }

    @Override
    public void yield() {
    }
}