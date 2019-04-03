package org.sic4change.nut4health.ui.create_account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.login.LoginActivity;
import org.sic4change.nut4health.ui.splash.SplashActivity;
import org.sic4change.nut4health.utils.Nut4HealthVibrator;
import org.sic4change.nut4health.utils.view.Nut4HealthSnackbar;

public class CreateAccountActivity extends AppCompatActivity {

    private CreateAccountViewModel mCreateAccountViewModel;

    private ImageView btnBack;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        CreateAccountViewModelFactory createAccountViewModelFactory = CreateAccountViewModelFactory.createFactory(this);
        mCreateAccountViewModel = ViewModelProviders.of(this, createAccountViewModelFactory).get(CreateAccountViewModel.class);
        mCreateAccountViewModel.getUser().observe(this, this::hasUser);
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
        etPassword = findViewById(R.id.etPassword);
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

    public void createAccount(View view) {
        mCreateAccountViewModel.createUser(etEmail.getText().toString(), etUsername.getText().toString(), etRepeatPassword.getText().toString());
    }

    private void hasUser(User user) {
        if ((!etEmail.getText().toString().isEmpty()) && (!etUsername.getText().toString().isEmpty()) &&
                (!etPassword.getText().toString().isEmpty()) && (!etRepeatPassword.getText().toString().isEmpty())) {
            if (user != null) {
                if (user.isEmptyUser()) {
                    Nut4HealthVibrator.vibrateError(getApplicationContext());
                    Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.user_exist));
                } else {
                    goToSplashView();
                }
            }
        }

    }

    private void goToSplashView() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }
}
