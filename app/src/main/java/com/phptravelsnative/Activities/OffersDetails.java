package com.phptravelsnative.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Adapters.SlidingImage_Adapter;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.Network.Post.SendOffers;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OffersDetails extends Drawer {


    int id;
    private static ViewPager mPager;
    ArrayList<DetailModel> arrayList = new ArrayList<>();
    OverView overView = new OverView();
    ProgressDialog dailog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.activity_offers_details);
        final View inflated = stub.inflate();

        dailog = new ProgressDialog(this);
        dailog.setMessage("Loading..");


        Intent intent = getIntent();
        arrayList = intent.getParcelableArrayListExtra("arrayList");
        overView = intent.getParcelableExtra("ov");



        textView.setText(overView.getTitle());

        final EditText ed_name = (EditText) inflated.findViewById(R.id.name_contacts);
        final EditText ed_email = (EditText) inflated.findViewById(R.id.email);
        final EditText ed_message = (EditText) inflated.findViewById(R.id.message);

        Button bt_send = (Button) inflated.findViewById(R.id.send);
        Button bt_call = (Button) inflated.findViewById(R.id.call);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(ed_name.getText().toString(), ed_email.getText().toString(), ed_message.getText().toString()
                        , overView.getPolicy());
            }
        });
        bt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + overView.getPolicy()));
                if (ActivityCompat.checkSelfPermission(OffersDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(intent);

            }
        });



        TextView desc= (TextView) inflated.findViewById(R.id.desc);
        desc.setText(overView.getDesc());
        Log.d("overView",overView.getDesc());

        TextView title= (TextView) inflated.findViewById(R.id.title);
        title.setText(overView.getTitle());





        mPager = (ViewPager) inflated.findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(OffersDetails.this, arrayList,true));

        CirclePageIndicator indicator =(CirclePageIndicator)
                findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

    }

    private void sendMessage(String name, String email, String msg, String phnone) {

        if(name.equals("") || email.equals("") || msg.equals("") || phnone.equals(""))
        {
            Toast.makeText(getBaseContext(),"Please fill every thing",Toast.LENGTH_LONG).show();
        }else {
                    Response.Listener<String> listener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {


                            JSONObject parentObject = null;
                            try {
                                parentObject = new JSONObject(response.toString());
                                Toast.makeText(getApplicationContext(),parentObject.getString("response"),Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {

                                Log.d("abcwwwwd", e.getMessage());
                            }
                            dailog.dismiss();

                        }
                    };
            Response.ErrorListener  error=  new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_LONG).show();
                }
            };

            SendOffers snf=new SendOffers(name,email,phnone,msg,listener,error);
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            snf.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(snf);
            dailog.show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

