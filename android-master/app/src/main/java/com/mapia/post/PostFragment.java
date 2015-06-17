package com.mapia.post;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.Session;
import com.facebook.SessionState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.mapia.MainApplication;
import com.mapia.R;
import com.mapia.api.QueryManager;
import com.mapia.common.BaseFragment;
import com.mapia.common.CommonConstants;
import com.mapia.custom.CustomProgressDialog;
import com.mapia.custom.FontSettableTextView;
import com.mapia.endpage.MentionListviewAdapter;
import com.mapia.login.LoginInfo;
import com.mapia.mention.MentionModel;
import com.mapia.myfeed.MyfeedActivity;
import com.mapia.network.RestRequestHelper;
import com.mapia.search.tag.SearchTagModel;
import com.mapia.setting.MapiaOneBtnDialog;
import com.mapia.util.BitmapUtils;
import com.mapia.util.DeviceUtils;
import com.mapia.util.FontUtils;
import com.mapia.util.TextUtils;
import com.mapia.videoplayer.MapiaVideoPlayer;
import com.volley.MapiaMultipartRequest;
import com.volley.MapiaVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;


public class PostFragment extends BaseFragment implements View.OnClickListener //implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    public static final String CLIP_FILTER_TYPE = "clip_filter_type";
    public static final String MEDIA_LATITUDE = "media_latitude";
    public static final String MEDIA_LONGTITUDE = "media_longitude";
    public static final String MEDIA_TYPE_CLIP = "CLIP";
    public static final String MEDIA_TYPE_PIC = "PIC";
    public static final float THUMBNAIL_HINT_HEIGHT_DP = 33.0f;
    public static final String WATERMARK_PATH = "watermark_path";
    private final int COLOR_TEXT_TAG;
    private final int INPUTHELPER_AUTOCOMPLETE;
    private final int INPUTHELPER_MENTION;
    private final int INPUTHELPER_NOHISTORY;
    private final int INPUTHELPER_NONE;
    private final int INPUTHELPER_TAGHISTORY;
    private final String NELO_ERR_NETWORK;
    private final String NELO_ERR_PARSING;
    private final String NELO_POSTING;
    private final String NELO_POSTING_ERR;
    private final String NELO_POSTING_MODE_POSTING;
    private Session.StatusCallback facebookCallback;
    private boolean isApplyWatermark;
    private Toast mAlertToast;
    private PostTagAutoCompletionAdapter mAutoCompleteAdapter;
    private ArrayList<SearchTagModel> mAutoCompleteArrayList;
    private ArrayList<MentionModel> mAutocompletionFollowList;
    private Bitmap mBitmapThumb;
    private boolean mBlockInputHelper;
    private HighlightEditText mEdtPost;
    private View mBottomBar;
    private View.OnClickListener mBtnUpload;
    private View mBtnPostAuth;
    private View mBtnPostTag;
    private String mCurrentQuery;
    private ImageView mFacebookIcon;
    private ArrayList<MentionModel> mFollowingList;
    private GoogleApiClient mGoogleApiClient;
    private int mInputMode;
    private boolean mIsLoginTwitter;
    private Location mLastLocation;
    private ImageView mLineIcon;
    private FontSettableTextView mLocation;
    private View mLocationLayout;
    private long mMemberNo;
    private MentionListviewAdapter mMentionListAdapter;
    private boolean mMentionMode;
    private View mNavigationBar;
    private View mNoTagHistoryView;
    private String mParagraph;
    private long mParentMemberNo;
    private long mParentPicNo;
    private long mPicNo;
    private PostingInfo mPostingInfo;
    private LinearLayout mProgressBar;
    private View.OnClickListener mRecommendedTagClickListener;
    private LinearLayout mRecommendedTagLayout;
    private String mReplacedBody;
    private com.android.volley.Request mRequest;
    private InterceptTouchViewGroup mRootInterceptTouchView;
    private View mRootView;
    private AdapterView.OnItemClickListener mTagClickListener;
    private TagHistory mTagHistory;
    private TagHistoryAdapter mTagHistoryAdapter;
    private View mTagInputHelper;
    private ListView mTagInputHelperListView;
    private View mThumbnail;
    private ImageView mTwitterIcon;
    private CustomProgressDialog mUploading;
    private View mUtilityBar;
    private View.OnClickListener mUtilityBarItemClickListener;
    private View.OnClickListener mPostPhotoClickListener;
    private View.OnClickListener mPostGalleryClickListener;

    private View.OnClickListener mLocationClickListener;

    private View mBtnLocation;
    private View mBtnUtilityTag;
    private File mWatermarkFile;
    private String mWatermarkPath;
    private ImageButton btnPostPhoto, btnPostVideo, btnPostGallery;
    private TextView textPostLocation;
    private LatLng latLng;
    final int REQ_CODE_SELECT_IMAGE=100;

    public PostFragment() {
        super();
        this.NELO_POSTING = "nelo_posting";
        this.NELO_POSTING_ERR = "nelo_posting_err";
        this.NELO_POSTING_MODE_POSTING = "nelo_posting";
        this.NELO_ERR_NETWORK = "nelo_err_network";
        this.NELO_ERR_PARSING = "nelo_err_parsing";
        this.INPUTHELPER_NONE = 0;
        this.INPUTHELPER_NOHISTORY = 1;
        this.INPUTHELPER_TAGHISTORY = 2;
        this.INPUTHELPER_AUTOCOMPLETE = 3;
        this.INPUTHELPER_MENTION = 4;
        this.COLOR_TEXT_TAG = Color.parseColor("#474cfa");

        this.mBtnUpload = (View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {

                RestRequestHelper requestHelper = RestRequestHelper.newInstance();
                final LatLng postLatlng = latLng;
                final String postComment = mEdtPost.getText().toString();
                final ArrayList<String> fileList;
                String mapType = "public";

                    requestHelper.posts(
                            mapType, postComment, postLatlng, new Callback<JsonObject>() {
                                @Override
                                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                                    Toast.makeText(getActivity(), "Post 등록 성공".toString(), Toast.LENGTH_LONG).show();
//                                    ((MainApplication) getActivity().getApplication()).getMapPublicFragment();

                                    getActivity().finish();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.i("Post 등록 실패", error.getMessage().toString());
                                    Toast.makeText(getActivity(), "Post 등록 실패".toString(), Toast.LENGTH_LONG).show();

                                }
                            });


//                AceUtils.nClick(NClicks.WRITING_OK);
                // 임시로 이전 리퀘스트 패턴 사용
//                ArrayList<String> mentionList = TextUtils.makeMentionList(PostFragment.this.mEdtPost.getText().toString());
//                if (mentionList.size() > 0) {
//                    PostFragment.this.requestMentionMap(PostFragment.this.mEdtPost.getText().toString(), mentionList);
//                    return;
//                }
//                PostFragment.this.post();
            }
        };
        this.mPostPhotoClickListener = (View.OnClickListener)new View.OnClickListener(){
            public void onClick(View view) {
//                if (mainActivity.getCurrFragment() instanceof TagGalleryFragment) {
//                    mPostingInfo.mode = 3;
//                    mPostingInfo.tag = ((TagGalleryFragment)MainActivity.this.getCurrFragment()).getTagName();
//                }
//                else {
                    mPostingInfo.mode = 0;
//                }
                mainActivity.startCameraActivity();
            }
        };

        this.mPostGalleryClickListener = (View.OnClickListener)new View.OnClickListener(){
            public void onClick(View view) {
                Intent intentImage = new Intent(Intent.ACTION_GET_CONTENT);
                intentImage.setType("image/*");
                startActivityForResult(intentImage, REQ_CODE_SELECT_IMAGE); //REQ_CODE_SELECT_IMAGE);
            }
        };


        this.mLocationClickListener = (View.OnClickListener)new View.OnClickListener(){
            public void onClick(View view) {

                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Context context = getActivity().getApplicationContext();
                    startActivityForResult(builder.build(context), CommonConstants.PLACE_PICKER_REQUEST);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        this.mUtilityBarItemClickListener = (View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                switch (view.getId()) {
                    default: {}
                    case 2131362490: {
//                        AceUtils.nClick(NClicks.WRITING_TAG);
                        if (PostFragment.this.mRootInterceptTouchView.isExpanded()) {
                            PostFragment.this.mRootInterceptTouchView.setExpand(false);
                        }
//                        PostFragment.this.makeTagInputKeyboardEvent();
                    }
                    case 2131362492: {
//                        AceUtils.nClick(NClicks.WRITING_LOCATION);
//                        PostFragment.this.openSearchLocationFragment();
                    }
                    case 2131362494: {
                        PostFragment.this.checkTwitterToken();
//                        AceUtils.nClick(NClicks.WRITING_TWITTER);
                    }
                    case 2131362496: {
                        PostFragment.this.checkFacebookToken();
//                        AceUtils.nClick(NClicks.WRITING_FACEBOOK);
                    }
                    case 2131362498: {
                        PostFragment.this.checkLineToken();
//                        AceUtils.nClick(NClicks.WRITING_LINE);
                    }
                }
            }
        };
        this.mRecommendedTagClickListener = (View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.WRITING_RECOMMEND);
                final String string = ((Button)view).getText().toString();
                PostFragment.this.mBlockInputHelper = true;
                PostFragment.this.mEdtPost.addText(string + " ");
                PostFragment.this.mEdtPost.setOnTagging(false);
            }
        };
        this.mTagClickListener = (AdapterView.OnItemClickListener)new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView adapterView, final View view, int selectionStart, final long n) {
                String s = null;
                Label_0053: {
                    switch (PostFragment.this.mInputMode) {
                        case 2: {
                            if (selectionStart == 0) {
                                break;
                            }
                            s = PostFragment.this.mTagHistoryAdapter.getItem(selectionStart).substring(1);
                            if (s != null) {
                                break Label_0053;
                            }
                            break;
                        }
                        case 3: {
                            s = ((SearchTagModel)PostFragment.this.mAutoCompleteAdapter.getItem(selectionStart)).getTag();
                            break Label_0053;
                        }
                    }
                    return;
                }
                PostFragment.this.mEdtPost.getText().replace(PostFragment.this.mEdtPost.getLeftTag() + 1, PostFragment.this.mEdtPost.getSelectionStart(), (CharSequence)s);
                PostFragment.this.mEdtPost.getText().insert(PostFragment.this.mEdtPost.getSelectionStart(), (CharSequence)" ");
                selectionStart = PostFragment.this.mEdtPost.getSelectionStart();
                PostFragment.this.mEdtPost.setText((CharSequence) PostFragment.this.mEdtPost.getText());
                PostFragment.this.mEdtPost.setSelection(selectionStart);
                PostFragment.this.hideInputHelper();
//                PostFragment.this.showSoftKeyboard();
            }
        };
        this.defaultMenuBarShow = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.mRootView = layoutInflater.inflate(R.layout.fragment_post, viewGroup, false);
        this.initialize();
        return this.mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.showSoftKeyboard();

    }

    private void showSoftKeyboard() {
        if (this.mEdtPost != null) {
            this.inputMethodManager.showSoftInput((View)this.mEdtPost, 1);
        }
    }


    public void onCompleteVideoEncoding() {
        if (this.mThumbnail == null) {
            return;
        }
        this.mainActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final MapiaVideoPlayer mapiaVideoPlayer = (MapiaVideoPlayer)PostFragment.this.mThumbnail;
                mapiaVideoPlayer.setFrom(12);
                mapiaVideoPlayer.hideLoading();
                mapiaVideoPlayer.setVideoPath(PostFragment.this.mPostingInfo.file.getAbsolutePath());
            }
        });
    }


    private void initThumbnail() {
        switch (this.mPostingInfo.mode) {
            default: {}
            case 0:
            case 2:
            case 3: {
                if (this.mPostingInfo.mediaType.equals("PIC")) {
                    this.mBitmapThumb = BitmapUtils.decodeSampledBitmapFromFilePath(this.mPostingInfo.file.getAbsolutePath(), DeviceUtils.getDeviceHeight(), DeviceUtils.getDeviceHeight());
                }
                else {
                    this.mBitmapThumb = this.getBitmapFromVideo();
                }
                if (this.mBitmapThumb == null) {
                    this.mainActivity.showCustomToast("Failed to load Thumbnail", 0);
                    this.mainActivity.finish();
                }
                this.initThumbnail(this.mBitmapThumb);
            }
            case 1: {
                Glide.with(this.mainActivity).load(this.mPostingInfo.picImageUrl + BitmapUtils.calcEndBitmapSuffic(this.mPostingInfo.picImageWidth, this.mPostingInfo.picImageHeight)).asBitmap().listener(requestListener);
            }
        }
    }

    private RequestListener<String, Bitmap> requestListener = new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(final Exception ex, final String s, final Target<Bitmap> target, final boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(final Bitmap bitmap, final String s, final Target<Bitmap> target, final boolean b, final boolean b2) {
                if (bitmap != null) {
                    PostFragment.this.initThumbnail(bitmap);
                }
                return false;
            }
    };

    private void initThumbnail(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        final int deviceWidth = DeviceUtils.getDeviceWidth();
        int n = 0;
        if ("PIC".equals(this.mPostingInfo.mediaType)) {
            n = (int)Math.ceil(deviceWidth * bitmap.getHeight() / bitmap.getWidth());
        }
        else if ("CLIP".equals(this.mPostingInfo.mediaType)) {
            n = (int)Math.ceil(deviceWidth * this.mPostingInfo.vidHeight / this.mPostingInfo.vidWidth);
        }
        final int convertDipToPixelInt = BitmapUtils.convertDipToPixelInt(33.0f);
        final int dimensionPixelSize = this.mainActivity.getResources().getDimensionPixelSize(R.dimen.common_title_height);
        this.mThumbnail = (View)new ImageView((Context)this.mainActivity);
        if (this.mPostingInfo.mediaType.equalsIgnoreCase("PIC") || this.mPostingInfo.mode == 1) {
            ((ImageView)this.mThumbnail).setImageBitmap(bitmap);
        }
        else {
            final MapiaVideoPlayer mThumbnail = new MapiaVideoPlayer((Context)this.mainActivity);
            mThumbnail.setFrom(12);
            mThumbnail.updateTextureViewSize(deviceWidth, n);
            mThumbnail.setThumbnail(bitmap);
            mThumbnail.setSound(true);
            if (MainApplication.getInstance().getUploadingStatus() != 5) {
                mThumbnail.showLoading();
            }
            else {
                mThumbnail.setVideoPath(this.mPostingInfo.file.getAbsolutePath());
            }
            this.mThumbnail = (View)mThumbnail;
        }
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(deviceWidth, n);
        layoutParams.setMargins(0, -(n - (convertDipToPixelInt + dimensionPixelSize)), 0, 0);
        this.mThumbnail.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.mThumbnail.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                PostFragment.this.mRootView.onTouchEvent(motionEvent);
                switch (action) {
                    default: {
                        return true;
                    }
                    case 0: {
                        PostFragment.this.hideSoftKeyboard();
                        return true;
                    }
                }
            }
        });
        ((RelativeLayout)this.mRootView).addView(this.mThumbnail);
        this.mNavigationBar.bringToFront();
        this.mRootInterceptTouchView.setThumbnailView(this.mThumbnail);
        this.mRootInterceptTouchView.setEditPostView((View)this.mEdtPost, this.inputMethodManager);
        this.mRootInterceptTouchView.setNavigationBar(this.mNavigationBar);
    }


    private void initialize() {
        this.mRootInterceptTouchView = (InterceptTouchViewGroup)this.mRootView;
        this.mInputMode = 0;
        this.latLng = ((PostActivity) getActivity()).getLatLng();
        this.mPostingInfo = new PostingInfo();
        mPostingInfo.locationData = new LocationData();

        this.initView();
        this.initNavigationBar();
        this.initLocationLayout();
//        this.initTagAutocompletion();
//        this.initTagHistoryListView();
//        this.initMentionListView();
        this.getParameter();
//        this.initRecommendedTag();
//        this.initThumbnail();
        this.initSns();
//        if (this.mPostingInfo.locationData != null) {
//            this.showLocationLayout(this.mPostingInfo.locationData);
//        }
//        this.checkTwitter();
//        this.setEditPost();
        this.setEventListener();
        this.applyFont();
//        this.reorderingView();

        new Handler().postDelayed((Runnable)new SoftInputShowHandler(), 200L);
    }

    private void setEventListener() {
//        this.mBtnUtilityTag.setOnClickListener(this.mUtilityBarItemClickListener);
        this.mBtnLocation.setOnClickListener(this.mLocationClickListener);
        this.mRootInterceptTouchView.setIntercept(true);
        this.mLineIcon = (ImageView)this.mUtilityBar.findViewById(R.id.imgShareLine);
        this.mTwitterIcon = (ImageView)this.mUtilityBar.findViewById(R.id.imgShareTwitter);
        this.mFacebookIcon = (ImageView)this.mUtilityBar.findViewById(R.id.imgShareFacebook);
        this.mUtilityBar.findViewById(R.id.btnShareLine).setOnClickListener(this.mUtilityBarItemClickListener);
        this.mUtilityBar.findViewById(R.id.btnShareTwitter).setOnClickListener(this.mUtilityBarItemClickListener);
        this.mUtilityBar.findViewById(R.id.btnShareFacebook).setOnClickListener(this.mUtilityBarItemClickListener);
        this.btnPostPhoto.setOnClickListener(this.mPostPhotoClickListener);
    }


    private void initView() {
        this.textPostLocation = (TextView)this.mRootView.findViewById(R.id.text_post_location);
        this.mBtnLocation = (LinearLayout)this.mRootView.findViewById(R.id.btn_post_location);

        this.mEdtPost = (HighlightEditText)this.mRootView.findViewById(R.id.edit_post);
        this.mBottomBar = this.mRootView.findViewById(R.id.bottomBar);
        this.mUtilityBar = this.mRootView.findViewById(R.id.utility_bar);
        this.mNavigationBar = this.mRootView.findViewById(R.id.topNavigationBar);
//        this.mBtnUtilityTag = this.mRootView.findViewById(R.id.btnUtilityTag);
        this.mNoTagHistoryView = this.mRootView.findViewById(R.id.noContents);
        this.mTagInputHelperListView = (ListView)this.mRootView.findViewById(R.id.listTagInputHelper);
        this.mRecommendedTagLayout = (LinearLayout)this.mRootView.findViewById(R.id.recommendedTagHolder);
        this.mLocationLayout = this.mBottomBar.findViewById(R.id.gpsLayer);
        this.mLocation = (FontSettableTextView) this.mLocationLayout.findViewById(R.id.location);
        this.mTagInputHelper = this.mRootView.findViewById(R.id.tagInputLayout);

        this.btnPostPhoto = (ImageButton) this.mRootView.findViewById(R.id.btnPostPhoto);
        this.mBtnPostTag = this.mRootView.findViewById(R.id.btnPostTag);
        this.mBtnPostAuth = this.mRootView.findViewById(R.id.btnPostAuth);
        this.textPostLocation.setText(latLng.toString());
    }

    private void initNavigationBar() {
        this.mNavigationBar.findViewById(R.id.btnPostCancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.WRITING_PRE);
                PostFragment.this.onBackPressed();
            }
        });
        this.mNavigationBar.findViewById(R.id.btnPostSubmit).setOnClickListener(this.mBtnUpload);
        this.mNavigationBar.bringToFront();
    }

    private void initSns() {

        mLineIcon = (ImageView) this.mUtilityBar.findViewById(R.id.imgShareLine);
        mTwitterIcon = (ImageView)this.mUtilityBar.findViewById(R.id.imgShareTwitter);
        mFacebookIcon = (ImageView)this.mUtilityBar.findViewById(R.id.imgShareFacebook);

        if (this.mPostingInfo.snsParam == null) {
            return;
        }
        mLineIcon.setSelected(this.mPostingInfo.snsParam.contains("LINE"));
        mTwitterIcon.setSelected(this.mPostingInfo.snsParam.contains("TWITTER"));
        mFacebookIcon.setSelected(this.mPostingInfo.snsParam.contains("FACEBOOK"));

    }



    public void showSplash(final boolean b) {
        if (b) {
            if (this.mUploading == null || !this.mUploading.isShowing()) {
                (this.mUploading = CustomProgressDialog.show((Context)this.mainActivity, null, null, true, false)).setCancelable(false);
                this.mUploading.setContentView(R.layout.mapia_progress_dialog);
            }
        }
        else {
            this.hideSoftKeyboard();
            if (this.mUploading != null && this.mUploading.isShowing()) {
                this.mUploading.dismiss();
            }
        }
    }

    public boolean isSplashOn() {
        return this.mUploading != null && this.mUploading.isShowing();
    }
    public boolean onBackPressed() {
        if (this.mPostingInfo == null) {
            this.mainActivity.finish();
            return true;
        }
        if (this.isSplashOn()) {
            this.showSplash(false);
            this.mainActivity.removeFragment(false);
            return true;
        }
        if (!"PIC".equals(this.mPostingInfo.mediaType) && "CLIP".equals(this.mPostingInfo.mediaType) && MainApplication.getInstance().getUploadingStatus() == 3) {
//            VideoSDK.stop();
//            MainApplication.getInstance().propagateBGUploadingStatus(-1);
        }
        this.mPostingInfo.body = this.mEdtPost.getText().toString();
        this.mPostingInfo.snsParam = this.getSnsParam();
        this.hideSoftKeyboard();
        if (this.mPostingInfo.mode == 1) {
            MainApplication.getInstance().clearBGUpload();
            this.mainActivity.removeFragment(true);
            return true;
        }
        this.mainActivity.finish();
        return true;
    }

    private int addRecommendedTag(final String s) {
        final int childCount = this.mRecommendedTagLayout.getChildCount();
        final Button button = new Button((Context)this.mainActivity);
        button.setText((CharSequence)("#" + s));
        button.setTextColor(-16777216);
        button.setBackgroundResource(R.drawable.selector_recommended_tag_bg);
        button.setTextSize(2, 13.0f);
        button.setPadding(BitmapUtils.convertDipToPixelInt(7.5f), 0, BitmapUtils.convertDipToPixelInt(7.5f), 0);
        button.setTypeface(FontUtils.getNanumRegular());
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, BitmapUtils.convertDipToPixelInt(25.0f));
        layoutParams.setMargins(0, 0, BitmapUtils.convertDipToPixelInt(5.5f), 0);
        button.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        button.setOnClickListener(this.mRecommendedTagClickListener);
        this.mRecommendedTagLayout.addView((View)button);
        return childCount;
    }

    private void applyFont() {
        this.mEdtPost.setTypeface(FontUtils.getNanumRegular());
    }

    private void changeToTagAutocompletion() {
        this.mNoTagHistoryView.setVisibility(View.INVISIBLE);
        this.mTagInputHelperListView.setVisibility(View.VISIBLE);
        this.mTagInputHelperListView.setAdapter((ListAdapter) this.mAutoCompleteAdapter);
        this.mTagInputHelperListView.setOnItemClickListener(this.mTagClickListener);
        this.mInputMode = 3;
    }

    private void changeToTagHistory() {
        if (this.mTagHistory.getTagCount() == 0) {
            this.mTagInputHelperListView.setVisibility(View.INVISIBLE);
            this.mNoTagHistoryView.setVisibility(View.VISIBLE);
            this.mInputMode = 1;
            return;
        }
        this.mTagInputHelperListView.setVisibility(View.VISIBLE);
        this.mNoTagHistoryView.setVisibility(View.INVISIBLE);
        this.mTagInputHelperListView.setAdapter((ListAdapter)this.mTagHistoryAdapter);
        this.mTagInputHelperListView.setOnItemClickListener(this.mTagClickListener);
        this.mInputMode = 2;
    }

    private void checkFacebookToken() {
        if (this.mFacebookIcon.isSelected()) {
            this.mFacebookIcon.setSelected(false);
            return;
        }
        final Session activeSession = Session.getActiveSession();
        if (activeSession != null && activeSession.isOpened()) {
            this.getFacebookTokenWithPermission();
            return;
        }
        Session.openActiveSession(this.mainActivity, true, (Session.StatusCallback) new Session.StatusCallback() {
            @Override
            public void call(final Session activeSession, final SessionState sessionState, final Exception ex) {
                if (sessionState.isOpened() && sessionState == SessionState.OPENED) {
                    Session.setActiveSession(activeSession);
                    PostFragment.this.getFacebookTokenWithPermission();
                }
            }
        });
    }

    private void checkLineToken() {
        if (this.mLineIcon.isSelected()) {
            this.mLineIcon.setSelected(false);
            return;
        }
//        LoginManager.lineLogin(this.mainActivity, new LineLoginFutureListener() {
//            @Override
//            public void loginComplete(final LineLoginFuture lineLoginFuture) {
//                switch (lineLoginFuture.getProgress()) {
//                    default: {}
//                    case SUCCESS: {
//                        PostFragment.this.requestSnsMapping("line", lineLoginFuture.getAccessToken().accessToken, "1408792549", LoginInfo.getInstance().getToken());
//                    }
//                    case CANCELED:
//                    case FAILED: {
//                        PostFragment.this.mLineIcon.setSelected(false);
//                    }
//                }
//            }
//        });
    }

    private void checkTwitter() {
//        final MapiaRequest MapiaRequest = new MapiaRequest(0, QueryManager.makeSnsMappingInfoUrl(LoginInfo.getInstance().getToken()), (JSONObject)null, new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                if (jsonObject == null) {
//                    return;
//                }
//                while (true) {
//                    while (true) {
//                        int n;
//                        try {
//                            final JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONObject("rtn_data").getJSONArray("sns_list");
//                            n = 0;
//                            if (n >= jsonArray.length()) {
//                                PostFragment.this.mIsLoginTwitter = false;
//                                return;
//                            }
//                            if (jsonArray.getJSONObject(n).getString("sns_cd").equals("twitter")) {
//                                PostFragment.this.mIsLoginTwitter = true;
//                                return;
//                            }
//                        }
//                        catch (JSONException ex) {
//                            return;
//                        }
//                        ++n;
//                        continue;
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(final VolleyError volleyError) {
//            }
//        });
//        MapiaRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
//        MapiaVolley.getRequestQueue().add((Request<Object>)MapiaRequest);
    }

    private void checkTwitterToken() {
        if (this.mTwitterIcon.isSelected()) {
            this.mTwitterIcon.setSelected(false);
            return;
        }
        if (this.mIsLoginTwitter) {
            this.mTwitterIcon.setSelected(true);
            return;
        }
//        this.startActivityForResult(new Intent((Context)this.mainActivity, (Class)TwitterLoginActivity.class), 100);
    }

    private void extractTag(final String s, final ArrayList<String> list) {
        if (s != null && list != null) {
            final Matcher matcher = Pattern.compile("#[a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]{1,20}").matcher(s);
            while (matcher.find()) {
                list.add(s.subSequence(matcher.start(), matcher.end()).toString().replace("#", ""));
            }
        }
    }

    private void fillCenter() {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mEdtPost.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.addRule(2, 2131362481);
        this.mEdtPost.setLayoutParams((ViewGroup.LayoutParams) layoutParams);
    }

    private Bitmap getBitmapFromVideo() {
//        if (MainApplication.getInstance().getUploadingStatus() == 5) {
//            return ThumbnailUtils.createVideoThumbnail(this.mPostingInfo.file.getAbsolutePath(), 1);
//        }
//        final Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(this.mPostingInfo.originalMediaPath, 1);
//        if (this.mPostingInfo.filterType != null && videoThumbnail != null) {
//            return VideoSDK.applyFilterToBitmap(this.mPostingInfo.filterType, videoThumbnail);
//        }
        return ThumbnailUtils.createVideoThumbnail(this.mPostingInfo.file.getAbsolutePath(), 1);
    }

    private String getCurrentParagraph() {
        final int selectionEnd = this.mEdtPost.getSelectionEnd();
        final Editable text = this.mEdtPost.getText();
        if (selectionEnd > 0 && selectionEnd <= text.length() && text.charAt(selectionEnd - 1) == '@') {
            return "@";
        }
        int i;
        for (i = selectionEnd; i > 0; --i) {
            final char char1 = text.charAt(i - 1);
            if (Character.isWhitespace(char1)) {
                return text.subSequence(i, selectionEnd).toString();
            }
            if (char1 == '@') {
                return text.subSequence(i - 1, selectionEnd).toString();
            }
        }
        return text.subSequence(i, selectionEnd).toString();
    }

    private void getFacebookTokenWithPermission() {
//        final Session activeSession = Session.getActiveSession();
//        if (!activeSession.getPermissions().contains("publish_actions")) {
//            final Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, new String[] { "publish_actions" });
//            if (this.facebookCallback == null) {
//                newPermissionsRequest.setCallback(this.facebookCallback = new Session.StatusCallback() {
//                    @Override
//                    public void call(final Session session, final SessionState sessionState, final Exception ex) {
//                        if (session.getPermissions().contains("publish_actions")) {
//                            PostFragment.this.requestSnsMapping("facebook", session.getAccessToken(), "1524945657777608", LoginInfo.getInstance().getToken());
//                        }
//                    }
//                });
//            }
//            activeSession.requestNewPublishPermissions(newPermissionsRequest);
//            return;
//        }
//        this.requestSnsMapping("facebook", activeSession.getAccessToken(), "1524945657777608", LoginInfo.getInstance().getToken());
    }

    private int getNicknameStart() {
        int selectionStart = this.mEdtPost.getSelectionStart();
        final Editable text = this.mEdtPost.getText();
        int n = 0;
        int n2;
        while (true) {
            n2 = selectionStart;
            if (selectionStart <= 0) {
                break;
            }
            n2 = selectionStart;
            if (n >= 15) {
                break;
            }
            final char char1 = text.charAt(selectionStart - 1);
            if (Character.isWhitespace(char1) || char1 == '@') {
                n2 = selectionStart - 1;
                break;
            }
            --selectionStart;
            ++n;
        }
        return n2;
    }

    private void getParameter() {
//        this.mPostingInfo = MainApplication.getInstance().getPostingInfo();
        final Bundle arguments = this.getArguments();
        if (arguments != null) {
            this.isApplyWatermark = arguments.getBoolean("isApplyWatermark");
            final String string = arguments.getString("intentTextBody");
//            if (string.length() > 0) {
//                this.mPostingInfo.body = string;
//            }
//            if ("PIC".equals(this.mPostingInfo.mediaType)) {
//                if (this.mPostingInfo.postingMediaPath != null) {
//                    this.mPostingInfo.file = new File(this.mPostingInfo.postingMediaPath);
//                }
//            }
//            else if ("CLIP".equals(this.mPostingInfo.mediaType) && this.mPostingInfo.originalMediaPath != null) {
//                this.mPostingInfo.file = new File(this.mPostingInfo.originalMediaPath);
//            }
            this.mWatermarkPath = arguments.getString("watermark_path");
            if (this.mWatermarkPath != null) {
                this.mWatermarkFile = new File(this.mWatermarkPath);
            }
        }
    }

    private String getRealPathFromURI(final Uri uri) {
        final Cursor loadInBackground = new CursorLoader((Context)this.mainActivity, uri, new String[] { "_data" }, null, null, null).loadInBackground();
        final int columnIndexOrThrow = loadInBackground.getColumnIndexOrThrow("_data");
        loadInBackground.moveToFirst();
        return loadInBackground.getString(columnIndexOrThrow);
    }

    private String getSnsParam() {
        String string = "";
        final StringBuilder sb = new StringBuilder();
        if (this.mLineIcon.isSelected()) {
            sb.append("&snsChannels=");
            sb.append("LINE");
        }
        if (this.mFacebookIcon.isSelected()) {
            sb.append("&snsChannels=");
            sb.append("FACEBOOK");
        }
        if (this.mTwitterIcon.isSelected()) {
            sb.append("&snsChannels=");
            sb.append("TWITTER");
        }
        if (sb.length() > 0) {
            string = "?" + sb.substring(1);
        }
        return string;
    }

    private SpannableString getTagStyleString(final CharSequence charSequence) {
        final SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan((Object)new ForegroundColorSpan(this.COLOR_TEXT_TAG), 0, spannableString.length(), 33);
        spannableString.setSpan((Object) new StyleSpan(1), 0, spannableString.length(), 33);
        return spannableString;
    }

    private SpannableString getTagStyleString(final String s) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        spannableString.setSpan((Object)new ForegroundColorSpan(this.COLOR_TEXT_TAG), 0, spannableString.length(), 33);
        spannableString.setSpan((Object)new StyleSpan(1), 0, spannableString.length(), 33);
        return spannableString;
    }

    private void goToMyfeed() {
        this.hideSoftKeyboard();
        if (this.mainApplication.getMyfeedActivity() == null) {
            final Intent intent = new Intent((Context)this.mainActivity, MyfeedActivity.class);
            intent.setFlags(537001984);
            this.startActivity(intent);
        }
        else {
            final Intent intent2 = new Intent((Context)this.mainActivity, MyfeedActivity.class);
            intent2.setFlags(537001984);
            intent2.putExtra("from", "postFragment");
            this.startActivity(intent2);
        }
        Log.d("jm.lee", "abc");
        this.mainActivity.setResult(2);
        this.mainActivity.finish();
    }

    private void goToRelatedContents(final ArrayList<String> list) {
        this.hideSoftKeyboard();
        final Intent intent = new Intent();
        intent.putExtra("mediaType", this.mPostingInfo.mediaType);
        intent.putExtra("linkedTags", (Serializable)list);
        this.mainActivity.setResult(2, intent);
        this.mainActivity.finish();
    }

    private void handleErrorResponse(final JSONObject jsonObject) throws JSONException, IOException {
        switch (jsonObject.getInt("errorCode")) {
            default: {}
            case 7077: {
                final MapiaOneBtnDialog MapiaOneBtnDialog = new MapiaOneBtnDialog((Context)this.mainActivity, jsonObject.getString("errorMessage"), this.getString(2131558426));
                MapiaOneBtnDialog.show();
                MapiaOneBtnDialog.setBtnClick((View.OnClickListener)new View.OnClickListener() {
                    public void onClick(final View view) {
                        MapiaOneBtnDialog.dismiss();
                    }
                });
            }
            case 4100: {
                this.handlePenalty(jsonObject);
            }
        }
    }

    private boolean handlePenalty(final JSONObject jsonObject) throws IOException, JSONException {
        if (!jsonObject.isNull("penalty")) {
//            final Penalty penalty = this.mainActivity.getObjectMapper().readValue(jsonObject.getJSONObject("penalty").toString(), Penalty.class);
//            if ("WARNING_MEMBER".equalsIgnoreCase(penalty.penaltyCode) || "CS_BLOCK_CONTENT".equalsIgnoreCase(penalty.penaltyCode)) {
//                final DialogPenaltyPosting dialogPenaltyPosting = new DialogPenaltyPosting((Context)this.mainActivity);
//                dialogPenaltyPosting.setDialogListener((DialogPenaltyPosting.DialogListener)new DialogPenaltyPosting.DialogListener() {
//                    @Override
//                    public void selectOption(final int n) {
//                        try {
//                            dialogPenaltyPosting.dismiss();
//                            PostFragment.this.leavePost(jsonObject);
//                        }
//                        catch (JSONException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//                dialogPenaltyPosting.setOnCancelListener((DialogInterface.OnCancelListener)new DialogInterface.OnCancelListener() {
//                    public void onCancel(final DialogInterface dialogInterface) {
//                        try {
//                            dialogInterface.dismiss();
//                            PostFragment.this.leavePost(jsonObject);
//                        }
//                        catch (JSONException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//                this.requestReadPenaltyAlert(penalty.penaltyNo);
//                dialogPenaltyPosting.setCanceledOnTouchOutside(true);
//                dialogPenaltyPosting.show();
//                return true;
//            }
//            if ("FORCE_BLIND_MEMBER".equalsIgnoreCase(penalty.penaltyCode)) {
//                final DialogPenaltyAccount dialogPenaltyAccount = new DialogPenaltyAccount((Context)this.mainActivity);
//                dialogPenaltyAccount.setDialogListener((DialogPenaltyAccount.DialogListener)new DialogPenaltyAccount.DialogListener() {
//                    @Override
//                    public void selectOption(final int n) {
//                        PostFragment.this.mainActivity.removeFragment(true);
//                        dialogPenaltyAccount.dismiss();
//                    }
//                });
//                this.requestReadPenaltyAlert(penalty.penaltyNo);
//                dialogPenaltyAccount.setCanceledOnTouchOutside(true);
//                dialogPenaltyAccount.show();
//                return true;
//            }
        }
        return false;
    }

    private void hideInputHelper() {
        if (this.mTagInputHelper == null || this.mTagInputHelper.getVisibility() != View.VISIBLE) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mThumbnail, "translationY", new float[] { 0.0f });
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.mEdtPost, "translationY", new float[] { 0.0f });
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.mBottomBar, "translationY", new float[] { 0.0f });
        final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)this.mTagInputHelper, "translationY", new float[] { 0.0f, this.mTagInputHelper.getHeight() });
        final AnimatorSet set = new AnimatorSet();
        set.setDuration(300L);
        set.play((Animator)ofFloat).with((Animator)ofFloat2).with((Animator)ofFloat3).with((Animator)ofFloat4);
        set.addListener((Animator.AnimatorListener) new Animator.AnimatorListener() {
            public void onAnimationCancel(final Animator animator) {
            }

            public void onAnimationEnd(final Animator animator) {
                PostFragment.this.mRootInterceptTouchView.setIntercept(true);
                PostFragment.this.mInputMode = 0;
                PostFragment.this.mTagInputHelper.setVisibility(View.INVISIBLE);
                PostFragment.this.mEdtPost.bringToFront();
                PostFragment.this.mBottomBar.bringToFront();
            }

            public void onAnimationRepeat(final Animator animator) {
            }

            public void onAnimationStart(final Animator animator) {
                PostFragment.this.fillCenter();
            }
        });
        set.start();
    }

    private void initLocationLayout() {
        this.mLocationLayout.findViewById(R.id.removeLoc).setOnClickListener((View.OnClickListener) new View.OnClickListener() {
            public void onClick(final View view) {
                PostFragment.this.mPostingInfo.locationData = null;
                PostFragment.this.mLocation.setText((CharSequence) "");
                PostFragment.this.mLocationLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    private class SoftInputShowHandler implements Runnable
    {
        @Override
        public void run() {
            PostFragment.this.mEdtPost.requestFocus();
            PostFragment.this.inputMethodManager.showSoftInput((View)PostFragment.this.mEdtPost, 1);
        }
    }


    private void requestMentionMap(String nicknameToMemberno, final ArrayList<String> list) {
        nicknameToMemberno = QueryManager.makeNicknameToMemberno(list);
        this.mainActivity.makeRequest(nicknameToMemberno, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (!"success".equalsIgnoreCase(jsonObject.getString("resultStatus"))) {
                        return;
                    }
                    jsonObject = jsonObject.getJSONObject("result").getJSONObject("nicknames");
                    PostFragment.this.mReplacedBody = TextUtils.replaceNicknameToMemberno(PostFragment.this.mEdtPost.getText().toString(), jsonObject);
                    PostFragment.this.post();
                } catch (JSONException ex) {
                    PostFragment.this.post();
                    ex.printStackTrace();
                }
            }
        });
    }

    private void requestModify(final Map map) {
        final String modifyEpicUrl = QueryManager.makeModifyEpicUrl(this.mPostingInfo.locationData, this.getSnsParam());
        this.neloLog("nelo_posting", this.mPostingInfo.mediaType, "nelo_posting");
        final MapiaMultipartRequest mapiaMultipartRequest = new MapiaMultipartRequest(modifyEpicUrl, "picFile", null, map, new Response.Listener<String>() {
            public void onResponse(final String s) {
                PostFragment.this.showSplash(false);
                PostFragment.this.saveTag();
                PostFragment.this.parsePostResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                PostFragment.this.neloLog("nelo_posting_err", PostFragment.this.mPostingInfo.mediaType, "nelo_err_network");
                PostFragment.this.showRetryDialog();
            }
        });
        this.showSplash(true);
        mapiaMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(100000, 0, 1.0f));
        MapiaVolley.getRequestQueue().add(mapiaMultipartRequest);
    }

    private void requestPost(final Map map, final File file) {


        final String postEpicUrl = QueryManager.makePostEpicUrl(this.getSnsParam());
//        this.neloLog("nelo_posting", this.mPostingInfo.mediaType, "nelo_posting");
        this.mRequest = new MapiaMultipartRequest(postEpicUrl, "picFile", file, map, new Response.Listener<String>() {
            public void onResponse(final String s) {
                PostFragment.this.saveTag();
                PostFragment.this.showSplash(false);
                PostFragment.this.parsePostResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                PostFragment.this.showSplash(false);
                PostFragment.this.neloLog("nelo_posting_err", PostFragment.this.mPostingInfo.mediaType, "nelo_err_network");
                PostFragment.this.mainActivity.showError(volleyError);
                if (!PostFragment.this.mainActivity.isNoConnectionVisible()) {
                    PostFragment.this.showRetryDialog();
                }
            }
        });
        this.showSplash(true);
        this.mRequest.setRetryPolicy(new DefaultRetryPolicy(100000, 0, 1.0f));
        MapiaVolley.getRequestQueue().add(this.mRequest);
    }

    private void saveTag() {
        final String string = this.mEdtPost.getText().toString();
        final Matcher matcher = Pattern.compile("#[a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]{1,20}").matcher(string);
        while (matcher.find()) {
            this.mTagHistory.addTag(string.substring(matcher.start(), matcher.end()));
        }
        this.mTagHistory.writeTagHistory();
    }

    private void parsePostResponse(final String s) {
        try {
            this.parsePostResponse(new JSONObject(s));
        }
        catch (JSONException ex) {
            this.neloLog("nelo_posting_err", this.mPostingInfo.mediaType, "nelo_err_parsing");
            this.showRetryDialog();
            ex.printStackTrace();
        }
    }

    private void parsePostResponse(final JSONObject jsonObject) {
        try {
            final JSONObject jsonObject2 = jsonObject.getJSONObject("result");
            if (jsonObject.getString("resultStatus").equals("error")) {
                this.handleErrorResponse(jsonObject2);
                return;
            }
            if (!this.handlePenalty(jsonObject2)) {
//                this.leavePost(jsonObject2);
            }
        }
        catch (JSONException ex) {
            this.neloLog("nelo_posting_err", this.mPostingInfo.mediaType, "nelo_err_parsing");
            this.showRetryDialog();
        }
        catch (IOException ex2) {
            this.neloLog("nelo_posting_err", this.mPostingInfo.mediaType, "nelo_err_parsing");
            this.showRetryDialog();
        }
    }
//    private void leavePost(final JSONObject jsonObject) throws JSONException {
//        this.mPicNo = jsonObject.getLong("picNo");
//        this.mMemberNo = jsonObject.getLong("memberNo");
//        MainApplication.getInstance().clearBGUpload();
//        if (this.mPostingInfo.mode == 1) {
//            this.propagatePicModified();
//            this.mainActivity.removeFragment(true);
//            return;
//        }
//        if (this.mPostingInfo.filterApplied) {
//            if ("Y".equalsIgnoreCase(CameraUtils.getSaveMapiaPhotoYn())) {
//                MediaScannerConnection.scanFile(MainApplication.getContext(), new String[]{this.mPostingInfo.file.getAbsolutePath()}, (String[]) null, (MediaScannerConnection$OnScanCompletedListener) null);
//            }
//            else {
//                FileUtils.deleteFile(this.mPostingInfo.file);
//            }
//        }
//        else {
//            FileUtils.deleteFile(this.mPostingInfo.file);
//        }
//        if (this.mPostingInfo.mode == 2) {
//            this.mParentPicNo = jsonObject.getLong("parentPicNo");
//            this.mParentMemberNo = jsonObject.getLong("parentMemberNo");
//            this.propagateRepicAdded();
//            this.mainActivity.setResult(2);
//            this.mainActivity.finish();
//            return;
//        }
//        PostingManager.propagatePicAdded(this.mainApplication.getEventBus());
//        final ArrayList<String> relatedContents = this.makeRelatedContents(jsonObject);
//        if (relatedContents.size() == 0) {
//            this.goToMyfeed();
//            return;
//        }
//        this.goToRelatedContents(relatedContents);
//    }
//    private void propagatePicModified() {
//        final HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("event", "emp");
//        hashMap.put("picNo", "" + this.mPicNo);
//        hashMap.put("memberNo", "" + this.mMemberNo);
//        hashMap.put("body", this.mEdtPost.getText().toString());
//        if (this.mPostingInfo.locationData != null) {
//            hashMap.put("location", this.mPostingInfo.locationData);
//        }
//        this.mainApplication.getEventBus().post(hashMap);
//    }
//
//    private void propagateRepicAdded() {
//        final HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("event", "ecrc");
//        hashMap.put("picNo", "" + this.mParentPicNo);
//        hashMap.put("operation", "era");
//        this.mainApplication.getEventBus().post(hashMap);
//        PostingManager.propagatePicAdded(this.mainApplication.getEventBus());
//    }







    private void requestReadPenaltyAlert(final long n) {
        this.mainActivity.makeRequest(1, QueryManager.makeReadPenaltyAlert(n), new Response.Listener<JSONObject>() {
            public void onResponse(final JSONObject jsonObject) {
            }
        });
    }


    private void showRetryDialog() {
        this.saveTag();
        final MapiaConfirmDialog mapiaConfirmDialog = new MapiaConfirmDialog((Context)this.mainActivity);
        mapiaConfirmDialog.setMessage(this.getString(R.string.post_failed));
        mapiaConfirmDialog.setLeft(this.getString(R.string.cancel));
        mapiaConfirmDialog.setLeftBtnClick((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                mapiaConfirmDialog.dismiss();
            }
        });
        final SpannableString right = new SpannableString((CharSequence)this.getString(R.string.post_retry));
        right.setSpan((Object)new ForegroundColorSpan(this.mainActivity.getResources().getColor(R.color.point_color)), 0, right.length(), 33);
        mapiaConfirmDialog.setRight(right);
        mapiaConfirmDialog.setRightBtnClick((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                final ArrayList<String> mentionList = TextUtils.makeMentionList(PostFragment.this.mEdtPost.getText().toString());
                if (mentionList.size() > 0) {
                    PostFragment.this.requestMentionMap(PostFragment.this.mEdtPost.getText().toString(), mentionList);
                }
                switch (PostFragment.this.mPostingInfo.mode) {
                    case 0:
                    case 3: {
                        if ("PIC".equalsIgnoreCase(PostFragment.this.mPostingInfo.mediaType)) {
                            PostFragment.this.requestPost(PostFragment.this.makePostParam(), PostFragment.this.mPostingInfo.file);
                            break;
                        }
                        if ("CLIP".equalsIgnoreCase(PostFragment.this.mPostingInfo.mediaType)) {
                            PostFragment.this.uploadVideo();
                            break;
                        }
                        break;
                    }
                    case 2: {
                        if ("PIC".equalsIgnoreCase(PostFragment.this.mPostingInfo.mediaType)) {
                            PostFragment.this.requestRepic(PostFragment.this.makeRepicParam(), PostFragment.this.mPostingInfo.file);
                            break;
                        }
                        if ("CLIP".equalsIgnoreCase(PostFragment.this.mPostingInfo.mediaType)) {
                            PostFragment.this.uploadVideo();
                            break;
                        }
                        break;
                    }
                    case 1: {
                        PostFragment.this.requestModify(PostFragment.this.makeModifyParam());
                        break;
                    }
                }
                mapiaConfirmDialog.dismiss();
            }
        });
        mapiaConfirmDialog.show();
    }
    private void requestRepic(final Map map, final File file) {
        final String postEpicUrl = QueryManager.makePostEpicUrl(this.getSnsParam());
        this.neloLog("nelo_posting", this.mPostingInfo.mediaType, "nelo_posting");
        this.mRequest = new MapiaMultipartRequest(postEpicUrl, "picFile", file, map, new Response.Listener<String>() {
            public void onResponse(final String s) {
                PostFragment.this.showSplash(false);
                PostFragment.this.saveTag();
//                PostFragment.this.parseRepicResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                PostFragment.this.neloLog("nelo_posting_err", PostFragment.this.mPostingInfo.mediaType, "nelo_err_network");
                PostFragment.this.showSplash(false);
                PostFragment.this.showRetryDialog();
            }
        });
        this.showSplash(true);
        this.mRequest.setRetryPolicy(new DefaultRetryPolicy(100000, 0, 1.0f));
        MapiaVolley.getRequestQueue().add(this.mRequest);
    }


    private void post() {
//        this.mPostingInfo.snsParam = this.getSnsParam();
        final PostingInfo mPostingInfo = this.mPostingInfo;
        String body;
        if (this.mReplacedBody != null) {
            body = this.mReplacedBody;
        }
        else {
            body = this.mEdtPost.getText().toString();
        }
        mPostingInfo.body = body;
        mPostingInfo.locationData.latitude = latLng.latitude;
        mPostingInfo.locationData.longitude = latLng.longitude;
        switch (this.mPostingInfo.mode) {
            case 0:
                this.requestPost(this.makePostParam(), this.mPostingInfo.file);

                break;
            case 3: {
                if ("CLIP".equalsIgnoreCase(this.mPostingInfo.mediaType)) {
                    this.uploadVideo();
                    return;
                }
                break;
            }
            case 1: {
                this.requestModify(this.makeModifyParam());
                break;
            }
            case 2: {
                if ("PIC".equalsIgnoreCase(this.mPostingInfo.mediaType)) {
                    this.requestRepic(this.makeRepicParam(), this.mPostingInfo.file);
                    return;
                }
                if ("CLIP".equalsIgnoreCase(this.mPostingInfo.mediaType)) {
                    this.uploadVideo();
                    return;
                }
                break;
            }
        }
    }


    private void uploadVideo() {
        int n = 1;
        MainApplication.getInstance().setRequestUpload(true);
        MainApplication.getInstance().uploadVideo();
        final ArrayList<String> list = new ArrayList<String>();
        this.extractTag(this.mEdtPost.getText().toString(), list);
        if (list.size() <= 0) {
            n = 0;
        }
        if (n != 0) {
//            this.requestVideoRelatedContents(list);
            return;
        }
        this.goToMyfeed();
    }


    private Map makePostParam() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("fileType", this.mPostingInfo.mediaType);
        hashMap.put("copyrightYn", this.mPostingInfo.copyrightYn);
        if (this.mReplacedBody != null) {
            hashMap.put("body", this.mReplacedBody);
        }
        else {
            hashMap.put("body", this.mEdtPost.getText().toString());
        }
        if (this.mPostingInfo.locationData != null) {
            hashMap.put("latitude", String.valueOf(this.mPostingInfo.locationData.latitude));
            hashMap.put("longitude", String.valueOf(this.mPostingInfo.locationData.longitude));
//            hashMap.put("code", String.valueOf(this.mPostingInfo.locationData.placeId));
//            hashMap.put("name", this.mPostingInfo.locationData.main);
//            hashMap.put("address", this.mPostingInfo.locationData.address);
        }
        return hashMap;
    }

    private Map makeModifyParam() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("picNo", String.valueOf(this.mPostingInfo.picNo));
        if (this.mReplacedBody != null) {
            hashMap.put("body", this.mReplacedBody);
        }
        else {
            hashMap.put("body", this.mEdtPost.getText().toString());
        }
        if (this.mPostingInfo.locationData != null) {
            hashMap.put("latitude", String.valueOf(this.mPostingInfo.locationData.latitude));
            hashMap.put("longitude", String.valueOf(this.mPostingInfo.locationData.longitude));
            hashMap.put("code", String.valueOf(this.mPostingInfo.locationData.placeId));
            hashMap.put("name", this.mPostingInfo.locationData.main);
            hashMap.put("address", this.mPostingInfo.locationData.address);
        }
        return hashMap;
    }

    private Map makeRepicParam() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("fileType", this.mPostingInfo.mediaType);
        hashMap.put("copyrightYn", this.mPostingInfo.copyrightYn);
        hashMap.put("parentPicNo", String.valueOf(this.mPostingInfo.parentPicNo));
        hashMap.put("parentMemberNo", String.valueOf(this.mPostingInfo.parentMemberNo));
        if (this.mReplacedBody != null) {
            hashMap.put("body", this.mReplacedBody);
        }
        else {
            hashMap.put("body", this.mEdtPost.getText().toString());
        }
        if (this.mPostingInfo.locationData != null) {
            hashMap.put("latitude", String.valueOf(this.mPostingInfo.locationData.latitude));
            hashMap.put("longitude", String.valueOf(this.mPostingInfo.locationData.longitude));
            hashMap.put("code", String.valueOf(this.mPostingInfo.locationData.placeId));
            hashMap.put("name", this.mPostingInfo.locationData.main);
            hashMap.put("address", this.mPostingInfo.locationData.address);
        }
        return hashMap;
    }

    private String makeVideoThumbnailUrl(final String s) {
        return s.replace("16x9", "origin");
    }

    private void neloLog(final String s, String s2, final String s3) {
        if (this.mPostingInfo.file == null) {
            s2 = String.format("postingMode : %d, memberNo : %d, mediaType : %s, fileName : %s", this.mPostingInfo.mode, LoginInfo.getInstance().getMemberNo(), s2, "null");
        }
        else {
            s2 = String.format("postingMode : %d, memberNo : %d, mediaType : %s, fileName : %s", this.mPostingInfo.mode, LoginInfo.getInstance().getMemberNo(), s2, this.mPostingInfo.file.getName());
        }
//        NeloLog.debug(s, s2, s3);
    }

//
}