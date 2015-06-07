package com.mapia.search.pic;

/**
 * Created by daehyun on 15. 6. 7..
 */

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.mapia.R;
import com.mapia.api.model.SearchPic;
import com.mapia.common.BaseFragment;
import com.mapia.util.FontUtils;

import java.util.ArrayList;


public class SearchPicResultFragment extends BaseFragment
{
    private SearchPicResultAdapter adapter;
    private ArrayList<SearchPic> arrayList;
    private boolean isListViewLocked;
    private RelativeLayout layout;
    private MultiColumnListView listView;
    private SearchPicResultManager manager;
    private int[] nodataImageResourceIdArray;
    private LinearLayout progressBar;
    private String query;
    private ImageView symbol;
    private TextView tag;

    public SearchPicResultFragment() {
        super();
        this.isListViewLocked = true;
        this.nodataImageResourceIdArray = new int[] { 2130837735, 2130837736, 2130837737, 2130837738, 2130837739 };
        this.defaultMenuBarShow = false;
    }

    private void hideProgressBar() {
        if (this.progressBar == null) {
            return;
        }
        ((AnimationDrawable)this.progressBar.getBackground()).stop();
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        if (this.progressBar == null) {
            return;
        }
        this.progressBar.setVisibility(View.VISIBLE);
        ((AnimationDrawable)this.progressBar.getBackground()).start();
    }

    public void afterShow(final boolean b) {
        if (b) {
            this.isListViewLocked = true;
            this.hideProgressBar();
            if (this.arrayList.size() > 16) {
                this.symbol.setVisibility(View.VISIBLE);
            }
            return;
        }
        this.isListViewLocked = false;
        this.hideProgressBar();
    }

//    @Override
    protected void onAnimationEnded() {
        this.manager.show(true, this.query);
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.query = this.getArguments().getString("query");
        this.arrayList = new ArrayList<SearchPic>();
        this.adapter = new SearchPicResultAdapter(this.mainActivity, this.arrayList);
        this.manager = new SearchPicResultManager(this.mainActivity, this.adapter, this);
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.layout = (RelativeLayout)layoutInflater.inflate(R.layout.fragment_search_pic_result, viewGroup, false);
        this.layout.findViewById(R.id.fspr_prev).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                SearchPicResultFragment.this.mainActivity.onBackPressed();
            }
        });
        (this.tag = (TextView)this.layout.findViewById(R.id.fspr_tag)).setTypeface(FontUtils.getNanumBold());
        this.tag.setText((CharSequence)this.query);
        this.listView = (MultiColumnListView)this.layout.findViewById(R.id.fspr_listview);
        final View inflate = layoutInflater.inflate(R.layout.fragment_common_footer, viewGroup, false);
        inflate.setLayoutParams((ViewGroup.LayoutParams)new PLA_AbsListView.LayoutParams(-1, -2));
        this.listView.addFooterView(inflate, null, false);
        this.progressBar = (LinearLayout)inflate.findViewById(R.id.fcf_progressbar);
        this.symbol = (ImageView)inflate.findViewById(R.id.fcf_symbol);
        this.listView.setAdapter((ListAdapter)this.adapter);
        this.listView.setOnScrollListener((PLA_AbsListView.OnScrollListener)new PLA_AbsListView.OnScrollListener() {
            @Override
            public void onScroll(final PLA_AbsListView pla_AbsListView, final int n, final int n2, final int n3) {
                if (!SearchPicResultFragment.this.isListViewLocked && n >= n3 - n2 && n3 > 0) {
                    SearchPicResultFragment.this.isListViewLocked = true;
                    SearchPicResultFragment.this.showProgressBar();
                    SearchPicResultFragment.this.manager.show(false, SearchPicResultFragment.this.query);
                }
            }

            @Override
            public void onScrollStateChanged(final PLA_AbsListView pla_AbsListView, final int n) {
            }
        });
        this.layout.findViewById(R.id.fspr_header).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                SearchPicResultFragment.this.listView.setSelectionFromTop(0, 0);
            }
        });
        this.showProgressBar();
        return (View)this.layout;
    }

//    @Override
    public void setConnectionCondition(final boolean b) {
        if (!b) {
            this.hideProgressBar();
        }
    }

    public void showNoData() {
        ((ImageView)this.layout.findViewById(R.id.fspr_nodata)).setVisibility(View.VISIBLE);
    }

    public void showNoDataText() {
        final LinearLayout linearLayout = (LinearLayout)this.layout.findViewById(R.id.fspr_nodata_text);
        ((ImageView)linearLayout.findViewById(R.id.fspr_nodata_image)).setImageResource(this.nodataImageResourceIdArray[(int)(Math.random() * 5.0)]);
        linearLayout.setVisibility(View.VISIBLE);
        this.listView.setVisibility(View.INVISIBLE);
    }
}