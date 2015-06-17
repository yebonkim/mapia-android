package com.mapia.myfeed;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapia.MainApplication;
import com.mapia.R;
import com.mapia.common.BaseFragment;
import com.mapia.custom.FontSettableTextView;
import com.mapia.home.HomeActivity;
import com.mapia.login.LoginInfo;
import com.mapia.search.SearchActivity;
import com.mapia.util.BitmapUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class MyfeedFragment extends BaseFragment
{
    private MyfeedPagerAdapter adapter;
    private View closeUploadingBar;
    private LinearLayout floatBar;
    private boolean isShowFloatBar;
    private RelativeLayout layout;
    private MyfeedManager manager;
    private RelativeLayout noData;
    private ImageView oneColumnButtonImage;
    private View retryUploading;
    private TextView tagCount;
    private int tagCountInt;
    private ImageView threeColumnButtonImage;
    private ObjectAnimator uploadingAnim;
    private ObjectAnimator uploadingAnim2;
    private View uploadingBar;
    private View uploadingProgressbar;
    private FontSettableTextView uploadingStatus;
    private TextView userCount;
    private int userCountInt;
    private ViewPager viewPager;

    public MyfeedFragment() {
        super();
        this.tagCountInt = 0;
        this.userCountInt = 0;
        this.isShowFloatBar = true;
    }

    private void hideBtnCloseUploadingBar() {
        this.closeUploadingBar.setVisibility(View.INVISIBLE);
    }

    private void hideBtnRetryUploading() {
        this.retryUploading.setVisibility(View.INVISIBLE);
    }

    private void setUploadingStatus(final String text) {
        if (this.uploadingStatus != null) {
            this.uploadingStatus.setText((CharSequence)text);
        }
    }

    private void showBtnCloseUploadingBar() {
        this.closeUploadingBar.setVisibility(View.VISIBLE);
    }

    private void showBtnRetryUploading() {
        this.retryUploading.setVisibility(View.VISIBLE);
    }

    private void startUploadingAnim() {
        if (this.uploadingAnim != null || this.uploadingAnim2 != null) {
            return;
        }
        (this.uploadingAnim2 = ObjectAnimator.ofFloat((Object)this.uploadingProgressbar, "scaleX", new float[] { 1.0f, 0.0f })).setDuration(700L);
        this.uploadingAnim2.setInterpolator((TimeInterpolator)new LinearInterpolator());
        (this.uploadingAnim = ObjectAnimator.ofFloat((Object)this.uploadingProgressbar, "scaleX", new float[] { 0.0f, 1.0f })).setDuration(1000L);
        this.uploadingAnim.setInterpolator((TimeInterpolator)new LinearInterpolator());
        this.uploadingAnim.addListener((Animator.AnimatorListener)new Animator.AnimatorListener() {
            public void onAnimationCancel(final Animator animator) {
            }

            public void onAnimationEnd(final Animator animator) {
                if (MyfeedFragment.this.uploadingAnim2 != null) {
                    MyfeedFragment.this.uploadingAnim2.start();
                }
            }

            public void onAnimationRepeat(final Animator animator) {
            }

            public void onAnimationStart(final Animator animator) {
                MyfeedFragment.this.uploadingProgressbar.setPivotX(0.0f);
            }
        });
        this.uploadingAnim2.addListener((Animator.AnimatorListener)new Animator.AnimatorListener() {
            public void onAnimationCancel(final Animator animator) {
            }

            public void onAnimationEnd(final Animator animator) {
                if (MyfeedFragment.this.uploadingAnim != null) {
                    MyfeedFragment.this.uploadingAnim.start();
                }
            }

            public void onAnimationRepeat(final Animator animator) {
            }

            public void onAnimationStart(final Animator animator) {
                MyfeedFragment.this.uploadingProgressbar.setPivotX((float)MyfeedFragment.this.uploadingProgressbar.getWidth());
            }
        });
        this.uploadingAnim.start();
    }

    private void stopUploadingAnim() {
        if (this.uploadingAnim != null) {
            this.uploadingAnim.cancel();
        }
        if (this.uploadingAnim2 != null) {
            this.uploadingAnim2.cancel();
        }
        this.uploadingAnim = null;
        this.uploadingAnim2 = null;
    }

    public void changeUploadingStatus(final int n) {
        this.mainActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (n == -1) {
                    MyfeedFragment.this.hideUploadingBar(true);
                    return;
                }
                MyfeedFragment.this.showUploadingBar();
                switch (n) {
                    default: {}
                    case 0: {
                        MyfeedFragment.this.hideBtnCloseUploadingBar();
                        MyfeedFragment.this.hideBtnRetryUploading();
                        MyfeedFragment.this.uploadingProgressbar.setBackgroundResource(R.drawable.progressbar_processing);
                        MyfeedFragment.this.setUploadingStatus(MyfeedFragment.this.getString(R.string.uploading));
                        MyfeedFragment.this.startUploadingAnim();
                    }
                    case 3: {
                        MyfeedFragment.this.hideBtnCloseUploadingBar();
                        MyfeedFragment.this.hideBtnRetryUploading();
                        MyfeedFragment.this.setUploadingStatus(MyfeedFragment.this.getString(R.string.processing));
                        MyfeedFragment.this.uploadingProgressbar.setBackgroundResource(R.drawable.progressbar_processing);
                        MyfeedFragment.this.startUploadingAnim();
                    }
                    case 5: {
                        MyfeedFragment.this.hideBtnCloseUploadingBar();
                        MyfeedFragment.this.hideBtnRetryUploading();
                        MyfeedFragment.this.startUploadingAnim();
                    }
                    case 1: {
                        MyfeedFragment.this.hideBtnCloseUploadingBar();
                        MyfeedFragment.this.hideBtnRetryUploading();
                        MyfeedFragment.this.stopUploadingAnim();
                        MyfeedFragment.this.setUploadingStatus(MyfeedFragment.this.getString(R.string.uploading_complete));
                        MyfeedFragment.this.uploadingProgressbar.setBackgroundColor(Color.parseColor("#4b60ea"));
                        MyfeedFragment.this.hideUploadingBar(true);
                    }
                    case 2: {
                        MyfeedFragment.this.showBtnCloseUploadingBar();
                        MyfeedFragment.this.showBtnRetryUploading();
                        MyfeedFragment.this.stopUploadingAnim();
                        MyfeedFragment.this.setUploadingStatus(MyfeedFragment.this.getString(R.string.uploading_failed));
                        MyfeedFragment.this.uploadingProgressbar.setBackgroundColor(Color.parseColor("#f54655"));
                    }
                    case 4: {
                        MyfeedFragment.this.showBtnCloseUploadingBar();
                        MyfeedFragment.this.hideBtnRetryUploading();
                        MyfeedFragment.this.stopUploadingAnim();
                        MyfeedFragment.this.setUploadingStatus(MyfeedFragment.this.getString(R.string.processing_failed));
                        MyfeedFragment.this.uploadingProgressbar.setBackgroundColor(Color.parseColor("#f54655"));
                    }
                    case 6: {
                        MyfeedFragment.this.showBtnCloseUploadingBar();
                        MyfeedFragment.this.hideBtnRetryUploading();
                        MyfeedFragment.this.stopUploadingAnim();
                        MyfeedFragment.this.setUploadingStatus(MyfeedFragment.this.getString(R.string.processing_failed_invalid_video));
                        MyfeedFragment.this.uploadingProgressbar.setBackgroundColor(Color.parseColor("#f54655"));
                    }
                }
            }
        });
    }

    public int getTagCount() {
        return this.tagCountInt;
    }

    public void hideHeader(final boolean b) {
        if (this.isShowFloatBar) {
            this.isShowFloatBar = false;
            this.floatBar.setVisibility(View.INVISIBLE);
            if (b) {
                this.floatBar.startAnimation(AnimationUtils.loadAnimation((Context)this.mainActivity, R.anim.header_exit_to_top));
            }
        }
    }

    public void hideNoDataAll() {
        this.viewPager.setVisibility(View.VISIBLE);
        this.floatBar.setVisibility(View.VISIBLE);
        this.noData.setVisibility(View.INVISIBLE);
    }

    public void hideUploadingBar(final boolean b) {
        if (this.uploadingBar == null) {
            return;
        }
        if (b) {
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.uploadingBar, "translationY", new float[] { -BitmapUtils.convertDipToPixelFloat(53.0f) });
            final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.uploadingBar, "alpha", new float[] { 0.5f });
            final AnimatorSet set = new AnimatorSet();
            set.setDuration(300L);
            set.play((Animator)ofFloat).with((Animator)ofFloat2);
            set.addListener((Animator.AnimatorListener)new Animator.AnimatorListener() {
                public void onAnimationCancel(final Animator animator) {
                }

                public void onAnimationEnd(final Animator animator) {
                    MyfeedFragment.this.uploadingBar.setVisibility(View.INVISIBLE);
                }

                public void onAnimationRepeat(final Animator animator) {
                }

                public void onAnimationStart(final Animator animator) {
                }
            });
            new Handler().postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    set.start();
                }
            }, 3000L);
            return;
        }
        this.uploadingBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBuriedBeforeAnimation() {
        this.pauseAllChildVideo();
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.manager = new MyfeedManager(this.mainActivity, this);
//        this.mainApplication.getEventBus().register(this);
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.layout = (RelativeLayout)layoutInflater.inflate(R.layout.fragment_myfeed, viewGroup, false);
        this.adapter = new MyfeedPagerAdapter(this);
        (this.viewPager = (ViewPager)this.layout.findViewById(R.id.fm_viewpager)).setAdapter(this.adapter);
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
            }

            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
            }

            @Override
            public void onPageSelected(final int n) {
            }
        });
        this.layout.findViewById(R.id.fm_header).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                MyfeedFragment.this.scrollToTop();
            }
        });
        this.layout.findViewById(R.id.fm_header).bringToFront();
        this.layout.findViewById(R.id.fm_search_button).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                final Intent intent = new Intent((Context)MyfeedFragment.this.mainActivity, (Class)SearchActivity.class);
//                intent.setFlags(537001984);
                MyfeedFragment.this.startActivity(intent);
            }
        });
        this.floatBar = (LinearLayout)this.layout.findViewById(R.id.fm_floatbar);
        this.tagCount = (TextView)this.layout.findViewById(R.id.fm_tag_count);//.setTypeface(FontUtils.getNanumRegular());
        this.layout.findViewById(R.id.fm_tag_count_area).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.MYFEED_TAG);
//                MyfeedFragment.this.mainActivity.addFragment(new MyfeedTagFragment());
            }
        });
        this.userCount = (TextView)this.layout.findViewById(R.id.fm_user_count);//).setTypeface(FontUtils.getNanumRegular());
        this.layout.findViewById(R.id.fm_user_count_area).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.MYFEED_USER);
                final Bundle arguments = new Bundle();
                arguments.putLong("memberNo", LoginInfo.getInstance().getMemberNo());
//                final MyfeedFollowFragment myfeedFollowFragment = new MyfeedFollowFragment();
//                myfeedFollowFragment.setArguments(arguments);
//                MyfeedFragment.this.mainActivity.addFragment(myfeedFollowFragment);
            }
        });
        this.oneColumnButtonImage = (ImageView)this.layout.findViewById(R.id.fm_one_column_image);
        this.threeColumnButtonImage = (ImageView)this.layout.findViewById(R.id.fm_three_column_image);
        ((LinearLayout)this.layout.findViewById(R.id.fm_user_count_area)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.MYFEED_SORTING1);
                MyfeedFragment.this.viewPager.setCurrentItem(0, false);
                MyfeedFragment.this.adapter.setListFragmentVisible(View.VISIBLE);
                MyfeedFragment.this.adapter.setGridFragmentVisible(View.INVISIBLE);
                MyfeedFragment.this.oneColumnButtonImage.setImageResource(R.drawable.common_1rowview_over);
                MyfeedFragment.this.threeColumnButtonImage.setImageResource(R.drawable.common_3rowview_normal);
            }
        });
        ((LinearLayout)this.layout.findViewById(R.id.fm_three_column)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.MYFEED_SORTING3);
                MyfeedFragment.this.viewPager.setCurrentItem(1, false);
                MyfeedFragment.this.adapter.setListFragmentVisible(View.INVISIBLE);
                MyfeedFragment.this.adapter.setGridFragmentVisible(View.VISIBLE);
                MyfeedFragment.this.oneColumnButtonImage.setImageResource(R.drawable.common_1rowview_normal);
                MyfeedFragment.this.threeColumnButtonImage.setImageResource(R.drawable.common_3rowview_over);
                MyfeedFragment.this.adapter.pauseVideoIfShowOnList();
            }
        });
        this.noData = (RelativeLayout)this.layout.findViewById(R.id.fm_nodata);
        this.uploadingBar = this.layout.findViewById(R.id.fm_uploading);
        this.uploadingStatus = (FontSettableTextView)this.uploadingBar.findViewById(R.id.fm_uploading_message);
        this.closeUploadingBar = this.uploadingBar.findViewById(R.id.fm_close_uploading_status_bar);
        this.retryUploading = this.uploadingBar.findViewById(R.id.fm_retry_uploading);
        this.uploadingProgressbar = this.uploadingBar.findViewById(R.id.fm_uploading_progressbar);
        this.closeUploadingBar.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                MyfeedFragment.this.hideUploadingBar(false);
                MainApplication.getInstance().clearBGUpload();
            }
        });
        this.retryUploading.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (MainApplication.getInstance().getUploadingStatus() == 2) {
                    MyfeedFragment.this.changeUploadingStatus(0);
//                    MainApplication.getInstance().uploadVideo();
                }
            }
        });
//        MainApplication.getInstance().setVideoUploadingUX();
        return (View)this.layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        this.mainApplication.getEventBus().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            final Field declaredField = Fragment.class.getDeclaredField("mChildFragmentManager");
            declaredField.setAccessible(true);
            declaredField.set(this, null);
        }
        catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        }
        catch (IllegalAccessException ex2) {
            throw new RuntimeException(ex2);
        }
    }

    @Override
    public void onEvent(final Map<String, Object> map) {
        if ("ecft".equals(map.get("event"))) {
            this.refreshInfo();
        }
        else {
            if ("ecfu".equals(map.get("event"))) {
                this.refreshInfo();
                return;
            }
            if ("ebu".equals(map.get("event"))) {
//                final int int1 = Integer.parseInt(map.get("status"));
//                this.changeUploadingStatus(int1);
//                if (this.noData.getVisibility() == 0 && int1 == 1) {
//                    this.adapter.refreshAllPage();
//                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (this.viewPager.getCurrentItem()) {
            default: {}
            case 0: {
                this.adapter.setGridFragmentVisible(8);
            }
            case 1: {
                this.adapter.setListFragmentVisible(8);
            }
        }
    }

    public void pauseAllChildVideo() {
//        final MyfeedPicListFragment myfeedPicListFragment = this.adapter.getMyfeedPicListFragment();
//        if (myfeedPicListFragment != null) {
//            myfeedPicListFragment.pauseVideoIfShowOnList();
//        }
    }

    @Override
    public void refreshInfo() {
        this.manager.getMyfeedInfo();
    }

    @Override
    public void refreshList() {
        this.adapter.refreshAllPage();
    }

    public void refreshPage(final int n) {
        this.adapter.refreshPage(n);
    }

    @Override
    public void scrollToTop() {
        this.adapter.scrollToTop(this.viewPager.getCurrentItem());
    }

    @Override
    public void setConnectionCondition(final boolean connectionCondition) {
        this.adapter.setConnectionCondition(connectionCondition);
    }

    public void setTagCount(final int tagCountInt) {
        this.tagCount.setText((CharSequence)("" + tagCountInt));
        this.tagCountInt = tagCountInt;
    }

    public void setUserCount(final int userCountInt) {
        this.userCount.setText((CharSequence)("" + userCountInt));
        this.userCountInt = userCountInt;
    }

    public void showHeader(final boolean b) {
        if (!this.isShowFloatBar) {
            this.isShowFloatBar = true;
            this.floatBar.setVisibility(View.VISIBLE);
            if (b) {
                this.floatBar.startAnimation(AnimationUtils.loadAnimation((Context)this.mainActivity, R.anim.header_enter_from_top));
            }
        }
    }

    public void showNoData() {
        this.noData.setVisibility(View.VISIBLE);
        this.noData.startAnimation(AnimationUtils.loadAnimation((Context)this.mainActivity, R.anim.bitmap_fade_in_200));
        ((FontSettableTextView)this.layout.findViewById(R.id.fm_nodata_text1)).setText((CharSequence)this.getString(R.string.myfeed_no_data_01));
        ((FontSettableTextView)this.layout.findViewById(R.id.fm_nodata_text2)).setText((CharSequence)this.getString(R.string.myfeed_no_data_02));
        this.layout.findViewById(R.id.fm_go_home).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.MYFEED_HOME);
                final Intent intent = new Intent((Context)MyfeedFragment.this.mainActivity, (Class)HomeActivity.class);
//                intent.setFlags(537001984);
                MyfeedFragment.this.startActivity(intent);
            }
        });
        this.layout.findViewById(R.id.fm_make_my_pic).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.MYFEED_CAMERA);
                MainApplication.getInstance().getPostingInfo().mode = 0;
                MyfeedFragment.this.mainActivity.startCameraActivity();
            }
        });
        this.viewPager.setVisibility(View.INVISIBLE);
    }

    public void showNoDataByNoUserNoTag() {
        this.hideHeader(true);
        this.noData.setVisibility(View.VISIBLE);
        this.noData.startAnimation(AnimationUtils.loadAnimation((Context) this.mainActivity, R.anim.bitmap_fade_in_200));
        this.layout.findViewById(R.id.fm_floatbar).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.MYFEED_HOME);
                final Intent intent = new Intent((Context)MyfeedFragment.this.mainActivity, (Class)HomeActivity.class);
//                intent.setFlags(537001984);
                MyfeedFragment.this.startActivity(intent);
            }
        });
        this.layout.findViewById(R.id.fm_make_my_pic).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.MYFEED_CAMERA);
                MainApplication.getInstance().getPostingInfo().mode = 0;
                MyfeedFragment.this.mainActivity.startCameraActivity();
            }
        });
        this.viewPager.setVisibility(View.INVISIBLE);
    }

    public void showUploadingBar() {
        if (this.uploadingBar == null || this.uploadingBar.getVisibility() == View.VISIBLE) {
            return;
        }
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                final Bitmap uploadingVidThumb = MainApplication.getInstance().getUploadingVidThumb();
                if (uploadingVidThumb != null) {
                    ((ImageView)MyfeedFragment.this.uploadingBar.findViewById(R.id.fm_video_thumb)).setImageBitmap(uploadingVidThumb);
                    MainApplication.getInstance().setUploadingVidThumb(null);
                }
                MyfeedFragment.this.uploadingBar.setVisibility(View.VISIBLE);
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)MyfeedFragment.this.uploadingBar, "translationY", new float[] { 0.0f });
                final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)MyfeedFragment.this.uploadingBar, "alpha", new float[] { 0.5f, 1.0f });
                final AnimatorSet set = new AnimatorSet();
                set.setDuration(300L);
                set.play((Animator)ofFloat).with((Animator)ofFloat2);
                set.start();
            }
        }, 1500L);
    }
}