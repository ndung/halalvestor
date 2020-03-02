package id.halalvestor.activity.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.EvaluationInvestment;

public class EvaluationInvestmentDetailAdapter extends RecyclerView.Adapter<EvaluationInvestmentDetailAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(EvaluationInvestment model);
    }

    private final OnItemClickListener listener;

    List<EvaluationInvestment> list;
    Context context;

    public EvaluationInvestmentDetailAdapter(Context context, List<EvaluationInvestment> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluation_investment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EvaluationInvestment model = list.get(position);

        holder.tvName.setText(model.getName());

        if(model.getState() == 1){
            holder.tvState.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.tvState.setText("Baik");
            holder.tvArgs.setText("Pertahankan produk anda");
        }else if(model.getState() == 2){
            holder.tvState.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
            holder.tvState.setText("Kurang baik");
            holder.tvArgs.setText("Performa produk ini saat ini tengah menurun. Jika penurunan berlanjut, segera ganti dengan produk rekomendasi lainnya");
        }else{
            holder.tvState.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            holder.tvState.setText("Hati-hati");
            holder.tvArgs.setText("Jual produk ini, ganti dengan produk rekomendasi lainnya");
        }

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

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_args)
        TextView tvArgs;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
