package id.halalvestor.activity.adapter;

import android.graphics.Color;
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
import id.halalvestor.model.Transaction;

public class CompletedTransactionAdapter extends RecyclerView.Adapter<CompletedTransactionAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Transaction model);
    }

    private final CompletedTransactionAdapter.OnItemClickListener listener;

    final String TYPE_JUAL = "JUAL";
    final String TYPE_BELI = "BELI";

    final int STATUS_ON_GOING = 1;
    final int STATUS_COMPLETED = 2;
    final int STATUS_CANCEL = 3;

    List<Transaction> list;

    public CompletedTransactionAdapter(List<Transaction> list, CompletedTransactionAdapter.OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public CompletedTransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_transaction_item_layout, parent, false);
        return new CompletedTransactionAdapter.ViewHolder(view);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id"));

    @Override
    public void onBindViewHolder(CompletedTransactionAdapter.ViewHolder holder, int position) {

        final Transaction model = list.get(position);

        String date = sdf.format(model.getTrxDate());

        holder.tvDate.setText(date.substring(0, 2));
        holder.tvMonth.setText(date.substring(3, 6));
        holder.tvYear.setText(date.substring(7, 11));

        holder.tvCategory.setText(model.getProductCategoryName());

        //holder.tvTitle.setText(model.getInvestmentProduct().getProductName());
        //holder.tvDesc.setText(model.getInvestmentProduct().getInvestmentManager());

        holder.tvTitle.setText(model.getDetail().get("product.name"));
        holder.tvDesc.setText(model.getDetail().get("product.investmentManager"));

        switch (model.getStatus()) {
            case STATUS_ON_GOING:
                holder.tvStatus.setText("on going");
                break;
            case STATUS_COMPLETED:
                holder.tvStatus.setTextColor(Color.parseColor("#ff99cc00"));
                holder.tvStatus.setText("completed");
                break;
            case STATUS_CANCEL:
                holder.tvStatus.setTextColor(Color.parseColor("#dd2c00"));
                holder.tvStatus.setText("canceled");
                break;
        }

        switch (model.getTrxType()) {
            case 1:
                holder.tvType.setText(TYPE_JUAL);
                break;
            case 2:
                holder.tvType.setText(TYPE_BELI);
                break;
        }
        holder.ivDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(model);
            }
        });
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

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getStatus();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_month)
        TextView tvMonth;
        @BindView(R.id.tv_year)
        TextView tvYear;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_download)
        TextView tvDownload;
        @BindView(R.id.iv_detail)
        ImageView ivDetail;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_category)
        TextView tvCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
