package com.mapia.search.tag;

/**
 * Created by daehyun on 15. 6. 7..
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
import com.mapia.R;
import com.mapia.api.model.SearchTag;
import com.mapia.util.FontUtils;

import java.util.ArrayList;
import java.util.List;


public class SearchTagResultAdapter extends ArrayAdapter<SearchTag>
{
    private ArrayList<SearchTag> arrayList;
    private LayoutInflater layoutinflater;
    private MainActivity mainActivity;

    public SearchTagResultAdapter(final MainActivity mainActivity, final ArrayList<SearchTag> arrayList) {
        super((Context)mainActivity, Integer.MIN_VALUE, (List)arrayList);
        this.arrayList = arrayList;
        this.mainActivity = mainActivity;
        this.layoutinflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void openTaggallery(final String s) {
        final Bundle arguments = new Bundle();
        arguments.putString("tag", s);
//        final TagGalleryFragment tagGalleryFragment = new TagGalleryFragment();
//        tagGalleryFragment.setArguments(arguments);
//        this.mainActivity.addFragment(tagGalleryFragment);
    }

    public ArrayList<SearchTag> getArrayList() {
        return this.arrayList;
    }

    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        TagViewHolder tag;
        if (inflate == null) {
            inflate = this.layoutinflater.inflate(R.layout.fragment_search_tag_result_row, (ViewGroup)null);
            tag = new TagViewHolder();
            tag.tag = (TextView)inflate.findViewById(R.id.fstrr_tag);
            tag.tagButton = (LinearLayout)inflate.findViewById(R.id.fstrr_tag_button);
            tag.tagCount = (TextView)inflate.findViewById(R.id.fstrr_tag_count);
            tag.tag.setTypeface(FontUtils.getNanumRegular());
            tag.tagCount.setTypeface(FontUtils.getNanumRegular());
            inflate.setTag((Object)tag);
        }
        else {
            tag = (TagViewHolder)inflate.getTag();
        }
        final SearchTag searchTag = this.arrayList.get(n);
        tag.tag.setText((CharSequence)("#" + searchTag.getTag()));
        tag.tagButton.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                SearchTagResultAdapter.this.openTaggallery(searchTag.getTag());
            }
        });
        tag.tagCount.setText((CharSequence)("" + searchTag.getDocumentCount()));
        return inflate;
    }

    private static class TagViewHolder
    {
        public TextView tag;
        public LinearLayout tagButton;
        public TextView tagCount;
    }
}