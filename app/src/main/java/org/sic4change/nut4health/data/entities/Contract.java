package org.sic4change.nut4health.data.entities;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataContractNames;
import org.sic4change.nut4health.utils.time.Nut4HealthTimeUtil;

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
    private double latitude;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_LONGITUDE)
    private double longitude;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_SCREENER)
    private String screener;

    @ColumnInfo(name = DataContractNames.COL_MEDICAL)
    private String medical;

    @ColumnInfo(name = DataContractNames.COL_CHILD_NAME)
    private String childName;

    @ColumnInfo(name = DataContractNames.COL_CHILD_SURNAME)
    private String childSurname;

    @ColumnInfo(name = DataContractNames.COL_CHILD_SEX)
    private String sex;

    @ColumnInfo(name = DataContractNames.COL_CHILD_DNI)
    private String childDNI;

    @ColumnInfo(name = DataContractNames.COL_CHILD_BROTHERS)
    private int childBrothers;

    @ColumnInfo(name = DataContractNames.COL_CODE)
    private String code;

    @ColumnInfo(name = DataContractNames.COL_CHILD_TUTOR)
    private String childTutor;

    @ColumnInfo(name = DataContractNames.COL_CHILD_ADDRESS)
    private String childAddress;

    @ColumnInfo(name = DataContractNames.COL_CHILD_PHONE_CONTRACT)
    private String childPhoneContract;

    @ColumnInfo(name = DataContractNames.COL_POINT)
    private String point;

    @ColumnInfo(name = DataContractNames.COL_POINT_FULL_NAME)
    private String pointFullName;

    @ColumnInfo(name = DataContractNames.COL_CHILD_FINGERPRINT)
    private String fingerprint;

    @ColumnInfo(name = DataContractNames.COL_STATUS)
    private String status;

    @ColumnInfo(name = DataContractNames.COL_DIAGNOSIS)
    private String diagnosis;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_DATE)
    private String creationDate;

    @ColumnInfo(name = DataContractNames.COL_DATE_MILI)
    private long creationDateMiliseconds;

    @ColumnInfo(name = DataContractNames.COL_MEDICAL_DATE_MILI)
    private long medicalDateMiliseconds;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_PERCENTAGE)
    private int percentage;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_ARM_CIRCUMFERENCE)
    private double arm_circumference;

    @NonNull
    @ColumnInfo(name = DataContractNames.COL_MEDICAL_DATE)
    private String medicalDate;

    public Contract() {
        this("", "", 0.0f, 0.0f, "", "", "", "", "",
                "", 0, "", "", "", "", "", "", "", Status.EMPTY.name(), "", "",0, 0, 0.0, "", 0);
    }

    @Ignore
    public Contract(@NonNull String id) {
        this(id, "", 0.0f, 0.0f, "", "", "",
                "", "", "", 0, "",  "", "", "", "", "", "", Status.EMPTY.name(), "", "",0,  0, 0.0,"", 0);
    }

    @Ignore
    public Contract(@NonNull String photo, double latitude, double longitude, @NonNull String screener) {
        this("", photo, latitude, longitude, screener, "", "", "", "", "", 0, "","",
                "", "", "", "", "", Status.EMPTY.name(), "", "",0,  0, 0.0, "", 0);
    }

    @Ignore
    public Contract(@NonNull String photo, double latitude, double longitude, @NonNull String screener,
                    String childName, String childSurname, String sex, String childDNI, int childBrothers, String code,
                    String childTutor, String childAddress, String childPhoneContract,
                    String point, String pointFullName, String fingerprint, String status, String creationDate,
                    int percentage, double arm_circumference) {
        this("", photo, latitude, longitude, screener, "", childName, childSurname, sex, childDNI,
                childBrothers, code, childTutor, childAddress, childPhoneContract, point, pointFullName,
                fingerprint, status, "", creationDate, Nut4HealthTimeUtil.convertCreationDateToTimeMilis(creationDate),
                percentage, arm_circumference, "", 0);
    }
    @Ignore
    public Contract(@NonNull String id, @NonNull String photo, double latitude, double longitude,
                    @NonNull String screener, String childName, String childSurname, String sex, String childDNI,
                    int childBrothers, String code, String childTutor, String childAddress, String childPhoneContract,
                    String point, String pointFullName, String fingerprint, String status,
                    String creationDate, long creationDateMiliseconds, int percentage,
                    double arm_circumference) {
        this(id, photo, latitude, longitude, screener, "", childName, childSurname, sex, childDNI,
                childBrothers, code, childTutor, childAddress, childPhoneContract, point, pointFullName,
                fingerprint, status, "", creationDate, creationDateMiliseconds, percentage, arm_circumference, "", 0);
    }

    public Contract(@NonNull String id, @NonNull String photo, double latitude, double longitude,
                    @NonNull String screener, String medical, String childName,  String childSurname,
                    String sex, String childDNI, int childBrothers, String code, String childTutor,
                    String childAddress, String childPhoneContract, String point, String pointFullName,
                    String fingerprint, String status, String diagnosis, String creationDate,
                    long creationDateMiliseconds, int percentage, double arm_circumference, String medicalDate,
                    long medicalDateMiliseconds) {
        this.id = id;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.screener = screener;
        this.medical = medical;
        this.childName = childName;
        this.childSurname = childSurname;
        this.sex = sex;
        this.childDNI = childDNI;
        this.childBrothers = childBrothers;
        this.code = code;
        this.childTutor = childTutor;
        this.childAddress = childAddress;
        this.childPhoneContract = childPhoneContract;
        this.point = point;
        this.pointFullName = pointFullName;
        this.fingerprint = fingerprint;
        this.status = status;
        this.diagnosis = diagnosis;
        this.creationDate = creationDate;
        this.creationDateMiliseconds = creationDateMiliseconds;
        this.percentage = percentage;
        this.arm_circumference = arm_circumference;
        this.medicalDate = medicalDate;
        this.medicalDateMiliseconds = medicalDateMiliseconds;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getChildDNI() {
        return childDNI;
    }

    public void setChildDNI(String childDNI) {
        this.childDNI = childDNI;
    }

    public int getChildBrothers() {
        return childBrothers;
    }

    public void setChildBrothers(int childBrothers) {
        this.childBrothers = childBrothers;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChildTutor() {
        return childTutor;
    }

    public void setChildTutor(String childTutor) {
        this.childTutor = childTutor;
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

    public String getChildPhoneContract() {
        return childPhoneContract;
    }

    public void setChildPhoneContract(String childPhoneContract) {
        this.childPhoneContract = childPhoneContract;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPointFullName() {
        return pointFullName;
    }

    public void setPointFullName(String pointFullName) {
        this.pointFullName = pointFullName;
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

    public double getArm_circumference() {
        return arm_circumference;
    }

    public void setArm_circumference(double arm_circumference) {
        this.arm_circumference = arm_circumference;
    }

    @NonNull
    public String getMedicalDate() {
        return medicalDate;
    }

    public void setMedicalDate(@NonNull String medicalDate) {
        this.medicalDate = medicalDate;
    }

    public long getMedicalDateMiliseconds() {
        return medicalDateMiliseconds;
    }

    public void setMedicalDateMiliseconds(long medicalDateMiliseconds) {
        this.medicalDateMiliseconds = medicalDateMiliseconds;
    }

    public enum Status {
        EMPTY, DIAGNOSIS, NO_DIAGNOSIS, PAID, FINISH, DUPLICATED
    }


}
