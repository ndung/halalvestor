package id.halalvestor.activity.credential;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.util.Helper;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    EditText oldPasswordText;
    EditText newPasswordText;
    EditText confirmNewPasswordText;

    Button setPwdButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);
        ButterKnife.bind(this);

        oldPasswordText = findViewById(R.id.input_password);
        newPasswordText = findViewById(R.id.input_password2);
        confirmNewPasswordText = findViewById(R.id.input_password3);
        setPwdButton = findViewById(R.id.btn_change_password);

        setPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changePassword(); }
        });

        tvTitle.setText(getResources().getString(R.string.change_password_page_title));

        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changePassword(){
        Helper.hideKeyboard(this);
        if (oldPasswordText.getText().toString().isEmpty()){
            showErrorMessage(oldPasswordText, getResources().getString(R.string.change_old_password_empty));
        }else if (newPasswordText.getText().toString().isEmpty()){
            showErrorMessage(newPasswordText, getResources().getString(R.string.change_new_password_empty));
        }else if (confirmNewPasswordText.getText().toString().isEmpty()){
            showErrorMessage(confirmNewPasswordText, getResources().getString(R.string.change_confirmed_new_password_empty));
        }else if (!newPasswordText.getText().toString().equals(confirmNewPasswordText.getText().toString())){
            showErrorMessage(confirmNewPasswordText, getResources().getString(R.string.change_oonfirmed_new_password_not_matched));
        }else {
            AuthCredential credential = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), oldPasswordText.getText().toString());
            FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPasswordText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showErrorMessage(newPasswordText, getResources().getString(R.string.change_password_success));
                                } else {
                                    showErrorMessage(newPasswordText, task.getException().getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        showErrorMessage(newPasswordText, task.getException().getLocalizedMessage());
                    }
                }
            });
        }
    }
}
