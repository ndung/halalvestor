package id.halalvestor.activity.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;
import id.halalvestor.ui.FixedHoloDatePickerDialog;

public class SurveySection1Adapter extends RecyclerView.Adapter<SurveySection1Adapter.ViewHolder> {

    List<SurveyQuestion> list = new ArrayList<>();

    private Activity activity;

    public SurveySection1Adapter(Activity activity, List<SurveyQuestion> list) {
        this.activity = activity;
        this.list = list;
    }

    private static final String TAG = SurveySection1Adapter.class.toString();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_section1_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SurveyQuestion question = list.get(position);

        holder.textView.setText(Html.fromHtml((position + 1) + ". " + question.getQuestion()));

        if (question.getType() == 2) {
            holder.editText.setVisibility(View.GONE);
            holder.checkBoxes.setVisibility(View.GONE);
            holder.radioGroup.setVisibility(View.VISIBLE);
            holder.radioGroup.removeAllViews();

            for (QuestionParameter choice : question.getParameters()) {
                RadioButton rb = new RadioButton(activity);
                rb.setTag(choice.getSequence());
                rb.setText(Html.fromHtml(choice.getDescription()));
                rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.radioGroup.addView(rb);
            }

            List<QuestionParameter> ans = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
            if (ans != null) {
                for (int i = 0; i < holder.radioGroup.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) holder.radioGroup.getChildAt(i);
                    if (ans.get(0).getSequence() == (Integer) rb.getTag()) {
                        rb.setChecked(true);
                    }
                }
            }

            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    RadioButton checkedRadioButton = group.findViewById(checkedId);
                    if (checkedRadioButton != null) {
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {
                            List<QuestionParameter> newans = new ArrayList<>();
                            newans.add(question.getParameters().get((Integer) checkedRadioButton.getTag() - 1));
                            App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), newans);
                        }
                    }
                }
            });
        } else if (question.getType() == 3) {
            holder.radioGroup.setVisibility(View.GONE);
            holder.checkBoxes.setVisibility(View.GONE);
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            //holder.editText.addTextChangedListener(new NumberTextWatcher(holder.editText));


            List<QuestionParameter> ans = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
            if (ans != null) {
                holder.editText.setText(decimalFormat.format(Double.parseDouble(ans.get(0).getDescription().replaceAll("[,.]", ""))));
            }

            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    List<QuestionParameter> ans = new ArrayList<>();
                    QuestionParameter parameter = new QuestionParameter();
                    parameter.setId(0);
                    parameter.setDescription(holder.editText.getText().toString().replaceAll("[,.]", ""));
                    ans.add(parameter);
                    App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), ans);
                }
            });
        } else if (question.getType() == 4) {
            holder.radioGroup.setVisibility(View.GONE);
            holder.checkBoxes.removeAllViews();
            holder.checkBoxes.setVisibility(View.VISIBLE);
            holder.editText.setVisibility(View.GONE);

            List<QuestionParameter> ans = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
            for (QuestionParameter choice : question.getParameters()) {
                CheckBox cb = new CheckBox(activity);
                cb.setTag(choice.getSequence());
                cb.setText(Html.fromHtml(choice.getDescription()));
                cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                if (ans != null && ans.contains(choice)) {
                    cb.setChecked(true);
                }
                holder.checkBoxes.addView(cb);

                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        CheckBox checkedCheckBox = (CheckBox) compoundButton;
                        List<QuestionParameter> newans = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
                        if (newans == null) {
                            newans = new ArrayList<>();
                        }
                        QuestionParameter checkAns = question.getParameters().get((Integer) checkedCheckBox.getTag() - 1);
                        if (isChecked && !newans.contains(checkAns)) {
                            newans.add(checkAns);
                        } else if (!isChecked && newans.contains(checkAns)) {
                            newans.remove(checkAns);
                        }
                        App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), newans);
                    }
                });
            }

        } else if (question.getType() == 5) {
            holder.radioGroup.setVisibility(View.GONE);
            holder.checkBoxes.setVisibility(View.GONE);
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setFocusable(false);
            holder.editText.setInputType(InputType.TYPE_CLASS_DATETIME);
            holder.editText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (Build.VERSION.SDK_INT >= 24) {
                        new FixedHoloDatePickerDialog(activity, new OnDateSetListener(holder.editText),
                                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }else{
                        new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new OnDateSetListener(holder.editText),
                                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
            });

            List<QuestionParameter> ans = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
            if (ans != null) {
                holder.editText.setText(ans.get(0).getDescription());
            }

            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    List<QuestionParameter> ans = new ArrayList<>();
                    QuestionParameter parameter = new QuestionParameter();
                    parameter.setId(0);
                    parameter.setDescription(holder.getEditText().getText().toString());
                    ans.add(parameter);
                    App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), ans);
                }
            });
        } else if (question.getType() == 6) {
            holder.radioGroup.removeAllViews();
            holder.radioGroup.setVisibility(View.VISIBLE);
            holder.checkBoxes.setVisibility(View.GONE);
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setInputType(InputType.TYPE_CLASS_TEXT);

            for (int i = 0; i < question.getParameters().size(); i++) {
                QuestionParameter choice = question.getParameters().get(i);

                RadioButton rb = new RadioButton(activity);
                rb.setTag(choice.getSequence());
                rb.setText(Html.fromHtml(choice.getDescription()));
                rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.radioGroup.addView(rb);
            }

            List<QuestionParameter> ans = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
            if (ans != null) {
                if (ans.get(0).getSequence() >= holder.radioGroup.getChildCount()) {
                    RadioButton rb = (RadioButton) holder.radioGroup.getChildAt(holder.radioGroup.getChildCount() - 1);
                    rb.setChecked(true);
                    holder.editText.setVisibility(View.VISIBLE);
                    holder.editText.setText(ans.get(0).getDescription());
                } else {
                    for (int i = 0; i < holder.radioGroup.getChildCount(); i++) {
                        RadioButton rb = (RadioButton) holder.radioGroup.getChildAt(i);
                        if (ans.get(0).getSequence() == (Integer) rb.getTag()) {
                            rb.setChecked(true);
                        }
                    }
                }
            }

            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    RadioButton checkedRadioButton = group.findViewById(checkedId);
                    if (checkedRadioButton != null) {
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked && (Integer) checkedRadioButton.getTag() != holder.radioGroup.getChildCount()) {
                            List<QuestionParameter> newans = new ArrayList<>();
                            newans.add(question.getParameters().get((Integer) checkedRadioButton.getTag() - 1));
                            App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), newans);
                        }
                    }
                }
            });

            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    RadioButton rb = ((RadioButton) holder.radioGroup.getChildAt(holder.radioGroup.getChildCount() - 1));
                    if (!rb.isChecked()) {
                        holder.radioGroup.clearCheck();
                        rb.setChecked(true);
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    List<QuestionParameter> ans = new ArrayList<>();
                    QuestionParameter questionParameter = new QuestionParameter();
                    int tempID = holder.radioGroup.getChildCount() + 1;
                    questionParameter.setId(tempID);
                    questionParameter.setSequence(tempID);
                    questionParameter.setDescription(holder.getEditText().getText().toString());
                    ans.add(questionParameter);
                    App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), ans);
                }
            });

            holder.setIsRecyclable(false);
        } else if (question.getType() == 7) {
            holder.radioGroup.setVisibility(View.GONE);
            holder.checkBoxes.setVisibility(View.GONE);
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            List<QuestionParameter> ans = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
            if (ans != null) {
                holder.editText.setText(ans.get(0).getDescription());
            }

            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    List<QuestionParameter> ans = new ArrayList<>();
                    QuestionParameter parameter = new QuestionParameter();
                    parameter.setId(0);
                    parameter.setDescription(holder.editText.getText().toString());
                    ans.add(parameter);
                    App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), ans);
                }
            });
        }
    }

    class OnDateSetListener implements DatePickerDialog.OnDateSetListener{

        EditText editText;

        OnDateSetListener(EditText editText){
            this.editText = editText;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            editText.setText(sdf.format(myCalendar.getTime()));
        }
    }

    private static boolean containsId(List<QuestionParameter> list, int id) {
        for (QuestionParameter object : list) {
            if (object.getSequence() == id) {
                return true;
            }
        }
        return false;
    }

    Calendar myCalendar = Calendar.getInstance();

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.description)
        protected TextView textView;
        @BindView(R.id.editText)
        protected EditText editText;
        @BindView(R.id.radioGroup)
        protected RadioGroup radioGroup;
        @BindView(R.id.linearLayout_checkboxes)
        protected LinearLayout checkBoxes;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public EditText getEditText() {
            return editText;
        }

        public RadioGroup getRadioGroup() {
            return radioGroup;
        }

        public LinearLayout getCheckBoxes() {
            return checkBoxes;
        }
    }
}
