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
import android.widget.ListView;
import android.widget.TextView;
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
 * Fragment controller for the questionnaire overview page.
 */
public class QuestionnaireOverviewFragment extends CustomFragment {

    private ArrayList<Questionnaire> questionnairesList = new ArrayList<>();
    private QuestionnaireListAdapter questionnaireListAdapter;

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
        initializeSuper(R.layout.questionnaire_overview_layout, true, null, inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Set the QuestionnaireListAdapter as adapter for the listview.
        ListView questionnaireListView = view.findViewById(R.id.questionnaireListView);
        questionnaireListAdapter= new QuestionnaireListAdapter(context, questionnairesList);
        questionnaireListView.setAdapter(questionnaireListAdapter);
        fetchQuestionnaires();

        // Handle the OnItemClick method.
        questionnaireListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                QuestionnaireDetailsFragment fragment = new QuestionnaireDetailsFragment();
                bundle.putInt("questionnaire_id", ((Questionnaire) parent.getItemAtPosition(position)).getId());
                fragment.setArguments(bundle);
                switchFragment(fragment, true);
            }
        });

        // Handle the OnItemClick method for the Floating Action Button
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchFragment(new NewQuestionnaireFragment(), true);
            }
        });
    }

    /**
     * Make a GET request to the API to get all the questionnaires.
     */
    private void fetchQuestionnaires() {

        // Clear the list with appointments (for refreshing purposes).
        questionnairesList.clear();

        // Make an API GET request.
        Log.d("TEST", getResources().getString(R.string.questionnaires_url) + "?clientId=" + activity.user.getId());
        ApiController.getInstance(context).getRequest(getResources().getString(R.string.questionnaires_url) + "?clientId=" + ((MainActivity)getActivity()).user.getId(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToQuestionnaire(new JSONArray(result));
                } catch (JSONException e) {
                    ((TextView) view.findViewById(R.id.text_no_questionnaire)).setText(getString(R.string.no_questionnaire_message));
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
        questionnaireListAdapter.notifyDataSetChanged();
    }
}
