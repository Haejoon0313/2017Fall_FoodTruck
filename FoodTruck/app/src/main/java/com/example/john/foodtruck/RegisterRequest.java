package com.example.john.foodtruck;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 2017-11-06.
 */

public class RegisterRequest extends StringRequest{

    final static private String URL = "http://192.168.3.1:5000/sign_in";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userName, String userNumber, String userType, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", userID);
        parameters.put("password", userPassword);
        parameters.put("name", userName);
        parameters.put("phone", userNumber);
        parameters.put("type", userType);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

