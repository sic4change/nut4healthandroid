package org.sic4change.nut4health.data.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataConfigurationNames;
import org.sic4change.nut4health.data.names.DataPaymentNames;
import org.sic4change.nut4health.utils.time.Nut4HealthTimeUtil;

@Entity(tableName = DataConfigurationNames.TABLE_NAME)
public class Configuration {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DataConfigurationNames.COL_ID)
    private String id;

    @NonNull
    @ColumnInfo(name = DataConfigurationNames.COL_MONEY)
    private String money;

    public Configuration() {
        this("", "");
    }

    @Ignore
    public Configuration(@NonNull String id) {
        this(id, "");
    }

    public Configuration(@NonNull String id, @NonNull String money) {
        this.id = id;
        this.money = money;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getMoney() {
        return money;
    }

    public void setMoney(@NonNull String money) {
        this.money = money;
    }


}
