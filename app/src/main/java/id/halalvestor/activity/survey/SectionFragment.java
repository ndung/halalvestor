package id.halalvestor.activity.survey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import id.halalvestor.R;
import id.halalvestor.model.SurveyQuestion;
import id.halalvestor.util.Helper;


public class SectionFragment extends BaseSurveyFragment{


    private SurveyQuestion question;
    private TextView tvTitle;
    private TextView tvDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.survey_section_layout, container, false);

        tvTitle = rootView.findViewById(R.id.title);
        tvDescription = rootView.findViewById(R.id.description);

        question = (SurveyQuestion) getArguments().getSerializable("data");
        String[] str = question.getQuestion().split(";");
        tvTitle.setText(Html.fromHtml(str[0]));
        tvDescription.setText(Html.fromHtml(str[1]));

        int no = getArguments().getInt("no");
        int size = getArguments().getInt("size");
        SpannableString ss1=  new SpannableString(String.format("%02d", no)+" dari "+size);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 2, 0); // set size

        TextView tvHeader = rootView.findViewById(R.id.no);
        tvHeader.setText(ss1);

        return rootView;
    }

    @Override
    public VerificationError verifyStep() {
        Helper.hideKeyboard(getActivity());
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

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
}
