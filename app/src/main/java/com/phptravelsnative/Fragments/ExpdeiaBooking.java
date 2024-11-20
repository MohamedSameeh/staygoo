package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;
import com.phptravelsnative.Activities.Card;
import com.phptravelsnative.Models.ExpediaExtra;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpdeiaBooking extends Fragment {


    EditText _firstName;
    EditText _lastName;
    EditText _emailText;
    EditText _address;
    EditText input_phone;
    EditText input_city;
    EditText input_postal_code;
    EditText input_state;
    EditText input_country;

    SharedPreferences sharedPreferences;
    String MyPREFERENCES = "MyPrefs";

    ProgressDialog pd;



    String country="";


    Button booking;
    int value_day_in, value_year_in, value_month_in;
    ExpediaExtra expediaExtra;

    Hotel_data hotel_data;

    ScrollView mRelativeLayout;


    public ExpdeiaBooking() {
    }

    public static Fragment newInstance(Hotel_data hotel_data, ExpediaExtra expedia) {
        ExpdeiaBooking fragment = new ExpdeiaBooking();
        Bundle args = new Bundle();
        args.putParcelable("hotel",hotel_data);
        args.putParcelable("expedia",expedia);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_expedia_booking, container, false);

        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        pd = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.setMessage(getString(R.string.loading));

        expediaExtra=getArguments().getParcelable("expedia");
        hotel_data=getArguments().getParcelable("hotel");

        mRelativeLayout=(ScrollView) view.findViewById(R.id.rl);


        booking=(Button)view.findViewById(R.id.next);
        _firstName=(EditText) view.findViewById(R.id.first_name);
        _lastName=(EditText) view.findViewById(R.id.last_name);
        _emailText=(EditText) view.findViewById(R.id.input_email);
        _address=(EditText)view.findViewById(R.id.input_address);
        input_phone=(EditText) view.findViewById(R.id.input_phone);
        input_city=(EditText) view.findViewById(R.id.input_city);
        input_postal_code=(EditText) view.findViewById(R.id.input_postal_code);
        input_state=(EditText) view.findViewById(R.id.input_state);
        input_country=(EditText) view.findViewById(R.id.input_country);




        if(!hotel_data.getChild().split("900")[1].equals("0")) {
            _firstName.setText("Test Booking");
            _lastName.setText("Test Booking");
            _address.setText("travelnow");
            input_state.setText("wa");
            input_city.setText("Seattle");
            input_postal_code.setText("98004");
            input_phone.setText("00923339509462");
            input_country.setText("United State");
            country="US";
        }



        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (validate()) {

                        expediaExtra.setFirst_name(_firstName.getText().toString());
                        expediaExtra.setLast_name(_lastName.getText().toString());
                        expediaExtra.setInput_email(_emailText.getText().toString());
                        expediaExtra.setInput_phone(input_phone.getText().toString());
                        expediaExtra.setInput_city(input_city.getText().toString());
                        expediaExtra.setInput_address(_address.getText().toString());
                        expediaExtra.setInput_state(input_state.getText().toString());
                        expediaExtra.setInput_postal_code(input_postal_code.getText().toString());
                        expediaExtra.setInput_country(country);


                        Intent card_activity=new Intent(getContext(),Card.class);
                        Bundle b=new Bundle();
                        b.putParcelable("expedia",expediaExtra);
                        b.putParcelable("hotel_object",hotel_data);
                        card_activity.putExtras(b);
                        startActivity(card_activity);

                    }


            }
        });


        input_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");
                picker.setListener(new CountryPickerListener() {

                    @Override
                    public void onSelectCountry(String name, String code) {
                        country=code;
                        input_country.setText(name);
                        picker.dismiss();
                        View keyBoard =getActivity().getCurrentFocus();
                        if (keyBoard != null) {
                            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(keyBoard.getWindowToken(), 0);
                        }

                    }
                });

                picker.show(getChildFragmentManager(), "COUNTRY_PICKER");
            }
        });


        if(sharedPreferences.getBoolean("Check_Login",false)) {

            getContacts(sharedPreferences.getString("id",""));

        }




        return view;
    }

    public void getContacts(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "login/profile?appKey="+Constant.key + "&id=" + id,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {


                        JSONObject parentObject = null;
                        try {
                            parentObject = new JSONObject(response.toString());
                            JSONObject main_object = parentObject.getJSONObject("response");
                            _firstName.setText(main_object.getString("ai_first_name"));
                            _lastName.setText(main_object.getString("ai_last_name"));;
                            _address.setText(main_object.getString("ai_address_1"));
                            input_phone.setText(main_object.getString("ai_mobile"));
                            input_city.setText(main_object.getString("ai_city"));
                            input_postal_code.setText(main_object.getString("ai_postal_code"));
                            input_state.setText(main_object.getString("ai_state"));
                            _emailText.setText(main_object.getString("accounts_email"));
                            pd.dismiss();

                        } catch (JSONException e) {

                            Log.d("abcwwwwd", e.getMessage());
                        }

                    }
                }, null);

        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }




    public boolean validate() {
        boolean valid = true;

        String last_name = _lastName.getText().toString();
        String email = _emailText.getText().toString();
        String first_name= _firstName.getText().toString();
        String address= _address.getText().toString();
        String phone= input_phone.getText().toString();
        String postal= input_postal_code.getText().toString();
        String state= input_state.getText().toString();
        String city= input_city.getText().toString();




        if (last_name.isEmpty() || last_name.length() < 3) {
            _lastName.setError("at least 3 characters");
            valid = false;
        } else {
            _lastName.setError(null);
        }
        if (first_name.isEmpty() || first_name.length() < 3) {
            _firstName.setError("at least 3 characters");
            valid = false;
        } else {
            _firstName.setError(null);
        }

        if (address.isEmpty()) {
            _address.setError("Address Must Be Specify");
            valid = false;
        } else {
            _address.setError(null);
        }

        if (phone.isEmpty()) {
            input_phone.setError("Phone Number Must Be Specify");
            valid = false;
        } else {
            input_phone.setError(null);
        }

        if (postal.isEmpty()) {
            input_postal_code.setError("Postal Number Must Be Specify");
            valid = false;
        } else {
            input_postal_code.setError(null);
        }
        if (state.isEmpty()) {
            input_state.setError("State Must Be Specify");
            valid = false;
        } else {
            input_state.setError(null);
        }

        if (city.isEmpty()) {
            input_city.setError("City Must Be Specify");
            valid = false;
        } else {
            input_city.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }


}
