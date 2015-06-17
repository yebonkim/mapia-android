package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.mapia.camera.model.RecordClipFile;
import com.mapia.camera.model.RecordClipHistory;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public final class RecordingProgressBar extends View
{
    private static int BLINK_DURATION = 0;
    private static final int BLINK_INDICATOR = 256;
    private static int DEFAULT_THROTTLE = 0;
    private static int MAX_DURATION = 0;
    private static final String TAG = "RecordingProgressBar";
    private List<Clip> clips;
    private int currentRecordLength;
    private long currentRecordTime;
    private boolean doBlinking;
    private Drawable gapDrawable;
    private int gapLength;
    private Handler handler;
    private int indicatorLength;
    private boolean isThrottled;
    private RecordingProgressBarListener listener;
    private RecordClipHistory mClipHistory;
    private int maxDuration;
    private OnAddMediaListener onAddMediaListener;
    private int pixelRangeMax;
    private int pixelRangeMin;
    Point point;
    private Drawable recordedClipDrawable;
    private Drawable recordedIndicatorDrawable;
    private Drawable recordingClipDrawable;
    private Drawable recordingIndicatorDrawable;
    private Drawable selectingClipDrawable;
    private Status status;
    private int throttle;
    private Drawable throttleDrawable;
    private int throttlePos;
    private int viewHeight;
    private int viewWidth;

    static {
        RecordingProgressBar.MAX_DURATION = 15000;
        RecordingProgressBar.BLINK_DURATION = 500;
        RecordingProgressBar.DEFAULT_THROTTLE = 5000;
    }

    public RecordingProgressBar(final Context context) {
        super(context);
        this.currentRecordLength = 0;
        this.currentRecordTime = 0L;
        this.throttlePos = 0;
        this.isThrottled = false;
        this.gapLength = 0;
        this.indicatorLength = 0;
        this.maxDuration = RecordingProgressBar.MAX_DURATION;
        this.throttle = 0;
        this.throttleDrawable = null;
        this.gapDrawable = null;
        this.recordingClipDrawable = null;
        this.recordedClipDrawable = null;
        this.selectingClipDrawable = null;
        this.recordingIndicatorDrawable = null;
        this.recordedIndicatorDrawable = null;
        this.doBlinking = true;
        this.mClipHistory = new RecordClipHistory();
        this.status = Status.NONE;
        this.pixelRangeMin = 0;
        this.pixelRangeMax = 0;
        this.point = new Point();
        this.clips = null;
        this.handler = new UIHandler(this);
        this.listener = null;
        this.onAddMediaListener = null;
        this.init();
    }

    public RecordingProgressBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.currentRecordLength = 0;
        this.currentRecordTime = 0L;
        this.throttlePos = 0;
        this.isThrottled = false;
        this.gapLength = 0;
        this.indicatorLength = 0;
        this.maxDuration = RecordingProgressBar.MAX_DURATION;
        this.throttle = 0;
        this.throttleDrawable = null;
        this.gapDrawable = null;
        this.recordingClipDrawable = null;
        this.recordedClipDrawable = null;
        this.selectingClipDrawable = null;
        this.recordingIndicatorDrawable = null;
        this.recordedIndicatorDrawable = null;
        this.doBlinking = true;
        this.mClipHistory = new RecordClipHistory();
        this.status = Status.NONE;
        this.pixelRangeMin = 0;
        this.pixelRangeMax = 0;
        this.point = new Point();
        this.clips = null;
        this.handler = new UIHandler(this);
        this.listener = null;
        this.onAddMediaListener = null;
        this.init();
//        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, com.mapia.R.styleable.RecordingProgressBar);
//        this.gapLength = Math.round(obtainStyledAttributes.getDimension(0, 10.0f));
//        this.indicatorLength = Math.round(obtainStyledAttributes.getDimension(1, 10.0f));
//        this.maxDuration = obtainStyledAttributes.getInt(2, RecordingProgressBar.MAX_DURATION);
//        this.throttle = obtainStyledAttributes.getInt(3, RecordingProgressBar.DEFAULT_THROTTLE);
//        this.throttleDrawable = obtainStyledAttributes.getDrawable(4);
//        this.gapDrawable = obtainStyledAttributes.getDrawable(5);
//        this.recordingClipDrawable = obtainStyledAttributes.getDrawable(6);
//        this.recordedClipDrawable = obtainStyledAttributes.getDrawable(7);
//        this.selectingClipDrawable = obtainStyledAttributes.getDrawable(8);
//        this.recordingIndicatorDrawable = obtainStyledAttributes.getDrawable(9);
//        this.recordedIndicatorDrawable = obtainStyledAttributes.getDrawable(10);
//        obtainStyledAttributes.recycle();
    }

    public RecordingProgressBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.currentRecordLength = 0;
        this.currentRecordTime = 0L;
        this.throttlePos = 0;
        this.isThrottled = false;
        this.gapLength = 0;
        this.indicatorLength = 0;
        this.maxDuration = RecordingProgressBar.MAX_DURATION;
        this.throttle = 0;
        this.throttleDrawable = null;
        this.gapDrawable = null;
        this.recordingClipDrawable = null;
        this.recordedClipDrawable = null;
        this.selectingClipDrawable = null;
        this.recordingIndicatorDrawable = null;
        this.recordedIndicatorDrawable = null;
        this.doBlinking = true;
        this.mClipHistory = new RecordClipHistory();
        this.status = Status.NONE;
        this.pixelRangeMin = 0;
        this.pixelRangeMax = 0;
        this.point = new Point();
        this.clips = null;
        this.handler = new UIHandler(this);
        this.listener = null;
        this.onAddMediaListener = null;
        this.init();
    }

    private void drawClips(final Canvas canvas) {
        if (this.clips != null) {
            for (final Clip clip : this.clips) {
                Drawable drawable = null;
                switch (clip.getStatus()) {
                    case RECORDING: {
                        drawable = this.recordingClipDrawable;
                        break;
                    }
                    case RECORDED: {
                        drawable = this.recordedClipDrawable;
                        break;
                    }
                    case SELECTING: {
                        drawable = this.selectingClipDrawable;
                        break;
                    }
                }
                if (drawable != null) {
                    final Rect bounds = new Rect();
                    final Rect rect = new Rect();
                    drawable.getPadding(rect);
                    bounds.left = clip.getLeft() + this.getPaddingLeft();
                    bounds.top = this.getPaddingTop() + rect.top;
                    bounds.right = clip.getRight() + this.getPaddingRight() - clip.getGap();
                    bounds.bottom = this.getMeasuredHeight() - this.getPaddingBottom() - rect.bottom;
                    drawable.setBounds(bounds);
                    drawable.draw(canvas);
                    this.drawGap(canvas, bounds.right, clip.getGap());
                }
            }
        }
    }

    private void drawGap(final Canvas canvas, final int left, final int n) {
        if (this.gapDrawable != null) {
            final Rect bounds = new Rect();
            bounds.left = left;
            bounds.top = this.getPaddingTop();
            bounds.right = left + n;
            bounds.bottom = this.getMeasuredHeight() - this.getPaddingBottom();
            this.gapDrawable.setBounds(bounds);
            this.gapDrawable.draw(canvas);
        }
    }

    private void drawIndicator(final Canvas canvas) {
        Drawable drawable2;
        final Drawable drawable = drawable2 = null;
        Label_0050: {
            switch (this.status) {
                default: {
                    drawable2 = drawable;
                    break Label_0050;
                }
                case RECORDED:
                case IDLE: {
                    drawable2 = this.recordedIndicatorDrawable;
                    break Label_0050;
                }
                case RECORDING: {
                    drawable2 = this.recordingIndicatorDrawable;
                }
                case SELECTING:
                case DELETE: {
                    if (drawable2 != null && this.doBlinking) {
                        final Rect bounds = new Rect();
                        final Rect rect = new Rect();
                        drawable2.getPadding(rect);
                        bounds.left = this.currentRecordLength;
                        bounds.top = this.getPaddingTop() + rect.top;
                        bounds.right = this.currentRecordLength + this.indicatorLength;
                        bounds.bottom = this.getMeasuredHeight() - this.getPaddingBottom() - rect.bottom;
                        drawable2.setBounds(bounds);
                        drawable2.draw(canvas);
                    }
                }
            }
        }
    }

    private void drawThrottle(final Canvas canvas) {
        if (this.throttleDrawable != null) {
            final Rect bounds = new Rect();
            bounds.top = this.getPaddingTop();
            bounds.bottom = this.getMeasuredHeight() - this.getPaddingBottom();
            bounds.left = this.getPaddingLeft();
            bounds.right = this.millsToPixels(this.throttle);
            this.throttleDrawable.setBounds(bounds);
            this.throttleDrawable.draw(canvas);
        }
    }

    private void init() {
        this.setStatus(Status.IDLE, true);
        this.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                RecordingProgressBar.this.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                RecordingProgressBar.this.handler.sendEmptyMessageDelayed(256, (long)RecordingProgressBar.BLINK_DURATION);
            }
        });
    }

    private int measureHeight(int n) {
        final int mode = View.MeasureSpec.getMode(n);
        final int size = View.MeasureSpec.getSize(n);
        if (mode == 1073741824) {
            n = size;
        }
        else {
            final int n2 = n = this.getPaddingTop() + size + this.getPaddingBottom();
            if (mode == Integer.MIN_VALUE) {
                return Math.min(n2, size);
            }
        }
        return n;
    }

    private int measureWidth(int n) {
        final int mode = View.MeasureSpec.getMode(n);
        final int size = View.MeasureSpec.getSize(n);
        if (mode == 1073741824) {
            n = size;
        }
        else {
            final int n2 = n = this.getPaddingLeft() + size + this.getPaddingRight();
            if (mode == Integer.MIN_VALUE) {
                return Math.min(n2, size);
            }
        }
        return n;
    }

    private int millsToPixels(final long n) {
        long n2 = n;
        if (n > this.maxDuration) {
            n2 = this.maxDuration;
            this.currentRecordTime = this.maxDuration;
        }
        return Math.round(n2 * (this.pixelRangeMax - this.pixelRangeMin) / this.maxDuration) + this.pixelRangeMin;
    }

    private int pixelsToMills(final int n) {
        return Math.round(this.maxDuration * n / (this.pixelRangeMax - this.pixelRangeMin));
    }

    public void addClip(final boolean facing) {
        if (this.clips == null) {
            this.clips = new ArrayList<Clip>();
        }
        if (this.clips != null) {
            final Clip clip = new Clip();
            clip.setLeft(this.currentRecordLength);
            clip.setGap(this.gapLength);
            clip.setStatus(Status.RECORDING);
            clip.setFacing(facing);
            if (this.onAddMediaListener != null) {
                clip.setType(MediaType.VIDEO);
                clip.setPath(this.onAddMediaListener.getMediaPath());
                clip.setPreviewWidth(this.onAddMediaListener.getPreviewWidth());
                clip.setPreviewHeight(this.onAddMediaListener.getPreviewHeight());
            }
            this.clips.add(clip);
        }
    }

    public void deleteClip() {
        if (this.clips != null && this.clips.size() > 0) {
            final Clip clip = this.clips.get(this.clips.size() - 1);
            if (clip != null) {
                final int duration = clip.getDuration();
                final int n = clip.getRight() - clip.getLeft();
                int pixelsToMills;
                if ((pixelsToMills = duration) == 0) {
                    pixelsToMills = this.pixelsToMills(n);
                }
                this.currentRecordLength -= n;
                this.currentRecordTime -= pixelsToMills;
                this.clips.remove(clip);
                if (this.listener != null) {
                    this.listener.onDelete(pixelsToMills);
                }
                if (this.currentRecordTime < RecordingProgressBar.DEFAULT_THROTTLE) {
                    this.isThrottled = false;
                }
                this.mClipHistory.deleteLastClipFile();
            }
        }
    }

    public void finishClip() {
        if (this.clips != null && this.clips.size() > 0) {
            final Clip clip = this.clips.get(this.clips.size() - 1);
            if (clip != null) {
                clip.setStatus(Status.RECORDED);
                while (true) {
                    Label_0140: {
                        if (this.onAddMediaListener == null) {
                            break Label_0140;
                        }
                        final int mediaDuration = this.onAddMediaListener.getMediaDuration();
                        if (mediaDuration <= 0 || clip.getDuration() != 0) {
                            break Label_0140;
                        }
                        clip.setDuration(mediaDuration);
                        final RecordClipFile recordClipFile = new RecordClipFile();
                        recordClipFile.setPath(clip.getPath());
                        recordClipFile.setDuration(clip.getDuration());
                        recordClipFile.setFacingBack(clip.back == 0);
                        this.mClipHistory.addClipFile(recordClipFile);
                        return;
                    }
                    if (clip.getDuration() == 0) {
                        clip.setDuration(this.pixelsToMills(clip.getRight() - clip.getLeft()));
                    }
                    continue;
                }
            }
        }
    }

    public List<Clip> getClips() {
        return this.clips;
    }

    public Status getStatus() {
        return this.status;
    }

    protected void onDetachedFromWindow() {
        this.handler.removeCallbacksAndMessages((Object)null);
        super.onDetachedFromWindow();
    }

    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.drawThrottle(canvas);
        this.drawClips(canvas);
        this.drawIndicator(canvas);
    }

    @SuppressLint({ "NewApi" })
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.viewWidth = this.measureWidth(n);
        this.viewHeight = this.measureHeight(n2);
        this.setMeasuredDimension(this.viewWidth, this.viewHeight);
        this.pixelRangeMin = this.getPaddingLeft();
        this.pixelRangeMax = this.viewWidth - this.getPaddingRight();
        this.throttlePos = this.millsToPixels(this.throttle);
    }

    protected void onRestoreInstanceState(final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.status = savedState.status;
        this.currentRecordLength = savedState.currentRecordLength;
        this.currentRecordTime = savedState.currentRecordTime;
        this.clips = savedState.clips;
        this.handler.sendEmptyMessage(256);
    }

    protected Parcelable onSaveInstanceState() {
        return (Parcelable)new SavedState(super.onSaveInstanceState(), this.status, this.currentRecordLength, this.currentRecordTime, this.clips);
    }

    public void selectClip() {
        if (this.clips != null && this.clips.size() > 0) {
            final Clip clip = this.clips.get(this.clips.size() - 1);
            if (clip != null) {
                clip.setStatus(Status.SELECTING);
            }
        }
    }

    public void setListener(final RecordingProgressBarListener listener) {
        this.listener = listener;
    }

    public void setOnAddMediaListener(final OnAddMediaListener onAddMediaListener) {
        this.onAddMediaListener = onAddMediaListener;
    }

    public void setStatus(final Status status, final boolean b) {
        if (this.status == status || (status == Status.DELETE && this.status != Status.SELECTING)) {
            return;
        }
        this.status = status;
        switch (this.status) {
            case RECORDING: {
                this.addClip(b);
                break;
            }
            case RECORDED: {
                this.finishClip();
                break;
            }
            case SELECTING: {
                this.selectClip();
                break;
            }
            case DELETE: {
                this.deleteClip();
                break;
            }
        }
        this.invalidate();
    }

    public void update(final long currentRecordTime) {
        if (this.currentRecordTime != currentRecordTime && this.currentRecordTime <= this.maxDuration) {
            this.currentRecordTime = currentRecordTime;
            final int millsToPixels = this.millsToPixels(currentRecordTime);
            if (this.currentRecordLength != millsToPixels) {
                this.currentRecordLength = millsToPixels;
                this.updateClip();
                if (!this.isThrottled && this.listener != null && this.currentRecordLength >= this.throttlePos) {
                    this.listener.onThrottle();
                    this.isThrottled = true;
                }
            }
        }
    }

    public void updateClip() {
        if (this.clips != null && this.clips.size() > 0) {
            final Clip clip = this.clips.get(this.clips.size() - 1);
            if (clip != null) {
                clip.setRight(this.currentRecordLength);
                this.invalidate();
            }
        }
    }

    public static class Clip implements Parcelable
    {
        public static final Parcelable.Creator<Clip> CREATOR;
        public int back;
        private int duration;
        private int gap;
        private int left;
        private String path;
        private int previewHeight;
        private int previewWidth;
        private int right;
        private Status status;
        private MediaType type;

        static {
            CREATOR = new Parcelable.Creator<Clip>() {
                public Clip createFromParcel(final Parcel parcel) {
                    final Clip clip = new Clip();
                    clip.left = parcel.readInt();
                    clip.right = parcel.readInt();
                    clip.status = (Status)parcel.readSerializable();
                    clip.gap = parcel.readInt();
                    clip.type = (MediaType)parcel.readSerializable();
                    clip.path = parcel.readString();
                    clip.duration = parcel.readInt();
                    clip.previewWidth = parcel.readInt();
                    clip.previewHeight = parcel.readInt();
                    clip.back = parcel.readInt();
                    return clip;
                }

                public Clip[] newArray(final int n) {
                    return new Clip[n];
                }
            };
        }

        public static Parcelable.Creator<Clip> getCreator() {
            return Clip.CREATOR;
        }

        public int describeContents() {
            return 0;
        }

        public int getDuration() {
            return this.duration;
        }

        public int getGap() {
            return this.gap;
        }

        public int getLeft() {
            return this.left;
        }

        public String getPath() {
            return this.path;
        }

        public int getPreviewHeight() {
            return this.previewHeight;
        }

        public int getPreviewWidth() {
            return this.previewWidth;
        }

        public int getRight() {
            return this.right;
        }

        public Status getStatus() {
            return this.status;
        }

        public MediaType getType() {
            return this.type;
        }

        public void setDuration(final int duration) {
            this.duration = duration;
        }

        public void setFacing(final boolean b) {
            if (b) {
                this.back = 0;
                return;
            }
            this.back = 1;
        }

        public void setGap(final int gap) {
            this.gap = gap;
        }

        public void setLeft(final int left) {
            this.left = left;
        }

        public void setPath(final String path) {
            this.path = path;
        }

        public void setPreviewHeight(final int previewHeight) {
            this.previewHeight = previewHeight;
        }

        public void setPreviewWidth(final int previewWidth) {
            this.previewWidth = previewWidth;
        }

        public void setRight(final int right) {
            this.right = right;
        }

        public void setStatus(final Status status) {
            this.status = status;
        }

        public void setType(final MediaType type) {
            this.type = type;
        }

        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.left);
            parcel.writeInt(this.right);
            parcel.writeSerializable((Serializable)this.status);
            parcel.writeInt(this.gap);
            parcel.writeSerializable((Serializable)this.type);
            parcel.writeString(this.path);
            parcel.writeInt(this.duration);
            parcel.writeInt(this.previewWidth);
            parcel.writeInt(this.previewHeight);
            parcel.writeInt(this.back);
        }
    }

    public enum MediaType
    {
        PHOTO,
        VIDEO;
    }

    public interface OnAddMediaListener
    {
        int getMediaDuration();

        String getMediaPath();

        int getPreviewHeight();

        int getPreviewWidth();
    }

    public interface RecordingProgressBarListener
    {
        void onDelete(int p0);

        void onThrottle();
    }

    private static class SavedState extends View.BaseSavedState
    {
        public static final Parcelable.Creator<SavedState> CREATOR;
        private List<Clip> clips;
        private int currentRecordLength;
        private long currentRecordTime;
        private Status status;

        static {
            CREATOR = new Parcelable.Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }

                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }

        public SavedState(final Parcel parcel) {
            super(parcel);
            this.status = Status.IDLE;
            this.currentRecordLength = 0;
            this.currentRecordTime = 0L;
            this.clips = null;
            if (parcel != null) {
                this.status = (Status)parcel.readSerializable();
                this.currentRecordLength = parcel.readInt();
                this.currentRecordTime = parcel.readLong();
                this.clips = (List<Clip>)parcel.readArrayList(Clip.class.getClassLoader());
            }
        }

        public SavedState(final Parcelable parcelable, final Status status, final int currentRecordLength, final long currentRecordTime, final List<Clip> clips) {
            super(parcelable);
            this.status = Status.IDLE;
            this.currentRecordLength = 0;
            this.currentRecordTime = 0L;
            this.clips = null;
            this.status = status;
            this.currentRecordLength = currentRecordLength;
            this.currentRecordTime = currentRecordTime;
            this.clips = clips;
        }

        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            if (parcel != null) {
                parcel.writeSerializable((Serializable)this.status);
                parcel.writeInt(this.currentRecordLength);
                parcel.writeLong(this.currentRecordTime);
                parcel.writeList((List)this.clips);
            }
        }
    }

    public enum Status
    {
        DELETE,
        IDLE,
        NONE,
        RECORDED,
        RECORDING,
        SELECTING;
    }

    private static class UIHandler extends Handler
    {
        private WeakReference<RecordingProgressBar> bar;

        UIHandler(final RecordingProgressBar recordingProgressBar) {
            super();
            this.bar = new WeakReference<RecordingProgressBar>(recordingProgressBar);
        }

        public void handleMessage(final Message message) {
            switch (message.what) {
                case 256: {
                    if (this.bar != null && this.bar.get() != null) {
                        this.bar.get().doBlinking = !this.bar.get().doBlinking;
                        this.bar.get().invalidate();
                        this.sendEmptyMessageDelayed(256, (long)RecordingProgressBar.BLINK_DURATION);
                        return;
                    }
                    break;
                }
            }
        }
    }
}