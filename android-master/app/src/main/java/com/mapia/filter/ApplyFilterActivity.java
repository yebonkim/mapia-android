package com.mapia.filter;

/**
 * Created by daehyun on 15. 6. 21..
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mapia.MainActivity;
import com.mapia.R;
import com.mapia.camera.CameraActivity;
import com.mapia.common.CommonConstants;
import com.mapia.common.data.PhotoData;
import com.mapia.post.PostActivity;
import com.mapia.ratio.PhotoRatioActivity;
import com.mapia.util.BitmapUtils;
import com.mapia.util.DeviceUtils;
import com.mapia.util.FontUtils;

import java.io.File;
import java.util.ArrayList;

public class ApplyFilterActivity extends MainActivity implements GestureDetector.OnGestureListener {
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    public final int MAIN_VIEW_MESSAGE;
    public final int START_POST_MESSAGE;
    public final int THUMBNAIL_MESSAGE;
    private Runnable applyFilterAndWatermarkSaveRunnable;
    private Runnable applyFilterToMainViewRunnable;
    private Runnable applyFilterToThumbnailRunnable;
    private File f;
    private int[] filterChildLocation;
    private int filterIndex;
    private ImageFilter filterLib;
    private int filterSize;
    private ArrayList<Bitmap> filteredBitmap;
    private ImageFilterItem[] filters;
    private int fromActivity;
    private Handler handler;
    private boolean isApplyFilter;
    private boolean isApplyWatermark;
    private Context mContext;
    private double mLatitude;
    private double mLongitude;
    private View.OnClickListener mWatermarkClickListener;
    private Handler mWatermarkHandler;
//    private WatermarkControlView mWatermarkLayout;
    private View.OnClickListener mWatermarkSelectListener;
//    private WatermarkView mWatermarkView;
    private ImageView mainImageView;
    private ImageView originalImageView;
    private PhotoFilterGestureView photoFilterGestureView;
    private HorizontalScrollView scrollView;
    private SeekBar seekBar;
    private View seekBarLayout;
    private LinearLayout thumbNailLinearLayout;
    private View thumbProgressView;
    private Bitmap thumbnailBitmap;
    private float thumbnailMargin;
    private float thumbnailSize;
    private View touchBlockView;
    private Bitmap viewBitmap;
    private File watermarkFile;

    public ApplyFilterActivity() {
        super();
        this.THUMBNAIL_MESSAGE = 0;
        this.START_POST_MESSAGE = 1;
        this.MAIN_VIEW_MESSAGE = 2;
        this.handler = new Handler((Handler.Callback) new Handler.Callback() {
            public boolean handleMessage(final Message message) {
                switch (message.what) {
                    case 0: {
                        for (int i = 0; i < ApplyFilterActivity.this.filterSize; ++i) {
                            final ImageView imageView = (ImageView) ApplyFilterActivity.this.thumbNailLinearLayout.getChildAt(i).findViewById(R.id.filter_select_image_view);
                            final Bitmap imageBitmap = ApplyFilterActivity.this.filteredBitmap.get(i);
                            if (imageBitmap != null) {
                                imageView.setImageBitmap(imageBitmap);
                            }
                        }
                        ApplyFilterActivity.this.filteredBitmap.clear();
                        break;
                    }
                    case 1: {
                        ApplyFilterActivity.this.startPostActivity(ApplyFilterActivity.this.f, ApplyFilterActivity.this.watermarkFile);
                        break;
                    }
                    case 2: {
                        final Bitmap imageBitmap2 = (Bitmap) message.obj;
                        if (imageBitmap2 == null) {
                            ApplyFilterActivity.this.mainImageView.setImageBitmap(ApplyFilterActivity.this.viewBitmap);
                            break;
                        }
                        ApplyFilterActivity.this.mainImageView.setImageBitmap(imageBitmap2);
                        break;
                    }
                }
                if (ApplyFilterActivity.this.thumbProgressView != null) {
                    ApplyFilterActivity.this.thumbProgressView.setVisibility(View.INVISIBLE);
                }
                ApplyFilterActivity.this.touchBlockView.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        this.applyFilterToThumbnailRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ApplyFilterActivity.this.filterSize; ++i) {
                    ApplyFilterActivity.this.filteredBitmap.add(ApplyFilterActivity.this.applyNativeFilter(ApplyFilterActivity.this.thumbnailBitmap, ApplyFilterActivity.this.filters[i].id));
                }
                ApplyFilterActivity.this.handler.sendEmptyMessage(0);
            }
        };
        this.applyFilterToMainViewRunnable = new Runnable() {
            @Override
            public void run() {
                final int id = ApplyFilterActivity.this.filters[ApplyFilterActivity.this.filterIndex].id;
                final Message message = new Message();
                message.what = 2;
                message.obj = ApplyFilterActivity.this.applyNativeFilter(ApplyFilterActivity.this.viewBitmap, id);
                ApplyFilterActivity.this.handler.sendMessage(message);
            }
        };
        this.applyFilterAndWatermarkSaveRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap watermark = null;
                Bitmap bitmap;
                if (ApplyFilterActivity.this.filterIndex == 0) {
                    bitmap = ApplyFilterActivity.this.viewBitmap;
                    ApplyFilterActivity.this.isApplyFilter = false;
                } else {
                    bitmap = ApplyFilterActivity.this.applyNativeFilter(ApplyFilterActivity.this.viewBitmap, ApplyFilterActivity.this.filters[ApplyFilterActivity.this.filterIndex].id, ApplyFilterActivity.this.filters[ApplyFilterActivity.this.filterIndex].intensity);
                    ApplyFilterActivity.this.isApplyFilter = true;
                }
//                if (ApplyFilterActivity.this.mWatermarkLayout.getSelectedWatermarkNo() == 0) {
//                    ApplyFilterActivity.this.isApplyWatermark = false;
//                } else {
//                    watermark = ApplyFilterActivity.this.applyWatermark(bitmap, ApplyFilterActivity.this.mWatermarkLayout.getColor(), ApplyFilterActivity.this.mWatermarkLayout.getOpacity());
//                    ApplyFilterActivity.this.isApplyWatermark = true;
//                }
                ApplyFilterActivity.this.f = BitmapUtils.bitmapToFile(bitmap, 100);
//                if (ApplyFilterActivity.this.fromActivity != 1 && ApplyFilterActivity.this.isApplyWatermark && watermark != null) {
//                    ApplyFilterActivity.this.watermarkFile = BitmapUtils.bitmapToFile(watermark, 95);
//                }
//                ApplyFilterActivity.this.handler.sendEmptyMessage(1);
            }
        };
        this.mWatermarkClickListener = (View.OnClickListener) new View.OnClickListener() {
            public void onClick(final View view) {
//                ApplyFilterActivity.this.mWatermarkLayout.showWatermarkLayout();
            }
        };
        this.mWatermarkSelectListener = (View.OnClickListener) new View.OnClickListener() {
            public void onClick(final View view) {
                switch (view.getId()) {
                    default: {
                    }
                    case 2131363039: {
                        ApplyFilterActivity.this.setWatermark(false);
                    }
                    case 2131363040: {
                        ApplyFilterActivity.this.setWatermark(true);
                    }
                }
            }
        };
        this.mWatermarkHandler = new Handler((Handler.Callback) new Handler.Callback() {
            public boolean handleMessage(final Message message) {
                switch (message.what) {
                    case 10: {
//                        if (ApplyFilterActivity.this.mWatermarkView != null && ApplyFilterActivity.this.mWatermarkLayout != null) {
//                            ApplyFilterActivity.this.mWatermarkView.setWatermarkAlphaColor(ApplyFilterActivity.this.mWatermarkLayout.getColor(), ApplyFilterActivity.this.mWatermarkLayout.getOpacity());
//                            break;
//                        }
                        break;
                    }
                }
                return true;
            }
        });
        this.filterChildLocation = new int[2];
    }

    private Bitmap applyWatermark(final Bitmap bitmap, int n, final int n2) {
        final float n3 = bitmap.getWidth() / this.mainImageView.getWidth();
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        final Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        final Rect rect = new Rect();
        final Paint paint = new Paint();
        paint.setColor(n);
        paint.setAlpha(n2);
        paint.setTypeface(FontUtils.getNanumRegular());
        paint.setTextSize(this.getRealPixel(n3, 11.0f));
        paint.setAntiAlias(true);
        paint.getTextBounds("\ub2c9\ub124\uc784", 0, "\ub2c9\ub124\uc784".length(), rect);
        final Paint paint2 = new Paint();
        paint2.setColor(n);
        paint2.setAlpha(n2);
        paint2.setAntiAlias(true);
        final Resources resources = this.getResources();
        if (n == -16777216) {
            n = 2130837533;
        } else {
            n = 2130837532;
        }
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, n), (int) this.getRealPixel(n3, 10.0f), (int) this.getRealPixel(n3, 10.0f), true);
        final float realPixel = this.getRealPixel(n3, 16.7f);
        final float realPixel2 = this.getRealPixel(n3, 7.8f);
        final float n4 = bitmap.getWidth() - rect.width() - realPixel + 0.5f;
        canvas.drawText("\ub2c9\ub124\uc784", n4, bitmap.getHeight() - rect.height() - realPixel2 + 0.5f, paint);
        canvas.drawBitmap(scaledBitmap, n4 - scaledBitmap.getWidth() - this.getRealPixel(n3, 2.0f), bitmap.getHeight() - scaledBitmap.getHeight() - this.getRealPixel(n3, 16.0f), paint2);
        return bitmap2;
    }

    private void backActivity() {
        final Intent intent = new Intent();
        intent.putExtra("cameraFrom", this.fromActivity);
        this.setResult(0, intent);
        this.finish();
    }

    private void changeFilter(final int filterIndex) {
//        final NClicks camera_VIDEOFILTER_FILTER = NClicks.CAMERA_VIDEOFILTER_FILTER;
//        camera_VIDEOFILTER_FILTER.setAction(this.filters[filterIndex].name);
//        AceUtils.nClick(camera_VIDEOFILTER_FILTER);
//        this.filterIndex = filterIndex;
//        for (int i = 0; i < this.thumbNailLinearLayout.getChildCount(); ++i) {
//            final View child = this.thumbNailLinearLayout.getChildAt(i);
//            final View viewById = child.findViewById(2131362145);
//            if (filterIndex == i) {
//                viewById.setBackgroundResource(2131230756);
//                final View viewById2 = child.findViewById(2131362147);
//                viewById2.setVisibility(0);
//                this.thumbProgressView = viewById2;
//            }
//            else {
//                viewById.setBackgroundResource(2131230755);
//            }
//        }
//        this.touchBlockView.setVisibility(0);
//        if (filterIndex == 0) {
//            this.seekBarLayout.setVisibility(8);
//        }
//        else {
//            this.seekBarLayout.setVisibility(0);
//        }
//        this.seekBar.setProgress(this.filters[filterIndex].intensity);
//        new Thread(this.applyFilterToMainViewRunnable).start();
    }

    private float getRealPixel(final float n, final float n2) {
        return BitmapUtils.convertDipToPixelInt(n2) * n;
    }

    private void initWatermark() {
        if (this.fromActivity == 0) {
            this.setWatermark(false);
//            this.mWatermarkView.setWatermarkNick("\ub2c9\ub124\uc784");
//            this.mWatermarkLayout.setWatermarkNick("\ub2c9\ub124\uc784");
//            this.mWatermarkView.setOnClickListener(this.mWatermarkClickListener);
//            this.mWatermarkLayout.setOnClickListener(this.mWatermarkSelectListener);
//            this.mWatermarkLayout.setHandler(this.mWatermarkHandler);
            return;
        }
//        this.mWatermarkView.disableWatermark();
    }

    private void scrollSwipeAction() {
        this.thumbNailLinearLayout.getChildAt(this.filterIndex).getLocationInWindow(this.filterChildLocation);
        this.scrollView.smoothScrollBy((int) (this.filterChildLocation[0] - (DeviceUtils.getDeviceWidth() - this.thumbnailSize) / 2.0f), 0);
    }

    private void setWatermark(final boolean b) {
//        this.mWatermarkView.setWatermark(b);
//        this.mWatermarkLayout.setWatermark(b);
    }

    public Bitmap applyNativeFilter(final Bitmap bitmap, final int n) {
        return this.applyNativeFilter(bitmap, n, 255);
    }

    public Bitmap applyNativeFilter(final Bitmap bitmap, int applyFilter, final int n) {
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        if (bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
            Log.e("tag", "Config is not ARGB_8888 : " + bitmap.getConfig().toString());
            return null;
        }
        applyFilter = this.filterLib.applyFilter(applyFilter, bitmap, bitmap2, n, 0, 0, 0);
        if (applyFilter != 0) {
            switch (applyFilter) {
                default: {
                    Log.e("tag", "Unknown Error");
                    break;
                }
                case 1: {
                    Log.e("tag", "FILTER_ERR_INVALID_PARAM");
                    break;
                }
                case 2: {
                    Log.e("tag", "FILTER_ERR_NOT_SUPPORT");
                    break;
                }
            }
            return null;
        }
        return bitmap2.copy(Bitmap.Config.ARGB_8888, true);
    }

    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n2 == 2) {
            this.setResult(2, intent);
            this.finish();
        }
    }

    public void onBackPressed() {
        this.backActivity();
    }

    protected void onCreate(final Bundle bundle) {
        PhotoData photoData;
        String name;
        View inflate;
        LinearLayout.LayoutParams linearLayoutLayoutParams = null;
        View viewById = null;
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_apply_filter);
        this.mContext = (Context) this;
        this.filterLib = new ImageFilter();

//    }

    //Filter Applying module. NDK not prepared yet.
//
        try {
//            this.filterLib.setAppName(this.getPackageManager().getPackageInfo(this.getPackageName(), 0).applicationInfo.sourceDir.getBytes());
            this.originalImageView = (ImageView) this.findViewById(R.id.apply_filter_original_image_view);
            this.mainImageView = (ImageView) this.findViewById(R.id.apply_filter_image_view);
            this.thumbNailLinearLayout = (LinearLayout) this.findViewById(R.id.apply_filter_thumbnail_linear_layout);
            this.scrollView = (HorizontalScrollView) this.thumbNailLinearLayout.getParent();
//                            this.mWatermarkLayout = (WatermarkControlView)this.findViewById(R.id.watermark_bottom_layout);
//                            this.mWatermarkView = (WatermarkView)this.findViewById(R.id.watermark_view);
            this.touchBlockView = this.findViewById(R.id.apply_filter_touch_block_view);
            this.fromActivity = this.getIntent().getIntExtra("cameraFrom", 0);
            this.filterIndex = 0;
            if (this.fromActivity == 0) {
                photoData = CameraActivity.photoData;
                this.viewBitmap = photoData.getPhoto();
                if (this.viewBitmap.getConfig() == null || this.viewBitmap.getConfig() != Bitmap.Config.ARGB_8888) {
                    this.viewBitmap = BitmapUtils.convert(this.viewBitmap, Bitmap.Config.ARGB_8888);
                }
                this.mLatitude = photoData.getLatitude();
                this.mLongitude = photoData.getLongitude();
                this.originalImageView.setImageBitmap(this.viewBitmap);
                this.mainImageView.setImageBitmap(this.viewBitmap);
                this.filters = ImageFilterItem.values();
                this.filterSize = this.filters.length;
                this.findViewById(R.id.edit_top_common_arrow_next_button).setOnClickListener((View.OnClickListener) new View.OnClickListener() {
                    public void onClick(final View view) {
////                                        AceUtils.nClick(NClicks.CAMERA_FILTER_NEXT);
                        ApplyFilterActivity.this.touchBlockView.setVisibility(View.VISIBLE);
                        new Thread(ApplyFilterActivity.this.applyFilterAndWatermarkSaveRunnable).start();
                    }
                });
                this.findViewById(R.id.edit_top_common_arrow_prev_button).setOnClickListener((View.OnClickListener) new View.OnClickListener() {
                    public void onClick(final View view) {
//                                        AceUtils.nClick(NClicks.CAMERA_FILTER_PRE);
                        ApplyFilterActivity.this.backActivity();
                    }
                });
                this.filteredBitmap = new ArrayList<Bitmap>();
                this.thumbnailMargin = this.getResources().getDimension(R.dimen.filter_thumbnail_side_margin);
                this.thumbnailSize = this.getResources().getDimension(R.dimen.filter_thumbnail_size);
                this.thumbnailBitmap = BitmapUtils.thumbnailBitmap(this.viewBitmap.copy(this.viewBitmap.getConfig(), true), (int) this.thumbnailSize);
                for (int i = 0; i < this.filterSize; ++i) {
                    name = this.filters[i].name;
                    inflate = this.getLayoutInflater().inflate(R.layout.filter_select_item, (ViewGroup) this.thumbNailLinearLayout, false);
                    ((TextView) inflate.findViewById(R.id.fnf_nodata_image)).setText((CharSequence) name);
                    linearLayoutLayoutParams = (LinearLayout.LayoutParams) ((FrameLayout) inflate.findViewById(R.id.filter_thumbnail_layout)).getLayoutParams();
                    if (i == 0) {
                        linearLayoutLayoutParams.leftMargin = (int) this.mContext.getResources().getDimension(R.dimen.filter_thumbnail_side_margin);
                        viewById = inflate.findViewById(R.id.filter_select_image_layout);
                        if (this.filterIndex != i) {
                            viewById.setBackgroundResource(R.color.filter_thumbnail_deselected);
                        } else {
                            viewById.setBackgroundResource(R.color.filter_thumbnail_selected);
                            ((ImageView) inflate.findViewById(R.id.filter_select_image_view)).setImageBitmap(this.thumbnailBitmap);
                            final int tmp_i = i;
                            inflate.setOnClickListener((View.OnClickListener) new View.OnClickListener() {
                                public void onClick(final View view) {
                                    if (tmp_i == ApplyFilterActivity.this.filterIndex) {
                                        return;
                                    }
                                    ApplyFilterActivity.this.changeFilter(tmp_i);
                                }
                            });
                            this.thumbNailLinearLayout.addView(inflate);
                        }
                    } else {
                        linearLayoutLayoutParams.leftMargin = 0;
                    }

                }
                linearLayoutLayoutParams.leftMargin = 0;

            }
        } catch(Exception e){
            e.printStackTrace();
        }
//        } catch (PackageManager.NameNotFoundException ex) {
//            this.backActivity();
//            Toast.makeText((Context) this, (CharSequence) this.getResources().getString(R.string.image_unknown_error), Toast.LENGTH_SHORT).show();
//            Log.e("tag", ex.toString());
//            return;
//        }
        photoData = PhotoRatioActivity.photoData;


        this.isApplyFilter = false;
        this.seekBarLayout = this.findViewById(R.id.apply_filter_intensity);
//        (this.seekBar = (SeekBar) this.findViewById(R.id.bu_header)).setMax(255);
//        this.seekBar.setProgress(255);
//        this.seekBar.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) new SeekBar.OnSeekBarChangeListener() {
//            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
//                ApplyFilterActivity.this.filters[ApplyFilterActivity.this.filterIndex].intensity = n;
//                ApplyFilterActivity.this.mainImageView.setAlpha(n);
//            }
//
//            public void onStartTrackingTouch(final SeekBar seekBar) {
//            }
//
//            public void onStopTrackingTouch(final SeekBar seekBar) {
//            }
//        });
        (this.photoFilterGestureView = (PhotoFilterGestureView) this.findViewById(R.id.apply_filter_gesture_view)).setOnGestureListener((GestureDetector.OnGestureListener) this);
        this.photoFilterGestureView.setView((View) this.mainImageView);
        new Thread(this.applyFilterToThumbnailRunnable).start();

        Bitmap bitmap;
            bitmap = ApplyFilterActivity.this.viewBitmap;
            ApplyFilterActivity.this.isApplyFilter = false;
        ApplyFilterActivity.this.f = BitmapUtils.bitmapToFile(bitmap, 100);


        ApplyFilterActivity.this.startPostActivity(ApplyFilterActivity.this.f, ApplyFilterActivity.this.watermarkFile);
        return;
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onDown(final MotionEvent motionEvent) {
        return true;
    }

    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        if (Math.abs(motionEvent.getY() - motionEvent2.getY()) > 250.0f) {
            return false;
        }
        if (motionEvent.getX() - motionEvent2.getX() > 120.0f && Math.abs(n) > 200.0f) {
            if (this.filterIndex == this.filters.length - 1) {
                this.scrollSwipeAction();
                return false;
            }
            this.changeFilter(this.filterIndex + 1);
            this.scrollSwipeAction();
        } else if (motionEvent2.getX() - motionEvent.getX() > 120.0f && Math.abs(n) > 200.0f) {
            if (this.filterIndex == 0) {
                this.scrollSwipeAction();
                return false;
            }
            this.changeFilter(this.filterIndex - 1);
            this.scrollSwipeAction();
        }
        return true;
    }

    public void onLongPress(final MotionEvent motionEvent) {
    }

    protected void onResume() {
        super.onResume();
        if (this.getClass() != null) {
//            AceUtils.site(this.getClass().getSimpleName());
        }
    }

    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        return true;
    }

    public void onShowPress(final MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        return true;
    }

    public void startPostActivity(final File file, final File file2) {
        String absolutePath;
        if (file2 != null) {
            absolutePath = file2.getAbsolutePath();
        } else {
            absolutePath = null;
        }
        mainApplication.getInstance().getPostingInfo().mediaType = "PIC";
        mainApplication.getInstance().getPostingInfo().filterApplied = this.isApplyFilter;
        mainApplication.getInstance().getPostingInfo().postingMediaPath = file.getAbsolutePath();
        final Intent intent = new Intent((Context) this, (Class) PostActivity.class);
        intent.setAction("action_start_post_fragment");
        intent.putExtra("watermark_path", absolutePath);
        intent.putExtra("media_latitude", this.mLatitude);
        intent.putExtra("media_longitude", this.mLongitude);
        intent.putExtra("watermark_path", absolutePath);
        intent.putExtra("watermark_path", absolutePath);
        intent.putExtra("isApplyWatermark", this.isApplyWatermark);
        intent.putExtra("intentTextBody", this.getIntent().getStringExtra("intentTextBody"));
        intent.setFlags(537001984);
        this.startActivityForResult(intent, CommonConstants.POST_PIC_SUC);
    }
}