package com.mapia.videoplayer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mapia.R;
import com.mapia.api.QueryManager;
import com.mapia.common.CompatiblityUtility;
import com.mapia.util.ImageViewUtils;
import com.volley.MapiaRequest;
import com.volley.MapiaVolley;

import org.json.JSONException;
import org.json.JSONObject;

public class MapiaVideoPlayer extends RelativeLayout
{
    public static final int OP_PAUSE = 0;
    public static final int OP_PLAY = 1;
    private final int ERR_VIDEO_PLAYER;
    private final int ERR_VIDEO_PLAY_DEFAULT;
    private boolean mAutoPlay;
    private View mBtnPlay;
    private View mBtnSound;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private boolean mDisableTouch;
    private View mErrLayout;
    private int mFrom;
    private boolean mIsSoundOn;
    private View mLoading;
    private boolean mLooping;
    private MapiaVideoPlayerEvent mMapiaVideoPlayerEvent;
    private MediaPlayer.OnPreparedListener mPreparedListener;
    private int mRequestedOp;
    private View mRootLayout;
    private ImageView mThumbnail;
    private float mTouchDownX;
    private float mTouchDownY;
    private MapiaVideoView mVideoView;

    public MapiaVideoPlayer(final Context context) {
        this(context, null, 0);
    }

    public MapiaVideoPlayer(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public MapiaVideoPlayer(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.ERR_VIDEO_PLAYER = -9999;
        this.ERR_VIDEO_PLAY_DEFAULT = -10000;
        this.mRequestedOp = 0;
        this.mPreparedListener = (MediaPlayer.OnPreparedListener)new MediaPlayer.OnPreparedListener() {
            public void onPrepared(final MediaPlayer mediaPlayer) {
                MapiaVideoPlayer.this.mLoading.setVisibility(View.INVISIBLE);
                MapiaVideoPlayer.this.mErrLayout.setVisibility(View.INVISIBLE);
                MapiaVideoPlayer.this.mVideoView.setSound(MapiaVideoPlayer.this.mIsSoundOn);
                if (MapiaVideoPlayer.this.mAutoPlay && MapiaVideoPlayer.this.mRequestedOp == 1) {
                    MapiaVideoPlayer.this.play();
                    return;
                }
                MapiaVideoPlayer.this.showVideoControls();
            }
        };
        this.mCompletionListener = (MediaPlayer.OnCompletionListener)new MediaPlayer.OnCompletionListener() {
            public void onCompletion(final MediaPlayer mediaPlayer) {
                if (mediaPlayer.isLooping()) {
                    MapiaVideoPlayer.this.hidePlayBtn();
                    MapiaVideoPlayer.this.play();
                }
                else {
                    MapiaVideoPlayer.this.showVideoControls();
                }
                MapiaVideoPlayer.this.hideLoading();
            }
        };
        this.mRootLayout = LayoutInflater.from(context).inflate(R.layout.mapia_video_player, (ViewGroup)null);
        this.mVideoView = (MapiaVideoView)this.mRootLayout.findViewById(R.id.videoView);
        this.mBtnSound = this.mRootLayout.findViewById(R.id.btnSound);
        this.mThumbnail = (ImageView)this.mRootLayout.findViewById(R.id.thumbnail);
        this.mBtnPlay = this.mRootLayout.findViewById(R.id.btnVideoController);
        this.mLoading = this.mRootLayout.findViewById(R.id.loadingVideo);
        this.mErrLayout = this.mRootLayout.findViewById(R.id.errLayout);
        this.init();
        this.addView(this.mRootLayout);
    }

    private void hideError() {
        this.showVideoControls();
        this.mLoading.setVisibility(View.VISIBLE);
        this.mErrLayout.setVisibility(View.INVISIBLE);
    }

    private void hidePlayBtn() {
        this.mBtnPlay.setVisibility(View.INVISIBLE);
    }

    private void hideVideoControls() {
        this.mBtnPlay.setVisibility(View.INVISIBLE);
        this.mBtnSound.setVisibility(View.INVISIBLE);
    }

    private void init() {
        this.mBtnPlay.setSelected(this.mVideoView.isPlaying());
        this.mBtnPlay.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (MapiaVideoPlayer.this.mFrom == 2) {
//                    AceUtils.nClick(NClicks.MYFEED_PLAY);
                }
                if (MapiaVideoPlayer.this.mVideoView.isPlaying()) {
                    MapiaVideoPlayer.this.pause();
                    return;
                }
                MapiaVideoPlayer.this.play();
            }
        });
        this.mVideoView.setOnPreparedListener(this.mPreparedListener);
        this.mVideoView.setOnCompetionListener(this.mCompletionListener);
        this.mBtnSound.setSelected(true);
        this.mBtnSound.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(View v) {
                boolean selected = true;
                final boolean selected2 = MapiaVideoPlayer.this.mBtnSound.isSelected();
                if (MapiaVideoPlayer.this.mFrom == 2) {
//                    AceUtils.nClick(NClicks.MYFEED_MUTE);
                }
                if (selected2) {
                    MapiaVideoPlayer.this.setSound(false);
                }
                else {
                    MapiaVideoPlayer.this.setSound(true);
                }
                v = MapiaVideoPlayer.this.mBtnSound;
                if (selected2) {
                    selected = false;
                }
                v.setSelected(selected);
            }
        });
        this.mVideoView.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                boolean b2;
                final boolean b = b2 = true;
                switch (motionEvent.getAction()) {
                    default: {
                        b2 = false;
                        return b2;
                    }
                    case 2: {
                        return b2;
                    }
                    case 0: {
                        MapiaVideoPlayer.this.mTouchDownX = motionEvent.getX();
                        MapiaVideoPlayer.this.mTouchDownY = motionEvent.getY();
                        return true;
                    }
                    case 1:
                    case 3: {
                        final float abs = Math.abs(motionEvent.getX() - MapiaVideoPlayer.this.mTouchDownX);
                        final float abs2 = Math.abs(motionEvent.getY() - MapiaVideoPlayer.this.mTouchDownY);
                        b2 = b;
                        if (abs >= 30.0f) {
                            return b2;
                        }
                        b2 = b;
                        if (abs2 >= 30.0f) {
                            return b2;
                        }
                        if (MapiaVideoPlayer.this.mMapiaVideoPlayerEvent != null) {
                            MapiaVideoPlayer.this.mMapiaVideoPlayerEvent.onSigleTap();
                        }
                        b2 = b;
                        if (MapiaVideoPlayer.this.mVideoView.isPlaying()) {
                            MapiaVideoPlayer.this.mVideoView.pause();
                            MapiaVideoPlayer.this.showVideoControls();
                            return true;
                        }
                        return b2;
                    }
                }
            }
        });
        this.hideVideoControls();
        this.mLoading.bringToFront();
    }

    private void requestVideoUrl(final long n, final long n2, final VideoUrlEvent videoUrlEvent) {
        final MapiaRequest mapiaRequest = new MapiaRequest(QueryManager.makeGetVideoUrl(n, n2), (JSONObject)null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (!"error".equalsIgnoreCase(jsonObject.getString("resultStatus"))) {
                        final String videoUrl = videoUrlEvent.toString(); //ah...
                        MapiaVideoPlayer.this.setVideoUrl(videoUrl);
                        if (videoUrlEvent != null) {
                            videoUrlEvent.onVideoUrlDownloaded();
                        }
                        MapiaVideoPlayer.this.mVideoView.setVisibility(View.VISIBLE);
                    }
                    jsonObject = jsonObject.getJSONObject("result");
                    final int int1 = jsonObject.getInt("errorCode");
                    final String string = jsonObject.getString("errorMessage");
                    if (int1 == 4004) {
                        MapiaVideoPlayer.this.showError(int1, string);
                        return;
                    }
                    final int int2 = Integer.parseInt(string);
                    MapiaVideoPlayer.this.mVideoView.setVisibility(View.INVISIBLE);
                    MapiaVideoPlayer.this.showError(int2);
                    return;
                }
                catch (JSONException ex) {
                    MapiaVideoPlayer.this.showError(-10000);
                    return;
                }
                catch (NumberFormatException ex2) {
                    MapiaVideoPlayer.this.showError(-10000);
                    return;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                volleyError.printStackTrace();
                MapiaVideoPlayer.this.showError(-10000);
            }
        });
        mapiaRequest.setRetryPolicy(new DefaultRetryPolicy(100000, 1, 1.0f));
        MapiaVolley.getRequestQueue().add(mapiaRequest);
    }

    private void showError(final int n) {
        this.hideVideoControls();
        this.mLoading.setVisibility(View.INVISIBLE);
        String text = null;
        switch (n) {
            default: {
                text = this.getContext().getString(R.string.video_play_err_default);
                break;
            }
            case -1: {
                text = this.getContext().getString(R.string.video_play_err_1);
                break;
            }
            case -2: {
                text = this.getContext().getString(R.string.video_play_err_2);
                break;
            }
            case -3: {
                text = this.getContext().getString(R.string.video_play_err_3);
                break;
            }
            case -4: {
                text = this.getContext().getString(R.string.video_play_err_4);
                break;
            }
            case -9: {
                text = this.getContext().getString(R.string.video_play_err_9);
                break;
            }
            case -26:
            case -25:
            case -24:
            case -23:
            case -22:
            case -21:
            case -20:
            case -19:
            case -18:
            case -17:
            case -16:
            case -15:
            case -14:
            case -13:
            case -12:
            case -11:
            case -10: {
                text = this.getContext().getString(R.string.video_play_err_10_26);
                break;
            }
            case -600: {
                text = this.getContext().getString(R.string.video_play_err_600);
                break;
            }
            case -700: {
                text = this.getContext().getString(R.string.video_play_err_700);
                break;
            }
            case -1005:
            case -1004: {
                ((TextView)this.mErrLayout.findViewById(R.id.errTitle)).setText((CharSequence)this.getResources().getString(R.string.video_player_preparing));
                text = this.getContext().getString(R.string.video_play_err_1004_1005);
                break;
            }
            case -10000: {
                text = this.getContext().getString(R.string.video_player_err_default);
                break;
            }
        }
        ((TextView)this.mErrLayout.findViewById(R.id.errMessage)).setText((CharSequence)text);
        this.mErrLayout.setVisibility(View.VISIBLE);
    }

    private void showError(final int n, final String text) {
        this.hideVideoControls();
        this.mLoading.setVisibility(View.INVISIBLE);
        ((TextView)this.mErrLayout.findViewById(R.id.errMessage)).setText((CharSequence)text);
        this.mErrLayout.setVisibility(View.VISIBLE);
    }

    private void showPlayBtn() {
        if (this.mBtnPlay.getVisibility() == View.VISIBLE) {
            return;
        }
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 0.0f, (float)(this.mBtnPlay.getWidth() / 2), (float)(this.mBtnPlay.getHeight() / 2));
        scaleAnimation.setInterpolator((Interpolator)new OvershootInterpolator());
        scaleAnimation.setDuration(300L);
        scaleAnimation.setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
            }

            public void onAnimationRepeat(final Animation animation) {
            }

            public void onAnimationStart(final Animation animation) {
                MapiaVideoPlayer.this.mBtnPlay.setVisibility(View.VISIBLE);
            }
        });
        this.mBtnPlay.startAnimation((Animation)scaleAnimation);
    }

    private void showSoundBtn(final boolean b) {
        if (b) {
            this.mBtnSound.setVisibility(View.VISIBLE);
            return;
        }
        this.mBtnSound.setVisibility(View.INVISIBLE);
    }

    private void showVideoControls() {
        if (this.mBtnPlay.getVisibility() == View.VISIBLE) {
            this.mBtnSound.setScaleX(1.0f);
            this.mBtnSound.setScaleY(1.0f);
            this.mBtnSound.setVisibility(View.VISIBLE);
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mBtnPlay, "scaleX", new float[] { 0.0f, 1.0f });
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.mBtnPlay, "scaleY", new float[] { 0.0f, 1.0f });
        ofFloat.setInterpolator((TimeInterpolator)new OvershootInterpolator());
        ofFloat2.setInterpolator((TimeInterpolator)new OvershootInterpolator());
        final AnimatorSet set = new AnimatorSet();
        set.setDuration(300L);
        set.setInterpolator((TimeInterpolator)new OvershootInterpolator());
        set.play((Animator)ofFloat).with((Animator)ofFloat2);
        set.addListener((Animator.AnimatorListener)new Animator.AnimatorListener() {
            public void onAnimationCancel(final Animator animator) {
            }

            public void onAnimationEnd(final Animator animator) {
                MapiaVideoPlayer.this.mBtnSound.setScaleX(1.0f);
                MapiaVideoPlayer.this.mBtnSound.setScaleY(1.0f);
                MapiaVideoPlayer.this.mBtnSound.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(final Animator animator) {
            }

            public void onAnimationStart(final Animator animator) {
                MapiaVideoPlayer.this.mBtnPlay.setVisibility(View.VISIBLE);
            }
        });
        set.start();
    }

    public int getFrom() {
        return this.mFrom;
    }

    public void hideLoading() {
        this.mLoading.setVisibility(View.INVISIBLE);
    }

    public boolean isPlaying() {
        return this.mVideoView != null && this.mVideoView.isPlaying();
    }

    public void pause() {
        this.mRequestedOp = 0;
        if (this.mVideoView != null && this.isPlaying()) {
            this.mVideoView.pause();
            this.showPlayBtn();
        }
    }

    public void play() {
        this.requestPlay();
        if (this.mVideoView != null && this.mVideoView.isReadyToPlay() && !this.mVideoView.isPlaying()) {
            this.mThumbnail.setVisibility(View.INVISIBLE);
            this.showSoundBtn(true);
            this.mErrLayout.setVisibility(View.INVISIBLE);
            this.mVideoView.start();
            this.hideLoading();
        }
        else if (this.mVideoView.isPlaying()) {
            this.hideLoading();
        }
        else {
            this.showLoading();
        }
        this.hidePlayBtn();
    }

    public void release() {
        if (this.mVideoView != null) {
            this.mVideoView.releaseResource();
        }
    }

    public void requestPlay() {
        this.mRequestedOp = 1;
        this.mAutoPlay = true;
    }

    public void setAutoPlay(final boolean mAutoPlay) {
        this.mAutoPlay = mAutoPlay;
    }

    public void setFrom(final int mFrom) {
        this.mFrom = mFrom;
    }

    public void setLooping(final boolean b) {
        this.mLooping = b;
        if (this.mVideoView != null) {
            this.mVideoView.setLooping(b);
        }
    }

    public void setMediaPlayer(final MediaPlayer mediaPlayer) {
        if (this.mVideoView != null) {
            this.mVideoView.setMediaPlayer(mediaPlayer);
        }
    }

    public void setMapiaVideoPlayerEvent(final MapiaVideoPlayerEvent mMapiaVideoPlayerEvent) {
        this.mMapiaVideoPlayerEvent = mMapiaVideoPlayerEvent;
    }

    public void setPlayerFromContentId(final long n, final long n2, final VideoUrlEvent videoUrlEvent) {
        if (this.mAutoPlay) {
            this.showLoading();
        }
        else {
            this.showPlayBtn();
        }
        this.requestVideoUrl(n, n2, videoUrlEvent);
    }

    public void setSound(final boolean sound) {
        this.mIsSoundOn = sound;
        this.mBtnSound.setSelected(sound);
        this.mVideoView.setSound(sound);
    }

    public void setThumbnail(final Bitmap imageBitmap) {
        this.mThumbnail.setImageBitmap(imageBitmap);
    }

    public void setThumbnail(final Drawable drawable) {
        CompatiblityUtility.setBackground((View) this.mThumbnail, drawable);
    }

    public void setThumbnail(final String s) {
        if (s == null || !s.trim().contains("http://")) {
            return;
        }
        Glide.with(this.getContext()).load(s).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(final Exception ex, final String s, final Target<GlideDrawable> target, final boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(final GlideDrawable glideDrawable, final String s, final Target<GlideDrawable> target, final boolean b, final boolean b2) {
                if (glideDrawable != null) {
                    CompatiblityUtility.setBackground((View)MapiaVideoPlayer.this.mThumbnail, glideDrawable);
                }
                return false;
            }
        });
    }

    public void setThumbnail(final String s, final ImageViewUtils.ImageViewUtilEvent imageViewUtilEvent) {
        ImageViewUtils.setImageFromUrl(this.mThumbnail, s, imageViewUtilEvent);
    }

    public void setVideoPath(final String path) {
        this.mVideoView.setPath(path);
    }

    public void setVideoUrl(final String url) {
        this.mVideoView.setUrl(url);
    }

    public void showLoading() {
        this.mBtnPlay.setVisibility(View.INVISIBLE);
        this.mLoading.setVisibility(View.VISIBLE);
    }

    public void stop() {
        if (this.mVideoView != null && this.isPlaying()) {
            this.mVideoView.stop();
        }
    }

    public void updateTextureViewSize(final int n, final int n2) {
        this.mThumbnail.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(n, n2));
        this.mVideoView.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(n, n2));
    }


    public interface VideoUrlEvent
    {
        void onVideoUrlDownloaded();
    }

    public interface MapiaVideoPlayerEvent {
        void onSigleTap();
    }

}
