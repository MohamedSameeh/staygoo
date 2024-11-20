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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Activities.TourDetail;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 23/10/2016.
 */

public class TourRequest implements NetworkRequest {


    Context context;
    public  OverView overView;

    ArrayList<review_model> review_list=new ArrayList<>();
    ArrayList<Amenities_Model> inc_list =new ArrayList<>();
    ArrayList<Amenities_Model> exc_list=new ArrayList<>();
    ArrayList<DetailModel> arrayList=new ArrayList<>();
    Model_Tour tourM;

    public ProgressDialog dialog;
    int id;


    public TourRequest(Context c, int id, Model_Tour t) {
        context=c;
        overView=new OverView();
        dialog = new ProgressDialog(c,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setMessage(c.getString(R.string.loading));
        tourM=t;
        this.id=id;

    }

    @Override
    public  void checkResult()
    {
        Log.d("hamzaUrl",Constant.domain_name+"tours/details?appKey="+Constant.key+"&id="+id+"&lang="+Constant.default_lang);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name+"tours/details?appKey="+Constant.key+"&id="+id+"&lang="+Constant.default_lang
                ,
                new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    dialog.dismiss();
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object= parentObject.getJSONObject("response");
                    JSONArray review_array=main_object.getJSONArray("reviews");
                    JSONObject tour_object=main_object.getJSONObject("tour");

                    overView.setPolicy(tour_object.getString("policy"));
                    overView.setLatitude(tour_object.getDouble("latitude"));
                    overView.setLongitude(tour_object.getDouble("longitude"));
                    overView.setDesc(tour_object.getString("desc"));
                    overView.setTitle(tour_object.getString("title"));
                    overView.setId(tour_object.getInt("id"));

                    tourM.setId(tour_object.getInt("id"));


                    tourM.setMaxAdult(tour_object.getInt("maxAdults"));
                    tourM.setMaxChild(tour_object.getInt("maxChild"));
                    tourM.setMaxInflants(tour_object.getInt("maxInfant"));
                    tourM.setPerAdultPrice(Integer.parseInt(tour_object.getString("perAdultPrice").replace(",","")));
                    tourM.setPerChildPrice(Integer.parseInt(tour_object.getString("perChildPrice").replace(",","")));
                    tourM.setPerInfantPrice(Integer.parseInt(tour_object.getString("perInfantPrice").replace(",","")));
                    tourM.setCurrency(tour_object.getString("currCode"));

                    if(tour_object.getString("currSymbol").equals("null")){
                    tourM.setSymbolCurrency("");}
                    else {
                        tourM.setSymbolCurrency(tour_object.getString("currSymbol"));
                    }

                    JSONArray inc_array=tour_object.getJSONArray("inclusions");
                    JSONArray exc_array=tour_object.getJSONArray("exclusions");
                    JSONArray payments_array=tour_object.getJSONArray("paymentOptions");
                    JSONArray image_array= tour_object.getJSONArray("sliderImages");
                    DetailModel dm;
                    StringBuilder sbL=new StringBuilder();
                    StringBuilder sbR=new StringBuilder();
                    for(int i=0;i<payments_array.length();i++)
                    {
                        JSONObject payment_object=payments_array.getJSONObject(i);
                        if(i%2!=0)
                            sbL.append(payment_object.getString("name")+"90");
                        else
                            sbR.append(payment_object.getString("name")+"90");
                    }
                    overView.setParmentsRights(sbR.toString());
                    overView.setPaymentsLefts(sbL.toString());

                    for(int i=0;i<image_array.length();i++)
                    {
                        dm=new DetailModel();
                        JSONObject child_ob=image_array.getJSONObject(i);
                        dm.setSliderImages(child_ob.getString("thumbImage"));
                        arrayList.add(dm);
                    }

                    review_model rv;
                    for(int i=0;i<review_array.length();i++)
                    {
                        rv=new review_model();
                        JSONObject review_object=review_array.getJSONObject(i);
                        rv.setRating(review_object.getString("rating"));
                        rv.setReview_by(review_object.getString("review_by"));
                        rv.setReview_date(review_object.getString("review_date"));
                        rv.setReview_comment(review_object.getString("review_comment"));
                        review_list.add(rv);
                    }
                    Amenities_Model amenities_model;
                    for(int i=0;i<inc_array.length();i++)
                    {
                        amenities_model=new Amenities_Model();
                        JSONObject amenities_object=inc_array.getJSONObject(i);
                        amenities_model.setEan_id_image(R.drawable.ic_checked);
                        amenities_model.setName(amenities_object.getString("name"));
                        inc_list.add(amenities_model);
                    }
                    for(int i=0;i<exc_array.length();i++)
                    {
                        amenities_model=new Amenities_Model();
                        JSONObject amenities_object=exc_array.getJSONObject(i);
                        amenities_model.setEan_id_image(R.drawable.ic_exclusions);
                        amenities_model.setName(amenities_object.getString("name"));
                        exc_list.add(amenities_model);
                    }

                }
                catch(JSONException e){

                    Log.d("abcwwwwd",e.getMessage());
                }
                callmethod();

//Set circle indicator radius

            }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null)
                    return;
                String logText;
                if (error.networkResponse == null) {
                    logText = error.getMessage();
                } else {
                    logText = error.getMessage() + ", status "
                            + error.networkResponse.statusCode
                            + " - " + error.networkResponse.toString();
                }
                dialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        dialog.show();
    }
    public void callmethod() {
        Intent mIntent=new Intent(context,TourDetail.class);

        Bundle mBundle = new Bundle();
        mBundle.putParcelableArrayList("arrayList", arrayList);
        mBundle.putParcelableArrayList("review", review_list);
        mBundle.putParcelableArrayList("am", inc_list);
        mBundle.putParcelableArrayList("am2", exc_list);
        mBundle.putParcelable("tou", tourM);

        mBundle.putParcelable("ov", overView);

        mIntent.putExtras(mBundle);
        context.startActivity(mIntent);
    }
}
