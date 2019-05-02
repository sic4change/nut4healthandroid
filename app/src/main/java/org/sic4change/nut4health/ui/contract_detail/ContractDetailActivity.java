package org.sic4change.nut4health.ui.contract_detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.ui.main.MainActivity;

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
        } else {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        }
        etDate.setReferenceTime(contract.getDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        goToMainActivity();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        customType(ContractDetailActivity.this,"right-to-left");
        finish();
    }

}
