package com.mapia.setting;

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

public class MapiaOneBtnDialog extends Dialog
{
    private FontSettableButton mBtnMain;
    private FontSettableTextView mTxtMessage;

    public MapiaOneBtnDialog(final Context context) {
        super(context);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.mapia_onebtn_dialog);
        this.init();
    }

    public MapiaOneBtnDialog(final Context context, final String message, final String btn) {
        super(context);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.mapia_onebtn_dialog);
        this.init();
        this.setMessage(message);
        this.setBtn(btn);
    }

    private void init() {
        this.mTxtMessage = (FontSettableTextView)this.findViewById(R.id.message);
        this.mBtnMain = (FontSettableButton)this.findViewById(R.id.btnMain);
    }

    public void setBtn(final String text) {
        this.mBtnMain.setText((CharSequence)text);
    }

    public void setBtnClick(final View.OnClickListener onClickListener) {
        this.mBtnMain.setOnClickListener(onClickListener);
    }

    public void setMessage(final SpannableString text) {
        this.mTxtMessage.setText((CharSequence)text);
    }

    public void setMessage(final String text) {
        this.mTxtMessage.setText((CharSequence)text);
    }
}