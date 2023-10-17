package org.sic4change.nut4health.ui.create_contract;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.machinezoo.sourceafis.FingerprintTemplate;

import org.apache.commons.collections4.Predicate;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.MalnutritionChildTable;
import org.sic4change.nut4health.data.entities.Point;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.data.names.DataPointNames;
import org.sic4change.nut4health.utils.fingerprint.AndroidBmpUtil;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;

public class CreateContractViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final LiveData<User> mUser;
    private final LiveData<List<Point>> mPoints;
    private final LiveData<Contract> mContract;

    public static final double MINIUM_DESNUTRITION_VALUE_MUAC = 12.6;

    private String role;
    private Uri uriPhoto;
    private int percentage = -1;
    private double arm_circumference = 28.0;
    private Nut4HealthSingleShotLocationProvider.GPSCoordinates location =
            new Nut4HealthSingleShotLocationProvider.GPSCoordinates(0,0);
    private String childName;
    private String childSurname;
    private String childBrothers;
    private String sex = "H";
    private String childDNI;

    private String childBirthdate;
    private String code;
    private String childTutor;
    private String childLocation;
    private String childPhoneContact;

    private String phoneCode = "+34";
    private boolean verification = false;
    private String point;
    private String pointFullName;
    private byte[] fingerPrint;
    private boolean imageSelected = false;
    private double height = 0;
    private double weight = 0.0;
    private double imc = 0.0;
    private Date startTimeCreateContract = null;
    private Date finishTimeCreateContract = null;

    private boolean dialerOpened = false;

    public CreateContractViewModel(DataRepository repository) {
        this.mRepository = repository;
        mUser = this.mRepository.getCurrentUser();

        this.mRepository.getPoints();
        mPoints = mRepository.getSortedPoints();

        this.mRepository.getMalnutritionChildValues();

        mContract = null;
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public LiveData<List<Point>> getPoints(String pointDefaultId) {
        try {
            if (pointDefaultId == null || pointDefaultId.isEmpty() || mPoints.getValue() == null) {
                return mPoints;
            } else {
                Point pointDefault = null;
                for (Point point : mPoints.getValue()) {
                    if (point.getPointId().equals(pointDefaultId)) {
                        pointDefault = point;
                        break;
                    }
                }
                List<PointFormatted> points = new ArrayList<PointFormatted>();
                for (Point i : mPoints.getValue()) {
                    int count = 0;
                    for (String j : i.getFullName().split(",")) {
                        if (pointDefault.getFullName().replace(" ", "").contains(j.replace(" ", ""))) {
                            count++;
                        }
                    }
                    points.add(new PointFormatted(i.getPointId(), i.getFullName(), i.getPhoneCode(), count));
                }
                mPoints.getValue().clear();
                Collections.sort(points, Comparator.comparingInt(PointFormatted::getOrder).reversed());
                for (PointFormatted k : points) {
                    mPoints.getValue().add(new Point(k.getPointId(), k.getFullName(), k.getPhoneCode()));
                }
                return mPoints;
            }
        } catch (Exception e) {
            return mPoints;
        }

    }

    public LiveData<Contract> getContract() {
        return mContract;
    }

    public void createContract(String id, String role, String screener, float latitude, float longitude,
                               Uri photo, String childName, String childSurname, String sex, String childBirthdate,
                               String childDNI, int childBrothers, String childTutor, String childAddress,
                               String childPhoneContact, String point, String pointFullName,
                               int percentage, double arm_circumference, double height, double weight) {
        String code = childPhoneContact + "-" + childBrothers;
        String phone = "+" + phoneCode + childPhoneContact;

        DateTime dt1 = new DateTime(getStartTimeCreateContract());
        DateTime dt2 = new DateTime(getFinishTimeCreateContract());

        String minutes = Minutes.minutesBetween(dt1, dt2).getMinutes() % 60 + " m, ";
        String seconds = Seconds.secondsBetween(dt1, dt2).getSeconds() % 60 + " s.";


        mRepository.createContract(id, role, screener, latitude, longitude, photo, childName,
                childSurname, sex, childBirthdate, childDNI, childBrothers, code, childTutor, childAddress, phone, point,
                pointFullName, percentage, arm_circumference, height, weight,"",
                minutes + seconds);

    }

    public void updatePointsUserLocal(String email) {
        mRepository.updatePointsUserLocal(email, mUser.getValue().getPoints() + 1);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Uri getUriPhoto() {
        return uriPhoto;
    }

    public void setUriPhoto(Uri uriPhoto) {
        this.uriPhoto = uriPhoto;
    }

    public Nut4HealthSingleShotLocationProvider.GPSCoordinates getLocation() {
        return location;
    }

    public void setLocation(Nut4HealthSingleShotLocationProvider.GPSCoordinates location) {
        this.location = location;
    }

    public boolean isImageSelected() {
        return imageSelected;
    }

    public void setImageSelected(boolean imageSelected) {
        this.imageSelected = imageSelected;
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

    public String getChildBrothers() {
        return childBrothers;
    }

    public void setChildBrothers(String childBrothers) {
        this.childBrothers = childBrothers;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if (sex.equals("RIGHT")) {
            this.sex = "F";
        } else {
            this.sex = "M";
        }
    }

    public String getChildDNI() {
        return childDNI;
    }

    public void setChildDNI(String childDNI) {
        this.childDNI = childDNI;
    }

    public String getChildBirthdate() {
        return childBirthdate;
    }

    public void setChildBirthdate(String childBirthdate) {
        this.childBirthdate = childBirthdate;
    }

    public String getChildTutor() {
        return childTutor;
    }

    public void setChildTutor(String childTutor) {
        this.childTutor = childTutor;
    }

    public String getChildLocation() {
        return childLocation;
    }

    public void setChildLocation(String childLocation) {
        this.childLocation = childLocation;
    }

    public String getChildPhoneContact() {
        return childPhoneContact;
    }

    public void setChildPhoneContact(String childPhoneContact) {
        this.childPhoneContact = childPhoneContact;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public boolean getVerification() {
        return verification;
    }

    public void setChildVerification(boolean verification) {
        this.verification = verification;
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

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public double getArmCircumference() {
        return arm_circumference;
    }

    public void setArmCircumference(double arm_circumference) {
        this.arm_circumference = arm_circumference;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public byte[] getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(byte[] fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public boolean isDialerOpened() {
        return dialerOpened;
    }

    public void setDialerOpened(boolean dialerOpened) {
        this.dialerOpened = dialerOpened;
    }

    public Date getStartTimeCreateContract() {
        return startTimeCreateContract;
    }

    public void setStartTimeCreateContract(Date startTimeCreateContract) {
        this.startTimeCreateContract = startTimeCreateContract;
    }

    public Date getFinishTimeCreateContract() {
        return finishTimeCreateContract;
    }

    public void setFinishTimeCreateContract(Date finishTimeCreateContract) {
        this.finishTimeCreateContract = finishTimeCreateContract;
    }

    public LiveData<List<MalnutritionChildTable>> getDesnutritionChildTable() {
        if (this.weight != 0 && this.height != 0.0) {
            return mRepository.getMalNutritionChildTable();
        }
        return null;
    }

    public String getStatus() {
        if (imc != 0) {
            if (arm_circumference < 11.5) {
                return "Aguda Severa";
            } else if ((arm_circumference >= 11.5 && arm_circumference <= 12.5)) {
                if ((imc == -3.0)) {
                    return "Aguda Severa";
                } else {
                    return "Aguda Moderada";
                }
            } else {
                if ((imc == -3.0)) {
                    return "Aguda Severa";
                } else if (imc == -2.0) {
                    return "Aguda Moderada";
                } else {
                    return "Normopeso";
                }
            }
        } else {
            if (arm_circumference < 11.5) {
                return "Aguda Severa";
            } else if (arm_circumference >= 11.5 && arm_circumference <= 12.5) {
                return "Aguda Moderada";
            } else {
                return "Normopeso";
            }
        }
    }

    public double checkMalnutritionByWeightAndHeight(List<MalnutritionChildTable> table) {
        if (table != null) {
            for (MalnutritionChildTable value : table) {
                if (value.getCm() >= (height - 0.1)) {
                    try {
                        if (weight < value.getMinusthree()) {
                            imc = -3.0;
                        } else if (weight >= value.getMinusthree() && weight < value.getMinustwo()) {
                            imc = -2;
                        } else {
                            imc = -1.5;
                        }
                    } catch (Exception e) {
                        if (height == 100) {
                            MalnutritionChildTable malNutritionChldTable = new MalnutritionChildTable(
                                    "X3fX5g2Fd9lpy0OVYkgA", 100, 14.2, 13.6,
                                    12.1, 13.1, 15.4
                            );
                            if (weight < value.getMinusthree()) {
                                imc = -3.0;
                            } else if (weight >= value.getMinusthree() && weight < value.getMinustwo()) {
                                imc = -2;
                            } else {
                                imc = -1.5;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return imc;
    }
}



class PointFormatted {

    private String pointId;
    private String fullName;
    private String phoneCode;
    private int order;

    PointFormatted() {
        this("", "", "", 0);
    }

    PointFormatted(@NonNull String pointId, String fullName, String phoneCode, int order) {
        this.pointId = pointId;
        this.fullName = fullName;
        this.phoneCode = phoneCode;
        this.order = order;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String toString() {
        return this.fullName;
    }

}


