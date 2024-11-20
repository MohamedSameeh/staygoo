package com.phptravelsnative.Network.Post;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Map;


public class Car_Booking extends StringRequest {

   private static final String BookingUrl= Constant.domain_name+"cars/invoice?appKey="+Constant.key;
    private Map<String,String>params;

    public Car_Booking(String userId, car_model car, String coupn_id, String btype, Response.Listener<String> listener)
    {
          super( Method.POST,BookingUrl,listener,null);
        params=new HashMap<>();

        params.put("userId",userId);

        params.put("itemid",car.getId()+"");
        params.put("pickuplocation",car.getPickupId()+"");
        params.put("couponid",coupn_id);
        params.put("dropofflocation",car.getDropOfId()+"");
        params.put("pickupTime",car.getPickupTime()+"");
        params.put("pickupDate",car.getPickupDate()+"");
        params.put("dropoffDate",car.getDropOfDate()+"");
        params.put("dropoffTime",car.getDropOfTime()+"");
        params.put("btype",btype);

    }
    public Car_Booking(Hotel_data user_info, car_model car, String coupn_id, String btype, Response.Listener<String> listener)
    {
          super( Method.POST,BookingUrl,listener,null);
        params=new HashMap<>();
        params.put("firstname",user_info.getAdult());
        params.put("lastname",user_info.getChild());
        params.put("email",user_info.getLocation());
        params.put("address",user_info.getFrom());
        params.put("phone",user_info.getTo());

        params.put("itemid",car.getId()+"");
        params.put("pickuplocation",car.getPickupId()+"");
        params.put("couponid",coupn_id);
        params.put("dropofflocation",car.getDropOfId()+"");
        params.put("pickupTime",car.getPickupTime()+"");
        params.put("pickupDate",car.getPickupDate()+"");
        params.put("dropoffDate",car.getDropOfDate()+"");
        params.put("dropoffTime",car.getDropOfTime()+"");
        params.put("btype",btype);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
