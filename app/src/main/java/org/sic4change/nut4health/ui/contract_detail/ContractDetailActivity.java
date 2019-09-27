package org.sic4change.nut4health.ui.contract_detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        mDetailContractViewModel.getContract().observe(this, contract -> showContractDetail(contract));
    }

    private void showContractDetail(Contract contract) {
        CircleView ivIcon = findViewById(R.id.ivIcon);
        EditText etName = findViewById(R.id.etName);
        EditText etSurname = findViewById(R.id.etSurname);
        EditText etLocation = findViewById(R.id.etLocation);
        com.github.curioustechizen.ago.RelativeTimeTextView etDate = findViewById(R.id.etDate);
        etName.setText(contract.getChildName());
        etSurname.setText(contract.getChildSurname());
        etLocation.setText(contract.getChildAddress());
        ivIcon.setTitleText(contract.getPercentage() + "%");
        if (contract.getStatus().equals(Contract.Status.DIAGNOSIS.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.ms_errorColor));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.ms_errorColor));
        } else if (contract.getStatus().equals(Contract.Status.NO_DIAGNOSIS.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
        } else if (contract.getStatus().equals(Contract.Status.PAID.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        } else if (contract.getStatus().equals(Contract.Status.FINISH.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.orange));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.orange));
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
        try {
            Date date = formatter.parse(contract.getCreationDate());
            etDate.setReferenceTime(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);
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
