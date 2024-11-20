package com.phptravelsnative.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.Models.MenuModel;
import com.phptravelsnative.Network.Parser.MainMenuParse;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import java.util.ArrayList;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {



    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String MyPREFERENCES = "MyPrefs" ;
    public static ArrayList<MenuModel> menuModels=new ArrayList<>();
    Intent intent;
    private Locale myLocale;
    ProgressBar pd;
    TextView textView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putBoolean("Check_splash",true);
        editor.putBoolean("coupons",false);
        editor.commit();

        pd =(ProgressBar) findViewById(R.id.progress_bar);

        textView=(TextView)findViewById(R.id.retry);
        imageView=(ImageView) findViewById(R.id.imageview);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainLayout.class);
            startActivity(intent);
            finish(); // إنهاء شاشة Splash
        }, 3000);
        changeLang(sharedPreferences.getString("Language",""));

        textView.setVisibility(View.GONE);
        checkResult();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
                pd.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                checkResult();
            }
        });

    }

    public void checkResult() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constant.domain_name + "?appKey=" + Constant.key + "&lang=" + Constant.default_lang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Log and parse response
                            Log.d("API_Response", response);
                            MainMenuParse mainMenuParse = new MainMenuParse(getBaseContext());
                            mainMenuParse.execute(response);

                            // Navigate to MainLayout
                        } catch (Exception e) {
                            Log.e("SplashActivity", "Error during response handling", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e("Volley_Error", error.toString());
                        }
                        pd.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue requestQueue = SingleTonRequest.getmInctance(getApplicationContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        editor.putBoolean("Check_first",true);
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.setLocale(myLocale);
            Locale.setDefault(myLocale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getApplicationContext().createConfigurationContext(newConfig);
            } else {
                getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase("")) return;

        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(myLocale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(config);
        } else {
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        Constant.default_lang = lang;
    }

}