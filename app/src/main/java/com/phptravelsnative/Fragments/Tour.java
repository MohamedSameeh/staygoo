package com.phptravelsnative.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Activities.SearchingCarTourOffers;
import com.phptravelsnative.Adapters.AutoSuggestedAdapter;
import com.phptravelsnative.Models.Auto_Model;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Network.Parser.TourRequest;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Extra.DateSeter;
import com.phptravelsnative.utality.Views.LightSpinner;
import com.phptravelsnative.utality.Views.TourSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class Tour extends Fragment {

    AutoCompleteTextView auto_tour;
    ArrayList<Auto_Model> auto_array_list=new ArrayList<>();
    ArrayList<Auto_Model> tour_type=new ArrayList<>();

    String tour_type_arr[];
    String type_id[];
    boolean first_=false;


    SharedPreferences sharedPreferences;
    String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    DateSeter checkDate;
    TextView date_from;
    AutoSuggestedAdapter adapter;
    TourSpinner tourSpinner;
    String checkType="";
    ImageView clear_btn;
    ImageView tour_icon;
    int my_var;
    LightSpinner adult;
    ProgressBar pb;
    Button search;
    View inflated;
    int year_from;
    String day_from;
    int month_from;
    private String COUNTRY_URL =
            Constant.domain_name+"tours/suggestions?appKey="+Constant.key+"&query=";
    private String item_url =
            Constant.domain_name+"tours/tourtypes?appKey="+Constant.key+"&lang="+Constant.default_lang;
    public Tour() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkDate = new DateSeter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflated= inflater.inflate(R.layout.activity_tours, container, false);

        auto_tour= (AutoCompleteTextView) inflated.findViewById(R.id.tour_auto_search);
        clear_btn= (ImageView) inflated.findViewById(R.id.clearButton);

        auto_tour.setText(sharedPreferences.getString("tour_search",""));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        my_var=sharedPreferences.getInt("tour_search_id",0);
        checkType=sharedPreferences.getString("tour_search_type","");

        first_=false;


        if(auto_tour.getText().length()>0)
            clear_btn.setVisibility(View.VISIBLE);
        else
            clear_btn.setVisibility(View.GONE);

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_tour.setText("");
            }
        });
        auto_tour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=2 && first_) {
                    my_var = 0;
                    checkType="";
                    checkResult(s.toString());
                }
                first_=true;
                if(auto_tour.getText().length()>0)
                    clear_btn.setVisibility(View.VISIBLE);
                else
                    clear_btn.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        auto_tour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                my_var = adapter.getItem(position).getId();
                checkType=adapter.getItem(position).getType();
                editor.putString("tour_search",auto_tour.getText().toString());
                editor.putInt("tour_search_id",my_var);
                editor.putString("tour_search_type",checkType);
                editor.apply();
                View keyBoard =getActivity().getCurrentFocus();
                if (keyBoard != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(keyBoard.getWindowToken(), 0);
                }
            }
        });


        date_from=(TextView)inflated.findViewById(R.id.date_from);
        search=(Button) inflated.findViewById(R.id.search_hotels);
        tour_icon=(ImageView) inflated.findViewById(R.id.tour_icon);
        tour_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourSpinner.performClick();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton();
            }
        });

        adult=(LightSpinner) inflated.findViewById(R.id.adult);
        tourSpinner=(TourSpinner) inflated.findViewById(R.id.tour_type);
        adult.setText(String.format("%s 2", getString(R.string.adult)));
        adult.setTag("2");



        pb= (ProgressBar) inflated.findViewById(R.id.tour_type_progress);
        checkResultForTerm();

        date_from.setText(checkDate.getCurrentDay()+"/"+checkDate.getCurrentMonth()+"/"+checkDate.getCurrentYear());

        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHotelSelect(v);
            }
        });
        year_from=checkDate.getCurrentYear();
        day_from=checkDate.getCurrentDay();
        month_from=checkDate.getCurrentMonthN();
        return inflated;
    }
    public void onClickButton()
    {

        Bundle bundle=new Bundle();
        Model_Tour t=new Model_Tour();
        Intent intent;

        if (auto_tour.getText().toString().equals("")) {

            t.setId(0);
            t.setType(tourSpinner.getTag().toString());
            t.setDate(get_date_from());
            t.setTitle(auto_tour.getText().toString());
            t.setGusest(adult.getText().charAt(adult.length()-1)+"");
            bundle.putParcelable("tours",t);
            intent=new Intent(getContext(),SearchingCarTourOffers.class);
            intent.putExtra("type","tour");
            intent.putExtras(bundle);

            startActivity(intent);

        }else
        {

            t.setId(my_var);
            t.setType(tourSpinner.getTag().toString());
            t.setDate(get_date_from());
            t.setTitle(auto_tour.getText().toString());
            t.setGusest(adult.getText().charAt(adult.length()-1)+"");
            bundle.putParcelable("tours",t);

            if(checkType.equals("tour"))
            {
                TourRequest tourRequest=new TourRequest(getContext(),my_var,t);
                tourRequest.checkResult();

            }else if(checkType.equals("location")){
                intent=new Intent(getContext(),SearchingCarTourOffers.class);
                intent.putExtra("type","tour");
                intent.putExtras(bundle);
                startActivity(intent);
            }else
            {

                Toast.makeText(getContext(),"Nothing Found"+auto_tour.getText().toString(),Toast.LENGTH_LONG).show();
            }
        }

    }
    public  void checkResult(String term)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,COUNTRY_URL+term+"&lang="+Constant.default_lang, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                auto_array_list.clear();
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONArray jsonArray= parentObject.getJSONArray("response");
                    Auto_Model auto_model;
                    for (int i = 0; i <jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        if(!jo.getString("text").equals("")) {
                            auto_model = new Auto_Model();
                            auto_model.setType(jo.getString("module"));
                            auto_model.setName(jo.getString("text"));
                            auto_model.setId(jo.getInt("id"));
                            if(jo.getString("module").equals("tour"))
                            {
                                auto_model.setImage_id(R.drawable.ic_tour_icon);
                            }else
                            {
                                auto_model.setImage_id(R.drawable.location_search);

                            }
                            auto_array_list.add(auto_model);
                        }
                    }
                }
                catch(JSONException e){

                    Log.d("abcwwwwd",e.getMessage());
                }
                if(adapter==null) {
                    adapter = new AutoSuggestedAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, auto_array_list);
                    auto_tour.setAdapter(adapter);
                }else
                {
                    adapter.notifyDataSetChanged();
                    if(auto_array_list.contains(""));
                }
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
                Log.e("HamzaError" + "-", logText, error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public  void checkResultForTerm()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,item_url, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONArray jsonArray= parentObject.getJSONArray("response");
                    type_id=new String[jsonArray.length()];
                    tour_type_arr=new String[jsonArray.length()];
                    for (int i = 0; i <jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        tour_type_arr[i]=jo.getString("name");
                        type_id[i]=jo.getInt("id")+"";
                    }
                }
                catch(JSONException e){

                    Log.d("abcwwwwd",e.getMessage());
                }
                tourSpinner.setVisibility(View.VISIBLE);
                tourSpinner.listAdapter(tour_type_arr,type_id);
                pb.setVisibility(View.GONE);
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
                Log.e("HamzaError" + "-", logText, error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public String get_date_from()
    {

        return month_from+"/"+day_from+"/"+year_from;
    }
    public void OnClickHotelSelect(View v)
    {
        showDatePicker(1);

    }
    private DatePickerDialog.OnDateSetListener listener_data=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String monthName=checkDate.theMonth(month);
            date_from.setText(dayOfMonth+"/"+monthName+"/"+year);

            year_from=year;
            month_from=month;
            if(dayOfMonth<10)
                day_from= "0"+dayOfMonth;
            else
                day_from= ""+dayOfMonth;
        }
    };
    private void showDatePicker(int a) {
        RoomFragment.DatePickerFragment date = new RoomFragment.DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();

        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("a", a);
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(listener_data);
        date.show(getFragmentManager(), "Date Picker");

    }
}
