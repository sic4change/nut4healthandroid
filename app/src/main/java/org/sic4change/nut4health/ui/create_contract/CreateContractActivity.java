package org.sic4change.nut4health.ui.create_contract;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.create_account.CreateAccountActivity;
import org.sic4change.nut4health.ui.login.LoginActivity;
import org.sic4change.nut4health.ui.main.MainActivity;

import static maes.tech.intentanim.CustomIntent.customType;

public class CreateContractActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contract);
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
        customType(CreateContractActivity.this,"right-to-left");
        finish();
    }
}
