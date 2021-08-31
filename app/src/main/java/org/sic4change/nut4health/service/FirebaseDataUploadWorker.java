package org.sic4change.nut4health.service;


import android.content.Context;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDataUploadWorker extends Worker {

    public FirebaseDataUploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public ListenableWorker.Result doWork() {
        FirebaseFirestore.getInstance().enableNetwork();
        return ListenableWorker.Result.success();
    }


}
