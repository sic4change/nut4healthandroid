package org.sic4change.nut4health.ui.main.contracts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.create_contract.CreateContractActivity;

import java.util.Calendar;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static maes.tech.intentanim.CustomIntent.customType;


public class ContractFragment extends Fragment {

    private FloatingActionButton btnCreateContract;
    private FloatingActionButton btnFilterContracts;
    private CardView lyFilter;
    private EditText etNameAndSurname;
    private Spinner spStatus;
    private EditText tvDateRange;
    private BubbleThumbRangeSeekbar slDesnutrition;
    private TextView tvMinRange;
    private TextView tvMaxRange;
    private Button btnFilter;

    public ContractFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.list));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.map));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = view.findViewById(R.id.pager);
        final PagerFragmentAdapter adapter = new PagerFragmentAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        btnCreateContract = view.findViewById(R.id.btnCreateContract);
        btnCreateContract.setOnClickListener(v -> {
            goToCreateContractActivity();
        });
        btnFilterContracts = view.findViewById(R.id.btnFilterContracts);
        btnFilterContracts.setOnClickListener(v -> {
            showContractFilterMenu();
        });
        lyFilter = view.findViewById(R.id.lyFilter);
        etNameAndSurname = view.findViewById(R.id.etNameAndSurname);
        spStatus = view.findViewById(R.id.spStatus);
        tvDateRange = view.findViewById(R.id.tvDateRange);
        slDesnutrition = view.findViewById(R.id.slDesnutrition);
        tvMinRange = view.findViewById(R.id.tvMinRange);
        tvMaxRange = view.findViewById(R.id.tvMaxRange);
        slDesnutrition.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            tvMinRange.setText(String.valueOf(minValue) + "%");
            tvMaxRange.setText(String.valueOf(maxValue)  + "%");
        });
        tvDateRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                        tvDateRange.setText(firstDate.get(Calendar.DAY_OF_MONTH) + "/" + firstDate.get(Calendar.MONTH) + "/" + firstDate.get(Calendar.YEAR)
                        + " - " + secondDate.get(Calendar.DAY_OF_MONTH) + "/" + secondDate.get(Calendar.MONTH) + "/" + secondDate.get(Calendar.YEAR));

                    }
                };
                new SlyCalendarDialog()
                        .setSingle(false)
                        .setCallback(callback)
                        .setBackgroundColor(getContext().getResources().getColor(R.color.white))
                        .setSelectedTextColor(getContext().getResources().getColor(R.color.white))
                        .setSelectedColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                        .show(getActivity().getSupportFragmentManager(), "TAG_CALENDAR_RANGE_SELECTION");
            }
        });
        btnFilter = view.findViewById(R.id.btnFilter);
        return view;
    }

    private void goToCreateContractActivity() {
        Intent intent = new Intent(getActivity(), CreateContractActivity.class);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

    private void showContractFilterMenu() {
        if (lyFilter.getVisibility() == View.VISIBLE) {
            lyFilter.setVisibility(View.GONE);
        } else if (lyFilter.getVisibility() == View.GONE) {
            lyFilter.setVisibility(View.VISIBLE);
        }
    }

}
