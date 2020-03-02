package id.halalvestor.activity.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.adapter.InvestmentGoalSurveyAdapter;
import id.halalvestor.activity.adapter.InvestmentGoalSurveyChoiceAdapter;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;
import id.halalvestor.util.Helper;
import id.halalvestor.util.IRDialogUtils;


public class ChangeInvestmentGoalsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_add_global)
    ImageView ivAddGlobal;
    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_question)
    TextView tvQuestion;

    LinearLayout layout;

    IRDialogUtils irDialogUtils;
    List<QuestionParameter> list;

    InvestmentGoalSurveyAdapter adapter;
    Dialog dialog;

    int id = 0;
    SurveyQuestion question;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_investment_goals_layout);
        ButterKnife.bind(this);

        tvTitle.setText(getResources().getString(R.string.investment_goal_title));

        irDialogUtils = new IRDialogUtils(this);

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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ivAddGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size() < 3) {
                    dialog.show();
                } else {
                    showErrorMessage(ivAddGlobal, getResources().getString(R.string.more_than_three_investment_goal));
                }
            }
        });

        connectServerApi("get_investment_goal_question", null);
    }

    @OnClick({R.id.iv_finish, R.id.btn_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.btn_change:
                changeInvestmentGoals();
                break;
        }
    }

    private void onClickChoice(int tag) {
        if (tag != question.getParameters().size()) {
            QuestionParameter parameter = question.getParameters().get(tag-1);
            if (!list.contains(parameter)) {
                list.add(parameter);
                adapter.notifyDataSetChanged();
                showingListState();
            }else{
                showErrorMessage(ivAddGlobal, getResources().getString(R.string.investment_goal_exist,parameter.getDescription()));
            }
            dialog.dismiss();
        } else {
            final Dialog dialogOther = irDialogUtils.showDialog(getResources().getString(R.string.investment_goal_title), R.layout.dialog_add_other, true);
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

    private void changeInvestmentGoals(){
        setAdditionalResponse();
        if (question.isRequired() && (list==null || list.isEmpty())){
            showErrorMessage(recyclerView, getResources().getString(R.string.survey_empty_cb_answer));
            return;
        }else{
            for (QuestionParameter param : list){
                if (!param.getAdditionalResponses().containsKey("time")||param.getAdditionalResponses().get("time").isEmpty()||param.getAdditionalResponses().get("time").equals("0")){
                    showErrorMessage(recyclerView, getResources().getString(R.string.zero_investment_time, param.getDescription()));
                    return;
                }
                if (!param.getAdditionalResponses().containsKey("fund")||param.getAdditionalResponses().get("fund").isEmpty()||param.getAdditionalResponses().get("fund").equals("0")){
                    showErrorMessage(recyclerView, getResources().getString(R.string.zero_investment_fund, param.getDescription()));
                    return;
                }
            }
        }

        Map<String,String> params = new HashMap<>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
        params.put("answers", gson.toJson(list));

        connectServerApi("update_investment_goals", params);
    }

    private static final String TAG = ChangeInvestmentGoalsActivity.class.toString();

    private void onSuccessfullyAnswerFetched(String response){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        question = gson.fromJson(response, SurveyQuestion.class);
        tvQuestion.setText(Html.fromHtml(question.getQuestion()));

        dialog = irDialogUtils.createDialog("Tujuan Investasi", R.layout.dialog_add_investment_goal, true);
        LinearLayout layout = dialog.findViewById(R.id.ll_dialog);
        TextView tvOther = dialog.findViewById(R.id.other);

        RecyclerView rvDialogChoice = new RecyclerView(this);
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

        rvDialogChoice.setLayoutManager(new GridLayoutManager(this, 3));
        rvDialogChoice.setAdapter(investmentGoalSurveyChoiceAdapter);

        for (QuestionParameter choice : question.getParameters()) {
            /**TextView tvChoice = new TextView(this);
            tvChoice.setTag(choice.getSequence());
            tvChoice.setText(choice.getDescription());
            tvChoice.setPadding(10, 10, 10, 10);
            tvChoice.setGravity(Gravity.CENTER);
            tvChoice.setTextSize(18);

            layout.addView(tvChoice, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvChoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickChoice((Integer) (view.getTag()));
                }
            });*/
            id = choice.getId();
        }
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

    private void setAdditionalResponse(){
        Helper.hideKeyboard(this);
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
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);

        if (requestHeader.equalsIgnoreCase("get_investment_goal_question")){
            onSuccessfullyAnswerFetched(result);
        }else if (requestHeader.equalsIgnoreCase("update_investment_goals")) {
            App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(),list);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            AppUser appUser = gson.fromJson(result, AppUser.class);
            if (appUser != null) {
                App.getInstance().getPreferenceManager().setAppUser(appUser);
            }
            finish();
        }
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);
        if (requestHeader.equalsIgnoreCase("get_investment_goal_question")) {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            finish();
        }else{
            showErrorMessage(recyclerView, error);
        }
    }
}
