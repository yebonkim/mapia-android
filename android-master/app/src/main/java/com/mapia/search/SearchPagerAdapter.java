package com.mapia.search;

/**
 * Created by daehyun on 15. 6. 6..
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mapia.MainActivity;
import com.mapia.search.pic.SearchPicFragment;
import com.mapia.search.tag.SearchTagFragment;
import com.mapia.search.user.SearchUserFragment;


public class SearchPagerAdapter extends FragmentStatePagerAdapter
{
//    private SearchHistoryCacheWrapper cacheWrapper;
    private SearchFragment searchFragment;
    private SearchPicFragment searchPicFragment;
    private SearchTagFragment searchTagFragment;
    private SearchUserFragment searchUserFragment;

    public SearchPagerAdapter(final MainActivity mainActivity, final SearchFragment searchFragment) {
        super(searchFragment.getChildFragmentManager());
        this.searchTagFragment = null;
        this.searchPicFragment = null;
        this.searchUserFragment = null;
        this.searchFragment = searchFragment;
//        this.cacheWrapper = new SearchHistoryCacheWrapper((Context)mainActivity);
    }

    public SearchPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(final int n) {
        if (n == 0) {
            (this.searchTagFragment = new SearchTagFragment()).setSearchFragment(this.searchFragment);
//            this.searchTagFragment.setCacheWrapper(this.cacheWrapper);
            return this.searchTagFragment;
        }
        if (n == 1) {
            (this.searchPicFragment = new SearchPicFragment()).setSearchFragment(this.searchFragment);
//            this.searchPicFragment.setCacheWrapper(this.cacheWrapper);
            return this.searchPicFragment;
        }
        (this.searchUserFragment = new SearchUserFragment()).setSearchFragment(this.searchFragment);
//        this.searchUserFragment.setCacheWrapper(this.cacheWrapper);
        return this.searchUserFragment;
//        return searchFragment;
    }

    public void hideProgress(final int n) {
        switch (n) {
            default: {}
            case 0: {
                this.searchTagFragment.delayHideProgress();
            }
            case 1: {
                this.searchPicFragment.delayHideProgress();
            }
            case 2: {
                this.searchUserFragment.delayHideProgress();
            }
        }
    }

    public void showAutoComplete(final String s) {
        this.searchTagFragment.showAutoComplete(s);
        this.searchPicFragment.showAutoComplete(s);
        this.searchUserFragment.showAutoComplete(s);
    }

    public void showHistory() {
//        this.searchTagFragment.showHistory();
//        this.searchPicFragment.showHistory();
//        this.searchUserFragment.showHistory();
    }

    public void showNoSearchResult(final int n) {
        switch (n) {
            default: {}
            case 0: {
                this.searchTagFragment.showAutoCompleteNodata(true);
            }
            case 1: {
                this.searchPicFragment.showAutoCompleteNodata(true);
            }
            case 2: {
                this.searchUserFragment.showAutoCompleteNodata(true);
            }
        }
    }

    public void showProgress(final int n) {
        switch (n) {
            default: {}
            case 0: {
                this.searchTagFragment.showProgressBar();
            }
            case 1: {
                this.searchPicFragment.showProgressBar();
            }
            case 2: {
                this.searchUserFragment.showProgressBar();
            }
        }
    }
}