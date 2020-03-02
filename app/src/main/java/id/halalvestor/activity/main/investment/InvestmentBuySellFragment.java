package id.halalvestor.activity.main.investment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.thefinestartist.finestwebview.FinestWebView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.activity.adapter.RecomendedProductAdapter;
import id.halalvestor.activity.main.BaseFragment;
import id.halalvestor.activity.main.BuySellFragment;
import id.halalvestor.model.InvestmentProduct;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;
import id.halalvestor.util.IRDialogUtils;

public class InvestmentBuySellFragment extends BaseFragment {

    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_sort)
    ImageView ivSort;
    IRDialogUtils irDialogUtils;

    int[] scrollableColumnWidths = new int[]{25, 15, 22, 15, 23};
    int[] scrollableColumnPixels = new int[]{25, 15, 22, 15, 23};

    TableLayout fixedColumn1;
    TableLayout fixedColumn2;
    TableLayout scrollablePart;
    @BindView(R.id.expandable_list)
    ExpandableListView expandableList;
    RecomendedProductAdapter recomendedProductAdapter;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<InvestmentProduct>> listDataChild = new HashMap<>();
    int lastExpandedPosition = -1;
    @BindView(R.id.ll_recomend_nav)
    LinearLayout llRecomendNav;
    boolean reoomendedIsGone = true;
    @BindView(R.id.ll_recomended_show_full)
    LinearLayout llRecomendedShowFull;
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
    @BindView(R.id.iv_indicator_right)
    ImageView ivIndicatorRight;
    @BindView(R.id.hs_table_item)
    HorizontalScrollView hsTableItem;
    SwipeRefreshLayout swipeLayout;
    private Map<String,List<InvestmentProduct>> recommendedProducts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity().getSystemService(Context.WINDOW_SERVICE) == null) {
            ((MainActivity) getActivity()).showHome();
        } else {
            View view = inflater.inflate(R.layout.buysell_investment_layout, container, false);
            ButterKnife.bind(this, view);

            irDialogUtils = new IRDialogUtils(getContext());
            ivSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = irDialogUtils.showDialog("Temukan Produk", R.layout.dialog_search_product, true);
                }
            });

            ivSort.setOnClickListener(new View.OnClickListener() {
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
                    List<String> list;

                    list = new ArrayList<String>();
                    list.add("YTD");
                    list.add("1 Tahun");
                    list.add("3 Tahun");
                    list.add("5 Tahun");

                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
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

            fixedColumn1 = (TableLayout) view.findViewById(R.id.fixed_column1);
            fixedColumn2 = (TableLayout) view.findViewById(R.id.fixed_column2);
            scrollablePart = (TableLayout) view.findViewById(R.id.scrollable_part);

            swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try{
                        fetchInvestmentProducts();
                    }catch(AuthFailureError err){

                    }
                }
            });

            try {
                fetchInvestmentProducts();
            } catch (AuthFailureError err) {
            }


            Double w = getResources().getDisplayMetrics().widthPixels * 1.3;
            int screenWidth = w.intValue();

            int temp = 0;
            scrollableColumnPixels[0] = 0;
            for (int i=1;i<scrollableColumnWidths.length;i++){
                temp = temp + scrollableColumnWidths[i] * screenWidth / 100;
                scrollableColumnPixels[i] = temp;
            }

            recomendedProductAdapter = new RecomendedProductAdapter(getContext(), listDataHeader, listDataChild);
            expandableList.setAdapter(recomendedProductAdapter);
            expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                        expandableList.collapseGroup(lastExpandedPosition);
                    }
                    lastExpandedPosition = groupPosition;
                }
            });

            expandableList.setVisibility(View.GONE);

            ivIndicatorRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivIndicator1.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator2.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator3.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator4.setImageResource(R.drawable.ellipse_primary_dark);
                    hsTableItem.scrollTo(scrollableColumnPixels[3], hsTableItem.getScrollY());
                }
            });

            ivIndicatorLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivIndicator1.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator2.setImageResource(R.drawable.ellipse_primary_dark);
                    ivIndicator3.setImageResource(R.drawable.ellipse_primary_light);
                    ivIndicator4.setImageResource(R.drawable.ellipse_primary_light);
                    hsTableItem.scrollTo(scrollableColumnPixels[0], hsTableItem.getScrollY());
                }
            });

            hsTableItem.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollX = hsTableItem.getScrollX();

                    if(scrollX > scrollableColumnPixels[0] && scrollX <= scrollableColumnPixels[1]){
                        ivIndicator1.setImageResource(R.drawable.ellipse_primary_dark);
                        ivIndicator2.setImageResource(R.drawable.ellipse_primary_dark);
                        ivIndicator3.setImageResource(R.drawable.ellipse_primary_light);
                        ivIndicator4.setImageResource(R.drawable.ellipse_primary_light);
                    }
                    if(scrollX > scrollableColumnPixels[1] && scrollX <= scrollableColumnPixels[2]){
                        ivIndicator1.setImageResource(R.drawable.ellipse_primary_light);
                        ivIndicator2.setImageResource(R.drawable.ellipse_primary_dark);
                        ivIndicator3.setImageResource(R.drawable.ellipse_primary_dark);
                        ivIndicator4.setImageResource(R.drawable.ellipse_primary_light);
                    }
                    if(scrollX > scrollableColumnPixels[2] && scrollX <= scrollableColumnPixels[3]){
                        ivIndicator1.setImageResource(R.drawable.ellipse_primary_light);
                        ivIndicator2.setImageResource(R.drawable.ellipse_primary_light);
                        ivIndicator3.setImageResource(R.drawable.ellipse_primary_dark);
                        ivIndicator4.setImageResource(R.drawable.ellipse_primary_dark);

                    }

                }
            });
            return view;
        }
        return null;
    }

    private void prepareListData() {
        //listDataHeader = new ArrayList<String>();
        //listDataChild = new HashMap<String, List<InvestmentProduct>>();
        Log.d(TAG, "recommendedProducts:"+recommendedProducts);
        listDataHeader.clear();
        listDataChild.clear();
        // Adding child data
        for (String productType : recommendedProducts.keySet()){
            listDataHeader.add(productType);
            listDataChild.put(productType, recommendedProducts.get(productType));
        }
        recomendedProductAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            fetchInvestmentProducts();
        } catch (AuthFailureError err) {
        }
    }


    private TextView makeTextView(String text, int color, int widthInPercentOfScreenWidth, int height) {
        //if (isAdded())
        Double w = getResources().getDisplayMetrics().widthPixels * 1.3;
        int screenWidth = w.intValue();
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setPadding(5, 5, 5, 5);
        textView.setTextSize(14);
        textView.setLines(2);
        textView.setHeight(height);
        textView.setGravity(Gravity.CENTER);
        int width = widthInPercentOfScreenWidth * screenWidth / 100;
        textView.setWidth(width);
        textView.setTextColor(color);
        return textView;
    }

    private static final String TAG = BuySellFragment.class.toString();

    private ImageView makeImageView(float alpha, String tag, int height) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setTag(tag);
        imageView.setPadding(5, 5, 5, 5);
        imageView.setImageResource(R.drawable.ic_shop);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(false);
        imageView.setAlpha(alpha);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(height / 2, height);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    public int getHeight(String text, int widthInPercentOfScreenWidth) {
        int deviceWidth = 1080;
        if (getActivity() != null) {
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
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
        Double w = deviceWidth * 1.3;
        deviceWidth = w.intValue();
        return getHeight(getContext(), text, deviceWidth);
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

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    private void fetchInvestmentProducts() throws AuthFailureError {
        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                swipeLayout.setRefreshing(false);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                List<InvestmentProduct> list = gson.fromJson(response, new TypeToken<List<InvestmentProduct>>() { }.getType());
                recommendedProducts = new TreeMap<>();

                if (list != null) {
                    fixedColumn1.removeAllViews();
                    fixedColumn2.removeAllViews();
                    scrollablePart.removeAllViews();

                    TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                    int height = getHeight("12345678901234567890123456789012", 16);

                    TextView fixedView = makeTextView("Nama Produk", Color.BLACK, scrollableColumnWidths[0], height);
                    fixedColumn1.addView(fixedView);

                    TableRow row = new TableRow(getActivity());
                    row.setLayoutParams(wrapWrapTableRowParams);
                    row.setGravity(Gravity.CENTER);
                    row.addView(makeTextView("NAB/unit\n(saat ini)", Color.BLACK, scrollableColumnWidths[1], height));
                    row.addView(makeTextView("Tipe Reksadana", Color.BLACK, scrollableColumnWidths[2], height));
                    row.addView(makeTextView("Growth", Color.BLACK, scrollableColumnWidths[3], height));
                    row.addView(makeTextView("Nilai\nInvestasi", Color.BLACK, scrollableColumnWidths[4], height));

                    scrollablePart.addView(row);

                    fixedColumn2.addView(makeImageView(0f, "", height));

                    for (int i = 0; i < list.size(); i++) {
                        final InvestmentProduct model = list.get(i);
                        //int height = getHeight(model.getProductName(), scrollableColumnWidths[0]);
                        //int height2 = getHeight(model.getProductType(), scrollableColumnWidths[2]);
                        //if (height2 > height) {
                        //    height = height2;
                        //}

                        if (model.isRecommended()){
                            List<InvestmentProduct> products;
                            if (recommendedProducts.containsKey(model.getProductType())){
                                products = recommendedProducts.get(model.getProductType());
                                if (products==null){
                                    products = new ArrayList<>();
                                }
                                products.add(model);
                            }else{
                                products = new ArrayList<>();
                                products.add(model);
                            }
                            recommendedProducts.put(model.getProductType(), products);
                        }

                        fixedView = makeTextView(model.getProductName(), getResources().getColor(R.color.colorPrimary), scrollableColumnWidths[0], height);
                        fixedColumn1.addView(fixedView);

                        row = new TableRow(getActivity());
                        row.setLayoutParams(wrapWrapTableRowParams);
                        row.setGravity(Gravity.CENTER);
                        row.addView(makeTextView(decimalFormat.format(model.getNab()), getResources().getColor(R.color.greyDark), scrollableColumnWidths[1], height));
                        row.addView(makeTextView(model.getProductType(), getResources().getColor(R.color.greyDark), scrollableColumnWidths[2], height));
                        row.addView(makeTextView(model.getGrowth().intValue() + "%", getResources().getColor(R.color.green), scrollableColumnWidths[3], height));
                        row.addView(makeTextView(decimalFormat.format(model.getAmount()) + "", getResources().getColor(R.color.greyDark), scrollableColumnWidths[4], height));

                        ImageView imageView = makeImageView(1f, "", height);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new FinestWebView.Builder(getActivity()).show(model.getLink());

//                                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
//                                intent.putExtra("product", model);
//                                startActivity(intent);
                            }
                        });

                        fixedColumn2.addView(imageView);
                        scrollablePart.addView(row);
                    }
                }
                prepareListData();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                swipeLayout.setRefreshing(false);
                NetworkResponse networkResponse = error.networkResponse;
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                return HttpClientRequest.getHeaders("get_investment_products");
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

    @OnClick({R.id.ll_recomend_nav, R.id.ll_recomended_show_full})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_recomend_nav:
                if (reoomendedIsGone) {
                    expandableList.setVisibility(View.VISIBLE);
                    llRecomendedShowFull.setVisibility(View.VISIBLE);
                    reoomendedIsGone = false;
                } else {
                    expandableList.setVisibility(View.GONE);
                    llRecomendedShowFull.setVisibility(View.GONE);
                    reoomendedIsGone = true;
                }
                break;
            case R.id.ll_recomended_show_full:
                Dialog dialog = irDialogUtils.showDialog("Produk Rekomendasi", R.layout.dialog_recomended_product, true);

                ExpandableListView expandableListView = (ExpandableListView) dialog.findViewById(R.id.expandable_list);

                RecomendedProductAdapter adapter = new RecomendedProductAdapter(getContext(), listDataHeader, listDataChild);
                expandableListView.setAdapter(adapter);

                for (int i=0;i<listDataChild.size();i++) {
                    expandableListView.expandGroup(i);
                }

                dialog.show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}