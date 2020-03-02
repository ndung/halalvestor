package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.model.Instruction;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Instruction model);
    }

    private final OnItemClickListener listener;

    List<Instruction> list;

    public InstructionAdapter(List<Instruction> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public InstructionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InstructionAdapter.ViewHolder holder, int position) {
        final Instruction model = list.get(position);
        holder.tvSubject.setText(model.getSubject());
        holder.imageView.setImageUrl(model.getPicture(), App.getInstance().getImageLoader());
        holder.tvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_subject)
        TextView tvSubject;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.iv)
        NetworkImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
