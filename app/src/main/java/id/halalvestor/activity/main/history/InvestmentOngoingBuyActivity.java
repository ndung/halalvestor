package id.halalvestor.activity.main.history;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.MainActivity;

public class InvestmentOngoingBuyActivity extends BaseActivity {

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
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.rl_container_ketentuan)
    RelativeLayout rlContainerKetentuan;
    boolean toogleShow = false;
    @BindView(R.id.bt_done)
    Button btDone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_ongoing_buy_transaction_detail_layout);
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

    @OnClick({R.id.iv_finish, R.id.iv_toogle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                done();
                break;
            case R.id.iv_toogle:
                if(!toogleShow){
                    rlContainerKetentuan.setBackgroundResource(R.drawable.round_background_light);
                    rlContainerKetentuan.setPadding(16,16,16,16);
                    ivToogle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    ivKetentuan.setImageResource(R.drawable.ic_down_toogle);
                    llLineKetentuan.setVisibility(View.VISIBLE);
                    llContent.setVisibility(View.VISIBLE);

                    toogleShow = true;

                }else{
                    rlContainerKetentuan.setBackgroundResource(R.drawable.round_background_primary);
                    rlContainerKetentuan.setPadding(16,16,16,16);
                    ivToogle.setTextColor(Color.parseColor("#FFFFFF"));
                    ivKetentuan.setImageResource(R.drawable.ic_add_white);
                    llLineKetentuan.setVisibility(View.GONE);
                    llContent.setVisibility(View.GONE);

                    toogleShow = false;
                }
                break;
        }
    }
}
