package id.halalvestor.activity.main.investment.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.ui.ImagePicker;
import id.halalvestor.util.SingleUploadBroadcastReceiver;

public class InvestmentFormActivity extends BaseActivity implements SingleUploadBroadcastReceiver.Delegate {

    private static final String TAG = InvestmentFormActivity.class.toString();

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.identityNo)
    EditText identityNo;
    @BindView(R.id.bankName)
    EditText bankName;
    @BindView(R.id.recipientName)
    EditText recipientName;
    @BindView(R.id.accountNo)
    EditText accountNo;
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.image_view)
    ImageView ivConfirm;
    @BindView(R.id.iv_upload)
    ImageView ivUpload;
    @BindView(R.id.bt_confirm)
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_form);
        ButterKnife.bind(this);
        tvTitle.setText("DAFTAR NASABAH");
        ivFinish.setImageResource(R.mipmap.ic_close);

        ivConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach(4);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identityNo.getText().toString().equalsIgnoreCase("")){
                    showErrorMessage(identityNo, "Masukkan no KTP Anda");
                }else if (bankName.getText().toString().equalsIgnoreCase("")){
                    showErrorMessage(bankName, "Masukkan nama bank Anda");
                }else if (recipientName.getText().toString().equalsIgnoreCase("")){
                    showErrorMessage(recipientName, "Masukkan nama pemilik rekening");
                }else if (accountNo.getText().toString().equalsIgnoreCase("")){
                    showErrorMessage(accountNo, "Masukkan nomor rekening Anda");
                }else if (ivConfirm.getVisibility()==View.VISIBLE){
                    showErrorMessage(ivConfirm, "Upload bukti KTP Anda");
                }else {
                    startActivity(new Intent(getApplicationContext(), InvestmentFormKYCActivity.class));
                    //uploadMultipart(4);
                }
            }
        });
    }

    private Uri filePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Object[] obj = ImagePicker.getImageFromResult(this, resultCode, data);
        filePath = (Uri) obj[0];//data.getData();
        //ivUpload.setImageURI(null);
        //ivUpload.setImageURI(filePath);
        ivUpload.setImageBitmap((Bitmap)obj[1]);
        ivUpload.setScaleType(ImageView.ScaleType.CENTER);
        ivConfirm.setVisibility(View.GONE);
        tvUpload.setVisibility(View.GONE);
        ivUpload.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgress(int progress) {
        this.showProgressDialog();
    }

    @Override
    public void onError(Exception exception) {
        Log.e(TAG, "excep", exception);
        this.hideProgressDialog();
    }

    @Override
    public void onCompleted(int serverResponseCode, int requestCode, String serverResponseBody) {
        Log.d(TAG, "serverResponseCode:"+serverResponseCode);
        Log.d(TAG, "requestCode:"+requestCode);
        Log.d(TAG, "onCompleted:"+serverResponseBody);
        this.hideProgressDialog();
        if (requestCode==9994) {
            this.finish();
        }
    }

    @Override
    public void onCancelled() {
        this.hideProgressDialog();
    }

    @OnClick({R.id.iv_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
        }
    }
}
