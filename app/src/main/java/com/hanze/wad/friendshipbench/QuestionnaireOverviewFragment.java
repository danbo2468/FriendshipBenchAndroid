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
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireController;
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireListAdapter;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Questionnaire;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * This class is the controller for the questionnaire_overview_layout.xml.
 */
public class QuestionnaireOverviewFragment extends Fragment {

    private ArrayList<Questionnaire> questionnairesList = new ArrayList<>();
    private QuestionnaireListAdapter customAdapter;

    /**
     * Initialize the view.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Get the current view.
        View view = inflater.inflate(R.layout.questionnaire_overview_layout, container, false);

        // Set the QuestionnaireListAdapter as adapter for the listview.
        ListView listView = view.findViewById(R.id.questionnaireListView);
        customAdapter = new QuestionnaireListAdapter(getActivity().getBaseContext(), questionnairesList);
        listView.setAdapter(customAdapter);
        fetchQuestionnaires();

        // Handle the OnItemClick method.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                QuestionnaireDetailsFragment fragment = new QuestionnaireDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("questionnaire_id", ((Questionnaire) parent.getItemAtPosition(position)).getId());
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        // Handle the OnItemClick method for the Floating Action Button
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new NewQuestionnaireFragment()).commit();
            }
        });

        // Return the view.
        return view;
    }

    /**
     * Make a GET request to the API to get all the questionnaires.
     */
    private void fetchQuestionnaires() {

        // Clear the list with appointments (for refreshing purposes).
        questionnairesList.clear();

        // Make an API GET request.
        ApiController.getInstance(getActivity().getBaseContext()).getRequest(getResources().getString(R.string.questionnaires_url), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToQuestionnaire(new JSONArray(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Convert a JSON list with questionnaires to a list with questionnaire models.
     * @param json The JSON array with all the appointments in it.
     */
    private void jsonToQuestionnaire(JSONArray json) {

        // Loop through the list.
        for (int i = 0; i < json.length(); i++) {
            JSONObject questionnaireJson = null;
            try {
                questionnaireJson = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            questionnairesList.add(QuestionnaireController.jsonToSummarizedModel(questionnaireJson));
        }

        // Let the custom adapter know that the dataset has been changed.
        customAdapter.notifyDataSetChanged();
    }

}
