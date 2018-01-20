/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers.VolleyCallbacks;

import com.android.volley.VolleyError;

public interface VolleyCallback {
    void onSuccess(String result);
    void onError(VolleyError result);
}
