package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    SharedPreferences sharedPreferences;

    EditText _firstName;
    EditText _lastName;
    EditText _emailText;
    EditText _address;
    EditText _address2;
    EditText input_phone;
    EditText input_city;
    EditText input_pass;
    EditText input_confirm_pass;
    EditText input_postal_code;
    EditText input_state;
    TextView input_country;
    Button submit;
    ProgressDialog progressDialog;

    public final String MyPREFERENCES = "MyPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        submit = (Button) view.findViewById(R.id.btn_book);
        _firstName = (EditText) view.findViewById(R.id.first_name);
        _lastName = (EditText) view.findViewById(R.id.last_name);
        input_phone = (EditText) view.findViewById(R.id.input_phone);
        input_pass = (EditText) view.findViewById(R.id.change_pass);
        input_confirm_pass = (EditText) view.findViewById(R.id.confirm_pass);
        _emailText = (EditText) view.findViewById(R.id.input_email);
        _address = (EditText) view.findViewById(R.id.input_address);
        _address2 = (EditText) view.findViewById(R.id.input_address2);
        input_city = (EditText) view.findViewById(R.id.input_city);
        input_postal_code = (EditText) view.findViewById(R.id.input_postal_code);
        input_state = (EditText) view.findViewById(R.id.input_state);
        input_country = (TextView) view.findViewById(R.id.input_country);

        getContacts(sharedPreferences.getString("id", ""));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send_user_detail();

            }
        });
        input_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");
                picker.setListener(new CountryPickerListener() {

                    @Override
                    public void onSelectCountry(String name, String code) {
                        input_country.setText(name);
                        picker.dismiss();
                    }
                });

                picker.show(getChildFragmentManager(), "COUNTRY_PICKER");
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
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
                            _lastName.setText(main_object.getString("ai_last_name"));
                            _firstName.setEnabled(false);
                            _lastName.setEnabled(false);
                            input_phone.requestFocus();
                            _address.setText(main_object.getString("ai_address_1"));
                            _address2.setText(main_object.getString("ai_address_2"));
                            input_phone.setText(main_object.getString("ai_mobile"));
                            input_city.setText(main_object.getString("ai_city"));
                            input_postal_code.setText(main_object.getString("ai_postal_code"));
                            input_state.setText(main_object.getString("ai_state"));
                            input_country.setText(main_object.getString("ai_country"));
                            _emailText.setText(main_object.getString("accounts_email"));
                            progressDialog.dismiss();

                        } catch (JSONException e) {

                            Log.d("abcwwwwd", e.getMessage());
                        }

                    }
                }, null);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void send_user_detail() {
        StringRequest putRequest = new StringRequest(Request.Method.POST, Constant.domain_name + "login/updateprofile?appKey="+Constant.key,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getBoolean("response"))
                            {
                                Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();

                            }else
                            {
                                Toast.makeText(getContext(), jsonObject.getJSONObject("error").getString("msg"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", _firstName.getText().toString());
                params.put("id", sharedPreferences.getString("id",""));
                params.put("lastname", _lastName.getText().toString());
                params.put("city", input_city.getText().toString());
                params.put("country", input_country.getText().toString());
                params.put("address1", _address.getText().toString());
                params.put("address2", _address2.getText().toString());
                params.put("phone", input_phone.getText().toString());
                params.put("zip", input_postal_code.getText().toString());
                params.put("email", _emailText.getText().toString());
                params.put("state", input_state.getText().toString());

                return params;
            }
        };

        progressDialog.show();
        RequestQueue requestQueue = SingleTonRequest.getmInctance(getContext()).getRequestQueue();
        requestQueue.add(putRequest);
    }

}


