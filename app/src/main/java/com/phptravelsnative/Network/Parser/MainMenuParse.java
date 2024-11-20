package com.phptravelsnative.Network.Parser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.phptravelsnative.Activities.MainLayout;
import com.phptravelsnative.Models.MenuModel;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static com.phptravelsnative.Activities.SplashActivity.menuModels;


public class MainMenuParse extends AsyncTask<Object,Void,String[]> {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String MyPREFERENCES = "MyPrefs" ;
    MenuModel menuModel=new MenuModel();
    private String[] lang_arry={"en","ar","fr","es","it","ru",""};
    Context c;
    private Locale myLocale;
    Intent intent;
    String s[];
    public  MainMenuParse(Context c)
    {
        s= new String[2];
        this.c=c;
        sharedPreferences = c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        intent=new Intent(c,MainLayout.class);
        intent.putExtra("CheckLayout","MainList");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    }
    @Override
    protected String[] doInBackground(Object... params) {



        try {
            JSONObject main_json = new JSONObject((String) params[0]);
            JSONObject error_object=main_json.getJSONObject("error");
            menuModels.clear();

            s[0]="nothing";
            if(error_object.getBoolean("status"))
            {
                s[0]="false";
                s[1]=error_object.getString("msg");
                return  s;

            }else {
                JSONArray parentArray = main_json.getJSONArray("response");

                if(!sharedPreferences.getBoolean("user_set",false)) {
                    JSONObject jsb = main_json.getJSONObject("languageInfo");
                    for(int i=0;i<lang_arry.length;i++)
                    {
                        if(lang_arry[i].equals(jsb.getString("code")))
                        {
                            if (jsb.getString("isRTL").equals("LTR")) {
                                saveLocale(jsb.getString("code"), 1);
                            } else {
                                saveLocale(jsb.getString("code"), 0);
                            }
                            s[0]="true";
                            s[1]=jsb.getString("code");
                            break;
                        }
                    }
                }
                menuModel = new MenuModel();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    if(finalObject.getBoolean("status")) {
                        if(finalObject.getString("title").toLowerCase().equals("hotels")) {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.hotesDescription);
                            menuModel.setTitle(R.string.hotels);
                            menuModel.setType("1default");
                            menuModels.add(menuModel);
                        }
                        else if(finalObject.getString("title").toLowerCase().equals("ean"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.hotesDescription);
                            menuModel.setTitle(R.string.hotels);
                            menuModel.setType("2ean");
                            menuModels.add(menuModel);
                        }
                        else if(finalObject.getString("title").toLowerCase().equals("travelpayouts"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.flightsDescription);
                            menuModel.setTitle(R.string.filghts);
                            menuModel.setType("3Travelpayouts");
                            menuModels.add(menuModel);
                        }
                        else if(finalObject.getString("title").toLowerCase().equals("travelstart"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.flightsDescription);
                            menuModel.setTitle(R.string.filghts);
                            menuModel.setType("4Travelstart");
                            menuModels.add(menuModel);
                        }
                        else if(finalObject.getString("title").toLowerCase().equals("flightsdohop"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.flightsDescription);
                            menuModel.setTitle(R.string.filghts);
                            menuModel.setType("5dhop");
                            menuModels.add(menuModel);
                        }
                        else if(finalObject.getString("title").toLowerCase().equals("tours"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.toursDescription);
                            menuModel.setTitle(R.string.Tours);
                            menuModel.setType("6default_tour");
                            menuModels.add(menuModel);
                        }
                        else if(finalObject.getString("title").toLowerCase().equals("wegoflights"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.flightsDescription);
                            menuModel.setTitle(R.string.filghts);
                            menuModel.setType("7Wegoflights");
                            menuModels.add(menuModel);
                        }
                         else if(finalObject.getString("title").toLowerCase().equals("hotelscombined"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.hotesDescription);
                            menuModel.setTitle(R.string.hotels);
                            menuModel.setType("8hotelscombined");
                            menuModels.add(menuModel);
                        }

                        else if(finalObject.getString("title").toLowerCase().equals("cars"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.carsDescription);
                            menuModel.setTitle(R.string.cars);
                            menuModel.setType("9default_cars");
                            menuModels.add(menuModel);

                        }else if(finalObject.getString("title").toLowerCase().equals("cartrawler"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.carsDescription);
                            menuModel.setTitle(R.string.cars);
                            menuModel.setType("9cartrawler");
                            menuModels.add(menuModel);

                        }else if(finalObject.getString("title").toLowerCase().equals("coupons"))
                        {
                            editor.putBoolean("coupons",true);
                            editor.apply();

                        }else if(finalObject.getString("title").toLowerCase().equals("offers"))
                        {
                            menuModel = new MenuModel();
                            menuModel.setDescription(R.string.offersDescription);
                            menuModel.setTitle(R.string.offers);
                            menuModel.setType("default_offers");
                            menuModels.add(menuModel);
                        }
                    }
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
            s[0]="false";
            s[1]=c.getString(R.string.error_api);
            return s;
        }
        return s;
    }

    @Override
    protected void onPostExecute(String[] aVoid) {
        super.onPostExecute(aVoid);
         if(aVoid[0].equals("false"))
        {
            Toast.makeText(c,s[1],Toast.LENGTH_LONG).show();
        }else
        {
            if(aVoid[0].equals("true"))
                changeLang(s[1]);

            c.startActivity(intent);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        c.getResources().updateConfiguration(config, c.getResources().getDisplayMetrics());
        Constant.default_lang=lang;
    }
    public void saveLocale(String lang, int id)
    {
        String langPref = "Language";
        editor.putString(langPref, lang);
        editor.putInt("check_rtl", id);
        editor.apply();
    }

}
