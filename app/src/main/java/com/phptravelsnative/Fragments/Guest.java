package com.phptravelsnative.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.phptravelsnative.Activities.WebViewInvoice;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.R;
import com.phptravelsnative.databinding.FragmentGuestBinding;  // Import ViewBinding

public class Guest extends Fragment {

    private FragmentGuestBinding binding;  // ViewBinding instance

    String room_number, room_id;
    Hotel_data hotel_data;
    Hotel_data guest = new Hotel_data();
    car_model car;
    Model_Tour tour;
    String type;

    public Guest() {}

    public static Fragment newInstance(String s, Model_Tour tour, car_model car, Hotel_data hotel_data, String room_id, String room_number) {
        Guest fragment = new Guest();
        Bundle args = new Bundle();
        args.putString("type", s);
        if (s.equals("hotel")) {
            args.putParcelable("hotel", hotel_data);
            args.putString("room_id", room_id);
            args.putString("room_number", room_number);
        } else if (s.equals("tour")) {
            args.putParcelable("tour", tour);
        } else if (s.equals("car")) {
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
        type = getArguments().getString("type");
        if (type.equals("hotel")) {
            room_id = getArguments().getString("room_id");
            room_number = getArguments().getString("room_number");
            hotel_data = getArguments().getParcelable("hotel");
        } else if (type.equals("car")) {
            car = getArguments().getParcelable("car");
        } else if (type.equals("tour")) {
            tour = getArguments().getParcelable("tour");
        }

        // Initialize ViewBinding
        binding = FragmentGuestBinding.inflate(inflater, container, false);

        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    guest.setAdult(binding.firstName.getText().toString());
                    guest.setChild(binding.secondName.getText().toString());
                    guest.setLocation(binding.inputEmail.getText().toString());
                    guest.setFrom(binding.inputAddress.getText().toString());
                    guest.setTo(binding.inputPhone.getText().toString());

                    Intent intent = new Intent(getContext(), WebViewInvoice.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Check_Guest", true);
                    bundle.putParcelable("guest", guest);

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
            }
        });

        return binding.getRoot();
    }

    public boolean validate() {
        boolean valid = true;

        String name = binding.firstName.getText().toString();
        String email = binding.inputEmail.getText().toString();
        String second = binding.secondName.getText().toString();
        String address = binding.inputAddress.getText().toString();
        String phone_number = binding.inputPhone.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            binding.firstName.setError("at least 3 characters");
            valid = false;
        } else {
            binding.firstName.setError(null);
        }
        if (address.isEmpty() || address.length() < 3) {
            binding.inputAddress.setError("at least 3 characters");
            valid = false;
        } else {
            binding.inputAddress.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }

        if (second.isEmpty() || second.length() < 4 || second.length() > 10) {
            binding.secondName.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            binding.secondName.setError(null);
        }

        return valid;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Clean up ViewBinding
    }
}
