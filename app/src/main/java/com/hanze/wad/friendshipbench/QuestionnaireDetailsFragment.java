package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Questionnaire;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is the controller for the questionnaire_details_layout.xml.
 */
public class QuestionnaireDetailsFragment extends Fragment {

    private View view;
    private Questionnaire questionnaire;
    private int id;

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
        view = inflater.inflate(R.layout.questionnaire_details_layout, container, false);

        // Get the current questionnaire ID.
        Bundle bundle = getArguments();
        this.id = bundle.getInt("questionnaire_id");
        fetchQuestionnaire(this.id);

        // Return the view.
        return view;
    }

    /**
     * Make a GET request to the API to get the requested questionnaire.
     * @param id The ID of the requested questionnaire.
     */
    private void fetchQuestionnaire(int id) {

        // Make an API GET request.
        ApiController.getInstance(getActivity().getBaseContext()).getRequest(getResources().getString(R.string.questionnaires_url) + "/" + id, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToQuestionnaire(new JSONObject(result));
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
     * Convert a JSON object to a questionnaire.
     * @param json The JSON object with the question in it.
     */
    private void jsonToQuestionnaire(JSONObject json) {
        this.questionnaire = QuestionnaireController.jsonToDetailedModel(json);
        updateView();
    }

    /**
     * Update the view with the right questionnaire information.
     */
    private void updateView(){

        // Update the header.
        ((TextView) getActivity().findViewById(R.id.questionnaireTextHeader)).setText("Questionnaire " + this.id);

        // Add a red flag icon to the header is needed.
        if(questionnaire.isRedflag())
            view.findViewById(R.id.redFlagHeader).setVisibility(View.VISIBLE);

        // Get the current layout which will wrap all the details.
        LinearLayout linearLayout = view.findViewById(R.id.questionnaireDetailsLayout);

        // Add a label value pair for every answer in the questionnaire.
        for (int i = 0; i < questionnaire.getAnswers().size(); i++) {

            // Create a new linearLabelValueLayout.
            Answer answer = questionnaire.getAnswers().get(i);
            ContextThemeWrapper layoutContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.LinearLabelValueLayout);
            LinearLayout linearLabelValueLayout = new LinearLayout(layoutContext);

            // Create a new label text field.
            ContextThemeWrapper labelContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.LabelText);
            TextView label = new TextView(labelContext);
            label.setText(answer.getQuestion());

            // Create a new value text field.
            ContextThemeWrapper valueContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.ValueText);
            TextView value = new TextView(valueContext);
            value.setText(answer.getAnswer());

            // Add both text fields to the linearLabelValueLayout.
            linearLayout.addView(linearLabelValueLayout);
            linearLabelValueLayout.addView(label);
            linearLabelValueLayout.addView(value);
        }
    }
}
