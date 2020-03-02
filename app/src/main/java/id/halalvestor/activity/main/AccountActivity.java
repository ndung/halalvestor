package id.halalvestor.activity.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.crypto.KeyGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.credential.ChangeEmailActivity;
import id.halalvestor.activity.credential.ChangePasswordActivity;
import id.halalvestor.activity.credential.ChangePhoneNumberActivity;
import id.halalvestor.model.AppUser;
import id.halalvestor.ui.CircleTransformer;
import id.halalvestor.util.SingleUploadBroadcastReceiver;

public class AccountActivity extends BaseActivity implements SingleUploadBroadcastReceiver.Delegate{

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_no_hp)
    TextView tvNoHp;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_reg_date)
    TextView tvRegDate;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tv_trx_history)
    TextView tvTrxHistory;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_change)
    Button btnChange;

    SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("id"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_account_layout);

        ButterKnife.bind(this);
        tvTitle.setText("Ubah Akun");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tvName.setText(firebaseUser.getDisplayName());
        tvEmail.setText(firebaseUser.getEmail());

        if (firebaseUser.getPhotoUrl()!=null) {
            // Loading profile image
            Glide.with(this).load(firebaseUser.getPhotoUrl())
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransformer(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

        AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
        tvNoHp.setText(appUser.getPhoneNumber());
        tvRegDate.setText(getResources().getString(R.string.member_since) + " " + sdf.format(appUser.getRegDate()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //boolean isEnabled = App.getInstance().getPreferenceManager().isFingerprintEnabled(user.getUid());
        //if (isEnabled){
        //    btnChange.setText("nonaktifkan fingerprint ID");
        //}else {
        //    btnChange.setText("gunakan fingerprint ID");
        //}

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDisplayPicture();
            }
        });
    }

    private void changeDisplayPicture(){
        this.setDelegate(this);
        this.showFileChooser(3);
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tvName.setText(firebaseUser.getDisplayName());
        tvEmail.setText(firebaseUser.getEmail());
        AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
        tvNoHp.setText(appUser.getPhoneNumber());
    }

    private static final String TAG = SettingFragment.class.toString();

    @OnClick({R.id.iv_finish, R.id.tv_edit_email, R.id.iv_edit_email, R.id.tv_edit_password, R.id.iv_edit_password, R.id.tv_edit_nohp, R.id.iv_edit_nohp, R.id.tv_fingerprint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                Log.d(TAG, "weird");
                finish();
                break;
            case R.id.tv_edit_nohp:
            case R.id.iv_edit_nohp:
                startActivity(new Intent(this, ChangePhoneNumberActivity.class));
                this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
            case R.id.tv_edit_password:
            case R.id.iv_edit_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
            case R.id.tv_edit_email:
            case R.id.iv_edit_email:
                startActivity(new Intent(this, ChangeEmailActivity.class));
                this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
//            case R.id.tv_logout:
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                App.getInstance().getPreferenceManager().updateLastActive(firebaseUser.getUid(), 0);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
//
//                getActivity().finish();
//                break;
//            case R.id.tv_trx_history:
//                startActivity(new Intent(getActivity(), HistoryTransactionActivity.class));
//                getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
//                break;
//            case R.id.iv_trx_history:
//                startActivity(new Intent(getActivity(), HistoryTransactionActivity.class));
//                getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
//                break;
            case R.id.tv_fingerprint:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean isEnabled = App.getInstance().getPreferenceManager().isFingerprintEnabled(user.getUid());
                if (isEnabled){
                    App.getInstance().getPreferenceManager().setEnabledFingerprint(user.getUid(), false);
                    Snackbar.make(btnChange, "Opsi masuk dengan fingerprint berhasil dinonaktifkan", Snackbar.LENGTH_LONG).show();
                    //btnChange.setText("gunakan fingerprint ID");
                } else {
                    createKey();
                    Snackbar.make(btnChange, "Opsi masuk dengan fingerprint berhasil diaktifkan", Snackbar.LENGTH_LONG).show();
                    //btnChange.setText("nonaktifkan fingerprint ID");
                }
                break;
            //case R.id.btn_remove:
            //    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //    App.getInstance().getPreferenceManager().setEnabledFingerprint(user.getUid(), false);
            //    Snackbar.make(btnChange, "Opsi masuk dengan fingerprint berhasil dinonaktifkan", Snackbar.LENGTH_LONG).show();
            //    break;
        }

    }

    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);

            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(Constants.FINGERPRINT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | KeyStoreException
                | CertificateException | NoSuchProviderException | IOException e) {
            throw new RuntimeException(e);
        }
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        App.getInstance().getPreferenceManager().setEnabledFingerprint(firebaseUser.getUid(), true);
    }

    @Override
    public void onProgress(int progress) {
        this.showProgressDialog();
    }

    @Override
    public void onError(Exception exception) {
        this.hideProgressDialog();
    }

    @Override
    public void onCompleted(int serverResponseCode, int requestCode, String serverResponseBody) {
        this.hideProgressDialog();
        if (requestCode==9993) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(serverResponseBody))
                    .build();
            firebaseUser.updateProfile(request);
            Glide.with(this).load(serverResponseBody)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransformer(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    @Override
    public void onCancelled() {
        this.hideProgressDialog();
    }

}
