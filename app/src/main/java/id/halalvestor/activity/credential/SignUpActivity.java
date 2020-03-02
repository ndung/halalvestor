package id.halalvestor.activity.credential;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.SplashActivity;
import id.halalvestor.activity.Unauthorizable;
import id.halalvestor.util.Helper;

public class SignUpActivity extends AuthenticationActivity implements Unauthorizable {

    private static final String TAG = SignUpActivity.class.toString();

    @BindView(R.id.btn_signup)
    Button signUpButton;

    @BindView(R.id.btn_signup_with_fb)
    ImageView signUpWithFbButton;

    @BindView(R.id.btn_signup_with_gmail)
    ImageView signUpWithGoogleButton;

    @BindView(R.id.input_email)
    EditText usernameText;

    @BindView(R.id.input_password)
    EditText passwordText;

    @BindView(R.id.input_name)
    EditText nameText;

    @BindView(R.id.input_password2)
    EditText passwordText2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_signup, R.id.btn_signup_with_fb, R.id.btn_signup_with_gmail, R.id.tv_halaman_awal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                Helper.hideKeyboard(this);
                if (usernameText.getText()==null || usernameText.getText().toString().isEmpty()){
                    showErrorMessage(usernameText, getResources().getString(R.string.signup_email_empty));
                }else if (passwordText.getText()==null || passwordText.getText().toString().isEmpty()){
                    showErrorMessage(usernameText, getResources().getString(R.string.signup_password_empty));
                }else if (nameText.getText()==null || nameText.getText().toString().isEmpty()){
                    showErrorMessage(usernameText, getResources().getString(R.string.signup_name_empty));
                }else if (passwordText2.getText()==null || passwordText2.getText().toString().isEmpty()){
                    showErrorMessage(passwordText2, getResources().getString(R.string.signup_confirm_password_empty));
                }else {
                    if (passwordText.getText().toString().equals(passwordText2.getText().toString())) {
                        createAccount(usernameText.getText().toString(), passwordText.getText().toString(), nameText.getText().toString());
                    }else{
                        showErrorMessage(passwordText2, getResources().getString(R.string.signup_confirm_password_not_matched));
                    }
                }
                break;
            case R.id.btn_signup_with_fb:
                authenticationWithFacebook();
                break;
            case R.id.btn_signup_with_gmail:
                authenticationWithGoogle();
                break;
            case R.id.tv_halaman_awal:
                back();
                break;
        }
    }

    private void createAccount(String email, String password, final String name) {
        showProgressDialog();

        // [START create_user_with_email]
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification(name);
                        } else {
                            showErrorMessage(signUpButton, task.getException().getMessage());
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void sendEmailVerification(final String name){
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profile);
                            showNotificationMessage(usernameText, getResources().getString(R.string.signup_email_verification_sent));
                        } else {
                            showErrorMessage(usernameText, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    private void back(){
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        this.finish();
    }
}
