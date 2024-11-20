package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.phptravelsnative.Activities.MainLayout;
import com.phptravelsnative.Network.Post.RequestRegister;
import com.phptravelsnative.R;
import com.phptravelsnative.databinding.ActivitySignUpBinding;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends Fragment {

    private ActivitySignUpBinding binding;

    public Registration() {
    }

    public static Registration newInstance(String param1, String param2) {
        return new Registration();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivitySignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSignup.setOnClickListener(v -> signup());
        binding.linkLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainLayout.class);
            intent.putExtra("CheckLayout", "login");
            startActivity(intent);
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = binding.inputName.getText().toString();
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        String firstName = binding.firstName.getText().toString();
        String phoneNumber = binding.inputPhone.getText().toString();

        Response.Listener<String> listener = response -> {
            try {
                progressDialog.dismiss();
                JSONObject mainJson = new JSONObject(response);
                if (mainJson.getBoolean("response")) {
                    onSignupSuccess();
                } else {
                    JSONObject errorObject = mainJson.getJSONObject("error");
                    Toast.makeText(getContext(), errorObject.getString("msg"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        RequestRegister requestRegister = new RequestRegister(email, password, firstName, name, phoneNumber, listener);
        RequestQueue requestQueue = SingleTonRequest.getmInctance(getContext()).getRequestQueue();
        requestQueue.add(requestRegister);
    }

    public void onSignupSuccess() {
        binding.btnSignup.setEnabled(true);
        Intent intent = new Intent(getContext(), MainLayout.class);
        intent.putExtra("CheckLayout", "login");
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getContext(), "Signup failed", Toast.LENGTH_LONG).show();
        binding.btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = binding.inputName.getText().toString();
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        String firstName = binding.firstName.getText().toString();
        String phoneNumber = binding.inputPhone.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            binding.inputName.setError("At least 3 characters");
            valid = false;
        } else {
            binding.inputName.setError(null);
        }

        if (firstName.isEmpty() || firstName.length() < 3) {
            binding.firstName.setError("At least 3 characters");
            valid = false;
        } else {
            binding.firstName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            binding.inputPassword.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
