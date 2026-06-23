package org.sic4change.nut4health.data.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataPointNames;

@Entity(tableName = DataPointNames.TABLE_NAME)
public class Point {


    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DataPointNames.COL_POINTID)
    private String pointId;

    @NonNull
    @ColumnInfo(name = DataPointNames.COL_ACTIVE)
    private boolean active;

    @NonNull
    @ColumnInfo(name = DataPointNames.COL_FULLNAME)
    private String fullName;

    @ColumnInfo(name = DataPointNames.COL_PHONECODE)
    private String phoneCode;

    @ColumnInfo(name = DataPointNames.COL_COUNTRY)
    private String country;

    public Point() {
        this("", false, "", "", "");
    }

    public Point(@NonNull String pointId, @NonNull boolean active, @NonNull String fullName, @NonNull String phoneCode) {
        this(pointId, active, fullName, phoneCode, "");
    }

    public Point(@NonNull String pointId, @NonNull boolean active, @NonNull String fullName, @NonNull String phoneCode, String country) {
        this.pointId = pointId;
        this.active = active;
        this.fullName = fullName;
        this.phoneCode = phoneCode;
        this.country = country;
    }

    @NonNull
    public String getPointId() {
        return pointId;
    }

    public void setPointId(@NonNull String pointId) {
        this.pointId = pointId;
    }

    @NonNull
    public boolean getActive() {
        return active;
    }

    public void setActive(@NonNull boolean active) {
        this.active = active;
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    @NonNull
    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(@NonNull String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return this.fullName;
    }


}
