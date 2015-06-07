package com.mapia;

import android.app.KeyguardManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mapia.common.BaseFragment;
import com.mapia.common.FragmentTagStack;
import com.mapia.home.HomeFragment;
import com.mapia.network.NetworkStatusManager;
import com.mapia.search.SearchFragment;

import java.util.ArrayList;
import java.util.UUID;

//import org.apache.commons.lang3.StringUtils;

/**
 * Created by daehyun on 15. 5. 31..
 */
public abstract class MainActivity extends FragmentActivity {
    public static final String ACTION_START_POSTFRAGMENT = "action_start_post_fragment";
    public static final int ACTIVITY_REQUESTCODE_APPLYFILTER = 1;
    public static final int ACTIVITY_REQUESTCODE_CROP = 3;
    public static final int ACTIVITY_REQUESTCODE_HOME = 6;
    public static final int ACTIVITY_REQUESTCODE_POST = 2;
    public static final int ACTIVITY_REQUESTCODE_RATIO_PROXY = 5;
    public static final int ACTIVITY_REQUESTCODE_VIDEO_PREVIEW = 4;

    public static final int RESULT_LEAVE_POSTING = 2;
    private static final long TOAST_TIMEOUT_MS = 1000L;
    private int HOME_BUTTON_CLICK_PERIOD = 1000;
    private long beforeHomeButtonClickTime = System.currentTimeMillis();
    protected FragmentManager fragmentManager;
    protected FragmentTagStack fragmentTagStack;
    protected ImageView homeButton;
    protected ImageView homeWhite;
    protected boolean isAdding = false;
    private boolean isFirstOnResume = true;
    protected boolean isShowMenubar = true;
    protected KeyguardManager keyguaredManager;
    private long mLastToastTime = 0L;
    protected String mSchemeUrl;
    protected MainApplication mainApplication;
    protected int menuNo;
    protected Class<?> menuRootFragmentClass;
    protected String menuRootFragmentTag;
    protected LinearLayout menubar;
    protected ImageView alarmButton;
    protected ImageView writeButton;
    protected RelativeLayout noConnection;
    protected ImageView notiButton;
    protected ImageView notiIcon;
//    protected NotiManager notiManager;
    protected ImageView notiWhite;
//    protected ObjectMapper objectMapper;
    protected BaseFragment previousFragment;
    protected BaseFragment currentFragment;
    protected ImageView profileButton;
    protected ImageView profileWhite;
    protected ArrayList<Boolean> withAnimationList = new ArrayList();


    public MainActivity(){
        super();
        this.withAnimationList = new ArrayList<Boolean>();
        mainApplication =  new MainApplication();

    }
//    private void openEndForNotibar(long paramLong1, long paramLong2){
//        Bundle localBundle = new Bundle();
//        localBundle.putLong("picNo", paramLong1);
//        localBundle.putLong("memberNo", paramLong2);
//        EndFragment localEndFragment = new EndFragment();
//        localEndFragment.setArguments(localBundle);
//        addFragment(localEndFragment, true, true, false);
//    }

    public void hideMenuBar(Boolean paramBoolean){
        if(!this.isShowMenubar){
            do{
                this.isShowMenubar = false;
                this.menubar.setVisibility(View.INVISIBLE);
            }while(!paramBoolean);
            this.menubar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menubar_exit_to_bottom));
        }
    }
    public boolean isShowMenubar(){
        return this.isShowMenubar;
    }

    public void showMenuBar(boolean flag){
        showMenuBar(flag, false);
    }

    public void showMenuBar(boolean paramBoolean1, boolean paramBoolean2){
        if(this.isShowMenubar || !this.currentFragment.getDefaultMenuBarShow()){
            return;
        }

                this.isShowMenubar = true;
                this.menubar.setVisibility(View.VISIBLE);
            this.menubar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menubar_enter_from_bottom));

    }

    public void addFragment(BaseFragment baseFragment){
        this.addFragment(baseFragment, true);

    }
    public void addFragment(BaseFragment baseFragment, boolean b){
        this.addFragment(baseFragment, b, false, true);
    }
    public void addFragment(BaseFragment baseFragment, boolean b, boolean b2){
        this.addFragment(baseFragment, b,b2,true);
    }
    public void addFragment(BaseFragment baseFragment, boolean b, boolean b2, boolean b3){
        if(this.fragmentTagStack.getSize() > 0 && !NetworkStatusManager.getIsAvailableNetwork()){
            this.showNoConnection(true);
        }
        if(!this.isAdding){
            if(b3){
                this.isAdding = true;
            }
            this.withAnimationList.add(this.fragmentTagStack.getFrontIndex() + 1, b);
            final String front = this.fragmentTagStack.getFront();
            final String uniqueTag = this.makeUniqueTag(baseFragment);
            FragmentTransaction beginTransaction = this.fragmentManager.beginTransaction();
            if(b){
                beginTransaction.setCustomAnimations(R.anim.fragment_enter_from_right, R.anim.fragment_exit_to_right);
            }
            beginTransaction.add(R.id.am_fragment, baseFragment, uniqueTag);
            if(b2){
                beginTransaction.commitAllowingStateLoss();
            }
            else{
                beginTransaction.commit();
            }
            this.fragmentTagStack.push(uniqueTag);
            this.setCurrentFragment(baseFragment);
//            this.resumeFragment(baseFragment, false);
            if(baseFragment.getDefaultMenuBarShow()){
                this.showMenuBar(true);
            }
            else{
                this.hideMenuBar(true);
            }
//            if(!StringUtils.isNotBlank(front)){
//                this.isAdding = false;
//                return;
//            }
            baseFragment = (BaseFragment)this.fragmentManager.findFragmentByTag(front);
            if(baseFragment != null){
                View view = baseFragment.getView();
                view.setVisibility(View.VISIBLE);
                if (b){
                    final Animation loadAnimation = AnimationUtils.loadAnimation((Context)this, R.anim.fragment_fade_out);
                    loadAnimation.setAnimationListener(new Animation.AnimationListener(){
                        public void onAnimationEnd(final Animation animation){
                            MainActivity.this.isAdding = false;
//                            baseFragment.onBuriedAfterAnimation();
                        }
                        public void onAnimationRepeat(final Animation animation){
                        }
                        public void onAnimationStart(final Animation animation){
//                            baseFragment.onBuriedBeforeAnimation();

                        }
                    });
                    view.startAnimation(loadAnimation);
                }
                else{
                    this.isAdding = false;
                    baseFragment.onBuriedBeforeAnimation();
                }
                this.setPrevFragment(baseFragment);
            }
        }
    }
//    private void resumeFragment(BaseFragment baseFragment, boolean b){
//        if(baseFragment != null){
//            if(baseFragment instanceof ProfileFollowFragment){
//                if(!b) {
//                    ((ProfileFollowFragment)baseFragment).mIsShown = true;
//                    return;
//                }
//                ((ProfileFollowFragment)baseFragment).resume();
//            }
//            else if(BaseFragment instanceof EndViewPagerFragment){
//                if(b){
//                    AceUtils.site("EndFragment");
//                }
//            }
//            else if(baseFragment.getClass() != null){
//                AceUtils.site(baseFragment.getClass().getSimpleName());
//            }
//        }
//    }
    protected void setPrevFragment(final BaseFragment previousFragment){
        this.previousFragment = previousFragment;
    }
    protected void setCurrentFragment(BaseFragment currentFragment){
        if(currentFragment == null){
            return;
        }
        this.currentFragment = currentFragment;

    }
    public void showNoConnection(boolean b){
        if(b){
            this.noConnection.setVisibility(View.INVISIBLE);
            if (this.currentFragment != null){
//                this.currentFragment.setConnectionCondition(false);
            }
        }
        else{
            this.noConnection.setVisibility(View.VISIBLE);
            if(this.currentFragment != null){
//                this.currentFragment.setConnectionCondition(true);
            }
        }
    }
    protected String makeUniqueSuffix(){
        return String.format("%s", UUID.randomUUID());
    }
    protected String makeUniqueTag(BaseFragment baseFragment){
        String string;
        if(baseFragment == null){
            string = null;
        }
        else{
            final String string2 = "" + baseFragment.getClass();
            String menuRootFragmentTag = string = string2.substring(string2.lastIndexOf(".") + 1) + "_" + this.makeUniqueSuffix();
            if(this.menuRootFragmentClass.isInstance(baseFragment)){
                string = menuRootFragmentTag;
                if(this.fragmentTagStack.getSize()== 0){
                    return this.menuRootFragmentTag = menuRootFragmentTag;
                }
            }
        }
        return string;
    }


    protected void init(final int menuNo){
        this.menuNo = menuNo;
        this.fragmentManager = this.getSupportFragmentManager();
        this.fragmentTagStack = new FragmentTagStack();
        final MainApplication mainApplication = this.mainApplication;
        this.alarmButton = (ImageView)this.findViewById(R.id.am_button_alarm);
        this.writeButton = (ImageView)this.findViewById(R.id.am_button_write);
        this.alarmButton.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){

           }
        });

        this.writeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

            }
        });

        switch(menuNo){
            case 0:
                this.menuRootFragmentClass = HomeFragment.class;
                this.addFragment(new HomeFragment(), false);
                break;
            case 4:
                this.menuRootFragmentClass = SearchFragment.class;
                this.addFragment(new SearchFragment(), false);
                break;

        }


    }

}
