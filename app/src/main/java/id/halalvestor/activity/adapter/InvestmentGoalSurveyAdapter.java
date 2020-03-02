package id.halalvestor.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;

public class InvestmentGoalSurveyAdapter extends RecyclerView.Adapter<InvestmentGoalSurveyAdapter.ViewHolder> {

    private static final String TAG = InvestmentGoalSurveyAdapter.class.toString();

    SurveyQuestion question;
    List<QuestionParameter> params;

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public interface OnItemClickListener {
        void onItemClick(QuestionParameter model, boolean isRemove);
    }

    private final OnItemClickListener listener;

    public InvestmentGoalSurveyAdapter(SurveyQuestion question, List<QuestionParameter> params, OnItemClickListener listener) {
        this.question = question;
        this.params = params;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_goal_survey_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final QuestionParameter model = params.get(position);

        holder.tvTitle.setText(model.getDescription());
        holder.qvTahun.setProgress(0);
        holder.etJumlah.setText("");

        if (model.getAdditionalResponses() != null && model.getAdditionalResponses().containsKey("time") && !model.getAdditionalResponses().get("time").equals("")) {
            holder.qvTahun.setProgress(Integer.parseInt(model.getAdditionalResponses().get("time")));
        }
        if (model.getAdditionalResponses() != null && model.getAdditionalResponses().containsKey("fund") && !model.getAdditionalResponses().get("fund").equals("")) {
            holder.etJumlah.setText(decimalFormat.format(Double.parseDouble((String) model.getAdditionalResponses().get("fund").replaceAll("\\,", "").replaceAll("\\.", ""))));
        }

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.qvTahun.getProgress()>0) {
                    holder.qvTahun.setProgress(holder.qvTahun.getProgress() - 1);
                }
            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.qvTahun.getProgress()<10) {
                    holder.qvTahun.setProgress(holder.qvTahun.getProgress() + 1);
                }
            }
        });

        /**holder.qvTahun.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
        @Override public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {

        }

        @Override public void onLimitReached() {

        }
        });*/

        //holder.etJumlah.addTextChangedListener(new NumberTextWatcher(holder.etJumlah));

        /**holder.etJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(current) && !editable.toString().equals("")) {
                    holder.etJumlah.removeTextChangedListener(this);
                    /**
                    String cleanString = editable.toString().replaceAll("[,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = decimalFormat.format(parsed);
                    current = formatted;
                    holder.etJumlah.setText(formatted);
                    holder.etJumlah.setSelection(formatted.length());*/
                    /**int inilen = editable.length();
                    Number n = Double.parseDouble(editable.toString().replaceAll("[,.]", ""));
                    int cp = holder.etJumlah.getSelectionStart();
                    holder.etJumlah.setText(decimalFormat.format(n));
                    int endlen = holder.etJumlah.getText().length();
                    int sel = (cp + (endlen - inilen));
                    if (sel > 0 && sel <= holder.etJumlah.getText().length()) {
                        holder.etJumlah.setSelection(sel);
                    } else {
                        holder.etJumlah.setSelection(holder.etJumlah.getText().length() - 1);
                    }
                    holder.etJumlah.addTextChangedListener(this);
                }
            }
        });*/

        holder.etJumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Number n = Double.parseDouble(holder.etJumlah.getText().toString().replaceAll("[,.]", ""));
                    holder.etJumlah.setText(decimalFormat.format(n));
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(model, false);
            }
        });

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(model, true);
            }
        });

    }

    private String current;

    @Override
    public int getItemCount() {
        return params.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        //@BindView(R.id.qv_tahun)
        //QuantityView qvTahun;
        @BindView(R.id.et_jumlah)
        EditText etJumlah;
        @BindView(R.id.iv_remove)
        ImageView ivRemove;
        @BindView(R.id.qty)
        BubbleSeekBar qvTahun;
        @BindView(R.id.iv_minus)
        ImageView ivMinus;
        @BindView(R.id.iv_plus)
        ImageView ivPlus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        //public QuantityView getQvTahun() {
        //    return qvTahun;
        //}

        public BubbleSeekBar getQvTahun() {
            return qvTahun;
        }

        public EditText getEtJumlah() {
            return etJumlah;
        }

    }
}
