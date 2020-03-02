package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;

public class MonthlyInstallmentAdapter extends RecyclerView.Adapter<MonthlyInstallmentAdapter.ViewHolder> {

    List<Object[]> list;

    public MonthlyInstallmentAdapter(List<Object[]> list) {
        this.list = list;
    }

    @Override
    public MonthlyInstallmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_installment_layout, parent, false);
        return new MonthlyInstallmentAdapter.ViewHolder(view);
    }

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Object[] str = list.get(position);
        holder.name.setText((String)str[0]);
        holder.amount.setText(decimalFormat.format(str[1]));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rd_name)
        TextView name;
        @BindView(R.id.rd_rp)
        TextView amount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
