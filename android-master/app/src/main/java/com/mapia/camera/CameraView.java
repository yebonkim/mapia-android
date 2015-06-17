package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import com.mapia.camera.glrenderer.GLCanvas;
import com.mapia.camera.ui.GLView;
import com.mapia.camera.ui.ScreenNail;


public class CameraView extends GLView
{
    private ScreenNail mScreenNail;

    @Override
    protected void render(final GLCanvas glCanvas) {
        this.mScreenNail.draw(glCanvas, 0, 0, this.getWidth(), this.getHeight());
    }

    public void setScreenNail(final ScreenNail mScreenNail) {
        this.mScreenNail = mScreenNail;
    }
}
