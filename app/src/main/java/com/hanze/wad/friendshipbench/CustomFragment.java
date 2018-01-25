/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Fragment controller for every fragment. Needs to be extended.
 */
public abstract class CustomFragment extends Fragment {

    protected View view;
    protected MainActivity activity;
    protected Context context;
    protected FragmentManager fragmentManager;
    protected Bundle bundle;
    protected Fragment previousFragment;

    /**
     * The initializeSuper method which will be called by the child class.
     * @param layout The layout.
     * @param connectionRequired Whether or not a connection is required.
     * @param previousFragment The previous fragment.
     * @param inflater The inflater.
     * @param container The container.
     */
    public void initializeSuper(int layout, boolean connectionRequired, Fragment previousFragment, LayoutInflater inflater, @Nullable ViewGroup container) {

        // Set all variables.
        view = inflater.inflate(layout, container, false);
        activity = (MainActivity) getActivity();
        context = activity.getBaseContext();
        fragmentManager = getFragmentManager();
        this.previousFragment = previousFragment;
        bundle = new Bundle();
        activity.currentFragment = this;

        // Check if there is an internet connection available.
        if(!isNetworkConnected() && connectionRequired)
            Toast.makeText(context, getResources().getString(R.string.no_connection_message), Toast.LENGTH_LONG).show();
        else
            initializeFragment();
    }

    /**
     * Show a new fragment.
     * @param newFragment The fragment that should be shown.
     * @param forward Whether the animation would be a forward or a backward one.
     */
    public void switchFragment(Fragment newFragment, boolean forward){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(forward)
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
        else
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

        fragmentTransaction.replace(R.id.content_frame, newFragment);
        fragmentTransaction.commit();
    }

    /**
     * Check if there is a connection available.
     * @return Whether there is a connection available.
     */
    protected boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected boolean goBack(){
        if(previousFragment == null)
            return false;
        switchFragment(previousFragment, false);
        return true;
    }

    protected void initializeFragment() {}
}
