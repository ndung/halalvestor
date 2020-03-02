package id.halalvestor.activity.main.investment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Global;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.adapter.MonthlyInstallmentAdapter;
import id.halalvestor.activity.main.investment.buysell.BuyProductTransactionActivity;
import id.halalvestor.activity.main.investment.buysell.SellDetailTransactionActivity;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.InvestmentProduct;
import id.halalvestor.model.ProductType;
import id.halalvestor.model.TransactionRequest;
import id.halalvestor.ui.QuantityView;

public class ProductDetailActivity extends BaseActivity {


    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.iv_collapse)
    ImageView ivCollapse;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_jual)
    Button btJual;
    @BindView(R.id.bt_beli)
    Button btBeli;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_type)
    TextView tvProductType;
    @BindView(R.id.tv_product_nab)
    TextView tvProductNab;
    @BindView(R.id.tv_product_growth)
    TextView tvProductGrowth;
    @BindView(R.id.tv_product_own)
    TextView tvProductOwn;
    @BindView(R.id.tv_own_amount)
    TextView tvOwnAmount;
    @BindView(R.id.quantityView)
    QuantityView quantityView;
    @BindView(R.id.tv_monthly_investment)
    TextView tvMonthlyInstallment;
    @BindView(R.id.ll_detail)
    LinearLayout ll;
    @BindView(R.id.rv)
    RecyclerView rv;

    InvestmentProduct product;

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_product_layout);
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText("YTD"));
        tabLayout.addTab(tabLayout.newTab().setText("1 TAHUN"));
        tabLayout.addTab(tabLayout.newTab().setText("3 TAHUN"));
        tabLayout.addTab(tabLayout.newTab().setText("5 TAHUN"));

        LinearLayout tl = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition());
        TextView text = (TextView) tl.getChildAt(1);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        tl.setBackgroundResource(R.drawable.round_background_primary_dark);

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
        tabLayout.setTabTextColors(Color.parseColor("#6e6e6e"), Color.parseColor("#FFFFFF"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                LinearLayout tl = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView text = (TextView) tl.getChildAt(1);
                text.setTypeface(text.getTypeface(), Typeface.BOLD);

                tl.setBackgroundResource(R.drawable.round_background_primary_dark);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout tl = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView text = (TextView) tl.getChildAt(1);
                text.setTypeface(text.getTypeface(), Typeface.NORMAL);

                tl.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        configChart();

        AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();

        product = (InvestmentProduct) getIntent().getSerializableExtra("product");
        if (product!=null){
            tvProductName.setText(product.getProductName());
            tvProductGrowth.setText(product.getGrowth().intValue()+"%");
            tvProductNab.setText(decimalFormat.format(product.getNab()));
            tvProductType.setText(product.getProductType());
            tvProductOwn.setText(product.getUnit().intValue()+" Dana Saham");
            tvOwnAmount.setText(decimalFormat.format(product.getAmount()));

            List<Object[]> list = new ArrayList<Object[]>();
            Map<ProductType,Double> map = appUser.getProductRecommendations();
            if (map!=null){
                for (ProductType productType : map.keySet()){
                    Object[] arr = new Object[2];
                    arr[0] = productType.getName();
                    arr[1] = Math.ceil(map.get(productType))/100*appUser.getMonthlyInstallment();
                    list.add(arr);
                }
            }

            MonthlyInstallmentAdapter adapter = new MonthlyInstallmentAdapter(list);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(adapter);

            ivCollapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ll.getVisibility()==View.VISIBLE){
                        ll.setVisibility(View.GONE);
                    }else if (ll.getVisibility()==View.GONE){
                        ll.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        tvMonthlyInstallment.setText("Investasi per bulan: "+decimalFormat.format(appUser.getMonthlyInstallment()));
        tvTitle.setText("Detail Produk");

    }

    private void configChart() {
        List<Entry> list = new ArrayList<>();

//        list.add(new Entry(1.0f, 1.0f));
//        list.add(new Entry(1.1f, 1.1f));
//        list.add(new Entry(1.1f, 1.1f));
//        list.add(new Entry(1.1f, 1.1f));
//        list.add(new Entry(1.3f, 1.2f));
//        list.add(new Entry(1.3f, 1.2f));
//        list.add(new Entry(1.3f, 1.2f));
//        list.add(new Entry(1.3f, 1.2f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.5f, 2.5f));
//        list.add(new Entry(3.0f, 3.0f));
//        list.add(new Entry(3.0f, 3.0f));
//        list.add(new Entry(3.0f, 3.0f));
//        list.add(new Entry(3.0f, 3.0f));
//        list.add(new Entry(3.0f, 3.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(2.0f, 2.0f));
//        list.add(new Entry(1.1f, 1.1f));
//        list.add(new Entry(1.1f, 1.1f));
//        list.add(new Entry(1.1f, 1.1f));
//        list.add(new Entry(1.1f, 1.1f));

//        list.add(new Entry(1f, 1));
//        list.add(new Entry(2f, 3));
//        list.add(new Entry(3f, 5));
//        list.add(new Entry(4f, 7));
//        list.add(new Entry(5f, 9));
//        list.add(new Entry(6f, 7));
//        list.add(new Entry(7f, 5));

        float randomMultiplier = 10 * 1000f;

        for (int i = 1; i < 5; i++) {
            list.add(new Entry(i, (float) (Math.random() * randomMultiplier)));
//            list.add(new Entry(i, (float) (Math.random() * randomMultiplier)));
//            list.add(new Entry(i, (float) (Math.random() * randomMultiplier)));
//            list.add(new Entry(i, (float) (Math.random() * randomMultiplier)));
        }


        LineDataSet lineDataSet = new LineDataSet(list, "Performa Product");
        lineDataSet.setDrawValues(false);
//        lineDataSet.setDrawCircles(false);
//        lineDataSet.setColor(R.color.colorPrimary);
//        lineDataSet.setCircleColor(R.color.colorPrimaryDark);
//        lineDataSet.setColors(int[]{R.color.colorPrimary, R.color.colorPrimaryDark},Context);
        lineDataSet.setColors(new int[]{R.color.colorPrimaryDark, R.color.colorPrimaryDark}, getApplicationContext());
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCubicIntensity(14f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(lineDataSet);

        chart.setContentDescription("Product Performance");
        chart.getDescription().setEnabled(false);
//         chart.setScaleXEnabled(false);
        chart.animateX(3000);
        chart.getLegend().setEnabled(false);
        chart.setData(lineData);


//        chart.getXAxis().setEnabled(false);
//        chart.getAxisLeft().setPosition(XAxis.XAxisPosition.TOP);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.setAutoScaleMinMaxEnabled(true);


        chart.invalidate();
    }

    @OnClick(R.id.iv_finish)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.bt_jual, R.id.bt_beli})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_jual:
                if (product.getAmount()>=quantityView.getQuantity()) {
                    TransactionRequest request = new TransactionRequest();
                    request.setInvestmentProduct(product);
                    request.setAmount(quantityView.getQuantity());
                    request.setType(1);
                    Global.TRANSACTION_REQUEST = request;
                    Intent intent = new Intent(getApplicationContext(), SellDetailTransactionActivity.class);
                    startActivity(intent);
                }else{
                    showErrorMessage(quantityView, "Jumlah penjualan tidak boleh lebih besar dari jumlah yang dimiliki");
                }
                break;
            case R.id.bt_beli:
                if (quantityView.getQuantity()>=product.getMinPurchase()) {
                    TransactionRequest request = new TransactionRequest();
                    request.setInvestmentProduct(product);
                    request.setAmount(quantityView.getQuantity());
                    request.setType(2);
                    Global.TRANSACTION_REQUEST = request;
                    Intent intent = new Intent(getApplicationContext(), BuyProductTransactionActivity.class);
                    startActivity(intent);
                }else{
                    showErrorMessage(quantityView, "Jumlah pembelian minimum untuk produk ini adalah "+decimalFormat.format(product.getMinPurchase()));
                }
                break;
        }
    }
}
