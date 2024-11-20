package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Network.Post.contact_request;
import com.phptravelsnative.R;
import com.phptravelsnative.databinding.FragmentContactUsBinding;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUs extends Fragment {

    private FragmentContactUsBinding binding;
    private ProgressDialog progressDialog;
    private String sendTo;

    public ContactUs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactUsBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        getContacts();

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    postReport();
                }
            }
        });

        return binding.getRoot();  // Return the root view from the binding
    }

    private void postReport() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject mainJson = new JSONObject(response);
                    JSONObject jsonResponse = mainJson.getJSONObject("response");
                    if (jsonResponse.getBoolean("sent")) {
                        Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        progressDialog.show();

        contact_request requestRegister = new contact_request(
                binding.inputEmail.getText().toString(),
                binding.inputName.getText().toString(),
                binding.inputSubject.getText().toString(),
                binding.inputMessage.getText().toString(),
                sendTo,
                listener
        );

        RequestQueue requestQueue = SingleTonRequest.getmInctance(getContext()).getRequestQueue();
        requestQueue.add(requestRegister);
    }

    public boolean validate() {
        boolean valid = true;

        String name = binding.inputName.getText().toString();
        String email = binding.inputEmail.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            binding.inputName.setError("At least 3 characters");
            valid = false;
        } else {
            binding.inputName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }

        return valid;
    }

    public void getContacts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "contact/info?appKey=" + Constant.key + "&lang=" + Constant.default_lang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject parentObject = new JSONObject(response);
                            JSONObject mainObject = parentObject.getJSONObject("response");
                            binding.txtAddress.setText(mainObject.getString("contact_address"));
                            sendTo = mainObject.getString("contact_email");
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            Log.d("Error", e.getMessage());
                        }
                    }
                }, null);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // To avoid memory leaks
    }
}
