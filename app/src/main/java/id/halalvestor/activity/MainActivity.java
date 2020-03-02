package id.halalvestor.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.credential.LoginActivity;
import id.halalvestor.activity.main.AllPortfolioFragment;
import id.halalvestor.activity.main.BuySellFragment;
import id.halalvestor.activity.main.CallCenterFragment;
import id.halalvestor.activity.main.HomeFragment;
import id.halalvestor.activity.main.InstructionFragment;
import id.halalvestor.activity.main.InvestmentProgressFragment;
import id.halalvestor.activity.main.NewProfileFragment;
import id.halalvestor.activity.main.ProfileFragment;
import id.halalvestor.activity.main.SettingFragment;
import id.halalvestor.activity.main.insurance.InsuranceOptionFragment;
import id.halalvestor.activity.main.investment.InvestmentBuySellFragment;
import id.halalvestor.activity.main.NotificationActivity;
import id.halalvestor.ui.CircleTransformer;
import id.halalvestor.util.Helper;
import id.halalvestor.util.IRDialogUtils;

public class MainActivity extends BaseActivity {

    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;

    @BindView(R.id.message)
    EditText inputMessage;

    @BindView(R.id.btn_send)
    ImageView btnSend;

    @BindView(R.id.btn_attach)
    ImageView btnAttach;

    @BindView(R.id.chatbox)
    LinearLayout chatbox;

    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_account)
    ImageView ivSetting;

    @BindView(R.id.toolbarTitle)
    TextView tvToolbarTitle;

    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private static final int TIME_DELAY = 2000;
    private static boolean doubleBackToExitPressedOnce = false;
    @BindView(R.id.iv_search_toolbar)
    ImageView ivSearchToolbar;
    @BindView(R.id.iv_sort_toolbar)
    ImageView ivSortToolbar;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        //showHome();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_DASHBOARD;
                loadHomeFragment();
                return;
            }
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan tombol back sekali lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, TIME_DELAY);

    }

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.navigation_drawer_base_layout);

        mHandler = new Handler();

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        if (frameLayout != null) {
            getLayoutInflater().inflate(R.layout.main_layout, frameLayout);
        } else {
            setContentView(R.layout.main_layout);
        }

        ButterKnife.bind(this);

        String fragment = getIntent().getStringExtra("fragment");

        setUpNavigationView();

        if (fragment != null && fragment.equals("investment_buysell")) {
            navItemIndex = 11;
            CURRENT_TAG = TAG_INVESTMENT_BUYSELL;
            loadHomeFragment();
        } else if (fragment != null && fragment.equals("profile")) {
            navItemIndex = 9;
            CURRENT_TAG = TAG_PROFILE;
            loadHomeFragment();
        } else if (fragment != null && fragment.equals("transaction_history")) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_HISTORY;
            loadHomeFragment();
        } else if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            loadHomeFragment();
        }

        drawerLayout.closeDrawer(Gravity.LEFT);

        View headerLayout = navView.getHeaderView(0); // 0-index header
        TextView tvName = (TextView) headerLayout.findViewById(R.id.tv_name);
        TextView tvProfile = (TextView) headerLayout.findViewById(R.id.tv_profile);

        ImageView imageView = headerLayout.findViewById(R.id.imageView);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tvName.setText(firebaseUser.getDisplayName());

        Glide.with(this).load(firebaseUser.getPhotoUrl())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransformer(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 10;
                CURRENT_TAG = TAG_NEW_PROFILE;
                loadHomeFragment();
            }
        });

        navView.setItemIconTintList(null);
    }

    private void initToolbarInvestment() {
        ivSearchToolbar.setVisibility(View.VISIBLE);
        ivSortToolbar.setVisibility(View.VISIBLE);
        ivSetting.setVisibility(View.VISIBLE);
        ivSetting.setImageResource(R.mipmap.ic_logout_toolbar);
        ivSearchToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new IRDialogUtils(MainActivity.this).showDialog("Temukan Produk", R.layout.dialog_search_product, true);
            }
        });

        ivSortToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new IRDialogUtils(MainActivity.this).showDialog("Urut Berdasarkan", R.layout.dialog_sort_product, true);
                ButterKnife.bind(dialog);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new IRDialogUtils(MainActivity.this).showDialogNoTitle("Logout", R.layout.dialog_logout_bareksa, true);
                TextView yes = dialog.findViewById(R.id.tv_done);
                TextView no = dialog.findViewById(R.id.tv_cancel);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showBuySell();
                        App.getInstance().getPreferenceManager().setBareksaLoggedIn(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        App.getInstance().getPreferenceManager().updateLastActive(firebaseUser.getUid(), 0);
        startActivity(intent);
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        this.finish();
    }

    private void setUpNavigationView() {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_dashboard:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_DASHBOARD;
                        break;
                    //case R.id.nav_history:
                    //    navItemIndex = 1;
                    //    CURRENT_TAG = TAG_HISTORY;
                    //    break;
                    case R.id.nav_portfolio:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PORTFOLIO;
                        break;
                    case R.id.nav_progress:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_INVESTMENT_PROGRESS;
                        break;
                    case R.id.nav_buysell:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_BUYSELL;
                        break;
                    case R.id.nav_investment:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_INVESTMENT;
                        break;
                    case R.id.nav_insurance:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_INSURANCE;
                        break;
                    case R.id.nav_help:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_HELP;
                        break;
                    case R.id.nav_instruction:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_INSTRUCTION;
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_LOGOUT;
                        final Dialog dialog = new IRDialogUtils(MainActivity.this) .showDialogNoTitle("Logout", R.layout.dialog_logout_miku, true);
                        TextView yes = dialog.findViewById(R.id.tv_done);
                        TextView no = dialog.findViewById(R.id.tv_cancel);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                logout();
                            }
                        });

                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();

            return;
        }

        //if (CURRENT_TAG == TAG_INVESTMENT && !App.getInstance().getPreferenceManager().isBareksaLoggedIn(firebaseUser.getUid())) {
        //    Intent intent = new Intent(this, InvestmentAuthActivity.class);
        //    startActivity(intent);
        //}else {
            // Sometimes, when fragment has huge data, screen seems hanging
            // when switching between navigation menus
            // So using runnable, the fragment is loaded with cross fade effect
            // This effect can be seen in GMail app
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    // update the main content by replacing fragments
                    Fragment fragment = getHomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(frameLayout.getId(), fragment, CURRENT_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };

            // If mPendingRunnable is not null, then add to the message queue
            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }
        //}
        //Closing drawer on item click
        drawerLayout.closeDrawers();

        // refresh toolbar menu
        if (CURRENT_TAG == TAG_INVESTMENT_BUYSELL) {
            initToolbarInvestment();
        } else {
            selectNavMenu();
        }
        invalidateOptionsMenu();
    }

    String[] title = {"Beranda", //"Sejarah Transaksi",
            "Portfolio", "Progres Investasi", "Jual Beli", "Investasi", "Asuransi", "Help Center", "Petunjuk", "Logout", "Profile", "Profile", "Investasi", "Ubah Akun"};

    private void selectNavMenu() {
        for (int i = 0; i < 8; i++) {
            if (i == navItemIndex) {
                navView.getMenu().getItem(i).setChecked(true);
            } else {
                navView.getMenu().getItem(i).setChecked(false);
            }
        }

        if (navItemIndex==11){
            navView.getMenu().getItem(4).setChecked(true);
        }

        if (navItemIndex == 0) {
            ivLogo.setVisibility(View.VISIBLE);
            ivSetting.setVisibility(View.VISIBLE);
            ivSetting.setImageResource(R.mipmap.ic_notif);
            tvToolbarTitle.setVisibility(View.GONE);
            chatbox.setVisibility(View.GONE);
            ivSearchToolbar.setVisibility(View.GONE);
            ivSortToolbar.setVisibility(View.GONE);
        } else {
            if (CURRENT_TAG == TAG_INVESTMENT_BUYSELL) {
                ivLogo.setVisibility(View.GONE);
                tvToolbarTitle.setVisibility(View.VISIBLE);
                tvToolbarTitle.setText(title[navItemIndex]);
                chatbox.setVisibility(View.VISIBLE);
                initToolbarInvestment();
            } else {
                ivSearchToolbar.setVisibility(View.GONE);
                ivSortToolbar.setVisibility(View.GONE);
                ivLogo.setVisibility(View.GONE);
                ivSetting.setVisibility(View.GONE);
                tvToolbarTitle.setVisibility(View.VISIBLE);
                tvToolbarTitle.setText(title[navItemIndex]);
                chatbox.setVisibility(View.VISIBLE);
            }
        }
    }

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            //case 1:
            //    HistoryTransactionFragment historyFragment = new HistoryTransactionFragment();
            //    return historyFragment;
            case 1:
                AllPortfolioFragment portfolioFragment = new AllPortfolioFragment();
                return portfolioFragment;
            case 2:
                InvestmentProgressFragment investmentProgressFragment = new InvestmentProgressFragment();
                return investmentProgressFragment;
            case 3:
                BuySellFragment buySellFragment = new BuySellFragment();
                return buySellFragment;
            case 4:
                //startActivity(new Intent(getApplicationContext(), InvestmentAuthActivity.class));
                InvestmentBuySellFragment buysellFragment = new InvestmentBuySellFragment();
                return buysellFragment;
            case 5:
                InsuranceOptionFragment insuranceFragment = new InsuranceOptionFragment();
                return insuranceFragment;
            case 6:
                CallCenterFragment callCenterFragment = new CallCenterFragment();
                return callCenterFragment;
            case 7:
                InstructionFragment instructionFragment = new InstructionFragment();
                return instructionFragment;
            case 9:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 10:
                NewProfileFragment newProfileFragment = new NewProfileFragment();
                return newProfileFragment;
            case 11:
                InvestmentBuySellFragment investmentBuySellFragment = new InvestmentBuySellFragment();
                return investmentBuySellFragment;
            case 12:
                SettingFragment settingFragment = new SettingFragment();
                return settingFragment;
            default:
                return new HomeFragment();
        }
    }

    private Handler mHandler;

    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_PORTFOLIO = "portfolio";
    private static final String TAG_BUYSELL = "buysell";
    private static final String TAG_INVESTMENT = "investment";
    private static final String TAG_INSURANCE = "insurance";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_NEW_PROFILE = "new_profile";
    private static final String TAG_HELP = "help";
    private static final String TAG_INSTRUCTION = "instruction";
    private static final String TAG_SETTING = "setting";
    private static final String TAG_INVESTMENT_PROGRESS = "investment_progress";
    private static final String TAG_INVESTMENT_BUYSELL = "investment_buysell";
    private static final String TAG_LOGOUT = "logout";
    public static String CURRENT_TAG = TAG_DASHBOARD;

    private static final String TAG = MainActivity.class.toString();

    @OnClick({R.id.iv_account, R.id.iv_home, R.id.btn_send, R.id.btn_attach})
    public void onViewClicked(View view) {
        ivHome.setImageResource(R.mipmap.ic_main);
        ivSetting.setImageResource(R.mipmap.ic_notif);

        switch (view.getId()) {
            case R.id.iv_account:
                showSetting();
                break;
            case R.id.iv_home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_send:
                Helper.hideKeyboard(this);
                try {
                    if (inputMessage.getText() != null && !inputMessage.getText().toString().isEmpty()) {
                        sendMessage(inputMessage.getText().toString());
                        inputMessage.setText("");
                    } else {
                        showChatActivity();
                    }
                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                }
                break;
            case R.id.btn_attach:
                attach(1);
                break;

        }
    }

    public void showChatActivity() {
        Intent intent = new Intent();
        intent.setClass(this, ChatActivity.class);
        startActivity(intent);
    }

    public void showHome() {
        ivSetting.setImageResource(R.mipmap.ic_notif);
        //tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        //tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
        frameLayout.setVisibility(View.VISIBLE);
        chatbox.setVisibility(View.GONE);
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), homeFragment).commit();

        //LinearLayout tl = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition());
        //TextView text = (TextView) tl.getChildAt(1);
        //text.setTypeface(text.getTypeface(), Typeface.NORMAL);
        //tl.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
    }

    private void showSetting() {
        ivSetting.setImageResource(R.mipmap.ic_notif);
        startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
    }

    public void showProfile() {
        navItemIndex = 9;
        CURRENT_TAG = TAG_PROFILE;
        loadHomeFragment();
    }

    public void showInstruction() {
        navItemIndex = 7;
        CURRENT_TAG = TAG_INSTRUCTION;
        loadHomeFragment();
    }

    public void showBuySell() {
        navItemIndex = 3;
        CURRENT_TAG = TAG_BUYSELL;
        loadHomeFragment();
    }

    public void showPortfolio() {
        navItemIndex = 1;
        CURRENT_TAG = TAG_PORTFOLIO;
        loadHomeFragment();
    }

    public void showProfileSetting() {
        navItemIndex = 12;
        CURRENT_TAG = TAG_SETTING;
        loadHomeFragment();
    }

}
