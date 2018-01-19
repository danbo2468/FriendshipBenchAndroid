/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.AppointmentController;
import com.hanze.wad.friendshipbench.Controllers.CustomListAdapter;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallbacks.VolleyCallbackArray;
import com.hanze.wad.friendshipbench.Models.Appointment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * The fragment controller for appointment_overview_layout.
 */
public class AppointmentOverviewFragment extends Fragment {

    private ArrayList<Appointment> appointmentsList = new ArrayList<>();
    private CustomListAdapter customAdapter;

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
        View view = inflater.inflate(R.layout.appointment_overview_layout, container, false);

        // Set the CustomListAdapter as adapter for the listview.
        ListView listView = view.findViewById(R.id.appointmentListView);
        customAdapter = new CustomListAdapter(getActivity().getBaseContext(), appointmentsList);
        listView.setAdapter(customAdapter);
        fetchAppointments();

        // Handle the OnItemClick method for a listitem. It will open a detailed view for the selected appointment.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                AppointmentDetailsFragment fragment = new AppointmentDetailsFragment();
                Bundle values = new Bundle();
                values.putInt("appointment_id", ((Appointment) parent.getItemAtPosition(position)).getId());
                fragment.setArguments(values);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        // Return the view.
        return view;
    }

    /**
     * Make a GET request to the API to get all the appointments.
     */
    private void fetchAppointments() {

        // Clear the list with appointments (for refreshing purposes).
        appointmentsList.clear();

        // Make an API GET request.
        ApiController.getArray(getResources().getString(R.string.appointments_url), getActivity().getBaseContext(), new VolleyCallbackArray(){
            @Override
            public void onSuccess(JSONArray result){
                jsonToAppointments(result);
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
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
            appointmentsList.add(AppointmentController.jsonToModel(appointmentJson));
        }

        // Let the custom adapter know that the dataset has been changed.
        customAdapter.notifyDataSetChanged();
    }
}
