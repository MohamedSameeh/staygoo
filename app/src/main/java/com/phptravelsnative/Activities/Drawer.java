package com.phptravelsnative.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.phptravelsnative.Adapters.DrawerAdapter;
import com.phptravelsnative.Models.DrawerItem;
import com.phptravelsnative.Models.MenuModel;
import com.phptravelsnative.Network.Post.LoginRequest;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Extra.DateSeter;
import com.phptravelsnative.utality.Views.SingleTonRequest;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout; // لإدارة DrawerLayout
import androidx.recyclerview.widget.RecyclerView; // لتصميم RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager; // لتعيين تخطيط RecyclerView
import androidx.appcompat.app.ActionBarDrawerToggle; // لإعداد DrawerToggle

import java.util.ArrayList;
import java.util.Locale;



import static com.phptravelsnative.Activities.SplashActivity.menuModels;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Drawer extends AppCompatActivity {


    ImageButton button;
    DrawerLayout drawer;
    SharedPreferences sharedPreferences;
    String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    DrawerItem login_logout;
    String day_to;
    ProgressDialog dialog;

    private RecyclerView mRecyclerView;
    private ArrayList<DrawerItem> mDrawerItemList;
    private Locale myLocale;
    int check_rtl = 1;
    TextView textView;
    DateSeter checkDate;
    DrawerAdapter adapter;
    boolean b=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        check_rtl = sharedPreferences.getInt("check_rtl", 1);


        mDrawerItemList = new ArrayList<>();

        textView = (TextView) findViewById(R.id.NaviText);
        checkDate = new DateSeter(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.drawerRecyclerView);

        button = (ImageButton) findViewById(R.id.naviIcon);

        textView.setSelected(true);

        DrawerItem drawer_home = new DrawerItem();
        drawer_home.setTitle(getString(R.string.home));
        drawer_home.setIcon(R.drawable.ic_home);
        drawer_home.setId("home");
        mDrawerItemList.add(drawer_home);

        DrawerItem drawer_login = new DrawerItem();
        drawer_login.setTitle(getString(R.string.login_register));
        drawer_login.setIcon(R.drawable.login);
        drawer_login.setId("login_re");
        mDrawerItemList.add(drawer_login);

        DrawerItem drawer_booking = new DrawerItem();
        drawer_booking.setTitle(getString(R.string.check_invoice));
        drawer_booking.setIcon(R.drawable.check_invoice);
        drawer_booking.setId(("invoice"));
        mDrawerItemList.add(drawer_booking);


        DrawerItem drawer_blog = new DrawerItem();
        drawer_blog.setTitle(getString(R.string.blog));
        drawer_blog.setIcon(R.drawable.ic_blog);
        drawer_blog.setId("blog");
        mDrawerItemList.add(drawer_blog);

        DrawerItem drawer_setting = new DrawerItem();
        drawer_setting.setTitle(getString(R.string.setting));
        drawer_setting.setId("setting");
        drawer_setting.setIcon(R.drawable.ic_settings);
        mDrawerItemList.add(drawer_setting);

        DrawerItem drawer_contacts = new DrawerItem();
        drawer_contacts.setTitle(getString(R.string.contact));
        drawer_contacts.setId("contacts");
        drawer_contacts.setIcon(R.drawable.ic_contats);
        mDrawerItemList.add(drawer_contacts);

        DrawerItem drawer_about = new DrawerItem();
        drawer_about.setTitle(getString(R.string.about));
        drawer_about.setId("about");
        drawer_about.setIcon(R.drawable.ic_about);
        mDrawerItemList.add(drawer_about);


        MenuModel menuModel = new MenuModel();
        menuModel.setImgID(R.drawable.offers);
        menuModel.setDescription(R.string.offersDescription);
        menuModel.setTitle(R.string.offers);
        menuModel.setType("default_offers");
        menuModels.add(menuModel);

        if (SplashActivity.menuModels.contains(menuModel)) {
            DrawerItem drawer_version = new DrawerItem();
            drawer_version.setTitle(getString(R.string.offers));
            drawer_version.setId("offers");
            drawer_version.setIcon(R.drawable.offers);
            mDrawerItemList.add(drawer_version);
        }
        DrawerItem drawer_version = new DrawerItem();
        drawer_version.setTitle(getString(R.string.version));
        drawer_version.setId("version");
        drawer_version.setIcon(R.drawable.ic_version);
        mDrawerItemList.add(drawer_version);

        DrawerItem app_exit = new DrawerItem();
        app_exit.setTitle(getString(R.string.exit));
        app_exit.setId("exit");
        app_exit.setIcon(R.drawable.exit);
        mDrawerItemList.add(app_exit);

        DrawerItem drawer_header = new DrawerItem();
        drawer_header.setTitle(Constant.appname);
        drawer_header.setIcon(R.drawable.user_header);
        mDrawerItemList.add(0, drawer_header);


        adapter = new DrawerAdapter(mDrawerItemList, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


        login_logout = mDrawerItemList.get(2);

        if (sharedPreferences.getBoolean("Check_first", false)) {
            editor.putBoolean("Check_first", false);
            checkLogin();
        }

        if (sharedPreferences.getBoolean("Check_Login", false)) {
            login_logout.setTitle(getString(R.string.my_account));
            login_logout.setIcon(R.drawable.my_account);
            mDrawerItemList.get(0).setTitle(sharedPreferences.getString("name", ""));
            adapter.notifyDataSetChanged();
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View container = findViewById(R.id.moving);
                if (check_rtl == 1)
                    container.setTranslationX(slideOffset * drawerView.getWidth());
                else
                    container.setTranslationX(-(slideOffset * drawerView.getWidth()));

                View keyBoard = getCurrentFocus();
                if (keyBoard != null) {
                    InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(keyBoard.getWindowToken(), 0);
                }

            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        adapter.setOnItemClickLister(new DrawerAdapter.OnItemSelecteListener() {
            @Override
            public void onItemSelected(View v, int position) {

                if (position != mDrawerItemList.size()) {
                    if (mDrawerItemList.get(position).getId().equals("home")) {
                        Intent intent = new Intent(getApplicationContext(), MainLayout.class);
                        intent.putExtra("CheckLayout", "MainList");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else if (mDrawerItemList.get(position).getId().equals("login_re")) {

                        if (mDrawerItemList.get(position).getTitle().equals(getString(R.string.my_account))) {
                            if (b) {
                                mDrawerItemList.remove(3);
                                mDrawerItemList.remove(3);
                                mDrawerItemList.remove(3);
                                mDrawerItemList.get(position).setIcon(R.drawable.my_account);
                                b = false;
                            } else {
                                mDrawerItemList.get(position).setIcon(R.drawable.my_account);
                                DrawerItem my_profile = new DrawerItem();
                                my_profile.setTitle(getString(R.string.profile));
                                my_profile.setId("profile");
                                my_profile.setIcon(R.drawable.my_profile);
                                mDrawerItemList.add(3, my_profile);

                                DrawerItem my_booking = new DrawerItem();
                                my_booking.setTitle(getString(R.string.my_booking));
                                my_booking.setId("my_booking");
                                my_booking.setIcon(R.drawable.ic_booking);
                                mDrawerItemList.add(4, my_booking);

                                DrawerItem my_logout = new DrawerItem();
                                my_logout.setTitle(getString(R.string.logout));
                                my_logout.setIcon(R.drawable.logout);
                                my_logout.setId("logout");
                                mDrawerItemList.add(5, my_logout);
                                b = true;
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Intent intent = new Intent(Drawer.this, MainLayout.class);
                            intent.putExtra("CheckLayout", "login");
                            startActivityForResult(intent, 2);
                            drawer.closeDrawer(GravityCompat.START);

                        }
                    } else if (mDrawerItemList.get(position).getId().equals("my_booking")) {
                        Intent intent = new Intent(Drawer.this, MainLayout.class);
                        intent.putExtra("CheckLayout", "mybooking");
                        startActivity(intent);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (mDrawerItemList.get(position).getId().equals("logout")) {
                        editor.putBoolean("Check_Login", false);
                        editor.putString("email", "");
                        editor.putString("id", "");
                        editor.commit();
                        mDrawerItemList.get(0).setTitle(Constant.appname);
                        login_logout.setTitle(getString(R.string.login_register));
                        login_logout.setIcon(R.drawable.login);
                        mDrawerItemList.remove(3);
                        mDrawerItemList.remove(3);
                        mDrawerItemList.remove(3);
                        b = false;
                        adapter.notifyDataSetChanged();
                        Intent intent = new Intent(getApplicationContext(), MainLayout.class);
                        intent.putExtra("CheckLayout", "MainList");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else if (mDrawerItemList.get(position).getId().equals("invoice")) {
                        Intent intent = new Intent(Drawer.this, MainLayout.class);
                        intent.putExtra("CheckLayout", "booking");
                        startActivity(intent);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (mDrawerItemList.get(position).getId().equals("invoice")) {
                        Intent intent = new Intent(Drawer.this, MainLayout.class);
                        intent.putExtra("CheckLayout", "booking");
                        startActivity(intent);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (mDrawerItemList.get(position).getId().equals("blog")) {
                        Intent intent = new Intent(Drawer.this, MainLayout.class);
                        intent.putExtra("CheckLayout", "blog");
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(intent);
                    } else if (mDrawerItemList.get(position).getId().equals("setting")) {
                        Intent intent = new Intent(Drawer.this, MainLayout.class);
                        intent.putExtra("CheckLayout", "setting");
                        drawer.closeDrawer(GravityCompat.START);
                        startActivityForResult(intent, 3);
                    } else if (mDrawerItemList.get(position).getId().equals("contacts")) {
                        Intent intent = new Intent(Drawer.this, MainLayout.class);
                        intent.putExtra("CheckLayout", "contact");
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(intent);
                    } else if (mDrawerItemList.get(position).getId().equals("about")) {
                        Intent intent = new Intent(Drawer.this, MainLayout.class);
                        intent.putExtra("CheckLayout", "about");
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(intent);
                    } else if (mDrawerItemList.get(position).getId().equals("offers")) {
                        Intent intent = new Intent(Drawer.this, SearchingCarTourOffers.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("type", "offers");
                        startActivity(new Intent(intent));
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (mDrawerItemList.get(position).getId().equals("profile")) {
                        Intent intent = new Intent(Drawer.this, MainLayout.class);
                        intent.putExtra("CheckLayout", "profile");
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(intent);
                    } else if (mDrawerItemList.get(position).getId().equals("exit")) {
                        finishAffinity();
                        System.exit(0);
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_rtl == 1) { // Arabic or RTL language
                    if (drawer.isDrawerOpen(GravityCompat.START)) { // Use GravityCompat.START for the leading edge
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                } else { // English or LTR language
                    if (drawer.isDrawerOpen(GravityCompat.START)) { // GravityCompat.START also applies here for consistency
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            }
        });
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            if (sharedPreferences.getBoolean("Check_Login", false)) {
                login_logout.setTitle(getString(R.string.my_account));
                login_logout.setIcon(R.drawable.my_account);

                mDrawerItemList.get(0).setTitle(sharedPreferences.getString("name",""));
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 3) {
            String s = sharedPreferences.getString("Language", "");
            check_rtl = sharedPreferences.getInt("check_rtl", -1);
            changeLang(s);
            if (check_rtl == 0) {
                drawer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                drawer.setTextDirection(View.TEXT_DIRECTION_RTL);
                Intent intent = new Intent(getApplicationContext(), MainLayout.class);
                intent.putExtra("CheckLayout", "MainList");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else {
                drawer.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                drawer.setTextDirection(View.TEXT_DIRECTION_LTR);
                Intent intent = new Intent(getApplicationContext(), MainLayout.class);
                intent.putExtra("CheckLayout", "MainList");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        }
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }
    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        Constant.default_lang = lang;
    }

    public void checkLogin() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject main_json = new JSONObject(response);
                    if (main_json.getBoolean("response")) {
                        editor.putBoolean("Check_Login", true);
                        editor.commit();
                    } else {
                        editor.putBoolean("Check_Login", false);
                        editor.putString("email", "");
                        editor.putString("password", "");
                        editor.putString("id", "");
                        login_logout.setTitle(getString(R.string.login_register));
                        adapter.notifyDataSetChanged();editor.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest requestRegister = new LoginRequest(sharedPreferences.getString("email", ""), sharedPreferences.getString("password", ""), listener);

        RequestQueue requestQueue = SingleTonRequest.getmInctance(getApplicationContext()).getRequestQueue();
        requestQueue.add(requestRegister);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(context));
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
