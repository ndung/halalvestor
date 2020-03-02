package id.halalvestor.activity.main.history;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.model.Transaction;

public class InvestmentCompletedBuyActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_investment_manager)
    TextView tvInvestmentManager;
    @BindView(R.id.tv_product_type)
    TextView tvProductType;
    @BindView(R.id.tv_instruction)
    TextView tvInstruction;
    @BindView(R.id.tv_net_amount)
    TextView tvNetAmount;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_buying_price)
    TextView tvBuyingPrice;
    @BindView(R.id.tv_buying_amount)
    TextView tvBuyingAmount;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private DecimalFormat currencyFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_buy_transaction_detail_layout);
        ButterKnife.bind(this);
        tvTitle.setText("Detail Transaksi");

        Transaction t = (Transaction) getIntent().getSerializableExtra("transaction");

        tvOrderTime.setText("Waktu Order "+sdf.format(t.getTrxDate()) + " WIB");
        //tvProductName.setText(t.getProduct().getProductName());
        tvProductName.setText(t.getDetail().get("product.name"));
        tvOrderNo.setText("Order No. "+t.getOrderNo());
        tvInvestmentManager.setText(t.getDetail().get("product.investmentManager"));
        tvProductType.setText(t.getDetail().get("product.type"));
        tvInstruction.setText(t.getDetail().get("instruction"));
        tvUnit.setText(decimalFormat.format(t.getDetail().get("unit")));
        tvNetAmount.setText(currencyFormat.format(t.getNetAmount()));
        tvBuyingPrice.setText((currencyFormat.format(t.getFee())));
        tvBuyingAmount.setText(currencyFormat.format(t.getTotalAmount()));
    }

    @OnClick(R.id.iv_finish)
    public void onViewClicked() {
        finish();
    }
}
