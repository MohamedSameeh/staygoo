package com.phptravelsnative.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Activities.SearchingCarTourOffers;
import com.phptravelsnative.Adapters.AutoSuggestedAdapter;
import com.phptravelsnative.Models.Auto_Model;
import com.phptravelsnative.Models.car_model;
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


public class car_home extends Fragment {


    AutoCompleteTextView car_to;
    AutoCompleteTextView car_from;

    TextView time_from,time_to;

    SharedPreferences sharedPreferences;
    String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;

    Boolean check=false;
    Boolean check_from=false;
    ImageView clear_btn,clear_btn_to;
    DatePickerDialog date2;
    Calendar calendar = Calendar.getInstance();
    int year_to;
    int month_to;
    String day_to;
    int year_from;
    String day_from;
    int month_from;
    int hour_from,mintue_to_s,mintue_from_s,hour_to=-1;
    View inflated;

    Button search;
    TextView date_from,date_to;
    DateSeter checkDate;
    View keyBoard;
    int car_from_id=0,car_to_id=0;

    ArrayList<Auto_Model> location_from=new ArrayList<>();
    ArrayList<Auto_Model> location_to=new ArrayList<>();
    private int day_to_int;

    public car_home() {
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
        inflated = inflater.inflate(R.layout.car_layout, container, false);

        car_to = (AutoCompleteTextView) inflated.findViewById(R.id.car_to);
        car_from = (AutoCompleteTextView) inflated.findViewById(R.id.car_from);

        car_from.setText(sharedPreferences.getString("car_from_text",""));
        car_to.setText(sharedPreferences.getString("car_to_text",""));

        car_from_id=sharedPreferences.getInt("car_from_id",0);
        car_to_id=sharedPreferences.getInt("car_to_id",0);

        date_from=(TextView)inflated.findViewById(R.id.date_from);
        date_to=(TextView)inflated.findViewById(R.id.date_to);

        clear_btn=(ImageView) inflated.findViewById(R.id.clearButton);
        clear_btn_to=(ImageView) inflated.findViewById(R.id.clearButton_to);
        search=(Button) inflated.findViewById(R.id.search_hotels);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton();
            }
        });


        autoCompleterequest();

        if(car_from.getText().length()>0)
            clear_btn.setVisibility(View.VISIBLE);
        else
            clear_btn.setVisibility(View.GONE);

        if(car_to.getText().length()>0)
            clear_btn_to.setVisibility(View.VISIBLE);
        else
            clear_btn_to.setVisibility(View.GONE);



        date2=new DatePickerDialog(getContext(), R.style.DialogTheme,listener_data2,year_to,month_to,day_to_int);

        date_from.setText(checkDate.getCurrentDay()+"/"+checkDate.getCurrentMonth()+"/"+checkDate.getCurrentYear());

        year_from=checkDate.getCurrentYear();
        day_from=checkDate.getCurrentDay();
        month_from=checkDate.getCurrentMonthN();

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_from.setText("");
            }
        });
        clear_btn_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_to.setText("");
            }
        });
        car_from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(car_from.getText().length()>0)
                    clear_btn.setVisibility(View.VISIBLE);
                else
                    clear_btn.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        car_to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(car_to.getText().length()>0)
                    clear_btn_to.setVisibility(View.VISIBLE);
                else
                    clear_btn_to.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        car_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                car_to.setText(car_from.getText());
                car_from_id=location_from.get(position).getId();
                editor.putString("car_from_text",location_from.get(position).getName());
                editor.putInt("car_from_id",location_from.get(position).getId());
                editor.apply();
                car_to_id=location_from.get(position).getId();
                car_to.requestFocus();

            }
        });
        car_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                keyBoard = getActivity().getCurrentFocus();
                car_to_id=location_to.get(position).getId();
                editor.putString("car_to_text",location_to.get(position).getName());
                editor.putInt("car_to_id",location_to.get(position).getId());
                editor.apply();
                if (keyBoard != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(keyBoard.getWindowToken(), 0);
                }
            }
        });

        checkDate.incrementBy();

        time_from=(TextView)inflated.findViewById(R.id.time_from);
        time_to=(TextView)inflated.findViewById(R.id.time_to);
        time_from.setText(checkDate.getCurrentTime());
        time_to.setText(checkDate.getCurrentTime());

        date_to.setText(checkDate.getCurrentDay()+"/"+checkDate.getCurrentMonth()+"/"+checkDate.getCurrentYear());

        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHotelSelect(v);
            }
        });
        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHotelSelect(v);
            }
        });
        time_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSeter(v);
            }
        });
        time_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSeter(v);
            }
        });

        year_to=checkDate.getCurrentYear();
        day_to=checkDate.getCurrentDay();
        month_to=checkDate.getCurrentMonthN();
        return inflated;

    }
    private void autoCompleterequest()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.domain_name+"cars/locations?appKey="+Constant.key+"&lang="+Constant.default_lang, new Response.Listener() {
            @Override
            public void onResponse(Object response) {


                JSONObject parentObject = null;

                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject error_object=parentObject.getJSONObject("error");

                    if(error_object.getBoolean("status"))
                    {
                        Toast.makeText(getContext(),error_object.getString("msg"),Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        JSONObject paretnObject1 = parentObject.getJSONObject("response");
                        JSONArray parentArray = paretnObject1.getJSONArray("pickupLocations");
                        JSONArray to = paretnObject1.getJSONArray("dropoffLocations");

                        Auto_Model auto_model;
                        for(int i=0;i<parentArray.length();i++)
                        {
                            auto_model=new Auto_Model();
                            JSONObject jsonObject=parentArray.getJSONObject(i);
                            auto_model.setName(jsonObject.getString("name"));
                            auto_model.setId(jsonObject.getInt("id"));
                            auto_model.setImage_id(R.drawable.location_search);
                            location_from.add(auto_model);
                        }
                        for(int i=0;i<to.length();i++)
                        {
                            auto_model=new Auto_Model();
                            JSONObject jsonObject=to.getJSONObject(i);
                            auto_model.setName(jsonObject.getString("name"));
                            auto_model.setId(jsonObject.getInt("id"));
                            auto_model.setImage_id(R.drawable.location_search);
                            location_to.add(auto_model);
                        }
                    }
                    AutoSuggestedAdapter adapterfrom = new AutoSuggestedAdapter(getContext(),android.R.layout.simple_list_item_1,location_from);
                    AutoSuggestedAdapter adapterto = new AutoSuggestedAdapter(getContext(),android.R.layout.simple_list_item_1,location_to);


                    car_from.setAdapter(adapterfrom);
                    car_to.setAdapter(adapterto);

                }
                catch(JSONException e){

                    Log.d("abcwwwwd",e.getMessage());
                }

            }
        }, null);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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
        /**
         * Set Call back to capture selected date
         */
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

    private void showTimePicker(int a) {

        booking_car.TimePickerFragment Time = new booking_car.TimePickerFragment();



        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();

        args.putInt("mint", calender.get(Calendar.MINUTE));
        args.putInt("hour", calender.get(Calendar.HOUR_OF_DAY));
        args.putInt("a", a);
        Time.setArguments(args);


        if (a == 1) {
            Time.setCallBack(Time_from_listener);
        } else {
            Time.setCallBack(Time_to_listener);
        }
        Time.show(getFragmentManager(), "Date Picker");


    }
    public void OnClickHotelSelect(View v)
    {
        if(v.getId()== R.id.date_from)
        {
            showDatePicker(1);
            if(check)
            {
                Toast.makeText(getContext(),check+"dfdfd",Toast.LENGTH_LONG).show();
                check=false;
            }
        }
        else if(v.getId()== R.id.date_to)
        {
            showDatePicker(false,0,0,0);
        }
    }

    private DatePickerDialog.OnDateSetListener listener_data=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String monthName =  checkDate.theMonth(month);
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
                    String  s=month+"."+dayOfMonth+"."+year;
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
    public void onTimeSeter(View v)
    {
        if(v.getId()== R.id.time_from)
            showTimePicker(0);
        else
            showTimePicker(1);
    }

    public void onClickButton()
    {
        Intent intent=new Intent(getContext(),SearchingCarTourOffers.class);
        car_model c=new car_model();
        c.setPickupDate(get_date_from());
        c.setDropOfDate(get_date_to());
        c.setDropOfTime(time_to.getText().toString());
        c.setPickupTime(time_from.getText().toString());
        Bundle bundle=new Bundle();
        bundle.putParcelable("carInfo",c);
        bundle.putString("type","cars");
        intent.putExtras(bundle);
        if(!car_to.getText().toString().equals("") || !car_from.getText().toString().equals(""))
        {
            if(car_from_id==0|| car_to_id==0)
            {
                Toast.makeText(getContext(),"Nothing Found",Toast.LENGTH_LONG).show();
            }else {
                c.setPickupId(car_from_id);
                c.setDropOfId(car_to_id);
                startActivity(intent);
            }

        }
        else
        {
            c.setPickupId(0);
            c.setDropOfId(0);
            bundle.putParcelable("carInfo",c);
            bundle.putString("type","cars");
            startActivity(intent);
        }

    }
    TimePickerDialog.OnTimeSetListener Time_from_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            time_from.setText(hourOfDay+":"+minute);
            showTimePicker(0);
        }
    };
    TimePickerDialog.OnTimeSetListener Time_to_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time_to.setText(hourOfDay + ":"+minute);
        }
    };

}
