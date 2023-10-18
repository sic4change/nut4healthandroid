package org.sic4change.nut4health.ui.main.create_contract;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.create_contract.CreateContractActivity;
import org.sic4change.nut4health.ui.create_contract_fefa.CreateFEFAContractActivity;

import static maes.tech.intentanim.CustomIntent.customType;


public class CreateContractFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button btnStartCreateChildContract;

    private Button btnStartCreateFEFAContract;

    public CreateContractFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_contract, container, false);
        btnStartCreateChildContract = view.findViewById(R.id.btnStartCreateContractChild);
        btnStartCreateChildContract.setOnClickListener(v -> goToCreateChildContractActivity());
        btnStartCreateFEFAContract = view.findViewById(R.id.btnStartCreateContractFefa);
        btnStartCreateFEFAContract.setOnClickListener(v -> goToCreateFEFAContractActivity());
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

    private void goToCreateChildContractActivity() {
        Intent intent = new Intent(getActivity(), CreateContractActivity.class);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

    private void goToCreateFEFAContractActivity() {
        Intent intent = new Intent(getActivity(), CreateFEFAContractActivity.class);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }
}
