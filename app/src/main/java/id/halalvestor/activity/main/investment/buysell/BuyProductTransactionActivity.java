package id.halalvestor.activity.main.investment.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;

public class BuyProductTransactionActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_lanjut)
    Button ivLanjut;

    Integer amount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_product_layout);
        ButterKnife.bind(this);

        tvTitle.setText("BELI PRODUK");

        ivLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BuyDetailTransactionActivity.class);
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.iv_finish)
    public void onViewClicked() {
        finish();
    }
}
