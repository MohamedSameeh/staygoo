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
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchingHotels extends Drawer {


    ArrayList<HotelModel> list_models = new ArrayList<>();
    ListView listView;
    boolean userScrolled = false;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    Response.Listener<String> response_listener;
    Response.Listener<String> response_listener_expedia;
    ListingAdapters hotelsAdapters;
    private RelativeLayout bottomLayout;
    int offest = 1;
    String offset_get = "&offset=";
    int total_offest;
    Boolean moreEan = false;
    Boolean check_ean;
    String Sesstion_id;
    TextView emptyView;
    String cache_key;
    String cache_location;
    Hotel_data hotel_data;
    String location[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.activity_searching);
        View inflated = stub.inflate();

        dialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setMessage(getString(R.string.loading));

        bottomLayout = inflated.findViewById(R.id.loadItemsLayout_listView);

        Intent intent = getIntent();
        check_ean = intent.getBooleanExtra("check_ean", true);
        hotel_data = intent.getParcelableExtra("hotel");

        // Error Handling for Null Data
        if (hotel_data == null) {
            hotel_data = new Hotel_data(); // Prevent null issues
            Log.e("SearchingHotels", "Hotel data is null. Using default object.");
        }


        if (hotel_data.getLocation() != null && !hotel_data.getLocation().isEmpty()) {
            textView.setText(hotel_data.getLocation());
            location = hotel_data.getLocation()
                    .replaceAll("-", ",")
                    .replaceAll(" ", ",")
                    .split(",");
        } else {
            textView.setText("Unknown Location");
            location = new String[0]; // Initialize as empty array
            Log.e("SearchingHotels", "Hotel location is null or empty");
        }


        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setSelected(true);

        listView = inflated.findViewById(R.id.list_view);
        emptyView = inflated.findViewById(R.id.emptyList);

        listView = (ListView) inflated.findViewById(R.id.list_view);

        emptyView=(TextView)inflated.findViewById(R.id.emptyList);

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
                    if (offest < total_offest) {
                        offest = offest + 1;
                        if (hotel_data.getLocation().equals("")) {
                            stringRequest = new StringRequest(Request.Method.GET,
                                    Constant.domain_name + "hotels/list?appKey=" + Constant.key + "&checkin=" + hotel_data.getFrom() + "&checkout=" + hotel_data.getTo() + "&child=" + hotel_data.getChild() + "&adults=" + hotel_data.getAdult() + offset_get + offest + "&lang=" + sharedPreferences.getString("Language", "en")
                                    , response_listener, null);
                        }
                        else
                            stringRequest = new StringRequest(Request.Method.GET,
                                    Constant.domain_name + "hotels/search?appKey=" + Constant.key + "&searching=" + hotel_data.getId() + "&checkin=" + hotel_data.getFrom() + "&checkout=" + hotel_data.getTo() + "&child=" + hotel_data.getChild() + "&adults=" + hotel_data.getAdult() + offset_get + offest + "&lang=" + sharedPreferences.getString("Language", "en")
                                    , response_listener, null);

                        requestQueue.add(stringRequest);
                        bottomLayout.setVisibility(View.VISIBLE);
                    }
                    if (moreEan) {
                        stringRequest = new StringRequest(Request.Method.GET,
                                Constant.domain_name + "expedia/listMore?appKey=&customerSessionId=" + Sesstion_id + "&cacheKey=" + cache_key + "&cacheLocation=" + cache_location+"&locale="+HashMapCompare(sharedPreferences.getString("Language", "en")), response_listener_expedia,
                                null);
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
                        if (offest == 1) {
                            total_offest = parentObject.getInt("totalPages");
                        }
                        JSONArray parentArray = parentObject.getJSONArray("response");
                        for (int i = 0; i < parentArray.length(); i++) {
                            hm = new HotelModel();
                            JSONObject finalObject = parentArray.getJSONObject(i);
                            hm = new HotelModel();
                            hotel_data.setId(finalObject.getInt("id"));
                            hm.setName(finalObject.getString("title"));
                            hm.setPrice(finalObject.getString("price"));
                            hm.setStarsCount(finalObject.getInt("starsCount"));
                            hm.setRating(finalObject.getJSONObject("avgReviews").getString("overall"));
                            hm.setThumbnail(finalObject.getString("thumbnail"));
                            hm.setcurrCode(finalObject.getString("currCode"));

                            if(finalObject.getString("currSymbol").equals("null"))
                                hm.setcurrSymbol("");
                            else
                                hm.setcurrSymbol(finalObject.getString("currSymbol"));


                            hm.setLocation(finalObject.getString("location"));
                            hm.setid(finalObject.getInt("id"));
                            list_models.add(hm);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (offest == 1) {
                    hotelsAdapters = new ListingAdapters(SearchingHotels.this, list_models, hotel_data, "hotel_default");
                    listView.setAdapter(hotelsAdapters);
                    if (hotelsAdapters.getCount()==0)
                         emptyView.setVisibility(View.VISIBLE);
                } else {
                    hotelsAdapters.notifyDataSetChanged();
                    bottomLayout.setVisibility(View.GONE);
                }

            }
        };
        response_listener_expedia = new Response.Listener<String>() {

            HotelModel hm;

            @Override
            public void onResponse(String response) {


                dialog.dismiss();

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response);
                    JSONObject main_object = parentObject.getJSONObject("HotelListResponse");

                    moreEan = main_object.getBoolean("moreResultsAvailable");
                    Sesstion_id = main_object.getString("customerSessionId");


                    if (moreEan) {
                        cache_key = main_object.getString("cacheKey");
                        cache_location = main_object.getString("cacheLocation");

                    }
                    JSONObject hotel_object = main_object.getJSONObject("HotelList");

                    JSONArray parentArray = hotel_object.getJSONArray("HotelSummary");
                    for (int i = 0; i < parentArray.length(); i++) {
                        hm = new HotelModel();
                        JSONObject finalObject = parentArray.getJSONObject(i);
                        hm = new HotelModel();
                        hm.setName(finalObject.getString("name"));
                        hm.setPrice(finalObject.getString("highRate"));
                        hm.setStarsCount(finalObject.getInt("hotelRating"));

                        hm.setLocation(finalObject.getString("city"));


                        hm.setThumbnail("http://media.expedia.com" + finalObject.getString("thumbNailUrl").replace("_t", "_b"));
                        hm.setcurrCode(finalObject.getString("rateCurrencyCode"));
                        hm.setcurrSymbol("");
                        hm.setid(finalObject.getInt("hotelId"));

                          hm.setRateCode(finalObject.getJSONObject("RoomRateDetailsList").getJSONObject("RoomRateDetails").getString("rateCode"));

                          hm.setRoomTypeCode(finalObject.getJSONObject("RoomRateDetailsList").getJSONObject("RoomRateDetails").getString("roomTypeCode"));
                          hm.setRateKey(finalObject.getJSONObject("RoomRateDetailsList").getJSONObject("RoomRateDetails").getJSONObject("RateInfos").getJSONObject("RateInfo").getJSONObject("RoomGroup").getJSONObject("Room").getString("rateKey"));

                        list_models.add(hm);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (hotelsAdapters == null) {
                    if(list_models.size()<=0)
                        emptyView.setVisibility(View.VISIBLE);

                    hotelsAdapters = new ListingAdapters(SearchingHotels.this, list_models, false, Sesstion_id, hotel_data, "ex_hotel");
                    listView.setAdapter(hotelsAdapters);
                } else {
                    hotelsAdapters.notifyDataSetChanged();
                    bottomLayout.setVisibility(View.GONE);
                }
            }
        };

        requestQueue = SingleTonRequest.getmInctance(this).getRequestQueue();
        if (check_ean) {
            String url;

            if (location.length > 0 && !location[0].trim().isEmpty()) {
                url = Constant.domain_name + "expedia/search?appKey=" + Constant.key
                        + "&location=" + location[0].trim()
                        + "&checkIn=" + hotel_data.getFrom()
                        + "&checkOut=" + hotel_data.getTo()
                        + "&adults=" + hotel_data.getAdult()
                        + "&child=" + hotel_data.getChild()
                        + "&locale=" + HashMapCompare(sharedPreferences.getString("Language", "en"));
            } else {
                url = Constant.domain_name + "expedia/list?appKey=" + Constant.key
                        + "&checkIn=" + hotel_data.getFrom()
                        + "&checkOut=" + hotel_data.getTo()
                        + "&adults=" + hotel_data.getAdult()
                        + "&child=" + hotel_data.getChild()
                        + "&locale=" + HashMapCompare(sharedPreferences.getString("Language", "en"));
            }

            stringRequest = new StringRequest(Request.Method.GET,url
                    , response_listener_expedia, null
            );
        } else {

            if (hotel_data.getId() == 0) {
                stringRequest = new StringRequest(Request.Method.GET,
                        Constant.domain_name + "hotels/list?appKey=" + Constant.key + "&checkin=" + hotel_data.getFrom() + "&checkout=" + hotel_data.getTo() + "&child=" + hotel_data.getChild() + "&adults=" + hotel_data.getAdult() + offset_get + offest + "&lang=" + sharedPreferences.getString("Language", "en")
                        , response_listener, null);
            } else
                stringRequest = new StringRequest(Request.Method.GET,
                        Constant.domain_name + "hotels/search?appKey=" + Constant.key + "&searching=" + hotel_data.getId() + "&checkin=" + hotel_data.getFrom() + "&checkout=" + hotel_data.getTo() + "&child=" + hotel_data.getChild() + "&adults=" + hotel_data.getAdult() + offset_get + offest + "&lang=" + sharedPreferences.getString("Language", "en")
                        , response_listener, null);

        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        dialog.show();
    }
    public static String HashMapCompare(String s)
    {

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("en","en_US");
        hashMap.put("ar","ar_SA");
        hashMap.put("cs","cs_CZ");
        hashMap.put("da","da_DK");
        hashMap.put("de","de_DE");
        hashMap.put("el","el_GR");
        hashMap.put("es","es_ES");
        hashMap.put("et","et_EE");
        hashMap.put("fi","fi_FI");
        hashMap.put("fr","fr_FR");
        hashMap.put("hu","hu_HU");
        hashMap.put("hr","hr_HR");
        hashMap.put("id","id_ID");
        hashMap.put("is","is_IS");
        hashMap.put("it","it_IT");
        hashMap.put("ja","ja_JP");
        hashMap.put("ko","ko_KR");
        hashMap.put("ms","ms_MY");
        hashMap.put("lv","lv_LV");
        hashMap.put("lt","lt_LT");
        hashMap.put("nl","nl_NL");
        hashMap.put("no","no_NO");
        hashMap.put("pl","pl_PL");
        hashMap.put("pt","pt_BR");
        hashMap.put("ru","ru_RU");
        hashMap.put("sv","sv_SE");
        hashMap.put("sk","sk_SK");
        hashMap.put("th","th_TH");
        hashMap.put("tr","tr_TR");
        hashMap.put("uk","uk_UA");
        hashMap.put("vi","vi_VN");
        hashMap.put("zh","zh_TW");

        return  hashMap.get(s);

    }
}
