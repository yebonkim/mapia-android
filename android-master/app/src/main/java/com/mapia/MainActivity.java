package com.mapia;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mapia.common.BaseFragment;
import com.mapia.common.CommonConstants;
import com.mapia.common.FragmentTagStack;
import com.mapia.custom.FontSettableTextView;
import com.mapia.home.HomeFragment;
import com.mapia.myfeed.MyfeedActivity;
import com.mapia.myfeed.MyfeedFragment;
import com.mapia.network.NetworkStatusManager;
import com.mapia.post.PostFragment;
import com.mapia.search.SearchFragment;
import com.mapia.util.RequestUtils;
import com.volley.MapiaRequest;
import com.volley.MapiaVolley;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

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
    public MainApplication mainApplication;
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
    public void hideMenuBar(final boolean b) {
        if (this.isShowMenubar) {
            this.isShowMenubar = false;
            this.menubar.setVisibility(View.INVISIBLE);
            if (b) {
                this.menubar.startAnimation(AnimationUtils.loadAnimation((Context)this, R.anim.menubar_exit_to_bottom));
            }
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

    public BaseFragment getCurrFragment() {
        return this.currentFragment;
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

    protected void addPostFragment(final Intent intent) {
        if (intent == null) {
            return;
        }
        final Bundle arguments = new Bundle();
        arguments.putString("watermark_path", intent.getStringExtra("watermark_path"));
        arguments.putBoolean("isApplyWatermark", intent.getBooleanExtra("isApplyWatermark", false));
        arguments.putDouble("media_latitude", intent.getDoubleExtra("media_latitude", 200.0));
        arguments.putDouble("media_longitude", intent.getDoubleExtra("media_longitude", 200.0));
        arguments.putSerializable("clip_filter_type", intent.getSerializableExtra("clip_filter_type"));
        arguments.putString("intentTextBody", intent.getStringExtra("intentTextBody"));
        final PostFragment postFragment = new PostFragment();
        postFragment.setArguments(arguments);
        this.hideMenuBar(false);
        this.addFragment(postFragment, false);
    }

    protected void init(final int menuNo){
        this.menuNo = menuNo;
        this.fragmentManager = this.getSupportFragmentManager();
        this.fragmentTagStack = new FragmentTagStack();
        final MainApplication mainApplication = this.mainApplication;
        this.alarmButton = (ImageView)this.findViewById(R.id.am_button_alarm);
        this.writeButton = (ImageView)this.findViewById(R.id.am_button_write);
        this.menubar = (LinearLayout)this.findViewById(R.id.am_menubar);

        this.alarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                AceUtils.nClick(NClicks.GNB_MYFEED);
                if (menuNo != 1) {
                    MainActivity.this.mainApplication.setIsMenubarClicked(true);
                    final Intent intent = new Intent(MainActivity.this.getApplicationContext(), (Class) MyfeedActivity.class);
//                    intent.setFlags(537001984);
                    MainActivity.this.startActivity(intent);
                    return;
                }
                if (MainActivity.this.fragmentTagStack.getSize() == 1) {
                    MainActivity.this.currentFragment.scrollToTop();
                    return;
                }
                MainActivity.this.removeAllFragement();
            }
        });

        this.writeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Context context = getApplicationContext();
                    startActivityForResult(builder.build(context), CommonConstants.PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        switch(menuNo){
            case 0:
                this.menuRootFragmentClass = HomeFragment.class;
                this.addFragment(new HomeFragment(), false);
                break;
            case 1: {
                this.menuRootFragmentClass = MyfeedFragment.class;
                this.addFragment(new MyfeedFragment(), false);
                break;
            }
            case 4:
                this.menuRootFragmentClass = SearchFragment.class;
                this.addFragment(new SearchFragment(), false);
                break;

            case 5: {
                this.menuRootFragmentClass = PostFragment.class;
                this.addPostFragment(this.getIntent());
            }

        }


    }

    public void setSchemeUrl(final String mSchemeUrl) {
        this.mSchemeUrl = mSchemeUrl;
    }

    private void resumeFragment(final BaseFragment baseFragment, final boolean b) {
//        if (baseFragment != null) {
//            if (baseFragment instanceof ProfileFollowFragment) {
//                if (!b) {
//                    ((ProfileFollowFragment)baseFragment).mIsShown = true;
//                    return;
//                }
//                ((ProfileFollowFragment)baseFragment).resume();
//            }
//            else if (baseFragment instanceof EndViewPagerFragment) {
//                if (b) {
//                    AceUtils.site("EndFragment");
//                }
//            }
//            else if (baseFragment.getClass() != null) {
//                AceUtils.site(baseFragment.getClass().getSimpleName());
//            }
//        }
    }

    public Toast showCustomToast(final String text, final int duration) {
        final long currentTimeMillis = System.currentTimeMillis();
        if (this.mLastToastTime + 1000L >= currentTimeMillis) {
            return null;
        }
        this.mLastToastTime = currentTimeMillis;
        final View inflate = this.getLayoutInflater().inflate(R.layout.layout_toast, (ViewGroup)this.findViewById(R.id.layoutToast));
        ((FontSettableTextView)inflate.findViewById(R.id.txtToast)).setText((CharSequence)text);
        final Toast toast = new Toast((Context)this);
        toast.setGravity(16, 0, 0);
        toast.setDuration(duration);
        toast.setView(inflate);
        toast.show();
        return toast;
    }

    protected void clearBackStack() {
        for (int i = 0; i < this.fragmentManager.getBackStackEntryCount() - 1; ++i) {
            this.fragmentManager.popBackStack();
        }
    }
    public void refreshMenuRootFragment(final boolean b, final int n, final boolean b2, final int n2) {
        if (b) {
            if (n == 0) {
                this.currentFragment.refreshInfo();
            }
            else {
                new Handler().postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.currentFragment.refreshInfo();
                    }
                }, (long)n);
            }
        }
        if (b2) {
            if (n2 != 0) {
                new Handler().postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.currentFragment.refreshList();
                    }
                }, (long)n2);
                return;
            }
            this.currentFragment.refreshList();
        }
    }

    public void removeAllFragement() {
        this.removeAllFragement(false, 0, false, 0, true);
    }

    public void removeAllFragement(final boolean b, final int n, final boolean b2, final int n2, final boolean b3) {
        if (this.fragmentTagStack.getSize() != 1) {
            int n3 = 1;
            do {
                final String pop = this.fragmentTagStack.pop();
                final FragmentTransaction beginTransaction = this.fragmentManager.beginTransaction();
                int n4;
                if ((n4 = n3) != 0) {
                    beginTransaction.setCustomAnimations(2130968604, 2130968605);
                    n4 = 0;
                }
                beginTransaction.remove(this.fragmentManager.findFragmentByTag(pop));
                beginTransaction.commit();
                n3 = n4;
            } while (this.fragmentTagStack.getSize() != 1);
            final BaseFragment currentFragment = (BaseFragment)this.fragmentManager.findFragmentByTag(this.menuRootFragmentTag);
            final View view = currentFragment.getView();
            view.setVisibility(View.VISIBLE);
            view.startAnimation(AnimationUtils.loadAnimation((Context)this, R.anim.fragment_fade_in));
            this.clearBackStack();
            this.setCurrentFragment(currentFragment);
            this.resumeFragment(currentFragment, true);
            this.setPrevFragment(null);
            if (b3) {
                this.showMenuBar(true);
            }
            if (b) {
                if (n == 0) {
                    currentFragment.refreshInfo();
                }
                else {
                    new Handler().postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            currentFragment.refreshInfo();
                        }
                    }, (long)n);
                }
            }
            if (b2) {
                if (n2 == 0) {
                    currentFragment.refreshList();
                }
                else {
                    new Handler().postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            currentFragment.refreshList();
                        }
                    }, (long)n2);
                }
            }
            if (this.isAdding) {
                this.isAdding = false;
            }
        }
    }

    public void removeFragment() {
        this.removeFragment(true);
    }

    public void removeFragment(final boolean b) {
        this.removeFragment(b, false);
    }

    public void removeFragment(final boolean b, final boolean b2) {
        final String pop = this.fragmentTagStack.pop();
        if (!StringUtils.isBlank(pop)) {
            final BaseFragment baseFragment = (BaseFragment)this.fragmentManager.findFragmentByTag(pop);
            if (baseFragment != null) {
                baseFragment.onFadeOutBeforeAnimation();
                final FragmentTransaction beginTransaction = this.fragmentManager.beginTransaction();
                if (b) {
                    beginTransaction.setCustomAnimations(R.anim.fragment_enter_from_right, R.anim.fragment_exit_to_right);
                }
                beginTransaction.remove(baseFragment);
                if (b2) {
                    beginTransaction.commitAllowingStateLoss();
                }
                else {
                    beginTransaction.commit();
                }
                final String front = this.fragmentTagStack.getFront();
                if (StringUtils.isNotBlank(front)) {
                    final BaseFragment currentFragment = (BaseFragment)this.fragmentManager.findFragmentByTag(front);
                    this.setCurrentFragment(currentFragment);
                    this.resumeFragment(currentFragment, true);
                    currentFragment.onFadeInBeforeAnimation();
                    final View view = currentFragment.getView();
                    view.setVisibility(View.VISIBLE);
                    if (b) {
                        final Animation loadAnimation = AnimationUtils.loadAnimation((Context)this, R.anim.fragment_fade_in);
                        loadAnimation.setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener() {
                            public void onAnimationEnd(final Animation animation) {
                                currentFragment.onFadeInAfterAnimation();
                            }

                            public void onAnimationRepeat(final Animation animation) {
                            }

                            public void onAnimationStart(final Animation animation) {
                            }
                        });
                        view.startAnimation(loadAnimation);
                    }
                    else {
                        currentFragment.onFadeInAfterAnimation();
                    }
                    if (currentFragment.getDefaultMenuBarShow()) {
                        this.showMenuBar(true);
                    }
                    else {
                        this.hideMenuBar(true);
                    }
                    final String prev = this.fragmentTagStack.getPrev();
                    if (StringUtils.isNotBlank(prev)) {
                        this.setPrevFragment((BaseFragment)this.fragmentManager.findFragmentByTag(prev));
                        return;
                    }
                    this.setPrevFragment(null);
                }
            }
        }
    }




    public MapiaRequest makeRequest(final int n, final String s, final Response.Listener<JSONObject> listener) {
        if (s == null || listener == null) {
            return null;
        }
        final MapiaRequest mapiaRequest = new MapiaRequest(n, s, (JSONObject)null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError || volleyError instanceof NetworkError) {
                    MainActivity.this.showNoConnection(true);
                    new Handler().postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (NetworkStatusManager.getIsAvailableNetwork()) {
                                MainActivity.this.showNoConnection(false);
                            }
                        }
                    }, 3000L);
                }
                else if (!(volleyError instanceof AuthFailureError) && !(volleyError instanceof ServerError) && volleyError instanceof ParseError) {
                    return;
                }
            }
        });
        mapiaRequest.setRetryPolicy(RequestUtils.getNoRetryPolicy());
        MapiaVolley.getRequestQueue().add(mapiaRequest);
        return mapiaRequest;
    }

    public MapiaRequest makeRequest(final String s, final Response.Listener<JSONObject> listener) {
        if (s == null || listener == null) {
            return null;
        }
        final MapiaRequest mapiaRequest = new MapiaRequest(s, (JSONObject)null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError || volleyError instanceof NetworkError) {
                    MainActivity.this.showNoConnection(true);
                    new Handler().postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (NetworkStatusManager.getIsAvailableNetwork()) {
                                MainActivity.this.showNoConnection(false);
                            }
                        }
                    }, 3000L);
                }
                else if (!(volleyError instanceof AuthFailureError) && !(volleyError instanceof ServerError) && volleyError instanceof ParseError) {
                    return;
                }
            }
        });
        mapiaRequest.setRetryPolicy(RequestUtils.getNoRetryPolicy());
        MapiaVolley.getRequestQueue().add(mapiaRequest);
        return mapiaRequest;
    }

    public MapiaRequest makeRequest(final String s, final RetryPolicy retryPolicy, final Response.Listener<JSONObject> listener) {
        if (s == null || retryPolicy == null || listener == null) {
            return null;
        }
        final MapiaRequest mapiaRequest = new MapiaRequest(s, (JSONObject)null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError || volleyError instanceof NetworkError) {
                    MainActivity.this.showNoConnection(true);
                    new Handler().postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (NetworkStatusManager.getIsAvailableNetwork()) {
                                MainActivity.this.showNoConnection(false);
                            }
                        }
                    }, 3000L);
                }
                else if (!(volleyError instanceof AuthFailureError) && !(volleyError instanceof ServerError) && volleyError instanceof ParseError) {
                    return;
                }
            }
        });
        mapiaRequest.setRetryPolicy(retryPolicy);
        MapiaVolley.getRequestQueue().add( mapiaRequest);
        return mapiaRequest;
    }


    public void showError(final VolleyError volleyError) {
        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError || volleyError instanceof NetworkError) {
            this.showNoConnection(true);
            new Handler().postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (NetworkStatusManager.getIsAvailableNetwork()) {
                        MainActivity.this.showNoConnection(false);
                    }
                }
            }, 3000L);
        }
        else if (!(volleyError instanceof AuthFailureError) && !(volleyError instanceof ServerError) && volleyError instanceof ParseError) {
            return;
        }
    }

    public boolean isNoConnectionVisible() {
        return this.noConnection.getVisibility() == View.VISIBLE;
    }



}
