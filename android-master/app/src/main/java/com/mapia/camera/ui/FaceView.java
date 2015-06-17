package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.mapia.R;
import com.mapia.camera.CameraActivity;
import com.mapia.camera.CameraScreenNail;
import com.mapia.camera.Util;
import com.mapia.custom.rotatableview.Rotatable;


@TargetApi(14)
public class FaceView extends View implements FocusIndicator, Rotatable
{
    private static final int MSG_SWITCH_FACES = 1;
    private static final int SWITCH_DELAY = 70;
    private static final String TAG = "CAM FaceView";
    private final boolean LOGV;
    private volatile boolean mBlocked;
    private int mColor;
    private int mDisplayOrientation;
    private Camera.Face[] mFaces;
    private final int mFailColor;
    private final int mFocusedColor;
    private final int mFocusingColor;
    private Handler mHandler;
    private Matrix mMatrix;
    private boolean mMirror;
    private int mOrientation;
    private Paint mPaint;
    private boolean mPause;
    private Camera.Face[] mPendingFaces;
    private RectF mRect;
    private boolean mStateSwitchPending;
    private int mUncroppedHeight;
    private int mUncroppedWidth;

    public FaceView(final Context context, final AttributeSet set) {
        super(context, set);
        this.LOGV = false;
        this.mMatrix = new Matrix();
        this.mRect = new RectF();
        this.mStateSwitchPending = false;
        this.mHandler = new Handler() {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {}
                    case 1: {
                        FaceView.this.mStateSwitchPending = false;
                        FaceView.this.mFaces = FaceView.this.mPendingFaces;
                        FaceView.this.invalidate();
                    }
                }
            }
        };
        final Resources resources = this.getResources();
        this.mFocusingColor = resources.getColor(R.color.face_detect_start);
        this.mFocusedColor = resources.getColor(R.color.face_detect_success);
        this.mFailColor = resources.getColor(R.color.face_detect_fail);
        this.mColor = this.mFocusingColor;
        (this.mPaint = new Paint()).setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(resources.getDimension(R.dimen.face_circle_stroke));
    }

    public void clear() {
        this.mColor = this.mFocusingColor;
        this.mFaces = null;
        this.invalidate();
    }

    public boolean faceExists() {
        return this.mFaces != null && this.mFaces.length > 0;
    }

    protected void onDraw(final Canvas canvas) {
        if (!this.mBlocked && this.mFaces != null && this.mFaces.length > 0) {
            int n;
            int n2;
            if (this.mUncroppedWidth == 0) {
                final CameraScreenNail cameraScreenNail = ((CameraActivity)this.getContext()).getCameraScreenNail();
                n = cameraScreenNail.getUncroppedRenderWidth();
                n2 = cameraScreenNail.getUncroppedRenderHeight();
            }
            else {
                n = this.mUncroppedWidth;
                n2 = this.mUncroppedHeight;
            }
            int n3 = 0;
            int n4 = 0;
            Label_0117: {
                if (n2 <= n || (this.mDisplayOrientation != 0 && this.mDisplayOrientation != 180)) {
                    if ((n3 = n) <= (n4 = n2)) {
                        break Label_0117;
                    }
                    if (this.mDisplayOrientation != 90) {
                        n4 = n2;
                        n3 = n;
                        if (this.mDisplayOrientation != 270) {
                            break Label_0117;
                        }
                    }
                }
                n3 = n2;
                n4 = n;
            }
            Util.prepareMatrix(this.mMatrix, this.mMirror, this.mDisplayOrientation, n3, n4);
            final int n5 = (this.getWidth() - n3) / 2;
            final int n6 = (this.getHeight() - n4) / 2;
            canvas.save();
            this.mMatrix.postRotate((float)this.mOrientation);
            canvas.rotate((float)(-this.mOrientation));
            for (int i = 0; i < this.mFaces.length; ++i) {
                if (this.mFaces[i].score >= 50) {
                    this.mRect.set(this.mFaces[i].rect);
                    this.mMatrix.mapRect(this.mRect);
                    this.mPaint.setColor(this.mColor);
                    this.mRect.offset((float)n5, (float)n6);
                    canvas.drawOval(this.mRect, this.mPaint);
                }
            }
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    public void pause() {
        this.mPause = true;
    }

    public void resume() {
        this.mPause = false;
    }

    public void setBlockDraw(final boolean mBlocked) {
        this.mBlocked = mBlocked;
    }

    public void setDisplayOrientation(final int mDisplayOrientation) {
        this.mDisplayOrientation = mDisplayOrientation;
    }

    public void setFaces(final Camera.Face[] array) {
        if (!this.mPause) {
            if (this.mFaces == null || ((array.length <= 0 || this.mFaces.length != 0) && (array.length != 0 || this.mFaces.length <= 0))) {
                if (this.mStateSwitchPending) {
                    this.mStateSwitchPending = false;
                    this.mHandler.removeMessages(1);
                }
                this.mFaces = array;
                this.invalidate();
                return;
            }
            this.mPendingFaces = array;
            if (!this.mStateSwitchPending) {
                this.mStateSwitchPending = true;
                this.mHandler.sendEmptyMessageDelayed(1, 70L);
            }
        }
    }

    public void setMirror(final boolean mMirror) {
        this.mMirror = mMirror;
    }

    public void setOrientation(final int mOrientation, final boolean b) {
        this.mOrientation = mOrientation;
        this.invalidate();
    }

    public void showFail(final boolean b) {
        this.mColor = this.mFailColor;
        this.invalidate();
    }

    public void showStart() {
        this.showStart(false);
    }

    public void showStart(final boolean b) {
        this.mColor = this.mFocusingColor;
        this.invalidate();
    }

    public void showSuccess(final boolean b) {
        this.mColor = this.mFocusedColor;
        this.invalidate();
    }
}