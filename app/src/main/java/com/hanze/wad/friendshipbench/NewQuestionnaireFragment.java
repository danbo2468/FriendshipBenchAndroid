package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.AnswerPost;
import com.hanze.wad.friendshipbench.ApiModels.QuestionnairePost;
import com.hanze.wad.friendshipbench.Controllers.QuestionController;
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireController;
import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Question;
import com.hanze.wad.friendshipbench.Models.Questionnaire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is the controller for the new_questionnaire_layout.xml.
 * Created by Daniel Boonstra (Friendship Bench).
 */
public class NewQuestionnaireFragment extends Fragment {

    private static final int SUICIDE_QUESTION = 11;
    private static final int YES_ANSWERS_FOR_REDFLAG = 8;

    View view;
    ArrayList<Question> questionList = new ArrayList<>();
    ArrayList<Answer> answerList = new ArrayList<>();
    private boolean suicideQuestionIsYes;
    private int currentQuestion = 0;
    private int numberOfYesQuestions = 0;
    private int questionnaireId;

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
        view = inflater.inflate(R.layout.new_questionnaire_layout, container, false);
        fetchQuestions();

        // Handle the OnItemClick method for the yes and no buttons.
        Button buttonYes = view.findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numberOfYesQuestions++;
                if (questionList.get(currentQuestion).getId() == SUICIDE_QUESTION)
                    suicideQuestionIsYes = true;
                saveAnswer("Yes");
                nextQuestion();
            }
        });
        Button buttonNo = view.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAnswer("No");
                nextQuestion();
            }
        });
        return view;
    }

    /**
     * Do an API Request to get all the questions.
     */
    private void fetchQuestions() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.questions_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonToQuestions(new JSONArray(response));
                            showNewQuestion();
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

    private void showNewQuestion(){
        Question question = questionList.get(currentQuestion);
        ((TextView) getActivity().findViewById(R.id.questionText)).setText(question.getQuestion());
        ((TextView) getActivity().findViewById(R.id.progressText)).setText((currentQuestion+1) + "/" + questionList.size());
    }

    private void nextQuestion(){
        if(currentQuestion == (questionList.size()-1)) {
            try {
                submitQuestionnaire();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            currentQuestion++;
            showNewQuestion();
        }
    }

    private void submitQuestionnaire() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentTime = df.format(new Date());
        QuestionnairePost questionnaire = new QuestionnairePost("4216d9b5-43ae-47e8-b63a-ec200e8fc2a6", currentTime, (numberOfYesQuestions > YES_ANSWERS_FOR_REDFLAG || suicideQuestionIsYes));
        JSONObject json = new JSONObject(new Gson().toJson(questionnaire));
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        JsonObjectRequest stringRequest = new JsonObjectRequest (Request.Method.POST, getResources().getString(R.string.questionnaires_url), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            submitAnswers(QuestionnaireController.jsonToSummarizedModel(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getBaseContext(), "Sorry, something went wrong.", Toast.LENGTH_LONG).show();
                        Log.d("API", error.getMessage());
                    }
                });
        queue.add(stringRequest);
    }

    private void submitAnswers(Questionnaire questionnaire) throws JSONException {
        this.questionnaireId = questionnaire.getId();
        ArrayList<AnswerPost> answersPost = new ArrayList();
        for (int i = 0; i < answerList.size(); i++) {
            answersPost.add(new AnswerPost(answerList.get(i).getAnswer(), answerList.get(i).getId(), questionnaireId));
        }
        JSONArray json = new JSONArray(new Gson().toJson(answersPost));
        Log.d("API", json.toString());
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        JsonArrayRequest stringRequest = new JsonArrayRequest (Request.Method.POST, getResources().getString(R.string.answers_url), json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getActivity().getBaseContext(), "You've completed the SSQ14 questionnaire.", Toast.LENGTH_LONG).show();
                        QuestionnaireDetailsFragment fragment = new QuestionnaireDetailsFragment();
                        Bundle args = new Bundle();
                        args.putInt("questionnaire_id", questionnaireId);
                        fragment.setArguments(args);
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getBaseContext(), "You've completed the SSQ14 questionnaire.", Toast.LENGTH_LONG).show();
                        QuestionnaireDetailsFragment fragment = new QuestionnaireDetailsFragment();
                        Bundle args = new Bundle();
                        args.putInt("questionnaire_id", questionnaireId);
                        fragment.setArguments(args);
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    }
                });
        queue.add(stringRequest);
    }

    private void saveAnswer(String answer){
        Question question = questionList.get(currentQuestion);
        answerList.add(new Answer(question.getId(), question.getQuestion(), answer));
    }

    /**
     * Convert all the JSON questions to models and at them to the list.
     * @param json The JSON Array.
     * @throws JSONException An exception for when the JSON can't be parsed correctly.
     */
    private void jsonToQuestions(JSONArray json) throws JSONException {
        for (int i = 0; i < json.length(); i++) {
            JSONObject questionJson = json.getJSONObject(i);
            questionList.add(QuestionController.jsonToModel(questionJson));
        }
    }

}
