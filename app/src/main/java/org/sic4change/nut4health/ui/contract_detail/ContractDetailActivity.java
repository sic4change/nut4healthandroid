package org.sic4change.nut4health.ui.contract_detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;

import java.text.ParseException;

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
        mDetailContractViewModel.getContract().observe(this, new Observer<Contract>() {
            @Override
            public void onChanged(Contract contract) {
                showContractDetail(contract);
            }
        });
    }

    private void showContractDetail(Contract contract) {
        if (contract != null) {
            CircleView ivIcon = findViewById(R.id.ivIcon);
            TextView tvStatus = findViewById(R.id.tvStatus);
            EditText etName = findViewById(R.id.etName);
            EditText etSurname = findViewById(R.id.etSurname);
            EditText etLocation = findViewById(R.id.etLocation);
            EditText etPhoneContact = findViewById(R.id.etPhoneContact);
            TextView etDate = findViewById(R.id.etDate);
            etName.setText(contract.getChildName());
            etSurname.setText(contract.getChildSurname());
            etLocation.setText(contract.getChildAddress());
            etPhoneContact.setText(contract.getChildPhoneContract());
            ivIcon.setTitleText(contract.getPercentage() + "%");
            if (contract.getStatus().equals(Contract.Status.DIAGNOSIS.name())) {
                ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.ms_errorColor));
                ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.ms_errorColor));
                tvStatus.setText(this.getResources().getString(R.string.diagnosis));
                tvStatus.setTextColor(this.getResources().getColor(R.color.ms_errorColor));
            } else if (contract.getStatus().equals(Contract.Status.NO_DIAGNOSIS.name())) {
                ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                tvStatus.setText(this.getResources().getString(R.string.no_diagnosis));
                tvStatus.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
            } else if (contract.getStatus().equals(Contract.Status.PAID.name())) {
                ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                tvStatus.setText(this.getResources().getString(R.string.paid));
                tvStatus.setTextColor(this.getResources().getColor(R.color.colorAccent));
            } else if (contract.getStatus().equals(Contract.Status.FINISH.name())) {
                ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.orange));
                ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.orange));
                tvStatus.setText(this.getResources().getString(R.string.finished));
                tvStatus.setTextColor(this.getResources().getColor(R.color.orange));
            }
            Date date = new Date(contract.getCreationDate());
            Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
            TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
            String text = TimeAgo.using(date.getTime(), messages);
            etDate.setText(text);
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.smoothScrollTo(0,0);
        }
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
