package com.mapia.endpage;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.makeramen.RoundedImageView;
import com.mapia.R;
import com.mapia.common.CompatiblityUtility;
import com.mapia.common.Thumb;
import com.mapia.custom.FontSettableTextView;
import com.mapia.mention.MentionModel;
import com.mapia.util.ThumbUtils;

import java.util.ArrayList;
import java.util.List;

public class MentionListviewAdapter extends ArrayAdapter<MentionModel>
{
    public MentionListviewAdapter(final Context context, final int n, final ArrayList<MentionModel> list) {
        super(context, n, (List)list);
    }

    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        ViewHolder tag;
        if (inflate == null) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mention_listview_item, viewGroup, false);
            tag = new ViewHolder();
            tag.btnProfile = inflate.findViewById(R.id.btnProfile);
            tag.imgProfile = (RoundedImageView)inflate.findViewById(R.id.imgProfile);
            tag.txtNickName = (FontSettableTextView)inflate.findViewById(R.id.txtNickname);
            inflate.setTag((Object)tag);
        }
        else {
            tag = (ViewHolder)inflate.getTag();
        }
        final MentionModel mentionModel = (MentionModel)this.getItem(n);
        tag.txtNickName.setText((CharSequence)mentionModel.nickname);
        CompatiblityUtility.setRoundedImageView(tag.imgProfile, mentionModel.profileUrl + ThumbUtils.getSuffix(Thumb.FOLLOW_ICON));
        return inflate;
    }

    private class ViewHolder
    {
        View btnProfile;
        RoundedImageView imgProfile;
        FontSettableTextView txtNickName;
    }
}