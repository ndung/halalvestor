package id.halalvestor.activity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InvestmentRecommendation;


public class InvestmentRecommendationAdapter extends RecyclerView.Adapter<InvestmentRecommendationAdapter.ViewHolder> {

    Activity activity;
    List<InvestmentRecommendation> list = new ArrayList<>();

    public InvestmentRecommendationAdapter(Activity activity, List<InvestmentRecommendation> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public InvestmentRecommendationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_recommendation_item_layout, parent, false);
        return new InvestmentRecommendationAdapter.ViewHolder(view);
    }

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    public void onBindViewHolder(InvestmentRecommendationAdapter.ViewHolder holder, final int position) {
        final InvestmentRecommendation model = list.get(position);
        holder.tvName.setText(model.getInvestmentName());
        holder.tvAmount.setText(decimalFormat.format(model.getInvestmentAmount()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_investment_name)
        TextView tvName;
        @BindView(R.id.tv_investment_amount)
        TextView tvAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
