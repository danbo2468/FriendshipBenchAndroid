/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.AppointmentPut;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.AppointmentController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallbacks.VolleyCallbackObject;
import com.hanze.wad.friendshipbench.Models.Appointment;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The fragment controller for appointment_details_layout.
 */
public class AppointmentDetailsFragment extends Fragment {

    private Appointment appointment;

    /**
     * Initialize the view.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The current view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Get the current view.
        View view = inflater.inflate(R.layout.appointment_details_layout, container, false);

        // Get the current appointment ID.
        Bundle bundle = getArguments();
        fetchAppointment(bundle.getInt("appointment_id"));

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

        // Return the view.
        return view;
    }

    /**
     * Make a GET request to the API to get the requested appointment.
     */
    private void fetchAppointment(int id) {

        // Make an API GET request.
        ApiController.getObject(getResources().getString(R.string.appointments_url) + "/" + id, getActivity().getBaseContext(), new VolleyCallbackObject(){
            @Override
            public void onSuccess(JSONObject result){
                jsonToAppointment(result);
            }
            @Override
            public void onError(VolleyError result){
                Log.d("API", "ERROR: " + result.getMessage());
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Convert a JSON object to an appointment.
     * @param json The JSON array with all the appointments in it.
     */
    private void jsonToAppointment(JSONObject json) {
        this.appointment = AppointmentController.jsonToModel(json);
        updateView();
    }

    /**
     * Update the view with the right appointment information.
     */
    private void updateView(){

        // Update all the text items.
        ((TextView) getActivity().findViewById(R.id.appointmentTextHeader)).setText(appointment.getSummary());
        ((TextView) getActivity().findViewById(R.id.appointmentWhenValue)).setText(appointment.getReadableTime());
        ((TextView) getActivity().findViewById(R.id.appointmentWhoValue)).setText("You and " + appointment.getHealthworker().getFullName());
        ((TextView) getActivity().findViewById(R.id.appointmentWhereValue)).setText(appointment.getBench().getFullLocation());
        ((TextView) getActivity().findViewById(R.id.appointmentStatusValue)).setText(appointment.getStatus().getName());

        // Show or hide the right buttons according to the current appointment status.
        if(appointment.getStatus().getId() == 1){
            (getActivity().findViewById(R.id.acceptButton)).setVisibility(View.VISIBLE);
            (getActivity().findViewById(R.id.cancelButton)).setVisibility(View.VISIBLE);
        } else if(appointment.getStatus().getId() == 2){
            (getActivity().findViewById(R.id.acceptButton)).setVisibility(View.GONE);
            (getActivity().findViewById(R.id.cancelButton)).setVisibility(View.VISIBLE);
        } else {
            (getActivity().findViewById(R.id.acceptButton)).setVisibility(View.GONE);
            (getActivity().findViewById(R.id.cancelButton)).setVisibility(View.GONE);
        }
    }

    /**
     * Update the appointment status by making an API PUT request.
     * @param accepted Whether the appointment has been accepted.
     */
    private void updateStatus(boolean accepted) {

        // Create a new appointment model in order to do a PUT request.
        final AppointmentPut appointmentPut;
        if(accepted)
            appointmentPut = new AppointmentPut(appointment.getId(), appointment.getTime(), 2, appointment.getBench().getId(), appointment.getClient().getId(), appointment.getHealthworker().getId());
        else
            appointmentPut = new AppointmentPut(appointment.getId(), appointment.getTime(), 3, appointment.getBench().getId(), appointment.getClient().getId(), appointment.getHealthworker().getId());
        JSONObject json = null;
        try {
            json = new JSONObject(new Gson().toJson(appointmentPut));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make an API PUT request.
        ApiController.putObject(getResources().getString(R.string.appointments_url) + "/" + appointment.getId(), json, getActivity().getBaseContext(), new VolleyCallbackObject(){
            @Override
            public void onSuccess(JSONObject result){
                Toast.makeText(getActivity().getBaseContext(), "The status for this appointment has been updated.", Toast.LENGTH_LONG).show();
                fetchAppointment(appointment.getId());
                updateView();
            }
            @Override
            public void onError(VolleyError result){
                if(result.getMessage().startsWith("org.json.JSONException")){
                    onSuccess(null);
                    return;
                }
                Log.d("API", "ERROR: " + result.getMessage());
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }
}
