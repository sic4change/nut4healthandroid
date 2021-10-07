package org.sic4change.nut4health.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;

import static android.content.Context.VIBRATOR_SERVICE;


public class Nut4HealthVibrator {

    private static final int ERROR_VIBRATE_LENGTh_MILISECONDS = 200;

    /**
     * Method to vibrate when error is appearing
     * @param context
     */
    public static void vibrateError(Context context) {
        android.os.Vibrator vibrator = (android.os.Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(ERROR_VIBRATE_LENGTh_MILISECONDS, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(ERROR_VIBRATE_LENGTh_MILISECONDS);
        }
    }
}
