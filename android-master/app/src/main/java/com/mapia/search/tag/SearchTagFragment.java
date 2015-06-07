package com.mapia.search.tag;

/**
 * Created by daehyun on 15. 6. 7..
 */

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mapia.R;
import com.mapia.common.BaseFragment;
import com.mapia.custom.FontSettableTextView;
import com.mapia.search.SearchFragment;

import java.util.ArrayList;

public class SearchTagFragment extends BaseFragment
{
//    private SearchTagAutoCompleteAdapter autoCompleteArrayList;
    private ArrayList<SearchTagModel> autoCompleteArrayList;
    private ListView autoCompleteListView;
//    private SearchTagAutoCompleteManager autoCompleteManager;
//    private SearchHistoryCacheWrapper cacheWrapper;
    private LinearLayout defaultView;
    private float endYPosition;
//    private SearchTagHistoryAdapter historyAdapter;
    private ArrayList<SearchTagModel> historyArrayList;
    private ListView historyListView;
//    private SearchTagHistoryManager historyManager;
    private View layout;
    private FontSettableTextView nodataTextView;
    private LinearLayout nodataView;
    private LinearLayout progress;
    private RelativeLayout progressView;
    private SearchFragment searchFragment;
    private float startYPosition;

    public SearchTagFragment() {
        super();
        this.defaultMenuBarShow = false;
    }

    private void checkSearchResultCount(final String s) {
//        this.mainActivity.makeRequest(QueryManager.makeSearchTagApiUrl(s, 1, 20), new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                if (jsonObject == null) {
//                    return;
//                }
//                try {
//                    if ("success".equals(jsonObject.getString("resultStatus"))) {
//                        if (jsonObject.getJSONObject("result").getInt("totalCount") == 0) {
//                            SearchTagFragment.this.showAutoCompleteNodata(true);
//                            SearchTagFragment.this.searchFragment.showKeyboard();
//                        }
//                        else {
//                            SearchTagFragment.this.openSearchResult(s);
//                        }
//                    }
//                    SearchTagFragment.this.delayHideProgress();
//                }
//                catch (Exception ex) {}
//            }
//        });
    }

    private void openSearchResult(final String s) {
        final Bundle arguments = new Bundle();
        arguments.putString("query", s);
        final SearchTagResultFragment searchTagResultFragment = new SearchTagResultFragment();
        searchTagResultFragment.setArguments(arguments);
        this.mainActivity.addFragment(searchTagResultFragment);
    }

    public void delayHideProgress() {
        new Handler().postDelayed((Runnable)new DelayHideProgress(), 300L);
    }

    public String getQuery() {
        return this.searchFragment.getQuery();
    }

    public void hideAutoCompleteNodata() {
        this.autoCompleteListView.setVisibility(View.VISIBLE);
        this.nodataView.setVisibility(View.INVISIBLE);
        this.nodataTextView.setVisibility(View.INVISIBLE);
        this.progressView.setVisibility(View.INVISIBLE);
    }

    public void hideProgressBar() {
        if (this.progressView == null || this.progress == null) {
            return;
        }
        ((AnimationDrawable)this.progress.getBackground()).stop();
        this.progressView.setVisibility(View.INVISIBLE);
    }

    public boolean isAutoCompleteNodata() {
        return this.nodataView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.historyArrayList = new ArrayList<SearchTagModel>();
//        this.historyAdapter = new SearchTagHistoryAdapter(this.mainActivity, this.searchFragment, this, this.historyArrayList, this.cacheWrapper);
//        this.historyManager = new SearchTagHistoryManager(this, this.historyAdapter, this.cacheWrapper);
//        this.autoCompleteArrayList = new ArrayList<SearchTagModel>();
//        this.autoCompleteAdapter = new SearchTagAutoCompleteAdapter(this.mainActivity, this.searchFragment, this.autoCompleteArrayList, this.cacheWrapper);
//        this.autoCompleteManager = new SearchTagAutoCompleteManager(this.mainActivity, this, this.autoCompleteAdapter);
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.layout = layoutInflater.inflate(R.layout.fragment_search_tag, viewGroup, false);
        this.defaultView = (LinearLayout)this.layout.findViewById(R.id.fst_default);
        ((ImageView)this.defaultView.findViewById(R.id.fst_nodata_image)).setImageResource(this.searchFragment.getNodataImageResourceIdArray()[(int)(Math.random() * 5.0)]);
        this.nodataView = (LinearLayout)this.layout.findViewById(R.id.fst_autocomplete_nodata);
        this.nodataTextView = (FontSettableTextView)this.layout.findViewById(R.id.fst_autocomplete_nodata_text);
        ((ImageView)this.nodataView.findViewById(R.id.fst_autocomplete_nodata_image)).setImageResource(this.searchFragment.getNodataImageResourceIdArray()[(int)(Math.random() * 5.0)]);
        this.progressView = (RelativeLayout)this.layout.findViewById(R.id.fst_progress);
        this.progress = (LinearLayout)this.layout.findViewById(R.id.fst_progressbar);
        (this.historyListView = (ListView)this.layout.findViewById(R.id.fst_history)).setDividerHeight(0);
        final View inflate = layoutInflater.inflate(R.layout.fragment_search_history_header, viewGroup, false);
        inflate.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, -2));
        this.historyListView.addHeaderView(inflate);
        final View inflate2 = layoutInflater.inflate(R.layout.fragment_search_history_footer, viewGroup, false);
        inflate2.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, -2));
        this.historyListView.addFooterView(inflate2, (Object)null, false);
        inflate2.findViewById(R.id.fshf_delete_button).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                SearchTagFragment.this.cacheWrapper.deleteTagAll();
//                SearchTagFragment.this.historyAdapter.clear();
//                SearchTagFragment.this.historyAdapter.notifyDataSetInvalidated();
                SearchTagFragment.this.showDefaultArea();
            }
        });
//        this.historyListView.setAdapter((ListAdapter)this.historyAdapter);
        this.historyListView.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 2: {
                        SearchTagFragment.this.dragFlag = true;
                        if (SearchTagFragment.this.firstDragFlag) {
                            SearchTagFragment.this.startYPosition = motionEvent.getY();
                            SearchTagFragment.this.firstDragFlag = false;
                            return false;
                        }
                        break;
                    }
                    case 1: {
                        SearchTagFragment.this.endYPosition = motionEvent.getY();
                        SearchTagFragment.this.firstDragFlag = true;
                        if (SearchTagFragment.this.dragFlag && SearchTagFragment.this.startYPosition < SearchTagFragment.this.endYPosition && SearchTagFragment.this.endYPosition - SearchTagFragment.this.startYPosition > 10.0f) {
                            SearchTagFragment.this.searchFragment.hideKeyboard();
                        }
                        SearchTagFragment.this.startYPosition = 0.0f;
                        SearchTagFragment.this.endYPosition = 0.0f;
                        return false;
                    }
                }
                return false;
            }
        });
        (this.autoCompleteListView = (ListView)this.layout.findViewById(R.id.fst_autocomplete)).setDividerHeight(0);
        final View inflate3 = layoutInflater.inflate(R.layout.fragment_search_autocomplete_header, viewGroup, false);
        inflate3.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, -2));
        this.autoCompleteListView.addHeaderView(inflate3);
        final View inflate4 = layoutInflater.inflate(R.layout.fragment_search_autocomplete_footer, viewGroup, false);
        inflate4.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, -2));
        this.autoCompleteListView.addFooterView(inflate4);
        this.autoCompleteListView.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 2: {
                        SearchTagFragment.this.dragFlag = true;
                        if (SearchTagFragment.this.firstDragFlag) {
                            SearchTagFragment.this.startYPosition = motionEvent.getY();
                            SearchTagFragment.this.firstDragFlag = false;
                            return false;
                        }
                        break;
                    }
                    case 1: {
                        SearchTagFragment.this.endYPosition = motionEvent.getY();
                        SearchTagFragment.this.firstDragFlag = true;
                        if (SearchTagFragment.this.dragFlag && SearchTagFragment.this.startYPosition < SearchTagFragment.this.endYPosition && SearchTagFragment.this.endYPosition - SearchTagFragment.this.startYPosition > 10.0f) {
                            SearchTagFragment.this.searchFragment.hideKeyboard();
                        }
                        SearchTagFragment.this.startYPosition = 0.0f;
                        SearchTagFragment.this.endYPosition = 0.0f;
                        return false;
                    }
                }
                return false;
            }
        });
        inflate4.findViewById(R.id.fsaf_search_button).setOnClickListener((View.OnClickListener) new View.OnClickListener() {
            public void onClick(final View view) {
                SearchTagFragment.this.showProgressBar();
                SearchTagFragment.this.searchFragment.hideKeyboard();
                SearchTagFragment.this.checkSearchResultCount(SearchTagFragment.this.searchFragment.getQuery());
//                SearchTagFragment.this.cacheWrapper.insertTag(SearchTagFragment.this.searchFragment.getQuery());
//                AceUtils.nClick(NClicks.SEARCH_LIST_ALL);
            }
        });
//        this.autoCompleteListView.setAdapter((ListAdapter)this.autoCompleteAdapter);
//        this.historyManager.show();
        return this.layout;
    }

//    public void setCacheWrapper(final SearchHistoryCacheWrapper cacheWrapper) {
//        this.cacheWrapper = cacheWrapper;
//    }

    public void setSearchFragment(final SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }

    public void showAutoComplete(final String s) {
        if (s.length() == 1) {
            this.showAutoCompleteArea();
        }
//        this.autoCompleteManager.show(s);
    }

    public void showAutoCompleteArea() {
        this.autoCompleteListView.setVisibility(View.VISIBLE);
        this.historyListView.setVisibility(View.INVISIBLE);
        this.defaultView.setVisibility(View.INVISIBLE);
        this.nodataView.setVisibility(View.INVISIBLE);
        this.progressView.setVisibility(View.INVISIBLE);
    }

    public void showAutoCompleteNodata(final boolean b) {
        int visibility = View.VISIBLE;
        this.autoCompleteListView.setVisibility(View.INVISIBLE);
        this.nodataView.setVisibility(View.VISIBLE);
        final FontSettableTextView nodataTextView = this.nodataTextView;
        if (!b) {
            visibility = View.INVISIBLE;
        }
        nodataTextView.setVisibility(visibility);
        this.progressView.setVisibility(View.INVISIBLE);
    }

    public void showDefaultArea() {
        this.defaultView.setVisibility(View.VISIBLE);
        this.nodataView.setVisibility(View.INVISIBLE);
        this.historyListView.setVisibility(View.INVISIBLE);
        this.autoCompleteListView.setVisibility(View.INVISIBLE);
        this.progressView.setVisibility(View.INVISIBLE);
    }

    public void showHistory() {
        this.showHistoryArea();
//        this.historyManager.show();
    }

    public void showHistoryArea() {
        this.historyListView.setVisibility(View.VISIBLE);
        this.autoCompleteListView.setVisibility(View.INVISIBLE);
        this.defaultView.setVisibility(View.INVISIBLE);
        this.nodataView.setVisibility(View.INVISIBLE);
        this.progressView.setVisibility(View.INVISIBLE);
    }

    public void showProgressBar() {
        if (this.progressView == null || this.progress == null) {
            return;
        }
        this.progressView.setVisibility(View.VISIBLE);
        ((AnimationDrawable)this.progress.getBackground()).start();
    }

    private class DelayHideProgress implements Runnable
    {
        @Override
        public void run() {
            SearchTagFragment.this.hideProgressBar();
        }
    }
}