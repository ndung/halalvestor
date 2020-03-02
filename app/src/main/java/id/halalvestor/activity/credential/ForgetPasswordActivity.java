package id.halalvestor.activity.credential;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.Unauthorizable;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.util.Helper;

public class ForgetPasswordActivity extends BaseActivity implements Unauthorizable {

    @BindView(R.id.input_email)
    EditText emailText;

    @BindView(R.id.btn_next)
    Button nextButton;

    @BindView(R.id.tv_halaman_awal)
    TextView tvHalamanAwal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forget_password_layout);
        ButterKnife.bind(this);
    }

    private void resetPassword(){
        Helper.hideKeyboard(this);
        if (emailText.getText()==null||emailText.getText().toString().isEmpty()){
            showErrorMessage(emailText, getResources().getString(R.string.forget_password_email_empty));
        }else {
            showProgressDialog();
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressDialog();
                            if (task.isSuccessful()){
                                next();
                            } else {
                                showErrorMessage(emailText, task.getException().getLocalizedMessage());
                            }
                        }
                    });
            hideProgressDialog();
        }
    }

    private void next(){
        Intent intent = new Intent(this, ConfirmPasswordResetActivity.class);
        intent.putExtra("email", emailText.getText().toString());
        startActivity(intent);
        this.finish();
    }

    @OnClick({R.id.btn_next, R.id.tv_halaman_awal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                resetPassword();
                break;
            case R.id.tv_halaman_awal:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
                break;
        }
    }
}
