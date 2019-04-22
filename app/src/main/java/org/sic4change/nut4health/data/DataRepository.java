package org.sic4change.nut4health.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.data.names.DataContractNames;
import org.sic4change.nut4health.data.names.DataUserNames;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DataRepository {

    private String TAG = DataRepository.class.getName();

    private Nut4HealtDao nut4HealtDao;
    private final ExecutorService mIoExecutor;
    private static volatile DataRepository sInstance = null;

    private ListenerRegistration listenerQuery;

    public static DataRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    Nut4HealthDatabase database = Nut4HealthDatabase.getInstance(context);
                    sInstance = new DataRepository(database.nut4HealtDao(), Executors.newSingleThreadExecutor());
                }
            }
        }
        return sInstance;
    }

    private DataRepository(Nut4HealtDao nut4HealtDao, ExecutorService executor) {
        this.nut4HealtDao = nut4HealtDao;
        this.mIoExecutor = executor;
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
                    Log.d(TAG, "Login correct with firebase");
                } else {
                    nut4HealtDao.deleteAllUser();
                    nut4HealtDao.insert(User.userEmpty);
                    Log.d(TAG, "Login incorrect with firebase");
                }
            } catch (Exception e) {
                nut4HealtDao.deleteAllUser();
                nut4HealtDao.insert(User.userEmpty);
                Log.d(TAG, "Login incorrect with firebase");
            }
        });
    }

    /**
     * Method to get user from firebase
     * @param email
     */
    private void getUser(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
        Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
        query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    Log.d(TAG, "Get user from firebase: " + user.getEmail());
                    nut4HealtDao.deleteEmptyUser();
                    nut4HealtDao.insert(user);
                    Log.d(TAG, "User inserted in local database : " + user.getEmail());
                } else {
                    Log.d(TAG, "Get user from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

    /**
     * Method to get current user from local bd
     * @return
     */
    public LiveData<User> getCurrentUser() {
        try {
            return mIoExecutor.submit(() -> nut4HealtDao.getCurrentUser()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUser(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
        Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
        query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    Log.d(TAG, "Get user from firebase: " + user.getEmail());
                    nut4HealtDao.updateNameUser(user.getName(), email);
                    nut4HealtDao.updateSurnameUser(user.getSurname(), email);
                    nut4HealtDao.updateCountryUser(user.getCountry(), email);
                    nut4HealtDao.updateCountryCodeUser(user.getCountryCode(), email);
                    Log.d(TAG, "User updated in local database : " + user.getEmail());
                } else {
                    Log.d(TAG, "Get user from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

    /**
     * Method to get user from local bd
     * @return
     */
    public LiveData<User> getUser() {
        try {
            return mIoExecutor.submit(() -> nut4HealtDao.getUser()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to reset pasword with firebase
     * @param email
     */
    public void resetPassword(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email);
    }

    /**
     * Method to create user
     * @param email
     * @param username
     * @param password
     */
    public void createUser(String email, String username, String password, String role) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(mIoExecutor, task -> {
            try {
                if ((task != null) && (task.getResult() != null) && (task.getResult().getUser() != null)) {
                    Log.d(TAG, "Create user correct with firebase auth");
                    User user = new User(email, username, role);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
                    Query query = userRef.whereEqualTo(DataUserNames.COL_USERNAME, username).limit(1);
                    listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
                        try {
                            if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                                    && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                                Log.d(TAG, "Error user exist in firebase with the same username");
                                nut4HealtDao.deleteAllUser();
                                nut4HealtDao.insert(User.userEmpty);
                                deleteAutenticatedUser(email, password);
                            } else {
                                userRef.add(user).addOnCompleteListener(mIoExecutor, task1 -> {
                                    nut4HealtDao.deleteEmptyUser();
                                    nut4HealtDao.insert(user);
                                });
                            }
                        } catch (Exception error) {
                            userRef.add(user).addOnCompleteListener(mIoExecutor, task1 -> {
                                nut4HealtDao.deleteEmptyUser();
                                nut4HealtDao.insert(user);
                            });
                        }
                        listenerQuery.remove();
                    });
                } else {
                    Log.d(TAG, "Error user exist in firebase with same email");
                    nut4HealtDao.deleteAllUser();
                    nut4HealtDao.insert(User.userEmpty);
                }
            } catch (Exception e) {
                Log.d(TAG, "Error create user in firebase: " + e.getMessage());
                nut4HealtDao.deleteAllUser();
                nut4HealtDao.insert(User.userEmpty);
            }
        });
    }

    /**
     * MEthod to delete autenticated user
     * @param email
     * @param password
     */
    public void deleteAutenticatedUser(String email, String password) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> user.delete().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Log.d(TAG, "User account deleted.");
                    }
                }));
    }

    /**
     * Method to change user avatar
     * @param email
     * @param username
     * @param urlPhoto
     */
    public void changePhoto(String email, String username, String urlPhoto) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
        Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
        listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference().child("avatars/" + username);
                    storageRef.putFile(Uri.fromFile(new File(urlPhoto))).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnCompleteListener(mIoExecutor, task -> {
                        user.setPhoto(task.getResult().toString());
                        queryDocumentSnapshots.getDocuments().get(0).getReference().set(user);
                        nut4HealtDao.updatePhotoUser(task.getResult().toString(), email);
                        listenerQuery.remove();
                    }));

                } else {
                    Log.d(TAG, "Get user from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

    /**
     * Method to change name user
     * @param email
     * @param name
     */
    public void changeName(String email, String name) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
        Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
        listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    user.setName(name);
                    queryDocumentSnapshots.getDocuments().get(0).getReference().set(user);
                    nut4HealtDao.updateNameUser(name, email);
                    listenerQuery.remove();
                } else {
                    Log.d(TAG, "Get user from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

    /**
     * Method to change surname user
     * @param email
     * @param surname
     */
    public void changeSurname(String email, String surname) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
        Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
        listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    user.setSurname(surname);
                    queryDocumentSnapshots.getDocuments().get(0).getReference().set(user);
                    nut4HealtDao.updateSurnameUser(surname, email);
                    listenerQuery.remove();
                } else {
                    Log.d(TAG, "Get user from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

    /**
     * Method to change country user
     * @param email
     * @param country
     * @param countryCode
     */
    public void changeCountry(String email, String country, String countryCode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
        Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
        listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    user.setCountry(country);
                    user.setCountryCode(countryCode);
                    queryDocumentSnapshots.getDocuments().get(0).getReference().set(user);
                    nut4HealtDao.updateCountryUser(country, email);
                    nut4HealtDao.updateCountryCodeUser(countryCode, email);
                    listenerQuery.remove();
                } else {
                    Log.d(TAG, "Get user from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

    /**
     * Method to logout
     */
    public void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        mIoExecutor.submit(() -> nut4HealtDao.deleteAllUser());
    }

    /**
     * Method to remove account
     * @param email
     */
    public void removeAccount(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.getCurrentUser().delete();
        Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
        listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    user.setEmail("anonymous" + queryDocumentSnapshots.getDocuments().get(0).getId() + "@anonymous.com");
                    user.setUsername("anonymous" + queryDocumentSnapshots.getDocuments().get(0).getId());
                    queryDocumentSnapshots.getDocuments().get(0).getReference().set(user);
                    nut4HealtDao.deleteAllUser();
                    listenerQuery.remove();
                } else {
                    Log.d(TAG, "Get user from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

    /**
     * Method to create a contract
     * @param email
     * @param latitude
     * @param longitude
     * @param photo
     * @param childName
     * @param childUsername
     * @param childAddress
     * @param status
     */
    public void createContract(String email, float latitude, float longitude, String photo, String childName, String childUsername,
                               String childAddress, String status) {
        String hash = "";
        try {
            hash = Files.hash(new File(photo), Hashing.sha512()).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Contract contract = new Contract(photo, latitude, longitude, email, childName, childUsername, childAddress,
                Contract.Status.INIT.name(), new Date().getTime(), hash);
        contract.setStatus(status);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contractRef = db.collection(DataContractNames.TABLE_FIREBASE_NAME);
        contractRef.add(contract).addOnCompleteListener(mIoExecutor, task -> {
            contract.setId(task.getResult().getId());
            task.getResult().set(contract);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("contracts/" + contract.getId());
            storageRef.putFile(Uri.fromFile(new File(contract.getPhoto()))).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnCompleteListener(mIoExecutor, storageTask -> {
                contract.setPhoto(storageTask.getResult().toString());
                task.getResult().set(contract);

                FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                CollectionReference userRef = db1.collection(DataUserNames.TABLE_FIREBASE_NAME);
                Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
                listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
                    try {
                        if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                                && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                            User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                            user.setPoints(user.getPoints() + 1);
                            queryDocumentSnapshots.getDocuments().get(0).getReference().set(user);
                            nut4HealtDao.deleteAllUser();
                            listenerQuery.remove();
                        } else {
                            Log.d(TAG, "Get user from firebase: " + "empty");
                        }
                    } catch (Exception error) {
                        Log.d(TAG, "Get user: " + "empty");
                    }
                });
            }));
        });
    }

}
