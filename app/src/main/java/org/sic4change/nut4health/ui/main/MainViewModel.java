package org.sic4change.nut4health.ui.main;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.google.android.gms.maps.model.LatLng;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Near;
import org.sic4change.nut4health.data.entities.Notification;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.Report;
import org.sic4change.nut4health.data.entities.User;

public class MainViewModel extends ViewModel {


    private Context mContext;
    private final DataRepository mRepository;
    private final LiveData<User> mUser;
    private LiveData<PagedList<Contract>> mContracts;
    private LiveData<PagedList<Near>> mNear;
    private LiveData<PagedList<Ranking>> mRanking;
    private LiveData<PagedList<Payment>> mPayments;
    private LiveData<PagedList<Notification>> mNotifications;
    private MutableLiveData<Report> mReport;

    private String name = "";
    private String surname = "";
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

    private LatLng currentPosition = new LatLng(0.0, 0.0);
    public static final double RADIUS_NEAR = 30.0;

    public MainViewModel(Context context, DataRepository repository) {
        this.mContext = context;
        this.mRepository = repository;

        mUser = this.mRepository.getCurrentUser();

        mUser.observeForever( user -> {
            if (user != null) {
                this.mRepository.getRanking();
                this.mRepository.getPayments(user.getEmail());
            }
        });

        mContracts = this.mRepository.getSortedContracts("DATE", name, surname, status, dateStart, dateEnd, percentageMin, percentageMax);

        mNear = this.mRepository.getSortedNearContracts("DATE", name, surname, status, dateStart, dateEnd, percentageMin, percentageMax);
        mRanking = this.mRepository.getSortedRanking("POINTS", usernameRanking);
        mPayments = this.mRepository.getSortedPayments("DATE", statusPayment, dateStartPayment, dateEndPayment);
        mNotifications = this.mRepository.getSortedNotifications();
        isFiltered.setValue(false);
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

    public void updateUser(String email) {
        this.mRepository.updateUser(email);
    }

    public void updateCurrentLocation(String email, String country, String state, String city) {
        this.mRepository.updateCurrentLocation(email, country, state, city);
    }

    public void getContracts(String email, String role) {
        this.mRepository.getContracts(email, role);
    }

    public void getPoints() {
        this.mRepository.getPoints();
    }

    public void getSortedContracts(String sort, String name, String surname, String status, long dateStart, long dataEnd,
                                   int percentageMin, int percentageMax) {
        mContracts = this.mRepository.getSortedContracts(sort, name, surname, status, dateStart, dataEnd,
                percentageMin, percentageMax);
    }

    public void getSortedRanking(String sort, String username) {
        mRanking = this.mRepository.getSortedRanking(sort, username);
    }

    public LiveData<PagedList<Contract>> getContracts() {
        return mContracts;
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

    public void checkContract(String fingerprint) {
        mRepository.checkContract(fingerprint);
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

    public void subscribeToTopicCountry(String country) {
        this.mRepository.subscribeToNotificationTopic(country);
    }

    public void subscribeToTopicState(String state) {
        this.mRepository.subscribeToNotificationTopic(state);
    }

    public void subscribeToTopicCity(String city) {
        this.mRepository.subscribeToNotificationTopic(city);
    }

}
