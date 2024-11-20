package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.phptravelsnative.Activities.Invoice;
import com.phptravelsnative.Activities.WebViewInvoice;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.Network.Post.Check_Coupon;
import com.phptravelsnative.R;
import com.phptravelsnative.databinding.FragmentCouponBinding;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Coupon extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private Check_Coupon requestRegister;

    private FragmentCouponBinding binding;  // Declare the binding object

    private String type;
    private Model_Tour tour;
    private car_model car;
    private String room_number, room_id;
    private Hotel_data hotel_data;

    public Coupon() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String type, Model_Tour tour, car_model car, Hotel_data hotel_data, String room_id, String room_number) {
        Coupon fragment = new Coupon();
        Bundle args = new Bundle();

        args.putParcelable("hotel", hotel_data);
        args.putString("room_id", room_id);
        args.putString("room_number", room_number);
        args.putString("check_type", type);

        if (type.equals("hotel")) {
            args.putParcelable("hotel", hotel_data);
            args.putString("room_id", room_id);
            args.putString("room_number", room_number);
        } else if (type.equals("tour")) {
            args.putParcelable("tour", tour);
        } else if (type.equals("car")) {
            args.putParcelable("car", car);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        sharedPreferences = getContext().getSharedPreferences(getString(R.string.my_preferences), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Initialize ViewBinding
        binding = FragmentCouponBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getString(R.string.loading));

        type = getArguments().getString("check_type");

        if (type.equals("hotel")) {
            hotel_data = getArguments().getParcelable("hotel");
            room_id = getArguments().getString("room_id");
            room_number = getArguments().getString("room_number");
        } else if (type.equals("car")) {
            car = getArguments().getParcelable("car");
        } else if (type.equals("tour")) {
            tour = getArguments().getParcelable("tour");
        }

        // Handle visibility of hide view
        if (sharedPreferences.getBoolean("Check_Login", true)) {
            binding.hide.setVisibility(View.VISIBLE);
        }

        // Set up listeners
        binding.verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _verfyCode();
            }
        });

        binding.continueCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebViewInvoice.class);
                Bundle bundle = new Bundle();

                if (type.equals("hotel")) {
                    bundle.putParcelable("hotel_object", hotel_data);
                    intent.putExtra("check_type", "hotel");
                    intent.putExtra("room_id", room_id);
                    intent.putExtra("numbers_rooms", room_number);
                    intent.putExtras(bundle);
                } else if (type.equals("tour")) {
                    bundle.putParcelable("tour_object", tour);
                    intent.putExtra("check_type", "tour");
                    intent.putExtras(bundle);
                } else if (type.equals("car")) {
                    bundle.putParcelable("car_object", car);
                    intent.putExtra("check_type", "car");
                    intent.putExtras(bundle);
                }

                startActivity(intent);
            }
        });

        return binding.getRoot(); // Return the root view from the binding
    }

    private void _verfyCode() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject main_json = new JSONObject(response);

                    JSONObject userInfo_object = main_json.getJSONObject("response");

                    String s = userInfo_object.getString("status");
                    if (s.equals("success")) {
                        // Store coupon in Invoice class
                        Invoice.coupon = userInfo_object.getString("couponid");
                        Toast.makeText(getContext(), R.string.coupon_applied, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), userInfo_object.getString("msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (type.equals("hotel")) {
            requestRegister = new Check_Coupon(binding.couponCode.getText().toString(), hotel_data.getId(), "hotels", listener);
        } else if (type.equals("tour")) {
            requestRegister = new Check_Coupon(binding.couponCode.getText().toString(), tour.getId(), "tours", listener);
        } else if (type.equals("car")) {
            requestRegister = new Check_Coupon(binding.couponCode.getText().toString(), car.getId(), "cars", listener);
        }

        RequestQueue requestQueue = SingleTonRequest.getmInctance(getContext()).getRequestQueue();
        requestQueue.add(requestRegister);
        progressDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up the binding object
        binding = null;
    }
}
