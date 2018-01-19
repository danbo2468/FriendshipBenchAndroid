package com.hanze.wad.friendshipbench;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireController;
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireListAdapter;
import com.hanze.wad.friendshipbench.Models.Questionnaire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * This class is the controller for the questionnaire_overview_layout.xml.
 * Created by Daniel Boonstra (Friendship Bench).
 */
public class QuestionnaireOverviewFragment extends Fragment {

    View view;
    ArrayList<Questionnaire> questionnairesList = new ArrayList<>();
    QuestionnaireListAdapter customAdapter;

    /**
     * This method is called when a view is opened.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.questionnaire_overview_layout, container, false);

        // Set the QuestionnaireListAdapter as adapter for the listview.
        ListView listView = view.findViewById(R.id.questionnaireListView);
        customAdapter = new QuestionnaireListAdapter(getActivity().getBaseContext(), questionnairesList);
        listView.setAdapter(customAdapter);
        fetchQuestionnaires();

        // Handle the OnItemClick method.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                Questionnaire questionnaire = (Questionnaire) parent.getItemAtPosition(position);
                QuestionnaireDetailsFragment fragment = new QuestionnaireDetailsFragment();

                // Sending the id to the new details view.
                Bundle args = new Bundle();
                args.putInt("questionnaire_id", questionnaire.getId());
                fragment.setArguments(args);

                // Start the animation.
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        // Handle the OnItemClick method for the Floating Action Button
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new NewQuestionnaireFragment()).commit();
            }
        });
        return view;
    }

    /**
     * Do an API Request to get all the questionnaires.
     */
    private void fetchQuestionnaires() {
        questionnairesList.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.questionnaires_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonToQuestionnaire(new JSONArray(response));
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", error.getMessage());
                    }
                });
        queue.add(stringRequest);
    }

    /**
     * Convert all the JSON questionnaires to models and at them to the list.
     * @param json The JSON Array.
     * @throws JSONException An exception for when the JSON can't be parsed correctly.
     */
    private void jsonToQuestionnaire(JSONArray json) throws JSONException {
        for (int i = 0; i < json.length(); i++) {
            JSONObject questionnaireJson = json.getJSONObject(i);
            questionnairesList.add(QuestionnaireController.jsonToSummarizedModel(questionnaireJson));
        }
    }

}
