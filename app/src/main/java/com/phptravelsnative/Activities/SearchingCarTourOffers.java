package com.phptravelsnative.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.Adapters.ListingAdapters;
import com.phptravelsnative.Models.HotelModel;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchingCarTourOffers extends Drawer {

    private ProgressDialog dialog;

    private  RelativeLayout bottomLayout;
    int offest=1;
    String offset_get="&offset=";
    int total_offest;

    boolean userScrolled = false;
    private ListView listView;
    ListingAdapters hotelsAdapters;
    RequestQueue requestQueue;
    TextView emptyView;
    StringRequest stringRequest;
    Response.Listener<String> response_listener;
    car_model c;
    Model_Tour t;
    ArrayList<HotelModel> list_models=new ArrayList<>();
    String url,checkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.activity_searching);
        View inflated = stub.inflate();



        TextView textView=(TextView) findViewById(R.id.NaviText);

        emptyView=(TextView)inflated.findViewById(R.id.emptyList);

        dialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setMessage(getString(R.string.loading));

        bottomLayout = (RelativeLayout)inflated.findViewById(R.id.loadItemsLayout_listView);
        Intent intent=getIntent();


        final Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.e("Error", "Intent extras are null");
            Toast.makeText(this, "No data received!", Toast.LENGTH_SHORT).show();
            finish(); // إغلاق النشاط لتجنب أخطاء أخرى
            return;
        }

        if (!bundle.containsKey("type")) {
            Log.e("Error", "Key 'type' is missing in bundle");
            Toast.makeText(this, "Missing 'type' data!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        checkType = bundle.getString("type");

        switch (checkType) {
            case "cars":
                c = bundle.getParcelable("carInfo");
                if(c.getPickupId()!=0) {
                    url = Constant.domain_name + "cars/search?appKey=" + Constant.key + "&pickupLocation=" + c.getPickupId() + "&dropoffLocation=" + c.getDropOfId() + "&pickupDate=" + c.getPickupDate() + "&dropoffDate=" + c.getDropOfDate() + "&pickupTime=" + c.getPickupTime() + "&dropoffTime=" + c.getDropOfTime() + offset_get+"1" + "&lang=" + Constant.default_lang;
                }
                else
                {
                    url = Constant.domain_name + "cars/list?appKey=" + Constant.key + "&pickupDate=" + c.getPickupDate() + "&dropoffDate=" + c.getDropOfDate() + "&pickupTime=" + c.getPickupTime() + "&dropoffTime=" + c.getDropOfTime() + offset_get+"1"  + "&lang=" + Constant.default_lang;

                }
                break;
            case "tour":
                t = new Model_Tour();
                t = bundle.getParcelable("tours");

                textView.setText(t.getTitle());

                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setSingleLine(true);
                textView.setSelected(true);

                if(t.getId()<=0) {
                    url = Constant.domain_name+"tours/list?appKey="+Constant.key+"&date=" + t.getDate() + "&type=" + t.getType() + "&adults=" + t.getGusest() + "&lang=" + Constant.default_lang+offset_get+"1" ;
                }else
                {
                    url = Constant.domain_name+"tours/search?appKey="+Constant.key+"&id=" + t.getId() + "&date=" + t.getDate() + "&type=" + t.getType() + "&adults=" + t.getGusest() + offset_get+"1" +"&lang=" + Constant.default_lang;
                }
                break;
            case "offers":
                url = Constant.domain_name+"offers/list?appKey="+Constant.key + "&lang="  + Constant.default_lang+ offset_get+"1" ;
                break;
        }

        listView=(ListView)inflated.findViewById(R.id.list_view);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (userScrolled
                        && firstVisibleItem + visibleItemCount == totalItemCount) {
                    userScrolled = false;
                    if(offest<total_offest) {
                        offest=offest+1;
                        stringRequest = new StringRequest(Request.Method.GET,
                                url+offset_get+offest,
                                response_listener, null);
                        requestQueue.add(stringRequest);
                        bottomLayout.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        response_listener = new Response.Listener<String>() {

            HotelModel hm;
            @Override
            public void onResponse(String response) {


                dialog.dismiss();

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response);

                    JSONObject error_object = parentObject.getJSONObject("error");

                    if (error_object.getBoolean("status")) {
                        Toast.makeText(getBaseContext(), error_object.getString("msg"), Toast.LENGTH_LONG).show();

                    } else {
                        if(offest==1)
                        {
                            total_offest=parentObject.getInt("totalPages");
                        }
                        JSONArray parentArray = parentObject.getJSONArray("response");
                        for (int i = 0; i < parentArray.length(); i++) {
                            hm = new HotelModel();
                            JSONObject finalObject = parentArray.getJSONObject(i);
                            hm = new HotelModel();
                            hm.setName(finalObject.getString("title"));
                            hm.setPrice(finalObject.getString("price"));
                            if (!checkType.equals("offers")) {
                                hm.setStarsCount(finalObject.getInt("starsCount"));
                                hm.setRating(finalObject.getJSONObject("avgReviews").getString("overall"));
                            }
                            hm.setThumbnail(finalObject.getString("thumbnail"));
                            hm.setcurrCode(finalObject.getString("currCode"));

                            if (finalObject.getString("currSymbol").equals("null"))
                                hm.setcurrSymbol("");
                            else{
                                hm.setcurrSymbol(finalObject.getString("currSymbol"));
                        }

                            if(!checkType.equals("offers"))
                            hm.setLocation(finalObject.getString("location"));
                            hm.setid(finalObject.getInt("id"));
                            list_models.add(hm);
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                    Log.d("Errors",e.getMessage());
                }
                if (offest == 1) {
                    if(list_models.size()<=0)
                        emptyView.setVisibility(View.VISIBLE);
                    if(checkType.equals("cars")) {
                        emptyView.setText(R.string.empty_car);
                        hotelsAdapters = new ListingAdapters(SearchingCarTourOffers.this, checkType, c, list_models);
                        listView.setAdapter(hotelsAdapters);
                    }else if(checkType.equals("tour")){
                        emptyView.setText(R.string.empty_tour);
                        hotelsAdapters = new ListingAdapters(SearchingCarTourOffers.this, checkType, t, list_models);
                        listView.setAdapter(hotelsAdapters);
                    }else if(checkType.equals("offers")){
                        emptyView.setText(R.string.empty_offers);
                        hotelsAdapters = new ListingAdapters(SearchingCarTourOffers.this, checkType, list_models);
                        listView.setAdapter(hotelsAdapters);
                    }
                } else {
                    hotelsAdapters.notifyDataSetChanged();
                    bottomLayout.setVisibility(View.GONE);
                }
            }
        };
        requestQueue = SingleTonRequest.getmInctance(this).getRequestQueue();

            stringRequest = new StringRequest(Request.Method.GET,url,
                    response_listener,null);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        dialog.show();
    }
}
