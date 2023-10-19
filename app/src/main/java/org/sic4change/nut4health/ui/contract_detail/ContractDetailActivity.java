package org.sic4change.nut4health.ui.contract_detail;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.MalnutritionChildTable;
import org.sic4change.nut4health.utils.ruler_picker.SimpleRulerViewer;
import org.sic4change.nut4health.utils.time.Nut4HealthTimeUtil;


import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;

public class ContractDetailActivity extends AppCompatActivity implements SimpleRulerViewer.OnValueChangeListener {

    private DetailContractViewModel mDetailContractViewModel;

    private TextView tvPercentage;
    private TextView tvCm;
    private View rulerBackground;
    private SimpleRulerViewer ruler;
    private EditText etHeight;
    private TextView tvHeight;
    private EditText etWeight;
    private TextView tvWeight;
    private Button btnValidate;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);
        this.activity = this;
        tvPercentage = findViewById(R.id.tvPercentage);
        tvCm = findViewById(R.id.tvCm);
        rulerBackground = findViewById(R.id.rulerBackground);
        ruler = findViewById(R.id.ruler);
        ruler.setSelectedValue(28.0f);
        rulerBackground.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvPercentage.setText(getResources().getString(R.string.normopeso_full));
        tvPercentage.setTextColor(getResources().getColor(R.color.colorAccent));
        tvCm.setTextColor(getResources().getColor(R.color.colorAccent));
        etHeight = findViewById(R.id.etHeight);
        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    mDetailContractViewModel.setHeight(Double.parseDouble(charSequence.toString()));
                    mDetailContractViewModel.getDesnutritionChildTable().observe((LifecycleOwner) activity, values -> {
                        Collections.sort(values, Comparator.comparingDouble(MalnutritionChildTable::getCm));
                        mDetailContractViewModel.checkMalnutritionByWeightAndHeight(values);
                        mDetailContractViewModel.getStatus();
                        paintStatusChanges();
                    });
                } catch (Exception e) {
                    System.out.println("empty o null value");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvHeight = findViewById(R.id.tvHeight);
        etWeight = findViewById(R.id.etWeight);
        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    mDetailContractViewModel.setWeight(Double.parseDouble(charSequence.toString()));
                    mDetailContractViewModel.getDesnutritionChildTable().observe((LifecycleOwner) activity, values -> {
                        Collections.sort(values, Comparator.comparingDouble(MalnutritionChildTable::getCm));
                        mDetailContractViewModel.checkMalnutritionByWeightAndHeight(values);
                        mDetailContractViewModel.getStatus();
                        paintStatusChanges();
                    });
                } catch (Exception e) {
                    System.out.println("empty o null value");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvWeight = findViewById(R.id.tvWeight);
        btnValidate = findViewById(R.id.btnValidate);
        ruler.setOnValueChangeListener(this);
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
            TextView tvSex = findViewById(R.id.tvSex);
            TextView tvName = findViewById(R.id.tvName);
            EditText etName = findViewById(R.id.etName);
            TextView tvSurname = findViewById(R.id.tvSurname);
            EditText etSurname = findViewById(R.id.etSurname);
            TextView tvChildBirthdate = findViewById(R.id.tvChildBirthdate);
            EditText etChildBirthdate = findViewById(R.id.etChildBirthdate);
            TextView tvChildDNI = findViewById(R.id.tvChildDNI);
            EditText etChildDNI = findViewById(R.id.etChildDNI);
            TextView tvChildBirthdateResult = findViewById(R.id.tvChildBirthdateResult);
            EditText etTutor = findViewById(R.id.etTutor);
            EditText etTutorBirthdate = findViewById(R.id.etTutorBirthdate);
            TextView tvTutorBirthdateResult = findViewById(R.id.tvTutorBirthdateResult);
            TextView tvTutorDNI = findViewById(R.id.tvTutorDNI);
            EditText etTutorDNI = findViewById(R.id.etTutorDNI);
            EditText etTutorStatus = findViewById(R.id.etTutorStatus);
            TextView tvTutorStatus = findViewById(R.id.tvTutorStatus);
            EditText etLocation = findViewById(R.id.etLocation);
            EditText spPoint = findViewById(R.id.spPoint);
            EditText etPhoneContact = findViewById(R.id.etPhoneContact);
            TextView etDate = findViewById(R.id.etDate);
            Button btnConfirm = findViewById(R.id.btnConfirm);

            TextView etConfirmationDate = findViewById(R.id.etConfirmationDate);
            TextView tvConfirmationDate = findViewById(R.id.tvConfirmationDate);

            if (contract.getChildName() != null && !contract.getChildName().equals("")) {
                etName.setText(contract.getChildName());
            } else {
                tvName.setVisibility(View.GONE);
                etName.setVisibility(View.GONE);
            }
            if (contract.getChildSurname() != null && !contract.getChildSurname().equals("")) {
                etSurname.setText(contract.getChildSurname());
            } else {
                tvSurname.setVisibility(View.GONE);
                etSurname.setVisibility(View.GONE);
            }
            if (contract.getChildBirthdate() != null && !contract.getChildBirthdate().equals("") && !contract.getChildBirthdate().equals("1/1/1970")) {
                etChildBirthdate.setText(contract.getChildBirthdate());
                tvChildBirthdateResult.setText(Nut4HealthTimeUtil.yearsAndMonthCalculator(contract.getChildBirthdate(), getString(R.string.andYear), getString(R.string.month)));
            } else {
                tvChildBirthdate.setVisibility(View.GONE);
                etChildBirthdate.setVisibility(View.GONE);
            }
            if (contract.getChildDNI() != null && !contract.getChildDNI().equals("")) {
                etChildDNI.setText(contract.getChildDNI());
            } else {
                etChildDNI.setVisibility(View.GONE);
                tvChildDNI.setVisibility(View.GONE);
            }
            etTutor.setText(contract.getChildTutor());
            etTutorBirthdate.setText(contract.getTutorBirthdate());
            if (contract.getTutorDNI() != null && !contract.getTutorDNI().equals("")) {
                etTutorDNI.setText(contract.getTutorDNI());
            } else {
                etTutorDNI.setVisibility(View.GONE);
                tvTutorDNI.setVisibility(View.GONE);
            }
            tvTutorBirthdateResult.setText(Nut4HealthTimeUtil.yearsAndMonthCalculator(contract.getTutorBirthdate(), getString(R.string.andYear), getString(R.string.month)));
            if (contract.getTutorStatus() != null && !contract.getTutorStatus().equals("")) {
                etTutorStatus.setText(contract.getTutorStatus());
            } else {
                tvTutorStatus.setVisibility(View.GONE);
                etTutorStatus.setVisibility(View.GONE);
            }
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
            if (contract.getStatus().equals(Contract.Status.ADMITTED.name())) {
                ivIcon.setFillColor(getResources().getColor(R.color.violet));
                ivIcon.setStrokeColor(getResources().getColor(R.color.violet));
                tvStatus.setText(getResources().getString(R.string.admitted));
                tvStatus.setTextColor(getResources().getColor(R.color.violet));
                etConfirmationDate.setVisibility(View.VISIBLE);
                tvConfirmationDate.setVisibility(View.VISIBLE);
                try {
                    Date date = new Date(contract.getMedicalDate());
                    Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
                    TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                    String text = TimeAgo.using(date.getTime(), messages);
                    etConfirmationDate.setText(text);
                } catch (Exception e) {
                    System.out.println("error parsing confirmation date");
                }
            } else if (contract.getStatus().equals(Contract.Status.DUPLICATED.name())) {
                ivIcon.setTitleText(getResources().getString(R.string.duplicated_abrev));
                ivIcon.setFillColor(getResources().getColor(R.color.rose));
                ivIcon.setStrokeColor(getResources().getColor(R.color.rose));
                tvStatus.setText(getResources().getString(R.string.duplicated));
                tvStatus.setTextColor(getResources().getColor(R.color.rose));
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

            if ((contract.getTutorStatus() != null && !contract.getTutorStatus().equals("")) || contract.getSex() == null || contract.getSex().equals("")) {
                tvSex.setVisibility(View.GONE);
            } else {
                if (contract.getSex().equals("F")) {
                    tvSex.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_female_icon, 0, 0, 0);
                } else {
                    tvSex.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_male_icon, 0, 0, 0);
                }
                tvSex.setVisibility(View.VISIBLE);
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
                    //showDialogConfirmDiagnosis(contract.getId());
                    btnValidate.setVisibility(View.VISIBLE);
                    tvPercentage.setVisibility(View.VISIBLE);
                    tvCm.setVisibility(View.VISIBLE);
                    rulerBackground.setVisibility(View.VISIBLE);
                    ruler.setVisibility(View.VISIBLE);
                    etHeight.setVisibility(View.VISIBLE);
                    tvHeight.setVisibility(View.VISIBLE);
                    etWeight.setVisibility(View.VISIBLE);
                    tvWeight.setVisibility(View.VISIBLE);
                    btnConfirm.setVisibility(View.GONE);
                }
            });
            btnValidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDetailContractViewModel.setArmCircumferenceMedical(Double.parseDouble(tvCm.getText().toString().replace(",", ".").replace(" cm", "")));
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

    private void paintStatusChanges() {
        if (mDetailContractViewModel.getStatus().equals("Aguda Severa")) {
            tvPercentage.setText(getResources().getString(R.string.severe_acute_malnutrition_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.error));
        } else if (mDetailContractViewModel.getStatus().equals("Aguda Moderada")) {
            tvPercentage.setText(getResources().getString(R.string.moderate_acute_malnutrition_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.orange));
        } else {
            tvPercentage.setText(getResources().getString(R.string.normopeso_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public void onChange(SimpleRulerViewer view, int position, float value) {
        mDetailContractViewModel.setArmCircumferenceMedical(value);
        DecimalFormat df = new DecimalFormat("#.0");
        tvCm.setText(df.format(value) + " cm");
        if (value < 11.5) {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.error));
            tvCm.setTextColor(getResources().getColor(R.color.error));
        } else if (value >= 11.5 && value <= 12.5) {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.orange));
            tvCm.setTextColor(getResources().getColor(R.color.orange));
        } else {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tvCm.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        mDetailContractViewModel.getStatus();
        paintStatusChanges();
    }
}
