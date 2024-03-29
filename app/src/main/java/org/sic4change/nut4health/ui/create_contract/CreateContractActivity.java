package org.sic4change.nut4health.ui.create_contract;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import org.sic4change.nut4health.R;
import static maes.tech.intentanim.CustomIntent.customType;

public class CreateContractActivity extends AppCompatActivity implements StepperLayout.StepperListener{

    private StepperLayout mStepperLayout;
    private StepCreateContractAdapter mStepCreateContractAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contract);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepCreateContractAdapter = new StepCreateContractAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepCreateContractAdapter);
        mStepperLayout.setListener(this);
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
        customType(CreateContractActivity.this,"right-to-left");
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onCompleted(View completeButton) {
        //goToMainActivity();
    }

    @Override
    public void onError(VerificationError verificationError) {
        mStepperLayout.updateErrorState(verificationError);
        Toast.makeText(this, verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }





}
