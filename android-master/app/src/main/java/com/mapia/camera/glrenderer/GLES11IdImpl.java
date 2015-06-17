package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11ExtensionPack;


public class GLES11IdImpl implements GLId
{
    private static Object sLock;
    private static int sNextId;

    static {
        GLES11IdImpl.sNextId = 1;
        GLES11IdImpl.sLock = new Object();
    }

    @Override
    public int generateTexture() {
        synchronized (GLES11IdImpl.sLock) {
            final int sNextId = GLES11IdImpl.sNextId;
            GLES11IdImpl.sNextId = sNextId + 1;
            return sNextId;
        }
    }

    @Override
    public void glDeleteBuffers(final GL11 gl11, final int n, final int[] array, final int n2) {
        synchronized (GLES11IdImpl.sLock) {
            gl11.glDeleteBuffers(n, array, n2);
        }
    }

    @Override
    public void glDeleteFramebuffers(final GL11ExtensionPack gl11ExtensionPack, final int n, final int[] array, final int n2) {
        synchronized (GLES11IdImpl.sLock) {
            gl11ExtensionPack.glDeleteFramebuffersOES(n, array, n2);
        }
    }

    @Override
    public void glDeleteTextures(final GL11 gl11, final int n, final int[] array, final int n2) {
        synchronized (GLES11IdImpl.sLock) {
            gl11.glDeleteTextures(n, array, n2);
        }
    }

    @Override
    public void glGenBuffers(int sNextId, final int[] array, final int n) {
        final Object sLock = GLES11IdImpl.sLock;
        // monitorenter(sLock)
        // monitorenter(sLock)
        while (true) {
            final int n2 = sNextId - 1;
            if (sNextId <= 0) {
                return;
            }
            try {
                sNextId = GLES11IdImpl.sNextId;
                GLES11IdImpl.sNextId = sNextId + 1;
                array[n + n2] = sNextId;
                sNextId = n2;
                continue;
            }
            finally {
            }
            // monitorexit(sLock)
        }
    }
}