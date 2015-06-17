package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import junit.framework.Assert;

import java.util.HashMap;

public abstract class UploadedTexture extends BasicTexture
{
    private static final String TAG = "Texture";
    private static final int UPLOAD_LIMIT = 100;
    private static BorderKey sBorderKey;
    private static HashMap<BorderKey, Bitmap> sBorderLines;
    private static int sUploadedCount;
    protected Bitmap mBitmap;
    private int mBorder;
    private boolean mContentValid;
    private boolean mIsUploading;
    private boolean mOpaque;
    private boolean mThrottled;

    static {
        UploadedTexture.sBorderLines = new HashMap<BorderKey, Bitmap>();
        UploadedTexture.sBorderKey = new BorderKey();
    }

    protected UploadedTexture() {
        this(false);
    }

    protected UploadedTexture(final boolean b) {
        super(null, 0, 0);
        this.mContentValid = true;
        this.mIsUploading = false;
        this.mOpaque = true;
        this.mThrottled = false;
        if (b) {
            this.setBorder(true);
            this.mBorder = 1;
        }
    }

    private void freeBitmap() {
        Assert.assertTrue(this.mBitmap != null);
        this.onFreeBitmap(this.mBitmap);
        this.mBitmap = null;
    }

    private Bitmap getBitmap() {
        if (this.mBitmap == null) {
            this.mBitmap = this.onGetBitmap();
            final int width = this.mBitmap.getWidth();
            final int mBorder = this.mBorder;
            final int height = this.mBitmap.getHeight();
            final int mBorder2 = this.mBorder;
            if (this.mWidth == -1) {
                this.setSize(width + mBorder * 2, height + mBorder2 * 2);
            }
        }
        return this.mBitmap;
    }

    private static Bitmap getBorderLine(final boolean vertical, final Bitmap.Config config, final int length) {
        final BorderKey sBorderKey = UploadedTexture.sBorderKey;
        sBorderKey.vertical = vertical;
        sBorderKey.config = config;
        sBorderKey.length = length;
        Bitmap bitmap;
        if ((bitmap = UploadedTexture.sBorderLines.get(sBorderKey)) == null) {
            Bitmap bitmap2;
            if (vertical) {
                bitmap2 = Bitmap.createBitmap(1, length, config);
            }
            else {
                bitmap2 = Bitmap.createBitmap(length, 1, config);
            }
            UploadedTexture.sBorderLines.put(sBorderKey.clone(), bitmap2);
            bitmap = bitmap2;
        }
        return bitmap;
    }

    public static void resetUploadLimit() {
        UploadedTexture.sUploadedCount = 0;
    }

    public static boolean uploadLimitReached() {
        return UploadedTexture.sUploadedCount > 100;
    }

    private void uploadToCanvas(final GLCanvas associatedCanvas) {
        final Bitmap bitmap = this.getBitmap();
        if (bitmap != null) {
            try {
                final int width = bitmap.getWidth();
                final int height = bitmap.getHeight();
                final int mBorder = this.mBorder;
                final int mBorder2 = this.mBorder;
                final int textureWidth = this.getTextureWidth();
                final int textureHeight = this.getTextureHeight();
                Assert.assertTrue(width <= textureWidth && height <= textureHeight);
                this.mId = associatedCanvas.getGLId().generateTexture();
                associatedCanvas.setTextureParameters(this);
                if (width == textureWidth && height == textureHeight) {
                    associatedCanvas.initializeTexture(this, bitmap);
                }
                else {
                    final int internalFormat = GLUtils.getInternalFormat(bitmap);
                    final int type = GLUtils.getType(bitmap);
                    final Bitmap.Config config = bitmap.getConfig();
                    associatedCanvas.initializeTextureSize(this, internalFormat, type);
                    associatedCanvas.texSubImage2D(this, this.mBorder, this.mBorder, bitmap, internalFormat, type);
                    if (this.mBorder > 0) {
                        associatedCanvas.texSubImage2D(this, 0, 0, getBorderLine(true, config, textureHeight), internalFormat, type);
                        associatedCanvas.texSubImage2D(this, 0, 0, getBorderLine(false, config, textureWidth), internalFormat, type);
                    }
                    if (this.mBorder + width < textureWidth) {
                        associatedCanvas.texSubImage2D(this, this.mBorder + width, 0, getBorderLine(true, config, textureHeight), internalFormat, type);
                    }
                    if (this.mBorder + height < textureHeight) {
                        associatedCanvas.texSubImage2D(this, 0, this.mBorder + height, getBorderLine(false, config, textureWidth), internalFormat, type);
                    }
                }
                this.freeBitmap();
                this.setAssociatedCanvas(associatedCanvas);
                this.mState = 1;
                this.mContentValid = true;
                return;
            }
            finally {
                this.freeBitmap();
            }
        }
        this.mState = -1;
        throw new RuntimeException("Texture load fail, no bitmap");
    }

    @Override
    public int getHeight() {
        if (this.mWidth == -1) {
            this.getBitmap();
        }
        return this.mHeight;
    }

    @Override
    protected int getTarget() {
        return 3553;
    }

    @Override
    public int getWidth() {
        if (this.mWidth == -1) {
            this.getBitmap();
        }
        return this.mWidth;
    }

    protected void invalidateContent() {
        if (this.mBitmap != null) {
            this.freeBitmap();
        }
        this.mContentValid = false;
        this.mWidth = -1;
        this.mHeight = -1;
    }

    public boolean isContentValid() {
        return this.isLoaded() && this.mContentValid;
    }

    @Override
    public boolean isOpaque() {
        return this.mOpaque;
    }

    public boolean isUploading() {
        return this.mIsUploading;
    }

    @Override
    protected boolean onBind(final GLCanvas glCanvas) {
        this.updateContent(glCanvas);
        return this.isContentValid();
    }

    protected abstract void onFreeBitmap(final Bitmap p0);

    protected abstract Bitmap onGetBitmap();

    @Override
    public void recycle() {
        super.recycle();
        if (this.mBitmap != null) {
            this.freeBitmap();
        }
    }

    protected void setIsUploading(final boolean mIsUploading) {
        this.mIsUploading = mIsUploading;
    }

    public void setOpaque(final boolean mOpaque) {
        this.mOpaque = mOpaque;
    }

    protected void setThrottled(final boolean mThrottled) {
        this.mThrottled = mThrottled;
    }

    public void updateContent(final GLCanvas glCanvas) {
        if (!this.isLoaded()) {
            if (!this.mThrottled || ++UploadedTexture.sUploadedCount <= 100) {
                this.uploadToCanvas(glCanvas);
            }
        }
        else if (!this.mContentValid) {
            final Bitmap bitmap = this.getBitmap();
            glCanvas.texSubImage2D(this, this.mBorder, this.mBorder, bitmap, GLUtils.getInternalFormat(bitmap), GLUtils.getType(bitmap));
            this.freeBitmap();
            this.mContentValid = true;
        }
    }

    private static class BorderKey implements Cloneable
    {
        public Bitmap.Config config;
        public int length;
        public boolean vertical;

        public BorderKey clone() {
            try {
                return (BorderKey)super.clone();
            }
            catch (CloneNotSupportedException ex) {
                throw new AssertionError((Object)ex);
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (o instanceof BorderKey) {
                final BorderKey borderKey = (BorderKey)o;
                if (this.vertical == borderKey.vertical && this.config == borderKey.config && this.length == borderKey.length) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            final int n = this.config.hashCode() ^ this.length;
            if (this.vertical) {
                return n;
            }
            return -n;
        }
    }
}