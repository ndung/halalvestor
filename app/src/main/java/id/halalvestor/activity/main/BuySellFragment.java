package id.halalvestor.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.activity.main.insurance.InsuranceOptionFragment;

public class BuySellFragment extends BaseFragment {

    @BindView(R.id.cv_invest)
    CardView cvInvest;
    @BindView(R.id.cv_asurance)
    CardView cvAsurance;

    private Handler mHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mHandler = new Handler();

        View view = inflater.inflate(R.layout.buysell_layout, container, false);
        ButterKnife.bind(this, view);

        mHandler = new Handler();

        cvInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //if (App.getInstance().getPreferenceManager().isBareksaLoggedIn(firebaseUser.getUid())) {
                    /**Runnable mPendingRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // update the main content by replacing fragments
                            Fragment fragment = new InvestmentBuySellFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame_layout, fragment);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    };
                    if (mPendingRunnable != null) {
                        mHandler.post(mPendingRunnable);
                    }*/
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("fragment", "investment_buysell");
                    startActivity(intent);
                    getActivity().finish();
                //} else {
                //    Intent intent = new Intent(getActivity(), InvestmentAuthActivity.class);
                //    startActivity(intent);
                //}
            }
        });

        cvAsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        Fragment fragment = new InsuranceOptionFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame_layout, fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };


                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
            }
        });

        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
