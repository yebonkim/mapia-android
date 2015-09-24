package com.mapia.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.mapia.R;
import com.mapia.cache.realmcache.RealmManager;
import com.mapia.login.LoginActivity;

/**
 * Created by daehyun on 15. 6. 2..
 */
public class SplashActivity extends Activity {
    private Context context;
    private LinearLayout filter;
//    private HomeTagListCahceWrapper homeTagListCacheWrapper;
//    private mapiaVideoView mapiaVideoView;
    private RealmManager realmManager;
//
//    private void getDataFromServerToPreload(){
//        this.realmManager = new RealmManager(this);
//        this.homeTagListCaheWrapper = new HomeTagListCacheWrapper();
//        mapiaRequest localmapiaRequest = new mapiaRequest(0, QueryManager.makeHomePaiUrl(), null, new Response.Listener(), new Response.ErrorListener{
//            p
//        })

    private void goToNextActivity(){
        Intent localIntent;

        localIntent = new Intent(this, LoginActivity.class);
        localIntent.putExtra("from", "splash");
//        if("Y".equals(PreferenceUtils.getPreference("alreadyTutorialShowYn"))){
//            localIntent = new Intent(this, HomeActivity.class);//LoginActivity.class);
////            localIntent.setFlags()
//        }
//        else{
//            localIntent = new Intent(this, TutorialActivity.class);
//            localIntent.putExtra("from", "splash");
//        }

        startActivity(localIntent);
        finish();
    }


    public void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);

        if(!isTaskRoot()){
            Intent intent = this.getIntent();
            String action = intent.getAction();
            if (intent.hasCategory("android.intent.category.LAUNCHER") && action != null && action.equals("android.intent.action.MAIN")){
                this.finish();
                return;
            }
        }
        if (SplashUtils.getIsShowSplash()){
            this.goToNextActivity();
            return;
        }
        this.goToNextActivity();
        SplashUtils.setIsShowSplash(true);
        this.setContentView(R.layout.activity_splash);
        this.filter = (LinearLayout)this.findViewById(R.id.as_filter);

    }
}







