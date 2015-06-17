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

import com.mapia.R;
import com.mapia.custom.FontSettableTextView;

import java.util.ArrayList;
import java.util.List;


public class TagHistoryAdapter extends ArrayAdapter<String>
{
    private LayoutInflater mInflater;
    private TagHistory mTagHistory;

    public TagHistoryAdapter(final Context context, final int n, final ArrayList<String> list, final TagHistory mTagHistory) {
        super(context, n, (List)list);
        this.mTagHistory = mTagHistory;
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return super.getCount() + 1;
    }

    public String getItem(final int n) {
        if (n == 0) {
            return null;
        }
        return (String)super.getItem(n - 1);
    }

    public View getView(final int n, View view, final ViewGroup viewGroup) {
        if (n == 0) {
            view = this.mInflater.inflate(R.layout.tag_listview_item_header, viewGroup, false);
        }
        else {
            view = this.mInflater.inflate(R.layout.tag_listview_item, viewGroup, false);
        }
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtTag = (FontSettableTextView)view.findViewById(R.id.tag);
        viewHolder.txtTagHistory = (FontSettableTextView)view.findViewById(R.id.txtTagHistory);
        viewHolder.btnDeleteTag = view.findViewById(R.id.btnDeleteTag);
        viewHolder.divider = view.findViewById(R.id.taghistoryDivider);
        if (n == 0) {
            viewHolder.txtTag.setVisibility(View.INVISIBLE);
            viewHolder.txtTagHistory.setVisibility(View.VISIBLE);
            viewHolder.btnDeleteTag.setVisibility(View.INVISIBLE);
            viewHolder.divider.setVisibility(View.VISIBLE);
            return view;
        }
        viewHolder.txtTag.setTextColor(Color.parseColor("#252525"));
        viewHolder.txtTag.setText((CharSequence)this.getItem(n));
        viewHolder.txtTag.setVisibility(View.VISIBLE);
        viewHolder.txtTagHistory.setVisibility(View.INVISIBLE);
        viewHolder.divider.setVisibility(View.INVISIBLE);
        viewHolder.btnDeleteTag.setVisibility(View.VISIBLE);
        viewHolder.btnDeleteTag.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                TagHistoryAdapter.this.remove(TagHistoryAdapter.this.getItem(n));
                TagHistoryAdapter.this.notifyDataSetChanged();
                TagHistoryAdapter.this.mTagHistory.writeTagHistory();
                if (TagHistoryAdapter.this.getCount() == 1) {
                    return;
                }
            }
        });
        return view;
    }

    private static class ViewHolder
    {
        public View btnDeleteTag;
        public View divider;
        public FontSettableTextView txtTag;
        public FontSettableTextView txtTagHistory;
    }
}
