package org.sic4change.nut4health.ui.main.report;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Report;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ReportFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;
    private EditText etReport;
    private Button btnSendReport;

    private static final long EXIT_DELAY_MILISECONDS = 1000;
    private static final long VERIFICATION_TICK_MILISECONDS  = 1000;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        etReport = view.findViewById(R.id.etReport);
        etReport.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Nut4HealthKeyboard.closeKeyboard(etReport, getActivity().getApplicationContext());
                createReport();
                return true;
            }
            return false;
        });
        btnSendReport = view.findViewById(R.id.btnSendReport);
        btnSendReport.setOnClickListener(v -> {
            createReport();
        });
        return view;
    }

    private void createReport() {
        if ((etReport.getText() != null) && (!etReport.getText().toString().isEmpty())) {
            etReport.setEnabled(false);
            etReport.setClickable(false);
            btnSendReport.setEnabled(false);
            btnSendReport.setClickable(false);
            mMainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
            Report report = new Report(etReport.getText().toString());
            mMainViewModel.sendReport(report);
            new CountDownTimer(EXIT_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(getResources().getString(R.string.reportSend))
                            .setContentText(getResources().getString(R.string.reportSendDescription))
                            .setConfirmText(getResources().getString(R.string.ok))
                            .setCancelClickListener(sweetAlertDialog -> {
                                etReport.setEnabled(true);
                                etReport.setClickable(true);
                                etReport.setText("");
                                btnSendReport.setEnabled(true);
                                btnSendReport.setClickable(true);
                                cancel();

                            })
                            .show();

                }
            }.start();


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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
