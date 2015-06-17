package com.mapia.myfeed.grid;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.mapia.R;
import com.mapia.api.model.FeedPic;
import com.mapia.common.BaseFragment;
import com.mapia.common.CommonConstants;
import com.mapia.myfeed.MyfeedFragment;
import com.mapia.network.NetworkStatusManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class MyfeedPicGridFragment extends BaseFragment
{
    private MyfeedPicGridAdapter adapter;
    private ArrayList<FeedPic> arrayList;
    private int currentItemCount;
    private int fixedItemCount;
    private boolean isListViewLocked;
    private RelativeLayout layout;
    private MultiColumnListView listView;
    private MyfeedPicGridManager manager;
    private MyfeedFragment myfeedFragment;
    private ImageView noData;
    private LinearLayout progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MyfeedPicGridFragment() {
        super();
        this.isListViewLocked = true;
        this.currentItemCount = 0;
        this.fixedItemCount = 999999;
    }

    private int getListOrder(final long n) {
        for (int i = 0; i < this.arrayList.size(); ++i) {
            if (this.arrayList.get(i).getPicNo() == n) {
                return i;
            }
        }
        return -1;
    }

    private ArrayList<Integer> getListOrderList(final long n) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = this.arrayList.size() - 1; i > -1; --i) {
            if (this.arrayList.get(i).getPicker().getMemberNo() == n) {
                list.add(i);
            }
        }
        return list;
    }

    private void hideProgressBar() {
        ((AnimationDrawable)this.progressBar.getBackground()).stop();
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
        ((AnimationDrawable)this.progressBar.getBackground()).start();
    }

    public void afterShow(final boolean b, final int n) {
        if (b) {
            this.isListViewLocked = true;
            this.fixedItemCount = this.currentItemCount + n;
        }
        else {
            this.isListViewLocked = false;
        }
        this.hideProgressBar();
    }

    public void hideLittleData() {
        this.noData.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.arrayList = new ArrayList<FeedPic>();
        this.adapter = new MyfeedPicGridAdapter(this.mainActivity, this.arrayList);
        this.manager = new MyfeedPicGridManager(this.mainActivity, this.adapter, this);
//        this.mainApplication.getEventBus().register(this);
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.layout = (RelativeLayout)layoutInflater.inflate(R.layout.fragment_myfeed_grid, viewGroup, false);
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.layout.findViewById(R.id.fmg_swiperefreshlayout);
        this.swipeRefreshLayout.setColorSchemeColors(CommonConstants.SWIPE_COLOR_SCHEME);
        this.swipeRefreshLayout.setRefreshing(false);
        this.swipeRefreshLayout.setEnabled(true);
        this.swipeRefreshLayout.setProgressViewOffset(false, CommonConstants.SWIPE_START_POINT_MYFEED_THREE_COLUMN, CommonConstants.SWIPE_END_POINT_MYFEED_THREE_COLUMN);
        this.swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RefreshAsyncTask().execute(new Void[0]);
            }
        });
        this.listView = (MultiColumnListView)this.layout.findViewById(R.id.fmg_multilistview);
        final View inflate = layoutInflater.inflate(R.layout.fragment_myfeed_grid_header, viewGroup, false);
        inflate.setLayoutParams((ViewGroup.LayoutParams)new PLA_AbsListView.LayoutParams(-1, -2));
        this.listView.addHeaderView(inflate);
        final View inflate2 = layoutInflater.inflate(R.layout.fragment_common_footer, viewGroup, false);
        inflate2.setLayoutParams((ViewGroup.LayoutParams)new PLA_AbsListView.LayoutParams(-1, -2));
        this.listView.addFooterView(inflate2, null, false);
        this.progressBar = (LinearLayout)inflate2.findViewById(R.id.fcf_progressbar);
        this.listView.setAdapter((ListAdapter)this.adapter);
        this.listView.setOnScrollListener((PLA_AbsListView.OnScrollListener)new PLA_AbsListView.OnScrollListener() {
            int lastFirstVisibleItem = 0;

            @Override
            public void onScroll(final PLA_AbsListView pla_AbsListView, final int n, final int n2, final int n3) {
                if (pla_AbsListView.getId() == MyfeedPicGridFragment.this.listView.getId()) {
                    final int firstVisiblePosition = MyfeedPicGridFragment.this.listView.getFirstVisiblePosition();
                    if (firstVisiblePosition > this.lastFirstVisibleItem && n + n2 < MyfeedPicGridFragment.this.fixedItemCount) {
                        MyfeedPicGridFragment.this.mainActivity.hideMenuBar(true);
                        MyfeedPicGridFragment.this.myfeedFragment.hideHeader(true);
                    }
                    else if (firstVisiblePosition < this.lastFirstVisibleItem) {
                        MyfeedPicGridFragment.this.mainActivity.showMenuBar(true);
                        MyfeedPicGridFragment.this.myfeedFragment.showHeader(true);
                    }
                    this.lastFirstVisibleItem = firstVisiblePosition;
                }
                if (!MyfeedPicGridFragment.this.isListViewLocked && n >= n3 - n2 && n3 > 0) {
                    MyfeedPicGridFragment.this.isListViewLocked = true;
                    MyfeedPicGridFragment.this.showProgressBar();
                    MyfeedPicGridFragment.this.manager.show(false);
                }
                MyfeedPicGridFragment.this.currentItemCount = n3;
                if (n + n2 >= MyfeedPicGridFragment.this.fixedItemCount) {
                    MyfeedPicGridFragment.this.mainActivity.showMenuBar(true);
                }
            }

            @Override
            public void onScrollStateChanged(final PLA_AbsListView pla_AbsListView, final int n) {
            }
        });
        this.noData = (ImageView)this.layout.findViewById(R.id.fmg_nodata);
        if (NetworkStatusManager.getIsAvailableNetwork()) {
            this.manager.show(true);
            this.showProgressBar();
        }
        else {
            this.swipeRefreshLayout.setEnabled(false);
        }
        return (View)this.layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        this.mainApplication.getEventBus().unregister(this);
    }

    @Override
    public void onEvent(final Map<String, Object> map) {
//        if ("edp".equals(map.get("event"))) {
//            this.processDeletePost(Long.parseLong(map.get("picNo")));
//        }
//        if ("ehp".equals(map.get("event"))) {
//            this.processHidePost(Long.parseLong(map.get("picNo")));
//        }
//        if ("ebm".equals(map.get("event"))) {
//            this.processBlockMember(Long.parseLong(map.get("memberNo")));
//        }
    }

    public void processBlockMember(final long n) {
        final Iterator<Integer> iterator = this.getListOrderList(n).iterator();
        while (iterator.hasNext()) {
            this.arrayList.remove((int)iterator.next());
        }
    }

    public void processDeletePost(final long n) {
        final int listOrder = this.getListOrder(n);
        if (listOrder > -1) {
            this.arrayList.remove(listOrder);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void processHidePost(final long n) {
        final int listOrder = this.getListOrder(n);
        if (listOrder > -1) {
            this.arrayList.remove(listOrder);
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshList() {
        this.isListViewLocked = true;
        this.fixedItemCount = 999999;
        this.arrayList.clear();
        this.adapter.clear();
        this.adapter.notifyDataSetInvalidated();
        this.showProgressBar();
        this.manager.show(true);
        this.listView.post((Runnable)new Runnable() {
            @Override
            public void run() {
                MyfeedPicGridFragment.this.myfeedFragment.showHeader(true);
                MyfeedPicGridFragment.this.mainActivity.showMenuBar(true, true);
            }
        });
    }

    @Override
    public void scrollToTop() {
//        this.listView.setSelectionFromTop(0, 0);
    }

    @Override
    public void setConnectionCondition(final boolean b) {
        if (!b) {
            this.hideProgressBar();
            this.swipeRefreshLayout.setEnabled(false);
            return;
        }
        this.swipeRefreshLayout.setEnabled(true);
    }

    public void setMyfeedFragment(final MyfeedFragment myfeedFragment) {
        this.myfeedFragment = myfeedFragment;
    }

    public void showLittleData() {
        this.noData.setVisibility(View.VISIBLE);
    }

    private class RefreshAsyncTask extends AsyncTask<Void, Void, Void>
    {
        protected Void doInBackground(final Void... array) {
            try {
                Thread.sleep(1000L);
                return null;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(final Void void1) {
            super.onPostExecute(void1);
            MyfeedPicGridFragment.this.isListViewLocked = true;
            MyfeedPicGridFragment.this.fixedItemCount = 999999;
            MyfeedPicGridFragment.this.arrayList.clear();
            MyfeedPicGridFragment.this.adapter.clear();
            MyfeedPicGridFragment.this.adapter.notifyDataSetInvalidated();
            MyfeedPicGridFragment.this.manager.show(true);
            MyfeedPicGridFragment.this.listView.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    MyfeedPicGridFragment.this.swipeRefreshLayout.setRefreshing(false);
                    MyfeedPicGridFragment.this.myfeedFragment.showHeader(true);
                    MyfeedPicGridFragment.this.mainActivity.showMenuBar(true, true);
                }
            });
            MyfeedPicGridFragment.this.myfeedFragment.refreshPage(0);
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}