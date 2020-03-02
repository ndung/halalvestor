package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InvestmentProduct;

public class NewInvestmentAdapter extends RecyclerView.Adapter<NewInvestmentAdapter.ViewHolder> {

    private final List<InvestmentProduct> list;
    private final NewInvestmentAdapter.OnItemCheckListener listener;

    public NewInvestmentAdapter(List<InvestmentProduct> list, NewInvestmentAdapter.OnItemCheckListener listener){
        this.list = list;
        this.listener = listener;
    }

    public interface OnItemCheckListener {
        void onItemCheck(InvestmentProduct model, boolean isChecked);
        void onTextChange(InvestmentProduct model, String propertyName, String propertyValue);
    }

    @Override
    public NewInvestmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_investment_item, parent, false);
        return new NewInvestmentAdapter.ViewHolder(view);
    }

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    public void onBindViewHolder(final NewInvestmentAdapter.ViewHolder holder, int position) {
        final InvestmentProduct product = list.get(position);
        holder.checkBox.setText(product.getProductName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onItemCheck(product, isChecked);
                if (isChecked){
                    holder.linearLayout.setVisibility(View.VISIBLE);
                }else{
                    holder.linearLayout.setVisibility(View.GONE);
                }
            }
        });
        holder.tvHargaBeli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.onTextChange(product, "hargaBeli", s.toString());
            }
        });
        holder.tvHargaBeli.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus&&!holder.tvHargaBeli.getText().toString().isEmpty()) {
                    Number n = Double.parseDouble(holder.tvHargaBeli.getText().toString().replaceAll("[,.]", "").replaceAll("Rp", "").trim());
                    holder.tvHargaBeli.setText(decimalFormat.format(n));
                }
            }
        });
        holder.tvNAB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.onTextChange(product, "nab", s.toString());
            }
        });
        holder.tvNAB.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (!hasFocus&&!holder.tvNAB.getText().toString().isEmpty()) {
                        Number n = Double.parseDouble(holder.tvNAB.getText().toString().replaceAll("[,.]", "").replaceAll("Rp", "").trim());
                        holder.tvNAB.setText(decimalFormat.format(n));
                    }
                }
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
        @BindView(R.id.ll)
        LinearLayout linearLayout;
        @BindView(R.id.tv_harga_beli)
        EditText tvHargaBeli;
        @BindView(R.id.tv_nab)
        EditText tvNAB;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
