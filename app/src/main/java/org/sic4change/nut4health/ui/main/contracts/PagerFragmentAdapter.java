package org.sic4change.nut4health.ui.main.contracts;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerFragmentAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String role;
    String patient;

    public PagerFragmentAdapter(FragmentManager fm, int NumOfTabs, String role, String patient) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.role = role;
        this.patient = patient;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ContractsListFragment(role, patient);
            case 1: return new ContractsMapFragment(role, patient);
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
