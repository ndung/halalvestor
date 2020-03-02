package id.halalvestor.activity.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import id.halalvestor.activity.survey.BaseSurveyFragment;
import id.halalvestor.activity.survey.CheckBoxesFragment;
import id.halalvestor.activity.survey.DateFragment;
import id.halalvestor.activity.survey.MultipleTextBoxesFragment;
import id.halalvestor.activity.survey.NumberFragment;
import id.halalvestor.activity.survey.RadioButtonsFragment;
import id.halalvestor.activity.survey.RadioButtonsWithtextFragment;
import id.halalvestor.activity.survey.SectionFragment;
import id.halalvestor.activity.survey.SurveySection1Fragment;
import id.halalvestor.activity.survey.SurveySection2Fragment;
import id.halalvestor.activity.survey.TextFragment;
import id.halalvestor.model.SurveyQuestion;

public class SurveyFragmentAdapter extends AbstractFragmentStepAdapter {

    private static final String TAG = SurveyFragmentAdapter.class.toString();

    Map<Integer, List<SurveyQuestion>> questions;

    public SurveyFragmentAdapter(@NonNull FragmentManager fm, @NonNull Context context, Map<Integer, List<SurveyQuestion>> questions) {
        super(fm, context);
        this.questions = questions;
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        List<SurveyQuestion> surveyQuestions = questions.get(position);
        BaseSurveyFragment fragment = null;
        Log.d(TAG, "position:" + position + ",surveyQuestions.size:" + surveyQuestions.size());
        if (surveyQuestions.size() == 1) {
            switch (surveyQuestions.get(0).getType()) {
                case 0:
                    fragment = new SectionFragment();
                    break;
                case 1:
                    fragment = new MultipleTextBoxesFragment();
                    break;
                case 2:
                    fragment = new RadioButtonsFragment();
                    break;
                case 3:
                    fragment = new NumberFragment();
                    break;
                case 4:
                    fragment = new CheckBoxesFragment();
                    break;
                case 5:
                    fragment = new DateFragment();
                    break;
                case 6:
                    fragment = new RadioButtonsWithtextFragment();
                    break;
                case 7:
                    fragment = new TextFragment();
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("no", position);
            bundle.putSerializable("size", (questions.size() - 1));
            bundle.putSerializable("data", surveyQuestions.get(0));
            fragment.setArguments(bundle);
        } else if (surveyQuestions.size() < 8) {
            Log.d(TAG, "entering: SurveySection1Fragment");
            fragment = new SurveySection1Fragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("no", position);
            bundle.putSerializable("size", (questions.size() - 1));
            bundle.putSerializable("data", (Serializable) surveyQuestions);
            fragment.setArguments(bundle);
        } else {
            Log.d(TAG, "entering: SurveySection2Fragment");
            fragment = new SurveySection2Fragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("no", position);
            bundle.putSerializable("size", (questions.size() - 1));
            bundle.putSerializable("data", (Serializable) surveyQuestions);
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);
        if (position == questions.size() - 1) {
            builder
                    .setBackButtonLabel("KEMBALI")
                    .setEndButtonLabel("SELESAI");
        } else {
            builder
                    .setBackButtonLabel("KEMBALI")
                    .setEndButtonLabel("LANJUT");
        }
        return builder.create();
    }
}
