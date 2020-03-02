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

public class BuyDetailPaymentActivity extends BaseActivity {

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
    @BindView(R.id.tv_before)
    TextView tvBefore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_detail_payment_layout);
        ButterKnife.bind(this);

        tvTitle.setText("BELI PRODUK");
    }

    @OnClick({R.id.iv_finish,R.id.iv_toogle,R.id.iv_toogle_2, R.id.tv_before, R.id.bt_konfirmasi})
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
            case R.id.iv_toogle_2:
                if (!toogleShow2) {
                    rlContainerKetentuan2.setBackgroundResource(R.drawable.round_background_light);
                    rlContainerKetentuan2.setPadding(16, 16, 16, 16);
                    ivToogle2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    ivKetentuan2.setImageResource(R.drawable.ic_down_toogle);
                    llLineKetentuan2.setVisibility(View.VISIBLE);
                    llContent2.setVisibility(View.VISIBLE);

                    toogleShow2 = true;

                } else {
                    rlContainerKetentuan2.setBackgroundResource(R.drawable.round_background_primary);
                    rlContainerKetentuan2.setPadding(16, 16, 16, 16);
                    ivToogle2.setTextColor(Color.parseColor("#FFFFFF"));
                    ivKetentuan2.setImageResource(R.drawable.ic_add_white);
                    llLineKetentuan2.setVisibility(View.GONE);
                    llContent2.setVisibility(View.GONE);

                    toogleShow2 = false;
                }
                break;
            case R.id.bt_konfirmasi:
                startActivity(new Intent(getApplicationContext(), BuyDetailConfirmationActivity.class));
                break;
            case R.id.tv_before:
                finish();
                break;
        }
    }
}
