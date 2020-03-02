package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InvestmentGoal;

public class ProgressGoalAdapter extends RecyclerView.Adapter<ProgressGoalAdapter.ViewHolder> {

    List<InvestmentGoal> list = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(InvestmentGoal model);
    }

    private final OnItemClickListener listener;

    int textColor;
    int activeImage;
    int inactiveImage;

    public ProgressGoalAdapter(List<InvestmentGoal> list, OnItemClickListener listener, int textColor, int activeImage, int inactiveImage) {
        this.list = list;
        this.listener = listener;
        this.textColor = textColor;
        this.activeImage = activeImage;
        this.inactiveImage = inactiveImage;
    }

    @Override
    public ProgressGoalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_goal_item_layout, parent, false);
        return new ProgressGoalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProgressGoalAdapter.ViewHolder holder, int position) {
        final InvestmentGoal model = list.get(position);
        holder.tv.setText("Goal "+(position+1));
        holder.tv.setTextColor(textColor);
        holder.iv.setImageResource(activeImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.iv.setImageResource(activeImage);
                listener.onItemClick(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.iv)
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
