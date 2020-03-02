package id.halalvestor.activity.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InsuranceProduct;

public class NewInsuranceGroupAdapter extends RecyclerView.Adapter<NewInsuranceGroupAdapter.ViewHolder> {

    List<String> list;
    Map<String,List<InsuranceProduct>> map;
    Context context;
    NewInsuranceAdapter.OnItemCheckListener listener;

    public NewInsuranceGroupAdapter(Context context, List<String> list, Map<String,List<InsuranceProduct>> map, NewInsuranceAdapter.OnItemCheckListener listener){
        this.context = context;
        this.list = list;
        this.map = map;
        this.listener = listener;
    }

    @Override
    public NewInsuranceGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_insurance_group_item, parent, false);
        return new NewInsuranceGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewInsuranceGroupAdapter.ViewHolder holder, int position) {
        String product = list.get(position);
        holder.textView.setText(product);
        List<InsuranceProduct> list = map.get(product);
        NewInsuranceAdapter adapter = new NewInsuranceAdapter(list, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv)
        TextView textView;
        @BindView(R.id.rv)
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
