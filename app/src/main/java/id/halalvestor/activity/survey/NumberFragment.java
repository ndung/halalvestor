package id.halalvestor.activity.survey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;
import id.halalvestor.ui.NumberTextWatcher;
import id.halalvestor.util.Helper;

public class NumberFragment extends BaseSurveyFragment {

    private TextView textView;
    private EditText editText;
    private SurveyQuestion question;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.survey_number_layout, container, false);

        textView = (TextView) rootView.findViewById(R.id.title);
        editText = (EditText) rootView.findViewById(R.id.answer);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        int no = getArguments().getInt("no");
        int size = getArguments().getInt("size");
        SpannableString ss1=  new SpannableString(String.format("%02d", no)+" dari "+size);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 2, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 2, 0);// set color

        TextView tvHeader = rootView.findViewById(R.id.no);
        tvHeader.setText(ss1);

        question = (SurveyQuestion) getArguments().getSerializable("data");

        textView.setText(Html.fromHtml(question.getQuestion()));
        editText.addTextChangedListener(new NumberTextWatcher(editText));

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

        if (question.isRequired() && editText.length()<=0){
            return new VerificationError(getResources().getString(R.string.survey_empty_et_answer));
        }

        if (editText.length()>0){
            List<QuestionParameter> list = new ArrayList<QuestionParameter>();
            QuestionParameter parameter = new QuestionParameter();
            parameter.setId(0);
            parameter.setDescription(editText.getText().toString());
            list.add(parameter);
            App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), list);
        }
        return null;
    }

    @Override
    public void onSelected() {
        editText.requestFocus();
        List<QuestionParameter> list = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
        if (list!=null){
            editText.setText(list.get(0).getDescription());
        }

        //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(editText, 0);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
