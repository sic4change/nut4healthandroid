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
    @ColumnInfo(name = DataPointNames.COL_FULLNAME)
    private String fullName;

    @ColumnInfo(name = DataPointNames.COL_PHONECODE)
    private String phoneCode;

    public Point() {
        this("", "", "");
    }

    public Point(@NonNull String pointId, @NonNull String fullName, @NonNull String phoneCode) {
        this.pointId = pointId;
        this.fullName = fullName;
        this.phoneCode = phoneCode;
    }

    @NonNull
    public String getPointId() {
        return pointId;
    }

    public void setPointId(@NonNull String pointId) {
        this.pointId = pointId;
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

    @Override
    public String toString() {
        return this.fullName;
    }


}
