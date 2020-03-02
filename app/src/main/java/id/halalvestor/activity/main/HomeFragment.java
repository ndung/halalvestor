package id.halalvestor.activity.main;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.activity.adapter.ProgressGoalAdapter;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.InvestmentGoal;
import id.halalvestor.util.ItemOffsetDecoration;

public class HomeFragment extends BaseFragment {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    ProgressGoalAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);

        TextView yourProgress = view.findViewById(R.id.btn_your_progress);
        yourProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showProfile();
            }
        });
        TextView tv_investment_progress = view.findViewById(R.id.tv_investment_progress);
        tv_investment_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showPortfolio();
            }
        });

        /**LinearLayout portfolioLayout = view.findViewById(R.id.ll_portfolio);
        portfolioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showPortfolio();
            }
        });*/

        LinearLayout buysellLayout = view.findViewById(R.id.ll_jualbeli);
        buysellLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showBuySell();
            }
        });

        LinearLayout instructionLayout = view.findViewById(R.id.ll_petunjuk);
        instructionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showInstruction();
            }
        });

        LinearLayout chatLayout = view.findViewById(R.id.ll_bantuan);
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showChatActivity();
            }
        });
        //ProgressBar progressBar = view.findViewById(R.id.progressBar);
        //progressBar.setScaleY(3f);

        TextView tvMonthly = view.findViewById(R.id.tv_monthly_investment);
        final AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();

        tvMonthly.setText("Investasi / bulan : "+decimalFormat.format(appUser.getMonthlyInstallment()));
        //final TextView tvInvestmentGoalAmount = view.findViewById(R.id.tv_investment_goal_amount);
        final TextView tvProgress = view.findViewById(R.id.tv_progress);
        final TextView tvInvestmentGoalTime = view.findViewById(R.id.tv_investment_goal_time);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);

        //TextView tvLastUpdateInvestment = view.findViewById(R.id.tv_last_update_investment);
        //tvLastUpdateInvestment.setText("per tanggal "+sdf.format(new Date()));
        TextView tvTotalInvestment = view.findViewById(R.id.tv_total_investment);
        tvTotalInvestment.setText(decimalFormat.format(appUser.getTotalInvestment()));

        List<InvestmentGoal> list = appUser.getInvestmentGoals();
        List<InvestmentGoal> newList = new ArrayList<>();

        double fv = 0;
        InvestmentGoal displayedGoal = null;
        for (int i = 0 ; i<list.size() ; i++){
            InvestmentGoal goal = list.get(i);
            fv = fv+goal.getFv();

            if (appUser.getTotalInvestment()>fv){
                goal.setAchieved(true);
                goal.setProgress(100);
            }
            if (i>0){
                goal.setPreviousGoalAchieved(newList.get(i-1).isAchieved());
                if (!goal.isPreviousGoalAchieved()){
                    goal.setProgress(0);
                }
            }
            if (i==0||goal.isPreviousGoalAchieved()) {
                Double progress = appUser.getTotalInvestment()*100 / fv;
                if (progress>100){
                    goal.setProgress(100);
                }else {
                    goal.setProgress(progress.intValue());
                    if(displayedGoal==null) {
                        displayedGoal = goal;
                    }
                }
            }
            newList.add(goal);
        }
        if (displayedGoal==null){
            displayedGoal = newList.get(0);
        }

        //tvInvestmentGoalAmount.setText(decimalFormat.format(displayedGoal.getFund()));
        tvInvestmentGoalTime.setText(displayedGoal.getTime() + " Tahun");
        tvProgress.setText(displayedGoal.getProgress()+"%");
        progressBar.setProgress(displayedGoal.getProgress());

        listAdapter = new ProgressGoalAdapter(newList, new ProgressGoalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InvestmentGoal model) {
                //tvInvestmentGoalAmount.setText(decimalFormat.format(model.getFund()));
                tvInvestmentGoalTime.setText(model.getTime()+" Tahun");
                tvProgress.setText(model.getProgress()+"%");
                progressBar.setProgress(model.getProgress());
            }
        }, getResources().getColor(R.color.greyDark), R.drawable.active_goal, R.drawable.active_goal);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), list.size());
        recyclerView.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(1);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(listAdapter);

        return view;
    }

    private static final String TAG = HomeFragment.class.toString();

}