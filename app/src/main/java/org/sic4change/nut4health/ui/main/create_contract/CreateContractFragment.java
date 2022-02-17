package org.sic4change.nut4health.ui.main.create_contract;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.sic4change.animation_check.AnimatedCircleLoadingView;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.events.MessageEvent;
import org.sic4change.nut4health.ui.create_contract.CreateContractActivity;
import org.sic4change.nut4health.ui.fingerprint.ScanActivity;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.ui.main.contracts.ContractFragment;
import org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome;

import static maes.tech.intentanim.CustomIntent.customType;

import com.airbnb.lottie.LottieAnimationView;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.google.android.material.navigation.NavigationView;
import com.machinezoo.sourceafis.FingerprintTemplate;


public class CreateContractFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ImageView ivCreateContract;
    private Button btnStartCreateContract;
    private AnimatedCircleLoadingView clView;

    private MainViewModel mMainViewModel;

    private String eventResult = "";
    private static final long VERIFICATION_DELAY_MILISECONDS = 6000;
    private static final long VERIFICATION_TICK_MILISECONDS  = 1000;

    public static final int REQUEST_TAKE_FINGERPRINT = 4;

    public CreateContractFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_contract, container, false);
        ivCreateContract = view.findViewById(R.id.ivCreateContract);
        btnStartCreateContract = view.findViewById(R.id.btnStartCreateContract);
        clView = view.findViewById(R.id.clView);
        clView.setVisibility(View.GONE);
        ivCreateContract.setVisibility(View.VISIBLE);
        btnStartCreateContract.setVisibility(View.VISIBLE);
        ivCreateContract.setOnClickListener(v -> goToCreateContractActivity());
        btnStartCreateContract.setOnClickListener(v -> goToCreateContractActivity());
        initData();
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

    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void goToCreateContractActivity() {
        if (mMainViewModel.getCurrentUser().getValue().getRole().equals("Servicio Salud")) {
            Intent fingerPrintIntent = new Intent(getActivity(), ScanActivity.class);
            //startActivity(intent);
            startActivityForResult(fingerPrintIntent, REQUEST_TAKE_FINGERPRINT);
            customType(getActivity(),"left-to-right");
        } else {
            Intent intent = new Intent(getActivity(), CreateContractActivity.class);
            startActivity(intent);
            customType(getActivity(),"left-to-right");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_FINGERPRINT && resultCode == RESULT_OK){
            byte[] fingerprint = data.getByteArrayExtra(ScanActivity.FINGERPRINT);
            FingerprintTemplate fingerprintTemplateContract = new FingerprintTemplate().dpi(500).create(fingerprint);
            if ((fingerprint != null) && (fingerprint.length > 0)) {
                mMainViewModel.checkContract(fingerprintTemplateContract.serialize(), mMainViewModel.getCurrentUser().getValue().getId());
                ivCreateContract.setVisibility(View.GONE);
                btnStartCreateContract.setVisibility(View.GONE);
                clView.setVisibility(View.VISIBLE);
                clView.startDeterminate();

            }
        }
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        eventResult = event.getMessage();
        new CountDownTimer(VERIFICATION_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                clView.setVisibility(View.GONE);
                ivCreateContract.setVisibility(View.VISIBLE);
                btnStartCreateContract.setVisibility(View.VISIBLE);
                new AwesomeSuccessDialog(getActivity())
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(eventResult)
                        .setPositiveButtonText(getResources().getString(R.string.ok))
                        .setPositiveButtonClick(() -> {
                            cancel();
                            goToContracts();
                        })
                        .show();
            }
        }.start();

    }

    private void goToContracts() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        getActivity().setTitle(R.string.contracts);
        Fragment fragment = new ContractFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lyMainContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
