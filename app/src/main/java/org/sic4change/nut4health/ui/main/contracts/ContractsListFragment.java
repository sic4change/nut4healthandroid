package org.sic4change.nut4health.ui.main.contracts;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Point;
import org.sic4change.nut4health.ui.contract_detail.ContractDetailActivity;
import org.sic4change.nut4health.ui.contract_detail.ContractDetailViewModelFactory;
import org.sic4change.nut4health.ui.contract_detail.DetailContractViewModel;
import org.sic4change.nut4health.ui.main.MainViewModel;

import static maes.tech.intentanim.CustomIntent.customType;


public class ContractsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;

    private ContractsAdapter contractsAdapter;
    private org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome ivEmptyContracts;
    private SwipeRefreshLayout swipe_container;
    private RecyclerView rvContracts;

    private String role= "";
    private boolean dataLoaded = false;

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
            goToContractDetailActivity(id, role);
        });
        return view;
    }

    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
            mMainViewModel.getCurrentUser().observe(getActivity(), user -> {
                if (!dataLoaded && user != null) {
                    mMainViewModel.getContracts(user.getEmail(), user.getRole());
                    role = user.getRole();
                }
            });
            mMainViewModel.getContracts().observe(getActivity(), contracts -> {
                    showContracts(contracts);
                    dataLoaded = true;
            });


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
        dataLoaded = false;
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

    private void goToContractDetailActivity(String id, String role) {
        Intent intent = new Intent(getActivity(), ContractDetailActivity.class);
        intent.putExtra("CONTRACT_ID", id);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

}
