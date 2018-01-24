package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.AnswerPost;
import com.hanze.wad.friendshipbench.ApiModels.QuestionnairePost;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.QuestionController;
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Question;
import com.hanze.wad.friendshipbench.Models.Questionnaire;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is the controller for the new_questionnaire_layout.xml.
 */
public class NewQuestionnaireFragment extends Fragment {

    private static final int SUICIDE_QUESTION = 11;
    private static final int YES_ANSWERS_FOR_REDFLAG = 8;

    private ArrayList<Question> questionList = new ArrayList<>();
    private ArrayList<Answer> answerList = new ArrayList<>();
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

        // Get the current view.
        View view = inflater.inflate(R.layout.new_questionnaire_layout, container, false);
        fetchQuestions();

        // Handle the OnItemClick method for the yes button.
        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numberOfYesQuestions++;
                if (questionList.get(currentQuestion).getId() == SUICIDE_QUESTION)
                    suicideQuestionIsYes = true;
                saveAnswerAndContinue("Yes");
            }
        });

        // Handle the OnItemClick method for the no button.
        Button buttonNo = view.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAnswerAndContinue("No");
            }
        });

        // Return the view.
        return view;
    }

    /**
     * Make a GET request to the API to get all the questions.
     */
    private void fetchQuestions() {

        // Make an API GET request.
        ApiController.getInstance(getActivity().getBaseContext()).getRequest(getResources().getString(R.string.questions_url), new VolleyCallback(){
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
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Update the text fields to show a new question.
     */
    private void showNewQuestion(){
        Question question = questionList.get(currentQuestion);
        String progress = (currentQuestion+1) + "/" + questionList.size();
        ((TextView) getActivity().findViewById(R.id.questionText)).setText(question.getQuestion());
        ((TextView) getActivity().findViewById(R.id.progressText)).setText(progress);
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

        // Convert the current date to the right format.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentTime = df.format(new Date());

        // Create a new questionnaire.
        QuestionnairePost questionnaire = new QuestionnairePost( ((MainActivity)getActivity()).user.getId(), currentTime, (numberOfYesQuestions > YES_ANSWERS_FOR_REDFLAG || suicideQuestionIsYes));
        JSONObject json = null;
        try {
            json = new JSONObject(new Gson().toJson(questionnaire));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make an API POST request.
        ApiController.getInstance(getActivity().getBaseContext()).postRequest(getResources().getString(R.string.questionnaires_url), json, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                JSONObject response = null;
                try {
                    response = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                submitAnswers(QuestionnaireController.jsonToSummarizedModel(response));
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Make an API POST request to submit all the answers.
     * @param questionnaire The questionnaire where the answers belong to.
     */
    private void submitAnswers(Questionnaire questionnaire) {

        // Save the questionnaire ID.
        this.questionnaireId = questionnaire.getId();

        // Create an AnswerPost model for every answer.
        ArrayList<AnswerPost> answersPost = new ArrayList<>();
        for (int i = 0; i < answerList.size(); i++) {
            answersPost.add(new AnswerPost(answerList.get(i).getAnswer(), answerList.get(i).getId(), questionnaireId));
        }

        // Create JSON from the list.
        JSONArray json = null;
        try {
            json = new JSONArray(new Gson().toJson(answersPost));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Make an API POST request.
        ApiController.getInstance(getActivity().getBaseContext()).postRequest(getResources().getString(R.string.answers_url), json, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                Toast.makeText(getActivity().getBaseContext(), "You've completed the SSQ14 questionnaire.", Toast.LENGTH_LONG).show();
                QuestionnaireDetailsFragment fragment = new QuestionnaireDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("questionnaire_id", questionnaireId);
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Save an answer to the list and continue to the next question.
     * @param answer The given answer. 'Yes' or 'No'.
     */
    private void saveAnswerAndContinue(String answer){
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
