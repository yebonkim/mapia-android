package com.mapia.myfeed;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mapia.myfeed.grid.MyfeedPicGridFragment;

public class MyfeedPagerAdapter extends FragmentStatePagerAdapter
{
    private MyfeedFragment myfeedFragment;
    private MyfeedPicGridFragment myfeedPicGridFragment;
//    private MyfeedPicListFragment myfeedPicListFragment;

    public MyfeedPagerAdapter(final MyfeedFragment myfeedFragment) {
        super(myfeedFragment.getChildFragmentManager());
//        this.myfeedPicListFragment = null;
        this.myfeedPicGridFragment = null;
        this.myfeedFragment = myfeedFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(final int n) {
//        if (n == 0) {
//            (this.myfeedPicListFragment = new MyfeedPicListFragment()).setMyfeedFragment(this.myfeedFragment);
//            return this.myfeedPicListFragment;
//        }
        (this.myfeedPicGridFragment = new MyfeedPicGridFragment()).setMyfeedFragment(this.myfeedFragment);
        return this.myfeedPicGridFragment;

    }

//    public MyfeedPicListFragment getMyfeedPicListFragment() {
//        return this.myfeedPicListFragment;
//    }

    public void pauseVideoIfShowOnList() {
//        if (this.myfeedPicListFragment != null) {
//            this.myfeedPicListFragment.pauseVideoIfShowOnList();
//        }
    }

    public void refreshAllPage() {
//        if (this.myfeedPicListFragment != null) {
//            this.myfeedPicListFragment.refreshList();
//        }
        if (this.myfeedPicGridFragment != null) {
            this.myfeedPicGridFragment.refreshList();
        }
    }

    public void refreshPage(final int n) {
        switch (n) {
            case 0: {
//                if (this.myfeedPicListFragment != null) {
//                    this.myfeedPicListFragment.refreshList();
//                    return;
//                }
                break;
            }
            case 1: {
                if (this.myfeedPicGridFragment != null) {
                    this.myfeedPicGridFragment.refreshList();
                    return;
                }
                break;
            }
        }
    }

    public void scrollToTop(final int n) {
        switch (n) {
            default: {}
            case 0: {
//                this.myfeedPicListFragment.scrollToTop();
            }
            case 1: {
                this.myfeedPicGridFragment.scrollToTop();
            }
        }
    }

    public void setConnectionCondition(final boolean b) {
//        if (this.myfeedPicListFragment != null) {
//            this.myfeedPicListFragment.setConnectionCondition(b);
//        }
        if (this.myfeedPicGridFragment != null) {
            this.myfeedPicGridFragment.setConnectionCondition(b);
        }
    }

    public void setGridFragmentVisible(final int n) {
        if (this.myfeedPicGridFragment != null) {
            return;
        }
    }

    public void setListFragmentVisible(final int n) {
//        if (this.myfeedPicListFragment != null) {
//            return;
//        }
    }
}