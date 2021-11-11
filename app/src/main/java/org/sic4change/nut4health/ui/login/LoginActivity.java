package org.sic4change.nut4health.ui.login;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.create_account.CreateAccountActivity;
import org.sic4change.nut4health.ui.main.MainActivity;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;
import org.sic4change.nut4health.utils.Nut4HealthVibrator;
import org.sic4change.nut4health.utils.validators.EmailValidator;
import org.sic4change.nut4health.utils.validators.NotEmptyValidator;
import org.sic4change.nut4health.utils.validators.PasswordValidator;
import org.sic4change.nut4health.utils.view.Nut4HealthSnackbar;

import static maes.tech.intentanim.CustomIntent.customType;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;


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
    private TextView tvVersion;

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
        tvVersion = findViewById(R.id.version);
        showCurrentVersion();
    }

    private void hasUser(User user) {
        if ((!etEmail.getText().toString().isEmpty()) &&
                (!etPassword.getText().toString().isEmpty())) {
            if (user != null) {
                if (user.isEmptyUser()) {
                    Nut4HealthVibrator.vibrateError(getApplicationContext());
                    Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.incorrect_user_or_password));
                } else {
                    if (mLoginViewModel != null) {
                        mLoginViewModel = null;
                        goToMainActivity();
                    }
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
                    mLoginViewModel.login(etEmail.getText().toString().trim().toLowerCase(), etPassword.getText().toString().trim());
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
                showToastChangePassword();
            } else {
                Nut4HealthVibrator.vibrateError(getApplicationContext());
                Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyLogin), getResources().getString(R.string.invalid_email));
            }
        }
    }

    public void showToastChangePassword() {
        new AwesomeInfoDialog(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.sent_instructions_to_change_password))
                .setPositiveButtonText(getResources().getString(R.string.ok))
                .setPositiveButtonClick(() -> {
                    mLoginViewModel.resetPassword(etEmail.getText().toString());
                    new AwesomeSuccessDialog(this)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(getResources().getString(R.string.sent_instructions_to_change_password_ok))
                            .setPositiveButtonText(getResources().getString(R.string.ok))
                            .setPositiveButtonClick(() -> {

                            })
                            .show();
                }).show();
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

    public void showDialogTermsAndConditionsSAM(View view) {
        String url = "https://firebasestorage.googleapis.com/v0/b/nut4health-830cc.appspot.com/o/SAMPhoto.pdf?alt=media&token=a5f10351-d873-4fc0-83fe-a9c9e6b3edf6";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void showCurrentVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            TextView tvVersion = findViewById(R.id.version);
            tvVersion.setText("Version - " + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
