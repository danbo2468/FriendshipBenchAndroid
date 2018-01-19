/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallbacks.VolleyCallbackArray;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallbacks.VolleyCallbackObject;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The controller that handles every API request.
 */
public class ApiController {

    /**
     * Make an API GET request to get an array.
     * @param url The endpoint for the request.
     * @param context The context.
     * @param callback The callback.
     */
    public static void getArray(String url, Context context, final VolleyCallbackArray callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", error.getMessage());
                    }
                });
        queue.add(arrayRequest);
    }

    /**
     * Make an API GET request to get an object.
     * @param url The endpoint for the request.
     * @param context The context.
     * @param callback The callback.
     */
    public static void getObject(String url, Context context, final VolleyCallbackObject callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", error.getMessage());
                    }
                });
        queue.add(objectRequest);
    }
}
