package org.sic4change.nut4health.ui.main;


import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;


import com.google.android.gms.maps.model.LatLng;

import org.reactivestreams.Publisher;
import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Configuration;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.MalnutritionChildTable;
import org.sic4change.nut4health.data.entities.Near;
import org.sic4change.nut4health.data.entities.Notification;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.Report;
import org.sic4change.nut4health.data.entities.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.reactive.ReactiveFlowKt;

public class MainViewModel extends ViewModel {


    private Context mContext;

    private Activity mActivity;
    private final DataRepository mRepository;
    private final LiveData<User> mUser;
    private final LiveData<Configuration> mConfiguration;
    public LiveData<PagingData<Contract>> contracts;
    private LiveData<PagedList<Near>> mNear;
    private LiveData<PagedList<Ranking>> mRanking;
    private LiveData<PagedList<Payment>> mPayments;
    private LiveData<PagedList<Notification>> mNotifications;
    private MutableLiveData<Report> mReport;

    private String name = "";
    private String surname = "";
    private String tutorName = "";
    private String tutorStatus = "";
    private String status = Contract.Status.EMPTY.name();
    private long dateStart = 0;
    private long dateEnd = 0;
    private int percentageMin = 0;
    private int percentageMax = 100;
    private final MutableLiveData<Boolean> isFiltered = new MutableLiveData<>();

    private String usernameRanking = "";

    private String statusPayment = Payment.Status.ALL.name();
    private long dateStartPayment = 0;
    private long dateEndPayment = 0;

    private User user;

    private LatLng currentPosition = new LatLng(0.0, 0.0);
    public static final double RADIUS_NEAR = 30.0;

    public MainViewModel(Context context, DataRepository repository) {
        this.mContext = context;
        this.mRepository = repository;

        Flow<PagingData<Contract>> flow = new Pager<>(
                new PagingConfig(20),
                () -> mRepository.getSortedContracts(
                        "DATE", "", name, surname, tutorName, tutorStatus,
                        status, dateStart, dateEnd, percentageMin, percentageMax
                )
        ).getFlow();

        Publisher<PagingData<Contract>> publisher = ReactiveFlowKt.asPublisher(flow, EmptyCoroutineContext.INSTANCE);

        contracts = LiveDataReactiveStreams.fromPublisher(publisher);

        mUser = this.mRepository.getCurrentUser();
        mConfiguration = this.mRepository.getCurrentConfiguration();
    }

    public void initContracts(String email, String role) {
        mRepository.getContracts(email, role);

        Flow<PagingData<Contract>> flow = new Pager<>(
                new PagingConfig(20),
                () -> mRepository.getSortedContracts(
                        "DATE", "", name, surname, tutorName, tutorStatus,
                        status, dateStart, dateEnd, percentageMin, percentageMax
                )
        ).getFlow();

        Publisher<PagingData<Contract>> publisher = ReactiveFlowKt.asPublisher(flow, EmptyCoroutineContext.INSTANCE);
        contracts = LiveDataReactiveStreams.fromPublisher(publisher);

        mNear = mRepository.getSortedNearContracts(
                "DATE", "", name, surname, tutorName, tutorStatus,
                status, dateStart, dateEnd, percentageMin, percentageMax
        );
    }


    public void initRanking() {
        this.mRepository.getRanking();
        mRanking = this.mRepository.getSortedRanking("POINTS", usernameRanking);
    }

    public void initPayments(String email) {
        this.mRepository.getPayments(email);
        mPayments = this.mRepository.getSortedPayments("DATE", statusPayment, dateStartPayment, dateEndPayment);
    }

    public LiveData<PagingData<Contract>> getContracts() {
        return contracts;
    }

    public LiveData<List<Contract>> getAllContractsForExport() {
        return mRepository.getAllContractsForExport();
    }


    public void init(Activity activity) {
        mUser.observe((LifecycleOwner) activity, user -> {
            if (user != null) {
                setUser(user);
                initRanking();
                initPayments(user.getEmail());
                this.mRepository.getMalnutritionChildValues();
            }
        });


        mNotifications = this.mRepository.getSortedNotifications();
        isFiltered.setValue(false);
    }

    public LiveData<List<Contract>> getAllContracts() {
        return mRepository.getAllContracts();
    }

    public LatLng getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(LatLng currentPosition) {
        this.currentPosition = currentPosition;
    }

    public LiveData<User> getCurrentUser() {
        return mUser;
    }

    public LiveData<Configuration> getCurrentConfiguration() {
        return mConfiguration;
    }

    public void updateUser(String email) {
        this.mRepository.updateUser(email);
    }

    public void updateCurrentLocation(String email, String country, String state, String city) {
        this.mRepository.updateCurrentLocation(email, country, state, city);
    }

    public void getPoints() {
        this.mRepository.getPoints();
    }

    /*public LiveData<PagingData<Contract>> getSortedContracts(String sort, String contractType, String name, String surname,
                                                             String tutorName, String tutorStatus,
                                                             String status, long dateStart, long dateEnd,
                                                             int percentageMin, int percentageMax) {

        Flow<PagingData<Contract>> flow = new Pager<>(
                new PagingConfig(20),
                () -> mRepository.getSortedContracts(
                        sort, contractType, name, surname, tutorName, tutorStatus,
                        status, dateStart, dateEnd, percentageMin, percentageMax
                )
        ).getFlow();

        Publisher<PagingData<Contract>> publisher = ReactiveFlowKt.asPublisher(flow, EmptyCoroutineContext.INSTANCE);
        return LiveDataReactiveStreams.fromPublisher(publisher);
    }*/

    public void getSortedContracts(String sort, String contractType, String name, String surname,
                                      String tutorName, String tutorStatus, String status,
                                      long dateStart, long dateEnd, int percentageMin, int percentageMax) {

        Flow<PagingData<Contract>> flow = new Pager<>(
                new PagingConfig(20),
                () -> mRepository.getSortedContracts(
                        sort, contractType, name, surname, tutorName, tutorStatus,
                        status, dateStart, dateEnd, percentageMin, percentageMax
                )
        ).getFlow();

        Publisher<PagingData<Contract>> publisher = ReactiveFlowKt.asPublisher(flow, EmptyCoroutineContext.INSTANCE);
        contracts = LiveDataReactiveStreams.fromPublisher(publisher);
    }


    public void getSortedRanking(String sort, String username) {
        mRanking = this.mRepository.getSortedRanking(sort, username);
    }


    public LiveData<PagedList<Near>> getNearContracts() {
        return mNear;
    }

    public void getSortedPayments(String sort, String status, long dateStart, long dataEnd) {
        mPayments = this.mRepository.getSortedPayments(sort, status, dateStart, dataEnd);
    }

    public void getPayments(String email) {
        this.mRepository.getPayments(email);
    }

    public LiveData<PagedList<Payment>> getPayments() {
        return mPayments;
    }

    public void getRankingUser() {
        this.mRepository.getRanking();
    }

    public LiveData<PagedList<Ranking>> getRanking() {
        return mRanking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public void setTutorStatus(String tutorStatus) {
        this.tutorStatus = tutorStatus;
    }

    public String getTutorStatus() {
        return tutorStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getPercentageMin() {
        return percentageMin;
    }

    public void setPercentageMin(int percentageMin) {
        this.percentageMin = percentageMin;
    }

    public int getPercentageMax() {
        return percentageMax;
    }

    public void setPercentageMax(int percentageMax) {
        this.percentageMax = percentageMax;
    }

    public MutableLiveData<Boolean> getIsFiltered() {
        return isFiltered;
    }

    public void setIsFiltered(Boolean filtered) {
        isFiltered.setValue(filtered);
    }

    public String getUsernameRanking() {
        return usernameRanking;
    }

    public void setUsernameRanking(String usernameRanking) {
        this.usernameRanking = usernameRanking;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }

    public long getDateStartPayment() {
        return dateStartPayment;
    }

    public void setDateStartPayment(long dateStartPayment) {
        this.dateStartPayment = dateStartPayment;
    }

    public long getDateEndPayment() {
        return dateEndPayment;
    }

    public void setDateEndPayment(long dateEndPayment) {
        this.dateEndPayment = dateEndPayment;
    }

    public MutableLiveData<Report> getReport() {
        return mReport;
    }

    public void sendReport(Report report) {
        report.setEmail(mUser.getValue().getEmail());
        mReport = new MutableLiveData<Report>();
        mReport.setValue(report);
        mRepository.sendReport(mReport);
    }

    public void getSortedNotifications() {
        mNotifications = this.mRepository.getSortedNotifications();
    }

    public void getNotifications(User user, long creationDate) {
        this.mRepository.getNotifications(user, creationDate);
    }

    public LiveData<PagedList<Notification>> getNotifications() {
        return mNotifications;
    }

    public void markAsReadNotification(String id) {
        this.mRepository.markNotificationRead(id, mUser.getValue().getId());
    }

    public int getNotificationsNoRead() {
        int notificatonsNoRead = 0;
        try {
            if ((mNotifications != null) && (mNotifications.getValue() != null)) {
                for (Notification notification : mNotifications.getValue()) {
                    if (!notification.getRead().contains(mUser.getValue().getId())) {
                        notificatonsNoRead++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("notifications empty");
        }
        return notificatonsNoRead;
    }

    public void removeAllNearContracts() {
        this.mRepository.removeAllNearContracts();
    }

    public void retrieveNearContracts(double latitude, double longitude) {
        this.mRepository.retrieveNearContracts(latitude, longitude, RADIUS_NEAR);
    }

    public void getSortedNearContracts(String sort, String contractType, String name, String surname,
                                       String tutorName, String tutorStatus,
                                       String status, long dateStart, long dataEnd,
                                   int percentageMin, int percentageMax) {
        mNear = this.mRepository.getSortedNearContracts(sort, contractType, name, surname,
                tutorName, tutorStatus,
                status, dateStart, dataEnd,
                percentageMin, percentageMax);
    }

    public void subscribeToTopicCountry(String country) {
        this.mRepository.subscribeToNotificationTopic(country);
    }

    public void subscribeToTopicState(String state) {
        this.mRepository.subscribeToNotificationTopic(state);
    }

    public void subscribeToTopicCity(String city) {
        this.mRepository.subscribeToNotificationTopic(city);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
