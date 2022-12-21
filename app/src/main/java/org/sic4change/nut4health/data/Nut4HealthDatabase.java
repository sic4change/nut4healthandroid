package org.sic4change.nut4health.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.type.Money;

import org.sic4change.nut4health.data.entities.Configuration;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.MalnutritionChildTable;
import org.sic4change.nut4health.data.entities.Near;
import org.sic4change.nut4health.data.entities.Notification;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.data.entities.Point;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.User;

@Database(entities = {User.class, Contract.class, Near.class, Ranking.class, Payment.class,
        Notification.class, Point.class, Configuration.class, MalnutritionChildTable.class},
        version = 39, exportSchema = false)
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
