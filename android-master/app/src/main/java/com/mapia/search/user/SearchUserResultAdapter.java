package com.mapia.search.user;

/**
 * Created by daehyun on 15. 6. 6..
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapia.MainActivity;
import com.mapia.sns.ProfileFragment;
import com.mapia.R;
import com.mapia.api.model.SearchUser;
import com.mapia.custom.UrlSetableRoundedImageView;
import com.mapia.util.CollectionUtils;
import com.mapia.util.FontUtils;

//import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class SearchUserResultAdapter extends ArrayAdapter<SearchUser>
{
    private ArrayList<SearchUser> arrayList;
    private LayoutInflater layoutinflater;
    private MainActivity mainActivity;

    public SearchUserResultAdapter(final MainActivity mainActivity, final ArrayList<SearchUser> arrayList) {
        super((Context)mainActivity, Integer.MIN_VALUE, (List)arrayList);
        this.arrayList = arrayList;
        this.mainActivity = mainActivity;
        this.layoutinflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void openProfile(final long n) {
        final Bundle arguments = new Bundle();
        arguments.putLong("memberNo", n);
        final ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(arguments);
//        this.mainActivity.addFragment(profileFragment);
    }

    private void openProfileAlbum(final long n, final String s) {
//        if (n == 0L || StringUtils.isBlank(s)) {
//            return;
//        }
        final Bundle arguments = new Bundle();
        arguments.putLong("memberNo", n);
        arguments.putString("tag", s);
//        final ProfileAlbumFragment profileAlbumFragment = new ProfileAlbumFragment();
//        profileAlbumFragment.setArguments(arguments);
//        this.mainActivity.addFragment(profileAlbumFragment);
    }

    public ArrayList<SearchUser> getArrayList() {
        return this.arrayList;
    }

    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        ViewHolder tag;
        if (inflate == null) {
            inflate = this.layoutinflater.inflate(R.layout.fragment_search_user_result_row, (ViewGroup)null);
            tag = new ViewHolder();
            tag.image = (UrlSetableRoundedImageView)inflate.findViewById(R.id.fsurr_icon);
            tag.nameButton = (LinearLayout)inflate.findViewById(R.id.fsurr_name_button);
            tag.name = (TextView)inflate.findViewById(R.id.fsurr_name);
            tag.tag = (TextView)inflate.findViewById(R.id.fsurr_tag);
            tag.tagArea = (LinearLayout)inflate.findViewById(R.id.fsurr_tag_area);
            tag.name.setTypeface(FontUtils.getNanumBold());
            tag.tag.setTypeface(FontUtils.getNanumRegular());
            inflate.setTag((Object)tag);
        }
        else {
            tag = (ViewHolder)inflate.getTag();
        }
        final SearchUser searchUser = this.arrayList.get(n);
        tag.image.setImageResource(R.drawable.common_noneprofile_ss);
//        if (StringUtils.isNotBlank(searchUser.getImageUrl())) {
//            tag.image.setImageUrl((Context)this.mainActivity, searchUser.getImageUrl() + ThumbUtils.getSuffix(Thumb.SEARCH_ROW_ICON));
//        }
        tag.image.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                SearchUserResultAdapter.this.openProfile(searchUser.getMemberNo());
            }
        });
        tag.name.setText((CharSequence)searchUser.getNickname());
        tag.nameButton.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                SearchUserResultAdapter.this.openProfile(searchUser.getMemberNo());
            }
        });
        if (CollectionUtils.isNotEmpty(searchUser.getTags())) {
            final String body = searchUser.getTags().get(0).toString();//.getBody();
            tag.tag.setText((CharSequence)("#" + body));
            tag.tag.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
                    SearchUserResultAdapter.this.openProfileAlbum(searchUser.getMemberNo(), body);
                }
            });
            tag.tagArea.setVisibility(View.INVISIBLE);
            return inflate;
        }
        tag.tag.setText((CharSequence)"");
        tag.tag.setOnClickListener((View.OnClickListener)null);
        tag.tagArea.setVisibility(View.VISIBLE);
        return inflate;
    }

    private static class ViewHolder
    {
        public UrlSetableRoundedImageView image;
        public TextView name;
        public LinearLayout nameButton;
        public TextView tag;
        public LinearLayout tagArea;
    }
}