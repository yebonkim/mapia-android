package com.mapia.camera.glrenderer;

/**
 * Created by daehyun on 15. 6. 16..
 */
public interface Texture
{
    void draw(GLCanvas p0, int p1, int p2);

    void draw(GLCanvas p0, int p1, int p2, int p3, int p4);

    int getHeight();

    int getWidth();

    boolean isOpaque();
}