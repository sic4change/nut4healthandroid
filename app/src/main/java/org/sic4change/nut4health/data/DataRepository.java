package org.sic4change.nut4health.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Notification;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.Report;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.data.names.DataContractNames;
import org.sic4change.nut4health.data.names.DataNotificationNames;
import org.sic4change.nut4health.data.names.DataPaymentNames;
import org.sic4change.nut4health.data.names.DataRankingNames;
import org.sic4change.nut4health.data.names.DataUserNames;
import org.sic4change.nut4health.utils.time.Nut4HealthTimeUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DataRepository {

    private String TAG = DataRepository.class.getName();

    private final static int PAGE_SIZE = 20;

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
                    queryDocumentSnapshots.getDocuments().get(0).getReference().set(user);
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
        mIoExecutor.submit(() -> nut4HealtDao.deleteAllContract());
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
     * @param role
     * @param email
     * @param latitude
     * @param longitude
     * @param photo
     * @param childName
     * @param childUsername
     * @param childAddress
     * @param percentage
     */
    public void createContract(String role, String email, float latitude, float longitude, Uri photo,
                               String childName, String childUsername, String childAddress, int percentage,
                               int userPoints) {
        String hash = "";
        try {
            hash = Files.hash(new File(photo.getPath()), Hashing.sha512()).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //hash = "55e0aaaa5c849aa153948d44ab45fbf7ae89eedce88dc73ced0a452bb8c2ad5d4aa08d3909e44e972ad04e2d9a7992528c51c8faa79a730ffd648957a00c4887";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contractRef = db.collection(DataContractNames.TABLE_FIREBASE_NAME);
        Query query = contractRef.whereEqualTo(DataContractNames.COL_HASH, hash).orderBy(DataContractNames.COL_DATE);
        String finalHash = hash;
        listenerQuery = query.addSnapshotListener(mIoExecutor, (queryDocumentSnapshots, e) -> {
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Contract contract = document.toObject(Contract.class);
                        //si el campo medical es distinto de null o vacio y la fecha actual
                        // es mayor que la fecha medical en 30 dias
                        //1) pones ese contrato a INIT
                        //2) pones el hash a su propio Id
                        //2) creas un nuevo contrato
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
                        try {
                            date = formatter.parse(contract.getCreationDate());
                        } catch (ParseException e3) {
                            e3.printStackTrace();
                        }
                        if (((contract.getMedical() != null) && (!contract.getMedical().isEmpty())) &&
                                date.getTime() != 0 && (new Date().getTime() > (30*24*3600*1000) + date.getTime())) {
                            contract.setStatus(Contract.Status.INIT.name());
                            contract.setHash(contract.getId());
                            document.getReference().set(contract);
                            mIoExecutor.execute(() -> nut4HealtDao.insert(contract));
                            String status;
                            if (percentage > 49) {
                                status = Contract.Status.DIAGNOSIS.name();
                            } else {
                                status = Contract.Status.NO_DIAGNOSIS.name();
                            }
                            Contract contractNew = new Contract(photo.toString(), latitude, longitude, "", childName, childUsername, childAddress,
                                    status, "", finalHash, percentage);
                            if (role.equals("Screener")) {
                                contractNew.setScreener(email);
                            } else {
                                contractNew.setMedical(email);
                                if (percentage > 49) {
                                    contractNew.setStatus(Contract.Status.PAID.name());
                                } else {
                                    contractNew.setStatus(Contract.Status.NO_DIAGNOSIS.name());
                                }
                            }
                            contractRef.add(contractNew);
                            continue;
                        } else {
                            if (contract.getStatus().equals(Contract.Status.DIAGNOSIS.name()) && !role.equals("Screener")) {
                                contract.setMedical(email);
                                contract.setStatus(Contract.Status.PAID.name());
                                document.getReference().set(contract);
                            } else if (contract.getStatus().equals(Contract.Status.NO_DIAGNOSIS.name()) && !role.equals("Screener")) {
                                contract.setMedical(email);
                                document.getReference().set(contract);
                            } else {
                                if (((contract.getMedical() == null) || (contract.getMedical().isEmpty())) && role.equals("Screener")
                                        && (!email.equals(contract.getScreener()))) {
                                    String status;
                                    if (percentage > 49) {
                                        status = Contract.Status.DIAGNOSIS.name();
                                    } else {
                                        status = Contract.Status.NO_DIAGNOSIS.name();
                                    }
                                    Contract contractScreener = new Contract(photo.toString(), latitude, longitude, "", childName, childUsername, childAddress,
                                            status, "", finalHash, percentage);
                                    contractScreener.setScreener(email);
                                    contractRef.add(contractScreener);
                                } else {
                                    Toast.makeText(mContext, R.string.error_create_contract_server, Toast.LENGTH_LONG).show();
                                }
                            }
                            break;
                        }
                    }
                } else {
                    String status;
                    if (percentage > 49) {
                        status = Contract.Status.DIAGNOSIS.name();
                    } else {
                        status = Contract.Status.NO_DIAGNOSIS.name();
                    }
                    Contract contract = new Contract(photo.toString(), latitude, longitude, "", childName, childUsername, childAddress,
                            status, "", finalHash, percentage);
                    if (role.equals("Screener")) {
                        contract.setScreener(email);
                    } else {
                        contract.setMedical(email);
                        if (percentage > 49) {
                            contract.setStatus(Contract.Status.PAID.name());
                        } else {
                            contract.setStatus(Contract.Status.NO_DIAGNOSIS.name());
                        }
                    }
                    contractRef.add(contract);
                }
                listenerQuery.remove();
            } catch (Exception error) {
                Log.d(TAG, "Get contract: " + "error");
            }
        });

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
        if (role.equals("Screener")) {
            query = contractRef.whereEqualTo(DataContractNames.COL_SCREENER, email);
        } else {
            query = contractRef.whereEqualTo(DataContractNames.COL_MEDICAL, email);
        }
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
                    Log.d(TAG, "Get contracts: " + "empty");
                }
            } catch (Exception error) {
                Log.d(TAG, "Get contracts: " + "empty");
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
    public LiveData<PagedList<Contract>> getSortedContracts(String sort, String name, String surname,
                                                            String status, long dateStart, long dateEnd,
                                                            int percentageMin, int percentageMax) {
        SimpleSQLiteQuery query = SortUtils.getFilterContracts(sort, name, surname, status, dateStart, dateEnd,
                percentageMin, percentageMax);
        return new LivePagedListBuilder<>(nut4HealtDao.getUserContracts(query), PAGE_SIZE).build();
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
                        if (!user.getUsername().contains("anonymous") && user.getRole().equals("Screener")) {
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
            try {
                if ((queryDocumentSnapshots != null) && (queryDocumentSnapshots.getDocuments() != null)
                        && (queryDocumentSnapshots.getDocuments().size() > 0)) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Payment payment = document.toObject(Payment.class);
                        if (((payment.getId() != null) && (!payment.getId().equals("")))) {
                            nut4HealtDao.insert(payment);
                        }
                    }
                } else {
                    Log.d(TAG, "Get payments: " + "empty");
                }
            } catch (Exception error) {
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
            String formattedId = id.replace(" ", "");
            formattedId = formattedId.replace("ñ", "n");
            formattedId = formattedId.replace("Ñ", "N");
            formattedId = formattedId.toLowerCase();
            String finalFormattedId = formattedId;
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
                String formattedId = id.replace(" ", "");
                formattedId = formattedId.replace("ñ", "n");
                formattedId = formattedId.replace("Ñ", "N");
                formattedId = formattedId.toLowerCase();
                String finalFormattedId = formattedId;
                FirebaseMessaging.getInstance().unsubscribeFromTopic(finalFormattedId)
                        .addOnCompleteListener(task -> Log.d(TAG, "Unsubscribe to notification topic: " + finalFormattedId));
            } catch (Exception e) {
                Log.d(TAG, "Error subscribing to topic");
            }
        }


    }

    /**
     * Method to send report
     * @param mutableReport
     */
    public void sendReport(MutableLiveData<Report> mutableReport) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contractRef = db.collection("reports");
        contractRef.add(mutableReport.getValue()).addOnCompleteListener(task -> {
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

}
