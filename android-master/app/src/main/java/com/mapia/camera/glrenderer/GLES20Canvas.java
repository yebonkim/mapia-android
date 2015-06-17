package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.mapia.camera.util.IntArray;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;


public class GLES20Canvas implements GLCanvas
{
    private static final String ALPHA_UNIFORM = "uAlpha";
    private static final float[] BOUNDS_COORDINATES;
    private static final float[] BOX_COORDINATES;
    private static final String COLOR_UNIFORM = "uColor";
    private static final int COORDS_PER_VERTEX = 2;
    private static final int COUNT_FILL_VERTEX = 4;
    private static final int COUNT_LINE_VERTEX = 2;
    private static final int COUNT_RECT_VERTEX = 4;
    private static final String DRAW_FRAGMENT_SHADER = "precision mediump float;\nuniform vec4 uColor;\nvoid main() {\n  gl_FragColor = uColor;\n}\n";
    private static final String DRAW_VERTEX_SHADER = "uniform mat4 uMatrix;\nattribute vec2 aPosition;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n}\n";
    private static final int FLOAT_SIZE = 4;
    private static final int INDEX_ALPHA = 4;
    private static final int INDEX_COLOR = 2;
    private static final int INDEX_MATRIX = 1;
    private static final int INDEX_POSITION = 0;
    private static final int INDEX_TEXTURE_COORD = 2;
    private static final int INDEX_TEXTURE_MATRIX = 2;
    private static final int INDEX_TEXTURE_SAMPLER = 3;
    private static final int INITIAL_RESTORE_STATE_SIZE = 8;
    private static final int MATRIX_SIZE = 16;
    private static final String MATRIX_UNIFORM = "uMatrix";
    private static final String MESH_VERTEX_SHADER = "uniform mat4 uMatrix;\nattribute vec2 aPosition;\nattribute vec2 aTextureCoordinate;\nvarying vec2 vTextureCoord;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n  vTextureCoord = aTextureCoordinate;\n}\n";
    private static final String OES_TEXTURE_FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uAlpha;\nuniform samplerExternalOES uTextureSampler;\nvoid main() {\n  gl_FragColor = texture2D(uTextureSampler, vTextureCoord);\n  gl_FragColor *= uAlpha;\n}\n";
    private static final int OFFSET_DRAW_LINE = 4;
    private static final int OFFSET_DRAW_RECT = 6;
    private static final int OFFSET_FILL_RECT = 0;
    private static final float OPAQUE_ALPHA = 0.95f;
    private static final String POSITION_ATTRIBUTE = "aPosition";
    private static final String TAG;
    private static final String TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate";
    private static final String TEXTURE_FRAGMENT_SHADER = "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uAlpha;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = texture2D(uTextureSampler, vTextureCoord);\n  gl_FragColor *= uAlpha;\n}\n";
    private static final String TEXTURE_MATRIX_UNIFORM = "uTextureMatrix";
    private static final String TEXTURE_SAMPLER_UNIFORM = "uTextureSampler";
    private static final String TEXTURE_VERTEX_SHADER = "uniform mat4 uMatrix;\nuniform mat4 uTextureMatrix;\nattribute vec2 aPosition;\nvarying vec2 vTextureCoord;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n  vTextureCoord = (uTextureMatrix * pos).xy;\n}\n";
    private static final int VERTEX_STRIDE = 8;
    private static final GLId mGLId;
    private float[] mAlphas;
    private int mBoxCoordinates;
    private int mCountDrawLine;
    private int mCountDrawMesh;
    private int mCountFillRect;
    private int mCountTextureRect;
    private int mCurrentAlphaIndex;
    private int mCurrentMatrixIndex;
    private final IntArray mDeleteBuffers;
    ShaderParameter[] mDrawParameters;
    private int mDrawProgram;
    private int[] mFrameBuffer;
    private int mHeight;
    private float[] mMatrices;
    ShaderParameter[] mMeshParameters;
    private int mMeshProgram;
    ShaderParameter[] mOesTextureParameters;
    private int mOesTextureProgram;
    private float[] mProjectionMatrix;
    private IntArray mSaveFlags;
    private int mScreenHeight;
    private int mScreenWidth;
    private ArrayList<RawTexture> mTargetTextures;
    private final float[] mTempColor;
    private final int[] mTempIntArray;
    private final float[] mTempMatrix;
    private final RectF mTempSourceRect;
    private final RectF mTempTargetRect;
    private final float[] mTempTextureMatrix;
    ShaderParameter[] mTextureParameters;
    private int mTextureProgram;
    private final IntArray mUnboundTextures;
    private int mWidth;

    static {
        TAG = GLES20Canvas.class.getSimpleName();
        BOX_COORDINATES = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f };
        BOUNDS_COORDINATES = new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        mGLId = new GLES20IdImpl();
    }

    public GLES20Canvas() {
        super();
        this.mMatrices = new float[128];
        this.mAlphas = new float[8];
        this.mSaveFlags = new IntArray();
        this.mCurrentAlphaIndex = 0;
        this.mCurrentMatrixIndex = 0;
        this.mProjectionMatrix = new float[16];
        this.mDrawParameters = new ShaderParameter[] { new AttributeShaderParameter("aPosition"), new UniformShaderParameter("uMatrix"), new UniformShaderParameter("uColor") };
        this.mTextureParameters = new ShaderParameter[] { new AttributeShaderParameter("aPosition"), new UniformShaderParameter("uMatrix"), new UniformShaderParameter("uTextureMatrix"), new UniformShaderParameter("uTextureSampler"), new UniformShaderParameter("uAlpha") };
        this.mOesTextureParameters = new ShaderParameter[] { new AttributeShaderParameter("aPosition"), new UniformShaderParameter("uMatrix"), new UniformShaderParameter("uTextureMatrix"), new UniformShaderParameter("uTextureSampler"), new UniformShaderParameter("uAlpha") };
        this.mMeshParameters = new ShaderParameter[] { new AttributeShaderParameter("aPosition"), new UniformShaderParameter("uMatrix"), new AttributeShaderParameter("aTextureCoordinate"), new UniformShaderParameter("uTextureSampler"), new UniformShaderParameter("uAlpha") };
        this.mUnboundTextures = new IntArray();
        this.mDeleteBuffers = new IntArray();
        this.mCountDrawMesh = 0;
        this.mCountTextureRect = 0;
        this.mCountFillRect = 0;
        this.mCountDrawLine = 0;
        this.mFrameBuffer = new int[1];
        this.mTargetTextures = new ArrayList<RawTexture>();
        this.mTempMatrix = new float[32];
        this.mTempColor = new float[4];
        this.mTempSourceRect = new RectF();
        this.mTempTargetRect = new RectF();
        this.mTempTextureMatrix = new float[16];
        this.mTempIntArray = new int[1];
        Matrix.setIdentityM(this.mTempTextureMatrix, 0);
        Matrix.setIdentityM(this.mMatrices, this.mCurrentMatrixIndex);
        this.mAlphas[this.mCurrentAlphaIndex] = 1.0f;
        this.mTargetTextures.add(null);
        this.mBoxCoordinates = this.uploadBuffer(createBuffer(GLES20Canvas.BOX_COORDINATES));
        final int loadShader = loadShader(35633, "uniform mat4 uMatrix;\nattribute vec2 aPosition;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n}\n");
        final int loadShader2 = loadShader(35633, "uniform mat4 uMatrix;\nuniform mat4 uTextureMatrix;\nattribute vec2 aPosition;\nvarying vec2 vTextureCoord;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n  vTextureCoord = (uTextureMatrix * pos).xy;\n}\n");
        final int loadShader3 = loadShader(35633, "uniform mat4 uMatrix;\nattribute vec2 aPosition;\nattribute vec2 aTextureCoordinate;\nvarying vec2 vTextureCoord;\nvoid main() {\n  vec4 pos = vec4(aPosition, 0.0, 1.0);\n  gl_Position = uMatrix * pos;\n  vTextureCoord = aTextureCoordinate;\n}\n");
        final int loadShader4 = loadShader(35632, "precision mediump float;\nuniform vec4 uColor;\nvoid main() {\n  gl_FragColor = uColor;\n}\n");
        final int loadShader5 = loadShader(35632, "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uAlpha;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = texture2D(uTextureSampler, vTextureCoord);\n  gl_FragColor *= uAlpha;\n}\n");
        final int loadShader6 = loadShader(35632, "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uAlpha;\nuniform samplerExternalOES uTextureSampler;\nvoid main() {\n  gl_FragColor = texture2D(uTextureSampler, vTextureCoord);\n  gl_FragColor *= uAlpha;\n}\n");
        this.mDrawProgram = this.assembleProgram(loadShader, loadShader4, this.mDrawParameters);
        this.mTextureProgram = this.assembleProgram(loadShader2, loadShader5, this.mTextureParameters);
        this.mOesTextureProgram = this.assembleProgram(loadShader2, loadShader6, this.mOesTextureParameters);
        this.mMeshProgram = this.assembleProgram(loadShader3, loadShader5, this.mMeshParameters);
        GLES20.glBlendFunc(1, 771);
        checkError();
    }

    private int assembleProgram(int n, int i, final ShaderParameter[] array) {
        final int glCreateProgram = GLES20.glCreateProgram();
        checkError();
        if (glCreateProgram == 0) {
            throw new RuntimeException("Cannot create GL program: " + GLES20.glGetError());
        }
        GLES20.glAttachShader(glCreateProgram, n);
        checkError();
        GLES20.glAttachShader(glCreateProgram, i);
        checkError();
        GLES20.glLinkProgram(glCreateProgram);
        checkError();
        final int[] mTempIntArray = this.mTempIntArray;
        GLES20.glGetProgramiv(glCreateProgram, 35714, mTempIntArray, 0);
        n = glCreateProgram;
        if (mTempIntArray[0] != 1) {
            Log.e(GLES20Canvas.TAG, "Could not link program: ");
            Log.e(GLES20Canvas.TAG, GLES20.glGetProgramInfoLog(glCreateProgram));
            GLES20.glDeleteProgram(glCreateProgram);
            n = 0;
        }
        for (i = 0; i < array.length; ++i) {
            array[i].loadHandle(n);
        }
        return n;
    }

    public static void checkError() {
        final int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            Log.e(GLES20Canvas.TAG, "GL error: " + glGetError, new Throwable());
        }
    }

    private static void checkFramebufferStatus() {
        final int glCheckFramebufferStatus = GLES20.glCheckFramebufferStatus(36160);
        if (glCheckFramebufferStatus == 36053) {
            return;
        }
        String s = "";
        switch (glCheckFramebufferStatus) {
            default: {
                s = s;
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatus));
            }
            case 36061: {
                s = "GL_FRAMEBUFFER_UNSUPPORTED";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatus));
            }
            case 36055: {
                s = "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatus));
            }
            case 36057: {
                s = "GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS";
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatus));
            }
            case 36054: {
                s = "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT";
            }
            case 36056:
            case 36058:
            case 36059:
            case 36060: {
                throw new RuntimeException(s + ":" + Integer.toHexString(glCheckFramebufferStatus));
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

    private static void copyTextureCoordinates(final BasicTexture basicTexture, final RectF rectF) {
        boolean b = false;
        boolean b2 = false;
        final int width = basicTexture.getWidth();
        int height = basicTexture.getHeight();
        int n = width;
        if (basicTexture.hasBorder()) {
            b = true;
            b2 = true;
            n = width - 1;
            --height;
        }
        rectF.set((float)(b ? 1 : 0), (float)(b2 ? 1 : 0), (float)n, (float)height);
    }

    private static FloatBuffer createBuffer(final float[] array) {
        final FloatBuffer floatBuffer = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(array, 0, array.length).position(0);
        return floatBuffer;
    }

    private void draw(final int n, final int n2, final int n3, final float n4, final float n5, final float n6, final float n7, final int n8, final float n9) {
        this.prepareDraw(n2, n8, n9);
        this.draw(this.mDrawParameters, n, n3, n4, n5, n6, n7);
    }

    private void draw(final int n, final int n2, final int n3, final float n4, final float n5, final float n6, final float n7, final GLPaint glPaint) {
        this.draw(n, n2, n3, n4, n5, n6, n7, glPaint.getColor(), glPaint.getLineWidth());
    }

    private void draw(final ShaderParameter[] array, final int n, final int n2, final float n3, final float n4, final float n5, final float n6) {
        this.setMatrix(array, n3, n4, n5, n6);
        final int handle = array[0].handle;
        GLES20.glEnableVertexAttribArray(handle);
        checkError();
        GLES20.glDrawArrays(n, 0, n2);
        checkError();
        GLES20.glDisableVertexAttribArray(handle);
        checkError();
    }

    private void drawTextureRect(final BasicTexture basicTexture, final RectF textureMatrix, final RectF rectF) {
        this.setTextureMatrix(textureMatrix);
        this.drawTextureRect(basicTexture, this.mTempTextureMatrix, rectF);
    }

    private void drawTextureRect(final BasicTexture basicTexture, final float[] array, final RectF rectF) {
        final ShaderParameter[] prepareTexture = this.prepareTexture(basicTexture);
        this.setPosition(prepareTexture, 0);
        GLES20.glUniformMatrix4fv(prepareTexture[2].handle, 1, false, array, 0);
        checkError();
        if (basicTexture.isFlippedVertically()) {
            this.save(2);
            this.translate(0.0f, rectF.centerY());
            this.scale(1.0f, -1.0f, 1.0f);
            this.translate(0.0f, -rectF.centerY());
        }
        this.draw(prepareTexture, 5, 4, rectF.left, rectF.top, rectF.width(), rectF.height());
        if (basicTexture.isFlippedVertically()) {
            this.restore();
        }
        ++this.mCountTextureRect;
    }

    private void enableBlending(final boolean b) {
        if (b) {
            GLES20.glEnable(3042);
            checkError();
            return;
        }
        GLES20.glDisable(3042);
        checkError();
    }

    private float[] getColor(final int n) {
        final float n2 = (n >>> 24 & 0xFF) / 255.0f * this.getAlpha();
        final float n3 = (n >>> 16 & 0xFF) / 255.0f;
        final float n4 = (n >>> 8 & 0xFF) / 255.0f;
        final float n5 = (n & 0xFF) / 255.0f;
        this.mTempColor[0] = n3 * n2;
        this.mTempColor[1] = n4 * n2;
        this.mTempColor[2] = n5 * n2;
        this.mTempColor[3] = n2;
        return this.mTempColor;
    }

    private RawTexture getTargetTexture() {
        return this.mTargetTextures.get(this.mTargetTextures.size() - 1);
    }

    private static int loadShader(int glCreateShader, final String s) {
        glCreateShader = GLES20.glCreateShader(glCreateShader);
        GLES20.glShaderSource(glCreateShader, s);
        checkError();
        GLES20.glCompileShader(glCreateShader);
        checkError();
        return glCreateShader;
    }

    private void prepareDraw(final int n, final int n2, final float n3) {
        GLES20.glUseProgram(this.mDrawProgram);
        checkError();
        if (n3 > 0.0f) {
            GLES20.glLineWidth(n3);
            checkError();
        }
        final float[] color = this.getColor(n2);
        final boolean b = color[3] < 1.0f;
        this.enableBlending(b);
        if (b) {
            GLES20.glBlendColor(color[0], color[1], color[2], color[3]);
            checkError();
        }
        GLES20.glUniform4fv(this.mDrawParameters[2].handle, 1, color, 0);
        this.setPosition(this.mDrawParameters, n);
        checkError();
    }

    private void prepareTexture(final BasicTexture basicTexture, final int n, final ShaderParameter[] array) {
        GLES20.glUseProgram(n);
        checkError();
        this.enableBlending(!basicTexture.isOpaque() || this.getAlpha() < 0.95f);
        GLES20.glActiveTexture(33984);
        checkError();
        basicTexture.onBind(this);
        GLES20.glBindTexture(basicTexture.getTarget(), basicTexture.getId());
        checkError();
        GLES20.glUniform1i(array[3].handle, 0);
        checkError();
        GLES20.glUniform1f(array[4].handle, this.getAlpha());
        checkError();
    }

    private ShaderParameter[] prepareTexture(final BasicTexture basicTexture) {
        ShaderParameter[] array;
        int n;
        if (basicTexture.getTarget() == 3553) {
            array = this.mTextureParameters;
            n = this.mTextureProgram;
        }
        else {
            array = this.mOesTextureParameters;
            n = this.mOesTextureProgram;
        }
        this.prepareTexture(basicTexture, n, array);
        return array;
    }

    private static void printMatrix(final String s, final float[] array, final int n) {
        final StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < 16; ++i) {
            sb.append(' ');
            if (i % 4 == 0) {
                sb.append('\n');
            }
            sb.append(array[n + i]);
        }
        Log.v(GLES20Canvas.TAG, sb.toString());
    }

    private void setMatrix(final ShaderParameter[] array, final float n, final float n2, final float n3, final float n4) {
        Matrix.translateM(this.mTempMatrix, 0, this.mMatrices, this.mCurrentMatrixIndex, n, n2, 0.0f);
        Matrix.scaleM(this.mTempMatrix, 0, n3, n4, 1.0f);
        Matrix.multiplyMM(this.mTempMatrix, 16, this.mProjectionMatrix, 0, this.mTempMatrix, 0);
        GLES20.glUniformMatrix4fv(array[1].handle, 1, false, this.mTempMatrix, 16);
        checkError();
    }

    private void setPosition(final ShaderParameter[] array, final int n) {
        GLES20.glBindBuffer(34962, this.mBoxCoordinates);
        checkError();
        GLES20.glVertexAttribPointer(array[0].handle, 2, 5126, false, 8, n * 8);
        checkError();
        GLES20.glBindBuffer(34962, 0);
        checkError();
    }

    private void setRenderTarget(final BasicTexture basicTexture, final RawTexture rawTexture) {
        if (basicTexture == null && rawTexture != null) {
            GLES20.glGenFramebuffers(1, this.mFrameBuffer, 0);
            checkError();
            GLES20.glBindFramebuffer(36160, this.mFrameBuffer[0]);
            checkError();
        }
        else if (basicTexture != null && rawTexture == null) {
            GLES20.glBindFramebuffer(36160, 0);
            checkError();
            GLES20.glDeleteFramebuffers(1, this.mFrameBuffer, 0);
            checkError();
        }
        if (rawTexture == null) {
            this.setSize(this.mScreenWidth, this.mScreenHeight);
            return;
        }
        this.setSize(rawTexture.getWidth(), rawTexture.getHeight());
        if (!rawTexture.isLoaded()) {
            rawTexture.prepare(this);
        }
        GLES20.glFramebufferTexture2D(36160, 36064, rawTexture.getTarget(), rawTexture.getId(), 0);
        checkError();
        checkFramebufferStatus();
    }

    private void setTextureMatrix(final RectF rectF) {
        this.mTempTextureMatrix[0] = rectF.width();
        this.mTempTextureMatrix[5] = rectF.height();
        this.mTempTextureMatrix[12] = rectF.left;
        this.mTempTextureMatrix[13] = rectF.top;
    }

    private int uploadBuffer(final Buffer buffer, final int n) {
        GLES20Canvas.mGLId.glGenBuffers(1, this.mTempIntArray, 0);
        checkError();
        final int n2 = this.mTempIntArray[0];
        GLES20.glBindBuffer(34962, n2);
        checkError();
        GLES20.glBufferData(34962, buffer.capacity() * n, buffer, 35044);
        checkError();
        return n2;
    }

    @Override
    public void beginRenderTarget(final RawTexture rawTexture) {
        this.save();
        final RawTexture targetTexture = this.getTargetTexture();
        this.mTargetTextures.add(rawTexture);
        this.setRenderTarget(targetTexture, rawTexture);
    }

    @Override
    public void clearBuffer() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        checkError();
        GLES20.glClear(16384);
        checkError();
    }

    @Override
    public void clearBuffer(final float[] array) {
        GLES20.glClearColor(array[1], array[2], array[3], array[0]);
        checkError();
        GLES20.glClear(16384);
        checkError();
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
            if (this.mUnboundTextures.size() > 0) {
                GLES20Canvas.mGLId.glDeleteTextures(null, mUnboundTextures.size(), mUnboundTextures.getInternalArray(), 0);
                mUnboundTextures.clear();
            }
            final IntArray mDeleteBuffers = this.mDeleteBuffers;
            if (mDeleteBuffers.size() > 0) {
                GLES20Canvas.mGLId.glDeleteBuffers(null, mDeleteBuffers.size(), mDeleteBuffers.getInternalArray(), 0);
                mDeleteBuffers.clear();
            }
        }
    }

    @Override
    public void drawLine(final float n, final float n2, final float n3, final float n4, final GLPaint glPaint) {
        this.draw(3, 4, 2, n, n2, n3 - n, n4 - n2, glPaint);
        ++this.mCountDrawLine;
    }

    @Override
    public void drawMesh(final BasicTexture basicTexture, final int n, final int n2, int handle, int handle2, final int n3, final int n4) {
        this.prepareTexture(basicTexture, this.mMeshProgram, this.mMeshParameters);
        GLES20.glBindBuffer(34963, n3);
        checkError();
        GLES20.glBindBuffer(34962, handle);
        checkError();
        handle = this.mMeshParameters[0].handle;
        GLES20.glVertexAttribPointer(handle, 2, 5126, false, 8, 0);
        checkError();
        GLES20.glBindBuffer(34962, handle2);
        checkError();
        handle2 = this.mMeshParameters[2].handle;
        GLES20.glVertexAttribPointer(handle2, 2, 5126, false, 8, 0);
        checkError();
        GLES20.glBindBuffer(34962, 0);
        checkError();
        GLES20.glEnableVertexAttribArray(handle);
        checkError();
        GLES20.glEnableVertexAttribArray(handle2);
        checkError();
        this.setMatrix(this.mMeshParameters, n, n2, 1.0f, 1.0f);
        GLES20.glDrawElements(5, n4, 5121, 0);
        checkError();
        GLES20.glDisableVertexAttribArray(handle);
        checkError();
        GLES20.glDisableVertexAttribArray(handle2);
        checkError();
        GLES20.glBindBuffer(34963, 0);
        checkError();
        ++this.mCountDrawMesh;
    }

    @Override
    public void drawMixed(final BasicTexture basicTexture, final int n, final float n2, final int n3, final int n4, final int n5, final int n6) {
        copyTextureCoordinates(basicTexture, this.mTempSourceRect);
        this.mTempTargetRect.set((float)n3, (float)n4, (float)(n3 + n5), (float)(n4 + n6));
        this.drawMixed(basicTexture, n, n2, this.mTempSourceRect, this.mTempTargetRect);
    }

    @Override
    public void drawMixed(final BasicTexture basicTexture, final int n, float min, final RectF rectF, final RectF rectF2) {
        if (rectF2.width() <= 0.0f || rectF2.height() <= 0.0f) {
            return;
        }
        this.save(1);
        final float alpha = this.getAlpha();
        min = Math.min(1.0f, Math.max(0.0f, min));
        this.setAlpha((1.0f - min) * alpha);
        this.drawTexture(basicTexture, rectF, rectF2);
        this.setAlpha(min * alpha);
        this.fillRect(rectF2.left, rectF2.top, rectF2.width(), rectF2.height(), n);
        this.restore();
    }

    @Override
    public void drawRect(final float n, final float n2, final float n3, final float n4, final GLPaint glPaint) {
        this.draw(2, 6, 4, n, n2, n3, n4, glPaint);
        ++this.mCountDrawLine;
    }

    @Override
    public void drawTexture(final BasicTexture basicTexture, final int n, final int n2, final int n3, final int n4) {
        if (n3 <= 0 || n4 <= 0) {
            return;
        }
        copyTextureCoordinates(basicTexture, this.mTempSourceRect);
        this.mTempTargetRect.set((float)n, (float)n2, (float)(n + n3), (float)(n2 + n4));
        convertCoordinate(this.mTempSourceRect, this.mTempTargetRect, basicTexture);
        this.drawTextureRect(basicTexture, this.mTempSourceRect, this.mTempTargetRect);
    }

    @Override
    public void drawTexture(final BasicTexture basicTexture, final RectF rectF, final RectF rectF2) {
        if (rectF2.width() <= 0.0f || rectF2.height() <= 0.0f) {
            return;
        }
        this.mTempSourceRect.set(rectF);
        this.mTempTargetRect.set(rectF2);
        convertCoordinate(this.mTempSourceRect, this.mTempTargetRect, basicTexture);
        this.drawTextureRect(basicTexture, this.mTempSourceRect, this.mTempTargetRect);
    }

    @Override
    public void drawTexture(final BasicTexture basicTexture, final float[] array, final int n, final int n2, final int n3, final int n4) {
        if (n3 <= 0 || n4 <= 0) {
            return;
        }
        this.mTempTargetRect.set((float)n, (float)n2, (float)(n + n3), (float)(n2 + n4));
        this.drawTextureRect(basicTexture, array, this.mTempTargetRect);
    }

    @Override
    public void dumpStatisticsAndClear() {
        String.format("MESH:%d, TEX_RECT:%d, FILL_RECT:%d, LINE:%d", this.mCountDrawMesh, this.mCountTextureRect, this.mCountFillRect, this.mCountDrawLine);
        this.mCountDrawMesh = 0;
        this.mCountTextureRect = 0;
        this.mCountFillRect = 0;
        this.mCountDrawLine = 0;
    }

    @Override
    public void endRenderTarget() {
        this.setRenderTarget(this.mTargetTextures.remove(this.mTargetTextures.size() - 1), this.getTargetTexture());
        this.restore();
    }

    @Override
    public void fillRect(final float n, final float n2, final float n3, final float n4, final int n5) {
        this.draw(5, 0, 4, n, n2, n3, n4, n5, 0.0f);
        ++this.mCountFillRect;
    }

    @Override
    public float getAlpha() {
        return this.mAlphas[this.mCurrentAlphaIndex];
    }

    @Override
    public void getBounds(final Rect rect, final int n, final int n2, final int n3, final int n4) {
        Matrix.translateM(this.mTempMatrix, 0, this.mMatrices, this.mCurrentMatrixIndex, (float)n, (float)n2, 0.0f);
        Matrix.scaleM(this.mTempMatrix, 0, (float)n3, (float)n4, 1.0f);
        Matrix.multiplyMV(this.mTempMatrix, 16, this.mTempMatrix, 0, GLES20Canvas.BOUNDS_COORDINATES, 0);
        Matrix.multiplyMV(this.mTempMatrix, 20, this.mTempMatrix, 0, GLES20Canvas.BOUNDS_COORDINATES, 4);
        rect.left = Math.round(this.mTempMatrix[16]);
        rect.right = Math.round(this.mTempMatrix[20]);
        rect.top = Math.round(this.mTempMatrix[17]);
        rect.bottom = Math.round(this.mTempMatrix[21]);
        rect.sort();
    }

    @Override
    public GLId getGLId() {
        return GLES20Canvas.mGLId;
    }

    @Override
    public void initializeTexture(final BasicTexture basicTexture, final Bitmap bitmap) {
        final int target = basicTexture.getTarget();
        GLES20.glBindTexture(target, basicTexture.getId());
        checkError();
        GLUtils.texImage2D(target, 0, bitmap, 0);
    }

    @Override
    public void initializeTextureSize(final BasicTexture basicTexture, final int n, final int n2) {
        final int target = basicTexture.getTarget();
        GLES20.glBindTexture(target, basicTexture.getId());
        checkError();
        GLES20.glTexImage2D(target, 0, n, basicTexture.getTextureWidth(), basicTexture.getTextureHeight(), 0, n, n2, (Buffer)null);
    }

    @Override
    public void multiplyAlpha(final float n) {
        this.setAlpha(this.getAlpha() * n);
    }

    @Override
    public void multiplyMatrix(final float[] array, final int n) {
        final float[] mTempMatrix = this.mTempMatrix;
        final float[] mMatrices = this.mMatrices;
        final int mCurrentMatrixIndex = this.mCurrentMatrixIndex;
        Matrix.multiplyMM(mTempMatrix, 0, mMatrices, mCurrentMatrixIndex, array, n);
        System.arraycopy(mTempMatrix, 0, mMatrices, mCurrentMatrixIndex, 16);
    }

    @Override
    public void recoverFromLightCycle() {
        GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
        GLES20.glDisable(2929);
        GLES20.glBlendFunc(1, 771);
        checkError();
    }

    @Override
    public void restore() {
        final boolean b = true;
        final int removeLast = this.mSaveFlags.removeLast();
        int n;
        if ((removeLast & 0x1) == 0x1) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            --this.mCurrentAlphaIndex;
        }
        if ((removeLast & 0x2) == 0x2 && b) {
            this.mCurrentMatrixIndex -= 16;
        }
    }

    @Override
    public void rotate(final float n, final float n2, final float n3, final float n4) {
        if (n == 0.0f) {
            return;
        }
        final float[] mTempMatrix = this.mTempMatrix;
        Matrix.setRotateM(mTempMatrix, 0, n, n2, n3, n4);
        final float[] mMatrices = this.mMatrices;
        final int mCurrentMatrixIndex = this.mCurrentMatrixIndex;
        Matrix.multiplyMM(mTempMatrix, 16, mMatrices, mCurrentMatrixIndex, mTempMatrix, 0);
        System.arraycopy(mTempMatrix, 16, mMatrices, mCurrentMatrixIndex, 16);
    }

    @Override
    public void save() {
        this.save(-1);
    }

    @Override
    public void save(final int n) {
        final boolean b = true;
        int n2;
        if ((n & 0x1) == 0x1) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        if (n2 != 0) {
            final float alpha = this.getAlpha();
            ++this.mCurrentAlphaIndex;
            if (this.mAlphas.length <= this.mCurrentAlphaIndex) {
                this.mAlphas = Arrays.copyOf(this.mAlphas, this.mAlphas.length * 2);
            }
            this.mAlphas[this.mCurrentAlphaIndex] = alpha;
        }
        if ((n & 0x2) == 0x2 && b) {
            final int mCurrentMatrixIndex = this.mCurrentMatrixIndex;
            this.mCurrentMatrixIndex += 16;
            if (this.mMatrices.length <= this.mCurrentMatrixIndex) {
                this.mMatrices = Arrays.copyOf(this.mMatrices, this.mMatrices.length * 2);
            }
            System.arraycopy(this.mMatrices, mCurrentMatrixIndex, this.mMatrices, this.mCurrentMatrixIndex, 16);
        }
        this.mSaveFlags.add(n);
    }

    @Override
    public void scale(final float n, final float n2, final float n3) {
        Matrix.scaleM(this.mMatrices, this.mCurrentMatrixIndex, n, n2, n3);
    }

    @Override
    public void setAlpha(final float n) {
        this.mAlphas[this.mCurrentAlphaIndex] = n;
    }

    @Override
    public void setSize(final int n, final int n2) {
        this.mWidth = n;
        this.mHeight = n2;
        GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
        checkError();
        Matrix.setIdentityM(this.mMatrices, this.mCurrentMatrixIndex);
        Matrix.orthoM(this.mProjectionMatrix, 0, 0.0f, (float)n, 0.0f, (float)n2, -1.0f, 1.0f);
        if (this.getTargetTexture() == null) {
            this.mScreenWidth = n;
            this.mScreenHeight = n2;
            Matrix.translateM(this.mMatrices, this.mCurrentMatrixIndex, 0.0f, (float)n2, 0.0f);
            Matrix.scaleM(this.mMatrices, this.mCurrentMatrixIndex, 1.0f, -1.0f, 1.0f);
        }
    }

    @Override
    public void setTextureParameters(final BasicTexture basicTexture) {
        final int target = basicTexture.getTarget();
        GLES20.glBindTexture(target, basicTexture.getId());
        checkError();
        GLES20.glTexParameteri(target, 10242, 33071);
        GLES20.glTexParameteri(target, 10243, 33071);
        GLES20.glTexParameterf(target, 10241, 9729.0f);
        GLES20.glTexParameterf(target, 10240, 9729.0f);
    }

    @Override
    public void texSubImage2D(final BasicTexture basicTexture, final int n, final int n2, final Bitmap bitmap, final int n3, final int n4) {
        final int target = basicTexture.getTarget();
        GLES20.glBindTexture(target, basicTexture.getId());
        checkError();
        GLUtils.texSubImage2D(target, 0, n, n2, bitmap, n3, n4);
    }

    @Override
    public void translate(final float n, final float n2) {
        final int mCurrentMatrixIndex = this.mCurrentMatrixIndex;
        final float[] mMatrices = this.mMatrices;
        final int n3 = mCurrentMatrixIndex + 12;
        mMatrices[n3] += mMatrices[mCurrentMatrixIndex + 0] * n + mMatrices[mCurrentMatrixIndex + 4] * n2;
        final int n4 = mCurrentMatrixIndex + 13;
        mMatrices[n4] += mMatrices[mCurrentMatrixIndex + 1] * n + mMatrices[mCurrentMatrixIndex + 5] * n2;
        final int n5 = mCurrentMatrixIndex + 14;
        mMatrices[n5] += mMatrices[mCurrentMatrixIndex + 2] * n + mMatrices[mCurrentMatrixIndex + 6] * n2;
        final int n6 = mCurrentMatrixIndex + 15;
        mMatrices[n6] += mMatrices[mCurrentMatrixIndex + 3] * n + mMatrices[mCurrentMatrixIndex + 7] * n2;
    }

    @Override
    public void translate(final float n, final float n2, final float n3) {
        Matrix.translateM(this.mMatrices, this.mCurrentMatrixIndex, n, n2, n3);
    }

    @Override
    public boolean unloadTexture(final BasicTexture basicTexture) {
        final boolean loaded = basicTexture.isLoaded();
        if (loaded) {
            synchronized (this.mUnboundTextures) {
                this.mUnboundTextures.add(basicTexture.getId());
                return loaded;
            }
        }
        return loaded;
    }

    @Override
    public int uploadBuffer(final ByteBuffer byteBuffer) {
        return this.uploadBuffer(byteBuffer, 1);
    }

    @Override
    public int uploadBuffer(final FloatBuffer floatBuffer) {
        return this.uploadBuffer(floatBuffer, 4);
    }

    private static class AttributeShaderParameter extends ShaderParameter
    {
        public AttributeShaderParameter(final String s) {
            super(s);
        }

        @Override
        public void loadHandle(final int n) {
            this.handle = GLES20.glGetAttribLocation(n, this.mName);
            GLES20Canvas.checkError();
        }
    }

    private abstract static class ShaderParameter
    {
        public int handle;
        protected final String mName;

        public ShaderParameter(final String mName) {
            super();
            this.mName = mName;
        }

        public abstract void loadHandle(final int p0);
    }

    private static class UniformShaderParameter extends ShaderParameter
    {
        public UniformShaderParameter(final String s) {
            super(s);
        }

        @Override
        public void loadHandle(final int n) {
            this.handle = GLES20.glGetUniformLocation(n, this.mName);
            GLES20Canvas.checkError();
        }
    }
}