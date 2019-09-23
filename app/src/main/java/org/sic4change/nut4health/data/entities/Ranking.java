package org.sic4change.nut4health.data.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataRankingNames;
import org.sic4change.nut4health.data.names.DataUserNames;

@Entity(tableName = DataRankingNames.TABLE_NAME)
public class Ranking {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DataUserNames.COL_USERNAME)
    private String username;

    @ColumnInfo(name = DataUserNames.COL_PHOTO)
    private String photo;

    @NonNull
    @ColumnInfo(name = DataUserNames.COL_POINTS)
    private int points;

    @NonNull
    @ColumnInfo(name = DataUserNames.COL_POSITION)
    private int position;

    public Ranking(@NonNull String username, String photo, int points, int position) {
        this.username = username;
        this.photo = photo;
        this.points = points;
        this.position = position;
    }

    @Ignore
    public Ranking(@NonNull String username, String photo, int points) {
        this(username, photo, points, 0);
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
