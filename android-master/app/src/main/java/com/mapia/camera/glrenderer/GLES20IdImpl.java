package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11ExtensionPack;



public class GLES20IdImpl implements GLId
{
    private final int[] mTempIntArray;

    public GLES20IdImpl() {
        super();
        this.mTempIntArray = new int[1];
    }

    @Override
    public int generateTexture() {
        GLES20.glGenTextures(1, this.mTempIntArray, 0);
        GLES20Canvas.checkError();
        return this.mTempIntArray[0];
    }

    @Override
    public void glDeleteBuffers(final GL11 gl11, final int n, final int[] array, final int n2) {
        GLES20.glDeleteBuffers(n, array, n2);
        GLES20Canvas.checkError();
    }

    @Override
    public void glDeleteFramebuffers(final GL11ExtensionPack gl11ExtensionPack, final int n, final int[] array, final int n2) {
        GLES20.glDeleteFramebuffers(n, array, n2);
        GLES20Canvas.checkError();
    }

    @Override
    public void glDeleteTextures(final GL11 gl11, final int n, final int[] array, final int n2) {
        GLES20.glDeleteTextures(n, array, n2);
        GLES20Canvas.checkError();
    }

    @Override
    public void glGenBuffers(final int n, final int[] array, final int n2) {
        GLES20.glGenBuffers(n, array, n2);
        GLES20Canvas.checkError();
    }
}