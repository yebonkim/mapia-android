package com.mapia.videoplayer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;


public class MapiaVideoView extends TextureView implements TextureView.SurfaceTextureListener
{
    private final int TYPE_DATASOURCE_PATH;
    private final int TYPE_DATASOURCE_URI;
    private final int TYPE_DATASOURCE_URL;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private Context mContext;
    private int mDatasourceType;
    private boolean mLooping;
    private MediaPlayer mMediaPlayer;
    private String mPath;
    private MediaPlayer.OnPreparedListener mPreparedListener;
    private boolean mReadyToPlay;
    private boolean mSoundOn;
    private Surface mSurface;
    private Uri mUri;
    private String mUrl;

    public MapiaVideoView(final Context context) {
        this(context, null, 0);
    }

    public MapiaVideoView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public MapiaVideoView(final Context mContext, final AttributeSet set, final int n) {
        super(mContext, set, n);
        this.TYPE_DATASOURCE_URL = 0;
        this.TYPE_DATASOURCE_PATH = 1;
        this.TYPE_DATASOURCE_URI = 2;
        this.mSoundOn = true;
        this.mContext = mContext;
        this.init();
    }

    private Uri getUrl() {
        if (this.mUrl != null) {
            return Uri.parse(this.mUrl);
        }
        return null;
    }

    private void openVideo() {
        if (this.mSurface == null) {
            return;
        }
        try {
            try {
                if (this.mMediaPlayer == null) {
                    this.mMediaPlayer = new MediaPlayer();
                }
                this.mMediaPlayer.setSurface(this.mSurface);
                this.mMediaPlayer.setOnPreparedListener((MediaPlayer.OnPreparedListener) new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(final MediaPlayer mediaPlayer) {
                        MapiaVideoView.this.mReadyToPlay = true;
                        if (MapiaVideoView.this.mPreparedListener != null) {
                            MapiaVideoView.this.mPreparedListener.onPrepared(mediaPlayer);
                        }
                    }
                });
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                switch (this.mDatasourceType) {
                    case 1: {
                        this.mMediaPlayer.release();
                        return;
                    }
                }
            } catch (IllegalArgumentException ex2) {
                this.releaseResource();
                this.mMediaPlayer.setDataSource(this.mContext, this.mUri);
                return;
            } catch (IllegalStateException ex3) {
                this.releaseResource();
                this.mMediaPlayer.setDataSource(this.mContext, getUrl());
                return;
            } catch (Exception ex4) {
                this.releaseResource();
                return;
            }
        } catch (IOException ex) {
            this.releaseResource();
            try {
                this.mMediaPlayer.setDataSource(this.mPath);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.setLooping(this.mLooping);
                this.mMediaPlayer.prepareAsync();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return;
        }
    }



    public void init() {
        this.setSurfaceTextureListener((TextureView.SurfaceTextureListener)this);
    }

    public boolean isPlaying() {
        return this.mMediaPlayer != null && this.mMediaPlayer.isPlaying();
    }

    public boolean isReadyToPlay() {
        return this.mReadyToPlay;
    }

    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        this.mSurface = new Surface(surfaceTexture);
        if (this.mSurface == null) {
            return;
        }
        this.openVideo();
    }

    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        return true;
    }

    public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
    }

    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
    }

    public void pause() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.pause();
        }
    }

    public void releaseResource() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        if (this.mSurface != null) {
            this.mSurface.release();
            this.mSurface = null;
        }
    }

    public void setLooping(final boolean mLooping) {
        this.mLooping = mLooping;
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setLooping(this.mLooping);
        }
    }

    public void setMediaPlayer(final MediaPlayer mMediaPlayer) {
        this.mMediaPlayer = mMediaPlayer;
    }

    public void setOnCompetionListener(final MediaPlayer.OnCompletionListener mCompletionListener) {
        this.mCompletionListener = mCompletionListener;
    }

    public void setOnPreparedListener(final MediaPlayer.OnPreparedListener mPreparedListener) {
        this.mPreparedListener = mPreparedListener;
    }

    public void setPath(final String mPath) {
        this.mDatasourceType = 1;
        this.mPath = mPath;
        this.openVideo();
    }

    public void setSound(final boolean mSoundOn) {
        this.mSoundOn = mSoundOn;
        if (this.mMediaPlayer != null) {
            if (!this.mSoundOn) {
                this.mMediaPlayer.setVolume(0.0f, 0.0f);
                return;
            }
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    public void setUri(final Uri mUri) {
        this.mDatasourceType = 2;
        this.mUri = mUri;
        this.openVideo();
    }

    public void setUrl(final String mUrl) {
        this.mUrl = mUrl;
        this.mDatasourceType = 0;
        this.openVideo();
    }

    public void start() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.start();
        }
    }

    public void stop() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
        }
    }
}