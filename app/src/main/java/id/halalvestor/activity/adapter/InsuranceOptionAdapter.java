package id.halalvestor.activity.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InsuranceProductType;

public class InsuranceOptionAdapter extends RecyclerView.Adapter<InsuranceOptionAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(InsuranceProductType model);
    }

    private final OnItemClickListener listener;

    List<InsuranceProductType> list;

    public InsuranceOptionAdapter(List<InsuranceProductType> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insurance_menu_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final InsuranceProductType model = list.get(position);

        switch (model.getId()){
            case 1:
                holder.ivGoal.setImageResource(R.drawable.asurance_menu_1);
                break;
            case 2:
                holder.ivGoal.setImageResource(R.drawable.asurance_menu_2);
                break;
            case 3:
                holder.ivGoal.setImageResource(R.drawable.asurance_menu_3);
                break;
            case 4:
                holder.ivGoal.setImageResource(R.drawable.asurance_menu_4);
                break;
            case 5:
                holder.ivGoal.setImageResource(R.drawable.asurance_menu_5);
                break;
            case 6:
                holder.ivGoal.setImageResource(R.drawable.asurance_menu_6);
                break;
        }
        holder.tvGoal.setText(model.getDescription());

        if (!model.isAvailable()){
            holder.ivGoal.setColorFilter(Color.rgb(100, 100, 100));
            holder.tvAvailability.setVisibility(View.VISIBLE);
        }else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(model);
                }
            });
        }
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
        @BindView(R.id.tv_available)
        TextView tvAvailability;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
