package id.halalvestor.activity.main.investment.buysell;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
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

public class BuyDetailTransactionActivity extends BaseActivity {

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
    @BindView(R.id.tv_ketentuan_2)
    ImageView ivKetentuan2;
    @BindView(R.id.iv_toogle_2)
    TextView ivToogle2;
    @BindView(R.id.ll_line_ketentuan_2)
    LinearLayout llLineKetentuan2;
    @BindView(R.id.ll_content_2)
    LinearLayout llContent2;
    @BindView(R.id.rl_container_ketentuan_2)
    RelativeLayout rlContainerKetentuan2;
    boolean toogleShow = false;
    boolean toogleShow2 = false;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.bt_before)
    TextView tvBefore;
    @BindView(R.id.tv_net_amount)
    EditText tvNetAmount;
    @BindView(R.id.tv_total_payment)
    TextView tvTotalPayment;
    @BindView(R.id.tv_reksadana_name)
    TextView tvReksadanaName;
    @BindView(R.id.tv_investment_manager)
    TextView tvInvestmentManager;
    @BindView(R.id.tv_reksadana_type)
    TextView tvReksadanaType;
    @BindView(R.id.tv_buying_amount)
    TextView tvBuyingAmount;
    @BindView(R.id.tv_nab)
    TextView tvNab;
    @BindView(R.id.tv_min_purchase)
    TextView tvMinPurchase;
    @BindView(R.id.tv_buying_price)
    TextView tvBuyingPrice;
    @BindView(R.id.tv_buying_type)
    TextView tvBuyingType;

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private DecimalFormat currencyFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_detail_transaction_layout);
        ButterKnife.bind(this);

        TransactionRequest request = Global.TRANSACTION_REQUEST;

        tvNetAmount.setText(decimalFormat.format(request.getAmount()));
        tvNetAmount.addTextChangedListener(new TextWatcher(tvNetAmount));
        tvTotalPayment.setText(decimalFormat.format(request.getAmount()));
        tvTitle.setText("BELI PRODUK");

        if (request.getInvestmentProduct().getUnit()>0) {
            tvBuyingType.setText("Top up");
        }else{
            tvBuyingType.setText("Subscription");
        }
        tvReksadanaName.setText(request.getInvestmentProduct().getProductName());
        tvInvestmentManager.setText(request.getInvestmentProduct().getInvestmentManager());
        tvReksadanaType.setText(request.getInvestmentProduct().getProductType());
        tvBuyingAmount.setText(decimalFormat.format(request.getInvestmentProduct().getAdminFee()));
        tvBuyingPrice.setText(currencyFormat.format(request.getInvestmentProduct().getAdminFee()));
        tvNab.setText(decimalFormat.format(request.getInvestmentProduct().getNab()));
        tvMinPurchase.setText(currencyFormat.format(request.getInvestmentProduct().getMinPurchase()));
    }

    class TextWatcher extends NumberTextWatcher{
        TextWatcher(EditText et){
            super(et);
        }

        @Override
        public void afterTextChanged(Editable s) {
            et.removeTextChangedListener(this);
            change(s);
            try {
                tvTotalPayment.setText(decimalFormat.format(decimalFormat.parse(s.toString()).doubleValue()));
            }catch (Exception ex){}
            et.addTextChangedListener(this);
        }
    }

    @OnClick({R.id.iv_finish,R.id.iv_toogle,R.id.iv_toogle_2,
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
            case R.id.iv_toogle_2:
                if (!toogleShow2) {
                    rlContainerKetentuan2.setBackgroundResource(R.drawable.round_background_light);
                    rlContainerKetentuan2.setPadding(8, 8, 8, 8);
                    ivToogle2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    ivKetentuan2.setImageResource(R.drawable.ic_down_toogle);
                    llLineKetentuan2.setVisibility(View.VISIBLE);
                    llContent2.setVisibility(View.VISIBLE);

                    toogleShow2 = true;

                } else {
                    rlContainerKetentuan2.setBackgroundResource(R.drawable.round_background_primary);
                    rlContainerKetentuan2.setPadding(8, 8, 8, 8);
                    ivToogle2.setTextColor(Color.parseColor("#FFFFFF"));
                    ivKetentuan2.setImageResource(R.drawable.ic_add_white);
                    llLineKetentuan2.setVisibility(View.GONE);
                    llContent2.setVisibility(View.GONE);

                    toogleShow2 = false;
                }
                break;
            case R.id.bt_next:
                startActivity(new Intent(getApplicationContext(), BuyDetailPaymentActivity.class));
                break;
            case R.id.bt_before:
                finish();
                break;
        }
    }

}
