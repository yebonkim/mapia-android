package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;


public interface GLCanvas
{
    public static final int SAVE_FLAG_ALL = -1;
    public static final int SAVE_FLAG_ALPHA = 1;
    public static final int SAVE_FLAG_MATRIX = 2;

    void beginRenderTarget(RawTexture p0);

    void clearBuffer();

    void clearBuffer(float[] p0);

    void deleteBuffer(int p0);

    void deleteRecycledResources();

    void drawLine(float p0, float p1, float p2, float p3, GLPaint p4);

    void drawMesh(BasicTexture p0, int p1, int p2, int p3, int p4, int p5, int p6);

    void drawMixed(BasicTexture p0, int p1, float p2, int p3, int p4, int p5, int p6);

    void drawMixed(BasicTexture p0, int p1, float p2, RectF p3, RectF p4);

    void drawRect(float p0, float p1, float p2, float p3, GLPaint p4);

    void drawTexture(BasicTexture p0, int p1, int p2, int p3, int p4);

    void drawTexture(BasicTexture p0, RectF p1, RectF p2);

    void drawTexture(BasicTexture p0, float[] p1, int p2, int p3, int p4, int p5);

    void dumpStatisticsAndClear();

    void endRenderTarget();

    void fillRect(float p0, float p1, float p2, float p3, int p4);

    float getAlpha();

    void getBounds(Rect p0, int p1, int p2, int p3, int p4);

    GLId getGLId();

    void initializeTexture(BasicTexture p0, Bitmap p1);

    void initializeTextureSize(BasicTexture p0, int p1, int p2);

    void multiplyAlpha(float p0);

    void multiplyMatrix(float[] p0, int p1);

    void recoverFromLightCycle();

    void restore();

    void rotate(float p0, float p1, float p2, float p3);

    void save();

    void save(int p0);

    void scale(float p0, float p1, float p2);

    void setAlpha(float p0);

    void setSize(int p0, int p1);

    void setTextureParameters(BasicTexture p0);

    void texSubImage2D(BasicTexture p0, int p1, int p2, Bitmap p3, int p4, int p5);

    void translate(float p0, float p1);

    void translate(float p0, float p1, float p2);

    boolean unloadTexture(BasicTexture p0);

    int uploadBuffer(ByteBuffer p0);

    int uploadBuffer(FloatBuffer p0);
}