package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.Adapters.BlogAdapter;
import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Blog extends Fragment {
    ArrayList<review_model> myList = new ArrayList<>();

    ProgressDialog progressDialog;
    private RelativeLayout bottomLayout;
    int offest=1;
    String offset_get="&offset=";
    int total_offest;
    Response.Listener<String> response_listener;
    RequestQueue requestQueue;
    BlogAdapter blog_adapter;
    String url=Constant.domain_name+"blog/list?appKey="+Constant.key+"&lang="+Constant.default_lang+ offset_get + "1" ;
    StringRequest stringRequest;
    boolean userScrolled = false;

    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_searching, container, false);

        progressDialog=  new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        listView= (ListView) v.findViewById(R.id.list_view);
        bottomLayout = (RelativeLayout)v.findViewById(R.id.loadItemsLayout_listView);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (userScrolled
                        && firstVisibleItem + visibleItemCount == totalItemCount) {
                    userScrolled = false;
                    if(offest<total_offest) {
                        offest=offest+1;
                        stringRequest = new StringRequest(Request.Method.GET,
                                url+offset_get+offest,
                                response_listener, null);
                        requestQueue.add(stringRequest);
                        bottomLayout.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        show_data();
        return v;
    }

    private void show_data() {
                response_listener =new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject main_json = new JSONObject((String) response);
                    JSONObject response_main=main_json.getJSONObject("response");

                    JSONArray response_array=response_main.getJSONArray("posts");
                    review_model menu_model;
                    for(int i=0;i<response_array.length();i++)
                    {
                        JSONObject child_object= response_array.getJSONObject(i);
                        menu_model=new review_model();
                        menu_model.setRating(child_object.getString("description"));
                        menu_model.setReview_by(child_object.getString("title"));
                        menu_model.setReview_comment(child_object.getString("thumbnail"));
                        total_offest=response_main.getInt("totalPages");

                        myList.add(menu_model);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.d("checkMenu",e.getMessage());
                }
                if(offest==1) {
                    progressDialog.dismiss();
                    blog_adapter=new BlogAdapter(getContext(),myList);
                    listView.setAdapter(blog_adapter);
                }
                else {
                    bottomLayout.setVisibility(View.GONE);
                    blog_adapter.notifyDataSetChanged();
                }


            }
        };
         stringRequest = new StringRequest(Request.Method.GET, url,response_listener,null);

        requestQueue = SingleTonRequest.getmInctance(getContext()).getRequestQueue();

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}
