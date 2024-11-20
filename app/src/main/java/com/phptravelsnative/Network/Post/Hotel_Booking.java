package com.phptravelsnative.Network.Post;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Map;


public class Hotel_Booking extends StringRequest {

   private static final String BookingUrl= Constant.domain_name+"hotels/invoice?appKey="+Constant.key;
    private Map<String,String>params;

    public Hotel_Booking(String user_id, String hotel_id,String rooms_count,String room_id,String check_in,String check_out,String adult,String child,String coupn_id,String btype, Response.Listener<String> listener)
    {
          super( Method.POST,BookingUrl,listener,null);
        params=new HashMap<>();
        params.put("userId",user_id);
         Log.d("abcdef",user_id+"");
        params.put("itemid",hotel_id);
        params.put("roomscount",rooms_count);
        params.put("subitemid",room_id);
        params.put("apicheckin",check_in);
        params.put("couponid",coupn_id);
        params.put("children",child);
        params.put("adults",adult);
        params.put("apicheckout",check_out);
        params.put("btype",btype);

    }
    public Hotel_Booking(Hotel_data user_info, String hotel_id, String rooms_count, String room_id, String check_in, String check_out, String adult, String child, String coupn_id, String btype, Response.Listener<String> listener)
    {
          super( Method.POST,BookingUrl,listener,null);
        params=new HashMap<>();
        params.put("firstname",user_info.getAdult());
        params.put("lastname",user_info.getChild());
        params.put("email",user_info.getLocation());
        params.put("address",user_info.getFrom());
        Log.d("address",user_info.getFrom());
        params.put("phone",user_info.getTo());

        params.put("itemid",hotel_id);
        params.put("roomscount",rooms_count);
        params.put("subitemid",room_id);
        params.put("apicheckin",check_in);
        params.put("couponid",coupn_id);
        params.put("child",child);
        params.put("adult",adult);


        params.put("apicheckout",check_out);
        params.put("btype",btype);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
