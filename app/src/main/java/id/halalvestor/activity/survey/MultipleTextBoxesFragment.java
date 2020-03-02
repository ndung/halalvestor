package id.halalvestor.activity.survey;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.InvestmentGoalSurveyAdapter;
import id.halalvestor.activity.adapter.InvestmentGoalSurveyChoiceAdapter;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;
import id.halalvestor.util.Helper;
import id.halalvestor.util.IRDialogUtils;

public class MultipleTextBoxesFragment extends BaseSurveyFragment {

    private static final String TAG = MultipleTextBoxesFragment.class.toString();

    private SurveyQuestion question;

    @BindView(R.id.iv_add_global)
    ImageView ivAddGlobal;
    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView textView;

    IRDialogUtils irDialogUtils;
    List<QuestionParameter> list;

    InvestmentGoalSurveyAdapter adapter;
    Dialog dialog;

    int id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.survey_multipleboxes_layout, container, false);
        irDialogUtils = new IRDialogUtils(getContext());
        unbinder = ButterKnife.bind(this, view);

        question = (SurveyQuestion) getArguments().getSerializable("data");

        int no = getArguments().getInt("no");
        int size = getArguments().getInt("size");
        SpannableString ss1=  new SpannableString(String.format("%02d", no)+" dari "+size);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 2, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 2, 0);// set color

        TextView tvHeader = view.findViewById(R.id.no);
        tvHeader.setText(ss1);

        textView.setText(Html.fromHtml(question.getQuestion()));

        list = new ArrayList<>();

        adapter = new InvestmentGoalSurveyAdapter(question, list, new InvestmentGoalSurveyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(QuestionParameter model, boolean isRemove) {
                if (isRemove) {
                    list.remove(model);
                    adapter.notifyDataSetChanged();
                    showingListState();
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        dialog = irDialogUtils.createDialog("Tujuan Investasi", R.layout.dialog_add_investment_goal, true);
        LinearLayout layout = dialog.findViewById(R.id.ll_dialog);
        TextView tvOther = dialog.findViewById(R.id.other);

        RecyclerView rvDialogChoice = new RecyclerView(getActivity());
        layout.addView(rvDialogChoice, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        InvestmentGoalSurveyChoiceAdapter investmentGoalSurveyChoiceAdapter = new InvestmentGoalSurveyChoiceAdapter(question.getParameters(), new InvestmentGoalSurveyChoiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(QuestionParameter model) {
                onClickChoice(model.getSequence());
            }
        });

        tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChoice(7);
            }
        });

        rvDialogChoice.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvDialogChoice.setAdapter(investmentGoalSurveyChoiceAdapter);


        for (QuestionParameter choice : question.getParameters()) {
//            TextView tvChoice = new TextView(getActivity());
//            tvChoice.setTag(choice.getSequence());
//            tvChoice.setText(choice.getDescription());
//            tvChoice.setPadding(10, 10, 10, 10);
//            tvChoice.setGravity(Gravity.CENTER);
//            tvChoice.setTextSize(18);
//
//            layout.addView(tvChoice, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            tvChoice.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onClickChoice((Integer) (view.getTag()));
//                }
//            });
            id = choice.getId();
        }

        ivAddGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size() < 3) {
                    dialog.show();
                } else {
                    Snackbar.make(ivAddGlobal, "Maksimal tiga", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void onClickChoice(int tag) {
        if (tag != question.getParameters().size()) {
            QuestionParameter parameter = question.getParameters().get(tag-1);
            if (!list.contains(parameter)) {
                list.add(parameter);
                adapter.notifyDataSetChanged();
                showingListState();
            }else{
                Snackbar.make(ivAddGlobal, parameter.getDescription()+" sudah ada", Snackbar.LENGTH_LONG).show();
            }
            dialog.dismiss();
        } else {
            final Dialog dialogOther = irDialogUtils.showDialog("Tujuan Investasi", R.layout.dialog_add_other, true);
            final EditText editText = (EditText) dialogOther.findViewById(R.id.et_other);
            Button button = (Button) dialogOther.findViewById(R.id.bt_save);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialogOther.dismiss();
                    id = id + 1;
                    QuestionParameter parameter = new QuestionParameter();
                    parameter.setId(id);
                    parameter.setDescription(editText.getText().toString());
                    list.add(parameter);
                    adapter.notifyDataSetChanged();
                    showingListState();
                }
            });
        }
        setAdditionalResponse();
    }

    private void showingListState() {
        if (list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //llEmptyView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            //llEmptyView.setVisibility(View.VISIBLE);
        }
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

    private void setAdditionalResponse(){
        Helper.hideKeyboard(getActivity());
        if (list!=null && !list.isEmpty()) {
            for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                if (list.get(i) != null) {
                    final InvestmentGoalSurveyAdapter.ViewHolder holder = (InvestmentGoalSurveyAdapter.ViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));

                    Map<String, String> response = new HashMap<>();
                    response.put("time", String.valueOf(holder.getQvTahun().getProgress()));
                    response.put("fund", holder.getEtJumlah().getText().toString());
                    list.get(i).setAdditionalResponses(response);
                }
            }
        }
    }

    @Override
    public VerificationError verifyStep() {
        Helper.hideKeyboard(getActivity());
        setAdditionalResponse();
        if (question.isRequired() && (list==null || list.isEmpty())){
            return new VerificationError(getResources().getString(R.string.survey_empty_cb_answer));
        }else{
            for (QuestionParameter param : list){
                if (!param.getAdditionalResponses().containsKey("time")||param.getAdditionalResponses().get("time").isEmpty()||
                        Integer.parseInt(param.getAdditionalResponses().get("time"))<=0){
                    return new VerificationError("Waktu investasi untuk "+param.getDescription()+" harus lebih besar dari 0");
                }
                if (!param.getAdditionalResponses().containsKey("fund")||param.getAdditionalResponses().get("fund").isEmpty()||
                        Double.parseDouble(param.getAdditionalResponses().get("fund").replaceAll("\\.","").replaceAll("\\,",""))<=0){
                    return new VerificationError("Dana yang dibutuhkan untuk "+param.getDescription()+" harus lebih besar dari 0");
                }
            }
        }
        App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(),list);
        return null;
    }

    @Override
    public void onSelected() {
        List<QuestionParameter> answers = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
        if (answers!=null) {
            list.clear();

            for (QuestionParameter answer : answers) {
                list.add(answer);
            }
            adapter.notifyDataSetChanged();
            showingListState();
        }
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
