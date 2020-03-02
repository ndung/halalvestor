package id.halalvestor.activity.main.history;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.SectionHistoryPagerAdapter;
import id.halalvestor.activity.main.BaseFragment;
import id.halalvestor.ui.PageTransformer;

public class HistoryTransactionFragment extends BaseFragment {

    private static final String TAG = HistoryTransactionFragment.class.toString();

    private ViewPager mViewPager;
    TabLayout tabLayout;
    SectionHistoryPagerAdapter mSectionHistoryPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_transaction_layout, container, false);
        ButterKnife.bind(this, view);

        mSectionHistoryPagerAdapter = new SectionHistoryPagerAdapter(getActivity().getSupportFragmentManager());

        mViewPager = view.findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionHistoryPagerAdapter);
        mViewPager.setPageTransformer(true, new PageTransformer());

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        LinearLayout tl = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition());
        TextView text = (TextView) tl.getChildAt(1);
        text.setTypeface(text.getTypeface(), Typeface.NORMAL);
        text.setTextColor(getResources().getColor(R.color.colorPrimary));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {

                LinearLayout tl = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView text = (TextView) tl.getChildAt(1);
                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout tl = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView text = (TextView) tl.getChildAt(1);
                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setTextColor(getResources().getColor(R.color.secondary_text));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

}
