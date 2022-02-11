package org.sic4change.nut4health.ui.main.contracts;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerFragmentAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String role;

    public PagerFragmentAdapter(FragmentManager fm, int NumOfTabs, String role) {
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
            case 0: return new ContractsListFragment(role);
            case 1: return new ContractsMapFragment(role);
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
