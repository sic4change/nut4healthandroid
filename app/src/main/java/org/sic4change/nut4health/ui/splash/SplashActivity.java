package org.sic4change.nut4health.ui.splash;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.login.LoginActivity;
import org.sic4change.nut4health.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private Activity activity;

    private SplashViewModel mSplashViewModel;

    private static final long  SPLASH_DELAY_MILISECONDS = 3000;
    private static final long  SPLASH_TICK_MILISECONDS  = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.activity = this;
        SplashViewModelFactory splashViewModelFactory = SplashViewModelFactory.createFactory(this);
        mSplashViewModel = ViewModelProviders.of(this, splashViewModelFactory).get(SplashViewModel.class);
        mSplashViewModel.getCurrentUser().observe(this, user -> {
            if (user == null) {
                goToLoginActivity();
            } else {
                goToMainActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Disable back pressed
    }

    public void goToMainActivity() {

        new CountDownTimer(SPLASH_DELAY_MILISECONDS, SPLASH_TICK_MILISECONDS) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    public void goToLoginActivity() {

        new CountDownTimer(SPLASH_DELAY_MILISECONDS, SPLASH_TICK_MILISECONDS) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

}
