package id.halalvestor.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

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
import id.halalvestor.activity.InvestmentOptionActivity;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.activity.SurveyActivity;
import id.halalvestor.activity.adapter.InvestmentGoalAdapter;
import id.halalvestor.activity.adapter.ProgressGoalAdapter;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.InvestmentGoal;
import id.halalvestor.model.ProductType;
import id.halalvestor.ui.CircleTransformer;
import id.halalvestor.util.Helper;
import id.halalvestor.util.ItemOffsetDecoration;
import id.halalvestor.util.SingleUploadBroadcastReceiver;

public class ProfileFragment extends BaseFragment implements SingleUploadBroadcastReceiver.Delegate {

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    private static final String TAG = ProfileFragment.class.toString();

    @BindView(R.id.imageView)
    ImageView imgProfile;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_monthly_income)
    TextView tvMonthlyIncome;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_education)
    TextView tvEducation;
    @BindView(R.id.tv_occupation)
    TextView tvOccupation;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.chart)
    PieChart mChart;

    TextView tvInvestmentGoalAmount;
    TextView tvProgress;
    TextView tvInvestmentGoalTime;
    ProgressBar progressBar;

    InvestmentGoalAdapter investmentGoalAdapter;
    List<InvestmentGoal> goals = new ArrayList<>();
    //List<InvestmentGoal> newList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        ButterKnife.bind(this, view);

        final AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser.getPhotoUrl() != null) {
            // Loading profile image
            Glide.with(this).load(firebaseUser.getPhotoUrl())
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransformer(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }

        tvName.setText(firebaseUser.getDisplayName());
        tvAge.setText(Helper.getAge(appUser.getBirthDate()) + " Tahun");
        tvStatus.setText(appUser.getMaritalStatus());
        //tvEducation.setText(appUser.getEducation());
        tvOccupation.setText(appUser.getOccupation());
        tvMonthlyIncome.setText(decimalFormat.format(appUser.getMonthlyIncome()));

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

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDisplayPicture();
            }
        });

        return view;
    }

    private void changeInvestmentGoalPic(InvestmentGoal goal, int position) {
        this.goal = goal;
        this.position = position;
        ((MainActivity) getActivity()).setDelegate(this);
        ((MainActivity) getActivity()).showFileChooser(2);
    }

    private void changeDisplayPicture() {
        ((MainActivity) getActivity()).setDelegate(this);
        ((MainActivity) getActivity()).showFileChooser(3);
    }

    int position;
    InvestmentGoal goal;
    ProgressGoalAdapter listAdapter;

    @Override
    public void onResume() {
        super.onResume();

        AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
        render(appUser);
        tvMonthlyIncome.setText(decimalFormat.format(appUser.getMonthlyIncome()));

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), goals.size()));
        investmentGoalAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();

        mChart.getData();
    }

    @OnClick({R.id.btn_resurvey, R.id.tv_edit_monthly_installment, R.id.iv_edit_monthly_installment, R.id.tv_edit_investment_goals, R.id.iv_edit_investment_goals,
            R.id.tv_edit_investment_profile, R.id.iv_edit_investment_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_resurvey:
                startSurveyActivity();
                break;
            case R.id.tv_edit_monthly_installment:
            case R.id.iv_edit_monthly_installment:
                startChangeMonthlyIncomeActivity();
                break;
            case R.id.tv_edit_investment_goals:
            case R.id.iv_edit_investment_goals:
                startChangeInvestmentGoalsActivity();
                break;
            case R.id.tv_edit_investment_profile:
            case R.id.iv_edit_investment_profile:
                startChangeInvestmentProfileActivity();
                break;
        }
    }

    private void render(AppUser appUser) {
        goals.clear();
        String riskProfile = "Konservatif";
        if (appUser.getRiskProfile() == 1) {
            riskProfile = "Agresif";
        } else if (appUser.getRiskProfile() == 2) {
            riskProfile = "Moderat";
        }
        tvEducation.setText(riskProfile);
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


        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);

        //mChart.setCenterText(generateCenterSpannableText());
        mChart.setRenderer(new CustomPieChartRenderer(
                mChart,
                mChart.getAnimator(),
                mChart.getViewPortHandler()
        ));

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(75f);
        mChart.setTransparentCircleRadius(80f);
        mChart.setDrawEntryLabels(false);

        //mChart.setDrawCenterText(true);
        //mChart.setDrawEntryLabels(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        Map<ProductType, Double> map = appUser.getProductRecommendations();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (ProductType type : map.keySet()) {
            entries.add(new PieEntry(map.get(type).floatValue(), type.getName()));
            colors.add(ColorTemplate.rgb(type.getPieColor()));
        }
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //entries.add(new PieEntry(appUser.getHighRisk().floatValue(), "high risk"));
        //entries.add(new PieEntry(appUser.getMediumRisk().floatValue(), "medium risk"));
        //entries.add(new PieEntry(appUser.getLowRisk().floatValue(), "low risk"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors


        ///colors.add(getResources().getColor(R.color.redDark));
        //colors.add(getResources().getColor(R.color.colorPrimaryDark));
        //colors.add(getResources().getColor(R.color.colorPrimary));

        dataSet.setColors(colors);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();

        float spin = 60 + (entries.size() * 15);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.spin(2000, 0, spin, Easing.EasingOption.EaseInOutQuad);

        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);
        //legend.setXEntrySpace(0f);
        legend.setYEntrySpace(5f);
        //legend.setYOffset(0f);
        legend.setWordWrapEnabled(true);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(10f);

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
        } else if (requestCode == 9993) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(serverResponseBody))
                    .build();
            firebaseUser.updateProfile(request);
            Glide.with(this).load(serverResponseBody)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransformer(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }
    }

    @Override
    public void onCancelled() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    private class CustomPieChartRenderer extends PieChartRenderer {
        private Context context;

        public CustomPieChartRenderer(PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
            super(chart, animator, viewPortHandler);
            context = chart.getContext();
        }

        @Override
        public void drawExtras(Canvas c) {
            super.drawExtras(c);
            drawImage(c);
        }

        private void drawImage(Canvas c) {
            MPPointF center = mChart.getCenterCircleBox();

            Drawable d = ResourcesCompat.getDrawable(context.getResources(), R.drawable.chart, null);

            if (d != null) {
                float halfWidth = d.getIntrinsicWidth() / 2;
                float halfHeight = d.getIntrinsicHeight() / 2;

                d.setBounds((int) (center.x - halfWidth), (int) (center.y - halfHeight), (int) (center.x + halfWidth), (int) (center.y + halfHeight));
                d.draw(c);
            }
        }
    }

    private void startSurveyActivity() {
        Intent intent = new Intent(getActivity(), SurveyActivity.class);
        intent.putExtra("activity", "id.halalvestor.activity.MainActivity");
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        getActivity().finish();
    }

    private void startChangeMonthlyIncomeActivity() {
        Intent intent = new Intent(getActivity(), ChangeMonthlyIncomeActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    private void startChangeInvestmentGoalsActivity() {
        Intent intent = new Intent(getActivity(), ChangeInvestmentGoalsActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    private void startChangeInvestmentProfileActivity() {
        Intent intent = new Intent(getActivity(), InvestmentOptionActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
