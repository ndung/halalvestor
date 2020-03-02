package id.halalvestor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import id.halalvestor.model.AppUser;
import id.halalvestor.model.ProductType;
import id.halalvestor.model.RiskProfile;

public class InvestmentOptionActivity extends BaseActivity {

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
    boolean toogleShow2 = false;

    @BindView(R.id.tv_monthly_investment)
    TextView tvMonthlyInvestment;
    @BindView(R.id.tv_insurance_recommendation)
    TextView tvInsuranceRecommendation;
    @BindView(R.id.tv_choose)
    Button tvChoose;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.chart)
    PieChart chart;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.investment_option_layout);
        ButterKnife.bind(this);

        final List<RiskProfile> options = App.getInstance().getPreferenceManager().getAppUser().getProfileOptions();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                LinearLayout tl = (LinearLayout) ((ViewGroup) tabs.getChildAt(0)).getChildAt(tab.getPosition());
                TextView text = (TextView) tl.getChildAt(1);
                text.setTextColor(getResources().getColor(R.color.white));
                tl.setBackgroundResource(R.drawable.round_background_primary_dark);

                int i = tab.getPosition();
                final RiskProfile model = options.get(i);
                tvMonthlyInvestment.setText(decimalFormat.format(model.getMonthlyInstallment()));
                AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
                if (model.getMonthlyInstallment() > appUser.getMonthlyIncome() * 0.3d) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(Html.fromHtml(getResources().getString(R.string.option_not_available)));
                }
                tvChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateInvestmentOption(model);
                    }
                });
                tvInsuranceRecommendation.setText("< Rp "+decimalFormat.format(App.getInstance().getPreferenceManager().getAppUser().getMonthlyIncome()*0.1)+" / bulan");

                chart.setUsePercentValues(true);
                chart.getDescription().setEnabled(false);
                chart.setExtraOffsets(5, 10, 5, 5);
                chart.setDragDecelerationFrictionCoef(0.95f);

                //mChart.setCenterText(generateCenterSpannableText());
                chart.setRenderer(new CustomPieChartRenderer(
                        chart,
                        chart.getAnimator(),
                        chart.getViewPortHandler()
                ));

                chart.setDrawHoleEnabled(true);
                chart.setHoleColor(Color.WHITE);

                chart.setTransparentCircleColor(Color.WHITE);
                chart.setTransparentCircleAlpha(110);

                chart.setHoleRadius(75f);
                chart.setTransparentCircleRadius(80f);
                chart.setDrawEntryLabels(false);

                //mChart.setDrawCenterText(true);
                //mChart.setDrawEntryLabels(true);

                chart.setRotationAngle(0);
                // enable rotation of the chart by touch
                chart.setRotationEnabled(true);
                chart.setHighlightPerTapEnabled(true);;

                // mChart.setUnit(" €");
                // mChart.setDrawUnitsInChart(true);

                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (ProductType product : model.getProductRecommendations().keySet()){
                    entries.add(new PieEntry(model.getProductRecommendations().get(product).floatValue(), product.getName()));
                    colors.add(ColorTemplate.rgb(product.getPieColor()));
                }

                PieDataSet dataSet = new PieDataSet(entries, "");

                dataSet.setDrawIcons(false);

                dataSet.setSliceSpace(3f);
                dataSet.setIconsOffset(new MPPointF(0, 40));
                dataSet.setSelectionShift(5f);

                dataSet.setColors(colors);
                dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                //dataSet.setSelectionShift(0f);

                PieData data = new PieData(dataSet);

                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(12f);
                data.setValueTextColor(Color.BLACK);
                chart.setData(data);

                // undo all highlights
                chart.highlightValues(null);
                chart.invalidate();

                float spin = 60+(entries.size()*15);

                chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                chart.spin(2000, 0, spin, Easing.EasingOption.EaseInOutQuad);

                Legend l = chart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setDrawInside(false);
                l.setForm(Legend.LegendForm.CIRCLE);
                l.setTextSize(12f);
                l.setXEntrySpace(0f);
                l.setYEntrySpace(5f);
                l.setYOffset(0f);
                l.setWordWrapEnabled(true);

                // entry label styling
                chart.setEntryLabelColor(Color.BLACK);
                chart.setEntryLabelTextSize(12f);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout tl = (LinearLayout) ((ViewGroup) tabs.getChildAt(0)).getChildAt(tab.getPosition());
                TextView text = (TextView) tl.getChildAt(1);
                text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tl.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i=0;i<options.size();i++){
            RiskProfile riskProfile = options.get(i);
            tabs.addTab(tabs.newTab().setText(riskProfile.getName()));
            if (riskProfile.isRecommended()){
                TabLayout.Tab tab = tabs.getTabAt(i);
                tab.select();
            }
        }

    }

    private void updateInvestmentOption(RiskProfile option){
        Map<String,String> params = new HashMap<>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("option", String.valueOf(option.getId()));
        connectServerApi("update_investment_option", params);
    }

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        AppUser appUser = gson.fromJson(result, AppUser.class);
        if (appUser!=null){
            App.getInstance().getPreferenceManager().setAppUser(appUser);
        }
        next();
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);
        showErrorMessage(tabs, error);
    }

    private static final String TAG = InvestmentOptionActivity.class.getSimpleName();

    private void next(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        this.finish();
    }

    @OnClick({R.id.btn_resurvey, R.id.iv_toogle_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_resurvey:
                Intent intent = new Intent(this, SurveyActivity.class);
                intent.putExtra("activity","id.halalvestor.activity.MainActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
                break;
            case R.id.iv_toogle_2:
                if (!toogleShow2) {
                    rlContainerKetentuan2.setBackgroundResource(R.drawable.round_background_light);
                    rlContainerKetentuan2.setPadding(8, 8, 8, 8);
                    ivToogle2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    ivKetentuan2.setImageResource(R.drawable.ic_down_toogle);
                    llLineKetentuan2.setVisibility(View.VISIBLE);
                    llContent2.setVisibility(View.VISIBLE);

                    toogleShow2 = true;

                } else {
                    rlContainerKetentuan2.setBackgroundResource(R.drawable.round_background_primary);
                    rlContainerKetentuan2.setPadding(8, 8, 8, 8);
                    ivToogle2.setTextColor(Color.parseColor("#FFFFFF"));
                    ivKetentuan2.setImageResource(R.drawable.ic_add_white);
                    llLineKetentuan2.setVisibility(View.GONE);
                    llContent2.setVisibility(View.GONE);

                    toogleShow2 = false;
                }
                break;
        }
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

            Drawable d =  ResourcesCompat.getDrawable(context.getResources(), R.drawable.option, null);

            if(d != null) {
                float halfWidth = d.getIntrinsicWidth() / 2;
                float halfHeight = d.getIntrinsicHeight() / 2;

                d.setBounds((int) (center.x - halfWidth), (int) (center.y - halfHeight), (int) (center.x + halfWidth), (int) (center.y + halfHeight) );
                d.draw(c);
            }
        }
    }

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

}
