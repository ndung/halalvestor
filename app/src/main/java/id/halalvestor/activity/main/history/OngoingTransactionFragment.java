package id.halalvestor.activity.main.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.OngoingTransactionAdapter;
import id.halalvestor.activity.main.BaseFragment;
import id.halalvestor.model.Transaction;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;

public class OngoingTransactionFragment extends BaseFragment {

    private static final String TAG = OngoingTransactionFragment.class.toString();

    @BindView(R.id.recyler_view)
    RecyclerView recyclerView;

    List<Transaction> transactions;
    OngoingTransactionAdapter historyTransactionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ongoing_transaction_layout, container, false);
        ButterKnife.bind(this, view);

        transactions = new ArrayList<>();

        historyTransactionAdapter = new OngoingTransactionAdapter(transactions, new OngoingTransactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Transaction model) {
                Intent intent = null;
                if (model.getProductCategory()==1 && model.getTrxType() == 2 && model.getStatus() == 1) {
                    intent = new Intent(getActivity(), InvestmentOngoingBuyActivity.class);
                }else if (model.getProductCategory()==2 && model.getTrxType() == 2 && model.getStatus() == 1) {
                    intent = new Intent(getActivity(), InsuranceOngoingBuyActivity.class);
                } else{
                    //TO-DO:
                }
                if (intent!=null) {
                    intent.putExtra("transaction", model);
                    startActivity(intent);
                }
            }

            @Override
            public void onConfirmed(Transaction model) {
                Intent intent = new Intent(getActivity(), TransactionConfirmationActivity.class);
                intent.putExtra("transaction", model);
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(historyTransactionAdapter);
        try {
            fetchTransactions();
        }catch(AuthFailureError err){};
        return view;
    }



    private void fetchTransactions() throws AuthFailureError {

        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();;
                List<Transaction> newlist = gson.fromJson(response, new TypeToken<List<Transaction>>() {}.getType());
                if(newlist!=null) {
                    transactions.clear();
                    for (Transaction t : newlist) {
                        transactions.add(t);
                    }
                }
                historyTransactionAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() {
                return HttpClientRequest.getHeaders("get_ongoing_transactions");
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String,String>();
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


        Log.d("url", strReq.getUrl() + "/" );

        //Adding request to request queue
        App.getInstance().addToRequestQueue(strReq);
    }
}
