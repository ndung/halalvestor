package id.halalvestor.activity.credential;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.SplashActivity;
import id.halalvestor.activity.Unauthorizable;
import id.halalvestor.util.Helper;

public class LoginActivity extends AuthenticationActivity implements Unauthorizable{

    @BindView(R.id.btn_login)
    Button loginButton;

    @BindView(R.id.input_email)
    EditText usernameText;

    @BindView(R.id.input_password)
    EditText passwordText;

    @BindView(R.id.login_with_fb)
    ImageView loginWithFb;

    @BindView(R.id.login_with_gmail)
    ImageView loginWithGoogle;

    @BindView(R.id.login_with_fingerprint)
    ImageView loginWithFingerprint;

    @BindView(R.id.tv_lupa_password)
    TextView tvLupaPassword;

    @BindView(R.id.tv_halaman_awal)
    TextView tvHalamanAwal;

    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        // making notification bar transparent
        changeStatusBarColor();

    }

    @OnClick({R.id.btn_login, R.id.login_with_fb, R.id.login_with_gmail, R.id.login_with_fingerprint, R.id.tv_lupa_password, R.id.tv_halaman_awal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Helper.hideKeyboard(this);
                if (usernameText.getText()==null || usernameText.getText().toString().isEmpty()){
                    showErrorMessage(usernameText, getResources().getString(R.string.login_email_empty));
                }else if (passwordText.getText()==null || passwordText.getText().toString().isEmpty()){
                    showErrorMessage(passwordText, getResources().getString(R.string.login_password_empty));
                }else {
                    signIn(usernameText.getText().toString(), passwordText.getText().toString());
                }
                break;
            case R.id.login_with_fb:
                authenticationWithFacebook();
                break;
            case R.id.login_with_gmail:
                authenticationWithGoogle();
                break;
            case R.id.login_with_fingerprint:
                loginWithFingerprint();
                break;
            case R.id.tv_lupa_password:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
            case R.id.tv_halaman_awal:
                back();
                break;
        }
    }

    private static final int FINGERPRINT_PERMISSION_REQUEST_CODE = 0;

    private void loginWithFingerprint(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            nextToLoginWithFingerprint();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, FINGERPRINT_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] state) {
        boolean isFingerprintPermissionGranted = state[0] == PackageManager.PERMISSION_GRANTED;

        if (isFingerprintPermissionGranted) {
            nextToLoginWithFingerprint();
        }else{
            showErrorMessage(loginWithFingerprint, getResources().getString(R.string.fingerprint_permission_not_granted));
        }
    }

    private void nextToLoginWithFingerprint(){
        startActivity(new Intent(this, FingerprintLoginActivity.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public void onEmailNotVerified(String errorMessage) {
        Snackbar snackbar = Snackbar.make(loginButton, errorMessage, Snackbar.LENGTH_LONG);
        snackbar.setAction(getResources().getString(R.string.login_resend_verification_email), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
            }
        });
        snackbar.show();
    }

    private static final String TAG = LoginActivity.class.getSimpleName();

    private void back(){
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        this.finish();
    }
}
