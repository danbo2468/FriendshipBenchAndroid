/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers.VolleyCallbacks;

import com.android.volley.VolleyError;
import org.json.JSONObject;

public interface VolleyCallbackObject {
    void onSuccess(JSONObject result);
    void onError(VolleyError result);
}
