package com.mapia.camera.glrenderer;

import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

/**
 * Created by daehyun on 15. 6. 16..
 */


public interface GLId
{
    int generateTexture();

    void glDeleteBuffers(GL11 p0, int p1, int[] p2, int p3);

    void glDeleteFramebuffers(GL11ExtensionPack p0, int p1, int[] p2, int p3);

    void glDeleteTextures(GL11 p0, int p1, int[] p2, int p3);

    void glGenBuffers(int p0, int[] p1, int p2);
}