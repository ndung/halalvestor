package id.halalvestor.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.activity.adapter.InvestmentGoalAdapter;
import id.halalvestor.activity.adapter.ProgressGoalAdapter;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.InvestmentGoal;
import id.halalvestor.util.ItemOffsetDecoration;
import id.halalvestor.util.SingleUploadBroadcastReceiver;

public class InvestmentProgressFragment extends BaseFragment implements SingleUploadBroadcastReceiver.Delegate {

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    private static final String TAG = InvestmentProgressFragment.class.toString();

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    TextView tvInvestmentGoalAmount;
    TextView tvProgress;
    TextView tvInvestmentGoalTime;
    ProgressBar progressBar;

    InvestmentGoalAdapter investmentGoalAdapter;
    List<InvestmentGoal> goals = new ArrayList<>();
    //List<InvestmentGoal> newList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_layout, container, false);
        ButterKnife.bind(this, view);

        final AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
        tvInvestmentGoalAmount = view.findViewById(R.id.tv_investment_goal_amount);
        tvProgress = view.findViewById(R.id.tv_progress);
        tvInvestmentGoalTime = view.findViewById(R.id.tv_investment_goal_time);
        progressBar = view.findViewById(R.id.progressBar);

        render(appUser);
        listAdapter = new ProgressGoalAdapter(goals, new ProgressGoalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InvestmentGoal model) {
                tvInvestmentGoalAmount.setText(decimalFormat.format(model.getFund()));
                tvInvestmentGoalTime.setText(model.getTime() + " Tahun");
                tvProgress.setText(model.getProgress() + "%");
                progressBar.setProgress(model.getProgress());
            }
        }, getResources().getColor(R.color.white), R.drawable.active_goal_white, R.drawable.inactive_goal);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), goals.size()));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(1));
        recyclerView.setAdapter(listAdapter);

        for (InvestmentGoal goal : appUser.getInvestmentGoals()) {
            goal.setInvestment(appUser.getTotalInvestment());
            goals.add(goal);
        }
        investmentGoalAdapter = new InvestmentGoalAdapter(getActivity(), goals, new InvestmentGoalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InvestmentGoal model, int position) {
                changeInvestmentGoalPic(model, position);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(investmentGoalAdapter);
        //rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    private void changeInvestmentGoalPic(InvestmentGoal goal, int position) {
        this.goal = goal;
        this.position = position;
        ((MainActivity) getActivity()).setDelegate(this);
        ((MainActivity) getActivity()).showFileChooser(2);
    }

    int position;
    InvestmentGoal goal;
    ProgressGoalAdapter listAdapter;

    @Override
    public void onResume() {
        super.onResume();

        AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
        render(appUser);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), goals.size()));
        investmentGoalAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_edit_investment_goals, R.id.iv_edit_investment_goals})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_investment_goals:
            case R.id.iv_edit_investment_goals:
                startChangeInvestmentGoalsActivity();
                break;
        }
    }

    private void render(AppUser appUser) {
        goals.clear();
        double fv = 0;
        InvestmentGoal displayedGoal = null;
        for (int i = 0; i < appUser.getInvestmentGoals().size(); i++) {
            InvestmentGoal goal = appUser.getInvestmentGoals().get(i);
            fv = fv + goal.getFv();

            if (appUser.getTotalInvestment() > fv) {
                goal.setAchieved(true);
                goal.setProgress(100);
            }
            if (i > 0) {
                goal.setPreviousGoalAchieved(goals.get(i - 1).isAchieved());
                if (!goal.isPreviousGoalAchieved()) {
                    goal.setProgress(0);
                }
            }
            if (i == 0 || goal.isPreviousGoalAchieved()) {
                Double progress = appUser.getTotalInvestment() * 100 / fv;
                if (progress > 100) {
                    goal.setProgress(100);
                } else {
                    goal.setProgress(progress.intValue());
                    if (displayedGoal == null) {
                        displayedGoal = goal;
                    }
                }
            }
            goal.setInvestment(appUser.getTotalInvestment());
            goals.add(goal);
        }
        if (displayedGoal == null) {
            displayedGoal = goals.get(0);
        }

        tvInvestmentGoalAmount.setText(decimalFormat.format(displayedGoal.getFund()));
        tvInvestmentGoalTime.setText(displayedGoal.getTime() + " Tahun");
        tvProgress.setText(displayedGoal.getProgress() + "%");
        progressBar.setProgress(displayedGoal.getProgress());

    }


    @Override
    public void onProgress(int progress) {
        ((MainActivity) getActivity()).showProgressDialog();
    }

    @Override
    public void onError(Exception exception) {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void onCompleted(int serverResponseCode, int requestCode, String serverResponseBody) {
        ((MainActivity) getActivity()).hideProgressDialog();
        if (requestCode == 9992) {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(goal.getId()));
            params.put("pic", serverResponseBody);
            ((MainActivity) getActivity()).connectServerApi("update_investment_goal_pic", params);
            goal.setPic(serverResponseBody);
            goals.remove(position);
            goals.add(position, goal);
            AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
            appUser.setInvestmentGoals(goals);
            App.getInstance().getPreferenceManager().setAppUser(appUser);
            investmentGoalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelled() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    private void startChangeInvestmentGoalsActivity() {
        Intent intent = new Intent(getActivity(), ChangeInvestmentGoalsActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

}
