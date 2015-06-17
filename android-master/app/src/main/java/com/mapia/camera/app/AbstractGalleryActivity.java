package com.mapia.camera.app;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.mapia.camera.common.ApiHelper;


public class AbstractGalleryActivity extends Activity
{
    private static final String TAG = "AbstractGalleryActivity";
    private AlertDialog mAlertDialog;
    private IntentFilter mMountFilter;
    private BroadcastReceiver mMountReceiver;

    public AbstractGalleryActivity() {
        super();
        this.mAlertDialog = null;
        this.mMountReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (AbstractGalleryActivity.this.getExternalCacheDir() != null) {
                    AbstractGalleryActivity.this.onStorageReady();
                }
            }
        };
        this.mMountFilter = new IntentFilter("android.intent.action.MEDIA_MOUNTED");
    }

    @TargetApi(11)
    private static void setAlertDialogIconAttribute(final AlertDialog.Builder alertDialogBuilder) {
//        alertDialogBuilder.setIconAttribute(16843605);
    }

    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.invalidateOptionsMenu();
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().setBackgroundDrawable((Drawable)null);
    }

    protected void onStart() {
        super.onStart();
        if (this.getExternalCacheDir() == null) {
            final AlertDialog.Builder setOnCancelListener = new AlertDialog.Builder((Context)this).setTitle(2131558678).setMessage(2131558677).setNegativeButton(17039360, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    dialogInterface.cancel();
                }
            }).setOnCancelListener((DialogInterface.OnCancelListener)new DialogInterface.OnCancelListener() {
                public void onCancel(final DialogInterface dialogInterface) {
                    AbstractGalleryActivity.this.finish();
                }
            });
            if (ApiHelper.HAS_SET_ICON_ATTRIBUTE) {
                setAlertDialogIconAttribute(setOnCancelListener);
            }
            else {
                setOnCancelListener.setIcon(17301543);
            }
            this.mAlertDialog = setOnCancelListener.show();
            this.registerReceiver(this.mMountReceiver, this.mMountFilter);
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.mAlertDialog != null) {
            this.unregisterReceiver(this.mMountReceiver);
            this.mAlertDialog.dismiss();
            this.mAlertDialog = null;
        }
    }

    protected void onStorageReady() {
        if (this.mAlertDialog != null) {
            this.mAlertDialog.dismiss();
            this.mAlertDialog = null;
            this.unregisterReceiver(this.mMountReceiver);
        }
    }
}