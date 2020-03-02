package id.halalvestor.activity.main.investment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;

public class InvestmentAuthActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);
        ButterKnife.bind(this);
        tvTitle.setText("DAFTAR NASABAH");
    }

    @OnClick({R.id.iv_finish, R.id.bt_register, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.bt_register:
                Intent register = new Intent(this, InvestmentFormActivity.class);
                startActivity(register);
                break;
            case R.id.tv_login:
                Intent login = new Intent(this, InvestmentAuthLoginActivity.class);
                startActivity(login);
                break;
        }
    }
}
