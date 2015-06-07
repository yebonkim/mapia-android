package com.mapia.search.user;

/**
 * Created by daehyun on 15. 6. 6..
 */

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapia.R;
import com.mapia.api.model.SearchUser;
import com.mapia.common.BaseFragment;
import com.mapia.util.FontUtils;

import java.util.ArrayList;


public class SearchUserResultFragment extends BaseFragment
{
    private SearchUserResultAdapter adapter;
    private ArrayList<SearchUser> arrayList;
    private boolean isListViewLocked;
    private RelativeLayout layout;
    private ListView listView;
    private SearchUserResultManager manager;
    private int[] nodataImageResourceIdArray;
    private LinearLayout progressBar;
    private String query;
    private ImageView symbol;
    private TextView tag;

    public SearchUserResultFragment() {
        super();
        this.isListViewLocked = true;
        this.nodataImageResourceIdArray = new int[] { 2130837735, 2130837736, 2130837737, 2130837738, 2130837739 };
        this.defaultMenuBarShow = false;
    }

    private void hideProgressBar() {
        ((AnimationDrawable)this.progressBar.getBackground()).stop();
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
        ((AnimationDrawable)this.progressBar.getBackground()).start();
    }

    public void afterShow(final boolean b) {
        if (b) {
            this.isListViewLocked = true;
            this.hideProgressBar();
            this.symbol.setVisibility(View.VISIBLE);
            return;
        }
        this.isListViewLocked = false;
        this.hideProgressBar();
    }


    protected void onAnimationEnded() {
        this.manager.show(true, this.query);
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.query = this.getArguments().getString("query");
        this.arrayList = new ArrayList<SearchUser>();
        this.adapter = new SearchUserResultAdapter(this.mainActivity, this.arrayList);
        this.manager = new SearchUserResultManager(this.mainActivity, this.adapter, this);
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.layout = (RelativeLayout)layoutInflater.inflate(R.layout.fragment_search_user_result, viewGroup, false);
        this.layout.findViewById(R.id.fsur_prev).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                SearchUserResultFragment.this.mainActivity.onBackPressed();
            }
        });
        (this.tag = (TextView)this.layout.findViewById(R.id.fsur_tag)).setTypeface(FontUtils.getNanumRegular());
        this.tag.setText((CharSequence)this.query);
        (this.listView = (ListView)this.layout.findViewById(R.id.fsur_listview)).setDividerHeight(0);
        final View inflate = layoutInflater.inflate(R.layout.fragment_search_result_header, viewGroup, false);
        inflate.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, -2));
        this.listView.addHeaderView(inflate);
        final View inflate2 = layoutInflater.inflate(R.layout.fragment_common_footer, viewGroup, false);
        inflate2.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, -2));
        this.listView.addFooterView(inflate2, (Object)null, false);
        this.progressBar = (LinearLayout)inflate2.findViewById(R.id.fcf_progressbar);
        this.symbol = (ImageView)inflate2.findViewById(R.id.fcf_symbol);
        this.listView.setAdapter((ListAdapter)this.adapter);
        this.listView.setOnScrollListener((AbsListView.OnScrollListener)new AbsListView.OnScrollListener() {
            public void onScroll(final AbsListView absListView, final int n, final int n2, final int n3) {
                if (!SearchUserResultFragment.this.isListViewLocked && n >= n3 - n2 && n3 > 0) {
                    SearchUserResultFragment.this.isListViewLocked = true;
                    SearchUserResultFragment.this.showProgressBar();
                    SearchUserResultFragment.this.manager.show(false, SearchUserResultFragment.this.query);
                }
            }

            public void onScrollStateChanged(final AbsListView absListView, final int n) {
            }
        });
        this.showProgressBar();
        return (View)this.layout;
    }


    public void setConnectionCondition(final boolean b) {
        if (!b) {
            this.hideProgressBar();
        }
    }

    public void showNoData() {
        final LinearLayout linearLayout = (LinearLayout)this.layout.findViewById(R.id.fsur_nodata);
        ((ImageView)linearLayout.findViewById(R.id.fsur_nodata_image)).setImageResource(this.nodataImageResourceIdArray[(int)(Math.random() * 5.0)]);
        linearLayout.setVisibility(View.VISIBLE);
        this.listView.setVisibility(View.INVISIBLE);
    }
}