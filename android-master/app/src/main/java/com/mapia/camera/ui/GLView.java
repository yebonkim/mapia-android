package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Rect;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.mapia.camera.anim.CanvasAnimation;
import com.mapia.camera.common.Utils;
import com.mapia.camera.glrenderer.GLCanvas;

import java.util.ArrayList;


public class GLView
{
    private static final int FLAG_INVISIBLE = 1;
    private static final int FLAG_LAYOUT_REQUESTED = 4;
    private static final int FLAG_SET_MEASURED_SIZE = 2;
    public static final int INVISIBLE = 1;
    private static final String TAG = "GLView";
    public static final int VISIBLE = 0;
    private CanvasAnimation mAnimation;
    private float[] mBackgroundColor;
    protected final Rect mBounds;
    private ArrayList<GLView> mComponents;
    private int mLastHeightSpec;
    private int mLastWidthSpec;
    protected int mMeasuredHeight;
    protected int mMeasuredWidth;
    private GLView mMotionTarget;
    protected final Rect mPaddings;
    protected GLView mParent;
    private GLRoot mRoot;
    protected int mScrollHeight;
    protected int mScrollWidth;
    protected int mScrollX;
    protected int mScrollY;
    private int mViewFlags;

    public GLView() {
        super();
        this.mBounds = new Rect();
        this.mPaddings = new Rect();
        this.mViewFlags = 0;
        this.mMeasuredWidth = 0;
        this.mMeasuredHeight = 0;
        this.mLastWidthSpec = -1;
        this.mLastHeightSpec = -1;
        this.mScrollY = 0;
        this.mScrollX = 0;
        this.mScrollHeight = 0;
        this.mScrollWidth = 0;
    }

    private void removeOneComponent(final GLView glView) {
        if (this.mMotionTarget == glView) {
            final long uptimeMillis = SystemClock.uptimeMillis();
            final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            this.dispatchTouchEvent(obtain);
            obtain.recycle();
        }
        glView.onDetachFromRoot();
        glView.mParent = null;
    }

    private boolean setBounds(final int n, final int n2, final int n3, final int n4) {
        final boolean b = n3 - n != this.mBounds.right - this.mBounds.left || n4 - n2 != this.mBounds.bottom - this.mBounds.top;
        this.mBounds.set(n, n2, n3, n4);
        return b;
    }

    public void addComponent(final GLView glView) {
        if (glView.mParent != null) {
            throw new IllegalStateException();
        }
        if (this.mComponents == null) {
            this.mComponents = new ArrayList<GLView>();
        }
        this.mComponents.add(glView);
        glView.mParent = this;
        if (this.mRoot != null) {
            glView.onAttachToRoot(this.mRoot);
        }
    }

    public void attachToRoot(final GLRoot glRoot) {
        Utils.assertTrue(this.mParent == null && this.mRoot == null);
        this.onAttachToRoot(glRoot);
    }

    public Rect bounds() {
        return this.mBounds;
    }

    public void detachFromRoot() {
        Utils.assertTrue(this.mParent == null && this.mRoot != null);
        this.onDetachFromRoot();
    }

    protected boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        final int n = (int)motionEvent.getX();
        final int n2 = (int)motionEvent.getY();
        final int action = motionEvent.getAction();
        if (this.mMotionTarget != null) {
            if (action != 0) {
                this.dispatchTouchEvent(motionEvent, n, n2, this.mMotionTarget, false);
                if (action == 3 || action == 1) {
                    this.mMotionTarget = null;
                }
                return true;
            }
            final MotionEvent obtain = MotionEvent.obtain(motionEvent);
            obtain.setAction(3);
            this.dispatchTouchEvent(obtain, n, n2, this.mMotionTarget, false);
            this.mMotionTarget = null;
        }
        if (action == 0) {
            for (int i = this.getComponentCount() - 1; i >= 0; --i) {
                final GLView component = this.getComponent(i);
                if (component.getVisibility() == 0 && this.dispatchTouchEvent(motionEvent, n, n2, component, true)) {
                    this.mMotionTarget = component;
                    return true;
                }
            }
        }
        return this.onTouch(motionEvent);
    }

    protected boolean dispatchTouchEvent(final MotionEvent motionEvent, final int n, final int n2, final GLView glView, final boolean b) {
        final Rect mBounds = glView.mBounds;
        final int left = mBounds.left;
        final int top = mBounds.top;
        if (!b || mBounds.contains(n, n2)) {
            motionEvent.offsetLocation((float)(-left), (float)(-top));
            if (glView.dispatchTouchEvent(motionEvent)) {
                motionEvent.offsetLocation((float)left, (float)top);
                return true;
            }
            motionEvent.offsetLocation((float)left, (float)top);
        }
        return false;
    }

    void dumpTree(final String s) {
        for (int i = 0; i < this.getComponentCount(); ++i) {
            this.getComponent(i).dumpTree(s + "....");
        }
    }

    public float[] getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public boolean getBoundsOf(final GLView glView, final Rect rect) {
        int n = 0;
        int n2 = 0;
        for (GLView mParent = glView; mParent != this; mParent = mParent.mParent) {
            if (mParent == null) {
                return false;
            }
            final Rect mBounds = mParent.mBounds;
            n += mBounds.left;
            n2 += mBounds.top;
        }
        rect.set(n, n2, glView.getWidth() + n, glView.getHeight() + n2);
        return true;
    }

    public GLView getComponent(final int n) {
        if (this.mComponents == null) {
            throw new ArrayIndexOutOfBoundsException(n);
        }
        return this.mComponents.get(n);
    }

    public int getComponentCount() {
        if (this.mComponents == null) {
            return 0;
        }
        return this.mComponents.size();
    }

    public GLRoot getGLRoot() {
        return this.mRoot;
    }

    public int getHeight() {
        return this.mBounds.bottom - this.mBounds.top;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public Rect getPaddings() {
        return this.mPaddings;
    }

    public int getVisibility() {
        if ((this.mViewFlags & 0x1) == 0x0) {
            return 0;
        }
        return 1;
    }

    public int getWidth() {
        return this.mBounds.right - this.mBounds.left;
    }

    public void invalidate() {
        final GLRoot glRoot = this.getGLRoot();
        if (glRoot != null) {
            glRoot.requestRender();
        }
    }

    public void layout(final int n, final int n2, final int n3, final int n4) {
        final boolean setBounds = this.setBounds(n, n2, n3, n4);
        this.mViewFlags &= 0xFFFFFFFB;
        this.onLayout(setBounds, n, n2, n3, n4);
    }

    public void lockRendering() {
        if (this.mRoot != null) {
            this.mRoot.lockRenderThread();
        }
    }

    public void measure(final int mLastWidthSpec, final int mLastHeightSpec) {
        if (mLastWidthSpec != this.mLastWidthSpec || mLastHeightSpec != this.mLastHeightSpec || (this.mViewFlags & 0x4) != 0x0) {
            this.mLastWidthSpec = mLastWidthSpec;
            this.mLastHeightSpec = mLastHeightSpec;
            this.mViewFlags &= 0xFFFFFFFD;
            this.onMeasure(mLastWidthSpec, mLastHeightSpec);
            if ((this.mViewFlags & 0x2) == 0x0) {
                throw new IllegalStateException(this.getClass().getName() + " should call setMeasuredSize() in onMeasure()");
            }
        }
    }

    protected void onAttachToRoot(final GLRoot mRoot) {
        this.mRoot = mRoot;
        for (int i = 0; i < this.getComponentCount(); ++i) {
            this.getComponent(i).onAttachToRoot(mRoot);
        }
    }

    protected void onDetachFromRoot() {
        for (int i = 0; i < this.getComponentCount(); ++i) {
            this.getComponent(i).onDetachFromRoot();
        }
        this.mRoot = null;
    }

    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
    }

    protected void onMeasure(final int n, final int n2) {
    }

    protected boolean onTouch(final MotionEvent motionEvent) {
        return false;
    }

    protected void onVisibilityChanged(final int n) {
        for (int i = 0; i < this.getComponentCount(); ++i) {
            final GLView component = this.getComponent(i);
            if (component.getVisibility() == 0) {
                component.onVisibilityChanged(n);
            }
        }
    }

    public void removeAllComponents() {
        for (int i = 0; i < this.mComponents.size(); ++i) {
            this.removeOneComponent(this.mComponents.get(i));
        }
        this.mComponents.clear();
    }

    public boolean removeComponent(final GLView glView) {
        if (this.mComponents != null && this.mComponents.remove(glView)) {
            this.removeOneComponent(glView);
            return true;
        }
        return false;
    }

    protected void render(final GLCanvas glCanvas) {
        this.renderBackground(glCanvas);
        glCanvas.save();
        for (int i = 0; i < this.getComponentCount(); ++i) {
            this.renderChild(glCanvas, this.getComponent(i));
        }
        glCanvas.restore();
    }

    protected void renderBackground(final GLCanvas glCanvas) {
        if (this.mBackgroundColor != null) {
            glCanvas.clearBuffer(this.mBackgroundColor);
        }
    }

    protected void renderChild(final GLCanvas glCanvas, final GLView glView) {
        if (glView.getVisibility() != 0 && glView.mAnimation == null) {
            return;
        }
        final int n = glView.mBounds.left - this.mScrollX;
        final int n2 = glView.mBounds.top - this.mScrollY;
        glCanvas.translate(n, n2);
        final CanvasAnimation mAnimation = glView.mAnimation;
        if (mAnimation != null) {
            glCanvas.save(mAnimation.getCanvasSaveFlags());
            if (mAnimation.calculate(AnimationTime.get())) {
                this.invalidate();
            }
            else {
                glView.mAnimation = null;
            }
            mAnimation.apply(glCanvas);
        }
        glView.render(glCanvas);
        if (mAnimation != null) {
            glCanvas.restore();
        }
        glCanvas.translate(-n, -n2);
    }

    public void requestLayout() {
        this.mViewFlags |= 0x4;
        this.mLastHeightSpec = -1;
        this.mLastWidthSpec = -1;
        if (this.mParent != null) {
            this.mParent.requestLayout();
        }
        else {
            final GLRoot glRoot = this.getGLRoot();
            if (glRoot != null) {
                glRoot.requestLayoutContentPane();
            }
        }
    }

    public void setBackgroundColor(final float[] mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    protected void setMeasuredSize(final int mMeasuredWidth, final int mMeasuredHeight) {
        this.mViewFlags |= 0x2;
        this.mMeasuredWidth = mMeasuredWidth;
        this.mMeasuredHeight = mMeasuredHeight;
    }

    public void setVisibility(final int n) {
        if (n == this.getVisibility()) {
            return;
        }
        if (n == 0) {
            this.mViewFlags &= 0xFFFFFFFE;
        }
        else {
            this.mViewFlags |= 0x1;
        }
        this.onVisibilityChanged(n);
        this.invalidate();
    }

    public void startAnimation(final CanvasAnimation mAnimation) {
        final GLRoot glRoot = this.getGLRoot();
        if (glRoot == null) {
            throw new IllegalStateException();
        }
        this.mAnimation = mAnimation;
        if (this.mAnimation != null) {
            this.mAnimation.start();
            glRoot.registerLaunchedAnimation(this.mAnimation);
        }
        this.invalidate();
    }

    public void unlockRendering() {
        if (this.mRoot != null) {
            this.mRoot.unlockRenderThread();
        }
    }

    public interface OnClickListener
    {
        void onClick(GLView p0);
    }
}