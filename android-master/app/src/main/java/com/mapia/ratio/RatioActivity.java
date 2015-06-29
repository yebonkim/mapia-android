package com.mapia.ratio;

/**
 * Created by daehyun on 15. 6. 21..
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.mapia.camera.viewmodel.RatioManager;

import java.io.File;


public class RatioActivity extends Activity
{
    protected final int ACTIVITY_REQUESTCODE_APPLY_FILTER;
    protected File croppedFile;
    protected Context mContext;
    protected String originalFilePath;
    protected RatioManager ratioManager;

    public RatioActivity() {
        super();
        this.ACTIVITY_REQUESTCODE_APPLY_FILTER = 100;
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = (Context)this;
        this.originalFilePath = this.getIntent().getStringExtra("chosenFile");
    }
}