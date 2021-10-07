package org.sic4change.nut4health.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.login.LoginActivity;
import org.sic4change.nut4health.ui.main.MainActivity;

import static maes.tech.intentanim.CustomIntent.customType;

public class SplashActivity extends AppCompatActivity {

    private Activity activity;

    private SplashViewModel mSplashViewModel;

    private static final long  SPLASH_DELAY_MILISECONDS = 3000;
    private static final long  SPLASH_TICK_MILISECONDS  = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }
        this.activity = this;
        SplashViewModelFactory splashViewModelFactory = SplashViewModelFactory.createFactory(this);
        mSplashViewModel = ViewModelProviders.of(this, splashViewModelFactory).get(SplashViewModel.class);
        mSplashViewModel.getCurrentUser().observe(this, user -> {
            if (mSplashViewModel != null) {
                mSplashViewModel.saveSelection(1);
                if (user == null) {
                    goToLoginActivity();
                } else {
                    if ((user.getId() != null) && (!user.getId().isEmpty())) {
                        mSplashViewModel.subscribeToTopicId(user.getId());
                        mSplashViewModel.subscribeToTopicUsername(user.getUsername());
                        mSplashViewModel.subscribeToGlobalNotification();
                    }
                    goToMainActivity();
                }
                mSplashViewModel = null;
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
                customType(SplashActivity.this,"bottom-to-up");
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
                customType(SplashActivity.this,"bottom-to-up");
                finish();
            }
        }.start();

    }

}
