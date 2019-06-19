package org.sic4change.nut4health.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.sic4change.nut4health.data.names.DataContractNames;

@Entity(tableName = DataContractNames.TABLE_NAME)
public class Contract {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DataContractNames.COL_CONTRACT_ID)
    private String id;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_PHOTO)
    private String photo;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_LATITUDE)
    private float latitude;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_LONGITUDE)
    private float longitude;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_SCREENER)
    private String screener;

    @ColumnInfo(name = DataContractNames.COL_MEDICAL)
    private String medical;

    @ColumnInfo(name = DataContractNames.COL_CHILD_NAME)
    private String childName;

    @ColumnInfo(name = DataContractNames.COL_CHILD_SURNAME)
    private String childSurname;

    @ColumnInfo(name = DataContractNames.COL_CHILD_ADDRESS)
    private String childAddress;

    @ColumnInfo(name = DataContractNames.COL_STATUS)
    private String status;

    @ColumnInfo(name = DataContractNames.COL_DIAGNOSIS)
    private String diagnosis;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_DATE)
    private String creationDate;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_PERCENTAGE)
    private int percentage;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_HASH)
    private String hash;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_MEDICAL_DATE)
    private String medicalDate;

    public Contract() {
        this("", "", 0.0f, 0.0f, "", "", "",
                "", "", Status.INIT.name(), "", "", "", 0, "");
    }

    @Ignore
    public Contract(@NonNull String id) {
        this(id, "", 0.0f, 0.0f, "", "", "",
                "", "", Status.INIT.name(), "", "", "", 0, "");
    }

    @Ignore
    public Contract(@NonNull String photo, float latitude, float longitude, @NonNull String screener) {
        this("", photo, latitude, longitude, screener, "", "", "",
                "", Status.INIT.name(), "", "", "", 0, "");
    }

    @Ignore
    public Contract(@NonNull String photo, float latitude, float longitude, @NonNull String screener,
                    String childName, String childSurname, String childAddress, String status, String creationDate,
                    String hash, int percentage) {
        this("", photo, latitude, longitude, screener, "", childName, childSurname,
                childAddress, status, "", creationDate, hash, percentage, "");
    }
    @Ignore
    public Contract(@NonNull String id, @NonNull String photo, float latitude, float longitude,
                    @NonNull String screener, String childName, String childSurname,
                    String childAddress, String status, String creationDate, String hash, int percentage) {
        this(id, photo, latitude, longitude, screener, "", childName, childSurname,
                childAddress, status, "", creationDate, hash, percentage, "");
    }

    public Contract(@NonNull String id, @NonNull String photo, float latitude, float longitude, @NonNull String screener,
                    String medical, String childName, String childSurname, String childAddress,
                    String status, String diagnosis, String creationDate, String hash, int percentage, String medicalDate) {
        this.id = id;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.screener = screener;
        this.medical = medical;
        this.childName = childName;
        this.childSurname = childSurname;
        this.childAddress = childAddress;
        this.status = status;
        this.diagnosis = diagnosis;
        this.creationDate = creationDate;
        this.hash = hash;
        this.percentage = percentage;
        this.medicalDate = medicalDate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(@NonNull String photo) {
        this.photo = photo;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public String getScreener() {
        return screener;
    }

    public void setScreener(@NonNull String screener) {
        this.screener = screener;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildSurname() {
        return childSurname;
    }

    public void setChildSurname(String childSurname) {
        this.childSurname = childSurname;
    }

    public String getChildAddress() {
        return childAddress;
    }

    public void setChildAddress(String childAddress) {
        this.childAddress = childAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @NonNull
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@NonNull String creationDate) {
        this.creationDate = creationDate;
    }

    @NonNull
    public String getHash() {
        return hash;
    }

    public void setHash(@NonNull String hash) {
        this.hash = hash;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public enum Status {
        INIT, DIAGNOSIS, NO_DIAGNOSIS, PAID, ALL
    }

    @NonNull
    public String getMedicalDate() {
        return medicalDate;
    }

    public void setMedicalDate(@NonNull String medicalDate) {
        this.medicalDate = medicalDate;
    }

}
