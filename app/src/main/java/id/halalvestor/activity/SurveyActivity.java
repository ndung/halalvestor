package id.halalvestor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.SurveyFragmentAdapter;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;

public class SurveyActivity extends BaseActivity implements StepperLayout.StepperListener {

    private static final String TAG = SurveyActivity.class.toString();

    private int startingStepPosition = 0;
    protected StepperLayout mStepperLayout;

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    private static final String QUESTIONS = "questions";

    private String questions;
    private String callingActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.survey_layout);

        callingActivity = getIntent().getStringExtra("activity");

        mStepperLayout = findViewById(R.id.stepperLayout);
        startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) : 0;
        questions = savedInstanceState != null ? savedInstanceState.getString(QUESTIONS) : null;
        fetchQuestions(questions);
    }

    private void onSuccessfullyQuestionFetched(String response) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<SurveyQuestion> list = gson.fromJson(response, new TypeToken<List<SurveyQuestion>>() {}.getType());
        Map<Integer,List<SurveyQuestion>> map = new TreeMap<>();
        for (SurveyQuestion sq : list){
            List<SurveyQuestion> newlist = new ArrayList<>();
            if (map.containsKey(sq.getSection())){
                newlist = map.get(sq.getSection());
                newlist.add(sq);
            }else{
                newlist.add(sq);
            }
            map.put(sq.getSection(),newlist);
        }
        Log.d(TAG, "question map:"+map);
        mStepperLayout.setAdapter(new SurveyFragmentAdapter(getSupportFragmentManager(), this, map), startingStepPosition);
        mStepperLayout.setListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        outState.putString(QUESTIONS, questions);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        final int currentStepPosition = mStepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            mStepperLayout.setCurrentStepPosition(currentStepPosition - 1);
        } else {
            fetchAnswers();
        }
    }

    private void onSuccessfullyAnswerFetched(String response) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Map<Integer, List<QuestionParameter>> map = gson.fromJson(response, new TypeToken<Map<Integer, List<QuestionParameter>>>() {
            }.getType());
            App.getInstance().getPreferenceManager().setSurveyAnswers(map);
            if (App.getInstance().getPreferenceManager().getSurveyAnswers() != null && !App.getInstance().getPreferenceManager().getSurveyAnswers().isEmpty()) {
                back();
            } else {
                showErrorMessage(mStepperLayout, getResources().getString(R.string.survey_cancel));
            }
        } catch (Exception ex) {
        }
        ;
    }

    @Override
    public void onCompleted(View completeButton) {
        updateAnswers();
    }

    @Override
    public void onError(VerificationError verificationError) {
        showErrorMessage(mStepperLayout, verificationError.getErrorMessage());
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        fetchAnswers();
    }

    private void back() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        this.finish();
    }

    private void next() {
        Intent intent = new Intent(this, RiskProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        this.finish();
    }

    private void updateAnswers() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Gson gson = new Gson();
        String answers = gson.toJson(App.getInstance().getPreferenceManager().getSurveyAnswers());
        params.put("answers", answers);
        connectServerApi("update_survey_responses", params);
    }


    private void fetchAnswers() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        connectServerApi("get_survey_response", params);
    }

    private void fetchQuestions(String param) {
        if (param == null) {
            connectServerApi("get_survey_question", null);
        } else {
            onSuccessfullyQuestionFetched(param);
        }
    }

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);

        if (requestHeader.equalsIgnoreCase("get_survey_response")) {
            onSuccessfullyAnswerFetched(result);
        } else if (requestHeader.equalsIgnoreCase("get_survey_question")) {
            onSuccessfullyQuestionFetched(result);
        } else if (requestHeader.equalsIgnoreCase("update_survey_responses")) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().setPrettyPrinting().create();
            AppUser appUser = gson.fromJson(result, AppUser.class);
            App.getInstance().getPreferenceManager().setAppUser(appUser);
            next();
        }
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);

        showErrorMessage(mStepperLayout, error);

        if (requestHeader.equalsIgnoreCase("get_survey_question")) {
            try {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, Class.forName(callingActivity));
                startActivity(intent);
                this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                this.finish();
            } catch (Exception ex) {
            }
        }
    }

}
