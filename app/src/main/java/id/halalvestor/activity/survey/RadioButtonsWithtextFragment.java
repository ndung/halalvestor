package id.halalvestor.activity.survey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;
import id.halalvestor.util.Helper;

public class RadioButtonsWithtextFragment extends BaseSurveyFragment {

    private static final String TAG = RadioButtonsWithtextFragment.class.toString();

    private SurveyQuestion question;
    private TextView textView;
    private RadioGroup radioGroup;

    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.survey_radiobuttons_withtext_layout, container, false);

        textView = rootView.findViewById(R.id.title);
        radioGroup = rootView.findViewById(R.id.radioGroup);

        question = (SurveyQuestion) getArguments().getSerializable("data");

        textView.setText(Html.fromHtml(question.getQuestion()));

        int no = getArguments().getInt("no");
        int size = getArguments().getInt("size");
        SpannableString ss1=  new SpannableString(String.format("%02d", no)+" dari "+size);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 2, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 2, 0);// set color

        TextView tvHeader = rootView.findViewById(R.id.no);
        tvHeader.setText(ss1);

        editText = rootView.findViewById(R.id.text_field);
        editText.setVisibility(View.GONE);

        for (QuestionParameter choice : question.getParameters()) {
            RadioButton rb = new RadioButton(getActivity());
            rb.setTag(choice.getSequence());
            rb.setText(Html.fromHtml(choice.getDescription()));
            rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            radioGroup.addView(rb);

            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    verifyStep();
                }
            });
        }
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
        boolean bool = false;
        List<QuestionParameter> list = new ArrayList<QuestionParameter>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton)radioGroup.getChildAt(i);
            if (rb.isChecked()) {
                bool = true;
                if ((Integer) rb.getTag()==radioGroup.getChildCount()){
                    editText.setVisibility(View.VISIBLE);
                    if (editText.getText().toString().equals("")){
                        bool = false;
                    }else {
                        QuestionParameter questionParameter = new QuestionParameter();
                        int tempID = (Integer) rb.getTag() + 1;
                        questionParameter.setId(tempID);
                        questionParameter.setSequence(tempID);
                        questionParameter.setDescription(editText.getText().toString());
                        list.add(questionParameter);
                    }
                }else {
                    list.add(question.getParameters().get((Integer) rb.getTag() - 1));
                }
            }
        }

        if (question.isRequired() && !bool){
            return new VerificationError(getResources().getString(R.string.survey_empty_rb_answer));
        }

        if (bool) {
            App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), list);
        }

        return null;
    }

    @Override
    public void onSelected() {
        Helper.hideKeyboard(getActivity());
        List<QuestionParameter> list = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
        if (list!=null) {
            if (list.get(0).getSequence()>=radioGroup.getChildCount()) {
                RadioButton rb = (RadioButton)radioGroup.getChildAt(radioGroup.getChildCount()-1);
                rb.setChecked(true);
                editText.setVisibility(View.VISIBLE);
                editText.setText(list.get(0).getDescription());
            }else {
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                    if (list.get(0).getSequence() == (Integer) rb.getTag()) {
                        rb.setChecked(true);
                    }
                }
            }
        }
    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
