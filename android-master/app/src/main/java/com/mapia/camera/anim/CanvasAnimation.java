package com.mapia.camera.anim;

/**
 * Created by daehyun on 15. 6. 16..
 */



import com.mapia.camera.glrenderer.GLCanvas;

public abstract class CanvasAnimation extends Animation
{
    public abstract void apply(final GLCanvas p0);

    public abstract int getCanvasSaveFlags();
}