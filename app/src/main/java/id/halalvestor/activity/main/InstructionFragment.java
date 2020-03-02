package id.halalvestor.activity.main;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.InstructionAdapter;
import id.halalvestor.model.Instruction;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;

public class InstructionFragment extends BaseFragment{

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = InstructionFragment.class.toString();

    InstructionAdapter listAdapter;
    List<Instruction> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instruction_layout, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        list = new ArrayList<>();
        listAdapter = new InstructionAdapter(list, new InstructionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Instruction model) {
                Intent intent = new Intent(getContext(), InstructionDetailActivity.class);
                intent.putExtra("data", model);
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);

        try {
            fetchInstructions();
        }catch(AuthFailureError err){};

        return view;
    }

    private void fetchInstructions() throws AuthFailureError {
        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();;
                List<Instruction> newlist = gson.fromJson(response, new TypeToken<List<Instruction>>() {}.getType());
                for (Instruction instruction : newlist){
                    list.add(instruction);
                }

                listAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() {
                return HttpClientRequest.getHeaders("get_instruction");
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String,String>();
                params.put("uid", firebaseUser.getUid());
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
