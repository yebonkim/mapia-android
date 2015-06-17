package com.mapia.post;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.view.View;

import com.mapia.R;
import com.mapia.custom.FontSettableButton;
import com.mapia.custom.FontSettableTextView;


public class MapiaConfirmDialog extends Dialog
{
    private FontSettableButton mBtnLeft;
    private FontSettableButton mBtnRight;
    private FontSettableTextView mTxtMessage;

    public MapiaConfirmDialog(final Context context) {
        super(context);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.mapia_confirm_dialog);
        this.init();
    }

    public MapiaConfirmDialog(final Context context, final String message, final String left, final String right) {
        super(context);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.mapia_confirm_dialog);
        this.init();
        this.setMessage(message);
        this.setLeft(left);
        this.setRight(right);
    }

    private void init() {
        this.mTxtMessage = (FontSettableTextView)this.findViewById(R.id.message);
        this.mBtnLeft = (FontSettableButton)this.findViewById(R.id.btnLeft);
        this.mBtnRight = (FontSettableButton)this.findViewById(R.id.btnRight);
    }

    public void setLeft(final SpannableString text) {
        this.mBtnLeft.setText((CharSequence)text);
    }

    public void setLeft(final String text) {
        this.mBtnLeft.setText((CharSequence)text);
    }

    public void setLeftBtnClick(final View.OnClickListener onClickListener) {
        this.mBtnLeft.setOnClickListener(onClickListener);
    }

    public void setMessage(final String text) {
        this.mTxtMessage.setText((CharSequence)text);
    }

    public void setRight(final SpannableString text) {
        this.mBtnRight.setText((CharSequence)text);
    }

    public void setRight(final String text) {
        this.mBtnRight.setText((CharSequence)text);
    }

    public void setRightBtnClick(final View.OnClickListener onClickListener) {
        this.mBtnRight.setOnClickListener(onClickListener);
    }
}