package com.phptravelsnative.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.google.android.material.tabs.TabLayout;
import com.phptravelsnative.Activities.SplashActivity;
import com.phptravelsnative.Models.MenuModel;
import com.phptravelsnative.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainLayoutFragment extends Fragment {

    LinearLayout view;
    RequestQueue requestQueue;


    boolean first=false;

    public MainLayoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated=inflater.inflate(R.layout.fragment_main_layout, container, false);

        ViewPager viewPager = (ViewPager)inflated. findViewById(R.id.viewpager);

        TabLayout tabLayout = (TabLayout) inflated. findViewById(R.id.tabs);
        final ScrollView scrollView=(ScrollView)inflated.findViewById(R.id.mainBar);
        final RelativeLayout relativeLayout=(RelativeLayout) inflated.findViewById(R.id.logo_layout);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(first)
                       relativeLayout.setVisibility(View.GONE);
                        first=true;
                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        tabLayout.setupWithViewPager(viewPager);




        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        Collections.sort(SplashActivity.menuModels, new Comparator<MenuModel>(){
            public int compare(MenuModel s1, MenuModel s2) {
                return s1.getType().compareToIgnoreCase(s2.getType());
            }
        });
        int size=SplashActivity.menuModels.size();

        if(size>5)
           size=5;


        for(int i=0;i<size;i++)
        {
            if(SplashActivity.menuModels.get(i).getType().equals("1default")){
                adapter.addFragment(new Default_Hotel(),getString(R.string.hotels));}

//            else if(SplashActivity.menuModels.get(i).getType().equals("2ean")){
//                adapter.addFragment(new Expedia_Hotel(),getString(R.string.hotels));}

            else if(SplashActivity.menuModels.get(i).getType().equals("3Travelpayouts")){
                adapter.addFragment(WebViewFragments.newInstance("Travelpayouts"),getString(R.string.filghts));}

            else if(SplashActivity.menuModels.get(i).getType().equals("4Travelstart")){
                adapter.addFragment(WebViewFragments.newInstance("Travelstart"),getString(R.string.filghts));}

            else if(SplashActivity.menuModels.get(i).getType().equals("5dhop"))
                adapter.addFragment(WebViewFragments.newInstance("dohope"),getString(R.string.filghts));

//           else if(SplashActivity.menuModels.get(i).getType().equals("6default_tour"))
//                adapter.addFragment(new Tour(),getString(R.string.Tours));

            else if(SplashActivity.menuModels.get(i).getType().equals("7Wegoflights"))
                adapter.addFragment(WebViewFragments.newInstance("wego"),getString(R.string.filghts));

            else if(SplashActivity.menuModels.get(i).getType().equals("8hotelscombined"))
                adapter.addFragment(WebViewFragments.newInstance("hotelscombined"),getString(R.string.hotels));

//            else if(SplashActivity.menuModels.get(i).getType().equals("9default_cars"))
//                adapter.addFragment(new car_home(),getString(R.string.cars));

            else if(SplashActivity.menuModels.get(i).getType().equals("9cartrawler"))
                adapter.addFragment(WebViewFragments.newInstance("cartrawler"),getString(R.string.cars));


        }

        viewPager.setAdapter(adapter);
        return inflated;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Expedia_Hotel> mFragmentList = new ArrayList<Expedia_Hotel>();
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

        public void addFragment(Default_Hotel fragment1, String title) {
//            mFragmentList.add(fragment1);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
