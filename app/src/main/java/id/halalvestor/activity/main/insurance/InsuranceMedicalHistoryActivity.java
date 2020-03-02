package id.halalvestor.activity.main.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.SurveySection1Adapter;
import id.halalvestor.model.InsuranceProduct;
import id.halalvestor.model.QuestionParameter;
import id.halalvestor.model.SurveyQuestion;

public class InsuranceMedicalHistoryActivity extends AppCompatActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_question)
    RecyclerView rvQuestion;
    @BindView(R.id.cb_term_1)
    CheckBox cbTerm1;
    @BindView(R.id.cb_term_2)
    CheckBox cbTerm2;
    @BindView(R.id.bt_next)
    Button btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_medical_history);
        ButterKnife.bind(this);

        tvTitle.setText("RIWAYAT KESEHATAN");
        final InsuranceProduct model = (InsuranceProduct) getIntent().getSerializableExtra("product");

        List<SurveyQuestion> list = new ArrayList<>();
        SurveyQuestion question1 = new SurveyQuestion();
        question1.setId(101);
        question1.setQuestion("Apakah anda dalam perawatan inap atau sedang dalam konsultasi yang memerlukan tindak lanjut rawat inap?");
        question1.setRequired(true);
        question1.setType(2);
        List<QuestionParameter> answer1 = new ArrayList<>();
        List<QuestionParameter> answer11 = new ArrayList<>();
        QuestionParameter answer1a = new QuestionParameter();
        answer1a.setId(1001);
        answer1a.setDescription("Ya");
        answer1a.setSequence(1);
        QuestionParameter answer1b = new QuestionParameter();
        answer1b.setId(1002);
        answer1b.setDescription("Tidak");
        answer1.add(answer1a);
        answer1.add(answer1b);
        answer11.add(answer1b);
        question1.setParameters(answer1);
        App.getInstance().getPreferenceManager().putSurveyAnswers(question1.getId(), answer11);

        SurveyQuestion question2 = new SurveyQuestion();
        question2.setId(102);
        question2.setQuestion("Apakah anda sedang mengalami atau dalam perawatan yang menyangkut salah satu dari beberapa penyakit ini?");
        question2.setRequired(true);
        question2.setType(4);
        List<QuestionParameter> answer2 = new ArrayList<>();
        List<QuestionParameter> answer22 = new ArrayList<>();
        QuestionParameter answer2a = new QuestionParameter();
        answer2a.setId(2001);
        answer2a.setDescription("Serangan Jantung");
        answer2a.setSequence(1);
        QuestionParameter answer2b = new QuestionParameter();
        answer2b.setId(2002);
        answer2b.setDescription("Stroke");
        answer2b.setSequence(2);
        QuestionParameter answer2c = new QuestionParameter();
        answer2c.setId(2003);
        answer2c.setDescription("Gagal Ginjal");
        answer2c.setSequence(3);
        QuestionParameter answer2d = new QuestionParameter();
        answer2d.setId(2004);
        answer2d.setDescription("Penyakit Hati Kronis");
        answer2d.setSequence(5);
        QuestionParameter answer2e = new QuestionParameter();
        answer2e.setId(2005);
        answer2e.setDescription("Tidak ada");
        answer2e.setSequence(5);
        answer2.add(answer2a);
        answer2.add(answer2b);
        answer2.add(answer2c);
        answer2.add(answer2d);
        answer2.add(answer2e);
        answer22.add(answer2e);
        question2.setParameters(answer2);
        App.getInstance().getPreferenceManager().putSurveyAnswers(question2.getId(), answer22);

        list.add(question1);
        list.add(question2);

        SurveySection1Adapter adapter = new SurveySection1Adapter(this, list);
        rvQuestion.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvQuestion.setAdapter(adapter);

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsurancePolisDataHolderActivity.class);
                intent.putExtra("product", model);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.iv_finish, R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
            case R.id.bt_back:
                finish();
                break;
        }
    }
}
