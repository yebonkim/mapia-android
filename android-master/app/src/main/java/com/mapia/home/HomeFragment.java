package com.mapia.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mapia.R;
import com.mapia.common.BaseFragment;
import com.mapia.map.CloseAnimation;
import com.mapia.map.OpenAnimation;
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
    private static boolean isMenuExpanded = false;
    private int menuWidth;
    private ListView lvNavList;
    private LinearLayout layout;
    LinearLayout llMenuMap = null, llMainMap = null;
    private Timer timer;
    private TimerTask timerTask;
    View mMapView;

    @Override
    public void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
//        this.homeHotAdaper = new HomeHotAdapter(this.mainActivity);
//        this.homeBannerAdapter = new HomeBannerAdapter(this.mainActivity);
//        this.homeTaggroupAdapter = new HomeTaggroupAdapter(this.mainAcitivity, this);
//        this.homeManager = new HomeManager(this.mainActivity, this, this.homeHotAdapter, this.homeBannerAdapter, this.homeTaggroupAdapter);
        ((HomeActivity)this.mainActivity).setHomeFragment(this);
    }


    @Override
    public View onCreateView(final LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle){


        this.layout = ((LinearLayout)paramLayoutInflater.inflate(R.layout.fragment_home, paramViewGroup, false));
//        if(!"Y".equals(PreferenceUtils.getPreference("alreadyHomeCoachShowYn"))){
//            this.mainActivity.showHomeCoach();
//        }
        this.header = ((LinearLayout)this.layout.findViewById(R.id.fh_header));

        this.layout.findViewById(R.id.fh_title_text).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
            }
        });
        this.layout.findViewById(R.id.fh_button_menu).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                menuAnimationToggle();
            }
        });
        this.layout.findViewById(R.id.fh_button_search).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent intent = new Intent(HomeFragment.this.mainActivity, SearchActivity.class);
                HomeFragment.this.startActivity(intent);
            }
        });
        initSildeMenu();
        return this.layout;

    }



    private void initSildeMenu() {
        /* init menu layout size */
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        menuWidth = (int) ((metrics.widthPixels) * 0.75);

        llMainMap = (LinearLayout) layout.findViewById(R.id.llMainMap);

        llMenuMap = (LinearLayout) layout.findViewById(R.id.llMenuMap);
        FrameLayout.LayoutParams MenuLayoutPrams = (FrameLayout.LayoutParams) llMenuMap.getLayoutParams();
        MenuLayoutPrams.width = menuWidth;
        llMenuMap.setLayoutParams(MenuLayoutPrams);

    }

    private void menuAnimationToggle() {
        if (!isMenuExpanded) {
            isMenuExpanded = true;
            // Expand
            new OpenAnimation(llMainMap, menuWidth,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);

            // enable all of menu view
            FrameLayout viewGroup = (FrameLayout) layout.findViewById(R.id.llMenuMap).getParent();
            enableDisableViewGroup(viewGroup, true);

            // enable empty view
            layout.findViewById(R.id.ll_empty).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.ll_empty).setEnabled(true);
            layout.findViewById(R.id.ll_empty).setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            menuAnimationToggle();
                            return true;
                        }
                    });

        } else {
            isMenuExpanded = false;

            // Collapse
            new CloseAnimation(llMainMap, menuWidth,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);


            // enable all of menu view
            FrameLayout viewGroup = (FrameLayout) layout.findViewById(R.id.mapShowFragment)
                    .getParent();
            enableDisableViewGroup(viewGroup, false);

            // disable empty view
            layout.findViewById(R.id.ll_empty).setVisibility(View.GONE);
            layout.findViewById(R.id.ll_empty).setEnabled(false);

        }
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup,
                                              boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {


            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);

            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }


}
