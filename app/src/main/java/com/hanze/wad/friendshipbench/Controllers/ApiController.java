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
import com.hanze.wad.friendshipbench.Controllers.VolleyCallbacks.VolleyCallback;

import org.json.JSONObject;

/**
 * The controller that handles every API request.
 */
public class ApiController {

    private static ApiController instance = null;
    private RequestQueue requestQueue;

    private ApiController(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public static ApiController getInstance(Context context){
        if(instance == null){
            instance = new ApiController(context);
        }
        return instance;
    }

    /**
     * Make an API GET request to get an array.
     * @param url The endpoint for the request.
     * @param callback The callback.
     */
    public void getRequest(String url, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", error.getMessage());
                        callback.onError(error);
                    }
                });
        requestQueue.add(stringRequest);
    }

    public void putObjectRequest(String url, final JSONObject object, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", error.getMessage());
                        callback.onError(error);
                    }
                }){
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return object.toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                };
        requestQueue.add(stringRequest);
    }
}
