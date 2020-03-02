package id.halalvestor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.RiskProfile;

public class RiskProfileActivity extends BaseActivity {

    @BindView(R.id.risk_profile_title)
    TextView tvRiskProfileTitle;

    @BindView(R.id.risk_profile_desc)
    TextView tvRiskProfileDesc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.risk_profile_layout);
        ButterKnife.bind(this);

        AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();

        for (RiskProfile profile : appUser.getProfileOptions()){
            if (profile.getId() == appUser.getRiskProfile()){
                tvRiskProfileTitle.setText(profile.getName().toUpperCase());
                tvRiskProfileDesc.setText(Html.fromHtml(profile.getDescription()));
                break;
            }
        }
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_next:
                Intent intent = new Intent();
                intent.setClass(this, InvestmentOptionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
