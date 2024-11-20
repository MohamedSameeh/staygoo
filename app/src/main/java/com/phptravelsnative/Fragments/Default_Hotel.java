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
import com.phptravelsnative.Activities.SearchingHotels;
import com.phptravelsnative.Adapters.AutoSuggestedAdapter;
import com.phptravelsnative.Models.Auto_Model;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Network.Parser.DetailRequest;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Extra.DateSeter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Default_Hotel extends Fragment {

    Hotel_data hotel_data;
    SharedPreferences sharedPreferences;
    String MyPREFERENCES = "MyPrefs";
    AutoSuggestedAdapter adapter;
    ArrayList<Auto_Model> auto_array_list=new ArrayList<>();
    TextView date_from, date_to;
    int year_to, month_to;
    boolean first_=false;

    DateSeter checkDate;

    int year_from, month_from;
    String day_from;
    String day_to;
    int day_to_int;
    private String COUNTRY_URL =
            Constant.domain_name+"hotels/suggestions?appKey="+Constant.key+"&query=";
    ArrayList<Auto_Model> list=new ArrayList<>();

    TextView child,adult;
    View inflated;
    String checkType="";
    AutoCompleteTextView hotel_name;
    int my_var;
    ImageView clear_btn;

    Boolean check=false;
    Boolean check_from=false;
    DatePickerDialog date2;
    SharedPreferences.Editor editor;

    Button search;

    public Default_Hotel() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflated  =  inflater.inflate(R.layout.hotels_layout, container, false);

        hotel_data = new Hotel_data();
        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkDate = new DateSeter(getContext());

        hotel_name= (AutoCompleteTextView) inflated.findViewById(R.id.tour_auto_search);

        clear_btn= (ImageView) inflated.findViewById(R.id.clearButton);

        hotel_name.setText(sharedPreferences.getString("hotel_search",""));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        my_var=sharedPreferences.getInt("hotel_search_id",0);
        checkType=sharedPreferences.getString("hotel_search_type","");
        first_=false;

        adult= (TextView) inflated.findViewById(R.id.adult);
        child= (TextView) inflated.findViewById(R.id.child);

        adult.setText(String.format("%s 2", getString(R.string.adult)));
        child.setText(String.format("%s 0", getString(R.string.child)));

        if(hotel_name.getText().length()>0)
            clear_btn.setVisibility(View.VISIBLE);
        else
            clear_btn.setVisibility(View.GONE);

        date_from=(TextView)inflated.findViewById(R.id.date_from);
        date_to=(TextView)inflated.findViewById(R.id.date_to);

        search= (Button) inflated.findViewById(R.id.search_hotels);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton();
            }
        });

        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHotelSelect(date_from);
            }
        });
        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHotelSelect(date_to);
            }
        });

        date2=new DatePickerDialog(getContext(), R.style.DialogTheme,listener_data2,year_to,month_to,day_to_int);


        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotel_name.setText("");
            }
        });
        search_hotel();



        date_from.setText(checkDate.getCurrentDay()+"/"+checkDate.getCurrentMonth()+"/"+checkDate.getCurrentYear());

        year_from=checkDate.getCurrentYear();
        day_from=checkDate.getCurrentDay();
        month_from=checkDate.getCurrentMonthN();

        checkDate.incrementBy();

        date_to.setText(checkDate.getCurrentDay()+"/"+checkDate.getCurrentMonth()+"/"+checkDate.getCurrentYear());

        year_to=checkDate.getCurrentYear();
        day_to=checkDate.getCurrentDay();
        month_to=checkDate.getCurrentMonthN();

        return inflated;
    }
    public void search_hotel() {
        hotel_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                my_var = adapter.getItem(position).getId();
                checkType=adapter.getItem(position).getType();
                editor.putString("hotel_search",hotel_name.getText().toString());
                editor.putInt("hotel_search_id",my_var);
                editor.putString("hotel_search_type",checkType);
                editor.apply();
                View keyBoard;
                keyBoard = getActivity().getCurrentFocus();
                if (keyBoard != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(keyBoard.getWindowToken(), 0);
                }
            }
        });
        hotel_name.addTextChangedListener(new TextWatcher() {
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
                if(hotel_name.getText().length()>0)
                    clear_btn.setVisibility(View.VISIBLE);
                else
                    clear_btn.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onClickButton() {
        Intent intent;
        Hotel_data data = new Hotel_data();
        data.setFrom(get_date_from());
        data.setTo(get_date_to());
        data.setAdult(adult.getText().charAt(adult.length()-1)+"");
        data.setChild(child.getText().charAt(child.length()-1)+"");
        data.setLocation(hotel_name.getText().toString());
        if (hotel_name.getText().toString().equals("")) {

            data.setId(0);
            data.setLocation(hotel_name.getText().toString());
            intent = new Intent(getContext(), SearchingHotels.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("ho", data);
            intent.putExtra("check_ean", false);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            data.setId(my_var);
            switch (checkType) {
                case "hotel":
                    DetailRequest checkDetial = new DetailRequest(getContext(), my_var, data);
                    checkDetial.checkResult();
                    break;
                case "location":
                    intent = new Intent(getContext(), SearchingHotels.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("ho", data);
                    intent.putExtra("check_ean", false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(getContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }


    public  void checkResult(String term)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                COUNTRY_URL+term+"&lang="+Constant.default_lang
                ,
                new Response.Listener() {
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

                                    if(jo.getString("module").equals("hotel"))
                                    {
                                        auto_model.setImage_id(R.drawable.hotel_search);
                                    }else
                                    {
                                        auto_model.setImage_id(R.drawable.location_search);

                                    }
                                    auto_model.setId(jo.getInt("id"));
                                    auto_array_list.add(auto_model);
                                }
                            }
                        }
                        catch(JSONException e){

                            Log.d("abcwwwwd",e.getMessage());
                        }
                        if(adapter==null) {
                            adapter = new AutoSuggestedAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, auto_array_list);
                            hotel_name.setAdapter(adapter);
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


    public void OnClickHotelSelect(View v)
    {
        if(v.getId()== R.id.date_from)
        {
            showDatePicker(1);
            if(check)
            {
                check=false;
            }
        }
        else if(v.getId()== R.id.date_to)
        {
            showDatePicker(false,0,0,0);
        }
    }
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

        date.setCallBack(listener_data);
        date.show(getFragmentManager(), "Date Picker");

    }
    private void showDatePicker(Boolean b,int year,int month,int day) {
        RoomFragment.DatePickerFragment date = new RoomFragment.DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Bundle args = new Bundle();
        if(!b) {
            Calendar calender = Calendar.getInstance();

            args.putInt("year", calender.get(Calendar.YEAR));
            args.putInt("month", calender.get(Calendar.MONTH));
            args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
            args.putInt("a", 0);
            date.setArguments(args);


            date.setCallBack(listener_data2);
        }else
        {
            args.putInt("year", year);
            args.putInt("month", month);
            args.putInt("day", day);
            args.putInt("a", 0);
            date.setArguments(args);
        }

        date.setCallBack(listener_data2);
        date.show(getFragmentManager(), "Date Picker");
    }

    private DatePickerDialog.OnDateSetListener listener_data=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String monthName =checkDate.theMonth(month);
            date_from.setText(dayOfMonth+"/"+monthName+"/"+year);

            year_from=year;
            month_from=month+1;
            year_from=year;
            if(dayOfMonth<10)
                day_from= "0"+dayOfMonth;
            else
                day_from= ""+dayOfMonth;
            date2.getDatePicker().setMinDate(0);

            Date date=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
            try {
                if(check_from) {
                    String  s=month+1+"."+dayOfMonth+"."+year;
                    date = sdf.parse(s);
                    date.setTime(date.getTime()+86400000);

                }else
                {
                    long s=System.currentTimeMillis() - 1000;
                    date.setTime(s+86400000);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            date2.getDatePicker().updateDate(year,month,dayOfMonth);
            date2.getDatePicker().setMinDate(date.getTime());
            check_from=true;
            showDatePicker(true,year,month,dayOfMonth);
        }
    };

    private DatePickerDialog.OnDateSetListener listener_data2=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String monthName=checkDate.theMonth(month);
            date_to.setText(dayOfMonth+"/"+monthName+"/"+year);
            year_to=year;
            month_to=month+1;
            if(dayOfMonth<10)
                day_to= "0"+dayOfMonth;
            else
                day_to= ""+dayOfMonth;
        }
    };
    public String get_date_from()
    {
        return month_from+"/"+day_from+"/"+year_from;
    }
    public String get_date_to()
    {
        return month_to+"/"+day_to+"/"+year_to;
    }



}
