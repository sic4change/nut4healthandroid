package org.sic4change.nut4health.ui.login;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.arch.lifecycle.ViewModelProviders;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.main.MainActivity;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;
import org.sic4change.nut4health.utils.Nut4HealthVibrator;
import org.sic4change.nut4health.utils.validators.EmailValidator;
import org.sic4change.nut4health.utils.validators.NotEmptyValidator;
import org.sic4change.nut4health.utils.validators.PasswordValidator;
import org.sic4change.nut4health.utils.view.Nut4HealthSnackbar;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel mLoginViewModel;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvResetPassword;
    private LinearLayout lyCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        LoginViewModelFactory loginViewModelFactory = LoginViewModelFactory.createFactory(this);
        mLoginViewModel = ViewModelProviders.of(this, loginViewModelFactory).get(LoginViewModel.class);
        mLoginViewModel.getUser().observe(this, this::hasUser);
    }

    private void initView() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvResetPassword = findViewById(R.id.tvResetPassword);
        lyCreateAccount = findViewById(R.id.lyCreateAccount);
    }

    private void hasUser(User user) {
        //if ((!etPassword.getText().toString().isEmpty()) && (!etEmail.getText().toString().isEmpty())) {
            if (user != null) {
                if (user.isEmptyUser()) {
                    Nut4HealthVibrator.vibrateError(getApplicationContext());
                    Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.incorrect_user_or_password));
                } else {
                    cleanFields();
                    goToMainActivity();
                }
            } /*else if (user == null) {
                Nut4HealthVibrator.vibrateError(getApplicationContext());
                Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.incorrect_user_or_password));
            }*/
        //}
    }

    private void cleanFields() {
        etPassword.setText("");
        etEmail.setText("");
    }

    public void login(View view) {
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

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
