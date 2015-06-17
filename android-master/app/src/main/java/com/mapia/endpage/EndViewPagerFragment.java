//package com.mapia.endpage;
//
///**
// * Created by daehyun on 15. 6. 8..
// */
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.AnimationDrawable;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.text.Editable;
//import android.text.InputFilter;
//import android.text.Spanned;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.makeramen.RoundedImageView;
//import com.mapia.api.model.End;
//import com.mapia.api.model.Picker;
//import com.mapia.common.BaseFragment;
//import com.mapia.common.Thumb;
//import com.mapia.custom.FontSettableTextView;
//import com.mapia.login.LoginInfo;
//import com.mapia.mention.MentionModel;
//import com.mapia.mention.MentionUtil;
//import com.mapia.util.PreferenceUtils;
//import com.mapia.util.ThumbUtils;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//
//public class EndViewPagerFragment extends BaseFragment
//{
//    private long FOLLOW_BUTTON_CLICK_PERIOD;
//    private long LIKE_BUTTON_CLICK_PERIOD;
//    private long beforeFollowButtonClickTime;
//    private long beforeLikeButtonClickTime;
//    private ImageView likeButtonOff;
//    private ImageView likeButtonOnLeft;
//    private ImageView likeButtonOnRight;
//    private RelativeLayout likeButtonScale;
//    private EndViewPagerAdapter mAdapter;
//    private Toast mAlertToast;
//    private boolean mAutoPlay;
//    private ArrayList<MentionModel> mAutocompletionFollowList;
//    private FrameLayout mBottomLayout;
//    private View mBtnLike;
//    private View mCommentLayout;
//    private SwipeEndFragment mCurrentFragment;
//    private int mCurrentPosition;
//    private MapiaEditText mEditComment;
//    private View mEndUtilityBar;
//    private View$OnClickListener mEndUtilityBarItemClickListener;
//    private View mFollowBtn;
//    private ArrayList<MentionModel> mFollowingList;
//    private View mMainSplash;
//    private MentionListviewAdapter mMentionListAdapter;
//    private ListView mMentionListView;
//    private boolean mMentionMode;
//    private RelativeLayout mNavigationBar;
//    private ViewPager.OnPageChangeListener mPageChangeListener;
//    private int mPrevState;
//    private ImageLoader.ImageContainer mProfileImageContainer;
//    private View mRootView;
//    private SharingLayout mSharingLayout;
//    private View$OnClickListener mSnsSharingClick;
//    private int mStartPosition;
//    private View mTopDivider;
//    private FontSettableTextView mUtilityBarCommentCount;
//    private ViewPager mViewPager;
//
//    public EndViewPagerFragment() {
//        super();
//        this.FOLLOW_BUTTON_CLICK_PERIOD = 0L;
//        this.beforeFollowButtonClickTime = System.currentTimeMillis();
//        this.LIKE_BUTTON_CLICK_PERIOD = 0L;
//        this.beforeLikeButtonClickTime = System.currentTimeMillis();
//        this.mEndUtilityBarItemClickListener = (View$OnClickListener)new View$OnClickListener() {
//            public void onClick(final View view) {
//                if (!StringUtils.isBlank(EndViewPagerFragment.this.mCurrentFragment.getEnd().picImageUrl)) {
//                    switch (view.getId()) {
//                        default: {}
//                        case 2131362175: {
//                            final long currentTimeMillis = System.currentTimeMillis();
//                            if (currentTimeMillis - EndViewPagerFragment.this.beforeLikeButtonClickTime < EndViewPagerFragment.this.LIKE_BUTTON_CLICK_PERIOD) {
//                                break;
//                            }
//                            EndViewPagerFragment.this.beforeLikeButtonClickTime = currentTimeMillis;
//                            EndViewPagerFragment.this.LIKE_BUTTON_CLICK_PERIOD = 700L;
//                            if (EndViewPagerFragment.this.mCurrentFragment == null) {
//                                break;
//                            }
//                            AceUtils.nClick(NClicks.END_GOOD);
//                            if (EndViewPagerFragment.this.mBtnLike.isSelected()) {
//                                EndViewPagerFragment.this.mCurrentFragment.decreaseLike(true);
//                                EndViewPagerFragment.this.mCurrentFragment.requestUnlike();
//                                return;
//                            }
//                            EndViewPagerFragment.this.mCurrentFragment.increaseLike(true);
//                            EndViewPagerFragment.this.mCurrentFragment.requestLike();
//                            EndViewPagerFragment.this.mCurrentFragment.showLikeAnimationStart();
//                        }
//                        case 2131362181: {
//                            if (EndViewPagerFragment.this.mCurrentFragment != null) {
//                                AceUtils.nClick(NClicks.END_COMMENT);
//                                EndViewPagerFragment.this.showSoftKeyboard();
//                                EndViewPagerFragment.this.mCurrentFragment.scrollToTheLast();
//                                EndViewPagerFragment.this.showCommentInputLayout();
//                                return;
//                            }
//                            break;
//                        }
//                        case 2131362184: {
//                            if (EndViewPagerFragment.this.mCurrentFragment != null) {
//                                AceUtils.nClick(NClicks.END_REPIC);
//                                EndViewPagerFragment.this.mCurrentFragment.addRepicFragment();
//                                return;
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
//        };
//        this.mPageChangeListener = new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrollStateChanged(final int n) {
//                switch (n) {
//                    case 1: {
//                        EndViewPagerFragment.this.hideDivider();
//                        EndViewPagerFragment.this.pauseAllChildVideo();
//                        break;
//                    }
//                    case 0: {
//                        EndViewPagerFragment.this.showDivider();
//                        if (AutoPlayUtil.isAlways()) {
//                            EndViewPagerFragment.this.mAdapter.getRegisteredFragment(EndViewPagerFragment.this.mCurrentPosition).playVideo();
//                            break;
//                        }
//                        if (AutoPlayUtil.isWifi() && NetworkUtils.isWifi((Context)EndViewPagerFragment.this.getActivity())) {
//                            EndViewPagerFragment.this.mAdapter.getRegisteredFragment(EndViewPagerFragment.this.mCurrentPosition).playVideo();
//                            break;
//                        }
//                        break;
//                    }
//                }
//                EndViewPagerFragment.this.mPrevState = n;
//            }
//
//            @Override
//            public void onPageScrolled(final int n, final float n2, final int n3) {
//            }
//
//            @Override
//            public void onPageSelected(final int n) {
//                if (EndViewPagerFragment.this.mCurrentFragment != null) {
//                    EndViewPagerFragment.this.mCurrentFragment.clear();
//                    EndViewPagerFragment.this.mCurrentFragment.setIsCurrent(false);
//                    EndViewPagerFragment.this.mCurrentFragment = null;
//                }
//                if (EndViewPagerFragment.this.mCurrentPosition > n) {
//                    AceUtils.nClick(NClicks.END_PREEND);
//                }
//                else if (EndViewPagerFragment.this.mCurrentPosition < n) {
//                    AceUtils.nClick(NClicks.END_NEXTEND);
//                }
//                EndViewPagerFragment.this.mCurrentPosition = n;
//                EndViewPagerFragment.this.setCurrentFragment(EndViewPagerFragment.this.mAdapter.getRegisteredFragment(EndViewPagerFragment.this.mCurrentPosition));
//            }
//        };
//        this.mSnsSharingClick = (View$OnClickListener)new View$OnClickListener() {
//            public void onClick(final View view) {
//                final SnsInfo snsInfo = (SnsInfo)view.getTag();
//                if (snsInfo == null) {
//                    return;
//                }
//                final End end = EndViewPagerFragment.this.mCurrentFragment.getEnd();
//                final String endWebUrl = QueryManager.makeEndWebUrl(end.picker.getMemberNo(), end.picNo);
//                EndViewPagerFragment.this.mSharingLayout.share(snsInfo, EndViewPagerFragment.this.getSharingMessage(end, snsInfo.pkgName, endWebUrl), end.picImageUrl + "?type=f238_238", endWebUrl);
//            }
//        };
//        this.defaultMenuBarShow = false;
//    }
//
//    private String getCurrentParagraph() {
//        final int selectionEnd = this.mEditComment.getSelectionEnd();
//        final Editable text = this.mEditComment.getText();
//        if (selectionEnd > 0 && selectionEnd <= text.length() && text.charAt(selectionEnd - 1) == '@') {
//            return "@";
//        }
//        int i;
//        for (i = selectionEnd; i > 0; --i) {
//            final char char1 = text.charAt(i - 1);
//            if (Character.isWhitespace(char1)) {
//                return text.subSequence(i, selectionEnd).toString();
//            }
//            if (char1 == '@') {
//                return text.subSequence(i - 1, selectionEnd).toString();
//            }
//        }
//        return text.subSequence(i, selectionEnd).toString();
//    }
//
//    private int getNicknameStart() {
//        int selectionStart = this.mEditComment.getSelectionStart();
//        final Editable text = this.mEditComment.getText();
//        int n = 0;
//        int n2;
//        while (true) {
//            n2 = selectionStart;
//            if (selectionStart <= 0) {
//                break;
//            }
//            n2 = selectionStart;
//            if (n >= 15) {
//                break;
//            }
//            final char char1 = text.charAt(selectionStart - 1);
//            if (Character.isWhitespace(char1) || char1 == '@') {
//                n2 = selectionStart - 1;
//                break;
//            }
//            --selectionStart;
//            ++n;
//        }
//        return n2;
//    }
//
//    private String getSharingMessage(final End end, final String s, final String s2) {
//        String string = s2;
//        if (s.equals(this.getString(2131558734)) || s.equals("")) {
//            string = end.body + "\n" + s2;
//        }
//        else if (s.equals(this.getString(2131558733))) {
//            return end.body;
//        }
//        return string;
//    }
//
//    private EndViewPagerAdapter getViewPagerAdapter() {
//        final Bundle arguments = this.getArguments();
//        if (arguments == null) {
//            return null;
//        }
//        final int int1 = arguments.getInt("from");
//        final int int2 = arguments.getInt("size");
//        final int int3 = arguments.getInt("page");
//        final long long1 = arguments.getLong("memberNo");
//        final long long2 = arguments.getLong("lastFeedId");
//        final String string = arguments.getString("tag", "");
//        final String string2 = arguments.getString("order", "");
//        final String string3 = arguments.getString("locationCode");
//        final boolean boolean1 = arguments.getBoolean("hasNextPage");
//        final ArrayList list = (ArrayList)arguments.getSerializable("endArgs");
//        this.mStartPosition = arguments.getInt("position");
//        switch (int1) {
//            default: {
//                this.mAdapter = new EndViewPagerAdapter(this.mainActivity, this.getChildFragmentManager(), list) {
//                    @Override
//                    protected boolean hasNextPage(final int n) {
//                        return false;
//                    }
//
//                    @Override
//                    protected void loadAdditionalPages() {
//                    }
//                };
//                break;
//            }
//            case 2: {
//                this.mAdapter = new MyfeedViewPagerAdapter(this.mainActivity, this.getChildFragmentManager(), list, long2, int2, boolean1);
//                break;
//            }
//            case 3:
//            case 4: {
//                this.mAdapter = new TaggalleryViewPagerAdapter(this.mainActivity, this.getChildFragmentManager(), list, int3, string2, string, int2, boolean1);
//                break;
//            }
//            case 6: {
//                this.mAdapter = new LocationgalleryViewPagerAdapter(this.mainActivity, this.getChildFragmentManager(), list, int3, string2, string3, int2, boolean1);
//                break;
//            }
//            case 7: {
//                this.mAdapter = new ProfilePicListViewPagerAdapter(this.mainActivity, this.getChildFragmentManager(), (Profile)arguments.getSerializable("profile"), list, long1, int3, int2, boolean1);
//                break;
//            }
//            case 5: {
//                this.mAdapter = new LikeViewPagerAdapter(this.mainActivity, this.getChildFragmentManager(), list, int3, int2, boolean1);
//                break;
//            }
//            case 8: {
//                this.mAdapter = new RepicViewPagerAdapter(this.mainActivity, this.getChildFragmentManager(), list, arguments.getLong("picNo"), long1, int3, int2, boolean1);
//                break;
//            }
//            case 10: {
//                this.mAdapter = new SearchPicViewPagerAdapter(this.mainActivity, this.getChildFragmentManager(), list, arguments.getString("query"), int3, int2, boolean1);
//                break;
//            }
//        }
//        this.mAdapter.setViewPagerFragment(this);
//        return this.mAdapter;
//    }
//
//    private void hideDivider() {
//        this.mTopDivider.setVisibility(8);
//    }
//
//    private void initCommentLayout() {
//        (this.mEditComment = (MapiaEditText)this.mCommentLayout.findViewById(2131362189)).setMentionKeyListener((MapiaEditText.OnMentionKeyListener)new MapiaEditText.OnMentionKeyListener() {
//            @Override
//            public void onMentionCharRemoved() {
//                EndViewPagerFragment.this.mMentionMode = false;
//                EndViewPagerFragment.this.mMentionListView.setVisibility(8);
//            }
//        });
//        this.mEditComment.setFilters(new InputFilter[] { new InputFilter$LengthFilter(500) {
//            public CharSequence filter(CharSequence filter, final int n, final int n2, final Spanned spanned, final int n3, final int n4) {
//                filter = super.filter(filter, n, n2, spanned, n3, n4);
//                if (filter != null && (EndViewPagerFragment.this.mAlertToast == null || !EndViewPagerFragment.this.mAlertToast.getView().isShown())) {
//                    EndViewPagerFragment.this.mAlertToast = EndViewPagerFragment.this.mainActivity.showCustomToast(EndViewPagerFragment.this.getString(2131558761), 0);
//                }
//                return filter;
//            }
//        }, new InputFilter() {
//            public CharSequence filter(final CharSequence charSequence, final int n, final int n2, final Spanned spanned, final int n3, final int n4) {
//                if ("@".equals(charSequence)) {
//                    AceUtils.nClick(NClicks.END_COMMENT_PROFILELIST);
//                    if (EndViewPagerFragment.this.mFollowingList != null && EndViewPagerFragment.this.mFollowingList.size() > 0) {
//                        EndViewPagerFragment.this.showMentionListView();
//                        EndViewPagerFragment.this.mMentionMode = true;
//                    }
//                }
//                else if (charSequence != null && charSequence.length() >= 1 && Character.isWhitespace(charSequence.charAt(0))) {
//                    return null;
//                }
//                return null;
//            }
//        } });
//        this.mEditComment.addTextChangedListener((TextWatcher)new TextWatcher() {
//            public void afterTextChanged(final Editable editable) {
//            }
//
//            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
//            }
//
//            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
//                EndViewPagerFragment.this.updateMentionList();
//            }
//        });
//        this.mEditComment.setMapiaEditTextEventListener((MapiaEditText.MapiaEditTextEvent)new MapiaEditText.MapiaEditTextEvent() {
//            @Override
//            public boolean onBackKeyDownPreIme(final int n, final KeyEvent keyEvent) {
//                EndViewPagerFragment.this.hideCommentInputLayout();
//                return true;
//            }
//
//            @Override
//            public void onCursorPositionChanged() {
//            }
//        });
//        this.mCommentLayout.findViewById(2131362187).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
//            public void onClick(final View view) {
//                EndViewPagerFragment.this.mCurrentFragment.addComment(EndViewPagerFragment.this.mEditComment.getText().toString());
//                EndViewPagerFragment.this.hideCommentInputLayout();
//                EndViewPagerFragment.this.mEditComment.setText((CharSequence)"");
//            }
//        });
//    }
//
//    private void initLikeButtonView() {
//        this.likeButtonScale = (RelativeLayout)this.mRootView.findViewById(2131362176);
//        this.likeButtonOff = (ImageView)this.mRootView.findViewById(2131362179);
//        this.likeButtonOnLeft = (ImageView)this.mRootView.findViewById(2131362177);
//        this.likeButtonOnRight = (ImageView)this.mRootView.findViewById(2131362178);
//    }
//
//    private void initMentionListView() {
//        this.mFollowingList = MentionUtil.getInstance().getMentionList();
//        if (!MentionUtil.getInstance().isFresh() || this.mFollowingList == null || this.mFollowingList.size() <= 0) {
//            MentionUtil.getInstance().requestMentionList(this.mainActivity, (MentionUtil.MentionListEvent)new MentionUtil.MentionListEvent() {
//                @Override
//                public void onMentionUpdated(final ArrayList<MentionModel> list) {
//                    EndViewPagerFragment.this.mFollowingList = list;
//                    EndViewPagerFragment.this.mMentionListAdapter.clear();
//                    EndViewPagerFragment.this.mAutocompletionFollowList.clear();
//                    for (int i = 0; i < EndViewPagerFragment.this.mFollowingList.size(); ++i) {
//                        EndViewPagerFragment.this.mAutocompletionFollowList.add(EndViewPagerFragment.this.mFollowingList.get(i));
//                    }
//                    EndViewPagerFragment.this.mMentionListAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//        this.mAutocompletionFollowList = new ArrayList<MentionModel>();
//        this.mMentionListAdapter = new MentionListviewAdapter((Context)this.mainActivity, 2130903232, this.mAutocompletionFollowList);
//        this.mMentionListView.setAdapter((ListAdapter)this.mMentionListAdapter);
//    }
//
//    private void initNavigationBar() {
//        this.mNavigationBar.setVisibility(0);
//        final View viewById = this.mNavigationBar.findViewById(2131361953);
//        final View viewById2 = this.mNavigationBar.findViewById(2131361954);
//        final View viewById3 = this.mNavigationBar.findViewById(2131362208);
//        viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
//            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.END_PRE);
//                EndViewPagerFragment.this.onBackPressed();
//            }
//        });
//        viewById2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
//            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.END_SHARE);
//                if (EndViewPagerFragment.this.mSharingLayout != null) {
//                    EndViewPagerFragment.this.mSharingLayout.show();
//                }
//            }
//        });
//        viewById3.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
//            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.END_PROFILE);
//                if (EndViewPagerFragment.this.mCurrentFragment != null) {
//                    EndViewPagerFragment.this.mCurrentFragment.openProfile();
//                }
//            }
//        });
//    }
//
//    private void initSharing() {
//        (this.mSharingLayout = new SharingLayout((Context)this.mainActivity)).init(this.mainActivity, this.mSnsSharingClick);
//        ((RelativeLayout)this.mRootView).addView((View)this.mSharingLayout);
//    }
//
//    private void initUtilityBar() {
//        this.mEndUtilityBar = this.mBottomLayout.findViewById(2131362174);
//        this.mUtilityBarCommentCount = (FontSettableTextView)this.mEndUtilityBar.findViewById(2131362183);
//        this.mBtnLike = this.mBottomLayout.findViewById(2131362175);
//        this.mCommentLayout = this.mBottomLayout.findViewById(2131362186);
//        this.initCommentLayout();
//    }
//
//    private void initViewPager() {
//        (this.mAdapter = this.getViewPagerAdapter()).setStartingPosition(this.mStartPosition);
//        (this.mViewPager = (ViewPager)this.mRootView.findViewById(2131362211)).setPageTransformer(false, (ViewPager.PageTransformer)new MapiaPageTransformer());
//        this.mViewPager.setOnPageChangeListener(this.mPageChangeListener);
//        new Handler().postDelayed((Runnable)new Runnable() {
//            @Override
//            public void run() {
//                EndViewPagerFragment.this.mViewPager.setAdapter(EndViewPagerFragment.this.mAdapter);
//                EndViewPagerFragment.this.mViewPager.setCurrentItem(EndViewPagerFragment.this.mStartPosition, false);
//                EndViewPagerFragment.this.showLoading(false);
//                EndViewPagerFragment.this.mViewPager.post((Runnable)new Runnable() {
//                    @Override
//                    public void run() {
//                        EndViewPagerFragment.this.mPageChangeListener.onPageSelected(EndViewPagerFragment.this.mViewPager.getCurrentItem());
//                    }
//                });
//            }
//        }, 200L);
//    }
//
//    private boolean isFirst() {
//        final boolean first = PreferenceUtils.isFirst("endfragment");
//        PreferenceUtils.setVisit("endfragment");
//        return first;
//    }
//
//    private void pauseAllChildVideo() {
//        for (int i = this.mCurrentPosition - 1; i < this.mCurrentPosition + 2; ++i) {
//            if (this.mAdapter != null) {
//                final SwipeEndFragment registeredFragment = this.mAdapter.getRegisteredFragment(i);
//                if (registeredFragment != null) {
//                    registeredFragment.pauseVideo();
//                }
//            }
//        }
//    }
//
//    private void setNavigationBarUX(final long n, final String text, String string, final String s) {
//        if (!this.isCurrentFragment()) {
//            return;
//        }
//        final RoundedImageView roundedImageView = (RoundedImageView)this.mNavigationBar.findViewById(2131362209);
//        final TextView textView = (TextView)this.mNavigationBar.findViewById(2131362159);
//        final View viewById = this.mNavigationBar.findViewById(2131362207);
//        string += ThumbUtils.getSuffix(Thumb.END_BODY_ICON);
//        Label_0207: {
//            if (!string.trim().contains("http://")) {
//                break Label_0207;
//            }
//            if (this.mProfileImageContainer != null) {
//                this.mProfileImageContainer.cancelRequest();
//                this.mProfileImageContainer = null;
//            }
//            while (true) {
//                try {
//                    Glide.with(this.mainActivity).load(string).asBitmap().listener((RequestListenersuper Object, Bitmap >)new RequestListener<String, Bitmap>() {
//                        @Override
//                        public boolean onException(final Exception ex, final String s, final Target<Bitmap> target, final boolean b) {
//                            EndViewPagerFragment.this.mainActivity.showError(new VolleyError());
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(final Bitmap imageBitmap, final String s, final Target<Bitmap> target, final boolean b, final boolean b2) {
//                            if (imageBitmap != null) {
//                                roundedImageView.setImageBitmap(imageBitmap);
//                            }
//                            return false;
//                        }
//                    }).preload();
//                    textView.setText((CharSequence)text);
//                    if (n != LoginInfo.getInstance().getMemberNo() && s != null && !s.equalsIgnoreCase("null")) {
//                        viewById.setVisibility(0);
//                        viewById.setSelected("Y".equalsIgnoreCase(s));
//                        viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
//                            public void onClick(final View view) {
//                                final long currentTimeMillis = System.currentTimeMillis();
//                                if (currentTimeMillis - EndViewPagerFragment.this.beforeFollowButtonClickTime < EndViewPagerFragment.this.FOLLOW_BUTTON_CLICK_PERIOD) {
//                                    return;
//                                }
//                                EndViewPagerFragment.this.beforeFollowButtonClickTime = currentTimeMillis;
//                                EndViewPagerFragment.this.FOLLOW_BUTTON_CLICK_PERIOD = 700L;
//                                EndViewPagerFragment.this.mCurrentFragment.requestFollow(EndViewPagerFragment.this.mCurrentFragment.getEnd().picker);
//                            }
//                        });
//                        return;
//                    }
//                    viewById.setVisibility(8);
//                    return;
//                    roundedImageView.setImageResource(2130837695);
//                    continue;
//                }
//                catch (Exception ex) {
//                    continue;
//                }
//                break;
//            }
//        }
//    }
//
//    private void showCommentInputLayout() {
//        this.mCommentLayout.bringToFront();
//        this.mCommentLayout.setVisibility(0);
//        this.mEndUtilityBar.setVisibility(8);
//    }
//
//    private void showDivider() {
//        this.mTopDivider.setVisibility(0);
//        this.mTopDivider.startAnimation(AnimationUtils.loadAnimation((Context) this.mainActivity, 2130968589));
//    }
//
//    private void showSoftKeyboard() {
//        if (this.mEditComment != null) {
//            this.mEditComment.requestFocus();
//            ((InputMethodManager)this.mainActivity.getSystemService("input_method")).showSoftInput((View)this.mEditComment, 1);
//        }
//    }
//
//    public void addMentionNickname(final String s) {
//        if (this.mEditComment != null) {
//            this.mEditComment.append((CharSequence)s);
//        }
//    }
//
//    public boolean getAutoPlay() {
//        return this.mAutoPlay;
//    }
//
//    public void hideBottomBar() {
//        this.mBottomLayout.setVisibility(8);
//    }
//
//    public void hideCommentInputLayout() {
//        if (this.mEndUtilityBar != null && this.mCommentLayout != null) {
//            this.mEndUtilityBar.setVisibility(0);
//            this.mCommentLayout.setVisibility(8);
//            this.hideSoftKeyboard();
//        }
//    }
//
//    public void hideNavigationBar() {
//        this.mNavigationBar.setVisibility(8);
//    }
//
//    @Override
//    public void hideSoftKeyboard() {
//        if (this.mEditComment != null) {
//            ((InputMethodManager)this.mainActivity.getSystemService("input_method")).hideSoftInputFromWindow(this.mEditComment.getWindowToken(), 0);
//        }
//    }
//
//    public boolean isCommentInputMode() {
//        return this.mCommentLayout != null && this.mCommentLayout.getVisibility() == 0;
//    }
//
//    boolean isCurrentFragment() {
//        return this.mViewPager != null && this.mCurrentFragment != null && this.mCurrentPosition == this.mCurrentFragment.getPosition();
//    }
//
//    @Override
//    protected void onAnimationEnded() {
//        this.initViewPager();
//    }
//
//    @Override
//    protected void onAnimationStarted() {
//    }
//
//    @Override
//    public boolean onBackPressed() {
//        if (this.mViewPager == null) {
//            return false;
//        }
//        if (this.mCommentLayout != null && this.mCommentLayout.getVisibility() == 0) {
//            this.hideCommentInputLayout();
//        }
//        this.mainActivity.removeFragment(true);
//        return true;
//    }
//
//    @Override
//    public void onBuriedBeforeAnimation() {
//        super.onBuriedBeforeAnimation();
//        this.pauseAllChildVideo();
//        this.hideSoftKeyboard();
//    }
//
//    @Override
//    public void onCreate(final Bundle bundle) {
//        super.onCreate(bundle);
//    }
//
//    @Override
//    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
//        this.mRootView = layoutInflater.inflate(2130903106, viewGroup, false);
//        this.mNavigationBar = (RelativeLayout)this.mRootView.findViewById(2131361922);
//        this.mTopDivider = this.mNavigationBar.findViewById(2131362210);
//        this.mFollowBtn = this.mNavigationBar.findViewById(2131362207);
//        this.mBottomLayout = (FrameLayout)this.mRootView.findViewById(2131362173);
//        this.mMentionListView = (ListView)this.mRootView.findViewById(2131362193);
//        this.mMainSplash = this.mRootView.findViewById(2131362212);
//        this.showLoading(true);
//        this.initNavigationBar();
//        this.initUtilityBar();
//        this.initMentionListView();
//        this.initLikeButtonView();
//        this.initSharing();
//        if (AutoPlayUtil.isAlways()) {
//            this.mAutoPlay = true;
//        }
//        else if (AutoPlayUtil.isWifi() && NetworkUtils.isWifi((Context)this.mainActivity)) {
//            this.mAutoPlay = true;
//        }
//        if (this.isFirst()) {
//            this.showCoach();
//        }
//        return this.mRootView;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        try {
//            final Field declaredField = Fragment.class.getDeclaredField("mChildFragmentManager");
//            declaredField.setAccessible(true);
//            declaredField.set(this, null);
//        }
//        catch (NoSuchFieldException ex) {
//            throw new RuntimeException(ex);
//        }
//        catch (IllegalAccessException ex2) {
//            throw new RuntimeException(ex2);
//        }
//    }
//
//    public void setBottomUX(final End end) {
//        if (end != null) {
//            this.updateLikeCount(end, false);
//            this.updateCommentCount(end);
//            this.updateRepicCount(end);
//            this.setUtilityBarEvent();
//        }
//    }
//
//    public void setCurrentFragment(final SwipeEndFragment mCurrentFragment) {
//        this.mCurrentFragment = mCurrentFragment;
//        if (this.mCurrentFragment != null) {
//            this.setNavigationBarUX();
//            this.setBottomUX(this.mCurrentFragment.getEnd());
//            this.mCurrentFragment.onSelected(this.mAutoPlay);
//            AceUtils.site("EndFragment");
//        }
//    }
//
//    public void setNavigationBarUX() {
//        if (this.mCurrentFragment != null && this.mCurrentFragment.getEnd() != null) {
//            this.setNavigationBarUX(this.mCurrentFragment.getEnd());
//        }
//    }
//
//    public void setNavigationBarUX(final End end) {
//        final Picker picker = end.picker;
//        if (picker != null) {
//            this.setNavigationBarUX(picker.getMemberNo(), picker.getNickname(), picker.getImageUrl(), picker.getFollowingYn());
//        }
//    }
//
//    protected void setUtilityBarEvent() {
//        this.mEndUtilityBar.findViewById(2131362175).setOnClickListener(this.mEndUtilityBarItemClickListener);
//        this.mEndUtilityBar.findViewById(2131362181).setOnClickListener(this.mEndUtilityBarItemClickListener);
//        this.mEndUtilityBar.findViewById(2131362184).setOnClickListener(this.mEndUtilityBarItemClickListener);
//    }
//
//    public void showBottomBar() {
//        this.mBottomLayout.setVisibility(0);
//    }
//
//    protected void showCoach() {
//        final View viewById = this.mRootView.findViewById(2131362194);
//        viewById.setVisibility(0);
//        viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
//            public void onClick(final View view) {
//                viewById.setVisibility(8);
//            }
//        });
//    }
//
//    public void showLoading(final boolean b) {
//        if (b) {
//            this.mMainSplash.setVisibility(0);
//            ((AnimationDrawable)this.mMainSplash.getBackground()).start();
//            return;
//        }
//        ((AnimationDrawable)this.mMainSplash.getBackground()).stop();
//        this.mMainSplash.setVisibility(8);
//    }
//
//    public void showMentionListView() {
//        if (this.mFollowingList == null) {
//            return;
//        }
//        this.mMentionListView.bringToFront();
//        this.mMentionListView.setVisibility(0);
//        this.mMentionListView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
//            public void onItemClick(final AdapterView adapterView, final View view, final int n, final long n2) {
//                EndViewPagerFragment.this.mEditComment.getText().replace(EndViewPagerFragment.this.getNicknameStart() + 1, EndViewPagerFragment.this.mEditComment.getSelectionStart(), (CharSequence)((MentionModel)adapterView.getItemAtPosition(n)).nickname);
//                EndViewPagerFragment.this.mEditComment.getText().insert(EndViewPagerFragment.this.mEditComment.getSelectionStart(), (CharSequence)" ");
//                EndViewPagerFragment.this.mMentionListView.setVisibility(8);
//            }
//        });
//    }
//
//    public void showNavigationBar() {
//        this.mNavigationBar.setVisibility(0);
//    }
//
//    public void updateCommentCount(final End end) {
//        int replyCount = 0;
//        if (end != null) {
//            replyCount = end.replyCount;
//        }
//        if (replyCount <= 0) {
//            this.mUtilityBarCommentCount.setVisibility(8);
//            return;
//        }
//        this.mUtilityBarCommentCount.setVisibility(0);
//        this.mUtilityBarCommentCount.setText((CharSequence)String.valueOf(replyCount));
//    }
//
//    public void updateFollowBtn(final boolean selected) {
//        this.mFollowBtn.setSelected(selected);
//    }
//
//    public void updateLikeCount(final End end, final boolean b) {
//        int likeCount = 0;
//        boolean equalsIgnoreCase = false;
//        if (end != null) {
//            likeCount = end.likeCount;
//            equalsIgnoreCase = "y".equalsIgnoreCase(end.likeYn);
//        }
//        this.likeButtonScale.clearAnimation();
//        this.likeButtonOff.clearAnimation();
//        this.likeButtonOnLeft.clearAnimation();
//        this.likeButtonOnRight.clearAnimation();
//        if (b) {
//            if (equalsIgnoreCase) {
//                this.likeButtonOff.setVisibility(0);
//                this.likeButtonOnLeft.setVisibility(8);
//                this.likeButtonOnRight.setVisibility(8);
//                this.likeButtonScale.startAnimation(AnimationUtils.loadAnimation((Context)this.mainActivity, 2130968598));
//                final Animation loadAnimation = AnimationUtils.loadAnimation((Context)this.mainActivity, 2130968596);
//                loadAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
//                    public void onAnimationEnd(final Animation animation) {
//                        EndViewPagerFragment.this.likeButtonOnRight.setVisibility(0);
//                        EndViewPagerFragment.this.likeButtonOnRight.startAnimation(AnimationUtils.loadAnimation((Context)EndViewPagerFragment.this.mainActivity, 2130968597));
//                        EndViewPagerFragment.this.likeButtonOff.setVisibility(8);
//                        EndViewPagerFragment.this.likeButtonOff.startAnimation(AnimationUtils.loadAnimation((Context)EndViewPagerFragment.this.mainActivity, 2130968595));
//                    }
//
//                    public void onAnimationRepeat(final Animation animation) {
//                    }
//
//                    public void onAnimationStart(final Animation animation) {
//                    }
//                });
//                this.likeButtonOnLeft.setVisibility(0);
//                this.likeButtonOnLeft.startAnimation(loadAnimation);
//            }
//            else {
//                this.likeButtonOff.setVisibility(8);
//                this.likeButtonOnLeft.setVisibility(0);
//                this.likeButtonOnRight.setVisibility(0);
//                final Animation loadAnimation2 = AnimationUtils.loadAnimation((Context)this.mainActivity, 2130968595);
//                this.likeButtonOnLeft.startAnimation(loadAnimation2);
//                this.likeButtonOnRight.startAnimation(loadAnimation2);
//                this.likeButtonOff.setVisibility(0);
//                this.likeButtonOff.startAnimation(AnimationUtils.loadAnimation((Context)this.mainActivity, 2130968594));
//            }
//        }
//        else if (equalsIgnoreCase) {
//            this.likeButtonOff.setVisibility(8);
//            this.likeButtonOnLeft.setVisibility(0);
//            this.likeButtonOnRight.setVisibility(0);
//        }
//        else {
//            this.likeButtonOff.setVisibility(0);
//            this.likeButtonOnLeft.setVisibility(8);
//            this.likeButtonOnRight.setVisibility(8);
//        }
//        this.mBtnLike.setSelected(equalsIgnoreCase);
//        final TextView textView = (TextView)this.mEndUtilityBar.findViewById(2131362180);
//        if (likeCount == 0) {
//            textView.setVisibility(8);
//            return;
//        }
//        textView.setVisibility(0);
//        textView.setText((CharSequence)String.valueOf(end.likeCount));
//    }
//
//    public void updateMentionList() {
//        if (!this.mMentionMode || this.mMentionListView == null || this.mMentionListView.getVisibility() != 0) {
//            return;
//        }
//        final String currentParagraph = this.getCurrentParagraph();
//        if (currentParagraph.length() == 0) {
//            this.mMentionMode = false;
//            this.mMentionListView.setVisibility(8);
//            return;
//        }
//        if (currentParagraph.length() == 1 && currentParagraph.charAt(0) == '@' && this.mFollowingList != null) {
//            this.mAutocompletionFollowList.clear();
//            this.mAutocompletionFollowList.addAll(this.mFollowingList);
//            this.mMentionListAdapter.notifyDataSetChanged();
//            return;
//        }
//        final String substring = currentParagraph.substring(1);
//        this.mAutocompletionFollowList.clear();
//        for (int i = 0; i < this.mFollowingList.size(); ++i) {
//            final MentionModel mentionModel = this.mFollowingList.get(i);
//            if (mentionModel.nickname.toLowerCase().contains(substring.toLowerCase())) {
//                this.mAutocompletionFollowList.add(mentionModel);
//            }
//        }
//        this.mMentionListAdapter.notifyDataSetChanged();
//    }
//
//    public void updateRepicCount(final End end) {
//        int repicCount = 0;
//        if (end != null) {
//            repicCount = end.repicCount;
//        }
//        final TextView textView = (TextView)this.mBottomLayout.findViewById(2131362185);
//        if (repicCount == 0) {
//            textView.setVisibility(8);
//            return;
//        }
//        textView.setVisibility(0);
//        textView.setText((CharSequence)String.valueOf(end.repicCount));
//        this.mRootView.requestLayout();
//    }
//}