package com.phptravelsnative.Network.Post;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.Models.ExpediaExtra;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ExpediaBooking extends StringRequest {

    private static final String RegisterUrl = Constant.domain_name+"expedia/proceedBooking?appKey="+Constant.key;
    private Map<String, String> params;

    public ExpediaBooking(ExpediaExtra expedia_hotel, Hotel_data hotel_data, Response.Listener<String> listener) {
        super(Method.POST, RegisterUrl, listener, null);
        params = new HashMap<>();
        params.put("customerSessionId", expedia_hotel.getSessionId());

        params.put("hotelId", hotel_data.getId() + "");

        params.put("checkIn", hotel_data.getFrom());

        params.put("checkOut", hotel_data.getTo());

        params.put("phone", expedia_hotel.getInput_phone());
        params.put("postalcode", expedia_hotel.getInput_postal_code());

        params.put("ratekey", expedia_hotel.getRateKey());

        params.put("rateCode", expedia_hotel.getRateCode());

        params.put("roomType", expedia_hotel.getRoomTypeCode());

        params.put("adults", hotel_data.getAdult());
        Log.d("adults", hotel_data.getAdult()+"");

        params.put("firstName", expedia_hotel.getFirst_name());

        params.put("lastName", expedia_hotel.getLast_name());

        params.put("email", expedia_hotel.getInput_email());

        params.put("cardType", expedia_hotel.getInput_card_type());

        params.put("cardNumber", expedia_hotel.getInput_card_number());

        params.put("cvv", expedia_hotel.getInput_card_cvw());

        params.put("cardExpirationMonth", expedia_hotel.getExpairy().substring(0,2));

        params.put("cardExpirationYear", expedia_hotel.getExpairy().substring(3,7));



        params.put("address", expedia_hotel.getInput_address());

        params.put("city", expedia_hotel.getInput_city());

        params.put("country", expedia_hotel.getInput_country());



        params.put("province", expedia_hotel.getInput_state());


        params.put("total", expedia_hotel.getTotal_price());

    }

    @Override
    public Map<String, String> getParams() {

        return checkParams(params);
    }

    private Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }
}
