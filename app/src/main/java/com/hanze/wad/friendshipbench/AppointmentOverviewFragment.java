/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.AppointmentListAdapter;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Appointment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Fragment controller for the appointment overview page.
 */
public class AppointmentOverviewFragment extends CustomFragment {

    private ArrayList<Appointment> appointmentsList = new ArrayList<>();
    private ArrayList<Appointment> allAppointmentsList = new ArrayList<>();
    private AppointmentListAdapter appointmentListAdapter;

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
        initializeSuper(R.layout.appointment_overview_layout, true, null, inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Set the AppointmentListAdapter as adapter for the listview.
        ListView appointmentListView = view.findViewById(R.id.appointmentListView);
        appointmentListAdapter = new AppointmentListAdapter(context, appointmentsList);
        appointmentListView.setAdapter(appointmentListAdapter);
        fetchAppointments();

        // Set the filter spinner details.
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.appointments_filter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterList(parentView.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Handle the OnItemClick method for a listitem. It will open a detailed view for the selected appointment.
        appointmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                AppointmentDetailsFragment fragment = new AppointmentDetailsFragment();
                bundle.putInt("appointment_id", ((Appointment) parent.getItemAtPosition(position)).getId());
                fragment.setArguments(bundle);
                switchFragment(fragment, true);
            }
        });
    }

    /**
     * Make a GET request to the API to get all the appointments.
     */
    private void fetchAppointments() {

        // Clear the list with appointments (for refreshing purposes).
        allAppointmentsList.clear();

        // Make an API GET request.
        ApiController.getInstance(context).apiRequest(getResources().getString(R.string.appointments_url), Request.Method.GET, null, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToAppointments(new JSONArray(result));
                } catch (JSONException e) {
                    ((TextView) view.findViewById(R.id.text_no_appointment)).setText(getString(R.string.no_appointment_message));
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
     * Convert a JSON list with appointments to a list with appointment models.
     * @param json The JSON array with all the appointments in it.
     */
    private void jsonToAppointments(JSONArray json){

        // Loop through the list.
        for (int i = 0; i < json.length(); i++) {
            JSONObject appointmentJson = null;
            try {
                appointmentJson = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            allAppointmentsList.add(new Appointment(appointmentJson));
        }

        // Filter the list with appointments.
        filterList("All");
    }

    /**
     * Filter the list with appointments.
     * @param filter The status to filter on.
     */
    private void filterList(String filter){
        appointmentsList.clear();
        if(filter.equals("All")) {
            for(Appointment appointment : allAppointmentsList)
                appointmentsList.add(appointment);
        } else {
            for(Appointment appointment : allAppointmentsList){
                if(appointment.getStatus().toUpperCase().equals(filter.toUpperCase()))
                    appointmentsList.add(appointment);
            }
        }
        if(appointmentsList.size() == 0)
            ((TextView) view.findViewById(R.id.text_no_appointment)).setText(getString(R.string.no_appointment_message));
        else
            ((TextView) view.findViewById(R.id.text_no_appointment)).setText("");
        appointmentListAdapter.notifyDataSetChanged();
    }
}
