package org.sic4change.nut4health.ui.error;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.splash.SplashActivity;

import static maes.tech.intentanim.CustomIntent.customType;

public class ErrorActivity extends AppCompatActivity {

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        customType(ErrorActivity.this, "fadein-to-fadeout");
        this.activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                restarApp();
            }

        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        restarApp();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void restarApp() {
        startActivity(new Intent(activity, SplashActivity.class));
        finish();

    }


}