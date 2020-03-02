package id.halalvestor.activity.main.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.InsuranceProduct;
import id.halalvestor.util.Helper;

public class InsuranceSummaryDetailActivity extends AppCompatActivity {

    private static final String TAG = InsuranceSummaryDetailActivity.class.toString();

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_frequency_type)
    TextView tvFrequencyType;
    @BindView(R.id.tv_provider)
    TextView tvProvider;
    @BindView(R.id.tv_payment_type)
    TextView tvPaymentType;
    @BindView(R.id.tv_amount_premi)
    TextView tvAmountPremi;
    @BindView(R.id.tv_amount_adm)
    TextView tvAmountAdm;
    @BindView(R.id.tv_amount_total)
    TextView tvAmountTotal;
    @BindView(R.id.bt_next)
    Button next;

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_sumary_detail);
        ButterKnife.bind(this);

        final InsuranceProduct model = (InsuranceProduct) getIntent().getSerializableExtra("product");
        Log.d(TAG, "model:"+model);

        Map<String,String> map = model.getDescription();

        final AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
        tvAge.setText(Helper.getAge(appUser.getBirthDate())+" Tahun");
        tvGender.setText(appUser.getGender());
        tvProvider.setText(model.getProvider());
        String frequencyType = "", paymentType = "", santunanKematian = "", frequency = "", manfaat = "", ketentuan = "";
        if (map.containsKey("frequencyType")){
            frequencyType = map.get("frequencyType");
        }
        if (map.containsKey("paymentType")){
            paymentType = map.get("paymentType");
        }
        tvAmountPremi.setText(decimalFormat.format(model.getPremium()));
        tvAmountAdm.setText(decimalFormat.format(model.getAdminFee()));
        tvAmountTotal.setText(decimalFormat.format(model.getPremium()+model.getAdminFee()));
        tvFrequencyType.setText(frequencyType);
        tvPaymentType.setText(paymentType);
        tvTitle.setText("RINGKASAN PRODUK");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsuranceMedicalHistoryActivity.class);
                Log.d(TAG, "model:"+model);
                intent.putExtra("product", model);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.iv_finish, R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
            case R.id.bt_back:
                finish();
                break;
        }
    }
}
