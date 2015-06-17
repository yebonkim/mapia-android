package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.os.SystemClock;
import android.util.Log;

import com.mapia.camera.glrenderer.GLCanvas;
import com.mapia.camera.glrenderer.RawTexture;


public class SwitchAnimManager
{
    private static final float ANIMATION_DURATION = 400.0f;
    public static final float INITIAL_DARKEN_ALPHA = 0.8f;
    private static final String TAG = "SwitchAnimManager";
    private static final float ZOOM_DELTA_PREVIEW = 0.2f;
    private static final float ZOOM_DELTA_REVIEW = 0.5f;
    private long mAnimStartTime;
    private int mPreviewFrameLayoutWidth;
    private int mReviewDrawingHeight;
    private int mReviewDrawingWidth;

    public boolean drawAnimation(final GLCanvas glCanvas, int round, int round2, int round3, int round4, final CameraScreenNail cameraScreenNail, final RawTexture rawTexture) {
        final long n = SystemClock.uptimeMillis() - this.mAnimStartTime;
        if (n > 400.0f) {
            return false;
        }
        final float alpha = n / 400.0f;
        final float n2 = round + round3 / 2.0f;
        final float n3 = round2 + round4 / 2.0f;
        final float n4 = 1.0f - 0.2f * (1.0f - alpha);
        final float n5 = round3 * n4;
        final float n6 = round4 * n4;
        round = Math.round(n2 - n5 / 2.0f);
        round2 = Math.round(n3 - n6 / 2.0f);
        final float n7 = 1.0f + 0.5f * alpha;
        float n8 = 1.0f;
        if (this.mPreviewFrameLayoutWidth != 0) {
            n8 = round3 / this.mPreviewFrameLayoutWidth;
        }
        else {
            Log.e("SwitchAnimManager", "mPreviewFrameLayoutWidth is 0.");
        }
        final float n9 = this.mReviewDrawingWidth * n7 * n8;
        final float n10 = this.mReviewDrawingHeight * n7 * n8;
        round3 = Math.round(n2 - n9 / 2.0f);
        round4 = Math.round(n3 - n10 / 2.0f);
        final float alpha2 = glCanvas.getAlpha();
        glCanvas.setAlpha(alpha);
        cameraScreenNail.directDraw(glCanvas, round, round2, Math.round(n5), Math.round(n6));
        glCanvas.setAlpha((1.0f - alpha) * 0.8f);
        rawTexture.draw(glCanvas, round3, round4, Math.round(n9), Math.round(n10));
        glCanvas.setAlpha(alpha2);
        return true;
    }

    public boolean drawDarkPreview(final GLCanvas glCanvas, int round, int round2, final int n, final int n2, final RawTexture rawTexture) {
        final float n3 = round;
        final float n4 = n / 2.0f;
        final float n5 = round2;
        final float n6 = n2 / 2.0f;
        float n7 = 1.0f;
        if (this.mPreviewFrameLayoutWidth != 0) {
            n7 = n / this.mPreviewFrameLayoutWidth;
        }
        else {
            Log.e("SwitchAnimManager", "mPreviewFrameLayoutWidth is 0.");
        }
        final float n8 = this.mReviewDrawingWidth * n7;
        final float n9 = this.mReviewDrawingHeight * n7;
        round = Math.round(n3 + n4 - n8 / 2.0f);
        round2 = Math.round(n5 + n6 - n9 / 2.0f);
        final float alpha = glCanvas.getAlpha();
        glCanvas.setAlpha(0.8f);
        rawTexture.draw(glCanvas, round, round2, Math.round(n8), Math.round(n9));
        glCanvas.setAlpha(alpha);
        return true;
    }

    public void setPreviewFrameLayoutSize(final int mPreviewFrameLayoutWidth, final int n) {
        this.mPreviewFrameLayoutWidth = mPreviewFrameLayoutWidth;
    }

    public void setReviewDrawingSize(final int mReviewDrawingWidth, final int mReviewDrawingHeight) {
        this.mReviewDrawingWidth = mReviewDrawingWidth;
        this.mReviewDrawingHeight = mReviewDrawingHeight;
    }

    public void startAnimation() {
        this.mAnimStartTime = SystemClock.uptimeMillis();
    }
}