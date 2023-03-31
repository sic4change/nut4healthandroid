package org.sic4change.nut4health.ui.main.payments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.ui.contract_detail.ContractDetailActivity;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;

import java.util.Calendar;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static maes.tech.intentanim.CustomIntent.customType;


public class PaymentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;
    private PaymentAdapter paymentAdapter;
    private RecyclerView rvPayment;
    private SwipeRefreshLayout swipe_container;
    private FloatingActionButton btnFilterPayments;
    private CardView lyFilter;
    private View ivStatus;
    private Spinner spStatus;
    private EditText tvDateRange;
    private Button btnFilter;
    private Button btnClear;
    private long timeRangeMin;
    private long timeRangeMax;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payments, container, false);
        swipe_container = view.findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));
        swipe_container.setOnRefreshListener(this);
        rvPayment = view.findViewById(R.id.rvPayments);
        paymentAdapter = new PaymentAdapter(getActivity().getApplicationContext());
        rvPayment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPayment.setAdapter(paymentAdapter);
        paymentAdapter.setItemOnClickAction((id) -> {
            goToContractDetailActivity(id);
        });
        btnFilterPayments = view.findViewById(R.id.btnFilterPayments);
        btnFilterPayments.setOnClickListener(v -> showPaymentFilterMenu());
        lyFilter = view.findViewById(R.id.lyFilter);
        tvDateRange = view.findViewById(R.id.tvDateRange);
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
        ivStatus = view.findViewById(R.id.ivStatus);
        spStatus = view.findViewById(R.id.spStatus);
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.ms_black));
                        break;
                    case 1:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 2:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    default:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            Nut4HealthKeyboard.closeKeyboard(tvDateRange, getContext());
            mMainViewModel.setIsFiltered(false);
            clear();
        });
        btnFilter = view.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> {
            Nut4HealthKeyboard.closeKeyboard(tvDateRange, getContext());
            filterPayments();
            lyFilter.setVisibility(View.GONE);
        });
        initData();
        return view;
    }

    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        /*mMainViewModel.getCurrentUser().observe(getActivity(), user -> {
            if (user != null) {
                mMainViewModel.getContracts(user.getEmail(), user.getRole());
                mMainViewModel.getPayments(user.getEmail());
            }
        });*/
        mMainViewModel.getCurrentConfiguration().observe(getActivity(), config -> {
            if (config != null) {
                paymentAdapter.setMoney(config.getMoney());
            }
        });
        mMainViewModel.getPayments().observe(getActivity(), payments -> {
            paymentAdapter.submitList(payments);
            paymentAdapter.notifyDataSetChanged();
            if (swipe_container.isRefreshing()) {
                swipe_container.setRefreshing(false);
            }
            mMainViewModel.getCurrentUser().removeObservers(getActivity());
            mMainViewModel.getCurrentConfiguration().removeObservers(getActivity());
            mMainViewModel.getPayments().removeObservers(getActivity());
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        mMainViewModel = null;
        initData();
        paymentAdapter.notifyDataSetChanged();
        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                swipe_container.setRefreshing(false);
            }

        }.start();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void showPaymentFilterMenu() {
        if (lyFilter.getVisibility() == View.VISIBLE) {
            lyFilter.setVisibility(View.GONE);
            Nut4HealthKeyboard.closeKeyboard(tvDateRange, getContext());
        } else if (lyFilter.getVisibility() == View.GONE) {
            lyFilter.setVisibility(View.VISIBLE);
        }
    }

    private void goToContractDetailActivity(String id) {
        Intent intent = new Intent(getActivity(), ContractDetailActivity.class);
        intent.putExtra("CONTRACT_ID", id);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

    private void clear() {
        tvDateRange.setText("");
        spStatus.setSelection(0);
        mMainViewModel.setName("");
        mMainViewModel.setSurname("");
        mMainViewModel.setStatus(Payment.Status.ALL.name());
        timeRangeMin = 0;
        timeRangeMax = 0;
        mMainViewModel.setDateEnd(timeRangeMin);
        mMainViewModel.setDateStart(timeRangeMax);
        mMainViewModel.setPercentageMax(100);
        mMainViewModel.setPercentageMin(0);
    }

    private void filterPayments() {
        switch (spStatus.getSelectedItemPosition()) {
            case 0:
                mMainViewModel.setStatusPayment(Payment.Status.ALL.name());
                break;
            case 1:
                mMainViewModel.setStatusPayment(Payment.Status.Month.name());
                break;
            case 2:
                mMainViewModel.setStatusPayment(Payment.Status.Diagnosis.name());
                break;
            case 3:
                mMainViewModel.setStatusPayment(Payment.Status.Confirmation.name());
                break;
            default:
                mMainViewModel.setStatusPayment(Payment.Status.ALL.name());
                break;
        }
        if (timeRangeMin != 0) {
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(timeRangeMin);
            c1.add(Calendar.DATE, -1);
            mMainViewModel.setDateStartPayment(c1.getTimeInMillis());
        } else {
            mMainViewModel.setDateStartPayment(timeRangeMin);
        }
        if (timeRangeMax != 0) {
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(timeRangeMax);
            c2.add(Calendar.DATE, 1);
            mMainViewModel.setDateEndPayment(c2.getTimeInMillis());
        } else {
            mMainViewModel.setDateEndPayment(timeRangeMax);
        }
        mMainViewModel.getSortedPayments("DATE", mMainViewModel.getStatusPayment(),
                mMainViewModel.getDateStartPayment(), mMainViewModel.getDateEndPayment());
        mMainViewModel.getPayments().observe(getActivity(), new Observer<PagedList<Payment>>() {
            @Override
            public void onChanged(@Nullable PagedList<Payment> payments) {
                paymentAdapter.submitList(payments);
            }
        });
    }


}
