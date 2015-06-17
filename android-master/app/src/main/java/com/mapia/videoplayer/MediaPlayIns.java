package com.mapia.videoplayer;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.media.MediaPlayer;

public class MediaPlayIns
{
    private static MediaPlayer mp;

    static {
        MediaPlayIns.mp = null;
    }

    public static MediaPlayer getMusicPlayIns() {
        if (MediaPlayIns.mp == null) {
            return MediaPlayIns.mp = new MediaPlayer();
        }
        return MediaPlayIns.mp;
    }
}