package com.phptravelsnative.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;


import com.phptravelsnative.Adapters.SlidingImage_Adapter;
import com.phptravelsnative.Fragments.DemoList;
import com.phptravelsnative.Fragments.Map;
import com.phptravelsnative.Fragments.RoomFragment;
import com.phptravelsnative.Fragments.overview_expedia;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.ExpediaExtra;
import com.phptravelsnative.Models.HotelModel;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.Models.rooms_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.CirclePageIndicator;
import com.phptravelsnative.utality.lib.Parallex.ParallaxFragmentPagerAdapter;
import com.phptravelsnative.utality.lib.Parallex.ParallaxViewPagerBaseActivity;
import com.phptravelsnative.utality.slidingTab.SlidingTabLayout;




public class ExpediaDetail extends ParallaxViewPagerBaseActivity {


    int id;
    private static ViewPager mPager;
    private SlidingTabLayout mNavigBar;
    ArrayList<DetailModel> arrayList=new ArrayList<>();
    ArrayList<Amenities_Model> amenities_list=new ArrayList<>();
    ArrayList<rooms_model> room_list=new ArrayList<>();
    ArrayList<HotelModel> related_list=new ArrayList<>();
    OverView overView=new OverView();
    Hotel_data hotel_data=new Hotel_data();
    ExpediaExtra expediaExtra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.activity_detail);
        View inflated = stub.inflate();



        TextView textView=(TextView) findViewById(R.id.NaviText);



        Intent intent=getIntent();
        amenities_list=intent.getParcelableArrayListExtra("am");
        arrayList=intent.getParcelableArrayListExtra("arrayList");
        related_list=intent.getParcelableArrayListExtra("related_list");
        room_list=intent.getParcelableArrayListExtra("room");
        overView=intent.getParcelableExtra("ov");
        hotel_data=intent.getParcelableExtra("hotel");
        expediaExtra=intent.getParcelableExtra("ex_hotel");

        initValues();



        textView.setText(overView.getTitle());
        textView.setSelected(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(5);
        textView.setSelected(true);

        mPager = (ViewPager) inflated.findViewById(R.id.pager);
        mHeader =inflated. findViewById(R.id.header);
        mViewPager = (ViewPager) inflated.findViewById(R.id.view_pager);
        mNavigBar = (SlidingTabLayout) inflated.findViewById(R.id.navig_tab);


        if (savedInstanceState != null) {
            mPager.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
            mHeader.setTranslationY(savedInstanceState.getFloat(HEADER_TRANSLATION_Y));
        }


        mPager.setAdapter(new SlidingImage_Adapter(ExpediaDetail.this, arrayList,true));


        setupAdapter();

        CirclePageIndicator indicator =(CirclePageIndicator)
                findViewById(R.id.indicator);


        indicator.setViewPager(mPager);



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initValues() {

        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + tabHeight;
        if(related_list.size()==0)
            mNumFragments = 3;
        else
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
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mNumFragments,amenities_list,overView,room_list,related_list,hotel_data);
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

        ArrayList<Amenities_Model> amenities_list;
        ArrayList<HotelModel> related_list;
        ArrayList<rooms_model> room_list;
        Hotel_data hotel_data;
        OverView overView;

        public ViewPagerAdapter(FragmentManager fm, int numFragments, ArrayList<Amenities_Model> amenities_list, OverView overView, ArrayList<rooms_model> room_list, ArrayList<HotelModel> hm, Hotel_data hotel_data) {
            super(fm, numFragments);
            this.amenities_list=amenities_list;
            this.overView=overView;
            this.room_list=room_list;
            this.hotel_data=hotel_data;
            this.related_list=hm;

        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if(related_list.size()==0) {
                switch (position) {
                    case 0:
                        fragment = overview_expedia.newInstance(0, overView, amenities_list);
                        break;
                    case 1:
                        fragment = RoomFragment.newInstanceExpedia(1, overView, expediaExtra, room_list, true, hotel_data);
                        break;
                    case 2:
                        fragment = Map.newInstance(2, overView);
                        break;
                    default:
                        throw new IllegalArgumentException("Wrong page given " + position);
                }
            }else
            {
                switch (position) {
                    case 0:
                        fragment = overview_expedia.newInstance(0, overView, amenities_list);
                        break;
                    case 1:
                        fragment = RoomFragment.newInstanceExpedia(1, overView, expediaExtra, room_list, true, hotel_data);
                        break;
                    case 2:
                        fragment = Map.newInstance(2, overView);
                        break;
                    case 3:
                        fragment = DemoList.newInstance(3, hotel_data, related_list, "ex_hotel");
                        break;
                    default:
                        throw new IllegalArgumentException("Wrong page given " + position);
                }
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(related_list.size()==0){
                switch (position) {
                    case 0:
                        return getString(R.string.overview);
                    case 1:
                        return getString(R.string.rooms);
                    case 2:
                        return getString(R.string.map);
                    default:
                        throw new IllegalArgumentException("wrong position for the fragment in vehicle page");
                }
            }else {
                switch (position) {
                    case 0:
                        return getString(R.string.overview);
                    case 1:
                        return getString(R.string.rooms);
                    case 2:
                        return getString(R.string.map);
                    case 3:
                        return getString(R.string.related);
                    default:
                        throw new IllegalArgumentException("wrong position for the fragment in vehicle page");
                }
            }
        }
    }
}

