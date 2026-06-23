package org.sic4change.nut4health.utils.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class NUT4HealthDialog {

    private NUT4HealthDialog() {
    }

    public static void showAlertDialog(Context context, String title, String message, String textButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message);
        builder.setPositiveButton(textButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

