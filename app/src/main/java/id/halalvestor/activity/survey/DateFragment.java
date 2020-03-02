package id.halalvestor.activity.survey;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;
import id.halalvestor.ui.FixedHoloDatePickerDialog;
import id.halalvestor.util.Helper;

public class DateFragment extends BaseSurveyFragment {

    private TextView textView;
    private EditText editText;
    private SurveyQuestion question;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.survey_date_layout, container, false);

        textView = rootView.findViewById(R.id.title);
        editText = rootView.findViewById(R.id.answer);

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(DateFragment.class.toString(), "Build:"+Build.VERSION.SDK_INT);
                // TODO Auto-generated method stub
                if (Build.VERSION.SDK_INT >= 24) {
                    new FixedHoloDatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }else{
                    new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                }
            }
        });


        question = (SurveyQuestion) getArguments().getSerializable("data");

        int no = getArguments().getInt("no");
        int size = getArguments().getInt("size");
        SpannableString ss1=  new SpannableString(String.format("%02d", no)+" dari "+size);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 2, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 2, 0);// set color

        TextView tvHeader = rootView.findViewById(R.id.no);
        tvHeader.setText(ss1);

        textView.setText(Html.fromHtml(question.getQuestion()));

        return rootView;
    }


    DatePickerDialog.OnDateSetListener date = new FixedHoloDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    SimpleDateFormat bodFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private void updateLabel() {
        editText.setText(bodFormatter.format(myCalendar.getTime()));
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
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
