package com.phptravelsnative.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;


import androidx.fragment.app.Fragment;

import com.phptravelsnative.Adapters.SpinnnerImageAdapter;
import com.phptravelsnative.Models.Auto_Model;
import com.phptravelsnative.R;

import java.util.ArrayList;
import java.util.Locale;


public class Setting extends Fragment {

    ArrayList<Auto_Model> auto_models = new ArrayList<>();
    private Locale myLocale;
    SharedPreferences sharedPreferences;
    String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    boolean first_check = false;


    public Setting() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_setting, container, false);

        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        final Spinner spinner= (Spinner) inflated.findViewById(R.id.spinner_lang);
        Auto_Model auto_model;

        auto_model=new Auto_Model();
        auto_model.setName("English");
        auto_model.setId(1);
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_us);
        auto_model.setType("en");
        auto_models.add(auto_model);

        auto_model=new Auto_Model();
        auto_model.setName("Arabic");
        auto_model.setType("ar");
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_sa);
        auto_model.setId(0);
        auto_models.add(auto_model);

        auto_model=new Auto_Model();
        auto_model.setName("French");
        auto_model.setId(1);
        auto_model.setType("fr");
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_fr);
        auto_models.add(auto_model);

        auto_model=new Auto_Model();
        auto_model.setName("Spanish");
        auto_model.setId(1);
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_es);
        auto_model.setType("es");
        auto_models.add(auto_model);

        auto_model=new Auto_Model();
        auto_model.setName("Italian");
        auto_model.setId(1);
        auto_model.setType("it");
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_it);

        auto_models.add(auto_model);

        auto_model=new Auto_Model();
        auto_model.setName("Portuguese");
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_pt);
        auto_model.setId(1);
        auto_model.setType("pt");
        auto_models.add(auto_model);


        auto_model=new Auto_Model();
        auto_model.setName("Russian");
        auto_model.setId(1);
        auto_model.setType("ru");
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_ru);
        auto_models.add(auto_model);

        auto_model=new Auto_Model();
        auto_model.setName("Filipino");
        auto_model.setId(1);
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_ph);
        auto_model.setType("tl");
        auto_models.add(auto_model);

        auto_model=new Auto_Model();
        auto_model.setName("Turkish");
        auto_model.setId(1);
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_tr);
        auto_model.setType("tr");
        auto_models.add(auto_model);

        auto_model=new Auto_Model();
        auto_model.setName("Ukrainian");
        auto_model.setImage_id(com.countrypicker.R.drawable.flag_ua);
        auto_model.setId(1);
        auto_model.setType("uk");
        auto_models.add(auto_model);


        auto_models.add(auto_model);

        SpinnnerImageAdapter spinnerAdapter=new SpinnnerImageAdapter(getContext(),auto_models);
        spinner.setAdapter(spinnerAdapter);

        String s=sharedPreferences.getString("Language","");
        for(int i=0;i<auto_models.size();i++)
        {
            if(auto_models.get(i).getType().equals(s)) {
                spinner.setSelection(i);
                break;
            }
        }
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spinner.setDropDownVerticalOffset(spinner.getHeight());

                return false;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                if(first_check) {
                    saveLocale(auto_models.get(position).getType(), auto_models.get(position).getId());
                    getActivity().finish();
                }else
                {
                    first_check=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return inflated;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }

    public void saveLocale(String lang, int id)
    {
        String langPref = "Language";
        editor.putString(langPref, lang);
        editor.putInt("check_rtl", id);
        editor.putBoolean("user_set",true);
        editor.apply();
    }
}
