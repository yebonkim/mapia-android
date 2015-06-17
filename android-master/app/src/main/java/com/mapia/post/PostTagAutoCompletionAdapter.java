package com.mapia.post;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.mapia.R;
import com.mapia.custom.FontSettableTextView;
import com.mapia.myfeed.MapiaTextView;
import com.mapia.search.tag.SearchTagModel;

import java.util.ArrayList;
import java.util.List;

public class PostTagAutoCompletionAdapter extends ArrayAdapter<SearchTagModel>
{
    private ArrayList<SearchTagModel> arrayList;
    private LayoutInflater layoutinflater;
    private String mQuery;

    public PostTagAutoCompletionAdapter(final Context context, final ArrayList<SearchTagModel> arrayList) {
        super(context, Integer.MIN_VALUE, (List)arrayList);
        this.arrayList = arrayList;
        this.layoutinflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        TagViewHolder tag;
        if (inflate == null) {
            inflate = this.layoutinflater.inflate(R.layout.post_search_tag_autocomplete_row, (ViewGroup)null);
            tag = new TagViewHolder();
            tag.tag = (MapiaTextView)inflate.findViewById(R.id.tag);
            tag.tagButton = (LinearLayout)inflate.findViewById(R.id.fstar_tag_button);
            tag.tagCount = (FontSettableTextView)inflate.findViewById(R.id.fstar_tag_count);
            tag.padding = inflate.findViewById(R.id.padding);
            inflate.setTag((Object)tag);
        }
        else {
            tag = (TagViewHolder)inflate.getTag();
        }
        if (n == 0) {
            tag.padding.setVisibility(View.VISIBLE);
        }
        else {
            tag.padding.setVisibility(View.INVISIBLE);
        }
        final SearchTagModel searchTagModel = this.arrayList.get(n);
        tag.tag.setText((CharSequence)("#" + searchTagModel.getTag()));
        tag.tag.highlightTag(Color.parseColor("#384cd8"), this.mQuery);
        tag.tagCount.setText((CharSequence)("" + searchTagModel.getTagCount()));
        return inflate;
    }

    public void setQuery(final String mQuery) {
        this.mQuery = mQuery;
    }

    private static class TagViewHolder
    {
        public View padding;
        public MapiaTextView tag;
        public LinearLayout tagButton;
        public FontSettableTextView tagCount;
    }
}