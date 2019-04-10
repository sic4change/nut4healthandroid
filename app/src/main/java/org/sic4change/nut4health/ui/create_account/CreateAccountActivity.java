package org.sic4change.nut4health.ui.create_account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.login.LoginActivity;
import org.sic4change.nut4health.ui.splash.SplashActivity;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;
import org.sic4change.nut4health.utils.Nut4HealthVibrator;
import org.sic4change.nut4health.utils.validators.EmailValidator;
import org.sic4change.nut4health.utils.validators.NotEmptyValidator;
import org.sic4change.nut4health.utils.validators.PasswordValidator;
import org.sic4change.nut4health.utils.view.Nut4HealthSnackbar;

import static maes.tech.intentanim.CustomIntent.customType;

public class CreateAccountActivity extends AppCompatActivity {

    private CreateAccountViewModel mCreateAccountViewModel;

    private ImageView btnBack;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private Button btnCreateAccount;
    private TextView tvTermsAndConditions;
    private CheckBox cbTermsAndConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        enableView();
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
        etRepeatPassword.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                createAccount();
                return true;
            }
            return false;
        });
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        cbTermsAndConditions = findViewById(R.id.cbTerms);
    }

    public void goToLoginView(View view) {
        goToLoginActivity();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        customType(CreateAccountActivity.this,"right-to-left");
        finish();
    }

    private void createAccount() {
        if (!NotEmptyValidator.isValid(etEmail.getText()) || !NotEmptyValidator.isValid(etUsername.getText()) ||
                !NotEmptyValidator.isValid(etPassword.getText()) || !NotEmptyValidator.isValid(etRepeatPassword.getText())) {
            Nut4HealthVibrator.vibrateError(getApplicationContext());
            Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyCreateAccount), getResources().getString(R.string.mandatory_field));
            if (!NotEmptyValidator.isValid(etEmail.getText())) {
                etEmail.requestFocus();
            } else if (!NotEmptyValidator.isValid(etUsername.getText())) {
                etUsername.requestFocus();
            } else if (!NotEmptyValidator.isValid(etPassword.getText())) {
                etPassword.requestFocus();
            } else if (!NotEmptyValidator.isValid(etRepeatPassword.getText())) {
                etRepeatPassword.requestFocus();
            }
        } else {
            if (!EmailValidator.isValidEmail(etEmail.getText())) {
                Nut4HealthVibrator.vibrateError(getApplicationContext());
                Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyCreateAccount), getResources().getString(R.string.invalid_email));
                if (!NotEmptyValidator.isValid(etEmail.getText())) {
                    etEmail.requestFocus();
                } else {
                    etEmail.requestFocus();
                }
            } else {
                if (!PasswordValidator.isValid(getApplicationContext(), etPassword.getText().toString(), etRepeatPassword.getText().toString())) {
                    Nut4HealthVibrator.vibrateError(getApplicationContext());
                    Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyCreateAccount), PasswordValidator.getErrors().get(0));
                    if (!NotEmptyValidator.isValid(etEmail.getText())) {
                        etPassword.requestFocus();
                    } else {
                        etPassword.requestFocus();
                    }
                } else {
                    if (!cbTermsAndConditions.isChecked()) {
                        Nut4HealthVibrator.vibrateError(getApplicationContext());
                        Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyCreateAccount), getResources().getString(R.string.error_terms_and_conditios));
                    } else {
                        Nut4HealthKeyboard.closeKeyboard(etEmail, getApplicationContext());
                        mCreateAccountViewModel.createUser(etEmail.getText().toString(), etUsername.getText().toString(), etRepeatPassword.getText().toString());
                        disableView();
                    }

                }
            }
        }
    }

    public void createAccount(View view) {
        createAccount();
    }

    private void hasUser(User user) {
        if ((!etEmail.getText().toString().isEmpty()) && (!etUsername.getText().toString().isEmpty()) &&
                (!etPassword.getText().toString().isEmpty()) && (!etRepeatPassword.getText().toString().isEmpty())) {
            if (user != null) {
                if (user.isEmptyUser()) {
                    Nut4HealthVibrator.vibrateError(getApplicationContext());
                    Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyCreateAccount), getResources().getString(R.string.user_exist));
                } else {
                    goToSplashView();
                }
            }
            enableView();
        }
    }

    private void enableView() {
        etPassword.setEnabled(true);
        etEmail.setEnabled(true);
        etRepeatPassword.setEnabled(true);
        etUsername.setEnabled(true);
        btnCreateAccount.setEnabled(true);
        btnCreateAccount.setClickable(true);
        tvTermsAndConditions.setEnabled(true);
        cbTermsAndConditions.setEnabled(true);
        cbTermsAndConditions.setClickable(true);
    }

    private void disableView() {
        etPassword.setEnabled(false);
        etEmail.setEnabled(false);
        etRepeatPassword.setEnabled(false);
        etUsername.setEnabled(false);
        btnCreateAccount.setEnabled(false);
        btnCreateAccount.setClickable(false);
        tvTermsAndConditions.setEnabled(false);
        cbTermsAndConditions.setEnabled(false);
        cbTermsAndConditions.setClickable(false);
    }

    private void goToSplashView() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        customType(CreateAccountActivity.this,"fadein-to-fadeout");
        finish();
    }

    public void showDialogTermsAndConditions(View view) {
        String url = "https://www.sic4change.org/politica-de-privacidad";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
