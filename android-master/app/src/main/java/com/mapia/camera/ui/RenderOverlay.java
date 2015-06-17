package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RenderOverlay extends FrameLayout
{
    private static final String TAG = "CAM_Overlay";
    private List<Renderer> mClients;
    private int[] mPosition;
    private RenderView mRenderView;
    private List<Renderer> mTouchClients;

    public RenderOverlay(final Context context, final AttributeSet set) {
        super(context, set);
        this.mPosition = new int[2];
        this.addView((View)(this.mRenderView = new RenderView(context)), (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
        this.mClients = new ArrayList<Renderer>(10);
        this.mTouchClients = new ArrayList<Renderer>(10);
        this.setWillNotDraw(false);
    }

    private void adjustPosition() {
        this.getLocationInWindow(this.mPosition);
    }

    public void addRenderer(final int n, final Renderer renderer) {
        this.mClients.add(n, renderer);
        renderer.setOverlay(this);
        renderer.layout(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
    }

    public void addRenderer(final Renderer renderer) {
        this.mClients.add(renderer);
        renderer.setOverlay(this);
        if (renderer.handlesTouch()) {
            this.mTouchClients.add(0, renderer);
        }
        renderer.layout(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
    }

    public boolean directDispatchTouch(final MotionEvent motionEvent, final Renderer touchTarget) {
        this.mRenderView.setTouchTarget(touchTarget);
        final boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        this.mRenderView.setTouchTarget(null);
        return dispatchTouchEvent;
    }

    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        return false;
    }

    public int getClientSize() {
        return this.mClients.size();
    }

    public int getWindowPositionX() {
        return this.mPosition[0];
    }

    public int getWindowPositionY() {
        return this.mPosition[1];
    }

    public void remove(final Renderer renderer) {
        this.mClients.remove(renderer);
        renderer.setOverlay(null);
    }

    public void update() {
        this.mRenderView.invalidate();
    }

    private class RenderView extends View
    {
        private Renderer mTouchTarget;

        public RenderView(final Context context) {
            super(context);
            this.setWillNotDraw(false);
        }

        public void draw(final Canvas canvas) {
            super.draw(canvas);
            if (RenderOverlay.this.mClients != null) {
                int n = 0;
                for (final Renderer renderer : RenderOverlay.this.mClients) {
                    renderer.draw(canvas);
                    if (n != 0 || ((OverlayRenderer)renderer).isVisible()) {
                        n = 1;
                    }
                    else {
                        n = 0;
                    }
                }
                if (n != 0) {
                    this.invalidate();
                }
            }
        }

        public void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            RenderOverlay.this.adjustPosition();
            super.onLayout(b, n, n2, n3, n4);
            if (RenderOverlay.this.mClients != null) {
                final Iterator<Renderer> iterator = RenderOverlay.this.mClients.iterator();
                while (iterator.hasNext()) {
                    iterator.next().layout(n, n2, n3, n4);
                }
            }
        }

        public boolean onTouchEvent(final MotionEvent motionEvent) {
            boolean onTouchEvent;
            if (this.mTouchTarget != null) {
                onTouchEvent = this.mTouchTarget.onTouchEvent(motionEvent);
            }
            else {
                if (RenderOverlay.this.mTouchClients == null) {
                    return false;
                }
                boolean b = false;
                final Iterator iterator = RenderOverlay.this.mTouchClients.iterator();
                while (true) {
                    onTouchEvent = b;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    b |= ((Renderer)iterator.next()).onTouchEvent(motionEvent);
                }
            }
            return onTouchEvent;
        }

        public void setTouchTarget(final Renderer mTouchTarget) {
            this.mTouchTarget = mTouchTarget;
        }
    }

    interface Renderer
    {
        void draw(Canvas p0);

        boolean handlesTouch();

        void layout(int p0, int p1, int p2, int p3);

        boolean onTouchEvent(MotionEvent p0);

        void setOverlay(RenderOverlay p0);
    }
}