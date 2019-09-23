package org.sic4change.nut4health.ui.main.report;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.lifecycle.ViewModelProviders;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Report;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;


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
            mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
            Report report = new Report(etReport.getText().toString());
            mMainViewModel.sendReport(report);
            new CountDownTimer(EXIT_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.reportSend)
                            .setMessage(R.string.reportSendDescription)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                etReport.setEnabled(true);
                                etReport.setClickable(true);
                                etReport.setText("");
                                btnSendReport.setEnabled(true);
                                btnSendReport.setClickable(true);
                                cancel();
                            })
                            .setIcon(R.mipmap.ic_launcher)
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
