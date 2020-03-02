package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.QuestionParameter;

public class InvestmentGoalSurveyChoiceAdapter extends RecyclerView.Adapter<InvestmentGoalSurveyChoiceAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(QuestionParameter model);
    }

    private final OnItemClickListener listener;

    List<QuestionParameter> list;

    public InvestmentGoalSurveyChoiceAdapter(List<QuestionParameter> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_goal_survey_item_choice_layout, parent, false);
        return new ViewHolder(view);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id"));

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final QuestionParameter model = list.get(position);

        switch (model.getSequence()) {
            case 1:
                holder.ivGoal.setImageResource(R.drawable.ic_buy_home);
                break;
            case 2:
                holder.ivGoal.setImageResource(R.drawable.ic_buy_car);
                break;
            case 3:
                holder.ivGoal.setImageResource(R.drawable.ic_married);
                break;
            case 4:
                holder.ivGoal.setImageResource(R.drawable.ic_travel);
                break;
            case 5:
                holder.ivGoal.setImageResource(R.drawable.ic_investment);
                break;
            case 6:
                holder.ivGoal.setImageResource(R.drawable.ic_finance_profit);
                break;
            case 7:
                holder.ivGoal.setVisibility(View.GONE);
                holder.tvGoal.setVisibility(View.GONE);
                break;
        }

        holder.tvGoal.setText(model.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_goal)
        ImageView ivGoal;
        @BindView(R.id.tv_goal)
        TextView tvGoal;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
