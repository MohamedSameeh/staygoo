package com.phptravelsnative.Network.Parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.phptravelsnative.Activities.HotelDetail;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.HotelModel;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.Models.rooms_model;
import com.phptravelsnative.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Default_Detail_Parser extends AsyncTask<Object,Void,String> {

    Context context;
    OverView overView;

    ArrayList<review_model> review_list;
    ArrayList<HotelModel> related_list;
    ArrayList<Amenities_Model> amenities_list;
    ArrayList<rooms_model> room_list;
    ArrayList<DetailModel> arrayList;
    ProgressDialog dialog;
    Hotel_data hotel_data;

    public Default_Detail_Parser(Context c, Hotel_data hotel)
    {
        context=c;
        this.hotel_data=hotel;
        dialog = new ProgressDialog(c,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setMessage(c.getString(R.string.loading));

        overView=new OverView();
        review_list=new ArrayList<>();
        amenities_list=new ArrayList<>();
        room_list=new ArrayList<>();
        related_list=new ArrayList<>();
        arrayList=new ArrayList<>();

    }
    @Override
    protected String doInBackground(Object... params) {



        JSONObject parentObject = null;
        try {
            parentObject = new JSONObject((String) params[0]);
            JSONObject main_object= parentObject.getJSONObject("response");
            JSONArray review_array=main_object.getJSONArray("reviews");
            JSONArray room_array=main_object.getJSONArray("rooms");
            JSONObject hotel_object=main_object.getJSONObject("hotel");

            JSONArray related_array=hotel_object.getJSONArray("relatedItems");

            overView.setPolicy(hotel_object.getString("policy"));
            overView.setLatitude(hotel_object.getDouble("latitude"));
            overView.setLongitude(hotel_object.getDouble("longitude"));
            overView.setDesc(hotel_object.getString("desc"));
            overView.setTitle(hotel_object.getString("title"));
            overView.setId(hotel_object.getInt("id"));

            JSONArray amenities_array=hotel_object.getJSONArray("amenities");
            JSONArray payments_array=hotel_object.getJSONArray("paymentOptions");
            JSONArray image_array= hotel_object.getJSONArray("sliderImages");
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
            HotelModel hm;
            for (int i = 0; i < related_array.length(); i++) {
                JSONObject finalObject = related_array.getJSONObject(i);
                hm = new HotelModel();
                hm.setName(finalObject.getString("title"));
                hm.setPrice(finalObject.getString("price"));
                hm.setStarsCount(finalObject.getInt("starsCount"));
                hm.setThumbnail(finalObject.getString("thumbnail"));
                hm.setRating(finalObject.getString("location") );
                hm.setcurrCode(finalObject.getString("currCode"));

                if(finalObject.getString("currSymbol").equals("null"))
                    hm.setcurrSymbol("");
                else
                hm.setcurrSymbol(finalObject.getString("currSymbol"));

                hm.setLocation(finalObject.getJSONObject("avgReviews").getString("overall"));
                hm.setid(finalObject.getInt("id"));
                related_list.add(hm);
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
            rooms_model rm;
            for(int i=0;i<room_array.length();i++)
            {
                JSONObject room_object=room_array.getJSONObject(i);
                if(room_object.getInt("maxQuantity")>0) {
                    rm = new rooms_model();
                    rm.setId(room_object.getString("id"));
                    rm.setStay(room_object.getJSONObject("Info").getString("stay"));
                    rm.setQuantity(room_object.getJSONObject("Info").getInt("quantity"));
                    rm.setTitle(room_object.getString("title"));
                    rm.setCurrencyCode(room_object.getString("currCode"));
                    rm.setPrice(room_object.getString("price"));
                    rm.setImage(room_object.getString("thumbnail"));
                    room_list.add(rm);
                }
            }
            review_model rv;
            for(int i=0;i<review_array.length();i++)
            {
                rv=new review_model();
                JSONObject review_object=review_array.getJSONObject(i);
                rv.setRating(review_object.getString("review_overall"));
                rv.setReview_by(review_object.getString("review_name"));
                rv.setReview_date(review_object.getString("review_date"));
                rv.setReview_comment(review_object.getString("review_comment"));
                review_list.add(rv);
            }
            Amenities_Model amenities_model;
            for(int i=0;i<amenities_array.length();i++)
            {
                amenities_model=new Amenities_Model();
                JSONObject amenities_object=amenities_array.getJSONObject(i);
                amenities_model.setIcon(amenities_object.getString("icon"));
                amenities_model.setName(amenities_object.getString("name"));
                amenities_list.add(amenities_model);
            }

        }
        catch(JSONException e){

            Log.d("abcwwwwd",e.getMessage());
        }
        return "Olamba";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Intent mIntent=new Intent(context,HotelDetail.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelableArrayList("arrayList", arrayList);
        mBundle.putParcelableArrayList("review", review_list);
        mBundle.putParcelableArrayList("room", room_list);
        mBundle.putParcelableArrayList("am", amenities_list);
        mBundle.putParcelableArrayList("related", related_list);
        mBundle.putParcelable("ov", overView);
        mBundle.putParcelable("hotel", hotel_data);

        mIntent.putExtras(mBundle);
        DetailRequest.dialog.dismiss();

        context.startActivity(mIntent);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
