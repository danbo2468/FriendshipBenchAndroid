/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireListAdapter;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Appointment;
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
    private ArrayList<Questionnaire> allQuestionnairesList = new ArrayList<>();
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

        // Set the filter spinner details.
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.redflag_filter, android.R.layout.simple_spinner_item);
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
        ApiController.getInstance(context).apiRequest(getResources().getString(R.string.questionnaires_url), Request.Method.GET, null, activity.token.getAccessToken(), new VolleyCallback(){
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
            allQuestionnairesList.add(new Questionnaire(questionnaireJson));
        }

        // Filter the list.
        filterList("All");
    }

    /**
     * Filter the list with questionnaires.
     * @param filter The status to filter on.
     */
    private void filterList(String filter){
        questionnairesList.clear();
        if(filter.equals("Without red flag")) {
            for(Questionnaire questionnaire : allQuestionnairesList){
                if(!questionnaire.isRedflag())
                    questionnairesList.add(questionnaire);
            }
        } else if(filter.equals("With red flag")){
            for(Questionnaire questionnaire : allQuestionnairesList){
                if(questionnaire.isRedflag())
                    questionnairesList.add(questionnaire);
            }
        } else {
            for(Questionnaire questionnaire : allQuestionnairesList)
                questionnairesList.add(questionnaire);
        }
        if(questionnairesList.size() == 0)
            ((TextView) view.findViewById(R.id.text_no_questionnaire)).setText(getString(R.string.no_questionnaire_message));
        else
            ((TextView) view.findViewById(R.id.text_no_questionnaire)).setText("");
        questionnaireListAdapter.notifyDataSetChanged();
    }
}
