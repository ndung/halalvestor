package id.halalvestor.activity.credential;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.annotations.VisibleForTesting;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.Unauthorizable;

public class FingerprintLoginActivity extends AuthenticationActivity implements Unauthorizable {

    FingerprintManagerCompat mFingerprintManager;

    @BindView(R.id.errorTextView)
    TextView mErrorTextView;

    @BindView(R.id.iconImageView)
    ImageView mIcon;

    @BindView(R.id.btn_back)
    Button btnBack;

    FingerprintHelper fingerprintHelper;
    private FingerprintManagerCompat.CryptoObject mCryptoObject;

    private Cipher mCipher;

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                this.finish();
        }
    }

    private static final String TAG = FingerprintLoginActivity.class.toString();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fingerprint_login_layout);
        ButterKnife.bind(this);

        mFingerprintManager = FingerprintManagerCompat.from(this);
        fingerprintHelper = new FingerprintHelper();

        boolean isFingerprintPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
                == PackageManager.PERMISSION_GRANTED;
        //boolean isFingerprintAvailable = mFingerprintManager.isHardwareDetected() && mFingerprintManager.hasEnrolledFingerprints();

        // The user either rejected permission to read their fingerprint, we're on
        // a device that doesn't support it, or the user doesn't have any
        // fingerprints enrolled.
        if (!isFingerprintPermissionGranted || !mFingerprintManager.isHardwareDetected()) {
            mErrorTextView.setText(getResources().getString(R.string.fingerprint_not_available));
            mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.red));
        } else if (!mFingerprintManager.hasEnrolledFingerprints()) {
            mErrorTextView.setText(getResources().getString(R.string.fingerprint_not_enrolled));
            mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.red));
        } else if (FirebaseAuth.getInstance().getCurrentUser()==null
                || !App.getInstance().getPreferenceManager().isFingerprintEnabled(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            mErrorTextView.setText(getResources().getString(R.string.no_fingerprint));
            mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.red));
        } else  if (initCipher()) {
            // Set up the crypto object for later. The object will be authenticated by use
            // of the fingerprint.

            // Show the fingerprint dialog. The user has the option to use the fingerprint with
            // crypto, or you can fall back to using a server-side verified password.
            mCryptoObject = new FingerprintManagerCompat.CryptoObject(mCipher);
        } else {
            mErrorTextView.setText(getResources().getString(R.string.new_fingerprint));
            mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.red));
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean initCipher() {
        try {
            KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(Constants.FINGERPRINT_KEY_NAME, null);

            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCryptoObject!=null) {
            fingerprintHelper.startListening(mCryptoObject);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        fingerprintHelper.stopListening();
    }

    public boolean isFingerprintAuthAvailable() {
        return mFingerprintManager.isHardwareDetected() && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void onAuthenticated() {
        authUser();
    }

    public void onError() {
        fingerprintHelper.stopListening();
        showErrorMessage(mIcon, "Error while reading fingerprint");
    }

    class FingerprintHelper extends FingerprintManagerCompat.AuthenticationCallback{

        @VisibleForTesting
        static final long ERROR_TIMEOUT_MILLIS = 1600;
        @VisibleForTesting
        static final long SUCCESS_DELAY_MILLIS = 1300;

        private CancellationSignal mCancellationSignal;

        public void startListening(FingerprintManagerCompat.CryptoObject cryptoObject) {
            if (!isFingerprintAuthAvailable()) {
                return;
            }
            mCancellationSignal = new CancellationSignal();
            mFingerprintManager.authenticate(cryptoObject, 0 /* flags */, mCancellationSignal, this, null);
        }

        public void stopListening() {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
                mCancellationSignal = null;
            }
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            showError(errString);
                mIcon.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onError();
                    }
                }, ERROR_TIMEOUT_MILLIS);
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            showError(helpString);
        }

        @Override
        public void onAuthenticationFailed() {
            showError(getResources().getString( R.string.fingerprint_not_recognized));
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
            mIcon.setImageResource(R.drawable.ic_fingerprint_success);
            mErrorTextView.setTextColor(getResources().getColor(R.color.success_color));
            mErrorTextView.setText(mErrorTextView.getResources().getString(R.string.fingerprint_success));
            mIcon.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onAuthenticated();
                }
            }, SUCCESS_DELAY_MILLIS);
        }

        private void showError(CharSequence error) {
            mIcon.setImageResource(R.drawable.ic_fingerprint_error);
            mErrorTextView.setText(error);
            mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.red));
            mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
            mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
        }

        @VisibleForTesting
        Runnable mResetErrorTextRunnable = new Runnable() {
            @Override
            public void run() {
                mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.hint_color));
                mErrorTextView.setText("");
                mIcon.setImageResource(R.drawable.ic_finger_print_blue);
            }
        };
    }

}
