package org.sic4change.nut4health;

import android.app.Application;

import org.sic4change.nut4health.ui.error.ErrorActivity;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupCrashView();

    }

    private void setupCrashView() {
        CaocConfig.Builder.create()
                .errorActivity(ErrorActivity.class)
                .apply();
    }

}


