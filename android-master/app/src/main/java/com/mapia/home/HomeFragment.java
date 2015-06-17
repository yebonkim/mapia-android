package com.mapia.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mapia.R;
import com.mapia.common.BaseFragment;
import com.mapia.custom.ListenableScrollView;
import com.mapia.search.SearchActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by daehyun on 15. 5. 31..
 */
public class HomeFragment extends BaseFragment {
//    private AnalogClockView analogClockView;
    private int[] bannerResourceIdArray = {};
    private boolean dragFlag = false;
    private boolean firstDragFlag = false;
    private int[] groupResourceIdArray = {};
    private LinearLayout header;
//    private HomeBannerAdapter homeBannerAdapter;
//    private HomeHotAdapter homeHotAdapter;
//    private HomeManager homeManager;
//    private HomeTaggroupAdapter homeTaggroupAdapter;
    private RelativeLayout layout;
    private ListenableScrollView scrollView;
//    private listenableSrollView scrollView;
//    private float startYPosition;
//    private HomeSwiperRefreshLayout swipeRefreshLayout;
    private Timer timer;
    private TimerTask timerTask;

//    private void setMyfeedPicTextViewWidth(final ViewparamView){
//        paramView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
//            @SuppressLint({"NewApi"})
//            public void onGlobalLayout(){
//               MyfeedPicUtils.bodyTextViewWidth = paramView.getWidth();
//
//            }
//        })
//    }
    @Override
    public void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
//        this.homeHotAdaper = new HomeHotAdapter(this.mainActivity);
//        this.homeBannerAdapter = new HomeBannerAdapter(this.mainActivity);
//        this.homeTaggroupAdapter = new HomeTaggroupAdapter(this.mainAcitivity, this);
//        this.homeManager = new HomeManager(this.mainActivity, this, this.homeHotAdapter, this.homeBannerAdapter, this.homeTaggroupAdapter);
        ((HomeActivity)this.mainActivity).setHomeFragment(this);
        }



    public void refreshHome(){
        this.scrollView.scrollTo(0,0);

    }
    @Override
    public View onCreateView(final LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle){
        this.layout = ((RelativeLayout)paramLayoutInflater.inflate(R.layout.fragment_home, paramViewGroup, false));
//        if(!"Y".equals(PreferenceUtils.getPreference("alreadyHomeCoachShowYn"))){
//            this.mainActivity.showHomeCoach();
//        }
        this.header = ((LinearLayout)this.layout.findViewById(R.id.fh_header));
        this.header.setOnClickListener(new View.OnClickListener(){
            public void onClick(View paramAnonymousView){
                HomeFragment.this.scrollToTop();
            }
        });
        this.layout.findViewById(R.id.fh_title_text).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
            }
        });
        this.layout.findViewById(R.id.fh_button_search).setOnClickListener(new View.OnClickListener(){
            public void onClick(View paramAnonymousView){
                Intent intent = new Intent(HomeFragment.this.mainActivity, SearchActivity.class);
                HomeFragment.this.startActivity(intent);
            }
        });
        return this.layout;

    }

    public void scrollToTop(){
        this.scrollView.smoothScrollTo(0,0);
        this.scrollView.post(new Runnable(){
            public void run(){
                HomeFragment.this.mainActivity.showMenuBar(true);
            }
        });
    }
}
