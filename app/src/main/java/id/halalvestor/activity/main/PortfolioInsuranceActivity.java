package id.halalvestor.activity.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.adapter.InsurancePortfolioAdapter;
import id.halalvestor.activity.adapter.NewInsuranceAdapter;
import id.halalvestor.activity.adapter.NewInsuranceGroupAdapter;
import id.halalvestor.model.InsuranceProduct;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;
import id.halalvestor.util.IRDialogUtils;

public class PortfolioInsuranceActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    @BindView(R.id.iv_search_toolbar)
    ImageView ivSearchToolbar;
    @BindView(R.id.iv_sort_toolbar)
    ImageView ivSortToolbar;

    IRDialogUtils irDialogUtils;
    @BindView(R.id.ll_add_porto)
    LinearLayout llAddPorto;

    InsurancePortfolioAdapter portofolioAsuranceAdapter;
    List<InsuranceProduct> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_asuransi);
        ButterKnife.bind(this);

        tvTitle.setText("PORTFOLIO ASURANSI");

        irDialogUtils = new IRDialogUtils(PortfolioInsuranceActivity.this);

        list = new ArrayList<>();

        portofolioAsuranceAdapter = new InsurancePortfolioAdapter(list, new InsurancePortfolioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InsuranceProduct model) {

            }
        }, new InsurancePortfolioAdapter.OnItemDeleteClickListener() {
            @Override
            public void onItemDeleteClick(final InsuranceProduct model) {
                managementDialog = irDialogUtils.showDialogNoTitle(null, R.layout.dialog_delete_insurance, false);

                final LinearLayout llDelete = managementDialog.findViewById(R.id.ll_delete);
                final LinearLayout llSuccess = managementDialog.findViewById(R.id.ll_success);
                final LinearLayout llDefault = managementDialog.findViewById(R.id.ll_default);

                TextView tvYes = managementDialog.findViewById(R.id.tv_yes);
                TextView tvNo = managementDialog.findViewById(R.id.tv_no);
                TextView tvBack = managementDialog.findViewById(R.id.tv_back);

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        managementDialog.dismiss();
                    }
                });

                tvBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        managementDialog.dismiss();
                    }
                });

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llDefault.setVisibility(View.GONE);
                        //llDelete.setVisibility(View.VISIBLE);

                        Map<String,String> params = new HashMap<>();
                        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        params.put("id", String.valueOf(model.getPortfolio()));
                        connectServerApi("del_insurance_portfolio", params);

                        /**new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            llDelete.setVisibility(View.GONE);
                                            llSuccess.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        }).start();*/
                    }
                });
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvMain.setLayoutManager(layoutManager);
        rvMain.setAdapter(portofolioAsuranceAdapter);

        ivSearchToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = irDialogUtils.showDialog("Temukan Produk", R.layout.dialog_search_product, true);
            }
        });

        ivSortToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = irDialogUtils.showDialog("Urut Berdasarkan", R.layout.dialog_portfolio_insurance, true);
                ButterKnife.bind(dialog);

//                RadioButton rbNamaAsc = (RadioButton) dialog.findViewById(R.id.rb_nama_asc);
//                RadioButton rbNamaDesc = (RadioButton) dialog.findViewById(R.id.rb_nama_desc);
//                RadioButton rbJenis = (RadioButton) dialog.findViewById(R.id.rb_jenis);
//                final RadioButton rbType = (RadioButton) dialog.findViewById(R.id.rb_type);
//                LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.ll_jenis);
//                final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.rg_pilih);
//                Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_waktu);
//
//                // spineer collectdata
//                ArrayAdapter<String> adapter;
//                List<String> list;
//
//                list = new ArrayList<String>();
//                list.add("YTD");
//                list.add("1 Tahun");
//                list.add("3 Tahun");
//                list.add("5 Tahun");
//
//                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(adapter);
//
//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        radioGroup.clearCheck();
//                        rbType.setChecked(true);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//
//                linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (radioGroup.getCheckedRadioButtonId() == rbType.getId()) {
//                            rbType.setChecked(true);
//                        } else {
//                            rbType.setChecked(false);
//                        }
//                    }
//                });
//
//                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                        if (checkedId == rbType.getId()) {
//                            rbType.setChecked(true);
//                        } else {
//                            rbType.setChecked(false);
//                        }
//                    }
//                });
//
//                rbType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            radioGroup.clearCheck();
//                            rbType.setChecked(true);
//                        }
//                    }
//                });
            }
        });

        llAddPorto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectServerApi("get_insurance_types_and_products", null);
            }
        });
        try {
            fetchPortfolios();
        }catch(Exception ex){}
    }

    private void fetchPortfolios() throws AuthFailureError {

        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                List<InsuranceProduct> newlist = gson.fromJson(response, new TypeToken<List<InsuranceProduct>>() {}.getType());
                if (newlist != null) {
                    list.clear();
                    for (InsuranceProduct product : newlist){
                        list.add(product);
                    }
                }
                portofolioAsuranceAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() {
                return HttpClientRequest.getHeaders("get_insurance_portfolio");
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

    Dialog managementDialog;

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        if (requestHeader.equalsIgnoreCase("get_insurance_types_and_products")){
            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
            Map<String,List<InsuranceProduct>> map = gson.fromJson(result, new TypeToken<Map<String, List<InsuranceProduct>>>() { }.getType());
            managementDialog = irDialogUtils.showDialog("Tambahkan Portofolio Asuransimu", R.layout.dialog_add_insurance, true);
            List<String> list = new ArrayList<String>();
            for (String str : map.keySet()){
                list.add(str);
            }
            NewInsuranceGroupAdapter adapter = new NewInsuranceGroupAdapter(getApplicationContext(), list, map, new Listener());
            RecyclerView rv1 = managementDialog.findViewById(R.id.rv1);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rv1.setLayoutManager(layoutManager);
            rv1.setAdapter(adapter);

            this.next = managementDialog.findViewById(R.id.btn_next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,String> params = new HashMap<>();
                    params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    params.put("list", gson.toJson(products.keySet()));
                    connectServerApi("add_insurance_portfolio", params);
                }
            });
        }else if (requestHeader.equalsIgnoreCase("add_insurance_portfolio")){
            if (managementDialog!=null && managementDialog.isShowing()){
                managementDialog.cancel();
            }
            try {
                fetchPortfolios();
            }catch(Exception ex){}
        }else if (requestHeader.equalsIgnoreCase("del_insurance_portfolio")){
            if (managementDialog!=null && managementDialog.isShowing()){
                managementDialog.cancel();
            }
            try {
                fetchPortfolios();
            }catch(Exception ex){}
        }
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        if (managementDialog!=null && managementDialog.isShowing()){
            managementDialog.cancel();
        }
    }

    Button next;

    private static final String TAG = PortfolioInsuranceActivity.class.toString();
    private Map<Integer,InsuranceProduct> products = new TreeMap<>();

    class Listener implements NewInsuranceAdapter.OnItemCheckListener{

        @Override
        public void onItemCheck(InsuranceProduct model, boolean isChecked) {
            if (isChecked){
                products.put(model.getId(), model);
            }else{
                products.remove(model.getId());
            }
            String text = "Tambahkan";
            if (products.size()>0){
                next.setText(text+" ("+products.size()+")");
            }
        }
    }

    @OnClick(R.id.iv_finish)
    public void onViewClicked() {
        finish();
    }


}
