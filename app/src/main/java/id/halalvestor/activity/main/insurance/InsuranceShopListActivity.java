package id.halalvestor.activity.main.insurance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.InsuranceProductShopAdapter;
import id.halalvestor.activity.adapter.InsuranceRecomendedProductAdapter;
import id.halalvestor.model.InsuranceProduct;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;
import id.halalvestor.util.IRDialogUtils;

public class InsuranceShopListActivity extends AppCompatActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    @BindView(R.id.iv_sort_toolbar)
    ImageView ivSortToolbar;
    @BindView(R.id.iv_search_toolbar)
    ImageView ivSearchToolbar;
    @BindView(R.id.expandable_list)
    ExpandableListView expandableList;
    InsuranceRecomendedProductAdapter recomendedProductAdapter;
    List<InsuranceProduct> listDataHeader = new ArrayList<>();
    int lastExpandedPosition = -1;
    @BindView(R.id.ll_recomend_nav)
    LinearLayout llRecomendNav;
    boolean reoomendedIsGone = true;
    //@BindView(R.id.ll_recomended_show_full)
    //LinearLayout llRecomendedShowFull;
    @BindView(R.id.tv_choose)
    TextView tvChoose;

    IRDialogUtils irDialogUtils;
    List<InsuranceProduct> list = new ArrayList<>();
    InsuranceProductShopAdapter insuranceProductShopAdapter;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_shop);
        ButterKnife.bind(this);
        irDialogUtils = new IRDialogUtils(this);
        tvTitle.setText("ASURANSI KESEHATAN");

        insuranceProductShopAdapter = new InsuranceProductShopAdapter(list, new InsuranceProductShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InsuranceProduct model) {
                new FinestWebView.Builder(InsuranceShopListActivity.this).show(model.getLink());
//                Intent intent = new Intent(getApplicationContext(), InsuranceShopDetailActivity.class);
//                intent.putExtra("product", model);
//                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvMain.setLayoutManager(layoutManager);
        rvMain.setAdapter(insuranceProductShopAdapter);

        ivSearchToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new IRDialogUtils(InsuranceShopListActivity.this).showDialog("Temukan Produk", R.layout.dialog_portfolio_insurance, true);
            }
        });

        ivSortToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new IRDialogUtils(InsuranceShopListActivity.this).showDialog("Urut Berdasarkan", R.layout.dialog_sort_product, true);
                ButterKnife.bind(dialog);
            }
        });

        //prepareListData();

        recomendedProductAdapter = new InsuranceRecomendedProductAdapter(this, listDataHeader);
        expandableList.setAdapter(recomendedProductAdapter);
        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        expandableList.setVisibility(View.GONE);

        Intent intent = getIntent(); // gets the previously created intent
        final int productType = intent.getIntExtra("product_type", 0);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    fetchInsuranceProducts(productType);
                }catch(AuthFailureError err){

                }
            }
        });


        try{
            fetchInsuranceProducts(productType);
        }catch(AuthFailureError err){

        }
    }

    @OnClick({R.id.iv_finish, R.id.ll_recomend_nav})//, R.id.ll_recomended_show_full})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_recomend_nav:
                if (reoomendedIsGone) {
                    tvChoose.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    llRecomendNav.setBackgroundResource(R.drawable.round_background_white_shadow);
                    llRecomendNav.setPadding(12,12,12,12);
                    expandableList.setVisibility(View.VISIBLE);
                    //llRecomendedShowFull.setVisibility(View.VISIBLE);
                    reoomendedIsGone = false;
                } else {
                    tvChoose.setTextColor(getResources().getColor(R.color.white));
                    llRecomendNav.setBackgroundResource(R.drawable.background_border_white);
                    llRecomendNav.setPadding(12,12,12,12);
                    expandableList.setVisibility(View.GONE);
                    //llRecomendedShowFull.setVisibility(View.GONE);
                    reoomendedIsGone = true;
                }
                break;
            case R.id.ll_recomended_show_full:
                Dialog dialog = irDialogUtils.showDialog("Produk Rekomendasi", R.layout.dialog_recomended_product, true);

                ExpandableListView expandableListView = (ExpandableListView) dialog.findViewById(R.id.expandable_list);

                InsuranceRecomendedProductAdapter adapter = new InsuranceRecomendedProductAdapter(this, listDataHeader);
                expandableListView.setAdapter(adapter);

                //expandableListView.expandGroup(0);
                //expandableListView.expandGroup(1);
                //expandableListView.expandGroup(2);
                //expandableListView.expandGroup(3);

                dialog.show();
                break;
            case R.id.iv_finish:
                finish();
        }
    }

    private static final String TAG = InsuranceShopListActivity.class.toString();

    private void fetchInsuranceProducts(final int productType) throws AuthFailureError {
        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                swipeLayout.setRefreshing(false);
                listDataHeader.clear();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                List<InsuranceProduct> newList = gson.fromJson(response, new TypeToken<List<InsuranceProduct>>() {}.getType());
                list.clear();
                if(newList!=null) {
                    for (InsuranceProduct p : newList) {
                        list.add(p);
                        if (p.isRecommended()){
                            listDataHeader.add(p);
                        }
                    }
                }
                recomendedProductAdapter.notifyDataSetChanged();
                insuranceProductShopAdapter.notifyDataSetChanged();
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
                return HttpClientRequest.getHeaders("get_insurance_products");
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                params.put("product_type", String.valueOf(productType));
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
}
