package com.mapia.ratio;

/**
 * Created by daehyun on 15. 6. 21..
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenVideo;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.imagechooser.api.VideoChooserListener;
import com.kbeanie.imagechooser.api.VideoChooserManager;
import com.mapia.MainApplication;
import com.mapia.R;
import com.mapia.util.CameraUtils;
import com.mapia.util.MapiaStringUtil;
import com.mapia.util.MapiaToast;

import org.apache.commons.lang3.StringUtils;

import java.io.File;


public class RatioProxyActivity extends Activity implements ImageChooserListener, VideoChooserListener
{
    private boolean isPhoto;
    private ProgressDialog progressDialog;

    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n2 == 2) {
            this.setResult(2, intent);
        }
        this.finish();
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final String type = this.getIntent().getType();
        final Uri uri = (Uri)this.getIntent().getParcelableExtra("galleryShareExtraSteam");
        this.isPhoto = false;
        this.progressDialog = ProgressDialog.show((Context)this, (CharSequence)"", (CharSequence)"Loading..", true);
        if (StringUtils.contains(type, "image")) {
            final ImageChooserManager imageChooserManager = new ImageChooserManager(this, 291, "pholar_tmp", false);
            imageChooserManager.setImageChooserListener(this);
            imageChooserManager.submit(291, new Intent().setData(uri));
            this.isPhoto = true;
            return;
        }
        if (StringUtils.contains(type, "video")) {
//            if (!VideoSDK.isInitialized()) {
//                VideoSDK.init((Context)this, false);
//            }
            final VideoChooserManager videoChooserManager = new VideoChooserManager(this, 295, "pholar_tmp", false);
            videoChooserManager.setVideoChooserListener(this);
            videoChooserManager.submit(295, new Intent().setData(uri));
            this.isPhoto = false;
            return;
        }
        this.progressDialog.dismiss();
        MapiaToast.show(this, this.getString(R.string.camera_error_4), 1);
        this.finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onError(final String s) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (RatioProxyActivity.this.progressDialog != null) {
                    RatioProxyActivity.this.progressDialog.dismiss();
                    RatioProxyActivity.this.progressDialog = null;
                }
                if (RatioProxyActivity.this.isPhoto) {
                    MapiaToast.show(RatioProxyActivity.this, RatioProxyActivity.this.getString(R.string.image_load_fail), 1);
                }
                else {
                    MapiaToast.show(RatioProxyActivity.this, RatioProxyActivity.this.getString(R.string.camera_error_1), 1);
                }
                RatioProxyActivity.this.finish();
            }
        });
    }

    public void onImageChosen(final ChosenImage chosenImage) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                MainApplication.getInstance().getPostingInfo().mode = 0;
                MainApplication.getInstance().getPostingInfo().copyrightYn = "N";
                final String decodeUrl = MapiaStringUtil.decodeUrl(chosenImage.getFilePathOriginal());
                final Intent intent = new Intent((Context)RatioProxyActivity.this, (Class)PhotoRatioActivity.class);
                intent.putExtra("chosenFile", decodeUrl);
                RatioProxyActivity.this.startActivityForResult(intent, 0);
                if (RatioProxyActivity.this.progressDialog != null) {
                    RatioProxyActivity.this.progressDialog.dismiss();
                    RatioProxyActivity.this.progressDialog = null;
                }
            }
        });
    }

    public void onVideoChosen(final ChosenVideo chosenVideo) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final String decodeUrl = MapiaStringUtil.decodeUrl(chosenVideo.getVideoFilePath());
                if (!CameraUtils.isSupportVideoFile(new File(decodeUrl).getName())) {
                    MapiaToast.show(RatioProxyActivity.this, RatioProxyActivity.this.getString(R.string.camera_error_4), 1);
                    RatioProxyActivity.this.finish();
                }
                else {
                    MainApplication.getInstance().getPostingInfo().mode = 0;
                    MainApplication.getInstance().getPostingInfo().copyrightYn = "N";
                    final Intent intent = new Intent((Context)RatioProxyActivity.this, (Class)VideoRatioActivity.class);
                    intent.putExtra("chosenFile", decodeUrl);
                    RatioProxyActivity.this.startActivityForResult(intent, 0);
                }
                if (RatioProxyActivity.this.progressDialog != null) {
                    RatioProxyActivity.this.progressDialog.dismiss();
                    RatioProxyActivity.this.progressDialog = null;
                }
            }
        });
    }
}