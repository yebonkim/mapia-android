package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.mapia.camera.common.Utils;
import com.mapia.camera.util.IntArray;

import junit.framework.Assert;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import javax.microedition.khronos.opengles.GL11ExtensionPack;


public class GLES11Canvas implements GLCanvas
{
    private static final float[] BOX_COORDINATES;
    private static final int MSCALE_X = 0;
    private static final int MSCALE_Y = 5;
    private static final int MSKEW_X = 4;
    private static final int MSKEW_Y = 1;
    private static final int OFFSET_DRAW_LINE = 4;
    private static final int OFFSET_DRAW_RECT = 6;
    private static final int OFFSET_FILL_RECT = 0;
    private static final float OPAQUE_ALPHA = 0.95f;
    private static final String TAG = "GLCanvasImp";
    private static GLId mGLId;
    private static float[] sCropRect;
    private float mAlpha;
    private boolean mBlendEnabled;
    private int mBoxCoords;
    int mCountDrawLine;
    int mCountDrawMesh;
    int mCountFillRect;
    int mCountTextureOES;
    int mCountTextureRect;
    private final IntArray mDeleteBuffers;
    private final RectF mDrawTextureSourceRect;
    private final RectF mDrawTextureTargetRect;
    private int[] mFrameBuffer;
    private GL11 mGL;
    private GLState mGLState;
    private final float[] mMapPointsBuffer;
    private final float[] mMatrixValues;
    private ConfigState mRecycledRestoreAction;
    private final ArrayList<ConfigState> mRestoreStack;
    private int mScreenHeight;
    private int mScreenWidth;
    private final ArrayList<RawTexture> mTargetStack;
    private RawTexture mTargetTexture;
    private final float[] mTempMatrix;
    private final float[] mTextureColor;
    private final float[] mTextureMatrixValues;
    private final IntArray mUnboundTextures;

    static {
        BOX_COORDINATES = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f };
        GLES11Canvas.sCropRect = new float[4];
        GLES11Canvas.mGLId = new GLES11IdImpl();
    }

    public GLES11Canvas(final GL11 mgl) {
        super();
        this.mMatrixValues = new float[16];
        this.mTextureMatrixValues = new float[16];
        this.mMapPointsBuffer = new float[4];
        this.mTextureColor = new float[4];
        this.mTargetStack = new ArrayList<RawTexture>();
        this.mRestoreStack = new ArrayList<ConfigState>();
        this.mDrawTextureSourceRect = new RectF();
        this.mDrawTextureTargetRect = new RectF();
        this.mTempMatrix = new float[32];
        this.mUnboundTextures = new IntArray();
        this.mDeleteBuffers = new IntArray();
        this.mBlendEnabled = true;
        this.mFrameBuffer = new int[1];
        this.mGL = mgl;
        this.mGLState = new GLState(mgl);
        final FloatBuffer floatBuffer = allocateDirectNativeOrderBuffer(GLES11Canvas.BOX_COORDINATES.length * 32 / 8).asFloatBuffer();
        floatBuffer.put(GLES11Canvas.BOX_COORDINATES, 0, GLES11Canvas.BOX_COORDINATES.length).position(0);
        final int[] array = { 0 };
        GLES11Canvas.mGLId.glGenBuffers(1, array, 0);
        mgl.glBindBuffer(34962, this.mBoxCoords = array[0]);
        mgl.glBufferData(34962, floatBuffer.capacity() * 4, (Buffer)floatBuffer, 35044);
        mgl.glVertexPointer(2, 5126, 0, 0);
        mgl.glTexCoordPointer(2, 5126, 0, 0);
        mgl.glClientActiveTexture(33985);
        mgl.glTexCoordPointer(2, 5126, 0, 0);
        mgl.glClientActiveTexture(33984);
        mgl.glEnableClientState(32888);
    }

    private static ByteBuffer allocateDirectNativeOrderBuffer(final int n) {
        return ByteBuffer.allocateDirect(n).order(ByteOrder.nativeOrder());
    }

    private boolean bindTexture(final BasicTexture basicTexture) {
        if (!basicTexture.onBind(this)) {
            return false;
        }
        final int target = basicTexture.getTarget();
        this.mGLState.setTextureTarget(target);
        this.mGL.glBindTexture(target, basicTexture.getId());
        return true;
    }

    private static void checkFramebufferStatus(final GL11ExtensionPack gl11ExtensionPack) {
        final int glCheckFramebufferStatusOES = gl11ExtensionPack.glCheckFramebufferStatusOES(36160);
        if (glCheckFramebufferStatusOES == 36053) {
            return;
        }
        String s = "";
        switch (glCheckFramebufferStatusOES) {
            default: {
                s = s;
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatusOES));
            }
            case 36057: {
                s = "FRAMEBUFFER_INCOMPLETE_DIMENSIONS";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatusOES));
            }
            case 36061: {
                s = "FRAMEBUFFER_UNSUPPORTED";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatusOES));
            }
            case 36060: {
                s = "FRAMEBUFFER_READ_BUFFER";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatusOES));
            }
            case 36059: {
                s = "FRAMEBUFFER_DRAW_BUFFER";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatusOES));
            }
            case 36055: {
                s = "FRAMEBUFFER_MISSING_ATTACHMENT";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatusOES));
            }
            case 36054: {
                s = "FRAMEBUFFER_ATTACHMENT";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatusOES));
            }
            case 36058: {
                s = "FRAMEBUFFER_FORMATS";
            }
            case 36056: {
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatusOES));
            }
        }
    }

    private static void convertCoordinate(final RectF rectF, final RectF rectF2, final BasicTexture basicTexture) {
        final int width = basicTexture.getWidth();
        final int height = basicTexture.getHeight();
        final int textureWidth = basicTexture.getTextureWidth();
        final int textureHeight = basicTexture.getTextureHeight();
        rectF.left /= textureWidth;
        rectF.right /= textureWidth;
        rectF.top /= textureHeight;
        rectF.bottom /= textureHeight;
        final float right = width / textureWidth;
        if (rectF.right > right) {
            rectF2.right = rectF2.left + rectF2.width() * (right - rectF.left) / rectF.width();
            rectF.right = right;
        }
        final float bottom = height / textureHeight;
        if (rectF.bottom > bottom) {
            rectF2.bottom = rectF2.top + rectF2.height() * (bottom - rectF.top) / rectF.height();
            rectF.bottom = bottom;
        }
    }

    private void drawBoundTexture(final BasicTexture basicTexture, int n, int n2, int n3, int n4) {
        if (isMatrixRotatedOrFlipped(this.mMatrixValues)) {
            if (basicTexture.hasBorder()) {
                this.setTextureCoords(1.0f / basicTexture.getTextureWidth(), 1.0f / basicTexture.getTextureHeight(), (basicTexture.getWidth() - 1.0f) / basicTexture.getTextureWidth(), (basicTexture.getHeight() - 1.0f) / basicTexture.getTextureHeight());
            }
            else {
                this.setTextureCoords(0.0f, 0.0f, basicTexture.getWidth() / basicTexture.getTextureWidth(), basicTexture.getHeight() / basicTexture.getTextureHeight());
            }
            this.textureRect(n, n2, n3, n4);
        }
        else {
            final float[] mapPoints = this.mapPoints(this.mMatrixValues, n, n2 + n4, n + n3, n2);
            n = (int)(mapPoints[0] + 0.5f);
            n2 = (int)(mapPoints[1] + 0.5f);
            n3 = (int)(mapPoints[2] + 0.5f) - n;
            n4 = (int)(mapPoints[3] + 0.5f) - n2;
            if (n3 > 0 && n4 > 0) {
                ((GL11Ext)this.mGL).glDrawTexiOES(n, n2, 0, n3, n4);
                ++this.mCountTextureOES;
            }
        }
    }

    private void drawMixed(final BasicTexture basicTexture, final int n, final float n2, final int n3, final int n4, final int n5, final int n6, final float n7) {
        if (n2 <= 0.01f) {
            this.drawTexture(basicTexture, n3, n4, n5, n6, n7);
        }
        else {
            if (n2 >= 1.0f) {
                this.fillRect(n3, n4, n5, n6, n);
                return;
            }
            this.mGLState.setBlendEnabled(this.mBlendEnabled && (!basicTexture.isOpaque() || !Utils.isOpaque(n) || n7 < 0.95f));
            final GL11 mgl = this.mGL;
            if (this.bindTexture(basicTexture)) {
                this.mGLState.setTexEnvMode(34160);
                this.setMixedColor(n, n2, n7);
                this.drawBoundTexture(basicTexture, n3, n4, n5, n6);
                this.mGLState.setTexEnvMode(7681);
            }
        }
    }

    private void drawTexture(final BasicTexture basicTexture, final int n, final int n2, final int n3, final int n4, final float textureAlpha) {
        if (n3 > 0 && n4 > 0) {
            this.mGLState.setBlendEnabled(this.mBlendEnabled && (!basicTexture.isOpaque() || textureAlpha < 0.95f));
            if (this.bindTexture(basicTexture)) {
                this.mGLState.setTextureAlpha(textureAlpha);
                this.drawBoundTexture(basicTexture, n, n2, n3, n4);
            }
        }
    }

    private void freeRestoreConfig(final ConfigState mRecycledRestoreAction) {
        mRecycledRestoreAction.mNextFree = this.mRecycledRestoreAction;
        this.mRecycledRestoreAction = mRecycledRestoreAction;
    }

    private static boolean isMatrixRotatedOrFlipped(final float[] array) {
        boolean b = false;
        if (Math.abs(array[4]) > 1.0E-5f || Math.abs(array[1]) > 1.0E-5f || array[0] < -1.0E-5f || array[5] > 1.0E-5f) {
            b = true;
        }
        return b;
    }

    private float[] mapPoints(final float[] array, final int n, final int n2, final int n3, final int n4) {
        final float[] mMapPointsBuffer = this.mMapPointsBuffer;
        final float n5 = array[0];
        final float n6 = n;
        final float n7 = array[4];
        final float n8 = n2;
        final float n9 = array[12];
        final float n10 = array[1];
        final float n11 = n;
        final float n12 = array[5];
        final float n13 = n2;
        final float n14 = array[13];
        final float n15 = array[3] * n + array[7] * n2 + array[15];
        mMapPointsBuffer[0] = (n5 * n6 + n7 * n8 + n9) / n15;
        mMapPointsBuffer[1] = (n10 * n11 + n12 * n13 + n14) / n15;
        final float n16 = array[0];
        final float n17 = n3;
        final float n18 = array[4];
        final float n19 = n4;
        final float n20 = array[12];
        final float n21 = array[1];
        final float n22 = n3;
        final float n23 = array[5];
        final float n24 = n4;
        final float n25 = array[13];
        final float n26 = array[3] * n3 + array[7] * n4 + array[15];
        mMapPointsBuffer[2] = (n16 * n17 + n18 * n19 + n20) / n26;
        mMapPointsBuffer[3] = (n21 * n22 + n23 * n24 + n25) / n26;
        return mMapPointsBuffer;
    }

    private ConfigState obtainRestoreConfig() {
        if (this.mRecycledRestoreAction != null) {
            final ConfigState mRecycledRestoreAction = this.mRecycledRestoreAction;
            this.mRecycledRestoreAction = mRecycledRestoreAction.mNextFree;
            return mRecycledRestoreAction;
        }
        return new ConfigState();
    }

    private void restoreTransform() {
        System.arraycopy(this.mTempMatrix, 0, this.mMatrixValues, 0, 16);
    }

    private void saveTransform() {
        System.arraycopy(this.mMatrixValues, 0, this.mTempMatrix, 0, 16);
    }

    private void setMixedColor(final int n, float n2, final float n3) {
        final float n4 = n3 * (1.0f - n2);
        n2 = n3 * n2 / (1.0f - n4);
        n2 = (n >>> 24) * n2 / 65025.0f;
        this.setTextureColor((n >>> 16 & 0xFF) * n2, (n >>> 8 & 0xFF) * n2, (n & 0xFF) * n2, n4);
        final GL11 mgl = this.mGL;
        mgl.glTexEnvfv(8960, 8705, this.mTextureColor, 0);
        mgl.glTexEnvf(8960, 34161, 34165.0f);
        mgl.glTexEnvf(8960, 34162, 34165.0f);
        mgl.glTexEnvf(8960, 34177, 34166.0f);
        mgl.glTexEnvf(8960, 34193, 768.0f);
        mgl.glTexEnvf(8960, 34185, 34166.0f);
        mgl.glTexEnvf(8960, 34201, 770.0f);
        mgl.glTexEnvf(8960, 34178, 34166.0f);
        mgl.glTexEnvf(8960, 34194, 770.0f);
        mgl.glTexEnvf(8960, 34186, 34166.0f);
        mgl.glTexEnvf(8960, 34202, 770.0f);
    }

    private void setRenderTarget(final RawTexture mTargetTexture) {
        final GL11ExtensionPack gl11ExtensionPack = (GL11ExtensionPack)this.mGL;
        if (this.mTargetTexture == null && mTargetTexture != null) {
            GLES11Canvas.mGLId.glGenBuffers(1, this.mFrameBuffer, 0);
            gl11ExtensionPack.glBindFramebufferOES(36160, this.mFrameBuffer[0]);
        }
        if (this.mTargetTexture != null && mTargetTexture == null) {
            gl11ExtensionPack.glBindFramebufferOES(36160, 0);
            gl11ExtensionPack.glDeleteFramebuffersOES(1, this.mFrameBuffer, 0);
        }
        if ((this.mTargetTexture = mTargetTexture) == null) {
            this.setSize(this.mScreenWidth, this.mScreenHeight);
            return;
        }
        this.setSize(mTargetTexture.getWidth(), mTargetTexture.getHeight());
        if (!mTargetTexture.isLoaded()) {
            mTargetTexture.prepare(this);
        }
        gl11ExtensionPack.glFramebufferTexture2DOES(36160, 36064, 3553, mTargetTexture.getId(), 0);
        checkFramebufferStatus(gl11ExtensionPack);
    }

    private void setTextureColor(final float n, final float n2, final float n3, final float n4) {
        final float[] mTextureColor = this.mTextureColor;
        mTextureColor[0] = n;
        mTextureColor[1] = n2;
        mTextureColor[2] = n3;
        mTextureColor[3] = n4;
    }

    private void setTextureCoords(final float n, final float n2, final float n3, final float n4) {
        this.mGL.glMatrixMode(5890);
        this.mTextureMatrixValues[0] = n3 - n;
        this.mTextureMatrixValues[5] = n4 - n2;
        this.mTextureMatrixValues[10] = 1.0f;
        this.mTextureMatrixValues[12] = n;
        this.mTextureMatrixValues[13] = n2;
        this.mTextureMatrixValues[15] = 1.0f;
        this.mGL.glLoadMatrixf(this.mTextureMatrixValues, 0);
        this.mGL.glMatrixMode(5888);
    }

    private void setTextureCoords(final RectF rectF) {
        this.setTextureCoords(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }

    private void setTextureCoords(final float[] array) {
        this.mGL.glMatrixMode(5890);
        this.mGL.glLoadMatrixf(array, 0);
        this.mGL.glMatrixMode(5888);
    }

    private void textureRect(final float n, final float n2, final float n3, final float n4) {
        final GL11 mgl = this.mGL;
        this.saveTransform();
        this.translate(n, n2);
        this.scale(n3, n4, 1.0f);
        mgl.glLoadMatrixf(this.mMatrixValues, 0);
        mgl.glDrawArrays(5, 0, 4);
        this.restoreTransform();
        ++this.mCountTextureRect;
    }

    private int uploadBuffer(final Buffer buffer, final int n) {
        final int[] array = { 0 };
        GLES11Canvas.mGLId.glGenBuffers(array.length, array, 0);
        final int n2 = array[0];
        this.mGL.glBindBuffer(34962, n2);
        this.mGL.glBufferData(34962, buffer.capacity() * n, buffer, 35044);
        return n2;
    }

    @Override
    public void beginRenderTarget(final RawTexture renderTarget) {
        this.save();
        this.mTargetStack.add(this.mTargetTexture);
        this.setRenderTarget(renderTarget);
    }

    @Override
    public void clearBuffer() {
        this.clearBuffer(null);
    }

    @Override
    public void clearBuffer(final float[] array) {
        if (array != null && array.length == 4) {
            this.mGL.glClearColor(array[1], array[2], array[3], array[0]);
        }
        else {
            this.mGL.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }
        this.mGL.glClear(16384);
    }

    @Override
    public void deleteBuffer(final int n) {
        synchronized (this.mUnboundTextures) {
            this.mDeleteBuffers.add(n);
        }
    }

    @Override
    public void deleteRecycledResources() {
        synchronized (this.mUnboundTextures) {
            final IntArray mUnboundTextures = this.mUnboundTextures;
            if (mUnboundTextures.size() > 0) {
                GLES11Canvas.mGLId.glDeleteTextures(this.mGL, mUnboundTextures.size(), mUnboundTextures.getInternalArray(), 0);
                mUnboundTextures.clear();
            }
            final IntArray mDeleteBuffers = this.mDeleteBuffers;
            if (mDeleteBuffers.size() > 0) {
                GLES11Canvas.mGLId.glDeleteBuffers(this.mGL, mDeleteBuffers.size(), mDeleteBuffers.getInternalArray(), 0);
                mDeleteBuffers.clear();
            }
        }
    }

    @Override
    public void drawLine(final float n, final float n2, final float n3, final float n4, final GLPaint glPaint) {
        final GL11 mgl = this.mGL;
        this.mGLState.setColorMode(glPaint.getColor(), this.mAlpha);
        this.mGLState.setLineWidth(glPaint.getLineWidth());
        this.saveTransform();
        this.translate(n, n2);
        this.scale(n3 - n, n4 - n2, 1.0f);
        mgl.glLoadMatrixf(this.mMatrixValues, 0);
        mgl.glDrawArrays(3, 4, 2);
        this.restoreTransform();
        ++this.mCountDrawLine;
    }

    @Override
    public void drawMesh(final BasicTexture basicTexture, final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        final float mAlpha = this.mAlpha;
        if (!this.bindTexture(basicTexture)) {
            return;
        }
        this.mGLState.setBlendEnabled(this.mBlendEnabled && (!basicTexture.isOpaque() || mAlpha < 0.95f));
        this.mGLState.setTextureAlpha(mAlpha);
        this.setTextureCoords(0.0f, 0.0f, 1.0f, 1.0f);
        this.saveTransform();
        this.translate(n, n2);
        this.mGL.glLoadMatrixf(this.mMatrixValues, 0);
        this.mGL.glBindBuffer(34962, n3);
        this.mGL.glVertexPointer(2, 5126, 0, 0);
        this.mGL.glBindBuffer(34962, n4);
        this.mGL.glTexCoordPointer(2, 5126, 0, 0);
        this.mGL.glBindBuffer(34963, n5);
        this.mGL.glDrawElements(5, n6, 5121, 0);
        this.mGL.glBindBuffer(34962, this.mBoxCoords);
        this.mGL.glVertexPointer(2, 5126, 0, 0);
        this.mGL.glTexCoordPointer(2, 5126, 0, 0);
        this.restoreTransform();
        ++this.mCountDrawMesh;
    }

    @Override
    public void drawMixed(final BasicTexture basicTexture, final int n, final float n2, final int n3, final int n4, final int n5, final int n6) {
        this.drawMixed(basicTexture, n, n2, n3, n4, n5, n6, this.mAlpha);
    }

    @Override
    public void drawMixed(final BasicTexture basicTexture, final int n, final float n2, RectF mDrawTextureSourceRect, RectF mDrawTextureTargetRect) {
        if (mDrawTextureTargetRect.width() > 0.0f && mDrawTextureTargetRect.height() > 0.0f) {
            if (n2 <= 0.01f) {
                this.drawTexture(basicTexture, mDrawTextureSourceRect, mDrawTextureTargetRect);
                return;
            }
            if (n2 >= 1.0f) {
                this.fillRect(mDrawTextureTargetRect.left, mDrawTextureTargetRect.top, mDrawTextureTargetRect.width(), mDrawTextureTargetRect.height(), n);
                return;
            }
            final float mAlpha = this.mAlpha;
            this.mDrawTextureSourceRect.set(mDrawTextureSourceRect);
            this.mDrawTextureTargetRect.set(mDrawTextureTargetRect);
            mDrawTextureSourceRect = this.mDrawTextureSourceRect;
            mDrawTextureTargetRect = this.mDrawTextureTargetRect;
            this.mGLState.setBlendEnabled(this.mBlendEnabled && (!basicTexture.isOpaque() || !Utils.isOpaque(n) || mAlpha < 0.95f));
            if (this.bindTexture(basicTexture)) {
                this.mGLState.setTexEnvMode(34160);
                this.setMixedColor(n, n2, mAlpha);
                convertCoordinate(mDrawTextureSourceRect, mDrawTextureTargetRect, basicTexture);
                this.setTextureCoords(mDrawTextureSourceRect);
                this.textureRect(mDrawTextureTargetRect.left, mDrawTextureTargetRect.top, mDrawTextureTargetRect.width(), mDrawTextureTargetRect.height());
                this.mGLState.setTexEnvMode(7681);
            }
        }
    }

    @Override
    public void drawRect(final float n, final float n2, final float n3, final float n4, final GLPaint glPaint) {
        final GL11 mgl = this.mGL;
        this.mGLState.setColorMode(glPaint.getColor(), this.mAlpha);
        this.mGLState.setLineWidth(glPaint.getLineWidth());
        this.saveTransform();
        this.translate(n, n2);
        this.scale(n3, n4, 1.0f);
        mgl.glLoadMatrixf(this.mMatrixValues, 0);
        mgl.glDrawArrays(2, 6, 4);
        this.restoreTransform();
        ++this.mCountDrawLine;
    }

    @Override
    public void drawTexture(final BasicTexture basicTexture, final int n, final int n2, final int n3, final int n4) {
        this.drawTexture(basicTexture, n, n2, n3, n4, this.mAlpha);
    }

    @Override
    public void drawTexture(final BasicTexture basicTexture, RectF mDrawTextureSourceRect, RectF mDrawTextureTargetRect) {
        if (mDrawTextureTargetRect.width() > 0.0f && mDrawTextureTargetRect.height() > 0.0f) {
            this.mDrawTextureSourceRect.set(mDrawTextureSourceRect);
            this.mDrawTextureTargetRect.set(mDrawTextureTargetRect);
            mDrawTextureSourceRect = this.mDrawTextureSourceRect;
            mDrawTextureTargetRect = this.mDrawTextureTargetRect;
            this.mGLState.setBlendEnabled(this.mBlendEnabled && (!basicTexture.isOpaque() || this.mAlpha < 0.95f));
            if (this.bindTexture(basicTexture)) {
                convertCoordinate(mDrawTextureSourceRect, mDrawTextureTargetRect, basicTexture);
                this.setTextureCoords(mDrawTextureSourceRect);
                this.mGLState.setTextureAlpha(this.mAlpha);
                this.textureRect(mDrawTextureTargetRect.left, mDrawTextureTargetRect.top, mDrawTextureTargetRect.width(), mDrawTextureTargetRect.height());
            }
        }
    }

    @Override
    public void drawTexture(final BasicTexture basicTexture, final float[] textureCoords, final int n, final int n2, final int n3, final int n4) {
        this.mGLState.setBlendEnabled(this.mBlendEnabled && (!basicTexture.isOpaque() || this.mAlpha < 0.95f));
        if (!this.bindTexture(basicTexture)) {
            return;
        }
        this.setTextureCoords(textureCoords);
        this.mGLState.setTextureAlpha(this.mAlpha);
        this.textureRect(n, n2, n3, n4);
    }

    @Override
    public void dumpStatisticsAndClear() {
        String.format("MESH:%d, TEX_OES:%d, TEX_RECT:%d, FILL_RECT:%d, LINE:%d", this.mCountDrawMesh, this.mCountTextureRect, this.mCountTextureOES, this.mCountFillRect, this.mCountDrawLine);
        this.mCountDrawMesh = 0;
        this.mCountTextureRect = 0;
        this.mCountTextureOES = 0;
        this.mCountFillRect = 0;
        this.mCountDrawLine = 0;
    }

    @Override
    public void endRenderTarget() {
        this.setRenderTarget(this.mTargetStack.remove(this.mTargetStack.size() - 1));
        this.restore();
    }

    @Override
    public void fillRect(final float n, final float n2, final float n3, final float n4, final int n5) {
        this.mGLState.setColorMode(n5, this.mAlpha);
        final GL11 mgl = this.mGL;
        this.saveTransform();
        this.translate(n, n2);
        this.scale(n3, n4, 1.0f);
        mgl.glLoadMatrixf(this.mMatrixValues, 0);
        mgl.glDrawArrays(5, 0, 4);
        this.restoreTransform();
        ++this.mCountFillRect;
    }

    @Override
    public float getAlpha() {
        return this.mAlpha;
    }

    @Override
    public void getBounds(final Rect rect, final int n, final int n2, final int n3, final int n4) {
    }

    @Override
    public GLId getGLId() {
        return GLES11Canvas.mGLId;
    }

    @Override
    public void initializeTexture(final BasicTexture basicTexture, final Bitmap bitmap) {
        final int target = basicTexture.getTarget();
        this.mGL.glBindTexture(target, basicTexture.getId());
        GLUtils.texImage2D(target, 0, bitmap, 0);
    }

    @Override
    public void initializeTextureSize(final BasicTexture basicTexture, final int n, final int n2) {
        final int target = basicTexture.getTarget();
        this.mGL.glBindTexture(target, basicTexture.getId());
        this.mGL.glTexImage2D(target, 0, n, basicTexture.getTextureWidth(), basicTexture.getTextureHeight(), 0, n, n2, (Buffer)null);
    }

    @Override
    public void multiplyAlpha(final float n) {
        Assert.assertTrue(n >= 0.0f && n <= 1.0f);
        this.mAlpha *= n;
    }

    @Override
    public void multiplyMatrix(final float[] array, final int n) {
        final float[] mTempMatrix = this.mTempMatrix;
        Matrix.multiplyMM(mTempMatrix, 0, this.mMatrixValues, 0, array, n);
        System.arraycopy(mTempMatrix, 0, this.mMatrixValues, 0, 16);
    }

    @Override
    public void recoverFromLightCycle() {
    }

    @Override
    public void restore() {
        if (this.mRestoreStack.isEmpty()) {
            throw new IllegalStateException();
        }
        final ConfigState configState = this.mRestoreStack.remove(this.mRestoreStack.size() - 1);
        configState.restore(this);
        this.freeRestoreConfig(configState);
    }

    @Override
    public void rotate(final float n, final float n2, final float n3, final float n4) {
        if (n == 0.0f) {
            return;
        }
        final float[] mTempMatrix = this.mTempMatrix;
        Matrix.setRotateM(mTempMatrix, 0, n, n2, n3, n4);
        Matrix.multiplyMM(mTempMatrix, 16, this.mMatrixValues, 0, mTempMatrix, 0);
        System.arraycopy(mTempMatrix, 16, this.mMatrixValues, 0, 16);
    }

    @Override
    public void save() {
        this.save(-1);
    }

    @Override
    public void save(final int n) {
        final ConfigState obtainRestoreConfig = this.obtainRestoreConfig();
        if ((n & 0x1) != 0x0) {
            obtainRestoreConfig.mAlpha = this.mAlpha;
        }
        else {
            obtainRestoreConfig.mAlpha = -1.0f;
        }
        if ((n & 0x2) != 0x0) {
            System.arraycopy(this.mMatrixValues, 0, obtainRestoreConfig.mMatrix, 0, 16);
        }
        else {
            obtainRestoreConfig.mMatrix[0] = Float.NEGATIVE_INFINITY;
        }
        this.mRestoreStack.add(obtainRestoreConfig);
    }

    @Override
    public void scale(final float n, final float n2, final float n3) {
        Matrix.scaleM(this.mMatrixValues, 0, n, n2, n3);
    }

    @Override
    public void setAlpha(final float mAlpha) {
        Assert.assertTrue(mAlpha >= 0.0f && mAlpha <= 1.0f);
        this.mAlpha = mAlpha;
    }

    @Override
    public void setSize(final int mScreenWidth, final int mScreenHeight) {
        Assert.assertTrue(mScreenWidth >= 0 && mScreenHeight >= 0);
        if (this.mTargetTexture == null) {
            this.mScreenWidth = mScreenWidth;
            this.mScreenHeight = mScreenHeight;
        }
        this.mAlpha = 1.0f;
        final GL11 mgl = this.mGL;
        mgl.glViewport(0, 0, mScreenWidth, mScreenHeight);
        mgl.glMatrixMode(5889);
        mgl.glLoadIdentity();
        GLU.gluOrtho2D((GL10) mgl, 0.0f, (float) mScreenWidth, 0.0f, (float) mScreenHeight);
        mgl.glMatrixMode(5888);
        mgl.glLoadIdentity();
        final float[] mMatrixValues = this.mMatrixValues;
        Matrix.setIdentityM(mMatrixValues, 0);
        if (this.mTargetTexture == null) {
            Matrix.translateM(mMatrixValues, 0, 0.0f, (float)mScreenHeight, 0.0f);
            Matrix.scaleM(mMatrixValues, 0, 1.0f, -1.0f, 1.0f);
        }
    }

    @Override
    public void setTextureParameters(final BasicTexture basicTexture) {
        final int width = basicTexture.getWidth();
        final int height = basicTexture.getHeight();
        GLES11Canvas.sCropRect[0] = 0.0f;
        GLES11Canvas.sCropRect[1] = height;
        GLES11Canvas.sCropRect[2] = width;
        GLES11Canvas.sCropRect[3] = -height;
        final int target = basicTexture.getTarget();
        this.mGL.glBindTexture(target, basicTexture.getId());
        this.mGL.glTexParameterfv(target, 35741, GLES11Canvas.sCropRect, 0);
        this.mGL.glTexParameteri(target, 10242, 33071);
        this.mGL.glTexParameteri(target, 10243, 33071);
        this.mGL.glTexParameterf(target, 10241, 9729.0f);
        this.mGL.glTexParameterf(target, 10240, 9729.0f);
    }

    @Override
    public void texSubImage2D(final BasicTexture basicTexture, final int n, final int n2, final Bitmap bitmap, final int n3, final int n4) {
        final int target = basicTexture.getTarget();
        this.mGL.glBindTexture(target, basicTexture.getId());
        GLUtils.texSubImage2D(target, 0, n, n2, bitmap, n3, n4);
    }

    @Override
    public void translate(final float n, final float n2) {
        final float[] mMatrixValues = this.mMatrixValues;
        mMatrixValues[12] += mMatrixValues[0] * n + mMatrixValues[4] * n2;
        mMatrixValues[13] += mMatrixValues[1] * n + mMatrixValues[5] * n2;
        mMatrixValues[14] += mMatrixValues[2] * n + mMatrixValues[6] * n2;
        mMatrixValues[15] += mMatrixValues[3] * n + mMatrixValues[7] * n2;
    }

    @Override
    public void translate(final float n, final float n2, final float n3) {
        Matrix.translateM(this.mMatrixValues, 0, n, n2, n3);
    }

    @Override
    public boolean unloadTexture(final BasicTexture basicTexture) {
        synchronized (this.mUnboundTextures) {
            if (!basicTexture.isLoaded()) {
                return false;
            }
            this.mUnboundTextures.add(basicTexture.mId);
            return true;
        }
    }

    @Override
    public int uploadBuffer(final ByteBuffer byteBuffer) {
        return this.uploadBuffer(byteBuffer, 1);
    }

    @Override
    public int uploadBuffer(final FloatBuffer floatBuffer) {
        return this.uploadBuffer(floatBuffer, 4);
    }

    private static class ConfigState
    {
        float mAlpha;
        float[] mMatrix;
        ConfigState mNextFree;

        private ConfigState() {
            super();
            this.mMatrix = new float[16];
        }

        public void restore(final GLES11Canvas gles11Canvas) {
            if (this.mAlpha >= 0.0f) {
                gles11Canvas.setAlpha(this.mAlpha);
            }
            if (this.mMatrix[0] != Float.NEGATIVE_INFINITY) {
                System.arraycopy(this.mMatrix, 0, gles11Canvas.mMatrixValues, 0, 16);
            }
        }
    }

    private static class GLState
    {
        private boolean mBlendEnabled;
        private final GL11 mGL;
        private boolean mLineSmooth;
        private float mLineWidth;
        private int mTexEnvMode;
        private float mTextureAlpha;
        private int mTextureTarget;

        public GLState(final GL11 mgl) {
            super();
            this.mTexEnvMode = 7681;
            this.mTextureAlpha = 1.0f;
            this.mTextureTarget = 3553;
            this.mBlendEnabled = true;
            this.mLineWidth = 1.0f;
            this.mLineSmooth = false;
            (this.mGL = mgl).glDisable(2896);
            mgl.glEnable(3024);
            mgl.glEnableClientState(32884);
            mgl.glEnableClientState(32888);
            mgl.glEnable(3553);
            mgl.glTexEnvf(8960, 8704, 7681.0f);
            mgl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            mgl.glEnable(3042);
            mgl.glBlendFunc(1, 771);
            mgl.glPixelStorei(3317, 2);
        }

        public void setBlendEnabled(final boolean mBlendEnabled) {
            if (this.mBlendEnabled == mBlendEnabled) {
                return;
            }
            this.mBlendEnabled = mBlendEnabled;
            if (mBlendEnabled) {
                this.mGL.glEnable(3042);
                return;
            }
            this.mGL.glDisable(3042);
        }

        public void setColorMode(final int n, float n2) {
            this.setBlendEnabled(!Utils.isOpaque(n) || n2 < 0.95f);
            this.mTextureAlpha = -1.0f;
            this.setTextureTarget(0);
            n2 = (n >>> 24) * n2 * 65535.0f / 255.0f / 255.0f;
            this.mGL.glColor4x(Math.round((n >> 16 & 0xFF) * n2), Math.round((n >> 8 & 0xFF) * n2), Math.round((n & 0xFF) * n2), Math.round(255.0f * n2));
        }

        public void setLineWidth(final float mLineWidth) {
            if (this.mLineWidth == mLineWidth) {
                return;
            }
            this.mLineWidth = mLineWidth;
            this.mGL.glLineWidth(mLineWidth);
        }

        public void setTexEnvMode(final int mTexEnvMode) {
            if (this.mTexEnvMode == mTexEnvMode) {
                return;
            }
            this.mTexEnvMode = mTexEnvMode;
            this.mGL.glTexEnvf(8960, 8704, (float)mTexEnvMode);
        }

        public void setTextureAlpha(final float mTextureAlpha) {
            if (this.mTextureAlpha == mTextureAlpha) {
                return;
            }
            this.mTextureAlpha = mTextureAlpha;
            if (mTextureAlpha >= 0.95f) {
                this.mGL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                this.setTexEnvMode(7681);
                return;
            }
            this.mGL.glColor4f(mTextureAlpha, mTextureAlpha, mTextureAlpha, mTextureAlpha);
            this.setTexEnvMode(8448);
        }

        public void setTextureTarget(final int mTextureTarget) {
            if (this.mTextureTarget != mTextureTarget) {
                if (this.mTextureTarget != 0) {
                    this.mGL.glDisable(this.mTextureTarget);
                }
                this.mTextureTarget = mTextureTarget;
                if (this.mTextureTarget != 0) {
                    this.mGL.glEnable(this.mTextureTarget);
                }
            }
        }
    }
}