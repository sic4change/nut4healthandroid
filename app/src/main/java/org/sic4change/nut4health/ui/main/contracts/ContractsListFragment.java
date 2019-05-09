package org.sic4change.nut4health.ui.main.contracts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.ui.contract_detail.ContractDetailActivity;
import org.sic4change.nut4health.ui.main.MainViewModel;

import static maes.tech.intentanim.CustomIntent.customType;


public class ContractsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;

    private ContractsAdapter contractsAdapter;
    private org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome ivEmptyContracts;
    private android.support.v4.widget.SwipeRefreshLayout swipe_container;
    private RecyclerView rvContracts;

    public ContractsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
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
        contractsAdapter = new ContractsAdapter(getActivity().getApplicationContext());
        rvContracts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContracts.setAdapter(contractsAdapter);
        contractsAdapter.setItemOnClickAction((position, id) -> {
            goToContractDetailActivity(id);
        });
        return view;
    }

    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getCurrentUser().observe(getActivity(), user -> {
            if (user != null) {
                mMainViewModel.getContracts(user.getEmail());
            }
        });
        mMainViewModel.getContracts().observe(getActivity(), contracts -> showContracts(contracts));
        mMainViewModel.getIsFiltered().observe(getActivity(), filtered -> {
            if (filtered) {
                showContracts(mMainViewModel.getContracts().getValue());
            }
        });
    }

    private void showContracts(PagedList<Contract> contracts) {
        if (contractsAdapter != null) {
            contractsAdapter.submitList(contracts);
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
        mMainViewModel = null;
        initData();
        contractsAdapter.notifyDataSetChanged();
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

    private void goToContractDetailActivity(String id) {
        Intent intent = new Intent(getActivity(), ContractDetailActivity.class);
        intent.putExtra("CONTRACT_ID", id);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

}
