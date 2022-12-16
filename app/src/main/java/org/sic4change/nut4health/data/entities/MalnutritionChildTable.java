package org.sic4change.nut4health.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataMalnutritionChildTableNames;

@Entity(tableName = DataMalnutritionChildTableNames.TABLE_NAME)
public class MalnutritionChildTable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_ID)
    private String id;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_CM)
    private double cm;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_MINUSONE)
    private double minusone;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_MINUSONEFIVE)
    private double minusonefive;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_MINUSTHREE)
    private double minusthree;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_MINUSTWO)
    private double minustwo;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_ZERO)
    private double zero;

    public MalnutritionChildTable() {
        this("", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    public MalnutritionChildTable(@NonNull String id, @NonNull double cm, @NonNull double minusone,
                                  @NonNull double minusonefive, @NonNull double minusthree,
                                  @NonNull double minustwo, @NonNull double zero) {
        this.id = id;
        this.cm = cm;
        this.minusone = minusone;
        this.minusonefive = minusonefive;
        this.minusthree = minusthree;
        this.minustwo = minustwo;
        this.zero = zero;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public double getCm() {
        return cm;
    }

    public void setCm(@NonNull double cm) {
        this.cm = cm;
    }

    @NonNull
    public double getMinusone() {
        return minusone;
    }

    public void setMinusone(@NonNull double minusone) {
        this.minusone = minusone;
    }

    @NonNull
    public double getMinusonefive() {
        return minusonefive;
    }

    public void setMinusonefive(@NonNull double minusonefive) {
        this.minusonefive = minusonefive;
    }

    @NonNull
    public double getMinusthree() {
        return minusthree;
    }

    public void setMinusthree(@NonNull double minusthree) {
        this.minusthree = minusthree;
    }

    @NonNull
    public double getMinustwo() {
        return minustwo;
    }

    public void setMinustwo(@NonNull double minustwo) {
        this.minustwo = minustwo;
    }

    @NonNull
    public double getZero() {
        return zero;
    }

    public void setZero(@NonNull double zero) {
        this.zero = zero;
    }
}
