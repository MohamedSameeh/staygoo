package com.phptravelsnative.Network.Parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Activities.CarDetail;
import com.phptravelsnative.Models.Auto_Model;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 23/10/2016.
 */

public class CarRequest implements NetworkRequest {


    Context context;
    public OverView overView;
    car_model carM;
    ArrayList<DetailModel> arrayList=new ArrayList<>();
    ArrayList<Auto_Model> pickup=new ArrayList<>();
    ArrayList<Auto_Model> dropOff=new ArrayList<>();

    public  ProgressDialog dialog;
    int id;


    public CarRequest(Context c, int id, car_model car) {
        context=c;
        overView=new OverView();
        carM=car;
        dialog = new ProgressDialog(c,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setMessage(c.getString(R.string.loading));
        this.id=id;

    }

    @Override
    public  void checkResult()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name+"cars/details?appKey="+Constant.key+"&id="+id+"&pickupLocation="+carM.getPickupId()+"&dropoffLocation="+carM.getDropOfId()+"&pickupDate="+carM.getPickupDate()+"&dropoffDate="+carM.getDropOfDate()+"&pickupTime="+carM.getPickupTime()+"&dropoffTime="+carM.getDropOfTime()+"&lang="+Constant.default_lang,
                new Response.Listener() {
            @Override
            public void onResponse(Object response) {


                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object= parentObject.getJSONObject("response");
                    JSONObject car_object= main_object.getJSONObject("car");

                    if(!car_object.getString("pickupLocation").equals("false"))
                    {

                        overView.setTitle(car_object.getString("title"));
                        carM.setId(car_object.getInt("id"));
                        overView.setLatitude(car_object.getDouble("latitude"));
                        overView.setLongitude(car_object.getDouble("longitude"));
                        overView.setDesc(car_object.getString("desc"));
                        overView.setPolicy(car_object.getString("policy"));

                        if(car_object.getString("currSymbol").equals("null"))
                        {
                            carM.setTotalPrice(car_object.getString("currCode")+" "+car_object.getString("totalCost"));
                            carM.setDepositePrice(car_object.getString("currCode")+" "+car_object.getString("totalDeposit"));
                            carM.setTaxPrice(car_object.getString("currCode")+" "+car_object.getString("taxValue"));
                        }else
                        {
                            carM.setTotalPrice(car_object.getString("currCode")+" "+car_object.getString("currSymbol")+" "+car_object.getString("totalCost"));
                            carM.setDepositePrice(car_object.getString("currCode")+" "+car_object.getString("currSymbol")+" "+car_object.getString("totalDeposit"));
                            carM.setTaxPrice(car_object.getString("currCode")+" "+car_object.getString("currSymbol")+" "+car_object.getString("taxValue"));
                        }


                        JSONArray payments_array=car_object.getJSONArray("paymentOptions");
                        JSONArray image_array= car_object.getJSONArray("sliderImages");
                        JSONArray pickup_array= car_object.getJSONArray("pickupLocationList");
                        JSONArray dropOff_array= car_object.getJSONArray("dropoffLocationList");


                        Auto_Model am;
                        for(int i=0;i<pickup_array.length();i++)
                        {
                            JSONObject ob=pickup_array.getJSONObject(i);
                            am=new Auto_Model();
                            am.setId(ob.getInt("id"));
                            am.setName(ob.getString("name"));
                            pickup.add(am);
                        }
                        for(int i=0;i<dropOff_array.length();i++)
                        {
                            JSONObject ob=dropOff_array.getJSONObject(i);
                            am=new Auto_Model();
                            am.setId(ob.getInt("id"));
                            am.setName(ob.getString("name"));
                            dropOff.add(am);
                        }

                        DetailModel dm;
                        for(int i=0;i<image_array.length();i++)
                        {
                            dm=new DetailModel();
                            JSONObject child_ob=image_array.getJSONObject(i);
                            dm.setSliderImages(child_ob.getString("thumbImage"));
                            arrayList.add(dm);
                        }

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

                    }else
                    {
                        Toast.makeText(context,"Please Specify Correct Location",Toast.LENGTH_LONG).show();

                    }

                }
                catch(JSONException e){

                    Log.d("abcwwwwd",e.getMessage());
                }
                callmethod();

//Set circle indicator radius

            }
        }, null);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        dialog.show();
    }
    public void callmethod() {
        Intent mIntent=new Intent(context,CarDetail.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("ov", overView);
        mBundle.putParcelable("car",carM);
        mBundle.putParcelableArrayList("arrayList",arrayList);
        mBundle.putParcelableArrayList("pickup",pickup);
        mBundle.putParcelableArrayList("DropOff",dropOff);
        dialog.dismiss();
        mIntent.putExtras(mBundle);
        context.startActivity(mIntent);
    }
}
