package id.halalvestor.activity.main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.NotificationAdapter;
import id.halalvestor.model.Notification;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tabs)
    TabLayout tabs;
    List<Notification> list = new ArrayList<>();
    List<Notification> listSource = new ArrayList<>();
    @BindView(R.id.rv_main)
    RecyclerView rvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabs.setElevation(0);
        }

        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        tabs.setTabTextColors(Color.parseColor("#6E6E6E"), R.color.colorPrimary);

        tabs.addTab(tabs.newTab().setText("All"));
        tabs.addTab(tabs.newTab().setText("Chat"));
        tabs.addTab(tabs.newTab().setText("Product"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getCustomView().setBackgroundResource(R.drawable.round_background_primary);
                switch (tab.getPosition()){
                    case 0:
                        List<Notification> listTemp = new ArrayList<>();
                        list.clear();
                        list.addAll(listSource);
                        break;
                    case 1:
                        listTemp = new ArrayList<>();
                        for (Notification notification:listSource) {
                            if(notification.getType() == 2){
                                listTemp.add(notification);
                            }
                        }

                        list.clear();
                        list.addAll(listTemp);
                        break;
                    case 2:
                        listTemp = new ArrayList<>();
                        for (Notification notification:listSource) {
                            if(notification.getType() == 3){
                                listTemp.add(notification);
                            }
                        }

                        list.clear();
                        list.addAll(listTemp);
                        break;
                }

                loadAdapter();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getCustomView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        /**Notification notification = new Notification();
        notification.setId(1);
        notification.setTitle("Produk Terbaru");
        notification.setTime("02:59 PM");
        notification.setDescription("Dapatkan produk investasi terbaru dari Suncorinvest Asset Management");
        notification.setType(1);
        list.add(notification);

        notification = new Notification();
        notification.setId(1);
        notification.setTitle("Budi - Financial Planner");
        notification.setTime("02:59 PM");
        notification.setDescription("We intend to work as soon as the proposal is accepeted and will dispatch the produtcs within specified time limit. The dates are");
        notification.setType(2);
        list.add(notification);

        notification = new Notification();
        notification.setId(1);
        notification.setTitle("Konfirmasi Pembelian");
        notification.setTime("02:59 PM");
        notification.setDescription("Harap segera mengkonfirmasi pembelian anda terhadap produk asuransi kesehatan");
        notification.setType(3);
        list.add(notification);

        listSource.addAll(list);*/


        loadAdapter();

    }

    private void loadAdapter() {
        NotificationAdapter notificationAdapter = new NotificationAdapter(list, new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Notification model) {

            }
        });

        rvMain.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMain.setAdapter(notificationAdapter);
    }

    @OnClick(R.id.iv_finish)
    public void onViewClicked() {
        finish();
    }
}
