package id.halalvestor.activity.main.investment.buysell;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.model.AppUser;
import id.halalvestor.model.InvestmentProduct;

public class SellDetailTransactionRevActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_all)
    CheckBox checkBox;
    @BindView(R.id.tv_net_amount)
    EditText tvUnit;
    @BindView(R.id.tv_sell_amount)
    EditText tvSell;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.bt_before)
    TextView btBefore;

    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private DecimalFormat currencyFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_detail_transaction_rev_layout);
        ButterKnife.bind(this);

        ivFinish.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_close));
        tvTitle.setText("JUAL PRODUK");

        final InvestmentProduct product = (InvestmentProduct) getIntent().getSerializableExtra("product");
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tvUnit.setText(decimalFormat.format(product.getAmount()));
                }
            }
        });

        tvUnit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus&&!tvUnit.getText().toString().isEmpty()) {
                    Number n = Double.parseDouble(tvUnit.getText().toString().replaceAll("[,.]", "").replaceAll("Rp", "").trim());
                    tvUnit.setText(decimalFormat.format(n));
                }
            }
        });

        tvSell.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus&&!tvSell.getText().toString().isEmpty()) {
                    Number n = Double.parseDouble(tvSell.getText().toString().replaceAll("[,.]", "").replaceAll("Rp", "").trim());
                    tvSell.setText(currencyFormat.format(n));
                }
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Map<String, String> params = new HashMap<>();
                    final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
                    params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Double amount = decimalFormat.parse(tvUnit.getText().toString()).doubleValue();
                    if (amount<=product.getAmount()) {
                        product.setUnit(decimalFormat.parse(tvUnit.getText().toString()).doubleValue()/product.getNab());
                        if (tvSell.getText().toString().contains("Rp")) {
                            product.setNab(currencyFormat.parse(tvSell.getText().toString()).doubleValue());
                        } else if (tvSell.getText().toString().contains("\\.")) {
                            product.setNab(decimalFormat.parse(tvSell.getText().toString()).doubleValue());
                        } else if (!tvSell.getText().toString().equals("")){
                            product.setNab(Double.parseDouble(tvSell.getText().toString()));
                        } else{
                            product.setNab(0d);
                        }
                        params.put("product", gson.toJson(product));
                        Log.d(TAG, "product:" + product);
                        connectServerApi("sell_investment_portfolio", params);
                    }else{
                        showErrorMessage(btNext, "Jumlah yang dijual tidak boleh melebihi dari yang dimiliki");
                    }
                }catch (Exception ex){
                    Log.d(TAG, "error", ex);
                }
            }
        });
        btBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().setPrettyPrinting().create();
        AppUser appUser = gson.fromJson(result, AppUser.class);
        App.getInstance().getPreferenceManager().setAppUser(appUser);
        finish();
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        showErrorMessage(btNext, error);
        finish();
    }

    private static final String TAG = SellDetailTransactionRevActivity.class.toString();
}
