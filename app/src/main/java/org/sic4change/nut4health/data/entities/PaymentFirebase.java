package org.sic4change.nut4health.data.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataPaymentNames;
import org.sic4change.nut4health.utils.time.Nut4HealthTimeUtil;

@Entity(tableName = DataPaymentNames.TABLE_NAME)
public class PaymentFirebase {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_ID)
    private String id;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_DATE)
    private String creationDate;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_DATE_MILI)
    private String creationDateMiliseconds;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_SCREENER)
    private String screener;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_QUANTITY)
    private String quantity;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_TYPE)
    private String type;

    @NonNull
    @ColumnInfo(name = DataPaymentNames.COL_STATUS)
    private String status;

    @ColumnInfo(name = DataPaymentNames.COL_CONTRACTID)
    private String contractId;

    public PaymentFirebase() {
        this("", "", "0", "", "0", "", "", "");
    }

    @Ignore
    public PaymentFirebase(@NonNull String id) {
        this(id, "", "0","", "0", "", "", "");
    }

    @Ignore
    public PaymentFirebase(@NonNull String id, String creationDate, @NonNull String screener, String quantity, @NonNull String type, @NonNull String status) {
        this(id, creationDate, Nut4HealthTimeUtil.convertCreationDateToTimeMilis(creationDate) + "", screener, quantity, type, status, "");
    }

    public PaymentFirebase(@NonNull String id, String creationDate, String creationDateMiliseconds, @NonNull String screener, String quantity, @NonNull String type, @NonNull String status, String contractId) {
        this.id = id;
        this.creationDate = creationDate;
        this.creationDateMiliseconds = creationDateMiliseconds;
        this.screener = screener;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
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

    public String getCreationDateMiliseconds() {
        return creationDateMiliseconds;
    }

    public void setCreationDateMiliseconds(@NonNull String creationDateMiliseconds) {
        this.creationDateMiliseconds = creationDateMiliseconds;
    }

    @NonNull
    public String getScreener() {
        return screener;
    }

    public void setScreener(@NonNull String screener) {
        this.screener = screener;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public enum Status {
        Month, Diagnosis, Confirmation, ALL
    }

}
