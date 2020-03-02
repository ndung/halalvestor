package id.halalvestor.activity.main.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.MainActivity;

public class InsuranceOngoingBuyActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.iv_investment_manager)
    ImageView ivInvestmentManager;
    @BindView(R.id.tv_insurance_manager)
    TextView tvInsuranceManager;
    @BindView(R.id.tv_product_type)
    TextView tvProductType;
    @BindView(R.id.tv_compensation_hospital)
    TextView tvCompensationHospital;
    @BindView(R.id.tv_compensation_outpatient)
    TextView tvCompensationOutpatient;
    @BindView(R.id.tv_compensation_of_death)
    TextView tvCompensationOfDeath;
    @BindView(R.id.tv_premi_price)
    TextView tvPremiPrice;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;
    @BindView(R.id.tv_buying_amount)
    TextView tvBuyingAmount;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_payment_info)
    TextView tvPaymentInfo;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;
    @BindView(R.id.tv_beneficiary_name)
    TextView tvBeneficiaryName;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_attach)
    TextView tvAttach;
    @BindView(R.id.bt_done)
    Button btDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_history_on_going);
        ButterKnife.bind(this);
        tvTitle.setText("RINCIAN TRANSAKSI");

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    private void done(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("fragment", "transaction_history");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    @OnClick({R.id.iv_finish, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                done();
                break;
            case R.id.btn_download:
                break;
        }
    }
}
