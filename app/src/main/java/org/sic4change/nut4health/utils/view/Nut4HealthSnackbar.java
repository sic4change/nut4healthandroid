package org.sic4change.nut4health.utils.view;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import org.sic4change.nut4health.R;

public class Nut4HealthSnackbar {

    public static void showError(Context context, View layout, String message) {
        Snackbar snError = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        View snErrorView = snError.getView();
        snErrorView.setBackgroundColor(context.getResources().getColor(R.color.error));
        snError.show();
    }
}
