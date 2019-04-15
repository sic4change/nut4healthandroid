package org.sic4change.nut4health.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
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
    private int latitude;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_LONGITUDE)
    private int longitude;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_SCREENER)
    private User screener;

    @ColumnInfo(name = DataContractNames.COL_MEDICAL)
    private User medical;

    @ColumnInfo(name = DataContractNames.COL_CHILD_NAME)
    private String childName;

    @ColumnInfo(name = DataContractNames.COL_CHILD_SURNAME)
    private String childSurname;

    @ColumnInfo(name = DataContractNames.COL_CHILD_COUNTRY)
    private String childCountry;

    @ColumnInfo(name = DataContractNames.COL_CHILD_COUNTRYCODE)
    private String childCountryCode;

    @ColumnInfo(name = DataContractNames.COL_CHILD_PROVINCE)
    private String childProvince;

    @ColumnInfo(name = DataContractNames.COL_CHILD_MUNICIPALITY)
    private String childMunicipality;

    @ColumnInfo(name = DataContractNames.COL_CHILD_CITY)
    private String childCity;

    @ColumnInfo(name = DataContractNames.COL_CHILD_ZONE)
    private String childZone;

    @ColumnInfo(name = DataContractNames.COL_CHILD_ADDRESS)
    private String childAddress;

    @ColumnInfo(name = DataContractNames.COL_STATUS)
    private String status;

    @ColumnInfo(name = DataContractNames.COL_DIAGNOSIS)
    private String diagnosis;

    @ColumnInfo(name = DataContractNames.COL_DATE)
    private long date;

    public Contract(@NonNull String id) {
        this.id = id;
    }

    public Contract(@NonNull String photo, int latitude, int longitude, @NonNull User screener) {
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.screener = screener;
    }

    public Contract(@NonNull String id, @NonNull String photo, int latitude, int longitude,
                    @NonNull User screener, String childName, String childSurname, String childCountry,
                    String childCountryCode, String childProvince, String childMunicipality, String childCity,
                    String childZone, String childAddress, String status, long date) {
        this.id = id;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.screener = screener;
        this.childName = childName;
        this.childSurname = childSurname;
        this.childCountry = childCountry;
        this.childCountryCode = childCountryCode;
        this.childProvince = childProvince;
        this.childMunicipality = childMunicipality;
        this.childCity = childCity;
        this.childZone = childZone;
        this.childAddress = childAddress;
        this.status = status;
        this.date = date;
    }

    public Contract(@NonNull String id, @NonNull String photo, int latitude, int longitude, @NonNull User screener,
                    User medical, String childName, String childSurname, String childCountry, String childCountryCode,
                    String childProvince, String childMunicipality, String childCity, String childZone, String childAddress,
                    String status, String diagnosis, long date) {
        this.id = id;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.screener = screener;
        this.medical = medical;
        this.childName = childName;
        this.childSurname = childSurname;
        this.childCountry = childCountry;
        this.childCountryCode = childCountryCode;
        this.childProvince = childProvince;
        this.childMunicipality = childMunicipality;
        this.childCity = childCity;
        this.childZone = childZone;
        this.childAddress = childAddress;
        this.status = status;
        this.diagnosis = diagnosis;
        this.date = date;
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

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public User getScreener() {
        return screener;
    }

    public void setScreener(@NonNull User screener) {
        this.screener = screener;
    }

    public User getMedical() {
        return medical;
    }

    public void setMedical(User medical) {
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

    public String getChildCountry() {
        return childCountry;
    }

    public void setChildCountry(String childCountry) {
        this.childCountry = childCountry;
    }

    public String getChildCountryCode() {
        return childCountryCode;
    }

    public void setChildCountryCode(String childCountryCode) {
        this.childCountryCode = childCountryCode;
    }

    public String getChildProvince() {
        return childProvince;
    }

    public void setChildProvince(String childProvince) {
        this.childProvince = childProvince;
    }

    public String getChildMunicipality() {
        return childMunicipality;
    }

    public void setChildMunicipality(String childMunicipality) {
        this.childMunicipality = childMunicipality;
    }

    public String getChildCity() {
        return childCity;
    }

    public void setChildCity(String childCity) {
        this.childCity = childCity;
    }

    public String getChildZone() {
        return childZone;
    }

    public void setChildZone(String childZone) {
        this.childZone = childZone;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public enum Status {
        INIT, DIAGNOSIS, NO_DIAGNOSIS, FINISHED, PAID
    }

}
