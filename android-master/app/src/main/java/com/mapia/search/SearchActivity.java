package com.mapia.search;

import android.os.Bundle;

import com.mapia.MainActivity;
import com.mapia.R;

/**
 * Created by daehyun on 15. 6. 6..
 */
public class SearchActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(null);
        this.setContentView(R.layout.activity_main);
        this.mainApplication.setSearchActivity(this);
        this.init(4);
    }
    public void onDestroy() {
        super.onDestroy();
//        if (SchemeUtils.getMainActivity() != null && SchemeUtils.getMainActivity() instanceof SearchActivity) {
//            SchemeUtils.setMainActivity(null);
//        }
        this.mainApplication.setSearchActivity(null);
    }

    @Override
    public void onResume() {
        super.onResume();
//        SchemeUtils.setMainActivity(this);
//        if (this.mSchemeUrl != null) {
//            SchemeUtils.goDestination(this.mSchemeUrl);
//            this.setSchemeUrl(null);
//        }
//        if (NotiUtils.getIsNewNoti()) {
//            this.showNotiIcon(false);
//        }
//        else {
//            this.notiManager.checkHasNew();
//        }
//        NpushUtils.setCurrentActivity(4);
//        if (this.currentFragment instanceof SearchFragment || this.currentFragment instanceof SearchTagResultFragment || this.currentFragment instanceof SearchPicResultFragment || this.currentFragment instanceof SearchUserResultFragment) {
//            this.hideMenuBar(true);
//        }
    }
}
