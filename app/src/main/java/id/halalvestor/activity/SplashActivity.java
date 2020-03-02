package id.halalvestor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.credential.AuthenticationActivity;
import id.halalvestor.activity.credential.LoginActivity;
import id.halalvestor.activity.credential.SignUpActivity;

public class SplashActivity extends AuthenticationActivity{

    private static final String TAG = SplashActivity.class.toString();

    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);

        /**if (FirebaseAuth.getInstance().getCurrentUser()!=null
                && (System.currentTimeMillis()-App.getInstance().getPreferenceManager().getLastActive(FirebaseAuth.getInstance().getCurrentUser().getUid())) < Constants.ACTIVE_TIME_MS){
            connectServerApi("key_exchange_to_main", null);
        }*/
    }

    @OnClick({R.id.btn_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                connectServerApi("key_exchange_to_login", null);
                break;
            case R.id.tv_register:
                connectServerApi("key_exchange_to_signup", null);
                break;
        }
    }

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        if (requestHeader.contains("key_exchange")) {
            String encryptionKey = new String(Base64.decode(result, Base64.NO_WRAP));
            App.getInstance().getPreferenceManager().setEncryptionKey(encryptionKey);
            if (requestHeader.contains("to_login")) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            } else if (requestHeader.contains("to_signup")) {
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            } else if (requestHeader.contains("to_main")) {
                authUser();
            }
        }
        else{
            super.onSuccessfullApiConnect(requestHeader, result);
        }
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);
        showErrorMessage(loginButton, error);
    }

}
