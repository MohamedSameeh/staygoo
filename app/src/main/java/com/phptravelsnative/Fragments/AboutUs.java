package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutUs extends Fragment {

    ProgressDialog progressDialog;
    TextView textView;

    public AboutUs() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        progressDialog= new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        getContacts();
         View view=inflater.inflate(R.layout.fragment_about_us, container, false);

        textView=(TextView)view.findViewById(R.id.textview);

        return view;
    }
    public void getContacts()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name+"modulesinfo/aboutus?appKey="+Constant.key+"&lang="+Constant.default_lang,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {


                        JSONObject parentObject = null;
                        try {
                            parentObject = new JSONObject(response.toString());

                            String main_object="";
                            if(!parentObject.getString("response").equals(null)){
                            main_object= parentObject.getString("response");}

                            textView.setText(main_object);

                            progressDialog.dismiss();

                        }
                        catch(JSONException e){

                            Log.d("abcwwwwd",e.getMessage());
                        }

                    }
                }, null);

        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }
}
