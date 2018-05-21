package com.ogaclejapan.smarttablayout;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public abstract class SmartTabViewPagerPresenter {
    protected SmartTabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected FragmentActivity fragmentActivity;
    private Fragment mFragment;
    protected FragmentPagerItemAdapter fragmentPagerItemAdapter;
    private FragmentPagerItems.Creator fragmentPagerItemsCreator;
    private boolean isChildFragment = false;

    public SmartTabViewPagerPresenter(FragmentActivity activity, SmartTabLayout tabLayout, ViewPager viewPager) {
        this.isChildFragment = false;
        this.fragmentActivity = activity;
        this.mTabLayout = tabLayout;
        this.mViewPager = viewPager;
    }

    public SmartTabViewPagerPresenter(Fragment fragment, SmartTabLayout tabLayout, ViewPager viewPager) {
        this.isChildFragment = true;
        this.mFragment = fragment;
        this.mTabLayout = tabLayout;
        this.mViewPager = viewPager;
    }

    public void prepared() {
        if (this.fragmentActivity == null && this.mFragment == null) {
            Log.e(this.getClass().getSimpleName(), "fragmentActivity==null:" + (this.fragmentActivity == null) + " mTabLayout==null:" + (this.mTabLayout == null) + " mViewPager==null:" + (this.mViewPager == null));
        } else {
            this.setPagerAdapter();
            this.updateParams();
        }

    }

    private void setPagerAdapter() {
        this.fragmentPagerItemsCreator = FragmentPagerItems.with((this.isChildFragment ? this.mFragment.getContext() : this.fragmentActivity));
        this.updateCreator(this.fragmentPagerItemsCreator);
        this.fragmentPagerItemAdapter = new FragmentPagerItemAdapter(this.isChildFragment ? this.mFragment.getChildFragmentManager() : this.fragmentActivity.getSupportFragmentManager(), this.fragmentPagerItemsCreator.create());
        if (this.mViewPager != null) {
            this.mViewPager.setAdapter(this.fragmentPagerItemAdapter);
        }

        if (this.mTabLayout != null) {
            this.mTabLayout.setViewPager(this.mViewPager);
        }

    }

    public void adapterRefresh() {
        if (this.fragmentPagerItemAdapter != null) {
            this.fragmentPagerItemAdapter.notifyDataSetChanged();
        }

    }

    public Context getContext() {
        return this.isChildFragment ? this.mFragment.getContext() : this.fragmentActivity.getBaseContext();
    }

    protected TextView getTabTextView(int position) {
        if (this.mTabLayout == null) {
            return null;
        } else {
            View view = this.mTabLayout.getTabAt(position);
            if (view != null) {
                View tabView = view.findViewById(this.mTabLayout.customTabTextViewId);
                if (tabView != null && tabView instanceof TextView) {
                    return (TextView)tabView;
                }
            }

            return null;
        }
    }

    protected View getPositionView(int position, @IdRes int viewid) {
        if (this.mTabLayout == null) {
            return null;
        } else {
            View view = this.mTabLayout.getTabAt(position);
            return view != null ? view.findViewById(viewid) : null;
        }
    }

    public void setCurrent(int position) {
        if (this.mViewPager != null && this.fragmentPagerItemAdapter != null) {
            if (position >= 0 && position < this.fragmentPagerItemAdapter.getCount()) {
                this.mViewPager.setCurrentItem(position);
            } else {
                this.mViewPager.setCurrentItem(0);
            }
        }

    }

    public int getCurrent() {
        return this.mViewPager != null ? this.mViewPager.getCurrentItem() : -1;
    }

    public Fragment getCurrentFragment() {
        if (this.fragmentPagerItemAdapter == null) {
            return null;
        } else {
            int curPos = this.getCurrent();
            return curPos != -1 ? this.fragmentPagerItemAdapter.getPage(curPos) : null;
        }
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (this.mViewPager != null) {
            this.mViewPager.addOnPageChangeListener(listener);
        }

    }

    public void removeOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (this.mViewPager != null && listener != null) {
            this.mViewPager.removeOnPageChangeListener(listener);
        }

    }

    protected abstract void updateParams();

    protected abstract void updateCreator(FragmentPagerItems.Creator var1);
}