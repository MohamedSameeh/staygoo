package com.phptravelsnative.utality.lib.Parallex;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;


import android.view.ViewGroup;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public abstract class ParallaxFragmentPagerAdapter extends FragmentPagerAdapter {

    private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
    private int mNumFragments;

    public ParallaxFragmentPagerAdapter(FragmentManager fm, int numFragments) {
        super(fm);
        mScrollTabHolders = new SparseArrayCompat<>();
        mNumFragments = numFragments;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);

        mScrollTabHolders.put(position, (ScrollTabHolder) object);

        return object;
    }

    @Override
    public int getCount() {
        return mNumFragments;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
        return mScrollTabHolders;
    }
}
