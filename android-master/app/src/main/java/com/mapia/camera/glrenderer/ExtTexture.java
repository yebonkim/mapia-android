package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class ExtTexture extends BasicTexture
{
    private int mTarget;

    public ExtTexture(final GLCanvas glCanvas, final int mTarget) {
        super();
        this.mId = glCanvas.getGLId().generateTexture();
        this.mTarget = mTarget;
    }

    private void uploadToCanvas(final GLCanvas associatedCanvas) {
        associatedCanvas.setTextureParameters(this);
        this.setAssociatedCanvas(associatedCanvas);
        this.mState = 1;
    }

    public int getTarget() {
        return this.mTarget;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    protected boolean onBind(final GLCanvas glCanvas) {
        if (!this.isLoaded()) {
            this.uploadToCanvas(glCanvas);
        }
        return true;
    }

    public void setId(final int mId) {
        this.mId = mId;
    }

    @Override
    public void yield() {
    }
}