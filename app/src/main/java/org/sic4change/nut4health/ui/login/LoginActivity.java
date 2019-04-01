package org.sic4change.nut4health.ui.login;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.arch.lifecycle.ViewModelProviders;
import org.sic4change.nut4health.R;


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
        mLoginViewModel.isLogued().observe(this, this::hasLoggued);
    }

    private void initView() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> mLoginViewModel.login(etEmail.getText().toString(), etPassword.getText().toString()));
        tvResetPassword = findViewById(R.id.tvResetPassword);
        lyCreateAccount = findViewById(R.id.lyCreateAccount);
    }

    private void hasLoggued(Boolean loggued) {
        if (!loggued) {
            //mostrar error
            return;
        }
        finish();
    }
}
