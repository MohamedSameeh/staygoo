package com.phptravelsnative.Network.Post;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Map;


public class contact_request extends StringRequest {

   private static final String RegisterUrl= Constant.domain_name+"contact/send/info?appKey="+Constant.key;

    private Map<String,String>params;
    public contact_request(String email, String name, String subject, String message, String to, Response.Listener<String> listener)
    {
          super( Method.POST,RegisterUrl,listener,null);
        params=new HashMap<>();
        params.put("email",email);
        params.put("name",name);
        params.put("subject",subject);
        params.put("message",message);
        params.put("sendto",to);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
