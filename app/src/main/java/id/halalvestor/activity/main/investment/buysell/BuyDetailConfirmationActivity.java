package id.halalvestor.activity.main.investment.buysell;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Global;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.main.history.InvestmentOngoingBuyActivity;
import id.halalvestor.model.AppUser;
import id.halalvestor.util.IRDialogUtils;

public class BuyDetailConfirmationActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    IRDialogUtils irDialogUtils;
    @BindView(R.id.bt_confirm)
    Button btConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_detail_confirmation_layout);
        ButterKnife.bind(this);

        irDialogUtils = new IRDialogUtils(BuyDetailConfirmationActivity.this);

        tvTitle.setText("BELI PRODUK");
    }

    private void insertTransaction(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("type", String.valueOf(Global.TRANSACTION_REQUEST.getType()));
        params.put("product_id", String.valueOf(Global.TRANSACTION_REQUEST.getInvestmentProduct().getId()));
        params.put("amount", String.valueOf(Global.TRANSACTION_REQUEST.getAmount()));
        connectServerApi("insert_transaction", params);
    }

    @OnClick({R.id.iv_finish, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.bt_confirm:
                insertTransaction();
                break;
            case R.id.iv_finish:
                finish();
                break;
        }
    }

    private void viewHistory(){
        Intent intent = new Intent(getApplicationContext(), InvestmentOngoingBuyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().setPrettyPrinting().create();
        AppUser appUser = gson.fromJson(result, AppUser.class);
        App.getInstance().getPreferenceManager().setAppUser(appUser);
        final Dialog dialog = irDialogUtils.createDialogNoTitle(R.layout.dialog_confirmation_order, true);
        TextView tv = (TextView)dialog.findViewById(R.id.tv_done);
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                viewHistory();
            }
        });
        dialog.show();
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);

        showErrorMessage(btConfirm, error);
    }
}
