package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InsuranceProduct;

public class InsurancePortfolioAdapter extends RecyclerView.Adapter<InsurancePortfolioAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(InsuranceProduct model);
    }

    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(InsuranceProduct model);
    }

    private final OnItemClickListener listener;
    private final OnItemDeleteClickListener listenerDelete;

    List<InsuranceProduct> list;

    public InsurancePortfolioAdapter(List<InsuranceProduct> list, OnItemClickListener listener, OnItemDeleteClickListener listenerDelete) {
        this.list = list;
        this.listener = listener;
        this.listenerDelete = listenerDelete;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insurance_portfolio_item, parent, false);
        return new ViewHolder(view);
    }

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final InsuranceProduct model = list.get(position);

        holder.tvTitle.setText(model.getType());
        holder.tvDesc.setText(model.getName());
        holder.tvHargaPremi.setText(decimalFormat.format(model.getPremium()));
        //holder.tvHargaBeli.setText(String.valueOf(model.getHargaJual()));
        //holder.tvFrequency.setText(String.valueOf(model.getHargaPermi()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(model);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerDelete.onItemDeleteClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_detail)
        TextView tvDetail;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_periode)
        TextView tvPeriode;
        @BindView(R.id.tv_harga_beli)
        TextView tvHargaBeli;
        @BindView(R.id.tv_harga_premi)
        TextView tvHargaPremi;
        @BindView(R.id.tv_frequency)
        TextView tvFrequency;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
