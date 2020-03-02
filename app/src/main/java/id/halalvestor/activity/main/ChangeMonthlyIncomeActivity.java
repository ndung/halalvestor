package id.halalvestor.activity.main;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import id.halalvestor.model.AppUser;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.util.Helper;
import id.halalvestor.ui.NumberTextWatcher;

public class ChangeMonthlyIncomeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.input_desc)
    TextView tvInputDesc;
    @BindView(R.id.input_amount)
    EditText inputAmount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_monthly_income_layout);
        ButterKnife.bind(this);
        tvInputDesc.setText(Html.fromHtml("Berapakah rata-rata <b><big>pendapatan per bulan</big></b> kamu dikurangi dengan pembayaran cicilan rutin?"));//getResources().getString(R.string.change_monthly_income_text)));
        tvTitle.setText(getResources().getString(R.string.change_monthly_income_title));
        inputAmount.addTextChangedListener(new NumberTextWatcher(inputAmount));
    }

    @OnClick({R.id.iv_finish, R.id.btn_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.btn_change:
                Helper.hideKeyboard(this);
                if (inputAmount.getText().toString().isEmpty()){
                    showErrorMessage(inputAmount, getResources().getString(R.string.change_monthly_income_empty));
                }
                else{
                    changeMonthlyIncome(inputAmount.getText().toString());
                }
                break;
        }
    }

    private void changeMonthlyIncome(String monthlyIncome){
        Map<String,String> params = new HashMap<>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("monthly_income", monthlyIncome);
        connectServerApi("update_monthly_income", params);
    }

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);

        QuestionParameter parameter = new QuestionParameter();
        parameter.setId(0);
        parameter.setDescription(inputAmount.getText().toString());
        List<QuestionParameter> list = new ArrayList<QuestionParameter>();
        list.add(parameter);
        App.getInstance().getPreferenceManager().putSurveyAnswers(4,list);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        AppUser appUser = gson.fromJson(result, AppUser.class);
        if (appUser!=null){
            App.getInstance().getPreferenceManager().setAppUser(appUser);
        }
        finish();
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);
        showErrorMessage(inputAmount, error);
    }
}
