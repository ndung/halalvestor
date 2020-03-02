package id.halalvestor.activity.main.insurance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Global;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.main.history.InsuranceOngoingBuyActivity;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.InsuranceProduct;
import id.halalvestor.model.InsuranceUser;
import id.halalvestor.model.TransactionRequest;
import id.halalvestor.ui.FixedHoloDatePickerDialog;
import id.halalvestor.ui.ImagePicker;

public class InsurancePolisDataInsuredActivity extends BaseActivity {

    private static final String TAG = InsurancePolisDataInsuredActivity.class.toString();

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.et_name_user_polis)
    EditText name;
    @BindView(R.id.et_phone)
    EditText phone;
    @BindView(R.id.et_email)
    EditText email;
    @BindView(R.id.et_date_of_birth)
    EditText dob;
    @BindView(R.id.et_place_of_birth)
    EditText pob;
    @BindView(R.id.et_no_ktp)
    EditText ktpNo;
    @BindView(R.id.et_address)
    EditText address;
    @BindView(R.id.iv_ktp)
    ImageView ivConfirmKtp;
    @BindView(R.id.iv_upload_ktp)
    ImageView ivUploadKtp;
    @BindView(R.id.tv_upload_ktp)
    TextView tvUploadKtp;
    @BindView(R.id.tv_add_polis_insured)
    TextView tvAddPolisInsured;
    @BindView(R.id.bt_next)
    Button btNext;

    Activity activity;

    Calendar myCalendar = Calendar.getInstance();

    int noData = 0;
    InsuranceProduct model;
    InsuranceUser polisHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_polis_data_insured);
        ButterKnife.bind(this);
        activity = this;
        tvTitle.setText("DATA DIRI");

        model = (InsuranceProduct) getIntent().getSerializableExtra("product");
        polisHolder = (InsuranceUser) getIntent().getSerializableExtra("polis_holder");

        noData = getIntent().getIntExtra("no_data", 1);
        tvSubtitle.setText("Pihak Tertanggung " + noData);

        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Build.VERSION.SDK_INT >= 24) {
                    new FixedHoloDatePickerDialog(activity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                } else {
                    new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
                    name.setText(polisHolder.getName());
                    phone.setText(polisHolder.getPhoneNumber());
                    email.setText(polisHolder.getEmail());
                    dob.setText(polisHolder.getDateOfBirth());
                    pob.setText(polisHolder.getPlaceOfBirth());
                    address.setText(polisHolder.getAddress());
                    ktpNo.setText(polisHolder.getIdentityNumber());
                    filePathKtp = polisHolder.getIdentityPicFilepath();
                    ivUploadKtp.setImageBitmap(BitmapFactory.decodeFile(filePathKtp));
                    ivUploadKtp.setScaleType(ImageView.ScaleType.CENTER);
                    ivConfirmKtp.setVisibility(View.GONE);
                    tvUploadKtp.setVisibility(View.GONE);
                    ivUploadKtp.setVisibility(View.VISIBLE);
                } else {
                    name.setText("");
                    phone.setText("");
                    email.setText("");
                    dob.setText("");
                    pob.setText("");
                    address.setText("");
                    ktpNo.setText("");
                    filePathKtp = "";
                    ivConfirmKtp.setVisibility(View.VISIBLE);
                    tvUploadKtp.setVisibility(View.VISIBLE);
                    ivUploadKtp.setVisibility(View.GONE);
                }
            }
        });


        ivConfirmKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach(1);
            }
        });

        ivUploadKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach(1);
            }
        });

        tvAddPolisInsured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsurancePolisDataInsuredActivity.class);
                intent.putExtra("no_data", (noData + 1));
                intent.putExtra("product", model);
                intent.putExtra("polis_holder", polisHolder);
                startActivity(intent);
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTransaction(model);
            }
        });
    }


    DatePickerDialog.OnDateSetListener date = new FixedHoloDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    SimpleDateFormat bodFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private void updateLabel() {
        dob.setText(bodFormatter.format(myCalendar.getTime()));
    }

    @OnClick({R.id.iv_finish, R.id.iv_ktp, R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
            case R.id.bt_back:
                finish();
                break;
            case R.id.iv_ktp:
                break;
        }
    }

    private void insertTransaction(InsuranceProduct model){
        TransactionRequest request = new TransactionRequest();
        request.setInsuranceProduct(model);
        request.setAmount(model.getPremium());
        request.setType(2);
        Global.TRANSACTION_REQUEST = request;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("type", String.valueOf(Global.TRANSACTION_REQUEST.getType()));
        params.put("product_id", String.valueOf(Global.TRANSACTION_REQUEST.getInsuranceProduct().getId()));
        params.put("amount", String.valueOf(Global.TRANSACTION_REQUEST.getAmount()));
        connectServerApi("insert_transaction", params);
    }

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().setPrettyPrinting().create();
        AppUser appUser = gson.fromJson(result, AppUser.class);
        App.getInstance().getPreferenceManager().setAppUser(appUser);
        Intent intent = new Intent(getApplicationContext(), InsuranceOngoingBuyActivity.class);
        intent.putExtra("product", model);
        startActivity(intent);
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);

        showErrorMessage(btNext, error);
    }

    private String filePathKtp;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Object[] obj = ImagePicker.getImageFromResult(this, resultCode, data);
        Bitmap bitmap = (Bitmap) obj[1];
        filePathKtp = (String) obj[2];
        ivUploadKtp.setImageBitmap(bitmap);
        ivUploadKtp.setScaleType(ImageView.ScaleType.CENTER);
        ivConfirmKtp.setVisibility(View.GONE);
        tvUploadKtp.setVisibility(View.GONE);
        ivUploadKtp.setVisibility(View.VISIBLE);
    }
}
