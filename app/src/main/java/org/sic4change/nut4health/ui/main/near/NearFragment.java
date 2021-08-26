package org.sic4change.nut4health.ui.main.near;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Near;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;

import java.util.Calendar;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;



public class NearFragment extends Fragment {

    private FloatingActionButton btnCreateContract;
    private FloatingActionButton btnFilterContracts;
    private CardView lyFilter;
    private EditText etName;
    private EditText etSurname;
    private View ivStatus;
    private Spinner spStatus;
    private EditText tvDateRange;
    private BubbleThumbRangeSeekbar slDesnutrition;
    private TextView tvMinRange;
    private TextView tvMaxRange;
    private Button btnFilter;
    private Button btnClear;

    private long timeRangeMin;
    private long timeRangeMax;

    private MainViewModel mMainViewModel;

    public NearFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.near_me));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.list));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = view.findViewById(R.id.pager);
        final NearPagerFragmentAdapter adapter = new NearPagerFragmentAdapter
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
        btnCreateContract.setVisibility(View.GONE);
        /*btnCreateContract.setOnClickListener(v -> {
            goToCreateContractActivity();
        });*/
        btnFilterContracts = view.findViewById(R.id.btnFilterContracts);
        btnFilterContracts.setOnClickListener(v -> {
            showContractFilterMenu();
        });
        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            Nut4HealthKeyboard.closeKeyboard(etName, getContext());
            mMainViewModel.setIsFiltered(false);
            clear();
        });
        lyFilter = view.findViewById(R.id.lyFilter);
        etName = view.findViewById(R.id.etName);
        etSurname = view.findViewById(R.id.etSurname);
        ivStatus = view.findViewById(R.id.ivStatus);
        spStatus = view.findViewById(R.id.spStatus);
        tvDateRange = view.findViewById(R.id.tvDateRange);
        slDesnutrition = view.findViewById(R.id.slDesnutrition);
        tvMinRange = view.findViewById(R.id.tvMinRange);
        tvMaxRange = view.findViewById(R.id.tvMaxRange);
        slDesnutrition.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            tvMinRange.setText(String.valueOf(minValue) + "%");
            tvMaxRange.setText(String.valueOf(maxValue)  + "%");
        });
        tvDateRange.setOnClickListener(v -> {
            SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
                @Override
                public void onCancelled() {

                }

                @Override
                public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                    try {
                        timeRangeMin = firstDate.getTimeInMillis();
                        timeRangeMax = secondDate.getTimeInMillis();
                        tvDateRange.setText(firstDate.get(Calendar.DAY_OF_MONTH) + "/" + (firstDate.get(Calendar.MONTH) + 1) + "/" + firstDate.get(Calendar.YEAR)
                                + " - " + secondDate.get(Calendar.DAY_OF_MONTH) + "/" + (firstDate.get(Calendar.MONTH) + 1) + "/" + secondDate.get(Calendar.YEAR));
                    } catch (Exception e) {
                        timeRangeMin = firstDate.getTimeInMillis();
                        timeRangeMax = Calendar.getInstance().getTimeInMillis();
                        tvDateRange.setText(firstDate.get(Calendar.DAY_OF_MONTH) + "/" + (firstDate.get(Calendar.MONTH) + 1)  + "/" + firstDate.get(Calendar.YEAR)
                                + " - " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (firstDate.get(Calendar.MONTH) + 1)  + "/" + Calendar.getInstance().get(Calendar.YEAR));
                    }
                }
            };
            new SlyCalendarDialog()
                    .setSingle(false)
                    .setCallback(callback)
                    .setHeaderColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .setBackgroundColor(getContext().getResources().getColor(R.color.white))
                    .setSelectedTextColor(getContext().getResources().getColor(R.color.white))
                    .setSelectedColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .show(getActivity().getSupportFragmentManager(), "TAG_CALENDAR_RANGE_SELECTION");
        });
        btnFilter = view.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> {
            Nut4HealthKeyboard.closeKeyboard(etName, getContext());
            filterContracts();
            lyFilter.setVisibility(View.GONE);
        });
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.ms_black));
                        break;
                    case 1:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.orange));
                        break;
                    case 2:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.ms_errorColor));
                        break;
                    case 3:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 4:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        break;
                    default:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.ms_black));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        return view;
    }

    private void showContractFilterMenu() {
        if (lyFilter.getVisibility() == View.VISIBLE) {
            lyFilter.setVisibility(View.GONE);
            Nut4HealthKeyboard.closeKeyboard(etName, getContext());
        } else if (lyFilter.getVisibility() == View.GONE) {
            lyFilter.setVisibility(View.VISIBLE);
        }
    }

    private void clear() {
        etName.setText("");
        etSurname.setText("");
        tvDateRange.setText("");
        slDesnutrition.setSelected(false);
        spStatus.setSelection(0);
        slDesnutrition.setMinStartValue(0).apply();
        slDesnutrition.setMaxStartValue(100).apply();
        tvMaxRange.setText("100%");
        tvMinRange.setText("0%");
        mMainViewModel.setName("");
        mMainViewModel.setSurname("");
        mMainViewModel.setStatus(Near.Status.EMPTY.name());
        timeRangeMin = 0;
        timeRangeMax = 0;
        mMainViewModel.setDateEnd(timeRangeMin);
        mMainViewModel.setDateStart(timeRangeMax);
        mMainViewModel.setPercentageMax(100);
        mMainViewModel.setPercentageMin(0);
    }

    private void filterContracts() {
        mMainViewModel.setName(etName.getText().toString());
        mMainViewModel.setSurname(etSurname.getText().toString());
        switch (spStatus.getSelectedItemPosition()) {
            case 0:
                mMainViewModel.setStatus(Near.Status.EMPTY.name());
                break;
            case 1:
                mMainViewModel.setStatus(Near.Status.FINISH.name());
                break;
            case 2:
                mMainViewModel.setStatus(Near.Status.DIAGNOSIS.name());
                break;
            case 3:
                mMainViewModel.setStatus(Near.Status.NO_DIAGNOSIS.name());
                break;
            case 4:
                mMainViewModel.setStatus(Near.Status.PAID.name());
                break;
            default:
                mMainViewModel.setStatus(Near.Status.EMPTY.name());
                break;
        }
        if (timeRangeMin != 0) {
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(timeRangeMin);
            c1.add(Calendar.DATE, -1);
            mMainViewModel.setDateStart(c1.getTimeInMillis());
        } else {
            mMainViewModel.setDateStart(timeRangeMin);
        }
        if (timeRangeMax != 0) {
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(timeRangeMax);
            c2.add(Calendar.DATE, 1);
            mMainViewModel.setDateEnd(c2.getTimeInMillis());
        } else {
            mMainViewModel.setDateStart(timeRangeMax);
        }
        mMainViewModel.setPercentageMax(Integer.parseInt(tvMaxRange.getText().toString().substring(0, tvMaxRange.getText().toString().length()-1)));
        mMainViewModel.setPercentageMin(Integer.parseInt(tvMinRange.getText().toString().substring(0, tvMinRange.getText().toString().length()-1)));
        mMainViewModel.getSortedNearContracts("DATE", mMainViewModel.getName(), mMainViewModel.getSurname(), mMainViewModel.getStatus(),
                mMainViewModel.getDateStart(), mMainViewModel.getDateEnd(), mMainViewModel.getPercentageMin(), mMainViewModel.getPercentageMax());
        mMainViewModel.getNearContracts().observe(getActivity(), contracts -> mMainViewModel.setIsFiltered(true));
    }

}