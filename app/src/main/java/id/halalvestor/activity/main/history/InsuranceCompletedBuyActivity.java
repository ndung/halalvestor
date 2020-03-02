package id.halalvestor.activity.main.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;

public class InsuranceCompletedBuyActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_history_complete);
        ButterKnife.bind(this);
        tvTitle.setText("RINCIAN TRANSAKSI");

    }

    @OnClick({R.id.iv_finish, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.btn_download:
                break;
        }
    }
}
