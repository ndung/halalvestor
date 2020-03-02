package id.halalvestor.activity.credential;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.model.AppUser;
import id.halalvestor.util.Helper;

public class ChangePhoneNumberActivity extends BaseActivity {

    private static final String TAG = ChangePhoneNumberActivity.class.toString();

    private boolean mVerificationInProgress = false;
    private String mVerificationId;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @BindView(R.id.input_nohp)
    EditText inputNoHp;
    @BindView(R.id.input_code)
    EditText inputCode;
    @BindView(R.id.tv_resend_code)
    TextView resendCode;
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_change_nohp)
    Button btnChangeNoHP;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phonenumber_layout);
        ButterKnife.bind(this);

        tvTitle.setText(getResources().getString(R.string.change_nohp_page_title));
        inputCode.setVisibility(View.GONE);
        resendCode.setVisibility(View.GONE);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                showErrorMessage(inputNoHp, e.getLocalizedMessage());

                /**if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    showErrorMessage(inputNoHp, getResources().getString(R.string.invalid_hp));
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    showErrorMessage(inputNoHp,  getResources().getString(R.string.quota_exceeded));
                    // [END_EXCLUDE]
                }else {
                    showErrorMessage(inputNoHp, e.getLocalizedMessage());
                }*/
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                showNotificationMessage(inputCode, getResources().getString(R.string.verification_code_sent));

                inputCode.setVisibility(View.VISIBLE);
                resendCode.setVisibility(View.VISIBLE);
                btnChangeNoHP.setText(getResources().getString(R.string.verify));
            }
        };
    }

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    @Override
    public void onStart() {
        super.onStart();
        // [START_EXCLUDE]
        if (mVerificationInProgress) {
            startPhoneNumberVerification(inputNoHp.getText().toString());
        }
        // [END_EXCLUDE]
    }

    @OnClick({R.id.iv_finish, R.id.tv_resend_code, R.id.btn_change_nohp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_resend_code:
                Helper.hideKeyboard(this);
                resendVerificationCode(inputNoHp.getText().toString(), mResendToken);
                break;
            case R.id.btn_change_nohp:
                Helper.hideKeyboard(this);
                if (inputNoHp.getText().toString().isEmpty()){
                    showErrorMessage(inputCode, getResources().getString(R.string.change_hp_empty));
                }
                else{
                    changePhoneNumber(inputNoHp.getText().toString());
                }
                /**if (btnChangeNoHP.getText().toString().equalsIgnoreCase(getResources().getString(R.string.verify))){
                    verifyPhoneNumberWithCode(mVerificationId, inputCode.getText().toString());
                }else if (btnChangeNoHP.getText().toString().equalsIgnoreCase(getResources().getString(R.string.change))){
                    startPhoneNumberVerification(inputNoHp.getText().toString());
                }*/
                break;
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        if (phoneNumber.isEmpty()){
            showErrorMessage(inputCode, getResources().getString(R.string.change_hp_empty));
        }else {
            // [START start_phone_auth]
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
            // [END start_phone_auth]

            mVerificationInProgress = true;
        }
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        if (code.isEmpty()){
            showErrorMessage(inputCode, getResources().getString(R.string.code_verification_empty));
        } else {
            // [START verify_with_code]
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // [END verify_with_code]
            signInWithPhoneAuthCredential(credential);
        }
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]


    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseAuth.getInstance().getCurrentUser().updatePhoneNumber(credential);
                            FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential);
                            finish();
                            // [END_EXCLUDE]
                        } else {
                            showErrorMessage(inputCode, task.getException().getLocalizedMessage());

                            /** Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                showErrorMessage(inputCode, getResources().getString(R.string.invalid_verification_code));
                                // [END_EXCLUDE]
                            }else{
                                showErrorMessage(inputCode, task.getException().getLocalizedMessage());
                            }*/
                        }
                    }
                });
    }

    private void changePhoneNumber(String phoneNumber){
        Map<String,String> params = new HashMap<>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("phone_number", phoneNumber);
        connectServerApi("update_phone_number", params);
    }

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        AppUser appUser = gson.fromJson(result, AppUser.class);
        if (appUser!=null){
            App.getInstance().getPreferenceManager().setAppUser(appUser);
        }
        finish();
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);
        showErrorMessage(btnChangeNoHP, error);
    }
}