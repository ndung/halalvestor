package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InsuranceProduct;

public class NewInsuranceAdapter extends RecyclerView.Adapter<NewInsuranceAdapter.ViewHolder> {

    private final List<InsuranceProduct> list;
    private final OnItemCheckListener listener;

    public NewInsuranceAdapter(List<InsuranceProduct> list, OnItemCheckListener listener){
        this.list = list;
        this.listener = listener;
    }

    public interface OnItemCheckListener {
        void onItemCheck(InsuranceProduct model, boolean isChecked);
    }

    @Override
    public NewInsuranceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_insurance_item, parent, false);
        return new NewInsuranceAdapter.ViewHolder(view);
    }

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    public void onBindViewHolder(final NewInsuranceAdapter.ViewHolder holder, int position) {
        final InsuranceProduct product = list.get(position);
        holder.checkBox.setText(product.getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onItemCheck(product, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cb)
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
