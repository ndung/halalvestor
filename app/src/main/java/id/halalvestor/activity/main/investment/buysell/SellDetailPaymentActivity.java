package id.halalvestor.activity.main.investment.buysell;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;

public class SellDetailPaymentActivity extends BaseActivity {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_detail_payment_layout);

        ButterKnife.bind(this);

        tvTitle.setText("JUAL PRODUK");
    }

    @OnClick({R.id.iv_finish,R.id.iv_toogle, R.id.tv_before, R.id.bt_konfirmasi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.iv_toogle:
                if (!toogleShow) {
                    rlContainerKetentuan.setBackgroundResource(R.drawable.round_background_light);
                    rlContainerKetentuan.setPadding(16, 16, 16, 16);
                    ivToogle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    ivKetentuan.setImageResource(R.drawable.ic_down_toogle);
                    llLineKetentuan.setVisibility(View.VISIBLE);
                    llContent.setVisibility(View.VISIBLE);

                    toogleShow = true;

                } else {
                    rlContainerKetentuan.setBackgroundResource(R.drawable.round_background_primary);
                    rlContainerKetentuan.setPadding(16, 16, 16, 16);
                    ivToogle.setTextColor(Color.parseColor("#FFFFFF"));
                    ivKetentuan.setImageResource(R.drawable.ic_add_white);
                    llLineKetentuan.setVisibility(View.GONE);
                    llContent.setVisibility(View.GONE);

                    toogleShow = false;
                }
                break;
            case R.id.bt_konfirmasi:
                startActivity(new Intent(getApplicationContext(), SellDetailConfirmationActivity.class));
                break;
            case R.id.tv_before:
                finish();
                break;
        }
    }
}
