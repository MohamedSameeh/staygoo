package com.phptravelsnative.Network.Post;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Map;


public class Tour_Booking extends StringRequest {

   private static final String BookingUrl= Constant.domain_name+"tours/invoice?appKey="+Constant.key;
    private Map<String,String>params;

    public Tour_Booking(String userId, Model_Tour tour, String coupn_id, String btype, Response.Listener<String> listener)
    {
          super( Method.POST,BookingUrl,listener,null);
        params=new HashMap<>();
        params.put("userId",userId);

        params.put("itemid",tour.getId()+"");
        params.put("tdate",tour.getDate());
        params.put("couponid",coupn_id);
        params.put("children",tour.getMaxChild()+"");
        params.put("adults",tour.getMaxAdult()+"");
        params.put("infant",tour.getMaxInflants()+"");
        params.put("btype",btype);

    }

    public Tour_Booking(Hotel_data user_info, Model_Tour tour, String coupn_id, String btype, Response.Listener<String> listener)
    {
        super( Method.POST,BookingUrl,listener,null);
        params=new HashMap<>();
        params.put("firstname",user_info.getAdult());
        params.put("lastname",user_info.getChild());
        params.put("email",user_info.getLocation());
        params.put("address",user_info.getFrom());
        params.put("phone",user_info.getTo());


        params.put("itemid",tour.getId()+"");
        params.put("tdate",tour.getDate());
        params.put("couponid",coupn_id);
        params.put("children",tour.getMaxChild()+"");
        params.put("adults",tour.getMaxAdult()+"");
        params.put("infant",tour.getMaxInflants()+"");
        params.put("btype",btype);

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
