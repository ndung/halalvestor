package id.halalvestor.activity.adapter;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import id.halalvestor.activity.main.history.CompletedTransactionFragment;
import id.halalvestor.activity.main.history.OngoingTransactionFragment;

public class SectionHistoryPagerAdapter extends FragmentStatePagerAdapter {

    public SectionHistoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new OngoingTransactionFragment();
                break;
            case 1:
                fragment = new CompletedTransactionFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "On Going";
            case 1:
                return "Completed";
        }
        return null;
    }

}
