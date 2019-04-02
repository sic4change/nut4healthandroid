package org.sic4change.nut4health.ui.create_account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.login.LoginActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etRepeatPassword;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToLoginActivity();
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
    }

    public void goToLoginView(View view) {
        goToLoginActivity();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
