package org.sic4change.nut4health.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.sic4change.nut4health.data.names.DataPaymentNames;

@Entity(tableName = DataPaymentNames.TABLE_NAME)
public class Payment {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_ID)
    private String id;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_DATE)
    private String creationDate;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_SCREENER)
    private String screener;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_QUANTITY)
    private int quantity;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_TYPE)
    private String type;

    @ColumnInfo(name = DataPaymentNames.COL_CONTRACTID)
    private String contractId;

    public Payment() {
        this("", "", "", 0, "", "");
    }

    @Ignore
    public Payment(@NonNull String id) {
        this(id, "", "", 0, "", "");
    }

    @Ignore
    public Payment(@NonNull String id, String creationDate, @NonNull String screener, int quantity, @NonNull String type) {
        this(id, creationDate, screener, quantity, type, "");
    }

    public Payment(@NonNull String id, String creationDate, @NonNull String screener, int quantity, @NonNull String type, String contractId) {
        this.id = id;
        this.creationDate = creationDate;
        this.screener = screener;
        this.quantity = quantity;
        this.type = type;
        this.contractId = contractId;
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

    @NonNull
    public String getScreener() {
        return screener;
    }

    public void setScreener(@NonNull String screener) {
        this.screener = screener;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public enum Status {
        Bonus, Confirmation, ALL
    }

}
