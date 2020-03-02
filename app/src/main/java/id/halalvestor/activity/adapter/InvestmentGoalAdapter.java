package id.halalvestor.activity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InvestmentGoal;


public class InvestmentGoalAdapter extends RecyclerView.Adapter<InvestmentGoalAdapter.ViewHolder> {

    Activity activity;
    List<InvestmentGoal> list = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(InvestmentGoal model, int position);
    }

    public InvestmentGoalAdapter(Activity activity, List<InvestmentGoal> list, OnItemClickListener listener) {
        this.activity = activity;
        this.list = list;
        this.listener = listener;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    @Override
    public InvestmentGoalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_goal_item_layout, parent, false);
        return new InvestmentGoalAdapter.ViewHolder(view);
    }

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    public void onBindViewHolder(InvestmentGoalAdapter.ViewHolder holder, final int position) {
        final InvestmentGoal model = list.get(position);
        Picasso.with(activity).load(model.getPic()).into(holder.ivGoal);
        holder.tvName.setText(model.getGoal()+" ("+sdf.format(model.getDateTime())+") ");
        holder.tvNo.setText("Goal "+(position+1));
        holder.progressBar.setProgress(model.getProgress());
        holder.tvInvestment.setText(decimalFormat.format(model.getInvestment()));
        holder.tvFv.setText(decimalFormat.format(model.getFund()));
        holder.ivGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(model, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.tv_fv)
        TextView tvFv;
        @BindView(R.id.iv_goal)
        ImageView ivGoal;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.tv_investment)
        TextView tvInvestment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
