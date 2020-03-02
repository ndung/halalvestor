package id.halalvestor.activity.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;

public class SurveySection2Adapter extends RecyclerView.Adapter<SurveySection2Adapter.ViewHolder> {

    private static final String TAG = SurveySection2Adapter.class.toString();

    List<SurveyQuestion> list = new ArrayList<>();

    private Activity activity;

    public SurveySection2Adapter(Activity activity, List<SurveyQuestion> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_section2_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SurveyQuestion question = list.get(position);
        holder.textView.setText(Html.fromHtml((position + 1) + ". " +question.getQuestion()));
        final List<QuestionParameter> list = App.getInstance().getPreferenceManager().getSurveyAnswers(question.getId());
        float progress = list.get(0).getSequence();

        holder.seekBar.setProgress(progress);
        holder.seekBar.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();

                for (int i=0;i<question.getParameters().size();i++){
                    QuestionParameter parameter = question.getParameters().get(i);
                    array.put(i, parameter.getDescription());
                }

                return array;
            }
        });

        holder.seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                List<QuestionParameter> ans = new ArrayList<>();
                for (QuestionParameter parameter : question.getParameters()) {
                    if (parameter.getSequence() == holder.seekBar.getProgress()) {
                        ans.add(parameter);
                    }
                }
                App.getInstance().getPreferenceManager().putSurveyAnswers(question.getId(), ans);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.description)
        protected TextView textView;
        @BindView(R.id.qty)
        protected BubbleSeekBar seekBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
