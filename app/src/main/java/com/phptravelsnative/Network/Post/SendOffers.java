package com.phptravelsnative.Network.Post;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Map;


public class SendOffers extends StringRequest {

    private static final String RegisterUrl= Constant.domain_name+"offers/sendMessage?appKey="+Constant.key;
    private Map<String,String>params;
    public SendOffers(String name,String email,String pnumber ,String message,Response.Listener<String> listener,Response.ErrorListener error)
    {
        super( Method.POST,RegisterUrl,listener,error);
        params=new HashMap<>();
        params.put("toemail",email);
        params.put("name",name);
        params.put("message",message);
        params.put("phone",pnumber);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
