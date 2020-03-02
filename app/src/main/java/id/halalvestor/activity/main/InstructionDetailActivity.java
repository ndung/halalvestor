package id.halalvestor.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.model.Instruction;

public class InstructionDetailActivity extends BaseActivity {

    @BindView(R.id.tv_subject)
    TextView tvSubject;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_buysell)
    Button btnBuySell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_detail_layout);
        ButterKnife.bind(this);

        Instruction model = (Instruction) getIntent().getSerializableExtra("data");

        if(model != null){
            tvSubject.setText(model.getSubject());
            tvDescription.setText(model.getDescription());
        }

        tvTitle.setText("Petunjuk Investasi");

    }

    @OnClick({R.id.btn_buysell, R.id.iv_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_buysell:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "buysell");
                startActivity(intent);
                this.finish();
                break;
            case R.id.iv_finish:
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
