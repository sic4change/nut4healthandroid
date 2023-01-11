package org.sic4change.nut4health.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.math.BigDecimal;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;
import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.blockchain.utils.Loaders;
import org.sic4change.nut4health.blockchain.utils.RevertReasonExtractor;
import org.sic4change.nut4health.data.entities.Configuration;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.MalnutritionChildTable;
import org.sic4change.nut4health.data.entities.Near;
import org.sic4change.nut4health.data.entities.Notification;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.data.entities.PaymentFirebase;
import org.sic4change.nut4health.data.entities.Point;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.Report;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.data.events.MessageEvent;
import org.sic4change.nut4health.data.names.DataConfigurationNames;
import org.sic4change.nut4health.data.names.DataContractNames;
import org.sic4change.nut4health.data.names.DataMalnutritionChildTableNames;
import org.sic4change.nut4health.data.names.DataNotificationNames;
import org.sic4change.nut4health.data.names.DataPaymentNames;
import org.sic4change.nut4health.data.names.DataPointNames;
import org.sic4change.nut4health.data.names.DataRankingNames;
import org.sic4change.nut4health.data.names.DataUserNames;
import org.sic4change.nut4health.utils.time.Nut4HealthTimeUtil;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DataRepository {

    private String TAG = DataRepository.class.getName();

    private final static int PAGE_SIZE = 100000;

    private Nut4HealtDao nut4HealtDao;
    private final ExecutorService mIoExecutor;
    private static volatile DataRepository sInstance = null;

    private ListenerRegistration listenerQuery;
    private ListenerRegistration listenerMarkNotification;

    private static Context mContext;

    public static DataRepository getInstance(Context context) {
        mContext = context;
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
                    nut4HealtDao.deleteAllConfiguration();
                    nut4HealtDao.insert(User.userEmpty);
                    Log.d(TAG, "Login incorrect with firebase");
                }
            } catch (Exception e) {
                nut4HealtDao.deleteAllUser();
                nut4HealtDao.deleteAllConfiguration();
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
                    getConfiguration(user.getConfiguration());
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
     * Method to get user from firebase
     * @param configuration
     */
    private void getConfiguration(String configuration) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference configRef = db.collection(DataConfigurationNames.TABLE_FIREBASE_NAME);
        Query query = configRef.whereEqualTo(DataConfigurationNames.COL_ID, configuration).limit(1);
        query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    Configuration config = queryDocumentSnapshots.getDocuments().get(0).toObject(Configuration.class);
                    Log.d(TAG, "Get configuration from firebase: " + config.getMoney());
                    nut4HealtDao.insert(config);
                    Log.d(TAG, "Configuration inserted in local database : " + config.getMoney());
                } else {
                    Log.d(TAG, "Get configuration from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get configuration: " + "empty");
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

    /**
     * Method to get current configuration from local bd
     * @return
     */
    public LiveData<Configuration> getCurrentConfiguration() {
        try {
            return mIoExecutor.submit(() -> nut4HealtDao.getConfiguration()).get();
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
                    nut4HealtDao.updatePointsUser(user.getPoints(), email);
                    Log.d(TAG, "User updated in local database : " + user.getEmail());
                } else {
                    Log.d(TAG, "Get user from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get user: " + "empty");
            }
        });
    }

    public void updateCurrentLocation(String email, String currentCountryUser, String currentStateUser, String currentCityUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
        Query query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
        query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    Log.d(TAG, "Get user from firebase: " + user.getEmail());
                    user.setCurrentCountry(currentCountryUser);
                    user.setCurrentState(currentStateUser);
                    user.setCurrentCity(currentCityUser);
                    //queryDocumentSnapshots.getDocuments().get(0).getReference().set(user, SetOptions.mergeFields("phone"));
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("currentCountry", currentCountryUser);
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("currentState", currentStateUser);
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("currentCity", currentCityUser);
                    nut4HealtDao.updateCurrentCountryUser(currentCountryUser, email);
                    nut4HealtDao.updateCurrentStateUser(currentStateUser, email);
                    nut4HealtDao.updateCurrentCityUser(currentCityUser, email);
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
                    user.setActive(true);
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
                                    DocumentReference docRef = task1.getResult();
                                    String key = docRef.getId();
                                    user.setId(key);
                                    nut4HealtDao.deleteEmptyUser();
                                    nut4HealtDao.insert(user);
                                });
                            }
                        } catch (Exception error) {
                            userRef.add(user).addOnCompleteListener(mIoExecutor, task1 -> {
                                nut4HealtDao.deleteEmptyUser();
                                DocumentReference docRef = task1.getResult();
                                String key = docRef.getId();
                                user.setId(key);
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
                        //queryDocumentSnapshots.getDocuments().get(0).getReference().set(user, SetOptions.mergeFields("photo"));
                        queryDocumentSnapshots.getDocuments().get(0).getReference().update("photo", user.getPhoto());
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
                    //queryDocumentSnapshots.getDocuments().get(0).getReference().set(user, SetOptions.mergeFields("phone"));
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("name", name);
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
     * Method to update points in db local
     * @param email
     * @param points
     */
    public void updatePointsUserLocal(String email, int points) {
        mIoExecutor.submit(() -> nut4HealtDao.updatePointsUser(points, email));
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
                    //queryDocumentSnapshots.getDocuments().get(0).getReference().set(user, SetOptions.mergeFields("phone"));
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("surname", surname);
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
                    //queryDocumentSnapshots.getDocuments().get(0).getReference().set(user, SetOptions.mergeFields("phone"));
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("country", country);
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("countryCode", countryCode);
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
        mIoExecutor.submit(() -> nut4HealtDao.deleteAllConfiguration());
        mIoExecutor.submit(() -> nut4HealtDao.deleteAllContract());
        mIoExecutor.submit(() -> nut4HealtDao.deleteAllNearContracts());
        mIoExecutor.submit(() -> nut4HealtDao.deleteAllRanking());
        mIoExecutor.submit(() -> nut4HealtDao.deleteAllPayment());
        mIoExecutor.submit(() -> nut4HealtDao.deleteAllNotification());
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
                    user.setEmail(queryDocumentSnapshots.getDocuments().get(0).getId() + "@anonymous.com");
                    user.setUsername(queryDocumentSnapshots.getDocuments().get(0).getId());
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
     * @param id
     * @param role
     * @param email
     * @param latitude
     * @param longitude
     * @param photo
     * @param childName
     * @param childSurname
     * @param childDNI
     * @param childBrothers
     * @param code
     * @param childTutor
     * @param childAddress
     * @param childPhoneContact
     * @param percentage
     */
    public void createContract(String id, String role, String email, double latitude, double longitude, Uri photo,
                               String childName, String childSurname, String sex, String childDNI,
                               int childBrothers, String code, String childTutor, String childAddress,
                               String childPhoneContact, String point, String pointFullName, int percentage,
                               double arm_circumference, double height, double weight, String fingerprint,
                               String duration) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contractRef = db.collection(DataContractNames.TABLE_FIREBASE_NAME);
        String status;
        if (percentage > 49) {
            status = Contract.Status.DERIVED.name();
        } else {
            status = Contract.Status.REGISTERED.name();
        }

       Contract contract = new Contract("", latitude, longitude, "", childName,
                childSurname, sex, childDNI, childBrothers, code, childTutor, childAddress, childPhoneContact, point,
                pointFullName,  "", status, "", percentage,
                new BigDecimal(arm_circumference).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue(),
                height, weight, duration);

        String newId = id + "_" + new Date().getTime();
        contract.setId(newId);
        contract.setCreationDate(new Date().toString());
        contract.setCreationDateMiliseconds(new Date().getTime());
        if (fingerprint != null && fingerprint != "") {
            contract.setFingerprint(fingerprint);
        }
        if (role.equals("Agente Salud")) {
            try {
                Query query = contractRef.whereEqualTo(DataContractNames.COL_SCREENER, email).orderBy("creationDateMiliseconds", Query.Direction.DESCENDING);
                listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
                    try {
                        if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                                && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                            boolean found = false;
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                Contract contractIt = document.toObject(Contract.class);
                                long eventStartDate = contractIt.getCreationDateMiliseconds();
                                long day30 = 30l * 24 * 60 * 60 * 1000;
                                long day7 = 7l * 24 * 60 * 60 * 1000;
                                boolean olderThan30 = new Date().before(new Date((eventStartDate + day30)));
                                boolean olderThan7 = new Date().before(new Date((eventStartDate + day7)));
                                if (contractIt.getCode().equals(contract.getCode())) {
                                    if (contractIt.getStatus().equals(Contract.Status.REGISTERED.name())) {
                                        // Lo añade como duplicado y lo hago esperar 7 días para registrar a ese menor
                                        if (olderThan7) {
                                            contract.setScreener(email);
                                            contract.setStatus(Contract.Status.DUPLICATED.name());
                                            contractRef.document(newId).set(contract).addOnCompleteListener(task -> {
                                                listenerQuery.remove();
                                                createGeoPoint(newId, latitude, longitude);
                                            });
                                            EventBus.getDefault().post(new MessageEvent(mContext.getString(R.string.diagnosis_duplicated_7)));
                                            found = true;
                                            break;
                                        }
                                    } else if (contractIt.getStatus().equals(Contract.Status.ADMITTED.name())) {
                                        if (olderThan30) {
                                            // Lo añade como duplicado y lo hago esperar 30 días para registrar a ese menor
                                            contract.setScreener(email);
                                            contract.setStatus(Contract.Status.DUPLICATED.name());
                                            contractRef.document(newId).set(contract).addOnCompleteListener(task -> {
                                                listenerQuery.remove();
                                                createGeoPoint(newId, latitude, longitude);
                                            });
                                            EventBus.getDefault().post(new MessageEvent(mContext.getString(R.string.diagnosis_duplicated_30)));
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (listenerQuery != null) {
                                listenerQuery.remove();
                            }
                            if (!found) {
                                //Si no lo encuentra debe añadirlo
                                contract.setScreener(email);
                                contract.setStatus(status);
                                contractRef.document(newId).set(contract).addOnCompleteListener(task -> {
                                    listenerQuery.remove();
                                    createGeoPoint(newId, latitude, longitude);
                                });
                                EventBus.getDefault().post(new MessageEvent(mContext.getString(R.string.diagnosis_ok)));
                            }
                        } else {
                            contract.setScreener(email);
                            contract.setStatus(status);
                            contractRef.document(newId).set(contract).addOnCompleteListener(task -> {
                                listenerQuery.remove();
                                createGeoPoint(newId, latitude, longitude);
                            });
                            EventBus.getDefault().post(new MessageEvent(mContext.getString(R.string.diagnosis_ok)));
                        }
                        var nut4health = Loaders.loadNut4HealthContract(mContext);
                        System.out.println("Aqui Contract address: " + nut4health.getContractAddress());

                        // Call example
                        try {
                            //var screenerRoleId = nut4health.ROLE_SCREENER().send();
                            //System.out.println("Aqui Has screener role: " + nut4health.hasRole(screenerRoleId, "0x1aa49faa8136d6ade31519ab2606996acd46af51").send());
                            //var healthCenter = nut4health.createHealthCentre("rosa_desierto").send();
                            //System.out.println("Aqui Transaction mined in block number " + healthCenter.getBlockNumber());
                            var screenerRoleId = nut4health.ROLE_SCREENER().send();
                            // Esta cuenta 0x7Bd5677D481Ce6429Ff2d0B113d53f855fa40166 existe y la puedes ver en Firebase, también la lee del screener.json para registrar el diagnostico
                            var grantRole = nut4health.grantRole(screenerRoleId, "0x7Bd5677D481Ce6429Ff2d0B113d53f855fa40166");
                            System.out.println("Transaction mined in block number " + grantRole);
                            System.out.println("Has screener role: " + nut4health.hasRole(screenerRoleId, "0x7Bd5677D481Ce6429Ff2d0B113d53f855fa40166").send());
                            var txReceipt = nut4health.registerDiagnosis(String.format(newId, System.currentTimeMillis()), "rosa_desierto").send();
                            System.out.println("Transaction mined in block number " + txReceipt.getBlockNumber());
                        } catch (TransactionException err) {
                            System.out.println("Transaction reverted: " + RevertReasonExtractor.revertReasonToAscii(err.getTransactionReceipt().get()));
                        }

                        // Transaction example
                        /*try {
                            //var healthCenter = nut4health.createHealthCentre("test_hc").send();
                            //System.out.println("Aqui Transaction mined in block number " + healthCenter.getBlockNumber());
                            //var txReceipt = nut4health.registerDiagnosis(String.format(newId, System.currentTimeMillis()), "test_hc").send();
                            //System.out.println("Aqui Transaction mined in block number " + txReceipt.getBlockNumber());
                        } catch (TransactionException err) {
                            System.out.println("Aqui Transaction reverted: " + RevertReasonExtractor.revertReasonToAscii(err.getTransactionReceipt().get()));
                        }*/
                    } catch (Exception error) {
                        Log.d(TAG, "Get contract: " + error);
                    }
                });
            } catch (Exception e){
                Log.d(TAG, "Get contract: " + e);
            }
        } else {
            try {
                Query query = contractRef.whereEqualTo(DataContractNames.COL_POINT, point).orderBy(DataContractNames.COL_DATE_MILI_FIREBASE, Query.Direction.ASCENDING);
                listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
                    boolean updated = false;
                    int count = 0;
                    try {
                        if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                                && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                Contract contractIt = document.toObject(Contract.class);
                                if (contractIt.getCode().equals(contract.getCode())) {
                                    if (contractIt.getStatus().equals(Contract.Status.REGISTERED.name())) {
                                        contractIt.setArm_circumference_medical(arm_circumference);
                                        contractIt.setHeight(height);
                                        contractIt.setWeight(weight);
                                        contractIt.setPercentage(percentage);
                                        contractIt.setMedical(email);
                                        document.getReference().set(contractIt, SetOptions.merge());
                                    } else {
                                        contractIt.setChildName(childName);
                                        contractIt.setChildSurname(childSurname);
                                        contractIt.setSex(sex);
                                        contractIt.setChildDNI(childDNI);
                                        contractIt.setChildTutor(childTutor);
                                        contractIt.setChildAddress(childAddress);
                                        contractIt.setChildPhoneContract(childPhoneContact);
                                        contractIt.setArm_circumference_medical(arm_circumference);
                                        contractIt.setHeight(height);
                                        contractIt.setWeight(weight);
                                        contractIt.setPercentage(percentage);
                                        contractIt.setMedical(email);
                                        if (contractIt.getStatus().equals(Contract.Status.DERIVED.name())) {
                                            if (count != 0) {
                                                contractIt.setStatus(Contract.Status.DUPLICATED.name());
                                            } else {
                                                contractIt.setStatus(Contract.Status.ADMITTED.name());
                                            }
                                            count++;
                                            document.getReference().set(contractIt, SetOptions.merge());
                                        }

                                    }
                                    updated = true;
                                }
                            }

                            if (listenerQuery != null) {
                                listenerQuery.remove();
                            }
                            //Si no lo encuentra el medico debe añadirlo
                            if (!updated) {
                                contract.setMedical(email);
                                contract.setArm_circumference(0.0);
                                contract.setArm_circumference_medical(arm_circumference);
                                contract.setHeight(height);
                                contract.setWeight(weight);
                                contractRef.document(newId).set(contract).addOnCompleteListener(task -> {
                                    listenerQuery.remove();
                                    createGeoPoint(newId, latitude, longitude);
                                });
                                EventBus.getDefault().post(new MessageEvent(mContext.getString(R.string.diagnosis_ok)));
                            } else {
                                EventBus.getDefault().post(new MessageEvent(mContext.getString(R.string.diagnosis_updated)));
                            }
                        } else {
                            contract.setMedical(email);
                            contract.setArm_circumference(0.0);
                            contract.setArm_circumference_medical(arm_circumference);
                            contract.setHeight(height);
                            contract.setWeight(weight);
                            contractRef.document(newId).set(contract).addOnCompleteListener(task -> {
                                listenerQuery.remove();
                                createGeoPoint(newId, latitude, longitude);
                            });
                            EventBus.getDefault().post(new MessageEvent(mContext.getString(R.string.diagnosis_ok)));
                        }
                    } catch (Exception error) {
                        Log.d(TAG, "Get contract: " + error);
                    }
                });
            } catch (Exception e){
                Log.d(TAG, "Get contract: " + e);
            }
        }
    }

    private void createGeoPoint(String id, double latitude, double longitude) {
        try {
            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(DataContractNames.TABLE_FIREBASE_NAME);
            GeoFirestore geoFirestore = new GeoFirestore(collectionReference);
            geoFirestore.setLocation(id, new GeoPoint(latitude, longitude));
        } catch (Exception e) {
            Log.d(TAG, "Impossible create geopoint " + e);
        }
    }

    /**
     * Method to get contracts from firebase
     * @param email
     * @param role
     */
    public void getContracts(String email, String role) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contractRef = db.collection(DataContractNames.TABLE_FIREBASE_NAME);
        Query query;
        if (role.equals("Agente Salud")) {
            query = contractRef.whereEqualTo(DataContractNames.COL_SCREENER, email);
            query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
                try {
                    if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                            && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Contract contract = document.toObject(Contract.class);
                            if (contract.getId() != null && !contract.getId().isEmpty()) {
                                nut4HealtDao.insert(contract);
                            }
                        }
                    } else {
                        nut4HealtDao.deleteAllContract();
                        Log.d(TAG, "Get contracts: " + "empty");
                    }
                } catch (Exception error) {
                    Log.d(TAG, "Get contracts: " + "empty");
                }
            });
        } else {
            CollectionReference userRef = db.collection(DataUserNames.TABLE_FIREBASE_NAME);
            query = userRef.whereEqualTo(DataUserNames.COL_EMAIL, email).limit(1);
            query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
                try {
                    if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                            && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                        User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                        Query queryContracts = contractRef.whereEqualTo(DataContractNames.COL_POINT, user.getPoint());
                        queryContracts.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots2, e2) -> {
                            try {
                                if ((queryDocumentSnapshots2 != null) && (queryDocumentSnapshots2.getDocuments() != null)
                                        && (queryDocumentSnapshots2.getDocuments().size() > 0)) {
                                    for (DocumentSnapshot document : queryDocumentSnapshots2.getDocuments()) {
                                        Contract contract = document.toObject(Contract.class);
                                        if (contract.getId() != null && !contract.getId().isEmpty()) {
                                            nut4HealtDao.insert(contract);
                                        }
                                    }
                                } else {
                                    nut4HealtDao.deleteAllContract();
                                    Log.d(TAG, "Get contracts: " + "empty");
                                }
                            } catch (Exception error) {
                                Log.d(TAG, "Get contracts: " + "empty");
                            }
                        });
                    } else {
                        Log.d(TAG, "Get user from firebase: " + "empty");
                    }
                } catch (Exception error) {
                    Log.d(TAG, "Get user: " + "empty");
                }
            });
        }

    }

    /**
     * Method to get contracts sorted
     * @param sort
     * @param name
     * @param surname
     * @param status
     * @param dateStart
     * @param dateEnd
     * @param percentageMin
     * @param percentageMax
     * @return
     */
    public LiveData<PagedList<Contract>> getSortedContracts(String sort, String name, String surname,
                                                            String status, long dateStart, long dateEnd,
                                                            int percentageMin, int percentageMax) {
        SimpleSQLiteQuery query = SortUtils.getFilterContracts(sort, name, surname, status, dateStart, dateEnd,
                percentageMin, percentageMax);
        LiveData<PagedList<Contract>> contracts = new LivePagedListBuilder<>(nut4HealtDao.getUserContracts(query), PAGE_SIZE).build();
        return contracts;
    }

    /**
     * Method to get a contract
     * @param id
     * @return
     */
    public LiveData<Contract> getContract(String id) {
        return nut4HealtDao.getContract(id);
    }

    /**
     * Method to get ranking sorted
     * @param sort
     * @param username
     * @return
     */
    public LiveData<PagedList<Ranking>> getSortedRanking(String sort, String username) {
        SimpleSQLiteQuery query = SortUtils.getAllQueryRanking(sort, username);
        return new LivePagedListBuilder<>(nut4HealtDao.getRanking(query), PAGE_SIZE).build();
    }

    /**
     * Method to get ranking from firebase
     */
    public void getRanking() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contractRef = db.collection(DataRankingNames.TABLE_FIREBASE_NAME);
        contractRef.orderBy(DataRankingNames.COL_POINTS).addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    List<User> users = new ArrayList<User>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        User user = document.toObject(User.class);
                        if (!user.getUsername().contains("anonymous") && user.getRole().equals("Agente Salud")) {
                            users.add(user);
                        }
                    }
                    int i = users.size();
                    for (User user : users) {
                        Ranking ranking = new Ranking(user.getUsername(), user.getPhoto(), user.getPoints());
                        ranking.setPosition(i);
                        nut4HealtDao.insert(ranking);
                        i--;
                    }
                } else {
                    Log.d(TAG, "Get ranking: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get ranking: " + "empty");
            }
        });
    }

    /**
     * Method to get user ranking
     * @param username
     * @return
     */
    public LiveData<Ranking> getUserRanking(String username) {
        return nut4HealtDao.getUserRanking(username);
    }

    /**
     * Method to get payments from firebase
     * @param email
     */
    public void getPayments(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference paymentsRef = db.collection(DataPaymentNames.TABLE_FIREBASE_NAME);
        Query query = paymentsRef.whereEqualTo(DataPaymentNames.COL_SCREENER, email);
        query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    /*if (!queryDocumentSnapshots.getDocuments().get(0).getMetadata().isFromCache()) {
                        nut4HealtDao.deleteAllPayment();
                    }*/
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        try {
                            Payment payment = document.toObject(Payment.class);
                            if (((payment.getId() != null) && (!payment.getId().equals("")))) {
                                nut4HealtDao.insert(payment);
                            }
                        } catch (Exception eFirebase) {
                            PaymentFirebase paymentFirebase = document.toObject(PaymentFirebase.class);
                            Payment payment = new Payment(paymentFirebase.getId(),
                                    paymentFirebase.getCreationDate(), Long.parseLong(paymentFirebase.getCreationDateMiliseconds()),
                                    paymentFirebase.getScreener(), Integer.parseInt(paymentFirebase.getQuantity()),
                                    paymentFirebase.getType(), paymentFirebase.getStatus(), paymentFirebase.getContractId());
                            if (((payment.getId() != null) && (!payment.getId().equals("")))) {
                                nut4HealtDao.insert(payment);
                            }
                        }

                    }
                } else {
                    nut4HealtDao.deleteAllPayment();
                    Log.d(TAG, "Get payments: " + "empty");
                }
        });
    }

    /**
     * Method to get payments sorted
     * @param sort
     * @param status
     * @return
     */
    public LiveData<PagedList<Payment>> getSortedPayments(String sort, String status, long dateStart, long dateEnd) {
        SimpleSQLiteQuery query = SortUtils.getFilterPayments(sort, status, dateStart, dateEnd);
        return new LivePagedListBuilder<>(nut4HealtDao.getPayments(query), PAGE_SIZE).build();
    }

    /**
     * Method to subscribe to notification
     * @param id
     */
    public void subscribeToNotificationTopic(String id) {
        try {
            String finalFormattedId = formatTopic(id);
            FirebaseMessaging.getInstance().subscribeToTopic(finalFormattedId)
                    .addOnCompleteListener(task -> Log.d(TAG, "Subscribe to notification topic: " + finalFormattedId));
        } catch (Exception e) {
            Log.d(TAG, "Error subscribing to topic");
        }
    }

    /**
     * Method to unsubscribe to notification
     * @param id
     */
    public void unsubscribeToNotificationTopic(String id) {
        if (id != null) {
            try {
                String finalFormattedId = formatTopic(id);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(finalFormattedId)
                        .addOnCompleteListener(task -> Log.d(TAG, "Unsubscribe to notification topic: " + finalFormattedId));
            } catch (Exception e) {
                Log.d(TAG, "Error unsubscribing to topic");
            }
        }
    }

    private static String formatTopic(String id) {
        String formattedId = id.replace(" ", "");
        formattedId = formattedId.replace("ñ", "n");
        formattedId = formattedId.replace("Ñ", "N");
        formattedId = formattedId.replace("Á", "A");
        formattedId = formattedId.replace("á", "a");
        formattedId = formattedId.replace("É", "E");
        formattedId = formattedId.replace("é", "e");
        formattedId = formattedId.replace("Í", "I");
        formattedId = formattedId.replace("í", "i");
        formattedId = formattedId.replace("Ó", "O");
        formattedId = formattedId.replace("ó", "o");
        formattedId = formattedId.replace("Ú", "U");
        formattedId = formattedId.replace("ú", "u");
        formattedId = formattedId.toLowerCase();
        return formattedId;
    }

    /**
     * Method to send report
     * @param mutableReport
     */
    public void sendReport(MutableLiveData<Report> mutableReport) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reportRef = db.collection("reports");
        reportRef.add(mutableReport.getValue()).addOnCompleteListener(task -> {
            mutableReport.getValue().setSent(true);
        });
    }

    /**
     * Method to get notification from firebase
     * @param user
     * @param creationDate
     */
    public void getNotifications(User user, long creationDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationRead = db.collection(DataNotificationNames.TABLE_FIREBASE_NAME);
        Query queryId = notificationRead.whereEqualTo(DataNotificationNames.COL_USERID, user.getId());
        queryId.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Notification notification = document.toObject(Notification.class);
                        notification.setCreationDateMiliseconds(Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()));
                        if (((notification.getId() != null) && (!notification.getId().equals("")))
                        && (Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()) > creationDate)) {
                            nut4HealtDao.insert(notification);
                        }
                    }
                } else {
                    Log.d(TAG, "Get notifications: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get notifications: " + "empty");
            }
        });
        Query queryUsername = notificationRead.whereEqualTo(DataNotificationNames.COL_USERID, user.getUsername());
        queryUsername.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Notification notification = document.toObject(Notification.class);
                        notification.setCreationDateMiliseconds(Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()));
                        if (((notification.getId() != null) && (!notification.getId().equals("")))
                                && (Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()) > creationDate)) {
                            nut4HealtDao.insert(notification);
                        }
                    }
                } else {
                    Log.d(TAG, "Get notifications: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get notifications: " + "empty");
            }
        });
        Query queryCountry = notificationRead.whereEqualTo(DataNotificationNames.COL_USERID, user.getCurrentCountry());
        queryCountry.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Notification notification = document.toObject(Notification.class);
                        notification.setCreationDateMiliseconds(Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()));
                        if (((notification.getId() != null) && (!notification.getId().equals("")))
                                && (Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()) > creationDate)) {
                            nut4HealtDao.insert(notification);
                        }
                    }
                } else {
                    Log.d(TAG, "Get notifications: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get notifications: " + "empty");
            }
        });
        Query queryState = notificationRead.whereEqualTo(DataNotificationNames.COL_USERID, user.getCurrentState());
        queryState.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Notification notification = document.toObject(Notification.class);
                        notification.setCreationDateMiliseconds(Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()));
                        if (((notification.getId() != null) && (!notification.getId().equals("")))
                                && (Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()) > creationDate)) {
                            nut4HealtDao.insert(notification);
                        }
                    }
                } else {
                    Log.d(TAG, "Get notifications: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get notifications: " + "empty");
            }
        });
        Query querCity = notificationRead.whereEqualTo(DataNotificationNames.COL_USERID, user.getCurrentCity());
        querCity.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Notification notification = document.toObject(Notification.class);
                        notification.setCreationDateMiliseconds(Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()));
                        if (((notification.getId() != null) && (!notification.getId().equals("")))
                                && (Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()) > creationDate)) {
                            nut4HealtDao.insert(notification);
                        }
                    }
                } else {
                    Log.d(TAG, "Get notifications: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get notifications: " + "empty");
            }
        });
        Query queryALL = notificationRead.whereEqualTo(DataNotificationNames.COL_USERID, "ALL");
        queryALL.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Notification notification = document.toObject(Notification.class);
                        notification.setCreationDateMiliseconds(Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()));
                        if (((notification.getId() != null) && (!notification.getId().equals("")))
                                && (Nut4HealthTimeUtil.convertCreationDateToTimeMilis(notification.getCreationDate()) > creationDate)) {
                            nut4HealtDao.insert(notification);
                        }
                    }
                } else {
                    Log.d(TAG, "Get notifications: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get notifications: " + "empty");
            }
        });
    }

    /**
     * Method to get notification sorted
     * @return
     */
    public LiveData<PagedList<Notification>> getSortedNotifications() {
        SimpleSQLiteQuery query = SortUtils.getNotifications();
        return new LivePagedListBuilder<>(nut4HealtDao.getNotifications(query), PAGE_SIZE).build();
    }

    /**
     * Method to mark notification as read
     * @param notificationId
     * @param userId
     */
    public void markNotificationRead(String notificationId, String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationRead = db.collection(DataNotificationNames.TABLE_FIREBASE_NAME);
        Query query = notificationRead.whereEqualTo(DataNotificationNames.COL_ID, notificationId);
        listenerMarkNotification = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Notification notification = document.toObject(Notification.class);
                        if (((notification.getId() != null) && (!notification.getId().equals("")))) {
                            if ((notification.getRead() == null) || (notification.getRead().isEmpty())) {
                                notification.setRead(userId);
                                queryDocumentSnapshots.getDocuments().get(0).getReference().set(notification);
                                nut4HealtDao.updateNotificationRead(notificationId, userId);
                            } else if (!notification.getRead().contains(userId)) {
                                notification.setRead(notification.getRead() + "," + userId);
                                queryDocumentSnapshots.getDocuments().get(0).getReference().set(notification);
                                nut4HealtDao.updateNotificationRead(notificationId, notification.getRead() + "," + userId);
                            }
                        }
                    }
                    listenerMarkNotification.remove();
                } else {
                    Log.d(TAG, "Get notifications: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get notifications: " + "empty");
            }
        });
    }

    /**
     * Mehtod to remove al near contracts
     */
    public void removeAllNearContracts() {
        mIoExecutor.submit(() -> {
            nut4HealtDao.deleteAllNearContracts();
        });
    }

    /**
     * Method to get near contracts from firebase
     * @param latitude
     * @param longitude
     * @param radius
     */
    public void retrieveNearContracts(double latitude, double longitude, double radius) {
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection(DataContractNames.TABLE_FIREBASE_NAME);
        GeoFirestore geoFirestore = new GeoFirestore(collectionRef);
        geoFirestore.queryAtLocation(new GeoPoint(latitude, longitude), radius).addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
                Near near = documentSnapshot.toObject(Near.class);
                mIoExecutor.submit(() -> nut4HealtDao.insert(near));
            }

            @Override
            public void onDocumentExited(DocumentSnapshot documentSnapshot) {
            }

            @Override
            public void onDocumentMoved(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {

            }

            @Override
            public void onDocumentChanged(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(Exception e) {

            }
        });
    }

    /**
     * Method to get contracts sorted
     * @param sort
     * @param name
     * @param surname
     * @param status
     * @param dateStart
     * @param dateEnd
     * @param percentageMin
     * @param percentageMax
     * @return
     */
    public LiveData<PagedList<Near>> getSortedNearContracts(String sort, String name, String surname,
                                                            String status, long dateStart, long dateEnd,
                                                            int percentageMin, int percentageMax) {
        SimpleSQLiteQuery query = SortUtils.getFilterNearContracts(sort, name, surname, status, dateStart, dateEnd,
                percentageMin, percentageMax);
        return new LivePagedListBuilder<>(nut4HealtDao.getNearContracts(query), PAGE_SIZE).build();
    }

    /**
     * Method to get a near contract
     * @param id
     * @return
     */
    public LiveData<Near> getNearContract(String id) {
        return nut4HealtDao.getNearContract(id);
    }

    /**
     * Method to get points from firebase
     */
    public void getPoints() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference pointRef = db.collection(DataPointNames.TABLE_FIREBASE_NAME);
        Query query = pointRef.whereEqualTo("active", true);
        query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    if (!queryDocumentSnapshots.getDocuments().get(0).getMetadata().isFromCache()) {
                        nut4HealtDao.deleteAllPoint();
                    }
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Point point = document.toObject(Point.class);
                        nut4HealtDao.insert(point);
                    }
                } else {
                    nut4HealtDao.deleteAllPoint();
                    Log.d(TAG, "Get point from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get points: " + "empty");
            }
        });
    }

    /**
     * Method to get a point
     * @param id
     * @return
     */
    public LiveData<Point> getPoint(String id) {
        return nut4HealtDao.getPoint(id);
    }

    /**
     * Method to get points from local bd
     * @return
     */
    public LiveData<List<Point>> getSortedPoints() {
        return nut4HealtDao.getPoints();
    }

    /**
     * Method to validate diagnosis
     * @param contractId
     */
    public void validateDiagnosis(String contractId, Double arm_circunference_medical, double height, double weight) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contractRef = db.collection(DataContractNames.TABLE_FIREBASE_NAME);
        Query query = contractRef.whereEqualTo(DataContractNames.COL_CONTRACT_ID, contractId).limit(1);
        listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    Contract contract = queryDocumentSnapshots.getDocuments().get(0).toObject(Contract.class);
                    contract.setArm_circumference_medical(arm_circunference_medical);
                    contract.setHeight(height);
                    contract.setWeight(weight);
                    contract.setStatus("FINISH");
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("status", "FINISH");
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("arm_circumference_medical", arm_circunference_medical);
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("height", height);
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("weight", weight);
                    nut4HealtDao.updateArmCircunferenceMedical(contractId, arm_circunference_medical);
                    nut4HealtDao.updateHeight(contractId, height);
                    nut4HealtDao.updateWeight(contractId, weight);
                    nut4HealtDao.updateContractStatus(contractId, "FINISH");
                    nut4HealtDao.updateMedicalDate(contractId, new Date().toString());
                    listenerQuery.remove();
                } else {
                    Log.d(TAG, "ValidateDiagnosis " + "error");
                }
            } catch (Exception error) {
                Log.d(TAG, "ValidateDiagnosis " + "error");
            }
        });
    }

    /**
     * Method to get malnutrition child values from firebase
     */
    public void getMalnutritionChildValues() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection(DataMalnutritionChildTableNames.TABLE_FIREBASE_NAME);
        query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    if (!queryDocumentSnapshots.getDocuments().get(0).getMetadata().isFromCache()) {
                        nut4HealtDao.deleteAllMalnutritionChildTable();
                    }
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        MalnutritionChildTable value = new MalnutritionChildTable(
                                document.get("id").toString(),
                                Double.parseDouble(document.get("cm").toString()),
                                Double.parseDouble(document.get("minusone").toString()),
                                Double.parseDouble(document.get("minusonefive").toString()),
                                Double.parseDouble(document.get("minusthree").toString()),
                                Double.parseDouble(document.get("minustwo").toString()),
                                Double.parseDouble(document.get("zero").toString())
                        );
                        nut4HealtDao.insert(value);
                    }
                } else {
                    nut4HealtDao.deleteAllMalnutritionChildTable();
                    Log.d(TAG, "Get malnutritionchildtable from firebase: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get malnutritionchildtable: " + "empty");
            }
        });
    }



    public LiveData<List<MalnutritionChildTable>> getMalNutritionChildTable() {
        try {
            return mIoExecutor.submit(() -> nut4HealtDao.getMalnutritionChildTable()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

   /* public LiveData<Double> checkDesnutritionByHeightAndWeight(double height, double weight) {
        SimpleSQLiteQuery query = SortUtils.getChildMalnutritionTable();
        try {
            mIoExecutor.submit(() -> {
                LiveData<PagedList<MalnutritionChildTable>> table = new LivePagedListBuilder<>(nut4HealtDao.getCurrentMalnutritionChildTable(query), PAGE_SIZE).build();
                if (table != null) {
                    for (MalnutritionChildTable value : table.getValue()) {
                        System.out.println("Aqui " + value);
                        if (Double.parseDouble(value.getCm()) >= (height - 0.1)) {
                            try {
                                if (weight >= Double.parseDouble(value.getMinusone())) {
                                    return 0.0;
                                } else if (weight >= Double.parseDouble(value.getMinustwo())) {
                                    return -1.0;
                                } else if (weight >= Double.parseDouble(value.getMinusthree())) {
                                    return -1.5;
                                } else {
                                    return -3.0;
                                }
                            } catch (Exception e) {
                                if (height == 100) {
                                    MalnutritionChildTable malNutritionChldTable = new MalnutritionChildTable(
                                            "X3fX5g2Fd9lpy0OVYkgA",
                                            "100",
                                            "14.2",
                                            "13.6",
                                            "12.1",
                                            "13.1",
                                            "15.4"
                                    );
                                    if (weight >= Double.parseDouble(malNutritionChldTable.getMinusone())) {
                                        return 0.0;
                                    } else if (weight >= Double.parseDouble(malNutritionChldTable.getMinustwo())) {
                                        return -1.0;
                                    } else if (weight >= Double.parseDouble(malNutritionChldTable.getMinusthree())) {
                                        return -1.5;
                                    } else {
                                        return -3.0;
                                    }
                                }
                            }
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /**
     * Method to get malnutrition child values from local bd
     * @return
     */
    /*public LiveData<List<MalnutritionChildTable>> getSortedMalnutritionChildValues() {
        return nut4HealtDao.getMalnutritionChildTable();
    }*/

}
