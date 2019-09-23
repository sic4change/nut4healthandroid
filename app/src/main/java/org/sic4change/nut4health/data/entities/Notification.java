package org.sic4change.nut4health.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataNotificationNames;


@Entity(tableName = DataNotificationNames.TABLE_NAME)
public class Notification {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DataNotificationNames.COL_ID)
    private String id;

    @NonNull
    @ColumnInfo(name = DataNotificationNames.COL_DATE)
    private String creationDate;

    @ColumnInfo(name = DataNotificationNames.COL_DATE_MILI)
    private long creationDateMiliseconds;

    @NonNull
    @ColumnInfo(name = DataNotificationNames.COL_TEXT)
    private String text;

    @NonNull
    @ColumnInfo(name = DataNotificationNames.COL_USERID)
    private String userId;

    @ColumnInfo(name = DataNotificationNames.COL_READ)
    private String read;

    public Notification() {
        this("", "", 0, "", "", "");
    }

    public Notification(@NonNull String id, String creationDate, long creationDateMiliseconds, @NonNull String text, @NonNull String userId, String read) {
        this.id = id;
        this.creationDate = creationDate;
        this.creationDateMiliseconds = creationDateMiliseconds;
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

    @NonNull
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@NonNull String creationDate) {
        this.creationDate = creationDate;
    }

    public long getCreationDateMiliseconds() {
        return creationDateMiliseconds;
    }

    public void setCreationDateMiliseconds(long creationDateMiliseconds) {
        this.creationDateMiliseconds = creationDateMiliseconds;
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
