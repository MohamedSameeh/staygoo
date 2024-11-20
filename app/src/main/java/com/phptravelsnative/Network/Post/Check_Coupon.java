package com.phptravelsnative.Network.Post;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Map;


public class Check_Coupon extends StringRequest {

   private static final String BookingUrl= Constant.domain_name+"invoice/verifyCoupon?appKey="+Constant.key;
    private Map<String,String>params;
    public Check_Coupon(String code, int hotel_id, String btype, Response.Listener<String> listener)
    {
          super( Method.POST,BookingUrl,listener,null);
        params=new HashMap<>();
        params.put("code",code);
        params.put("itemId",hotel_id+"");
        params.put("module",btype);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
