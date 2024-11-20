package com.phptravelsnative.Network.Post;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Map;


public class RequestRegister extends StringRequest {

   private static final String RegisterUrl= Constant.domain_name+"login/signup?appKey="+ Constant.key;
    private Map<String,String>params;
    public RequestRegister(String email, String password, String first_name, String last_name, String phone, Response.Listener<String> listener)
    {
          super( Method.POST,RegisterUrl,listener,null);
        params=new HashMap<>();
        params.put("email",email);
        params.put("password",password);
        params.put("first_name",first_name);
        params.put("last_name",last_name);
        params.put("phone",phone);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
