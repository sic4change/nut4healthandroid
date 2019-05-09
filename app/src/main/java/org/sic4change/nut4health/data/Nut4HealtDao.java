package org.sic4change.nut4health.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.User;

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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

}
