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

public class ConfirmPasswordResetActivity extends BaseActivity implements Unauthorizable {

    @BindView(R.id.input_code)
    EditText codeText;

    @BindView(R.id.btn_next)
    Button nextButton;

    @BindView(R.id.tv_halaman_awal)
    TextView tvHalamanAwal;

    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirm_password_reset_layout);
        ButterKnife.bind(this);

        if (savedInstanceState!=null){
            email = savedInstanceState.getString("email");
        }else {
            email = getIntent().getStringExtra("email");
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("email", email);
        super.onSaveInstanceState(outState);
    }

    private void confirmPasswordReset(){
        Helper.hideKeyboard(this);
        if (!codeText.getText().toString().isEmpty()) {
            showProgressDialog();
            FirebaseAuth.getInstance().verifyPasswordResetCode(codeText.getText().toString()).addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        next();
                    } else {
                        showErrorMessage(codeText, task.getException().getLocalizedMessage());
                    }
                }
            });
            hideProgressDialog();
        }else{
            showErrorMessage(codeText, getResources().getString(R.string.password_reset_code_empty));
        }
    }

    private void next(){
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("code", codeText.getText().toString());
        startActivity(intent);
        this.finish();
    }

    @OnClick({R.id.btn_next, R.id.tv_halaman_awal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                confirmPasswordReset();
                break;
            case R.id.tv_halaman_awal:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
                break;
        }
    }
}
