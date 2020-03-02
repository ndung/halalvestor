package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.InsuranceProduct;

public class InsuranceProductShopAdapter extends RecyclerView.Adapter<InsuranceProductShopAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(InsuranceProduct model);
    }

    private final OnItemClickListener listener;

    List<InsuranceProduct> list;

    public InsuranceProductShopAdapter(List<InsuranceProduct> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insurance_shop_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final InsuranceProduct model = list.get(position);

        Map<String,String> map = model.getDescription();

        holder.tvDesc.setText(model.getName());
        String rawatInap = "", rawatJalan = "", santunanKematian = "", frequency = "";
        if (map.containsKey("rawatInap")){
            rawatInap = map.get("rawatInap");
        }
        if (map.containsKey("rawatJalan")){
            rawatJalan = map.get("rawatJalan");
        }
        if (map.containsKey("santunanKematian")){
            santunanKematian = map.get("santunanKematian");
        }
        if (map.containsKey("frequency")){
            frequency = map.get("frequency");
        }
        holder.tvRawatInap.setText(rawatInap);
        holder.tvRawatJalan.setText(rawatJalan);
        holder.tvSantunanKematian.setText(santunanKematian);
        holder.tvHargaPremi.setText(decimalFormat.format(model.getPremium()));
        holder.tvFrequency.setText(frequency);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(model);
            }
        });
    }

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_rawat_jalan)
        TextView tvRawatJalan;
        @BindView(R.id.tv_rawat_inap)
        TextView tvRawatInap;
        @BindView(R.id.tv_santunan_kematian)
        TextView tvSantunanKematian;
        @BindView(R.id.tv_frequency)
        TextView tvFrequency;
        @BindView(R.id.tv_harga_premi)
        TextView tvHargaPremi;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
