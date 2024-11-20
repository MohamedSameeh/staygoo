package com.phptravelsnative.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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


public class WebViewFragments extends Fragment {



    WebView web_view;
    View progressView;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    public WebViewFragments() {
    }

   String checkType;
    public static Default_Hotel newInstance(String param1) {
        Default_Hotel fragment = new Default_Hotel();
        Bundle args = new Bundle();
        args.putString("checkType", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            checkType = getArguments().getString("checkType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated= inflater.inflate(R.layout.fragment_web_view_fragments, container, false);


        web_view = (WebView) inflated.findViewById(R.id.web_flight);


        progressView=inflated.findViewById(R.id.webView);

        if (checkType.equals("dohope")) {
            dohope();
        } else if (checkType.equals("Travelstart")) {
            travelStart();
        } else if (checkType.equals("wego")) {
            wego();
        }else if (checkType.equals("hotelscombined")) {
            hotelCombile();
        }else if (checkType.equals("Travelpayouts")) {
            travelPayouts();
        }
        else if (checkType.equals("cartrawler")) {
            Cartraler();
        }
        return inflated;
    }
    private void dohope() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "modulesinfo/dohop?appKey=" + Constant.key

                , new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object = parentObject.getJSONObject("response");

                    String url = main_object.getString("username");
                    ShowUrl("http://whitelabel.dohop.com/w/" + url);
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
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (progressView.getVisibility()==View.VISIBLE) {

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        progressView.setVisibility(View.GONE);
                        web_view.setVisibility(View.VISIBLE);
                    }
                };

                Handler pdCanceller = new Handler();
                progressView.postDelayed(progressRunnable, 5000);

            }
        }

        @Override
        public void onPageStarted(WebView view, String url,
                                  android.graphics.Bitmap favicon) {

            if (progressView.getVisibility()!=View.VISIBLE) {
                progressView.setVisibility(View.VISIBLE);
            }
        };
    }


    private void wego() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "modulesinfo/wego?appKey=" + Constant.key

                , new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object = parentObject.getJSONObject("response");

                    ShowUrl(main_object.getString("url"));
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
            progressView.setVisibility(View.VISIBLE);
    }
    private void hotelCombile() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "modulesinfo/hotelscombined?appKey=" + Constant.key

                , new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object = parentObject.getJSONObject("response");

                    ShowUrl("http://brands.datahc.com/?a_aid="+main_object.getString("aid")+"&brandid="+main_object.getString("brandID")+"&languageCode="+Constant.default_lang.toUpperCase());
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
            progressView.setVisibility(View.VISIBLE);
    }

    private void ShowUrl(String s) {
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_view.setWebViewClient(new MyWebViewClient());
        web_view.loadUrl(s);
    }


    private void travelStart() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "modulesinfo/travelstart?appKey=" + Constant.key

                , new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object = parentObject.getJSONObject("response");
                    ShowUrl(main_object.getString("url"));
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
    private void travelPayouts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "modulesinfo/travelpayouts?appKey=" + Constant.key

                , new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object = parentObject.getJSONObject("response");
                    ShowUrl(main_object.getString("url"));
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
    private void ShowHtml(String s) {
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_view.setWebViewClient(new MyWebViewClientHtml());
        web_view.loadDataWithBaseURL("", s, mimeType, encoding, "");
    }

    private void Cartraler() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "modulesinfo/cartrawler?appKey=" + Constant.key

                , new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object = parentObject.getJSONObject("response");
                    ShowUrl("https://book.cartrawler.com/?client="+main_object.getString("cid")+"#/searchcars");

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
            progressView.setVisibility(View.VISIBLE);

    }
    private class MyWebViewClientHtml extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadDataWithBaseURL("", url, mimeType, encoding, "");

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("ondddd finish");
            if (progressView.getVisibility()==View.VISIBLE) {


                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        progressView.setVisibility(View.GONE);
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 4000);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url,
                                  android.graphics.Bitmap favicon) {
            System.out.println("ondddd start");
            if (progressView.getVisibility()!=View.VISIBLE) {
                progressView.setVisibility(View.VISIBLE);
            }
        };
    }

}
