package id.halalvestor.activity.main.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.model.InsuranceProduct;
import id.halalvestor.ui.UITagHandler;

public class InsuranceShopDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_rawat_inap)
    TextView tvRawatInap;
    @BindView(R.id.tv_rawat_jalan)
    TextView tvRawatJalan;
    @BindView(R.id.tv_santunan)
    TextView tvSantunan;
    @BindView(R.id.tv_harga_premi)
    TextView tvHargaPremi;
    @BindView(R.id.tv_frequency)
    TextView tvFrequency;
    @BindView(R.id.tv_manfaat)
    TextView tvManfaat;
    @BindView(R.id.tv_term)
    TextView tvTerm;
    @BindView(R.id.bt_choice)
    Button next;

    private DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_shop_detail);
        ButterKnife.bind(this);

        final InsuranceProduct model = (InsuranceProduct) getIntent().getSerializableExtra("product");
        Map<String,String> map = model.getDescription();

        tvTitle.setText("DETAIL PRODUK");
        tvDesc.setText(model.getName());
        String rawatInap = "", rawatJalan = "", santunanKematian = "", frequency = "", manfaat = "", ketentuan = "";
        if (map.containsKey("rawatInap")){
            rawatInap = map.get("rawatInap");
        }
        if (map.containsKey("rawatJalan")){
            rawatJalan = map.get("rawatJalan");
        }
        if (map.containsKey("santunanKematian")){
            santunanKematian = map.get("santunanKematian");
        }
        if (map.containsKey("frequency")){
            frequency = map.get("frequency");
        }
        if (map.containsKey("manfaat")){
            manfaat = map.get("manfaat");
        }
        if (map.containsKey("ketentuan")){
            ketentuan = map.get("ketentuan");
        }
        tvRawatInap.setText(rawatInap);
        tvRawatJalan.setText(rawatJalan);
        tvSantunan.setText(santunanKematian);
        tvHargaPremi.setText(decimalFormat.format(model.getPremium()));
        tvFrequency.setText(frequency);
        tvManfaat.setText(Html.fromHtml(manfaat));
        tvTerm.setText(Html.fromHtml(ketentuan, null, new UITagHandler()));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsuranceSummaryDetailActivity.class);
                intent.putExtra("product", model);
                startActivity(intent);
            }
        });
    }



    @OnClick(R.id.iv_finish)
    public void onViewClicked() {
        finish();
    }

}
