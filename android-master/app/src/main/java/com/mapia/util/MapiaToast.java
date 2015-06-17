package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapia.R;
import com.mapia.custom.FontSettableTextView;

public class MapiaToast
{
    public static void show(final Activity activity, final String text, final int duration) {
        if (TextUtils.isEmpty((CharSequence) text)) {
            return;
        }
        final View inflate = activity.getLayoutInflater().inflate(R.layout.layout_toast, (ViewGroup)null);
        ((FontSettableTextView)inflate.findViewById(R.id.txtToast)).setText((CharSequence)text);
        final Toast toast = new Toast((Context)activity);
        toast.setGravity(16, 0, 0);
        toast.setDuration(duration);
        toast.setView(inflate);
        toast.show();
    }
}