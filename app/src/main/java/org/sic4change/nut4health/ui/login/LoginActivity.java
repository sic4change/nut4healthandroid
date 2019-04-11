package org.sic4change.nut4health.ui.login;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.arch.lifecycle.ViewModelProviders;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.create_account.CreateAccountActivity;
import org.sic4change.nut4health.ui.main.MainActivity;
import org.sic4change.nut4health.ui.splash.SplashActivity;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;
import org.sic4change.nut4health.utils.Nut4HealthVibrator;
import org.sic4change.nut4health.utils.validators.EmailValidator;
import org.sic4change.nut4health.utils.validators.NotEmptyValidator;
import org.sic4change.nut4health.utils.validators.PasswordValidator;
import org.sic4change.nut4health.utils.view.Nut4HealthSnackbar;

import static maes.tech.intentanim.CustomIntent.customType;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel mLoginViewModel;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvResetPassword;
    private TextView tvTermsAndConditions;
    private LinearLayout lyCreateAccount;
    private TextView tvNewUser;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        enableView();
        LoginViewModelFactory loginViewModelFactory = LoginViewModelFactory.createFactory(this);
        mLoginViewModel = ViewModelProviders.of(this, loginViewModelFactory).get(LoginViewModel.class);
        mLoginViewModel.getUser().observe(this, this::hasUser);
    }

    private void initView() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvResetPassword = findViewById(R.id.tvResetPassword);
        tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        lyCreateAccount = findViewById(R.id.lyCreateAccount);
        tvNewUser = findViewById(R.id.tvNewUser);
        tvSignUp = findViewById(R.id.tvSignUp);
        etPassword.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                login();
                return true;
            }
            return false;
        });
    }

    private void hasUser(User user) {
        if ((!etEmail.getText().toString().isEmpty()) &&
                (!etPassword.getText().toString().isEmpty())) {
            if (user != null) {
                if (user.isEmptyUser()) {
                    Nut4HealthVibrator.vibrateError(getApplicationContext());
                    Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.incorrect_user_or_password));
                } else {
                    goToMainActivity();
                }
            }
            enableView();
        }
    }

    private void enableView() {
        etPassword.setEnabled(true);
        etEmail.setEnabled(true);
        btnLogin.setEnabled(true);
        btnLogin.setClickable(true);
        tvResetPassword.setEnabled(true);
        tvTermsAndConditions.setEnabled(true);
        tvNewUser.setEnabled(true);
        tvSignUp.setEnabled(true);
    }

    private void disableView() {
        etPassword.setEnabled(false);
        etEmail.setEnabled(false);
        btnLogin.setEnabled(false);
        btnLogin.setClickable(false);
        tvResetPassword.setEnabled(false);
        tvTermsAndConditions.setEnabled(false);
        tvNewUser.setEnabled(false);
        tvSignUp.setEnabled(false);
    }

    private void login() {
        if (!NotEmptyValidator.isValid(etEmail.getText()) || !NotEmptyValidator.isValid(etPassword.getText())) {
            Nut4HealthVibrator.vibrateError(getApplicationContext());
            Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.mandatory_field));
            if (!NotEmptyValidator.isValid(etEmail.getText())) {
                etEmail.requestFocus();
            } else {
                etPassword.requestFocus();
            }
        } else {
            if (EmailValidator.isValidEmail(etEmail.getText())) {
                if (PasswordValidator.isValid(getApplicationContext(), etPassword.getText().toString(), etPassword.getText().toString())) {
                    mLoginViewModel.login(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                    disableView();
                } else {
                    Nut4HealthVibrator.vibrateError(getApplicationContext());
                    Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), PasswordValidator.getErrors().get(0));
                }
            } else {
                Nut4HealthVibrator.vibrateError(getApplicationContext());
                Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.invalid_email));
            }
        }
        Nut4HealthKeyboard.closeKeyboard(etEmail, getApplicationContext());
    }

    public void login(View view) {
        login();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        customType(LoginActivity.this,"bottom-to-up");
        finish();
    }

    public void resetPassword(View view) {
        if (!NotEmptyValidator.isValid(etEmail.getText())) {
            Nut4HealthVibrator.vibrateError(getApplicationContext());
            Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.mandatory_field));
            if (!NotEmptyValidator.isValid(etEmail.getText())) {
                etEmail.requestFocus();
            } else {
                etPassword.requestFocus();
            }
        } else {
            if (EmailValidator.isValidEmail(etEmail.getText())) {
                Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.sent_instructions_to_change_password));
                mLoginViewModel.resetPassword(etEmail.getText().toString());
            } else {
                Nut4HealthVibrator.vibrateError(getApplicationContext());
                Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.invalid_email));
            }
        }
    }

    public void goToCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        customType(LoginActivity.this,"left-to-right");
        finish();
    }

    public void showDialogTermsAndConditions(View view) {
        String url = "https://www.sic4change.org/politica-de-privacidad";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
