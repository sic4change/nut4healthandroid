package org.sic4change.nut4health.data.entities;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataNearNames;
import org.sic4change.nut4health.utils.time.Nut4HealthTimeUtil;

@Entity(tableName = DataNearNames.TABLE_NAME)
public class Near {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DataNearNames.COL_CONTRACT_ID)
    private String id;

    @NonNull
    @ColumnInfo(name = DataNearNames.COL_PHOTO)
    private String photo;

    @NonNull
    @ColumnInfo(name = DataNearNames.COL_LATITUDE)
    private float latitude;

    @NonNull
    @ColumnInfo(name = DataNearNames.COL_LONGITUDE)
    private float longitude;

    @NonNull
    @ColumnInfo(name = DataNearNames.COL_SCREENER)
    private String screener;

    @ColumnInfo(name = DataNearNames.COL_MEDICAL)
    private String medical;

    @ColumnInfo(name = DataNearNames.COL_CHILD_NAME)
    private String childName;

    @ColumnInfo(name = DataNearNames.COL_CHILD_SURNAME)
    private String childSurname;

    @ColumnInfo(name = DataNearNames.COL_CHILD_ADDRESS)
    private String childAddress;

    @ColumnInfo(name = DataNearNames.COL_CHILD_FINGERPRINT)
    private String fingerprint;

    @ColumnInfo(name = DataNearNames.COL_STATUS)
    private String status;

    @ColumnInfo(name = DataNearNames.COL_DIAGNOSIS)
    private String diagnosis;

    @NonNull
    @ColumnInfo(name = DataNearNames.COL_DATE)
    private String creationDate;

    @ColumnInfo(name = DataNearNames.COL_DATE_MILI)
    private long creationDateMiliseconds;

    @NonNull
    @ColumnInfo(name = DataNearNames.COL_PERCENTAGE)
    private int percentage;

    @NonNull
    @ColumnInfo(name = DataNearNames.COL_MEDICAL_DATE)
    private String medicalDate;

    public Near() {
        this("", "", 0.0f, 0.0f, "", "", "",
                "", "", "", Status.INIT.name(), "", "",0, 0, "");
    }

    @Ignore
    public Near(@NonNull String id) {
        this(id, "", 0.0f, 0.0f, "", "", "",
                "", "", "", Status.INIT.name(), "", "",0,  0, "");
    }

    @Ignore
    public Near(@NonNull String photo, float latitude, float longitude, @NonNull String screener) {
        this("", photo, latitude, longitude, screener, "", "", "",
                "", "", Status.INIT.name(), "", "",0,  0, "");
    }

    @Ignore
    public Near(@NonNull String photo, float latitude, float longitude, @NonNull String screener,
                String childName, String childSurname, String childAddress, String fingerprint, String status, String creationDate,
                int percentage) {
        this("", photo, latitude, longitude, screener, "", childName, childSurname,
                childAddress, fingerprint, status, "", creationDate, Nut4HealthTimeUtil.convertCreationDateToTimeMilis(creationDate), percentage, "");
    }
    @Ignore
    public Near(@NonNull String id, @NonNull String photo, float latitude, float longitude,
                @NonNull String screener, String childName, String childSurname,
                String childAddress, String fingerprint, String status, String creationDate, int percentage) {
        this(id, photo, latitude, longitude, screener, "", childName, childSurname,
                childAddress, fingerprint, status, "", creationDate, Nut4HealthTimeUtil.convertCreationDateToTimeMilis(creationDate), percentage, "");
    }

    public Near(@NonNull String id, @NonNull String photo, float latitude, float longitude, @NonNull String screener,
                String medical, String childName, String childSurname, String childAddress, String fingerprint,
                String status, String diagnosis, String creationDate, long creationDateMiliseconds, int percentage, String medicalDate) {
        this.id = id;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.screener = screener;
        this.medical = medical;
        this.childName = childName;
        this.childSurname = childSurname;
        this.childAddress = childAddress;
        this.fingerprint = fingerprint;
        this.status = status;
        this.diagnosis = diagnosis;
        this.creationDate = creationDate;
        this.creationDateMiliseconds = creationDateMiliseconds;
        //this.hash = hash;
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

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
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

    public long getCreationDateMiliseconds() {
        return creationDateMiliseconds;
    }

    public void setCreationDateMiliseconds(long creationDateMiliseconds) {
        this.creationDateMiliseconds = creationDateMiliseconds;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @NonNull
    public String getMedicalDate() {
        return medicalDate;
    }

    public void setMedicalDate(@NonNull String medicalDate) {
        this.medicalDate = medicalDate;
    }

    public enum Status {
        INIT, DIAGNOSIS, NO_DIAGNOSIS, PAID, ALL
    }


}
