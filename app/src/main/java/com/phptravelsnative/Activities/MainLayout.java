package com.phptravelsnative.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.phptravelsnative.Fragments.AboutUs;
import com.phptravelsnative.Fragments.Blog;
import com.phptravelsnative.Fragments.Blog_detail;
import com.phptravelsnative.Fragments.ContactUs;
import com.phptravelsnative.Fragments.Login_Fragment;
import com.phptravelsnative.Fragments.MainLayoutFragment;
import com.phptravelsnative.Fragments.MyBooking;
import com.phptravelsnative.Fragments.ProfileFragment;
import com.phptravelsnative.Fragments.Registration;
import com.phptravelsnative.Fragments.Setting;
import com.phptravelsnative.Fragments.booking_fragment;
import com.phptravelsnative.R;



public class MainLayout extends Drawer {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.mainlayout);
        View inflated = stub.inflate();
        View v = inflated.findViewById(R.id.main_fragment);


        Intent intent = getIntent();


        String s = intent.getStringExtra("CheckLayout");

        if (s == null) {
            s = "MainList";  // أو أي قيمة افتراضية تختارها
        }


        textView.setPaddingRelative(0,0,95,0);

        switch (s) {
            case "MainList": {
                MainLayoutFragment listFragment = new MainLayoutFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                fragmentTransaction.commit();
                break;
            }
            case "login": {
                Login_Fragment listFragment = (Login_Fragment) Login_Fragment.newInstance("login", true, null, null, null, null, null);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getFragmentManager().getBackStackEntryCount() == 0) finish();
                    }
                });
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);

                textView.setText(R.string.login);
                fragmentTransaction.commit();
                break;
            }
            case "register": {
                Registration listFragment = new Registration();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                textView.setText(R.string.registration);
                fragmentTransaction.commit();
                break;
            }
            case "booking": {

                booking_fragment listFragment = new booking_fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                textView.setText(R.string.booking);
                fragmentTransaction.commit();
                break;
            }
            case "setting": {

                Setting listFragment = new Setting();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                textView.setText(R.string.setting);
                fragmentTransaction.commit();
                break;
            }
            case "blog": {

                Blog listFragment = new Blog();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                fragmentTransaction.commit();
                textView.setText(R.string.blog);
                break;
            }
            case "blog_detail": {

                Blog_detail listFragment =Blog_detail.newInstance(intent.getStringExtra("desc"),intent.getStringExtra("image_url"));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                fragmentTransaction.commit();
                break;
            } case "contact": {

                ContactUs listFragment =new ContactUs();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                fragmentTransaction.commit();
                textView.setText(R.string.contacts);
                break;
            }case "about": {

                AboutUs listFragment =new AboutUs();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                textView.setText(R.string.about);

                fragmentTransaction.commit();
                break;
            }case "mybooking": {

                MyBooking listFragment =new MyBooking();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                textView.setText(R.string.my_booking);
                fragmentTransaction.commit();
                break;
            }case "profile": {

                ProfileFragment listFragment =new ProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(v.getId(), listFragment);
                textView.setText(R.string.profile);
                fragmentTransaction.commit();
                break;
            }
        }

    }
}

