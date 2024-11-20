package com.phptravelsnative.Fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.phptravelsnative.Activities.WebViewInvoice;
import com.phptravelsnative.R;


public class booking_fragment extends Fragment {

    TextView inv_number,inv_code;


    public booking_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_booking_fragment, container, false);

        inv_number =(TextView)v.findViewById(R.id.invoice_number);
        inv_code=(TextView)v.findViewById(R.id.invoice_code);

        Button b=(Button) v.findViewById(R.id.search_hotels) ;


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()) {
                    Intent intent = new Intent(getContext(), WebViewInvoice.class);
                    intent.putExtra("check_type", "booking");
                    intent.putExtra("inv_number", inv_number.getText().toString());
                    intent.putExtra("inv_code", inv_code.getText().toString());
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    private boolean validate() {

        if(inv_number.getText().toString().equals("")) {
            inv_number.setError("ENTER YOUR INVOICE NUMBER");
            return false;
        }
        else if(inv_code.getText().toString().equals("")) {
            inv_code.setError("ENTER YOUR CODE");
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }
}
