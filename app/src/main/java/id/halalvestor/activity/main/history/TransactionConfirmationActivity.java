package id.halalvestor.activity.main.history;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.model.Transaction;
import id.halalvestor.ui.ImagePicker;
import id.halalvestor.util.Helper;
import id.halalvestor.util.SingleUploadBroadcastReceiver;

public class TransactionConfirmationActivity extends BaseActivity implements SingleUploadBroadcastReceiver.Delegate {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.image_view)
    ImageView ivConfirm;
    @BindView(R.id.iv_upload)
    ImageView ivUpload;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.bt_confirm)
    Button btnConfirm;

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_upload)
    TextView tvUpload;

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id"));

    private static final String TAG = TransactionConfirmationActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_confirmation);
        ButterKnife.bind(this);

        tvTitle.setText("KONFIRMASI");

        ivConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach(4);
            }
        });

        transaction = (Transaction) getIntent().getSerializableExtra("transaction");

        String date = sdf.format(transaction.getTrxDate());

        tvDate.setText(date.substring(0, 2));
        tvMonth.setText(date.substring(3, 6));
        tvYear.setText(date.substring(7, 11));

        switch (transaction.getTrxType()) {
            case 1:
                tvType.setText("JUAL");
                break;
            case 2:
                tvType.setText("BELI");
                break;
        }
        if (transaction.getProductCategory()==1){
            tvSubtitle.setText(transaction.getDetail().get("product.name"));
            tvDesc.setText(transaction.getDetail().get("product.investmentManager"));
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvBankName.getText().toString().equalsIgnoreCase("")){
                    showErrorMessage(tvBankName, "Masukkan nama bank Anda");
                }else if (tvAccountNumber.getText().toString().equalsIgnoreCase("")){
                    showErrorMessage(tvAccountNumber, "Masukkan nomor rekening Anda");
                }else if (tvAccountName.getText().toString().equalsIgnoreCase("")){
                    showErrorMessage(tvAccountNumber, "Masukkan nama pemilik rekening");
                }else if (ivConfirm.getVisibility()==View.VISIBLE){
                    showErrorMessage(ivConfirm, "Upload bukti transfer Anda");
                }else {
                    upload(4);
                }
            }
        });
    }

    Transaction transaction;

    private void upload(int requestCode) {
        try {
            //String path = imageCompressor.compressImageFromFilePath(getPath(filePath));
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setRequestCode(requestCode);
            uploadReceiver.setUploadID(uploadId);
            String url = Constants.API_URL+"?"
                    + Base64.encodeToString("uid".getBytes(), Base64.NO_WRAP) + "=" + Helper.encrypt(FirebaseAuth.getInstance().getCurrentUser().getUid()) + "&"
                    + Base64.encodeToString("tid".getBytes(), Base64.NO_WRAP) + "=" + Helper.encrypt(String.valueOf(transaction.getId())) + "&"
                    + Base64.encodeToString("bank_name".getBytes(), Base64.NO_WRAP) + "=" + Helper.encrypt(tvBankName.getText().toString()) + "&"
                    + Base64.encodeToString("account_no".getBytes(), Base64.NO_WRAP)+ "=" + Helper.encrypt(tvAccountNumber.getText().toString()) + "&"
                    + Base64.encodeToString("account_name".getBytes(), Base64.NO_WRAP)+ "=" + Helper.encrypt(tvAccountName.getText().toString());
            new MultipartUploadRequest(this, uploadId, url)
                    //.setMethod(httpMethod)
                    .setUtf8Charset()
                    .addHeader("req_type", Base64.encodeToString("confirm_transaction".getBytes(), Base64.NO_WRAP))
                    //.addParameter(Base64.encodeToString("uid".getBytes(), Base64.NO_WRAP), Helper.encrypt(firebaseUser.getUid()))
                    //.addParameter(Base64.encodeToString("tid".getBytes(), Base64.NO_WRAP), Helper.encrypt(String.valueOf(transaction.getId())))
                    //.addParameter(Base64.encodeToString("bank_name".getBytes(), Base64.NO_WRAP), Helper.encrypt(tvBankName.getText().toString()))
                    //.addParameter(Base64.encodeToString("account_no".getBytes(), Base64.NO_WRAP), Helper.encrypt(tvAccountNumber.getText().toString()))
                    //.addParameter(Base64.encodeToString("account_name".getBytes(), Base64.NO_WRAP), Helper.encrypt(tvAccountName.getText().toString()))
                    .addFileToUpload(filePath, "img")
                    //.setAutoDeleteFilesAfterSuccessfulUpload(true)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setUsesFixedLengthStreamingMode(true)
                    .startUpload();

        } catch (Exception exc) {
            Log.e(TAG, "error", exc);
        }
    }

    @OnClick({R.id.iv_finish, R.id.tv_back})
    public void onViewClicked() {
        this.finish();
    }

    private String filePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //filePath = data.getData();
        Object[] obj = ImagePicker.getImageFromResult(this, resultCode, data);
        Bitmap bitmap = (Bitmap) obj[1];
        filePath = (String) obj[2];
        ivUpload.setImageBitmap(bitmap);
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

}
