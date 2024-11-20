package com.phptravelsnative.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.phptravelsnative.Fragments.Coupon;
import com.phptravelsnative.Fragments.ExpdeiaBooking;
import com.phptravelsnative.Fragments.Guest;
import com.phptravelsnative.Fragments.Login_Fragment;
import com.phptravelsnative.Models.ExpediaExtra;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.R;

import java.util.ArrayList;
import java.util.List;


public class Invoice extends Drawer {

    SharedPreferences sharedPreferences;
    public  final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    public static String coupon="0";

    car_model car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.activity_invoice);
        View inflated = stub.inflate();

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        coupon="0";

        Intent intent=getIntent();

        Hotel_data hotel_data;
        String room_id;
        String number_room;

        ViewPager viewPager = (ViewPager)inflated. findViewById(R.id.viewpager);

        TabLayout tabLayout = (TabLayout)inflated. findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(intent.getStringExtra("check_type").equals("hotel"))
        {
            hotel_data=intent.getParcelableExtra("hotel_object");
            room_id =intent.getStringExtra("room_id");
             number_room=intent.getStringExtra("numbers_rooms");
            setupViewPager(viewPager,hotel_data,room_id,number_room);


        }else if(intent.getStringExtra("check_type").equals("tour"))
        {
           Model_Tour tour=intent.getParcelableExtra("tour_object");
            setupViewPager(viewPager,tour);

        }else if(intent.getStringExtra("check_type").equals("car"))
        {
           car_model car=intent.getParcelableExtra("car_object");
            setupViewPager(viewPager,car);

        }else if(intent.getStringExtra("check_type").equals("expedia"))
        {
            hotel_data =intent.getParcelableExtra("hotel_object");
            ExpediaExtra expedia=intent.getParcelableExtra("expedia");
            setupViewPager(viewPager,hotel_data,expedia);
        }

    }

    private void setupViewPager(ViewPager viewPager, Hotel_data hotel_data, ExpediaExtra expedia) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if(!sharedPreferences.getBoolean("Check_Login",true)) {
            adapter.addFragment(ExpdeiaBooking.newInstance(hotel_data,expedia),getString(R.string.booking));
            adapter.addFragment(Login_Fragment.newInstance("expedia",false,expedia,hotel_data), getString(R.string.login));
        }else
        {
            adapter.addFragment(ExpdeiaBooking.newInstance(hotel_data,expedia),getString(R.string.booking));

        }
//        viewPager.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coupon=null;
    }

    private void setupViewPager(ViewPager viewPager, Hotel_data hotel_data, String room_id, String room_number) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(!sharedPreferences.getBoolean("Check_Login",true)) {
            adapter.addFragment(Guest.newInstance("hotel", null, null, hotel_data,room_id,room_number), getString(R.string.guest));
            adapter.addFragment(Login_Fragment.newInstance("hotel",false,hotel_data,null,null,room_id,room_number), getString(R.string.login));
        }
        if(sharedPreferences.getBoolean("coupons",false)) {
            adapter.addFragment(Coupon.newInstance("hotel",null,null,hotel_data,room_id,room_number), getString(R.string.coupon));
        }
        viewPager.setAdapter(adapter);
    }
    private void setupViewPager(ViewPager viewPager, Model_Tour tour) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(!sharedPreferences.getBoolean("Check_Login",true)) {
            adapter.addFragment(Guest.newInstance("tour",tour,null,null,null,null), getString(R.string.guest));
            adapter.addFragment(Login_Fragment.newInstance("tour",false,null,tour,null,null,null), getString(R.string.login));
        }
        if(sharedPreferences.getBoolean("coupons",false)) {
            adapter.addFragment(Coupon.newInstance("tour",tour,null,null,null,null), getString(R.string.coupon));
        }
//        viewPager.setAdapter(adapter);
    }
    private void setupViewPager(ViewPager viewPager, car_model car) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(!sharedPreferences.getBoolean("Check_Login",true)) {
            adapter.addFragment(Guest.newInstance("car",null,car,null,null,null), getString(R.string.guest));
            adapter.addFragment(Login_Fragment.newInstance("car",false,null,null,car,null,null), getString(R.string.login));
        }
        if(sharedPreferences.getBoolean("coupons",false)) {
            adapter.addFragment(Coupon.newInstance("car",null,car,null,null,null), getString(R.string.coupon));
        }
//        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
