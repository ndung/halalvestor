package id.halalvestor.activity.main.insurance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.InsuranceProduct;
import id.halalvestor.model.InsuranceUser;
import id.halalvestor.ui.FixedHoloDatePickerDialog;
import id.halalvestor.ui.ImagePicker;

public class InsurancePolisDataHolderActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
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
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.iv_ktp)
    ImageView ivConfirmKtp;
    @BindView(R.id.iv_upload_ktp)
    ImageView ivUploadKtp;
    @BindView(R.id.tv_upload_ktp)
    TextView tvUploadKtp;
    @BindView(R.id.iv_cover_book)
    ImageView ivConfirmBook;
    @BindView(R.id.iv_upload_book)
    ImageView ivUploadBook;
    @BindView(R.id.tv_upload_book)
    TextView tvUploadBook;

    Activity activity;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_polis_data);
        ButterKnife.bind(this);
        activity = this;
        tvTitle.setText("DATA DIRI");

        final InsuranceProduct model = (InsuranceProduct) getIntent().getSerializableExtra("product");

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

        ivConfirmBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach(2);
            }
        });

        ivUploadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach(2);
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Build.VERSION.SDK_INT >= 24) {
                    new FixedHoloDatePickerDialog(activity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }else{
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
                if (isChecked){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AppUser appUser = App.getInstance().getPreferenceManager().getAppUser();
                    name.setText(user.getDisplayName());
                    phone.setText(appUser.getPhoneNumber());
                    email.setText(user.getEmail());
                    dob.setText(bodFormatter.format(appUser.getBirthDate()));
                }else{
                    name.setText("");
                    phone.setText("");
                    email.setText("");
                    dob.setText("");
                }

            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsurancePolisDataInsuredActivity.class);
                InsuranceUser user = new InsuranceUser();
                user.setName(name.getText().toString());
                user.setPhoneNumber(phone.getText().toString());
                user.setEmail(email.getText().toString());
                user.setDateOfBirth(dob.getText().toString());
                user.setPlaceOfBirth(pob.getText().toString());
                user.setIdentityNumber(ktpNo.getText().toString());
                user.setAddress(address.getText().toString());
                user.setIdentityPicFilepath(filePathKtp);
                user.setBookPicFilepath(filePathBook);
                if (user.getName()==null || user.getName().equalsIgnoreCase("")){
                    showErrorMessage(name, "Nama pemegang polis harus diisi");
                }else if (user.getPhoneNumber()==null || user.getPhoneNumber().equalsIgnoreCase("")){
                    showErrorMessage(phone, "No telepon harus diisi");
                }else if (user.getEmail()==null || user.getEmail().equalsIgnoreCase("")){
                    showErrorMessage(email, "Email harus diisi");
                }else if (user.getDateOfBirth()==null || user.getDateOfBirth().equalsIgnoreCase("")){
                    showErrorMessage(dob, "Tanggal lahir harus diisi");
                }else if (user.getPlaceOfBirth()==null || user.getPlaceOfBirth().equalsIgnoreCase("")){
                    showErrorMessage(pob, "Tempat lahir harus diisi");
                }else if (user.getIdentityNumber()==null || user.getIdentityNumber().equalsIgnoreCase("")){
                    showErrorMessage(ktpNo, "No KTP harus diisi");
                }else if (user.getAddress()==null || user.getAddress().equalsIgnoreCase("")){
                    showErrorMessage(address, "Alamat harus diisi");
                }else if (user.getIdentityPicFilepath()==null || user.getIdentityPicFilepath().equalsIgnoreCase("")){
                    showErrorMessage(ivUploadKtp, "Foto KTP harus dimasukkan");
                }else if (user.getBookPicFilepath()==null || user.getBookPicFilepath().equalsIgnoreCase("")){
                    showErrorMessage(ivUploadBook, "Foto cover buku tabungan harus dimasukkan");
                }else {
                    intent.putExtra("polis_holder", user);
                    intent.putExtra("product", model);
                    startActivity(intent);
                }
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

    @OnClick({R.id.iv_finish, R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
            case R.id.bt_back:
                finish();
                break;
        }
    }

    private static final String TAG = InsurancePolisDataHolderActivity.class.toString();

    private String filePathKtp, filePathBook;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Object[] obj = ImagePicker.getImageFromResult(this, resultCode, data);
        if (requestCode==9991) {
            Bitmap bitmap = (Bitmap) obj[1];
            filePathKtp = (String) obj[2];
            ivUploadKtp.setImageBitmap(bitmap);
            ivUploadKtp.setScaleType(ImageView.ScaleType.CENTER);
            ivConfirmKtp.setVisibility(View.GONE);
            tvUploadKtp.setVisibility(View.GONE);
            ivUploadKtp.setVisibility(View.VISIBLE);
        }else if (requestCode==9992) {
            Bitmap bitmap = (Bitmap) obj[1];
            filePathBook = (String) obj[2];
            ivUploadBook.setImageBitmap(bitmap);
            ivUploadBook.setScaleType(ImageView.ScaleType.CENTER);
            ivConfirmBook.setVisibility(View.GONE);
            tvUploadBook.setVisibility(View.GONE);
            ivUploadBook.setVisibility(View.VISIBLE);
        }
    }
}
