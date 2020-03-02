package id.halalvestor.activity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import id.halalvestor.model.BuySell;

public class BuySellAdapter extends RecyclerView.Adapter<BuySellAdapter.ViewHolder> {

    private static final int HEADER_VIEW = 1;

    List<BuySell> list = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(BuySell model);
    }

    private Activity activity;
    private final OnItemClickListener listener;

    public BuySellAdapter(Activity activity, List<BuySell> list, OnItemClickListener listener) {
        this.activity = activity;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buysell_item_layout, parent, false);

        if (viewType == HEADER_VIEW) {
            return new HeaderViewHolder(view);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.tvNama.setText("Nama Produk");
            vh.tvNama.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            vh.tvNama.setGravity(Gravity.CENTER);

            vh.tvPunya.setText("Punya");
            vh.tvPunya.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            vh.tvPunya.setGravity(Gravity.CENTER);

            vh.tvGrowth.setText("Growth");
            vh.tvGrowth.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            vh.tvGrowth.setGravity(Gravity.CENTER);

            vh.tvNAB.setText("NAB");
            vh.tvNAB.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            vh.tvNAB.setGravity(Gravity.CENTER);

            vh.tvTipe.setText("Tipe Reksadana");
            vh.tvTipe.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            vh.tvTipe.setGravity(Gravity.CENTER);

            vh.ivBeli.setAlpha(0f);
        } else if (holder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) holder;
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(null);
                }
            });
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            // This is where we'll add footer.
            return HEADER_VIEW;
        }

        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }

        if (list.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return list.size() + 1;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_nama)
        protected TextView tvNama;
        @BindView(R.id.tv_nab)
        protected TextView tvNAB;
        @BindView(R.id.tv_tipe)
        protected TextView tvTipe;
        @BindView(R.id.tv_growth)
        protected TextView tvGrowth;
        @BindView(R.id.tv_punya)
        protected TextView tvPunya;
        @BindView(R.id.iv_beli)
        protected ImageView ivBeli;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderViewHolder extends ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
