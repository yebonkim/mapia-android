package com.mapia.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mapia.home.HomeActivity;
import com.mapia.util.PreferenceUtils;

/**
 * Created by daehyun on 15. 6. 5..
 */
public class TutorialActivity extends Activity {
    static final int MIN_DISTANCE = 300;
    private int currentPosition;
    private float endX;
    private String from;
    private ImageView logoImageView;
    private LinearLayout naviLinearLayout;
    private View nowView;
    private float startX;
    private ViewPager viewPager;

    public void goToNextActivity(){
        Intent localIntent = new Intent(this, HomeActivity.class);//LoginActivity.class);
//        localIntent.setFlags();
        startActivity(localIntent);
        finish();
        PreferenceUtils.putPreference("alreadyTutorialShowYn","Y");
    }

    public void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
        goToNextActivity();
    }
}
