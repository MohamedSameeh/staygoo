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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Activities.MainLayout;
import com.phptravelsnative.Activities.WebViewInvoice;
import com.phptravelsnative.Models.HotelModel;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyBooking extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;

    ArrayList<HotelModel> myList = new ArrayList<>();

    ProgressDialog dialog;
    String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyBooking() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyBooking newInstance(String param1, String param2) {
        MyBooking fragment = new MyBooking();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_list_view, container, false);
        listView= (ListView) v.findViewById(R.id.listview);
        sharedPreferences =getContext(). getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.loading));


        if(sharedPreferences.getBoolean("Check_Login",false))
        {

            callForBooking();
        }else
        {
            Intent intent=new Intent(getContext(),MainLayout.class);
            intent.putExtra("CheckLayout","login");
            startActivityForResult(intent, 2);
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sharedPreferences.getBoolean("Check_Login",false))
        {
            callForBooking();
        }
    }

    private void callForBooking() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name+"invoice/list?appKey="+Constant.key+"&userid="+sharedPreferences.getString("id","")+"&lang="+Constant.default_lang,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JSONObject parentObject = null;
                        try {
                            parentObject = new JSONObject(response.toString());
                            JSONArray main_object = parentObject.getJSONArray("response");
                            HotelModel hm;
                            for(int i=0;i<main_object.length();i++)
                            {
                                JSONObject jsonObject=main_object.getJSONObject(i);
                                hm=new HotelModel();
                                hm.setName(jsonObject.getString("title"));
                                hm.setThumbnail(jsonObject.getString("thumbnail"));
                                hm.setcurrSymbol(jsonObject.getString("checkin"));
                                hm.setcurrCode(jsonObject.getString("checkout"));
                                hm.setPrice(jsonObject.getString("currCode")+jsonObject.getString("currSymbol")+" "+jsonObject.getString("checkoutTotal"));
                                hm.setRateCode(jsonObject.getString("code"));
                                hm.setRateKey(jsonObject.getString("id"));
                                hm.setType(jsonObject.getString("module"));

                                myList.add(hm);
                            }

                        } catch (JSONException e) {

                            Log.d("abcwwwwd", e.getMessage());
                        }

                        BookingAdapter book_adapter=new BookingAdapter(getContext(),myList);
                        listView.setAdapter(book_adapter);
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

    public class BookingAdapter extends BaseAdapter {

        ArrayList<HotelModel> myList = new ArrayList<>();
        LayoutInflater inflater;
        Context context;

        public BookingAdapter(Context context, ArrayList<HotelModel> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public HotelModel getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.my_booking_child, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            final HotelModel currentListData = getItem(position);

            mViewHolder.tvTitle.setText(currentListData.getName());
            mViewHolder.price.setText(currentListData.getPrice());
            mViewHolder.checkin.setText(currentListData.getcurrSymbol());
            mViewHolder.checkout.setText(currentListData.getcurrCode());

            final View finalConvertView = convertView;
            mViewHolder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalConvertView.callOnClick();
                }
            });
            Picasso.get().load(currentListData.getThumbnail()).into(mViewHolder.ivIcon);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(getContext(), WebViewInvoice.class);
                    intent.putExtra("check_type","booking");
                    intent.putExtra("inv_number",currentListData.getRateKey());
                    intent.putExtra("inv_code",currentListData.getRateCode());
                    startActivity(intent);

                }
            });
            return convertView;
        }



        private class MyViewHolder {
            TextView tvTitle;
            ImageView ivIcon;
            TextView price;
            TextView checkin;
            TextView checkout;
            Button book;


            public MyViewHolder(View item) {
                tvTitle = (TextView) item.findViewById(R.id.title);
                checkin = (TextView) item.findViewById(R.id.check_in);
                checkout = (TextView) item.findViewById(R.id.check_out);
                price = (TextView) item.findViewById(R.id.price);
                ivIcon = (ImageView) item.findViewById(R.id.room_icon);

                book= (Button) item.findViewById(R.id.room_btn);

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }
}
