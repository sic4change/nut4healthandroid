package org.sic4change.nut4health.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import org.sic4change.nut4health.data.names.DataNotificationNames;

import java.util.Date;

@Entity(tableName = DataNotificationNames.TABLE_NAME)
public class Notification {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DataNotificationNames.COL_ID)
    private String id;

    @NonNull
    @ColumnInfo(name = DataNotificationNames.COL_DATE)
    private long date;

    @NonNull
    @ColumnInfo(name = DataNotificationNames.COL_TEXT)
    private String text;

    @NonNull
    @ColumnInfo(name = DataNotificationNames.COL_USERID)
    private String userId;

    @ColumnInfo(name = DataNotificationNames.COL_READ)
    private String read;

    public Notification() {
        this("", new Date().getTime(), "", "", "");
    }

    public Notification(@NonNull String id, long date, @NonNull String text, @NonNull String userId, String read) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.userId = userId;
        this.read = read;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

}
