package com.hanze.wad.friendshipbench;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Questionnaire;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment controller for the questionnaire detail page.
 */
public class QuestionnaireDetailsFragment extends CustomFragment {

    private Questionnaire questionnaire;
    private int id;

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
        initializeSuper(R.layout.questionnaire_details_layout, true, new QuestionnaireOverviewFragment(), inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){
        this.id = getArguments().getInt("questionnaire_id");
        fetchQuestionnaire(this.id);
    }

    /**
     * Make a GET request to the API to get the requested questionnaire.
     * @param id The ID of the requested questionnaire.
     */
    private void fetchQuestionnaire(int id) {

        // Make an API GET request.
        ApiController.getInstance(context).apiRequest(getResources().getString(R.string.questionnaires_url) + "/" + id, Request.Method.GET, null, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    questionnaire = new Questionnaire(new JSONObject(result));
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
     * Update the view with the right questionnaire information.
     */
    private void updateView(){

        // Update the header.
        ((TextView) activity.findViewById(R.id.questionnaireTextHeader)).setText("Questionnaire " + this.id);

        // Add a red flag icon to the header is needed.
        if(questionnaire.isRedflag())
            view.findViewById(R.id.redFlagHeader).setVisibility(View.VISIBLE);

        // Get the current layout which will wrap all the details.
        LinearLayout linearLayout = view.findViewById(R.id.questionnaireDetailsLayout);

        // Add a label value pair for every answer in the questionnaire.
        for (int i = 0; i < questionnaire.getAnswers().size(); i++) {

            // Create a new linearLabelValueLayout.
            Answer answer = questionnaire.getAnswers().get(i);
            ContextThemeWrapper layoutContext = new ContextThemeWrapper(activity.getBaseContext(), R.style.LinearLabelValueLayout);
            LinearLayout linearLabelValueLayout = new LinearLayout(layoutContext);

            // Create a new label text field.
            ContextThemeWrapper labelContext = new ContextThemeWrapper(activity.getBaseContext(), R.style.LabelText);
            TextView label = new TextView(labelContext);
            label.setText(answer.getQuestion().getQuestionText());

            // Create a new value text field.
            ContextThemeWrapper valueContext = new ContextThemeWrapper(activity.getBaseContext(), R.style.ValueText);
            TextView value = new TextView(valueContext);
            value.setText(answer.getAnswer() ? "Yes" : "No");

            // Add both text fields to the linearLabelValueLayout.
            linearLayout.addView(linearLabelValueLayout);
            linearLabelValueLayout.addView(label);
            linearLabelValueLayout.addView(value);
        }
    }
}
