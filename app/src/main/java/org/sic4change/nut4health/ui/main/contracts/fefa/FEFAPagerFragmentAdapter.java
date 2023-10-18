package org.sic4change.nut4health.ui.main.contracts.fefa;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FEFAPagerFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String role;

    public FEFAPagerFragmentAdapter(FragmentManager fm, int NumOfTabs, String role) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.role = role;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FEFAContractsListFragment(role);
            case 1: return new FEFAContractsMapFragment(role);
            default: return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
