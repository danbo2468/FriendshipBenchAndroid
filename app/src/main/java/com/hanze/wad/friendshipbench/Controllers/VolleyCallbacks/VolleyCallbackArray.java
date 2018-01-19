/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers.VolleyCallbacks;

import com.android.volley.VolleyError;
import org.json.JSONArray;

public interface VolleyCallbackArray{
    void onSuccess(JSONArray result);
    void onError(VolleyError result);
}
