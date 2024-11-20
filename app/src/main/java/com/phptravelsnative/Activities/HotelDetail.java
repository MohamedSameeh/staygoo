package com.phptravelsnative.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.phptravelsnative.Adapters.SlidingImage_Adapter;
import com.phptravelsnative.Fragments.DemoList;
import com.phptravelsnative.Fragments.DemoRecyclerViewFragment;
import com.phptravelsnative.Fragments.RoomFragment;
import com.phptravelsnative.Fragments.SecondScrollViewFragment;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.HotelModel;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.Models.rooms_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.CirclePageIndicator;
import com.phptravelsnative.utality.lib.Parallex.ParallaxFragmentPagerAdapter;
import com.phptravelsnative.utality.lib.Parallex.ParallaxViewPagerBaseActivity;
import com.phptravelsnative.utality.slidingTab.SlidingTabLayout;

import java.util.ArrayList;


public class HotelDetail extends ParallaxViewPagerBaseActivity {


    int id;
    private static ViewPager mPager;
    private SlidingTabLayout mNavigBar;
    ArrayList<DetailModel> arrayList=new ArrayList<>();
    ArrayList<review_model> review_list=new ArrayList<>();
    ArrayList<Amenities_Model> amenities_list=new ArrayList<>();
    ArrayList<rooms_model> room_list=new ArrayList<>();
    ArrayList<HotelModel> related_list=new ArrayList<>();
    OverView overView=new OverView();
    Hotel_data hotel_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.activity_detail);
        View inflated = stub.inflate();

        TextView textView = (TextView) findViewById(R.id.NaviText);

        initValues();

        Intent intent = getIntent();
        amenities_list = intent.getParcelableArrayListExtra("am");
        arrayList = intent.getParcelableArrayListExtra("arrayList");
        review_list = intent.getParcelableArrayListExtra("review");
        related_list = intent.getParcelableArrayListExtra("related");
        room_list = intent.getParcelableArrayListExtra("room");
        overView = intent.getParcelableExtra("ov");
        hotel_data = intent.getParcelableExtra("hotel");

        if (overView != null) {
            textView.setText(overView.getTitle());
        } else {
            textView.setText("unknown error");
            Log.e("HotelDetail", "OverView is null!");
        }

        textView.setSelected(true);

        mPager = (ViewPager) inflated.findViewById(R.id.pager);
        mHeader = inflated.findViewById(R.id.header);
        mViewPager = (ViewPager) inflated.findViewById(R.id.view_pager);
        mNavigBar = (SlidingTabLayout) inflated.findViewById(R.id.navig_tab);

        if (savedInstanceState != null) {
            mPager.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
            mHeader.setTranslationY(savedInstanceState.getFloat(HEADER_TRANSLATION_Y));
        }

        mPager.setAdapter(new SlidingImage_Adapter(HotelDetail.this, arrayList, true));

        setupAdapter();

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

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
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mNumFragments,review_list,amenities_list,overView,room_list,related_list,hotel_data);
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mNumFragments);
        mNavigBar.setOnPageChangeListener(getViewPagerChangeListener());
//        mNavigBar.setViewPager(mViewPager);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(IMAGE_TRANSLATION_Y, mPager.getTranslationY());
        outState.putFloat(HEADER_TRANSLATION_Y, mHeader.getTranslationY());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRecyclerViewScroll(RecyclerView view, int dx, int dy, int scrollY, int pagePosition) {

    }


    private  class ViewPagerAdapter extends ParallaxFragmentPagerAdapter {

        ArrayList<review_model>review_models;
        ArrayList<Amenities_Model> amenities_list;
        ArrayList<rooms_model> room_list;
        ArrayList<HotelModel> related_list;
        Hotel_data hotel_data;
        OverView overView;
        public ViewPagerAdapter(FragmentManager fm, int numFragments, ArrayList<review_model> rv, ArrayList<Amenities_Model> amenities_list, OverView overView, ArrayList<rooms_model> room_list, ArrayList<HotelModel> related_list, Hotel_data hotel_data) {
            super(fm, numFragments);
            review_models=rv;
            this.amenities_list=amenities_list;
            this.overView=overView;
            this.room_list=room_list;
            this.related_list=related_list;
            this.hotel_data=hotel_data;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = SecondScrollViewFragment.newInstance(0,overView,amenities_list);
                    break;

                case 1:
                    fragment = RoomFragment.newInstance(1,overView,room_list,false,hotel_data);

                    break;

                case 2:
                    fragment = DemoRecyclerViewFragment.newInstance(2,review_models);

                    break;
                case 3:
                    fragment = DemoList.newInstance(3,hotel_data,related_list,"hotel_default");
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
                    return getString(R.string.rooms);
                case 2:
                    return getString(R.string.reviews);

                case 3:
                    return getString(R.string.related);
                default:
                    throw new IllegalArgumentException("wrong position for the fragment in vehicle page");
            }
        }
    }
}

