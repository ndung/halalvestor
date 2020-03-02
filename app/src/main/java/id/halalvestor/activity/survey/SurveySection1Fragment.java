package id.halalvestor.activity.survey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.SurveySection1Adapter;
import id.halalvestor.model.SurveyQuestion;
import id.halalvestor.util.Helper;

public class SurveySection1Fragment extends BaseSurveyFragment {

    private static final String TAG = SurveySection1Fragment.class.toString();

    private List<SurveyQuestion> list;
    private RecyclerView recyclerView;

    SurveySection1Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.survey_section1_layout, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        list = (List<SurveyQuestion>) getArguments().getSerializable("data");

        adapter = new SurveySection1Adapter(this.getActivity(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int no = getArguments().getInt("no");
        int size = getArguments().getInt("size");
        SpannableString ss1 = new SpannableString(String.format("%02d", no) + " dari " + size);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 2, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 2, 0);// set color

        TextView tvHeader = rootView.findViewById(R.id.no);
        tvHeader.setText(ss1);
        return rootView;
    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        Helper.hideKeyboard(getActivity());
        for (int i=0;i<list.size();i++){
            try {
                SurveyQuestion question = list.get(i);
                boolean bool = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId())!=null;
                if (question.getType() == 2) {
                    if (question.isRequired() && !bool) {
                        return new VerificationError(getResources().getString(R.string.survey_empty_rb_answer) + " pada pertanyaan no " + (i + 1));
                    }
                } else if (question.getType() == 3 || question.getType() == 7) {
                    if (question.isRequired() && !bool){
                        return new VerificationError(getResources().getString(R.string.survey_empty_et_answer) + " pada pertanyaan no " + (i + 1));
                    }
                } else if (question.getType() == 4) {
                    if (question.isRequired() && !bool) {
                        return new VerificationError(getResources().getString(R.string.survey_empty_cb_answer) + " pada pertanyaan no " + (i + 1));
                    }
                } else if (question.getType() == 5) {
                    if (question.isRequired() && !bool){
                        return new VerificationError(getResources().getString(R.string.survey_empty_et_answer) + " pada pertanyaan no " + (i + 1));
                    }
                } else if (question.getType() == 6) {
                    if (question.isRequired() && !bool) {
                        return new VerificationError(getResources().getString(R.string.survey_empty_cb_answer) + " pada pertanyaan no " + (i + 1));
                    }
                }
            }catch(Exception ex){
                Log.e(TAG, "error", ex);
            }
        }
        return null;
    }

    @Override
    public void onSelected() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
