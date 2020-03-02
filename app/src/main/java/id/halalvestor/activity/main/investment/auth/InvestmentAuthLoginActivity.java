package id.halalvestor.activity.main.investment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.MainActivity;

public class InvestmentAuthLoginActivity extends AppCompatActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_id)
    TextInputEditText etId;
    @BindView(R.id.et_pwd)
    TextInputEditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_auth_login);
        ButterKnife.bind(this);
        tvTitle.setText("SIGN IN");
        ivFinish.setImageResource(R.mipmap.ic_close);
    }

    @OnClick({R.id.iv_finish, R.id.bt_login, R.id.tv_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                break;
            case R.id.bt_login:
                String id = etId.getText().toString();
                String pwd = etPwd.getText().toString();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "investment_buysell");
                startActivity(intent);
                this.finish();
                break;
            case R.id.tv_regist:
                startActivity(new Intent(getApplicationContext(), InvestmentFormActivity.class));
                break;
        }
    }
}
