/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Appointment;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment controller for the appointment detail page.
 */
public class AppointmentDetailsFragment extends CustomFragment implements OnMapReadyCallback {

    private Appointment appointment;
    private MapView mapView;

    /**
     * The OnCreateView method which will be called first.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The created view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initializeSuper(R.layout.appointment_details_layout, true, new AppointmentOverviewFragment(), inflater, container);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Fetch a new appointment.
        fetchAppointment(getArguments().getInt("appointment_id"));

        // Handle the OnItemClick method for the accept button. It will do an API PUT request.
        view.findViewById(R.id.acceptButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateStatus(true);
            }
        });

        // Handle the OnItemClick method for the cancel button. It will do an API PUT request.
        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateStatus(false);
            }
        });
    }

    /**
     * Make a GET request to the API to get the requested appointment.
     * @param id The ID of the requested appointment.
     */
    private void fetchAppointment(int id) {

        // Make an API GET request.
        ApiController.getInstance(context).apiRequest(getResources().getString(R.string.appointments_url) + "/" + id, Request.Method.GET, null, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    appointment = new Appointment(new JSONObject(result));
                    updateView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(context, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Update the view with the right appointment information.
     */
    private void updateView(){

        // Update all the text items.
        ((TextView) activity.findViewById(R.id.appointmentTextHeader)).setText(appointment.getSummary());
        ((TextView) activity.findViewById(R.id.appointmentWhenValue)).setText(appointment.getFancyTime());
        ((TextView) activity.findViewById(R.id.appointmentWhoValue)).setText("You and " + appointment.getHealthworker().getFullname());
        ((TextView) activity.findViewById(R.id.appointmentWhereValue)).setText(appointment.getBench().getFullLocation());
        ((TextView) activity.findViewById(R.id.appointmentStatusValue)).setText(appointment.getFancyStatus());

        // Show or hide the right buttons according to the current appointment status.
        if(appointment.getStatus().equals("PENDING")){
            (activity.findViewById(R.id.acceptButton)).setVisibility(View.VISIBLE);
            (activity.findViewById(R.id.cancelButton)).setVisibility(View.VISIBLE);
        } else if(appointment.getStatus().equals("ACCEPTED")){
            (activity.findViewById(R.id.acceptButton)).setVisibility(View.GONE);
            (activity.findViewById(R.id.cancelButton)).setVisibility(View.VISIBLE);
        } else {
            (activity.findViewById(R.id.acceptButton)).setVisibility(View.GONE);
            (activity.findViewById(R.id.cancelButton)).setVisibility(View.GONE);
        }
    }

    /**
     * Update the appointment status by making an API PUT request.
     * @param accepted Whether the appointment has been accepted.
     */
    private void updateStatus(boolean accepted) {

        String url;
        if(accepted)
            url = getString(R.string.appointments_url) + "/" + appointment.getId() + "/accept";
        else
            url = getString(R.string.appointments_url) + "/" + appointment.getId() + "/cancel";

        // Make an API PUT request.
        ApiController.getInstance(context).apiRequest(url, Request.Method.PUT, null, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                Toast.makeText(context, "The status for this appointment has been updated.", Toast.LENGTH_LONG).show();
                fetchAppointment(appointment.getId());
                updateView();
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(context, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Initialize the map.
     * @param googleMap The map.
     */
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = appointment.getBench().getLatLong(context);
        googleMap.addMarker(new MarkerOptions().position(location).title(appointment.getBench().getFullLocation()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
