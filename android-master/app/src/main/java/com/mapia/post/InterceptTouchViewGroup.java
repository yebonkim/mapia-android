package com.mapia.post;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.mapia.R;
import com.mapia.util.DeviceUtils;


public class InterceptTouchViewGroup extends RelativeLayout
{
    private View mEditPostView;
    private InputMethodManager mInputMethodManager;
    private boolean mIntercept;
    private boolean mIsExpanded;
    private boolean mIsMoved;
    private boolean mIsScrolling;
    private View mNavigationBar;
    private float mPivotYEditPost;
    private float mPivotYThumbnail;
    private float mScrollYThreshold;
    private float mStartingY;
    private View mThumbnailView;

    public InterceptTouchViewGroup(final Context context, final AttributeSet set) {
        super(context, set);
        this.mIntercept = false;
        this.mIsExpanded = false;
        this.mIsMoved = false;
        this.mIsScrolling = false;
        this.mNavigationBar = this.findViewById(R.id.topNavigationBar);
    }

    private void hideSoftKeyboard() {
        if (this.mEditPostView != null) {
            this.mInputMethodManager.hideSoftInputFromWindow(this.mEditPostView.getWindowToken(), 0);
        }
    }

    private boolean isIn(final View view, float n, final float n2) {
        if (view != null && n >= view.getLeft() && n <= view.getRight()) {
            if (this.mIsExpanded) {
//                n = BitmapUtils.convertDipToPixelInt(60.0f);
//                final float n3 = BitmapUtils.convertDipToPixelInt(60.0f) + view.getHeight();
//                if (n2 < n || n2 > n3) {
//                    return false;
//                }
            }
            else if (n2 < view.getTop() || n2 > view.getBottom()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public boolean isMoved() {
        return this.mIsMoved;
    }

    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        boolean expand = true;
        if (this.mIntercept) {
            final int action = motionEvent.getAction();
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            switch (action) {
                default: {
                    return false;
                }
                case 0: {
                    if (this.isIn(this.mThumbnailView, x, y)) {
                        this.mPivotYThumbnail = this.mThumbnailView.getTranslationY();
                        this.mPivotYEditPost = this.mEditPostView.getTranslationY();
                        this.mIsScrolling = true;
                        this.mStartingY = y;
                        return false;
                    }
                    break;
                }
                case 1:
                case 3: {
                    final float n = this.mStartingY - y;
                    if (n == 0.0f) {
                        return this.mIsScrolling = false;
                    }
                    if (this.isIn(this.mThumbnailView, x, y) && this.mStartingY == y && !this.isIn(this.mNavigationBar, x, y)) {
                        if (this.mIsExpanded) {
                            expand = false;
                        }
                        this.setExpand(expand);
                        return this.mIsScrolling = false;
                    }
                    if (this.mIsScrolling) {
                        if (Math.abs(n) >= this.mScrollYThreshold) {
                            if (n > 0.0f && this.mIsExpanded) {
                                this.setExpand(false);
                            }
                            else {
                                this.setExpand(this.mIsExpanded);
                            }
                            if (n < 0.0f && !this.mIsExpanded) {
                                this.setExpand(true);
                            }
                            else {
                                this.setExpand(this.mIsExpanded);
                            }
                        }
                        else {
                            this.setExpand(this.mIsExpanded);
                        }
                        return this.mIsScrolling = false;
                    }
                    break;
                }
                case 2: {
                    if (!this.mIsScrolling) {
                        break;
                    }
                    if (this.mIsExpanded) {
                        final float n2 = this.mStartingY - y;
                        this.mThumbnailView.setTranslationY(this.mPivotYThumbnail - n2);
                        this.mEditPostView.setTranslationY(this.mPivotYEditPost - n2);
                        this.mIsMoved = true;
                        return false;
                    }
                    final float n3 = this.mStartingY - y;
                    this.mThumbnailView.setTranslationY(this.mPivotYThumbnail - n3);
                    this.mEditPostView.setTranslationY(this.mPivotYEditPost - n3);
                    this.mIsMoved = true;
                    return false;
                }
            }
        }
        return false;
    }

    public void setEditPostView(final View mEditPostView, final InputMethodManager mInputMethodManager) {
        this.mEditPostView = mEditPostView;
        this.mInputMethodManager = mInputMethodManager;
    }

    public void setExpand(final boolean b) {
        float n;
        if (b) {
            n = this.mThumbnailView.getHeight();// - BitmapUtils.convertDipToPixelInt(33.0f);
            this.mIsExpanded = true;
        }
        else {
            n = 0.0f;
            this.mIsExpanded = false;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mThumbnailView, "translationY", new float[] { n });
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.mEditPostView, "translationY", new float[] { n });
        final AnimatorSet set = new AnimatorSet();
        set.setDuration(200L);
        set.play((Animator)ofFloat).with((Animator)ofFloat2);
        set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        set.start();
        if (b) {
            this.hideSoftKeyboard();
        }
    }

    public void setIntercept(final boolean mIntercept) {
        this.mIntercept = mIntercept;
    }

    public void setNavigationBar(final View mNavigationBar) {
        this.mNavigationBar = mNavigationBar;
    }

    public void setThumbnailView(final View mThumbnailView) {
        this.mThumbnailView = mThumbnailView;
        this.mScrollYThreshold = DeviceUtils.getDeviceHeight() / 7;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_THRESHOLD = 0;
        private static final int SWIPE_VELOCITY_THRESHOLD = 0;

        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, float n, final float n2) {
            n = motionEvent2.getY() - motionEvent.getY();
            Math.abs(n);
            if (n > 0.0f) {
                InterceptTouchViewGroup.this.setExpand(true);
                return true;
            }
            InterceptTouchViewGroup.this.setExpand(false);
            return true;
        }
    }
}