package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mapia.R;
import com.mapia.util.FontUtils;


public class VideoDeleteDialog extends Dialog
{
    private OnClickConfirmListener onClickConfirmListener;

    public VideoDeleteDialog(final Context context) {
        super(context);
        this.onClickConfirmListener = (OnClickConfirmListener)new DummyListener();
    }

    public VideoDeleteDialog(final Context context, final int n) {
        super(context, n);
        this.onClickConfirmListener = (OnClickConfirmListener)new DummyListener();
    }

    public VideoDeleteDialog(final Context context, final OnClickConfirmListener onClickConfirmListener) {
        super(context);
        this.onClickConfirmListener = (OnClickConfirmListener)new DummyListener();
        if (onClickConfirmListener != null) {
            this.onClickConfirmListener = onClickConfirmListener;
        }
    }

    protected VideoDeleteDialog(final Context context, final boolean b, final DialogInterface.OnCancelListener dialogInterfaceOnCancelListener) {
        super(context, b, dialogInterfaceOnCancelListener);
        this.onClickConfirmListener = (OnClickConfirmListener)new DummyListener();
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
//        this.getWindow().setBackgroundDrawableResource(17170445);
        this.setContentView(R.layout.video_delete_alert);
        final TextView textView = (TextView)this.findViewById(R.id.video_delete_dialog_text_1);
        final TextView textView2 = (TextView)this.findViewById(R.id.video_delete_dialog_text_2);
        final TextView textView3 = (TextView)this.findViewById(R.id.video_delete_dialog_text_3);
        final TextView textView4 = (TextView)this.findViewById(R.id.video_delete_dialog_cancel);
        final TextView textView5 = (TextView)this.findViewById(R.id.video_delete_dialog_confirm);
        textView.setTypeface(FontUtils.getNanumRegular());
        textView2.setTypeface(FontUtils.getNanumRegular());
        textView3.setTypeface(FontUtils.getNanumRegular());
        textView4.setTypeface(FontUtils.getNanumRegular());
        textView5.setTypeface(FontUtils.getNanumRegular());
        textView4.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                VideoDeleteDialog.this.dismiss();
            }
        });
        textView5.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.CAMERA_VIDEO_DELETE);
                VideoDeleteDialog.this.onClickConfirmListener.onClickConfirm();
                VideoDeleteDialog.this.dismiss();
            }
        });
    }

    private class DummyListener implements OnClickConfirmListener
    {
        @Override
        public void onClickConfirm() {
        }
    }

    public interface OnClickConfirmListener
    {
        void onClickConfirm();
    }
}