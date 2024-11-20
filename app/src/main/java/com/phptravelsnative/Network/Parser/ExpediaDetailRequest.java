package com.phptravelsnative.Network.Parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.Activities.SearchingHotels;
import com.phptravelsnative.Models.ExpediaExtra;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;


public class ExpediaDetailRequest {


    Context context;
    ExpediaExtra expediaExtra;
    Hotel_data hotel_data;
    SharedPreferences sharedPreferences;
    int id;
    String MyPREFERENCES = "MyPrefs";


    public ExpediaDetailRequest(Context c, ExpediaExtra expediaExtra, int HotelId, Hotel_data hotel_data) {
        this.hotel_data=hotel_data;
        this.expediaExtra=expediaExtra;
        DetailRequest.dialog = new ProgressDialog(c,
                R.style.AppTheme_Dark_Dialog);
        this.id=HotelId;
        sharedPreferences = c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        DetailRequest.dialog.setIndeterminate(true);
        DetailRequest.dialog.setCancelable(true);
        DetailRequest.dialog.setMessage(c.getString(R.string.loading));
        this.context=c;
    }


    public void checkResult() {

        Log.d("hamza",Constant.domain_name+"expedia/hoteldetails?appKey="+Constant.key+"&hotelId=" + id + "&checkIn=" + hotel_data.getFrom()
                + "&checkOut=" + hotel_data.getTo() + "&adults=" + hotel_data.getAdult() + "&customerSessionId=" + expediaExtra.getSessionId()+"&locale="+ SearchingHotels.HashMapCompare(sharedPreferences.getString("Language", "en")));
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name+"expedia/hoteldetails?appKey="+Constant.key+"&hotelId=" + id + "&checkIn=" + hotel_data.getFrom()
                + "&checkOut=" + hotel_data.getTo() + "&adults=" + hotel_data.getAdult() + "&customerSessionId=" + expediaExtra.getSessionId()+"&locale="+ SearchingHotels.HashMapCompare(sharedPreferences.getString("Language", "en"))
                , new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                ExpediaParser mainMenuParse=new ExpediaParser(context,expediaExtra,hotel_data);
                mainMenuParse.execute(response);
            }
        }, null);

        RequestQueue requestQueue = SingleTonRequest.getmInctance(context).getRequestQueue();
        requestQueue.add(stringRequest);
       DetailRequest.dialog.show();

    }

}
