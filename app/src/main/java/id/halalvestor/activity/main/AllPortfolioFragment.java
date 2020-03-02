package id.halalvestor.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.halalvestor.R;

public class AllPortfolioFragment extends BaseFragment {

    @BindView(R.id.cv_invest)
    CardView cvInvest;
    @BindView(R.id.cv_asurance)
    CardView cvAsurance;
    Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.portfolio_all_layout, container, false);
        ButterKnife.bind(this, view);

        cvInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PortfolioInvestmentActivity.class));
            }
        });

        cvAsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PortfolioInsuranceActivity.class));
            }
        });
        return view;
    }
}
