package org.sic4change.nut4health.data;


import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Near;
import org.sic4change.nut4health.data.entities.Notification;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.data.entities.Point;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.User;

import java.util.List;

@Dao
public interface Nut4HealtDao {

    /**
     * Returns a current user.
     *
     */
    @Query("SELECT * FROM user WHERE email != 'empty@emtpy.com' LIMIT 1")
    LiveData<User> getCurrentUser();

    /**
     * Returns a user (maybe user empty)
     * @return
     */
    @Query("SELECT * FROM user LIMIT 1")
    LiveData<User> getUser();

    /**
     * Insert a user
     * @param user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    /**
     * Delete all user
     */
    @Query("DELETE FROM user")
    void deleteAllUser();

    /**
     * Delete all user
     */
    @Query("DELETE FROM user WHERE email == 'empty@emtpy.com'")
    void deleteEmptyUser();

    /**
     * Update photo user
     */
    @Query("UPDATE user SET photo =:photo WHERE email=:email ")
    void updatePhotoUser(String photo, String email);

    /**
     * Update name user
     */
    @Query("UPDATE user SET name =:name WHERE email=:email ")
    void updateNameUser(String name, String email);

    /**
     * Update surname user
     */
    @Query("UPDATE user SET surname =:surname WHERE email=:email ")
    void updateSurnameUser(String surname, String email);

    /**
     * Update country user
     */
    @Query("UPDATE user SET country =:country WHERE email=:email ")
    void updateCountryUser(String country, String email);

    /**
     * Update country code user
     */
    @Query("UPDATE user SET countryCode =:countryCode WHERE email=:email ")
    void updateCountryCodeUser(String countryCode, String email);

    /**
     * Update points user
     */
    @Query("UPDATE user SET points =:points WHERE email=:email ")
    void updatePointsUser(int points, String email);

    /**
     * Update current country user
     */
    @Query("UPDATE user SET currentCountry =:currentCountry WHERE email=:email ")
    void updateCurrentCountryUser(String currentCountry, String email);

    /**
     * Update current state user
     */
    @Query("UPDATE user SET currentState =:currentState WHERE email=:email ")
    void updateCurrentStateUser(String currentState, String email);

    /**
     * Update current city user
     */
    @Query("UPDATE user SET currentCity =:currentCity WHERE email=:email ")
    void updateCurrentCityUser(String currentCity, String email);

    /**
     * Get all contract for Paging
     * @param query
     * @return
     */
    @RawQuery(observedEntities = Contract.class)
    DataSource.Factory<Integer, Contract> getUserContracts(SupportSQLiteQuery query);

    /**
     * Get a contract based on the contract id
     * @param id
     * @return
     */
    @Query("SELECT * FROM contract WHERE contractId =:id LIMIT 1")
    LiveData<Contract> getContract(String id);

    /**
     * Update contract status
     * @param id
     * @param status
     */
    @Query("UPDATE contract SET status =:status WHERE contractId=:id ")
    void updateContractStatus(String id, String status);

    /**
     * Update contract medicalDate
     * @param id
     * @param medicalDate
     */
    @Query("UPDATE contract SET medical_date =:medicalDate WHERE contractId=:id ")
    void updateMedicalDate(String id, String medicalDate);

    /**
     * Update contract photo
     * @param id
     * @param photo
     */
    @Query("UPDATE contract SET photo =:photo WHERE contractId=:id ")
    void updateContractPhoto(String id, String photo);

    /**
     * Update contract medical
     * @param id
     * @param medical
     */
    @Query("UPDATE contract SET medical =:medical WHERE contractId=:id ")
    void updateContractMedical(String id, String medical);

    /**
     * Update contract diagnosis
     * @param id
     * @param diagnosis
     */
    @Query("UPDATE contract SET diagnosis =:diagnosis WHERE contractId=:id ")
    void updateContractDiagnosis(String id, String diagnosis);

    /**
     * Insert a contract
     * @param contract
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contract... contract);

    /**
     * Delete a contract
     * @param contract
     */
    @Delete
    void delete(Contract contract);

    /**
     * Delete contract
     * @param id
     */
    @Query("DELETE FROM contract WHERE contractId =:id AND date =:date")
    void deleteContract(String id, long date);

    /**
     * Delete all contracts
     */
    @Query("DELETE FROM contract")
    void deleteAllContract();

    /**
     * Get a contract based on the contract id
     * @param id
     * @return
     */
    @Query("SELECT * FROM contract WHERE contractId =:id AND status =:status LIMIT 1")
    LiveData<Contract> getContract(String id, String status);

    /**
     * Get ranking for Paging
     * @param query
     * @return
     */
    @RawQuery(observedEntities = Ranking.class)
    DataSource.Factory<Integer, Ranking> getRanking(SupportSQLiteQuery query);

    /**
     * Insert a ranking
     * @param rankings
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ranking... rankings);

    /**
     * Get a user ranking based on the username
     * @param username
     * @return
     */
    @Query("SELECT * FROM ranking WHERE username =:username LIMIT 1")
    LiveData<Ranking> getUserRanking(String username);

    /**
     * Delete all ranking
     */
    @Query("DELETE FROM ranking")
    void deleteAllRanking();

    /**
     * Get payments for Paging
     * @param query
     * @return
     */
    @RawQuery(observedEntities = Payment.class)
    DataSource.Factory<Integer, Payment> getPayments(SupportSQLiteQuery query);

    /**
     * Insert a payment
     * @param payments
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Payment... payments);

    /**
     * Delete all payments
     */
    @Query("DELETE FROM payment")
    void deleteAllPayment();

    /**
     * Get notifications for Paging
     * @param query
     * @return
     */
    @RawQuery(observedEntities = Notification.class)
    DataSource.Factory<Integer, Notification> getNotifications(SupportSQLiteQuery query);

    /**
     * Insert a notification
     * @param notifications
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notification... notifications);

    /**
     * Delete all notificationss
     */
    @Query("DELETE FROM notification")
    void deleteAllNotification();

    /**
     * Update notification read
     * @param id
     * @param read
     */
    @Query("UPDATE notification SET read =:read WHERE id=:id ")
    void updateNotificationRead(String id, String read);

    /**
     * Get all near contract for Paging
     * @param query
     * @return
     */
    @RawQuery(observedEntities = Near.class)
    DataSource.Factory<Integer, Near> getNearContracts(SupportSQLiteQuery query);

    /**
     * Get a near contract based on the contract id
     * @param id
     * @return
     */
    @Query("SELECT * FROM near WHERE contractId =:id LIMIT 1")
    LiveData<Near> getNearContract(String id);


    /**
     * Get all near contract
     * @return
     */
    @Query("SELECT * FROM near")
    List<Near> getNearContracts();

    /**
     * Delete all near contracts
     */
    @Query("DELETE FROM near")
    void deleteAllNearContracts();

    /**
     * Insert a near contract
     * @param near
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Near... near);

    /**
     * Delete contract
     * @param id
     */
    @Query("DELETE FROM near WHERE contractId =:id")
    void deleteNear(String id);

    /**
     * Returns all points
     * @return
     */
    @Query("SELECT * FROM point")
    LiveData<List<Point>> getPoints();

    /**
     * Get a point based on the point id
     * @param id
     * @return
     */
    @Query("SELECT * FROM point WHERE pointId =:id LIMIT 1")
    LiveData<Point> getPoint(String id);

    /**
     * Insert a point
     * @param point
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Point... point);
}
