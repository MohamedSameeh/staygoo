package com.phptravelsnative.Network.Parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Activities.OffersDetails;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OfffersRequest implements NetworkRequest {


    Context context;
    public  OverView overView;
    ArrayList<DetailModel> arrayList=new ArrayList<>();
    public static ProgressDialog progressDialog;
    int id;


    public OfffersRequest(Context c, int id) {
        context=c;
        progressDialog = new ProgressDialog(c,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(c.getString(R.string.loading));
        overView=new OverView();
        this.id=id;

    }

    @Override
    public  void checkResult()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name+"offers/offerdetails?appKey="+Constant.key+"&id="+id+"&lang="+ Constant.default_lang
                ,new Response.Listener() {
            @Override
            public void onResponse(Object response) {


                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object= parentObject.getJSONObject("response");
                    JSONArray image_array= main_object.getJSONArray("sliderImages");

                        overView.setTitle(main_object.getString("title"));
                        overView.setId(main_object.getInt("id"));
                        overView.setDesc(main_object.getString("desc"));
                        overView.setPolicy(main_object.getString("phone"));
                        overView.setLongitude(0.0);
                        overView.setLatitude(0.0);

                        DetailModel dm;
                        for(int i=0;i<image_array.length();i++) {
                            dm = new DetailModel();
                            JSONObject child_ob = image_array.getJSONObject(i);
                            dm.setSliderImages(child_ob.getString("thumbImage"));
                            arrayList.add(dm);

                        }
                }
                catch(JSONException e){

                    Log.d("abcwwwwd",e.getMessage());
                }
                callmethod();
                progressDialog.dismiss();

//Set circle indicator radius

            }
        }, null);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        progressDialog.show();
    }
    public void callmethod() {
        Intent mIntent=new Intent(context,OffersDetails.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("ov", overView);
        mBundle.putParcelableArrayList("arrayList",arrayList);
        mIntent.putExtras(mBundle);
        context.startActivity(mIntent);
    }
}
