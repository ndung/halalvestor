package id.halalvestor.activity.main.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import butterknife.Unbinder;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.InsuranceOptionAdapter;
import id.halalvestor.activity.main.BaseFragment;
import id.halalvestor.model.InsuranceProductType;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;

public class InsuranceOptionFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;

    private static final String TAG = InsuranceOptionFragment.class.toString();

    List<InsuranceProductType> list = new ArrayList<>();
    InsuranceOptionAdapter insuranceOptionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insurance_layout, container, false);
        //ButterKnife.bind(this, view);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        insuranceOptionAdapter = new InsuranceOptionAdapter(list, new InsuranceOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InsuranceProductType model) {
                Intent intent = new Intent(getActivity(), InsuranceShopListActivity.class);
                intent.putExtra("product_type",model.getId());
                startActivity(intent);
            }
        });
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv.setAdapter(insuranceOptionAdapter);

        try {
            fetchInsuranceProducts();
        }catch(AuthFailureError err){

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void fetchInsuranceProducts() throws AuthFailureError {
        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                List<InsuranceProductType> newlist = gson.fromJson(response, new TypeToken<List<InsuranceProductType>>() {}.getType());
                list.clear();
                if(newlist!=null) {
                    for (InsuranceProductType t : newlist) {
                        list.add(t);
                    }
                }
                insuranceOptionAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() {
                return HttpClientRequest.getHeaders("get_insurance_product_types");
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
}
