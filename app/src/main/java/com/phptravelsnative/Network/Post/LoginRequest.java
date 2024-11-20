package com.phptravelsnative.Network.Post;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.phptravelsnative.utality.Extra.Constant;

import java.util.HashMap;
import java.util.Map;


public class LoginRequest extends StringRequest {

   private static final String RegisterUrl= Constant.domain_name+"login/check?appKey="+ Constant.key;
    private Map<String,String>params;
    public LoginRequest(String email, String password,Response.Listener<String> listener)
    {
          super( Method.POST,RegisterUrl,listener,null);
        params=new HashMap<>();
        params.put("email",email);
        params.put("password",password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
