package com.phptravelsnative.utality.lib.Parallex;


import android.view.View;

import androidx.collection.SparseArrayCompat;
import androidx.viewpager.widget.ViewPager;

public class ParallaxViewPagerChangeListener implements ViewPager.OnPageChangeListener {

    public static final String TAG = ParallaxViewPagerChangeListener.class.getSimpleName();

    protected ViewPager mViewPager;
    protected ParallaxFragmentPagerAdapter mAdapter;

    protected View mHeader;

    protected int mNumFragments;

    public ParallaxViewPagerChangeListener(ViewPager viewPager, ParallaxFragmentPagerAdapter adapter, View headerView) {
        mViewPager = viewPager;
        mAdapter = adapter;
        mNumFragments = mAdapter.getCount();
        mHeader = headerView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int currentItem = mViewPager.getCurrentItem();
        if (positionOffsetPixels > 0) {
            SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mAdapter.getScrollTabHolders();

            ScrollTabHolder fragmentContent;
            if (position < currentItem) {
                // Revealed the previous page
                fragmentContent = scrollTabHolders.valueAt(position);
            } else {
                // Revealed the next page
                fragmentContent = scrollTabHolders.valueAt(position + 1);
            }

            fragmentContent.adjustScroll((int) (mHeader.getHeight() + mHeader.getTranslationY()),
                    mHeader.getHeight());
        }
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mAdapter.getScrollTabHolders();

        if (scrollTabHolders == null || scrollTabHolders.size() != mNumFragments) {
            return;
        }

        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        currentHolder.adjustScroll(
                (int) (mHeader.getHeight() + mHeader.getTranslationY()),
                mHeader.getHeight());
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
