package com.mapia.custom;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mapia.R;

public class CustomProgressDialog extends Dialog
{
    public CustomProgressDialog(final Context context) {
        super(context, R.style.CustomProgressDialog);
    }

    public static CustomProgressDialog show(final Context context, final CharSequence charSequence, final CharSequence charSequence2) {
        return show(context, charSequence, charSequence2, false);
    }

    public static CustomProgressDialog show(final Context context, final CharSequence charSequence, final CharSequence charSequence2, final boolean b) {
        return show(context, charSequence, charSequence2, b, false, null);
    }

    public static CustomProgressDialog show(final Context context, final CharSequence charSequence, final CharSequence charSequence2, final boolean b, final boolean b2) {
        return show(context, charSequence, charSequence2, b, b2, null);
    }

    public static CustomProgressDialog show(final Context context, final CharSequence title, final CharSequence charSequence, final boolean b, final boolean cancelable, final DialogInterface.OnCancelListener onCancelListener) {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(context);
        customProgressDialog.setTitle(title);
        customProgressDialog.setCancelable(cancelable);
        customProgressDialog.setOnCancelListener(onCancelListener);
        customProgressDialog.addContentView((View)new ProgressBar(context), new ViewGroup.LayoutParams(-2, -2));
        customProgressDialog.show();
        return customProgressDialog;
    }
}