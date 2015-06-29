package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mapia.R;

public class CountDownView extends FrameLayout
{
    private static final int SET_TIMER_TEXT = 1;
    private static final String TAG = "CAM_CountDownView";
    private int mBeepOnce;
    private int mBeepTwice;
    private final Handler mHandler;
    private OnCountDownFinishedListener mListener;
    private boolean mPlaySound;
    private ImageView mRemainingSecondsView;
    private int mRemainingSecs;
    private SoundPool mSoundPool;

    public CountDownView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mRemainingSecs = 0;
        this.mHandler = new MainHandler();
        this.mSoundPool = new SoundPool(1, 5, 0);
        this.mBeepOnce = this.mSoundPool.load(context, R.drawable.cam_thumbnail, 1);
        this.mBeepTwice = this.mSoundPool.load(context, R.raw.beep_twice, 1);
    }

    private void remainingSecondsChanged(final int mRemainingSecs) {
        this.mRemainingSecs = mRemainingSecs;
        if (mRemainingSecs == 0) {
            this.setVisibility(View.VISIBLE);
            this.mListener.onCountDownFinished();
            return;
        }
        switch (mRemainingSecs) {
            case 1: {
                this.mRemainingSecondsView.setImageResource(R.drawable.cam_timer_n1);
                break;
            }
            case 2: {
                this.mRemainingSecondsView.setImageResource(R.drawable.cam_timer_n2);
                break;
            }
            case 3: {
                this.mRemainingSecondsView.setImageResource(R.drawable.cam_timer_n3);
                break;
            }
            case 4: {
                this.mRemainingSecondsView.setImageResource(R.drawable.cam_timer_n4);
                break;
            }
            case 5: {
                this.mRemainingSecondsView.setImageResource(R.drawable.cam_timer_n5);
                break;
            }
        }
        if (this.mPlaySound) {
            if (mRemainingSecs == 1) {
                this.mSoundPool.play(this.mBeepTwice, 1.0f, 1.0f, 0, 0, 1.0f);
            }
            else if (mRemainingSecs <= 3) {
                this.mSoundPool.play(this.mBeepOnce, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
        this.mHandler.sendEmptyMessageDelayed(1, 1000L);
    }

    public void cancelCountDown() {
        if (this.mRemainingSecs > 0) {
            this.mRemainingSecs = 0;
            this.mHandler.removeMessages(1);
            this.setVisibility(View.VISIBLE);
        }
    }

    public boolean isCountingDown() {
        return this.mRemainingSecs > 0;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mRemainingSecondsView = (ImageView)this.findViewById(R.id.remaining_seconds);
    }

    public void setCountDownFinishedListener(final OnCountDownFinishedListener mListener) {
        this.mListener = mListener;
    }

    public void startCountDown(final int n, final boolean mPlaySound) {
        if (n <= 0) {
            Log.w("CAM_CountDownView", "Invalid input for countdown timer: " + n + " seconds");
            return;
        }
        this.setVisibility(View.VISIBLE);
        this.mPlaySound = mPlaySound;
        this.remainingSecondsChanged(n);
    }

    private class MainHandler extends Handler
    {
        public void handleMessage(final Message message) {
            if (message.what == 1) {
                CountDownView.this.remainingSecondsChanged(CountDownView.this.mRemainingSecs - 1);
            }
        }
    }

    public interface OnCountDownFinishedListener
    {
        void onCountDownFinished();
    }
}