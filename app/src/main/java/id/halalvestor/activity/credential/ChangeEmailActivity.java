package id.halalvestor.activity.credential;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.util.Helper;

public class ChangeEmailActivity extends BaseActivity {

    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_email_layout);
        ButterKnife.bind(this);

        tvTitle.setText(getResources().getString(R.string.change_email_page_title));
    }

    private void changeEmail(){
        Helper.hideKeyboard(this);
        if (emailText.getText()==null||emailText.getText().toString().isEmpty()){
            showErrorMessage(emailText, getResources().getString(R.string.forget_password_email_empty));
        }else {
            showProgressDialog();
            FirebaseAuth.getInstance().getCurrentUser().updateEmail(emailText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressDialog();
                            if (task.isSuccessful()){
                                finish();
                            } else {
                                showErrorMessage(emailText, task.getException().getLocalizedMessage());
                            }
                        }
                    });
            hideProgressDialog();
        }
    }

    @OnClick({R.id.btn_change_email, R.id.iv_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_change_email:
                changeEmail();
                break;
            case R.id.iv_finish:
                finish();
                break;
        }
    }
}
