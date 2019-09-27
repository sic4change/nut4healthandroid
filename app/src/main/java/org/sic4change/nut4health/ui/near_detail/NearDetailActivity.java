package org.sic4change.nut4health.ui.near_detail;

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
import org.sic4change.nut4health.data.entities.Near;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;

public class NearDetailActivity extends AppCompatActivity {

    private DetailNearViewModel mDetailNearContractViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);
        NearDetailViewModelFactory contractDetailViewModelFactory = NearDetailViewModelFactory.createFactory(this, getIntent().getStringExtra("CONTRACT_ID"));
        mDetailNearContractViewModel = ViewModelProviders.of(this, contractDetailViewModelFactory).get(DetailNearViewModel.class);
        mDetailNearContractViewModel.getNear().observe(this, near -> showContractDetail(near));
    }

    private void showContractDetail(Near contract) {
        CircleView ivIcon = findViewById(R.id.ivIcon);
        EditText etName = findViewById(R.id.etName);
        EditText etSurname = findViewById(R.id.etSurname);
        EditText etLocation = findViewById(R.id.etLocation);
        com.github.curioustechizen.ago.RelativeTimeTextView etDate = findViewById(R.id.etDate);
        etName.setText(contract.getChildName());
        etSurname.setText(contract.getChildSurname());
        etLocation.setText(contract.getChildAddress());
        ivIcon.setTitleText(contract.getPercentage() + "%");
        if (contract.getStatus().equals(Near.Status.DIAGNOSIS.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.ms_errorColor));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.ms_errorColor));
        } else if (contract.getStatus().equals(Near.Status.NO_DIAGNOSIS.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
        } else {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
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
        customType(NearDetailActivity.this,"right-to-left");
        getSupportFragmentManager().popBackStackImmediate();
    }

}
