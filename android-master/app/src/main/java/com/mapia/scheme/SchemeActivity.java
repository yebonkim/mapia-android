package com.mapia.scheme;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.mapia.MainActivity;
import com.mapia.MainApplication;
import com.mapia.home.HomeActivity;
import com.mapia.login.LoginActivity;
import com.mapia.login.LoginInfo;

import org.apache.commons.lang3.StringUtils;


public class SchemeActivity extends Activity
{
    private MainActivity mainActivity;

    private Intent setLoginActivityIntent(final Intent intent) {
        intent.setFlags(67108864);
        intent.setClass((Context)this, (Class)LoginActivity.class);
        return intent;
    }

    private Intent setMainActivityIntent(final Intent intent) {
        intent.setFlags(603979776);
        if (this.mainActivity != null) {
            intent.setClass((Context)this, (Class)this.mainActivity.getClass());
            return intent;
        }
        intent.setClass((Context)this, (Class)HomeActivity.class);
        return intent;
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final String action = this.getIntent().getAction();
        final Intent intent = new Intent();
        if (StringUtils.equals(action, "android.intent.action.SEND")) {
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("galleryShareExtraSteam", this.getIntent().getExtras().getParcelable("android.intent.extra.STREAM"));
            MainApplication.getInstance().getPostingInfo().body = this.getIntent().getStringExtra("android.intent.extra.TEXT");
            intent.setType(this.getIntent().getType());
        }
        else {
            final Uri data = this.getIntent().getData();
            this.mainActivity = SchemeUtils.getMainActivity();
            if (this.mainActivity != null) {
                this.mainActivity.setSchemeUrl(data.toString());
            }
            else {
                intent.putExtra("scheme_data", data.toString());
            }
        }
        Intent intent2;
        if (LoginInfo.getInstance().isLogined()) {
            intent2 = this.setMainActivityIntent(intent);
        }
        else {
            intent2 = this.setLoginActivityIntent(intent);
        }
        this.startActivity(intent2);
        this.finish();
    }
}