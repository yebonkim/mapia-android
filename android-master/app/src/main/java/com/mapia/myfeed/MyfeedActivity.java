package com.mapia.myfeed;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Intent;
import android.os.Bundle;

import com.mapia.MainActivity;
import com.mapia.R;
import com.mapia.scheme.SchemeUtils;

import org.apache.commons.lang3.StringUtils;


public class MyfeedActivity extends MainActivity
{
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(null);
        this.setContentView(R.layout.activity_main);
        this.mainApplication.setMyfeedActivity(this);
        this.init(1);
    }

    public void onDestroy() {
        super.onDestroy();
        if (SchemeUtils.getMainActivity() != null && SchemeUtils.getMainActivity() instanceof MyfeedActivity) {
            SchemeUtils.setMainActivity(null);
        }
        this.mainApplication.setMyfeedActivity(null);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        final String stringExtra = intent.getStringExtra("from");
        if (StringUtils.isNotBlank(stringExtra)) {
            if ("postFragment".equals(stringExtra)) {
                if (this.fragmentTagStack.getSize() <= 1) {
                    this.refreshMenuRootFragment(false, 0, true, 700);
                    return;
                }
                this.removeAllFragement(false, 0, true, 700, true);
            }
            else if ("relatedContentFragment".equals(stringExtra)) {
                if (this.fragmentTagStack.getSize() > 1) {
                    this.removeAllFragement(false, 0, true, 0, true);
                    return;
                }
                this.refreshMenuRootFragment(false, 0, true, 0);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.currentFragment instanceof MyfeedFragment) {
            ((MyfeedFragment)this.currentFragment).pauseAllChildVideo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SchemeUtils.setMainActivity(this);
        if (this.mSchemeUrl != null) {
            SchemeUtils.goDestination(this.mSchemeUrl);
            this.setSchemeUrl(null);
        }
//        if (NotiUtils.getIsNewNoti()) {
//            this.showNotiIcon(false);
//        }
//        else {
//            this.notiManager.checkHasNew();
//        }
//        NpushUtils.setCurrentActivity(1);
    }
}