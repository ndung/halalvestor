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
import id.halalvestor.model.InvestmentProduct;

public class NewInvestmentGroupAdapter extends RecyclerView.Adapter<NewInvestmentGroupAdapter.ViewHolder> {

    private final List<String> list;
    private final Map<String,List<InvestmentProduct>> map;
    private final Context context;
    private final NewInvestmentAdapter.OnItemCheckListener listener;

    public NewInvestmentGroupAdapter(Context context, List<String> list, Map<String,List<InvestmentProduct>> map, NewInvestmentAdapter.OnItemCheckListener listener){
        this.context = context;
        this.list = list;
        this.map = map;
        this.listener = listener;
    }

    @Override
    public NewInvestmentGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_investment_group_item, parent, false);
        return new NewInvestmentGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewInvestmentGroupAdapter.ViewHolder holder, int position) {
        String product = list.get(position);
        holder.textView.setText(product);
        List<InvestmentProduct> list = map.get(product);
        NewInvestmentAdapter adapter = new NewInvestmentAdapter(list, listener);
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
