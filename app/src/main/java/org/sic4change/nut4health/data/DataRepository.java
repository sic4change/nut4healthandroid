package org.sic4change.nut4health.data;

import android.content.Context;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.sic4change.nut4health.data.entities.User;

import java.net.UnknownServiceException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class DataRepository {

    private String TAG = DataRepository.class.getName();

    private final ExecutorService mIoExecutor;
    private static volatile DataRepository sInstance = null;

    public static DataRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(Executors.newSingleThreadExecutor());
                }
            }
        }
        return sInstance;
    }

    private DataRepository(ExecutorService executor) {
        mIoExecutor = executor;
    }

    /**
     * Method to login with firebase
     * @param email
     * @param password
     */
    public void login(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(mIoExecutor, task -> {
            try {
                if ((task != null) && (task.getResult() != null) && (task.getResult().getUser() != null)) {
                    getUser(email);
                    Log.d(TAG, "Login correct");
                } else {
                    Log.d(TAG, "Login incorrect");
                }
            } catch (Exception e) {
                Log.d(TAG, "Login incorrect");
            }
        });
    }

    /**
     * Method to get user from firebase
     * @param email
     */
    private void getUser(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("users");
        Query query = userRef.whereEqualTo("email", email).limit(1);
        query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    Log.d(TAG, "Get user: " + user.getEmail());
                } else {
                    Log.d(TAG, "Get user: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

}
