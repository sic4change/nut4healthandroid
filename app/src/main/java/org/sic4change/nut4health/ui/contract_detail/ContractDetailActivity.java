package org.sic4change.nut4health.ui.contract_detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.ui.main.MainViewModel;


import java.util.Date;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;

public class ContractDetailActivity extends AppCompatActivity {

    private DetailContractViewModel mDetailContractViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);
        ContractDetailViewModelFactory contractDetailViewModelFactory = ContractDetailViewModelFactory.createFactory(this, getIntent().getStringExtra("CONTRACT_ID"));
        mDetailContractViewModel = ViewModelProviders.of(this, contractDetailViewModelFactory).get(DetailContractViewModel.class);
        mDetailContractViewModel.setRole(getIntent().getStringExtra("ROLE"));
        mDetailContractViewModel.getContract().observe(this, new Observer<Contract>() {
            @Override
            public void onChanged(Contract contract) {
                showContractDetail(contract, mDetailContractViewModel.getRole());
            }
        });
    }

    private void showContractDetail(Contract contract, String role) {
        if (contract != null) {
            CircleView ivIcon = findViewById(R.id.ivIcon);
            TextView tvStatus = findViewById(R.id.tvStatus);
            EditText etName = findViewById(R.id.etName);
            EditText etSurname = findViewById(R.id.etSurname);
            EditText etTutor = findViewById(R.id.etTutor);
            EditText etLocation = findViewById(R.id.etLocation);
            EditText spPoint = findViewById(R.id.spPoint);
            EditText etPhoneContact = findViewById(R.id.etPhoneContact);
            TextView etDate = findViewById(R.id.etDate);
            Button btnConfirm = findViewById(R.id.btnConfirm);
            etName.setText(contract.getChildName());
            etSurname.setText(contract.getChildSurname());
            etTutor.setText(contract.getChildTutor());
            etLocation.setText(contract.getChildAddress());
            etPhoneContact.setText(contract.getChildPhoneContract());
            spPoint.setText(contract.getPointFullName());
            ivIcon.setTitleText(contract.getPercentage() + "%");
            if (contract.getPercentage() < 50) {
                ivIcon.setTitleText(getResources().getString(R.string.normopeso_abrev));
                ivIcon.setFillColor(getResources().getColor(R.color.colorPrimaryDark));
                ivIcon.setStrokeColor(getResources().getColor(R.color.colorPrimaryDark));
                tvStatus.setText(getResources().getString(R.string.normopeso));
                tvStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else if (contract.getPercentage() == 50) {
                ivIcon.setTitleText(getResources().getString(R.string.moderate_acute_malnutrition_abrev));
                ivIcon.setFillColor(getResources().getColor(R.color.orange));
                ivIcon.setStrokeColor(getResources().getColor(R.color.orange));
                tvStatus.setText(getResources().getString(R.string.moderate_acute_malnutrition));
                tvStatus.setTextColor(getResources().getColor(R.color.orange));
            } else {
                ivIcon.setTitleText(getResources().getString(R.string.severe_acute_malnutrition_abrev));
                ivIcon.setFillColor(getResources().getColor(R.color.ms_errorColor));
                ivIcon.setStrokeColor(getResources().getColor(R.color.ms_errorColor));
                tvStatus.setText(getResources().getString(R.string.severe_acute_malnutrition));
                tvStatus.setTextColor(getResources().getColor(R.color.ms_errorColor));
            }
            if (contract.getStatus().equals(Contract.Status.PAID.name())) {
                ivIcon.setFillColor(getResources().getColor(R.color.colorAccent));
                ivIcon.setStrokeColor(getResources().getColor(R.color.colorAccent));
                Date date = new Date(contract.getMedicalDate());
                Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
                TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                String text = TimeAgo.using(date.getTime(), messages);
                tvStatus.setText(getResources().getString(R.string.paid));
                tvStatus.setTextColor(getResources().getColor(R.color.colorAccent));
            } else if (contract.getStatus().equals(Contract.Status.FINISH.name())) {
                ivIcon.setFillColor(getResources().getColor(R.color.violet));
                ivIcon.setStrokeColor(getResources().getColor(R.color.violet));
                try {
                    Date date = new Date(contract.getMedicalDate());
                    Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
                    TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                    String text = TimeAgo.using(date.getTime(), messages);
                } catch (Exception e) {
                }
                tvStatus.setText(getResources().getString(R.string.finished));
                tvStatus.setTextColor(getResources().getColor(R.color.violet));
            }
            if (contract.getStatus().equals("DIAGNOSIS") && role.equals("Servicio Salud")) {
                btnConfirm.setEnabled(true);
                btnConfirm.setClickable(true);
                btnConfirm.setVisibility(View.VISIBLE);
                btnConfirm.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
            } else {
                btnConfirm.setEnabled(false);
                btnConfirm.setClickable(false);
                btnConfirm.setVisibility(View.INVISIBLE);
                btnConfirm.setBackgroundColor(this.getResources().getColor(R.color.ms_material_grey_400));
            }
            Date date = new Date(contract.getCreationDate());
            Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
            TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
            String text = TimeAgo.using(date.getTime(), messages);
            etDate.setText(text);
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.smoothScrollTo(0,0);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogConfirmDiagnosis(contract.getId());
                }
            });
        }
    }

    private void showDialogConfirmDiagnosis(String id) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_diagnosis_first_question)
                .setPositiveButton(R.string.ok, (dialog, which) -> showDialogConfirmDiagnosisSecondQuestion(id))
                .setIcon(R.mipmap.icon)
                .show();
    }

    private void showDialogConfirmDiagnosisSecondQuestion(String id) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_diagnosis_second_question)
                .setPositiveButton(R.string.ok, (dialog, which) -> mDetailContractViewModel.validateDiagnosis(id))
                .setIcon(R.mipmap.icon)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity();
    }

    private void goToMainActivity() {
        customType(ContractDetailActivity.this,"right-to-left");
        getSupportFragmentManager().popBackStackImmediate();
    }

}
