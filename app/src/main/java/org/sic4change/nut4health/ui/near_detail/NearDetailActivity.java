package org.sic4change.nut4health.ui.near_detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Near;

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
        TextView tvStatus = findViewById(R.id.tvStatus);
        EditText etName = findViewById(R.id.etName);
        EditText etSurname = findViewById(R.id.etSurname);
        EditText etLocation = findViewById(R.id.etLocation);
        TextView etDate = findViewById(R.id.etDate);
        etName.setText(contract.getChildName());
        etSurname.setText(contract.getChildSurname());
        etLocation.setText(contract.getChildAddress());
        ivIcon.setTitleText(contract.getPercentage() + "%");
        if (contract.getStatus().equals(Near.Status.DERIVED.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.ms_errorColor));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.ms_errorColor));
            tvStatus.setText(this.getResources().getString(R.string.derived));
            tvStatus.setTextColor(this.getResources().getColor(R.color.ms_errorColor));
        } else if (contract.getStatus().equals(Near.Status.REGISTERED.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            tvStatus.setText(this.getResources().getString(R.string.registered));
            tvStatus.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        } else if (contract.getStatus().equals(Near.Status.ADMITTED.name())) {
            ivIcon.setFillColor(getApplicationContext().getResources().getColor(R.color.orange));
            ivIcon.setStrokeColor(getApplicationContext().getResources().getColor(R.color.orange));
            tvStatus.setText(this.getResources().getString(R.string.admitted));
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
