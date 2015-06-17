package com.mapia.ratio;

/**
 * Created by daehyun on 15. 6. 17..
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import com.mapia.MainApplication;
import com.mapia.R;
import com.mapia.camera.viewmodel.RatioManager;
import com.mapia.common.data.PhotoData;
import com.mapia.ratio.view.CropImageView;
import com.mapia.util.BitmapUtils;
import com.mapia.util.FileUtils;
import com.mapia.util.GPSUtil;
import com.mapia.util.ImageSizeUtil;

import java.io.File;

public class PhotoRatioActivity extends Activity
{
    public static PhotoData photoData;
    private final int ACTIVITY_REQUESTCODE_APPLY_FILTER;
    private final float MAX_SCALE;
    private Bitmap bitmap;
    private FrameLayout centerLayout;
    private File croppedFile;
    Handler handler;
    private int imageViewHeight;
    private CropImageView imageViewTouch;
    private int imageViewWidth;
    private Context mContext;
    private float minScale;
    private String originalFilePath;
    private int originalHeight;
    private float originalScale;
    private int originalWidth;
    private ProgressDialog progressDialog;
    private ImageButton ratio11Button;
    private ImageButton ratio34Button;
    private ImageButton ratio43Button;
    private RatioManager ratioManager;

    public PhotoRatioActivity() {
        super();
        this.ACTIVITY_REQUESTCODE_APPLY_FILTER = 100;
        this.MAX_SCALE = 8.0f;
        this.handler = new Handler();
    }

    private void backActivity() {
        this.deleteCroppedFile();
        this.setResult(0);
        this.finish();
    }

    private void crop() {
        if (this.imageViewTouch.isChangingScale()) {
            return;
        }
        new CropBitmapAsyncTask().execute(new Void[0]);
    }

    private void deleteCroppedFile() {
        FileUtils.deleteFile(this.croppedFile);
    }

    private void drawGridGuideView(final int currentRect) {
        if (this.ratioManager != null) {
            this.ratioManager.setCurrentRect(currentRect);
            this.imageViewTouch.setCurrentRect(this.ratioManager.getCurrentRect());
            this.imageViewTouch.invalidate();
        }
    }

    private void fitBitmapToCurrentRect() {
        if (this.bitmap == null) {
            return;
        }
        final Rect currentRect = this.ratioManager.getCurrentRect();
        final int width = currentRect.width();
        final int width2 = this.bitmap.getWidth();
        final int height = currentRect.height();
        final int height2 = this.bitmap.getHeight();
        if (width > width2) {
            this.minScale = width / width2;
        }
        else {
            this.minScale = 1.0f;
        }
        if (height > height2 * this.minScale) {
            this.minScale = height / height2;
        }
        final Matrix matrix = new Matrix();
        matrix.postScale(this.minScale, this.minScale);
        this.imageViewTouch.setDisplayType(ImageViewTouchBase.DisplayType.NONE);
        this.imageViewTouch.setImageBitmap(this.bitmap, matrix, this.minScale, 8.0f);
    }

    private void initGridGuideView() {
        this.imageViewTouch.getCropGridGuide().setInnerLineAlpha(154);
        this.imageViewTouch.getCropGridGuide().setIsOuterLine(true);
    }

    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        this.deleteCroppedFile();
        if (n2 == 2) {
            this.setResult(2, intent);
            this.finish();
        }
    }

    public void onBackPressed() {
        this.backActivity();
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_crop);
        this.mContext = (Context)this;
        this.centerLayout = (FrameLayout)this.findViewById(R.id.crop_center_layout);
        this.imageViewTouch = (CropImageView)this.findViewById(R.id.image_view_touch);
        this.ratio11Button = (ImageButton)this.findViewById(R.id.crop_ratio11_button);
        this.ratio34Button = (ImageButton)this.findViewById(R.id.crop_ratio34_button);
        this.ratio43Button = (ImageButton)this.findViewById(R.id.crop_ratio43_button);
        ((TextView)this.findViewById(R.id.edit_top_common_title_text)).setText(R.string.crop_top_title);
        final RatioButtonListener onClickListener = new RatioButtonListener();
        this.ratio11Button.setOnClickListener((View.OnClickListener)onClickListener);
        this.ratio34Button.setOnClickListener((View.OnClickListener)onClickListener);
        this.ratio43Button.setOnClickListener((View.OnClickListener)onClickListener);
        this.initGridGuideView();
        this.imageViewTouch.setDoubleTapEnabled(false);
        this.originalScale = 1.0f;
        this.minScale = 1.0f;
        this.findViewById(R.id.edit_top_common_arrow_next_button).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (!PhotoRatioActivity.this.imageViewTouch.isChangingScale()) {
                    PhotoRatioActivity.this.crop();
                }
            }
        });
        this.findViewById(R.id.edit_top_common_arrow_prev_button).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                PhotoRatioActivity.this.backActivity();
            }
        });
        this.originalFilePath = this.getIntent().getStringExtra("chosenFile");
        this.centerLayout.post((Runnable)new Runnable() {
            @Override
            public void run() {
                final Rect fitCenter = ImageSizeUtil.fitCenter(new Rect(0, 0, 3, 4), new Rect(0, 0, PhotoRatioActivity.this.centerLayout.getWidth(), PhotoRatioActivity.this.centerLayout.getHeight()));
                PhotoRatioActivity.this.ratioManager = new RatioManager(fitCenter.width(), fitCenter.height());
                PhotoRatioActivity.this.drawGridGuideView(0);
                final View viewById = PhotoRatioActivity.this.findViewById(R.id.image_view_touch_layout);
                PhotoRatioActivity.this.imageViewHeight = fitCenter.height();
                PhotoRatioActivity.this.imageViewWidth = fitCenter.width();
                viewById.getLayoutParams().height = PhotoRatioActivity.this.imageViewHeight;
                viewById.getLayoutParams().width = PhotoRatioActivity.this.imageViewWidth;
                new ImageLoadAsyncTask().execute(new Void[0]);
            }
        });
        this.progressDialog = ProgressDialog.show(this.mContext, (CharSequence)"", (CharSequence)"Loading...", true);
    }

    protected void onDestroy() {
        this.imageViewTouch.clear();
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        if (this.getClass() != null) {
//            AceUtils.site(this.getClass().getSimpleName());
        }
    }

    private class CropBitmapAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private String errorMessage;

        protected Void doInBackground(final Void... array) {
            final RectF bitmapRect = PhotoRatioActivity.this.imageViewTouch.getBitmapRect();
            float n;
            if ((n = PhotoRatioActivity.this.imageViewTouch.getScale()) < PhotoRatioActivity.this.minScale) {
                n = PhotoRatioActivity.this.minScale;
            }
            final RectF drawRect = PhotoRatioActivity.this.imageViewTouch.getCropGridGuide().getDrawRect();
            final float n2 = (drawRect.left - bitmapRect.left) / n;
            final float n3 = (drawRect.top - bitmapRect.top) / n;
            final float n4 = (drawRect.right - bitmapRect.left) / n;
            final float n5 = (drawRect.bottom - bitmapRect.top) / n;
            final int n6 = (int)((n4 - n2) / PhotoRatioActivity.this.originalScale);
            final int n7 = (int)((n5 - n3) / PhotoRatioActivity.this.originalScale);
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFilePath(PhotoRatioActivity.this.originalFilePath, 4000, 4000);
            if (bitmap == null) {
                this.errorMessage = PhotoRatioActivity.this.getResources().getString(R.string.image_unknown_error);
                return null;
            }
            if (n6 != 0 && n7 != 0) {
                final int n8 = (int)(n2 / PhotoRatioActivity.this.originalScale);
                final int n9 = (int)(n3 / PhotoRatioActivity.this.originalScale);
                int n10;
                if ((n10 = n8) < 0) {
                    n10 = 0;
                }
                int n11;
                if ((n11 = n9) < 0) {
                    n11 = 0;
                }
                try {
                    bitmap = Bitmap.createBitmap(bitmap, n10, n11, n6, n7);
                }
                catch (OutOfMemoryError outOfMemoryError) {
                    this.errorMessage = PhotoRatioActivity.this.getResources().getString(R.string.image_oom_error);
                    return null;
                }
            }
            final ExifInterface exif = BitmapUtils.getExif(PhotoRatioActivity.this.originalFilePath);
            final String attribute = exif.getAttribute("GPSLatitude");
            final String attribute2 = exif.getAttribute("GPSLatitudeRef");
            final String attribute3 = exif.getAttribute("GPSLongitude");
            final String attribute4 = exif.getAttribute("GPSLongitudeRef");
            final double doubleValue = GPSUtil.getLatitudeDegree(attribute, attribute2);
            final double doubleValue2 = GPSUtil.getLongitudeDegree(attribute3, attribute4);
            PhotoRatioActivity.photoData = new PhotoData(bitmap, 0, false);
            if (doubleValue != 200.0 && doubleValue2 != 200.0) {
                PhotoRatioActivity.photoData.setLocation(doubleValue, doubleValue2);
            }
            return null;
        }

        protected void onPostExecute(final Void void1) {
            if (PhotoRatioActivity.photoData.getPhoto() != null) {
                MainApplication.getInstance().getPostingInfo().copyrightYn = "N";
//                final Intent intent = new Intent((Context)PhotoRatioActivity.this, (Class)ApplyFilterActivity.class);
//                intent.putExtra("cameraFrom", 1);
//                intent.putExtra("videoRatio", PhotoRatioActivity.this.ratioManager.getCurrentRatio());
//                intent.putExtra("intentTextBody", PhotoRatioActivity.this.getIntent().getStringExtra("intentTextBody"));
//                PhotoRatioActivity.this.startActivityForResult(intent, 100);
            }
            else {
                Toast.makeText(PhotoRatioActivity.this.mContext, (CharSequence) this.errorMessage, Toast.LENGTH_SHORT).show();
                PhotoRatioActivity.this.backActivity();
            }
            PhotoRatioActivity.this.progressDialog.dismiss();
        }

        protected void onPreExecute() {
            PhotoRatioActivity.this.progressDialog = ProgressDialog.show(PhotoRatioActivity.this.mContext, (CharSequence)"", (CharSequence)"Cropping...", true);
        }
    }

    private class ImageLoadAsyncTask extends AsyncTask<Void, Void, Void>
    {
        protected Void doInBackground(final Void... array) {
            final Bitmap decodeSampledBitmapFromFilePath = BitmapUtils.decodeSampledBitmapFromFilePath(PhotoRatioActivity.this.originalFilePath, 4000, 4000);
            if (decodeSampledBitmapFromFilePath == null) {
                return null;
            }
            PhotoRatioActivity.this.originalWidth = decodeSampledBitmapFromFilePath.getWidth();
            PhotoRatioActivity.this.originalHeight = decodeSampledBitmapFromFilePath.getHeight();
            final int imageViewWidth = PhotoRatioActivity.this.imageViewWidth;
            final int imageViewHeight = PhotoRatioActivity.this.imageViewHeight;
            PhotoRatioActivity.this.originalScale = imageViewWidth / PhotoRatioActivity.this.originalWidth;
            int n = imageViewWidth;
            int n2;
            if ((n2 = (int)(PhotoRatioActivity.this.originalHeight * PhotoRatioActivity.this.originalScale)) > imageViewHeight) {
                PhotoRatioActivity.this.originalScale = imageViewHeight / PhotoRatioActivity.this.originalHeight;
                n2 = imageViewHeight;
                n = (int)(PhotoRatioActivity.this.originalWidth * PhotoRatioActivity.this.originalScale);
            }
            if (imageViewWidth > PhotoRatioActivity.this.originalWidth && imageViewHeight > PhotoRatioActivity.this.originalHeight) {
                PhotoRatioActivity.this.bitmap = BitmapUtils.resizeBitmap(decodeSampledBitmapFromFilePath, n, n2);
                return null;
            }
            PhotoRatioActivity.this.bitmap = BitmapUtils.lanczosResizeBitmap(decodeSampledBitmapFromFilePath, n, n2);
            return null;
        }

        protected void onPostExecute(final Void void1) {
            PhotoRatioActivity.this.progressDialog.dismiss();
            if (PhotoRatioActivity.this.bitmap == null) {
                Toast.makeText(PhotoRatioActivity.this.mContext, (CharSequence)PhotoRatioActivity.this.getResources().getString(R.string.crop_image_error), Toast.LENGTH_SHORT).show();
                PhotoRatioActivity.this.backActivity();
                return;
            }
            PhotoRatioActivity.this.fitBitmapToCurrentRect();
            PhotoRatioActivity.this.handler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    PhotoRatioActivity.this.imageViewTouch.resetDisplay();
                }
            });
        }
    }

    private class RatioButtonListener implements View.OnClickListener
    {
        public void onClick(final View view) {

                if(view == ratio11Button){
                    PhotoRatioActivity.this.ratio11Button.setImageResource(R.drawable.cam_rate_11_over);
                    PhotoRatioActivity.this.ratio11Button.setBackgroundResource(R.drawable.camera_rate_background_pressed);
                    PhotoRatioActivity.this.ratio34Button.setImageResource(R.drawable.cam_rate_34);
                    PhotoRatioActivity.this.ratio34Button.setBackgroundResource(R.drawable.camera_rate_background);
                    PhotoRatioActivity.this.ratio43Button.setImageResource(R.drawable.cam_rate_43);
                    PhotoRatioActivity.this.ratio43Button.setBackgroundResource(R.drawable.camera_rate_background);
                    PhotoRatioActivity.this.drawGridGuideView(0);

                }
                else if(view==ratio34Button) {
                    if (PhotoRatioActivity.this.originalWidth < 3 && PhotoRatioActivity.this.originalHeight < 4) {
                        Toast.makeText(PhotoRatioActivity.this.getApplicationContext(), (CharSequence)PhotoRatioActivity.this.getResources().getString(R.string.crop_no_more), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PhotoRatioActivity.this.ratio11Button.setImageResource(R.drawable.cam_rate_11);
                    PhotoRatioActivity.this.ratio11Button.setBackgroundResource(R.drawable.camera_rate_background);
                    PhotoRatioActivity.this.ratio34Button.setImageResource(R.drawable.cam_rate_34);
                    PhotoRatioActivity.this.ratio34Button.setBackgroundResource(R.drawable.camera_rate_background_pressed);
                    PhotoRatioActivity.this.ratio43Button.setImageResource(R.drawable.cam_rate_43);
                    PhotoRatioActivity.this.ratio43Button.setBackgroundResource(R.drawable.camera_rate_background);
                    PhotoRatioActivity.this.drawGridGuideView(1);

                }
                else if(view==ratio43Button) {
                    if (PhotoRatioActivity.this.originalWidth < 4 && PhotoRatioActivity.this.originalHeight < 3) {
                        Toast.makeText(PhotoRatioActivity.this.getApplicationContext(), (CharSequence)PhotoRatioActivity.this.getResources().getString(R.string.crop_no_more), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PhotoRatioActivity.this.ratio11Button.setImageResource(R.drawable.cam_rate_11);
                    PhotoRatioActivity.this.ratio11Button.setBackgroundResource(R.drawable.camera_rate_background);
                    PhotoRatioActivity.this.ratio34Button.setImageResource(R.drawable.cam_rate_34);
                    PhotoRatioActivity.this.ratio34Button.setBackgroundResource(R.drawable.camera_rate_background);
                    PhotoRatioActivity.this.ratio43Button.setImageResource(R.drawable.cam_rate_43_over);
                    PhotoRatioActivity.this.ratio43Button.setBackgroundResource(R.drawable.cam_rate_43);
                    PhotoRatioActivity.this.drawGridGuideView(2);

                }

            PhotoRatioActivity.this.fitBitmapToCurrentRect();
            PhotoRatioActivity.this.handler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    PhotoRatioActivity.this.imageViewTouch.resetDisplay();
                }
            });
        }
    }
}