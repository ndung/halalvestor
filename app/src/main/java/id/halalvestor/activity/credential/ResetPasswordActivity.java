package id.halalvestor.activity.credential;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ResetPasswordActivity extends BaseActivity implements Unauthorizable {

    @BindView(R.id.input_password)
    EditText pwdText;

    @BindView(R.id.btn_next)
    Button nextButton;

    String code;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reset_password_layout);
        ButterKnife.bind(this);

        if (savedInstanceState!=null){
            code = savedInstanceState.getString("code");
            email = savedInstanceState.getString("email");
        }else {
            email = getIntent().getStringExtra("email");
            code = getIntent().getStringExtra("code");
        }
    }

    private void confirmPasswordReset(){
        Helper.hideKeyboard(this);
        if (!pwdText.getText().toString().isEmpty()) {
            showProgressDialog();
            FirebaseAuth.getInstance().confirmPasswordReset(code, pwdText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        next();
                        //showNotificationMessage(pwdText, getResources().getString(R.string.new_password_reset_success));
                    } else {
                        showErrorMessage(pwdText, task.getException().getLocalizedMessage());
                    }
                }
            });
            hideProgressDialog();
        }else{
            showErrorMessage(pwdText, getResources().getString(R.string.new_password_reset_empty));
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("code", code);
        outState.putString("email", email);
        super.onSaveInstanceState(outState);
    }

    private void next(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", pwdText.getText().toString());
        startActivity(intent);
        this.finish();
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                confirmPasswordReset();
                break;
        }
    }
}
