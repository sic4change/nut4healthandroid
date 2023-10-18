package org.sic4change.nut4health.ui.main.contracts.fefa;


import static maes.tech.intentanim.CustomIntent.customType;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.ui.contract_detail.ContractDetailActivity;
import org.sic4change.nut4health.ui.main.MainViewModel;


public class FEFAContractsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;

    private FEFAContractsAdapter FEFAContractsAdapter;
    private org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome ivEmptyContracts;
    private SwipeRefreshLayout swipe_container;
    private RecyclerView rvContracts;
    private TextView tvTotalCasesList;

    private String role= "";

    public FEFAContractsListFragment(String role) {
        // Required empty public constructor
        this.role = role;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract_list, container, false);
        swipe_container = view.findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));
        swipe_container.setOnRefreshListener(this);
        ivEmptyContracts = view.findViewById(R.id.ivEmptyContracts);
        rvContracts = view.findViewById(R.id.rvContracts);
        FEFAContractsAdapter = new FEFAContractsAdapter(getActivity().getApplicationContext());
        rvContracts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContracts.setAdapter(FEFAContractsAdapter);
        FEFAContractsAdapter.setItemOnClickAction((position, id) -> {
            goToContractDetailActivity(id, role);
        });
        tvTotalCasesList = view.findViewById(R.id.tvTotalCasesList);
        initData();
        return view;
    }

    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        /*mMainViewModel.getContracts().observe(getActivity(), contracts -> {
            showContracts(contracts);
            showContractNumber(contracts);
        });*/

        try {
            mMainViewModel.getIsFiltered().observe(getActivity(), filtered ->{
                mMainViewModel.getContracts().observe(getActivity(), contracts -> {
                    showContracts(contracts);
                    showContractNumber(contracts);
                });
            });
        } catch (Exception e) {
            System.out.println("error");
        }

    }

    private void showContractNumber(PagedList<Contract> contracts) {
        try {
            tvTotalCasesList.setText(getString(R.string.showing) + " " + contracts.size() + " " + getString(R.string.diagnosis_show));
        } catch (Exception e) {
            System.out.println("null contracts");
        }
    }

    private void showContracts(PagedList<Contract> contracts) {
        if (FEFAContractsAdapter != null) {
            FEFAContractsAdapter.submitList(contracts);
            FEFAContractsAdapter.notifyDataSetChanged();
            if (contracts.size() > 0) {
                ivEmptyContracts.setVisibility(View.GONE);
            } else {
                ivEmptyContracts.setVisibility(View.VISIBLE);
            }
            if (swipe_container != null && swipe_container.isRefreshing()) {
                swipe_container.setRefreshing(false);
            }
        }
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
        //mMainViewModel = null;
        //initData();
        //contractsAdapter.notifyDataSetChanged();
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

    private void goToContractDetailActivity(String id, String role) {
        Intent intent = new Intent(getActivity(), ContractDetailActivity.class);
        intent.putExtra("CONTRACT_ID", id);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }



}
