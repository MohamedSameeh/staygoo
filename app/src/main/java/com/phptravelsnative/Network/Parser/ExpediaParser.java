package com.phptravelsnative.Network.Parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.phptravelsnative.Activities.ExpediaDetail;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.ExpediaExtra;
import com.phptravelsnative.Models.HotelModel;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.Models.rooms_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExpediaParser extends AsyncTask<Object,Void,String> {


    String seesionId;
    Context context;
    OverView overView;

    ArrayList<review_model> review_list;
    ArrayList<HotelModel> related_list;
    ArrayList<Amenities_Model> amenities_list;
    ArrayList<rooms_model> room_list;
    ArrayList<DetailModel> arrayList;
    ProgressDialog dialog;
    Hotel_data hotel_data;
    String result="";
    ExpediaExtra expediaExtra;
    public ExpediaParser(Context c, ExpediaExtra expediaExtra, Hotel_data hotel_data) {
        this.hotel_data=hotel_data;
        this.expediaExtra=expediaExtra;

        overView=new OverView();
        review_list=new ArrayList<>();
        amenities_list=new ArrayList<>();
        room_list=new ArrayList<>();
        related_list=new ArrayList<>();
        arrayList=new ArrayList<>();
        this.context=c;
    }
    @Override
    protected String doInBackground(Object... params) {


            int room_size = 0;
            JSONObject parentObject = null;
            try {
                parentObject = new JSONObject((String) params[0]);
                JSONObject main_object = parentObject.getJSONObject("response");
                JSONObject hotel_info = main_object.getJSONObject("hoteldata");
                JSONObject room_response = main_object.getJSONObject("roomsdata");
                if (room_response.getBoolean("hasRooms")) {

                    overView.setPolicy(hotel_info.getString("checkInInstructions"));

                    hotel_data.setChild(hotel_data.getChild()+"900"+main_object.getString("testingMode"));

                    JSONArray hotel_Images = hotel_info.getJSONArray("sliderImages");

                    JSONArray amenities =hotel_info.getJSONArray("amenities");



                    Amenities_Model amenities_model;
                    for (int i = 0; i < amenities.length(); i++) {
                        amenities_model = new Amenities_Model();
                        JSONObject amenities_object = amenities.getJSONObject(i);
                        amenities_model.setName(amenities_object.getString("name"));
                        amenities_list.add(amenities_model);
                    }

                    overView.setTitle(hotel_info.getString("title"));
                    overView.setSessionId(seesionId);

                    overView.setLatitude(hotel_info.getDouble("latitude"));
                    overView.setLongitude(hotel_info.getDouble("longitude"));

                    overView.setDesc(hotel_info.getString("desc"));
                    overView.setId(hotel_info.getInt("id"));


                    DetailModel dm;

                    for (int i = 0; i < hotel_Images.length() && i <= 24; i++) {
                        dm = new DetailModel();
                        JSONObject child_ob = hotel_Images.getJSONObject(i);
                        dm.setSliderImages(child_ob.getString("thumbImage"));
                        arrayList.add(dm);
                    }

                        JSONArray room_array = room_response.getJSONArray("roomsList");
                        rooms_model rm;
                        for (int i = 0; i < room_array.length(); i++) {
                            JSONObject room_object = room_array.getJSONObject(i);
                            rm = new rooms_model();
                            rm.setId(room_object.getString("id"));
                            rm.setTitle(room_object.getString("title"));
                            rm.setImage(room_object.getString("thumbnail"));
                            rm.setStay("For "+main_object.getString("totalStay")+" Nights");
                            rm.setCurrencyCode(room_object.getString("currency"));
                            rm.setPrice(room_object.getString("price"));
                            room_list.add(rm);
                        }

                    JSONObject related = main_object.getJSONObject("relatedItems");
                    if(related.getString("errorMsg").equals("")) {
                        JSONArray relatedArray = related.getJSONArray("hotels");
                        HotelModel hm;
                        for (int i = 0; i < relatedArray.length(); i++) {
                            JSONObject relatedItem = relatedArray.getJSONObject(i);
                            hm = new HotelModel();
                            hm.setRating(relatedItem.getString("location"));
                            hm.setStarsCount(relatedItem.getInt("rating"));
                            hm.setName(relatedItem.getString("title"));
                            hm.setPrice(relatedItem.getString("price"));
                            hm.setThumbnail(relatedItem.getString("thumbnail"));
                            hm.setcurrCode(relatedItem.getString("currCode"));
                            hm.setid(relatedItem.getInt("id"));
                            related_list.add(hm);
                        }
                    }

                } else {
                    result="Room Not Available";
                    DetailRequest.dialog.dismiss();
                }

            } catch (JSONException e) {

                Log.d("abcwwwwd", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute (String aVoid){
            super.onPostExecute(aVoid);

            if(aVoid.equals("")) {
                Intent mIntent = new Intent(context, ExpediaDetail.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelableArrayList("arrayList", arrayList);
                mBundle.putParcelableArrayList("review", review_list);
                mBundle.putParcelableArrayList("room", room_list);
                mBundle.putParcelableArrayList("am", amenities_list);
                mBundle.putParcelableArrayList("related_list", related_list);
                mBundle.putParcelable("hotel", hotel_data);
                mBundle.putParcelable("ex_hotel", expediaExtra);
                mBundle.putParcelable("ov", overView);

                mIntent.putExtras(mBundle);
                context.startActivity(mIntent);
            }else{

                Toast.makeText(context,"Room Not Avaliable",Toast.LENGTH_LONG).show();
            }

            DetailRequest.dialog.dismiss();

        }

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
        }
    }

