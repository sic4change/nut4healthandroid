package org.sic4change.nut4health.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.ui.create_account.CreateAccountActivity;
import org.sic4change.nut4health.ui.create_contract.CreateContractActivity;
import org.sic4change.nut4health.ui.login.LoginActivity;

import static maes.tech.intentanim.CustomIntent.customType;


public class ContractsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;

    private ContractsAdapter contractsAdapter;
    private FloatingActionButton btnCreateContract;
    private org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome ivEmptyContracts;

    public ContractsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModelFactory mainViewModelFactory = MainViewModelFactory.createFactory(getActivity());
        mMainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        btnCreateContract = view.findViewById(R.id.btnCreateContract);
        ivEmptyContracts = view.findViewById(R.id.ivEmptyContracts);
        btnCreateContract.setOnClickListener(v -> {
            goToCreateContractActivity();
        });
        RecyclerView recyclerView = view.findViewById(R.id.rvContracts);
        contractsAdapter = new ContractsAdapter(getActivity().getApplicationContext());
        mMainViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                mMainViewModel.getContracts(user.getEmail());
                mMainViewModel.getContracts().observe(getActivity(), contracts -> {
                    contractsAdapter.submitList(contracts);
                    if (contracts.size() > 0) {
                        ivEmptyContracts.setVisibility(View.GONE);
                    } else {
                        ivEmptyContracts.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contractsAdapter);
        return view;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void goToCreateContractActivity() {
        Intent intent = new Intent(getActivity(), CreateContractActivity.class);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
        getActivity().finish();
    }

}
