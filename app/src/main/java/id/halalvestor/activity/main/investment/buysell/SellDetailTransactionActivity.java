package id.halalvestor.activity.main.investment.buysell;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.Global;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.model.TransactionRequest;
import id.halalvestor.ui.NumberTextWatcher;

public class SellDetailTransactionActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_ketentuan)
    ImageView ivKetentuan;
    @BindView(R.id.iv_toogle)
    TextView ivToogle;
    @BindView(R.id.ll_line_ketentuan)
    LinearLayout llLineKetentuan;
    @BindView(R.id.rl_container_ketentuan)
    RelativeLayout rlContainerKetentuan;
    boolean toogleShow = false;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_ongoing_unit)
    TextView tvOngoingUnit;
    @BindView(R.id.tv_reksadana_name)
    TextView tvReksadanaName;
    @BindView(R.id.tv_investment_manager)
    TextView tvInvestmentManager;
    @BindView(R.id.tv_reksadana_type)
    TextView tvReksadanaType;
    @BindView(R.id.tv_reksadana_value)
    TextView tvReksadanaValue;
    @BindView(R.id.tv_nab)
    TextView tvNab;
    @BindView(R.id.tv_nab_per_unit)
    TextView tvNabPerUnit;
    @BindView(R.id.tv_min_purchase)
    TextView tvMinPurchase;
    @BindView(R.id.tv_buying_price)
    TextView tvBuyingPrice;
    @BindView(R.id.tv_available_unit)
    TextView tvAvailableUnit;
    @BindView(R.id.tv_net_amount)
    EditText tvNetAmount;

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private DecimalFormat currencyFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_detail_transaction_layout);
        ButterKnife.bind(this);

        final TransactionRequest request = Global.TRANSACTION_REQUEST;

        tvReksadanaName.setText(request.getInvestmentProduct().getProductName());
        tvInvestmentManager.setText(request.getInvestmentProduct().getInvestmentManager());
        tvReksadanaType.setText(request.getInvestmentProduct().getProductType());
        tvAvailableUnit.setText(decimalFormat.format(request.getInvestmentProduct().getUnit()));
        tvReksadanaValue.setText(currencyFormat.format(request.getInvestmentProduct().getAmount()));
        tvBuyingPrice.setText(currencyFormat.format(request.getInvestmentProduct().getAdminFee()));
        tvNab.setText(decimalFormat.format(request.getInvestmentProduct().getNab()));
        tvNabPerUnit.setText(decimalFormat.format(request.getInvestmentProduct().getNab()));
        tvMinPurchase.setText(currencyFormat.format(request.getInvestmentProduct().getMinPurchase()));
        Double unit = request.getAmount()/request.getInvestmentProduct().getNab().intValue();
        tvNetAmount.setText(decimalFormat.format(unit));
        tvNetAmount.addTextChangedListener(new NumberTextWatcher(tvNetAmount));
        tvUnit.setText(decimalFormat.format(unit));
        tvTitle.setText("JUAL PRODUK");
    }

    @OnClick({R.id.iv_finish,R.id.iv_toogle,
            R.id.bt_next, R.id.bt_before})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.iv_toogle:
                if (!toogleShow) {
                    rlContainerKetentuan.setBackgroundResource(R.drawable.round_background_light);
                    rlContainerKetentuan.setPadding(8, 8, 8, 8);
                    ivToogle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    ivKetentuan.setImageResource(R.drawable.ic_down_toogle);
                    llLineKetentuan.setVisibility(View.VISIBLE);
                    llContent.setVisibility(View.VISIBLE);

                    toogleShow = true;

                } else {
                    rlContainerKetentuan.setBackgroundResource(R.drawable.round_background_primary);
                    rlContainerKetentuan.setPadding(8, 8, 8, 8);
                    ivToogle.setTextColor(Color.parseColor("#FFFFFF"));
                    ivKetentuan.setImageResource(R.drawable.ic_add_white);
                    llLineKetentuan.setVisibility(View.GONE);
                    llContent.setVisibility(View.GONE);

                    toogleShow = false;
                }
                break;
            case R.id.bt_next:
                try {
                    TransactionRequest request = Global.TRANSACTION_REQUEST;
                    double netAmount = decimalFormat.parse(tvNetAmount.getText().toString()).doubleValue();
                    if (netAmount>=request.getInvestmentProduct().getUnit()) {
                        showErrorMessage(tvNetAmount, "Unit yang dijual tidak boleh lebih besar atau sama dengan unit yang dimiliki");
                    }else{
                        request.setAmount(netAmount*request.getInvestmentProduct().getNab());
                        startActivity(new Intent(getApplicationContext(), SellDetailPaymentActivity.class));
                    }
                }catch(Exception ex){
                    // do nothing?
                    Log.d(TAG, "exception", ex);
                }
                break;
            case R.id.bt_before:
                finish();
                break;
        }
    }

    private static final String TAG = SellDetailTransactionActivity.class.toString();
}
