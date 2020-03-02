package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.MedicalHistory;

public class InsuranceMedicalHistoryAdapter extends RecyclerView.Adapter<InsuranceMedicalHistoryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onRadioChangeListener(MedicalHistory model);
    }

    private final OnItemClickListener listener;

    List<MedicalHistory> list;

    public InsuranceMedicalHistoryAdapter(List<MedicalHistory> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insurance_medical_history_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MedicalHistory model = list.get(position);

        holder.tvDesc.setText(Html.fromHtml(model.getDesc()));
        holder.tvQuestion.setText(model.getQuestion());

        holder.rgState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                listener.onRadioChangeListener(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_question)
        TextView tvQuestion;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.rb_yes)
        RadioButton rbYes;
        @BindView(R.id.rb_no)
        RadioButton rbNo;
        @BindView(R.id.rg_state)
        RadioGroup rgState;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
