package org.sic4change.nut4health.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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

}
