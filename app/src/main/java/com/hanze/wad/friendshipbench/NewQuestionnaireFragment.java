package com.hanze.wad.friendshipbench;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.AnswerPost;
import com.hanze.wad.friendshipbench.ApiModels.QuestionPost;
import com.hanze.wad.friendshipbench.ApiModels.QuestionnairePost;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.QuestionController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment controller for the new questionnaire page.
 */
public class NewQuestionnaireFragment extends CustomFragment {

    private static final int SUICIDE_QUESTION = 11;
    private static final int YES_ANSWERS_FOR_REDFLAG = 8;

    private ArrayList<Question> questionList = new ArrayList<>();
    private ArrayList<Answer> answerList = new ArrayList<>();
    private boolean suicideQuestionIsYes;
    private int currentQuestion = 0;
    private int numberOfYesQuestions = 0;
    private int questionnaireId;

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
        initializeSuper(R.layout.new_questionnaire_layout, true, new QuestionnaireOverviewFragment(), inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Get all the questions.
        fetchQuestions();

        // Handle the OnItemClick method for the yes button.
        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numberOfYesQuestions++;
                if (questionList.get(currentQuestion).getId() == SUICIDE_QUESTION)
                    suicideQuestionIsYes = true;
                saveAnswerAndContinue(true);
            }
        });

        // Handle the OnItemClick method for the no button.
        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAnswerAndContinue(false);
            }
        });
    }

    /**
     * Make a GET request to the API to get all the questions.
     */
    private void fetchQuestions() {

        // Make an API GET request.
        ApiController.getInstance(context).getRequest(getResources().getString(R.string.questions_url) + "?only-active=true", activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToQuestions(new JSONArray(result));
                    showNewQuestion();
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
     * Update the text fields to show a new question.
     */
    private void showNewQuestion(){
        Question question = questionList.get(currentQuestion);
        String progress = (currentQuestion+1) + "/" + questionList.size();
        ((TextView) activity.findViewById(R.id.questionText)).setText(question.getQuestion());
        ((TextView) activity.findViewById(R.id.progressText)).setText(progress);
    }

    /**
     * Submit the questionnaire or show the next question.
     */
    private void nextQuestion(){
        if(currentQuestion == (questionList.size()-1)) {
            submitQuestionnaire();
        } else {
            currentQuestion++;
            showNewQuestion();
        }
    }

    /**
     * Make an API POST request to submit the new questionnaire.
     */
    private void submitQuestionnaire() {

        // Hide the buttons.
        view.findViewById(R.id.buttonYes).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.buttonNo).setVisibility(View.INVISIBLE);

        // Convert the current date to the right format.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentTime = df.format(new Date());

        // Create an AnswerPost model for every answer.
        ArrayList<AnswerPost> answersPost = new ArrayList<>();
        for (int i = 0; i < answerList.size(); i++) {
            answersPost.add(new AnswerPost(answerList.get(i).getAnswer(), new QuestionPost(answerList.get(i).getId())));
        }

        // Create a new questionnaire.
        QuestionnairePost questionnaire = new QuestionnairePost(currentTime, (numberOfYesQuestions > YES_ANSWERS_FOR_REDFLAG || suicideQuestionIsYes), answersPost);
        JSONObject json = null;
        try {
            json = new JSONObject(new Gson().toJson(questionnaire));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make an API POST request.
        ApiController.getInstance(context).postRequest(getResources().getString(R.string.questionnaires_url), json, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    questionnaireId = jsonResponse.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "You've completed the SSQ14 questionnaire.", Toast.LENGTH_LONG).show();
                QuestionnaireDetailsFragment fragment = new QuestionnaireDetailsFragment();
                bundle.putInt("questionnaire_id", questionnaireId);
                fragment.setArguments(bundle);
                switchFragment(fragment, true);
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(context, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Save an answer to the list and continue to the next question.
     * @param answer The given answer. 'Yes' or 'No'.
     */
    private void saveAnswerAndContinue(Boolean answer){
        Question question = questionList.get(currentQuestion);
        answerList.add(new Answer(question.getId(), question.getQuestion(), answer));
        nextQuestion();
    }

    /**
     * Convert a JSON object to a question.
     * @param json The JSON object with the question in it.
     */
    private void jsonToQuestions(JSONArray json) {
        for (int i = 0; i < json.length(); i++) {
            JSONObject questionJson = null;
            try {
                questionJson = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            questionList.add(QuestionController.jsonToModel(questionJson));
        }
    }

}
