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
    private String cm;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_MINUSONE)
    private String minusone;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_MINUSONEFIVE)
    private String minusonefive;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_MINUSTHREE)
    private String minusthree;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_MINUSTWO)
    private String minustwo;

    @NonNull
    @ColumnInfo(name = DataMalnutritionChildTableNames.COL_MALNUTRITION_CHILD_TABLE_ZERO)
    private String zero;

    public MalnutritionChildTable() {
        this("", "", "", "", "", "", "");
    }

    public MalnutritionChildTable(@NonNull String id, @NonNull String cm, @NonNull String minusone,
                                  @NonNull String minusonefive, @NonNull String minusthree,
                                  @NonNull String minustwo, @NonNull String zero) {
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
    public String getCm() {
        return cm;
    }

    public void setCm(@NonNull String cm) {
        this.cm = cm;
    }

    @NonNull
    public String getMinusone() {
        return minusone;
    }

    public void setMinusone(@NonNull String minusone) {
        this.minusone = minusone;
    }

    @NonNull
    public String getMinusonefive() {
        return minusonefive;
    }

    public void setMinusonefive(@NonNull String minusonefive) {
        this.minusonefive = minusonefive;
    }

    @NonNull
    public String getMinusthree() {
        return minusthree;
    }

    public void setMinusthree(@NonNull String minusthree) {
        this.minusthree = minusthree;
    }

    @NonNull
    public String getMinustwo() {
        return minustwo;
    }

    public void setMinustwo(@NonNull String minustwo) {
        this.minustwo = minustwo;
    }

    @NonNull
    public String getZero() {
        return zero;
    }

    public void setZero(@NonNull String zero) {
        this.zero = zero;
    }
}
