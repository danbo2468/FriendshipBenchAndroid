/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers;

import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * The controller that handles every API request.
 */
public class ApiController {

    private static ApiController instance = null;
    private RequestQueue requestQueue;

    /**
     * The constructor for the ApiController.
     * @param context The context for creating a new RequestQueue.
     */
    private ApiController(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Get an instance of the ApiController.
     * @param context The context which is required to create a new ApiController.
     * @return The instance of the ApiController.
     */
    public static ApiController getInstance(Context context){
        if(instance == null){
            instance = new ApiController(context);
        }
        return instance;
    }

    /**
     * Make an API GET request to get a String response.
     * @param url The endpoint for the request.
     * @param callback The callback that will be called afterwards.
     */
    public void apiRequest(String url, int method, final Object body, final String token, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse == null)
                            Log.d("API", "Unknown error occurred");
                        else
                            Log.d("API", "Error " + error.networkResponse.statusCode + " occurred");
                        callback.onError(error);
                    }
                }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    if(token != null)
                        params.put("Authorization", "Bearer " + token);
                    return params;
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    if(body != null)
                        return body.toString().getBytes();
                    return null;
                }
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
        };
        requestQueue.add(stringRequest);
    }

    public void getToken(String url, final String username, final String password, final String base64, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse == null)
                            Log.d("API", "Unknown error occurred");
                        else
                            Log.d("API", "Error " + error.networkResponse.statusCode + " occurred");
                        callback.onError(error);
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Basic " + base64);
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("grant_type", "password");
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
}
