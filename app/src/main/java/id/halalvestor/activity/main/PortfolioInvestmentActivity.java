package id.halalvestor.activity.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.activity.adapter.EvaluationInvestmentDetailAdapter;
import id.halalvestor.activity.adapter.NewInvestmentAdapter;
import id.halalvestor.activity.adapter.NewInvestmentGroupAdapter;
import id.halalvestor.activity.main.investment.buysell.SellDetailTransactionRevActivity;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.EvaluationInvestment;
import id.halalvestor.model.InvestmentGoal;
import id.halalvestor.model.InvestmentProduct;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;
import id.halalvestor.util.IRDialogUtils;

public class PortfolioInvestmentActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_sort)
    ImageView ivSort;
    IRDialogUtils irDialogUtils;
    TextView tvTotalInvestment;
    TextView tvProgress;
    ProgressBar progressBar;

    int[] scrollableColumnWidths = new int[]{25, 16, 15, 15, 12, 17};
    int[] scrollableColumnPixels = new int[]{25, 16, 15, 15, 12, 17};
    TableLayout fixedColumn1;
    TableLayout fixedColumn2;
    TableLayout scrollablePart;
    @BindView(R.id.ll_evaluasi_invest)
    LinearLayout llEvaluasiInvest;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_search_toolbar)
    ImageView ivSearchToolbar;
    @BindView(R.id.iv_sort_toolbar)
    ImageView ivSortToolbar;
    @BindView(R.id.iv_indicator_left)
    ImageView ivIndicatorLeft;
    @BindView(R.id.iv_indicator_1)
    ImageView ivIndicator1;
    @BindView(R.id.iv_indicator_2)
    ImageView ivIndicator2;
    @BindView(R.id.iv_indicator_3)
    ImageView ivIndicator3;
    @BindView(R.id.iv_indicator_4)
    ImageView ivIndicator4;
    @BindView(R.id.iv_indicator_5)
    ImageView ivIndicator5;
    @BindView(R.id.iv_indicator_right)
    ImageView ivIndicatorRight;
    @BindView(R.id.hs_table_item)
    HorizontalScrollView hsTableItem;
    @BindView(R.id.ll_add_porto)
    LinearLayout llAddPorto;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private DecimalFormat currencyFormat = new DecimalFormat("Rp ###,###,###");

    @BindView(R.id.tv_portfolio_growth)
    TextView tvGrowth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        ButterKnife.bind(this);

        tvTitle.setText("PORTFOLIO INVESTASI");

        irDialogUtils = new IRDialogUtils(PortfolioInvestmentActivity.this);
        ivSearchToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = irDialogUtils.showDialog("Temukan Produk", R.layout.dialog_search_product, true);
            }
        });

        ivSortToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = irDialogUtils.showDialog("Urut Berdasarkan", R.layout.dialog_sort_product, true);
                ButterKnife.bind(dialog);

                RadioButton rbNamaAsc = (RadioButton) dialog.findViewById(R.id.rb_nama_asc);
                RadioButton rbNamaDesc = (RadioButton) dialog.findViewById(R.id.rb_nama_desc);
                RadioButton rbJenis = (RadioButton) dialog.findViewById(R.id.rb_jenis);
                final RadioButton rbType = (RadioButton) dialog.findViewById(R.id.rb_type);
                LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.ll_jenis);
                final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.rg_pilih);
                Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_waktu);

                // spineer collectdata
                ArrayAdapter<String> adapter;
                List<String> objects = new ArrayList<String>();
                objects.add("YTD");
                objects.add("1 Tahun");
                objects.add("3 Tahun");
                objects.add("5 Tahun");

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, objects);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        radioGroup.clearCheck();
                        rbType.setChecked(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (radioGroup.getCheckedRadioButtonId() == rbType.getId()) {
                            rbType.setChecked(true);
                        } else {
                            rbType.setChecked(false);
                        }
                    }
                });

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        if (checkedId == rbType.getId()) {
                            rbType.setChecked(true);
                        } else {
                            rbType.setChecked(false);
                        }
                    }
                });

                rbType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            radioGroup.clearCheck();
                            rbType.setChecked(true);
                        }
                    }
                });
            }
        });

        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        fixedColumn1 = findViewById(R.id.fixed_column1);
        fixedColumn2 = findViewById(R.id.fixed_column2);
        //rest of the table (within a scroll view)
        scrollablePart = findViewById(R.id.scrollable_part);

        AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();

        TextView tvMonthly = findViewById(R.id.tv_monthly_investment);
        tvMonthly.setText("Investasi per bulan : " + currencyFormat.format(appUser.getMonthlyInstallment()));

        tvProgress = findViewById(R.id.tv_progress);
        progressBar = findViewById(R.id.progressBar);

        TextView tvLastUpdateInvestment = findViewById(R.id.tv_last_update_investment);
        tvLastUpdateInvestment.setText("per tanggal " + sdf.format(new Date()));

        tvTotalInvestment = findViewById(R.id.tv_total_investment);

        TextView tvProgress = findViewById(R.id.tv_investment_progress);
        tvProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfile();
            }
        });

        ImageView ivProgress = findViewById(R.id.iv_investment_progress);
        ivProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfile();
            }
        });
        render();

        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        ivIndicatorRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scrollX = hsTableItem.getScrollX();
                if (scrollX <= scrollableColumnPixels[1]) {
                    hsTableItem.scrollTo(scrollableColumnPixels[2] + 1, hsTableItem.getScrollY());
                } else if (scrollX > scrollableColumnPixels[1] && scrollX <= scrollableColumnPixels[2]) {
                    hsTableItem.scrollTo(scrollableColumnPixels[3] + 1, hsTableItem.getScrollY());
                } else if (scrollX > scrollableColumnPixels[2]) {
                    hsTableItem.scrollTo(scrollableColumnPixels[4] + 1, hsTableItem.getScrollY());
                }

            }
        });

        ivIndicatorLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scrollX = hsTableItem.getScrollX();
                if (scrollX < scrollableColumnPixels[2]) {
                    hsTableItem.scrollTo(scrollableColumnPixels[0] + 1, hsTableItem.getScrollY());
                } else if (scrollX >= scrollableColumnPixels[2] && scrollX < scrollableColumnPixels[3]) {
                    hsTableItem.scrollTo(scrollableColumnPixels[1] + 1, hsTableItem.getScrollY());
                } else if (scrollX >= scrollableColumnPixels[3]) {
                    hsTableItem.scrollTo(scrollableColumnPixels[2] + 1, hsTableItem.getScrollY());
                }
            }
        });

        hsTableItem.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // max scroll x count 629 / 5 = 125.8
                int scrollX = hsTableItem.getScrollX(); // For HorizontalScrollView
                if (scrollX <= scrollableColumnPixels[1]) {
                    ivIndicator1.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator2.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator3.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator4.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator5.setImageResource(R.drawable.ellipse_primary_light);
                }
                if (scrollX > scrollableColumnPixels[1] && scrollX <= scrollableColumnPixels[2]) {
                    ivIndicator1.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator2.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator3.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator4.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator5.setImageResource(R.drawable.ellipse_primary_light);
                }
                if (scrollX > scrollableColumnPixels[2] && scrollX <= scrollableColumnPixels[3]) {
                    ivIndicator1.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator2.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator3.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator4.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator5.setImageResource(R.drawable.ellipse_primary_light);

                }
                if (scrollX > scrollableColumnPixels[3] && scrollX <= scrollableColumnPixels[4]) {
                    ivIndicator1.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator2.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator3.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator4.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator5.setImageResource(R.drawable.ellipse_primary_dark);

                }
            }
        });

    }

    private void close() {
        this.finish();
    }

    private void showProfile() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragment", "profile");
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    private void render() {
        AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
        tvTotalInvestment.setText(currencyFormat.format(appUser.getTotalInvestment()));
        double fv = 0;
        InvestmentGoal displayedGoal = null;
        List<InvestmentGoal> newList = new ArrayList<>();
        for (int i = 0; i < appUser.getInvestmentGoals().size(); i++) {
            InvestmentGoal goal = appUser.getInvestmentGoals().get(i);
            fv = fv + goal.getFv();

            if (appUser.getTotalInvestment() > fv) {
                goal.setAchieved(true);
                goal.setProgress(100);
            }
            if (i > 0) {
                goal.setPreviousGoalAchieved(newList.get(i - 1).isAchieved());
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
            newList.add(goal);
        }
        if (displayedGoal == null) {
            displayedGoal = newList.get(0);
        }

        tvProgress.setText(displayedGoal.getProgress() + "%");
        progressBar.setProgress(displayedGoal.getProgress());

//        if (isAdded()) {
        try {
            fetchPortfolios();
        } catch (AuthFailureError err) {
        }
//        }
        Double w = getResources().getDisplayMetrics().widthPixels * 1.3;
        int screenWidth = w.intValue();

        int temp = 0;
        scrollableColumnPixels[0] = 0;
        for (int i = 1; i < scrollableColumnWidths.length; i++) {
            temp = temp + scrollableColumnWidths[i] * screenWidth / 100;
            scrollableColumnPixels[i] = temp;
        }

        llAddPorto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectServerApi("get_investment_types_and_products", null);
            }
        });
    }

    Dialog managementDialog;

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        if (requestHeader.equalsIgnoreCase("get_investment_types_and_products")) {
            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
            Map<String, List<InvestmentProduct>> map = gson.fromJson(result, new TypeToken<Map<String, List<InvestmentProduct>>>() { }.getType());
            managementDialog = irDialogUtils.showDialog("Tambahkan Portofolio Investasimu", R.layout.dialog_add_investment, true);
            managementDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            List<String> objects = new ArrayList<String>();
            for (String str : map.keySet()) {
                objects.add(str);
            }
            NewInvestmentGroupAdapter adapter = new NewInvestmentGroupAdapter(getApplicationContext(), objects, map, new Listener());
            RecyclerView rv1 = managementDialog.findViewById(R.id.rv1);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rv1.setLayoutManager(layoutManager);
            rv1.setAdapter(adapter);

            this.next = managementDialog.findViewById(R.id.btn_next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> params = new HashMap<>();
                    params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    params.put("list", gson.toJson(products));
                    Log.d(TAG, "products:" + products);
                    boolean passed = true;
                    for (Integer key : products.keySet()) {
                        InvestmentProduct product = products.get(key);
                        if (product.getAmount() == null) {
                            passed = false;
                            showErrorMessage(next, "Harga beli produk " + product.getProductName() + " tidak boleh kosong");
                            break;
                        }
                        if (product.getBoughtNab() == null) {
                            passed = false;
                            showErrorMessage(next, "NAB produk " + product.getProductName() + " tidak boleh kosong");
                            break;
                        }
                    }
                    if (passed) {
                        connectServerApi("buy_investment_portfolio", params);
                    }
                }
            });
        }else if (requestHeader.equalsIgnoreCase("buy_investment_portfolio")) {
            super.onSuccessfullApiConnect(requestHeader, result);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().setPrettyPrinting().create();
            AppUser appUser = gson.fromJson(result, AppUser.class);
            App.getInstance().getPreferenceManager().setAppUser(appUser);
            if (managementDialog!=null && managementDialog.isShowing()){
                managementDialog.cancel();
            }
            render();
        }
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        if (managementDialog!=null && managementDialog.isShowing()){
            managementDialog.cancel();
        }
        showErrorMessage(llAddPorto, error);
    }

    Button next;

    private TextView makeTextView(String text, int color, int widthInPercentOfScreenWidth, int height) {
        Log.d(TAG, "height tv:"+height);
        Double w = getResources().getDisplayMetrics().widthPixels * 1.4;
        int screenWidth = w.intValue();
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        textView.setPadding(5, 5, 5, 5);
        textView.setTextSize(14);
        textView.setLines(2);
        textView.setHeight(height);
        textView.setGravity(Gravity.CENTER);
        textView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        textView.setTextColor(color);
        return textView;
    }

    private ImageView makeImageView(float alpha, String tag, int height) {
        Log.d(TAG, "height iv:"+height);
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setTag(tag);
        imageView.setPadding(5, 5, 5, 5);
        imageView.setImageResource(R.drawable.ic_shop);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(false);
        imageView.setAlpha(alpha);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(height/2, height);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    public int getHeight(String text, int widthInPercentOfScreenWidth) {
        int deviceWidth = 1080;
        if (getApplicationContext() != null) {
            WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Point size = new Point();
                display.getSize(size);
                deviceWidth = size.x;
            } else {
                deviceWidth = display.getWidth();
            }
        }
        deviceWidth = (widthInPercentOfScreenWidth * deviceWidth / 100);
        Double w = deviceWidth * 1.4;
        deviceWidth = w.intValue();
        return getHeight(getApplicationContext(), text, deviceWidth);
    }

    static int getHeight(Context context, CharSequence text, int deviceWidth) {
        if (context != null) {
            TextView textView = new TextView(context);
            textView.setPadding(8, 8, 8, 8);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setText(text);
            textView.setTextSize(14);
            textView.setMaxLines(3);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            textView.measure(widthMeasureSpec, heightMeasureSpec);
            return textView.getMeasuredHeight();
        }
        return 160;
    }

    private static final String TAG = ProfileFragment.class.toString();
    List<InvestmentProduct> list;

    private void fetchPortfolios() throws AuthFailureError {

        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                list = gson.fromJson(response, new TypeToken<List<InvestmentProduct>>() {}.getType());

                if (list != null) {
                    fixedColumn1.removeAllViews();
                    fixedColumn2.removeAllViews();
                    scrollablePart.removeAllViews();

                    TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                    int height = getHeight("12345678901234567890123456789012", 16);

                    TextView fixedView = makeTextView("Nama Produk", Color.BLACK, scrollableColumnWidths[0], height);
                    fixedColumn1.addView(fixedView);
                    TableRow row = new TableRow(getApplicationContext());
                    row.setLayoutParams(wrapWrapTableRowParams);

                    row.setGravity(Gravity.CENTER);
                    row.addView(makeTextView("Jumlah", Color.BLACK, scrollableColumnWidths[1], height));
                    row.addView(makeTextView("NAB/unit\n(saat beli)", Color.BLACK, scrollableColumnWidths[2], height));
                    row.addView(makeTextView("NAB/unit\n(saat ini)", Color.BLACK, scrollableColumnWidths[3], height));
                    row.addView(makeTextView("Growth", Color.BLACK, scrollableColumnWidths[4], height));
                    row.addView(makeTextView("Nilai\nInvestasi", Color.BLACK, scrollableColumnWidths[5], height));
                    scrollablePart.addView(row);

                    fixedColumn2.addView(makeImageView(0f, "", height));

                    Double jumlahPembelian = 0d;
                    Double nilaiInvestasi = 0d;

                    for (int i = 0; i < list.size(); i++) {
                        final InvestmentProduct model = list.get(i);
                        //int height = getHeight(model.getProductName(), scrollableColumnWidths[0]);
                        //int height2 = getHeight(model.getProductType(), scrollableColumnWidths[2]);
                        //if (height2 > height) {
                        //    height = height2;
                        //}

                        Double pembelian = model.getUnit()*model.getBoughtNab();
                        jumlahPembelian = jumlahPembelian+pembelian;
                        nilaiInvestasi = nilaiInvestasi+model.getUnit()*model.getNab();
                        Double growth = ((model.getNab()/model.getBoughtNab())-1)*100;

                        fixedView = makeTextView(model.getProductName(), getResources().getColor(R.color.colorPrimary), scrollableColumnWidths[0], height);
                        fixedColumn1.addView(fixedView);

                        row = new TableRow(getApplicationContext());
                        row.setLayoutParams(wrapWrapTableRowParams);
                        row.setGravity(Gravity.CENTER);
                        row.addView(makeTextView(decimalFormat.format(pembelian), getResources().getColor(R.color.greyDark), scrollableColumnWidths[1], height));
                        row.addView(makeTextView(decimalFormat.format(model.getBoughtNab()), getResources().getColor(R.color.greyDark), scrollableColumnWidths[2], height));
                        row.addView(makeTextView(decimalFormat.format(model.getNab()), getResources().getColor(R.color.greyDark), scrollableColumnWidths[3], height));
                        row.addView(makeTextView(growth.intValue() + "%", getResources().getColor(R.color.green), scrollableColumnWidths[4], height));
                        row.addView(makeTextView(decimalFormat.format(model.getAmount()) + "", getResources().getColor(R.color.greyDark), scrollableColumnWidths[5], height));

                        ImageView imageView = makeImageView(1f, "", height);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                                //intent.putExtra("product", model);
                                //startActivity(intent);
                                Intent intent = new Intent(getApplicationContext(), SellDetailTransactionRevActivity.class);
                                intent.putExtra("product", model);
                                //startActivity(intent);
                                startActivity(intent);
                            }
                        });

                        fixedColumn2.addView(imageView);

                        scrollablePart.addView(row);
                    }
                    tvTotalInvestment.setText(currencyFormat.format(nilaiInvestasi));
                    Double totalGrowth = ((nilaiInvestasi/jumlahPembelian)-1)*100;
                    tvGrowth.setText("Total pertumbuhan investasi : "+totalGrowth.intValue()+"%");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() {
                return HttpClientRequest.getHeaders("get_portfolios");
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                return Helper.translateParameters(params, false);
            }
        };

        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);


        Log.d("url", strReq.getUrl() + "/");

        //Adding request to request queue
        App.getInstance().addToRequestQueue(strReq);
    }

    @OnClick(R.id.ll_evaluasi_invest)
    public void onViewClicked() {
        Dialog dialog = irDialogUtils.showDialog("Produk Anda", R.layout.dialog_detail_investment, true);

        List<EvaluationInvestment> evaluations = new ArrayList<>();
        if (list!=null && list.size()>0) {
            for (InvestmentProduct product : list) {
                EvaluationInvestment evaluationInvestment = new EvaluationInvestment();
                evaluationInvestment.setId(product.getId());
                evaluationInvestment.setName(product.getProductName());
                if (product.getGrowth() >= product.getGoodRecommendation()) {
                    evaluationInvestment.setState(1);
                } else if (product.getGrowth() <= product.getCarefulRecommendation()) {
                    evaluationInvestment.setState(3);
                } else {
                    evaluationInvestment.setState(2);
                }
                evaluations.add(evaluationInvestment);
            }
        }
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
        EvaluationInvestmentDetailAdapter adapter = new EvaluationInvestmentDetailAdapter(getApplicationContext(), evaluations, new EvaluationInvestmentDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EvaluationInvestment model) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private Map<Integer, InvestmentProduct> products = new TreeMap<>();

    class Listener implements NewInvestmentAdapter.OnItemCheckListener {

        @Override
        public void onItemCheck(InvestmentProduct model, boolean isChecked) {
            if (isChecked) {
                products.put(model.getId(), model);
            } else {
                products.remove(model.getId());
            }
            String text = "Tambahkan";
            if (products.size() > 0) {
                next.setText(text + " (" + products.size() + ")");
            }
        }

        @Override
        public void onTextChange(InvestmentProduct model, String propertyName, String propertyValue) {
            try {
                InvestmentProduct product = products.get(model.getId());
                if (propertyName.equalsIgnoreCase("hargaBeli")) {
                    if (propertyValue.toString().contains("Rp")) {
                        product.setAmount(currencyFormat.parse(propertyValue).doubleValue());
                    } else if (propertyValue.contains("\\.")) {
                        product.setAmount(decimalFormat.parse(propertyValue).doubleValue());
                    } else if (propertyValue.matches("\\d+")) {
                        product.setAmount(Double.parseDouble(propertyValue));
                    } else if (propertyValue.equalsIgnoreCase("")){
                        product.setAmount(null);
                    }
                } else if (propertyName.equalsIgnoreCase("nab")) {
                    if (propertyValue.toString().contains("Rp")) {
                        product.setBoughtNab(currencyFormat.parse(propertyValue).doubleValue());
                    } else if (propertyValue.contains("\\.")) {
                        product.setBoughtNab(decimalFormat.parse(propertyValue).doubleValue());
                    } else if (propertyValue.matches("\\d+")) {
                        product.setBoughtNab(Double.parseDouble(propertyValue));
                    } else if (propertyValue.equalsIgnoreCase("")){
                        product.setBoughtNab(null);
                    }
                }
                products.put(model.getId(), product);
            } catch (Exception ex) {
                Log.e(TAG, "error", ex);
            }
        }
    }
}
