package com.phptravelsnative.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Activities.Invoice;
import com.phptravelsnative.Activities.WebViewInvoice;
import com.phptravelsnative.Adapters.HintSpinnerAdapter;
import com.phptravelsnative.Models.Auto_Model;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.lib.Parallex.NotifyingScrollView;
import com.phptravelsnative.utality.lib.Parallex.ScrollViewFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class booking_car extends ScrollViewFragment {

    private ProgressDialog dialog;
    int value_day_in, value_day_out, value_year_in, value_month_in, value_month_out, value_year_out;
    TextView time_from,time_to;
    TextView Date_from,Date_to;
    SharedPreferences sharedPreferences;
    public  final String MyPREFERENCES = "MyPrefs" ;
    ArrayList<Auto_Model> location_from = new ArrayList<>();
    ArrayList<Auto_Model> location_to = new ArrayList<>();
    View hide;
    Spinner car_from, car_to;
    car_model car;

    int from_id,to_id=0;
    TextView total_price,tax,deposite;
    public static Fragment newInstance(int position, ArrayList<Auto_Model> pickup, ArrayList<Auto_Model> dropOff, car_model car) {
        booking_car fragment = new booking_car();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelable("car", car);
        args.putParcelableArrayList("pick",pickup);
        args.putParcelableArrayList("drop",dropOff);
        fragment.setArguments(args);
        return fragment;
    }

    public booking_car() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        car = getArguments().getParcelable("car");
        location_from = getArguments().getParcelableArrayList("pick");
        location_to = getArguments().getParcelableArrayList("drop");
        dialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.loading));

        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.car_booking, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);

        time_from = (TextView) view.findViewById(R.id.time_pickup);
        time_to = (TextView) view.findViewById(R.id.time_drop);

        hide=view.findViewById(R.id.hide);

        total_price = (TextView) view.findViewById(R.id.price_car);
        tax = (TextView) view.findViewById(R.id.tax);
        deposite = (TextView) view.findViewById(R.id.deposite);

        tax.setText(String.format(" %s", car.getTaxPrice()));
        total_price.setText(car.getTotalPrice());
        deposite.setText(String.format(" %s", car.getDepositePrice()));

        time_from.setText(car.getPickupTime());
        time_to.setText(car.getDropOfTime());




        View time_picker_from = view.findViewById(R.id.time_from_picker);
        View time_picker_to = view.findViewById(R.id.time_to_picker);

        time_picker_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(1);
            }
        });
        time_picker_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(0);
            }
        });


        car_from = (Spinner) view.findViewById(R.id.location_from);
        car_to = (Spinner) view.findViewById(R.id.location_to);
       final Button update = (Button) view.findViewById(R.id.update);
       Button booking = (Button) view.findViewById(R.id.booking);

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(from_id==0 || to_id==0)
                {
                    Toast.makeText(getContext(),"Please First Select Pickup and Dropoff Location",Toast.LENGTH_LONG).show();
                }else
                {
                    Update_Rate(from_id,to_id);
                }

            }
        });


        from_id=car.getPickupId();
        to_id=car.getDropOfId();

        final RoomsAdapters ar=new RoomsAdapters(getContext(),location_from);
        final RoomsAdapters ar2=new RoomsAdapters(getContext(),location_to);

        HintSpinnerAdapter spinnerAdapter=new HintSpinnerAdapter(ar, R.layout.hint_row_pickup,getContext());
        HintSpinnerAdapter spinnerAdapter2=new HintSpinnerAdapter(ar2, R.layout.hint_row_dropoff,getContext());


        car_from.setAdapter(spinnerAdapter);
        car_to.setAdapter(spinnerAdapter2);



        if(car.getPickupId()==0 && car.getDropOfId()==0)
        {
            hide.setVisibility(View.GONE);
        }

        car_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    to_id = location_to.get(position - 1).getId();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        car_from.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                car_from.setDropDownVerticalOffset(car_from.getHeight());
                return false;
            }
        });
        car_to.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                car_to.setDropDownVerticalOffset(car_to.getHeight());
                return false;
            }
        });
        car_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0)
                    from_id=location_from.get(position-1).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        View view_in = view.findViewById(R.id.date_check_in);
        View view_out = view.findViewById(R.id.date_check_out);

        Date_from = (TextView) view.findViewById(R.id.pickup_date);
        Date_to = (TextView) view.findViewById(R.id.drop_date);

        Date_from.setText(car.getPickupDate());
        Date_to.setText(car.getDropOfDate());

        view_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(1);
            }
        });
        view_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(false, 0, 0, 0);
            }
        });

        setScrollViewOnScrollListener();

        return view;
    }

    private void book() {

        Bundle bundle=new Bundle();
        bundle.putParcelable("car_object",car);
        if(sharedPreferences.getBoolean("Check_Login",true) && !sharedPreferences.getBoolean("coupons",false))
        {
            Intent intent=new Intent(getContext(), WebViewInvoice.class);
            intent.putExtra("check_type","car");
            intent.putExtra("Check_Guest",false);

            intent.putExtras(bundle);
            startActivity(intent);

        }else
        {
            Intent intent=new Intent(getContext(), Invoice.class);
            intent.putExtra("check_type","car");
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }


    public  void Update_Rate(int from, int to)
    {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constant.domain_name+"cars/details?appKey="+Constant.key+"&id=" + car.getId() + "&pickupLocation=" + from + "&dropoffLocation=" + to + "&pickupDate=" + Date_from.getText().toString() + "&dropoffDate=" + Date_to.getText().toString() + "&pickupTime=" + time_from.getText().toString() + "&dropoffTime=" + time_to.getText().toString()+"&lang="+Constant.default_lang,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            JSONObject parentObject = null;
                            try {
                                parentObject = new JSONObject(response.toString());
                                JSONObject main_object = parentObject.getJSONObject("response");
                                JSONObject car_object = main_object.getJSONObject("car");
                                if (!car_object.getString("pickupLocation").equals("false")) {

                                    if(car_object.getString("currSymbol").equals("null"))
                                    {

                                        total_price.setText(car_object.getString("currCode") + " "  + car_object.getString("totalCost"));
                                        deposite.setText(" "+car_object.getString("currCode") + " " + car_object.getString("totalDeposit"));
                                        tax.setText(" "+car_object.getString("currCode") + " "  + car_object.getString("taxValue"));
                                    }else
                                    {

                                        total_price.setText(car_object.getString("currCode") + " " + car_object.getString("currSymbol") + " " + car_object.getString("totalCost"));
                                        deposite.setText(" "+car_object.getString("currCode") + " " + car_object.getString("currSymbol") + " " + car_object.getString("totalDeposit"));
                                        tax.setText(" "+car_object.getString("currCode") + " " + car_object.getString("currSymbol") + " " + car_object.getString("taxValue"));
                                    }

                                }
                            } catch (JSONException e) {

                                Log.d("abcwwwwd", e.getMessage());
                            }
                            if(total_price.getText().equals("USD $ 0"))
                            {
                                hide.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"Car Service Not In That area",Toast.LENGTH_LONG).show();
                            }else
                            {
                                hide.setVisibility(View.VISIBLE);
                            }
                            mScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mScrollView.smoothScrollBy(400,400);
                                }
                            });
                            dialog.dismiss();

                        }
                    }, null);

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            dialog.show();
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
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");

    }

    private void showTimePicker(int a) {

        TimePickerFragment Time = new TimePickerFragment();



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

    private void showDatePicker(Boolean b, int year, int month, int day) {
        RoomFragment.DatePickerFragment date = new RoomFragment.DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Bundle args = new Bundle();
        if (!b) {
            Calendar calender = Calendar.getInstance();

            args.putInt("year", calender.get(Calendar.YEAR));
            args.putInt("month", calender.get(Calendar.MONTH));
            args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
            args.putInt("a", 0);
            date.setArguments(args);
            /**
             * Set Call back to capture selected date
             */
            date.setCallBack(ondate);
        } else {
            args.putInt("year", year);
            args.putInt("month", month);
            args.putInt("day", day);
            args.putInt("a", 0);
            date.setArguments(args);
        }

        date.setCallBack(ondate2);
        date.show(getFragmentManager(), "Date Picker");
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

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {


            value_day_in = dayOfMonth;
            value_month_in = monthOfYear;
            value_year_in = year;

            Date_from.setText((monthOfYear + 1)+"/"+dayOfMonth + "/"+year);
            showDatePicker(true, year, monthOfYear, dayOfMonth);
        }
    };
    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            value_day_out = dayOfMonth;
            value_month_out = monthOfYear;
            value_year_out = year;
            Date_to.setText((monthOfYear + 1)+ "/"+dayOfMonth +"/"+year);
        }
    };



    public static class TimePickerFragment extends DialogFragment {
        TimePickerDialog.OnTimeSetListener Time_from_listener;

        TextView textView;

        public TimePickerFragment() {
        }

        public void setCallBack(TimePickerDialog.OnTimeSetListener ondate) {
            Time_from_listener = ondate;
        }

        int mintue_from, hour_from;
        Boolean b = false;
        int a;

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            mintue_from = args.getInt("mint");
            hour_from = args.getInt("hour");
            a = args.getInt("a");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (a == 1) {
                TimePickerDialog timepicker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, Time_from_listener, mintue_from, hour_from, false);
                textView = new TextView(getContext());
                timepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(20, 20, 20, 20);
                timepicker.setCustomTitle(textView);
                return timepicker;

            } else {
                TimePickerDialog timepicker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, Time_from_listener, mintue_from, hour_from, false);
                textView = new TextView(getContext());
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);
                timepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                textView.setPadding(20, 20, 20, 20);
                timepicker.setCustomTitle(textView);
                return timepicker;
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            if (a == 1) {
                textView.setText(R.string.pick_up);
            } else {
                textView.setText(R.string.dropoff);
            }

        }
    }

    public class RoomsAdapters extends BaseAdapter {

        ArrayList<Auto_Model> myList = new ArrayList<>();
        LayoutInflater inflater;
        Context context;

        public RoomsAdapters(Context context, ArrayList<Auto_Model> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Auto_Model getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.dropdown, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            final Auto_Model currentListData = getItem(position);

            mViewHolder.name.setText(currentListData.getName());
            return convertView;
        }

        private class MyViewHolder {
            TextView name;

            public MyViewHolder(View item) {
                name = (TextView) item.findViewById(R.id.text1);

            }
        }
    }


}
