package org.sic4change.nut4health.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.User;

@Database(entities = {User.class, Contract.class, Ranking.class}, version = 2, exportSchema = false)
public abstract  class Nut4HealthDatabase extends RoomDatabase {

    private static volatile Nut4HealthDatabase sInstance = null;

    private static Context mContext;

    public abstract Nut4HealtDao nut4HealtDao();

    /**
     * Returns an instance of Room Database.
     *
     * @param context application context
     * @return The singleton NUT4HealthDatabase
     */
    @NonNull
    public static synchronized Nut4HealthDatabase getInstance(final Context context) {
        mContext = context;
        if (sInstance == null) {
            synchronized (Nut4HealthDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            Nut4HealthDatabase.class, "nut4health_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return sInstance;
    }

}
