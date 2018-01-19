package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.AppointmentPut;
import com.hanze.wad.friendshipbench.Controllers.AppointmentController;
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireController;
import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Appointment;
import com.hanze.wad.friendshipbench.Models.Questionnaire;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * This class is the controller for the questionnaire_details_layout.xml.
 * Created by Daniel Boonstra (Friendship Bench).
 */
public class QuestionnaireDetailsFragment extends Fragment {

    View view;
    Questionnaire questionnaire;
    int id;

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
        view = inflater.inflate(R.layout.questionnaire_details_layout, container, false);
        Bundle bundle = getArguments();
        this.id = bundle.getInt("questionnaire_id");
        fetchQuestionnaire(bundle.getInt("questionnaire_id"));
        return view;
    }

    /**
     * Do an API Request to get a certain questionnaire.
     * @param id The ID of the requested questionnaire.
     */
    private void fetchQuestionnaire(int id) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.questionnaires_url) + "/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonToQuestionnaire(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateView();
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
     * Convert the JSON appointment to a model.
     * @param json The JSON object.
     * @throws JSONException An exception for when the JSON can't be parsed correctly.
     */
    private void jsonToQuestionnaire(JSONObject json) throws JSONException {
        this.questionnaire = QuestionnaireController.jsonToDetailedModel(json);
    }

    /**
     * Update the current fragment fields.
     */
    private void updateView(){
        ((TextView) getActivity().findViewById(R.id.questionnaireTextHeader)).setText("Questionnaire " + this.id);
        if(questionnaire.isRedflag()){
            view.findViewById(R.id.redFlagHeader).setVisibility(View.VISIBLE);
        }
        LinearLayout linearLayout = view.findViewById(R.id.questionnaireDetailsLayout);
        for (int i = 0; i < questionnaire.getAnswers().size(); i++) {
            Answer answer = questionnaire.getAnswers().get(i);

            ContextThemeWrapper layoutContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.LinearLabelValueLayout);
            LinearLayout linearLabelValueLayout = new LinearLayout(layoutContext);

            ContextThemeWrapper labelContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.LabelText);
            TextView label = new TextView(labelContext);
            label.setText(answer.getQuestion());

            ContextThemeWrapper valueContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.ValueText);
            TextView value = new TextView(valueContext);
            value.setText(answer.getAnswer());

            linearLayout.addView(linearLabelValueLayout);
            linearLabelValueLayout.addView(label);
            linearLabelValueLayout.addView(value);

        }
    }
}
