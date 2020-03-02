package id.halalvestor.activity.credential;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.activity.WelcomeActivity;
import id.halalvestor.model.LoginWrapper;
import id.halalvestor.util.Helper;

public class SetPasswordActivity extends BaseActivity {

    private static final String TAG = SetPasswordActivity.class.toString();

    private AuthCredential authCredential;
    private LoginWrapper loginWrapper;

    EditText passwordText;
    Button setPwdButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_password_layout);

        authCredential = getIntent().getParcelableExtra("credential");
        loginWrapper = (LoginWrapper) getIntent().getSerializableExtra("data");

        passwordText = findViewById(R.id.input_password);
        setPwdButton = findViewById(R.id.btn_set_password);

        setPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void changePassword(){
        Helper.hideKeyboard(this);
        if (passwordText.getText()==null || passwordText.getText().toString().isEmpty()){
            showErrorMessage(passwordText, getResources().getString(R.string.set_new_password_empty));
        }else {
            FirebaseAuth.getInstance().getCurrentUser().reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(passwordText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showErrorMessage(passwordText, getResources().getString(R.string.set_new_password_success));
                                    next();
                                } else {
                                    showErrorMessage(passwordText, task.getException().getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        showErrorMessage(passwordText, task.getException().getLocalizedMessage());
                    }
                }
            });
        }
    }

    private void next(){
        Intent intent = new Intent(this, MainActivity.class);
        if (loginWrapper.isNewUser() || loginWrapper.getSurveyAnswers()==null || loginWrapper.getSurveyAnswers().isEmpty()) {
            intent = new Intent(this, WelcomeActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        this.finish();
    }

    @Override
    public void onBackPressed() {

    }
}
