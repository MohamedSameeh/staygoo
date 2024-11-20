package com.phptravelsnative.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.phptravelsnative.Adapters.SlidingImage_Adapter;
import com.phptravelsnative.Fragments.DemoListViewFragment;
import com.phptravelsnative.Fragments.DemoRecyclerViewFragment;
import com.phptravelsnative.Fragments.Tour_Overview;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.CirclePageIndicator;
import com.phptravelsnative.utality.lib.Parallex.ParallaxFragmentPagerAdapter;
import com.phptravelsnative.utality.lib.Parallex.ParallaxViewPagerBaseActivity;
import com.phptravelsnative.utality.slidingTab.SlidingTabLayout;

import java.util.ArrayList;


public class TourDetail extends ParallaxViewPagerBaseActivity {


    int id;
    private ProgressDialog dialog;
    private ViewPager mPager;
    private SlidingTabLayout mNavigBar;
    ArrayList<DetailModel> arrayList=new ArrayList<>();
    ArrayList<review_model> review_list=new ArrayList<>();
    ArrayList<Amenities_Model> amenities_list=new ArrayList<>();
    ArrayList<Amenities_Model> amenities_list2=new ArrayList<>();
    OverView overView=new OverView();
    Model_Tour tourM;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.activity_detail);
        View inflated = stub.inflate();


        TextView textView=(TextView) findViewById(R.id.NaviText);



        initValues();

        Intent intent=getIntent();
        amenities_list=intent.getParcelableArrayListExtra("am");
        amenities_list2=intent.getParcelableArrayListExtra("am2");
        arrayList=intent.getParcelableArrayListExtra("arrayList");
        review_list=intent.getParcelableArrayListExtra("review");
        overView=intent.getParcelableExtra("ov");
        tourM=intent.getParcelableExtra("tou");

        textView.setText(overView.getTitle());
        textView.setPaddingRelative(0,0,0,0);


        mPager = (ViewPager) inflated.findViewById(R.id.pager);
        mHeader =inflated. findViewById(R.id.header);
        mViewPager = (ViewPager) inflated.findViewById(R.id.view_pager);
        mNavigBar = (SlidingTabLayout) inflated.findViewById(R.id.navig_tab);


        if (savedInstanceState != null) {
            mPager.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
            mHeader.setTranslationY(savedInstanceState.getFloat(HEADER_TRANSLATION_Y));
        }

        mPager.setAdapter(new SlidingImage_Adapter(TourDetail.this, arrayList,true));


        setupAdapter();

        CirclePageIndicator indicator =(CirclePageIndicator)
                findViewById(R.id.indicator);


        indicator.setViewPager(mPager);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initValues() {

        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + tabHeight;
        mNumFragments = 4;

    }

    @Override
    protected void scrollHeader(int scrollY) {

        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        mHeader.setTranslationY(translationY);
        mPager.setTranslationY(-translationY / 3);
    }

    @Override
    protected void setupAdapter() {

        if (mAdapter == null) {
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mNumFragments,review_list,amenities_list,overView,amenities_list2,tourM);
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mNumFragments);
        mNavigBar.setOnPageChangeListener(getViewPagerChangeListener());
        mNavigBar.setViewPager(mViewPager);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(IMAGE_TRANSLATION_Y, mPager.getTranslationY());
        outState.putFloat(HEADER_TRANSLATION_Y, mHeader.getTranslationY());
        super.onSaveInstanceState(outState);
    }



    private  class ViewPagerAdapter extends ParallaxFragmentPagerAdapter {

        ArrayList<review_model>review_models;
        ArrayList<Amenities_Model> amenities_list;
        ArrayList<Amenities_Model> am2;
        OverView overView;
        Model_Tour tour;
        public ViewPagerAdapter(FragmentManager fm, int numFragments, ArrayList<review_model> rv, ArrayList<Amenities_Model> amenities_list, OverView overView, ArrayList<Amenities_Model> am2, Model_Tour tourM) {
            super(fm, numFragments);
            review_models=rv;
            this.amenities_list=amenities_list;
            this.overView=overView;
            this.am2=am2;
            this.tour=tourM;

        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = Tour_Overview.newInstance(0,overView,tour);
                    break;

                case 1:
                    fragment = DemoListViewFragment.newInstance(1,amenities_list);
                    break;

                case 2:
                    fragment = DemoListViewFragment.newInstance(2,am2);

                    break;
                case 3:
                    fragment = DemoRecyclerViewFragment.newInstance(3,review_models);
                    break;
                default:
                    throw new IllegalArgumentException("Wrong page given " + position);
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.overview);

                case 1:
                    return getString(R.string.inclusion);
                case 2:
                    return getString(R.string.Exclusion);

                case 3:
                    return getString(R.string.reviews);
                default:
                    throw new IllegalArgumentException("wrong position for the fragment in vehicle page");
            }
        }
    }
}

